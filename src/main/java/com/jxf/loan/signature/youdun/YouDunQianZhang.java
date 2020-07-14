package com.jxf.loan.signature.youdun;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
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
import com.jxf.loan.entity.NfsLoanRecord;
import com.jxf.svc.config.Global;
import com.jxf.svc.font.MyFontsProvider;
import com.jxf.svc.security.MD5Utils;
import com.jxf.svc.utils.HtmlUtils;
public class YouDunQianZhang {
	
    private static final Logger logger = LoggerFactory.getLogger(YouDunQianZhang.class);
	/**
	 * 定向签署接口调用地址
	 */
	static final String directionally_sign_contract_url = "https://esignature.udcredit.com/frontserver/4.3/esignature/directionally-sign-contract/pub_key/%s";

	/**
	 * 商户pub_key(下发到商户联系人邮箱)
	 */
	static final String PUB_KEY = "3f39fc1c-eca4-4fc3-aeb1-e8dafcdca65b";
	/**
	 * 商户secretkey(下发到商户联系人邮箱)
	 */
	static final String security_key = "3934d22d-8bff-461c-9328-0141629bbafe";
	
	
	/**
	 * 公司名称
	 */
	static final String comName = "北京友信宝网络科技有限公司";
	
	/**
	 * 企业信息码
	 */
	/**
	 * 公司名称
	 */
	static final String comCode ="91110105335565804C";
	
	
    private static TrustManager myX509TrustManager = new X509TrustManager() {
        public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
        }

        public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
        }

        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }
    };
    
	/**
	 * 定向签署接口
	 * 
	 * @throws Exception
	 * 
	 * 
	 * 
	 */
	public static String detecttest(NfsLoanRecord loan,Long loanNum){
		String applyNo = null;
		String url = String.format(directionally_sign_contract_url, PUB_KEY);
		JSONObject jsonObject = new JSONObject();
		JSONObject body = new JSONObject();
		try {
			jsonObject.put("header", getRequestHeader());
		} catch (Exception e1) {
			e1.printStackTrace();
		};
		JSONArray sign_info = new JSONArray();

		// 设置position参数 借款人
		JSONObject json1 = new JSONObject();
		json1.put("key_word", loan.getLoanee().getName());
		json1.put("llX",110);
		json1.put("llY",850);
		json1.put("urX",250);
		json1.put("urY",890);
		json1.put("sign_page",1);
		
		//放款人
		JSONObject json2 = new JSONObject();
		json2.put("llX",110);
		json2.put("llY",900);
		json2.put("urX",250);
		json2.put("urY",940);
		json2.put("sign_page",1);
		
		JSONObject json3 = new JSONObject();
		json3.put("llX",290);
		json3.put("llY",770);
		json3.put("urX",410);
		json3.put("urY",890);
		json3.put("sign_page",1);
		// 设置sign_info参数
		JSONObject str = new JSONObject();
		JSONObject str1 = new JSONObject();
		JSONObject str2 = new JSONObject();
		str.put("user_type", "1");
		str.put("name", loan.getLoanee().getName());
		str.put("license_no", loan.getLoanee().getIdNo());
		str.put("is_willing_sign","1");
		str.put("sign_type", "1");
		str.put("position", json1);

		str1.put("user_type", "1");
		str1.put("name", loan.getLoaner().getName());
		str1.put("license_no", loan.getLoaner().getIdNo());
		str1.put("is_willing_sign", "1");
		str1.put("sign_type", "1");
		str1.put("position", json2);
		
		str2.put("user_type", "2");
		str2.put("name",comName );
		str2.put("license_no",comCode);
		str2.put("is_willing_sign","1");
		str2.put("sign_type","1");
		str2.put("position", json3);

		sign_info.add(str);
		sign_info.add(str1);
		sign_info.add(str2);
		// 暂时的
		body.put("business_code",loanNum);
		body.put("sign_info", sign_info);
		jsonObject.put("body", body);
		String pDfUrl = getPDfUrl(loan,loanNum);
		if (pDfUrl == null) {
			return applyNo;
		}
		File file = new File(pDfUrl);
		CloseableHttpClient client = HttpClients.createDefault();
		String responseContent = null; // 响应内容
		CloseableHttpResponse response = null;
		ContentType contentType = ContentType.create("text/plain", Charset.forName("UTF-8"));
		MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create().addTextBody("content",
				jsonObject.toJSONString(), contentType);
		multipartEntityBuilder.addBinaryBody("file", file);
		try {

			HttpEntity reqEntity = multipartEntityBuilder.build();
			HttpPost post = new HttpPost(url);
//            post.setHeader("Content-type", "application/json; charset=utf-8");
			post.setEntity(reqEntity);
			response = client.execute(post);
//			logger.info("有盾response："+response);
			if (response.getStatusLine().getStatusCode() == 200) {
				HttpEntity entity = response.getEntity();
				responseContent = EntityUtils.toString(entity, "UTF-8");
				JSONObject object = JSONObject.parseObject(responseContent);
				boolean isSuccess = object.getJSONObject("result").getBoolean("success");
				if (isSuccess) {
					String string = object.getJSONObject("data").getString("sign_status");
					if (string.equals("1")) {
						applyNo=object.getJSONObject("data").getString("download_path");
					}

				}else {
					String message = object.getJSONObject("result").getString("message");
					String errorcode = object.getJSONObject("result").getString("errorcode");
					logger.error("电子合同：{}有盾签章错误码：{}，错误信息：{}",loanNum,errorcode,message);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (response != null)
					response.close();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					if (client != null)
						client.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return applyNo;
	}

	/**
	 * header请求头
	 *
	 * @return
	 * @throws Exception
	 */
	public static JSONObject getRequestHeader() throws Exception {
		JSONObject header = new JSONObject();
		/**
		 * 商户订单号：由商户自定义传入的唯一且不大于32位的字符串， 生成的订单号不能带有:、_#_等特殊字符，建议使用时间戳或数字、字母、下划线等组合
		 */
		String partner_order_id = UUID.randomUUID().toString();
		String sign_time = TestUtils.getStringDate(new Date());
		String sign = getMd5(PUB_KEY, partner_order_id, sign_time, security_key);
		header.put("sign_time", sign_time);
		header.put("partner_order_id", partner_order_id);
		header.put("sign", sign);
		return header;
	}

	/**
	 * @param pub_key
	 * @param partner_order_id
	 * @param sign_time
	 * @param security_key
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String getMd5(String pub_key, String partner_order_id, String sign_time, String security_key)
			throws UnsupportedEncodingException {
		String signStr = String.format("pub_key=%s|partner_order_id=%s|sign_time=%s|security_key=%s", pub_key,
				partner_order_id, sign_time, security_key);
		return MD5Utils.EncoderByMd5(signStr);
	}

	// 生成本地的pdf
	public static String getPDfUrl(NfsLoanRecord loan,Long loanNum) {
		Document document = new Document(new RectangleReadOnly(842F, 1000F));
		OutputStream os;
		try {
			String html = HtmlUtils.getContent(Global.getConfig("domain")+"/callback/contract/genContract?loanId=" + loan.getId(),"UTF-8");
			logger.info("有盾html:" + html);
	        os = new FileOutputStream(Global.getBaseStaticPath()+Global.getConfig("youdunContractPath") + loanNum + "_TMT.pdf");
	        PdfWriter writer = PdfWriter.getInstance(document, os);
	        File file = new File(Global.getBaseStaticPath() + Global.getConfig("youdunContractPath"));
			if (!file.exists()) {
				file.mkdirs();
			}
			os = new FileOutputStream(Global.getBaseStaticPath() + Global.getConfig("youdunContractPath")+loanNum + "_TMT.pdf");
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

	// 下载合同
	public static String doDownloadYouDunPdf(String fileUrl,Long loanNum){
		if (StringUtils.isBlank(fileUrl)) {
			return "下载地址不能为空!";
		}
		Date date = new Date();
		InputStream in = null;
		FileOutputStream fos = null;
		SimpleDateFormat sdf_all = new SimpleDateFormat("yyyy/MM/dd");
		String timestamp = sdf_all.format(date);
		String pdfName = "youdunqianzhang_" + loanNum + ".pdf";
		try {
			 SSLContext	sslcontext = SSLContext.getInstance("SSL", "SunJSSE");
				sslcontext.init(null, new TrustManager[] { myX509TrustManager }, new java.security.SecureRandom());
				URL  url = new URL(fileUrl);
		
			HostnameVerifier ignoreHostnameVerifier = new HostnameVerifier() {
				public boolean verify(String s, SSLSession sslsession) {
					return true;
				}
			};
			HttpsURLConnection.setDefaultHostnameVerifier(ignoreHostnameVerifier);
			HttpsURLConnection.setDefaultSSLSocketFactory(sslcontext.getSocketFactory());
			HttpsURLConnection urlCon = (HttpsURLConnection) url.openConnection();
			urlCon.setConnectTimeout(6000);
			urlCon.setReadTimeout(6000);
			int code = urlCon.getResponseCode();
			if (code != HttpURLConnection.HTTP_OK) {
				throw new Exception("文件读取失败");
			}
			in = url.openStream();
			String contentType = url.openConnection().getContentType();
			if (StringUtils.isNotBlank(contentType) && contentType.contains("json")) {
				return null;
			}
			File destDir = new File(Global.getBaseStaticPath()+Global.getConfig("youdunContractPath") + timestamp);
			if (!destDir.exists()) {
				destDir.mkdirs();
			}
			fos = new FileOutputStream(new File(destDir, pdfName));
			int length = -1;
			byte[] buffer = new byte[1024];
			while ((length = in.read(buffer)) > -1) {
				fos.write(buffer, 0, length);
			}
			// 删除pdf
			boolean deleteFile = deleteFile(Global.getBaseStaticPath()+Global.getConfig("youdunContractPath") + loanNum + "_TMT.pdf");
			if (deleteFile == false) {
				deleteFile(Global.getBaseStaticPath()+Global.getConfig("youdunContractPath") + loanNum + "_TMT.pdf");
			}
		} catch (Exception e) {
			  return null;
		} finally {
			try {
				fos.close();
				in.close();
			} catch (Exception e) {
	
			}
		}
		return Global.getConfig("youdunContractPath") + timestamp + "/" + pdfName; // 成功
	}

	// 删除未盖章的文件
	public static boolean deleteFile(String fileName) {
		File file = new File(fileName);
		// 如果文件路径所对应的文件存在，并且是一个文件，则直接删除
		if (file.exists() && file.isFile()) {
			if (file.delete()) {
				return true;
			} else {
				return false;
			}
		} else {
			return true;
		}
	}	
}
