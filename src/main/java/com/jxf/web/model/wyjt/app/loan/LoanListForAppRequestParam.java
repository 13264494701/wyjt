package com.jxf.web.model.wyjt.app.loan;
/**
 * @作者: xiaorongdian
 * @创建时间 :2018年11月29日 上午10:50:08
 * @功能说明:借条列表给APP展示
 */
public class LoanListForAppRequestParam {
	
	/** 借贷角色  0 我欠谁钱 我是借款人 1 谁欠我钱 我是放款人*/
	private Integer loanRole;   
	
	/** 借条类型 0单人 1多人 (必传)*/
	private Integer loanType;         
	
	/** 还款方式  -1全部 0全额 1分期(必传) */
	private Integer repayType; 
	
	/** 借条申请状态 0待确认/待放款 1待还/待收 2逾期 3已还  4已失效   (必传)  */ 
	private Integer status;
	
	/**
	 * 附加状态(必传)
	 * -1 全部
	 * 0 借款人申请延期待确认
	 * 1 放款人申请延期待确认
	 * 2 借款人部分还款待确认
	 * 3 放款人要求部分还款待确认
	 * 4 放款人发起部分销账待确认
	 * 5 线下还款待确认
	 * 现在没用上,预留吧
	 */
	private Integer subStatus;
	
	/** 开始日期 */
	private String beginDate;
	
	/** 结束日期 */
	private String endDate;
	
	/** 最大金额 */
	private String maxAmount;
	
	/** 最小金额 */
	private String minAmount;
	
	/** 排序 ：0综合 1金额正序 2金额倒叙 3还款时间正序 4还款时间倒序  */
	private Integer orderBy;
	
	/** 搜索条件：姓名或者手机号 */
	private String usernameOrName;
	
	/** 页码(必传) */
	private Integer pageNo;
	
	
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
	public Integer getSubStatus() {
		return subStatus;
	}
	public void setSubStatus(Integer subStatus) {
		this.subStatus = subStatus;
	}
	public Integer getOrderBy() {
		return orderBy;
	}
	public void setOrderBy(Integer orderBy) {
		this.orderBy = orderBy;
	}
	public String getUsernameOrName() {
		return usernameOrName;
	}
	public void setUsernameOrName(String usernameOrName) {
		this.usernameOrName = usernameOrName;
	}
}
