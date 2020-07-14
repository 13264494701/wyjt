package com.jxf.web.h5.gxt.member;




import javax.servlet.http.HttpServletRequest;

import org.ebaoquan.rop.thirdparty.org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jxf.mem.entity.Member;
import com.jxf.mem.entity.MemberCard;
import com.jxf.mem.entity.MemberVerified;
import com.jxf.mem.service.MemberCardService;
import com.jxf.mem.service.MemberService;
import com.jxf.mem.service.MemberVerifiedService;
import com.jxf.mem.utils.VerifiedUtils;
import com.jxf.nfs.service.NfsBankBinService;
import com.jxf.svc.cache.RedisUtils;
import com.jxf.web.minipro.BaseController;
import com.jxf.web.model.ResponseData;
import com.jxf.web.model.wyjt.app.card.GetBankCardResponseResult;


/**
 * Controller - 会员银行卡
 * 
 * @author gaobo
 */
@Controller("gxtH5MemberCardController")
@RequestMapping(value="${gxtH5}/card")
public class MemberCardController extends BaseController {

	private static final Logger logger = LoggerFactory.getLogger(MemberCardController.class);
	@Autowired
	private MemberService memberService;
	@Autowired
	private MemberCardService memberCardService;
	@Autowired
	private NfsBankBinService bankBinService;
	@Autowired
	private MemberVerifiedService memberVerifiedService;
	
	
	/**
	 *	 获取会员银行卡信息
	 */
	@RequestMapping(value = "/getBankCard")
	public @ResponseBody ResponseData getBankCard(HttpServletRequest request){
		Member member = memberService.getCurrent();
		GetBankCardResponseResult result = new GetBankCardResponseResult();
	
		MemberCard memberCard = memberCardService.getCardByMember(member);
		if(memberCard == null) {
			logger.error("=====尚未绑定银行卡=====");
			return ResponseData.error("尚未绑定银行卡");
		}
		
		String cardNo = memberCardService.hideCardNo(memberCard.getCardNo());
		result.setName(member.getName());
		result.setBankName(memberCard.getBank().getName());
		result.setCardNo(cardNo);
		return ResponseData.success("获取会员银行卡信息成功", result);
	}


	/**
	 * 	绑定银行卡
	 */
	@RequestMapping(value = "/bindBankCard")
	public @ResponseBody ResponseData bindBankCard(String cardNo, String smsCode){
		Member member = memberService.getCurrent();
		String phoneNo = member.getUsername();
		String name = member.getName();
		String idNo = member.getIdNo();

		String serverSmsCode = RedisUtils.get("smsCode" + phoneNo);
        if (StringUtils.isBlank(serverSmsCode) || !StringUtils.equals(serverSmsCode, smsCode)) {
        	logger.error("======== serverSmsCode:"+serverSmsCode+"smsCode:"+smsCode);
        	return ResponseData.error("短信验证码错误 请重试");
        } 
        
        int count = memberCardService.getChangeCardCount(member);
		if(count > 7) {
			logger.error("=====每年重新绑定银行卡次数超过八次=====");
			return ResponseData.error("每年重新绑定银行卡次数不得超过八次");
		}
		
		if (StringUtils.isBlank(cardNo)) {
			logger.error("=====银行卡号为空=====");
			return ResponseData.error("银行卡号不能为空");
		}
		
		Boolean isSupportCardNo = bankBinService.checkCardNo(cardNo);
		if(!isSupportCardNo) {
			logger.error("=====不支持的银行卡号=====");
			return ResponseData.error("不支持的银行卡号");
		}	
		
		if(!(VerifiedUtils.isVerified(member.getVerifiedList(), 1)&&
			 VerifiedUtils.isVerified(member.getVerifiedList(), 2))) {
			logger.error("=====未进行身份认证=====");
			return ResponseData.error("请先进行身份认证");
		}

		/**记录认证信息*/
		MemberVerified memberVerified = new MemberVerified();
		memberVerified.setMember(member);
		memberVerified.setIdNo(idNo);
		memberVerified.setRealName(name);
		memberVerified.setPhoneNo(phoneNo);
		memberVerified.setCardNo(cardNo);
		memberVerified.setEmail(member.getEmail());
		memberVerified.setStatus(MemberVerified.Status.unverified);
		memberVerifiedService.save(memberVerified);
		
		/**验证四要素*/
		ResponseData responseData = memberVerifiedService.verifyCardInformation(cardNo, phoneNo, name, idNo, memberVerified);
		return responseData;
	}
	
	/**
	 * 	解绑银行卡
	 */
	@RequestMapping(value = "/unBindBankCard")
	public @ResponseBody ResponseData unBindBankCard(HttpServletRequest request){
		Member member = memberService.getCurrent();
		
		memberCardService.unBindBankCard(member);
		return ResponseData.success("银行卡解绑成功");
	}
	
	
	


}