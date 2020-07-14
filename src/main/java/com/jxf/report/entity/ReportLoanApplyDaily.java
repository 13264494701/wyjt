package com.jxf.report.entity;

import java.math.BigDecimal;


import com.jxf.svc.sys.crud.entity.CrudEntity;

/**
 * 申请统计Entity
 * @author XIAORONGDIAN
 * @version 2019-04-04
 */
public class ReportLoanApplyDaily extends CrudEntity<ReportLoanApplyDaily> {
	
	private static final long serialVersionUID = 1L;
			/** 日期 */
	private String date;	
			/** 借款申请笔数 */
	private Integer borrowCount;		
			/** 借款申请金额 */
	private BigDecimal borrowAmount;		
			/** 放款申请笔数 */
	private Integer loanCount;		
			/** 放款申请金额 */
	private BigDecimal loanAmount;		
			/** 借款达成笔数 */
	private Integer borrowSuccessCount;		
			/** 借款达成金额 */
	private BigDecimal borrowSuccessAmount;		
			/** 放款达成笔数 */
	private Integer loanSuccessCount;		
			/** 放款达成金额 */
	private BigDecimal loanSuccessAmount;		
	/** 报表类型 */
	private Integer type;
	
	public ReportLoanApplyDaily() {
		super();
	}

	public ReportLoanApplyDaily(Long id){
		super(id);
	}

	public Integer getBorrowCount() {
		return borrowCount;
	}

	public void setBorrowCount(Integer borrowCount) {
		this.borrowCount = borrowCount;
	}

	public BigDecimal getBorrowAmount() {
		return borrowAmount;
	}

	public void setBorrowAmount(BigDecimal borrowAmount) {
		this.borrowAmount = borrowAmount;
	}

	public Integer getLoanCount() {
		return loanCount;
	}

	public void setLoanCount(Integer loanCount) {
		this.loanCount = loanCount;
	}

	public BigDecimal getLoanAmount() {
		return loanAmount;
	}

	public void setLoanAmount(BigDecimal loanAmount) {
		this.loanAmount = loanAmount;
	}

	public Integer getBorrowSuccessCount() {
		return borrowSuccessCount;
	}

	public void setBorrowSuccessCount(Integer borrowSuccessCount) {
		this.borrowSuccessCount = borrowSuccessCount;
	}

	public BigDecimal getBorrowSuccessAmount() {
		return borrowSuccessAmount;
	}

	public void setBorrowSuccessAmount(BigDecimal borrowSuccessAmount) {
		this.borrowSuccessAmount = borrowSuccessAmount;
	}

	public BigDecimal getLoanSuccessAmount() {
		return loanSuccessAmount;
	}

	public void setLoanSuccessAmount(BigDecimal loanSuccessAmount) {
		this.loanSuccessAmount = loanSuccessAmount;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getLoanSuccessCount() {
		return loanSuccessCount;
	}

	public void setLoanSuccessCount(Integer loanSuccessCount) {
		this.loanSuccessCount = loanSuccessCount;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}
}