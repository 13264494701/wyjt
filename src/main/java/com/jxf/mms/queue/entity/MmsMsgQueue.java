package com.jxf.mms.queue.entity;

import org.hibernate.validator.constraints.Length;

import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;
import com.jxf.mms.consts.MmsConstant;
import com.jxf.svc.sys.crud.entity.CrudEntity;

/**
 * 消息队列Entity
 * @author JINXINFU
 * @version 2016-04-08
 */
public class MmsMsgQueue extends CrudEntity<MmsMsgQueue> {
	
	private static final long serialVersionUID = 1L;
	private String msgNo;		// 消息序号
	private MmsConstant.MsgType type;		// 消息类型
	private String sendChl;		// 发送方式
	private String sender;		// 发送者
	private String receiver;		// 接收者
	private String subject;		// 主题
	private String content;		// 内容
	private String msgSts;		// 发送状态
	private Date sendTime;		// 发送时间
	
	public MmsMsgQueue() {
		super();
	}

	public MmsMsgQueue(Long id){
		super(id);
	}

	@Length(min=1, max=24, message="消息序号长度必须介于 1 和 24 之间")
	public String getMsgNo() {
		return msgNo;
	}

	public void setMsgNo(String msgNo) {
		this.msgNo = msgNo;
	}
	
	public MmsConstant.MsgType getType() {
		return type;
	}

	public void setType(MmsConstant.MsgType type) {
		this.type = type;
	}
	
	@Length(min=1, max=3, message="发送方式长度必须介于 1 和 3 之间")
	public String getSendChl() {
		return sendChl;
	}

	public void setSendChl(String sendChl) {
		this.sendChl = sendChl;
	}
	
	@Length(min=1, max=64, message="发送者长度必须介于 1 和 64 之间")
	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}
	
	@Length(min=0, max=64, message="接收者长度必须介于 0 和 64 之间")
	public String getReceiver() {
		return receiver;
	}

	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}
	
	@Length(min=0, max=256, message="主题长度必须介于 0 和 256 之间")
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
	
	@Length(min=1, max=3, message="发送状态长度必须介于 1 和 3 之间")
	public String getMsgSts() {
		return msgSts;
	}

	public void setMsgSts(String msgSts) {
		this.msgSts = msgSts;
	}
	
	@JSONField(format="yyyy-MM-dd HH:mm:ss")
	public Date getSendTime() {
		return sendTime;
	}

	public void setSendTime(Date sendTime) {
		this.sendTime = sendTime;
	}


	
}