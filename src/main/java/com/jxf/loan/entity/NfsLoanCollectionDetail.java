package com.jxf.loan.entity;

import javax.validation.constraints.NotNull;

import com.jxf.svc.sys.crud.entity.CrudEntity;

/**
 * 催收明细Entity
 * @author Administrator
 * @version 2018-12-24
 */
public class NfsLoanCollectionDetail extends CrudEntity<NfsLoanCollectionDetail> {
	
	public enum Status{
		/** 0 审核中 */
		underReview,
		/* 1已受理  */
		accepted,
		/* 2拒绝受理  */
		refuseToAccept,
		/* 3 催收中  */
		collection,
		/* 4 催收成功  */
		successfulCollection,
		/* 5催收失败(协议到期) */
		collectionFailure
	}
	
	public enum Type{
		/** 全额 */
		fullAmount,
		/** 分期 */
		staging
	}
	
	public enum Task{
		/** 0 信息不实 */
		inaccurateInformation,
	    /* 1 资料不全*/
		incompleteInformation,
	    /* 2标的额不足1000元*/
		insufficientAmount,
	    /* 3账期超长*/
		longDelay,
	    /* 4不愿支付佣金*/
		unwillingToPayCommission,
	    /* 5其他(其他情况手动填写在备注中)*/
		other
	}
	
	private static final long serialVersionUID = 1L;
			/** 催收申请编号 */
	private Long collectionId;		
			/** 催收类型 */
	private Type type;		
			/** 催收状态 */
	private Status status;		
			/** 拒绝受理原因 */
	private Task task;		
	
	public NfsLoanCollectionDetail() {
		super();
	}

	public NfsLoanCollectionDetail(Long id){
		super(id);
	}

	@NotNull(message="借条编号不能为空")
	public Long getCollectionId() {
		return collectionId;
	}

	public void setCollectionId(Long collectionId) {
		this.collectionId = collectionId;
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