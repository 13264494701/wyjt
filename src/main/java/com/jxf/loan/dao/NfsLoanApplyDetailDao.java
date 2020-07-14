package com.jxf.loan.dao;

import com.jxf.svc.annotation.MyBatisDao;
import com.jxf.svc.sys.crud.dao.CrudDao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.jxf.loan.entity.NfsLoanApplyDetail;


/**
 * 借贷对象DAO接口
 * @author wo
 * @version 2018-10-18
 */
@MyBatisDao
public interface NfsLoanApplyDetailDao extends CrudDao<NfsLoanApplyDetail> {

	NfsLoanApplyDetail getByApplyIdAndMemberId(@Param("applyId")Long applyId,@Param("memberId")Long memberId);
	/**
	 * 查找作为放款人的待确认的借条申请
	 * @param paramMap
	 * @return
	 */
	Map<String, Long> getLendingLoanAndAmount(@Param("paramMap")Map<String, Object> paramMap);
	
	/**
	 * 查询gxt超时借条
	 * @return
	 */
	List<NfsLoanApplyDetail> findLoanOutTimeList();
}