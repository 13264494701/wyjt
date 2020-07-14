package com.jxf.loan.entity;

import java.math.BigDecimal;
import java.util.Date;

import org.hibernate.validator.constraints.Length;

import com.jxf.svc.sys.crud.entity.CrudEntity;
import com.jxf.svc.utils.enumUtils.BaseCodeEnum;

/**
 * 借条操作记录Entity
 * @author XIAORONGDIAN
 * @version 2018-12-18
 */
public class NfsLoanOperatingRecord extends CrudEntity<NfsLoanOperatingRecord> {
	
	private static final long serialVersionUID = 1L;
	/**
	 * 操作类型
	 */
	public enum Type implements BaseCodeEnum{

		/** 部分还款 */
		partial("部分还款", 0),
		
		/** 延期还款  */
		delay("延期还款", 1),
		
		/** 部分还款后延期  */
		partialAndDelay("部分还款后延期", 2),
		
		/** 生成借条  */
		create("生成借条", 3),
		
		/** 全额还款  */
		totalDueAmount("全额还款", 4),
		
		/** 线下全额还款 */
		lineDown("线下全额还款", 5),
		/** 分期还款 */
		principalAndInterestByMonth("分期还款", 6);
		
		private int code;
		private String name;
		Type(String name,int code) {
			this.code = code; 
			this.name = name; 
		}
		 
		@Override
		public int getCode() { return this.code; }
		@Override
		public String getName() { return this.name; }

	}
	
	/** 发起方 */
	private String initiator;
	/** 延期天数 */
	private Integer delayDays;		
	/** 延期利息 */
	private BigDecimal delayInterest;		
	/** 部分还款金额 */
	private BigDecimal repaymentAmount;	
	/** 操作类型 */
	private Type type;	
	
	/** 操作前的借条: 应还本金 应还利息 操作前逾期利息   关联loanId在这里  借款人/放款人 在这里 */
	private NfsLoanRecord oldRecord;
	
	/** 操作后的借条:  操作后本金 操作后利息 操作后还款日 操作后借条状态 生成借条在这里 */
	private NfsLoanRecord nowRecord;
	/** 开始时间 */
	private Date beginTime;	
	/** 结束时间*/
	private Date endTime;
	
	public NfsLoanOperatingRecord() {
		super();
	}

	public NfsLoanOperatingRecord(Long id){
		super(id);
	}

	@Length(min=1, max=255, message="发起方长度必须介于 1 和 255 之间")
	public String getInitiator() {
		return initiator;
	}

	public void setInitiator(String initiator) {
		this.initiator = initiator;
	}

	public NfsLoanRecord getOldRecord() {
		return oldRecord;
	}

	public void setOldRecord(NfsLoanRecord oldRecord) {
		this.oldRecord = oldRecord;
	}

	public NfsLoanRecord getNowRecord() {
		return nowRecord;
	}

	public void setNowRecord(NfsLoanRecord nowRecord) {
		this.nowRecord = nowRecord;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public Integer getDelayDays() {
		return delayDays;
	}

	public void setDelayDays(Integer delayDays) {
		this.delayDays = delayDays;
	}

	public BigDecimal getDelayInterest() {
		return delayInterest;
	}

	public void setDelayInterest(BigDecimal delayInterest) {
		this.delayInterest = delayInterest;
	}

	public BigDecimal getRepaymentAmount() {
		return repaymentAmount;
	}

	public void setRepaymentAmount(BigDecimal repaymentAmount) {
		this.repaymentAmount = repaymentAmount;
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