package com.jxf.web.model.gxt;


import java.util.ArrayList;
import java.util.List;

public class ArbitrationListResponseResult   {

	/** 仲裁List */
	List<Arbitration> arbitrationList = new ArrayList<Arbitration>();
	
	
	public List<Arbitration> getArbitrationList() {
		return arbitrationList;
	}


	public void setArbitrationList(List<Arbitration> arbitrationList) {
		this.arbitrationList = arbitrationList;
	}


	public static class Arbitration {
	
		/** 借条ID */
		private String loanId;
		
		/** 0 apply 1 detail 2 record*/
		private Integer type;

		/** 仲裁id */
		private String arbitrationId;
		
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
		
		/** 申请时间 */
		private String createDate;
		
		/** 借条开始时间 */
		private String loanStart;
		
		/** 还款时间 */
		private String repayDate;		
		
		/** 借条进度*/
		private String progress;
		
		/** 出裁决时间 */
		private String ruleTime;
		
		/**  剩余天数 */
		private  Integer  surplusDay;
		
		/**  是否申请过强执  0申请过  1未申请*/
		private String applyType;
		
		/**
		 * 逾期天数
		 */
		private Integer day;

		/**  仲裁状态 */
		private String arbitrationStstus;
		
		/** 借条状态 */
		private String loanStatus;
		
		/**
		 * 是否逾期 1已经逾期. 0没有逾期.
		 */
		private String overduePayment;

		/**
		 * 借据方式 1借出高亮。 2借入高亮。 3借出置灰。 4借入置灰
		 */
		private String loanMode = "0";
		
		/**
		 * 控制图标 0:不显示，1：进行中，2：成功，3：失败
		 */
		private Integer speedStatus = 0;
		
		/**
		 * 逾期金额
		 */
		private  String overMoney;
		
		public String getOverMoney() {
			return overMoney;
		}

		public void setOverMoney(String overMoney) {
			this.overMoney = overMoney;
		}

		public Integer getSpeedStatus() {
			return speedStatus;
		}

		public void setSpeedStatus(Integer speedStatus) {
			this.speedStatus = speedStatus;
		}

		public Integer getSurplusDay() {
			return surplusDay;
		}

		public void setSurplusDay(Integer surplusDay) {
			this.surplusDay = surplusDay;
		}

		public Integer getDay() {
			return day;
		}

		public void setDay(Integer day) {
			this.day = day;
		}

		public String getOverduePayment() {
			return overduePayment;
		}

		public void setOverduePayment(String overduePayment) {
			this.overduePayment = overduePayment;
		}

		public String getLoanMode() {
			return loanMode;
		}

		public void setLoanMode(String loanMode) {
			this.loanMode = loanMode;
		}

		public String getLoanStatus() {
			return loanStatus;
		}

		public void setLoanStatus(String loanStatus) {
			this.loanStatus = loanStatus;
		}

		public String getArbitrationStstus() {
			return arbitrationStstus;
		}

		public void setArbitrationStstus(String arbitrationStstus) {
			this.arbitrationStstus = arbitrationStstus;
		}

		public String getLoanId() {
			return loanId;
		}

		public void setLoanId(String loanId) {
			this.loanId = loanId;
		}

		public String getArbitrationId() {
			return arbitrationId;
		}

		public void setArbitrationId(String arbitrationId) {
			this.arbitrationId = arbitrationId;
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

		public String getCreateDate() {
			return createDate;
		}

		public void setCreateDate(String createDate) {
			this.createDate = createDate;
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
		
		public String getRuleTime() {
			return ruleTime;
		}

		public void setRuleTime(String ruleTime) {
			this.ruleTime = ruleTime;
		}

		public Arbitration() {
			super();
		}
		
		public Integer getType() {
			return type;
		}

		public void setType(Integer type) {
			this.type = type;
		}

		public String getApplyType() {
			return applyType;
		}

		public void setApplyType(String applyType) {
			this.applyType = applyType;
		}

		public Arbitration(String loanId, String arbitrationId, String partnerName, String partnerHeadImage,
				Integer repayType, String amount, String interest, String createDate, String repayDate, String progress,
				String ruleTime, int surplusDay, Integer status) {
			super();
			this.loanId = loanId;
			this.arbitrationId = arbitrationId;
			this.partnerName = partnerName;
			this.partnerHeadImage = partnerHeadImage;
			this.repayType = repayType;
			this.amount = amount;
			this.interest = interest;
			this.createDate = createDate;
			this.repayDate = repayDate;
			this.progress = progress;
			this.ruleTime = ruleTime;
			this.surplusDay = surplusDay;
		}

		public String getLoanStart() {
			return loanStart;
		}

		public void setLoanStart(String loanStart) {
			this.loanStart = loanStart;
		}
		
	}			
}
