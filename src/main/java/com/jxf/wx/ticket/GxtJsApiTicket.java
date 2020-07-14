package com.jxf.wx.ticket;

import java.text.SimpleDateFormat;
import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jxf.svc.utils.HttpUtil;
import com.jxf.svc.utils.JSONUtil;

/**
 * 公信堂微信公众号API接口临时凭证
 */
public class GxtJsApiTicket {
	
	private static final Logger log = LoggerFactory.getLogger(GxtJsApiTicket.class);
	
    public static String ACCESS_TOKEN;
    public static Date EXPIRES_IN;

    public static String JsTicketApi;
    public static Date JSEXPIRES_IN;
    public static SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public interface Config {
        String grant_type = "client_credential";
        
        String APPID = "wx61a1c7b0f189fe01";
        String SECRET = "4e66ecf7f55aabefa4a9008c77b4ebc2";
    }

    public static String getAccessToken() {
        if (ACCESS_TOKEN == null || EXPIRES_IN.before(new Date())) {
            if (EXPIRES_IN != null) {
            	log.error("wechat_getAccessToken={}",sf.format(EXPIRES_IN));
            }
            ACCESS_TOKEN = getWechatAccessToken();
        }
        return ACCESS_TOKEN;
    }

    private static String getWechatAccessToken() {
        String appID = Config.APPID;
        String appsecret = Config.SECRET;
        String grant_type = Config.grant_type;

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.SECOND, 7000);
        EXPIRES_IN = calendar.getTime();

        String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=" + grant_type + "&appid=" + appID + "&secret=" + appsecret;
        String result = HttpUtil.sendHttpsGET(url);

        log.info(result);

        Map<String, Object> map = JSONUtil.toMap(result);
        String access_token = map.get("access_token").toString();
        return access_token;
    }

    public static String getJsapiticket() {
        if (JSEXPIRES_IN != null) {
        	log.error("wechat_getJsapiticket={}", sf.format(JSEXPIRES_IN));
        }
        if (JsTicketApi == null || JSEXPIRES_IN.before(new Date())) {
            JsTicketApi = getWechatJsapiticket();
        }
        return JsTicketApi;
    }

    private static String getWechatJsapiticket() {

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.SECOND, 7000);
        JSEXPIRES_IN = calendar.getTime();

        String url = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=" + getAccessToken() + "&type=jsapi";

        String result = HttpUtil.sendHttpsGET(url);

        Map<String, Object> map = JSONUtil.toMap(result);
        String ticket = map.get("ticket").toString();
        return ticket;
    }

    public static void sendWechatMsg(String jsonString) {
        String access_token = getAccessToken();
        String postUrl = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=" + access_token;
        String resultJson = HttpUtil.sendHttpsPOST(postUrl, jsonString);
        log.info("sendWechatMsg={}", resultJson);
    }

}