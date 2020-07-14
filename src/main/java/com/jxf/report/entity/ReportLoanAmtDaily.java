package com.jxf.report.entity;

import java.math.BigDecimal;
import com.alibaba.fastjson.annotation.JSONField;
import javax.validation.constraints.NotNull;

import com.jxf.svc.sys.crud.entity.CrudEntity;

/**
 * 借条金额统计Entity
 * @author wo
 * @version 2019-07-10
 */
public class ReportLoanAmtDaily extends CrudEntity<ReportLoanAmtDaily> {
	
	private static final long serialVersionUID = 1L;
	
	/** 日期 */
	private String date;
	
	/** 借款笔数*/
	private Long loanQuantity;	
	/** 放款人借出 */
	private BigDecimal loanerLend;	
	/** 借款人借入 */
	private BigDecimal loaneeBorrow;	
	
	/** 还款笔数*/
	private Long repayQuantity;	
	/** 借款人还款 */
	private BigDecimal loaneeRepay;	
	/** 放款人收款 */
	private BigDecimal loanerReceive;	
	
	/** 报表类型 */
	private Integer type;
	
	public ReportLoanAmtDaily() {
		super();
	}

	public ReportLoanAmtDaily(Long id){
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


	public BigDecimal getLoanerLend() {
		return loanerLend;
	}

	public void setLoanerLend(BigDecimal loanerLend) {
		this.loanerLend = loanerLend;
	}

	public BigDecimal getLoaneeBorrow() {
		return loaneeBorrow;
	}

	public void setLoaneeBorrow(BigDecimal loaneeBorrow) {
		this.loaneeBorrow = loaneeBorrow;
	}

	public Long getRepayQuantity() {
		return repayQuantity;
	}

	public void setRepayQuantity(Long repayQuantity) {
		this.repayQuantity = repayQuantity;
	}

	public BigDecimal getLoaneeRepay() {
		return loaneeRepay;
	}

	public void setLoaneeRepay(BigDecimal loaneeRepay) {
		this.loaneeRepay = loaneeRepay;
	}

	public BigDecimal getLoanerReceive() {
		return loanerReceive;
	}

	public void setLoanerReceive(BigDecimal loanerReceive) {
		this.loanerReceive = loanerReceive;
	}
	
	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	
}