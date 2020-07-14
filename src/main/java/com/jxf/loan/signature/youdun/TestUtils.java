package com.jxf.loan.signature.youdun;

import com.alibaba.fastjson.JSONObject;
import com.jxf.svc.utils.Encodes;
import com.jxf.svc.utils.StringUtils;

import org.apache.commons.io.FileUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class TestUtils {



    private static final CloseableHttpClient closeableHttpClient = HttpClients.createDefault();
    static final String CHASET_UTF_8 = "UTF-8";

    /**
     * http请求
     *
     * @param url
     * @param jsonObject
     * @return
     * @throws Exception
     */
    public static JSONObject doHttpRequest(String url, JSONObject jsonObject) throws Exception {

        //设置传入参数
        StringEntity stringEntity = new StringEntity(jsonObject.toJSONString(), CHASET_UTF_8);
        stringEntity.setContentEncoding(CHASET_UTF_8);
        stringEntity.setContentType("application/json");
        HttpPost httpPost = new HttpPost(url);
        httpPost.setHeader("Connection", "close");
        httpPost.setEntity(stringEntity);
        HttpResponse resp = closeableHttpClient.execute(httpPost);
        HttpEntity he = resp.getEntity();
        String respContent = EntityUtils.toString(he, CHASET_UTF_8);
        return (JSONObject) JSONObject.parse(respContent);
    }
    /**
     * 格式化日期字符串 yyyyMMddHHmmss
     *
     * @param date
     * @return
     */
    public static String getStringDate(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        return simpleDateFormat.format(date);
    }

    /**
     * 从测试目录（//src//main//java//picture//）获取测试base64数据
     *
     */
    public static String getFileBase64Str(String fileName) throws IOException {
        String filePath = System.getProperty("user.dir") + "//src//main//java//picture//" + fileName;
        System.out.println("测试文件：" + filePath);
        File file = new File(filePath);
        byte[] front = FileUtils.readFileToByteArray(file);
        return  Encodes.encodeBase64(front);
    }
    
    public static void main(String[] args) {
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
        bodyJson.put("sealText", "");
        bodyJson.put("sealColor", 0);
        bodyJson.put("sealType", 2);
        
        reqJson.put("header", header);
        reqJson.put("body", bodyJson);
        
        String encryptResult =  reqJson.toJSONString();
        String encrypts = Base64.SHA1WithRSASign(YouDunConstant.RSA_PRIVATE_KEY, encryptResult);
        
        HttpPost httpPost = new HttpPost(reqUrl);
        CloseableHttpClient httpClient = HttpClients.createDefault();
        ContentType contentType = ContentType.create("text/plain", Charset.forName("UTF-8"));
        MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();
        multipartEntityBuilder.addTextBody("content", reqJson.toString(), contentType);
        httpPost.setHeader("X-UD-Signature", encrypts);
        httpPost.setEntity(multipartEntityBuilder.build());
        String resultString;
		try {
			HttpResponse response = httpClient.execute(httpPost);
			resultString = EntityUtils.toString(response.getEntity());
			JSONObject responseJson = JSONObject.parseObject(resultString);
			System.out.println(resultString);
		} catch (ParseException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
       
        
        
        





    	
    }
}
