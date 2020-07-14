package com.jxf.ufang.entity;

import java.util.Date;

import com.jxf.svc.sys.crud.entity.CrudEntity;

/**
 * 中智诚上报Entity
 * @author XIAORONGDIAN
 * @version 2019-04-22
 */
public class ZZCUpload extends CrudEntity<ZZCUpload> {
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * 借款类型
	 */
	public enum Type {

		/** 批核数据上报 */
		apply,

		/** 贷后数据上报  */
		record,
		
		/** 展期数据上报  */
		delay

	}
	
	/** 上报类型 */
	private Type type;		
		/** 数据区间-起始时间 */
	private Date startTime;		
		/** 数据区间-结束时间 */
	private Date endTime;		
			/** 本次上报数量 */
	private Integer count;		
	
	public ZZCUpload() {
		super();
	}

	public ZZCUpload(Long id){
		super(id);
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}
	
}