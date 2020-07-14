package com.jxf.web.model.wyjt.app.loan;


public class LoanApplyResponseResult   {
	
	/** 申请ID */
	private String loanId;
	/** 类型*/
	private Integer type;//0 多人apply 1detail 2record
	
	public String getLoanId() {
		return loanId;
	}

	public void setLoanId(String loanId) {
		this.loanId = loanId;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

}
