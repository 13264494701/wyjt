package com.jxf.web.model.wyjt.app.loan;

import java.util.ArrayList;
import java.util.List;

/**
 * @作者: xiaorongdian
 * @创建时间 :2018年11月29日 上午11:18:39
 * @功能说明:借条列表APP展示
 */
public class LoanListForAppResponseResult {
	/** 借条List */
	private List<LoanForApp> loanList = new ArrayList<LoanForApp>();
	
	public static class LoanForApp {
	
		/** 借条ID */
		private String loanId;
		/** 类型(用来跳转详情用) */
		private Integer type;
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
	    /**
	     * 催收/仲裁时右边图标
	     * 0：不显示 1进行中 2成功 3失败
	     */
	    private Integer iconStatus;
		
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

		public String getProgress() {
			return progress;
		}

		public void setProgress(String progress) {
			this.progress = progress;
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

		public Integer getIconStatus() {
			return iconStatus;
		}

		public void setIconStatus(Integer iconStatus) {
			this.iconStatus = iconStatus;
		}

		public Integer getType() {
			return type;
		}

		public void setType(Integer type) {
			this.type = type;
		}

	}

	public List<LoanForApp> getLoanList() {
		return loanList;
	}

	public void setLoanList(List<LoanForApp> loanList) {
		this.loanList = loanList;
	}
}
