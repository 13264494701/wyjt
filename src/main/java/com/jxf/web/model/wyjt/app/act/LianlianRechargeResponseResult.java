package com.jxf.web.model.wyjt.app.act;


/**
 * lianlian充值返回实体类
 * @author suhuimin
 *
 */
public class LianlianRechargeResponseResult  {
	/**
	 * 连连下单请求url
	 */
	private String gatewayUrl;
	/**
	 * 银行名称
	 */
	private String bankName;
	/**
	 * 银行卡号
	 */
	private String cardNo;

	public String getGatewayUrl() {
		return gatewayUrl;
	}

	public void setGatewayUrl(String gatewayUrl) {
		this.gatewayUrl = gatewayUrl;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getCardNo() {
		return cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}
	
}
