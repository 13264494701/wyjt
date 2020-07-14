package com.jxf.loan.service;


import java.util.ArrayList;
import java.util.List;

import com.jxf.loan.entity.NfsLoanPartialAndDelay;
import com.jxf.loan.entity.NfsLoanRecord;
import com.jxf.mem.entity.Member;
import com.jxf.payment.entity.Payment;
import com.jxf.svc.sys.crud.service.CrudService;
import com.jxf.web.model.ResponseData;

/**
 * 部分还款和延期Service
 * @author XIAORONGDIAN
 * @version 2018-12-11
 */
public interface NfsLoanPartialAndDelayService extends CrudService<NfsLoanPartialAndDelay> {

	
	/**
	 * 获取已同意的部分还款申请
	 * @param loanId
	 * @return
	 */
	int getAgreedApplyCount(Long loanId);

	/**
	 * 取消延期/部分还款
	 * @param loanPartialRepayApply
	 * @param member
	 * @param record
	 */
	void cancelPartialPayOrDelay(NfsLoanPartialAndDelay loanPartialRepayApply, Member member, NfsLoanRecord record);
	/**
	 * 同意/拒绝 部分还款/延期
	 * @param nfsLoanPartialRepayApply
	 * @param me
	 * @param record
	 * @param nfsLoanPartialRepayApply 
	 * @param isAgree 
	 */
	ArrayList<Object> answerPartialPay(NfsLoanRecord record, Member me, NfsLoanPartialAndDelay nfsLoanPartialRepayApply, String isAgree);

	/**
	 * 根据借条ID查最近的部分还款申请
	 * @param loanId
	 * @return
	 */
	NfsLoanPartialAndDelay findNearestPartialAndDelayByRecordId(Long loanId);
	
	
	
	void filter(List<NfsLoanPartialAndDelay> loanPartialRepayApplyList);
	/**
	 * 放款人同意延期/拒绝延期 借款人拒绝延期
	 * @param loanPartialRepayApply 
	 * @param member 
	 * @param isAgree 
	 * @param loanId
	 * @return
	 */
	ResponseData answerDelay(NfsLoanRecord loanRecord, Member member, NfsLoanPartialAndDelay loanPartialRepayApply, int isAgree);
	/**
	 * 借款人使用余额支付延期费用
	 * @param loanPartialAndDelay
	 */
	ResponseData loaneePayForDelayByActBal(NfsLoanPartialAndDelay loanPartialAndDelay);
	/**
	 * 借款人使用微信支付延期费用
	 * @param loanPartialAndDelay
	 */
	boolean loaneePayForDelayByWx(Payment payment);
	
	/**
	 * 借款人取消申请延期
	 * @param loanPartialRepayApply
	 * @return
	 */
	int loaneeCancelDelay(NfsLoanPartialAndDelay loanPartialRepayApply);

	
	NfsLoanPartialAndDelay getLoanerPartialApplyForApp(NfsLoanRecord loanRecord);
	
	
}