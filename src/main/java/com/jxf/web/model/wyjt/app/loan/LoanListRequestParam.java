package com.jxf.web.model.wyjt.app.loan;

public class LoanListRequestParam {

	/** 借贷角色 */
	private Integer loanRole; // 0->借款人，1->放款人
	/** 借条类型 */
	private Integer loanType; // 0->单人，1->多人
	/** 还款方式 */
	private Integer repayType; // 0->全额，1->分期 -1全部
	/** 借条状态  0待还/待收 1已还 2逾期 */
	private Integer status;
	/** 附加状态 
	 * 0 借款人申请延期待确认 
	 * 1 放款人申请延期待确认 
	 * 2 借款人部分还款待确认 
	 * 3 放款人要求部分还款待确认 
	 * 4 放款人发起部分销账待确认
	 * 5 线下还款待确认
	 * */
	private Integer subStatus;
	/** 开始日期 */
	private String beginDate;
	/** 结束日期 */
	private String endDate;
	/** 最大金额 */
	private String maxAmount;
	/** 最小金额 */
	private String minAmount;
	/** 页码 */
	private Integer pageNo;
	/** 每页大小 */
	private Integer pageSize;
	

	public Integer getLoanRole() {
		return loanRole;
	}

	public void setLoanRole(Integer loanRole) {
		this.loanRole = loanRole;
	}

	public Integer getLoanType() {
		return loanType;
	}

	public void setLoanType(Integer loanType) {
		this.loanType = loanType;
	}

	public Integer getRepayType() {
		return repayType;
	}

	public void setRepayType(Integer repayType) {
		this.repayType = repayType;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getSubStatus() {
		return subStatus;
	}

	public void setSubStatus(Integer subStatus) {
		this.subStatus = subStatus;
	}

	public String getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(String beginDate) {
		this.beginDate = beginDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getMaxAmount() {
		return maxAmount;
	}

	public void setMaxAmount(String maxAmount) {
		this.maxAmount = maxAmount;
	}

	public String getMinAmount() {
		return minAmount;
	}

	public void setMinAmount(String minAmount) {
		this.minAmount = minAmount;
	}

	public Integer getPageNo() {
		return pageNo;
	}

	public void setPageNo(Integer pageNo) {
		this.pageNo = pageNo;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

}
