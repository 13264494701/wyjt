package com.jxf.web.model.wyjt.app.loan;



public class LoanPeriodizationDetail {

	/** 名称 */
	private String fundName;
	
	/** 应还金额 */
	private String expectRepayAmt;
	
	/** 应还本金 */
    private String expectRepayPrn;
    
    /** 应还利息 */
	private String expectRepayInt;

	/** 交易时间 逾期显示当前时间 */
    private String date;
    
    /**
     * 	期数
     */
    private String nums = "";
    
	public LoanPeriodizationDetail(String fundName, String expectRepayAmt, String expectRepayPrn, String expectRepayInt,
			String date, String nums ) {
		super();
		this.fundName = fundName;
		this.expectRepayAmt = expectRepayAmt;
		this.expectRepayPrn = expectRepayPrn;
		this.expectRepayInt = expectRepayInt;
		this.date = date;
		this.nums = nums;
	}

	public LoanPeriodizationDetail() {
		super();
	}

	public String getExpectRepayAmt() {
		return expectRepayAmt;
	}


	public void setExpectRepayAmt(String expectRepayAmt) {
		this.expectRepayAmt = expectRepayAmt;
	}



	public String getExpectRepayPrn() {
		return expectRepayPrn;
	}



	public void setExpectRepayPrn(String expectRepayPrn) {
		this.expectRepayPrn = expectRepayPrn;
	}



	public String getExpectRepayInt() {
		return expectRepayInt;
	}



	public void setExpectRepayInt(String expectRepayInt) {
		this.expectRepayInt = expectRepayInt;
	}



	public String getDate() {
		return date;
	}



	public void setDate(String date) {
		this.date = date;
	}

	public String getFundName() {
		return fundName;
	}

	public void setFundName(String fundName) {
		this.fundName = fundName;
	}

	public String getNums() {
		return nums;
	}
	public void setNums(String nums) {
		this.nums = nums;
	}

}
