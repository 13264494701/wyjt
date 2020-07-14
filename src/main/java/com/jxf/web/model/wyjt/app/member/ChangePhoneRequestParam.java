package com.jxf.web.model.wyjt.app.member;

public class ChangePhoneRequestParam {

	/** 手机号*/
	private String phoneNo;
	
	/** 验证码*/
	private String smsCode;
	
	/** 0-更换手机号 1-继续更换*/
	private String type;


	public String getPhoneNo() {
		return phoneNo;
	}


	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}


	public String getSmsCode() {
		return smsCode;
	}


	public void setSmsCode(String smsCode) {
		this.smsCode = smsCode;
	}


	public String getType() {
		return type;
	}


	public void setType(String type) {
		this.type = type;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
