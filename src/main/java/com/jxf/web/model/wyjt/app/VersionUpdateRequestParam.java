package com.jxf.web.model.wyjt.app;


/***
 * 系统版本更新检查
 * @author wo
 *
 */
public class VersionUpdateRequestParam  {

	/** 设备操作系统类型 */
	private String osType; //ios、android

	/** 设备操作系统版本 */
	private String osVersion;//7.0
		
	/** APP版本 */
	private String appVersion;//3.43

	/** APP类型 */
	private String ak;//

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

	


}

