package com.jxf.wx.api.response;

import com.alibaba.fastjson.annotation.JSONField;

/***
 * 
 * @类功能说明： Oauth授权获取token接口响应对象
 * @类修改者： 
 * @修改日期： 
 * @修改说明： 
 * @公司名称：北京金信服科技有限公司
 * @作者：Administrator 
 * @创建时间：2016年11月11日 下午9:07:40 
 * @版本：V1.0
 */
public class OauthGetTokenResponse extends GetTokenResponse {


	private static final long serialVersionUID = 1L;

	@JSONField(name = "refresh_token")
    private String refreshToken;

    private String openid;

    private String scope;

    private String unionid;

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getUnionid() {
        return unionid;
    }

    public void setUnionid(String unionid) {
        this.unionid = unionid;
    }
}
