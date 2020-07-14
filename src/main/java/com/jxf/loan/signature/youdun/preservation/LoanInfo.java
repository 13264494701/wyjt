package com.jxf.loan.signature.youdun.preservation;

/**
 * 有盾业务保全付款数据
 * @author Administrator
 *
 */
public class LoanInfo {
	
	/** 借款人名称*/
	private String userName;
	/** 借款人证件号码*/
	private String idNo;
	/** 借款合同编号*/
	private String contractCode;
	/** 支付机构*/
	private String paymentInstitution;
	/** 支付流水号*/
	private String paymentNo;
	/** 付款方户名*/
	private String paymentName;
	/** 付款方账号*/
	private String paymentBankCardNo;
	/** 付款方开户单位*/
	private String paymentBankCardName;
	/** 付款金额*/
	private String paymentAmount;
	/** 收款方户名*/
	private String loanName;
	/** 收款方账号*/
	private String loanBankCardNo;
	/** 收款方开户单位*/
	private String loanBankCardName;
	/** 交易状态*/
	private Integer dealStatus;
	/** 交易时间*/
	private String dealTime;
	
	
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getIdNo() {
		return idNo;
	}
	public void setIdNo(String idNo) {
		this.idNo = idNo;
	}
	public String getContractCode() {
		return contractCode;
	}
	public void setContractCode(String contractCode) {
		this.contractCode = contractCode;
	}
	public String getPaymentInstitution() {
		return paymentInstitution;
	}
	public void setPaymentInstitution(String paymentInstitution) {
		this.paymentInstitution = paymentInstitution;
	}
	public String getPaymentNo() {
		return paymentNo;
	}
	public void setPaymentNo(String paymentNo) {
		this.paymentNo = paymentNo;
	}
	public String getPaymentName() {
		return paymentName;
	}
	public void setPaymentName(String paymentName) {
		this.paymentName = paymentName;
	}
	public String getPaymentBankCardNo() {
		return paymentBankCardNo;
	}
	public void setPaymentBankCardNo(String paymentBankCardNo) {
		this.paymentBankCardNo = paymentBankCardNo;
	}
	public String getPaymentBankCardName() {
		return paymentBankCardName;
	}
	public void setPaymentBankCardName(String paymentBankCardName) {
		this.paymentBankCardName = paymentBankCardName;
	}
	public String getPaymentAmount() {
		return paymentAmount;
	}
	public void setPaymentAmount(String paymentAmount) {
		this.paymentAmount = paymentAmount;
	}
	public String getLoanName() {
		return loanName;
	}
	public void setLoanName(String loanName) {
		this.loanName = loanName;
	}
	public String getLoanBankCardNo() {
		return loanBankCardNo;
	}
	public void setLoanBankCardNo(String loanBankCardNo) {
		this.loanBankCardNo = loanBankCardNo;
	}
	public String getLoanBankCardName() {
		return loanBankCardName;
	}
	public void setLoanBankCardName(String loanBankCardName) {
		this.loanBankCardName = loanBankCardName;
	}
	public Integer getDealStatus() {
		return dealStatus;
	}
	public void setDealStatus(Integer dealStatus) {
		this.dealStatus = dealStatus;
	}
	public String getDealTime() {
		return dealTime;
	}
	public void setDealTime(String dealTime) {
		this.dealTime = dealTime;
	}
	
}
