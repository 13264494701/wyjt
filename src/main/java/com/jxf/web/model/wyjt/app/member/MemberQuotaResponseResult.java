package com.jxf.web.model.wyjt.app.member;

import java.util.ArrayList;
import java.util.List;

/**
 *  e额度评估页面返回实体
 * @author Administrator
 *
 */
public class MemberQuotaResponseResult {

	/** 是否已评估 0否，1是 */
	private Integer hasQuota;
	/** 额度分值 */
	private String quota;
	/** 评估时间 */
	private String assessmentTime;
	/** 是否可以再次评估 0否，1是*/
	private Integer canAssessmentAgain;
	/** 评估前提示语 */
	private String reminder0fBeforeAssessment;
	/** 提示语 */
	private String reminder;
	/** 禁止再次评估提示语 */
	private String reminderOfForbidenAssessmetn;
	/** 贷超推荐位 */
	private List<LoanMarket> recommendLoanMarketList = new ArrayList<LoanMarket>();
	
	public class LoanMarket {
		//贷超id
		private String loanMarketId;
		//贷超名字
		private String loanMarketName;
		//最小放款金额
		private String minAmount;
		//最大放款金额
		private String maxAmount;
		//展示的放款笔数
		private String showLoanCount;
		//图标地址
		private String iconUrl;
		
		public String getLoanMarketId() {
			return loanMarketId;
		}
		public void setLoanMarketId(String loanMarketId) {
			this.loanMarketId = loanMarketId;
		}
		public String getLoanMarketName() {
			return loanMarketName;
		}
		public void setLoanMarketName(String loanMarketName) {
			this.loanMarketName = loanMarketName;
		}
		public String getMinAmount() {
			return minAmount;
		}
		public void setMinAmount(String minAmount) {
			this.minAmount = minAmount;
		}
		public String getMaxAmount() {
			return maxAmount;
		}
		public void setMaxAmount(String maxAmount) {
			this.maxAmount = maxAmount;
		}
		public String getShowLoanCount() {
			return showLoanCount;
		}
		public void setShowLoanCount(String showLoanCount) {
			this.showLoanCount = showLoanCount;
		}
		public String getIconUrl() {
			return iconUrl;
		}
		public void setIconUrl(String iconUrl) {
			this.iconUrl = iconUrl;
		}
	}
	
	public Integer getHasQuota() {
		return hasQuota;
	}
	public void setHasQuota(Integer hasQuota) {
		this.hasQuota = hasQuota;
	}
	public String getQuota() {
		return quota;
	}
	public void setQuota(String quota) {
		this.quota = quota;
	}
	public String getAssessmentTime() {
		return assessmentTime;
	}
	public void setAssessmentTime(String assessmentTime) {
		this.assessmentTime = assessmentTime;
	}
	public Integer getCanAssessmentAgain() {
		return canAssessmentAgain;
	}
	public void setCanAssessmentAgain(Integer canAssessmentAgain) {
		this.canAssessmentAgain = canAssessmentAgain;
	}
	public String getReminder0fBeforeAssessment() {
		return reminder0fBeforeAssessment;
	}
	public void setReminder0fBeforeAssessment(String reminder0fBeforeAssessment) {
		this.reminder0fBeforeAssessment = reminder0fBeforeAssessment;
	}
	public String getReminder() {
		return reminder;
	}
	public void setReminder(String reminder) {
		this.reminder = reminder;
	}
	public String getReminderOfForbidenAssessmetn() {
		return reminderOfForbidenAssessmetn;
	}
	public void setReminderOfForbidenAssessmetn(String reminderOfForbidenAssessmetn) {
		this.reminderOfForbidenAssessmetn = reminderOfForbidenAssessmetn;
	}
	public List<LoanMarket> getRecommendLoanMarketList() {
		return recommendLoanMarketList;
	}
	public void setRecommendLoanMarketList(List<LoanMarket> recommendLoanMarketList) {
		this.recommendLoanMarketList = recommendLoanMarketList;
	}
	
}
