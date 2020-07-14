package com.jxf.web.home;

import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jxf.mem.entity.Member;
import com.jxf.mem.service.MemberService;
import com.jxf.svc.config.Constant;
import com.jxf.svc.config.Global;
import com.jxf.svc.security.Principal;
import com.jxf.svc.security.jwt.JwtUtil;
import com.jxf.web.model.ResponseData;


/**
 * Interceptor - 会员权限
 * 
 * @author JINXINFU
 * @version 2.0
 */
public class WyjtHomeInterceptor extends HandlerInterceptorAdapter {

	private static final Logger log = LoggerFactory.getLogger(WyjtHomeInterceptor.class);
	
	/** 重定向视图名称前缀 */
	private static final String REDIRECT_VIEW_NAME_PREFIX = "redirect:";

	/** "重定向URL"参数名称 */
	private static final String REDIRECT_URL_PARAMETER_NAME = "redirectUrl";

	/** "会员"属性名称 */
	private static final String MEMBER_ATTRIBUTE_NAME = "member";

	/** 默认登录URL */
	private static final String DEFAULT_LOGIN_URL = Global.getConfig("homePath")+"/login";

	/** 登录URL */
	private String loginUrl = DEFAULT_LOGIN_URL;


	@Autowired
	private MemberService memberService;


	/**  
     * 在业务处理器处理请求之前被调用  
     * 如果返回false  
     *     从当前的拦截器往回执行所有拦截器的afterCompletion(),再退出拦截器链 
     * 如果返回true  
     *    执行下一个拦截器,直到所有的拦截器都执行完毕  
     *    再执行被拦截的Controller  
     *    然后进入拦截器链,  
     *    从最后一个拦截器往回执行所有的postHandle()  
     *    接着再从最后一个拦截器往回执行所有的afterCompletion()    
	 * 
	 * @param request
	 *            HttpServletRequest
	 * @param response
	 *            HttpServletResponse
	 * @param handler
	 *            处理器
	 * @return 是否继续执行
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		
		String requestType = request.getHeader("X-Requested-With");
		if (requestType != null && requestType.equalsIgnoreCase("XMLHttpRequest")) {
			//如果是ajax请求，直接略过，不做处理
			return true;
		}
		String memberToken = request.getParameter("memberToken");	
		if (StringUtils.isBlank(memberToken)) {
			log.warn("=========================token 为空");	
			response.sendRedirect(request.getContextPath() + loginUrl);
			return false;
		}else{		         
			ResponseData responseData= JwtUtil.verifyToken(memberToken);
			if(responseData.getCode()==Constant.JWT_SUCCESS){
				JSONObject payload = (JSONObject) JSON.toJSON(responseData.getResult());
				Long id = payload.getLong("id");
				HttpSession session = request.getSession();
				session.setAttribute(Member.PRINCIPAL_ATTRIBUTE_NAME, new Principal(id,memberToken));
				return true;	

			}else{
				if (request.getMethod().equalsIgnoreCase("GET")) {
					String redirectUrl = request.getQueryString() != null ? request.getRequestURI() + "?" + request.getQueryString() : request.getRequestURI();
					response.sendRedirect(request.getContextPath() + loginUrl + "?" + REDIRECT_URL_PARAMETER_NAME + "=" + URLEncoder.encode(redirectUrl, "UTF-8"));
				} else {
					response.sendRedirect(request.getContextPath() + loginUrl);
				}
				log.warn("Token验证失败{}",responseData.getMessage());	
				return false;
			}
		}	
		
		
	}

	/**
	 * 请求后处理
	 * 
	 * @param request
	 *            HttpServletRequest
	 * @param response
	 *            HttpServletResponse
	 * @param handler
	 *            处理器
	 * @param modelAndView
	 *            数据视图
	 */
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

	}

	/**
	 * 获取登录URL
	 * 
	 * @return 登录URL
	 */
	public String getLoginUrl() {
		return loginUrl;
	}

	/**
	 * 设置登录URL
	 * 
	 * @param loginUrl
	 *            登录URL
	 */
	public void setLoginUrl(String loginUrl) {
		this.loginUrl = loginUrl;
	}

}