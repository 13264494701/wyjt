package com.jxf.wx.api;


import java.io.File;
import java.util.List;

import org.springframework.util.Assert;

import com.jxf.wx.api.response.BaseResponse;
import com.jxf.wx.api.response.BaseResponse.ResultType;
import com.jxf.wx.api.utils.CollectionUtil;
import com.jxf.wx.api.utils.NetWorkCenter;

/***
 * 
 * @类功能说明：API基类，提供一些通用方法 ,包含自动刷新token、通用get post请求等
 * @类修改者： 
 * @修改日期： 
 * @修改说明： 
 * @公司名称：北京金信服科技有限公司
 * @作者：Administrator 
 * @创建时间：2016年11月11日 下午3:44:03 
 * @版本：V1.0
 */
public abstract class BaseAPI {

    protected static final String BASE_API_URL = "https://api.weixin.qq.com/";

 //   protected final ApiConfig config;
    private String accessToken;
    
    private String appId;
    
    private String secret;

    /**
     * 构造方法，设置apiConfig
     *
     * @param config 微信API配置对象
     */
//    protected BaseAPI(ApiConfig config) {
//        this.config = config;
//    }
    
    protected BaseAPI() {
	
    }
    protected BaseAPI(String accessToken) {
		this.accessToken = accessToken;
	}
    public BaseAPI(String appId, String secret) {
		this.appId = appId;
		this.secret = secret;
	}
	protected BaseAPI(String accessToken, String appId, String secret) {
		this.accessToken = accessToken;
		this.appId = appId;
		this.secret = secret;
	}
    
    
    public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}


	public String getAppId() {
		return appId;
	}


	public void setAppId(String appId) {
		this.appId = appId;
	}


	public String getSecret() {
		return secret;
	}


	public void setSecret(String secret) {
		this.secret = secret;
	}


	/**
     * 通用post请求
     *
     * @param url  地址，其中token用#代替
     * @param json 参数，json格式
     * @return 请求结果
     */
    protected BaseResponse executePost(String url, String json) {
        return executePost(url, json, null);
    }

    /**
     * 通用post请求
     *
     * @param url  地址，其中token用#代替
     * @param json 参数，json格式
     * @param file 上传的文件
     * @return 请求结果
     */
    @SuppressWarnings("unchecked")
	protected BaseResponse executePost(String url, String json, File file) {
        BaseResponse response;
        Assert.notNull(url,"url不能为空");
        List<File> files = null;
        if (null != file) {
            files = CollectionUtil.newArrayList(file);
        }
        System.out.println(getAccessToken());
        System.out.println(this.accessToken);
        //需要传token
        if(url.contains("access_token=#")){
        	url = url.replace("#", this.accessToken);
        }
        response = NetWorkCenter.post(url, json, files);
        return response;
    }


    /**
     * 通用get请求
     *
     * @param url 地址，其中token用#代替
     * @return 请求结果
     */
    protected BaseResponse executeGet(String url) {
    	 Assert.notNull(url,"url不能为空");
        //需要传token
        if(url.contains("access_token=#")){
        	url = url.replace("#", this.accessToken);
        }
        BaseResponse response = NetWorkCenter.get(url);
        return response;
    }

    /**
     * 判断本次请求是否成功
     *
     * @param errCode 错误码
     * @return 是否成功
     */
    protected boolean isSuccess(String errCode) {
        return ResultType.SUCCESS.getCode().toString().equals(errCode);
    }
}
