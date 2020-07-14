package com.jxf.api.entity;

import org.hibernate.validator.constraints.Length;
import java.util.Date;
import com.alibaba.fastjson.annotation.JSONField;

import com.jxf.svc.sys.crud.entity.CrudEntity;

/**
 * 给第三方查询流量Entity
 * @author XIAORONGDIAN
 * @version 2019-04-17
 */
public class ApiLoaneeData extends CrudEntity<ApiLoaneeData> {
	
	private static final long serialVersionUID = 1L;
			/** 商户号 */
	private String merchantNumber;		
			/** 商户订单号：由商户自定义传入的唯一且不大于32位的字符串， 生成的订单号不能带有:、_#_等特殊字符，建议使用时间戳或数字、字母、下划线等组合 */
	private String orderId;		
			/** 本次查询数据数量 */
	private Integer count;		
			/** 签名 */
	private String sign;		
			/** 第三方ip */
	private String ip;		
			/** 数据区间-起始时间 */
	private Date startTime;		
			/** 数据区间-结束时间 */
	private Date endTime;		
	
	public ApiLoaneeData() {
		super();
	}

	public ApiLoaneeData(Long id){
		super(id);
	}

	@Length(min=0, max=255, message="商户号长度必须介于 0 和 255 之间")
	public String getMerchantNumber() {
		return merchantNumber;
	}

	public void setMerchantNumber(String merchantNumber) {
		this.merchantNumber = merchantNumber;
	}
	
	@Length(min=0, max=255, message="商户订单号：由商户自定义传入的唯一且不大于32位的字符串， 生成的订单号不能带有:、_#_等特殊字符，建议使用时间戳或数字、字母、下划线等组合长度必须介于 0 和 255 之间")
	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	
	@Length(min=1, max=255, message="签名长度必须介于 1 和 255 之间")
	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}
	
	@Length(min=0, max=255, message="第三方ip长度必须介于 0 和 255 之间")
	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}
	
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}
	
}