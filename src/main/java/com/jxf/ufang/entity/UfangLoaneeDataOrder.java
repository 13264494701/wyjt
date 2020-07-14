package com.jxf.ufang.entity;

import javax.validation.constraints.NotNull;

import com.jxf.svc.sys.crud.entity.CrudEntity;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 流量订单Entity
 * @author wo
 * @version 2018-11-24
 */
public class UfangLoaneeDataOrder extends CrudEntity<UfangLoaneeDataOrder> {
	
	private static final long serialVersionUID = 1L;
	/**
	 * 订单状态
	 */
	public enum Status {

	    /** 已支付*/
		success,
		
		/** 已过期 */
		expired
	}
	
	/**
	 * 流量分组
	 */
	public enum Group {

	    /** 待跟进*/
		pendingFollow,
		
	    /** 跟进中*/
		followup,
		
		/** 后续跟进*/
		laterFollow,
		
		/** 已放弃 */
		gaveup
	}
	
	/** 优放用户 */
	private UfangUser user;		
	/** 流量数据 */
	private UfangLoaneeData data;		
	/** 付款金额 */
	private BigDecimal amount;
	/** 流量分组 */
	private Group dataGroup;
	/** 订单状态 */
	private Status status;		
	/** 支付流水 */
	private Long payId;	
	
	private String prodCode;
	
	private String minAge;		// 开始 年龄
	private String maxAge;		// 结束 年龄
	private String minZmf;		// 开始 芝麻分
	private String maxZmf;		// 结束 芝麻分
	
	/** 查询起始时间 */
	private Date beginTime;		
	/** 查询结束时间 */
	private Date endTime;
	
	public UfangLoaneeDataOrder() {
		super();
	}

	public UfangLoaneeDataOrder(Long id){
		super(id);
	}

	public UfangUser getUser() {
		return user;
	}

	public void setUser(UfangUser user) {
		this.user = user;
	}
	
	public UfangLoaneeData getData() {
		return data;
	}

	public void setData(UfangLoaneeData data) {
		this.data = data;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public Group getDataGroup() {
		return dataGroup;
	}

	public void setDataGroup(Group dataGroup) {
		this.dataGroup = dataGroup;
	}
	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
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

	public String getProdCode() {
		return prodCode;
	}

	public void setProdCode(String prodCode) {
		this.prodCode = prodCode;
	}

	public String getMinAge() {
		return minAge;
	}

	public void setMinAge(String minAge) {
		this.minAge = minAge;
	}

	public String getMaxAge() {
		return maxAge;
	}

	public void setMaxAge(String maxAge) {
		this.maxAge = maxAge;
	}

	public String getMinZmf() {
		return minZmf;
	}

	public void setMinZmf(String minZmf) {
		this.minZmf = minZmf;
	}

	public String getMaxZmf() {
		return maxZmf;
	}

	public void setMaxZmf(String maxZmf) {
		this.maxZmf = maxZmf;
	}





	
}