package com.jxf.web.model.wyjt.app.auction;

import java.util.ArrayList;
import java.util.List;

/**
 * @作者: gaobo
 * @创建时间 :2019年3月6日 
 * @功能说明: 可转让列表返回
 */
public class CrTransferListResponseResult {

	
	/** 可转让列表 */
	private List<CrTransferList> crTransferList = new ArrayList<CrTransferList>();
	
	public static class CrTransferList {
	
		/** 借条ID */
		private String loanId;
		/** 姓名 */
		private String partnerName;		
		/** 头像 */
		private String partnerHeadImage;	
		/** 还款方式 0全额 1分期 */
		private Integer repayType;	
		/** 借款金额 */
		private String amount;
		/** 利息(多人借条时是利率) */
		private String interest;
		/** 逾期利息 */
		private String overdueInterest;
		/** 还款时间 */
		private String repayDate;	
		/** 借条进度*/
		private String progress;
		/**
	     * 今日角标 1：显示；0：不显示
	     */
	    private Integer isToday;
	    /**
	     * 逾期图标 1：显示；0：不显示
	     */
	    private Integer isOverdue;
	    
	    /** 逾期天数*/
	    private Integer overdueDays;
		
		public String getPartnerName() {
			return partnerName;
		}

		public void setPartnerName(String partnerName) {
			this.partnerName = partnerName;
		}

		public String getPartnerHeadImage() {
			return partnerHeadImage;
		}

		public void setPartnerHeadImage(String partnerHeadImage) {
			this.partnerHeadImage = partnerHeadImage;
		}

		public Integer getRepayType() {
			return repayType;
		}

		public void setRepayType(Integer repayType) {
			this.repayType = repayType;
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

		public String getRepayDate() {
			return repayDate;
		}

		public void setRepayDate(String repayDate) {
			this.repayDate = repayDate;
		}

		public String getLoanId() {
			return loanId;
		}

		public void setLoanId(String loanId) {
			this.loanId = loanId;
		}

		public Integer getIsToday() {
			return isToday;
		}

		public void setIsToday(Integer isToday) {
			this.isToday = isToday;
		}

		public Integer getIsOverdue() {
			return isOverdue;
		}

		public void setIsOverdue(Integer isOverdue) {
			this.isOverdue = isOverdue;
		}

		public Integer getOverdueDays() {
			return overdueDays;
		}

		public void setOverdueDays(Integer overdueDays) {
			this.overdueDays = overdueDays;
		}

		public String getOverdueInterest() {
			return overdueInterest;
		}

		public void setOverdueInterest(String overdueInterest) {
			this.overdueInterest = overdueInterest;
		}

		public String getProgress() {
			return progress;
		}

		public void setProgress(String progress) {
			this.progress = progress;
		}

	}

	public List<CrTransferList> getCrTransferList() {
		return crTransferList;
	}

	public void setCrTransferList(List<CrTransferList> crTransferList) {
		this.crTransferList = crTransferList;
	}
	
	
}
