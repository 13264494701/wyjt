package com.jxf.web.model.wyjt.app.loan;
/**
 * @作者: xiaorongdian
 * @创建时间 :2018年12月6日 下午4:49:21
 * @功能说明:放款人同意/拒绝 部分还款 线下还款 延期的 请求实体
 */
public class AnswerRequestParam {

	/**
	 * 借条ID
	 */
	private String loanId;
	
	/**
	 * 同意/拒绝 0拒绝 1同意
	 */
	private Integer isAgree;

	public String getLoanId() {
		return loanId;
	}

	public void setLoanId(String loanId) {
		this.loanId = loanId;
	}

	public Integer getIsAgree() {
		return isAgree;
	}

	public void setIsAgree(Integer isAgree) {
		this.isAgree = isAgree;
	}
	
	
}
