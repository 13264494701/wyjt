package com.jxf.report.entity;

import java.math.BigDecimal;
import com.alibaba.fastjson.annotation.JSONField;
import javax.validation.constraints.NotNull;

import com.jxf.svc.sys.crud.entity.CrudEntity;

/**
 * 转账统计Entity
 * @author wo
 * @version 2019-03-25
 */
public class ReportTransferDaily extends CrudEntity<ReportTransferDaily> {
	
	private static final long serialVersionUID = 1L;
	/** 日期 */
	private String date;		
	/** 转账笔数 */
	private Long quantity;		
	/** 转账金额 */
	private BigDecimal amount;		
	
	
	/** 报表类型 */
	private Integer type;
	
	public ReportTransferDaily() {
		super();
	}

	public ReportTransferDaily(Long id){
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

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Long getQuantity() {
		return quantity;
	}

	public void setQuantity(Long quantity) {
		this.quantity = quantity;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	
}