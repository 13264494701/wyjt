package com.jxf.rc.utils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.jxf.loan.signature.youdun.Base64;
import com.jxf.rc.entity.TianjiConstant;
import com.jxf.svc.utils.Exceptions;
import com.jxf.svc.utils.HttpUtils;
import com.jxf.svc.utils.StringUtils;

public class TianJiHttpUtils {
	private static final Logger logger = LoggerFactory.getLogger(TianJiHttpUtils.class);
	private String method;
	private String sign;
	private String signType = "RSA";
	private String bizData;
	private String appId = "2010475";
	private String version = "1.0";
	private String format = "json";
	private String timestamp;
	private String returnUrl;
	
	public JSONObject post() {
		//组装参数	
		Map<String, String> params = new HashMap<String, String>();
		params.put("method", this.method);
		params.put("sign_type", this.signType);
		params.put("biz_data", this.bizData);
		params.put("app_id", this.appId);
		params.put("version", this.version);
		params.put("format", this.format);
		params.put("timestamp", this.timestamp);
		params.put("sign", this.sign);
		if(!StringUtils.isBlank(this.returnUrl)) {
			params.put("returnUrl", this.returnUrl);
		}
		String logid = String.valueOf(System.currentTimeMillis());
		try {
			String response = HttpUtils.doPost(TianjiConstant.tj_gateway_url+System.currentTimeMillis()+logid,JSONObject.toJSONString(params).toString());
			if(StringUtils.isNotBlank(response)) {
				JSONObject resJson = JSONObject.parseObject(response);
				return resJson;
			}
		} catch (Exception e) {
			logger.error("天机请求异常：{}",Exceptions.getStackTraceAsString(e));
		}
		return null;
	}
	
	 public static String getSortParams(Map<String, String> params) {
	        SortedMap<String, String> map = new TreeMap<String, String>();
	        for (String key: params.keySet()) {
	            map.put(key, params.get(key));
	        }
	        
	        Set<String> keySet = map.keySet();
	        Iterator<String> iter = keySet.iterator();
	        String str = "";
	        while (iter.hasNext()) {
	            String key = iter.next();
	            String value = map.get(key);
	            str += key + "=" + value + "&";
	        }
	        if(str.length()>0){
	            str = str.substring(0, str.length()-1);
	        }
	        return str;
	    }
	
	
	public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
	}
	public String getSign() {
		return sign;
	}
	public void setSign() {
		Map<String, String> params = new HashMap<String, String>();
		params.put("method", this.method);
		params.put("sign_type", this.signType);
		params.put("biz_data", this.bizData);
		params.put("app_id", this.appId);
		params.put("version", this.version);
		params.put("format", this.format);
		params.put("timestamp", this.timestamp);
		String signSourceStr = TianJiHttpUtils.getSortParams(params);
		logger.debug("天机获取数据接口待签名数据:{}",signSourceStr);
		this.sign = Base64.SHA1WithRSASign(TianjiConstant.getPrivateKey(), signSourceStr); 
	}
	
	public String getSignType() {
		return signType;
	}

	public String getBizData() {
		return bizData;
	}

	public void setBizData(String bizData) {
		this.bizData = bizData;
	}

	public String getAppId() {
		return appId;
	}
	public String getVersion() {
		return version;
	}
	public String getFormat() {
		return format;
	}
	public String getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	public String getReturnUrl() {
		return returnUrl;
	}
	public void setReturnUrl(String returnUrl) {
		this.returnUrl = returnUrl;
	}
	
	
}
