package com.jxf.web.model.wyjt.app.loan;

public class RepayRecordRequestParam   {
	
	/** 借款金额 */
	private String amount;		
	/** 借款利率 */
	private String intRate;	
	/** 还款方式 */
	private Integer repayType;	//0->全额;1->分期;	
	/** 借款期限 */
	private Integer term;//借款时长天数
	

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getIntRate() {
		return intRate;
	}

	public void setIntRate(String intRate) {
		this.intRate = intRate;
	}

	public Integer getRepayType() {
		return repayType;
	}

	public void setRepayType(Integer repayType) {
		this.repayType = repayType;
	}

	public Integer getTerm() {
		return term;
	}

	public void setTerm(Integer term) {
		this.term = term;
	}

	
}
