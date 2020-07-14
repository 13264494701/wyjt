package com.jxf.fee.service;

import java.math.BigDecimal;

import com.jxf.fee.entity.NfsFeeRule;
import com.jxf.svc.sys.crud.service.CrudService;

/**
 * 收费规则Service
 * @author wo
 * @version 2019-01-05
 */
public interface NfsFeeRuleService extends CrudService<NfsFeeRule> {

	/**
	 * 获取交易手续费
	 * @param trxCode 交易代码
	 * @param amount 交易金额
	 * @return
	 */
	BigDecimal getFee(String trxCode,BigDecimal amount);
	
}