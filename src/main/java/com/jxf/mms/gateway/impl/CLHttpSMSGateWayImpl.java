package com.jxf.mms.gateway.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.jxf.mms.gateway.HttpSMSGateWay;
import com.jxf.svc.utils.Exceptions;

/**
 * 创蓝短信接口
 * @author JINXINFU
 */
@Component("cLHttpSMSGateWayImpl")
public class CLHttpSMSGateWayImpl implements HttpSMSGateWay{
	
	protected final Logger logger = LoggerFactory.getLogger(getClass());
	
	public  String  send(String url, Map<String, String> map,String encoding) throws Exception {
		String body ="";
		//创建httpclient对象  
		CloseableHttpClient client = HttpClients.createDefault();  
		//创建post方式请求对象  
        HttpPost httpPost = new HttpPost(url);  
        //装填参数  
        JSONObject json = new JSONObject();  
        if(map!=null){  
            for (Entry<String, String> entry : map.entrySet()) {  
                json.put(entry.getKey(),entry.getValue());
            }  
        }  
        //设置参数到请求对象中  
        httpPost.setEntity(new StringEntity(json.toString(), encoding));
        //设置header信息  
        //指定报文头【Content-type】、【User-Agent】  
        httpPost.setHeader("Content-type", "application/json");  
        httpPost.setHeader("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");  
        //执行请求操作，并拿到结果（同步阻塞）  
        CloseableHttpResponse response = client.execute(httpPost);  
        //获取结果实体  
        HttpEntity entity = response.getEntity();  
        if (entity != null) {  
            //按指定编码转换结果实体为String类型  
            body = EntityUtils.toString(entity, encoding);  
        }  
        EntityUtils.consume(entity);  
        //释放链接  
        response.close();  
        return body; 
	}
	
	/**
	 * 	短信发送(行业帐号)
	 * @param phoneNo 短信接收手机号
	 * @param msg 消息内容
	 * @return
	 */

	public  int send(String phoneNo,String msg){

		try {
			Map<String, String> map = new HashMap<String, String>();  
		    map.put("account", "N5234817");  
		    map.put("password", "VwdOM1PByhb9d2");  
		    map.put("phone", phoneNo);  
		    map.put("msg", "【无忧借条】"+msg);  
		    String body = send("http://smsbj1.253.com/msg/send/json", map,"utf-8");  
		    logger.debug(body);
		    return 0;
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(e));
		}
		
		return -1;

	}
	
	/**
	 * 	短信发送(催收帐号)
	 * @param phoneNo 短信接收手机号
	 * @param msg 消息内容
	 * @return
	 */

	public  int sendCollectionMessage(String phoneNo,String msg){

		try {
			Map<String, String> map = new HashMap<String, String>();  
		    map.put("account", "N5234817");  
		    map.put("password", "VwdOM1PByhb9d2");  
		    map.put("phone", phoneNo);  
		    map.put("msg", "【无忧借条】"+msg);  
		    String body = send("http://smsbj1.253.com/msg/send/json", map,"utf-8");  
		    logger.debug(body);
		    return 0;
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(e));
		}
		
		return -1;

	}

	@Override
	public int sendNetLoanMessage(String phoneNo, String msg) {
		try {
			Map<String, String> map = new HashMap<String, String>();  
		    map.put("account", "N5234817");  
		    map.put("password", "VwdOM1PByhb9d2");  
		    map.put("phone", phoneNo);  
		    map.put("msg", "【无忧借条】"+msg);  
		    String body = send("http://smsbj1.253.com/msg/send/json", map,"utf-8");  
		    logger.debug(body);
		    return 0;
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(e));
		}
		
		return -1;
	}
}
