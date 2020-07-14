package com.jxf.loan.entity;

import org.hibernate.validator.constraints.Length;
import javax.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.Date;
import com.alibaba.fastjson.annotation.JSONField;

import com.jxf.svc.sys.crud.entity.CrudEntity;

/**
 * 强执Entity
 * @author LIUHUAIXIN
 * @version 2018-12-20
 */
public class NfsLoanArbitrationExecution extends CrudEntity<NfsLoanArbitrationExecution> {
	/** 强执状态*/
	public enum ExecutionStatus{
		/** 申请中 0*/
		executionApplication,
		/** 拒绝受理 1*/
		executionRefuseToAccept,
		/** 缴费中 2*/
		executionPayment,
		/** 进行中 3*/
		executionProcessing,
		/** 已结束 4*/
		executionOver,
		/** 已失效 5*/
		executionExpired,
		/** 强执失败 6*/
		executionFailure,
		/** 借条关闭 7*/
		debit
	}
	
	/** 成交渠道 */
	public enum Channel{
		/** 张三公司*/
		zhangsan,

		/** 李四公司*/
		lisi,
		
		/** 王五公司  */
		wangwu
	}
	
	private static final long serialVersionUID = 1L;
	/** 借条编号 */
	private String loanNo;		
	/** 债权人ID */
	private Long loanerId;		
	/** 债权人姓名 */
	private String loanerName;		
	/** 债务人ID */
	private Long loaneeId;		
	/** 债务人姓名 */
	private String loaneeName;		
	/** 借款金额 */
	private BigDecimal amount;		
    /** 借款利息 */
	private BigDecimal interest;		
	/** 借款利率 */
	private BigDecimal intRate;		
	/** 借款期限 */
	private Integer term;		
	/** 已还期数 */
	private Integer repayedTerm;		
	/** 未还期数 */
	private Integer dueRepayTerm;		
	/** 本期到期日 */
	private Date dueRepayDate;		
	/** 缴费时间 */
	private Date paytime;		
	/** 出裁决时间 */
	private Date rulingtime;		
	/** 本期应还金额 */
	private BigDecimal dueRepayAmount;		
	/** 结清日期 */
	private Date completeDate;		
	/** 强执状态 */
	private ExecutionStatus status;		
	/** 成交渠道 */
	private Channel channel;		
	/** 费用 */
	private BigDecimal fee;		
	/** 借条 */
	private NfsLoanRecord loan;		
	/** 仲裁编号 */
	private Long arbitrationId;
	
	/** 强执申请起始时间 */
	private Date beginTime;
	/** 强执申请结束时间 */
	private Date endTime;
	


	public NfsLoanArbitrationExecution() {
		super();
	}

	public NfsLoanArbitrationExecution(Long id){
		super(id);
	}

	@Length(min=1, max=32, message="借条编号长度必须介于 1 和 32 之间")
	public String getLoanNo() {
		return loanNo;
	}

	public void setLoanNo(String loanNo) {
		this.loanNo = loanNo;
	}
	
	@NotNull(message="债权人ID不能为空")
	public Long getLoanerId() {
		return loanerId;
	}

	public void setLoanerId(Long loanerId) {
		this.loanerId = loanerId;
	}
	
	@Length(min=1, max=64, message="债权人姓名长度必须介于 1 和 64 之间")
	public String getLoanerName() {
		return loanerName;
	}

	public void setLoanerName(String loanerName) {
		this.loanerName = loanerName;
	}
	
	@NotNull(message="债务人ID不能为空")
	public Long getLoaneeId() {
		return loaneeId;
	}

	public void setLoaneeId(Long loaneeId) {
		this.loaneeId = loaneeId;
	}
	
	@Length(min=1, max=64, message="债务人姓名长度必须介于 1 和 64 之间")
	public String getLoaneeName() {
		return loaneeName;
	}

	public void setLoaneeName(String loaneeName) {
		this.loaneeName = loaneeName;
	}
	
	public Integer getTerm() {
		return term;
	}

	public void setTerm(Integer term) {
		this.term = term;
	}

	public Integer getRepayedTerm() {
		return repayedTerm;
	}

	public void setRepayedTerm(Integer repayedTerm) {
		this.repayedTerm = repayedTerm;
	}

	public Integer getDueRepayTerm() {
		return dueRepayTerm;
	}

	public void setDueRepayTerm(Integer dueRepayTerm) {
		this.dueRepayTerm = dueRepayTerm;
	}

	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	public Date getDueRepayDate() {
		return dueRepayDate;
	}

	public void setDueRepayDate(Date dueRepayDate) {
		this.dueRepayDate = dueRepayDate;
	}
	
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	public Date getPaytime() {
		return paytime;
	}

	public void setPaytime(Date paytime) {
		this.paytime = paytime;
	}
	
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	public Date getRulingtime() {
		return rulingtime;
	}

	public void setRulingtime(Date rulingtime) {
		this.rulingtime = rulingtime;
	}
	
	public BigDecimal getDueRepayAmount() {
		return dueRepayAmount;
	}

	public void setDueRepayAmount(BigDecimal dueRepayAmount) {
		this.dueRepayAmount = dueRepayAmount;
	}

	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	public Date getCompleteDate() {
		return completeDate;
	}

	public void setCompleteDate(Date completeDate) {
		this.completeDate = completeDate;
	}
	
	public ExecutionStatus getStatus() {
		return status;
	}

	public void setStatus(ExecutionStatus status) {
		this.status = status;
	}

	public Channel getChannel() {
		return channel;
	}

	public void setChannel(Channel channel) {
		this.channel = channel;
	}
	
	public NfsLoanRecord getLoan() {
		return loan;
	}

	public void setLoan(NfsLoanRecord loan) {
		this.loan = loan;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public BigDecimal getInterest() {
		return interest;
	}

	public void setInterest(BigDecimal interest) {
		this.interest = interest;
	}

	public BigDecimal getIntRate() {
		return intRate;
	}

	public void setIntRate(BigDecimal intRate) {
		this.intRate = intRate;
	}

	public BigDecimal getFee() {
		return fee;
	}

	public void setFee(BigDecimal fee) {
		this.fee = fee;
	}

	public Long getArbitrationId() {
		return arbitrationId;
	}

	public void setArbitrationId(Long arbitrationId) {
		this.arbitrationId = arbitrationId;
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