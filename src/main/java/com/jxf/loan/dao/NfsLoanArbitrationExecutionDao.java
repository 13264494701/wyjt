package com.jxf.loan.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jxf.loan.entity.NfsLoanArbitrationExecution;
import com.jxf.svc.annotation.MyBatisDao;
import com.jxf.svc.sys.crud.dao.CrudDao;


/**
 * 强执DAO接口
 * @author LIUHUAIXIN
 * @version 2018-12-20
 */
@MyBatisDao
public interface NfsLoanArbitrationExecutionDao extends CrudDao<NfsLoanArbitrationExecution> {
	/**
	 *获取强执列表
	 */
	List<NfsLoanArbitrationExecution> findExecution(NfsLoanArbitrationExecution nfsLoanArbitrationExecution);
	
	/**
	 *获取强执列表(结束)
	 */
	List<NfsLoanArbitrationExecution> findExecuted(@Param("id")Long id,@Param("nfsLoanArbitrationExecution")NfsLoanArbitrationExecution nfsLoanArbitrationExecution);
	
	
	/**
	 * 根据仲裁id获取强执记录
	 * @param arbitrationId
	 * @return
	 */
	NfsLoanArbitrationExecution findByArbitrationId(Long arbitrationId);
}