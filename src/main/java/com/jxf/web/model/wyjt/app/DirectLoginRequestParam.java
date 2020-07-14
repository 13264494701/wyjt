package com.jxf.web.model.wyjt.app;

public class DirectLoginRequestParam {
	/** 当前APP对应的appid,SDK传入*/
	private String appId;
	/** 运营商token,SDK传入*/
	private String accessToken;
	/** 运营商，SDK传入*/
	private String telecom;
	/** UNIX时间戳，毫秒级，SDK传入*/
	private String timestamp;
	/** 随机数，SDK传入*/
	private String randoms;
	/** SDK版本号，SDK传入*/
	private String version;
	/** 设备型号，SDK传入*/
	private String device;
	/** 签名，SDK传入*/
	private String sign;
	
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
	
	/**同意时传 用户是否授权注册协议 默认false*/
	private Boolean isAgreeAuth = false;
	
	/** 同意时传 不用再调第三方*/
	private String phoneNo;
	/** 同意时传true 不用再调第三方*/
	private Boolean flag = false;

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public String getTelecom() {
		return telecom;
	}

	public void setTelecom(String telecom) {
		this.telecom = telecom;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public String getRandoms() {
		return randoms;
	}

	public void setRandoms(String randoms) {
		this.randoms = randoms;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getDevice() {
		return device;
	}

	public void setDevice(String device) {
		this.device = device;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
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

	public String getPhoneNo() {
		return phoneNo;
	}

	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}

	public Boolean getFlag() {
		return flag;
	}

	public void setFlag(Boolean flag) {
		this.flag = flag;
	}
	
	
}
