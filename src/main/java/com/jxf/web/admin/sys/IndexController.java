package com.jxf.web.admin.sys;



import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.RequestMapping;



import com.jxf.svc.config.Global;

import com.jxf.svc.security.Principal;

import com.jxf.svc.security.shiro.session.SessionDAO;

import com.jxf.svc.sys.util.UserUtils;
import com.jxf.svc.utils.CookieUtils;

import com.jxf.svc.utils.StringUtils;


/**
 * 登录Controller
 * @author jxf
 * @version 2015-07-28
 */
@Controller("adminIndexController")
@RequestMapping(value = "${adminPath}")
public class IndexController extends BaseController{
	
	@Autowired
	private SessionDAO sessionDAO;

	
	/**
	 * 登录成功，进入管理首页
	 */
	@RequiresPermissions("admin")
	@RequestMapping(value = {"index",""})
	public String index(HttpServletRequest request, HttpServletResponse response) {
		Principal principal = UserUtils.getPrincipal();

		// 登录成功后，验证码计算器清零
		isValidateCodeLogin(principal.getLoginName(), false, true);
		
		if (logger.isDebugEnabled()){
			logger.debug("show index, active session size: {}", sessionDAO.getActiveSessions(false).size());
		}
		
		// 如果已登录，再次访问主页，则退出原账号。
		if (Global.TRUE.equals(Global.getConfig("notAllowRefreshIndex"))){
			String logined = CookieUtils.getCookie(request,null, "LOGINED",false);
			if (StringUtils.isBlank(logined) || "false".equals(logined)){
				CookieUtils.setCookie(response, "LOGINED", "true","/",Global.COOKIE_MAX_AGE);
			}else if (StringUtils.equals(logined, "true")){
				UserUtils.getSubject().logout();
				return "redirect:" + adminPath + "/login";
			}
		}		
		return "admin/sys/sysIndex";
	}
	

}
