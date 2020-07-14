<%@ page import="java.util.Enumeration" %>
<%

    String version = "0222";
    String webPath = "https://www.51jt.com/wap/";      //手机wap 配置目录
    String webViewPath = "https://www.51jt.com/webView/";     //手机webView配置目录
    //String httpUrl = "http://www.yxinbao.com/wrap/"; //cdn配置
    String httpUrl = "https://www.51jt.com/wap/";
    String cdnUrl = "https://image.51jt.com/51jt/";
    String cdnUrlJS = "https://image.51jt.com/wap/";
    String appName = "51jt";
    String appHome = "https://www.51jt.com";
    String serverName = request.getServerName();
    String designMode = "v1"; // v1 ： 设计图尺寸为750的专用  v0: 尺寸为640
    String headerReferer = request.getHeader("Referer");
%>
