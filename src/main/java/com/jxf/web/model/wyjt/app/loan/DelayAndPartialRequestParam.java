package com.jxf.web.model.wyjt.app.loan;
/**
 * @作者: xiaorongdian
 * @创建时间 :2018年12月13日 上午11:49:01
 * @功能说明:申请延期/部分还款请求类
 */
public class DelayAndPartialRequestParam {
	/** 借款记录Id */
	private String loanId;
	
	/** 延期利息 */
	private String delayInterest;
	
	/** 延期后还款时间 */
	private String repayDateAfterDelay;
	
	/** 部分还款金额 */
	private String partialAmount;
	

	public String getPartialAmount() {
		return partialAmount;
	}

	public void setPartialAmount(String partialAmount) {
		this.partialAmount = partialAmount;
	}

	public String getRepayDateAfterDelay() {
		return repayDateAfterDelay;
	}

	public void setRepayDateAfterDelay(String repayDateAfterDelay) {
		this.repayDateAfterDelay = repayDateAfterDelay;
	}

	public String getLoanId() {
		return loanId;
	}

	public void setLoanId(String loanId) {
		this.loanId = loanId;
	}

	public String getDelayInterest() {
		return delayInterest;
	}

	public void setDelayInterest(String delayInterest) {
		this.delayInterest = delayInterest;
	}
}
