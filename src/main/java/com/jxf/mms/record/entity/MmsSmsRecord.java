package com.jxf.mms.record.entity;

import org.hibernate.validator.constraints.Length;

import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;
import com.jxf.svc.sys.crud.entity.CrudEntity;

/**
 * 短信记录Entity
 * @author JINXINFU
 * @version 2016-04-08
 */
public class MmsSmsRecord extends CrudEntity<MmsSmsRecord> {
	
	private static final long serialVersionUID = 1L;
	
	private String tmplCode;		    //模板代码
	private String msgPriority;		// 优先级
	private String phoneNo;		// 手机号码
	private String content;		// 短信内容
	private String verifyCode;		// verify_code
	private Date sendTime;		// 发送时间
	private Date beginTime;		// 起始时间
	private Date endTime;		// 结束时间
	
	public MmsSmsRecord() {
		super();
	}

	public MmsSmsRecord(Long id){
		super(id);
	}
	
	public String getTmplCode() {
		return tmplCode;
	}

	public void setTmplCode(String tmplCode) {
		this.tmplCode = tmplCode;
	}
	
	@Length(min=1, max=3, message="优先级长度必须介于 1 和 3 之间")
	public String getMsgPriority() {
		return msgPriority;
	}

	public void setMsgPriority(String msgPriority) {
		this.msgPriority = msgPriority;
	}
	
	@Length(min=1, max=11, message="手机号码长度必须介于 1 和 11 之间")
	public String getPhoneNo() {
		return phoneNo;
	}

	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}
	
	@Length(min=0, max=256, message="短信内容长度必须介于 0 和 256 之间")
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
	@Length(min=0, max=10, message="verify_code长度必须介于 0 和 10 之间")
	public String getVerifyCode() {
		return verifyCode;
	}

	public void setVerifyCode(String verifyCode) {
		this.verifyCode = verifyCode;
	}
	
	@JSONField(format="yyyy-MM-dd HH:mm:ss")
	public Date getSendTime() {
		return sendTime;
	}

	public void setSendTime(Date sendTime) {
		this.sendTime = sendTime;
	}

	public Date getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(Date beginTime) {
		this.beginTime = beginTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}






	
}