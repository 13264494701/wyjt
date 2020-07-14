package com.jxf.svc.sys.app.entity;

import org.hibernate.validator.constraints.Length;

import com.jxf.svc.sys.crud.entity.CrudEntity;

/**
 * 应用曝光Entity
 * @author wo
 * @version 2019-07-09
 */
public class AppTouch extends CrudEntity<AppTouch> {
	
	private static final long serialVersionUID = 1L;
	/** 设备标识 */
	private String idfa;		
	/** 回调地址 */
	private String callbackUrl;		
	/** 最后登录IP */
	private String reqIp;		
	/** 是否激活 */
	private Boolean isInst;		
	
	public AppTouch() {
		super();
	}

	public AppTouch(Long id){
		super(id);
	}

	@Length(min=1, max=64, message="设备标识长度必须介于 1 和 64 之间")
	public String getIdfa() {
		return idfa;
	}

	public void setIdfa(String idfa) {
		this.idfa = idfa;
	}
	
	@Length(min=1, max=255, message="回调地址长度必须介于 1 和 255 之间")
	public String getCallbackUrl() {
		return callbackUrl;
	}

	public void setCallbackUrl(String callbackUrl) {
		this.callbackUrl = callbackUrl;
	}
	
	@Length(min=1, max=15, message="最后登录IP长度必须介于 1 和 15 之间")
	public String getReqIp() {
		return reqIp;
	}

	public void setReqIp(String reqIp) {
		this.reqIp = reqIp;
	}
	

	public Boolean getIsInst() {
		return isInst;
	}

	public void setIsInst(Boolean isInst) {
		this.isInst = isInst;
	}
	
}