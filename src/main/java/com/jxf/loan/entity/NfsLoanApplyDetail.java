package com.jxf.loan.entity;

import java.math.BigDecimal;


import com.jxf.loan.entity.NfsLoanApply.LoanRole;
import com.jxf.loan.entity.NfsLoanApply.TrxType;
import com.jxf.mem.entity.Member;
import com.jxf.svc.sys.crud.entity.CrudEntity;

/**
 * 借贷对象Entity
 * @author wo
 * @version 2018-10-18
 */
public class NfsLoanApplyDetail extends CrudEntity<NfsLoanApplyDetail> {
	
	private static final long serialVersionUID = 1L;
	/**
	 * 申请状态
	 */
	public enum Status {

	    /** 待确认-0*/
		pendingAgree,
		
		/** 已成功-1 */
		success,
				
		/** 已拒绝-2*/
		reject,
		
		/** 已取消-3*/
		canceled,
		
		/** 已过期-4 */
		expired
	}
	/**
	 * 利率协商状态
	 */
	public enum IntStatus {

		/** 原来的 */
		primary,
	
		/** 已修改(申请修改利息待借款人同意) */
		changed,
		
		/** 已确认(同意或者拒绝之后) */
		confirmed

	}
	/**
	 * 活体视频状态
	 */
	public enum AliveVideoStatus {

		/** 不需要上传-0 */
		notUpload,
	
		/** 待上传-1 */
		pendingUpload,
		
		/** 已上传-2 */
		hasUpload,
	    
		/** 已驳回-3 */
		pendingReUpload,
		
		/** 已通过-4 */
		passed		

	}
	/**
	 * 争议解决方式
	 * @author SuHuimin
	 *
	 */
	public enum DisputeResolution{
		/** 仲裁 */
		arbitration,
		/** 诉送 */
		prosecution
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
	
	/** 借贷申请 */
	private NfsLoanApply apply;		
	
	/** 借贷会员 */
	private Member member;	
	
	/** 交易类型*/
	private TrxType trxType;
	
	/** 借贷角色 */
	private LoanRole loanRole;	
	
	/** 借款金额 */
	private BigDecimal amount;	
	
	/** 申请状态 */
	private Status status;	
	
	/** 利息状态 */
	private IntStatus intStatus;
	
	/** 活体视频状态 */
	private AliveVideoStatus aliveVideoStatus;
	
	/** 视频地址*/
	private String videoUrl;
		
	/** 借条进度*/
	private String progress;
	
	/** 争议解决方式*/
	private DisputeResolution disputeResolution;
	
	/** 争议解决方式*/
	private PayStatus payStatus;
	
	
	public NfsLoanApplyDetail() {
		super();
	}

	public NfsLoanApplyDetail(Long id){
		super(id);
	}

	public NfsLoanApply getApply() {
		return apply;
	}

	public void setApply(NfsLoanApply apply) {
		this.apply = apply;
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
	
	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}


	public IntStatus getIntStatus() {
		return intStatus;
	}

	public void setIntStatus(IntStatus intStatus) {
		this.intStatus = intStatus;
	}

	public AliveVideoStatus getAliveVideoStatus() {
		return aliveVideoStatus;
	}

	public void setAliveVideoStatus(AliveVideoStatus aliveVideoStatus) {
		this.aliveVideoStatus = aliveVideoStatus;
	}

	public String getProgress() {
		return progress;
	}

	public void setProgress(String progress) {
		this.progress = progress;
	}

	public String getVideoUrl() {
		return videoUrl;
	}

	public void setVideoUrl(String videoUrl) {
		this.videoUrl = videoUrl;
	}

	public DisputeResolution getDisputeResolution() {
		return disputeResolution;
	}

	public void setDisputeResolution(DisputeResolution disputeResolution) {
		this.disputeResolution = disputeResolution;
	}

	public TrxType getTrxType() {
		return trxType;
	}

	public void setTrxType(TrxType trxType) {
		this.trxType = trxType;
	}

	public PayStatus getPayStatus() {
		return payStatus;
	}

	public void setPayStatus(PayStatus payStatus) {
		this.payStatus = payStatus;
	}
}