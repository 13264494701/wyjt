package com.jxf.wx.api;
import com.alibaba.fastjson.JSON;
import com.jxf.svc.utils.StringUtils;
import com.jxf.wx.api.entity.CustomAccount;
import com.jxf.wx.api.entity.OnlineCustom;
import com.jxf.wx.api.exception.WeixinException;
import com.jxf.wx.api.message.ArticleMsg;
import com.jxf.wx.api.message.BaseMsg;
import com.jxf.wx.api.message.ImageMsg;
import com.jxf.wx.api.message.MusicMsg;
import com.jxf.wx.api.message.NewsMsg;
import com.jxf.wx.api.message.TextMsg;
import com.jxf.wx.api.message.VideoMsg;
import com.jxf.wx.api.message.VoiceMsg;
import com.jxf.wx.api.response.BaseResponse;
import com.jxf.wx.api.response.BaseResponse.ResultType;
import com.jxf.wx.api.response.GetCustomAccountsResponse;
import com.jxf.wx.api.response.GetOnlineCustomResponse;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/***
 * 
 * @类功能说明： 客服相关API
 * @类修改者： 
 * @修改日期： 
 * @修改说明： 
 * @公司名称：北京金信服科技有限公司
 * @作者：Administrator 
 * @创建时间：2016年11月11日 下午5:52:14 
 * @版本：V1.0
 */
public class CustomAPI extends BaseAPI {


	public CustomAPI(String accessToken) {
		super(accessToken);

	}

	private static final Logger LOG = LoggerFactory.getLogger(CustomAPI.class);

//    public CustomAPI(ApiConfig config) {
//        super(config);
//    }
    

    /**
     * 发布客服消息
     *
     * @param openid  关注者ID
     * @param message 消息对象，支持各种消息类型
     * @return 调用结果
     */
    public ResultType sendCustomMessage(String openid, BaseMsg message) {
        Assert.notNull(openid,"");
        Assert.notNull(message,"");
        LOG.debug("发布客服消息......");
        String url = BASE_API_URL + "cgi-bin/message/custom/send?access_token=#";
        final Map<String, Object> params = new HashMap<String, Object>();
        params.put("touser", openid);
        if (message instanceof TextMsg) {
            TextMsg msg = (TextMsg) message;
            params.put("msgtype", "text");
            Map<String, String> text = new HashMap<String, String>();
            text.put("content", msg.getContent());
            params.put("text", text);
        } else if (message instanceof ImageMsg) {
            ImageMsg msg = (ImageMsg) message;
            params.put("msgtype", "image");
            Map<String, String> image = new HashMap<String, String>();
            image.put("media_id", msg.getMediaId());
            params.put("image", image);
        } else if (message instanceof VoiceMsg) {
            VoiceMsg msg = (VoiceMsg) message;
            params.put("msgtype", "voice");
            Map<String, String> voice = new HashMap<String, String>();
            voice.put("media_id", msg.getMediaId());
            params.put("voice", voice);
        } else if (message instanceof VideoMsg) {
            VideoMsg msg = (VideoMsg) message;
            params.put("msgtype", "video");
            Map<String, String> video = new HashMap<String, String>();
            video.put("media_id", msg.getMediaId());
            video.put("thumb_media_id", msg.getMediaId());
            video.put("title", msg.getTitle());
            video.put("description", msg.getDescription());
            params.put("video", video);
        } else if (message instanceof MusicMsg) {
            MusicMsg msg = (MusicMsg) message;
            params.put("msgtype", "music");
            Map<String, String> music = new HashMap<String, String>();
            music.put("thumb_media_id", msg.getThumbMediaId());
            music.put("title", msg.getTitle());
            music.put("description", msg.getDescription());
            music.put("musicurl", msg.getMusicUrl());
            music.put("hqmusicurl", msg.getHqMusicUrl());
            params.put("music", music);
        } else if (message instanceof NewsMsg) {
            NewsMsg msg = (NewsMsg) message;
            params.put("msgtype", "news");
            Map<String, Object> news = new HashMap<String, Object>();
            List<Object> articles = new ArrayList<Object>();
            List<ArticleMsg> list = msg.getArticles();
            for (ArticleMsg article : list) {
                Map<String, String> map = new HashMap<String, String>();
                map.put("title", article.getTitle());
                map.put("description", article.getDescription());
                map.put("url", article.getUrl());
                map.put("picurl", article.getPicUrl());
                articles.add(map);
            }
            news.put("articles", articles);
            params.put("news", news);
        }
        BaseResponse response = executePost(url, JSON.toJSONString(params));
        return ResultType.get(response.getErrcode());
    }

    /**
     * 添加客服帐号
     *
     * @param customAccount 客服对象
     * @return 添加结果
     */
    public ResultType addCustomAccount(CustomAccount customAccount) {
        LOG.debug("添加客服帐号.....");
        Assert.notNull(customAccount,"");
        Assert.notNull(customAccount.getAccountName(),"");
        Assert.notNull(customAccount.getNickName(),"");
        String url = BASE_API_URL + "customservice/kfaccount/add?access_token=#";
        Map<String, String> params = new HashMap<String, String>();
        params.put("kf_account", customAccount.getAccountName());
        params.put("nickname", customAccount.getNickName());
        if (StringUtils.isNotBlank(customAccount.getPassword())) {
            params.put("password", customAccount.getPassword());
        }
        BaseResponse response = executePost(url, JSON.toJSONString(params));
        return ResultType.get(response.getErrcode());
    }

    /**
     * 修改客服帐号信息
     *
     * @param customAccount 客服帐号信息
     * @return 修改结果
     */
    public ResultType updateCustomAccount(CustomAccount customAccount) {
        LOG.debug("修改客服帐号信息......");
        Assert.notNull(customAccount,"");
        Assert.notNull(customAccount.getAccountName(),"");
        Assert.notNull(customAccount.getNickName(),"");
        String url = BASE_API_URL + "customservice/kfaccount/update?access_token=#";
        Map<String, String> params = new HashMap<String, String>();
        params.put("kf_account", customAccount.getAccountName());
        params.put("nickname", customAccount.getNickName());
        if (StringUtils.isNotBlank(customAccount.getPassword())) {
            params.put("password", customAccount.getPassword());
        }
        BaseResponse response = executePost(url, JSON.toJSONString(params));
        return ResultType.get(response.getErrcode());
    }

    /**
     * 删除客服帐号
     * @param customAccount 客服帐号信息
     * @return 删除结果
     */
    public ResultType deleteCustomAccount(CustomAccount customAccount) {
        LOG.debug("删除客服帐号信息......");
        Assert.notNull(customAccount,"");
        Assert.notNull(customAccount.getAccountName(),"");
        Assert.notNull(customAccount.getNickName(),"");
        String url = BASE_API_URL + "customservice/kfaccount/del?access_token=#";
        Map<String, String> params = new HashMap<String, String>();
        params.put("kf_account", customAccount.getAccountName());
        params.put("nickname", customAccount.getNickName());
        if(StringUtils.isNotBlank(customAccount.getPassword())) {
            params.put("password", customAccount.getPassword());
        }
        BaseResponse response = executePost(url, JSON.toJSONString(params));
        return ResultType.get(response.getErrcode());
    }

    /**
     * 设置客服帐号头像
     *
     * @param accountName 客服帐号名
     * @param file        头像文件
     * @return 设置结果
     */
    public ResultType uploadHeadImg(String accountName, File file) {
        LOG.debug("设置客服帐号头像.....");
        Assert.notNull(accountName,"");
        Assert.notNull(file,"");
        String fileName = file.getName().toLowerCase();
        if (!fileName.endsWith("jpg")) {
            throw new WeixinException("头像必须是jpg格式");
        }
        String url = BASE_API_URL + "customservice/kfaccount/uploadheadimg?access_token=#&kf_account=" + accountName;
        BaseResponse response = executePost(url, null, file);
        return ResultType.get(response.getErrcode());
    }

    /**
     * 获取所有客服帐号信息
     * @return 所有客服帐号信息对象
     */
    public GetCustomAccountsResponse getCustomAccountList() {
        LOG.debug("获取所有客服帐号信息....");
        GetCustomAccountsResponse response;
        String url = BASE_API_URL + "cgi-bin/customservice/getkflist?access_token=#";
        BaseResponse r = executeGet(url);
        String resultJson = isSuccess(r.getErrcode()) ? r.getErrmsg() : JSON.toJSONString(r);
        response = JSON.parseObject(resultJson, GetCustomAccountsResponse.class);
        return response;
    }
    /**
     * 获取所有在线客服
     */
    public GetOnlineCustomResponse getOnlineCustomList(){
        LOG.debug("获取所有在线客服信息....");
        GetOnlineCustomResponse response;
        String url = BASE_API_URL + "cgi-bin/customservice/getonlinekflist?access_token=#";
        BaseResponse r = executeGet(url);
        String resultJson = isSuccess(r.getErrcode()) ? r.getErrmsg() : JSON.toJSONString(r);
        response = JSON.parseObject(resultJson, GetOnlineCustomResponse.class);
        return response;
    }
    /**
     * 判断客服是否在线
     * @param kfAccount 客服账号
     * @return
     */
    public boolean isCustomOnline(String kfAccount){
    	GetOnlineCustomResponse response = getOnlineCustomList();
    	List<OnlineCustom> onlineCustoms = response.getOnlineCustomList();
        if(onlineCustoms!=null&&!onlineCustoms.isEmpty()){
            for(Iterator<OnlineCustom> it = onlineCustoms.iterator();it.hasNext();){
            	OnlineCustom onlineCustom = (OnlineCustom)it.next();
                //不在线、没有开启自动接入或者自动接入已满,都返回不可用
                if (onlineCustom != null && onlineCustom.getAccountName().equals(kfAccount)
                        && onlineCustom.getAutoAccept() > 0
                        && onlineCustom.getAutoAccept()>onlineCustom.getAcceptedCase()){
                    return true;
                }
            }
        }
    	return false;
    }
    
    /**
     * 获取转发多客服的响应消息
     * @param touser
     * @param fromuser
     * @return
     */
    public String getMultiCustServcieMessage(String toUserName, String fromUserName) {
        StringBuilder custServiceMessage = new StringBuilder();  
        custServiceMessage.append("<xml>");  
        custServiceMessage.append("<ToUserName><![CDATA["+toUserName+"]]></ToUserName>");  
        custServiceMessage.append("<FromUserName><![CDATA["+fromUserName+"]]></FromUserName>");  
        custServiceMessage.append("<CreateTime>"+new Date().getTime()+"</CreateTime>");  
        custServiceMessage.append("<MsgType><![CDATA["+"transfer_customer_service"+"]]></MsgType>"); 
        custServiceMessage.append("</xml>");  
        return custServiceMessage.toString(); 
    }
    
    /**
     * 获取指定客服的响应消息
     * @param accessToken
     * @param toUserName
     * @param fromUserName
     * @param kfAccount
     * @return
     */
    public String getSpecCustServcie(String toUserName,String fromUserName,String kfAccount) {
        if(isCustomOnline(kfAccount)){
            StringBuilder custServiceMessage = new StringBuilder();  
            custServiceMessage.append("<xml>");  
            custServiceMessage.append("<ToUserName><![CDATA["+toUserName+"]]></ToUserName>");  
            custServiceMessage.append("<FromUserName><![CDATA["+fromUserName+"]]></FromUserName>");  
            custServiceMessage.append("<CreateTime>"+new Date().getTime()+"</CreateTime>");  
            custServiceMessage.append("<MsgType><![CDATA["+"transfer_customer_service"+"]]></MsgType>"); 
            custServiceMessage.append("<TransInfo>");  
            custServiceMessage.append("<KfAccount><![CDATA["+kfAccount+"]]></KfAccount>");
            custServiceMessage.append("</TransInfo>");  
            custServiceMessage.append("</xml>");  
            return custServiceMessage.toString();             
        }else{
            return null;
        }
    }
    
}
