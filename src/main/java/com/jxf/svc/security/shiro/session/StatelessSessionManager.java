package com.jxf.svc.security.shiro.session;

import org.apache.shiro.session.mgt.SessionKey;
import org.apache.shiro.web.servlet.Cookie;
import org.apache.shiro.web.servlet.ShiroHttpServletRequest;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jxf.svc.utils.StringUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;
import java.util.UUID;

/***
 * 
 * @author wo
 *
 */
public class StatelessSessionManager extends DefaultWebSessionManager {
	
	
    public final static Logger LOG = LoggerFactory.getLogger(StatelessSessionManager.class);
    
    /**
     * 这个是服务端要返回给客户端，
     */
    public final static String TOKEN_NAME = "token";
    

    @Override
    public Serializable getSessionId(SessionKey key) {
        Serializable sessionId = key.getSessionId();
        if(sessionId == null){
            HttpServletRequest request = WebUtils.getHttpRequest(key);
            HttpServletResponse response = WebUtils.getHttpResponse(key);
            sessionId = this.getSessionId(request,response);
        }
        HttpServletRequest request = WebUtils.getHttpRequest(key);
        request.setAttribute(TOKEN_NAME,sessionId.toString());
        return sessionId;
    }

    @Override
    protected Serializable getSessionId(ServletRequest request, ServletResponse response) {

        String token = request.getParameter(TOKEN_NAME);
		if (StringUtils.isNotBlank(token)) {
			// 是否将sid保存到cookie，浏览器模式下使用此参数。
			if (WebUtils.isTrue(request, "__cookie")){
		        HttpServletRequest req = (HttpServletRequest)request;
		        HttpServletResponse rsp = (HttpServletResponse)response;
				Cookie template = getSessionIdCookie();
		        Cookie cookie = new SimpleCookie(template);
				cookie.setValue(token); 
				cookie.saveTo(req, rsp);
			}
			// 设置当前session状态
            request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID_SOURCE,
                    ShiroHttpServletRequest.URL_SESSION_ID_SOURCE); // session来源与url
            request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID, token);
            request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID_IS_VALID, Boolean.TRUE);
        	return token;
		}else{
			return super.getSessionId(request, response);
		}
    }

}