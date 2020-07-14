package com.jxf.web.model.wyjt.app.loan;
/**
 * @作者: xiaorongdian
 * @创建时间 :2018年12月20日 下午5:07:11
 * @功能说明:放款人一键关闭借条
 */
public class CloseLoanRequestParam {

	/** 支付密码  */
	private String payPwd;
	
	/** 借条Id */
	private String loanId;

	public String getPayPwd() {
		return payPwd;
	}

	public void setPayPwd(String payPwd) {
		this.payPwd = payPwd;
	}

	public String getLoanId() {
		return loanId;
	}

	public void setLoanId(String loanId) {
		this.loanId = loanId;
	}
	
}
