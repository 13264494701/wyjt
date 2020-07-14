package com.jxf.web.app.wyjt;


import javax.servlet.http.HttpServletRequest;

import com.jxf.svc.cache.RedisUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.jxf.mem.entity.Member;
import com.jxf.mem.service.MemberService;
import com.jxf.mms.msg.SendSmsMsgService;
import com.jxf.svc.security.PasswordUtils;
import com.jxf.web.app.BaseController;

import com.jxf.web.model.ResponseData;
import com.jxf.web.model.wyjt.app.FindPasswordRequestParam;

/**
 * 
 * @类功能说明： 密码
 * @类修改者： 
 * @修改日期： 
 * @修改说明： 
 * @公司名称：
 * @作者：wo 
 * @创建时间：2016年12月21日 下午4:07:01 
 * @版本：V1.0
 */
@Controller("wyjtAppPasswdController")
@RequestMapping(value="${wyjtApp}/password")
public class PasswdController extends BaseController {

	private static final Logger logger = LoggerFactory.getLogger(PasswdController.class);
	@Autowired
	private MemberService memberService;

	@Autowired
    private SendSmsMsgService sendSmsMsgService;

	/**
	 * 找回登录密码
	 * @return 
	 */
	@ResponseBody
	@RequestMapping(value = "/findPassword", method = RequestMethod.POST)
	public ResponseData findPassword(HttpServletRequest request) {
		String param = request.getParameter("param");
		FindPasswordRequestParam reqData = JSONObject.parseObject(param,FindPasswordRequestParam.class);
		
		String password = reqData.getPassword();
		String phoneNo = reqData.getPhoneNo();
		String smsCode = reqData.getSmsCode();
		
		String code = RedisUtils.get("smsCode" + phoneNo);
		if(!StringUtils.equals(smsCode, code)){
			 return ResponseData.error("验证码错误,请重试!");
		}
		Member member = memberService.findByUsername(phoneNo); 
		if(member == null){
			 return ResponseData.error("该手机号未注册!");
		}
		member.setPassword(PasswordUtils.entryptPassword(password));
		memberService.save(member);
		
		sendSmsMsgService.sendMessage("changeLoginPasswd", phoneNo, null);
	    return ResponseData.success("找回登录密码成功。");
	}


}