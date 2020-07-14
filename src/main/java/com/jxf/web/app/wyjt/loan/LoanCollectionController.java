package com.jxf.web.app.wyjt.loan;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.jxf.loan.constant.LoanConstant;
import com.jxf.loan.constant.RecordMessage;
import com.jxf.loan.entity.NfsLoanApply;

import com.jxf.loan.entity.NfsLoanCollection;
import com.jxf.loan.entity.NfsLoanCollectionDetail;
import com.jxf.loan.entity.NfsLoanDetailMessage;
import com.jxf.loan.entity.NfsLoanRecord;

import com.jxf.loan.entity.NfsLoanRecord.CollectionStatus;
import com.jxf.loan.entity.NfsLoanRecord.Status;

import com.jxf.loan.service.NfsLoanCollectionDetailService;
import com.jxf.loan.service.NfsLoanCollectionService;
import com.jxf.loan.service.NfsLoanDetailMessageService;
import com.jxf.loan.service.NfsLoanRecordService;
import com.jxf.loan.utils.LoanUtils;
import com.jxf.mem.entity.Member;
import com.jxf.mem.service.MemberActService;
import com.jxf.mem.service.MemberService;
import com.jxf.mem.utils.H5Utils;
import com.jxf.mem.utils.VerifiedUtils;
import com.jxf.svc.persistence.Page;
import com.jxf.svc.utils.DateUtils;
import com.jxf.svc.utils.Exceptions;
import com.jxf.svc.utils.StringUtils;
import com.jxf.web.admin.sys.BaseController;
import com.jxf.web.model.ResponseData;
import com.jxf.web.model.wyjt.app.arbitration.ArbitrationListResponseResult;
import com.jxf.web.model.wyjt.app.arbitration.ArbitrationListResponseResult.Arbitration;
import com.jxf.web.model.wyjt.app.collection.ApplyCollectionRequestParam;
import com.jxf.web.model.wyjt.app.collection.CollectionListRequestParam;

/**
 * 催收
 * @author liuhuaixin
 *
 */
@Controller("wyjtAppLoanCollectionController")
@RequestMapping(value="${wyjtApp}/collection")
public class LoanCollectionController extends BaseController {
		
	@Autowired
	private MemberService memberService;
	@Autowired
	private MemberActService memberActService;
	@Autowired
	private NfsLoanRecordService loanRecordService;
	@Autowired
	private NfsLoanCollectionService loanCollectionService;
	@Autowired
	private NfsLoanCollectionDetailService loanCollectionDetailService;
	@Autowired
	private NfsLoanDetailMessageService loanDetailMessageService;
	
	@RequestMapping(value ="/list")
	@ResponseBody
	public ResponseData list(HttpServletRequest request) {
		try {
		Member member = memberService.getCurrent();
		if(member == null) {
			return ResponseData.error("请登录！");
		}
		String param = request.getParameter("param");
		CollectionListRequestParam reqData = JSONObject.parseObject(param,CollectionListRequestParam.class);
		Integer pageNo = reqData.getPageNo();
		Integer pageSize = reqData.getPageSize();
		String type =reqData.getType();
		if(type.equals("1")) {
			NfsLoanRecord loanRecord = new NfsLoanRecord();
			loanRecord.setLoaner(member);
			Page<NfsLoanRecord> page = loanRecordService.findCollection(loanRecord,pageNo, pageSize);
			ArbitrationListResponseResult result = new ArbitrationListResponseResult();
			ArrayList<Arbitration> loanList = new ArrayList<Arbitration>();
			for(NfsLoanRecord everyLoanRecord:page.getList()) {
				if(everyLoanRecord.getStatus().equals(Status.repayed)) {
					continue;
				}
				Arbitration arbitration = new Arbitration();
				//获取借条金额
				String intValueOfAmount = StringUtils.decimalToStr(everyLoanRecord.getAmount(),2);
				//将借条金额存到response
				arbitration.setAmount(intValueOfAmount);
				//讲借条利息存到response
				arbitration.setInterest(StringUtils.decimalToStr(everyLoanRecord.getInterest(),2));
				//将借条id存到response
				arbitration.setLoanId(everyLoanRecord.getId().toString());
				Member partner = null;
				partner = everyLoanRecord.getLoanee();
				//将借款人头像存到response
				arbitration.setPartnerHeadImage(partner.getHeadImage());
				//将借款人姓名存到response
				arbitration.setPartnerName(partner.getName());
				arbitration.setType(LoanConstant.TYPE_RECORD);
				Date date = new Date();
				arbitration.setDay(DateUtils.getDistanceOfTwoDate(date, everyLoanRecord.getDueRepayDate()));
				arbitration.setProgress(everyLoanRecord.getProgress());
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
				loanList.add(arbitration);
			}
			result.setArbitrationList(loanList);;
			return ResponseData.success("成功查询全部可申请催收借条",result);
		}else if(type.equals("2")) {
			NfsLoanCollection collection = new NfsLoanCollection();
			Long loanerId = member.getId();
			Page<NfsLoanCollection> page = loanCollectionService.findInCollection(loanerId,collection,pageNo,pageSize);
			ArbitrationListResponseResult result = new ArbitrationListResponseResult();
			ArrayList<Arbitration> loanList = new ArrayList<Arbitration>();
			for(NfsLoanCollection everyLoanCollection:page.getList()) {
				NfsLoanRecord everyLoanRecord = loanRecordService.get(everyLoanCollection.getLoan().getId());
				/*if(everyLoanRecord.getStatus().equals(Status.repayed)) {
					continue;
				}*/
				Arbitration arbitrationList = new Arbitration();
				arbitrationList.setAmount(StringUtils.decimalToStr(everyLoanCollection.getLoan().getAmount(),2));
				arbitrationList.setInterest(StringUtils.decimalToStr(everyLoanCollection.getLoan().getInterest(),2));
				arbitrationList.setLoanId(everyLoanCollection.getLoan().getId().toString());
				arbitrationList.setArbitrationId(everyLoanCollection.getId().toString());
				Member partner = null;
				partner = everyLoanCollection.getLoan().getLoanee();
				arbitrationList.setPartnerHeadImage(partner.getHeadImage());
				arbitrationList.setPartnerName(partner.getName());
				arbitrationList.setType(LoanConstant.TYPE_RECORD);
				Date date2 = new Date();
				arbitrationList.setDay(DateUtils.getDistanceOfTwoDate(date2, everyLoanRecord.getDueRepayDate()));
				arbitrationList.setProgress(everyLoanCollection.getLoan().getProgress());
				arbitrationList.setRepayDate(DateUtils.formatDate(everyLoanCollection.getLoan().getDueRepayDate(), "yyyy-MM-dd"));
				arbitrationList.setRepayType(everyLoanCollection.getLoan().getRepayType().ordinal());
				arbitrationList.setCreateDate(DateUtils.formatDate(everyLoanCollection.getCreateTime(),"yyyy-MM-dd"));
				arbitrationList.setArbitrationStstus("催收中;000000");
				arbitrationList.setSpeedStatus(1);
				loanList.add(arbitrationList);
			}
			result.setArbitrationList(loanList);
			return ResponseData.success("成功查询全部催收中借条",result);
		}else if(type.equals("3")){
			NfsLoanCollection collection = new NfsLoanCollection();
			Long loanerId = member.getId();
			Page<NfsLoanCollection> page = loanCollectionService.findEndOfCollection(loanerId,collection,pageNo,pageSize);
			ArbitrationListResponseResult result = new ArbitrationListResponseResult();
			ArrayList<Arbitration> loanList = new ArrayList<Arbitration>();
			for(NfsLoanCollection everyLoanCollection:page.getList()) {
				NfsLoanRecord everyLoanRecord = loanRecordService.get(everyLoanCollection.getLoan().getId());
				/*if(everyLoanRecord.getStatus().equals(Status.repayed)) {
					continue;
				}*/
				Arbitration arbitrationList = new Arbitration();
				arbitrationList.setAmount(StringUtils.decimalToStr(everyLoanCollection.getLoan().getAmount(),2));
				arbitrationList.setInterest(StringUtils.decimalToStr(everyLoanCollection.getLoan().getInterest(),2));
				arbitrationList.setLoanId(everyLoanCollection.getLoan().getId().toString());
				arbitrationList.setArbitrationId(everyLoanCollection.getId().toString());
				Member partner = null;
				partner = everyLoanCollection.getLoan().getLoanee();
				arbitrationList.setPartnerHeadImage(partner.getHeadImage());
				arbitrationList.setPartnerName(partner.getName());
				arbitrationList.setType(LoanConstant.TYPE_RECORD);
				Date date2 = new Date();
				arbitrationList.setDay(DateUtils.getDistanceOfTwoDate(date2, everyLoanRecord.getDueRepayDate()));
				arbitrationList.setProgress(everyLoanCollection.getLoan().getProgress());
				arbitrationList.setRepayDate(DateUtils.formatDate(everyLoanCollection.getLoan().getDueRepayDate(), "yyyy-MM-dd"));
				arbitrationList.setRepayType(everyLoanCollection.getLoan().getRepayType().ordinal());
				arbitrationList.setCreateDate(DateUtils.formatDate(everyLoanCollection.getCreateTime(),"yyyy-MM-dd"));
				if (com.jxf.loan.entity.NfsLoanCollection.Status.success.equals(everyLoanCollection.getStatus())) {
					arbitrationList.setArbitrationStstus("催收成功;000000");
					arbitrationList.setSpeedStatus(2);
				} else {
					arbitrationList.setArbitrationStstus("催收失败;000000");
					arbitrationList.setSpeedStatus(3);
				}
				loanList.add(arbitrationList);
			}
			result.setArbitrationList(loanList);
			return ResponseData.success("成功查询全部催收已完成借条",result);
		}
		}catch(Exception e){
			e.printStackTrace();
			return ResponseData.error("查询失败");
		}
		return ResponseData.error("查询失败");
	}
	
	/**
	 * 	申请催收预支付页面跳转
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "apply")
	public ModelAndView collectionApply(HttpServletRequest request,Model model) {
		Member member = memberService.getCurrent();
		String param = request.getParameter("param");
		ApplyCollectionRequestParam reqData = JSONObject.parseObject(param,ApplyCollectionRequestParam.class);
		ModelAndView mv = new ModelAndView("app/applyCollectionResult");
		ModelAndView mv2 = new ModelAndView("app/collection/collectionPay");
		mv = H5Utils.addPlatform(member, mv);
		mv2 = H5Utils.addPlatform(member, mv2);
		String loanId = reqData.getLoanId();
		NfsLoanRecord loanRecord = loanRecordService.get(Long.parseLong(loanId));

		if(loanRecord.getCollectionStatus().equals(NfsLoanRecord.CollectionStatus.doing)) {
			mv.addObject("success", false);
			mv.addObject("errStr", "该借条正在催收中");
			return mv;
		}
		if(loanRecord.getArbitrationStatus().equals(NfsLoanRecord.ArbitrationStatus.doing)) {
			mv.addObject("success", false);
			mv.addObject("errStr", "该借条正在仲裁中");
			return mv;
		}
		if(!loanRecord.getStatus().equals(NfsLoanRecord.Status.overdue)){
			mv.addObject("success", false);
			mv.addObject("errStr", "此借款单当前无法申请催收!");
		}

		int decTotalMoney = loanRecord.getDueRepayAmount().intValue();
		if(decTotalMoney < 100){
			mv.addObject("success", false);
			mv.addObject("errStr", "争议金额低于100元无法申请该服务");
			return mv;
 		}
		if(loanRecord.getCollectionTimes() != null && loanRecord.getCollectionTimes()>3) {
			mv.addObject("success", false);
			mv.addObject("errStr", "此借款单已经催收三次，无法再次申请");
			return mv;
		}
		// 计算逾期利息
    	BigDecimal overMoney=new BigDecimal(0.0);
    	int days=DateUtils.getDistanceOfTwoDate(new Date(),loanRecord.getDueRepayDate());
    	BigDecimal overdueDaysBig = new BigDecimal(days);
    	//计算利息 默认给24
        overMoney=LoanUtils.calOverdueInterest(loanRecord.getDueRepayAmount(),overdueDaysBig);
	    //总金额
	    BigDecimal dueRepayMoney = loanRecord.getDueRepayAmount();
	    BigDecimal totalMoney= dueRepayMoney.add(overMoney);
	    Date date = new Date();
	    int delayDays = DateUtils.getDistanceOfTwoDate(date, loanRecord.getDueRepayDate());
//		1)M1(≤30天)的费率为10%；
//		2)M2(31<逾期≤60天)的费率为15%；
//		3)M3(61<逾期≤90天)的费率为20%；
//		4)M4(91<逾期≤120天)的费率为25%；
//		5)M5(121<逾期≤150天)的费率为30%；
//		6)M6(151<逾期≤180天)的费率为35%；
//		7)M6+(181<逾期<365天)的费率为40%；
		BigDecimal res = new BigDecimal(0.0);
		if(delayDays>=1&&delayDays<=30){
			res = new BigDecimal(0.1).multiply(totalMoney);
		}
		if(delayDays>=31&&delayDays<=60){
			res = new BigDecimal(0.15).multiply(totalMoney);
		}
		if(delayDays>=61&&delayDays<=90){
			res = new BigDecimal(0.2d).multiply(totalMoney);
		}
		if(delayDays>=91&&delayDays<=120){
			res = new BigDecimal(0.25).multiply(totalMoney);
		}
		if(delayDays>=121&&delayDays<=150){
			res = new BigDecimal(0.3).multiply(totalMoney);
		}
		if(delayDays>=151&&delayDays<=180){
			res = new BigDecimal(0.35).multiply(totalMoney);
		}
		if(delayDays>=181){
			res = new BigDecimal(0.4).multiply(totalMoney);
		}
		BigDecimal avlBal = memberActService.getAvlBal(member);
		boolean isEnough = false;
		if(avlBal.compareTo(res) >= 0) {
			isEnough = true;
		}
        String memberToken = request.getHeader("x-memberToken");
        model.addAttribute("memberToken",memberToken);
        model.addAttribute("isEnough", isEnough);
        model.addAttribute("isPayPsw", VerifiedUtils.isVerified(member.getVerifiedList(), 22));
        model.addAttribute("loan", loanRecord);
        //预支付
		model.addAttribute("payMoney",StringUtils.decimalToStr(res, 2));
		//未还金额
		model.addAttribute("totalMoney",totalMoney);
		//未还期数
		model.addAttribute("repayNum",loanRecord.getDueRepayTerm());
		//账户余额
		model.addAttribute("remainMoney",avlBal);
		return mv2;
	}
	/**
	 * 申请催收缴费预支付
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "payForApply")
	public ModelAndView payForApply(HttpServletRequest request) {
		ModelAndView mv = new ModelAndView("app/collection/collectionPayResult");
		String loanId = request.getParameter("loanId");
		String amountStr = request.getParameter("payMoney");
		String memberToken = request.getHeader("x-memberToken");
		Member loaner = memberService.getCurrent();
		if(loaner == null) {
			mv.addObject("code", -1);
			mv.addObject("message", "登录状态已过期，请重新登录！");
			return mv;
		}
		mv = H5Utils.addPlatform(loaner, mv);
		NfsLoanRecord loanRecord = loanRecordService.get(Long.valueOf(loanId));
		if(!NfsLoanRecord.Status.overdue.equals(loanRecord.getStatus())) {
			mv.addObject("code", -1);
			mv.addObject("message", "借条状态不支持催收服务！");
			return mv;
		}
		int decTotalMoney = loanRecord.getDueRepayAmount().intValue();
		if(decTotalMoney < 100){
			mv.addObject("code", -1);
			mv.addObject("message", "争议金额低于100元无法申请该服务");
			return mv;
 		}
		if(loanRecord.getCollectionStatus() != null && NfsLoanRecord.CollectionStatus.doing.equals(loanRecord.getCollectionStatus())) {
			mv.addObject("code", -1);
			mv.addObject("message", "您已经申请了催收服务，请耐心等待催收结果！");
			return mv;
		}
		Integer collectionTimes = loanRecord.getCollectionTimes();
		if(collectionTimes != null && collectionTimes >= 3) {
			mv.addObject("code", -1);
			mv.addObject("message", "您已经申请催收服务三次了，不能再申请催收了！");
			return mv;
		}
		BigDecimal amount = StringUtils.toDecimal(amountStr);
		if(amount.compareTo(BigDecimal.ZERO) <= 0) {
			mv.addObject("code", -1);
			mv.addObject("message", "催收服务费预缴金额错误，请重新操作！");
			return mv;
		}
		//更改loanrecord的状态
		if(collectionTimes != null) {
			collectionTimes = collectionTimes + 1;
		}else {
			collectionTimes = 1;
		}
		loanRecord.setCollectionTimes(collectionTimes);
		loanRecord.setCollectionStatus(CollectionStatus.doing);
		
		//生成催收记录
		NfsLoanCollection collection = new NfsLoanCollection();
		collection.setLoan(loanRecord);
		collection.setFee(amount);
		collection.setStatus(NfsLoanCollection.Status.auditing);
		
		//催收详情
		NfsLoanCollectionDetail collectionDetail = new NfsLoanCollectionDetail();
		collectionDetail.setStatus(NfsLoanCollectionDetail.Status.underReview);
		if(NfsLoanApply.RepayType.oneTimePrincipalAndInterest.equals(loanRecord.getRepayType())) {
			collectionDetail.setType(NfsLoanCollectionDetail.Type.fullAmount);
		}else {
			collectionDetail.setType(NfsLoanCollectionDetail.Type.staging);
		}
		try {
			int code = loanCollectionService.payForApplyCollection(loaner, collection, collectionDetail);
			if(code == 0) {
				//生成对话
				NfsLoanDetailMessage nfsLoanDetailMessage = new NfsLoanDetailMessage();
				nfsLoanDetailMessage.setDetail(loanRecord.getLoanApplyDetail());
				nfsLoanDetailMessage.setMember(loaner);
				nfsLoanDetailMessage.setMessageId(RecordMessage.CHAT_2201);
				nfsLoanDetailMessage.setType(RecordMessage.SEND_REMIND);
				loanDetailMessageService.save(nfsLoanDetailMessage);
				
				mv.addObject("memberToken", memberToken);
				mv.addObject("collectionId", collection.getId());
				mv.addObject("code", 0);
				mv.addObject("message", "催收服务费预支付成功！");
			}else {
				mv.addObject("code", -1);
				mv.addObject("message", "记录状态已更新，请勿重复操作！");
			}
			return mv;
		} catch (Exception e) {
			logger.error("会员{}支付催收服务费更新账户异常，异常信息：{}",loaner.getId(),Exceptions.getStackTraceAsString(e));
			mv.addObject("code", -1);
			mv.addObject("message", "账户异常，操作失败！");
			return mv;
		}
	}
	/**
	 * 查看催收进度
	 * @return
	 */
	@RequestMapping(value="goColSchedule")
	public ModelAndView goColSchedule(HttpServletRequest request) {
		String collectionId = request.getParameter("collectionId");
		Member loaner = memberService.getCurrent();
		ModelAndView mv = new ModelAndView("app/collection/collectionSchedule");
		mv = H5Utils.addPlatform(loaner, mv);
		NfsLoanCollectionDetail param = new NfsLoanCollectionDetail();
		param.setCollectionId(Long.valueOf(collectionId));
		List<NfsLoanCollectionDetail> details = loanCollectionDetailService.findList(param);
		mv.addObject("detail", details);
		return mv;
	}
	
}
