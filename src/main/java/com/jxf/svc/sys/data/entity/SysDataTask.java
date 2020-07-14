package com.jxf.svc.sys.data.entity;


import javax.validation.constraints.NotNull;

import com.jxf.svc.sys.crud.entity.CrudEntity;

/**
 * 批次任务Entity
 * @author wo
 * @version 2019-01-12
 */
public class SysDataTask extends CrudEntity<SysDataTask> {
	
	private static final long serialVersionUID = 1L;
	/**
	 * 状态
	 */
	public enum Status {

		/** 未开始 */
		waitDo,

		/** 进行中 */
		doing,
		
		/** 已结束 */
		closed
	}
	/** 任务名称 */
	private SysDataImport data;		
	/** 起始ID */
	private Long startId;		
	/** 结束ID */
	private Long endId;		
	/** quantity */
	private Integer quantity;		
	/** 任务状态 */
	private Status status;		
	/** 是否启动 */
	private Boolean isOn;		

	
	public SysDataTask() {
		super();
	}

	public SysDataTask(Long id){
		super(id);
	}

	public SysDataImport getData() {
		return data;
	}

	public void setData(SysDataImport data) {
		this.data = data;
	}
	
	public Long getStartId() {
		return startId;
	}

	public void setStartId(Long startId) {
		this.startId = startId;
	}
	
	public Long getEndId() {
		return endId;
	}

	public void setEndId(Long endId) {
		this.endId = endId;
	}
	
	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}
	
	@NotNull(message="是否启动不能为空")
	public Boolean getIsOn() {
		return isOn;
	}

	public void setIsOn(Boolean isOn) {
		this.isOn = isOn;
	}


	

	
}