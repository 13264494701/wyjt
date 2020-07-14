package com.jxf.ufang.entity;

import javax.validation.constraints.NotNull;
import com.jxf.svc.sys.crud.entity.CrudEntity;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 查询订单Entity
 * @author wo
 * @version 2018-11-24
 */
public class UfangLoaneeRcOrder extends CrudEntity<UfangLoaneeRcOrder> {
	
	private static final long serialVersionUID = 1L;
	/**
	 * 类型
	 */
	public enum Type {
		
		/** 无忧借条 */
		wyjt,

		/** 今借到 */
		jjd,
		
	}

	/**
	 * 状态
	 */
	public enum Status {

		/** 待付款 */
		pendingPayment,

		/** 待生成   */
		pendingCreate,
		
		/** 生成成功   */
		success,

		/** 生成失败 */
		notfound
	}
	
	/**
	 * 支付状态
	 */
	public enum PayStatus {

		/** 待付款 */
		pendingPayment,
		
		/** 已付款   */
		payed,

		/** 已退款 */
		refunded
	}
	
	/** 优放用户 */
	private UfangUser user;		
	/** 订单类型 */
	private Type type;	
	
	/** 姓名 */
	private String qName;		
	/** 手机号码 */
	private String qPhoneNo;		
	/** 身份证号 */
	private String qIdNo;	
	
	/** 风控数据 */
	private String data;
	
	/** 订单价格 */
	private BigDecimal price;	
	
	/** 订单状态 */
	private Status status;	
	/** 支付状态 */
	private PayStatus payStatus;
	
	/** 支付流水 */
	private Long payId;	
	
	
	/** 查询起始时间 */
	private Date beginTime;		
	/** 查询结束时间 */
	private Date endTime;
	
	public UfangLoaneeRcOrder() {
		super();
	}

	public UfangLoaneeRcOrder(Long id){
		super(id);
	}

	public UfangUser getUser() {
		return user;
	}

	public void setUser(UfangUser user) {
		this.user = user;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public String getqName() {
		return qName;
	}

	public void setqName(String qName) {
		this.qName = qName;
	}

	public String getqPhoneNo() {
		return qPhoneNo;
	}

	public void setqPhoneNo(String qPhoneNo) {
		this.qPhoneNo = qPhoneNo;
	}

	public String getqIdNo() {
		return qIdNo;
	}

	public void setqIdNo(String qIdNo) {
		this.qIdNo = qIdNo;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public PayStatus getPayStatus() {
		return payStatus;
	}

	public void setPayStatus(PayStatus payStatus) {
		this.payStatus = payStatus;
	}

	
	@NotNull(message="支付流水不能为空")
	public Long getPayId() {
		return payId;
	}

	public void setPayId(Long payId) {
		this.payId = payId;
	}

	public Date getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(Date beginTime) {
		this.beginTime = beginTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	
}