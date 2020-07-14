package com.jxf.report.entity;

import java.math.BigDecimal;
import com.alibaba.fastjson.annotation.JSONField;
import javax.validation.constraints.NotNull;

import com.jxf.svc.sys.crud.entity.CrudEntity;

/**
 * 充值统计Entity
 * @author wo
 * @version 2019-03-25
 */
public class ReportRchgDaily extends CrudEntity<ReportRchgDaily> {
	
	private static final long serialVersionUID = 1L;
	/** 日期 */
	private String date;		
	/** 线上笔数 */
	private Long onlineQuantity;		
	/** 线上金额 */
	private BigDecimal onlineAmount;		
	/** 线下笔数 */
	private Long offlineQuantity;		
    /** 线下金额 */
	private BigDecimal offlineAmount;		
	
	/** 报表类型 */
	private Integer type;
	
	public ReportRchgDaily() {
		super();
	}

	public ReportRchgDaily(Long id){
		super(id);
	}

	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	@NotNull(message="日期不能为空")
	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}
	
	@NotNull(message="线上笔数不能为空")
	public Long getOnlineQuantity() {
		return onlineQuantity;
	}

	public void setOnlineQuantity(Long onlineQuantity) {
		this.onlineQuantity = onlineQuantity;
	}
	
	public BigDecimal getOnlineAmount() {
		return onlineAmount;
	}

	public void setOnlineAmount(BigDecimal onlineAmount) {
		this.onlineAmount = onlineAmount;
	}
	
	@NotNull(message="线下笔数不能为空")
	public Long getOfflineQuantity() {
		return offlineQuantity;
	}

	public void setOfflineQuantity(Long offlineQuantity) {
		this.offlineQuantity = offlineQuantity;
	}
	
	public BigDecimal getOfflineAmount() {
		return offlineAmount;
	}

	public void setOfflineAmount(BigDecimal offlineAmount) {
		this.offlineAmount = offlineAmount;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}
	
}