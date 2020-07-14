package com.jxf.web.model.wyjt.app.loan;
/**
 * @作者: xiaorongdian
 * @创建时间 :2018年12月28日 下午7:05:31
 * @功能说明:跳转借条详情
 */
public class LoanDetailForAppRequestParam {

	/** 借条ID */
	private String loanId;
	
	/** 类型 0 apply 1 detail 2 record */
	private Integer type;

	public String getLoanId() {
		return loanId;
	}

	public void setLoanId(String loanId) {
		this.loanId = loanId;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

}
