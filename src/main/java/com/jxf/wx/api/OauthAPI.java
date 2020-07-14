package com.jxf.wx.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import com.alibaba.fastjson.JSON;
import com.jxf.svc.utils.StringUtils;
import com.jxf.wx.api.response.BaseResponse;
import com.jxf.wx.api.response.GetUserInfoResponse;
import com.jxf.wx.api.response.OauthGetTokenResponse;


import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * 网页授权API
 *
 * @author peiyu
 */
public class OauthAPI extends BaseAPI {
	
	public enum OauthScope {

	    /**
	     * 仅仅获取用户openid
	     */
	    SNSAPI_BASE("snsapi_base"),

	    /**
	     * 获取用户完整信息
	     */
	    SNSAPI_USERINFO("snsapi_userinfo");

	    String value;

	    OauthScope(String value) {
	        this.value = value;
	    }

	    @Override
	    public String toString() {
	        return this.value;
	    }
	}

	private static final Logger LOG = LoggerFactory.getLogger(OauthAPI.class);
    
    public OauthAPI() {
	}
    public OauthAPI(String appId, String secret) {
    	super(appId, secret);
	}
    
    /**
     * 生成回调url，这个结果要求用户在微信中打开，即可获得token，并指向redirectUrl
     *
     * @param redirectUrl 用户自己设置的回调地址
     * @param scope       授权作用域
     * @param state       用户自带参数
     * @return 回调url，用户在微信中打开即可开始授权
     */
    public String getOauthPageUrl(String redirectUrl, OauthScope scope, String state) {
       
        Assert.notNull(redirectUrl,"");
        Assert.notNull(scope,"");
        String userState = StringUtils.isBlank(state) ? "STATE" : state;
        String url = null;
        try {
            url = URLEncoder.encode(redirectUrl, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            LOG.error("异常", e);
        }
        StringBuilder stringBuilder = new StringBuilder("https://open.weixin.qq.com/connect/oauth2/authorize?");
        stringBuilder.append("appid=").append(this.getAppId())
                .append("&redirect_uri=").append(url)
                .append("&response_type=code&scope=").append(scope.toString())
                .append("&state=")
                .append(userState)
                .append("#wechat_redirect");
        return stringBuilder.toString();
    }

    /**
     * 用户同意授权后在回调url中会得到code，调用此方法用code换token以及openid，所以如果仅仅是授权openid，到这步就结束了
     *
     * @param code 授权后得到的code
     * @return token对象
     */
    public OauthGetTokenResponse getToken(String code) {
        Assert.notNull(code,"");
        OauthGetTokenResponse response = null;
        String url = BASE_API_URL + "sns/oauth2/access_token?appid=" + this.getAppId() + "&secret=" + this.getSecret() + "&code=" + code + "&grant_type=authorization_code";
        BaseResponse r = executeGet(url);
        if(isSuccess(r.getErrcode())) {
        	response = JSON.parseObject(r.getErrmsg(), OauthGetTokenResponse.class);
        }else {
        	response = new OauthGetTokenResponse();
        }
        response.setErrcode(r.getErrcode());
        response.setErrmsg(r.getErrmsg());
        return response;
    }

    /**
     * 刷新token
     *
     * @param refreshToken token对象中会包含refreshToken字段，通过这个字段再次刷新token
     * @return 全新的token对象
     */
    public OauthGetTokenResponse refreshToken(String refreshToken) {
        Assert.notNull(refreshToken,"");
        OauthGetTokenResponse response = null;
        String url = BASE_API_URL + "sns/oauth2/refresh_token?appid=" + this.getAppId() + "&grant_type=refresh_token&refresh_token=" + refreshToken;
        BaseResponse r = executeGet(url);
        if(isSuccess(r.getErrcode())) {
        	response = JSON.parseObject(r.getErrmsg(), OauthGetTokenResponse.class);
        }else {
        	response = new OauthGetTokenResponse();
        }
        response.setErrcode(r.getErrcode());
        response.setErrmsg(r.getErrmsg());
        return response;
    }

    /**
     * 获取用户详细信息
     *
     * @param token  token
     * @param openid 用户openid
     * @return 用户信息对象
     */
    public GetUserInfoResponse getUserInfo(String token, String openid) {
        Assert.notNull(token,"");
        Assert.notNull(openid,"");
        GetUserInfoResponse response = null;
        String url = BASE_API_URL + "sns/userinfo?access_token=" + token + "&openid=" + openid + "&lang=zh_CN";
        BaseResponse r = executeGet(url);
        if(isSuccess(r.getErrcode())) {
        	response = JSON.parseObject(r.getErrmsg(), GetUserInfoResponse.class);
        }else {
        	response = new GetUserInfoResponse();
        }
        response.setErrcode(r.getErrcode());
        response.setErrmsg(r.getErrmsg());
        return response;
    }

    /**
     * 校验token是否合法有效
     *
     * @param token  token
     * @param openid 用户openid
     * @return 是否合法有效
     */
    public boolean validToken(String token, String openid) {
        Assert.notNull(token,"");
        Assert.notNull(openid,"");
        String url = BASE_API_URL + "sns/auth?access_token=" + token + "&openid=" + openid;
        BaseResponse r = executeGet(url);
        return isSuccess(r.getErrcode());
    }
}
