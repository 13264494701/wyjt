package com.jxf.test;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jxf.mem.service.MemberVideoVerifyService;
import com.jxf.mem.service.impl.MemberVideoVerifyServiceImpl;
import com.jxf.svc.config.Global;
import com.jxf.svc.security.MD5Utils;
import com.jxf.svc.security.jwt.JwtUtil;
import com.jxf.web.model.ResponseData;

public class MyTest implements BeanPostProcessor{

	
	@Test
	public void test() throws ClientProtocolException, IOException {
		
//		XmlBeanFactory ss = new XmlBeanFactory(null);
//		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext();
//		ss.getBean("");
//		
//		ResponseData token = JwtUtil.verifyToken("gjjzeyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpZCI6MjQ3NjI4MSwiZXhwIjoxNTY2Mjk1MjY0LCJkZXZpY2VUb2tlbiI6bnVsbH0.r7XM9eSS1IOLk_nY9-r9tlCq1fjagJdpHIqIlG8tjXU");
//		JSONObject payload = (JSONObject) JSON.toJSON(token.getResult());
//		Long id = payload.getLong("id");
		String readFileToString = FileUtils.readFileToString(new File("/usr/local/staticfile/upload/sjmh/report_data/2019/09/02/365925025930416128.txt"),"utf-8");
		System.err.println(readFileToString);
//		System.err.println(token.getCode());
	}
	
	
	
	
    
}
