package com.jxf.nfs.entity;

import java.math.BigDecimal;

import org.hibernate.validator.constraints.Length;

import com.jxf.svc.sys.brn.entity.Brn;
import com.jxf.svc.sys.crud.entity.CrudEntity;

/**
 * 账户交易Entity
 * @author jinxinfu
 * @version 2018-07-01
 */
public class NfsBrnActTrx extends CrudEntity<NfsBrnActTrx> {
	
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
	/** 公司 */
	private Brn company;	
	/** 科目编号 */
	private String subNo;
	
	/** 记账方向 D入账 C出账 */
	private String drc;		
	/** 交易金额 */
	private BigDecimal trxAmt;		
	/** 账户余额 */
	private BigDecimal curBal;	
	/** 币种 */
	private String currCode;	
		
	/** 原业务id */
	private Long orgId;	
	/** 交易状态 */
	private Status status;		
	
	public NfsBrnActTrx() {
		super();
	}

	public NfsBrnActTrx(Long id){
		super(id);
	}
	public String getTrxCode() {
		return trxCode;
	}

	public void setTrxCode(String trxCode) {
		this.trxCode = trxCode;
	}
	public Brn getCompany() {
		return company;
	}

	public void setCompany(Brn company) {
		this.company = company;
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
	
	@Length(min=1, max=3, message="币种长度必须介于 1 和 3 之间")
	public String getCurrCode() {
		return currCode;
	}

	public void setCurrCode(String currCode) {
		this.currCode = currCode;
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