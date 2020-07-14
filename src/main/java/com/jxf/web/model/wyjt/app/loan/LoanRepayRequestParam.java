package com.jxf.web.model.wyjt.app.loan;

/***
 *  还款跳转支付页请求实体类
 * @author suhuimin	
 *
 */
public class LoanRepayRequestParam {

	/** 借条Id */
	private String recordId;
	
	/** 
	 *还款类型  0：全部，1：部分  (分期还款时传0或1都可)
	 */
	private String repayType; 
	
	/** 
	 * 还款金额  全部还款时为0，分期还款时传0
	 */
	private String repayAmount;
	
	/**
	 *  延期利息  没有则传"0"，分期时传0
	 */
	private String delayInterest;
	
	/**
	 * 下一次还款日	格式yyyy-MM-dd 如果没有则传“”，分期传“”
	 */
	private String nextRepayDate;
	/*
	 * 延期时长 没有则传"0"，分期传0
	 */
	private String delayTerm;
	
	public String getRecordId() {
		return recordId;
	}

	public void setRecordId(String recordId) {
		this.recordId = recordId;
	}

	public String getRepayAmount() {
		return repayAmount;
	}

	public void setRepayAmount(String repayAmount) {
		this.repayAmount = repayAmount;
	}

	public String getRepayType() {
		return repayType;
	}

	public void setRepayType(String repayType) {
		this.repayType = repayType;
	}

	public String getDelayInterest() {
		return delayInterest;
	}

	public void setDelayInterest(String delayInterest) {
		this.delayInterest = delayInterest;
	}

	public String getNextRepayDate() {
		return nextRepayDate;
	}

	public void setNextRepayDate(String nextRepayDate) {
		this.nextRepayDate = nextRepayDate;
	}

	public String getDelayTerm() {
		return delayTerm;
	}

	public void setDelayTerm(String delayTerm) {
		this.delayTerm = delayTerm;
	}
}
