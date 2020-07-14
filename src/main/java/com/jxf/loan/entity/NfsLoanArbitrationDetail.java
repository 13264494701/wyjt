package com.jxf.loan.entity;

import javax.validation.constraints.NotNull;

import com.jxf.svc.sys.crud.entity.CrudEntity;

/**
 * 仲裁明细Entity
 * @author Administrator
 * @version 2018-12-24
 */
public class NfsLoanArbitrationDetail extends CrudEntity<NfsLoanArbitrationDetail> {
	
	public enum Status{
	     /** 审核中*/
			underReview,
	     /** 代理仲裁申请中*/
			agentArbitrationApplication,
		 /** 仲裁中 */
			applicating,
	     /** 仲裁院已立案*/
			arbitrationHasFiled,
	     /** 仲裁已裁决*/
			arbitrationHasBeenDecided,
	     /** 仲裁结束*/
			endOfArbitration,
	     /** 仲裁申请费退回*/
			arbitrationApplicationFeeReturned,
	     /** 退款已到账*/
			refundHasArrived,
	     /** 审核未通过*/
			theAuditFailed,
	     /** 立案失败*/
			failureToFile,
	     /** 仲裁失败*/
			arbitrationFailure,
		 /** 借条关闭 */
			debit,
		 /** 已缴费 */
			paid
	     
	}
	/*  任务（task）     		类别（stateStr） lawsuit的状态
     * 1000  人工审核  			审核流程			100  0
     * 1100  审核已通过		 	审核结果			200	 1
     * 1101  仲裁审理进行中 		仲裁流程			300	 2
     * 1102  立案成功，等待开庭 	立案结果			301	 3
     * 1103  仲裁院已宣判裁决 		仲裁结果 			400	 5
     * 1104  仲裁裁决执行中   		仲裁结果			401	 4
     * 1200  仲裁院已告知失败原因 	仲裁结果			700  10 
     * 1201 仲裁失败，结果已邮箱通知   仲裁结果			401	 10
     * 1202 审核未通过，仲裁流程结束  仲裁结果			401	 8
     * 1203 立案未通过，仲裁流程结束  仲裁结果			401	 9
     * 1204 审核资料异常			审核结果	    	500	 8
     * 1205 仲裁院立案未通过	            立案结果            		600	 9
     * 1400 退还预支付款项  	            退款流程			402	 6
     * 1450 查看款项是否到账 	            退款流程 			403	 7
     */
	public enum Task{
		/** 人工审核 */
		manualReview,
		/** 审核已通过 */
		theAuditHasBeenApproved,
		/** 仲裁审理进行中 */
		arbitrationTrialIsInProgress,
		/** 立案成功 */
		successfullyFiled,
		/** 仲裁院已宣判裁决 */
		ArbitrationHasPronouncedTheVerdict,
		/** 仲裁裁决执行中 */
		arbitralAwardEnforcement,
		/** 仲裁院已告知失败原因 */
		arbitrationHasInformedTheReasonForTheFailure,
		/** 仲裁失败 */
		arbitrationFailure,
		/**  审核未通过 */
		theAuditFailed,
		/** 立案未通过 */
		filingFailed,
		/** 审核资料异常 */
		auditDataIsAbnormal,
		/** 仲裁院立案未通过 */
		arbitrationFailedToPassTheCase,
		/** 退还预支付款项 */
		refundOfPrepayments,
		/** 查看款项是否到账 */
		checkIfThePaymentIsReceived,
		/** 借条关闭 */
		debit,
		/** 已缴费 */
		paid
	}
	
	public enum Type{
		/** 审核流程 */
		auditProcess,
		/** 审核结果 */
		auditResult,
		/** 仲裁流程 */
		arbitrationProcess,
		/** 立案结果 */
		filedResult,		
		/** 仲裁结果 */
		arbitrationResult,
		/** 退款流程 */
		refundProcess
	}
	
	private static final long serialVersionUID = 1L;
			/** 借条编号 */
	private Long arbitrationId;		
			/** 仲裁类型 */
	private Type type;		
			/** 仲裁状态 */
	private Status status;		
			/** 任务 */
	private Task task;		
	
	public NfsLoanArbitrationDetail() {
		super();
	}

	public NfsLoanArbitrationDetail(Long id){
		super(id);
	}

	@NotNull(message="借条编号不能为空")
	public Long getArbitrationId() {
		return arbitrationId;
	}

	public void setArbitrationId(Long arbitrationId) {
		this.arbitrationId = arbitrationId;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public Task getTask() {
		return task;
	}

	public void setTask(Task task) {
		this.task = task;
	}
	
}