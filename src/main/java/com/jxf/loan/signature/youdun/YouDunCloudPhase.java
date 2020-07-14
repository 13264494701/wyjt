package com.jxf.loan.signature.youdun;


import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.jxf.rc.entity.RcQuota;
import com.jxf.svc.utils.Exceptions;
import com.jxf.svc.utils.MD5Code;

/**
    *           有盾云相接口
 * @author Administrator
 *
 */
public class YouDunCloudPhase {
	private static final Logger logger = LoggerFactory.getLogger(YouDunCloudPhase.class);
	/**
	 * @param member以后改成额度评估表
	 */
	public static String dataService(RcQuota rcQuota) {
		String id_card = rcQuota.getMember().getIdNo();
		String user_name = rcQuota.getMember().getName();
		String telephone = rcQuota.getMember().getUsername();
		String digestAlgorithm = "SHA-256";
		id_card = DigestUtil.digest(id_card.getBytes(), digestAlgorithm);
		user_name = DigestUtil.digest(user_name.getBytes(), digestAlgorithm);
		telephone = DigestUtil.digest(telephone.getBytes(), digestAlgorithm);
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("id_card", id_card);
		jsonObject.put("user_name",user_name);
		jsonObject.put("telephone", telephone);
		String sighNature = getMd5(jsonObject, YouDunConstant.SECURITY_KEY);
		CloseableHttpClient httpClient = HttpClients.createDefault();
		String outOrderId = String.valueOf(rcQuota.getOrderId());
		String url = String.format(YouDunConstant.CLOUDPHASE_REQUEST_URL, outOrderId,sighNature);
		StringEntity stringEntity = new StringEntity(jsonObject.toString(),"UTF-8");
		stringEntity.setContentType("application/json");
		stringEntity.setContentEncoding("UTF-8");
		HttpPost post = new HttpPost(url);
		post.setHeader("Connection","close");
		post.setEntity(stringEntity);
		try {
			CloseableHttpResponse resp = httpClient.execute(post);
			HttpEntity he = resp.getEntity();
			return EntityUtils.toString(he, "UTF-8");
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(e));
		} 
		return null;
	}
	
	
	/**
	 * @description 加签
	 * @param sighJson
	 * @param secretKey
	 * @return
	 */
	private static String getMd5(JSONObject sighJson,String secretKey) {
		String source = String.format("%s|%s", sighJson, secretKey);
		String sign = MD5Code.MD5Encode(source, "UTF-8");
		return sign;
	} 
	
	public static void main(String[] args) {
//		JSONObject jsonObject = new JSONObject();
//		String id_card = "410526199011067381";
//		String user_name = "郑淑玲";
//		String telephone = "15910691574";
//		String digestAlgorithm = "SHA-256";
//		id_card = DigestUtil.digest(id_card.getBytes(), digestAlgorithm);
//		user_name = DigestUtil.digest(user_name.getBytes(), digestAlgorithm);
//		telephone = DigestUtil.digest(telephone.getBytes(), digestAlgorithm);
//		jsonObject.put("id_card", id_card);
//		jsonObject.put("user_name",user_name);
//		jsonObject.put("telephone", telephone);
//		JSONObject response = dataService(jsonObject);
//		System.out.println(response.toString());
	}
	
}
