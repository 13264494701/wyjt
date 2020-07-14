package com.jxf.payment.dao;

import com.jxf.svc.annotation.MyBatisDao;
import com.jxf.svc.sys.crud.dao.CrudDao;
import com.jxf.payment.entity.PaymentMethod;

/**
 * 支付方式DAO接口
 * @author JINXINFU
 * @version 2016-10-21
 */
@MyBatisDao
public interface PaymentMethodDao extends CrudDao<PaymentMethod> {
	
}