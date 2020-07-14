package com.jxf.nfs.entity;

import org.hibernate.validator.constraints.Length;
import javax.validation.constraints.NotNull;

import com.jxf.svc.sys.crud.entity.CrudEntity;

/**
 * 签约协议Entity
 * @author suhuimin
 * @version 2018-09-30
 */
public class NfsBankProtocol extends CrudEntity<NfsBankProtocol> {
	
	private static final long serialVersionUID = 1L;
			/** 协议号 */
	private String protocolNo;		
			/** 会员信息 */
	private Long memberId;		
			/** 银行卡信息 */
	private String cardNo;		
			/** 银行名称 */
	private String bankName;		
			/** 支付插件 */
	private String paymentPluginId;		
			/** 支付插件名称 */
	private String paymentPluginName;		
	
	public NfsBankProtocol() {
		super();
	}

	public NfsBankProtocol(Long id){
		super(id);
	}

	@Length(min=1, max=127, message="协议号长度必须介于 1 和 127 之间")
	public String getProtocolNo() {
		return protocolNo;
	}

	public void setProtocolNo(String protocolNo) {
		this.protocolNo = protocolNo;
	}
	
	@NotNull(message="会员信息不能为空")
	public Long getMemberId() {
		return memberId;
	}

	public void setMemberId(Long memberId) {
		this.memberId = memberId;
	}
	
	@NotNull(message="银行卡信息不能为空")
	public String getCardNo() {
		return cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}
	
	
	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	@Length(min=1, max=127, message="支付插件Id长度必须介于 1 和 127 之间")
	public String getPaymentPluginId() {
		return paymentPluginId;
	}

	public void setPaymentPluginId(String paymentPluginId) {
		this.paymentPluginId = paymentPluginId;
	}
	
	@Length(min=1, max=127, message="支付插件名称长度必须介于 1 和 127 之间")
	public String getPaymentPluginName() {
		return paymentPluginName;
	}

	public void setPaymentPluginName(String paymentPluginName) {
		this.paymentPluginName = paymentPluginName;
	}
	
}