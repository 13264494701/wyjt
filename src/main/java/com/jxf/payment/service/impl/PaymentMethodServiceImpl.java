package com.jxf.payment.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jxf.payment.dao.PaymentMethodDao;
import com.jxf.payment.entity.PaymentMethod;
import com.jxf.payment.service.PaymentMethodService;
import com.jxf.svc.persistence.Page;
import com.jxf.svc.sys.crud.service.impl.CrudServiceImpl;

/**
 * 支付方式ServiceImpl
 * @author JINXINFU
 * @version 2016-10-21
 */
@Service("shopPaymentMethodService")
@Transactional(readOnly = true)
public class PaymentMethodServiceImpl extends CrudServiceImpl<PaymentMethodDao, PaymentMethod> implements PaymentMethodService{

	public PaymentMethod get(Long id) {
		return super.get(id);
	}
	
	public List<PaymentMethod> findList(PaymentMethod shopPaymentMethod) {
		return super.findList(shopPaymentMethod);
	}
	
	public Page<PaymentMethod> findPage(Page<PaymentMethod> page, PaymentMethod shopPaymentMethod) {
		return super.findPage(page, shopPaymentMethod);
	}
	
	@Transactional(readOnly = false)
	public void save(PaymentMethod shopPaymentMethod) {
		super.save(shopPaymentMethod);
	}
	
	@Transactional(readOnly = false)
	public void delete(PaymentMethod shopPaymentMethod) {
		super.delete(shopPaymentMethod);
	}

	@Override
	public PaymentMethod getDefaultPaymentMethod() {

		return null;
	}
	
}