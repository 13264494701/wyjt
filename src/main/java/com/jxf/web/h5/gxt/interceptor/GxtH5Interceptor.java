package com.jxf.web.h5.gxt.interceptor;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jxf.mem.entity.Member;
import com.jxf.svc.cache.RedisUtils;
import com.jxf.svc.config.Constant;
import com.jxf.svc.config.Global;
import com.jxf.svc.security.Principal;
import com.jxf.svc.security.jwt.JwtUtil;
import com.jxf.web.model.ResponseData;


public class GxtH5Interceptor implements HandlerInterceptor {

	private static final Logger log = LoggerFactory.getLogger(GxtH5Interceptor.class);

	@Override
	public void afterCompletion(HttpServletRequest arg0,
			HttpServletResponse arg1, Object arg2, Exception arg3)
			throws Exception {

	}

	@Override
	public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1,
			Object arg2, ModelAndView arg3) throws Exception {

	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
			String memberToken = request.getHeader("x-memberToken");
			if (StringUtils.isBlank(memberToken)) {
				memberToken = request.getHeader("memberToken");
			}
			
			if (StringUtils.isBlank(memberToken)) {
				log.warn("=========================token为空" + request.getRequestURI());
				responseMessage(response, response.getWriter(), ResponseData.unauthorized());
				return false;	
			}else{		         
				ResponseData responseData= JwtUtil.verifyAndUpdateToken(memberToken,Global.getTokenTimeout());
				if(responseData.getCode()==Constant.JWT_SUCCESS){
					JSONObject obj = (JSONObject) JSON.toJSON(responseData.getResult());
					response.setHeader("memberToken", obj.getString("token"));
					JSONObject payload = (JSONObject) JSON.toJSON(obj.get("payload"));
					Long id = payload.getLong("id");
				
					//检查用户是否在锁定列表中
					if(RedisUtils.isMember("lockedlist", String.valueOf(id))) {
						log.warn("用户{}已被锁定",id);
						responseMessage(response, response.getWriter(), ResponseData.error("用户已被锁定,请联系管理员解锁！"));
						return false;
					}
								
					HttpSession session = request.getSession();
					session.setAttribute(Member.PRINCIPAL_ATTRIBUTE_NAME, new Principal(id,memberToken));
					return true;	

				}else{
					responseMessage(response, response.getWriter(), ResponseData.unauthorized());
					log.warn("Token验证失败{}",responseData.getMessage());	
					log.warn("当前Token"+memberToken);
					return false;
				}
			}		
	}
	
	//请求不通过，返回错误信息给客户端
    private void responseMessage(HttpServletResponse response, PrintWriter out, ResponseData responseData) {

        response.setContentType("application/json; charset=utf-8");  
        String json = JSONObject.toJSONString(responseData);
        out.print(json);
        out.flush();
        out.close();
    }

}
