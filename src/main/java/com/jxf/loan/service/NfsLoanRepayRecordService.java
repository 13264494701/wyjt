package com.jxf.loan.service;

import java.math.BigDecimal;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jxf.loan.entity.NfsLoanOperatingRecord;
import com.jxf.loan.entity.NfsLoanPartialAndDelay;
import com.jxf.loan.entity.NfsLoanRecord;
import com.jxf.loan.entity.NfsLoanRepayRecord;
import com.jxf.svc.sys.crud.service.CrudService;


/**
 * 还款计划Service
 * @author wo
 * @version 2018-11-15
 */
public interface NfsLoanRepayRecordService extends CrudService<NfsLoanRepayRecord> {

	/**
	 *     根据借条id获取已还记录
	 * @param loanId
	 * @return
	 */
	List<NfsLoanRepayRecord> getRepayRecordsByLoanId(Long loanId);

	/**
	 * 根据借条id查询还款记录
	 * @param loanId
	 * @return
	 */
	List<NfsLoanRepayRecord> findNotPendingRecordsByLoanId(Long loanId);

	/**
	 * 计算分期有多少期逾期
	 * @param recordId
	 * @return
	 */
	Integer countOverdueTimes(Long recordId);
	
	/**
	 * 借款人申请部分还款
	 * @param loanee
	 * @param loanRecord
	 * @param repayAmount
	 * @return
	 */
	int loaneeApplyPartialRepay(NfsLoanRecord loanRecord, NfsLoanPartialAndDelay partialAndDelay);
	
	/**
	 * 借款人同意部分还款和延期申请
	 * @param loanee
	 * @param loaner
	 * @param loanRecord
	 * @param partialAndDelay
	 * @return
	 */
	boolean agreePartialRepayAndDelay(NfsLoanRecord loanRecord,NfsLoanPartialAndDelay partialAndDelay,NfsLoanOperatingRecord operatingRecord);
	
	/**
	 *     根据借条id获取已还记录 (分期用)
	 * @param loanId
	 * @return
	 */
	List<NfsLoanRepayRecord> findPendingRepayList(NfsLoanRepayRecord record);
	
	/**
	 * 全部还款
	 * @param loanId
	 * @param overdueInterest
	 * @return
	 */
	int repayAll(NfsLoanRecord loanRecord, BigDecimal dueRepayAmount,BigDecimal overdueInterest ) ;
	
	/**
	 * 	分期还款
	 * @param repayAmount 还款金额
	 * @param overdueInterest 当前逾期利息
	 * @param loanRecord 借条
	 * @param shouldRepayRecords 要还的还款记录
	 * @return
	 */
	int stagesRepay(BigDecimal repayAmount,BigDecimal overdueInterest,NfsLoanRecord loanRecord,List<NfsLoanRepayRecord> shouldRepayRecords);
	
	/**
	 * 计算借条总金额
	 * @param loanIds
	 * @return
	 */
	BigDecimal sumLoanAmount(@Param("loanIds")String loanIds);
	
}