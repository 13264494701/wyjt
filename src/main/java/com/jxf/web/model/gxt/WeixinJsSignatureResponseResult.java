package com.jxf.web.model.gxt;



/**
 * @作者: wo
 * @创建时间 :2019年4月26日 上午11:18:39
 * @功能说明: 
 */
public class WeixinJsSignatureResponseResult {
	
    private String appId;
    
	private String nonceStr;
	
    private long   timestamp;
        
    private String signature;


	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}
	
	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}


	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	public String getNonceStr() {
		return nonceStr;
	}

	public void setNonceStr(String nonceStr) {
		this.nonceStr = nonceStr;
	}



	
}
