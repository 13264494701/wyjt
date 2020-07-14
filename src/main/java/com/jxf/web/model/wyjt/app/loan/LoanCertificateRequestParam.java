package com.jxf.web.model.wyjt.app.loan;
/**
 * @作者: xiaorongdian
 * @创建时间 :2018年12月5日 上午11:27:45
 * @功能说明:查看电子借条
 */
public class LoanCertificateRequestParam {

	/** 借条ID */
	private String loanId;
	/** 显示方式 0全显示 1脱敏显示  */
	private Integer channel;

	public String getLoanId() {
		return loanId;
	}

	public void setLoanId(String loanId) {
		this.loanId = loanId;
	}

	public Integer getChannel() {
		return channel;
	}

	public void setChannel(Integer channel) {
		this.channel = channel;
	}

}
