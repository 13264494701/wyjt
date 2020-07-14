package com.jxf.loan.signature.youdun;

import com.alibaba.fastjson.JSONObject;

import org.apache.http.HttpResponse;
import org.apache.http.ParseException;

import org.apache.http.client.methods.HttpPost;

import org.apache.http.entity.StringEntity;


import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;


import java.util.UUID;

public class CreateSeal {

    
    public static void CreateCommonSeal() {
    	String partnerCode = YouDunConstant.PARTNERCODE;
        String reqUrl = "https://esignature.udcredit.com/api/2.0/user/create-user-seal/partner-code/" + partnerCode;
        JSONObject reqJson = new JSONObject();
        JSONObject header = new JSONObject();
        JSONObject bodyJson = new JSONObject();
        
        String partnerOrderId = UUID.randomUUID().toString().replace("-", "");
        header.put("partnerOrderId", partnerOrderId);
        header.put("requestTime", System.currentTimeMillis());
        
        bodyJson.put("userCode", "UD461063540953120768");
        bodyJson.put("sealContext", "北京友信宝网络科技有限公司");
        bodyJson.put("sealText", " ");
        bodyJson.put("sealColor", 0);
        bodyJson.put("sealType", 2);
        
        reqJson.put("header", header);
        reqJson.put("body", bodyJson);
        System.out.println(reqJson.toString());
        String encryptResult =  reqJson.toJSONString();
        String signature = Base64.SHA1WithRSASign(YouDunConstant.RSA_PRIVATE_KEY, encryptResult);
        
        StringEntity stringEntity = new StringEntity(reqJson.toString(), "UTF-8");
        stringEntity.setContentEncoding("UTF-8");
        stringEntity.setContentType("application/json");
        
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(reqUrl);
        httpPost.setHeader("X-UD-Signature", signature);
        httpPost.setHeader("Content-Type", "application/json;charset=UTF-8");
        httpPost.setEntity(stringEntity);
        String resultString;
		try {
			HttpResponse response = httpClient.execute(httpPost);
			resultString = EntityUtils.toString(response.getEntity());
//			JSONObject responseJson = JSONObject.parseObject(resultString);
			System.out.println(resultString);
		} catch (ParseException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
       
    	
    }
}
