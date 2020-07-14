package com.jxf.web.wap;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


import com.jxf.mms.msg.SendSmsMsgService;
import com.jxf.mms.msg.utils.VerifyCodeUtils;
import com.jxf.svc.annotation.AccessLimit;
import com.jxf.svc.cache.RedisUtils;
import com.jxf.ufang.entity.UfangLoaneeData;
import com.jxf.ufang.service.UfangLoaneeDataService;
import com.jxf.web.model.ResponseData;






/**
 * 贷款超市Controller
 * @author wo
 * @version 2018-12-20
 */
@Controller
@RequestMapping(value = "${wapPath}/sMarket")
public class SuperMarketController extends BaseController {


	@Autowired
	private SendSmsMsgService sendSmsMsgService;
	@Autowired
	private UfangLoaneeDataService loaneeDataService;
	
	/**
	 * 发送短信验证码
	 */
	@RequestMapping(value = "/sendSmsVerifyCode", method = RequestMethod.POST)
	public @ResponseBody ResponseData sendSmsVerifyCode(HttpServletRequest request) {
		String phoneNo = request.getParameter("mobile");
		// 生成短信验证码
		String smsCode = VerifyCodeUtils.genSmsValidCode();
        //将短信验证码存入缓存中
		RedisUtils.setForTimeMIN("smsCode" + phoneNo, smsCode, 10);
		sendSmsMsgService.sendSms("sMarketRegister", phoneNo, smsCode);

		return ResponseData.success("发送短信验证码成功");
	}
	
	
	
	/**
	 * 注册申请提交
	 */
	@AccessLimit(maxCount = 1, seconds = 1)
	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public @ResponseBody ResponseData register(HttpServletRequest request) {
		String phoneNo = request.getParameter("mobile");
		String verifyCode = request.getParameter("verifyCode");
		
        String smsVerifyCode = RedisUtils.get("smsCode" + phoneNo);
        if (StringUtils.isBlank(smsVerifyCode) || !StringUtils.equals(smsVerifyCode, verifyCode)) {
            return ResponseData.error("短信验证码错误");
        }
        logger.warn("注册手机号{}的验证码为{}",phoneNo,verifyCode);
        UfangLoaneeData loaneeData = new UfangLoaneeData();
        loaneeData.setPhoneNo(phoneNo);
        loaneeData.setChannel(UfangLoaneeData.Channel.weixin);
        loaneeData.setStatus(UfangLoaneeData.Status.fresh);
        loaneeData.setSales("0");
        loaneeDataService.save(loaneeData);
        
		return ResponseData.success("申请提交成功");
	}
	
}