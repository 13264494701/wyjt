package com.jxf.web.app.wyjt.loan;


import java.math.BigDecimal;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.jxf.loan.constant.LoanConstant;
import com.jxf.loan.entity.NfsLoanOperatingRecord;
import com.jxf.loan.entity.NfsLoanOperatingRecord.Type;
import com.jxf.loan.entity.NfsLoanRecord;
import com.jxf.loan.service.NfsLoanOperatingRecordService;
import com.jxf.loan.service.NfsLoanRecordService;
import com.jxf.mem.entity.Member;
import com.jxf.mem.service.MemberService;
import com.jxf.svc.utils.DateUtils;
import com.jxf.web.admin.sys.BaseController;
import com.jxf.web.model.ResponseData;
import com.jxf.web.model.wyjt.app.loan.OperatingRecordDetailRequestParam;
import com.jxf.web.model.wyjt.app.loan.OperatingRecordDetailResponseResult;
import com.jxf.web.model.wyjt.app.loan.OperatingRecordRequestParam;
import com.jxf.web.model.wyjt.app.loan.OperatingRecordResponseResult;
import com.jxf.web.model.wyjt.app.loan.OperatingRecordDetailResponseResult.OperatingDetail;
import com.jxf.web.model.wyjt.app.loan.OperatingRecordResponseResult.OperatingRecord;

/**
 * 借条操作记录Controller
 * @author XIAORONGDIAN
 * @version 2018-12-18
 */
@Controller("wyjtAppLoanOperatingRecordController")
@RequestMapping(value = "${wyjtApp}/loan")
public class LoanOperatingRecordController extends BaseController {

	@Autowired
	private NfsLoanOperatingRecordService nfsLoanOperatingRecordService;
	@Autowired
	private MemberService memberService;
	@Autowired
	private NfsLoanRecordService loanRecordService;
	/**
	 *  查看更多借条历史记录
	 */
	@RequestMapping(value = "/operatingRecordList")
	public @ResponseBody ResponseData loanHistory(HttpServletRequest request) {
		Member member = memberService.getCurrent();
		String param = request.getParameter("param");
		OperatingRecordRequestParam reqData = JSONObject.parseObject(param, OperatingRecordRequestParam.class);
		OperatingRecordResponseResult result = new OperatingRecordResponseResult();
		String recordId = reqData.getLoanId();
		
		NfsLoanRecord nfsLoanRecord = new NfsLoanRecord();
		nfsLoanRecord.setId(Long.parseLong(recordId));
		nfsLoanRecord = loanRecordService.get(nfsLoanRecord);
		NfsLoanOperatingRecord operatingRecord = new NfsLoanOperatingRecord();
		operatingRecord.setOldRecord(nfsLoanRecord);
		List<NfsLoanOperatingRecord> findOperatingRecordList = nfsLoanOperatingRecordService.findList(operatingRecord);
		
		List<OperatingRecord> operatingRecordRecords = result.getOperatingRecordRecords();
		boolean isLoaner = false;
		Member loaner = nfsLoanRecord.getLoaner();
		if(member.getId().compareTo(loaner.getId()) == 0){
			isLoaner = true;
		}
		
		for (NfsLoanOperatingRecord loanOperatingRecord : findOperatingRecordList) {
			OperatingRecord resultBean = new OperatingRecordResponseResult().new OperatingRecord();
			resultBean.setName(loanOperatingRecord.getType().getName());
			resultBean.setTime(DateUtils.formatDate(loanOperatingRecord.getCreateTime(), "yyyy-MM-dd"));
			resultBean.setOperatingRecordId(loanOperatingRecord.getId().toString());
			Type type = loanOperatingRecord.getType();
			if(isLoaner){// #ED2E24是红的 #313131是黑的
				if(type.equals(Type.create)){//生成借条
					resultBean.setChange("-" + loanOperatingRecord.getNowRecord().getAmount().setScale(2, BigDecimal.ROUND_HALF_UP)+"|#313131");
				} else if(type.equals(Type.partialAndDelay) || type.equals(Type.partial) 
						|| type.equals(Type.principalAndInterestByMonth)){//分期视为部分还款
					resultBean.setChange("+" + loanOperatingRecord.getRepaymentAmount().setScale(2, BigDecimal.ROUND_HALF_UP)+"|#ED2E24");
				} else if(type.equals(Type.totalDueAmount) ||type.equals(Type.lineDown)){//从oldRecord里取本金+利息
					resultBean.setChange("+" + (loanOperatingRecord.getOldRecord().getAmount().add(loanOperatingRecord.getOldRecord().getInterest()))
							.setScale(2, BigDecimal.ROUND_HALF_UP)+"|#ED2E24");
				}
			}else{
				if(type.equals(Type.create)){//生成借条
					resultBean.setChange("+" + loanOperatingRecord.getNowRecord().getAmount().setScale(2, BigDecimal.ROUND_HALF_UP)+"|#ED2E24");
				} else if(type.equals(Type.partialAndDelay) || type.equals(Type.partial) 
						|| type.equals(Type.principalAndInterestByMonth)){//分期视为部分还款
					resultBean.setChange("-" + loanOperatingRecord.getRepaymentAmount().setScale(2, BigDecimal.ROUND_HALF_UP)+"|#313131");
				} else if(type.equals(Type.totalDueAmount) ||type.equals(Type.lineDown)){//从oldRecord里取应还金额
					resultBean.setChange("-" + (loanOperatingRecord.getOldRecord().getAmount().add(loanOperatingRecord.getOldRecord().getInterest()))
							.setScale(2, BigDecimal.ROUND_HALF_UP)+"|#313131");
				}
			}
			if(type.equals(Type.delay)){
				resultBean.setChange("延期" + loanOperatingRecord.getDelayDays() + "日|#313131");
			}
			operatingRecordRecords.add(resultBean);
		}
		result.setOperatingRecordRecords(operatingRecordRecords);
		
		return ResponseData.success("借条记录查询成功", result);
	}
	
	/**
	 *  查看借条历史记录详情
	 */
	@RequestMapping(value = "/operatingRecordDetail")
	public @ResponseBody ResponseData loanHistoryDetail(HttpServletRequest request) {
		
		Member member = memberService.getCurrent();
		String param = request.getParameter("param");
		OperatingRecordDetailRequestParam reqData = JSONObject.parseObject(param, OperatingRecordDetailRequestParam.class);
		OperatingRecordDetailResponseResult result = new OperatingRecordDetailResponseResult();
		String operatingRecordId = reqData.getOperatingRecordId();
		NfsLoanOperatingRecord nfsLoanOperatingRecord = nfsLoanOperatingRecordService.get(Long.parseLong(operatingRecordId));
		
		NfsLoanRecord loanRecord = nfsLoanOperatingRecord.getOldRecord();
		
		
		NfsLoanOperatingRecord.Type type = nfsLoanOperatingRecord.getType();
		boolean isLoaner = true;
		if(member.getId().equals(loanRecord.getLoanee().getId())){
			isLoaner = false;
		}
		
		List<OperatingDetail> detailList = result.getDetailList();
		List<OperatingDetail> nowRecordList = result.getNowRecord();
		
		result.setApplyMemberName(nfsLoanOperatingRecord.getInitiator());//发起方
		result.setTime(DateUtils.formatDate(nfsLoanOperatingRecord.getCreateTime(), "yyyy-MM-dd HH:mm"));//交易时间
		
		result.setTitle(nfsLoanOperatingRecord.getType().getName());
		// #ED2E24是红的 #313131是黑的
		if(type.equals(NfsLoanOperatingRecord.Type.create)){//生成借条
			if(isLoaner){
				result.setChange("-" + nfsLoanOperatingRecord.getNowRecord().getAmount().setScale(2, BigDecimal.ROUND_HALF_UP) + "|#313131");
			}else{
				result.setChange("+" + nfsLoanOperatingRecord.getNowRecord().getAmount().setScale(2, BigDecimal.ROUND_HALF_UP) + "|#ED2E24");
			}
			
			NfsLoanRecord nowRecord = nfsLoanOperatingRecord.getNowRecord();
			OperatingDetail loanAmount = new OperatingRecordDetailResponseResult().new OperatingDetail();
			loanAmount.setName(LoanConstant.LOAN_AMOUNT);
			loanAmount.setValue(nowRecord.getAmount().setScale(2, BigDecimal.ROUND_HALF_UP)+"");
			nowRecordList.add(loanAmount);
			
			OperatingDetail loanInterest = new OperatingRecordDetailResponseResult().new OperatingDetail();
			loanInterest.setName(LoanConstant.LOAN_INTEREST);
			loanInterest.setValue(nowRecord.getInterest().setScale(2, BigDecimal.ROUND_HALF_UP)+"");
			nowRecordList.add(loanInterest);
			
			OperatingDetail repayDate = new OperatingRecordDetailResponseResult().new OperatingDetail();
			repayDate.setName(LoanConstant.LOAN_REPAY_DATE);
			repayDate.setValue(DateUtils.formatDate(nowRecord.getDueRepayDate(), "yyyy-MM-dd"));
			nowRecordList.add(repayDate);
			
		}else if(type.equals(NfsLoanOperatingRecord.Type.totalDueAmount) || type.equals(NfsLoanOperatingRecord.Type.lineDown)){//全额还款
			NfsLoanRecord oldRecord = nfsLoanOperatingRecord.getOldRecord();
			
			if(isLoaner){
				result.setChange("+" + (oldRecord.getAmount().add(oldRecord.getInterest())).setScale(2, BigDecimal.ROUND_HALF_UP) + "|#ED2E24");
			}else{
				result.setChange("-" + (oldRecord.getAmount().add(oldRecord.getInterest())).setScale(2, BigDecimal.ROUND_HALF_UP) + "|#313131");
			}
			//应还本金
			OperatingDetail shouldGiveAmount = new OperatingRecordDetailResponseResult().new OperatingDetail();
			shouldGiveAmount.setName(LoanConstant.SHOULD_GIVE_AMOUNT);
			shouldGiveAmount.setValue(oldRecord.getAmount().setScale(2, BigDecimal.ROUND_HALF_UP) + "" );
			detailList.add(shouldGiveAmount);
			
			if(oldRecord.getInterest() != null && oldRecord.getInterest().compareTo(BigDecimal.ZERO) > 0){
				//应还利息
				OperatingDetail shouldGiveInterest = new OperatingRecordDetailResponseResult().new OperatingDetail();
				shouldGiveInterest.setName(LoanConstant.SHOULD_GIVE_INTEREST);
				shouldGiveInterest.setValue(oldRecord.getInterest().setScale(2, BigDecimal.ROUND_HALF_UP) + "" );
				detailList.add(shouldGiveInterest);
			}
			
			if(oldRecord.getOverdueInterest() != null && oldRecord.getOverdueInterest().compareTo(BigDecimal.ZERO) > 0){
				//逾期利息
				OperatingDetail overDueInterest = new OperatingRecordDetailResponseResult().new OperatingDetail();
				overDueInterest.setName(LoanConstant.OVER_DUE_INTEREST);
				overDueInterest.setValue(oldRecord.getOverdueInterest().setScale(2, BigDecimal.ROUND_HALF_UP) + "" );
				detailList.add(overDueInterest);
			}
			
			//全额还款金额
			OperatingDetail totalAmountDetail = new OperatingRecordDetailResponseResult().new OperatingDetail();
			totalAmountDetail.setName(LoanConstant.TOTAL_AMOUNT);
			totalAmountDetail.setValue((oldRecord.getAmount().add(oldRecord.getInterest())).setScale(2, BigDecimal.ROUND_HALF_UP) + "" );
			detailList.add(totalAmountDetail);
			
			//借条本金
			OperatingDetail loanAmount = new OperatingRecordDetailResponseResult().new OperatingDetail();
			loanAmount.setName(LoanConstant.LOAN_AMOUNT);
			loanAmount.setValue("0.00");
			nowRecordList.add(loanAmount);
			
			//借条利息
			OperatingDetail loanInterest = new OperatingRecordDetailResponseResult().new OperatingDetail();
			loanInterest.setName(LoanConstant.LOAN_INTEREST);
			loanInterest.setValue("0.00");
			nowRecordList.add(loanInterest);
			
			//借条状态
			OperatingDetail loanStatus = new OperatingRecordDetailResponseResult().new OperatingDetail();
			loanStatus.setName(LoanConstant.LOAN_STATUS);
			loanStatus.setValue("已完成");
			nowRecordList.add(loanStatus);
			
		}else if(type.equals(NfsLoanOperatingRecord.Type.delay) || type.equals(NfsLoanOperatingRecord.Type.partial) 
				|| type.equals(NfsLoanOperatingRecord.Type.partialAndDelay) 
				|| type.equals(NfsLoanOperatingRecord.Type.principalAndInterestByMonth)){//延期还款 or 部分还款 or 部分还款+延期 or 分期还款
			
			if(type.equals(NfsLoanOperatingRecord.Type.delay)){
				result.setChange(nfsLoanOperatingRecord.getDelayDays()+"天|#313131");
			}else{
				if(isLoaner){
					result.setChange("+" + nfsLoanOperatingRecord.getRepaymentAmount().setScale(2, BigDecimal.ROUND_HALF_UP) + "|#ED2E24");
				}else{
					result.setChange("-" + nfsLoanOperatingRecord.getRepaymentAmount().setScale(2, BigDecimal.ROUND_HALF_UP) + "|#313131");
				}
			}
			
			//应还本金
			OperatingDetail shouldGiveAmount = new OperatingRecordDetailResponseResult().new OperatingDetail();
			shouldGiveAmount.setName(LoanConstant.SHOULD_GIVE_AMOUNT);
			shouldGiveAmount.setValue(loanRecord.getAmount().setScale(2, BigDecimal.ROUND_HALF_UP) + "" );
			detailList.add(shouldGiveAmount);
			
			if(loanRecord.getInterest() != null && loanRecord.getInterest().compareTo(BigDecimal.ZERO) > 0){
				//应还利息
				OperatingDetail shouldGiveInterest = new OperatingRecordDetailResponseResult().new OperatingDetail();
				shouldGiveInterest.setName(LoanConstant.SHOULD_GIVE_INTEREST);
				shouldGiveInterest.setValue(loanRecord.getInterest().setScale(2, BigDecimal.ROUND_HALF_UP) + "" );
				detailList.add(shouldGiveInterest);
			}
			
			if(nfsLoanOperatingRecord.getRepaymentAmount() != null && nfsLoanOperatingRecord.getRepaymentAmount().compareTo(BigDecimal.ZERO) > 0){
				//部分还款金额
				OperatingDetail partialPayAmount = new OperatingRecordDetailResponseResult().new OperatingDetail();
				partialPayAmount.setName(LoanConstant.PARTIAL_PAY_AMOUNT);
				partialPayAmount.setValue(nfsLoanOperatingRecord.getRepaymentAmount().setScale(2, BigDecimal.ROUND_HALF_UP) + "" );
				detailList.add(partialPayAmount);
			}
			
			if(nfsLoanOperatingRecord.getDelayInterest() != null && nfsLoanOperatingRecord.getDelayInterest().compareTo(BigDecimal.ZERO) > 0){
				//延期利息
				OperatingDetail delayInterest = new OperatingRecordDetailResponseResult().new OperatingDetail();
				delayInterest.setName(LoanConstant.TXT_DELAY_INTEREST);
				delayInterest.setValue(nfsLoanOperatingRecord.getDelayInterest().setScale(2, BigDecimal.ROUND_HALF_UP) + "" );
				detailList.add(delayInterest);
			}
			if(loanRecord.getOverdueInterest() != null && loanRecord.getOverdueInterest().compareTo(BigDecimal.ZERO) > 0){
				//逾期利息
				OperatingDetail overDueInterest = new OperatingRecordDetailResponseResult().new OperatingDetail();
				overDueInterest.setName(LoanConstant.OVER_DUE_INTEREST);
				overDueInterest.setValue(loanRecord.getOverdueInterest().setScale(2, BigDecimal.ROUND_HALF_UP) + "" );
				detailList.add(overDueInterest);
			}
			
			if(nfsLoanOperatingRecord.getDelayDays() != null &&  nfsLoanOperatingRecord.getDelayDays() > 0){
				//延长时间
				OperatingDetail delayDays = new OperatingRecordDetailResponseResult().new OperatingDetail();
				delayDays.setName(LoanConstant.DELAY_DAYS);
				delayDays.setValue(nfsLoanOperatingRecord.getDelayDays() + "天" );
				detailList.add(delayDays);
			}
			
			NfsLoanRecord nowRecord = nfsLoanOperatingRecord.getNowRecord();
			//借条本金
			OperatingDetail loanAmount = new OperatingRecordDetailResponseResult().new OperatingDetail();
			loanAmount.setName(LoanConstant.LOAN_AMOUNT);
			loanAmount.setValue(nowRecord.getAmount().setScale(2, BigDecimal.ROUND_HALF_UP) + "");
			nowRecordList.add(loanAmount);
			
			//借条利息
			OperatingDetail loanInterest = new OperatingRecordDetailResponseResult().new OperatingDetail();
			loanInterest.setName(LoanConstant.LOAN_INTEREST);
			loanInterest.setValue(nowRecord.getInterest().setScale(2, BigDecimal.ROUND_HALF_UP) + "");
			nowRecordList.add(loanInterest);
			
			//还款时间
			OperatingDetail repayDate = new OperatingRecordDetailResponseResult().new OperatingDetail();
			repayDate.setName(LoanConstant.LOAN_REPAY_DATE);
			repayDate.setValue(DateUtils.formatDate(nowRecord.getDueRepayDate(), "yyyy-MM-dd") );
			nowRecordList.add(repayDate);
			
		} 
		result.setDetailList(detailList);
		return ResponseData.success("详情查询成功",result);
	}
	
	
}