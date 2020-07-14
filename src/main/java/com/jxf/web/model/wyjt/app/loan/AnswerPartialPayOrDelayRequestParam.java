package com.jxf.web.model.wyjt.app.loan;
/**
 * @作者: xiaorongdian
 * @创建时间 :2018年12月17日 下午7:01:18
 * @功能说明:放款人同意部分还款/延期
 */
public class AnswerPartialPayOrDelayRequestParam {

	/** 借条ID */
	private String loanId;
	
	/** 同意 */
	private String isAgree;
	
	public String getLoanId() {
		return loanId;
	}

	public void setLoanId(String loanId) {
		this.loanId = loanId;
	}

	public String getIsAgree() {
		return isAgree;
	}

	public void setIsAgree(String isAgree) {
		this.isAgree = isAgree;
	}
	
}
