package com.jxf.mms.msg.utils;

import java.io.IOException;
import java.text.ParseException;

import org.ebaoquan.rop.thirdparty.org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jxf.mms.record.entity.MmsAppMsgRecord;
import com.jxf.svc.cache.RedisUtils;
import com.jxf.svc.utils.Exceptions;
import com.xiaomi.xmpush.server.Constants;
import com.xiaomi.xmpush.server.Message;
import com.xiaomi.xmpush.server.Result;
import com.xiaomi.xmpush.server.Sender;


/**
 * 消息推送工具类
 * @author zhuhuijie
 *
 */
public class XiaomiPushUtils {
	
	
	private static String PACKAGE_NAME_ANDROID = "com.yxbao.faith";
	private static String APP_SECRET_KEY_ANDROID = "4+o7FC3+RV5ATAmhMnc4/w==";
	
	private static String PACKAGE_NAME_IOS = "com.feili.quanCai";
    private static String APP_SECRET_KEY_IOS = "t/9rzJ4ctoEwismIfwEfng==";
    
    
    private static String PACKAGE_NAME_IOS_BANK = "com.yxbbank.51jt";
    private static String APP_SECRET_KEY_IOS_BANK = "ye0kHODwpji22gHdO/7Zug==";
   
    private static final Logger logger = LoggerFactory.getLogger(XiaomiPushUtils.class);
    
 
    /**
     * IOS推送
     *
     * @param title          消息标题
     * @param description    消息描述
     * @param mode       消息类型
     * @param para      消息链接
     * @param msgId     消息链接
     * @param pushToken     推送码
     * @throws IOException
     * @throws ParseException
     */
    public static void sendIosMessage_Bank(String title,String description,String mode,String para,String msgId,String pushToken) throws Exception {
    	Constants.useOfficial();//正式
      //Constants.useSandbox();//测试
    	Sender sender = new Sender(APP_SECRET_KEY_IOS_BANK);
    	Message message = new Message.Builder().title(title)
    			.description(title).payload(title)
    			.restrictedPackageName(PACKAGE_NAME_IOS_BANK).notifyType(1) // 使用默认提示音提示
    			.extra("mode", mode)
    			.extra("para", para)
    			.extra("msgId", msgId)
    			.build();
    	Result sendToAlias = sender.sendToAlias(message,pushToken, 0);// 根据regID，发送消息到指定设备上，不重试。
    	logger.debug("========IOS 消息推送返回:==========" + sendToAlias);
    }
 
    /**
     * IOS推送
     *
     * @param messagePayload 消息
     * @param title          消息标题
     * @param description    消息描述
     * @param mode       消息
     * @param para      消息
     * @param msgId      消息
     * @param pushToken      推送码
     * @throws IOException
     * @throws org.json.simple.parser.ParseException 
     */
    public static void sendIosMessage(String title,String description,String mode,String para,String msgId,String pushToken) throws IOException, ParseException, org.json.simple.parser.ParseException {
 
        Constants.useOfficial();
      //Constants.useSandbox();//测试
    	Sender sender = new Sender(APP_SECRET_KEY_IOS);
    	Message message = new Message.Builder().title(title)
    			.description(title).payload(title)
    			.restrictedPackageName(PACKAGE_NAME_IOS).notifyType(1) // 使用默认提示音提示
    			.extra("mode", mode)
    			.extra("para", para)
    			.extra("msgId", msgId)
    			.build();
    	Result sendToAlias = sender.sendToAlias(message,pushToken, 0);// 根据pushToken，发送消息到指定设备上，不重试。
    	logger.debug("========IOS 消息推送返回:==========" + sendToAlias);
    }
 
    /**
     * //安卓推送
     *
     * @param title          消息标题
     * @param description    消息描述
     * @param messagePayload 消息
     * @param pushToken      推送码
     * @throws IOException
     * @throws ParseException
     * @throws org.json.simple.parser.ParseException 
     */
    public static void sendAndroidMessage(String title,String description,String messagePayload, String pushToken) throws IOException, ParseException, org.json.simple.parser.ParseException {
        Constants.useOfficial();
      //Constants.useSandbox();//测试
        Sender sender = new Sender(APP_SECRET_KEY_ANDROID);
    	Message message = new Message.Builder()
    			.title(title)
    			.description(description)
    			.payload(messagePayload)
    			.restrictedPackageName(PACKAGE_NAME_ANDROID)
    			.notifyType(1) // 使用默认提示音提示
    			.build();
    	Result send = sender.send(message, pushToken, 0); // 根据regID，发送消息到指定设备上，不重试。
 
    	logger.debug("========android 消息推送返回:==========" + send);
    }
    
    public static int sendMessage(MmsAppMsgRecord appMsg) {
    	try {
    		String pushToken = (String) RedisUtils.getHashKey("loginInfo" + appMsg.getMemberId(), "pushToken");
    		if(StringUtils.isBlank(pushToken)) {
    			logger.error("pushToken为空，忽略消息推送");
    			return -1;
    		}
    		String osType = (String) RedisUtils.getHashKey("loginInfo" + appMsg.getMemberId(), "osType");
    		logger.debug("========osType:==========" + osType);
    		
	    	if(StringUtils.equals(osType, "android")){	    		
			    sendAndroidMessage("无忧借条给您通知", appMsg.getTitle(),"{\"para\":\""+appMsg.getPara()+"\",\"mode\":\""+appMsg.getMode()+"\",\"d\":\""+appMsg.getMemberId()+"\"}", pushToken);		
	    	}else if(StringUtils.equals(osType, "ios")){   		
	    		sendIosMessage_Bank(appMsg.getTitle(),appMsg.getTitle(),appMsg.getMode(), appMsg.getPara(),String.valueOf(appMsg.getMemberId()),pushToken);
	    	}else if(StringUtils.equals(osType, "ios_bank")){
	    		sendIosMessage_Bank(appMsg.getTitle(),appMsg.getTitle(),appMsg.getMode(), appMsg.getPara(),String.valueOf(appMsg.getMemberId()),pushToken);
	    	}
	    	return 0;
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(e));
			return -1;
		}
    }

}
