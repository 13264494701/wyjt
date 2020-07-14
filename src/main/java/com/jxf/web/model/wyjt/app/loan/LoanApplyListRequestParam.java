package com.jxf.web.model.wyjt.app.loan;
/**
 * @作者: xiaorongdian
 * @创建时间 :2018年11月26日 上午11:34:35
 * @功能说明:借款申请列表
 */
public class LoanApplyListRequestParam {

	/** 借贷角色 */
	private Integer loanRole; // 0->借款人，1->放款人
	/** 借条类型 */
	private Integer loanType; // 0->单人，1->多人
	/** 还款方式 */
	private Integer repayType; // 0->全额，1->分期
	/** 借条申请状态  0待确认/待放款 1已失效 -1全部 */
	private Integer status;
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
