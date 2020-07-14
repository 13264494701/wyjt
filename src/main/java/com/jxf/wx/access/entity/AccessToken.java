package com.jxf.wx.access.entity;

import org.hibernate.validator.constraints.Length;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import javax.validation.constraints.NotNull;

import com.jxf.svc.sys.crud.entity.CrudEntity;

/**
 * access_tokenEntity
 * @author zhj
 * @version 2015-11-11
 */
public class AccessToken extends CrudEntity<AccessToken> {
	
	private static final long serialVersionUID = 1L;
	private String acctId;		// 公众号ID
	private String accessToken;		// access_token
	private Date expireTime;		// 失效时间
	private Date lastUpdateTime;		// 最后更新时间
	
	public AccessToken() {
		super();
	}

	public AccessToken(Long id){
		super(id);
	}

	@Length(min=1, max=64, message="公众号ID长度必须介于 1 和 64 之间")
	public String getAcctId() {
		return acctId;
	}

	public void setAcctId(String acctId) {
		this.acctId = acctId;
	}
	
	@Length(min=1, max=500, message="access_token长度必须介于 1 和 500 之间")
	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@NotNull(message="失效时间不能为空")
	public Date getExpireTime() {
		return expireTime;
	}

	public void setExpireTime(Date expireTime) {
		this.expireTime = expireTime;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@NotNull(message="最后更新时间不能为空")
	public Date getLastUpdateTime() {
		return lastUpdateTime;
	}

	public void setLastUpdateTime(Date lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}
	
}