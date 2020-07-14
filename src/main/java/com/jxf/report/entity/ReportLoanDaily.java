package com.jxf.report.entity;

import java.math.BigDecimal;
import com.alibaba.fastjson.annotation.JSONField;
import javax.validation.constraints.NotNull;

import com.jxf.svc.sys.crud.entity.CrudEntity;

/**
 * 借条统计Entity
 * @author wo
 * @version 2019-02-28
 */
public class ReportLoanDaily extends CrudEntity<ReportLoanDaily> {
	
	private static final long serialVersionUID = 1L;

	/**
	 * 交易类型
	 */
	public enum TrxType {

		/** 线上交易 */
		online,

		/** 线下交易  */
		offline
		
	}
	
	/** 日期 */
	private String date;
	
	/** 交易类型*/
	private TrxType trxType;
	/** 借条数量 */
	private Long loanQuantity;		
	/** 借条金额 */
	private BigDecimal loanAmount;		
	/** 借条手续费 */
	private BigDecimal loanFee;	
	
	/** 报表类型 */
	private Integer type;
	
	public ReportLoanDaily() {
		super();
	}

	public ReportLoanDaily(Long id){
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
	
	@NotNull(message="借条数量不能为空")
	public Long getLoanQuantity() {
		return loanQuantity;
	}

	public void setLoanQuantity(Long loanQuantity) {
		this.loanQuantity = loanQuantity;
	}
	
	public BigDecimal getLoanAmount() {
		return loanAmount;
	}

	public void setLoanAmount(BigDecimal loanAmount) {
		this.loanAmount = loanAmount;
	}
	
	public BigDecimal getLoanFee() {
		return loanFee;
	}

	public void setLoanFee(BigDecimal loanFee) {
		this.loanFee = loanFee;
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
	
}