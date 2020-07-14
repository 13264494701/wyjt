package com.jxf.web.app.wyjt;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jxf.mem.service.MemberService;
import com.jxf.web.app.BaseController;
import com.jxf.web.model.ResponseData;

/**
 * 
 * @类功能说明： 会员注销
 * @类修改者： 
 * @修改日期： 
 * @修改说明： 
 * @公司名称：北京金信服科技有限公司
 * @作者：HUOJIABAO 
 * @创建时间：2016年5月1日 下午3:00:44 
 * @版本：V1.0
 */
@Controller("wyjtAppLogoutController")
@RequestMapping(value="${wyjtApp}/logout")
public class LogoutController extends BaseController {

	@Autowired
	private MemberService memberService;

	/**
	 * 设置点击退出当前用户
	 */
	@ResponseBody
	@RequestMapping(value = "", method = RequestMethod.GET)
	public ResponseData logout(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		String token = request.getHeader("token");
		//ResponseData responseData = JwtUtil.updateToken(token, 0);
		//response.setHeader("token",responseData.getResult().toString());
		return ResponseData.success("成功退出");
	}

}