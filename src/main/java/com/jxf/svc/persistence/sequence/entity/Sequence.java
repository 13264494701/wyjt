package com.jxf.svc.persistence.sequence.entity;

import com.jxf.svc.sys.crud.entity.CrudEntity;




/**
 * 用户Entity
 * @author jxf
 * @version 2015-07-28
 */
public class Sequence extends CrudEntity<Sequence> {

	private static final long serialVersionUID = 1L;
	
	private String name;//  表名
	private String current_value;// 增长值
	private String increment;		// 每次自增值
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCurrent_value() {
		return current_value;
	}
	public void setCurrent_value(String current_value) {
		this.current_value = current_value;
	}
	public String getIncrement() {
		return increment;
	}
	public void setIncrement(String increment) {
		this.increment = increment;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
}