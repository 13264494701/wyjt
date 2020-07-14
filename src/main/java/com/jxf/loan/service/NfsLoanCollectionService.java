package com.jxf.loan.service;

import com.jxf.loan.entity.NfsLoanCollection;
import com.jxf.loan.entity.NfsLoanCollectionDetail;
import com.jxf.loan.entity.NfsLoanRecord;
import com.jxf.mem.entity.Member;
import com.jxf.svc.persistence.Page;
import com.jxf.svc.sys.crud.service.CrudService;

/**
 * 	借条催收Service
 * @author gaobo
 * @version 2018-11-07
 */
public interface NfsLoanCollectionService extends CrudService<NfsLoanCollection> {



	/**
	 * 获取催收中记录
	 * @param loanerId
	 * @param collection
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	Page<NfsLoanCollection> findInCollection(Long loanerId,NfsLoanCollection collection,Integer pageNo,Integer pageSize);
	
	
	/**
	 * 获取催收完成记录
	 * @param loanerId
	 * @param collection
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	Page<NfsLoanCollection> findEndOfCollection(Long loanerId,NfsLoanCollection collection,Integer pageNo,Integer pageSize);

	/**
	 * 获取催收记录
	 * @param loanId
	 * @return
	 */
	NfsLoanCollection getCollectionByLoanId(Long loanId);


	/***
	 * 获取当前的催收实体
	 * @param loanRecord
	 * @return
	 */
	NfsLoanCollection findNowCollection(NfsLoanRecord loanRecord);

	/***
	 * 获取第三次催收实体
	 * @param loanRecord
	 * @return
	 */
	NfsLoanCollection findThirdCollection(NfsLoanRecord loanRecord);
	
	/***
	 * 借款单关闭关闭催收记录
	 * @param loanId
	 * @return
	 */
	int close(Long loanId);
	/**
	 * 催收服务费预支付
	 * @param collection
	 * @param collectionDetail
	 * @return
	 */
	int payForApplyCollection(Member member, NfsLoanCollection collection,NfsLoanCollectionDetail collectionDetail);
	
}