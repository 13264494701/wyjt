package com.jxf.loan.entity;




import java.math.BigDecimal;
import java.util.Date;
import com.alibaba.fastjson.annotation.JSONField;
import com.jxf.mem.entity.Member;
import com.jxf.svc.sys.crud.entity.CrudEntity;

/**
 * 借条数据Entity
 * @author wo
 * @version 2019-04-28
 */
public class NfsLoanRecordData extends CrudEntity<NfsLoanRecordData> {
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * 状态
	 */
	public enum RepayStatus {

		
		/**提前还款 */
		repayedAdvance,
		
		/**按期还款 */
		repayedDueDate,
		
		/** 逾期待还*/
		overdueNotRepayed,
		
		/** 逾期已还*/
		overdueAndRepayed,
		
		/** 延期还款*/
		delayRepayed
		
	}
	
	
	/** 借款人 */
	private Member loanee;	
	/** 借款金额 */
	private BigDecimal amount;		
	/** 借款利率 */
	private BigDecimal intRate;		
	/** 利息 */
	private BigDecimal interest;		
	/** 借款期限 */
	private Integer term;		
	/** 应还日期 */
	private Date dueRepayDate;		
	/** 结清日期 */
	private Date completeDate;		
	/** 还款状态 */
	private RepayStatus repayStatus;		
	/** 逾期天数 */
	private Integer overdueDays;		
	/** 是否OK */
	private Boolean isOk;		
	
	private String beginOverdueDays;		// 开始 逾期天数
	
	private String endOverdueDays;		// 结束 逾期天数
	
	public NfsLoanRecordData() {
		super();
	}

	public NfsLoanRecordData(Long id){
		super(id);
	}

	public Member getLoanee() {
		return loanee;
	}

	public void setLoanee(Member loanee) {
		this.loanee = loanee;
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
	
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	public Date getDueRepayDate() {
		return dueRepayDate;
	}

	public void setDueRepayDate(Date dueRepayDate) {
		this.dueRepayDate = dueRepayDate;
	}
	
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	public Date getCompleteDate() {
		return completeDate;
	}

	public void setCompleteDate(Date completeDate) {
		this.completeDate = completeDate;
	}
	
	public RepayStatus getRepayStatus() {
		return repayStatus;
	}

	public void setRepayStatus(RepayStatus repayStatus) {
		this.repayStatus = repayStatus;
	}
	

	public Integer getOverdueDays() {
		return overdueDays;
	}

	public void setOverdueDays(Integer overdueDays) {
		this.overdueDays = overdueDays;
	}
	

	public Boolean getIsOk() {
		return isOk;
	}

	public void setIsOk(Boolean isOk) {
		this.isOk = isOk;
	}
	
	public String getBeginOverdueDays() {
		return beginOverdueDays;
	}

	public void setBeginOverdueDays(String beginOverdueDays) {
		this.beginOverdueDays = beginOverdueDays;
	}
	
	public String getEndOverdueDays() {
		return endOverdueDays;
	}

	public void setEndOverdueDays(String endOverdueDays) {
		this.endOverdueDays = endOverdueDays;
	}


		
}