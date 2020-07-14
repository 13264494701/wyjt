package com.jxf.payment.entity;

import org.hibernate.validator.constraints.Length;
import com.jxf.svc.sys.crud.entity.CrudEntity;

/**
 * 支付方式Entity
 * @author JINXINFU
 * @version 2016-10-21
 */
public class PaymentMethod extends CrudEntity<PaymentMethod> {
	
	private static final long serialVersionUID = 1L;
	/**
	 * 类型
	 */
	public enum Type {

		/** 款到发货 */
		deliveryAgainstPayment,

		/** 货到付款 */
		cashOnDelivery
	}

	/**
	 * 方式
	 */
	public enum Method {

		/** 在线支付 */
		online,

		/** 线下支付 */
		offline
	}
	
	private String name;		// 支付名称
	
	/**支付类型*/
	private PaymentMethod.Type type;	
	/**支付方式 */
	private PaymentMethod.Method method;
	
	private Integer timeout;		// 支付超时
	private String icon;		// 图标
	private String description;		// 描述
	private String content;		// 内容
	private Long displayOrder;		// 展示排序
	
	public PaymentMethod() {
		super();
	}

	public PaymentMethod(Long id){
		super(id);
	}

	@Length(min=1, max=64, message="支付名称长度必须介于 1 和 64 之间")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public PaymentMethod.Type getType() {
		return type;
	}

	public void setType(PaymentMethod.Type type) {
		this.type = type;
	}
	
	public PaymentMethod.Method getMethod() {
		return method;
	}

	public void setMethod(PaymentMethod.Method method) {
		this.method = method;
	}
	
	public Integer getTimeout() {
		return timeout;
	}

	public void setTimeout(Integer timeout) {
		this.timeout = timeout;
	}
	
	@Length(min=0, max=255, message="图标长度必须介于 0 和 255 之间")
	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}
	
	@Length(min=0, max=255, message="描述长度必须介于 0 和 255 之间")
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
	public Long getDisplayOrder() {
		return displayOrder;
	}

	public void setDisplayOrder(Long displayOrder) {
		this.displayOrder = displayOrder;
	}
	
}