package com.jxf.payment.dao;

import com.jxf.svc.annotation.MyBatisDao;
import com.jxf.svc.sys.crud.dao.CrudDao;
import com.jxf.payment.entity.Refund;


/**
 * 退款DAO接口
 * @author wo
 * @version 2018-08-11
 */
@MyBatisDao
public interface RefundDao extends CrudDao<Refund> {
	
	Refund getByRefundNo(String refundNo);
}