package com.jxf.web.model.wyjt.app.auction;


import java.util.ArrayList;

import java.util.List;


/***
 * 
 * @author wo
 *
 */
public class LoanCrAuctionListResponseResult {

	/** 拍卖中的债权列表 */
	private List<CrAuction> crAuctionList = new ArrayList<CrAuction>();
	
	public List<CrAuction> getCrAuctionList() {
		return crAuctionList;
	}

	public void setCrAuctionList(List<CrAuction> crAuctionList) {
		this.crAuctionList = crAuctionList;
	}
	
	public class CrAuction{
		
		/** 借条Id  */
		private String loanId;
		/** 债权编号 */
		private String auctionId;		
		/** 应还金额 */
		private String dueRepayAmount;
		/** 借条本金 */
		private String amount;
		/** 借条利息 */
		private String interest;
		/** 逾期利息 */
		private String overdueInterest;
		/** 姓名 */
		private String realName;
		/** 头像 */
		private String partnerHeadImage;
		/** 身份证号 */
		private String idNo;
		/** 手机号码 */
		private String username;
		/** 邮箱地址 */
		private String email;
		/** 联系地址 */
		private String addr;
		/** 应还期日 */
		private String dueRepayDate;	
		/** 逾期天数 */
		private Integer overDueDays;
		/** 转让金额 */
		private String price;
		/** 转让申请时间 */
		private String applyTime;
		/** 取消转让时间 */
		private String releaseTime;
		/** 转让成功时间 */
		private String successedTime;
		/** 结清日期 */
		private String completeDate;
		/** 逾期字符串 */
		private String overdueStr;
		/** 所属地市编号 */
		private String cityId;
		/** 所属地市名称 */
		private String cityName;
		/** 是否还款 */
		private Integer isRepay; // 0-> 未还 1-> 已还
		/** 转让状态 */
		private Integer auctionStatus;// 0-> 转让中 1-> 审核中 2-> 审核失败 3-> 转让成功  4-> 转让失败
		/** 备注 */
		private String rmk;
		public String getLoanId() {
			return loanId;
		}
		public void setLoanId(String loanId) {
			this.loanId = loanId;
		}
		public String getAuctionId() {
			return auctionId;
		}
		public void setAuctionId(String auctionId) {
			this.auctionId = auctionId;
		}
		public String getDueRepayAmount() {
			return dueRepayAmount;
		}
		public void setDueRepayAmount(String dueRepayAmount) {
			this.dueRepayAmount = dueRepayAmount;
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
		public String getPartnerHeadImage() {
			return partnerHeadImage;
		}
		public void setPartnerHeadImage(String partnerHeadImage) {
			this.partnerHeadImage = partnerHeadImage;
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
		public String getDueRepayDate() {
			return dueRepayDate;
		}
		public void setDueRepayDate(String dueRepayDate) {
			this.dueRepayDate = dueRepayDate;
		}
		public Integer getOverDueDays() {
			return overDueDays;
		}
		public void setOverDueDays(Integer overDueDays) {
			this.overDueDays = overDueDays;
		}
		public String getPrice() {
			return price;
		}
		public void setPrice(String price) {
			this.price = price;
		}
		public String getApplyTime() {
			return applyTime;
		}
		public void setApplyTime(String applyTime) {
			this.applyTime = applyTime;
		}
		public String getOverdueStr() {
			return overdueStr;
		}
		public void setOverdueStr(String overdueStr) {
			this.overdueStr = overdueStr;
		}
		public String getCityId() {
			return cityId;
		}
		public void setCityId(String cityId) {
			this.cityId = cityId;
		}
		public String getCityName() {
			return cityName;
		}
		public void setCityName(String cityName) {
			this.cityName = cityName;
		}
		public Integer getIsRepay() {
			return isRepay;
		}
		public void setIsRepay(Integer isRepay) {
			this.isRepay = isRepay;
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
		public String getAddr() {
			return addr;
		}
		public void setAddr(String addr) {
			this.addr = addr;
		}
		public String getCompleteDate() {
			return completeDate;
		}
		public void setCompleteDate(String completeDate) {
			this.completeDate = completeDate;
		}

	
	}



}
