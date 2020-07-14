package com.jxf.loan.service;

import com.jxf.svc.sys.crud.service.CrudService;
import com.jxf.loan.entity.NfsCrAuction;
import com.jxf.loan.entity.NfsCrContract;

/**
 * 债转合同Service
 * @author suHuimin
 * @version 2019-03-12
 */
public interface NfsCrContractService extends CrudService<NfsCrContract> {

	/**
	 * 有盾合同盖章
	 * 
	 * @param 债权转让记录的id
	 */
	void createYDPdf(NfsCrAuction crAuction);

	/**
	 * 下载 更新认证状态和url（有盾）
	 * 
	 * @param loanCenterId 电子借条id
	 * @return
	 */
	void updateYDUrl(Long crContractId);

	/**
	 * 生成合同记录
	 * 
	 * @param loan 
	 */
	void createContract(NfsCrAuction crAuction);

	/**
	 * 生成君子签章
	 * 
	 * @param loan
	 */
	void createPdf(NfsCrAuction crAuction);

	/**
	 * 下载君子签章合同
	 * 
	 * @param loan
	 */
	void updateAuth(Long crId);
	
	/**
	 * 根据债转记录Id获取债转合同
	 * @param crContract
	 * @return
	 */
	NfsCrContract getCrContractByCrId(Long crId);

}