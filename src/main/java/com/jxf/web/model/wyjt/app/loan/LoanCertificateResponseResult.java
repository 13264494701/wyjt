package com.jxf.web.model.wyjt.app.loan;
/**
 * @作者: xiaorongdian
 * @创建时间 :2018年12月5日 上午11:28:31
 * @功能说明:查看电子借条
 */
public class LoanCertificateResponseResult {

	/** 单据编号 */
	private String loanNo;
	
	/** 出借人姓名(甲方) */
	private String loanerName;
	
	/** 出借人电话 */
	private String loanerPhoneNo;
	
	/** 出借人邮箱 */
	private String loanerEmail;
	
	/** 出借人身份证号 */
	private String loanerIdNo;
	
	/** 借款人姓名(乙方) */
	private String loaneeName;
	
	/** 借款人电话 */
	private String loaneePhoneNo;
	
	/** 借款人邮箱 */
	private String loaneeEmail;
	
	/** 借款人身份证号 */
	private String loaneeIdNo;
	
	/** 借款金额 */
	private String amount;
	
	/** 借款时长(天数) */
	private Integer term;
	
	/** 借款用途 */
	private String loanPurp;
	
	/** 利息 */
	private String interest;
	
	/** 还款方式0全额1等额本息 */
	private Integer repayType;
	
	/** 逾期利息 */
	private String overdueInterest;
	
	/** 合同地址 */
    private String contractUrl;	
    
    /** 争议解决方式文案*/
    private String disputeResolution;

	public String getLoanNo() {
		return loanNo;
	}

	public void setLoanNo(String loanNo) {
		this.loanNo = loanNo;
	}

	public String getLoanerName() {
		return loanerName;
	}

	public void setLoanerName(String loanerName) {
		this.loanerName = loanerName;
	}

	public String getLoanerPhoneNo() {
		return loanerPhoneNo;
	}

	public void setLoanerPhoneNo(String loanerPhoneNo) {
		this.loanerPhoneNo = loanerPhoneNo;
	}

	public String getLoanerEmail() {
		return loanerEmail;
	}

	public void setLoanerEmail(String loanerEmail) {
		this.loanerEmail = loanerEmail;
	}

	public String getLoanerIdNo() {
		return loanerIdNo;
	}

	public void setLoanerIdNo(String loanerIdNo) {
		this.loanerIdNo = loanerIdNo;
	}

	public String getLoaneeName() {
		return loaneeName;
	}

	public void setLoaneeName(String loaneeName) {
		this.loaneeName = loaneeName;
	}

	public String getLoaneePhoneNo() {
		return loaneePhoneNo;
	}

	public void setLoaneePhoneNo(String loaneePhoneNo) {
		this.loaneePhoneNo = loaneePhoneNo;
	}

	public String getLoaneeEmail() {
		return loaneeEmail;
	}

	public void setLoaneeEmail(String loaneeEmail) {
		this.loaneeEmail = loaneeEmail;
	}

	public String getLoaneeIdNo() {
		return loaneeIdNo;
	}

	public void setLoaneeIdNo(String loaneeIdNo) {
		this.loaneeIdNo = loaneeIdNo;
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

	public Integer getRepayType() {
		return repayType;
	}

	public void setRepayType(Integer repayType) {
		this.repayType = repayType;
	}

	public String getOverdueInterest() {
		return overdueInterest;
	}

	public void setOverdueInterest(String overdueInterest) {
		this.overdueInterest = overdueInterest;
	}

	public Integer getTerm() {
		return term;
	}

	public void setTerm(Integer term) {
		this.term = term;
	}

	public String getLoanPurp() {
		return loanPurp;
	}

	public void setLoanPurp(String loanPurp) {
		this.loanPurp = loanPurp;
	}

	public String getContractUrl() {
		return contractUrl;
	}

	public void setContractUrl(String contractUrl) {
		this.contractUrl = contractUrl;
	}

	public String getDisputeResolution() {
		return disputeResolution;
	}

	public void setDisputeResolution(String disputeResolution) {
		this.disputeResolution = disputeResolution;
	}

}
