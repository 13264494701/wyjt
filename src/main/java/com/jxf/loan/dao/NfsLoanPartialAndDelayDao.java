package com.jxf.loan.dao;

import org.apache.ibatis.annotations.Param;

import com.jxf.loan.entity.NfsLoanPartialAndDelay;
import com.jxf.svc.sys.crud.dao.CrudDao;
import com.jxf.svc.annotation.MyBatisDao;

/**
 * 部分还款和延期DAO接口
 * @author XIAORONGDIAN
 * @version 2018-12-11
 */
@MyBatisDao
public interface NfsLoanPartialAndDelayDao extends CrudDao<NfsLoanPartialAndDelay> {

	/**
	 * 获取已同意的部分还款申请
	 * @param loanId
	 * @return
	 */
	int getAgreedApplyCount(@Param("loanId")Long loanId);

	/**
	 * 根据借条ID获取最近的申请
	 * @param loanId
	 * @return
	 */
	NfsLoanPartialAndDelay findNearestByRecordId(@Param("loanId")Long loanId);
	
}