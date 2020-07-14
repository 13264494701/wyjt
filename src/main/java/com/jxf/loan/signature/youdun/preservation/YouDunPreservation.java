package com.jxf.loan.signature.youdun.preservation;

import java.io.*;
import java.nio.charset.Charset;
import java.util.UUID;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.jxf.loan.signature.youdun.Base64;
import com.jxf.loan.signature.youdun.YouDunConstant;
import com.jxf.svc.utils.Exceptions;

/**
 * 有盾业务保全接口
 * @author Richard.Su
 *
 */
public class YouDunPreservation {
	
	private static final Logger logger = LoggerFactory.getLogger(YouDunPreservation.class);
	/**
	 * 上传保全数据
	 * @param  UdPreservationUploadInfo 
	 * @return JSONObject 
	 */
	public static JSONObject uploadInfo(UdPreservationInfo preservationInfo) {
		JSONObject info = PreservationInfoBuilderFactory.getInstance().buildPreservationInfo(preservationInfo.getPreservationBuilderData(), preservationInfo.getNodeType());
		
		JSONObject contentHeader = new JSONObject();
		contentHeader.put("partnerOrderId", preservationInfo.getPartnerOrderId());
		contentHeader.put("requestTime", System.currentTimeMillis());

		JSONObject contentBody = new JSONObject();
		contentBody.put("proofChainId", preservationInfo.getProofChainId());
		contentBody.put("infoType", String.valueOf(preservationInfo.getNodeType().ordinal()+1));
		contentBody.put("parentOrderId", preservationInfo.getParentOrderId());
		contentBody.put(preservationInfo.getNodeType().name(), info);
		
		JSONObject contentJson = new JSONObject();
		contentJson.put("header", contentHeader);
		contentJson.put("body", contentBody);
		return httpPost(contentJson);
	}
	
	 /**
     * 发送post请求
     * @param contentJson  请求报文
     */
    private static JSONObject httpPost(JSONObject contentJson) {
    	String sign = Base64.SHA1WithRSASign(YouDunConstant.RSA_PRIVATE_KEY,contentJson.toString());
    	
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(YouDunConstant.PRESERVATION_REQUEST_URL);
        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(10000).setSocketTimeout(10000).build();
        httpPost.setConfig(requestConfig);
        httpPost.setHeader("X-UD-Signature", sign);
        httpPost.setHeader("ContentType","multipart/form-data;charset=UTF-8");
        MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();
        ContentType contentType = ContentType.create("application/json", Charset.forName("UTF-8"));
        multipartEntityBuilder.addTextBody("content", contentJson.toString(), contentType);
        httpPost.setEntity(multipartEntityBuilder.build());
        JSONObject httpResultJson = null;
        try {
            HttpResponse resp = httpClient.execute(httpPost);
            HttpEntity entity = resp.getEntity();
            String response = EntityUtils.toString(entity);
            httpResultJson = JSONObject.parseObject(response);
        } catch (IOException e) {
        	logger.error("有盾业务保全请求异常： {}",Exceptions.getStackTraceAsString(e));
        }
        return httpResultJson;
    }
    
    
	public static void main(String[] args) {
		String partnerOrderId = UUID.randomUUID().toString().replace("-", ""); 
		logger.error(partnerOrderId);
		IdentifyInfo memberIdentifyInfo = new IdentifyInfo();
		memberIdentifyInfo.setUserName("suhuimin");
		System.out.println(JSONObject.toJSON(memberIdentifyInfo));
	}

}
