
package com.jxf.web.minipro.wyjt.member;


import java.util.Date;
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
import com.jxf.mem.entity.MemberAct;
import com.jxf.mem.entity.MemberActTrx;
import com.jxf.mem.entity.MemberVerified;
import com.jxf.mem.service.MemberActTrxService;
import com.jxf.mem.service.MemberService;
import com.jxf.mem.service.MemberVerifiedService;
import com.jxf.svc.cache.CacheConstant;
import com.jxf.svc.cache.CacheUtils;
import com.jxf.svc.cache.RedisUtils;
import com.jxf.svc.utils.DateUtils;
import com.jxf.web.minipro.BaseController;
import com.jxf.web.model.ResponseData;
import com.jxf.web.model.wyjt.app.member.MemberInfoResponseResult;
import com.jxf.wx.user.entity.WxUserInfo;
import com.jxf.wx.user.service.WxUserInfoService;




/**
 * Controller - 会员信息
 * 
 * @author JINXINFU
 * @version 2.0
 */
@Controller("wyjtMiniproMemberController")
@RequestMapping(value="${wyjtMinipro}/member")
public class MemberController extends BaseController {


	@Autowired
	private MemberService memberService;
	@Autowired
	private MemberVerifiedService memberVerifiedService;
	@Autowired
	private MemberActTrxService memberActTrxService;
	@Autowired
	private WxUserInfoService wxUserInfoService;

	


	/**
	 * 查看会员信息
	 */
	@RequestMapping(value = "/info", method = RequestMethod.GET)
	public @ResponseBody
	ResponseData info() {	
		
		WxUserInfo wxUserInfo = wxUserInfoService.getCurrent();
		Member member = memberService.get(wxUserInfo.getMember().getId());

		
		return ResponseData.success("查看会员信息成功",member);
	}

	/**
	 * 查看会员账户信息
	 */
	@RequestMapping(value = "/actinfo", method = RequestMethod.GET)
	public @ResponseBody
	ResponseData actinfo() {	
		Map<String, Object> data = new HashMap<String, Object>();
		WxUserInfo wxUserInfo = wxUserInfoService.getCurrent();
		Member member = memberService.get(wxUserInfo.getMember().getId());
		MemberInfoResponseResult result = memberService.getMemberInfo(member);
		data.put("curBal", result.getCurBal());
		data.put("freezenMoney", result.getFrozenAmount());
		data.put("redbag", result.getRedBag());
		data.put("pendingReceive", result.getPendingReceive());
		data.put("pendingRepayment", result.getPendingRepayment());
		return ResponseData.success("查看会员账户信息成功",data);
	}

	/**
	 * 查看会员账户流水信息
	 */
	@RequestMapping(value = "/acttrx/list", method = RequestMethod.GET)
	public @ResponseBody
	ResponseData acttrxlist() {	
		Map<String, Object> data = new HashMap<String, Object>();
		WxUserInfo wxUserInfo = wxUserInfoService.getCurrent();
		Member member = memberService.get(wxUserInfo.getMember().getId());
		MemberAct memberAct = new MemberAct();
		memberAct.setMember(member);
		//账户余额
		String curBal = memberService.getMemberInfo(member).getCurBal();
		MemberActTrx memberActTrx = new MemberActTrx();
		memberActTrx.setMember(member);
		List<MemberActTrx> actTrxList = memberActTrxService.findList(memberActTrx);
		data.put("curBal", curBal);
		data.put("actTrxList", actTrxList);	
		return ResponseData.success("查看会员账户流水信息成功",data);
	}
	/**
	 * 修改/重置支付密码
	 */
	@RequestMapping(value = "/resetPayPwd", method = RequestMethod.POST)
	public @ResponseBody
	ResponseData resetPayPwd(String payPw1,String payPw2) {	
		WxUserInfo wxUserInfo = wxUserInfoService.getCurrent();
		Member member = memberService.get(wxUserInfo.getMember().getId());
		Long memberId = member.getId();
		if (StringUtils.isEmpty(payPw1)) {
			return ResponseData.error("密码不能为空");
		}
		if (!StringUtils.equals(payPw1, payPw2)) {
			return ResponseData.error("两次输入的密码不相同，请重新输入");
		}
		if (StringUtils.isEmpty(member.getPayPassword())) {
			member.setPayPassword("");
		}
		if (StringUtils.equals(member.getPayPassword(), payPw2)) {
			return ResponseData.error("新密码不能与旧密码相同");
		}
		MemberVerified memberVerified = memberVerifiedService.getVerifiedByMemberId(memberId);
		if (memberVerified == null) {
			return ResponseData.error("您还未进行实名认证");
		}
		memberService.resetPayPw(memberId, payPw1);
		return ResponseData.success("修改支付密码成功");
	}
	/**
	 * 验证旧支付密码是否正确
	 */
	@RequestMapping(value = "/checkOldPwd", method = RequestMethod.GET)
	public @ResponseBody
	ResponseData checkOldPwd(String oldPwd) {	
		WxUserInfo wxUserInfo = wxUserInfoService.getCurrent();
		Member member = memberService.get(wxUserInfo.getMember().getId());
		String payPassword = member.getPayPassword();
		Boolean isLocked = (Boolean)CacheUtils.get(CacheConstant.MEMBER_CACHE, Member.ISLOCKED+member.getId());
		Date lockedDate = (Date)CacheUtils.get(CacheConstant.MEMBER_CACHE, Member.LOCKEDDATE+member.getId());
		long pastMinutes = 0;
		if(lockedDate != null){
			pastMinutes = DateUtils.pastMinutes(lockedDate);//过去的分钟数
		}
		if(isLocked != null && isLocked){
			//被锁定未过30分钟
			if(pastMinutes < 30){
				return ResponseData.error("账号已冻结,请30分钟后再尝试");
			}else{ //被锁定过了30分钟
				CacheUtils.put(CacheConstant.MEMBER_CACHE, Member.ISLOCKED+member.getId(), false);
				member.setIsLocked(false);
				memberService.save(member);
				CacheUtils.put(CacheConstant.MEMBER_CACHE, Member.WRONG_TIMES+member.getId(), 0);//重置错误次数
			}
		}
		if(oldPwd.equals(payPassword)){
			return ResponseData.success("旧密码验证通过");
		}else{
			//错误次数
			Integer wrongTimes = (Integer) CacheUtils.get(CacheConstant.MEMBER_CACHE, Member.WRONG_TIMES+member.getId());
			if(wrongTimes == null){
				wrongTimes = 1;
			}else{
				wrongTimes++;
			}
			if(wrongTimes>=5){
				Date nowDate = new Date();
				CacheUtils.put(CacheConstant.MEMBER_CACHE,Member.ISLOCKED+member.getId(), true);
				CacheUtils.put(CacheConstant.MEMBER_CACHE,Member.LOCKEDDATE+member.getId(), nowDate);
				member.setIsLocked(true);
				member.setLockedDate(nowDate);
				memberService.save(member);
				return ResponseData.error("账号已冻结,请30分钟后再尝试");
			}else{
				CacheUtils.put(CacheConstant.MEMBER_CACHE, Member.WRONG_TIMES+member.getIdNo(), wrongTimes);
				return ResponseData.error("支付密码输入错误，您还有" + (5 - wrongTimes) + "次机会");
			}
		}
	}



}