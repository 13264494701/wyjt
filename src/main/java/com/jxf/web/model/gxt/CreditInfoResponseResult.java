package com.jxf.web.model.gxt;

import java.util.HashMap;
import java.util.Map;

public class CreditInfoResponseResult {

	/** 头像*/
	private String headImage;
	/** 姓名*/
	private String name;
	/** 性别*/
	private Integer gender;
	/** 肖像*/
	private Integer realIdentityStatus;
	/** 银行卡*/
	private Integer bankCardStatus;
	
	/** 借入明细*/
	Map<String,Object> borrowDetailMap1 = new HashMap<String,Object>();
	Map<String,Object> borrowDetailMap2 = new HashMap<String,Object>();
	Map<String,Object> borrowDetailMap3 = new HashMap<String,Object>();
	/** 逾期记录*/
	Map<String,Object> overdueDetailMap1 = new HashMap<String,Object>();
	Map<String,Object> overdueDetailMap2 = new HashMap<String,Object>();
	Map<String,Object> overdueDetailMap3 = new HashMap<String,Object>();
	/** 借贷统计*/
	Map<String,Object> loanDetailMap1 = new HashMap<String,Object>();
	Map<String,Object> loanDetailMap2 = new HashMap<String,Object>();
	Map<String,Object> loanDetailMap3 = new HashMap<String,Object>();
	public String getHeadImage() {
		return headImage;
	}
	public void setHeadImage(String headImage) {
		this.headImage = headImage;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getGender() {
		return gender;
	}
	public void setGender(Integer gender) {
		this.gender = gender;
	}
	public Integer getRealIdentityStatus() {
		return realIdentityStatus;
	}
	public void setRealIdentityStatus(Integer realIdentityStatus) {
		this.realIdentityStatus = realIdentityStatus;
	}
	public Integer getBankCardStatus() {
		return bankCardStatus;
	}
	public void setBankCardStatus(Integer bankCardStatus) {
		this.bankCardStatus = bankCardStatus;
	}
	public Map<String, Object> getBorrowDetailMap1() {
		return borrowDetailMap1;
	}
	public void setBorrowDetailMap1(Map<String, Object> borrowDetailMap1) {
		this.borrowDetailMap1 = borrowDetailMap1;
	}
	public Map<String, Object> getBorrowDetailMap2() {
		return borrowDetailMap2;
	}
	public void setBorrowDetailMap2(Map<String, Object> borrowDetailMap2) {
		this.borrowDetailMap2 = borrowDetailMap2;
	}
	public Map<String, Object> getBorrowDetailMap3() {
		return borrowDetailMap3;
	}
	public void setBorrowDetailMap3(Map<String, Object> borrowDetailMap3) {
		this.borrowDetailMap3 = borrowDetailMap3;
	}
	public Map<String, Object> getOverdueDetailMap1() {
		return overdueDetailMap1;
	}
	public void setOverdueDetailMap1(Map<String, Object> overdueDetailMap1) {
		this.overdueDetailMap1 = overdueDetailMap1;
	}
	public Map<String, Object> getOverdueDetailMap2() {
		return overdueDetailMap2;
	}
	public void setOverdueDetailMap2(Map<String, Object> overdueDetailMap2) {
		this.overdueDetailMap2 = overdueDetailMap2;
	}
	public Map<String, Object> getOverdueDetailMap3() {
		return overdueDetailMap3;
	}
	public void setOverdueDetailMap3(Map<String, Object> overdueDetailMap3) {
		this.overdueDetailMap3 = overdueDetailMap3;
	}
	public Map<String, Object> getLoanDetailMap1() {
		return loanDetailMap1;
	}
	public void setLoanDetailMap1(Map<String, Object> loanDetailMap1) {
		this.loanDetailMap1 = loanDetailMap1;
	}
	public Map<String, Object> getLoanDetailMap2() {
		return loanDetailMap2;
	}
	public void setLoanDetailMap2(Map<String, Object> loanDetailMap2) {
		this.loanDetailMap2 = loanDetailMap2;
	}
	public Map<String, Object> getLoanDetailMap3() {
		return loanDetailMap3;
	}
	public void setLoanDetailMap3(Map<String, Object> loanDetailMap3) {
		this.loanDetailMap3 = loanDetailMap3;
	}
	
	
	
	
}
