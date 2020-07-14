package com.jxf.web.minipro.interceptor;

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
import com.jxf.svc.cache.JedisUtils;
import com.jxf.svc.config.Constant;
import com.jxf.svc.config.Global;
import com.jxf.svc.security.Principal;
import com.jxf.svc.security.jwt.JwtUtil;
import com.jxf.web.model.ResponseData;
import com.jxf.wx.user.entity.WxUserInfo;

public class WxUserInterceptor implements HandlerInterceptor {

	private static final Logger log = LoggerFactory
			.getLogger(WxUserInterceptor.class);

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
			String token = request.getHeader("token");
			if (StringUtils.isBlank(token)) {
				log.info("=========================token 为空");		
				responseMessage(response, response.getWriter(), ResponseData.unauthorized());
				return false;
			}else{		         
				ResponseData responseData= JwtUtil.verifyAndUpdateToken(token,Global.getTokenTimeout());
				if(responseData.getCode()==Constant.JWT_SUCCESS){
					JSONObject obj = (JSONObject) JSON.toJSON(responseData.getResult());
					response.setHeader("token", obj.getString("token"));
					JSONObject payload = (JSONObject) JSON.toJSON(obj.get("payload"));
					Long id = payload.getLong("id");
//					JedisUtils.setObject(token, id, 1800);
					HttpSession session = request.getSession();
					session.setAttribute(WxUserInfo.PRINCIPAL_ATTRIBUTE_NAME, new Principal(id,token));
					return true;
				}else{
					responseMessage(response, response.getWriter(), ResponseData.unauthorized());
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
