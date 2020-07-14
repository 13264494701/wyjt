package com.jxf.ufang.entity;

import java.math.BigDecimal;

import org.hibernate.validator.constraints.Length;


import com.jxf.svc.sys.crud.entity.CrudEntity;


/**
 * 优放用户账户交易Entity
 * @author jinxinfu
 * @version 2018-07-01
 */
public class UfangUserActTrx extends CrudEntity<UfangUserActTrx> {
	
	private static final long serialVersionUID = 1L;
	 
	 
	/**
	 * 交易状态
	 */
	public enum Status {
		
		/**登记*/
		REGIST,
		
		/**成功*/
		SUCC,

		/**失败*/
		FAIL,
		
		/**其它*/
		OTHERS
		
	}
	
	/** 交易代码 */
	private String trxCode;	
	/** 优放用户 */
	private UfangUser user;	
	/** 账户科目 */
	private String subNo;		
	/** 记账方向 */
	private String drc;		
	/** 交易金额 */
	private BigDecimal trxAmt;		
	/** 账户余额 */
	private BigDecimal curBal;	
	/** 币种 */
	private String currCode;	

	/** 原业务ID */
	private Long orgId;
	/** 交易状态 */
	private Status status;	
	
	public UfangUserActTrx() {
		super();
	}

	public UfangUserActTrx(Long id){
		super(id);
	}

	
	public String getTrxCode() {
		return trxCode;
	}

	public void setTrxCode(String trxCode) {
		this.trxCode = trxCode;
	}
	
	public UfangUser getUser() {
		return user;
	}

	public void setUser(UfangUser user) {
		this.user = user;
	}
		
	@Length(min=1, max=4, message="科目编号长度必须介于 1 和 4 之间")
	public String getSubNo() {
		return subNo;
	}
	public void setSubNo(String subNo) {
		this.subNo = subNo;
	}

	public String getDrc() {
		return drc;
	}

	public void setDrc(String drc) {
		this.drc = drc;
	}

	
	public BigDecimal getTrxAmt() {
		return trxAmt;
	}

	public void setTrxAmt(BigDecimal trxAmt) {
		this.trxAmt = trxAmt;
	}
	
	public BigDecimal getCurBal() {
		return curBal;
	}

	public void setCurBal(BigDecimal curBal) {
		this.curBal = curBal;
	}
	
	@Length(min=1, max=3, message="币种长度必须介于 1 和 3 之间")
	public String getCurrCode() {
		return currCode;
	}

	public void setCurrCode(String currCode) {
		this.currCode = currCode;
	}
	
	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public Long getOrgId() {
		return orgId;
	}

	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}



	
}