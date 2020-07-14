package com.jxf.web.minipro.wyjt.member;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.ebaoquan.rop.thirdparty.org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


import com.jxf.mem.entity.Member;
import com.jxf.mem.entity.MemberCard;

import com.jxf.mem.service.MemberCardService;
import com.jxf.mem.service.MemberService;
import com.jxf.mem.utils.VerifiedUtils;
import com.jxf.nfs.constant.TrxRuleConstant;

import com.jxf.nfs.entity.NfsWdrlRecord;
import com.jxf.nfs.service.NfsActService;
import com.jxf.nfs.service.NfsWdrlRecordService;
import com.jxf.pwithdraw.entity.PaymentRequestBean;
import com.jxf.pwithdraw.service.LianlianPayService;
import com.jxf.svc.config.Constant;
import com.jxf.svc.utils.JSONUtil;
import com.jxf.web.minipro.BaseController;
import com.jxf.web.model.ResponseData;
import com.jxf.wx.user.entity.WxUserInfo;
import com.jxf.wx.user.service.WxUserInfoService;

/**
 * 会员提现Controller
 * @author gaobo
 * @version 2018-10-22
 */
@Controller("wyjtMiniproMemberWithdrawCaseController")
@RequestMapping(value = "${wyjtMinipro}/member")
public class MemberWithdrawCaseController extends BaseController {


	@Autowired
	private WxUserInfoService wxUserInfoService;
	@Autowired
	private MemberService memberService;
	@Autowired
	private NfsWdrlRecordService nfsWdrlRecordService;
	@Autowired
	private NfsActService actService;
	@Autowired
	private MemberCardService memberCardService;
	@Autowired
	private LianlianPayService lianlianPayService;

	
	/**
	 * 会员提现
	 */
	@RequestMapping(value = "/withdrawCase", method = RequestMethod.POST)
	public @ResponseBody ResponseData withdrawCase(String payNum,BigDecimal amount) {	
		
		Map<String,Object> data = new HashMap<String,Object>();
		WxUserInfo wxUserInfo = wxUserInfoService.getCurrent();
		Member member = memberService.get(wxUserInfo.getMember());
		
		//检验用户是否被锁定 和 是否不可用
		if(member.getIsEnabled()==false||member.getIsLocked()==true) {
			data.put("resultCode", 10);
			data.put("resultMsg", "您没有权限 不能进行提现");
			return ResponseData.success("用户已被锁定",data);
		}
		//验证支付密码
		boolean isPayPassword = nfsWdrlRecordService.checkPayPassword(member,payNum);
		if(!isPayPassword) {
			data.put("resultCode", 10);
			data.put("resultMsg", "支付密码错误,请重试");
			return ResponseData.success("支付密码错误",data);
		}
		//验证是否绑卡
		MemberCard memberCard = memberCardService.getCardByMember(member);
		if(memberCard == null) {
			data.put("resultCode", 10);
			data.put("resultMsg", "您没有可用银行卡,请先去绑定");
			return ResponseData.success("没有可用银行卡",data);
		}
		//您有逾期借条尚未还款,需还款后再进行提现!
		Boolean verified = VerifiedUtils.isVerified(member.getVerifiedList(),11);
		if(verified) {
			data.put("resultCode", 10);
			data.put("resultMsg", "您有逾期借条尚未还款,需还款后再进行提现!");
			return ResponseData.success("逾期借条尚未还款",data);
		}
		//您当前的提现金额超出限制,请重新输入金额！
		NfsWdrlRecord nfsWdrlRecord = new NfsWdrlRecord();
		boolean flag = nfsWdrlRecordService.checkMoney(nfsWdrlRecord,member,amount,memberCard,data);
		if(!flag) {
			return ResponseData.success("提现失败",data);
		}
		//应该在发送提现请求前扣除用户的提现金额，如果提现失败再退
		int code = actService.updateAct(TrxRuleConstant.MEMBER_WITHDRAWALS, amount, member, nfsWdrlRecord.getId());
		if(code == Constant.UPDATE_FAILED) {
			return ResponseData.error("账户处理失败");
		}
		/**
		 * 第三方交互 提现	加控制。。。
		 */
		PaymentRequestBean paymentRequestBean = new PaymentRequestBean();
		paymentRequestBean.setNo_order(nfsWdrlRecord.getId()+"");
		String response = lianlianPayService.sendData(paymentRequestBean);
		if(StringUtils.isEmpty(response)) {
			data.put("resultCode", 10);
			data.put("resultMsg", "提现未知异常");
			return ResponseData.success("提现未知异常",data);
		}
		Map<String, Object> map = JSONUtil.toMap(response);
		if(StringUtils.equals(map.get("ret_code").toString(), "0000")) {
			data.put("bankName", memberCard.getBank().getName());
			data.put("withdrawalsTime", new Date());
			data.put("money", amount);
			return ResponseData.success("提现成功",data);
		}else {
			data.put("resultCode", 10);
			data.put("resultMsg", map.get("ret_msg"));
			return ResponseData.success("提现未知异常",data);
		}
	}
	
	@RequestMapping(value="llnotify")
	@ResponseBody
	public String notifyProcess(HttpServletRequest request) {
		lianlianPayService.notifyProcess(request);
		return "";
	}
	
}