package com.jxf.report.entity;

import com.alibaba.fastjson.annotation.JSONField;

import java.math.BigDecimal;

import javax.validation.constraints.NotNull;

import com.jxf.svc.sys.crud.entity.CrudEntity;

/**
 * 账户统计Entity
 * @author wo
 * @version 2019-02-22
 */
public class ReportMemberActDaily extends CrudEntity<ReportMemberActDaily> {
	
	private static final long serialVersionUID = 1L;
	/** 日期 */
	private String date;	
	/** 可用余额 */
	private BigDecimal avlBal;		
	/** 借款账户 */
	private BigDecimal loanBal;		
	/** 冻结账户 */
	private BigDecimal freezenBal;		
	/** 红包账户 */
	private BigDecimal redbagBal;		
	/** 待还账户 */
	private BigDecimal pendingRepayment;		
	/** 待收账户 */
	private BigDecimal pendingReceiver;		
	
	/** 报表类型 */
	private Integer type;
	
	public ReportMemberActDaily() {
		super();
	}

	public ReportMemberActDaily(Long id){
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
	
	public BigDecimal getAvlBal() {
		return avlBal;
	}

	public void setAvlBal(BigDecimal avlBal) {
		this.avlBal = avlBal;
	}
	
	public BigDecimal getLoanBal() {
		return loanBal;
	}

	public void setLoanBal(BigDecimal loanBal) {
		this.loanBal = loanBal;
	}
	
	public BigDecimal getFreezenBal() {
		return freezenBal;
	}

	public void setFreezenBal(BigDecimal freezenBal) {
		this.freezenBal = freezenBal;
	}
	
	public BigDecimal getRedbagBal() {
		return redbagBal;
	}

	public void setRedbagBal(BigDecimal redbagBal) {
		this.redbagBal = redbagBal;
	}
	
	public BigDecimal getPendingRepayment() {
		return pendingRepayment;
	}

	public void setPendingRepayment(BigDecimal pendingRepayment) {
		this.pendingRepayment = pendingRepayment;
	}
	
	public BigDecimal getPendingReceiver() {
		return pendingReceiver;
	}

	public void setPendingReceiver(BigDecimal pendingReceiver) {
		this.pendingReceiver = pendingReceiver;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}
	
}