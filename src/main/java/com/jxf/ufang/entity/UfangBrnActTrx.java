package com.jxf.ufang.entity;

import java.math.BigDecimal;


import com.jxf.svc.sys.crud.entity.CrudEntity;

/**
 * 优放公司账户交易
 * @author jinxinfu
 * @version 2018-07-01
 */
public class UfangBrnActTrx extends CrudEntity<UfangBrnActTrx> {
	
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
	/** 归属公司 */
	private UfangBrn company;	
	/** 账户科目 */
	private String subNo;
	/** 账户余额 */
	private BigDecimal curBal;	
	/** 记账方向 */
	private String drc;		
	/** 交易金额 */
	private BigDecimal trxAmt;			
	/** 币种 */
	private String currCode;	
	/** 原业务ID */
	private Long orgId;
	/** 交易状态 */
	private Status status;	
	
	
	public UfangBrnActTrx() {
		super();
	}

	public UfangBrnActTrx(Long id){
		super(id);
	}

	public String getTrxCode() {
		return trxCode;
	}

	public void setTrxCode(String trxCode) {
		this.trxCode = trxCode;
	}

	public UfangBrn getCompany() {
		return company;
	}

	public void setCompany(UfangBrn company) {
		this.company = company;
	}

	public String getSubNo() {
		return subNo;
	}

	public void setSubNo(String subNo) {
		this.subNo = subNo;
	}

	public BigDecimal getCurBal() {
		return curBal;
	}

	public void setCurBal(BigDecimal curBal) {
		this.curBal = curBal;
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

	public String getCurrCode() {
		return currCode;
	}

	public void setCurrCode(String currCode) {
		this.currCode = currCode;
	}

	public Long getOrgId() {
		return orgId;
	}

	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}	
}