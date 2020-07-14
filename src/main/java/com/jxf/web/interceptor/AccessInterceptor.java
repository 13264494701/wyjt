package com.jxf.web.interceptor;

import java.io.PrintWriter;
import java.util.concurrent.TimeUnit;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.jxf.svc.annotation.AccessLimit;
import com.jxf.svc.cache.RedisUtils;
import com.jxf.svc.utils.StringUtils;
import com.jxf.web.model.ResponseData;


/** 
* 接口限制拦截器 
* 
* @ClassName: AccessInterceptor 
* @Description: 
* @author wo
* @date 2018/10/27
*/ 

public class AccessInterceptor implements HandlerInterceptor{

	private static final Logger log = LoggerFactory.getLogger(AccessInterceptor.class);
	
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
	    if (handler instanceof HandlerMethod) {
	        HandlerMethod hm = (HandlerMethod) handler;
	        // 使用注解
	        AccessLimit accessLimit = hm.getMethodAnnotation(AccessLimit.class);
	        if (accessLimit == null) {
	            return true;
	        }
	        String ip = this.getRemoteIP(request);
	        String url = request.getRequestURL().toString();
	        String key = "req_limit_".concat(url).concat(ip);
	        // 
	        int seconds = accessLimit.seconds();
	        // 最大数
	        int maxCount = accessLimit.maxCount();
	        key += "_";
	        long count = RedisUtils.increment(key, 1);
	        if (count == 1) {
	        	RedisUtils.expire(key, seconds, TimeUnit.SECONDS);
	        }
	        if (count > maxCount) {
	        	log.info("用户IP[" + ip + "]访问地址[" + url + "]超过了限定的次数["
	                    + maxCount + "]");
	            // 超过次数，权限拒绝访问，访问太频繁！
	        	responseMessage(response, response.getWriter(), ResponseData.error("亲，请不要访问太频繁哦！"));
	            return false;
	        }
	    }
	    return true;
	}

    
	@Override
	public void afterCompletion(HttpServletRequest arg0,
	        HttpServletResponse arg1, Object arg2, Exception arg3)
	        throws Exception {
	}

	@Override
	public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1,
	        Object arg2, ModelAndView arg3) throws Exception {
	}

	private String getRemoteIP(HttpServletRequest request) {
	    String ip = request.getHeader("X-Forwarded-For");
	    if (StringUtils.isNotEmpty(ip) && !"unKnown".equalsIgnoreCase(ip)) {
	        // 多次反向代理后会有多个ip值，第一个ip才是真实ip
	        int index = ip.indexOf(",");
	        if (index != -1) {
	            return ip.substring(0, index);
	        } else {
	            return ip;
	        }
	    }

	    ip = request.getHeader("X-Real-IP");
	    if (StringUtils.isNotEmpty(ip) && !"unKnown".equalsIgnoreCase(ip)) {
	        return ip;
	    }

	    return request.getRemoteAddr();
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
