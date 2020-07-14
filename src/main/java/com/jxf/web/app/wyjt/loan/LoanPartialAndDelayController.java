package com.jxf.web.app.wyjt.loan;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jxf.loan.constant.LoanConstant;
import com.jxf.loan.constant.RecordMessage;
import com.jxf.loan.entity.NfsLoanApply;
import com.jxf.loan.entity.NfsLoanDetailMessage;
import com.jxf.loan.entity.NfsLoanPartialAndDelay;
import com.jxf.loan.entity.NfsLoanPartialAndDelay.PayStatus;
import com.jxf.loan.entity.NfsLoanRecord;
import com.jxf.loan.entity.NfsLoanRecord.AuctionStatus;
import com.jxf.loan.service.NfsLoanDetailMessageService;
import com.jxf.loan.service.NfsLoanPartialAndDelayService;
import com.jxf.loan.service.NfsLoanRecordService;
import com.jxf.loan.utils.LoanUtils;
import com.jxf.mem.entity.Member;
import com.jxf.mem.entity.MemberMessage;
import com.jxf.mem.service.MemberMessageService;
import com.jxf.mem.service.MemberService;
import com.jxf.mms.service.SendMsgService;
import com.jxf.svc.utils.DateUtils;
import com.jxf.svc.utils.Exceptions;
import com.jxf.svc.utils.StringUtils;
import com.jxf.web.minipro.BaseController;
import com.jxf.web.model.ResponseData;
import com.jxf.web.model.wyjt.app.loan.AnswerPartialPayOrDelayRequestParam;
import com.jxf.web.model.wyjt.app.loan.DelayAndPartialRequestParam;
import com.jxf.web.model.wyjt.app.loan.LoanDetailForAppResponseResult;



/**
 * 借条部分还款和延期
 * 
 * @author xrd
 * @version 2.0
 */
@Controller("wyjtLoanRecordPartialController")
@RequestMapping(value="${wyjtApp}/loan")
public class LoanPartialAndDelayController extends BaseController {
	private static final Logger logger = LoggerFactory.getLogger(LoanPartialAndDelayController.class);
	@Autowired
	private MemberService memberService;
	@Autowired
	private MemberMessageService memberMessageService;
	@Autowired
	private NfsLoanRecordService loanRecordService;
	@Autowired
	private NfsLoanPartialAndDelayService loanPartialAndDelayService;
	@Autowired
	private NfsLoanDetailMessageService loanDetailMessageService;

	@Autowired
	private SendMsgService sendMsgService;
	
	/**
	 * 取消延期/部分还款
	 */
	@RequestMapping(value = "/cancelPartialPayOrDelay")
	public @ResponseBody
	ResponseData cancelPartialPayOrDelay(HttpServletRequest request) {
		Member me = memberService.getCurrent();
		String loanId = request.getParameter("loanId");
		NfsLoanRecord record = loanRecordService.get(Long.valueOf(loanId));
		NfsLoanPartialAndDelay nfsLoanPartialRepayApply = new NfsLoanPartialAndDelay();
		nfsLoanPartialRepayApply.setLoan(record);
		nfsLoanPartialRepayApply.setStatus(NfsLoanPartialAndDelay.Status.confirm);
		List<NfsLoanPartialAndDelay> findList = loanPartialAndDelayService.findList(nfsLoanPartialRepayApply);
		if(findList != null && findList.size() > 0){
			nfsLoanPartialRepayApply = findList.get(0);
		}else{
			LoanDetailForAppResponseResult detail = loanRecordService.getDetail(record.getId().toString(),2,me);
			return ResponseData.success("申请已结束",detail);
		}
		try {
			loanPartialAndDelayService.cancelPartialPayOrDelay(nfsLoanPartialRepayApply,me,record);//放一个事务里
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(e));
			return ResponseData.error("操作失败，请稍候重试");
		}
		LoanDetailForAppResponseResult detail = loanRecordService.getDetail(record.getId().toString(),2,me);
		return ResponseData.success("已取消",detail);
	}
	/**
	 * 同意/拒绝 部分还款or延期
	 * 前端现在是从button按钮转发来的
	 */
	@RequestMapping(value = "/answerPartialPayOrDelay")
	public @ResponseBody
	ResponseData answerPartialPay(HttpServletRequest request) {
		Member me = memberService.getCurrent();
		String param = request.getParameter("param");
		String loanId =null;
		String isAgree = null;
		if(StringUtils.isNotBlank(param)){//预留 从接口请求来也行
			AnswerPartialPayOrDelayRequestParam reqData = JSONObject.parseObject(param, AnswerPartialPayOrDelayRequestParam.class);
			loanId = reqData.getLoanId();
			isAgree = reqData.getIsAgree();
		}else{
			loanId = request.getParameter("loanId");
			isAgree = request.getParameter("isAgree");
		}
		
		NfsLoanRecord loanRecord = loanRecordService.get(Long.valueOf(loanId));
		
		NfsLoanPartialAndDelay loanPartialRepayApply = new NfsLoanPartialAndDelay();
		loanPartialRepayApply.setLoan(loanRecord);
		loanPartialRepayApply.setStatus(NfsLoanPartialAndDelay.Status.confirm);
		List<NfsLoanPartialAndDelay> findList = loanPartialAndDelayService.findList(loanPartialRepayApply);
		
		if(findList != null && findList.size() > 0){
			loanPartialRepayApply = findList.get(0);
		}else{
			LoanDetailForAppResponseResult detail = loanRecordService.getDetail(loanRecord.getId().toString(),2,me);
			return ResponseData.success("申请已结束",detail);
		}
		//放一个事务里
		try {
			ArrayList<Object> answerPartialPay = loanPartialAndDelayService.answerPartialPay(loanRecord,me,loanPartialRepayApply,isAgree);
			if(answerPartialPay.get(1) != null){
				//推送
				sendMsgService.beforeSendAppMsg((MemberMessage) answerPartialPay.get(1));
			}
			//返回详情
			LoanDetailForAppResponseResult detail = loanRecordService.getDetail(loanRecord.getId().toString(),2,me);
			return ResponseData.success((String)answerPartialPay.get(0),detail);
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(e));
			return ResponseData.error("账户更新失败，请重试！");
		}
	}
	
	/**
	 * 	申请延期or部分还款
	 */
	@RequestMapping(value = "/delayAndPartial")
	public @ResponseBody ResponseData delayAndPartial(HttpServletRequest request) {
		
		Member member = memberService.getCurrent();
		String param = request.getParameter("param");
		DelayAndPartialRequestParam reqData = JSONObject.parseObject(param, DelayAndPartialRequestParam.class);
		
		String recordId = reqData.getLoanId();
		String delayInterest = reqData.getDelayInterest();//延期利息
		String repayDateAfterDelay = reqData.getRepayDateAfterDelay();//延期后还款日
		String partialAmount = reqData.getPartialAmount();

		if(StringUtils.isNotBlank(delayInterest)&&!StringUtils.isNumeric(delayInterest)){
			return ResponseData.error("延期利息应为整数");
		}

		NfsLoanRecord loanRecord = loanRecordService.get(Long.valueOf(recordId));
		
		BigDecimal amount = loanRecord.getAmount();//本期本金
		BigDecimal interest = loanRecord.getInterest();//本期利息
		BigDecimal intRate = loanRecord.getIntRate();//本期利率
		BigDecimal dueRepayAmount = amount.add(interest);//上期应还金额
		BigDecimal delayRateBig = BigDecimal.ZERO;//延期利率
		
		Date dueRepayDate = loanRecord.getDueRepayDate();
		BigDecimal partialAmountBig = BigDecimal.ZERO;//部分还款金额

		//债转检查
		if(!loanRecord.getAuctionStatus().equals(AuctionStatus.initial)){
			return ResponseData.error("借条正在转让中无法操作！");
		}
		
		//延期校验
		if(StringUtils.isNotBlank(repayDateAfterDelay)){
			Date repayLatestDate = DateUtils.parseDate(repayDateAfterDelay);
			if (repayLatestDate.getTime() < loanRecord.getDueRepayDate().getTime()) {
				return ResponseData.error("延期时长必须大于还款日");
			}
			if (DateUtils.addCalendarByMonth(loanRecord.getDueRepayDate(), 12).getTime() < repayLatestDate.getTime()) {
				return ResponseData.error("最长延期时长为一年");
			}
			if (!loanRecord.getDelayStatus().equals(NfsLoanRecord.DelayStatus.initial)) {
				return ResponseData.error("已申请延期等待对方确认,请不要重复操作");
			}
		}
		//部分还款金额检查
		if(StringUtils.isNotBlank(partialAmount)){
			if(!StringUtils.isNumeric(partialAmount)) {
				return ResponseData.error("部分还款金额应为整数");
			}	
			partialAmountBig = new BigDecimal(partialAmount);
			if(dueRepayAmount.compareTo(partialAmountBig) <= 0){
				return ResponseData.error("部分还款金额不能大于或等于借款总额!");
			}else if(dueRepayAmount.subtract(partialAmountBig).compareTo(new BigDecimal(1)) < 0){
				return ResponseData.error("部分还款后剩余借条金额不能小于1元!");
			}
		}
		
		//生成 延期/部分还款申请记录	
		boolean isLoaner = member.getId().compareTo(loanRecord.getLoaner().getId())==0;
		
		NfsLoanPartialAndDelay loanDelayApply = new NfsLoanPartialAndDelay();
		loanDelayApply.setMember(member);		
		if(isLoaner){
			loanDelayApply.setMemberRole(NfsLoanApply.LoanRole.loaner);
		}else{
			loanDelayApply.setMemberRole(NfsLoanApply.LoanRole.loanee);
		}
		loanDelayApply.setLoan(loanRecord);
		
		
		
		MemberMessage sendMessage = null;
		/**
		 * 仅延期
		 */
		if((StringUtils.isBlank(partialAmount) || (StringUtils.isNotBlank(partialAmount) && StringUtils.equals(partialAmount, "0")))){
			
			Date repayLatestDate = DateUtils.parseDate(repayDateAfterDelay);
			int delayDays = DateUtils.getDistanceOfTwoDate(repayLatestDate, loanRecord.getDueRepayDate());
			loanDelayApply.setDelayDays(delayDays);
			BigDecimal nowAmount = amount.add(interest);//延期本金
			if(StringUtils.isNotBlank(delayInterest) && !delayInterest.equals("0")){
				BigDecimal delayInterestBig = new BigDecimal(delayInterest);
				delayRateBig = LoanUtils.getIntRate(nowAmount, delayDays, delayInterestBig);
			}else{
				delayRateBig = BigDecimal.ZERO;
			}
			BigDecimal realRate = delayRateBig.multiply(new BigDecimal("100"));//存数据库的利率 *100之后的数
			if(realRate.compareTo(new BigDecimal("24")) > 0){
				return ResponseData.error("延期期间利率不能超过24%");
			}
			
			if(isLoaner){
				loanRecord.setDelayStatus(NfsLoanRecord.DelayStatus.loanerApplyDelay);
			}else{
				loanRecord.setDelayStatus(NfsLoanRecord.DelayStatus.loaneeApplyDelay);
			}
			loanRecordService.save(loanRecord);
			
			//计算延期期间的还款计划
			NfsLoanRecord afterDelayRecord = LoanUtils.calInt(nowAmount, realRate, NfsLoanApply.RepayType.oneTimePrincipalAndInterest, delayDays);
			loanDelayApply.setPayStatus(PayStatus.noPay);
			loanDelayApply.setStatus(NfsLoanPartialAndDelay.Status.confirm);
			loanDelayApply.setDelayRate(realRate);
			loanDelayApply.setDelayInterest(afterDelayRecord.getInterest());
			loanDelayApply.setOldInterest(interest);
			loanDelayApply.setOldRate(intRate);
			loanDelayApply.setOldAmount(amount);
			loanDelayApply.setPartialAmount(BigDecimal.ZERO);
			loanDelayApply.setRemainAmount(nowAmount);
			loanDelayApply.setNowRepayDate(DateUtils.parseDate(repayDateAfterDelay));
			loanDelayApply.setType(NfsLoanPartialAndDelay.Type.delay);
			loanDelayApply.setNowInterest(afterDelayRecord.getInterest());
			loanPartialAndDelayService.save(loanDelayApply);
			
			//发送会员消息
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("name", member.getName());
			
			//发送对话
			Map<String, String> dialogueMap = new HashMap<String, String>();
			dialogueMap.put(LoanConstant.DELAY_INTEREST, delayInterest);//延期利息
			dialogueMap.put(LoanConstant.OLD_AMOUNT, StringUtils.decimalToStr(loanRecord.getAmount(), 2));
			Date parseDate = DateUtils.parseDate(repayDateAfterDelay);
			dialogueMap.put(LoanConstant.LAST_REPAY_DATE, DateUtils.formatDate(parseDate, "yyyy-MM-dd"));
			dialogueMap.put(LoanConstant.OLD_INTEREST, StringUtils.decimalToStr(loanRecord.getInterest(), 2));
			dialogueMap.put(LoanConstant.CURRENT_REPAY_DATE, DateUtils.formatDate(loanRecord.getDueRepayDate(), "yyyy-MM-dd"));
			
			NfsLoanDetailMessage loanDetailMessage = new NfsLoanDetailMessage();
			if(isLoaner){
				loanDetailMessage.setDetail(loanRecord.getLoanApplyDetail());
				loanDetailMessage.setMember(member);
				loanDetailMessage.setNote(JSON.toJSONString(dialogueMap));
				loanDetailMessage.setMessageId(RecordMessage.CHAT_9);
				loanDetailMessage.setType(RecordMessage.LENDER_EXTENSION_REPAYMENT);
				loanDetailMessageService.save(loanDetailMessage);
				
				//发送会员消息
				sendMessage = memberMessageService.sendMessage(MemberMessage.Type.extensionApplication,loanDelayApply.getId());
			}else{
				loanDetailMessage.setDetail(loanRecord.getLoanApplyDetail());
				loanDetailMessage.setMember(member);
				loanDetailMessage.setNote(JSON.toJSONString(dialogueMap));
				loanDetailMessage.setMessageId(RecordMessage.CHAT_3);
				loanDetailMessage.setType(RecordMessage.BORROWER_EXTENSION_REPAYMENT);
				loanDetailMessageService.save(loanDetailMessage);
				
				//发送会员消息
				sendMessage = memberMessageService.sendMessage(MemberMessage.Type.extensionApplication,loanDelayApply.getId());
				
			}
			//推送
			sendMsgService.beforeSendAppMsg(sendMessage);
			LoanDetailForAppResponseResult detail = loanRecordService.getDetail(loanRecord.getId().toString(),2,member);
			
			return ResponseData.success("申请延期成功,待对方确认",detail);
		}else{
			/**
			 * 部分还款 or 部分还款+延期
			 */
			if(isLoaner){
				loanRecord.setPartialStatus(NfsLoanRecord.PartialStatus.loanerApplyPartial);
			}else{
				loanRecord.setPartialStatus(NfsLoanRecord.PartialStatus.loaneeApplyPartial);
			}
			
			//生成部分还款申请
			loanDelayApply.setPartialAmount(partialAmountBig);
			BigDecimal remainAmount = dueRepayAmount.subtract(partialAmountBig);
			loanDelayApply.setRemainAmount(remainAmount);
			loanDelayApply.setPayStatus(PayStatus.noPay);
			loanDelayApply.setStatus(NfsLoanPartialAndDelay.Status.confirm);
			loanDelayApply.setOldInterest(loanRecord.getInterest());
			loanDelayApply.setOldRate(loanRecord.getIntRate());
			loanDelayApply.setOldAmount(amount);
			loanDelayApply.setNowRepayDate(loanRecord.getDueRepayDate());
			Integer delayDays = 0;
			loanDelayApply.setType(NfsLoanPartialAndDelay.Type.partial);
			
			if(StringUtils.isNotEmpty(repayDateAfterDelay)){
				Date repayDateAfterDelayDate = DateUtils.parseDate(repayDateAfterDelay);
				delayDays = DateUtils.getDistanceOfTwoDate(dueRepayDate,repayDateAfterDelayDate);
				loanDelayApply.setDelayDays(delayDays);
				if(delayDays > 0){//有延期
					loanDelayApply.setType(NfsLoanPartialAndDelay.Type.partialAndDelay);
					loanDelayApply.setNowRepayDate(DateUtils.addCalendarByDate(loanRecord.getDueRepayDate(), delayDays));
					if(isLoaner){
						loanRecord.setDelayStatus(NfsLoanRecord.DelayStatus.loanerApplyDelay);
					}else{
						loanRecord.setDelayStatus(NfsLoanRecord.DelayStatus.loaneeApplyDelay);
					}
				}
			}else{
				loanDelayApply.setDelayDays(0);
			}
			
			BigDecimal nowAmount = amount.add(interest).subtract(partialAmountBig);
			if(StringUtils.isNotEmpty(delayInterest)  && !delayInterest.equals("0")){
				BigDecimal delayInterestBig = new BigDecimal(delayInterest);
				delayRateBig = LoanUtils.getIntRate(nowAmount, delayDays, delayInterestBig);
			}else{
				loanDelayApply.setDelayRate(BigDecimal.ZERO);
			}
			
			BigDecimal realRate = delayRateBig.multiply(new BigDecimal("100"));
			if(realRate.compareTo(new BigDecimal("24")) > 0){
				return ResponseData.error("延期期间利率不能超过24%");
			}
			
			loanRecordService.save(loanRecord);
			//计算延期期间的还款计划
			NfsLoanRecord afterDelayRecord = LoanUtils.calInt(nowAmount, realRate, 
					NfsLoanApply.RepayType.oneTimePrincipalAndInterest, loanDelayApply.getDelayDays());
			loanDelayApply.setDelayInterest(afterDelayRecord.getInterest());
			loanDelayApply.setDelayRate(realRate);
			loanPartialAndDelayService.save(loanDelayApply);
			
			Map<String, String> dialogueMap = new HashMap<String, String>();
			dialogueMap.put(LoanConstant.OLD_AMOUNT, StringUtils.decimalToStr(loanRecord.getAmount(), 2));
			dialogueMap.put(LoanConstant.OLD_INTEREST, StringUtils.decimalToStr(loanRecord.getInterest(), 2));
			dialogueMap.put(LoanConstant.DELAY_INTEREST, delayInterest);//延期利息
			dialogueMap.put(LoanConstant.CURRENT_REPAY_DATE, DateUtils.formatDate(loanRecord.getDueRepayDate(), "yyyy-MM-dd"));
			dialogueMap.put(LoanConstant.LAST_REPAY_DATE, repayDateAfterDelay);
			dialogueMap.put(LoanConstant.PARTIAL_PAYMENT, partialAmountBig.intValue() + "");
			BigDecimal delayInterestBig = new BigDecimal(delayInterest);
			BigDecimal remainRepayAmount = dueRepayAmount.subtract(partialAmountBig).add(delayInterestBig).setScale(2, BigDecimal.ROUND_HALF_UP);
			if(isLoaner){
				dialogueMap.put(LoanConstant.REMAIN_PAYMENT, remainRepayAmount + "");
			}
			
			//发送对话 注:借款人发起申请走H5页面
			NfsLoanDetailMessage nfsLoanDetailMessage = new NfsLoanDetailMessage();
			nfsLoanDetailMessage.setDetail(loanRecord.getLoanApplyDetail());
			nfsLoanDetailMessage.setMember(member);
			nfsLoanDetailMessage.setNote(JSON.toJSONString(dialogueMap));
			nfsLoanDetailMessage.setMessageId(RecordMessage.CHAT_8);
			nfsLoanDetailMessage.setType(RecordMessage.LENDER_PART_REPAYMENT);
			loanDetailMessageService.save(nfsLoanDetailMessage);
			
			//发送会员消息
			sendMessage = memberMessageService.sendMessage(MemberMessage.Type.partialRepaymentApplication,loanDelayApply.getId());
			//推送
			sendMsgService.beforeSendAppMsg(sendMessage);
			LoanDetailForAppResponseResult detail = loanRecordService.getDetail(loanRecord.getId().toString(),2,member);
			return ResponseData.success("部分还款申请成功,待对方确认",detail);
		} 
	}
}