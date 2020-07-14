package com.jxf.mms.record.entity;

import org.hibernate.validator.constraints.Length;

import java.util.Date;

import javax.validation.constraints.NotNull;

import com.alibaba.fastjson.annotation.JSONField;
import com.jxf.svc.sys.crud.entity.CrudEntity;

/**
 * 站内消息Entity
 * @author JINXINFU
 * @version 2016-04-08
 */
public class MmsInternalMsg extends CrudEntity<MmsInternalMsg> {
	
	private static final long serialVersionUID = 1L;
	private String tmplCode;		    //模板代码
	private String sender;		// 发送者
	private String receiver;		// 接收者
	private String subject;		// 主题
	private String content;		// 内容
	private String isread;		// 是否已读
	private String isflag;		// 是否标记重要
	private Date sendTime;		// 发送时间
	
	public MmsInternalMsg() {
		super();
	}

	public MmsInternalMsg(Long id){
		super(id);
	}
	
	public String getTmplCode() {
		return tmplCode;
	}

	public void setTmplCode(String tmplCode) {
		this.tmplCode = tmplCode;
	}
	
	@Length(min=1, max=16, message="发送者长度必须介于 1 和 16 之间")
	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}
	
	@Length(min=1, max=16, message="接收者长度必须介于 1 和 16 之间")
	public String getReceiver() {
		return receiver;
	}

	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}
	
	@Length(min=0, max=128, message="主题长度必须介于 0 和 128 之间")
	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}
	
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
	@Length(min=1, max=1, message="是否已读长度必须介于 1 和 1 之间")
	public String getIsread() {
		return isread;
	}

	public void setIsread(String isread) {
		this.isread = isread;
	}
	
	@Length(min=1, max=1, message="是否标记重要长度必须介于 1 和 1 之间")
	public String getIsflag() {
		return isflag;
	}

	public void setIsflag(String isflag) {
		this.isflag = isflag;
	}
	
	@JSONField(format="yyyy-MM-dd HH:mm:ss")
	@NotNull(message="发送时间不能为空")
	public Date getSendTime() {
		return sendTime;
	}

	public void setSendTime(Date sendTime) {
		this.sendTime = sendTime;
	}




	
}