package com.jxf.web.model.wyjt.app.member;


public class SetPayPwdRequestParam   {
	

	/**支付密码*/
	private String payPassword;
	
	/**验证码*/
	private String smsCode;
	
	/**商户id*/
	private String orderId;

	
	public String getPayPassword() {
		return payPassword;
	}
	public void setPayPassword(String payPassword) {
		this.payPassword = payPassword;
	}
	public String getSmsCode() {
		return smsCode;
	}
	public void setSmsCode(String smsCode) {
		this.smsCode = smsCode;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}	
	
}
