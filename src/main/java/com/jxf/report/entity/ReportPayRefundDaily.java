package com.jxf.report.entity;

import java.math.BigDecimal;
import com.alibaba.fastjson.annotation.JSONField;
import javax.validation.constraints.NotNull;

import com.jxf.svc.sys.crud.entity.CrudEntity;

/**
 * 缴费退费统计Entity
 * @author wo
 * @version 2019-02-28
 */
public class ReportPayRefundDaily extends CrudEntity<ReportPayRefundDaily> {
	
	private static final long serialVersionUID = 1L;

	/**
	 * 交易类型
	 */
	public enum TrxType {

		/** 缴费 */
		pay,

		/** 退费  */
		refund
		
	}
	
	/** 日期 */
	private String date;
	
	/** 交易类型*/
	private TrxType trxType;
	/** 总金额 */
	private BigDecimal totalAmount;		
	/** 催收金额 */
	private BigDecimal clAmount;	
	/** 仲裁金额 */
	private BigDecimal arAmount;	
	/** 强执金额 */
	private BigDecimal exAmount;	
	
	/** 报表类型 */
	private Integer type;
	
	public ReportPayRefundDaily() {
		super();
	}

	public ReportPayRefundDaily(Long id){
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

	public TrxType getTrxType() {
		return trxType;
	}

	public void setTrxType(TrxType trxType) {
		this.trxType = trxType;
	}

	public BigDecimal getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}

	public BigDecimal getClAmount() {
		return clAmount;
	}

	public void setClAmount(BigDecimal clAmount) {
		this.clAmount = clAmount;
	}

	public BigDecimal getArAmount() {
		return arAmount;
	}

	public void setArAmount(BigDecimal arAmount) {
		this.arAmount = arAmount;
	}

	public BigDecimal getExAmount() {
		return exAmount;
	}

	public void setExAmount(BigDecimal exAmount) {
		this.exAmount = exAmount;
	}
	
	
}