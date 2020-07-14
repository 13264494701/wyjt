package com.jxf.web.model.wyjt.app.loan;

/***
 *   申请修改利息
 * @author gaobo	
 *
 */
public class ApplyChangeInterestRequestParam {

	/** 借款Id */
	private String loanId;
	
	/** 借款利息*/
	private String interest;

	public String getLoanId() {
		return loanId;
	}

	public void setLoanId(String loanId) {
		this.loanId = loanId;
	}

	public String getInterest() {
		return interest;
	}

	public void setInterest(String interest) {
		this.interest = interest;
	}
}
