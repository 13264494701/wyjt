package com.jxf.web.app.interceptor;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.filter.HiddenHttpMethodFilter;


public class MyHiddenHttpMethodFilter extends HiddenHttpMethodFilter {

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String memberToken=request.getParameter("_header");
		String type = request.getParameter("_type");
		if ((memberToken!=null && !memberToken.trim().equals("") && type != null) || (type != null && !type.trim().equals(""))) {
			HttpServletRequest wrapper = new HttpHeaderRequestWrapper(request,memberToken,type);
			super.doFilterInternal(wrapper, response, filterChain);
		}else {
			super.doFilterInternal(request, response, filterChain);
		}
	}
	
	private static class HttpHeaderRequestWrapper extends HttpServletRequestWrapper{
 
		private final String memberToken;
		private final String type;
		
		public HttpHeaderRequestWrapper(HttpServletRequest request,String memberToken,String type) {
			super(request);
			this.memberToken= memberToken;
			this.type= type;
		}
 
		@Override
		public String getHeader(String name) {
			if (name!=null && name.equals("x-memberToken") && super.getHeader("x-memberToken")==null) {
				return memberToken;
			}else if (name!=null && name.equals("type") && super.getHeader("type")==null){
				return type;
			}else {
				return super.getHeader(name);
			}
		}
		
	}
}
