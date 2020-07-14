package com.jxf.loan.entity;

import javax.validation.constraints.NotNull;

import com.jxf.svc.sys.crud.entity.CrudEntity;

/**
 * 强执明细Entity
 * @author Administrator
 * @version 2018-12-27
 */
public class NfsLoanArbitrationExecutionDetail extends CrudEntity<NfsLoanArbitrationExecutionDetail> {
	
	private static final long serialVersionUID = 1L;
	
	/** 借条类型*/
	public enum Type{
		/** 全额 */
		fullAmount,
		/** 分期 */
		staging
	}
	
	/** 强执状态*/
	public enum Status{
		/** 申请中 */
		executionApplication,
		/** 拒绝受理 */
		executionRefuseToAccept,
		/** 缴费中 */
		executionPayment,
		/** 进行中 */
		executionProcessing,
		/** 已结束 */
		executionOver,
		/** 已失效 */
		executionExpired,
		/** 强执失败 */
		executionFailure,
		/** 借条关闭 */
		debit
	}
	/** 强执编号 */
	private Long executionId;		
	/** 借条类型 */
	private Type type;		
	/** 强执状态 */
	private Status status;				
	
	public NfsLoanArbitrationExecutionDetail() {
		super();
	}

	public NfsLoanArbitrationExecutionDetail(Long id){
		super(id);
	}

	@NotNull(message="强执编号不能为空")
	public Long getExecutionId() {
		return executionId;
	}

	public void setExecutionId(Long executionId) {
		this.executionId = executionId;
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
	
}