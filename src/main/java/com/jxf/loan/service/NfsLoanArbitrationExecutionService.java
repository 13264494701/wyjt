package com.jxf.loan.service;


import java.math.BigDecimal;

import com.jxf.loan.entity.NfsLoanArbitration;
import com.jxf.loan.entity.NfsLoanArbitrationExecution;
import com.jxf.loan.entity.NfsLoanRecord;
import com.jxf.mem.entity.Member;
import com.jxf.payment.entity.Payment;
import com.jxf.svc.persistence.Page;
import com.jxf.svc.sys.crud.service.CrudService;


/**
 * 强执Service
 * @author LIUHUAIXIN
 * @version 2018-12-20
 */
public interface NfsLoanArbitrationExecutionService extends CrudService<NfsLoanArbitrationExecution> {

	/**
	 *获取强执列表
	 */
	Page<NfsLoanArbitrationExecution> findExecution(NfsLoanArbitrationExecution nfsLoanArbitrationExecution,Integer pageNo,Integer pageSize);
	
	/**
	 *获取强执列表(结束)
	 */
	Page<NfsLoanArbitrationExecution> findExecuted(Long id,NfsLoanArbitrationExecution nfsLoanArbitrationExecution,Integer pageNo,Integer pageSize);
	
	/**
	 * 申请强执
	 * @return
	 */
	int apply(NfsLoanRecord nfsLoanRecord,NfsLoanArbitration nfsLoanArbitration);
	
	/**
	 * 借条关闭  关闭强执
	 * @return
	 */
	int close(Long loanId);
	/**
	 * 强执缴费支付
	 * @param member
	 * @param execution
	 * @param executionDetail
	 * @return
	 */
	int payForExecutionApply(Member member,NfsLoanArbitrationExecution execution);

	/**
	 * 公信堂申请强执
	 * @return
	 */
	NfsLoanArbitrationExecution applyForGxt(NfsLoanRecord loanRecord, NfsLoanArbitration loanArbitration, BigDecimal fee);
	
	/**
	 * 公信堂申请强执缴费
	 * @param payment
	 * @return
	 */
	Boolean payForApplyByWx(Payment payment);
	
	/**
	 * 根据仲裁id获取强执记录
	 * @param arbitrationId
	 * @return
	 */
	NfsLoanArbitrationExecution findByArbitrationId(Long arbitrationId);
}