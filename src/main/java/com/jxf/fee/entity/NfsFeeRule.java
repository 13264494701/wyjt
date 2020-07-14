package com.jxf.fee.entity;

import java.math.BigDecimal;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.Length;

import com.jxf.svc.sys.crud.entity.CrudEntity;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;

/**
 * 收费规则Entity
 * @author wo
 * @version 2019-01-05
 */
public class NfsFeeRule extends CrudEntity<NfsFeeRule> {
	
	private static final long serialVersionUID = 1L;
	/** 业务代码 */
	private String trxCode;		
	/** 收费规则表达式 */
	private String expression;		
	
	public NfsFeeRule() {
		super();
	}

	public NfsFeeRule(Long id){
		super(id);
	}

	@Length(min=1, max=5, message="业务代码长度必须介于 1 和 5 之间")
	public String getTrxCode() {
		return trxCode;
	}

	public void setTrxCode(String trxCode) {
		this.trxCode = trxCode;
	}
	
	@Length(min=0, max=255, message="收费规则表达式长度必须介于 0 和 255 之间")
	public String getExpression() {
		return expression;
	}

	public void setExpression(String expression) {
		this.expression = expression;
	}
	
	
	/**
	 * 计算金额
	 * 
	 * @param binding
	 *            
	 * @return 
	 */
	public BigDecimal calculate(Binding binding) {
		if (StringUtils.isEmpty(getExpression())) {
			return BigDecimal.ZERO;
		}
		BigDecimal result = BigDecimal.ZERO;
		try {
			GroovyShell groovyShell = new GroovyShell(binding);
			result = new BigDecimal(groovyShell.evaluate(getExpression()).toString());
		} catch (Exception e) {
			e.printStackTrace();
			return BigDecimal.ZERO;
		}
		return result;
	}
	
}