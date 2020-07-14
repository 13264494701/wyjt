package com.jxf.web.model.wyjt.app;

public class WechatLoginResultResponseResult {
    /**微信登录结果，是否绑定了借条用户，success,fail*/
    private String loginResult;
    /**微信信息id*/
    private String wxUserInfoId;

    public String getLoginResult() {
        return loginResult;
    }

    public void setLoginResult(String loginResult) {
        this.loginResult = loginResult;
    }

    public String getWxUserInfoId() {
        return wxUserInfoId;
    }

    public void setWxUserInfoId(String wxUserInfoId) {
        this.wxUserInfoId = wxUserInfoId;
    }
}