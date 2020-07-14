package com.jxf.svc.security.shiro.filter;


import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.UserFilter;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.stereotype.Service;

import com.jxf.svc.security.Principal;






/**
 * 用户鉴权过滤类
 * @author wo
 * @version 2018-11-17
 */
@Service
public class UfangUserFilter extends UserFilter  {

	private String redirectUrl = "/";

	public String getRedirectUrl() {
		return redirectUrl;
	}

	public void setRedirectUrl(String redirectUrl) {
		this.redirectUrl = redirectUrl;
	}
	
	@Override
	protected boolean isAccessAllowed(ServletRequest request,ServletResponse response, Object mappedValue) {

	
		Subject subject = getSubject(request, response);
		Principal principal = (Principal)subject.getPrincipal();
        if(null!=principal&&!principal.getUserType().equals(Principal.UserType.ufang)) {
        	subject.logout();
        	return false;
        }
		
		return super.isAccessAllowed(request, response, mappedValue);
	}
	
	@Override
	protected boolean onAccessDenied(ServletRequest request,
			ServletResponse response) throws Exception {
		
        saveRequest(request);  
        WebUtils.issueRedirect(request, response, getRedirectUrl());  
		return false;
	}
}