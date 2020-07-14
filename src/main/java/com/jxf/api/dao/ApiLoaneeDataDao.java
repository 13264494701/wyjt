package com.jxf.api.dao;

import org.apache.ibatis.annotations.Param;

import com.jxf.api.entity.ApiLoaneeData;
import com.jxf.svc.sys.crud.dao.CrudDao;
import com.jxf.svc.annotation.MyBatisDao;

/**
 * 给第三方查询流量DAO接口
 * @author XIAORONGDIAN
 * @version 2019-04-17
 */
@MyBatisDao
public interface ApiLoaneeDataDao extends CrudDao<ApiLoaneeData> {

	/***
	 * 查商户最近一次查询记录
	 * @param merchantNumber
	 * @return
	 */
	ApiLoaneeData getNearest(@Param("merchantNumber")String merchantNumber);
	
}