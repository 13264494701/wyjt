package com.jxf.web.model.wyjt.app;

public class WechatBindRequestParam {
    private String wxUserInfoId;
    private String phoneNo;
    private String smsCode;
    private String nickname;
    private String headimgurl;

    public String getWxUserInfoId() {
        return wxUserInfoId;
    }

    public void setWxUserInfoId(String wxUserInfoId) {
        this.wxUserInfoId = wxUserInfoId;
    }

	public String getPhoneNo() {
		return phoneNo;
	}

	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}

	public String getSmsCode() {
		return smsCode;
	}

	public void setSmsCode(String smsCode) {
		this.smsCode = smsCode;
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
}