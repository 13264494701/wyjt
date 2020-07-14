package com.jxf.web.model.wyjt.app.loan;


public class LoanApplyPayResponseResult   {
	
	/** 申请ID/借条ID */
	private String id;//借款人发起借款申请，放款人支付借条达成，ID 为借条ID；
	                  //放款人主动放款支付，借条还未达成，ID为放款申请的ID
	
	/** 支付金额 */
	private Integer amount;
	

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Integer getAmount() {
		return amount;
	}

	public void setAmount(Integer amount) {
		this.amount = amount;
	}
	
}
