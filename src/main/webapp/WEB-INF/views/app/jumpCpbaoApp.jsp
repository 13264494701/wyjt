<%@ page contentType="text/html;charset=UTF-8" language="java" %><%!

public static int getV(String agent){
	int v = 0;
	try{
		String v_str = agent.substring(agent.indexOf("MicroMessenger/")+" MicroMessenger".length(),agent.indexOf(" NetType"));
		int v_int = Integer.valueOf(v_str.replaceAll("\\.", ""));
		v = v_int;
	}catch(Exception e ){
		
	}	
	return v;
}

%><%
	String request_host_tmp = request.getServerName();
	if (request_host_tmp.contains("www.51jt.com")) {
		response.sendRedirect("https://www.51jt.com/jumpCpbaoApp.jsp" + (request.getQueryString() != null ? "?" + request.getQueryString() : ""));
		return;
	}
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <title>无忧借条</title>
    <meta content="minimum-scale=1.0, width=device-width, maximum-scale=1.0, user-scalable=no" name="viewport" />
</head>
<body style=" margin: 0; padding: 0;background:#f8f7f5;">
<%

    String iosDLUrl = "https://itunes.apple.com/cn/app/%E6%97%A0%E5%BF%A7%E5%80%9F%E6%9D%A1-%E7%94%B5%E5%AD%90%E5%80%9F%E6%9D%A1%E5%80%9F%E6%8D%AE/id1321795027?l=zh&ls=1&mt=8";
    String agent  =  request.getHeader("user-agent");
    if(agent == null)agent="";
    agent = agent.toUpperCase();

       
if(agent.indexOf("MICROMESSENGER")!=-1){
	

        String src_img = "";
		if(agent.indexOf("IPHONE") != -1){
        	
			src_img = "https://www.51jt.com/upload/2015/8/13/yxbao_144211397464612.jpg";
        	
        }else{
        	
        	src_img = "https://www.51jt.com/upload/2015/8/13/yxbao_14421139746461.jpg";
        	
        }

%>
<div  style="text-align:center;color:#000; font-size:12px; font-family:'微软雅黑';">
<img src="<%=src_img%>" width="100%">
</div>
<%}else{
	String url ="http://www.51jt.com/app/";
	 response.sendRedirect(url);
	
}%>