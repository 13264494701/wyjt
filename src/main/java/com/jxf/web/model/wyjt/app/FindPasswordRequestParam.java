package com.jxf.web.model.wyjt.app;


public class FindPasswordRequestParam   {
	
	/**手机号码*/
	private String phoneNo;
	/**短信验证码*/
	private String smsCode;
	/** 新密码 */
	private String password;
	

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

	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	
	
	
}
