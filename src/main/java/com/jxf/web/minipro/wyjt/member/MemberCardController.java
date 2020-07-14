package com.jxf.web.minipro.wyjt.member;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jxf.mem.entity.Member;
import com.jxf.mem.entity.MemberCard;
import com.jxf.mem.entity.MemberVerified;
import com.jxf.mem.service.MemberCardService;
import com.jxf.mem.service.MemberService;
import com.jxf.mem.service.MemberVerifiedService;
import com.jxf.nfs.entity.NfsBankProtocol;
import com.jxf.nfs.service.NfsBankProtocolService;
import com.jxf.pay.service.FuyouPayService;
import com.jxf.svc.utils.EncryptUtils;
import com.jxf.web.minipro.BaseController;
import com.jxf.web.model.ResponseData;
import com.jxf.wx.user.service.WxUserInfoService;




/**
 * Controller - 会员信息
 * 
 * @author JINXINFU
 * @version 2.0
 */
@Controller("wyjtMiniproMemberCardController")
@RequestMapping(value="${wyjtMinipro}/member/card")
public class MemberCardController extends BaseController {


	@Autowired
	private MemberService memberService;
	@Autowired
	private MemberCardService memberCardService;
	@Autowired
	private WxUserInfoService wxUserInfoService;
	@Autowired
	private FuyouPayService fuyouPayService;
	@Autowired
	private NfsBankProtocolService nfsBankProtocolService;
	@Autowired
	private MemberVerifiedService memberVerifiedService;
	
	/**
	 * 查看会员银行卡信息
	 */
	@RequestMapping(value = "/view", method = RequestMethod.GET)
	@ResponseBody
	public ResponseData view() {	
		Map<String, Object> data = new HashMap<String, Object>();
		Member member = wxUserInfoService.getCurrent().getMember();
		member = memberService.get(member.getId());

		MemberCard card = memberCardService.getCardByMember(member);
		card.setCardNo(StringUtils.right(card.getCardNo(), 4));
		data.put("card", card);
		
		return ResponseData.success("查看会员银行卡信息成功",data);
	}
	
	/**
	 * 跳转修改会员银行卡信息页
	 */
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	@ResponseBody 
	public ResponseData edit() {	
		Map<String, Object> data = new HashMap<String, Object>();

		Member member = wxUserInfoService.getCurrent().getMember();
		member = memberService.get(member.getId());

		MemberCard card = memberCardService.getCardByMember(member);
		card.setCardNo(StringUtils.right(card.getCardNo(), 4));
		data.put("name", member.getName());
		data.put("idNo", EncryptUtils.encryptString(member.getIdNo(), EncryptUtils.Type.ID));
		return ResponseData.success("查看会员银行卡信息成功",data);
	}
	
	/**
	 * 修改会员银行卡信息
	 */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	@ResponseBody
	public ResponseData changeCard(String cardNo,String phoneNo) {	 
		Map<String, Object> data = new HashMap<String, Object>();
		Member member = wxUserInfoService.getCurrent().getMember();
		member = memberService.get(member.getId());
		
		NfsBankProtocol proTemp = new NfsBankProtocol();
		proTemp.setMemberId(member.getId());
		List<NfsBankProtocol> list = nfsBankProtocolService.findList(proTemp);
		NfsBankProtocol protocol = list.get(0);
		
		//实名认证
		MemberVerified memberVerified = new MemberVerified();
		memberVerified.setCardNo(cardNo);
		memberVerified.setPhoneNo(phoneNo);
		memberVerified.setIdNo(member.getIdNo());
		memberVerified.setRealName(member.getName());
		boolean result = memberVerifiedService.verifyInputInformation(memberVerified, data);
		if(!result) {
			return ResponseData.success("实名认证不通过，请重新输入换卡信息");
		}
		//解绑
		Map<String, Object> resData = fuyouPayService.unBind(protocol);
		if(!"0".equals(resData.get("success"))) {
			//解绑失败
			data.put("errCode", "100");
			data.put("errMsg", resData.get("reson"));
			return ResponseData.success("会员银行卡解绑失败",data);
		}
		try {
			member.setUsername(phoneNo);
			memberCardService.changeCard(cardNo, member, protocol);
		} catch (Exception e) {
			e.printStackTrace();
			data.put("errCode", "100");
			data.put("errMsg", "系统错误");
		}
		data.put("errCode", "0");
		return ResponseData.success("会员银行卡解绑成功",data);
	}


}