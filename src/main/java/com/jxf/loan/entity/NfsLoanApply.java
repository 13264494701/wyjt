package com.jxf.loan.entity;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.jxf.mem.entity.Member;
import com.jxf.svc.sys.crud.entity.CrudEntity;
import com.jxf.svc.utils.enumUtils.BaseCodeEnum;

/**
 * 借款申请Entity
 * @author wo
 * @version 2018-09-26
 */
public class NfsLoanApply extends CrudEntity<NfsLoanApply> {
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * 申请人角色
	 */
	public enum LoanRole implements BaseCodeEnum{

		loanee("借款人",0), 
		loaner("放款人",1);
		
		private int code;
		private String name;
		LoanRole(String name,int code) {
			this.code = code; 
			this.name = name; 
		}
		 
		@Override
		public int getCode() { return this.code; }
		@Override
		public String getName() { return this.name; }
	}
	
	/**
	 * 借款类型
	 */
	public enum LoanType {

		/** 单人 */
		single,

		/** 多人  */
		multiple,
		
		/** 待定  */
		undetermined
	}
	
	/**
	 * 交易类型
	 */
	public enum TrxType {

		/** 线上交易 */
		online,

		/** 线下交易  */
		offline
		
	}
	
	/**
	 * 借款用途
	 */
	public enum LoanPurp implements BaseCodeEnum{

		turnover("临时周转", 0), 
		rent("交房租", 1), 
		consume("消费", 2),
		repayCreditCard("还信用卡", 3), 
		attendTraining("报培训班", 4), 
		driverLicense("考驾照", 5), 
		other("其它", 6);
		
		private int code;
		private String name;
		LoanPurp(String name,int code) {
			this.code = code; 
			this.name = name; 
		}
		 
		@Override
		public int getCode() { return this.code; }
		@Override
		public String getName() { return this.name; }
	}
	
	/**
	 * 还款方式
	 */
	public enum RepayType {

		/** 到期还本付息 */
		oneTimePrincipalAndInterest,
		
		/** 按月等额本息 */
		principalAndInterestByMonth,
		
		/** 按月付息到期还本 */
		interestFirstByMonth
	}
	


	/**
	 * 申请渠道
	 */
	public enum Channel {

		/** 小程序 */
		 minipro,
 
		/** 苹果*/
		  ios,
		
		/** 安卓 */
          andriod,
          
        /** ufang */
          ufang,
          
        /** 公信堂 */
          gxt
	}
	
	/** 发起会员 */
	private Member member;		
	/** 申请人角色 */
	private LoanRole loanRole;	
	/** 借款类型*/
	private LoanType loanType;
	/** 交易类型*/
	private TrxType trxType;
	/** 借款用途 */
	private LoanPurp loanPurp;		
	/** 借款金额 */
	private BigDecimal amount;	
	/** 剩余可借金额 */
	private BigDecimal remainAmount;
	/** 借款利率 */
	private BigDecimal intRate;	
	/** 借款利息 */
	private BigDecimal interest;	
	/** 应还总额 */
	private BigDecimal repayAmt;	
	/** 还款方式 */
	private RepayType repayType;		
	/** 借款期限 */
	private Integer term;		
	/** 借款期限 */
	private Date loanStart;		

	public Date getLoanStart() {
		return loanStart;
	}

	public void setLoanStart(Date loanStart) {
		this.loanStart = loanStart;
	}

	/** 申请渠道 */
	private Channel channel;
	
	/** 交易对象(单人) */
	private NfsLoanApplyDetail detail;
	
	/** 交易对象(多人) */
	private List<NfsLoanApplyDetail> details = new ArrayList<NfsLoanApplyDetail>();
	
    /** 列表查询条件 开始 */
	/** 开始时间 */
	private Date beginDate;
	
	/** 结束时间 */
	private Date endDate;
	
	/** 最大金额 */
	private BigDecimal maxAmount;	
	
	/** 最小金额 */
	private BigDecimal minAmount;	
	
	/** 列表查询条件 结束 */
	
	public NfsLoanApply() {
		super();
	}

	public NfsLoanApply(Long id){
		super(id);
	}

	public Member getMember() {
		return member;
	}

	public void setMember(Member member) {
		this.member = member;
	}
	public LoanRole getLoanRole() {
		return loanRole;
	}

	public void setLoanRole(LoanRole loanRole) {
		this.loanRole = loanRole;
	}
	public LoanType getLoanType() {
		return loanType;
	}

	public void setLoanType(LoanType loanType) {
		this.loanType = loanType;
	}
	public TrxType getTrxType() {
		return trxType;
	}

	public void setTrxType(TrxType trxType) {
		this.trxType = trxType;
	}
	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	
	public BigDecimal getIntRate() {
		return intRate;
	}

	public void setIntRate(BigDecimal intRate) {
		this.intRate = intRate;
	}
	public BigDecimal getInterest() {
		return interest;
	}

	public void setInterest(BigDecimal interest) {
		this.interest = interest;
	}

	

	public Integer getTerm() {
		return term;
	}

	public void setTerm(Integer term) {
		this.term = term;
	}


	public LoanPurp getLoanPurp() {
		return loanPurp;
	}

	public void setLoanPurp(LoanPurp loanPurp) {
		this.loanPurp = loanPurp;
	}

	public RepayType getRepayType() {
		return repayType;
	}

	public void setRepayType(RepayType repayType) {
		this.repayType = repayType;
	}


	public Channel getChannel() {
		return channel;
	}

	public void setChannel(Channel channel) {
		this.channel = channel;
	}

	public BigDecimal getRepayAmt() {
		return repayAmt;
	}

	public void setRepayAmt(BigDecimal repayAmt) {
		this.repayAmt = repayAmt;
	}

	public NfsLoanApplyDetail getDetail() {
		return detail;
	}

	public void setDetail(NfsLoanApplyDetail detail) {
		this.detail = detail;
	}

	public List<NfsLoanApplyDetail> getDetails() {
		return details;
	}

	public void setDetails(List<NfsLoanApplyDetail> details) {
		this.details = details;
	}

	public Date getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
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

	public BigDecimal getRemainAmount() {
		return remainAmount;
	}

	public void setRemainAmount(BigDecimal remainAmount) {
		this.remainAmount = remainAmount;
	}






	
}