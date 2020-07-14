package com.jxf.web.model.wyjt.app;



public class LoginSubmitRequestParam  {

	/** 用户手机号 */
	private String phoneNo;

	/** 用户登录密码 */
	private String password;
	
	/** 用户是否授权注册协议 默认false*/
	private Boolean isAgreeAuth = false;
	
	/** 设备操作系统类型 */
	private String osType; //ios、android

	/** 设备操作系统版本 */
	private String osVersion;//7.0
		
	/** APP版本 */
	private String appVersion;//3.43

	/** APP类型 */
	private String ak;//
	
	/** 设备型号 */
	private String deviceModel;//HUAWEI NXT-AL10
	
	/** 设备唯一标示(ios idfa ,android imei) */
	private String deviceToken;
	
	/** 百度统计渠道号 */
	private String channeId;
	
	
	

	public String getPhoneNo() {
		return phoneNo;
	}

	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getOsType() {
		return osType;
	}

	public void setOsType(String osType) {
		this.osType = osType;
	}

	public String getOsVersion() {
		return osVersion;
	}

	public void setOsVersion(String osVersion) {
		this.osVersion = osVersion;
	}

	public String getAppVersion() {
		return appVersion;
	}

	public void setAppVersion(String appVersion) {
		this.appVersion = appVersion;
	}

	public String getAk() {
		return ak;
	}

	public void setAk(String ak) {
		this.ak = ak;
	}

	public String getDeviceModel() {
		return deviceModel;
	}

	public void setDeviceModel(String deviceModel) {
		this.deviceModel = deviceModel;
	}

	public String getDeviceToken() {
		return deviceToken;
	}

	public void setDeviceToken(String deviceToken) {
		this.deviceToken = deviceToken;
	}

	public String getChanneId() {
		return channeId;
	}

	public void setChanneId(String channeId) {
		this.channeId = channeId;
	}

	public Boolean getIsAgreeAuth() {
		return isAgreeAuth;
	}

	public void setIsAgreeAuth(Boolean isAgreeAuth) {
		this.isAgreeAuth = isAgreeAuth;
	}

}
