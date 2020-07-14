package com.jxf.test;

import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.Provider;
import java.security.PublicKey;
import java.security.cert.X509Certificate;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.AuthSchemes;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.apache.commons.lang3.time.DateUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

/**
 * @作者: xiaorongdian
 * @创建时间 :2019年4月18日 上午9:24:51
 * @功能说明:demo
 */
public class WyjtLoaneeDataDemo {
	/** 加密/解密算法 */
	private static final String TRANSFORMATION = "RSA/ECB/PKCS1Padding";
	/** 密钥算法 */
	private static final String KEY_ALGORITHM = "RSA";
	/** 安全服务提供者 */ 
	private static final Provider PROVIDER = new BouncyCastleProvider();
	/** 公钥 */
	private static String PUBIIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC2Gk+0YjegPk5v3cU80c9logXfh+mJGh2XkOpfGVN74ObwijjcMwylOXAluAM5ZBYYsKaTjdDn3bQnZEquqE01MjXSN0YxeWWhPrUGujKyqxHlFUVNFw5SCTBP+gcGgz824TfCPXlHQYboRDs3xWKVY1YdBzO9Otrpokn722+JtQIDAQAB";
	
	public static String[] parsePatterns = { "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", "yyyy-MM",
		"yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm", "yyyy/MM", "yyyy.MM.dd", "yyyy.MM.dd HH:mm:ss",
		"yyyy.MM.dd HH:mm", "yyyy.MM","yyyyMMdd" };
	
	public static void main(String[] args) { 
		/** 商户号 */
		String merchantNumber = "316555873143099392";
		
		/** 商户订单号：由商户自定义传入的唯一且不大于32位的字符串， 生成的订单号不能带有:、#-等特殊字符，建议使用时间戳或数字、字母、下划线等组合 */
		String orderId = "123456789086227890123456789AB1";
		
		/** 时间戳 */
		Date parseDate = parseDate("2019-05-28 11:15:10");
		Long timestamp = parseDate.getTime();
		
		/** 签名 */
		String sign = generateSign(merchantNumber,orderId,timestamp,PUBIIC_KEY);
		
		Map<String, Object> map = new HashMap<>();
        map.put("merchantNumber", merchantNumber);
        map.put("orderId", orderId);
        map.put("timestamp", timestamp);
        map.put("sign", sign);
		
        String requestJson = JSON.toJSONString(map);
        
        String result = null;
        Map<String, String> data = null;
		try { 
			//result = doPost("https://test.yxinbao.com/wyjt/api/loaneeData/getData", requestJson); //测试路径
			result = doPost("https://prod.51jt.com/wyjt/api/loaneeData/getData", requestJson);   //正式路径
			
			data = JSON.parseObject(result, new TypeReference<Map<String, String>>() {});
		} catch (Exception e) {
			e.printStackTrace();
		}
        
		String code = data.get("code");
        if(StringUtils.equals(code, "1")){//失败了
        	System.out.println(data.get("msg"));
        	return;
        }
        
		@SuppressWarnings("unchecked")
		List<Map<String, Object>> dataList = (List<Map<String, Object>>) JSON.parse(data.get("data"));
        
		if(dataList != null && dataList.size() > 0){
			for (Map<String, Object> loanee : dataList) {
				String name = (String) loanee.get("name");
				String phoneNo = (String) loanee.get("phoneNo");
				Integer age = (Integer) loanee.get("age");
				String sesamePoints = (String) loanee.get("sesamePoints");
				String qqNo = (String) loanee.get("qqNo");
				String area = (String) loanee.get("area");
				
				System.out.println(name);
				System.out.println(phoneNo);
				System.out.println(age);
				System.out.println(sesamePoints);
				System.out.println(qqNo);
				System.out.println(area);
			}
		}
        System.out.println(data.get("msg"));
        System.out.println(data.get("orderId"));
	}

	private static String generateSign(String merchantNumber, String orderId, Long timestamp, String publicKey) {
		
		String md5String = DigestUtils.md5Hex(String.format("%s%s%s", merchantNumber, orderId, timestamp));
		
		byte[] data = Base64.decodeBase64(md5String); 
		
		PublicKey generatePublicKey = generatePublicKey(publicKey);
		
		byte[] encrypt = encrypt(generatePublicKey,data);
		
		return encodeBase64(encrypt);
	}
	
	
	/**
	 * 加密
	 * 
	 * @param publicKey
	 *            公钥
	 * @param data
	 *            数据
	 * @return 密文
	 */
	public static byte[] encrypt(PublicKey publicKey, byte[] data) {
	
		try {
			Cipher cipher = Cipher.getInstance(TRANSFORMATION, PROVIDER);
			cipher.init(Cipher.ENCRYPT_MODE, publicKey);
			return cipher.doFinal(data);
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e.getMessage(), e);
		} catch (NoSuchPaddingException e) {
			throw new RuntimeException(e.getMessage(), e);
		} catch (InvalidKeyException e) {
			throw new RuntimeException(e.getMessage(), e);
		} catch (IllegalBlockSizeException e) {
			throw new RuntimeException(e.getMessage(), e);
		} catch (BadPaddingException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}
	
	/**
	 * 生成公钥
	 * 
	 * @param keyString
	 *            密钥字符串(BASE64编码)
	 * @return 公钥
	 */
	public static PublicKey generatePublicKey(String keyString) {


		return generatePublicKey(Base64.decodeBase64(keyString));
	}
	
	/**
	 * 生成公钥
	 * 
	 * @param encodedKey
	 *            密钥编码
	 * @return 公钥
	 */
	public static PublicKey generatePublicKey(byte[] encodedKey) {


		try {
			KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM, PROVIDER);
			return keyFactory.generatePublic(new X509EncodedKeySpec(encodedKey));
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e.getMessage(), e);
		} catch (InvalidKeySpecException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}
	
    /**
     * Post String
     *
     * @param url
     *
     * @param headers
     * @param querys
     * @param body
     * @return
     * @throws Exception
     */
    public static String doPost(String url,String body)
            throws Exception {
        HttpClient httpClient = wrapClient(url);
        HttpPost request = new HttpPost(url);
        if (StringUtils.isNotBlank(body)) {
            request.setEntity(new StringEntity(body, "utf-8"));
        }
        HttpResponse response = httpClient.execute(request);
        if (response != null) {
			HttpEntity resEntity = response.getEntity();
			if (resEntity != null) {
				return EntityUtils.toString(resEntity, "utf-8");
			}
		}
        return null;
    }
	
    /**
     * 获取 HttpClient
     * @param host
     * @param path
     * @return
     */
    private static HttpClient wrapClient(String url) {
        HttpClient httpClient = HttpClientBuilder.create().build();
        if (url != null && url.startsWith("https://")) {
            return sslClient();
        }
        return httpClient;
    }
    /**
     * 在调用SSL之前需要重写验证方法，取消检测SSL
     * 创建ConnectionManager，添加Connection配置信息
     * @return HttpClient 支持https
     */
    private static HttpClient sslClient() {
        try {
            // 在调用SSL之前需要重写验证方法，取消检测SSL
            X509TrustManager trustManager = new X509TrustManager() {
                @Override public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
                @Override public void checkClientTrusted(X509Certificate[] xcs, String str) {}
                @Override public void checkServerTrusted(X509Certificate[] xcs, String str) {}
            };
            SSLContext ctx = SSLContext.getInstance(SSLConnectionSocketFactory.TLS);
            ctx.init(null, new TrustManager[] { trustManager }, null);
            SSLConnectionSocketFactory socketFactory = new SSLConnectionSocketFactory(ctx, NoopHostnameVerifier.INSTANCE);
            // 创建Registry
            RequestConfig requestConfig = RequestConfig.custom().setCookieSpec(CookieSpecs.STANDARD_STRICT)
                    .setExpectContinueEnabled(Boolean.TRUE).setTargetPreferredAuthSchemes(Arrays.asList(AuthSchemes.NTLM,AuthSchemes.DIGEST))
                    .setProxyPreferredAuthSchemes(Arrays.asList(AuthSchemes.BASIC)).build();
            Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
                    .register("http", PlainConnectionSocketFactory.INSTANCE)
                    .register("https",socketFactory).build();
            // 创建ConnectionManager，添加Connection配置信息
            PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
            CloseableHttpClient closeableHttpClient = HttpClients.custom().setConnectionManager(connectionManager)
                    .setDefaultRequestConfig(requestConfig).build();
            return closeableHttpClient;
        } catch (KeyManagementException ex) {
            throw new RuntimeException(ex);
        } catch (NoSuchAlgorithmException ex) {
            throw new RuntimeException(ex);
        }
    }
    
	/**
	 * Base64编码.
	 */
	public static String encodeBase64(byte[] input) {
		return new String(Base64.encodeBase64(input));
	}
	
	/**
	 * 日期型字符串转化为日期 格式 { "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm",
	 * "yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm", "yyyy.MM.dd",
	 * "yyyy.MM.dd HH:mm:ss", "yyyy.MM.dd HH:mm" }
	 */
	public static Date parseDate(Object str) {
		if (str == null) {
			return null;
		}
		try {
			return DateUtils.parseDate(str.toString(), parsePatterns);
		} catch (ParseException e) {
			return null;
		}
	}
}
