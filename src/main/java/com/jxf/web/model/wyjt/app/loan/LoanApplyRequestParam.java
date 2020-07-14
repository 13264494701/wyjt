package com.jxf.web.model.wyjt.app.loan;

/**
 * @作者: xiaorongdian
 * @创建时间 :2018年10月30日 下午4:12:48
 * @功能说明:发起借款
 */
public class LoanApplyRequestParam   {
	
	/** 申请人角色 */
	private Integer loanRole;//0->借款人,1->出借人	
	/** 借款类型*/
	private Integer loanType;//0->单人,1->多人
	/** 借款用途 */
	private Integer loanPurp;//0->资金周转,1->交房租,2->消费,3->还信用卡,4->报培训班,5->考驾照,6->其它		
	/** 借款金额 */
	private String amount;		
	/** 借款利率 */
	private String intRate;//不含%，如年化15.5%利率，只传15.5
	/** 还款方式 */
	private Integer repayType;//0->全额，1->分期		
	/** 借款期限 */
	private Integer term;//借款天数	
	
	/** 借贷对象 */
	private String partners;//好友ID "|" 分割
	
	
	public Integer getLoanRole() {
		return loanRole;
	}

	public void setLoanRole(Integer loanRole) {
		this.loanRole = loanRole;
	}

	public Integer getLoanType() {
		return loanType;
	}

	public void setLoanType(Integer loanType) {
		this.loanType = loanType;
	}

	public Integer getLoanPurp() {
		return loanPurp;
	}

	public void setLoanPurp(Integer loanPurp) {
		this.loanPurp = loanPurp;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getIntRate() {
		return intRate;
	}

	public void setIntRate(String intRate) {
		this.intRate = intRate;
	}

	public Integer getRepayType() {
		return repayType;
	}

	public void setRepayType(Integer repayType) {
		this.repayType = repayType;
	}

	public Integer getTerm() {
		return term;
	}

	public void setTerm(Integer term) {
		this.term = term;
	}

	public String getPartners() {
		return partners;
	}

	public void setPartners(String partners) {
		this.partners = partners;
	}

}
