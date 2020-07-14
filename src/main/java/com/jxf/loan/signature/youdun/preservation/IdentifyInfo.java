package com.jxf.loan.signature.youdun.preservation;

/**
 * 有盾业务保全会员认证数据
 * @author Administrator
 *
 */
public class IdentifyInfo {
	/** 姓名*/
	private String userName;
	/** 证件类型*/
	private String idType;
	/** 证件号码*/
	private String idNo;
	/** 性别*/
	private Integer gender;
	/** 生日*/
	private String birthday;
	/** 地址*/
	private String address;
	/** 手机号*/
	private String mobile;
	/** 邮箱*/
	private String email;
	/** 银行卡开户行*/
	private String bankName;
	/** 银行卡号*/
	private String bankNo;
	/** 平台名称*/
	private String appid;
	/** 身份证人像面 Base64图片*/
	private String idcardFrontPhoto;
	/** 身份证国徽面 Base64图片*/
	private String idcardBackPhoto;
	/** 营业执照照片 Base64图片*/
	private String licensePhoto;
	/** 法定代表人姓名*/
	private String corpLegalName;
	/** 法定代表人职务*/
	private String corpLegalTitle;
	/** 法人手机号码*/
	private String corpLegalMobile;
	/** 法人邮箱*/
	private String corpLegalEmail;
	
	
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getIdType() {
		return idType;
	}
	public void setIdType(String idType) {
		this.idType = idType;
	}
	public String getIdNo() {
		return idNo;
	}
	public void setIdNo(String idNo) {
		this.idNo = idNo;
	}
	public Integer getGender() {
		return gender;
	}
	public void setGender(Integer gender) {
		this.gender = gender;
	}
	public String getBirthday() {
		return birthday;
	}
	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	public String getBankNo() {
		return bankNo;
	}
	public void setBankNo(String bankNo) {
		this.bankNo = bankNo;
	}
	public String getAppid() {
		return appid;
	}
	public void setAppid(String appid) {
		this.appid = appid;
	}
	public String getIdcardFrontPhoto() {
		return idcardFrontPhoto;
	}
	public void setIdcardFrontPhoto(String idcardFrontPhoto) {
		this.idcardFrontPhoto = idcardFrontPhoto;
	}
	public String getIdcardBackPhoto() {
		return idcardBackPhoto;
	}
	public void setIdcardBackPhoto(String idcardBackPhoto) {
		this.idcardBackPhoto = idcardBackPhoto;
	}
	public String getLicensePhoto() {
		return licensePhoto;
	}
	public void setLicensePhoto(String licensePhoto) {
		this.licensePhoto = licensePhoto;
	}
	public String getCorpLegalName() {
		return corpLegalName;
	}
	public void setCorpLegalName(String corpLegalName) {
		this.corpLegalName = corpLegalName;
	}
	public String getCorpLegalTitle() {
		return corpLegalTitle;
	}
	public void setCorpLegalTitle(String corpLegalTitle) {
		this.corpLegalTitle = corpLegalTitle;
	}
	public String getCorpLegalMobile() {
		return corpLegalMobile;
	}
	public void setCorpLegalMobile(String corpLegalMobile) {
		this.corpLegalMobile = corpLegalMobile;
	}
	public String getCorpLegalEmail() {
		return corpLegalEmail;
	}
	public void setCorpLegalEmail(String corpLegalEmail) {
		this.corpLegalEmail = corpLegalEmail;
	}
	
}
