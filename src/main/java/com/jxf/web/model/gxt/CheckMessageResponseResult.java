package com.jxf.web.model.gxt;

public class CheckMessageResponseResult {

	/** 是否有未读的交易消息 0-没有  1-有*/
	private Integer transactionStatus;
	/** 是否有未读的借条消息 0-没有  1-有*/
	private Integer loanStatus;
	/** 是否有未读的服务消息 0-没有  1-有*/
	private Integer arbitrationStatus;

	public Integer getTransactionStatus() {
		return transactionStatus;
	}

	public void setTransactionStatus(Integer transactionStatus) {
		this.transactionStatus = transactionStatus;
	}

	public Integer getLoanStatus() {
		return loanStatus;
	}

	public void setLoanStatus(Integer loanStatus) {
		this.loanStatus = loanStatus;
	}

	public Integer getArbitrationStatus() {
		return arbitrationStatus;
	}

	public void setArbitrationStatus(Integer arbitrationStatus) {
		this.arbitrationStatus = arbitrationStatus;
	}

	
	
}
