package com.jxf.loan.entity;


import java.math.BigDecimal;
import java.util.Date;

import com.jxf.loan.entity.NfsLoanApply.LoanRole;
import com.jxf.mem.entity.Member;
import com.jxf.svc.annotation.ExcelField;
import com.jxf.svc.sys.crud.entity.CrudEntity;
import com.jxf.svc.utils.DateUtils;
import com.jxf.svc.utils.StringUtils;

/**
 * 部分还款和延期/生成借条/全额还款
 * @author XIAORONGDIAN
 * @version 2018-12-11
 */
public class NfsLoanPartialAndDelay extends CrudEntity<NfsLoanPartialAndDelay> {
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * 状态
	 */
	public enum Status {

		/** 待确认 */
		confirm,

		/** 已同意  */
		agreed,
		
		/** 已拒绝  */
		reject,
		
		/** 已取消  */
		canceled,
		
		/** 其他 */
		other

	}
	/**
	 * 类型
	 */
	public enum Type {
		
		/** 部分还款 */
		partial,
		
		/** 延期还款  */
		delay,
		
		/** 部分还款后延期  */
		partialAndDelay,
		
	}
	
	/**
	 * 支付状态
	 */
	public enum PayStatus {

	    /** 不需支付*/
		noPay,
		
		/** 待支付 */
		waitingPay,
		
		/** 支付中 */
		paying,		
		
		/** 支付成功 */
		success,
		
		/** 支付失败*/
		fail
	}
	
	
	/** 申请人 */
	private Member member;
	
	/** 操作类型 */
	private Type type;	
	
	/** 发起方 */
	private LoanRole memberRole;	
	
	/** 借条ID */
	private NfsLoanRecord loan;		
	
	/** 部分 还款金额 */
	private BigDecimal partialAmount = BigDecimal.ZERO;
	
	/** 操作后本金 */
	private BigDecimal remainAmount;	
	
	/** 操作后利息 */
	private BigDecimal nowInterest = BigDecimal.ZERO;		
	
	/** 操作后还款日 */
	private Date nowRepayDate;		
	
	/** 延期利息 */
	private BigDecimal delayInterest = BigDecimal.ZERO;	
	
	/** 逾期利息 */
	private BigDecimal overdueInterest = BigDecimal.ZERO;		
	
	/** 延期利率 */
	private BigDecimal delayRate = BigDecimal.ZERO;		
	
	/** 上期本金 */
	private BigDecimal oldAmount;		
	
	/** 上期利息 */
	private BigDecimal oldInterest = BigDecimal.ZERO;		
	
	/** 上期利率 */
	private BigDecimal oldRate = BigDecimal.ZERO;		
	
	/** 延期天数 */
	private Integer delayDays = 0;		
	
	/** 申请状态 */
	private Status status;
	
	/** 申请状态 */
	private PayStatus payStatus;
	
	/** 开始时间 */
	private Date beginTime;	
	/** 结束时间*/
	private Date endTime;
	
	/** 最大金额 */
	private BigDecimal maxAmount;	
	
	/** 最小金额 */
	private BigDecimal minAmount;
	
	
	public NfsLoanPartialAndDelay() {
		super();
	}

	public NfsLoanPartialAndDelay(Long id){
		super(id);
	}
	
	public Member getMember() {
		return member;
	}

	public void setMember(Member member) {
		this.member = member;
	}

	public LoanRole getMemberRole() {
		return memberRole;
	}

	public void setMemberRole(LoanRole memberRole) {
		this.memberRole = memberRole;
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

	public BigDecimal getPartialAmount() {
		return partialAmount;
	}

	public void setPartialAmount(BigDecimal partialAmount) {
		this.partialAmount = partialAmount;
	}

	public BigDecimal getDelayInterest() {
		return delayInterest;
	}

	public void setDelayInterest(BigDecimal delayInterest) {
		this.delayInterest = delayInterest;
	}

	public BigDecimal getRemainAmount() {
		return remainAmount;
	}

	public void setRemainAmount(BigDecimal remainAmount) {
		this.remainAmount = remainAmount;
	}

	public Integer getDelayDays() {
		return delayDays;
	}

	public void setDelayDays(Integer delayDays) {
		this.delayDays = delayDays;
	}

	public BigDecimal getDelayRate() {
		return delayRate;
	}

	public void setDelayRate(BigDecimal delayRate) {
		this.delayRate = delayRate;
	}

	public BigDecimal getOldInterest() {
		return oldInterest;
	}

	public void setOldInterest(BigDecimal oldInterest) {
		this.oldInterest = oldInterest;
	}

	public BigDecimal getOldRate() {
		return oldRate;
	}

	public void setOldRate(BigDecimal oldRate) {
		this.oldRate = oldRate;
	}

	public BigDecimal getOldAmount() {
		return oldAmount;
	}

	public void setOldAmount(BigDecimal oldAmount) {
		this.oldAmount = oldAmount;
	}

	public Date getNowRepayDate() {
		return nowRepayDate;
	}

	public void setNowRepayDate(Date nowRepayDate) {
		this.nowRepayDate = nowRepayDate;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public BigDecimal getOverdueInterest() {
		return overdueInterest;
	}

	public void setOverdueInterest(BigDecimal overdueInterest) {
		this.overdueInterest = overdueInterest;
	}

	public BigDecimal getNowInterest() {
		return nowInterest;
	}

	public void setNowInterest(BigDecimal nowInterest) {
		this.nowInterest = nowInterest;
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
	
	@ExcelField(title="借款人姓名", align=2, sort=10)
	public String getLoaneeName() {
		return getLoan().getLoanee().getName();
	}
	
	@ExcelField(title="借款人手机号码", align=2, sort=20)
	public String getLoaneePhoneNo() {
		return getLoan().getLoanee().getUsername();
	}
	@ExcelField(title="延期天数", align=2, sort=60)
	public String getLoanTerm() {
		return getDelayDays().toString();
	}
	
	@ExcelField(title="延期金额", align=2, sort=60)
	public String getDelayAmount() {
		return StringUtils.decimalToStr(getRemainAmount(), 2);
	}
	
	@ExcelField(title="通过时间", align=2, sort=90)
	public String getDelayTime() {
		return DateUtils.getDateStr(getCreateTime(), "yyyy-MM-dd HH:mm:ss");
	}

	public BigDecimal getMaxAmount() {
		return maxAmount;
	}

	public void setMaxAmount(BigDecimal maxAmount) {
		this.maxAmount = maxAmount;
	}

	public BigDecimal getMinAmount() {
		return minAmount;
	}

	public void setMinAmount(BigDecimal minAmount) {
		this.minAmount = minAmount;
	}

	public PayStatus getPayStatus() {
		return payStatus;
	}

	public void setPayStatus(PayStatus payStatus) {
		this.payStatus = payStatus;
	}
}