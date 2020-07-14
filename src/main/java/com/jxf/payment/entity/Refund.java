package com.jxf.payment.entity;

import org.hibernate.validator.constraints.Length;

import java.math.BigDecimal;
import java.util.Date;
import com.alibaba.fastjson.annotation.JSONField;
import com.jxf.mem.entity.Member;
import com.jxf.svc.sys.crud.entity.CrudEntity;

/**
 * 退款Entity
 * @author wo
 * @version 2018-08-11
 */
public class Refund extends CrudEntity<Refund> {
	
	private static final long serialVersionUID = 1L;
	/**
	 * 状态
	 */
	public enum Status {

		/** 待审核 */
		pendingReview,
		
		/** 待退款 */
		pendingRefund,

		/** 退款成功 */
		success,

		/** 退款失败 */
		failure,
		
		/** 其它 */
		other
	}
	
	/** 退款单号 */
	private String refundNo;		
	/** 原支付单 */
	private Payment payment;		
	/** 会员 */
	private Member member;		
	
	/** 原业务编号 */
	private String orgId;		
	/** 退款金额 */
	private BigDecimal refundAmount;		
	/** 退款状态 */
	private Status status;		
	/** 支付插件ID */
	private String paymentPluginId;		
	/** 支付插件名称 */
	private String paymentPluginName;		
	/** 商户号 */
	private String mchId;	
	/**商户秘钥*/
	private String key;
	
	/** 支付平台流水号 */
	private String refundId;		
	/** 退款原因 */
	private String refundDesc;		
	/** 响应时间 */
	private Date respTime;		
	/** 响应编码 */
	private String respCode;		
	/** 响应信息 */
	private String respMsg;		
	/** 客户端IP */
	private String remoteAddr;	
	
	/**支付请求地址*/
	private String requestUrl;  
	/**支付回调地址*/
    private String notifyUrl; 
	
	public Refund() {
		super();
	}

	public Refund(Long id){
		super(id);
	}

	@Length(min=1, max=16, message="退款单号长度必须介于 1 和 16 之间")
	public String getRefundNo() {
		return refundNo;
	}

	public void setRefundNo(String refundNo) {
		this.refundNo = refundNo;
	}
	
	public Payment getPayment() {
		return payment;
	}

	public void setPayment(Payment payment) {
		this.payment = payment;
	}
	
	public Member getMember() {
		return member;
	}

	public void setMember(Member member) {
		this.member = member;
	}
	
	@Length(min=1, max=32, message="原业务编号长度必须介于 1 和 32 之间")
	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	
	public BigDecimal getRefundAmount() {
		return refundAmount;
	}

	public void setRefundAmount(BigDecimal refundAmount) {
		this.refundAmount = refundAmount;
	}
	
	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}
	
	@Length(min=0, max=127, message="支付插件ID长度必须介于 0 和 127 之间")
	public String getPaymentPluginId() {
		return paymentPluginId;
	}

	public void setPaymentPluginId(String paymentPluginId) {
		this.paymentPluginId = paymentPluginId;
	}
	
	@Length(min=0, max=127, message="支付插件名称长度必须介于 0 和 127 之间")
	public String getPaymentPluginName() {
		return paymentPluginName;
	}

	public void setPaymentPluginName(String paymentPluginName) {
		this.paymentPluginName = paymentPluginName;
	}
	
	@Length(min=0, max=64, message="商户号长度必须介于 0 和 64 之间")
	public String getMchId() {
		return mchId;
	}

	public void setMchId(String mchId) {
		this.mchId = mchId;
	}
	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}
	@Length(min=0, max=64, message="支付平台流水号长度必须介于 0 和 64 之间")
	public String getRefundId() {
		return refundId;
	}

	public void setRefundId(String refundId) {
		this.refundId = refundId;
	}
	
	@Length(min=0, max=255, message="退款原因长度必须介于 0 和 255 之间")
	public String getRefundDesc() {
		return refundDesc;
	}

	public void setRefundDesc(String refundDesc) {
		this.refundDesc = refundDesc;
	}
	
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	public Date getRespTime() {
		return respTime;
	}

	public void setRespTime(Date respTime) {
		this.respTime = respTime;
	}
	
	@Length(min=0, max=64, message="响应编码长度必须介于 0 和 64 之间")
	public String getRespCode() {
		return respCode;
	}

	public void setRespCode(String respCode) {
		this.respCode = respCode;
	}
	
	@Length(min=0, max=1024, message="响应信息长度必须介于 0 和 1024 之间")
	public String getRespMsg() {
		return respMsg;
	}

	public void setRespMsg(String respMsg) {
		this.respMsg = respMsg;
	}
	
	@Length(min=0, max=127, message="客户端IP长度必须介于 0 和 127 之间")
	public String getRemoteAddr() {
		return remoteAddr;
	}

	public void setRemoteAddr(String remoteAddr) {
		this.remoteAddr = remoteAddr;
	}



	public String getRequestUrl() {
		return requestUrl;
	}

	public void setRequestUrl(String requestUrl) {
		this.requestUrl = requestUrl;
	}

	public String getNotifyUrl() {
		return notifyUrl;
	}

	public void setNotifyUrl(String notifyUrl) {
		this.notifyUrl = notifyUrl;
	}






	
}