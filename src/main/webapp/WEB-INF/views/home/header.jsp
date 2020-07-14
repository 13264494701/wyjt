<%@ page contentType="text/html;charset=utf-8" language="java"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!--头部开始-->
<div class="w1 v1-top ">
    <div class="logo"><a href="javascript:;" class="index"><img src="${homeStatic}/assets/images/logo.png"></a></div>
    <div class="nav clear">
        <ul>
            <li><a href="javascript:;" id="index">首页</a></li>
            <li><a href="javascript:;" id="download">下载</a></li>
            <li><a href="javascript:;" id="charge-list">业务服务</a></li>
            <li><a href="javascript:;" id="intro">关于我们</a></li>

        </ul>
    </div>
   <%--  <%
        if (userHeader == null) {%> --%>
    <style>
        .v1-top .telphone{
            width: 318px;
       /*  } */
    </style>
    <div class="telphone"><a id="_pingansec_bottomimagesmall_brand" href="http://si.trustutn.org/info?sn=468180509035457714210&certType=1"><img src="http://v.trustutn.org/images/cert/brand_bottom_small.jpg" style="margin-right: 20px;"/></a><em></em>400-6688-658</div>
    <%-- <% } else {%>
    <div class="v1-uheader">
        <span class="v1-img">
           <img src="<%=userHeader.getIconAddr()%>" width="36" height="36">
        </span>
        <span class="br"><%=userHeader.getUsername()%></span>
        <a href="/user/logout.jsp<%=urlPath%>">退出</a>
    </div>
    <%
        }
    %> --%>
</div>
<!--头部结束--========================================-->
<script src="${homeStatic}/assets/jquery/jquery-1.8.3.js" type="text/javascript"></script>
<link href="${homeStatic}/assets/bootstrap/2.3.1/css_${not empty cookie.theme.value ? cookie.theme.value : 'cerulean'}/bootstrap.min.css" type="text/css" rel="stylesheet" />
<script src="${homeStatic}/assets/bootstrap/2.3.1/js/bootstrap.min.js" type="text/javascript"></script>
<link href="${homeStatic}/assets/jquery-select2/3.4/select2.min.css" rel="stylesheet" />
<script src="${homeStatic}/assets/jquery-select2/3.4/select2.min.js" type="text/javascript"></script>
<link href="${homeStatic}/assets/jquery-validation/1.11.0/jquery.validate.min.css" type="text/css" rel="stylesheet" />
<script src="${homeStatic}/assets/jquery-validation/1.11.0/jquery.validate.min.js" type="text/javascript"></script>
<script src="${homeStatic}/assets/jquery-validation/1.11.0/jxf.validate.js" type="text/javascript"></script>
<link href="${homeStatic}/assets/jquery-jbox/2.3/Skins/Bootstrap/jbox.min.css" rel="stylesheet" />
<script src="${homeStatic}/assets/jquery-jbox/2.3/jquery.jBox-2.3.min.js" type="text/javascript"></script>
<script src="${homeStatic}/assets/js/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
<script src="${homeStatic}/assets/common/mustache.min.js" type="text/javascript"></script>
<link href="${homeStatic}/assets/common/jinxinfu.min.css" type="text/css" rel="stylesheet" />
<script src="${homeStatic}/assets/common/jinxinfu.min.js" type="text/javascript"></script>
<script src="${homeStatic}/assets/rsa/jsbn.js" type="text/javascript"></script>
<script src="${homeStatic}/assets/rsa/prng4.js" type="text/javascript"></script>
<script src="${homeStatic}/assets/rsa/rng.js" type="text/javascript"></script>
<script src="${homeStatic}/assets/rsa/rsa.js" type="text/javascript"></script>
<script src="${homeStatic}/assets/rsa/base64.js" type="text/javascript"></script>
<script>
var href = location.href;

if(href.indexOf('/index') > -1) {
	$("#index").addClass('check');
}
if(href.indexOf('/download') > -1) {
	$("#download").addClass('check');
}
if(href.indexOf('/charge_list') > -1) {
	$("#charge_list").addClass('check');
}
if(href.indexOf('/intro') > -1) {
	$("#intro").addClass('check');
}
	$("#index").on("click",function(){
		$("#index").addClass("check");
		window.location.href="index";
	});
	$(".index").on("click",function(){
		window.location.href="index";
	});
	$("#download").on("click",function(){
		$("#download").addClass("check");
		window.location.href="download";
	});
	$("#charge-list").on("click",function(){
		$("#charge-list").addClass("check");
		window.location.href="${home}/member/charge_list";
	});
</script>