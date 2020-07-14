package com.jxf.mem.entity;

import org.hibernate.validator.constraints.Length;

import com.jxf.svc.sys.crud.entity.CrudEntity;

/**
 * 发送站内信记录表Entity
 * @author gaobo
 * @version 2019-03-19
 */
public class MessageUserRecord extends CrudEntity<MessageUserRecord> {
	
	private static final long serialVersionUID = 1L;
	/** 手机号 */
	private String usernameStr;		
	/** 站内信 标题 */
	private String title;		
	/** 站内信内容 */
	private String content;		
	
	public MessageUserRecord() {
		super();
	}

	public MessageUserRecord(Long id){
		super(id);
	}
	
	@Length(min=1, max=255, message="站内信 标题长度必须介于 1 和 255 之间")
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	@Length(min=1, max=1024, message="站内信内容长度必须介于 1 和 1024 之间")
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