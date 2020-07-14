package com.jxf.loan.entity;

import javax.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.Date;
import com.alibaba.fastjson.annotation.JSONField;

import com.jxf.svc.sys.crud.entity.CrudEntity;

/**
 * 还款计划Entity
 * @author wo
 * @version 2018-11-15
 */
public class NfsLoanRepayRecord extends CrudEntity<NfsLoanRepayRecord> {
	
	private static final long serialVersionUID = 1L;
	/**
	 * 还款状态
	 */
	public enum Status {

	    /** 未还 0*/
		pending,
		
		/** 已还 1*/
		done,
		
		/** 部分已还 2*/
		partialDone,
		
		/** 逾期 3*/
		overdue
	}
	
	/** 借条 */
	private NfsLoanRecord loan;		
	/** 期数序号 */
	private Integer periodsSeq;		
	/** 应还金额 */
	private BigDecimal expectRepayAmt;		
	/** 应还本金 */
	private BigDecimal expectRepayPrn;		
	/** 应还利息 */
	private BigDecimal expectRepayInt;		
	/** 应还日期 */
	private Date expectRepayDate;		
	/** 实还金额 */
	private String actualRepayAmt;		
	/** 实还日期 */
	private Date actualRepayDate;		
	/** 还款状态 */
	private Status status;	
	
	/** 开始还款日期 */
	private Date beginRepayDate;	
	/** 结束还款日期*/
	private Date endRepayDate;	
	
	public NfsLoanRepayRecord() {
		super();
	}

	public NfsLoanRepayRecord(Long id){
		super(id);
	}

	public NfsLoanRecord getLoan() {
		return loan;
	}

	public void setLoan(NfsLoanRecord loan) {
		this.loan = loan;
	}
	
	public Integer getPeriodsSeq() {
		return periodsSeq;
	}

	public void setPeriodsSeq(Integer periodsSeq) {
		this.periodsSeq = periodsSeq;
	}
	
	public BigDecimal getExpectRepayAmt() {
		return expectRepayAmt;
	}

	public void setExpectRepayAmt(BigDecimal expectRepayAmt) {
		this.expectRepayAmt = expectRepayAmt;
	}
	
	public BigDecimal getExpectRepayPrn() {
		return expectRepayPrn;
	}

	public void setExpectRepayPrn(BigDecimal expectRepayPrn) {
		this.expectRepayPrn = expectRepayPrn;
	}
	
	public BigDecimal getExpectRepayInt() {
		return expectRepayInt;
	}

	public void setExpectRepayInt(BigDecimal expectRepayInt) {
		this.expectRepayInt = expectRepayInt;
	}
	
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	@NotNull(message="应还日期不能为空")
	public Date getExpectRepayDate() {
		return expectRepayDate;
	}

	public void setExpectRepayDate(Date expectRepayDate) {
		this.expectRepayDate = expectRepayDate;
	}
	
	public String getActualRepayAmt() {
		return actualRepayAmt;
	}

	public void setActualRepayAmt(String actualRepayAmt) {
		this.actualRepayAmt = actualRepayAmt;
	}
	
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	public Date getActualRepayDate() {
		return actualRepayDate;
	}

	public void setActualRepayDate(Date actualRepayDate) {
		this.actualRepayDate = actualRepayDate;
	}
	
	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public Date getBeginRepayDate() {
		return beginRepayDate;
	}

	public void setBeginRepayDate(Date beginRepayDate) {
		this.beginRepayDate = beginRepayDate;
	}

	public Date getEndRepayDate() {
		return endRepayDate;
	}

	public void setEndRepayDate(Date endRepayDate) {
		this.endRepayDate = endRepayDate;
	}


	
}