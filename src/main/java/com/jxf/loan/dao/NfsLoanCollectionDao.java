package com.jxf.loan.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jxf.loan.entity.NfsLoanCollection;
import com.jxf.loan.entity.NfsLoanRecord;
import com.jxf.svc.annotation.MyBatisDao;
import com.jxf.svc.sys.crud.dao.CrudDao;

/**
 * 借条催收DAO接口
 * @author gaobo
 * @version 2018-11-07
 */
@MyBatisDao
public interface NfsLoanCollectionDao extends CrudDao<NfsLoanCollection> {


	/**
	 * 获取催收中记录
	 * @param loanerId
	 * @param collection
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	List<NfsLoanCollection> findInCollection(@Param("loanerId") Long loanerId,@Param("collection") NfsLoanCollection collection);

	/**
	 * 获取催收完成记录
	 * @param loanerId
	 * @param collection
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	List<NfsLoanCollection> findEndOfCollection(@Param("loanerId") Long loanerId,@Param("collection") NfsLoanCollection collection);

	/**
	 * 获取催收记录
	 * @param loanId
	 * @return
	 */
	NfsLoanCollection getCollectionByLoanId(Long loanId);

	/**
	 * 获取当前的催收实体
	 * @param loanRecord
	 * @return
	 */
	NfsLoanCollection findNowCollection(NfsLoanRecord loanRecord);

	/**
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
	List<NfsLoanCollection> findCollections(@Param("loanId") Long loanId);
}