package com.jxf.web.model.wyjt.app;

import com.jxf.web.model.wyjt.app.member.MemberInfoResponseResult;

public class WechatLoginResponseResult {
    /**微信登录结果*/
    private WechatLoginResultResponseResult wechatLoginResultResponseResult;
    /**登录成功信息*/
    private MemberInfoResponseResult memberInfoResponseResult;

    public WechatLoginResultResponseResult getWechatLoginResultResponseResult() {
        return wechatLoginResultResponseResult;
    }

    public void setWechatLoginResultResponseResult(WechatLoginResultResponseResult wechatLoginResultResponseResult) {
        this.wechatLoginResultResponseResult = wechatLoginResultResponseResult;
    }

    public MemberInfoResponseResult getMemberInfoResponseResult() {
        return memberInfoResponseResult;
    }

    public void setMemberInfoResponseResult(MemberInfoResponseResult memberInfoResponseResult) {
        this.memberInfoResponseResult = memberInfoResponseResult;
    }

}