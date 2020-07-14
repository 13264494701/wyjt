package com.jxf.ufang.entity;

import java.math.BigDecimal;
import java.util.Date;
import com.alibaba.fastjson.annotation.JSONField;
import javax.validation.constraints.NotNull;

import com.jxf.svc.sys.crud.entity.CrudEntity;

/**
 * 借条统计Entity
 * @author suHuimin
 * @version 2019-03-25
 */
public class ReportUfangLoanDaily extends CrudEntity<ReportUfangLoanDaily> {
	
	private static final long serialVersionUID = 1L;
	
	/** 机构 */
	private UfangBrn ufangBrn;	
			/** 日期 */
	private Date date;		
			/** 结清借条数量 */
	private Long completedLoanQuantity;		
			/** 收到的还款金额 */
	private BigDecimal repayedLoanAmount;		
			/** 借出借条数量 */
	private Long createdLoanQuantity;		
			/** 借出借条金额 */
	private BigDecimal createdLoanAmount;		
			/** 逾期未还借条数量 */
	private Long overdueLoanQuantity;		
			/** 逾期未还借条金额 */
	private BigDecimal overdueLoanAmount;		
			/** 放款中借条数量 */
	private Long lendingLoanQuantity;		
			/** 放款中借条金额 */
	private BigDecimal lendingLoanAmount;		
	/** 查询申请起始时间 */
	private Date beginTime;		
	/** 查询申请结束时间 */
	private Date endTime;
	
	public ReportUfangLoanDaily() {
		super();
	}

	public ReportUfangLoanDaily(Long id){
		super(id);
	}

	
	public UfangBrn getUfangBrn() {
		return ufangBrn;
	}

	public void setUfangBrn(UfangBrn ufangBrn) {
		this.ufangBrn = ufangBrn;
	}

	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	@NotNull(message="日期不能为空")
	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
	
	public Long getCompletedLoanQuantity() {
		return completedLoanQuantity;
	}

	public void setCompletedLoanQuantity(Long completedLoanQuantity) {
		this.completedLoanQuantity = completedLoanQuantity;
	}
	
	
	public Long getCreatedLoanQuantity() {
		return createdLoanQuantity;
	}

	public void setCreatedLoanQuantity(Long createdLoanQuantity) {
		this.createdLoanQuantity = createdLoanQuantity;
	}
	
	
	public Long getOverdueLoanQuantity() {
		return overdueLoanQuantity;
	}

	public void setOverdueLoanQuantity(Long overdueLoanQuantity) {
		this.overdueLoanQuantity = overdueLoanQuantity;
	}
	
	
	public Long getLendingLoanQuantity() {
		return lendingLoanQuantity;
	}

	public void setLendingLoanQuantity(Long lendingLoanQuantity) {
		this.lendingLoanQuantity = lendingLoanQuantity;
	}

	public BigDecimal getRepayedLoanAmount() {
		return repayedLoanAmount;
	}

	public void setRepayedLoanAmount(BigDecimal repayedLoanAmount) {
		this.repayedLoanAmount = repayedLoanAmount;
	}

	public BigDecimal getCreatedLoanAmount() {
		return createdLoanAmount;
	}

	public void setCreatedLoanAmount(BigDecimal createdLoanAmount) {
		this.createdLoanAmount = createdLoanAmount;
	}

	public BigDecimal getOverdueLoanAmount() {
		return overdueLoanAmount;
	}

	public void setOverdueLoanAmount(BigDecimal overdueLoanAmount) {
		this.overdueLoanAmount = overdueLoanAmount;
	}

	public BigDecimal getLendingLoanAmount() {
		return lendingLoanAmount;
	}

	public void setLendingLoanAmount(BigDecimal lendingLoanAmount) {
		this.lendingLoanAmount = lendingLoanAmount;
	}

	public Date getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(Date beginTime) {
		this.beginTime = beginTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
}