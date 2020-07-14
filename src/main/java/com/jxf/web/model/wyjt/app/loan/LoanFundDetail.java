package com.jxf.web.model.wyjt.app.loan;

public class LoanFundDetail {

	private String fundName;

    private String moneyStr;

    private String date;
    
    /**
     * 	序号 0：已还；
     */
    private int nums = 0;

    public LoanFundDetail(String fundName, String moneyStr, String date, int nums) {
		super();
		this.fundName = fundName;
		this.moneyStr = moneyStr;
		this.date = date;
	}

	public LoanFundDetail() {
		super();
	}
	public String getFundName() {
		return fundName;
	}

	public void setFundName(String fundName) {
		this.fundName = fundName;
	}

	public String getMoneyStr() {
		return moneyStr;
	}

	public void setMoneyStr(String moneyStr) {
		this.moneyStr = moneyStr;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public int getNums() {
		return nums;
	}

	public void setNums(int nums) {
		this.nums = nums;
	}
    
    
}
