package com.jxf.mem.entity;

import org.hibernate.validator.constraints.Length;

import com.jxf.svc.sys.crud.entity.CrudEntity;

/**
 * 发送短信记录表Entity
 * @author gaobo
 * @version 2019-03-19
 */
public class MmsSmsUserRecord extends CrudEntity<MmsSmsUserRecord> {
	
	private static final long serialVersionUID = 1L;
	/** 手机号 */
	private String usernameStr;		
	/** 短信通道类型0-催收 1 行业 */
	private String type;		
	/** 短信内容 */
	private String content;		
	
	public MmsSmsUserRecord() {
		super();
	}

	public MmsSmsUserRecord(Long id){
		super(id);
	}

	@Length(min=1, max=4, message="短信通道类型0-催收 1 行业长度必须介于 1 和 4 之间")
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	@Length(min=1, max=1024, message="短信内容长度必须介于 1 和 1024 之间")
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getUsernameStr() {
		return usernameStr;
	}

	public void setUsernameStr(String usernameStr) {
		this.usernameStr = usernameStr;
	}
	
}