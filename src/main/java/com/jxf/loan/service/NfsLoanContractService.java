package com.jxf.loan.service;


import com.jxf.svc.sys.crud.service.CrudService;


import java.util.List;


import com.jxf.loan.entity.NfsLoanContract;
import com.jxf.loan.entity.NfsLoanContract.SignatureType;
import com.jxf.loan.entity.NfsLoanRecord;

/**
 * 合同表Service
 * 
 * @author lmy
 * @version 2019-01-08
 */
public interface NfsLoanContractService extends CrudService<NfsLoanContract> {

	/**
	 * 有盾合同盖章 有盾那边接口已废弃不用，此方法作废
	 * 
	 * @param loanCenterId loanNum 记录的id
	 */
	void createYDPdf(NfsLoanRecord loan, Long loanNum);

	/**
	 * 下载 更新认证状态和url（有盾）  有盾那边接口已废弃不用，此方法作废
	 * 
	 * @param loanCenterId 电子借条id
	 * @return
	 */
	void updateYDUrl(NfsLoanRecord loan, Long loanNum);
	
	/**
	 * 有盾合同盖章
	 * 
	 * @param loanCenterId loanNum 记录的id
	 */
	void createYDPdfV2(NfsLoanRecord loanRecord, Long loanContractId);

	/**
	 * 下载 更新认证状态和url（有盾）
	 * 
	 * @param loanCenterId 电子借条id
	 * @return
	 */
	void updateYDUrlV2(NfsLoanContract loanContract);

	/**
	 * 生成合同记录
	 * 
	 * @param loan 
	 */
	void createContract(NfsLoanRecord loanRecord,SignatureType type);

	/**
	 * 生成君子签章
	 * 
	 * @param loan
	 */
	void createPdf(NfsLoanRecord loan,Long loanNum);

	/**
	 * 下载君子签章合同
	 * 
	 * @param loan
	 */
	void updateAuth(Long loanNum);

	
	List<NfsLoanContract> getContractByLoanId(NfsLoanContract loanContract);
	
	/**
	 * 根据loanId查当前合同
	 * @param nfsLoanContract
	 */
	NfsLoanContract getCurrentContractByLoanId(Long loanId);
}