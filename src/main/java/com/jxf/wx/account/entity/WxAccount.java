package com.jxf.wx.account.entity;

import org.hibernate.validator.constraints.Length;

import com.jxf.svc.sys.crud.entity.CrudEntity;

/**
 * 微信公众号账号Entity
 * @author gaobo
 * @version 2018-10-16
 */
public class WxAccount extends CrudEntity<WxAccount> {
	
	private static final long serialVersionUID = 1L;
	/** 账号代码  */
	private String code;		
	/** 公众号名称 */
	private String name;		
	/** 公众号类型 */
	private String type;		
	/** 公众号appId */
	private String appid;		
	/** 公众号秘钥 */
	private String secret;		
	/** 账号状态 */
	private String status;		
	
	public WxAccount() {
		super();
	}

	public WxAccount(Long id){
		super(id);
	}

	@Length(min=1, max=10, message="code长度必须介于 1 和 10 之间")
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	
	@Length(min=1, max=64, message="公众号名称长度必须介于 1 和 64 之间")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@Length(min=1, max=4, message="公众号类型长度必须介于 1 和 4 之间")
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	@Length(min=1, max=64, message="公众号appId长度必须介于 1 和 64 之间")
	public String getAppid() {
		return appid;
	}

	public void setAppid(String appid) {
		this.appid = appid;
	}
	
	@Length(min=1, max=127, message="公众号secret长度必须介于 1 和 127 之间")
	public String getSecret() {
		return secret;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}
	
	@Length(min=1, max=4, message="账号状态长度必须介于 1 和 4 之间")
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
}