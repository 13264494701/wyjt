package com.jxf.web.model;

/***
 * 
 * @author wo
 *
 */
public class CheckSmsRequestParam {


	/**手机号码*/
	private String phoneNo;
	/**短信验证码*/
	private String smsCode;

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

}
