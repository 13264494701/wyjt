package com.jxf.web.model.wyjt.app.loan;


import java.util.ArrayList;
import java.util.List;

public class LoanListResponseResult   {

	/** 借条List */
	List<Loan> loanList = new ArrayList<Loan>();
	
	public List<Loan> getLoanList() {
		return loanList;
	}
	public void setLoanList(List<Loan> loanList) {
		this.loanList = loanList;
	}
	
	public static class Loan {
	
		/** 借条ID */
		private String loanId;	
		
		/** 姓名 */
		private String partnerName;		
		/** 头像 */
		private String partnerHeadImage;	
		
		/** 还款方式 */
		private Integer repayType;	
		
		/** 借款金额 */
		private String amount;	
		
		/** 利息 */
		private String interest;
		
		/** 还款时间 */
		private String repayDate;		
		
		/** 借条进度*/
		private String progress;
		
				
		public Loan(String loanId, String partnerName, String partnerHeadImage, Integer repayType, String amount,
				String interest, String repayDate, String progress) {
			super();
			this.loanId = loanId;
			this.partnerName = partnerName;
			this.partnerHeadImage = partnerHeadImage;
			this.repayType = repayType;
			this.amount = amount;
			this.interest = interest;
			this.repayDate = repayDate;
			this.progress = progress;
		}
		public Loan(){
			
		}
		public String getLoanId() {
			return loanId;
		}

		public void setLoanId(String loanId) {
			this.loanId = loanId;
		}

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

	}
}
