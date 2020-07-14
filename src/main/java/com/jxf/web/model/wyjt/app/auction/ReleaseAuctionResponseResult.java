package com.jxf.web.model.wyjt.app.auction;
/**
 * @作者: gaobo
 * @创建时间 :2019年3月6日 
 * @功能说明: 转让详情
 */
public class ReleaseAuctionResponseResult {

	/** 债权编号 */
	private String auctionId;
	/** 姓名 */
	private String realName;
	/** 头像 */
	private String partnerHeadImage;
	/** 借条本金 */
	private String amount;
	/** 借条利息 */
	private String interest;
	/** 逾期利息 */
	private String overdueInterest;
	/** 应还期日 */
	private String dueRepayDate;
	/** 转让金额 */
	private String price;
	/** 逾期字符串 */
	private String overdueStr;
	/** 转让申请时间 */
	private String applyTime;
	/** 取消转让时间 */
	private String releaseTime;
	/** 转让成功时间 */
	private String successedTime;
	/** 转让状态 */
	private Integer auctionStatus;// 0-> 转让中 1-> 审核中 2-> 审核失败 3-> 转让成功  4-> 转让失败
	/** 备注 */
	private String rmk;
	public String getRealName() {
		return realName;
	}
	public void setRealName(String realName) {
		this.realName = realName;
	}
	public String getPartnerHeadImage() {
		return partnerHeadImage;
	}
	public void setPartnerHeadImage(String partnerHeadImage) {
		this.partnerHeadImage = partnerHeadImage;
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
	public String getDueRepayDate() {
		return dueRepayDate;
	}
	public void setDueRepayDate(String dueRepayDate) {
		this.dueRepayDate = dueRepayDate;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getOverdueStr() {
		return overdueStr;
	}
	public void setOverdueStr(String overdueStr) {
		this.overdueStr = overdueStr;
	}
	public String getApplyTime() {
		return applyTime;
	}
	public void setApplyTime(String applyTime) {
		this.applyTime = applyTime;
	}
	public String getReleaseTime() {
		return releaseTime;
	}
	public void setReleaseTime(String releaseTime) {
		this.releaseTime = releaseTime;
	}
	public String getSuccessedTime() {
		return successedTime;
	}
	public void setSuccessedTime(String successedTime) {
		this.successedTime = successedTime;
	}
	public Integer getAuctionStatus() {
		return auctionStatus;
	}
	public void setAuctionStatus(Integer auctionStatus) {
		this.auctionStatus = auctionStatus;
	}
	public String getRmk() {
		return rmk;
	}
	public void setRmk(String rmk) {
		this.rmk = rmk;
	}
	public String getAuctionId() {
		return auctionId;
	}
	public void setAuctionId(String auctionId) {
		this.auctionId = auctionId;
	}
	
	
}
