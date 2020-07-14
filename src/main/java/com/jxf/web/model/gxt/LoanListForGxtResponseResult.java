package com.jxf.web.model.gxt;

import java.util.ArrayList;
import java.util.List;

/**
 * @作者: xiaorongdian
 * @创建时间 :2019年4月26日 上午11:18:39
 * @功能说明:借条列表公信堂展示
 */
public class LoanListForGxtResponseResult {
	/** 借条List */
	private List<LoanForGxt> loanList = new ArrayList<LoanForGxt>();
	
	public static class LoanForGxt {
	
		/** 借条ID */
		private String loanId;
		/** 请求详情用 */
		private String loan_type;
		/** 姓名 */
		private String partnerName;		
		/** 头像 */
		private String partnerHeadImage;	
		/** 借款金额 */
		private String amount;
		/** 利息 */
		private String interest;
		/** 开始时间 */
		private String loanStart;		
		/** 还款时间 */
		private String repayDate;		
		/** 借条状态 0:待确认 
		 * 1:已取消 
		 * 2:被拒绝
		 * 3:已超时 
		 * 4:今日还款/今日收款 
		 * 5:距离还款/收款日30天之内 
		 * 6:距离还款/收款日30天以上 
		 * 7:延期待确认 
		 * 8:还款/收款待确认
		 * 9:今日逾期
		 * 10:已逾期未超过15天
		 * 11:已逾期超过15天 
		 * 12:已完成 
		 * */
		private Integer status;
		/** 借条距离还款天数/逾期天数 status=5 或者 10  时使用*/
		private Integer days;
		/**
	     * 仲裁/强执图标
	     * 0:不显示 1仲裁 2强执 
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

		public Integer getIconStatus() {
			return iconStatus;
		}

		public void setIconStatus(Integer iconStatus) {
			this.iconStatus = iconStatus;
		}

		public Integer getDays() {
			return days;
		}

		public void setDays(Integer days) {
			this.days = days;
		}

		public Integer getStatus() {
			return status;
		}

		public void setStatus(Integer status) {
			this.status = status;
		}

		public String getLoanStart() {
			return loanStart;
		}

		public void setLoanStart(String loanStart) {
			this.loanStart = loanStart;
		}

		public String getLoan_type() {
			return loan_type;
		}

		public void setLoan_type(String loan_type) {
			this.loan_type = loan_type;
		}

	}

	public List<LoanForGxt> getLoanList() {
		return loanList;
	}

	public void setLoanList(List<LoanForGxt> loanList) {
		this.loanList = loanList;
	}
}
