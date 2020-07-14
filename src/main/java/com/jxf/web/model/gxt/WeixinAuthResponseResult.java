package com.jxf.web.model.gxt;



/**
 * @作者: wo
 * @创建时间 :2019年4月26日 上午11:18:39
 * @功能说明:
 */
public class WeixinAuthResponseResult {
	
    private String  openid;
    
    private String  unionid;
    
    private String  nickname;
    
    private String  headimgurl;

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
}
