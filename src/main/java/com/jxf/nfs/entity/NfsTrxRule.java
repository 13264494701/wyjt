package com.jxf.nfs.entity;

import org.hibernate.validator.constraints.Length;

import com.jxf.svc.sys.crud.entity.CrudEntity;

/**
 * 交易规则Entity
 * @author wo
 * @version 2018-09-20
 */
public class NfsTrxRule extends CrudEntity<NfsTrxRule> {
	
	private static final long serialVersionUID = 1L;

	
	/** 交易代码 */
	private String trxCode;		
	/** 交易名称 */
	private String name;		
	
	
	public NfsTrxRule() {
		super();
	}

	public NfsTrxRule(Long id){
		super(id);
	}

	@Length(min=1, max=5, message="交易代码长度必须介于 1 和 5 之间")
	public String getTrxCode() {
		return trxCode;
	}

	public void setTrxCode(String trxCode) {
		this.trxCode = trxCode;
	}
	
	@Length(min=1, max=127, message="交易名称长度必须介于 1 和 127之间")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}