package com.jxf.svc.utils;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.alibaba.fastjson.JSON;
import com.jxf.svc.model.JsonpRsp;

/**
 * 
 * @类功能说明： jsonp 工具类
 * @类修改者： 
 * @修改日期： 
 * @修改说明： 
 * @公司名称：北京金信服科技有限公司
 * @作者：Administrator 
 * @创建时间：2016年12月19日 下午6:13:58 
 * @版本：V1.0
 */
public final class JsonpUtils {


    
	public static void callback(HttpServletRequest req, HttpServletResponse response,JsonpRsp jsonpRsp) {
		String  jsonpRspStr = JSON.toJSONString(jsonpRsp);		
		String callback = req.getParameter("callback");
		response.setContentType("application/x-javascript");
		response.setCharacterEncoding("UTF-8");
		response.setHeader("P3P", "CP=CURa ADMa DEVa PSAo PSDo OUR BUS UNI PUR INT DEM STA PRE COM NAV OTC NOI DSP COR");
		try {
			PrintWriter out = response.getWriter();
			out.write(callback+ "("+jsonpRspStr+");"); 
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}