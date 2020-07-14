package com.jxf.svc.security.shiro;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.web.servlet.SimpleCookie;

public class CustomSimpleCookie extends SimpleCookie {

	
	
	@Override
    public String readValue(HttpServletRequest request, HttpServletResponse ignored) {
        //读取 SessionId，即 jwt 串
        String value = request.getHeader(getName());
        if (value == null)
            value = request.getParameter(getName());
        return value;
    }

}
