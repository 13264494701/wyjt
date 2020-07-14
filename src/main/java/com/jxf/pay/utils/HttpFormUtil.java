package com.jxf.pay.utils;

import java.util.Iterator;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jxf.pwithdraw.service.impl.LianlianPayServiceImpl;


public class HttpFormUtil{
	private static final Logger log = LoggerFactory.getLogger(LianlianPayServiceImpl.class);
	/**
	 * 自定义编码格式跳转
	 * 
	 * @param url
	 * @param params
	 * @param charset
	 * @return
	 */
	public static String formForward(String url, Map <String, String> params, String charset)
	{
		StringBuffer formHtml = new StringBuffer();
		formHtml.append("<html>");
		String head = "<head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=" + charset
				+ "\" pageEncoding=\"" + charset + "\" />";
		formHtml.append(head);
		formHtml.append("<title>loading</title>");
		formHtml.append("<style type=\"text/css\">");
		formHtml.append("body{margin:200px auto;font-family: \"宋体\", Arial;font-size: 12px;color: #369;text-align: center;}");
		formHtml.append("#1{height:auto; width:78px; margin:0 auto;}");
		formHtml.append("#2{height:auto; width:153px; margin:0 auto;}");
		formHtml.append("vertical-align: bottom;}");
		formHtml.append("</style>");
		formHtml.append("</head>");
		formHtml.append("<body OnLoad=\"OnLoadEvent();\" >");
		formHtml.append("<div id=\"3\">");
		formHtml.append("Loading...");
		formHtml.append("</div>");

		formHtml.append("<form name=\"forwardForm\" action=\"").append(url).append("\" method=\"POST\">");
		log.info("form表单跳转url:"+url);
		Iterator <String> keyIterator = params.keySet().iterator();
		while (keyIterator.hasNext())
		{
			String key = keyIterator.next();
			formHtml.append("  <input type=\"hidden\" name=\"").append(key).append("\" value=\"")
					.append(params.get(key)).append("\"/>");
			log.info("form表单跳转参数：" + key + "=" + params.get(key));
		}
		formHtml.append("</form>");
		formHtml.append("<SCRIPT LANGUAGE=\"Javascript\">");
		formHtml.append("  function OnLoadEvent(){");
		formHtml.append("    document.forwardForm.submit();");
		formHtml.append("  }");
		formHtml.append("</SCRIPT>");
		formHtml.append("</body>");
		formHtml.append("</html>");

		return formHtml.toString();
	}

	/**
	 * 指定UTF-8编码格式跳转
	 * 
	 * @param url
	 * @param params
	 * @return
	 */
	public static String formForward(String url, Map <String, String> params)
	{
		return formForward(url, params, "utf8");
	}
}
