package com.jxf.mms.sender.entity;

import org.hibernate.validator.constraints.Length;

import com.jxf.svc.sys.crud.entity.CrudEntity;

/**
 * 发件人列表Entity
 * @author JINXINFU
 * @version 2016-04-08
 */
public class MmsSender extends CrudEntity<MmsSender> {
	
	private static final long serialVersionUID = 1L;
	private String senderName;		// 发件人
	private String sendChl;		// 发送渠道
	private String displayName;		// 显示名称
	private String accountName;		// 账户名称
	private String password;		// 账户密码
	private String serverAddr;		// 服务器地址
	
	public MmsSender() {
		super();
	}

	public MmsSender(Long id){
		super(id);
	}
	
	public MmsSender(String displayName,String accountName,String password){
		this.displayName = displayName;
		this.accountName = accountName;
		this.password = password;
	}
	@Length(min=1, max=64, message="发件人长度必须介于 1 和 64 之间")
	public String getSenderName() {
		return senderName;
	}

	public void setSenderName(String senderName) {
		this.senderName = senderName;
	}
	
	@Length(min=1, max=3, message="发送渠道长度必须介于 1 和 3 之间")
	public String getSendChl() {
		return sendChl;
	}

	public void setSendChl(String sendChl) {
		this.sendChl = sendChl;
	}
	
	@Length(min=1, max=64, message="显示名称长度必须介于 1 和 64 之间")
	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	
	@Length(min=1, max=64, message="账户名称长度必须介于 1 和 64 之间")
	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
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
	
}