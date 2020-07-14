package com.jxf.svc.sys.app.entity;

import org.hibernate.validator.constraints.Length;

import com.jxf.svc.sys.crud.entity.CrudEntity;

/**
 * 应用激活Entity
 * @author wo
 * @version 2019-07-09
 */
public class AppInst extends CrudEntity<AppInst> {
	
	private static final long serialVersionUID = 1L;
	/** 系统类型 */
	private String osType;		
	/** 系统版本 */
	private String osVersion;		
	/** 应用版本 */
	private String appVersion;		
	/** ak */
	private String ak;		
	/** 设备型号 */
	private String deviceModel;		
	/** 设备号 */
	private String deviceToken;		
	/** 推送码 */
	private String pushToken;		
	/** 渠道编号 */
	private String channeId;		
	/** 登录IP */
	private String loginIp;	
	
	public AppInst() {
		super();
	}

	public AppInst(Long id){
		super(id);
	}

	@Length(min=1, max=16, message="系统类型长度必须介于 1 和 16 之间")
	public String getOsType() {
		return osType;
	}

	public void setOsType(String osType) {
		this.osType = osType;
	}
	
	@Length(min=1, max=16, message="系统版本长度必须介于 1 和 16 之间")
	public String getOsVersion() {
		return osVersion;
	}

	public void setOsVersion(String osVersion) {
		this.osVersion = osVersion;
	}
	
	@Length(min=1, max=16, message="应用版本长度必须介于 1 和 16 之间")
	public String getAppVersion() {
		return appVersion;
	}

	public void setAppVersion(String appVersion) {
		this.appVersion = appVersion;
	}
	
	@Length(min=1, max=16, message="ak长度必须介于 1 和 16 之间")
	public String getAk() {
		return ak;
	}

	public void setAk(String ak) {
		this.ak = ak;
	}
	
	@Length(min=1, max=64, message="设备型号长度必须介于 1 和 64 之间")
	public String getDeviceModel() {
		return deviceModel;
	}

	public void setDeviceModel(String deviceModel) {
		this.deviceModel = deviceModel;
	}
	
	@Length(min=1, max=64, message="设备号长度必须介于 1 和 64 之间")
	public String getDeviceToken() {
		return deviceToken;
	}

	public void setDeviceToken(String deviceToken) {
		this.deviceToken = deviceToken;
	}
	
	@Length(min=1, max=255, message="推送码长度必须介于 1 和 255 之间")
	public String getPushToken() {
		return pushToken;
	}

	public void setPushToken(String pushToken) {
		this.pushToken = pushToken;
	}
	
	@Length(min=1, max=32, message="渠道编号长度必须介于 1 和 32 之间")
	public String getChanneId() {
		return channeId;
	}

	public void setChanneId(String channeId) {
		this.channeId = channeId;
	}
	
	@Length(min=1, max=15, message="最后登录IP长度必须介于 1 和 15 之间")
	public String getLoginIp() {
		return loginIp;
	}

	public void setLoginIp(String loginIp) {
		this.loginIp = loginIp;
	}
	
}