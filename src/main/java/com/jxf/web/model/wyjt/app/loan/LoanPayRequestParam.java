package com.jxf.web.model.wyjt.app.loan;

/***
 *  付款页面H5跳转请求实体
 * @author suhuimin
 *
 */
public class LoanPayRequestParam   {
	/* 好友ID*/
	private String friendId;
	/* 借款金额*/
	private String amount;
	/* 
	 *借款时长 分期需要计算出借款时长
	 */
	private String term;
	/* 
	 * 还款方式  全额：0，分期：1
	 */
	private String repayType;
	/* 借款利率*/
	private String intRate;
	/* 借款利息*/
	private String interest;
	/* 
	 * 分期期数 还款方式是全额则值为1
	 */
	private String periods;
	
	public String getFriendId() {
		return friendId;
	}
	public void setFriendId(String friendId) {
		this.friendId = friendId;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getTerm() {
		return term;
	}
	public void setTerm(String term) {
		this.term = term;
	}
	public String getRepayType() {
		return repayType;
	}
	public void setRepayType(String repayType) {
		this.repayType = repayType;
	}
	public String getIntRate() {
		return intRate;
	}
	public void setIntRate(String intRate) {
		this.intRate = intRate;
	}
	public String getInterest() {
		return interest;
	}
	public void setInterest(String interest) {
		this.interest = interest;
	}
	public String getPeriods() {
		return periods;
	}
	public void setPeriods(String periods) {
		this.periods = periods;
	}
}
