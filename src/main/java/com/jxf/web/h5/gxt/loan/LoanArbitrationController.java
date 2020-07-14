package com.jxf.web.h5.gxt.loan;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jxf.loan.constant.LoanConstant;
import com.jxf.loan.entity.NfsLoanApply.TrxType;
import com.jxf.loan.entity.NfsLoanArbitration;
import com.jxf.loan.entity.NfsLoanArbitration.StrongStatus;
import com.jxf.loan.entity.NfsLoanArbitrationDetail;
import com.jxf.loan.entity.NfsLoanArbitrationExecution;
import com.jxf.loan.entity.NfsLoanRecord;
import com.jxf.loan.entity.NfsLoanRecord.AuctionStatus;
import com.jxf.loan.entity.NfsLoanRecord.Status;
import com.jxf.loan.service.NfsLoanArbitrationDetailService;
import com.jxf.loan.service.NfsLoanArbitrationExecutionService;
import com.jxf.loan.service.NfsLoanArbitrationService;
import com.jxf.loan.service.NfsLoanRecordService;
import com.jxf.loan.utils.LoanUtils;
import com.jxf.mem.entity.Member;
import com.jxf.mem.entity.MemberActTrx;
import com.jxf.mem.service.MemberActTrxService;
import com.jxf.mem.service.MemberService;
import com.jxf.mem.utils.VerifiedUtils;
import com.jxf.svc.persistence.Page;
import com.jxf.svc.utils.DateUtils;
import com.jxf.svc.utils.IdCardUtils;
import com.jxf.svc.utils.StringUtils;
import com.jxf.web.model.ResponseData;
import com.jxf.web.model.gxt.ArbitrationListResponseResult;
import com.jxf.web.model.gxt.ArbitrationListResponseResult.Arbitration;
import com.jxf.wx.user.entity.WxUserInfo;
import com.jxf.wx.user.service.WxUserInfoService;

/**
 * 仲裁
 * @author Richard.Su
 *
 */
@Controller("gxtH5LoanArbitrationController")
@RequestMapping(value="${gxtH5}/arbitration")
public class LoanArbitrationController {
	private static final Logger logger = LoggerFactory.getLogger(LoanArbitrationController.class);

	@Autowired
	private MemberService memberService;
	@Autowired
	private MemberActTrxService memberActTrxService;
	@Autowired
	private NfsLoanRecordService loanRecordService;
	@Autowired
	private NfsLoanArbitrationService loanArbitrationService;
	@Autowired
	private NfsLoanArbitrationDetailService loanArbitrationDetailService;
	@Autowired
	private WxUserInfoService wxUserInfoService;
	@Autowired
	private NfsLoanArbitrationExecutionService arbitrationExecutionService;
	
	

	@RequestMapping(value = "/goToArbitrationApplyPage")
	@ResponseBody
	public ResponseData goToArbitrationApplyPage(HttpServletRequest request) {
		Member member = memberService.getCurrent();
		String loanId = request.getParameter("loanId");
		if (StringUtils.isBlank(loanId)) {
			logger.error("仲裁申请页面跳转请求参数错误：loanId:{}", loanId);
			return ResponseData.error("参数错误");
		}
		NfsLoanRecord loanRecord = loanRecordService.get(Long.valueOf(loanId));
		if (loanRecord == null) {
			logger.error("仲裁申请页面跳转请求参数错误：loanId:{},找不到对应的借条", loanId);
			return ResponseData.error("参数错误");
		}
		if(!loanRecord.getLoaner().getId().equals(member.getId())) {
			logger.error("token用户和借条用户不匹配！");
			return ResponseData.error("不能操作不属于自己的借条");
		}
		// 申请条件校验
		if (loanRecord.getArbitrationStatus().equals(NfsLoanRecord.ArbitrationStatus.doing)) {
			return ResponseData.error("此借款单已申请过仲裁，无法再次申请");
		}
		if (loanRecord.getCollectionStatus().equals(NfsLoanRecord.CollectionStatus.doing)) {
			return ResponseData.error("该借条正在催收中");
		}
		if (loanRecord.getDueRepayAmount().compareTo(new BigDecimal(100)) < 0) {
			return ResponseData.error("争议金额低于100元无法申请该服务");
		}
		Date loanStart = loanRecord.getLoanStart();
		int age = IdCardUtils.getAge(loanRecord.getLoanee().getIdNo(), loanStart);
		if (age < 23) {
			return ResponseData.error("借款人借款时年龄不满23岁不能申请仲裁");
		}
		if (!loanRecord.getAuctionStatus().equals(AuctionStatus.initial)) {
			return ResponseData.error("转让中/转让成功的借条暂不支持申请仲裁!");
		}
		NfsLoanArbitration loanArbitration = loanArbitrationService.getByLoanId(Long.valueOf(loanId));
		
		BigDecimal disAmount = loanRecord.getDueRepayAmount();// 争议金额
		Map<String, Object> data = new HashMap<String, Object>();
		int overdueDays = DateUtils.getDistanceOfTwoDate(loanRecord.getDueRepayDate(), new Date());
		BigDecimal overdueIntrest = LoanUtils.calOverdueInterest(disAmount, new BigDecimal(overdueDays));
		// 计算仲裁服务费
		BigDecimal applyAmount = disAmount.add(overdueIntrest);
		BigDecimal applyfee = calArbitrationApplyFee(applyAmount);

		if(loanArbitration != null && loanArbitration.getStatus().equals(NfsLoanArbitration.Status.waitingPay)) {
			data.put("images", loanArbitration.getImages());
			data.put("arbitrationId", loanArbitration.getId());
		}
		
		// 借条信息
		data.put("fee", StringUtils.decimalToStr(applyfee, 2));
		data.put("applyAmount", StringUtils.decimalToStr(applyAmount, 2));
		data.put("loaneeName", loanRecord.getLoanee().getName());
		data.put("amount", loanRecord.getAmount());
		data.put("intrest", loanRecord.getInterest());
		data.put("overdueIntrest", StringUtils.decimalToStr(overdueIntrest, 2));
		data.put("overdueDays", overdueDays);
			
		// 获取当前用户信息
		Integer verifiedList = member.getVerifiedList();
		data.put("isRealIdentity",
				VerifiedUtils.isVerified(verifiedList, 1) && VerifiedUtils.isVerified(verifiedList, 2));
		data.put("isPayPsw", VerifiedUtils.isVerified(verifiedList, 22));
		data.put("curBal", StringUtils.decimalToStr(memberService.getAvlBal(member), 2));
		data.put("name", member.getName());
		data.put("username", member.getUsername());
		WxUserInfo wxUserInfo = wxUserInfoService.findByMember(member.getId(), "gxt");
		data.put("headImage", wxUserInfo == null ? "" : wxUserInfo.getHeadImage());
		return ResponseData.success("请求成功", data);
	}

	/**
	 * 申请仲裁页面跳转
	 * 
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "apply")
	@ResponseBody
	public ResponseData apply(HttpServletRequest request, Model model) {
		Member member = memberService.getCurrent();
		String loanId = request.getParameter("loanId");
		String images = request.getParameter("images");
		String pwd = request.getParameter("pwd");
		ResponseData checkPayPwd = memberService.checkPayPwd(pwd, member);
		if(checkPayPwd == null) {
			return ResponseData.error("系统错误，请联系客服处理！");
		}
		if (checkPayPwd.getCode() != 0) {
			return ResponseData.error(checkPayPwd.getMessage());
		}
		Map<String, Object> data = new HashMap<String, Object>();
		NfsLoanRecord loanRecord = loanRecordService.get(Long.parseLong(loanId));
		
		if(!loanRecord.getLoaner().getId().equals(member.getId())) {
			logger.error("token用户和借条用户不匹配！");
			return ResponseData.error("不能操作不属于自己的借条");
		}
		// 申请条件校验
		if (loanRecord.getArbitrationStatus().equals(NfsLoanRecord.ArbitrationStatus.doing)) {
			return ResponseData.error("此借款单已申请过仲裁，无法再次申请");
		}
		if (loanRecord.getCollectionStatus().equals(NfsLoanRecord.CollectionStatus.doing)) {
			return ResponseData.error("该借条正在催收中");
		}
		BigDecimal disAmount = loanRecord.getDueRepayAmount();// 争议金额
		if (disAmount.compareTo(new BigDecimal(100)) < 0) {
			return ResponseData.error("争议金额低于100元无法申请该服务");
		}
		Date loanStart = loanRecord.getLoanStart();
		int age = IdCardUtils.getAge(loanRecord.getLoanee().getIdNo(), loanStart);
		if (age < 23) {
			return ResponseData.error("借款人借款时年龄不满23岁不能申请仲裁");
		}
		NfsLoanArbitration loanArbitration = loanArbitrationService.getByLoanId(Long.valueOf(loanId));
		if(loanArbitration != null && loanArbitration.getStatus().equals(NfsLoanArbitration.Status.waitingPay)) {
			loanArbitration.setImages(images);
			loanArbitrationService.save(loanArbitration);
			data.put("arbitrationId", loanArbitration.getId() + "");
			return ResponseData.success("请求成功",data);
		}
		if(loanArbitration != null && !loanArbitration.getStatus().equals(NfsLoanArbitration.Status.waitingPay)) {
			return ResponseData.error("该借条已申请仲裁,不能进行此操作");
		}
		BigDecimal overdueDays = new BigDecimal(DateUtils.getDistanceOfTwoDate(loanRecord.getDueRepayDate(), new Date()));
		BigDecimal overdueIntrest = LoanUtils.calOverdueInterest(disAmount, overdueDays);
		BigDecimal applyAmount = disAmount.add(overdueIntrest);//申请仲裁金额
		// 计算仲裁服务费
		BigDecimal applyfee = calArbitrationApplyFee(applyAmount);

		NfsLoanArbitration arbitration = new NfsLoanArbitration();
		arbitration.setApplyAmount(applyAmount);
		arbitration.setFee(StringUtils.decimalToStr(applyfee, 2));
		arbitration.setLoan(loanRecord);
		arbitration.setMember(member);
		arbitration.setStatus(NfsLoanArbitration.Status.waitingPay);
		arbitration.setChannel(NfsLoanArbitration.Channel.zhangsan);
		arbitration.setStrongStatus(NfsLoanArbitration.StrongStatus.notApplyStrong);
		arbitration.setImages(images);
		loanArbitrationService.save(arbitration);

		data.put("arbitrationId", arbitration.getId() + "");
		return ResponseData.success("请求成功", data);
	}

	/**
	 * 查看进度
	 */
	@RequestMapping(value = "goArbSchedule")
	@ResponseBody
	public ResponseData goArbSchedule(HttpServletRequest request) {
		Member member = memberService.getCurrent();
		String loanId = request.getParameter("loanId");
		NfsLoanArbitration arbitration = loanArbitrationService.getByLoanId(Long.valueOf(loanId));
		if(arbitration == null) {
			return ResponseData.error("没有找到对应的仲裁记录");
		}
		NfsLoanRecord loanRecord = loanRecordService.get(arbitration.getLoan());
		
		if(!loanRecord.getLoaner().getId().equals(member.getId())) {
			logger.error("token用户和借条用户不匹配！");
			return ResponseData.error("不能操作不属于自己的借条");
		}
		NfsLoanArbitrationDetail temp = new NfsLoanArbitrationDetail();
		temp.setArbitrationId(Long.valueOf(arbitration.getId()));
		List<NfsLoanArbitrationDetail> arbitrationDetails = loanArbitrationDetailService.findList(temp);

		Map<String, Object> data = new HashMap<String,Object>();
		/**
		 * isExecution : 0：不需要申请强执，1：可以申请强执，2：已经申请强执
		 */
		data.put("isExecution", 0);
		data.put("arbitrationId", arbitration.getId() + "");
		data.put("loanId", loanRecord.getId() + "");
		data.put("loanType", loanRecord.getId() + "_2");
		data.put("laoneeName", loanRecord.getLoanee().getName());
		data.put("dueRepayAmount", StringUtils.decimalToStr(loanRecord.getDueRepayAmount(), 2));
		data.put("applyAmount", StringUtils.decimalToStr(arbitration.getApplyAmount(), 2));
		data.put("dueRepayDate", DateUtils.getDateStr(loanRecord.getDueRepayDate(), "yyyy-MM-dd HH:mm:ss"));
		data.put("fee", StringUtils.decimalToStr(new BigDecimal(arbitration.getFee()), 2));
		List<Map<String, String>> detailList = new ArrayList<Map<String, String>>();
		for (NfsLoanArbitrationDetail nfsLoanArbitrationDetail : arbitrationDetails) {
			Map<String, String> detailMap = new HashMap<String, String>();
			detailMap.put("arbitrationStatus", getStatusNameToCN(nfsLoanArbitrationDetail.getStatus()));
			detailMap.put("createTime",
					DateUtils.getDateStr(nfsLoanArbitrationDetail.getCreateTime(), "yyyy-MM-dd HH:mm:ss"));
			detailMap.put("type", getTypeToCN(nfsLoanArbitrationDetail.getType()));
			detailMap.put("task", getTaskToCN(nfsLoanArbitrationDetail.getTask()));
			if (NfsLoanArbitrationDetail.Status.refundHasArrived.equals(nfsLoanArbitrationDetail.getStatus())) {
				MemberActTrx trxParam = new MemberActTrx();
				trxParam.setOrgId(arbitration.getId());
				trxParam.setDrc("C");
				trxParam.setMember(member);
				List<MemberActTrx> actTrxs = memberActTrxService.findList(trxParam);
				MemberActTrx memberActTrx = actTrxs.get(0);
				Long actTrxId = memberActTrx.getId();
				detailMap.put("actTrxId", actTrxId + "");
			}
			if(nfsLoanArbitrationDetail.getStatus().equals(NfsLoanArbitrationDetail.Status.arbitrationHasBeenDecided)) {
				data.put("isExecution", 1);
			}
			NfsLoanArbitrationExecution execution = arbitrationExecutionService.findByArbitrationId(arbitration.getId());
			if(execution != null && execution.getId() != null && arbitration.getStrongStatus().equals(StrongStatus.appliedStrong)) {
				data.put("isExecution", 2);
				data.put("executionStatus", getExecutionStatusToCN(execution.getStatus()));
			}
			detailList.add(detailMap);
		}
		data.put("detailList", detailList);
		return ResponseData.success("请求成功", data);
	}

	@RequestMapping(value = "/list")
	@ResponseBody
	public ResponseData list(HttpServletRequest request) {
		try {
			Member member = memberService.getCurrent();
			String type = request.getParameter("arbitrationType");
			Integer pageNo = Integer.valueOf(request.getParameter("pageNo"));
			Integer pageSize = 20;
			int trxType = 1;
			if (type.equals("1")) { 
				NfsLoanRecord loanRecord = new NfsLoanRecord();
				loanRecord.setLoaner(member);
				loanRecord.setTrxType(TrxType.offline);
				Page<NfsLoanRecord> page = loanRecordService.findApplyForArbitration(loanRecord, pageNo, pageSize);
				ArbitrationListResponseResult result = new ArbitrationListResponseResult();
				ArrayList<Arbitration> loanList = new ArrayList<Arbitration>();
				if (page.getList().size() > 0) {
					for (NfsLoanRecord everyLoanRecord : page.getList()) {
						if (everyLoanRecord.getStatus().equals(Status.repayed)) {
							continue;
						}
						Arbitration arbitration = new Arbitration();
						// 获取借条金额
						// 将借条金额存到response
						arbitration.setAmount(StringUtils.decimalToStr(everyLoanRecord.getAmount(), 2));
						// 讲借条利息存到response
						arbitration.setInterest(StringUtils.decimalToStr(everyLoanRecord.getInterest(), 2));
						// 将借条id存到response
						arbitration.setLoanId(everyLoanRecord.getId().toString());
						Member partner = null;
						partner = everyLoanRecord.getLoanee();
						// 将借款人头像存到response
						arbitration.setPartnerHeadImage(partner.getHeadImage());
						// 将借款人姓名存到response
						arbitration.setPartnerName(partner.getName());
						arbitration.setType(LoanConstant.TYPE_RECORD);
						Date date = new Date();
						arbitration.setDay(DateUtils.getDistanceOfTwoDate(date, everyLoanRecord.getDueRepayDate()));
						arbitration.setProgress(everyLoanRecord.getProgress());
						arbitration.setLoanStart(DateUtils.formatDate(everyLoanRecord.getLoanStart(), "yyyy-MM-dd"));
						arbitration.setRepayDate(DateUtils.formatDate(everyLoanRecord.getDueRepayDate(), "yyyy-MM-dd"));
						arbitration.setRepayType(everyLoanRecord.getRepayType().ordinal());
						// 判断状态
						String loanStatus = "";
						if (arbitration.getDay() == 1) {
							loanStatus = "今日逾期;FFAE38";
						} else if (arbitration.getDay() > 1) {
							loanStatus = "已逾期;757575|" + (arbitration.getDay()) + ";ED2E24|日;757575";
						} else {
							loanStatus = "逾期已超过;757575|15;ED2E24|天;757575";
						}
						String loanMode = "0"; // 今日角标 1：显示；0：不显示
						String overduePayment = "0"; // 逾期图标 1：显示；0：不显示
						if (arbitration.getDay() == 0 || arbitration.getDay() == 1) {
							loanMode = "1";
						} else if (arbitration.getDay() > 0) {
							overduePayment = "1";
						}
						arbitration.setLoanStatus(loanStatus);
						arbitration.setLoanMode(loanMode);
						arbitration.setOverduePayment(overduePayment);
						arbitration.setArbitrationStstus("1");
						loanList.add(arbitration);
					}
				}
				result.setArbitrationList(loanList);
				;
				return ResponseData.success("成功查询全部可申请仲裁借条", result);
			} else if (type.equals("2")) {
				NfsLoanArbitration arbitration = new NfsLoanArbitration();
				Long loanerId = member.getId();
				Page<NfsLoanArbitration> page = loanArbitrationService.findInArbitration(loanerId, arbitration, pageNo,
						pageSize,trxType);
				ArbitrationListResponseResult result = new ArbitrationListResponseResult();
				ArrayList<Arbitration> loanList = new ArrayList<Arbitration>();
				if (page.getList().size() > 0) {
					for (NfsLoanArbitration everyLoanArbitration : page.getList()) {
						NfsLoanRecord everyLoanRecord = loanRecordService.get(everyLoanArbitration.getLoan().getId());
						if (everyLoanRecord.getStatus().equals(Status.repayed)) {
							continue;
						}
						Arbitration arbitrationList = new Arbitration();
						arbitrationList.setAmount(StringUtils.decimalToStr(everyLoanRecord.getAmount(), 2));
						arbitrationList
								.setInterest(StringUtils.decimalToStr(everyLoanArbitration.getLoan().getInterest(), 2));
						arbitrationList.setLoanId(everyLoanArbitration.getLoan().getId().toString());
						arbitrationList.setArbitrationId(everyLoanArbitration.getId().toString());
						Member partner = null;
						partner = everyLoanRecord.getLoanee();
						arbitrationList.setPartnerHeadImage(partner.getHeadImage());
						arbitrationList.setPartnerName(partner.getName());
						arbitrationList.setType(LoanConstant.TYPE_RECORD);
						Date date2 = new Date();
						arbitrationList
								.setDay(DateUtils.getDistanceOfTwoDate(date2, everyLoanRecord.getDueRepayDate()));
						arbitrationList.setProgress(everyLoanArbitration.getLoan().getProgress());
						arbitrationList
								.setLoanStart(DateUtils.formatDate(everyLoanRecord.getDueRepayDate(), "yyyy-MM-dd"));
						arbitrationList.setRepayDate(
								DateUtils.formatDate(everyLoanArbitration.getLoan().getDueRepayDate(), "yyyy-MM-dd"));
						arbitrationList.setRepayType(everyLoanArbitration.getLoan().getRepayType().ordinal());
						arbitrationList.setCreateDate(
								DateUtils.formatDate(everyLoanArbitration.getCreateTime(), "yyyy-MM-dd"));
						arbitrationList.setArbitrationStstus("2");
						arbitrationList.setSpeedStatus(1);
						
						// 判断状态
						String loanStatus = "";
						if (arbitrationList.getDay() == 1) {
							loanStatus = "今日逾期;FFAE38";
						} else if (arbitrationList.getDay() > 1) {
							loanStatus = "已逾期;757575|" + (arbitrationList.getDay()) + ";ED2E24|日;757575";
						} else {
							loanStatus = "逾期已超过;757575|15;ED2E24|天;757575";
						}
						String loanMode = "0"; // 今日角标 1：显示；0：不显示
						String overduePayment = "0"; // 逾期图标 1：显示；0：不显示
						if (arbitrationList.getDay() == 0 || arbitrationList.getDay() == 1) {
							loanMode = "1";
						} else if (arbitrationList.getDay() > 0) {
							overduePayment = "1";
						}
						arbitrationList.setLoanStatus(loanStatus);
						arbitrationList.setLoanMode(loanMode);
						arbitrationList.setOverduePayment(overduePayment);
						
						loanList.add(arbitrationList);
					}
				}
				result.setArbitrationList(loanList);
				return ResponseData.success("成功查询全部仲裁中借条", result);
			} else if (type.equals("3")) {
				NfsLoanArbitration arbitration = new NfsLoanArbitration();
				Long loanerId = member.getId();
				Page<NfsLoanArbitration> page = loanArbitrationService.findArbitration(loanerId, arbitration, pageNo,
						pageSize,trxType);
				ArbitrationListResponseResult result = new ArbitrationListResponseResult();
				ArrayList<Arbitration> loanList = new ArrayList<Arbitration>();
				if (page.getList().size() > 0) {
					for (NfsLoanArbitration everyLoanArbitration : page.getList()) {
						NfsLoanRecord everyLoanRecord = loanRecordService.get(everyLoanArbitration.getLoan().getId());
						if (everyLoanRecord.getStatus().equals(Status.repayed)) {
							continue;
						}
						Arbitration arbitrationList = new Arbitration();
						arbitrationList.setAmount(StringUtils.decimalToStr(everyLoanRecord.getAmount(), 2));
						arbitrationList
								.setInterest(StringUtils.decimalToStr(everyLoanArbitration.getLoan().getInterest(), 2));
						arbitrationList.setLoanId(everyLoanArbitration.getLoan().getId().toString());
						arbitrationList.setArbitrationId(everyLoanArbitration.getId().toString());
						Member partner = null;
						partner = everyLoanRecord.getLoanee();
						arbitrationList.setPartnerHeadImage(partner.getHeadImage());
						arbitrationList.setPartnerName(partner.getName());
						arbitrationList.setType(LoanConstant.TYPE_RECORD);
						Date date2 = new Date();
						arbitrationList
								.setDay(DateUtils.getDistanceOfTwoDate(date2, everyLoanRecord.getDueRepayDate()));
						arbitrationList.setProgress(everyLoanArbitration.getLoan().getProgress());
						arbitrationList
								.setLoanStart(DateUtils.formatDate(everyLoanRecord.getDueRepayDate(), "yyyy-MM-dd"));
						arbitrationList.setRepayDate(
								DateUtils.formatDate(everyLoanArbitration.getLoan().getDueRepayDate(), "yyyy-MM-dd"));
						arbitrationList.setRepayType(everyLoanArbitration.getLoan().getRepayType().ordinal());
						arbitrationList.setCreateDate(
								DateUtils.formatDate(everyLoanArbitration.getCreateTime(), "yyyy-MM-dd"));
						int day = DateUtils.getDistanceOfTwoDate(
								DateUtils.addCalendarByDate(everyLoanArbitration.getRuleTime(), 720), new Date());
						arbitrationList.setSurplusDay(day);
						arbitrationList
								.setRuleTime(DateUtils.formatDate(everyLoanArbitration.getRuleTime(), "yyyy-MM-dd"));
						// 统计待还金额
						BigDecimal decMoney = everyLoanArbitration.getLoan().getDueRepayAmount();
						// 计算逾期利息
						BigDecimal overMoney = new BigDecimal(0.0);
						int days = DateUtils.getDistanceOfTwoDate(new Date(),
								everyLoanArbitration.getLoan().getDueRepayDate());
						BigDecimal overdueDaysBig = new BigDecimal(days);
						// 计算利息 默认给24
						overMoney = LoanUtils.calOverdueInterest(everyLoanArbitration.getLoan().getDueRepayAmount(),
								overdueDaysBig);
						// 总金额
						BigDecimal totalMoney = decMoney.add(overMoney);
						arbitrationList.setOverMoney(StringUtils.decimalToStr(totalMoney, 2));
						arbitrationList.setArbitrationStstus("3");
						arbitrationList.setSpeedStatus(1);
						StrongStatus strongStatus = everyLoanArbitration.getStrongStatus();
						/** 已申请 appliedStrong,未申请 notApplyStrong */
						if (strongStatus.equals(NfsLoanArbitration.StrongStatus.appliedStrong)) {
							arbitrationList.setApplyType("0");
						} else {
							arbitrationList.setApplyType("1");
						}
						
						// 判断状态
						String loanStatus = "";
						if (arbitrationList.getDay() == 1) {
							loanStatus = "今日逾期;FFAE38";
						} else if (arbitrationList.getDay() > 1) {
							loanStatus = "已逾期;757575|" + (arbitrationList.getDay()) + ";ED2E24|日;757575";
						} else {
							loanStatus = "逾期已超过;757575|15;ED2E24|天;757575";
						}
						String loanMode = "0"; // 今日角标 1：显示；0：不显示
						String overduePayment = "0"; // 逾期图标 1：显示；0：不显示
						if (arbitrationList.getDay() == 0 || arbitrationList.getDay() == 1) {
							loanMode = "1";
						} else if (arbitrationList.getDay() > 0) {
							overduePayment = "1";
						}
						arbitrationList.setLoanStatus(loanStatus);
						arbitrationList.setLoanMode(loanMode);
						arbitrationList.setOverduePayment(overduePayment);
						
						loanList.add(arbitrationList);
					}
				}
				result.setArbitrationList(loanList);
				return ResponseData.success("成功查询全部仲裁出裁决借条", result);
			} else if (type.equals("4")) {
				NfsLoanArbitration arbitration = new NfsLoanArbitration();
				Long loanerId = member.getId();
				Page<NfsLoanArbitration> page = loanArbitrationService.findEndOfArbitration(loanerId, arbitration,
						pageNo, pageSize,trxType);
				ArbitrationListResponseResult result = new ArbitrationListResponseResult();
				ArrayList<Arbitration> loanList = new ArrayList<Arbitration>();
				if (page.getList().size() > 0) {
					for (NfsLoanArbitration everyLoanArbitration : page.getList()) {
						Arbitration arbitrationList = new Arbitration();
						NfsLoanRecord everyLoanRecord = loanRecordService.get(everyLoanArbitration.getLoan().getId());
						/*
						 * if(everyLoanRecord.getStatus().equals(Status.repayed)) { continue; }
						 */
						arbitrationList.setAmount(StringUtils.decimalToStr(everyLoanRecord.getAmount(), 2));
						arbitrationList.setInterest(StringUtils.decimalToStr(everyLoanArbitration.getLoan().getInterest(), 2));
						arbitrationList.setLoanId(everyLoanArbitration.getLoan().getId().toString());
						arbitrationList.setArbitrationId(everyLoanArbitration.getId().toString());
						Member partner = null;
						partner = everyLoanRecord.getLoanee();
						arbitrationList.setPartnerHeadImage(partner.getHeadImage());
						arbitrationList.setPartnerName(partner.getName());
						arbitrationList.setType(LoanConstant.TYPE_RECORD);
						Date date2 = new Date();
						arbitrationList
								.setDay(DateUtils.getDistanceOfTwoDate(date2, everyLoanRecord.getDueRepayDate()));
						arbitrationList.setProgress(everyLoanArbitration.getLoan().getProgress());
						arbitrationList
								.setLoanStart(DateUtils.formatDate(everyLoanRecord.getDueRepayDate(), "yyyy-MM-dd"));
						arbitrationList.setRepayDate(
								DateUtils.formatDate(everyLoanArbitration.getLoan().getDueRepayDate(), "yyyy-MM-dd"));
						arbitrationList.setRepayType(everyLoanArbitration.getLoan().getRepayType().ordinal());
						arbitrationList.setCreateDate(
								DateUtils.formatDate(everyLoanArbitration.getCreateTime(), "yyyy-MM-dd"));
						arbitrationList.setArbitrationStstus("4");
						arbitrationList.setSpeedStatus(2);
						
						// 判断状态
						String loanStatus = "";
						if (arbitrationList.getDay() == 1) {
							loanStatus = "今日逾期;FFAE38";
						} else if (arbitrationList.getDay() > 1) {
							loanStatus = "已逾期;757575|" + (arbitrationList.getDay()) + ";ED2E24|日;757575";
						} else {
							loanStatus = "逾期已超过;757575|15;ED2E24|天;757575";
						}
						String loanMode = "0"; // 今日角标 1：显示；0：不显示
						String overduePayment = "0"; // 逾期图标 1：显示；0：不显示
						if (arbitrationList.getDay() == 0 || arbitrationList.getDay() == 1) {
							loanMode = "1";
						} else if (arbitrationList.getDay() > 0) {
							overduePayment = "1";
						}
						arbitrationList.setLoanStatus(loanStatus);
						arbitrationList.setLoanMode(loanMode);
						arbitrationList.setOverduePayment(overduePayment);
						
						loanList.add(arbitrationList);
					}
				}
				result.setArbitrationList(loanList);
				return ResponseData.success("成功查询全部仲裁已完成借条", result);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseData.error("查询失败");
		}
		return ResponseData.error("查询失败1");
	}

	/**
	 * 计算仲裁服务费
	 * 
	 * @param applyAmount 计算金额： 应还金额+逾期利息
	 * @return
	 */
	private BigDecimal calArbitrationApplyFee(BigDecimal totalAmount) {
		BigDecimal slmoney = BigDecimal.ZERO;// 仲裁受理费
		BigDecimal LawyerMoney = BigDecimal.ZERO;// 律师代理执行费
		Double totalMoneyD = totalAmount.doubleValue();
		if (totalMoneyD <= 1000) {
			slmoney = new BigDecimal(140);
		}
		if (totalMoneyD > 1000 && totalMoneyD <= 50000) {
			LawyerMoney = new BigDecimal(100);
			slmoney = new BigDecimal(totalMoneyD).subtract(new BigDecimal(1000));
			slmoney = slmoney.multiply(new BigDecimal(0.04));
			slmoney = slmoney.add(LawyerMoney).add(new BigDecimal(40)).setScale(2, BigDecimal.ROUND_HALF_UP);
		}
		if (totalMoneyD > 50000 && totalMoneyD <= 100000) {
			LawyerMoney = new BigDecimal(100);
			slmoney = new BigDecimal(totalMoneyD).subtract(new BigDecimal(50000));
			slmoney = slmoney.multiply(new BigDecimal(0.03));
			slmoney = slmoney.add(LawyerMoney).add(new BigDecimal(2000)).setScale(2, BigDecimal.ROUND_HALF_UP);
		}
		if (totalMoneyD > 100000 && totalMoneyD <= 200000) {
			LawyerMoney = new BigDecimal(100);
			slmoney = new BigDecimal(totalMoneyD).subtract(new BigDecimal(100000));
			slmoney = slmoney.multiply(new BigDecimal(0.02));
			slmoney = slmoney.add(LawyerMoney).add(new BigDecimal(3500)).setScale(2, BigDecimal.ROUND_HALF_UP);
		}
		if (totalMoneyD > 200000 && totalMoneyD <= 500000) {
			LawyerMoney = new BigDecimal(100);
			slmoney = new BigDecimal(totalMoneyD).subtract(new BigDecimal(200000));
			slmoney = slmoney.multiply(new BigDecimal(0.01));
			slmoney = slmoney.add(LawyerMoney).add(new BigDecimal(5500)).setScale(2, BigDecimal.ROUND_HALF_UP);
		}
		if (totalMoneyD > 500000 && totalMoneyD <= 1000000) {
			LawyerMoney = new BigDecimal(100);
			slmoney = new BigDecimal(totalMoneyD).subtract(new BigDecimal(500000));
			slmoney = slmoney.multiply(new BigDecimal(0.005));
			slmoney = slmoney.add(LawyerMoney).add(new BigDecimal(8500)).setScale(2, BigDecimal.ROUND_HALF_UP);
		}
		if (totalMoneyD > 1000000) {
			LawyerMoney = new BigDecimal(100);
			slmoney = new BigDecimal(totalMoneyD).subtract(new BigDecimal(1000000));
			slmoney = slmoney.multiply(new BigDecimal(0.0025));
			slmoney = slmoney.add(LawyerMoney).add(new BigDecimal(11000)).setScale(2, BigDecimal.ROUND_HALF_UP);
		}
		slmoney = slmoney.setScale(2, BigDecimal.ROUND_HALF_UP);
		return slmoney;
	}

	private String getStatusNameToCN(NfsLoanArbitrationDetail.Status status) {
		String name = "";
		switch (status) {
		case underReview:
			name = "审核中";
			break;
		case agentArbitrationApplication:
			name = "代理仲裁申请中";
			break;
		case applicating:
			name = "仲裁中";
			break;
		case arbitrationHasFiled:
			name = "仲裁院已立案";
			break;
		case arbitrationHasBeenDecided:
			name = "仲裁已裁决";
			break;
		case endOfArbitration:
			name = "仲裁结束";
			break;
		case arbitrationApplicationFeeReturned:
			name = "仲裁申请费退回";
			break;
		case refundHasArrived:
			name = "退款已到账";
			break;
		case theAuditFailed:
			name = "审核未通过";
			break;
		case failureToFile:
			name = "立案失败";
			break;
		case arbitrationFailure:
			name = "仲裁失败";
			break;
		case debit:
			name = "借条关闭";
			break;
		case paid:
			name = "已缴费";
			break;
		default:
			name = "状态异常";
			break;
		}
		return name;
	}

	private String getTaskToCN(NfsLoanArbitrationDetail.Task task) {
		String name = "";
		switch (task) {
		case manualReview:
			name = "人工审核 ";
			break;
		case theAuditHasBeenApproved:
			name = " 审核已通过";
			break;
		case arbitrationTrialIsInProgress:
			name = "仲裁审理进行中";
			break;
		case successfullyFiled:
			name = "立案成功";
			break;
		case ArbitrationHasPronouncedTheVerdict:
			name = "仲裁院已宣判裁决";
			break;
		case arbitralAwardEnforcement:
			name = "仲裁裁决执行中";
			break;
		case arbitrationHasInformedTheReasonForTheFailure:
			name = "仲裁院已告知失败原因";
			break;
		case arbitrationFailure:
			name = "仲裁失败 ";
			break;
		case theAuditFailed:
			name = "审核未通过";
			break;
		case filingFailed:
			name = "立案未通过";
			break;
		case auditDataIsAbnormal:
			name = "审核资料异常";
			break;
		case arbitrationFailedToPassTheCase:
			name = "仲裁院立案未通过 ";
			break;
		case refundOfPrepayments:
			name = "退还预支付款项";
			break;
		case checkIfThePaymentIsReceived:
			name = "查看款项是否到账";
			break;
		case debit:
			name = "借条关闭";
			break;
		case paid:
			name = "已缴费";
			break;
		default:
			name = "状态异常";
			break;
		}
		return name;
	}

	private String getTypeToCN(NfsLoanArbitrationDetail.Type type) {
		String name = "";
		switch (type) {
		case auditProcess:
			name = "审核流程";
			break;
		case auditResult:
			name = "审核结果";
			break;
		case arbitrationProcess:
			name = "仲裁流程";
			break;
		case filedResult:
			name = "立案结果 ";
			break;
		case arbitrationResult:
			name = "仲裁结果";
			break;
		case refundProcess:
			name = "退款流程";
			break;
		default:
			name = "状态异常";
			break;
		}
		return name;
	}

	private String getExecutionStatusToCN(NfsLoanArbitrationExecution.ExecutionStatus executionStatus) {
		String name = "";
		switch (executionStatus) {
		case executionApplication:
			name = "强执申请中";
			break;
		case executionRefuseToAccept:
			name = "强执拒绝受理";
			break;
		case executionPayment:
			name = "强执缴费中";
			break;
		case executionProcessing:
			name = "强执进行中 ";
			break;
		case executionOver:
			name = "强执已结束";
			break;
		case executionExpired:
			name = "强执已失效";
			break;
		case executionFailure:
			name = "强执失败";
			break;
		case debit:
			name = "借条关闭";
			break;
		default:
			name = "状态异常";
			break;
		}
		return name;
	}
	
}
