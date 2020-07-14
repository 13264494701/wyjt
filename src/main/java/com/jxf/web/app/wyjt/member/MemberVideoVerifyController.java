package com.jxf.web.app.wyjt.member;


import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.ebaoquan.rop.thirdparty.org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.jxf.mem.entity.Member;
import com.jxf.mem.entity.MemberVideoVerify;
import com.jxf.mem.entity.MemberVideoVerify.Status;
import com.jxf.mem.service.MemberService;
import com.jxf.mem.service.MemberVideoVerifyService;
import com.jxf.mem.utils.VerifiedUtils;
import com.jxf.web.app.BaseController;
import com.jxf.web.model.ResponseData;
import com.jxf.web.model.wyjt.app.member.CheckVideoVerifyRequestParam;
import com.jxf.web.model.wyjt.app.member.CheckVideoVerifyResponseResult;
import com.jxf.web.model.wyjt.app.member.VideoVerifyResponseResult;

/**
 * Controller - 身份+视频认证
 * 
 * @author gaobo
 */
@Controller("wyjtAppMemberVideoVerifyController")
@RequestMapping(value="${wyjtApp}/member")
public class MemberVideoVerifyController extends BaseController {

	private static final Logger logger = LoggerFactory.getLogger(MemberVideoVerifyController.class);
	
	@Autowired
	private MemberService memberService;
	@Autowired
	private MemberVideoVerifyService memberVideoVerifyService;

	/**
	 * 实名认证
	 */
	@RequestMapping(value = "/realIdentity")
	public @ResponseBody
	ResponseData videoVerify(HttpServletRequest request){
		Member member = memberService.getCurrent();
		MemberVideoVerify videoVerify = new MemberVideoVerify();
		videoVerify.setMember(member);
		videoVerify.setStatus(MemberVideoVerify.Status.pendingReview);
		videoVerify.setType(MemberVideoVerify.Type.realIdentity);
		videoVerify.setChannel(MemberVideoVerify.Channel.app);
		//检查认证失败次数
		Integer failureTimes = memberVideoVerifyService.countFailure(member.getId());
		if(failureTimes > 3){
			return ResponseData.error("您尝试认证失败，不能再继续尝试。如需重新认证请联系客服.");
		}
		
		Integer verifiedList = member.getVerifiedList();
		Integer identityStatus = 0;
		if(VerifiedUtils.isVerified(verifiedList, 1) && VerifiedUtils.isVerified(verifiedList, 2)){
			identityStatus++;
			if(VerifiedUtils.isVerified(verifiedList, 22)){
				identityStatus++;
				if(VerifiedUtils.isVerified(verifiedList, 23)){
					identityStatus++;
				}
			}
		} 
		VideoVerifyResponseResult result = new VideoVerifyResponseResult();
		if(identityStatus == 0) {
			// 人脸demo数据
			memberVideoVerifyService.save(videoVerify);
			result = memberVideoVerifyService.getResult(2,videoVerify.getId().toString(),member);
		}
		
		result.setIdentityStatus(identityStatus+"");
		return ResponseData.success("需要视频认证",result);
		
	}
	
	/**
	 * 	人脸异步通知
	 */
	@RequestMapping(value = "/videoVerify/notify")
	public @ResponseBody Map<String, String> verifyNotify(HttpServletRequest request) {	
		logger.debug("视频认证回调");
		Map<String, String> result = memberVideoVerifyService.notifyProcess(request);
		return result;
	}
	
	/**
	 * 检查视频认证是否成功
	 */
	@RequestMapping(value = "/checkVideoVerify")
	public @ResponseBody
	ResponseData checkVideoVerify(HttpServletRequest request){
		String param = request.getParameter("param");
		CheckVideoVerifyRequestParam reqData = JSONObject.parseObject(param,CheckVideoVerifyRequestParam.class);
		String orderId = reqData.getOrderId();
		if(StringUtils.isBlank(orderId)) {
			return ResponseData.error("查询订单编号不能为空");
		}
		MemberVideoVerify memberVideoVerify = memberVideoVerifyService.get(Long.valueOf(orderId));
		Status status = memberVideoVerify.getStatus();
		CheckVideoVerifyResponseResult result = new CheckVideoVerifyResponseResult();
		if(status.equals(MemberVideoVerify.Status.pendingReview)){
			result.setResultCode(0);
			result.setResultMessage("待认证");
		}else if(status.equals(MemberVideoVerify.Status.verified)){
			result.setResultCode(1);
			result.setResultMessage("认证成功");
		}else if(status.equals(MemberVideoVerify.Status.failure)){
			result.setResultCode(2);
			result.setResultMessage(memberVideoVerify.getFailReason());
		}
		return ResponseData.success("查询结果",result);
	}
}