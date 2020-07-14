package com.jxf.web.model.wyjt.app.loan;

/***
 *  申请仲裁支付页面H5跳转请求实体
 * @author liuhuaixin
 *
 */
public class ArbitrationPayRequestParam   {
	
	/* 借款单id */
	private String loanId;
	/* 借款人姓名*/
	private String loaneeName;
	/* 借款金额*/
	private String amount;
	/* 借款利息*/
	private String interest;
	/* 逾期时间*/
	private String overDueDuration;
	/* 账户余额*/
	private String accountBalance;
	
	public String getLoaneeName() {
		return loaneeName;
	}
	public void setLoaneeName(String loaneeName) {
		this.loaneeName = loaneeName;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getInterest() {
		return interest;
	}
	public void setInterest(String interest) {
		this.interest = interest;
	}
	public String getOverDueDuration() {
		return overDueDuration;
	}
	public void setOverDueDuration(String overDueDuration) {
		this.overDueDuration = overDueDuration;
	}
	public String getAccountBalance() {
		return accountBalance;
	}
	public void setAccountBalance(String accountBalance) {
		this.accountBalance = accountBalance;
	}
	public String getLoanId() {
		return loanId;
	}
	public void setLoanId(String loanId) {
		this.loanId = loanId;
	}
	
	
	
}
