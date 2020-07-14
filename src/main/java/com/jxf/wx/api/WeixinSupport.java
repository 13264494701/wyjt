package com.jxf.wx.api;

import com.jxf.svc.utils.StringUtils;
import com.jxf.wx.api.handle.EventHandle;
import com.jxf.wx.api.handle.MessageHandle;
import com.jxf.wx.api.message.BaseMsg;
import com.jxf.wx.api.message.TextMsg;
import com.jxf.wx.api.message.aes.AesException;
import com.jxf.wx.api.message.aes.WXBizMsgCrypt;
import com.jxf.wx.api.message.req.BaseEvent;
import com.jxf.wx.api.message.req.BaseReq;
import com.jxf.wx.api.message.req.BaseReqMsg;
import com.jxf.wx.api.message.req.EventType;
import com.jxf.wx.api.message.req.ImageReqMsg;
import com.jxf.wx.api.message.req.LinkReqMsg;
import com.jxf.wx.api.message.req.LocationEvent;
import com.jxf.wx.api.message.req.LocationReqMsg;
import com.jxf.wx.api.message.req.MenuEvent;
import com.jxf.wx.api.message.req.QrCodeEvent;
import com.jxf.wx.api.message.req.ReqType;
import com.jxf.wx.api.message.req.ScanCodeEvent;
import com.jxf.wx.api.message.req.SendPicsInfoEvent;
import com.jxf.wx.api.message.req.TemplateMsgEvent;
import com.jxf.wx.api.message.req.TextReqMsg;
import com.jxf.wx.api.message.req.VideoReqMsg;
import com.jxf.wx.api.message.req.VoiceReqMsg;
import com.jxf.wx.api.utils.CollectionUtil;
import com.jxf.wx.api.utils.MessageUtil;
import com.jxf.wx.api.utils.SignUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

/**
 * 
 * @类功能说明： 将微信处理通用部分再抽象一层，使用其他框架框架的同学可以自行继承此类集成微信
 * @类修改者： 
 * @修改日期： 
 * @修改说明： 
 * @公司名称：北京金信服科技有限公司
 * @作者：Administrator 
 * @创建时间：2016年11月11日 下午8:04:05 
 * @版本：V1.0
 */
@SuppressWarnings("rawtypes")
public abstract class WeixinSupport {

    private static final Logger LOG  = LoggerFactory.getLogger(WeixinSupport.class);
    //充当锁
    private static final Object LOCK = new Object();

    protected String fromUserName, toUserName;

    /**
     * 微信消息处理器列表
     */
    private static List<MessageHandle> messageHandles;
    /**
     * 微信事件处理器列表
     */
    
	private static List<EventHandle>   eventHandles;

    /**
     * 子类重写，加入自定义的微信消息处理器，细化消息的处理
     *
     * @return 微信消息处理器列表
     */
    protected List<MessageHandle> initMessageHandles() {
        return null;
    }

    /**
     * 子类重写，加入自定义的微信事件处理器，细化消息的处理
     *
     * @return 微信事件处理器列表
     */
    protected List<EventHandle> initEventHandles() {
        return null;
    }

    /**
     * 子类提供token用于绑定微信公众平台
     *
     * @return token值
     */
    protected abstract String getToken();

    /**
     * 公众号APPID，使用消息加密模式时用户自行设置
     *
     * @return 微信公众平台提供的appid
     */
    protected String getAppId() {
        return null;
    }

    /**
     * 加密的密钥，使用消息加密模式时用户自行设置
     *
     * @return 用户自定义的密钥
     */
    protected String getAESKey() {
        return null;
    }

    /**
     * 绑定服务器的方法
     * @param request 请求
     * @param response 响应
     */
    public void bindServer(HttpServletRequest request, HttpServletResponse response) {
        if (isLegal(request)) {
            try {
                PrintWriter pw = response.getWriter();
                pw.write(request.getParameter("echostr"));
                pw.flush();
                pw.close();
            } catch (Exception e) {
                LOG.error("绑定服务器异常", e);
            }
        }
    }

    /**
     * 处理微信服务器发来的请求方法
     *
     * @param request http请求对象
     * @param acctId 公众号Id
     * @return 处理消息的结果，已经是接口要求的xml报文了
     */
    @SuppressWarnings("unchecked")
	public String processRequest(HttpServletRequest request) {
        // 解析请求报文
    	Map<String, Object> reqMap = MessageUtil.parseXml(request, getToken(), getAppId(), getAESKey());
        fromUserName = (String) reqMap.get("FromUserName");
        toUserName = (String) reqMap.get("ToUserName");
        String msgType = (String) reqMap.get("MsgType");
        LOG.info("接收公众号:{}",toUserName);
        LOG.debug("收到消息,消息类型:{}", msgType);
        LOG.info("消息内容{}",reqMap.toString());
        
        BaseMsg msg = null;
        //如果是事件消息
        if (msgType.equals(ReqType.EVENT)) {
            String eventType = (String) reqMap.get("Event");
            String ticket = (String) reqMap.get("Ticket");
            QrCodeEvent qrCodeEvent = null;
            if (StringUtils.isNotBlank(ticket)) {
                String eventKey = (String) reqMap.get("EventKey");
                LOG.debug("eventKey:{}", eventKey);
                LOG.debug("ticket:{}", ticket);
                qrCodeEvent = new QrCodeEvent(eventKey, ticket);
                buildBasicEvent(reqMap, qrCodeEvent);
                if (eventType.equals(EventType.SCAN)) {
                    msg = handleQrCodeEvent(qrCodeEvent);
                    if (msg==null) {
                        msg = processEventHandle(qrCodeEvent);
                    }
                }
            }
            if (eventType.equals(EventType.SUBSCRIBE)) {
            	BaseEvent event = new BaseEvent();
            	if (qrCodeEvent != null) {
            		event = qrCodeEvent;
                    msg = handleQrCodeSubscribe(qrCodeEvent);
                } else {
                    buildBasicEvent(reqMap, event);
                    msg = handleSubscribe(event);
                }
                if (msg==null) {
                    msg = processEventHandle(event);
                }
            } else if (eventType.equals(EventType.UNSUBSCRIBE)) {
                BaseEvent event = new BaseEvent();
                buildBasicEvent(reqMap, event);
                msg = handleUnsubscribe(event);
                if (msg==null) {
                    msg = processEventHandle(event);
                }
            } else if (eventType.equals(EventType.CLICK)) {
                String eventKey = (String) reqMap.get("EventKey");
                LOG.debug("eventKey:{}", eventKey);
                MenuEvent event = new MenuEvent(eventKey);
                buildBasicEvent(reqMap, event);
                msg = handleMenuClickEvent(event);
                if (msg==null) {
                    msg = processEventHandle(event);
                }
            } else if (eventType.equals(EventType.VIEW)) {
                String eventKey = (String) reqMap.get("EventKey");
                LOG.debug("eventKey:{}", eventKey);
                MenuEvent event = new MenuEvent(eventKey);
                buildBasicEvent(reqMap, event);
                msg = handleMenuViewEvent(event);
                if (msg==null) {
                    msg = processEventHandle(event);
                }
            } else if (eventType.equals(EventType.LOCATION)) {
                double latitude = Double.parseDouble((String) reqMap.get("Latitude"));
                double longitude = Double.parseDouble((String) reqMap.get("Longitude"));
                double precision = Double.parseDouble((String) reqMap.get("Precision"));
                LocationEvent event = new LocationEvent(latitude, longitude,
                        precision);
                buildBasicEvent(reqMap, event);
                msg = handleLocationEvent(event);
                if (msg==null) {
                    msg = processEventHandle(event);
                }
            } else if (EventType.SCANCODEPUSH.equals(eventType) || EventType.SCANCODEWAITMSG.equals(eventType)) {
                String eventKey = (String) reqMap.get("EventKey");
                Map<String, Object> scanCodeInfo = (Map<String, Object>)reqMap.get("ScanCodeInfo");
                String scanType = (String) scanCodeInfo.get("ScanType");
                String scanResult = (String) scanCodeInfo.get("ScanResult");
                ScanCodeEvent event = new ScanCodeEvent(eventKey, scanType, scanResult);
                buildBasicEvent(reqMap, event);
                msg = handleScanCodeEvent(event);
                if (msg==null) {
                    msg = processEventHandle(event);
                }
            } else if (EventType.PICPHOTOORALBUM.equals(eventType) || EventType.PICSYSPHOTO.equals(eventType) || EventType.PICWEIXIN.equals(eventType)) {
                String eventKey = (String) reqMap.get("EventKey");
                Map<String, Object> sendPicsInfo = (Map<String, Object>)reqMap.get("SendPicsInfo");
                int count = Integer.parseInt((String) sendPicsInfo.get("Count"));
                List<Map> picList = (List) sendPicsInfo.get("PicList");
                SendPicsInfoEvent event = new SendPicsInfoEvent(eventKey, count, picList);
                buildBasicEvent(reqMap, event);
                msg = handlePSendPicsInfoEvent(event);
                if (msg==null) {
                    msg = processEventHandle(event);
                }
            } else if (EventType.TEMPLATESENDJOBFINISH.equals(eventType)) {
                String msgId = (String) reqMap.get("MsgID");
                String status = (String) reqMap.get("Status");
                TemplateMsgEvent event = new TemplateMsgEvent(msgId,status);
                buildBasicEvent(reqMap, event);
                msg = handleTemplateMsgEvent(event);
                if (msg==null) {
                    msg = processEventHandle(event);
                }
            }
        } else {
            if (msgType.equals(ReqType.TEXT)) {
                String content = (String) reqMap.get("Content");
                LOG.debug("文本消息内容:{}", content);
                TextReqMsg textReqMsg = new TextReqMsg(content);
                buildBasicReqMsg(reqMap, textReqMsg);
                msg = handleTextMsg(textReqMsg);
                if (msg==null) {
                    msg = processMessageHandle(textReqMsg);
                }
            } else if (msgType.equals(ReqType.IMAGE)) {
                String picUrl = (String) reqMap.get("PicUrl");
                String mediaId = (String) reqMap.get("MediaId");
                ImageReqMsg imageReqMsg = new ImageReqMsg(picUrl, mediaId);
                buildBasicReqMsg(reqMap, imageReqMsg);
                msg = handleImageMsg(imageReqMsg);
                if (msg==null) {
                    msg = processMessageHandle(imageReqMsg);
                }
            } else if (msgType.equals(ReqType.VOICE)) {
                String format = (String) reqMap.get("Format");
                String mediaId = (String) reqMap.get("MediaId");
                String recognition = (String) reqMap.get("Recognition");
                VoiceReqMsg voiceReqMsg = new VoiceReqMsg(mediaId, format,
                        recognition);
                buildBasicReqMsg(reqMap, voiceReqMsg);
                msg = handleVoiceMsg(voiceReqMsg);
                if (msg==null) {
                    msg = processMessageHandle(voiceReqMsg);
                }
            } else if (msgType.equals(ReqType.VIDEO)) {
                String thumbMediaId = (String) reqMap.get("ThumbMediaId");
                String mediaId = (String) reqMap.get("MediaId");
                VideoReqMsg videoReqMsg = new VideoReqMsg(mediaId, thumbMediaId);
                buildBasicReqMsg(reqMap, videoReqMsg);
                msg = handleVideoMsg(videoReqMsg);
                if (msg==null) {
                    msg = processMessageHandle(videoReqMsg);
                }
            } else if (msgType.equals(ReqType.SHORT_VIDEO)) {
                String thumbMediaId = (String) reqMap.get("ThumbMediaId");
                String mediaId = (String) reqMap.get("MediaId");
                VideoReqMsg videoReqMsg = new VideoReqMsg(mediaId, thumbMediaId);
                buildBasicReqMsg(reqMap, videoReqMsg);
                msg = hadnleShortVideoMsg(videoReqMsg);
                if (msg==null) {
                    msg = processMessageHandle(videoReqMsg);
                }
            } else if (msgType.equals(ReqType.LOCATION)) {
                double locationX = Double.parseDouble((String) reqMap.get("Location_X"));
                double locationY = Double.parseDouble((String) reqMap.get("Location_Y"));
                int scale = Integer.parseInt((String) reqMap.get("Scale"));
                String label = (String) reqMap.get("Label");
                LocationReqMsg locationReqMsg = new LocationReqMsg(locationX,
                        locationY, scale, label);
                buildBasicReqMsg(reqMap, locationReqMsg);
                msg = handleLocationMsg(locationReqMsg);
                if (msg==null) {
                    msg = processMessageHandle(locationReqMsg);
                }
            } else if (msgType.equals(ReqType.LINK)) {
                String title = (String) reqMap.get("Title");
                String description = (String) reqMap.get("Description");
                String url = (String) reqMap.get("Url");
                LOG.debug("链接消息地址:{}", url);
                LinkReqMsg linkReqMsg = new LinkReqMsg(title, description, url);
                buildBasicReqMsg(reqMap, linkReqMsg);
                msg = handleLinkMsg(linkReqMsg);
                if (msg==null) {
                    msg = processMessageHandle(linkReqMsg);
                }
            }
        }
        String result = "";
        if (msg!=null) {
            msg.setFromUserName(toUserName);
            msg.setToUserName(fromUserName);
            result = msg.toXml();
            if (StringUtils.isNotBlank(getAESKey())) {
                try {
                    WXBizMsgCrypt pc = new WXBizMsgCrypt(getToken(), getAESKey(), getAppId());
                    result = pc.encryptMsg(result, request.getParameter("timestamp"), request.getParameter("nonce"));
                    LOG.debug("加密后密文:{}", result);
                } catch (AesException e) {
                    LOG.error("加密异常", e);
                }
            }
        }
        return result;
    }

	@SuppressWarnings("unchecked")
	private BaseMsg processMessageHandle(BaseReqMsg msg) {
        if (CollectionUtil.isEmpty(messageHandles)) {
            synchronized (LOCK) {
                messageHandles = this.initMessageHandles();
            }
        }
        if (CollectionUtil.isNotEmpty(messageHandles)) {
            for (MessageHandle messageHandle : messageHandles) {
                BaseMsg resultMsg = null;
                boolean result;
                try {
                    result = messageHandle.beforeHandle(msg);
                } catch (Exception e) {
                    result = false;
                }
                if (result) {
                    resultMsg = messageHandle.handle(msg);
                }
                if (resultMsg!=null) {
                    return resultMsg;
                }
            }
        }
        return null;
    }

    @SuppressWarnings("unchecked")
	private BaseMsg processEventHandle(BaseEvent event) {
        if (CollectionUtil.isEmpty(eventHandles)) {
            synchronized (LOCK) {
                eventHandles = this.initEventHandles();
            }
        }
        if (CollectionUtil.isNotEmpty(eventHandles)) {
            for (EventHandle eventHandle : eventHandles) {
                BaseMsg resultMsg = null;
                boolean result;
                try {
                    result = eventHandle.beforeHandle(event);
                } catch (Exception e) {
                    result = false;
                }
                if (result) {
                    resultMsg = eventHandle.handle(event);
                }
                if (resultMsg!=null) {
                    return resultMsg;
                }
            }
        }
        return null;
    }

    /**
     * 处理文本消息，有需要时子类重写
     *
     * @param msg 请求消息对象
     * @return 响应消息对象
     */
    protected BaseMsg handleTextMsg(TextReqMsg msg) {
        return handleDefaultMsg(msg);
    }

    /**
     * 处理图片消息，有需要时子类重写
     *
     * @param msg 请求消息对象
     * @return 响应消息对象
     */
    protected BaseMsg handleImageMsg(ImageReqMsg msg) {
        return handleDefaultMsg(msg);
    }

    /**
     * 处理语音消息，有需要时子类重写
     *
     * @param msg 请求消息对象
     * @return 响应消息对象
     */
    protected BaseMsg handleVoiceMsg(VoiceReqMsg msg) {
        return handleDefaultMsg(msg);
    }

    /**
     * 处理视频消息，有需要时子类重写
     *
     * @param msg 请求消息对象
     * @return 响应消息对象
     */
    protected BaseMsg handleVideoMsg(VideoReqMsg msg) {
        return handleDefaultMsg(msg);
    }

    /**
     * 处理小视频消息，有需要时子类重写
     *
     * @param msg 请求消息对象
     * @return 响应消息对象
     */
    protected BaseMsg hadnleShortVideoMsg(VideoReqMsg msg) {
        return handleDefaultMsg(msg);
    }

    /**
     * 处理地理位置消息，有需要时子类重写
     *
     * @param msg 请求消息对象
     * @return 响应消息对象
     */
    protected BaseMsg handleLocationMsg(LocationReqMsg msg) {
        return handleDefaultMsg(msg);
    }

    /**
     * 处理链接消息，有需要时子类重写
     *
     * @param msg 请求消息对象
     * @return 响应消息对象
     */
    protected BaseMsg handleLinkMsg(LinkReqMsg msg) {
        return handleDefaultMsg(msg);
    }

    /**
     * 处理扫描二维码事件，有需要时子类重写
     *
     * @param event 扫描二维码事件对象
     * @return 响应消息对象
     */
    protected BaseMsg handleQrCodeEvent(QrCodeEvent event) {
        return handleDefaultEvent(event);
    }

    /**
     * 处理地理位置事件，有需要时子类重写
     *
     * @param event 地理位置事件对象
     * @return 响应消息对象
     */
    protected BaseMsg handleLocationEvent(LocationEvent event) {
        return handleDefaultEvent(event);
    }

    /**
     * 处理菜单点击事件，有需要时子类重写
     *
     * @param event 菜单点击事件对象
     * @return 响应消息对象
     */
    protected BaseMsg handleMenuClickEvent(MenuEvent event) {
        return handleDefaultEvent(event);
    }

    /**
     * 处理菜单跳转事件，有需要时子类重写
     *
     * @param event 菜单跳转事件对象
     * @return 响应消息对象
     */
    protected BaseMsg handleMenuViewEvent(MenuEvent event) {
        return handleDefaultEvent(event);
    }

    /**
     * 处理菜单扫描推事件，有需要时子类重写
     *
     * @param event 菜单扫描推事件对象
     * @return 响应的消息对象
     */
    protected BaseMsg handleScanCodeEvent(ScanCodeEvent event) {
        return handleDefaultEvent(event);
    }

    /**
     * 处理菜单弹出相册事件，有需要时子类重写
     *
     * @param event 菜单弹出相册事件
     * @return 响应的消息对象
     */
    protected BaseMsg handlePSendPicsInfoEvent(SendPicsInfoEvent event) {
        return handleDefaultEvent(event);
    }

    /**
     * 处理模版消息发送事件，有需要时子类重写
     *
     * @param event 菜单弹出相册事件
     * @return 响应的消息对象
     */
    protected BaseMsg handleTemplateMsgEvent(TemplateMsgEvent event) {
        return handleDefaultEvent(event);
    }

    /**
     * 处理添加关注事件，有需要时子类重写
     *
     * @param event 添加关注事件对象
     * @return 响应消息对象
     */
    protected BaseMsg handleSubscribe(BaseEvent event) {
        return new TextMsg("感谢您的关注!");
    }
    /**
     * 
     * @param event
     * @return
     */
    protected BaseMsg handleQrCodeSubscribe(QrCodeEvent event) {
		return new TextMsg("感谢您的关注!");
	}

    /**
     * 处理取消关注事件，有需要时子类重写
     *
     * @param event 取消关注事件对象
     * @return 响应消息对象
     */
    protected BaseMsg handleUnsubscribe(BaseEvent event) {
        return null;
    }

    protected BaseMsg handleDefaultMsg(BaseReqMsg msg) {
        return null;
    }

    protected BaseMsg handleDefaultEvent(BaseEvent event) {
        return null;
    }

    private void buildBasicReqMsg(Map<String, Object> reqMap, BaseReqMsg reqMsg) {
        addBasicReqParams(reqMap, reqMsg);
        reqMsg.setMsgId((String) reqMap.get("MsgId"));
    }

    private void buildBasicEvent(Map<String, Object> reqMap, BaseEvent event) {
        addBasicReqParams(reqMap, event);
        event.setEvent((String) reqMap.get("Event"));
    }

    private void addBasicReqParams(Map<String, Object> reqMap, BaseReq req) {
        req.setMsgType((String) reqMap.get("MsgType"));
        req.setFromUserName((String) reqMap.get("FromUserName"));
        req.setToUserName((String) reqMap.get("ToUserName"));
        req.setCreateTime(Long.parseLong((String) reqMap.get("CreateTime")));
    }

    protected boolean isLegal(HttpServletRequest request) {
        String signature = request.getParameter("signature");
        String timestamp = request.getParameter("timestamp");
        String nonce = request.getParameter("nonce");
        return SignUtil.checkSignature(getToken(), signature, timestamp, nonce);
    }
}