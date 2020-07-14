package com.jxf.ufang.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.alibaba.fastjson.JSONObject;
import com.jxf.svc.config.Global;

public class LoanAnalysisUtils {

    static final String CHASET_UTF_8 = "UTF-8";
    private static char[] hexDigits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
    /**
     * 	接口调用地址
     */
    static final String dataservice_url = "https://api4.udcredit.com/dsp-front/4.1/dsp-front/default/pubkey/%s/product_code/%s/out_order_id/%s/signature/%s";

    /**
     * 	商户pub_key(下发到商户联系人邮箱)
     */
    final static String pub_key = Global.getConfig("ud.pub_key");

    /**
     * 	商户secretkey(下发到商户联系人邮箱)
     */
    final static String secretkey = Global.getConfig("ud.security_key");

    /**
     * 	请商户按照实际接入产品传入对应的产品编号
     */
    final static String product_code = "Y1001005";

    /**
     * 	商户订单号：由商户自定义传入的唯一且不大于32位的字符串
     */
    String out_order_id = getStringDate(new Date());

    /**
     * 	加签
     * @param jsonObject
     * @param secretkey
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String getMd5(JSONObject jsonObject, String secretkey) throws UnsupportedEncodingException {
        String sign = String.format("%s|%s", jsonObject, secretkey);
        return MD5Encrpytion(sign.getBytes("UTF-8"));
    }

    /**
     * 	注：demo中调用方法的参数均为范例，不具有实际意义，请商户自行替换
     * 	测试接口的入参信息要真实有效
	 * 	测试类
     */
//    @Test
//    public void test() throws Exception {
//        JSONObject jsonObject = new JSONObject();
//        jsonObject.put("id_no","210381199404101612");
////        InputStream inputStream = new FileInputStream("D:/123.jpg");
////        byte[] data = new byte[inputStream.available()];
////        inputStream.read(data);
////        jsonObject.put("photo",Base64.getEncoder().encodeToString(data));
//        System.out.println(jsonObject);
//        dataservice(jsonObject);
//    }
    
    public JSONObject getResult(String idNo) throws Exception {
    	
    	JSONObject jsonObject = new JSONObject();
        jsonObject.put("id_no",idNo);
    	
    	return dataservice(jsonObject);
    }
	
	
    public JSONObject dataservice(JSONObject jsonObject) throws Exception {
        String signature = getMd5(jsonObject, secretkey);
        String url = String.format(dataservice_url,pub_key,product_code,out_order_id,signature);
        JSONObject response = doHttpRequest(url, jsonObject);
        return response;
    }
    /**
     * MD5加密
     * @param source
     * @return
     */
    public static final String MD5Encrpytion(byte[] source) {
        try {
            MessageDigest mdTemp = MessageDigest.getInstance("MD5");
            mdTemp.update(source);
            byte[] md = mdTemp.digest();
            int j = md.length;
            char[] str = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; ++i) {
                byte byte0 = md[i];
                str[(k++)] = hexDigits[(byte0 >>> 4 & 0xF)];
                str[(k++)] = hexDigits[(byte0 & 0xF)];
            }
            for (int m = 0; m < str.length; ++m) {
                if ((str[m] >= 'a') && (str[m] <= 'z')) {
                    str[m] = (char) (str[m] - ' ');
                }
            }
            return new String(str);
        } catch (Exception e) {
        }
        return null;
    }

    /**
     * 	格式化日期字符串 yyyyMMddHHmmss
     * @param date
     * @return
     */
    public static String getStringDate(Date date){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        return simpleDateFormat.format(date);
    }


    private static final CloseableHttpClient closeableHttpClient = HttpClients.createDefault();
    /**
     * http请求
     * @param url
     * @param jsonObject
     * @return
     * @throws Exception
     */
    private static JSONObject doHttpRequest(String url,JSONObject jsonObject) throws Exception{
        //设置传入参数
        StringEntity stringEntity = new StringEntity(jsonObject.toJSONString(),CHASET_UTF_8);
        stringEntity.setContentEncoding(CHASET_UTF_8);
        stringEntity.setContentType("application/json");
        HttpPost httpPost = new HttpPost(url);
        httpPost.setHeader("Connection","close");
        httpPost.setEntity(stringEntity);
        HttpResponse resp = closeableHttpClient.execute(httpPost);
        HttpEntity he = resp.getEntity();
        String respContent = EntityUtils.toString(he, CHASET_UTF_8);
        return (JSONObject) JSONObject.parse(respContent);
    }
}
