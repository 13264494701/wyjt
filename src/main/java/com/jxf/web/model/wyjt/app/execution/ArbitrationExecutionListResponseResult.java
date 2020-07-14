package com.jxf.web.model.wyjt.app.execution;


import java.util.ArrayList;
import java.util.List;

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
		private String loanId;

		/**
		 * 头像url地址
		 */
		private String imgUrl;

		/**
		 * 描述 0:全额  1：分期
		 */
		private String descript;

		/**
		 * 当前强制状态
		 */
		private String status;

		/**
		 * 金额
		 */
		private String money;

		/**
		 * 利息
		 */
		private String interest;

		/**
		 * 控制图标 0:不显示，1：进行中，2：成功，3：失败
		 */
		private Integer speedStatus = 0;
		
		/**
		 * 制裁id
		 */
		private String arbitratId;
		
		/**
		 * 页面跳转类型 mode 1：申请强制  2： 付费接口  3：仲裁进度
		 */
		private String mode;
		
		/**
		 * 强制id
		 */
		private String executionId;
		/**
		 * 逾期天数
		 */
		private Integer day;
		
		/**
		 * 出裁决时间
		 */
		private String ruleTime;
		
		/**
		 * 剩余时间
		 */
		private  Integer  surplusDay;
		
		/**
		 * 借款人姓名
		 * @return
		 */
		private  String loanee_name;
		
		/**
		 * 逾期金额
		 */
		private  String overMoney;
		
		/** 还款时间 */
		private String repayDate;
		
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

		public String getImgUrl() {
			return imgUrl;
		}

		public void setImgUrl(String imgUrl) {
			this.imgUrl = imgUrl;
		}

		public String getDescript() {
			return descript;
		}

		public void setDescript(String descript) {
			this.descript = descript;
		}

		public String getMoney() {
			return money;
		}

		public void setMoney(String money) {
			this.money = money;
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

		public String getMode() {
			return mode;
		}

		public void setMode(String mode) {
			this.mode = mode;
		}

		public String getRuleTime() {
			return ruleTime;
		}

		public void setRuleTime(String ruleTime) {
			this.ruleTime = ruleTime;
		}

		public String getOverMoney() {
			return overMoney;
		}

		public void setOverMoney(String overMoney) {
			this.overMoney = overMoney;
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

		public String getLoanee_name() {
			return loanee_name;
		}

		public void setLoanee_name(String loanee_name) {
			this.loanee_name = loanee_name;
		}

		public Integer getSpeedStatus() {
			return speedStatus;
		}

		public void setSpeedStatus(Integer speedStatus) {
			this.speedStatus = speedStatus;
		}

		public Integer getDay() {
			return day;
		}

		public void setDay(Integer day) {
			this.day = day;
		}

		public Integer getSurplusDay() {
			return surplusDay;
		}

		public void setSurplusDay(Integer surplusDay) {
			this.surplusDay = surplusDay;
		}
		
		
	}			
}
