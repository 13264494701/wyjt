package com.jxf.loan.signature.youdun.preservation;

/**
 * 有盾业务保全续签合同数据
 * @author Administrator
 *
 */
public class RenewalInfo {
	/** 借款合同名称*/
	private String contractName;
	/** 借款合同编号*/
	private String contractCode;
	/** 上一条借款合同编号*/
	private String lastContractCode;
	/** 签订时间*/
	private String signTime;
	/** 借款本金*/
	private String loanAmount;
	/** 借款期限（起止日）*/
	private String loanStartEndDate;
	/** 年化利率*/
	private String rateYear;
	/** 宽限期*/
	private Integer graceDays;
	/** 逾期日*/
	private String overdueDate;
	/** 逾期年化利率*/
	private String overdueRate;
	/** 还款方式*/
	private String repayType;
	/** 借款用途*/
	private String loanPurpose;
	/** 出借人姓名/名称*/
	private String pUserName;
	/** 出借人证件号码*/
	private String pIdNo;
	/** 居间人名称*/
	private String mUserName;
	/** 居间人证件号码*/
	private String mIdNo;
	/** 借款人姓名/名称*/
	private String userName;
	/** 借款人证件号码*/
	private String idNo;
	/** 借款服务协议编号*/
	private String udContractCode;
	
	
	public String getContractName() {
		return contractName;
	}
	public void setContractName(String contractName) {
		this.contractName = contractName;
	}
	public String getContractCode() {
		return contractCode;
	}
	public void setContractCode(String contractCode) {
		this.contractCode = contractCode;
	}
	public String getLastContractCode() {
		return lastContractCode;
	}
	public void setLastContractCode(String lastContractCode) {
		this.lastContractCode = lastContractCode;
	}
	public String getSignTime() {
		return signTime;
	}
	public void setSignTime(String signTime) {
		this.signTime = signTime;
	}
	public String getLoanAmount() {
		return loanAmount;
	}
	public void setLoanAmount(String loanAmount) {
		this.loanAmount = loanAmount;
	}
	public String getLoanStartEndDate() {
		return loanStartEndDate;
	}
	public void setLoanStartEndDate(String loanStartEndDate) {
		this.loanStartEndDate = loanStartEndDate;
	}
	public String getRateYear() {
		return rateYear;
	}
	public void setRateYear(String rateYear) {
		this.rateYear = rateYear;
	}
	public Integer getGraceDays() {
		return graceDays;
	}
	public void setGraceDays(Integer graceDays) {
		this.graceDays = graceDays;
	}
	public String getOverdueDate() {
		return overdueDate;
	}
	public void setOverdueDate(String overdueDate) {
		this.overdueDate = overdueDate;
	}
	public String getOverdueRate() {
		return overdueRate;
	}
	public void setOverdueRate(String overdueRate) {
		this.overdueRate = overdueRate;
	}
	public String getRepayType() {
		return repayType;
	}
	public void setRepayType(String repayType) {
		this.repayType = repayType;
	}
	public String getLoanPurpose() {
		return loanPurpose;
	}
	public void setLoanPurpose(String loanPurpose) {
		this.loanPurpose = loanPurpose;
	}
	public String getpUserName() {
		return pUserName;
	}
	public void setpUserName(String pUserName) {
		this.pUserName = pUserName;
	}
	public String getpIdNo() {
		return pIdNo;
	}
	public void setpIdNo(String pIdNo) {
		this.pIdNo = pIdNo;
	}
	public String getmUserName() {
		return mUserName;
	}
	public void setmUserName(String mUserName) {
		this.mUserName = mUserName;
	}
	public String getmIdNo() {
		return mIdNo;
	}
	public void setmIdNo(String mIdNo) {
		this.mIdNo = mIdNo;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getIdNo() {
		return idNo;
	}
	public void setIdNo(String idNo) {
		this.idNo = idNo;
	}
	public String getUdContractCode() {
		return udContractCode;
	}
	public void setUdContractCode(String udContractCode) {
		this.udContractCode = udContractCode;
	}
}
