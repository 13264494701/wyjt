package com.jxf.web.model.wyjt.app.auction;
/**
 * @作者: gaobo
 * @创建时间 :2019年3月6日 
 * @功能说明: 申请转让
 */
public class ApplyCrAuctionRequestParam {

	/** 借款记录Id */
	private String loanId;
	
	/** 债转金额 */
	private String price;

	/** 支付密码  */
	private String payPwd;
	
	public String getLoanId() {
		return loanId;
	}

	public void setLoanId(String loanId) {
		this.loanId = loanId;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getPayPwd() {
		return payPwd;
	}

	public void setPayPwd(String payPwd) {
		this.payPwd = payPwd;
	}

}
