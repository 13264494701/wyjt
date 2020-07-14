package com.jxf.rc.utils;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.protocol.Protocol;

import com.jxf.mem.utils.MySSLProtocolSocketFactory;

import com.jxf.svc.utils.JSONUtil;
/**
 * @author 淘气
 * @createtime 2016年9月26日下午8:58:24
 */
public class CreditRadarAction extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static String getXinyan(String id_no, String id_name, String phone_no, String bankcard_no)
			throws ServletException, IOException {
//		Map<String, String> headers = new HashMap<String, String>();
//		String PostString = null;
		// String trade_date = new SimpleDateFormat("yyyyMMddHHmmss").format(new
		// Date());// 订单日期
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		String trade_date = sdf.format(new Date());
		String trans_id = "" + System.currentTimeMillis();// 商户订单号
		String XmlOrJson = "";
		/** 组装参数 **/
		Map<Object, Object> ArrayData = new HashMap<Object, Object>();
		ArrayData.put("member_id", ThirdPartyUtils.member_id);
		ArrayData.put("terminal_id", ThirdPartyUtils.terminal_id);
		ArrayData.put("trade_date", trade_date);
		ArrayData.put("trans_id", trans_id);
		ArrayData.put("id_no", id_no);
		ArrayData.put("id_name", id_name);
		ArrayData.put("phone_no", phone_no);
		ArrayData.put("bankcard_no", bankcard_no);
		ArrayData.put("versions", ThirdPartyUtils.versions);
		ArrayData.put("industry_type", "B1");// 参照文档传自己公司对应的行业参数
		XmlOrJson =JSONUtil.toJson(ArrayData);
		String base64str = SecurityUtil.Base64Encode(XmlOrJson);
		base64str = base64str.replaceAll("\r|\n*", "");
		/** rsa加密 **/
		File pfxfile = new File(ThirdPartyUtils.pfxpath);
		if (!pfxfile.exists()) {
			throw new RuntimeException("私钥文件不存在！");
		}

		String data_content = RsaCodingUtil.encryptByPriPfxFile(base64str, ThirdPartyUtils.pfxpath, ThirdPartyUtils.pfxpwd);// 加密数据
		String response = "";
		HttpClient http = new HttpClient();
		Protocol myhttps = new Protocol("https", new MySSLProtocolSocketFactory(), 443);
		Protocol.registerProtocol("https", myhttps);
		PostMethod post = new PostMethod(ThirdPartyUtils.xinyanurl);
		try {
			post.getParams().setContentCharset("UTF-8"); 
			NameValuePair[] data = { new NameValuePair("member_id", ThirdPartyUtils.member_id),
					new NameValuePair("terminal_id", ThirdPartyUtils.terminal_id), new NameValuePair("data_type", "json"),
					new NameValuePair("data_content", data_content), };
			post.setRequestBody(data);
			http.executeMethod(post);
			response = post.getResponseBodyAsString();
		} catch (HttpException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return response;

	}

}
