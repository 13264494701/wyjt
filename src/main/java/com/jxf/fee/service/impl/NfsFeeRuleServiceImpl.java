package com.jxf.fee.service.impl;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jxf.fee.dao.NfsFeeRuleDao;
import com.jxf.fee.entity.NfsFeeRule;
import com.jxf.fee.service.NfsFeeRuleService;
import com.jxf.svc.persistence.Page;
import com.jxf.svc.sys.crud.service.impl.CrudServiceImpl;

import groovy.lang.Binding;

/**
 * 收费规则ServiceImpl
 * @author wo
 * @version 2019-01-05
 */
@Service("nfsFeeRuleService")
@Transactional(readOnly = true)
public class NfsFeeRuleServiceImpl extends CrudServiceImpl<NfsFeeRuleDao, NfsFeeRule> implements NfsFeeRuleService{

	@Autowired
	private NfsFeeRuleDao feeRuleDao;
	
	public NfsFeeRule get(Long id) {
		return super.get(id);
	}
	
	public List<NfsFeeRule> findList(NfsFeeRule nfsFeeRule) {
		return super.findList(nfsFeeRule);
	}
	
	public Page<NfsFeeRule> findPage(Page<NfsFeeRule> page, NfsFeeRule nfsFeeRule) {
		return super.findPage(page, nfsFeeRule);
	}
	
	@Transactional(readOnly = false)
	public void save(NfsFeeRule nfsFeeRule) {
		super.save(nfsFeeRule);
	}
	
	@Transactional(readOnly = false)
	public void delete(NfsFeeRule nfsFeeRule) {
		super.delete(nfsFeeRule);
	}
	
	/**
	 * 获取交易手续费
	 * @param trxCode 交易代码
	 * @param amount 交易金额
	 * @return
	 */
	public BigDecimal getFee(String trxCode,BigDecimal amount) {

		Binding binding = new Binding();
		binding.setVariable("amount", amount);
		NfsFeeRule nfsFeeRule = feeRuleDao.getByTrxCode(trxCode);
		BigDecimal fee = nfsFeeRule.calculate(binding);
		return fee;
	}
	
}