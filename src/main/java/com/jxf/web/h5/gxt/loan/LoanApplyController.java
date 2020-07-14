package com.jxf.web.h5.gxt.loan;



import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.jxf.loan.entity.NfsLoanApply;
import com.jxf.loan.entity.NfsLoanApply.LoanRole;
import com.jxf.loan.entity.NfsLoanApplyDetail;
import com.jxf.loan.entity.NfsLoanApplyDetail.PayStatus;
import com.jxf.loan.entity.NfsLoanApplyDetail.Status;
import com.jxf.loan.entity.NfsLoanRecord;
import com.jxf.loan.service.NfsLoanApplyDetailService;
import com.jxf.loan.service.NfsLoanApplyService;
import com.jxf.loan.utils.LoanUtils;
import com.jxf.mem.entity.Member;
import com.jxf.mem.entity.MemberActTrx;
import com.jxf.mem.entity.MemberMessage;
import com.jxf.mem.service.MemberActTrxService;
import com.jxf.mem.service.MemberMessageService;
import com.jxf.mem.service.MemberService;
import com.jxf.mem.utils.VerifiedUtils;
import com.jxf.nfs.constant.TrxRuleConstant;
import com.jxf.svc.config.Constant;
import com.jxf.svc.config.Global;
import com.jxf.svc.utils.CalendarUtil;
import com.jxf.svc.utils.Collections3;
import com.jxf.svc.utils.DateUtils;
import com.jxf.svc.utils.Exceptions;
import com.jxf.svc.utils.StringUtils;
import com.jxf.web.app.BaseController;
import com.jxf.web.model.ResponseData;
import com.jxf.wx.user.entity.WxUserInfo;
import com.jxf.wx.user.service.WxUserInfoService;


/**
 * Controller - 借条申请
 * 
 * @author JINXINFU
 * @version 2.0
 */
@Controller("gxtH5LoanApplyController")
@RequestMapping(value="${gxtH5}/loan")
public class LoanApplyController extends BaseController {
	private static final Logger logger = LoggerFactory.getLogger(LoanApplyController.class);
	@Autowired
	private MemberService memberService;
	@Autowired
	private NfsLoanApplyService loanApplyService;
	@Autowired
	private NfsLoanApplyDetailService loanApplyDetailService;
	@Autowired
	private MemberMessageService memberMessageService;
	@Autowired
	private WxUserInfoService wxUserInfoService;
	@Autowired
	private MemberActTrxService memberActTrxService;
	
	/**
	 * 进入申请补借条页面
	 */
	@RequestMapping(value = "/goToLoanApply")
	@ResponseBody
	public ResponseData goToLoanApply(HttpServletRequest request) {
		Member member = memberService.getCurrent();
		Map<String, Object> data = new HashMap<String,Object>();
		Integer verifiedList = member.getVerifiedList();
		data.put("isRealIdentity", VerifiedUtils.isVerified(verifiedList));
		data.put("isPayPsw", VerifiedUtils.isVerified(verifiedList, 22)); 
		data.put("curBal", StringUtils.decimalToStr(memberService.getCulBal(member), 2));
		data.put("name", member.getName());
		data.put("username", member.getUsername());
		WxUserInfo wxUserInfo = wxUserInfoService.findByMember(member.getId(), "gxt");
		data.put("headImage", wxUserInfo == null ? "":wxUserInfo.getHeadImage());
		data.put("fee", StringUtils.decimalToStr(new BigDecimal(Global.getConfig("gxt.loanDoneFee")), 2));
		return ResponseData.success("请求成功",data);
	}
	
	/**
	 * 申请补借条
	 */
	@RequestMapping(value = "/apply")
	@ResponseBody
	public ResponseData apply(HttpServletRequest request) {
		Member member = memberService.getCurrent();
		if(member.getId()==2597123L) {
			return ResponseData.error("您既往行为触发平台风控规则,无法继续使用补借条功能,如有疑问请联系客服！");
		}
		String pwd = request.getParameter("pwd");
		ResponseData checkPayPwd = memberService.checkPayPwd(pwd, member);
		if(checkPayPwd == null) {
			return ResponseData.error("系统错误，请联系客服处理！");
		}
		if (checkPayPwd.getCode() != 0) {
			return ResponseData.error(checkPayPwd.getMessage());
		}
		String loaneeName = request.getParameter("loaneeName");
		String amount = request.getParameter("amount");
		String beginDate = request.getParameter("beginDate");// 借款日期
		String repayDate = request.getParameter("repayDate");// 应还日期
		String intRate = request.getParameter("intRate");
		String loanPurp = request.getParameter("loanPurp");
		String loanerName = request.getParameter("loanerName");
		String disputeResolution = request.getParameter("disputeResolution");
		String loanRoleStr = request.getParameter("loanRole");
		String rmk = request.getParameter("rmk");
		Date loanStart = DateUtils.parseDate(beginDate);
		Date dueRepayDate = DateUtils.parseDate(repayDate);
		long pastDaysStart = DateUtils.getDifferenceOfTwoDate(new Date(),loanStart );
		long pastDaysRepay = DateUtils.getDifferenceOfTwoDate(dueRepayDate,new Date() );
		int term = DateUtils.getDifferenceOfTwoDate(dueRepayDate, loanStart) + 1;
		if(pastDaysStart < 0){
			return ResponseData.error("开始时间最大为今日");
		}
		if(pastDaysRepay < 1){
			return ResponseData.error("还款时间最小为明日");
		}
		if(CalendarUtil.addYear(loanStart, 3).compareTo(dueRepayDate) <= 0) {
			return ResponseData.error("借条时长最多3年");
		}
		// 邮箱
		Boolean verifiedEmail = VerifiedUtils.isVerified(member.getVerifiedList(), 23);
		if (!verifiedEmail) {
			return ResponseData.error("您还未填写邮箱，暂时无法申请借款");
		}

		NfsLoanApply loanApply = new NfsLoanApply();
		loanApply.setMember(member);
		LoanRole loanRole = LoanRole.values()[Integer.valueOf(loanRoleStr)];
		loanApply.setLoanRole(loanRole);
		loanApply.setLoanType(NfsLoanApply.LoanType.single);
		loanApply.setLoanPurp(NfsLoanApply.LoanPurp.values()[Integer.valueOf(loanPurp)]);
		loanApply.setRepayType(NfsLoanApply.RepayType.oneTimePrincipalAndInterest);
		BigDecimal loanAmount = new BigDecimal(amount);

		loanApply.setAmount(loanAmount);
		loanApply.setRemainAmount(loanAmount);
		loanApply.setIntRate(new BigDecimal(intRate));

		int differenceOfTwoDate = DateUtils.getDifferenceOfTwoDate(dueRepayDate, new Date());
		if(differenceOfTwoDate <= 0){
			return ResponseData.error("还款日至少是明天");
		}
		
		loanApply.setTerm(term);
		loanApply.setChannel(NfsLoanApply.Channel.gxt);

		NfsLoanRecord loanRecord = LoanUtils.calIntForOffline(loanAmount, loanApply.getIntRate(),
				loanApply.getRepayType(), term, loanStart);

		loanApply.setInterest(loanRecord.getInterest());
		loanApply.setRepayAmt(loanRecord.getDueRepayAmount());
		loanApply.setLoanStart(loanStart);
		loanApply.setTrxType(NfsLoanApply.TrxType.offline);
		loanApplyService.save(loanApply);

		NfsLoanApplyDetail detail = new NfsLoanApplyDetail();

		detail.setApply(loanApply);

		Member detailMember = new Member();
		detailMember.setId(0L);
		detailMember.setName(loanerName);
		detail.setLoanRole(NfsLoanApply.LoanRole.loaner);
		detail.setDisputeResolution(NfsLoanApplyDetail.DisputeResolution.arbitration);
		if (loanRole.equals(LoanRole.loaner)) {
			detailMember.setName(loaneeName);
			detail.setLoanRole(NfsLoanApply.LoanRole.loanee);
			detail.setDisputeResolution(
					NfsLoanApplyDetail.DisputeResolution.values()[Integer.valueOf(disputeResolution)]);
		}
		detail.setMember(detailMember);

		detail.setAliveVideoStatus(NfsLoanApplyDetail.AliveVideoStatus.notUpload);
		detail.setIntStatus(NfsLoanApplyDetail.IntStatus.primary);
		detail.setAmount(loanAmount);
		detail.setStatus(NfsLoanApplyDetail.Status.pendingAgree);
		detail.setTrxType(NfsLoanApply.TrxType.offline);
		detail.setPayStatus(PayStatus.waitingPay);
		detail.setRmk(rmk);
		loanApplyDetailService.save(detail);
		Map<String, String> data = new HashMap<String, String>();
		data.put("loan_type", detail.getId() + "_1");
		return ResponseData.success("请求成功", data);
	}
	
	/**
	 * 	取消补借条
	 */
	@RequestMapping(value = "/cancelLoanApply")
	@ResponseBody
	public ResponseData cancelLoanApply(HttpServletRequest request, String loanId) {
		Member member = memberService.getCurrent();
		if(member == null) {
			return ResponseData.error("请登录后再操作");
		}
		NfsLoanApplyDetail loanApplyDetail = loanApplyDetailService.get(Long.parseLong(loanId));
		NfsLoanApply loanApply = loanApplyService.get(loanApplyDetail.getApply());
		Long applyMemberId = loanApply.getMember().getId();
		if(!member.getId().equals(applyMemberId)){
			return ResponseData.error("不能操作与自己无关的借条");
		}
		Status status = loanApplyDetail.getStatus();
		if(!status.equals(Status.pendingAgree)){
			logger.error("detail：{}的状态为：{},会员：{}不能取消操作",loanApplyDetail.getId(),status,member.getId());
			String statusName = getStatusName(status);
			return ResponseData.error("借条"+statusName+"，不能进行此操作");
		}
		LoanRole loanRole = loanApplyDetail.getLoanRole();
		if(loanRole.equals(LoanRole.loanee)) {
			loanApplyDetail.setStatus(Status.canceled);
			loanApplyDetailService.save(loanApplyDetail);
		}else {
			// 更新借条申请状态，退还借款人服务费
			if(!loanApplyDetail.getPayStatus().equals(PayStatus.success)) {
				return ResponseData.error("借条申请未支付，无法取消");
			}
			try {
				int code = loanApplyDetailService.loaneeCancelLoanApply(loanApplyDetail);
				if(code == Constant.UPDATE_FAILED) {
					logger.error("会员：{}补借条申请apply:{}失败",member.getId(),loanApply.getId());
					return ResponseData.error("申请状态异常，操作失败");
				}
				Long orgTrxId = 0L;
				MemberActTrx memberActTrx = new MemberActTrx();
				memberActTrx.setOrgId(loanApplyDetail.getId());
				memberActTrx.setTrxCode(TrxRuleConstant.GXT_CANCEL_LOAN_REFUND_LOANACT);
				List<MemberActTrx> list = memberActTrxService.findList(memberActTrx);
				if(!Collections3.isEmpty(list)) {
					orgTrxId = list.get(0).getId();
				}else {
					memberActTrx.setTrxCode(TrxRuleConstant.GXT_CANCEL_LOAN_REFUND_AVLACT);
					List<MemberActTrx> list1 = memberActTrxService.findList(memberActTrx);
					if(!Collections3.isEmpty(list1)) {
						orgTrxId = list1.get(0).getId();
					}else {
						memberActTrx.setTrxCode(TrxRuleConstant.GXT_CANCEL_LOAN_REFUND_WXPAY);
						List<MemberActTrx> list2 = memberActTrxService.findList(memberActTrx);
						if(!Collections3.isEmpty(list2)) {
							orgTrxId = list2.get(0).getId();
						}
					}
				}
				memberMessageService.sendMessage(MemberMessage.Type.returnFee,orgTrxId);
			} catch (Exception e) {
				logger.error("会员：{}取消补借条申请apply:{}异常：{}",member.getId(),loanApply.getId(),Exceptions.getStackTraceAsString(e));
				return ResponseData.error("账户更新异常，操作失败");
			}
		}
		
		Map<String,Object> result = new HashMap<String,Object>();
		result.put("loan_type", loanApplyDetail.getId()+"_1");
		return ResponseData.success("已取消补借条申请",result);
	}
	
	/**
	 * 	拒绝补借条申请
	 */
	@RequestMapping(value = "/refuseLoanApply")
	@ResponseBody
	public ResponseData refuseLoanApply(HttpServletRequest request) {
		Member member = memberService.getCurrent();
		if(member == null) {
			return ResponseData.error("请登录后再操作");
		}
		String loanIdStr = request.getParameter("loanId");
		if(StringUtils.isBlank(loanIdStr)) {
			logger.error("loanId：{}参数错误",loanIdStr);
			return ResponseData.error("参数错误");
		}
		NfsLoanApplyDetail loanApplyDetail = loanApplyDetailService.get(Long.valueOf(loanIdStr));
		if(!StringUtils.equals(member.getName(), loanApplyDetail.getMember().getName())) {
			return ResponseData.error("不能操作与自己无关的借条");
		}
		Status status = loanApplyDetail.getStatus();
		if(!status.equals(NfsLoanApplyDetail.Status.pendingAgree)) {
			logger.error("detail：{}的状态为{},会员{}不能拒绝操作",loanApplyDetail.getId(),status,member.getId());
			String statusName = getStatusName(status);
			return ResponseData.error("借条"+statusName+"，不能进行此操作");
		}
		loanApplyDetail.setMember(member);
		//更新借条申请状态，退还借款人服务费
		LoanRole loanRole = loanApplyDetail.getLoanRole();
		if(loanRole.equals(LoanRole.loanee)) {
			//借款人拒绝
			loanApplyDetail.setStatus(Status.reject);
			loanApplyDetailService.save(loanApplyDetail);
		} else {
			//放款人拒绝
			try {
				int code = loanApplyDetailService.loanerRefuseLoanApply(loanApplyDetail);
				if (code == Constant.UPDATE_FAILED) {
					logger.error("会员：{}拒绝补借条申请detail:{}失败", member.getId(), loanApplyDetail.getId());
					return ResponseData.error("拒绝申请失败");
				}
				Long orgTrxId = 0L;
				MemberActTrx memberActTrx = new MemberActTrx();
				memberActTrx.setOrgId(loanApplyDetail.getId());
				memberActTrx.setTrxCode(TrxRuleConstant.GXT_REFUSE_LOAN_REFUND_LOANACT);
				List<MemberActTrx> list = memberActTrxService.findList(memberActTrx);
				if(!Collections3.isEmpty(list)) {
					orgTrxId = list.get(0).getId();
				}else {
					memberActTrx.setTrxCode(TrxRuleConstant.GXT_REFUSE_LOAN_REFUND_AVLACT);
					List<MemberActTrx> list1 = memberActTrxService.findList(memberActTrx);
					if(!Collections3.isEmpty(list1)) {
						orgTrxId = list1.get(0).getId();
					}else {
						memberActTrx.setTrxCode(TrxRuleConstant.GXT_REFUSE_LOAN_REFUND_WXPAY);
						List<MemberActTrx> list2 = memberActTrxService.findList(memberActTrx);
						if(!Collections3.isEmpty(list2)) {
							orgTrxId = list2.get(0).getId();
						}
					}
				}
				//消息
				memberMessageService.sendMessage(MemberMessage.Type.returnFee,orgTrxId);
			
			} catch (Exception e) {
				logger.error("会员：{}拒绝申请detail:{}异常：{}", member.getId(), loanApplyDetail.getId(),
						Exceptions.getStackTraceAsString(e));
				return ResponseData.error("账户更新异常，操作失败");
			}
		}
		//消息
		if(loanRole.equals(LoanRole.loanee)) {
			memberMessageService.sendMessage(MemberMessage.Type.refuseApplicationLoanee,loanApplyDetail.getId());
		}else{
			memberMessageService.sendMessage(MemberMessage.Type.refuseApplicationLoaner,loanApplyDetail.getId());
		}
		
		Map<String, String> result = new HashMap<String, String>();
		result.put("loan_type", loanApplyDetail.getId()+"_1");
		return ResponseData.success("拒绝成功",result);
	}
	
	private String getStatusName(Status status) {
		String name = "";
		switch (status) {
		case pendingAgree:
			name = "待确认";
			break;
		case success:
			name = "已成功";
			break;
		case reject:
			name = "已拒绝";
			break;
		case canceled:
			name = "已取消";
			break;
		case expired:
			name = "已过期 ";
			break;
		default:
			name = "状态异常";
			break;
		}
		return name;
	}
	
}