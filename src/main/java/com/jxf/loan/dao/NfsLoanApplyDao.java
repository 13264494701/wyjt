package com.jxf.loan.dao;

import com.jxf.svc.annotation.MyBatisDao;
import com.jxf.svc.sys.crud.dao.CrudDao;

import java.util.List;

import com.jxf.loan.entity.NfsLoanApply;


/**
 * 借款申请DAO接口
 * @author wo
 * @version 2018-09-26
 */
@MyBatisDao
public interface NfsLoanApplyDao extends CrudDao<NfsLoanApply> {
	
	List<NfsLoanApply> findSingleLoanApplyList(NfsLoanApply nfsLoanApply);
	
	List<NfsLoanApply> findMultipleLoanApplyList(NfsLoanApply nfsLoanApply);

	/**
	 * 分页获取单人申请给APP展示
	 * @param loanApply
	 * @return
	 */
	List<NfsLoanApply> findSingleLoanApplyListForApp(NfsLoanApply loanApply);
	/**
	 * 分页获取member收到的和发起的申请给APP展示
	 * @param loanApply
	 * @return
	 */
	List<NfsLoanApply> findMemberApplyListForApp(NfsLoanApply loanApply);
	/**
	 * 仅用来处理没生成detail错误
	 * @param loanApply
	 */
	List<NfsLoanApply>  findNoDetailApply(NfsLoanApply loanApply);
	/**
	 * 分页获取member收到的和发起的申请给公信堂展示
	 * @param loanApply
	 * @return
	 */
	List<NfsLoanApply> findApplyListForGxt(NfsLoanApply loanApply);
}