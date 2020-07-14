package com.jxf.loan.entity;

import java.math.BigDecimal;
import java.util.Date;

import com.jxf.mem.entity.Member;
import org.hibernate.validator.constraints.Length;

import com.alibaba.fastjson.annotation.JSONField;
import com.jxf.svc.sys.crud.entity.CrudEntity;

/**
 * 借条仲裁增删改查Entity
 * @author LIUHUAIXIN
 * @version 2018-11-07
 */
public class NfsLoanArbitration extends CrudEntity<NfsLoanArbitration> {
	
	/**
	 * 仲裁状态
	 */
	public enum Status {
		/** 审核中 0*/
		review,

		/** 审核失败 1*/
		auditFailure,
		
		/** 申请中  2*/
		application,
		
		/** 仲裁中 3*/
		inArbitration,
		
		/** 立案失败 4*/
		failureToFile,
		
		/** 仲裁失败 5*/
		arbitrationFailure,
		
		/** 仲裁出裁决 6*/
		arbitration,
		
		/** 借条关闭  7*/
		debit,
		
		/** 已缴费  8*/
		paid,
		/** 待支付服务费 9*/
		waitingPay
	}
	
	/**
	 * 代理渠道
	 */
	public enum Channel {

		/** 张三公司*/
		zhangsan,

		/** 李四公司*/
		lisi,
		
		/** 王五公司  */
		wangwu
	}
	
	public enum StrongStatus{
		/** 已申请*/
		appliedStrong,
		/** 未申请*/
		notApplyStrong
	}
	
	/**
	 * 业务保全进程
	 */
	public enum PreservationProcess {
		
		/** 未上传  0*/
		noUpload,
		
		/** 借款人认证 1*/
		loaneeIdentify,
		
		/** 放款人认证 2*/
		loanerIdentify,

		/** 合同 3*/
		contract,
		
		/** 付款  4*/
		loan,
		
		/** 还款  5*/
		repay,
		
		/** 续签  6*/
		renewal,
		
		/** 保全成功  7*/
		success
		
	}
	
	
	private static final long serialVersionUID = 1L;
			/** 借条编号 */
	private NfsLoanRecord loan;		
			/** 申请仲裁金额 */
	private BigDecimal applyAmount;		
			/** 仲裁服务费 */
	private String fee;		
			/** 退费金额 */
	private String refundFee;		
			/** 驳回原因 */
	private String refuseReason;		
			/** 仲裁状态 */
	private Status status;		
			/** 代理渠道 */
	private Channel channel;		
			/** 第三方ID */
	private String thirdPartId;		
	/** 线下交易借款人账号*/
	private String loaneeAccount;
	/** 线下交易借款人账户开户机构名*/
	private String loaneeAccountName;
	/** 线下交易放款人账号*/
	private String loanerAccount;
	/** 线下交易放款人账户开户机构名*/
	private String loanerAccountName;
	/** 支付单号*/
	private String payOrderNo;
	/** 业务保全进程*/
	private PreservationProcess	preservationProcess;
	
			/** 结案时间 */
	private Date endTime;
			/** 结案时间 */
	private String images;
	
			/** 强执状态 */
	private StrongStatus strongStatus;
	
			/** 出裁决时间 */
	private Date ruleTime;
	private Member member;
	
	/** 仲裁申请起始时间 */
	private Date beginTime;
	/** 仲裁申请结束时间 */
	private Date endTimes;

	public Date getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(Date beginTime) {
		this.beginTime = beginTime;
	}

	public Date getEndTimes() {
		return endTimes;
	}

	public void setEndTimes(Date endTimes) {
		this.endTimes = endTimes;
	}

	public Member getMember() {
		return member;
	}

	public void setMember(Member member) {
		this.member = member;
	}
	
	public StrongStatus getStrongStatus() {
		return strongStatus;
	}

	public void setStrongStatus(StrongStatus strongStatus) {
		this.strongStatus = strongStatus;
	}

	public NfsLoanArbitration() {
		super();
	}

	public NfsLoanArbitration(Long id){
		super(id);
	}

	public void setLoan(NfsLoanRecord loan) {
		this.loan = loan;
	}
	
	public NfsLoanRecord getLoan() {
		return loan;
	}

	public BigDecimal getApplyAmount() {
		return applyAmount;
	}

	public void setApplyAmount(BigDecimal applyAmount) {
		this.applyAmount = applyAmount;
	}

	public String getFee() {
		return fee;
	}

	public void setFee(String fee) {
		this.fee = fee;
	}
	
	public String getRefundFee() {
		return refundFee;
	}

	public void setRefundFee(String refundFee) {
		this.refundFee = refundFee;
	}
	
	@Length(min=0, max=255, message="驳回原因长度必须介于 0 和 255 之间")
	public String getRefuseReason() {
		return refuseReason;
	}

	public void setRefuseReason(String refuseReason) {
		this.refuseReason = refuseReason;
	}
	
	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	
	@Length(min=0, max=64, message="第三方ID长度必须介于 0 和 64 之间")
	public String getThirdPartId() {
		return thirdPartId;
	}

	public void setThirdPartId(String thirdPartId) {
		this.thirdPartId = thirdPartId;
	}
	
	public String getLoaneeAccount() {
		return loaneeAccount;
	}

	public void setLoaneeAccount(String loaneeAccount) {
		this.loaneeAccount = loaneeAccount;
	}

	public String getLoaneeAccountName() {
		return loaneeAccountName;
	}

	public void setLoaneeAccountName(String loaneeAccountName) {
		this.loaneeAccountName = loaneeAccountName;
	}

	public String getLoanerAccount() {
		return loanerAccount;
	}

	public void setLoanerAccount(String loanerAccount) {
		this.loanerAccount = loanerAccount;
	}

	public String getLoanerAccountName() {
		return loanerAccountName;
	}

	public void setLoanerAccountName(String loanerAccountName) {
		this.loanerAccountName = loanerAccountName;
	}

	public String getPayOrderNo() {
		return payOrderNo;
	}

	public void setPayOrderNo(String payOrderNo) {
		this.payOrderNo = payOrderNo;
	}

	public PreservationProcess getPreservationProcess() {
		return preservationProcess;
	}

	public void setPreservationProcess(PreservationProcess preservationProcess) {
		this.preservationProcess = preservationProcess;
	}

	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public Channel getChannel() {
		return channel;
	}

	public void setChannel(Channel channel) {
		this.channel = channel;
	}

	public Date getRuleTime() {
		return ruleTime;
	}

	public void setRuleTime(Date ruleTime) {
		this.ruleTime = ruleTime;
	}

	public String getImages() {
		return images;
	}

	public void setImages(String images) {
		this.images = images;
	}
}