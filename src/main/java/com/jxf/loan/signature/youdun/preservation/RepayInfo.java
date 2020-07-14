package com.jxf.loan.signature.youdun.preservation;

/**
 * 有盾业务保全还款数据
 * @author Administrator
 *
 */
public class RepayInfo {
	/** 还款人名称*/
	private String userName;
	/** 证件号码*/
	private String idNo;
	/** 还款时间*/
	private String repayTime;
	/** 还款本金*/
	private String repayAmount;
	/** 还款利息*/
	private String interestAmount;
	/** 还款账户户名*/
	private String loanName;
	/** 还款账户号码*/
	private String loanBankCardNo;
	/** 还款账户开户单位*/
	private String loanBankCardName;
	/** 借款合同（或借据）编号*/
	private String contractCode;
	
	
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
	public String getRepayTime() {
		return repayTime;
	}
	public void setRepayTime(String repayTime) {
		this.repayTime = repayTime;
	}
	public String getRepayAmount() {
		return repayAmount;
	}
	public void setRepayAmount(String repayAmount) {
		this.repayAmount = repayAmount;
	}
	public String getInterestAmount() {
		return interestAmount;
	}
	public void setInterestAmount(String interestAmount) {
		this.interestAmount = interestAmount;
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
	public String getContractCode() {
		return contractCode;
	}
	public void setContractCode(String contractCode) {
		this.contractCode = contractCode;
	}
	
}
