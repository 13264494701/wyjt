package com.jxf.pay.utils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.SimpleHttpConnectionManager;
import org.apache.commons.httpclient.methods.InputStreamRequestEntity;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.params.HttpClientParams;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.httpclient.protocol.Protocol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.jxf.svc.utils.Exceptions;

/**
 * Http Post
 * @author Aillans
 *
 */
public class HttpPoster {
	private static final Logger logger = LoggerFactory.getLogger(HttpPoster.class);
	
	public HttpPoster() {
	}

	private String url;

	private String charset;

	public void setUrl(String url) {
		this.url = url;
	}

	public void setCharset(String charset) {
		this.charset = charset;
	}

	public HttpPoster(String url, String charset) {
		super();
		this.url = url;
		this.charset = charset;
	}

	public HttpPoster(String url) {
		super();
		this.url = url;
		this.charset = "UTF-8";
	}

	/**
	 * 静态post
	 * @param url
	 * @param charset
	 * @param parameters
	 * @return
	 * @throws FuiouException
	 */
	public static int post(final Map<String, String> parameters, String url, String charset) throws Exception {
		HttpPoster post = new HttpPoster(url, charset);
		return post.post(parameters);
	};

	public static String getResponseString(final Map<String, String> parameters, String url, String charset) throws Exception {
		HttpPoster post = new HttpPoster(url, charset);
		return post.postStr(parameters);
	};

	public static int post(final Map<String, String> parameters, String url) throws Exception {
		HttpPoster post = new HttpPoster(url, "UTF-8");
		return post.post(parameters);
	};

	/**
	 * 发送参数包
	 * @param parameters
	 * @return
	 * @throws IOException
	 */
	public int post(final Map<String, String> parameters) {
		return post(new PostMethodCallback() {

			@Override
			public void doInPostMethod(PostMethod postMethod) {
//				System.out.println("发送URL==" + url);
//				System.out.println("发送参数==" + parameters);
				NameValuePair[] nameValuePairs = new NameValuePair[parameters.size()];
				Set<Entry<String, String>> set = parameters.entrySet();
				int i = 0;
				//设置查询参数
				for (Entry<String, String> entry : set) {
					NameValuePair pair = new NameValuePair(entry.getKey(), entry.getValue());
					nameValuePairs[i] = pair;
					i++;
				}
				//发送参数包
				postMethod.setRequestBody(nameValuePairs);

			}
		});
	}

	public static String postJson(final Map<String, String> parameters, String url, String charset) {
		HttpPoster post = new HttpPoster(url, charset);
		return post.postStrNew(parameters);
	}
	
	public String postStrNew(final Map<String, String> parameters){
		return postStr(new PostMethodCallback() {

			@Override
			public void doInPostMethod(PostMethod postMethod) {
				
			RequestEntity re = null;
			try {
				re = new StringRequestEntity(JSON.toJSONString(parameters),"application/json","UTF-8");
			} catch (Exception e) {
				logger.error(Exceptions.getStackTraceAsString(e));
			}
	         postMethod.setRequestEntity(re); 
			}
		});
	}
	
	public String postStr(final Map<String, String> parameters) {
		//System.out.println("发送url==" + url);
		return postStr(new PostMethodCallback() {

			@Override
			public void doInPostMethod(PostMethod postMethod) {
				//System.out.println("发送参数==" + parameters);
				NameValuePair[] nameValuePairs = new NameValuePair[parameters.size()];
				Set<Entry<String, String>> set = parameters.entrySet();
				int i = 0;
				//设置查询参数
				for (Entry<String, String> entry : set) {
					NameValuePair pair = new NameValuePair(entry.getKey(), entry.getValue());
					nameValuePairs[i] = pair;
					i++;
				}
				//发送参数包
				postMethod.setRequestBody(nameValuePairs);

			}
		});
	}

	/**
	 * 使用http协议发送xmltext到url
	 * 
	 * @param url
	 *            将要发送的地址
	 * @param xmltext
	 *            将要发送的内容
	 * @return http返回码
	 */
	public int post(final String body) throws Exception {
		return post(new PostMethodCallback() {

			@Override
			public void doInPostMethod(PostMethod postMethod) {

				try {
					InputStream instream = new ByteArrayInputStream(body.getBytes(charset));
					postMethod.setRequestEntity(new InputStreamRequestEntity(instream, body.getBytes(charset).length));
				} catch (UnsupportedEncodingException e) {
					logger.error(Exceptions.getStackTraceAsString(e));
				}
			}
		});
	}

	/**
	 * 使用http协议发送xmltext到url
	 * 
	 * @param url
	 *            将要发送的地址
	 * @param xmltext
	 *            将要发送的内容
	 * @return http返回码
	 * @throws FuiouException 
	 */
	private int post(PostMethodCallback callback) {
		HttpClient httpclient = null;
		PostMethod xmlpost = null;
		try {
			//https设置
			if (url.indexOf("https://") != -1) {
				//创建SSL连接
				@SuppressWarnings("deprecation")
				Protocol myhttps = new Protocol("https", new MySSLSocketFactory(), 443);
				Protocol.registerProtocol("https", myhttps);
			}
			httpclient = new HttpClient(new HttpClientParams(), new SimpleHttpConnectionManager());
			httpclient.getHttpConnectionManager().getParams().setConnectionTimeout(1000 * 10);//设连接超时时间
			httpclient.getHttpConnectionManager().getParams().setSoTimeout(1000 * 10);//设读取数据超时时间
			xmlpost = new PostMethod(url);
			httpclient.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, charset);
			httpclient.getParams().setContentCharset(charset);
			//xmlpost.setRequestHeader("content-type", "text/xml; charset=" + charset);
			//内部回调，发送数据，设置参数用
			callback.doInPostMethod(xmlpost);
			//执行请求
			int responseStatCode = httpclient.executeMethod(xmlpost);
			//获取返回信息
			InputStream ips = xmlpost.getResponseBodyAsStream();
			List<Byte> byteList = new ArrayList<Byte>();
			int is = 0;
			while ((is = ips.read()) != -1)
				byteList.add((byte) is);
			byte[] allb = new byte[byteList.size()];
			for (int j = 0; j < byteList.size(); j++)
				allb[j] = byteList.get(j);
			String responseString = new String(allb, charset);
			if (url.indexOf("https://") != -1)
				Protocol.unregisterProtocol("https");
			return responseStatCode;
		} catch (IOException e) {
			logger.error("报文发送到[{}]失败:{}",url,Exceptions.getStackTraceAsString(e));
			throw new IllegalArgumentException("通信异常");
		} finally {
			if (xmlpost != null){
				xmlpost.releaseConnection();
			}
			if (httpclient != null){
				httpclient.getHttpConnectionManager().closeIdleConnections(0);
			}
		}
	}

	private String postStr(PostMethodCallback callback) {
		HttpClient httpclient = null;
		PostMethod xmlpost = null;
		try {
			//https设置
			if (url.indexOf("https://") != -1) {
				//创建SSL连接
				@SuppressWarnings("deprecation")
				Protocol myhttps = new Protocol("https", new MySSLSocketFactory(), 443);
				Protocol.registerProtocol("https", myhttps);
			}
			httpclient = new HttpClient(new HttpClientParams(), new SimpleHttpConnectionManager());
			httpclient.getHttpConnectionManager().getParams().setConnectionTimeout(1000 * 10);
			httpclient.getHttpConnectionManager().getParams().setSoTimeout(1000 * 10);//设读取数据超时时间
			xmlpost = new PostMethod(url);
			httpclient.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, charset);
			httpclient.getParams().setContentCharset(charset);
			//xmlpost.setRequestHeader("content-type", "text/xml; charset=" + charset);
			//内部回调，发送数据，设置参数用
			callback.doInPostMethod(xmlpost);
			//执行请求
			int responseStatCode = httpclient.executeMethod(xmlpost);
			//获取返回信息
			InputStream ips = xmlpost.getResponseBodyAsStream();
			List<Byte> byteList = new ArrayList<Byte>();
			int is = 0;
			while ((is = ips.read()) != -1)
				byteList.add((byte) is);
			byte[] allb = new byte[byteList.size()];
			for (int j = 0; j < byteList.size(); j++)
				allb[j] = byteList.get(j);
			String responseString = new String(allb, charset);
//			System.out.println("HTTP返回码=" + responseStatCode);
//			System.out.println("应答数据=" + responseString);
			if (url.indexOf("https://") != -1)
				Protocol.unregisterProtocol("https");
			return responseString;
		} catch (IOException e) {
			logger.error("报文发送到[{}]失败:{}",url,Exceptions.getStackTraceAsString(e));
			throw new IllegalArgumentException("通信异常");
		} finally {
			if (xmlpost != null){
				xmlpost.releaseConnection();
			}
			if (httpclient != null){
				httpclient.getHttpConnectionManager().closeIdleConnections(0);
			}
		}
	}

	/**
	 * PostMethod回调处理
	 *
	 */
	public interface PostMethodCallback {
		public void doInPostMethod(PostMethod postMethod);
	}

}
