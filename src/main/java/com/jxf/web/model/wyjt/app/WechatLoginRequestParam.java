package com.jxf.web.model.wyjt.app;

public class WechatLoginRequestParam {

    private String openid;
    private String unionid;
    private String nickname;
    private String headimgurl;

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
	
	/** 用户是否授权注册协议 默认false*/
	private Boolean isAgreeAuth = false;
	
	
    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getUnionid() {
        return unionid;
    }

    public void setUnionid(String unionid) {
        this.unionid = unionid;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getHeadimgurl() {
        return headimgurl;
    }

    public void setHeadimgurl(String headimgurl) {
        this.headimgurl = headimgurl;
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