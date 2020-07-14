package com.jxf.mem.utils;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;

import com.jxf.mem.entity.Member;
import com.jxf.svc.cache.RedisUtils;
import com.jxf.svc.utils.StringUtils;

/**
 * 	H5页面的工具类
 * @author jxf
 * @version 1.0 2015-07-10
 */
public class H5Utils {
	
	/**
	 *  给页面添加平台和微信信息
	 * @param member
	 * @param mv
	 * @return
	 */
	public static ModelAndView addPlatform(Member member,ModelAndView mv) {
		String appPlatform = (String) RedisUtils.getHashKey("loginInfo"+member.getId(), "osType");
		boolean isWeiXin = false;
	    if (StringUtils.startsWith(appPlatform, "weixin_")) {
	        isWeiXin = true;
	        appPlatform = appPlatform.replace("weixin_", "");
	    } 	    
	    mv.addObject("isWeiXin", isWeiXin);
	    mv.addObject("appPlatform", appPlatform);
		return mv;
	}
	public static ModelAndView addPlatform(HttpServletRequest request, ModelAndView mv) {
		String appPlatform = request.getHeader("x-osType");
		boolean isWeiXin = false;
	    if (StringUtils.startsWith(appPlatform, "weixin_")) {
	        isWeiXin = true;
	        appPlatform = appPlatform.replace("weixin_", "");
	    } 	    
	    mv.addObject("isWeiXin", isWeiXin);
	    mv.addObject("appPlatform", appPlatform);
		return mv;
	}
	
}
