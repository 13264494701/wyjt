package com.jxf.payment.service;


import java.math.BigDecimal;

import javax.servlet.http.HttpServletRequest;

import com.jxf.mem.entity.Member;
import com.jxf.payment.entity.Payment;
import com.jxf.payment.entity.Refund;
import com.jxf.svc.sys.crud.service.CrudService;


/**
 * 退款Service
 * @author wo
 * @version 2018-08-11
 */
public interface RefundService extends CrudService<Refund> {

	
	void refundFinishProcess(Refund refund);
	
	Refund getByRefundNo(String refundNo);
	
	Boolean wxRefund(Member member,Payment.Type type, Long orgId,  BigDecimal amount);
}