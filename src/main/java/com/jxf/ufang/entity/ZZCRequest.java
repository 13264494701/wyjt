package com.jxf.ufang.entity;

import java.io.UnsupportedEncodingException;
import java.util.Base64;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;

import com.jxf.svc.config.Global;

/**
 * @作者: xiaorongdian
 * @创建时间 :2019年3月21日 下午4:44:34
 * @功能说明:
 */
public class ZZCRequest {
	private final String host = Global.getConfig("zzc.host"); 
	
	// 替换成已开通服务的用户邮箱及密码
    private final String DEFAULT_USER = "457290140@qq.com";
    private final String DEFAULT_PASS = "Wxxg785276+++";
    private HttpPost post;
    
    private String getAuthHeader() throws UnsupportedEncodingException {
        String auth = DEFAULT_USER + ":" + DEFAULT_PASS;
        byte[] bytes = auth.getBytes("UTF-8");
        String encoding = Base64.getEncoder().encodeToString(bytes);
        return "Basic " + encoding;
    }
    
    public ZZCRequest() throws UnsupportedEncodingException {
        // default to blacklist v2
        this.post = new HttpPost(host.concat("/blacklist/api/v2/blacklist/search"));
        post.setHeader("Content-Type", "application/json");
        post.setHeader("Authorization", getAuthHeader());
        post.setEntity(new StringEntity("",ContentType.create("application/json", "UTF-8")));
    }
    
    public ZZCRequest(String url, String jsonBody) throws UnsupportedEncodingException {
        this.post = new HttpPost(host.concat(url));
        post.setHeader("Content-Type", "application/json");
        post.setHeader("Authorization", getAuthHeader());
        post.setEntity(new StringEntity(jsonBody, ContentType.create("application/json", "UTF-8")));
    }
    
    public HttpPost getPost() {
        return post;
    }
}
