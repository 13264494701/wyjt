package com.jxf.web.model.wyjt.app;


/***
 * 注册请求接口
 * @author wo
 *
 */
public class RegisterSubmitRequestParam  {

	/**手机号码(用户名)*/
    private String phoneNo;
    
    /**用户密码*/
    private String password;
    
	/**图形验证码*/
	private String wxUserInfoId;
	
	/**短信验证码*/
    private String smsVerifyCode;

	public String getPhoneNo() {
		return phoneNo;
	}

	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getSmsVerifyCode() {
		return smsVerifyCode;
	}

	public void setSmsVerifyCode(String smsVerifyCode) {
		this.smsVerifyCode = smsVerifyCode;
	}

	public String getWxUserInfoId() {
		return wxUserInfoId;
	}

	public void setWxUserInfoId(String wxUserInfoId) {
		this.wxUserInfoId = wxUserInfoId;
	}


}

