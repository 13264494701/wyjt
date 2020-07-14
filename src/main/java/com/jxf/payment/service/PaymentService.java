package com.jxf.payment.service;

import com.jxf.payment.entity.Payment;
import com.jxf.svc.sys.crud.service.CrudService;

/**
 * 支付记录Service
 * @author HUOJIABAO
 * @version 2016-06-08
 */
public interface PaymentService extends CrudService<Payment> {

	/**
	 * 根据编号查找支付记录
	 * 
	 * @param paymentNo
	 *            编号(忽略大小写)
	 * @return 支付记录，若不存在则返回null
	 */
	Payment getByPaymentNo(String paymentNo);
	
	/**
	 * 根据支付类型和原业务编号查找支付记录
	 * 
	 * @param 
	 *            编号(忽略大小写)
	 * @return 支付记录，若不存在则返回null
	 */
	Payment getByTypeAndOrgId(Payment.Type type, Long orgId);
	

	/**
	 * 支付处理
	 * 
	 * @param paymentRecord
	 *            支付记录
	 */
	void handle(Payment payment);
	
	
	void payFinishProcess(Payment payment);
	
}