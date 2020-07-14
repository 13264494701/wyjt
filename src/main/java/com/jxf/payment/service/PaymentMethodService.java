package com.jxf.payment.service;

import com.jxf.payment.entity.PaymentMethod;
import com.jxf.svc.sys.crud.service.CrudService;

/**
 * 支付方式Service
 * @author JINXINFU
 * @version 2016-10-21
 */
public interface PaymentMethodService extends CrudService<PaymentMethod> {

	
	PaymentMethod getDefaultPaymentMethod();
}