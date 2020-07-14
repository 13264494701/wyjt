package com.jxf.web.model.wyjt.app.card;

public class BindBankCardRequestParam {

	/** 银行卡号 */
	private String cardNo;
	
	/** 验证码 */
	private String smsCode;


	public String getCardNo() {
		return cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}

	public String getSmsCode() {
		return smsCode;
	}

	public void setSmsCode(String smsCode) {
		this.smsCode = smsCode;
	}

	
	
}
