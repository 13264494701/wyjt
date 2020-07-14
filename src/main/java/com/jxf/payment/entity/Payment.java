package com.jxf.payment.entity;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.Date;

import com.jxf.svc.sys.crud.entity.CrudEntity;

/**
 * 支付记录Entity
 * @author HUOJIABAO
 * @version 2016-06-08
 */
public class Payment extends CrudEntity<Payment> {
	
	private static final long serialVersionUID = 1L;
	/**
	 * 类型
	 */
	public enum Type {
		
		/** 预存款充值 */
		recharge,

		/** 借条达成服务费 */
		loanDone,

		/** 借条延期服务费 */
		loanDelay,
		
		/** 仲裁 */
		arbitration,

		/** 强执 */
		execution,
	}

	/**
	 * 状态
	 */
	public enum Status {

		/** 等待支付 */
		wait,

		/** 支付成功 */
		success,

		/** 支付失败 */
		failure
	}
	public enum Channel {

		/** 无忧借条 */
		wyjt,

		/** 优放 */
		ufang,

		/** 平台管理员 */
		admin,
		/** 公信堂*/
		gxt,
	}
	/**渠道类型*/
	private Channel channel;
	/**会员*/
	private Long principalId;
	/**原业务ID*/
	private Long orgId;
	/**支付流水号、上送支付平台*/
	private String paymentNo;
	/**支付类型*/
	private Type type;	
	/**支付金额*/
	private BigDecimal paymentAmount;	
	/**支付手续费*/
	private BigDecimal paymentFee;	
	/**支付状态*/
	private Status status;	
	
	/**签约协议号*/
	private String protocolNo;	
	/**支付插件ID*/
	private String paymentPluginId;		
	/**支付插件名称*/
	private String paymentPluginName;
	
	/**商户号*/
	private String mchId;
	/**商户秘钥*/
	private String key;
	
	/**支付平台返回流水号*/
	private String payId;	
	/**响应编码*/
	private String respCode;		
	/**响应信息*/
	private String respMsg;		
	/**响应时间*/
	private Date respTime;	
	/**客户端IP*/
	private String remoteAddr;	
	/**版本号*/
	private Integer version;	
	/**更新时候旧版本参数*/
	private Integer oldVersion;  
	
	/**用户微信公众平台ID*/
	private String openID; 
	/**支付请求地址*/
	private String requestUrl;  
	/**支付回调地址*/
    private String notifyUrl; 
    /**第三方支付单号*/
	private String thirdPaymentNo;
	
	public Payment() {
		super();
	}

	public Payment(Long id){
		super(id);
	}

	public Channel getChannel() {
		return channel;
	}

	public void setChannel(Channel channel) {
		this.channel = channel;
	}

	public Long getPrincipalId() {
		return principalId;
	}

	public void setPrincipalId(Long principalId) {
		this.principalId = principalId;
	}

	public Long getOrgId() {
		return orgId;
	}

	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}
	@Length(min=1, max=16, message="支付编号长度必须介于 1 和 16 之间")
	public String getPaymentNo() {
		return paymentNo;
	}

	public void setPaymentNo(String paymentNo) {
		this.paymentNo = paymentNo;
	}
	
	@NotNull(message="支付金额不能为空")
	public BigDecimal getPaymentAmount() {
		return paymentAmount;
	}

	public void setPaymentAmount(BigDecimal paymentAmount) {
		this.paymentAmount = paymentAmount;
	}
	
	@NotNull(message="支付手续费不能为空")
	public BigDecimal getPaymentFee() {
		return paymentFee;
	}

	public void setPaymentFee(BigDecimal paymentFee) {
		this.paymentFee = paymentFee;
	}
	
	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}
	@Length(min=0, max=127, message="协议号长度必须介于 0 和 127 之间")
	public String getProtocolNo() {
		return protocolNo;
	}

	public void setProtocolNo(String protocolNo) {
		this.protocolNo = protocolNo;
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


	public String getMchId() {
		return mchId;
	}

	public void setMchId(String mchId) {
		this.mchId = mchId;
	}
	public String getPayId() {
		return payId;
	}

	public void setPayId(String payId) {
		this.payId = payId;
	}
	public String getRespCode() {
		return respCode;
	}

	public void setRespCode(String respCode) {
		this.respCode = respCode;
	}

	public String getRespMsg() {
		return respMsg;
	}

	public void setRespMsg(String respMsg) {
		this.respMsg = respMsg;
	}

	public Date getRespTime() {
		return respTime;
	}

	public void setRespTime(Date respTime) {
		this.respTime = respTime;
	}


	public String getRemoteAddr() {
		return remoteAddr;
	}

	public void setRemoteAddr(String remoteAddr) {
		this.remoteAddr = remoteAddr;
	}
	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public Integer getOldVersion() {
		return oldVersion;
	}

	public void setOldVersion(Integer oldVersion) {
		this.oldVersion = oldVersion;
	}
	public String getOpenID() {
		return openID;
	}

	public void setOpenID(String openID) {
		this.openID = openID;
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

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getThirdPaymentNo() {
		return thirdPaymentNo;
	}

	public void setThirdPaymentNo(String thirdPaymentNo) {
		this.thirdPaymentNo = thirdPaymentNo;
	}


}