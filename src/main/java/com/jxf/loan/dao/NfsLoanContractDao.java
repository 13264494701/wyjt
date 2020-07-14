package com.jxf.loan.dao;

import com.jxf.svc.sys.crud.dao.CrudDao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jxf.loan.entity.NfsLoanContract;
import com.jxf.svc.annotation.MyBatisDao;

/**
 * 合同表DAO接口
 * @author lmy
 * @version 2019-01-08
 */
@MyBatisDao
public interface NfsLoanContractDao extends CrudDao<NfsLoanContract> {
	/**
	 * @description 根据借条id查借条合同
	 * @param loanContract
	 * @return id倒排的合同集合
	 */
	List<NfsLoanContract> getContractByLoanId(NfsLoanContract loanContract);

	/**
	 * 根据loanId 查当前合同
	 * @param nfsLoanContract
	 * @return
	 */
	NfsLoanContract getCurrentContractByLoanId(@Param("loanId")Long loanId);
}