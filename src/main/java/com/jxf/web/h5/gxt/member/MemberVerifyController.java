package com.jxf.web.h5.gxt.member;


import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.ebaoquan.rop.thirdparty.org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jxf.mem.entity.Member;
import com.jxf.mem.entity.MemberVideoVerify;
import com.jxf.mem.entity.MemberVideoVerify.Status;
import com.jxf.mem.service.MemberService;
import com.jxf.mem.service.MemberVideoVerifyService;
import com.jxf.mem.utils.VerifiedUtils;
import com.jxf.web.app.BaseController;
import com.jxf.web.model.ResponseData;

/**
 * Controller - 身份+视频认证
 * 
 * @author gaobo
 */
@Controller("gxtH5MemberVerifyController")
@RequestMapping(value="${gxtH5}/member")
public class MemberVerifyController extends BaseController {

	private static final Logger logger = LoggerFactory.getLogger(MemberVerifyController.class);
	
	@Autowired
	private MemberService memberService;
	@Autowired
	private MemberVideoVerifyService memberVideoVerifyService;

	/**
	 * 实名认证
	 */
	@RequestMapping(value = "/realIdentity")
	public @ResponseBody
	ResponseData videoVerify(HttpServletRequest request,Integer isClear){
		Member member = memberService.getCurrent();
		MemberVideoVerify videoVerify = new MemberVideoVerify();
		videoVerify.setMember(member);
		videoVerify.setStatus(MemberVideoVerify.Status.pendingReview);
		videoVerify.setType(MemberVideoVerify.Type.realIdentity);
		videoVerify.setChannel(MemberVideoVerify.Channel.gxt);
		MemberVideoVerify findPendingReviewVerify = memberVideoVerifyService.get(videoVerify);
		if(findPendingReviewVerify != null){
			return ResponseData.error("身份信息已提交，请勿重复提交!");
		}
		//检查认证失败次数
		Integer failureTimes = memberVideoVerifyService.countFailure(member.getId());
		if(failureTimes > 3){
			return ResponseData.error("您尝试认证失败，不能再继续尝试。如需重新认证请联系客服.");
		}
		
		Integer verifiedList = member.getVerifiedList();
		Integer identityStatus = 0;
		HashMap<String, Object> result = new HashMap<String,Object>();
		if(VerifiedUtils.isVerified(verifiedList, 1) && VerifiedUtils.isVerified(verifiedList, 2)){
			identityStatus++;
			if(VerifiedUtils.isVerified(verifiedList, 22)){
				identityStatus++;
				if(VerifiedUtils.isVerified(verifiedList, 23)){
					identityStatus++;
				}
			}
		} 
		
		String h5Url = "";
		if(identityStatus==0 || isClear == 1){
			// 人脸demo数据
			memberVideoVerifyService.save(videoVerify);
			h5Url = memberVideoVerifyService.getGxtResult(0,videoVerify.getId().toString(),member);
		}
		
		result.put("identityStatus", isClear==1 ? 0:identityStatus);
		result.put("h5Url", h5Url);
		result.put("orderId", videoVerify.getId() == null ? "" : videoVerify.getId().toString());
		if(identityStatus>0){
			result.put("name", member.getName());
			result.put("idNo", member.getIdNo());
		}
		return ResponseData.success("成功",result);
	}
	
	/**
	 * 	人脸异步通知
	 */
	@RequestMapping(value = "/videoVerify/notify")
	public @ResponseBody Map<String, String> verifyNotify(HttpServletRequest request) {	
		logger.info("收到视频认证回调");
		Map<String, String> result = memberVideoVerifyService.notifyProcessForGxt(request);
		return result;
	}
	
	/**
	 * 检查人脸认证结果
	 */
	@RequestMapping(value = "/checkVerify")
	public @ResponseBody
	ResponseData checkVideoVerify(HttpServletRequest request,String orderId,Integer type){
		Member member = memberService.getCurrent();
		if(StringUtils.isBlank(orderId)) {
			return ResponseData.error("查询订单编号不能为空");
		}
		MemberVideoVerify memberVideoVerify = memberVideoVerifyService.get(Long.valueOf(orderId));
		Status status = memberVideoVerify.getStatus();
		HashMap<String, Object> result = new HashMap<String,Object>();
		if(status.equals(MemberVideoVerify.Status.pendingReview)){
			result.put("verifyCode", 0);
			result.put("verifyMessage", "待认证");
		}else if(status.equals(MemberVideoVerify.Status.verified)){
			result.put("verifyCode", 1);
			result.put("verifyMessage", "认证成功");
			if(type == 0){
				result.put("name", memberVideoVerify.getRealName());
				result.put("idNo", memberVideoVerify.getIdNo());
			}
		}else if(status.equals(MemberVideoVerify.Status.failure)){
			result.put("verifyCode", 2);
			result.put("verifyMessage", memberVideoVerify.getFailReason());
		}
		result.put("username", member.getUsername());
		return ResponseData.success("查询结果",result);
	}
}