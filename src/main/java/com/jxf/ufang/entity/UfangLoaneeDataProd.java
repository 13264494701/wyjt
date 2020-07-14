package com.jxf.ufang.entity;

import org.hibernate.validator.constraints.Length;

import com.jxf.svc.sys.crud.entity.CrudEntity;

/**
 * 借款人数据产品表Entity
 * @author gaobo
 * @version 2019-07-19
 */
public class UfangLoaneeDataProd extends CrudEntity<UfangLoaneeDataProd> {
	
	private static final long serialVersionUID = 1L;
	/** 产品编码 */
	private String code;		
	/** 产品名称 */
	private String name;		
	/** 申请渠道 */
	private String channel;		
	/** 最小年龄 */
	private String minAge;		
	/** 最大年龄 */
	private String maxAge;		
	/** 芝麻分 */
	private String minZmf;		
	/** 最大芝麻分 */
	private String maxZmf;		
	/** 运营商认证 */
	private Boolean isYys;		
	/** 售价 */
	private String price;		
	/** 最大销量 */
	private String maxSales;		
	/** 折扣 */
	private String discount;		
	/** 折扣开始天数 */
	private String discountBeginDays;		
	/** 是否启用 */
	private Boolean isOn;		
	
	public UfangLoaneeDataProd() {
		super();
	}

	public UfangLoaneeDataProd(Long id){
		super(id);
	}

	@Length(min=1, max=10, message="产品编码长度必须介于 1 和 10 之间")
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	
	@Length(min=1, max=64, message="产品名称长度必须介于 1 和 64 之间")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@Length(min=1, max=4, message="申请渠道长度必须介于 1 和 4 之间")
	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}
	
	@Length(min=1, max=4, message="最小年龄长度必须介于 1 和 4 之间")
	public String getMinAge() {
		return minAge;
	}

	public void setMinAge(String minAge) {
		this.minAge = minAge;
	}
	
	@Length(min=1, max=4, message="最大年龄长度必须介于 1 和 4 之间")
	public String getMaxAge() {
		return maxAge;
	}

	public void setMaxAge(String maxAge) {
		this.maxAge = maxAge;
	}
	
	@Length(min=1, max=11, message="芝麻分长度必须介于 1 和 11 之间")
	public String getMinZmf() {
		return minZmf;
	}

	public void setMinZmf(String minZmf) {
		this.minZmf = minZmf;
	}
	
	@Length(min=1, max=11, message="最大芝麻分长度必须介于 1 和 11 之间")
	public String getMaxZmf() {
		return maxZmf;
	}

	public void setMaxZmf(String maxZmf) {
		this.maxZmf = maxZmf;
	}
	
	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}
	
	@Length(min=1, max=11, message="最大销量长度必须介于 1 和 11 之间")
	public String getMaxSales() {
		return maxSales;
	}

	public void setMaxSales(String maxSales) {
		this.maxSales = maxSales;
	}
	
	public String getDiscount() {
		return discount;
	}

	public void setDiscount(String discount) {
		this.discount = discount;
	}
	
	@Length(min=1, max=4, message="折扣开始天数长度必须介于 1 和 4 之间")
	public String getDiscountBeginDays() {
		return discountBeginDays;
	}

	public void setDiscountBeginDays(String discountBeginDays) {
		this.discountBeginDays = discountBeginDays;
	}

	public Boolean getIsYys() {
		return isYys;
	}

	public void setIsYys(Boolean isYys) {
		this.isYys = isYys;
	}

	public Boolean getIsOn() {
		return isOn;
	}

	public void setIsOn(Boolean isOn) {
		this.isOn = isOn;
	}
}