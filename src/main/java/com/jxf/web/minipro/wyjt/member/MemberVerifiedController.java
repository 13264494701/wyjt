package com.jxf.web.minipro.wyjt.member;


import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jxf.mem.entity.Member;
import com.jxf.mem.entity.MemberCard;
import com.jxf.mem.entity.MemberVerified;
import com.jxf.mem.service.MemberCardService;
import com.jxf.mem.service.MemberService;
import com.jxf.mem.service.MemberVerifiedService;
import com.jxf.mem.utils.VerifiedUtils;
import com.jxf.nfs.service.NfsBankBinService;
import com.jxf.svc.cache.CacheConstant;
import com.jxf.svc.cache.CacheUtils;
import com.jxf.web.minipro.BaseController;
import com.jxf.web.model.ResponseData;
import com.jxf.wx.user.entity.WxUserInfo;
import com.jxf.wx.user.service.WxUserInfoService;




/**
 * Controller - 会员信息
 * 
 * @author JINXINFU
 * @version 2.0
 */
@Controller("wyjtMiniproMemberVerifiedController")
@RequestMapping(value="${wyjtMinipro}/member")
public class MemberVerifiedController extends BaseController {


	@Autowired
	private MemberService memberService;
	@Autowired
	private MemberVerifiedService memberVerifiedService;
	@Autowired
	private MemberCardService memberCardService;
	@Autowired
	private WxUserInfoService wxUserInfoService;
	@Autowired
	private NfsBankBinService bankBinService;
	
	/**
	 * 实名认证
	 */
	@RequestMapping(value = "/realIdentityVerified", method = RequestMethod.POST)
	public @ResponseBody
	ResponseData info(@RequestBody MemberVerified memberVerified) {	
		
		WxUserInfo wxUserInfo = wxUserInfoService.getCurrent();
		Map<String, Object> data = new HashMap<String, Object>();
		String cardNo = memberVerified.getCardNo();
		String idNo = memberVerified.getIdNo();
		String phoneNo = memberVerified.getPhoneNo();
		
		/***
		 * 检查银行卡号是否在支持列表
		 */

		Boolean isSupportCardNo = bankBinService.checkCardNo(cardNo);
		if(!isSupportCardNo) {
			data.put("resultCode", "20");
			data.put("resultMsg", "不支持的该银行卡");
			return ResponseData.success("不支持的该银行卡",data);
		}
				
		String verifySmsCode = (String) CacheUtils.get(CacheConstant.TEMP_CACHE, CacheConstant.TEMP_CACHE+memberVerified.getPhoneNo());
		if(StringUtils.isBlank(verifySmsCode)||!StringUtils.equals(verifySmsCode, memberVerified.getSmsCode())) {

				data.put("resultCode", "30");
				data.put("resultMsg", "短信验证码错误 请重试！！");
				return ResponseData.success("短信验证码错误",data);
		}

		/***
		 * 参数检查
		 * 通过手机号判定是否为老用户
		 */
		Member member = memberService.findByUsername(phoneNo);
		if( member!= null) {
			Integer verifiedList = member.getVerifiedList();
			if(VerifiedUtils.isVerified(verifiedList, 0)||//手机认证
			   VerifiedUtils.isVerified(verifiedList, 1)||//身份证认证
			   VerifiedUtils.isVerified(verifiedList, 3)) {//银行卡认证
				if(!StringUtils.equals(idNo, member.getIdNo())) {
					data.put("resultCode", "30");
					data.put("resultMsg", "您的手机号和身份证号不匹配");
					return ResponseData.success("",data);
				}
				data.put("resultCode", "40");
				wxUserInfo.setIsMember(true);
				wxUserInfo.setMember(member);
				wxUserInfoService.update(wxUserInfo);
				member.setNickname(wxUserInfo.getNickname());
				member.setHeadImage(wxUserInfo.getHeadImage());
				memberService.save(member);
				return ResponseData.success("已绑定老用户",data);
			}
		}
		
	   /***
	     * 第三方四要素认证
	    */
		boolean flag = memberVerifiedService.verifyInputInformation(memberVerified,data);
		if(flag) {			
			//记录用户信息
			member = new Member();
			member.setName(memberVerified.getRealName());
			member.setIdNo(memberVerified.getIdNo());
			member.setUsername(memberVerified.getPhoneNo());
			member.setEmail(memberVerified.getEmail());
			member.setNickname(wxUserInfo.getNickname());
			member.setHeadImage(wxUserInfo.getHeadImage());
			member.setVerifiedList(11);//手机号认证通过，身份证认证通过，银行卡认证通过
			memberService.initMember(member,wxUserInfo.getReferrer().getId());
			//绑定微信端
			wxUserInfo.setIsMember(true);
			wxUserInfo.setMember(member);
			wxUserInfoService.update(wxUserInfo);
			
			//记录认证信息
			memberVerified.setMember(member);
			memberVerified.setStatus(MemberVerified.Status.verified);
			memberVerifiedService.save(memberVerified);
			
			//绑定银行卡信息
			MemberCard card = new MemberCard();
			card.setMember(member);
			card.setCardNo(memberVerified.getCardNo());
			card.setStatus(MemberCard.Status.binded);
			memberCardService.save(card);
			
			return ResponseData.success("四要素认证通过",data);
		}else {
			return ResponseData.success("四要素认证不通过",data);
		}
				

			
	}
	/**
	 * 修改支付密码前验证银行卡号
	 */
	@RequestMapping(value = "/checkBankCardBeforeChangePwd", method = RequestMethod.GET)
	public @ResponseBody
	ResponseData checkBankCardBeforeChangePwd(String bankNum) {	
		Member member = memberService.getCurrent();
		Long memberId = member.getId();
		MemberVerified memberVerified = memberVerifiedService.getVerifiedByMemberId(memberId);
		String cardNo = memberVerified.getCardNo();
		if(bankNum.equals(cardNo)){
			return ResponseData.success("银行卡验证通过");
		}else {
			return ResponseData.error("请输入绑定的银行卡号");
		}
	}
	
	
	
}