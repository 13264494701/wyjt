package com.jxf.mem.utils;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.protocol.Protocol;

import java.io.IOException;

/**
 * 用于获取https链接内容
 */
public class HttpsClientUtil {
	
	public static String getContent(String url){
		
		String response = "";
		HttpClient http = new HttpClient();
		Protocol myhttps = new Protocol("https", new MySSLProtocolSocketFactory(), 443);
		Protocol.registerProtocol("https", myhttps);
		PostMethod post = new PostMethod(url);
		
		
		try {
			post.getParams().setContentCharset("UTF-8");
			http.executeMethod(post);
			response = post.getResponseBodyAsString();
		} catch (HttpException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}  
        
        return response;
        
	}
	
	
}  
