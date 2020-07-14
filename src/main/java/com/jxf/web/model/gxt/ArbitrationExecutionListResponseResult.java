package com.jxf.web.model.gxt;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ArbitrationExecutionListResponseResult   {

	/** 仲裁List */
	List<ArbitrationExecution> arbitrationList = new ArrayList<ArbitrationExecution>();
	
	
	public List<ArbitrationExecution> getArbitrationList() {
		return arbitrationList;
	}


	public void setArbitrationList(List<ArbitrationExecution> arbitrationList) {
		this.arbitrationList = arbitrationList;
	}


	public static class ArbitrationExecution {
	
		/**
		 * 借款单Id
		 */
		private String loanType;
		
		/**
		 * 仲裁id
		 */
		private String arbitratId;
		
		/**
		 * 强执id
		 */
		private String executionId;

		/**
		 * 头像url地址
		 */
		private String headImageUrl;

		/**
		 * 当前强执状态
		 */
		private String status;

		/**
		 * 金额
		 */
		private String amount;

		/**
		 * 利息
		 */
		private String interest;
		
		/**
		 * 借款人姓名
		 * @return
		 */
		private  String loaneeName;
		
		/** 出借时间 */
		private String loanStart;
		
		/** 还款时间 */
		private String repayDate;
		/** 缴费支付时的用户信息*/
		private Map<String, Object> userInfo;
		
		public String getRepayDate() {
			return repayDate;
		}

		public void setRepayDate(String repayDate) {
			this.repayDate = repayDate;
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

		public String getArbitratId() {
			return arbitratId;
		}

		public void setArbitratId(String arbitratId) {
			this.arbitratId = arbitratId;
		}

		public String getStatus() {
			return status;
		}

		public void setStatus(String status) {
			this.status = status;
		}

		public String getExecutionId() {
			return executionId;
		}

		public void setExecutionId(String executionId) {
			this.executionId = executionId;
		}

		public String getHeadImageUrl() {
			return headImageUrl;
		}

		public void setHeadImageUrl(String headImageUrl) {
			this.headImageUrl = headImageUrl;
		}

		public String getLoaneeName() {
			return loaneeName;
		}

		public void setLoaneeName(String loaneeName) {
			this.loaneeName = loaneeName;
		}

		public String getLoanStart() {
			return loanStart;
		}

		public void setLoanStart(String loanStart) {
			this.loanStart = loanStart;
		}

		public String getLoanType() {
			return loanType;
		}

		public void setLoanType(String loanType) {
			this.loanType = loanType;
		}

		public Map<String, Object> getUserInfo() {
			return userInfo;
		}

		public void setUserInfo(Map<String, Object> userInfo) {
			this.userInfo = userInfo;
		}
		
	}			
}
