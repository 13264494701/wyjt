package com.jxf.web.app.wyjt;




import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jxf.mem.service.MemberService;
import com.jxf.svc.config.Global;
import com.jxf.web.minipro.BaseController;


/**
 * Controller - 请求调动中心
 * 
 * @author JINXINFU
 * @version 2.0
 */
@Controller("wyjtAppDispatchController")
@RequestMapping(value="${wyjtApp}")
public class DispatchController extends BaseController {

	private static final Logger log = LoggerFactory.getLogger(DispatchController.class);

	@Autowired
	private MemberService memberService;
	
	/**
	 * 调度转发
	 */
//	@IPFilter(deny="127.0.0.1")
	@RequestMapping(value = "api")
	public String dispatch(HttpServletRequest request) {
		StringBuffer uri = new StringBuffer();
		String actionName = request.getParameter("actionName");
		String methodName = request.getParameter("methodName");
		uri.append(Global.getWyjtAppPath());
		uri.append("/");
		uri.append(actionName);
		uri.append("/");
		uri.append(methodName);
		log.warn("[{}]-[{}]-[{}]",Global.getRemoteAddr(request),uri.toString(),memberService.getCurrent2().getId());
		return "forward:"+uri.toString();
	}
}