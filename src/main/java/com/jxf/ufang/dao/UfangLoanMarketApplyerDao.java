package com.jxf.ufang.dao;

import com.jxf.svc.sys.crud.dao.CrudDao;
import com.jxf.ufang.entity.UfangLoanMarketApplyer;

import org.apache.ibatis.annotations.Param;

import com.jxf.svc.annotation.MyBatisDao;

/**
 * 优放贷申请人DAO接口
 * @author suHuimin
 * @version 2019-03-27
 */
@MyBatisDao
public interface UfangLoanMarketApplyerDao extends CrudDao<UfangLoanMarketApplyer> {
	
	
	  int selectWeekUpdateCount(@Param("marketId")Long marketId,@Param("phoneNo")String phoneNo);
	  
	/**
	 * @description 获取指定条件的申请人数量
	 * @param applyer
	 * @return
	 */
	int	getCountByCondition(UfangLoanMarketApplyer applyer);
	
	/**
	 * 根据手机号和贷超获取申请人
	 * @param applyer
	 * @return
	 */
	UfangLoanMarketApplyer getByPhoneNoAndMarketId(UfangLoanMarketApplyer applyer);
}