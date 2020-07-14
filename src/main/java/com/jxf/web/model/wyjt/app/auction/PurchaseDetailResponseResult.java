package com.jxf.web.model.wyjt.app.auction;
/**
 * @作者: gaobo
 * @创建时间 :2019年3月6日 
 * @功能说明: 买入详情
 */
public class PurchaseDetailResponseResult {

	/** 债转Id  */
	private String auctionId;
	
	/** 借条Id  */
	private String loanId;
	
	/** 购入金额 */
	private String price;
	
	/** 应还总额 */
	private String dueRepayAmount;
	
	/** 借条本金 */
	private String amount;
	
	/** 借条利息 */
	private String interest;
	
	/** 逾期利息 */
	private String overdueInterest;
	
	/** 真实姓名 */
	private String realName;
	
	/** 身份证号 */
	private String idNo;
	
	/** 手机号码 */
	private String username;
	
	/** 邮箱地址 */
	private String email;
	
	/** 联系地址 */
	private String Addr;
	
	/** 还款时间 */
	private String dueRepayDate;
	
	/** 逾期天数 */
	private Integer overDueDays;
	
	/** 结清日期 */
	private String completeDate;
	
	/** 是否还款 */
	private Integer isRepay; // 0-> 未还 1-> 已还

	public String getLoanId() {
		return loanId;
	}

	public void setLoanId(String loanId) {
		this.loanId = loanId;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getInterest() {
		return interest;
	}

	public void setInterest(String interest) {
		this.interest = interest;
	}

	public String getOverdueInterest() {
		return overdueInterest;
	}

	public void setOverdueInterest(String overdueInterest) {
		this.overdueInterest = overdueInterest;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getIdNo() {
		return idNo;
	}

	public void setIdNo(String idNo) {
		this.idNo = idNo;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getAddr() {
		return Addr;
	}

	public void setAddr(String addr) {
		Addr = addr;
	}

	public Integer getOverDueDays() {
		return overDueDays;
	}

	public void setOverDueDays(Integer overDueDays) {
		this.overDueDays = overDueDays;
	}

	public Integer getIsRepay() {
		return isRepay;
	}

	public void setIsRepay(Integer isRepay) {
		this.isRepay = isRepay;
	}

	public String getDueRepayAmount() {
		return dueRepayAmount;
	}

	public void setDueRepayAmount(String dueRepayAmount) {
		this.dueRepayAmount = dueRepayAmount;
	}

	public String getDueRepayDate() {
		return dueRepayDate;
	}

	public void setDueRepayDate(String dueRepayDate) {
		this.dueRepayDate = dueRepayDate;
	}

	public String getCompleteDate() {
		return completeDate;
	}

	public void setCompleteDate(String completeDate) {
		this.completeDate = completeDate;
	}

	public String getAuctionId() {
		return auctionId;
	}

	public void setAuctionId(String auctionId) {
		this.auctionId = auctionId;
	}
	
	
	
	
	
	
}
