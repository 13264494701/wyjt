package com.jxf.payment.utils;

import java.util.List;

import com.jxf.payment.dao.PaymentMethodDao;
import com.jxf.payment.entity.PaymentMethod;
import com.jxf.svc.init.SpringContextHolder;

public class PaymentUtils {
	
	private static PaymentMethodDao paymentMethodDao = SpringContextHolder.getBean(PaymentMethodDao.class);
	/**
	 * 
	 * 函数功能说明  支付方式 列表
	 * HUOJIABAO  2016年6月26日 
	 * 修改者名字 修改日期 
	 * 修改内容 
	 * @参数： @return     
	 * @return    
	 * @throws
	 */
	public static List<PaymentMethod> getPaymentMethods(){
		return paymentMethodDao.findList(new PaymentMethod());
	}

}
