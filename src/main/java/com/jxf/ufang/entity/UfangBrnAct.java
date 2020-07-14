package com.jxf.ufang.entity;

import java.math.BigDecimal;

import org.hibernate.validator.constraints.Length;

import com.jxf.svc.sys.crud.entity.CrudEntity;

/**
 * 机构账户Entity
 * @author jinxinfu
 * @version 2018-06-29
 */
public class UfangBrnAct extends CrudEntity<UfangBrnAct> {
	
	private static final long serialVersionUID = 1L;
	/**
	 * 状态
	 */
	public enum Status {

		/** 已启用 */
		enabled,

		/** 已冻结 */
		disabled
	}
	/** 公司 */
	private UfangBrn company;	
	/** 账户科目 */
	private String subNo;		
	/** 币种 */
	private String currCode;		
	/** 账户余额 */
	private BigDecimal curBal;		
	/** 账户状态 */
	private Status status;		
	
	
	public UfangBrnAct() {
		super();
	}

	public UfangBrnAct(Long id){
		super(id);
	}

	public UfangBrn getCompany() {
		return company;
	}

	public void setCompany(UfangBrn company) {
		this.company = company;
	}
	
	@Length(min=1, max=4, message="账户科目长度必须介于 1 和 4 之间")
	public String getSubNo() {
		return subNo;
	}

	public void setSubNo(String subNo) {
		this.subNo = subNo;
	}
	
	@Length(min=1, max=3, message="币种长度必须介于 1 和 3 之间")
	public String getCurrCode() {
		return currCode;
	}

	public void setCurrCode(String currCode) {
		this.currCode = currCode;
	}
	
	public BigDecimal getCurBal() {
		return curBal;
	}

	public void setCurBal(BigDecimal curBal) {
		this.curBal = curBal;
	}
	
	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}


	
}