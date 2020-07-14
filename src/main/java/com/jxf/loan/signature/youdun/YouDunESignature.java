package com.jxf.loan.signature.youdun;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.itextpdf.text.Document;
import com.itextpdf.text.RectangleReadOnly;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import com.itextpdf.tool.xml.html.CssAppliers;
import com.itextpdf.tool.xml.html.CssAppliersImpl;
import com.itextpdf.tool.xml.html.Tags;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipelineContext;
import com.jxf.loan.entity.NfsLoanApply.TrxType;
import com.jxf.loan.entity.NfsCrAuction;
import com.jxf.loan.entity.NfsCrContract;
import com.jxf.loan.entity.NfsLoanContract;
import com.jxf.loan.entity.NfsLoanRecord;
import com.jxf.mem.entity.Member;
import com.jxf.svc.config.Global;
import com.jxf.svc.font.MyFontsProvider;
import com.jxf.svc.utils.Exceptions;
import com.jxf.svc.utils.FileUtils;
import com.jxf.svc.utils.HtmlUtils;
import com.jxf.svc.utils.SnowFlake;
import com.jxf.svc.utils.StringUtils;


public class YouDunESignature {
	private static final Logger logger = LoggerFactory.getLogger(YouDunESignature.class);
	/**
	  * 云慧签开户接口
     */
	public static String constructOpen(Member member) {
		String partnerOrderId = UUID.randomUUID().toString().replace("-", "");
		JSONObject resultJson = new JSONObject();
		JSONObject header = new JSONObject();
		header.put("partnerOrderId", partnerOrderId);
		header.put("requestTime", System.currentTimeMillis());
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("userType", 1);
		jsonObject.put("userName", member.getName());
		jsonObject.put("idType", "0");
		jsonObject.put("idNo", member.getIdNo());
		jsonObject.put("mobile", member.getUsername());
		jsonObject.put("sendSMS", 2);
		resultJson.put("header", header);
		resultJson.put("body", jsonObject);
		String partnerCode = YouDunConstant.PARTNERCODE;
		String signature = Base64.SHA1WithRSASign(YouDunConstant.RSA_PRIVATE_KEY, resultJson.toJSONString());
		String requestUrl = "https://esignature.udcredit.com/api/2.0/user/establish/partner-code/" + partnerCode;
		JSONObject httpJson = httpPost(requestUrl, signature, resultJson);
		String userCode = "";
		Boolean isSuccess = httpJson.getBoolean("success");
		if(isSuccess) {
			userCode = httpJson.getJSONObject("data").getString("userCode");
		}else {
			logger.error("会员：{}，有盾签章开户：{}",member.getId()+httpJson.toString());
		}
		return userCode;
	}
	/**
     * 自动签署
     *
     * @throws IOException
     */
    public static String autoSignCrContract(NfsCrAuction crAuction) throws IOException {
    	Member seller = crAuction.getCrSeller(); 
		Member buyer = crAuction.getCrBuyer();
        String partnerCode = YouDunConstant.PARTNERCODE;
        JSONObject resultJson = new JSONObject();
        JSONObject header = new JSONObject();
        String partnerOrderId = UUID.randomUUID().toString().replace("-", "");
        header.put("partnerOrderId", partnerOrderId);
        header.put("requestTime", System.currentTimeMillis());
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        jsonObject.put("lockedStatus", 1);
        
        JSONObject sellerJson = new JSONObject();
        String sellerUserCode = constructOpen(seller);
        if(StringUtils.isBlank(sellerUserCode)) {
        	return null;
        }
        sellerJson.put("userCode", sellerUserCode);
        sellerJson.put("signType", 1);
        JSONObject sellerSignLocation = new JSONObject();
        sellerSignLocation.put("signPage", 2);
        sellerSignLocation.put("llX", 40);
        sellerSignLocation.put("llY", 805);
        sellerSignLocation.put("urX",180);
        sellerSignLocation.put("urY", 845);
        sellerJson.put("signLocation", sellerSignLocation);
        jsonArray.add(sellerJson);
        
        JSONObject buyerJson = new JSONObject();
        String buyerUserCode = constructOpen(buyer);
        if(StringUtils.isBlank(buyerUserCode)) {
        	return null;
        }
        buyerJson.put("userCode", buyerUserCode);
        buyerJson.put("signType", 1);
        JSONObject buyerSignLocation = new JSONObject();
        buyerSignLocation.put("signPage", 2);
        buyerSignLocation.put("llX", 40);
        buyerSignLocation.put("llY", 745);
        buyerSignLocation.put("urX", 180);
        buyerSignLocation.put("urY", 785);
        buyerJson.put("signLocation", buyerSignLocation);
        jsonArray.add(buyerJson);
        
        JSONObject companyJson = new JSONObject();
        String companyUserCode = YouDunConstant.USERCODE;
        companyJson.put("userCode", companyUserCode);
        companyJson.put("signType", 1);
        JSONObject companySignLocation = new JSONObject();
        companySignLocation.put("signPage", 2);
        companySignLocation.put("llX", 200);
        companySignLocation.put("llY", 650);
        companySignLocation.put("urX", 320);
        companySignLocation.put("urY", 770);
        companyJson.put("signLocation", companySignLocation);
        jsonArray.add(companyJson);
        
        jsonObject.put("signInfo", jsonArray);
        resultJson.put("header", header);
        resultJson.put("body", jsonObject);
        //文件转为字节数组
        String pDfUrl = getCrPDfUrl(crAuction);
        File file = new File(pDfUrl);
        byte[] bytes = DigestUtil.fileConvertToByteArray(file);
        String SHAStr = DigestUtil.digest(bytes).toUpperCase();
        String encryptResult = SHAStr + "|" + resultJson.toJSONString();
        String encrypts = Base64.SHA1WithRSASign(YouDunConstant.RSA_PRIVATE_KEY, encryptResult);
        String requestUrl = "https://esignature.udcredit.com/api/2.0/contract/auto-sign/partner-code/" + partnerCode;
        HttpPost httpPost = new HttpPost(requestUrl);
        CloseableHttpClient httpClient = HttpClients.createDefault();
        ContentType contentType = ContentType.create("text/plain", Charset.forName("UTF-8"));
        ContentType pdfType = ContentType.create("application/pdf", Charset.forName("UTF-8"));
        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(10000).setSocketTimeout(10000).build();
        MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();
        multipartEntityBuilder.addPart("contractFile", new FileBody(file, pdfType, ""));
        multipartEntityBuilder.addTextBody("content", resultJson.toString(), contentType);
        httpPost.setConfig(requestConfig);
        httpPost.setHeader("X-UD-Signature", encrypts);
        httpPost.setEntity(multipartEntityBuilder.build());
        HttpResponse response = httpClient.execute(httpPost);
        String resultString = EntityUtils.toString(response.getEntity());
        JSONObject responseJson = JSONObject.parseObject(resultString);
        Boolean isSuccess = responseJson.getBoolean("success");
        String contractCode = "";
        if(isSuccess) {
        	//操作成功
        	JSONObject data = responseJson.getJSONObject("data");
        	String contractSignStatus = data.getString("contractSignStatus");
        	if(StringUtils.equals(contractSignStatus, "1")) {
        		contractCode = data.getString("contractCode");
        	}else {
        		logger.error("债转合同：{}有盾签章返回数据：{}",crAuction.getId(),responseJson.toString());
        	}
        }else {
        	logger.error("债转合同：{}有盾签章返回数据：{}",crAuction.getId(),responseJson.toString());
        }
        return contractCode;
    }
	
	    /**
	     * 自动签署
	     *
	     * @throws IOException
	     */
	    public static String autoSign(NfsLoanRecord loanRecord,Long loanContractId) throws IOException {
	        String partnerCode = YouDunConstant.PARTNERCODE;
	        JSONObject resultJson = new JSONObject();
	        JSONObject header = new JSONObject();
	        String partnerOrderId = UUID.randomUUID().toString().replace("-", "");
	        header.put("partnerOrderId", partnerOrderId);
	        header.put("requestTime", System.currentTimeMillis());
	        JSONObject jsonObject = new JSONObject();
	        JSONArray jsonArray = new JSONArray();
	        jsonObject.put("lockedStatus", 1);
	        
	        JSONObject loanerJson = new JSONObject();
	        String loanerUserCode = constructOpen(loanRecord.getLoaner());
	        if(StringUtils.isBlank(loanerUserCode)) {
	        	return null;
	        }
	        loanerJson.put("userCode", loanerUserCode);
	        loanerJson.put("signType", 1);
	        JSONObject loanerSignLocation = new JSONObject();
	        loanerSignLocation.put("signPage", 1);
	        loanerSignLocation.put("llX", 80);
	        loanerSignLocation.put("llY", 875);
	        loanerSignLocation.put("urX",220);
	        loanerSignLocation.put("urY", 915);
	        loanerJson.put("signLocation", loanerSignLocation);
	        jsonArray.add(loanerJson);
	        
	        JSONObject loaneeJson = new JSONObject();
	        String loaneeUserCode = constructOpen(loanRecord.getLoanee());
	        if(StringUtils.isBlank(loaneeUserCode)) {
	        	return null;
	        }
	        loaneeJson.put("userCode", loaneeUserCode);
	        loaneeJson.put("signType", 1);
	        JSONObject loaneeSignLocation = new JSONObject();
	        loaneeSignLocation.put("signPage", 1);
	        loaneeSignLocation.put("llX", 80);
	        loaneeSignLocation.put("llY", 835);
	        loaneeSignLocation.put("urX", 220);
	        loaneeSignLocation.put("urY", 875);
	        loaneeJson.put("signLocation", loaneeSignLocation);
	        jsonArray.add(loaneeJson);
	        
	        JSONObject companyJson = new JSONObject();
	        String companyUserCode = YouDunConstant.USERCODE;
	        companyJson.put("userCode", companyUserCode);
	        companyJson.put("signType", 1);
	        JSONObject companySignLocation = new JSONObject();
	        companySignLocation.put("signPage", 1);
	        companySignLocation.put("llX", 260);
	        companySignLocation.put("llY", 740);
	        companySignLocation.put("urX", 380);
	        companySignLocation.put("urY", 860);
	        companyJson.put("signLocation", companySignLocation);
	        jsonArray.add(companyJson);
	        
	        jsonObject.put("signInfo", jsonArray);
	        resultJson.put("header", header);
	        resultJson.put("body", jsonObject);
	        //文件转为字节数组
	        String pDfUrl = getPDfUrl(loanRecord,loanContractId);
	        File file = new File(pDfUrl);
	        byte[] bytes = DigestUtil.fileConvertToByteArray(file);
	        String SHAStr = DigestUtil.digest(bytes).toUpperCase();
	        String encryptResult = SHAStr + "|" + resultJson.toJSONString();
	        String encrypts = Base64.SHA1WithRSASign(YouDunConstant.RSA_PRIVATE_KEY, encryptResult);
	        String requestUrl = "https://esignature.udcredit.com/api/2.0/contract/auto-sign/partner-code/" + partnerCode;
	        HttpPost httpPost = new HttpPost(requestUrl);
	        CloseableHttpClient httpClient = HttpClients.createDefault();
	        ContentType contentType = ContentType.create("text/plain", Charset.forName("UTF-8"));
	        ContentType pdfType = ContentType.create("application/pdf", Charset.forName("UTF-8"));
	        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(10000).setSocketTimeout(10000).build();
	        MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();
	        multipartEntityBuilder.addPart("contractFile", new FileBody(file, pdfType, ""));
	        multipartEntityBuilder.addTextBody("content", resultJson.toString(), contentType);
	        httpPost.setConfig(requestConfig);
	        httpPost.setHeader("X-UD-Signature", encrypts);
	        httpPost.setEntity(multipartEntityBuilder.build());
	        HttpResponse response = httpClient.execute(httpPost);
	        String resultString = EntityUtils.toString(response.getEntity());
	        JSONObject responseJson = JSONObject.parseObject(resultString);
	        logger.info("有盾签章返回数据：" + responseJson.toString());
	        Boolean isSuccess = responseJson.getBoolean("success");
	        String contractCode = "";
	        if(isSuccess) {
	        	//操作成功
	        	JSONObject data = responseJson.getJSONObject("data");
	        	String contractSignStatus = data.getString("contractSignStatus");
	        	if(StringUtils.equals(contractSignStatus, "1")) {
	        		contractCode = data.getString("contractCode");
	        	}else {
	        		logger.error("借条合同：{}有盾签章返回数据：{}",loanContractId,responseJson.toString());
	        	}
	        }else {
	        	logger.error("借条合同：{}有盾签章返回数据：{}",loanContractId,responseJson.toString());
	        }
	        return contractCode;
	    }
	    
	    /**
	     * 
	     *   调用有盾签章对文档做签章
	     * @throws IOException
	     */
	    public static String signPdf(File file) throws IOException {
	        String partnerCode = YouDunConstant.PARTNERCODE;
	        JSONObject reqJson = new JSONObject();
	        JSONObject header = new JSONObject();
	        String partnerOrderId = UUID.randomUUID().toString().replace("-", "");
	        header.put("partnerOrderId", partnerOrderId);
	        header.put("requestTime", System.currentTimeMillis());
	        JSONObject bodyJson = new JSONObject();
	        JSONArray jsonArray = new JSONArray();
	        bodyJson.put("lockedStatus", 1);
	   
	        	        
	        JSONObject companyJson = new JSONObject();
	        String companyUserCode = YouDunConstant.USERCODE;
	        companyJson.put("userCode", companyUserCode);
	        companyJson.put("sealId", YouDunConstant.COMMON_SEAL_ID);
	        companyJson.put("signType", 1);
	        JSONObject companySignLocation = new JSONObject();
	        companySignLocation.put("signPage", 1);
	        companySignLocation.put("llX", 260);
	        companySignLocation.put("llY", 740);
	        companySignLocation.put("urX", 380);
	        companySignLocation.put("urY", 860);
	        companyJson.put("signLocation", companySignLocation);
	        jsonArray.add(companyJson);
	        
	        bodyJson.put("signInfo", jsonArray);
	        reqJson.put("header", header);
	        reqJson.put("body", bodyJson);
	        //文件转为字节数组
	        byte[] bytes = DigestUtil.fileConvertToByteArray(file);
	        String SHAStr = DigestUtil.digest(bytes).toUpperCase();
	        String encryptResult = SHAStr + "|" + reqJson.toJSONString();
	        String encrypts = Base64.SHA1WithRSASign(YouDunConstant.RSA_PRIVATE_KEY, encryptResult);
	        String requestUrl = "https://esignature.udcredit.com/api/2.0/contract/auto-sign/partner-code/" + partnerCode;
	        HttpPost httpPost = new HttpPost(requestUrl);
	        CloseableHttpClient httpClient = HttpClients.createDefault();
	        ContentType contentType = ContentType.create("text/plain", Charset.forName("UTF-8"));
	        ContentType pdfType = ContentType.create("application/pdf", Charset.forName("UTF-8"));
	        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(10000).setSocketTimeout(10000).build();
	        MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();
	        multipartEntityBuilder.addPart("contractFile", new FileBody(file, pdfType, ""));
	        multipartEntityBuilder.addTextBody("content", reqJson.toString(), contentType);
	        httpPost.setConfig(requestConfig);
	        httpPost.setHeader("X-UD-Signature", encrypts);
	        httpPost.setEntity(multipartEntityBuilder.build());
	        HttpResponse response = httpClient.execute(httpPost);
	        String resultString = EntityUtils.toString(response.getEntity());
	        JSONObject responseJson = JSONObject.parseObject(resultString);
	        Boolean isSuccess = responseJson.getBoolean("success");
	        String contractCode = "";
	        if(isSuccess) {
	        	//操作成功
	        	JSONObject data = responseJson.getJSONObject("data");
	        	String contractSignStatus = data.getString("contractSignStatus");
	        	if(StringUtils.equals(contractSignStatus, "1")) {
	        		contractCode = data.getString("contractCode");
	        	}else {
	        		logger.error("有盾签章返回数据：{}",responseJson.toString());
	        	}
	        }else {
	        	logger.error("有盾签章返回数据：{}",responseJson.toString());
	        }
	        return contractCode;
	    }
	    
	    /**
	     * 下载签章完成的文档
	     */
	    public static void downloadSignedPdf(String contractCode,String dirName,String fileName) {
	    	try {
		        JSONObject resultJson = new JSONObject();
		        JSONObject header = new JSONObject();
		        String partnerOrderId = UUID.randomUUID().toString().replace("-", "");
		        header.put("partnerOrderId",partnerOrderId );
		        header.put("requestTime", System.currentTimeMillis());
		        JSONObject jsonObject = new JSONObject();
		        jsonObject.put("contractCode", contractCode);
		        resultJson.put("header", header);
		        resultJson.put("body", jsonObject);
		        String partnerCode = YouDunConstant.PARTNERCODE;
		        String requestUrl = "https://esignature.udcredit.com/api/2.0/contract/download/partner-code/" + partnerCode;
		        String signature = Base64.SHA1WithRSASign(YouDunConstant.RSA_PRIVATE_KEY, resultJson.toJSONString());
		        CloseableHttpClient httpClient = HttpClients.createDefault();
		        HttpPost httpPost = new HttpPost(requestUrl);
		        httpPost.setHeader("X-UD-Signature", signature);
		        httpPost.setHeader("Content-Type", "application/json;charset=UTF-8");
		        StringEntity stringEntity = new StringEntity(resultJson.toString(), "UTF-8");
		        stringEntity.setContentEncoding("UTF-8");
		        stringEntity.setContentType("application/json");
		        httpPost.setEntity(stringEntity);
		        HttpResponse resp = httpClient.execute(httpPost);
		        HttpEntity httpEntity = resp.getEntity();

		        FileUtils.copyInputStreamToFile(httpEntity.getContent(), new File(dirName+fileName));
		
	
			} catch (Exception e) {
				 logger.error("==============有盾下载PDF异常：{}" , Exceptions.getStackTraceAsString(e));
			}
	    }


	    /**
	     * 合同信息查询 暂时不用，有需要可以加上
	     */
	    public static void querySign(String code) {
	        JSONObject resultJson = new JSONObject();
	        JSONObject header = new JSONObject();
	        header.put("partnerOrderId", SnowFlake.getId()+"");
	        header.put("requestTime", System.currentTimeMillis());
	        JSONObject jsonObject = new JSONObject();
	        jsonObject.put("contractCode", code);
	        resultJson.put("header", header);
	        resultJson.put("body", jsonObject);
	        String requestUrl = "https://esignature.udcredit.com/api/2.0/contract/query/partner-code/" + YouDunConstant.PARTNERCODE;
	        String signature = Base64.SHA1WithRSASign(YouDunConstant.RSA_PRIVATE_KEY, resultJson.toJSONString());
	        JSONObject httpJson = httpPost(requestUrl, signature, resultJson);
	        logger.info("合同查询信息：{}",httpJson.toString());
	    }

	    /**
	     * 合同下载
	     */
	    public static String download(NfsLoanContract loanContract) {
	    	try {
		        JSONObject resultJson = new JSONObject();
		        JSONObject header = new JSONObject();
		        String partnerOrderId = UUID.randomUUID().toString().replace("-", "");
		        header.put("partnerOrderId",partnerOrderId );
		        header.put("requestTime", System.currentTimeMillis());
		        JSONObject jsonObject = new JSONObject();
		        jsonObject.put("contractCode", loanContract.getSignatureNo());
		        resultJson.put("header", header);
		        resultJson.put("body", jsonObject);
		        String partnerCode = YouDunConstant.PARTNERCODE;
		        String requestUrl = "https://esignature.udcredit.com/api/2.0/contract/download/partner-code/" + partnerCode;
		        String signature = Base64.SHA1WithRSASign(YouDunConstant.RSA_PRIVATE_KEY, resultJson.toJSONString());
		        CloseableHttpClient httpClient = HttpClients.createDefault();
		        HttpPost httpPost = new HttpPost(requestUrl);
		        httpPost.setHeader("X-UD-Signature", signature);
		        httpPost.setHeader("Content-Type", "application/json;charset=UTF-8");
		        StringEntity stringEntity = new StringEntity(resultJson.toString(), "UTF-8");
		        stringEntity.setContentEncoding("UTF-8");
		        stringEntity.setContentType("application/json");
		        httpPost.setEntity(stringEntity);
		        HttpResponse resp = httpClient.execute(httpPost);
		        HttpEntity httpEntity = resp.getEntity();
		        //转字节数组 写文件到硬盘
		        byte[] bytes = DigestUtil.readInputStream(httpEntity.getContent());
		        SimpleDateFormat sdf_all = new SimpleDateFormat("yyyy/MM/dd");
				String timestamp = sdf_all.format(new Date());
				String pdfName = "youdunqianzhang_" + loanContract.getId() + ".pdf";
				File destDir = new File(Global.getBaseStaticPath()+Global.getConfig("youdunContractPath") + timestamp);
				if (!destDir.exists()) {
					destDir.mkdirs();
				}
				String filePath = destDir.getPath() +"/" + pdfName;
		        Path path = Paths.get(filePath);
		        Path path2 = Files.write(path, bytes);
		        //删除之前写的临时文件
		        FileUtils.deleteFile(Global.getBaseStaticPath()+Global.getConfig("youdunContractPath") + loanContract.getId() + "_TMT.pdf");
		        return path2.toString();
			} catch (Exception e) {
				 logger.error("==============有盾下载PDF异常：{}" , Exceptions.getStackTraceAsString(e));
			}
	    	return null;
	    }

	    /**
	     * 合同下载
	     */
	    public static String downloadCrPdf(NfsCrContract crContract) {
	    	try {
		        JSONObject resultJson = new JSONObject();
		        JSONObject header = new JSONObject();
		        String partnerOrderId = UUID.randomUUID().toString().replace("-", "");
		        header.put("partnerOrderId",partnerOrderId );
		        header.put("requestTime", System.currentTimeMillis());
		        JSONObject jsonObject = new JSONObject();
		        jsonObject.put("contractCode", crContract.getSignatureNo());
		        resultJson.put("header", header);
		        resultJson.put("body", jsonObject);
		        String partnerCode = YouDunConstant.PARTNERCODE;
		        String requestUrl = "https://esignature.udcredit.com/api/2.0/contract/download/partner-code/" + partnerCode;
		        String signature = Base64.SHA1WithRSASign(YouDunConstant.RSA_PRIVATE_KEY, resultJson.toJSONString());
		        CloseableHttpClient httpClient = HttpClients.createDefault();
		        HttpPost httpPost = new HttpPost(requestUrl);
		        httpPost.setHeader("X-UD-Signature", signature);
		        httpPost.setHeader("Content-Type", "application/json;charset=UTF-8");
		        StringEntity stringEntity = new StringEntity(resultJson.toString(), "UTF-8");
		        stringEntity.setContentEncoding("UTF-8");
		        stringEntity.setContentType("application/json");
		        httpPost.setEntity(stringEntity);
		        HttpResponse resp = httpClient.execute(httpPost);
		        HttpEntity httpEntity = resp.getEntity();
		        //转字节数组 写文件到硬盘
		        byte[] bytes = DigestUtil.readInputStream(httpEntity.getContent());
		        SimpleDateFormat sdf_all = new SimpleDateFormat("yyyy/MM/dd");
				String timestamp = sdf_all.format(new Date());
				String pdfName = "youdunqianzhang_cr_" + crContract.getId() + ".pdf";
				File destDir = new File(Global.getBaseStaticPath()+Global.getConfig("crAuctionContractPath") + timestamp);
				if (!destDir.exists()) {
					destDir.mkdirs();
				}
				String filePath = destDir.getPath() +"/" + pdfName;
		        Path path = Paths.get(filePath);
		        Path path2 = Files.write(path, bytes);
		        //删除之前写的临时文件
		        FileUtils.deleteFile(Global.getBaseStaticPath()+Global.getConfig("crAuctionContractPath") + crContract.getCrId()+ "_CR_TMT.pdf");
		        return path2.toString();
			} catch (Exception e) {
				 logger.error("==============有盾下载PDF异常：{}" , Exceptions.getStackTraceAsString(e));
			}
	    	return null;
	    }

	    

	    /**
	     * 发送post请求
	     *
	     * @param requestUrl  请求url
	     * @param signature   签名
	     * @param requestJson 请求json
	     */
	    private static JSONObject httpPost(String requestUrl, String signature, JSONObject requestJson) {
	        CloseableHttpClient httpClient = HttpClients.createDefault();
	        HttpPost httpPost = new HttpPost(requestUrl);
	        httpPost.setHeader("X-UD-Signature", signature);
	        httpPost.setHeader("Content-Type", "application/json;charset=UTF-8");
	        StringEntity stringEntity = new StringEntity(requestJson.toString(), "UTF-8");
	        stringEntity.setContentEncoding("UTF-8");
	        stringEntity.setContentType("application/json");
	        httpPost.setEntity(stringEntity);
	        JSONObject httpResultJson = null;
	        try {
	            HttpResponse resp = httpClient.execute(httpPost);
	            HttpEntity entity = resp.getEntity();
	            String response = EntityUtils.toString(entity);
	            httpResultJson = JSONObject.parseObject(response);
	        } catch (IOException e) {
	        	logger.error(Exceptions.getStackTraceAsString(e));
	        }
	        return httpResultJson;
	    }

	private static JSONObject httpPostPdf(String requestUrl, JSONObject requestJson, String signature, String filePath) {
		try {
			File file = new File(filePath);
			HttpPost httpPost = new HttpPost(requestUrl);
			CloseableHttpClient httpClient = HttpClients.createDefault();
			ContentType contentType = ContentType.create("text/plain", Charset.forName("UTF-8"));
			ContentType pdfType = ContentType.create("application/pdf", Charset.forName("UTF-8"));
			RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(10000).setSocketTimeout(10000)
					.build();
			MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();
			multipartEntityBuilder.addPart("contractFile", new FileBody(file, pdfType, ""));
			multipartEntityBuilder.addTextBody("content", requestJson.toString(), contentType);
			httpPost.setConfig(requestConfig);
			httpPost.setHeader("X-UD-Signature", signature);
			httpPost.setEntity(multipartEntityBuilder.build());
			HttpResponse response = httpClient.execute(httpPost);
			String resultString = EntityUtils.toString(response.getEntity());
			JSONObject responseJson = JSONObject.parseObject(resultString);
			logger.info("有盾签章返回数据：" + responseJson.toString());
			return responseJson;
		} catch (Exception e) {
			logger.error("有盾签章异常：{}", Exceptions.getStackTraceAsString(e));
		}
		return null;
	}
	    
	    

	    /**
	     * 读文件
	     *
	     * @param in
	     * @param charset
	     * @return
	     * @throws IOException
	     */
	    public static String readStreamAsString(InputStream in, String charset) throws IOException {
	        if (in == null) {
	            return "";
	        } else {
	            Reader reader = null;
	            Writer writer = new StringWriter();
	            char[] buffer = new char[1024];

	            try {
	                reader = new BufferedReader(new InputStreamReader(in, charset));

	                int n;
	                while ((n = reader.read(buffer)) > 0) {
	                    writer.write(buffer, 0, n);
	                }

	                String result = writer.toString();
	                return result;
	            } finally {
	                in.close();
	                if (reader != null) {
	                    reader.close();
	                }

	                if (writer != null) {
	                    writer.close();
	                }

	            }
	        }
	    }
	 // 生成本地的pdf
		public static String getPDfUrl(NfsLoanRecord loan,Long loanNum) {
			Document document = new Document(new RectangleReadOnly(842F, 1000F));
			OutputStream os;
			try {
				String html = null;
				if(loan.getTrxType().equals(TrxType.online)) {
					html = HtmlUtils.getContent(Global.getConfig("domain")+"/callback/contract/genContract?loanId=" + loan.getId());
				}else {
					html = HtmlUtils.getContent(Global.getConfig("domain")+"/callback/contract/genGxtContract?loanId=" + loan.getId(),"UTF-8");
				}
		        os = new FileOutputStream(Global.getBaseStaticPath()+Global.getConfig("youdunContractPath") + loanNum + "_TMT.pdf");
		        PdfWriter writer = PdfWriter.getInstance(document, os);
				 //使用字体提供器，并将其设置为unicode字体样式
				document.open();
			    MyFontsProvider fontProvider = new MyFontsProvider();
			    fontProvider.addFontSubstitute("lowagie", "garamond");
			    fontProvider.setUseUnicode(true);
			    CssAppliers cssAppliers = new CssAppliersImpl(fontProvider);
			    HtmlPipelineContext htmlContext = new HtmlPipelineContext(cssAppliers);
			    htmlContext.setTagFactory(Tags.getHtmlTagProcessorFactory());
			    XMLWorkerHelper.getInstance().getDefaultCssResolver(true);
			    XMLWorkerHelper.getInstance().parseXHtml(writer, document, 
				        new ByteArrayInputStream(html.getBytes("UTF-8")),  
				        Charset.forName("UTF-8"),fontProvider);  
				document.close();    
				os.flush();
				os.close();
				os = null;
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
			return Global.getBaseStaticPath()+Global.getConfig("youdunContractPath") + loanNum + "_TMT.pdf";

		}
	    
		public static String getCrPDfUrl(NfsCrAuction crAuction) {
			Document document = new Document(new RectangleReadOnly(842F, 1000F));
			OutputStream os;
			try {
				String html = HtmlUtils.getContent(Global.getConfig("domain")+"/callback/contract/genCrAuctionContract?crId=" + crAuction.getId());
		        os = new FileOutputStream(Global.getBaseStaticPath()+Global.getConfig("crAuctionContractPath") + crAuction.getId() + "_CR_TMT.pdf");
		        PdfWriter writer = PdfWriter.getInstance(document, os);
				 //使用字体提供器，并将其设置为unicode字体样式
				document.open();
			    MyFontsProvider fontProvider = new MyFontsProvider();
			    fontProvider.addFontSubstitute("lowagie", "garamond");
			    fontProvider.setUseUnicode(true);
			    CssAppliers cssAppliers = new CssAppliersImpl(fontProvider);
			    HtmlPipelineContext htmlContext = new HtmlPipelineContext(cssAppliers);
			    htmlContext.setTagFactory(Tags.getHtmlTagProcessorFactory());
			    XMLWorkerHelper.getInstance().getDefaultCssResolver(true);
			    XMLWorkerHelper.getInstance().parseXHtml(writer, document, 
				        new ByteArrayInputStream(html.getBytes("UTF-8")),  
				        Charset.forName("UTF-8"),fontProvider);  
				document.close();    
				os.flush();
				os.close();
				os = null;
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
			return Global.getBaseStaticPath()+Global.getConfig("crAuctionContractPath") + crAuction.getId() + "_CR_TMT.pdf";
		}
		
		
	public static void main(String[] args) {
		Member member = new Member();
		member.setName("刘辉辉");
		member.setIdNo("320321199204044632");
		member.setUsername("18201660917");
		System.out.println(constructOpen(member));
	} 
	 
}
