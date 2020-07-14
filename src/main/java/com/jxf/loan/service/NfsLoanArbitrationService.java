package com.jxf.loan.service;

import java.util.List;

import com.jxf.loan.entity.NfsLoanArbitration;

import com.jxf.mem.entity.Member;
import com.jxf.payment.entity.Payment;
import com.jxf.svc.persistence.Page;
import com.jxf.svc.sys.crud.service.CrudService;


/**
 * 借条仲裁增删改查Service
 * @author LIUHUAIXIN
 * @version 2018-11-07
 */
public interface NfsLoanArbitrationService extends CrudService<NfsLoanArbitration> {

	/**
	 * 查询全部仲裁中的借条
	 * @param loanId
	 * @param arbitration
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	Page<NfsLoanArbitration> findInArbitration(Long loanerId,NfsLoanArbitration nfsLoanArbitration,Integer pageNo,Integer pageSize,int trxType);
	
	/**
	 * 查询全部仲裁出裁决的借条
	 * @param loanId
	 * @param arbitration
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	Page<NfsLoanArbitration> findArbitration(Long loanerId,NfsLoanArbitration nfsLoanArbitration,Integer pageNo,Integer pageSize,int trxType);
	
	/**
	 * 查询全部仲裁已完成的借条
	 * @param loanId
	 * @param arbitration
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	Page<NfsLoanArbitration> findEndOfArbitration(Long loanerId,NfsLoanArbitration nfsLoanArbitration,Integer pageNo,Integer pageSize,int trxType);

	/**
	 * 获取仲裁证据列表
	 * @return
	 */
	List<NfsLoanArbitration> findTransferCase();
	
	/**
	 *获取可申请强执列表
	 */
	Page<NfsLoanArbitration> findExecution(Long id,NfsLoanArbitration nfsLoanArbitration,Integer pageNo,Integer pageSize,int trxType);

	/**
	 * 我的仲裁记录
	 * @param nfsLoanArbitration
	 * @return
	 */
	List<NfsLoanArbitration> findMemberArbitrationList(NfsLoanArbitration nfsLoanArbitration);
	
	/**
	 * 根据借条id查询仲裁记录
	 * @param loanId
	 * @return
	 */
	NfsLoanArbitration getByLoanId(Long loanId);
	
	/**
	 * 关闭借条 修改仲裁状态
	 */
	int close(Long loanId);
	/**
	 * 仲裁申请预缴费支付
	 * @return
	 */
	int payForApplyArbitration(Member member,NfsLoanArbitration arbitration);

	/**
	 * 公信堂仲裁申请预缴费支付
	 * @param loanArbitration
	 */
	boolean payForApplyByWx(Payment payment);
	
}