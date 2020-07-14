package com.jxf.partner.udcredit;

import java.io.IOException;
import java.util.UUID;

import org.apache.commons.codec.binary.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.jxf.loan.signature.youdun.YouDunConstant;
import com.jxf.svc.model.HandleRsp;
import com.jxf.svc.security.MD5Utils;
import com.jxf.svc.utils.Exceptions;


public class UdcreditUtils {

	private static final Logger logger = LoggerFactory.getLogger(UdcreditUtils.class);


    /**
     * 产品编号
     */
    final static String product_code = "O1001S0401";

    /**
     * 接口调用地址
     */
    static final String dataservice_url = "https://api4.udcredit.com/dsp-front/4.1/dsp-front/default/pubkey/%s/product_code/%s/out_order_id/%s/signature/%s";
	

    private static final CloseableHttpClient closeableHttpClient = HttpClients.createDefault();
    
	/***
	 * 银行卡四要素验证
	 * 
	 * @param cardNo
	 * @param name
	 * @param idNo
	 * @param phoneNo
	 * @return
	 */
	public static HandleRsp checkCard4Factors(String cardNo, String name, String idNo, String phoneNo) {

		JSONObject jsonObject = new JSONObject();
		jsonObject.put("id_name", name);
		jsonObject.put("id_no", idNo);
		jsonObject.put("bank_card_no", cardNo);
		jsonObject.put("mobile", phoneNo);
        
		String out_order_id = UUID.randomUUID().toString().replace("-", "");
		String signature = MD5Utils.EncoderByMd5(String.format("%s|%s", jsonObject, YouDunConstant.SECURITY_KEY));
		String reqUrl = String.format(dataservice_url,YouDunConstant.PUB_KEY,product_code,out_order_id,signature);
		
        StringEntity stringEntity = new StringEntity(jsonObject.toJSONString(),"UTF-8");
        stringEntity.setContentEncoding("UTF-8");
        stringEntity.setContentType("application/json");
        HttpPost httpPost = new HttpPost(reqUrl);
        httpPost.setHeader("Connection","close");
        httpPost.setEntity(stringEntity);

		try {
			HttpResponse resp = closeableHttpClient.execute(httpPost);
	        String respContent = EntityUtils.toString(resp.getEntity(), "UTF-8");
	        logger.info(respContent);
	        JSONObject rspObj = JSONObject.parseObject(respContent);
	        JSONObject bodyJson = rspObj.getJSONObject("body");
	        String status = bodyJson.getString("status");
	        if(StringUtils.equals(status, "1")) {
	        	return HandleRsp.success(bodyJson.getString("message"));
	        }else {
	        	return HandleRsp.fail(bodyJson.getString("message"));
	        }
		} catch (IOException e) {
			logger.error(Exceptions.getStackTraceAsString(e));
			return HandleRsp.fail("认证失败,请求有盾接口失败");
		}
	}
	
    public static void main(String args[])
    {
    	HandleRsp rsp = checkCard4Factors("6212264402064539574", "宋杨", "510722198806198793", "13880168325");
    	logger.info(JSONObject.toJSONString(rsp));
    }
}
