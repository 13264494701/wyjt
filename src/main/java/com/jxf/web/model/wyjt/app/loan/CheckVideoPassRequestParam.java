package com.jxf.web.model.wyjt.app.loan;
/**
 * @作者: xiaorongdian
 * @创建时间 :2018年12月24日 上午11:46:01
 * @功能说明:放款人审核视频
 */
public class CheckVideoPassRequestParam {

	/** 借条id (后台是detailId) */
	private String loanId;
	
	/** 是否通过 0否 1通过 */
	private Integer isPass;

	public String getLoanId() {
		return loanId;
	}

	public void setLoanId(String loanId) {
		this.loanId = loanId;
	}

	public Integer getIsPass() {
		return isPass;
	}

	public void setIsPass(Integer isPass) {
		this.isPass = isPass;
	}

}
