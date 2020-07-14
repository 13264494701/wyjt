package com.jxf.payment.dao;

import com.jxf.svc.annotation.MyBatisDao;
import com.jxf.svc.sys.crud.dao.CrudDao;
import com.jxf.payment.entity.Payment;

/**
 * 支付记录DAO接口
 * @author HUOJIABAO
 * @version 2016-06-08
 */
@MyBatisDao
public interface PaymentDao extends CrudDao<Payment> {
	
	/**
	 * 根据编号查找支付记录
	 * 
	 * @param paymentNo
	 *            编号(忽略大小写)
	 * @return 支付记录，若不存在则返回null
	 */
	public Payment findByPaymentNo(String paymentNo);
}