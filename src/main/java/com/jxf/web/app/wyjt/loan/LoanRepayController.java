package com.jxf.web.app.wyjt.loan;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.jxf.loan.constant.LoanConstant;
import com.jxf.loan.constant.RecordMessage;
import com.jxf.loan.entity.NfsLoanApply;
import com.jxf.loan.entity.NfsLoanApply.LoanRole;
import com.jxf.loan.entity.NfsLoanApply.RepayType;
import com.jxf.loan.entity.NfsLoanApplyDetail;
import com.jxf.loan.entity.NfsLoanDetailMessage;
import com.jxf.loan.entity.NfsLoanOperatingRecord;
import com.jxf.loan.entity.NfsLoanPartialAndDelay;
import com.jxf.loan.entity.NfsLoanRecord;
import com.jxf.loan.entity.NfsLoanRecord.ArbitrationStatus;
import com.jxf.loan.entity.NfsLoanRecord.CollectionStatus;
import com.jxf.loan.entity.NfsLoanRecord.Status;
import com.jxf.loan.entity.NfsLoanRepayRecord;
import com.jxf.loan.service.NfsLoanApplyDetailService;
import com.jxf.loan.service.NfsLoanApplyService;
import com.jxf.loan.service.NfsLoanDetailMessageService;
import com.jxf.loan.service.NfsLoanPartialAndDelayService;
import com.jxf.loan.service.NfsLoanRecordService;
import com.jxf.loan.service.NfsLoanRepayRecordService;
import com.jxf.loan.utils.LoanUtils;
import com.jxf.mem.entity.Member;
import com.jxf.mem.entity.MemberMessage;
import com.jxf.mem.service.MemberActService;
import com.jxf.mem.service.MemberMessageService;
import com.jxf.mem.service.MemberService;
import com.jxf.mem.utils.H5Utils;
import com.jxf.mem.utils.VerifiedUtils;
import com.jxf.svc.config.Constant;
import com.jxf.svc.utils.CalendarUtil;
import com.jxf.svc.utils.DateUtils;
import com.jxf.svc.utils.Exceptions;
import com.jxf.svc.utils.StringUtils;
import com.jxf.web.app.BaseController;
import com.jxf.web.model.wyjt.app.loan.LoanRepayRequestParam;

/**
 * 还款相关
 * @author JINXINFU
 * @version 2.0
 */
@Controller("wyjtAppLoanRepayController")
@RequestMapping(value="${wyjtApp}/loan")
public class LoanRepayController extends BaseController {
	private static final Logger logger = LoggerFactory.getLogger(LoanRepayController.class);
	@Autowired
	private NfsLoanRecordService loanRecordService;
	@Autowired
	private MemberService memberService;
	@Autowired
	private NfsLoanRepayRecordService loanRepayRecordService;
	@Autowired
	private MemberActService memberActService;
	@Autowired
	private NfsLoanPartialAndDelayService nfsLoanPartialAndDelayService;
	@Autowired
	private NfsLoanApplyService applyService;
	@Autowired
	private NfsLoanApplyDetailService detailService;
	@Autowired
	private MemberMessageService memberMessageService;
	@Autowired
	private NfsLoanDetailMessageService nfsLoanDetailMessageService;
	
	/**
	 * 跳转到还款支付页面
	 * @return
	 */
	@RequestMapping("/toRepayPage")
	public ModelAndView toRepayPage(HttpServletRequest request) {
		String param = request.getParameter("param");
		LoanRepayRequestParam reqData = JSONObject.parseObject(param,LoanRepayRequestParam.class);
		String recordId = reqData.getRecordId();
		BigDecimal repayAmount = StringUtils.toDecimal(reqData.getRepayAmount());
		String repayType = reqData.getRepayType();
		String delayInterestStr = reqData.getDelayInterest();
		String nextRepayDate = reqData.getNextRepayDate();
		ModelAndView errMv = new ModelAndView("app/loanPay/payResult");
		if(StringUtils.isBlank(recordId)||StringUtils.isBlank(repayType)) {
			errMv.addObject("code", "-1");
			errMv.addObject("message", "参数错误");
			return errMv;
		}
		Member loanee = memberService.getCurrent();
		if(loanee == null) {
			errMv.addObject("code", "-1");
			errMv.addObject("message", "登录状态已过期，请重新登录");
			return errMv;
		}
		BigDecimal zero = BigDecimal.ZERO;
		NfsLoanRecord loanRecord = loanRecordService.get(Long.valueOf(recordId));
		NfsLoanApplyDetail detail = detailService.get(loanRecord.getLoanApplyDetail().getId());
		NfsLoanApply apply = applyService.get(detail.getApply().getId());
		BigDecimal avlBal = memberActService.getAvlBal(loanee);
		
		ModelAndView mv = null;//还款方式不同跳转到不同的支付页面
		
		if (RepayType.oneTimePrincipalAndInterest.equals(apply.getRepayType()) && StringUtils.equals(repayType, "0")) {
			//全额借条 全部还款
			mv = new ModelAndView("app/loanPay/allRepayIsOverdue");
			
			if (Status.overdue.equals(loanRecord.getStatus())) {
				int overdueDays = DateUtils.getDistanceOfTwoDate(new Date(), loanRecord.getDueRepayDate());
				BigDecimal overdueInterest = LoanUtils.calOverdueInterest(loanRecord.getDueRepayAmount(),new BigDecimal(overdueDays));
				loanRecord.setDueRepayAmount(loanRecord.getDueRepayAmount().add(overdueInterest));
				mv.addObject("isOverdue", true);
				mv.addObject("reminder", "逾期第二天起产生逾期利息");
				mv.addObject("overdueInterest", StringUtils.decimalToStr(overdueInterest, 2));
			}
			boolean isEnough = avlBal.compareTo(loanRecord.getDueRepayAmount()) >= 0 ? true:false;
			mv.addObject("isEnough", isEnough);
			
			//页面数值显示处理
			loanRecord.setDueRepayAmount(loanRecord.getDueRepayAmount().setScale(2, RoundingMode.HALF_UP));
			loanRecord.setAmount(loanRecord.getAmount().setScale(2, RoundingMode.HALF_UP));
			loanRecord.setInterest(loanRecord.getInterest().setScale(2, RoundingMode.HALF_UP));
			
		} else if (RepayType.oneTimePrincipalAndInterest.equals(apply.getRepayType()) && StringUtils.equals(repayType, "1")) {
			//全额借条 部分还款
			if (repayAmount.compareTo(loanRecord.getDueRepayAmount()) >= 0) {
				errMv.addObject("code", -1);
				errMv.addObject("message", "还款金额已超过待还金额，请选择全部还款方式！");
				return errMv;
			}
			mv = new ModelAndView("app/loanPay/partRepayNotOverdue");
			
			BigDecimal delayIntRate = zero;//延期利率
			BigDecimal remainderIntrest = zero;//剩余利息
			BigDecimal remainderAmt = zero;//剩余本金
			BigDecimal delayInterest = zero;//延期利息
			int delayTerm = 0;//延期天数
			
			if (loanRecord.getPartialStatus().equals(NfsLoanRecord.PartialStatus.loanerApplyPartial)) {
				//放款人发起的部分还款
				NfsLoanPartialAndDelay loanerPartialAndDelay = nfsLoanPartialAndDelayService.getLoanerPartialApplyForApp(loanRecord);
				if(loanerPartialAndDelay == null) {
					errMv.addObject("code", "-1");
					errMv.addObject("message", "没有找到部分还款申请记录，请联系客服处理！");
					return errMv;
				}
				delayInterest = loanerPartialAndDelay.getDelayInterest();
				delayIntRate = loanerPartialAndDelay.getDelayRate();
				repayAmount = loanerPartialAndDelay.getPartialAmount();
				nextRepayDate = DateUtils.getDateStr(loanerPartialAndDelay.getNowRepayDate(), "yyyy-MM-dd");
				delayTerm = loanerPartialAndDelay.getDelayDays();
				
				//是否是放款人主动发起部分还款申请 true:走借款人同意放款人部分还款请求分支 
				mv.addObject("hasApply", true);
				
			} else {
				//借款人主动申请部分还款 false:走借款人主动发起部分还款申请分支
				mv.addObject("hasApply", false);
				String dueRepayDate = DateUtils.getDateStr(loanRecord.getDueRepayDate(), "yyyy-MM-dd");
				delayTerm = CalendarUtil.getIntervalDays(dueRepayDate, nextRepayDate);
				if (StringUtils.isNotBlank(delayInterestStr)) {
					delayInterest = new BigDecimal(delayInterestStr);
					if (delayInterest.compareTo(zero) > 0) {
						BigDecimal remainderRepayAmount = loanRecord.getDueRepayAmount().subtract(repayAmount);
						delayIntRate = LoanUtils.getIntRate(remainderRepayAmount, delayTerm, delayInterest);
						if (delayIntRate.compareTo(new BigDecimal(0.24)) == 1) {
							errMv.addObject("code", -1);
							errMv.addObject("message", "延期利率已超过最高年化利率24%，请重新协商延期利息！");
							return errMv;
						}
					}
				}
			}
			BigDecimal nowIntrest = loanRecord.getInterest();
			remainderIntrest = (nowIntrest.compareTo(repayAmount) > 0 ? nowIntrest.subtract(repayAmount) : zero).add(delayInterest);
			remainderAmt = loanRecord.getAmount().subtract(repayAmount.subtract(loanRecord.getInterest()));
			
			mv.addObject("isEnough", avlBal.compareTo(repayAmount) >= 0 ? true:false);
			mv.addObject("repayAmount", StringUtils.decimalToStr(repayAmount, 2));
			mv.addObject("remainderAmt", StringUtils.decimalToStr(remainderAmt, 2));
			mv.addObject("delayInterest", StringUtils.decimalToStr(delayInterest, 2));
			mv.addObject("delayIntRate", StringUtils.decimalToStr(delayIntRate, 2));
			mv.addObject("remainderInt", StringUtils.decimalToStr(remainderIntrest, 2));
			mv.addObject("nextRepayDate", nextRepayDate);
			mv.addObject("delayTerm", delayTerm);
		} else {
			// 分期借条
			mv = new ModelAndView("app/loanPay/paymentList");

			NfsLoanRepayRecord paramRepayRecord = new NfsLoanRepayRecord();
			paramRepayRecord.setLoan(loanRecord);
			List<NfsLoanRepayRecord> allRepayRecords = loanRepayRecordService.findList(paramRepayRecord);
			BigDecimal overdueInterest = zero;

			List<NfsLoanRepayRecord> doneRecords = new ArrayList<NfsLoanRepayRecord>(36);
			List<NfsLoanRepayRecord> overdueRecords = new ArrayList<NfsLoanRepayRecord>(36);
			List<NfsLoanRepayRecord> pendingRecords = new ArrayList<NfsLoanRepayRecord>(36);

			for (NfsLoanRepayRecord loanRepayRecord : allRepayRecords) {
				String expectDate = DateUtils.getDateStr(loanRepayRecord.getExpectRepayDate(), "yyyy-MM-dd");
				String actualDate = DateUtils.getDateStr(new Date(), "yyyy-MM-dd");
				if (actualDate.compareTo(expectDate) > 0
						&& !NfsLoanRepayRecord.Status.done.equals(loanRepayRecord.getStatus())) {
					overdueRecords.add(loanRepayRecord);
					BigDecimal overdueDays = new BigDecimal(CalendarUtil.getIntervalDays(expectDate, actualDate));
					BigDecimal currentOverdueInt = LoanUtils.calOverdueInterest(loanRepayRecord.getExpectRepayAmt(),
							overdueDays);
					overdueInterest = overdueInterest.add(currentOverdueInt);
				} else if (actualDate.compareTo(expectDate) <= 0
						&& NfsLoanRepayRecord.Status.pending.equals(loanRepayRecord.getStatus())) {
					pendingRecords.add(loanRepayRecord);
				} else {
					doneRecords.add(loanRepayRecord);
				}
			}
			if(loanRecord.getId().longValue() == 7482970L) {
				overdueInterest = overdueInterest.add(new BigDecimal(61.4));
			}else if(loanRecord.getId().longValue() == 7481485L) {
				overdueInterest = overdueInterest.add(new BigDecimal(58.82));
			}else if(loanRecord.getId().longValue() == 7493035L) {
				overdueInterest = overdueInterest.add(new BigDecimal(54.72));
			}
			if (overdueInterest.compareTo(zero) == 0) {
				mv.addObject("hasOverdue", false);
			} else {
				mv.addObject("hasOverdue", true);
				mv.addObject("overdueInterest", StringUtils.decimalToStr(overdueInterest, 2));
			}
			boolean isArbitration = loanRecord.getArbitrationStatus().equals(ArbitrationStatus.doing)
					|| loanRecord.getCollectionStatus().equals(CollectionStatus.doing) ? true : false;
			mv.addObject("isArbitration", isArbitration);
			mv.addObject("doneRecords", doneRecords);
			mv.addObject("overdueRecords", overdueRecords);
			mv.addObject("pendingRecords", pendingRecords);
		}
		mv.addObject("avlBal", StringUtils.decimalToStr(avlBal, 2));
		mv.addObject("loan", loanRecord);
		mv.addObject("isPayPsw", VerifiedUtils.isVerified(loanee.getVerifiedList(), 22));
		mv.addObject("memberToken", request.getHeader("x-memberToken"));
		mv = H5Utils.addPlatform(loanee, mv);
		return mv;
	}
	
	/**
	 * 全部还款
	 * @return
	 */
	@RequestMapping("/repayAll")
	public ModelAndView repayAll(HttpServletRequest request) {
		String loanId = request.getParameter("loanId");
		Member loanee = memberService.getCurrent();
		ModelAndView mv = new ModelAndView("app/loanPay/payResult");
		if(loanee == null) {
			mv.addObject("code", "-1");
			mv.addObject("message", "登录状态已过期，请重新登录");
			return mv;
		}
		
		mv =H5Utils.addPlatform(loanee,mv);

		NfsLoanRecord loanRecord = loanRecordService.get(Long.valueOf(loanId));
		if(NfsLoanRecord.Status.repayed.equals(loanRecord.getStatus())) {
			mv.addObject("code", "-1");
			mv.addObject("message", "借条已还清！");
			return mv;
		}
		
		BigDecimal shouldRepayAmount = loanRecord.getDueRepayAmount();
		BigDecimal overdueInterest = BigDecimal.ZERO;
		
		//计算逾期利息
		if(loanRecord.getStatus().equals(NfsLoanRecord.Status.overdue)) {
			int overdueDays = DateUtils.getDistanceOfTwoDate(new Date(), loanRecord.getDueRepayDate());
			overdueInterest = LoanUtils.calOverdueInterest(loanRecord.getDueRepayAmount(),new BigDecimal(overdueDays));
			loanRecord.setOverdueInterest(overdueInterest);
			shouldRepayAmount = shouldRepayAmount.add(overdueInterest);
		}
		BigDecimal avlBal = memberService.getAvlBal(loanee);
		if(shouldRepayAmount.compareTo(avlBal) > 0) {
			mv.addObject("code", "-1");
			mv.addObject("message", "您的账户余额不足，请先充值再进行此操作！");
			return mv;
		}
		loanRecord.setLoanee(loanee);
		int code = Constant.UPDATE_FAILED;
		try {
			code = loanRepayRecordService.repayAll(loanRecord, loanRecord.getDueRepayAmount(),overdueInterest);
		} catch (Exception e) {
			code = Constant.UPDATE_FAILED;
			logger.error("借款单Id:{}全部还款失败！异常：{}",loanRecord.getId(),Exceptions.getStackTraceAsString(e));
		}
		if(code == Constant.UPDATE_SUCCESS) {
			mv.addObject("loanId", loanRecord.getId());
			mv.addObject("type", LoanConstant.TYPE_RECORD);
			mv.addObject("amount", shouldRepayAmount);
			mv.addObject("code", "0");
			mv.addObject("message", "全部还款成功");
		}else {
			mv.addObject("code", "-1");
			mv.addObject("message", "还款失败！");
		}
		return mv;
	}
	
	/**
	 *  借款人主动部分还款支付
	 * @param request
	 * @return
	 */
	@RequestMapping("/partialRepay")
	public ModelAndView partialRepay(HttpServletRequest request) {
		String repayAmountStr = request.getParameter("repayAmount");
		String loanId = request.getParameter("loanId");
		String nextRepayDateStr = request.getParameter("nextRepayDate");
		String delayTermStr = request.getParameter("delayTerm");
		String intRateStr = request.getParameter("delayIntRate");
		String delayInterestStr = request.getParameter("delayInterest");
		ModelAndView mv = new ModelAndView("app/loanPay/payResult");
		if(repayAmountStr == null ||repayAmountStr.trim().length() == 0 || loanId == null || loanId.trim().length() == 0) {
			mv.addObject("code", "-1");
			mv.addObject("message", "参数错误");
			return mv;
		}
		Member loanee = memberService.getCurrent();
		if(loanee == null) {
			mv.addObject("code", "-1");
			mv.addObject("message", "登录状态已过期，请重新登录");
			return mv;
		}
		
		mv =H5Utils.addPlatform(loanee,mv);
		
		NfsLoanRecord loanRecord = loanRecordService.get(Long.valueOf(loanId));
		if(loanRecord == null || loanRecord.getLoanee().getId().longValue() != loanee.getId().longValue()) {
			mv.addObject("code", "-1");
			mv.addObject("message", "您没有权限操作此借条");
			return mv;
		}
		if(loanRecord.getStatus().equals(NfsLoanRecord.Status.repayed)) {
			mv.addObject("code", "-1");
			mv.addObject("message", "借条已还清，请勿重复操作！");
			return mv;
		}
		if(NfsLoanRecord.AuctionStatus.auction.equals(loanRecord.getAuctionStatus())) {
			mv.addObject("code", "-1");
			mv.addObject("message", "借条处于转让中，不能进行还款操作！");
			return mv;
		}
		if(NfsLoanRecord.AuctionStatus.auctioned.equals(loanRecord.getAuctionStatus())) {
			mv.addObject("code", "-1");
			mv.addObject("message", "借条已转让，不能进行部分还款操作！");
			return mv;
		}
		BigDecimal avlBal = memberService.getAvlBal(loanee);
		BigDecimal repayAmount = new BigDecimal(repayAmountStr);
		if(repayAmount.compareTo(avlBal) > 0) {
			mv.addObject("code", "-1");
			mv.addObject("message", "您的账户余额不足，请先充值再进行此操作！");
			return mv;
		}
		Date nextRepayDate = null;
		try {
			if(StringUtils.isNotBlank(nextRepayDateStr)) {
				nextRepayDate = DateUtils.parseDate(nextRepayDateStr, "yyyy-MM-dd");
			}
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(e));
			mv.addObject("code", "-1");
			mv.addObject("message", "还款日期错误");
			return mv;
		}

		Integer delayTerm = StringUtils.toInteger(delayTermStr);
		BigDecimal intRate = StringUtils.toDecimal(intRateStr);
		BigDecimal delayInterest = StringUtils.toDecimal(delayInterestStr);
		
		NfsLoanPartialAndDelay partialRepayApply = new NfsLoanPartialAndDelay();
		partialRepayApply.setLoan(loanRecord);
		partialRepayApply.setMember(loanee);
		partialRepayApply.setMemberRole(NfsLoanApply.LoanRole.loanee);
		partialRepayApply.setPartialAmount(repayAmount);
		partialRepayApply.setStatus(NfsLoanPartialAndDelay.Status.confirm);
		partialRepayApply.setPayStatus(NfsLoanPartialAndDelay.PayStatus.noPay);
		partialRepayApply.setType(NfsLoanPartialAndDelay.Type.partial);
		partialRepayApply.setOldAmount(loanRecord.getAmount());
		partialRepayApply.setOldInterest(loanRecord.getInterest());
		partialRepayApply.setOldRate(loanRecord.getIntRate());
		partialRepayApply.setDelayRate(intRate);
		partialRepayApply.setDelayInterest(delayInterest);
		partialRepayApply.setDelayDays(delayTerm);
		BigDecimal remainAmount = loanRecord.getDueRepayAmount().subtract(repayAmount);
		partialRepayApply.setRemainAmount(remainAmount);
		BigDecimal nowInterest = loanRecord.getInterest().compareTo(repayAmount) > 0? loanRecord.getInterest().subtract(repayAmount): BigDecimal.ZERO;
		partialRepayApply.setNowInterest(nowInterest);
		partialRepayApply.setNowRepayDate(loanRecord.getDueRepayDate());
		if(delayTerm.intValue() > 0) {
			partialRepayApply.setNowInterest(delayInterest);
			partialRepayApply.setNowRepayDate(nextRepayDate);
			partialRepayApply.setType(NfsLoanPartialAndDelay.Type.partialAndDelay);
			partialRepayApply.setRemainAmount(remainAmount);
			loanRecord.setDelayStatus(NfsLoanRecord.DelayStatus.loaneeApplyDelay);
		}
		loanRecord.setPartialStatus(NfsLoanRecord.PartialStatus.loaneeApplyPartial);
		loanRecord.setLoanee(loanee);
		int respCode = Constant.UPDATE_FAILED;
		try {
			respCode = loanRepayRecordService.loaneeApplyPartialRepay(loanRecord,partialRepayApply);
		} catch (Exception e) {
			respCode = Constant.UPDATE_FAILED;
			logger.error("借款单Id: " + loanRecord.getId() + "账户更新失败，操作失败！");
		}
		
		if(respCode == Constant.UPDATE_SUCCESS) {
			mv.addObject("loanId", loanRecord.getId());
			mv.addObject("type", LoanConstant.TYPE_RECORD);
			mv.addObject("amount", repayAmount);
			mv.addObject("code", "0");
			mv.addObject("message", "部分还款申请成功");
		}else {
			mv.addObject("code", "-1");
			mv.addObject("message", "账户更新失败，部分还款申请失败");
		}
		return mv;
	}
	/**
	 *   借款人同意放款人的部分还款申请
	 * @return
	 */
	@RequestMapping("/agreePartialRepay")
	public ModelAndView agreePartialRepay(HttpServletRequest request) {
		String loanId = request.getParameter("loanId");
		ModelAndView mv = new ModelAndView("app/loanPay/payResult");
		if(loanId == null || loanId.trim().length() == 0) {
			mv.addObject("code", "-1");
			mv.addObject("message", "参数错误");
			return mv;
		}
		Member loanee = memberService.getCurrent();
		if(loanee == null) {
			mv.addObject("code", "-1");
			mv.addObject("message", "登录状态已过期，请重新登录");
			return mv;
		}
		
		mv =H5Utils.addPlatform(loanee,mv);
		
		NfsLoanRecord loanRecord = loanRecordService.get(Long.valueOf(loanId));
		NfsLoanPartialAndDelay param = new NfsLoanPartialAndDelay();
		param.setMember(loanRecord.getLoaner());
		param.setMemberRole(LoanRole.loaner);
		param.setStatus(NfsLoanPartialAndDelay.Status.confirm);
		param.setLoan(loanRecord);
		List<NfsLoanPartialAndDelay> list = nfsLoanPartialAndDelayService.findList(param);
		NfsLoanPartialAndDelay partialAndDelayApply = list.get(0);
		BigDecimal avlBal = memberActService.getAvlBal(loanee);
		BigDecimal repayAmount = partialAndDelayApply.getPartialAmount();
		if(!partialAndDelayApply.getStatus().equals(NfsLoanPartialAndDelay.Status.confirm)) {
			logger.error("借条{}的部分还款申请{}状态已更新，请勿重复操作！",loanRecord.getId(),partialAndDelayApply.getId());
			mv.addObject("code", -1);
			mv.addObject("message", "该部分还款/延期申请状态已更新，请勿重复操作！");
			return mv;
		}
		
		//部分还款申请时间如果在到期日之前
		if(DateUtils.getDateStr(partialAndDelayApply.getNowRepayDate(), "yyyy-MM-dd").compareTo(DateUtils.getDate()) < 0) {
			mv.addObject("code", -1);
			mv.addObject("message", "该部分还款/延期申请已过期，操作失败！");
			return mv;
		}
		if(NfsLoanRecord.AuctionStatus.auction.equals(loanRecord.getAuctionStatus())) {
			mv.addObject("code", "-1");
			mv.addObject("message", "借条处于转让中，不能进行还款操作！");
			return mv;
		}
		if(NfsLoanRecord.AuctionStatus.auctioned.equals(loanRecord.getAuctionStatus())) {
			mv.addObject("code", "-1");
			mv.addObject("message", "借条已转让，不能进行部分还款操作！");
			return mv;
		}
		if(repayAmount.compareTo(avlBal) > 0) {
			mv.addObject("code", "-1");
			mv.addObject("message", "您的账户余额不足，请先充值再进行此操作！");
			return mv;
		}
		Member loaner = loanRecord.getLoaner();
		loaner = memberService.get(loaner.getId());
		
		//保存操作记录---部分还款加延期
		NfsLoanOperatingRecord operatingRecord = new NfsLoanOperatingRecord();
		NfsLoanRecord oldRecord = new NfsLoanRecord();
		oldRecord.setId(loanRecord.getId());
		oldRecord.setLoaner(loanRecord.getLoaner());
		oldRecord.setLoanee(loanRecord.getLoanee());
		oldRecord.setAmount(loanRecord.getAmount());
		oldRecord.setInterest(loanRecord.getInterest());
		oldRecord.setOverdueInterest(loanRecord.getOverdueInterest());
		operatingRecord.setOldRecord(oldRecord);
		boolean resultCode = false;
		loanRecord.setLoanee(loanee);
		loanRecord.setLoaner(loaner);
		try {
			resultCode = loanRepayRecordService.agreePartialRepayAndDelay(loanRecord, partialAndDelayApply,operatingRecord);	
			if(resultCode) {
				mv.addObject("loanId", loanRecord.getId());
				mv.addObject("type", LoanConstant.TYPE_RECORD);
				mv.addObject("amount", repayAmount);
				mv.addObject("code", 0);
				mv.addObject("message", "部分还款成功");
			}else {
				mv.addObject("code", -1);
				mv.addObject("message", "账户更新失败，还款失败");
			}
	 		return mv;
		} catch (Exception e) {
			logger.error("借款单Id: " + loanRecord.getId() + "账户更新失败，还款失败!");
			resultCode =false;
			mv.addObject("code", -1);
			mv.addObject("message", "账户更新失败，还款失败");
			return mv;
		}
	}
	/**
	 * 分期还款
	 */
	@RequestMapping("/stagesRepay")
	public ModelAndView stagesRepay(HttpServletRequest request) {
		String loanIdStr = request.getParameter("loanId");
		String checkedNumStr = request.getParameter("allNum");//选择的未还状态的还款记录数
		
		Member loanee = memberService.getCurrent();
		
		ModelAndView mv = new ModelAndView("app/loanPay/payResult");
		if(loanee == null) {
			mv.addObject("code", -1);
			mv.addObject("message", "登录状态已过期，请重新登录！");
			return mv;
		}
		mv =H5Utils.addPlatform(loanee,mv);
		NfsLoanRecord loanRecord = loanRecordService.get(Long.valueOf(loanIdStr));
		if(NfsLoanRecord.Status.repayed.equals(loanRecord.getStatus())) {
			mv.addObject("code", -1);
			mv.addObject("message", "借条已还清，请勿重复操作！");
			return mv;
		}
		//逾期记录
		List<NfsLoanRepayRecord> overdueRepayRecordList = new ArrayList<NfsLoanRepayRecord>(36);
		NfsLoanRepayRecord nfsLoanRepayRecord = new NfsLoanRepayRecord();
		nfsLoanRepayRecord.setLoan(loanRecord);
		List<NfsLoanRepayRecord> allLoanRepayRecords = loanRepayRecordService.findList(nfsLoanRepayRecord);
		for (NfsLoanRepayRecord repayRecord : allLoanRepayRecords) {
			String expectDate =DateUtils.getDateStr(repayRecord.getExpectRepayDate(), "yyyy-MM-dd");
			String actualDate = DateUtils.getDateStr(new Date(), "yyyy-MM-dd");
			if(actualDate.compareTo(expectDate) > 0 && !NfsLoanRepayRecord.Status.done.equals(repayRecord.getStatus())) {
				overdueRepayRecordList.add(repayRecord);
			}
		}
		//未还记录
		List<NfsLoanRepayRecord> pendingRepayRecordList = new ArrayList<NfsLoanRepayRecord>(36);
		NfsLoanRepayRecord pendingRecord = new NfsLoanRepayRecord();
		pendingRecord.setStatus(NfsLoanRepayRecord.Status.pending);
		pendingRecord.setLoan(loanRecord);
		pendingRepayRecordList = loanRepayRecordService.findList(pendingRecord);
		
		int overdueNums =  overdueRepayRecordList.size();
		int checkedNums = Integer.valueOf(checkedNumStr);
		if (loanRecord.getArbitrationStatus().equals(ArbitrationStatus.doing)
				|| loanRecord.getCollectionStatus().equals(CollectionStatus.doing)) {
			checkedNums = pendingRepayRecordList.size();
		}
		//本次还的还款记录总数
		int repaySize = checkedNums + overdueNums;
		if(repaySize == 0) {
			//用户要还的记录数为0，不能操作还款
			mv.addObject("code", -1);
			mv.addObject("message", "请选择要还的期数！");
			return mv;
		}
		
		//本次操作应还款金额
		BigDecimal dueRepayAmount = BigDecimal.ZERO;
		//当前逾期利息
		BigDecimal overdueInterest = BigDecimal.ZERO;
		//本次还款应还的还款记录
		List<NfsLoanRepayRecord> shouldRepayRecords = new ArrayList<NfsLoanRepayRecord>(36);
		for (NfsLoanRepayRecord overdueLoanRepayRecord : overdueRepayRecordList) {
			String expectDate =DateUtils.getDateStr(overdueLoanRepayRecord.getExpectRepayDate(), "yyyy-MM-dd");
			String actualDate = DateUtils.getDateStr(new Date(), "yyyy-MM-dd");
			BigDecimal delayDays = new BigDecimal(CalendarUtil.getIntervalDays(expectDate, actualDate));
			BigDecimal currentOverdueInt = LoanUtils.calOverdueInterest(overdueLoanRepayRecord.getExpectRepayAmt(), delayDays);
			overdueInterest = overdueInterest.add(currentOverdueInt);
			dueRepayAmount = dueRepayAmount.add(overdueLoanRepayRecord.getExpectRepayAmt());
			shouldRepayRecords.add(overdueLoanRepayRecord);
		}
		if (pendingRepayRecordList.size() > 0) {
			for (int i = 0; i < checkedNums; i++) {
				dueRepayAmount = dueRepayAmount.add(pendingRepayRecordList.get(i).getExpectRepayAmt());
				shouldRepayRecords.add(pendingRepayRecordList.get(i));
			}
		}
		if(dueRepayAmount.compareTo(BigDecimal.ZERO) == 0) {
			logger.error("分期借条{}还款操作金额为0");
			mv.addObject("code", -1);
			mv.addObject("message", "请选择要还的期数！");
			return mv;
		}
		if(shouldRepayRecords.size() == 0) {
			logger.error("分期借条{}还款操作没有获取到应还的还款记录",loanRecord.getId());
			mv.addObject("code", -1);
			mv.addObject("message", "借条分期计划异常，请联系客服处理！");
			return mv;
		}
		Member loaner = memberService.get(loanRecord.getLoaner());
		BigDecimal avlBal = memberService.getAvlBal(loanee);
		if(loanRecord.getId().longValue() == 7482970L) {
			overdueInterest = overdueInterest.add(new BigDecimal(61.4));
		}else if(loanRecord.getId().longValue() == 7481485L) {
			overdueInterest = overdueInterest.add(new BigDecimal(58.82));
		}else if(loanRecord.getId().longValue() == 7493035L) {
			overdueInterest = overdueInterest.add(new BigDecimal(54.72));
		}
		//当前操作还款金额
		BigDecimal repayAmount = dueRepayAmount.add(overdueInterest);
		if(avlBal.compareTo(repayAmount) < 0) {
			mv.addObject("code", -1);
			mv.addObject("message", "账户余额不足，请先充值再操作！");
			return mv;
		}
		logger.info("分期借条{}还款当前逾期{}期，逾期利息：{}本次还款选择待还状态{}期,本次还款金额共{}",loanRecord.getId(),overdueNums
				,StringUtils.decimalToStr(overdueInterest, 2),checkedNums,StringUtils.decimalToStr(repayAmount, 2));
		loanRecord.setLoanee(loanee);
		loanRecord.setLoaner(loaner);
		int result = Constant.UPDATE_FAILED;
		try {
			result = loanRepayRecordService.stagesRepay(repayAmount,overdueInterest,loanRecord,shouldRepayRecords);
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(e));
			mv.addObject("code", -1);
			mv.addObject("message", "账户更新异常，还款失败");
			return mv;
		}
		if(result == Constant.UPDATE_SUCCESS) {
			mv.addObject("loanId", loanRecord.getId());
			mv.addObject("type", LoanConstant.TYPE_RECORD);
			mv.addObject("amount", dueRepayAmount);
			mv.addObject("code",0);
			mv.addObject("message", "还款成功！");
		}else{
			mv.addObject("code", -1);
			mv.addObject("message", "账户更新失败，还款失败");
		}
		return mv;
	}
	/**
	 * 线下还款
	 * @return
	 */
	@RequestMapping(value = "repayDownLine")
	public ModelAndView repayDownLine(HttpServletRequest request) {
		ModelAndView mv = new ModelAndView("app/loanPay/payResult");
		String loanId = request.getParameter("loanId");
		Member loanee = memberService.getCurrent();
		if(loanee == null ) {
			mv.addObject("code", -1);
			mv.addObject("message", "登录状态已过期，请重新登录！");
			return mv;
		}
		mv = H5Utils.addPlatform(loanee, mv);
		NfsLoanRecord record = loanRecordService.get(Long.valueOf(loanId));
		if(NfsLoanRecord.Status.repayed.equals(record.getStatus())) {
			mv.addObject("code", -1);
			mv.addObject("message", "借条状态已改变，操作失败！");
			return mv;
		}
		if(NfsLoanRecord.AuctionStatus.auction.equals(record.getAuctionStatus())) {
			mv.addObject("code", "-1");
			mv.addObject("message", "借条处于转让中，请选择在线全部还款！");
			return mv;
		}
		if(NfsLoanRecord.AuctionStatus.auctioned.equals(record.getAuctionStatus())) {
			mv.addObject("code", "-1");
			mv.addObject("message", "借条已转让，请选择在线全部还款！");
			return mv;
		}
		record.setLineDownStatus(NfsLoanRecord.LineDownStatus.loaneeLineDownRepayment);
		loanRecordService.save(record);
		
		//发送会员消息
		memberMessageService.sendMessage(MemberMessage.Type.lineDownRepayment,record.getId());
		
		//生成对话
		NfsLoanDetailMessage nfsLoanDetailMessage = new NfsLoanDetailMessage();
		nfsLoanDetailMessage.setDetail(record.getLoanApplyDetail());
		nfsLoanDetailMessage.setMember(loanee);
		nfsLoanDetailMessage.setMessageId(RecordMessage.CHAT_2101);
		nfsLoanDetailMessage.setType(RecordMessage.BORROWER_LINEDOWN_REPAYMENT);
		nfsLoanDetailMessageService.save(nfsLoanDetailMessage);
		
		mv.addObject("loanId", record.getId());
		mv.addObject("type", LoanConstant.TYPE_RECORD);
		mv.addObject("amount", BigDecimal.ZERO);
		mv.addObject("code", 0);
		mv.addObject("message", "线下还款已成功，请等待放款人确认收款！");
		return mv;
	}
	
}