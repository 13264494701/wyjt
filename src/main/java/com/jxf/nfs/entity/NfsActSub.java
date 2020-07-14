package com.jxf.nfs.entity;

import org.hibernate.validator.constraints.Length;


import com.jxf.svc.sys.crud.entity.CrudEntity;

/**
 * 账户科目Entity
 * @author wo
 * @version 2018-09-18
 */
public class NfsActSub extends CrudEntity<NfsActSub> {
	
	private static final long serialVersionUID = 1L;

	/**
	 * 业务角色
	 */
	public enum TrxRole {
		
		/** 借条用户 */
		member,
		
		/** 付款人 */
		payer,
		
		/** 收款人 */
		payee,
				
		/** 优放机构 */
		ufangBrn,
		
		/** 优放用户 */
		ufangUser,
		
		/** 平台内部 */
		inner
						
	}
	
	/** 业务角色 */
	private TrxRole trxRole;		
	/** 科目编号 */
	private String subNo;		
	/** 业务名称 */
	private String name;		
	
	public NfsActSub() {
		super();
	}

	public NfsActSub(Long id){
		super(id);
	}


	public TrxRole getTrxRole() {
		return trxRole;
	}

	public void setTrxRole(TrxRole trxRole) {
		this.trxRole = trxRole;
	}
	
	@Length(min=1, max=4, message="科目类型长度必须介于 1 和 4 之间")
	public String getSubNo() {
		return subNo;
	}

	public void setSubNo(String subNo) {
		this.subNo = subNo;
	}
	
	@Length(min=1, max=32, message="业务名称长度必须介于 1 和 32 之间")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


	
}