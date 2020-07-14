package com.jxf.web.app.wyjt.member;


import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.jxf.mem.entity.Member;
import com.jxf.mem.entity.MemberResetPayPwd;
import com.jxf.mem.entity.MemberVideoVerify;
import com.jxf.mem.service.MemberResetPayPwdService;
import com.jxf.mem.service.MemberService;
import com.jxf.mem.service.MemberVideoVerifyService;
import com.jxf.mem.utils.VerifiedUtils;
import com.jxf.mms.msg.SendSmsMsgService;
import com.jxf.svc.cache.RedisUtils;
import com.jxf.svc.security.PasswordUtils;
import com.jxf.svc.utils.StringUtils;
import com.jxf.web.app.BaseController;
import com.jxf.web.model.ResponseData;
import com.jxf.web.model.wyjt.app.member.ChangePasswordRequestParam;
import com.jxf.web.model.wyjt.app.member.SetPayPwdRequestParam;
import com.jxf.web.model.wyjt.app.member.VideoVerifyResponseResult;




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
@Controller("wyjtAppMemberPasswdController")
@RequestMapping(value="${wyjtApp}/member")
public class MemberPasswdController extends BaseController {

	private static final Logger logger = LoggerFactory.getLogger(MemberPasswdController.class);
	@Autowired
	private MemberService memberService;
	@Autowired
	private MemberVideoVerifyService memberVideoVerifyService;
	@Autowired
	private MemberResetPayPwdService memberResetPayPwdService;
	@Autowired
    private SendSmsMsgService sendSmsMsgService;


	/**
	 * 修改登录密码
	 * @return 
	 */
	@ResponseBody
	@RequestMapping(value = "/changePassword", method = RequestMethod.POST)
	public ResponseData changePassword(HttpServletRequest request) {
		Member member = memberService.getCurrent();
		String param = request.getParameter("param");
		ChangePasswordRequestParam reqData = JSONObject.parseObject(param,ChangePasswordRequestParam.class);
		String password = reqData.getPassword();
		
		member.setPassword(PasswordUtils.entryptPassword(password));
		memberService.save(member);
		
		sendSmsMsgService.sendMessage("changeLoginPasswd", member.getUsername(), null);
	    return ResponseData.success("修改登录密码成功");
		
	}
	
	
	/**
	 * 	修改支付密码前实名认证
	 */
	@RequestMapping(value = "/beforeSetPayPwd")
	public @ResponseBody
	ResponseData beforeSetPayPwd(HttpServletRequest request) {	
		Member member = memberService.getCurrent();
		MemberResetPayPwd resetPayPwd = new MemberResetPayPwd();//修改支付密码记录
		resetPayPwd.setMember(member);
		resetPayPwd.setPayPwd("");
		resetPayPwd.setStatus(MemberResetPayPwd.Status.pendingReview);
		memberResetPayPwdService.save(resetPayPwd);
		
		MemberVideoVerify videoVerify = new MemberVideoVerify();
		videoVerify.setMember(member);
		videoVerify.setStatus(MemberVideoVerify.Status.pendingReview);
		videoVerify.setType(MemberVideoVerify.Type.setPayPwd);
		videoVerify.setChannel(MemberVideoVerify.Channel.app);
		videoVerify.setTrxId(resetPayPwd.getId());
		MemberVideoVerify findPendingReviewVerify = memberVideoVerifyService.get(videoVerify);
		if(findPendingReviewVerify != null){
			return ResponseData.error("身份信息已提交，请勿重复提交!");
		}
		//检查认证失败次数
		Integer failureTimes = memberVideoVerifyService.countFailure(member.getId());
		if(failureTimes > 3){
			return ResponseData.error("您尝试认证失败，不能再继续尝试。如需重新认证请联系客服.");
		}
		
		//3.验证每天重置密码次数 --不能超过2次
		int dayCounts = memberResetPayPwdService.getResetPayPwdCountEveryday(member);
		if(dayCounts > 1) {
			logger.error("====每天重置密码次数不能超过2次====");
			return ResponseData.error("每天重置密码次数不能超过2次");
		}
		//4.验证每月重置密码次数 --不能超过5次
		int monthCounts = memberResetPayPwdService.getResetPayPwdCountEveryMonth(member);
		if(monthCounts > 4) {
			logger.error("====每月重置密码次数不能超过5次====");
			return ResponseData.error("每月重置密码次数不能超过5次");
		}
		memberVideoVerifyService.save(videoVerify);
		VideoVerifyResponseResult result = memberVideoVerifyService.getResult(1,videoVerify.getId().toString(),member);
		return ResponseData.success("需要视频认证",result);
	}
	
	/**
	 * 	修改支付密码
	 */
	@RequestMapping(value = "/setPayPwd")
	public @ResponseBody
	ResponseData setPayPwd(HttpServletRequest request) {
		Member member = memberService.getCurrent();
		String param = request.getParameter("param");
		SetPayPwdRequestParam reqData = JSONObject.parseObject(param,SetPayPwdRequestParam.class);
		String payPassword = reqData.getPayPassword();
		String smsCode = reqData.getSmsCode();
		String orderId = reqData.getOrderId();
		
		String code = RedisUtils.get("smsCode" + member.getUsername());
		if(!StringUtils.equals(smsCode, code)){
			 return ResponseData.error("验证码错误,请重试!");
		}
		
		if(PasswordUtils.validatePassword(payPassword, member.getPassword())) {
			logger.warn("会员：" + member.getId() + " 修改支付密码====支付密码不能和登录密码一致====");
			return ResponseData.error("支付密码不能和登录密码一致");
		}
		if(PasswordUtils.validatePassword(payPassword, member.getPayPassword())) {
			logger.warn("会员：" + member.getId() + " 修改支付密码====支付密码不能和原支付密码一致====");
			return ResponseData.error("支付密码不能和原支付密码一致");
		}
		
		/** 1验证实名认证 */
		Integer verifiedList = member.getVerifiedList();
		if(!VerifiedUtils.isVerified(verifiedList, 0)) {
			logger.error("====您还没有通过身份认证====");
			return ResponseData.error("您还没有通过身份认证");
		}
		if(!VerifiedUtils.isVerified(verifiedList, 1)) {
			logger.error("====您还没有通过人脸认证====");
			return ResponseData.error("您还没有通过人脸认证");
		}
		if(!VerifiedUtils.isVerified(verifiedList, 2)) {
			logger.error("====您还没有通过手机认证====");
			return ResponseData.error("您还没有通过手机认证");
		}
		
		memberVideoVerifyService.dealPayPassword(member,payPassword,orderId);
		return ResponseData.success("修改支付密码成功");
		
		
	}
	
	@RequestMapping(value="checkPwd")
	@ResponseBody
	public Map<String, Object> checkPayPwd(String pwd) {
		Map<String, Object> data = new HashMap<String,Object>();
		if(StringUtils.isBlank(pwd)) {
			data.put("success", false);
			data.put("msg", "参数错误");
			return data;
		}
		Member member = memberService.getCurrent();	
		ResponseData responseData = memberService.checkPayPwd(pwd, member);
		if(responseData != null && responseData.getCode() == 0) {
			data.put("success", true);
			data.put("pwd", pwd);
		}else if(responseData != null &&responseData.getCode() != 0){
			data.put("success", false);
			data.put("msg", responseData.getMessage());
		}else{
			data.put("success", false);
			data.put("msg", "服务器开小差，请稍候再试！");
		}
		return data;
	}
	
	/**
	 * 	设置支付密码
	 */
	@RequestMapping(value = "/installPayPwd")
	public @ResponseBody
	ResponseData installPayPwd(HttpServletRequest request) {	
		Member member = memberService.getCurrent();
		String param = request.getParameter("param");
		SetPayPwdRequestParam reqData = JSONObject.parseObject(param,SetPayPwdRequestParam.class);
		String payPassword = reqData.getPayPassword();
		if(PasswordUtils.validatePassword(payPassword, member.getPassword())) {
			return ResponseData.error("支付密码不能和登录密码一致");
		}
		
		if(PasswordUtils.validatePassword(payPassword, member.getPayPassword())) {
			return ResponseData.error("支付密码不能和原支付密码一致");
		}
		
		/** 1验证实名认证 */
		Integer verifiedList = member.getVerifiedList();
		if(!VerifiedUtils.isVerified(verifiedList, 0)) {
			return ResponseData.error("您还没有通过身份认证");
		}
		if(!VerifiedUtils.isVerified(verifiedList, 1)) {
			return ResponseData.error("您还没有通过人脸认证");
		}
		if(VerifiedUtils.isVerified(verifiedList,22)) {
			return ResponseData.error("您已设置支付密码!");
		}
		
		member.setVerifiedList(VerifiedUtils.addVerified(member.getVerifiedList(), 22));
		member.setPayPassword(payPassword);
		memberService.save(member);
		
		return ResponseData.success("设置支付密码成功");
	}
	
	
}