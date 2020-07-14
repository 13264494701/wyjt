package com.jxf.mms.record.entity;

import org.hibernate.validator.constraints.Length;

import java.util.Date;

import javax.validation.constraints.NotNull;

import com.alibaba.fastjson.annotation.JSONField;
import com.jxf.svc.sys.crud.entity.CrudEntity;

/**
 * 
 * @类功能说明： 
 * @类修改者： 
 * @修改日期： 
 * @修改说明： 
 * @公司名称：北京金信服科技有限公司
 * @作者：HUOJIABAO 
 * @创建时间：2016年4月10日 下午11:55:29 
 * @版本：V1.0
 */
public class MmsEmailRecord extends CrudEntity<MmsEmailRecord> {
	
	private static final long serialVersionUID = 1L;
	private String tmplCode;		    //模板代码
	private String msgPriority;		  // 优先级
	private String senderName;		  // 发件人名称
	private String senderAddr;		 // 发件地址
	private String password;		 // 账户密码
	private String serverAddr;		// 服务器地址
	private String receiverAddr;   // 收件地址
	private String subject;		// 主题
	private String content;		// 内容
	private Date sendTime;		// 发送时间
	private String verifyCode;		// verify_code
	
	public MmsEmailRecord() {
		super();
	}

	public MmsEmailRecord(Long id){
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
	
	@Length(min=1, max=64, message="发件人名称长度必须介于 1 和 64 之间")
	public String getSenderName() {
		return senderName;
	}

	public void setSenderName(String senderName) {
		this.senderName = senderName;
	}
	
	@Length(min=1, max=64, message="发件地址长度必须介于 1 和 64 之间")
	public String getSenderAddr() {
		return senderAddr;
	}

	public void setSenderAddr(String senderAddr) {
		this.senderAddr = senderAddr;
	}
	@Length(min=1, max=64, message="账户密码长度必须介于 1 和 64 之间")
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	@Length(min=1, max=128, message="服务器地址长度必须介于 1 和 128 之间")
	public String getServerAddr() {
		return serverAddr;
	}

	public void setServerAddr(String serverAddr) {
		this.serverAddr = serverAddr;
	}
	@Length(min=1, max=64, message="收件地址长度必须介于 1 和 64 之间")
	public String getReceiverAddr() {
		return receiverAddr;
	}

	public void setReceiverAddr(String receiverAddr) {
		this.receiverAddr = receiverAddr;
	}
	
	@Length(min=1, max=128, message="主题长度必须介于 1 和 128 之间")
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
	
	@JSONField(format="yyyy-MM-dd HH:mm:ss")
	@NotNull(message="发送时间不能为空")
	public Date getSendTime() {
		return sendTime;
	}

	public void setSendTime(Date sendTime) {
		this.sendTime = sendTime;
	}
	
	@Length(min=0, max=10, message="verify_code长度必须介于 0 和 10 之间")
	public String getVerifyCode() {
		return verifyCode;
	}

	public void setVerifyCode(String verifyCode) {
		this.verifyCode = verifyCode;
	}




	
}