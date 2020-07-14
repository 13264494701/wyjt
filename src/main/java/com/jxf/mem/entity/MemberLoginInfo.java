package com.jxf.mem.entity;

import org.hibernate.validator.constraints.Length;

import com.jxf.svc.sys.crud.entity.CrudEntity;

/**
 * 会员登陆信息Entity
 * @author gaobo
 * @version 2019-05-31
 */
public class MemberLoginInfo extends CrudEntity<MemberLoginInfo> {
	
	private static final long serialVersionUID = 1L;
	/** 会员编号 */
	private Member member;		
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
	/** 会员昵称 */
	private String deviceToken;		
	/** 渠道编号 */
	private String channeId;		
	/** 推送码 */
	private String pushToken;		
	/** 最后登录IP */
	private String loginIp;		
	
	public MemberLoginInfo() {
		super();
	}

	public MemberLoginInfo(Long id){
		super(id);
	}

	@Length(min=0, max=4, message="系统类型长度必须介于 0 和 4 之间")
	public String getOsType() {
		return osType;
	}

	public void setOsType(String osType) {
		this.osType = osType;
	}
	
	@Length(min=0, max=16, message="系统版本长度必须介于 0 和 16 之间")
	public String getOsVersion() {
		return osVersion;
	}

	public void setOsVersion(String osVersion) {
		this.osVersion = osVersion;
	}
	
	@Length(min=0, max=16, message="应用版本长度必须介于 0 和 16 之间")
	public String getAppVersion() {
		return appVersion;
	}

	public void setAppVersion(String appVersion) {
		this.appVersion = appVersion;
	}
	
	@Length(min=0, max=16, message="ak长度必须介于 0 和 16 之间")
	public String getAk() {
		return ak;
	}

	public void setAk(String ak) {
		this.ak = ak;
	}
	
	@Length(min=0, max=64, message="设备型号长度必须介于 0 和 64 之间")
	public String getDeviceModel() {
		return deviceModel;
	}

	public void setDeviceModel(String deviceModel) {
		this.deviceModel = deviceModel;
	}
	
	@Length(min=0, max=64, message="会员昵称长度必须介于 0 和 64 之间")
	public String getDeviceToken() {
		return deviceToken;
	}

	public void setDeviceToken(String deviceToken) {
		this.deviceToken = deviceToken;
	}
	
	@Length(min=0, max=32, message="渠道编号长度必须介于 0 和 32 之间")
	public String getChanneId() {
		return channeId;
	}

	public void setChanneId(String channeId) {
		this.channeId = channeId;
	}
	
	@Length(min=0, max=255, message="推送码长度必须介于 0 和 255 之间")
	public String getPushToken() {
		return pushToken;
	}

	public void setPushToken(String pushToken) {
		this.pushToken = pushToken;
	}
	
	@Length(min=0, max=15, message="最后登录IP长度必须介于 0 和 15 之间")
	public String getLoginIp() {
		return loginIp;
	}

	public void setLoginIp(String loginIp) {
		this.loginIp = loginIp;
	}

	public Member getMember() {
		return member;
	}

	public void setMember(Member member) {
		this.member = member;
	}
	
}