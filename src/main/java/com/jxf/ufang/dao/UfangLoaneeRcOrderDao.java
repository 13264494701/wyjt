package com.jxf.ufang.dao;

import com.jxf.svc.sys.crud.dao.CrudDao;
import com.jxf.ufang.entity.UfangLoaneeRcOrder;

import com.jxf.svc.annotation.MyBatisDao;

/**
 * 风控订单DAO接口
 * @author wo
 * @version 2019-07-14
 */
@MyBatisDao
public interface UfangLoaneeRcOrderDao extends CrudDao<UfangLoaneeRcOrder> {
	
}