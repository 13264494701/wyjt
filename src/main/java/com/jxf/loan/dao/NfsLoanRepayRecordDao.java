package com.jxf.loan.dao;

import java.math.BigDecimal;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jxf.loan.entity.NfsLoanRepayRecord;
import com.jxf.svc.annotation.MyBatisDao;
import com.jxf.svc.sys.crud.dao.CrudDao;


/**
 * 还款计划DAO接口
 * @author wo
 * @version 2018-11-15
 */
@MyBatisDao
public interface NfsLoanRepayRecordDao extends CrudDao<NfsLoanRepayRecord> {
	/**
	 *     根据借条id获取已还记录
	 * @param loanId
	 * @return
	 */
	List<NfsLoanRepayRecord> getRepayRecordsByLoanId(@Param("loanId")Long loanId);

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
	Integer countOverdueTimes(@Param("loanId")Long recordId);
	/**
	 *     根据借条id获取已还记录 (分期用) 状态传的是已还
	 * @param loanId
	 * @return
	 */
	List<NfsLoanRepayRecord> findPendingRepayList(NfsLoanRepayRecord record);
	
	/**
	 * 计算借条总金额
	 * @param loanIds
	 * @return
	 */
	BigDecimal sumLoanAmount(@Param("loanIds")String loanIds);
	
	
}