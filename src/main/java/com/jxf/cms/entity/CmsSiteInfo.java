package com.jxf.cms.entity;

import org.hibernate.validator.constraints.Length;

import com.jxf.svc.sys.crud.entity.CrudEntity;

/**
 * 站点信息Entity
 * @author JINXINFU
 * @version 2016-11-20
 */
public class CmsSiteInfo extends CrudEntity<CmsSiteInfo> {
	
	private static final long serialVersionUID = 1L;
	/**
	 * 类型
	 */
	public enum Type {
		
		/** 多行文本*/
		textarea,

		/** 富文本 */
		ckeditor 
	}
	 
	private String key;		// 引用名称
	private Type type;		// 编辑类型
	private String value;	// 显示内容
	
	public CmsSiteInfo() {
		super();
	}

	public CmsSiteInfo(Long id){
		super(id);
	}

	@Length(min=1, max=100, message="引用名称长度必须介于 1 和 100 之间")
	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}
	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}


	
}