package com.jxf.mms.tmpl.entity;

import org.hibernate.validator.constraints.Length;

import com.jxf.svc.sys.crud.entity.CrudEntity;

/**
 * 消息模板Entity
 * @author wo
 * @version 2018-10-28
 */
public class MmsMsgTmpl extends CrudEntity<MmsMsgTmpl> {
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * 模板类型
	 */
	public enum Type {

	    /** 短信*/
		sms,
		
		/** 邮件 */
		email,
				
		/** 站内信 */
		internalmsg
	}
	
	/** 模板类型 */
	private Type type;	
	/** 模板识别码 */
	private String code;		
	/** 消息模板名称 */
	private String name;
	/** 消息模板名称 */
	private String nameValue;
	/** 模板内容 */
	private String content;		
	
	public MmsMsgTmpl() {
		super();
	}

	public MmsMsgTmpl(Long id){
		super(id);
	}

	@Length(min=1, max=32, message="模板识别码长度必须介于 1 和 32 之间")
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	
	@Length(min=1, max=64, message="消息模板名称长度必须介于 1 和 64 之间")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}
	
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getNameValue() {
		return nameValue;
	}

	public void setNameValue(String nameValue) {
		this.nameValue = nameValue;
	}

	
	
}