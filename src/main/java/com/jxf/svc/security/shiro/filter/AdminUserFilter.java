package com.jxf.svc.security.shiro.filter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.UserFilter;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.stereotype.Service;

import com.jxf.svc.security.Principal;
import com.jxf.svc.security.shiro.CustomUsernamePasswordToken;

/**
 * 用户鉴权过滤类
 * @author wo
 * @version 2018-11-17
 */
@Service
public class AdminUserFilter extends UserFilter   {

	private String redirectUrl = "/";;

	public String getRedirectUrl() {
		return redirectUrl;
	}

	public void setRedirectUrl(String redirectUrl) {
		this.redirectUrl = redirectUrl;
	}
	
	/**
     *返回false
     * @param request
     * @param response
     * @param mappedValue
     * @return 返回结果是false的时候才会执行下面的onAccessDenied方法
     * @throws Exception
     */
	@Override
	protected boolean isAccessAllowed(ServletRequest request,ServletResponse response, Object mappedValue) {

		Subject subject = getSubject(request, response);
		Principal principal = (Principal)subject.getPrincipal();
        if(null!=principal&&!principal.getUserType().equals(Principal.UserType.admin)) {
        	subject.logout();
        	return false;
        }
		
		return super.isAccessAllowed(request, response, mappedValue);
	}
	
	/**
     * 从请求头获取token并验证，验证通过后交给realm进行登录
     * @param request
     * @param response
     * @return 返回结果为true表明登录通过
     * @throws Exception
     */

	@Override
	protected boolean onAccessDenied(ServletRequest servletRequest,ServletResponse servletResponse) throws Exception {
		
		HttpServletRequest request = (HttpServletRequest) servletRequest;
        String jwt = request.getHeader("Authorization");
//        if (JwtUtil.verifyToken(jwt)) {
//        	CustomUsernamePasswordToken usernamePasswordToken = new CustomUsernamePasswordToken(jwt, jwt);
//            try {
//                //委托realm进行登录认证
//                getSubject(servletRequest, servletResponse).login(usernamePasswordToken);
//                return true;
//            }catch (Exception e) {
//                return false;
//            }
//        }

        saveRequest(servletRequest);  
        WebUtils.issueRedirect(servletRequest, servletResponse, getRedirectUrl());  
		return false;
	}
}