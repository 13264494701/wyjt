package com.jxf.loan.entity;

import java.math.BigDecimal;
import java.util.Date;

import org.hibernate.validator.constraints.Length;

import com.jxf.svc.annotation.ExcelField;
import com.jxf.svc.sys.crud.entity.CrudEntity;
import com.jxf.svc.utils.DateUtils;

/**
 * 	借条催收Entity
 * @author gaobo
 * @version 2018-11-07
 */
public class NfsLoanCollection extends CrudEntity<NfsLoanCollection> {
	
	private static final long serialVersionUID = 1L;
	/**
	 * 	催收状态
	 */
	public enum Status {
		
		/** 审核中 */
		auditing,
		
		/** 已受理 */
		accepted,
		
		/** 拒绝受理 */
		refuse,
		
		/**  催收中  */
		collection,
		
		/** 催收成功 */
		success,
		
		/** 催收失败 */
		fail,
	}
	
	/** 借条编号 */
	private NfsLoanRecord loan;		
	/** 催收费用 */
	private BigDecimal fee;		
	/** 退费金额 */
	private BigDecimal refund;		
	/** 拒绝受理原因 */
	private String refuseReason;		
	/** 催收状态 */
	private Status status;		
	/** 催收申请起始时间 */
	private Date beginTime;
	/** 催收申请结束时间 */
	private Date endTime;
	
	

	public NfsLoanCollection() {
		super();
	}

	public NfsLoanCollection(Long id){
		super(id);
	}
	
	public NfsLoanRecord getLoan() {
		return loan;
	}

	public void setLoan(NfsLoanRecord loan) {
		this.loan = loan;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public BigDecimal getFee() {
		return fee;
	}

	public void setFee(BigDecimal fee) {
		this.fee = fee;
	}
	
	public BigDecimal getRefund() {
		return refund;
	}

	public void setRefund(BigDecimal refund) {
		this.refund = refund;
	}
	
	@Length(min=0, max=255, message="拒绝受理原因长度必须介于 0 和 255 之间")
	public String getRefuseReason() {
		return refuseReason;
	}

	public void setRefuseReason(String refuseReason) {
		this.refuseReason = refuseReason;
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

	@ExcelField(title="借款编号", align=2, sort=10)
	public String getLoanId() {
		return getLoan().getId().toString();
	}
	
	@ExcelField(title="放款人姓名", align=2, sort=20)
	public String getLoanerName() {
		return getLoan().getLoaner().getName();
	}
	
	@ExcelField(title="放款人手机号码", align=2, sort=30)
	public String getLoanerPhoneNo() {
		return getLoan().getLoaner().getUsername();
	}
	
	@ExcelField(title="借款人姓名", align=2, sort=40)
	public String getLoaneeName() {
		return getLoan().getLoanee().getName();
	}
	
	@ExcelField(title="借款人手机号码", align=2, sort=50)
	public String getLoaneePhoneNo() {
		return getLoan().getLoanee().getUsername();
	}
	
	@ExcelField(title="借款人身份证号", align=2, sort=60)
	public String getLoaneeIdNo() {
		return getLoan().getLoanee().getIdNo();
	}
	
	@ExcelField(title="借款金额", align=2, sort=70)
	public BigDecimal getAmount() {
		return getLoan().getAmount();
	}
	
	@ExcelField(title="借款利息", align=2, sort=80)
	public BigDecimal getInterest() {
		return getLoan().getInterest();
	}
	
	@ExcelField(title="到期还款时间", align=2, sort=90)
	public String getDueRepayDate() {
		return DateUtils.getDateStr(getLoan().getDueRepayDate(), "yyyy-MM-dd HH:mm:ss");
	}
	
	@ExcelField(title="逾期天数", align=2, sort=100)
	public String getOverdueDays() {
		return DateUtils.getDifferenceOfTwoDate(new Date(),getLoan().getDueRepayDate())+"";
	}
	
	@ExcelField(title="申请催收时间", align=2, sort=110)
	public String getApplyTime() {
		return DateUtils.getDateStr(getCreateTime(), "yyyy-MM-dd HH:mm:ss");
	}
	
	@ExcelField(title="催收天数", align=2, sort=120)
	public String getApplyDays() {
		return DateUtils.getDifferenceOfTwoDate(new Date(),getCreateTime())+"";
	}
}