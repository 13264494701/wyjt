package com.jxf.web.model.wyjt.app.loan;


public class LoanNewApplyResponseResult   {
	
	/**
	 * 申请ID
	 */
	private String applyId;
	
	/** 好友角色 */
	private Integer loanRole;//0->借款人，1->放款人 
	/**好友姓名 */
	private String friendName;
	/**好友头像 */
	private String friendHeadImage;
	/**好友信用级别 */
	private String friendCreditRank;
	/**好友信用描述 */
	private String friendCreditDesc;
	
	/** 借款金额 */
	private String amount;		
	/** 借款利息 */
	private String interest;
	/** 借款时长 */
	private Integer term;
	/** 还款方式 */
	private String repayType;	
	/** 类型 用来跳转详情用 0 apply 1 detail 2 record */
	private Integer type;	
	
	
	public String getApplyId() {
		return applyId;
	}
	public void setApplyId(String applyId) {
		this.applyId = applyId;
	}
	public Integer getLoanRole() {
		return loanRole;
	}
	public void setLoanRole(Integer loanRole) {
		this.loanRole = loanRole;
	}
	public String getFriendName() {
		return friendName;
	}
	public void setFriendName(String friendName) {
		this.friendName = friendName;
	}
	public String getFriendHeadImage() {
		return friendHeadImage;
	}
	public void setFriendHeadImage(String friendHeadImage) {
		this.friendHeadImage = friendHeadImage;
	}
	public String getFriendCreditRank() {
		return friendCreditRank;
	}
	public void setFriendCreditRank(String friendCreditRank) {
		this.friendCreditRank = friendCreditRank;
	}
	public String getFriendCreditDesc() {
		return friendCreditDesc;
	}
	public void setFriendCreditDesc(String friendCreditDesc) {
		this.friendCreditDesc = friendCreditDesc;
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
	public Integer getTerm() {
		return term;
	}
	public void setTerm(Integer term) {
		this.term = term;
	}	

	public String getRepayType() {
		return repayType;
	}
	public void setRepayType(String repayType) {
		this.repayType = repayType;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}


	
}
