package com.jxf.nfs.entity;

import org.hibernate.validator.constraints.Length;
import javax.validation.constraints.NotNull;
import com.jxf.svc.sys.crud.entity.CrudEntity;

/**
 * 银行编码Entity
 * @author wo
 * @version 2018-09-29
 */
public class NfsBankInfo extends CrudEntity<NfsBankInfo> {
	
	private static final long serialVersionUID = 1L;
	/** 银行名称 */
	private String name;	
	/** 简称 */
	private String abbrName;
	/** 图标 */
	private String logo;		
	/** 每笔限额 */
	private Integer limitPerTrx;		
	/** 每天限额 */
	private Integer limitPerDay;		
	/** 每月限额 */
	private Integer limitPerMonth;		
	/** 客服热线 */
	private String hotline;		
	/** 是否支持 */
	private Boolean isSupport;

	public NfsBankInfo() {
		super();
	}

	public NfsBankInfo(Long id){
		super(id);
	}

	@Length(min=1, max=64, message="银行名称长度必须介于 1 和 64 之间")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getAbbrName() {
		return abbrName;
	}

	public void setAbbrName(String abbrName) {
		this.abbrName = abbrName;
	}
	
	
	@Length(min=1, max=255, message="图标长度必须介于 1 和 255 之间")
	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}
	
	public Integer getLimitPerTrx() {
		return limitPerTrx;
	}

	public void setLimitPerTrx(Integer limitPerTrx) {
		this.limitPerTrx = limitPerTrx;
	}
	
	public Integer getLimitPerDay() {
		return limitPerDay;
	}

	public void setLimitPerDay(Integer limitPerDay) {
		this.limitPerDay = limitPerDay;
	}
	
	public Integer getLimitPerMonth() {
		return limitPerMonth;
	}

	public void setLimitPerMonth(Integer limitPerMonth) {
		this.limitPerMonth = limitPerMonth;
	}
	
	@Length(min=0, max=32, message="客服热线长度必须介于 0 和 32 之间")
	public String getHotline() {
		return hotline;
	}

	public void setHotline(String hotline) {
		this.hotline = hotline;
	}
	
	@NotNull(message="是否支持不能为空")
	public Boolean getIsSupport() {
		return isSupport;
	}

	public void setIsSupport(Boolean isSupport) {
		this.isSupport = isSupport;
	}




}