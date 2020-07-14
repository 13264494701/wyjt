package com.jxf.web.model.wyjt.app.loan;
/**
 * @作者: suhuimin
 * @创建时间 :2018年12月27日 下午7:01:18
 * @功能说明 :多人借款放款人出借跳转请求实体
 */
public class MultiplyLoanRequestParam {

	/** 借条申请详情ID */
	private String detailId;
	/** 出借金额 */
	private String amount;
	public String getDetailId() {
		return detailId;
	}
	public void setDetailId(String detailId) {
		this.detailId = detailId;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
}
