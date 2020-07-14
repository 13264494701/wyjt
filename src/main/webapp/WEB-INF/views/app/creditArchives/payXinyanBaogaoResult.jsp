<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%@include file="/WEB-INF/views/app/constant.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <title>信用报告支付</title>
    <%@include file="/WEB-INF/views/app/meta-flex.jsp" %>
    <meta name="keywords" content=""/>
    <meta name="description" content=""/>
    <link rel="stylesheet" href="${mbStatic}/assets/css/public.css?v=<%=version%>" type="text/css">
    <link rel="stylesheet" href="${mbStatic}/assets/css/user.css?v=<%=version%>" type="text/css">
    <script src="${mbStatic}/assets/scripts/base/sea.js?v=<%=version%>21"></script>
    <script src="${mbStatic}/assets/scripts/base/flexible.js?v=<%=version%>"></script>
    <style>
        .listbox{border:0;}
        .listbox:after{background:none;}
        .paysucessed p.p4{margin:0 0 0 2.68rem;}
        .paysucessed img{ width: 5rem;}
        .red-Btn{
            color: white;
		    display: block;
		    background: red;
		    height: 3rem;
		    line-height: 3rem;
		    margin: 0 1rem;
		    border-radius: .3rem;
		    font-size: 1.1rem;
		    text-align:center;
		}
        }
    </style>
</head>
<c:choose>
	<c:when test="${empty appPlatform}">
		<body>	
	</c:when>
	<c:otherwise>
		<body class="${isWeiXin? 'InWeixin':'InWebView'}" data-platform="${appPlatform }"  data-app="${appName}">
		
	</c:otherwise>
</c:choose>
<article class="views" style="max-width:100%">
    <section class="view">
        <div class="pages navbar-fixed">
            <div class="page">
                <div class="page-content" style="background: white">
                    <!--content -->
                    <div class="bg" style="background: white">
                        <div class="listbox">
                            <div class="paysucessed" style="text-align: center;">
                            	<c:choose>
                            	<c:when test="${code eq '0'}">
	                                <p style="margin-top: 3rem;"><img src="${mbStatic}/assets/images/debt/success@2x.png"></p>
	                                <p style="font-size: 1.2rem;margin-top: 1rem;margin-bottom: .3rem;font-weight: bold">支付成功</p>
	                                <p style="margin:2rem;font-size: 1rem;color: RGB(109,109,109);">支付金额<span style="color: red;">${showMoney}</span>元</p>
                                </c:when>
                            	<c:otherwise>
	                                <p style="margin-top: 3rem;"><img src="${mbStatic}/assets/images/debt/success@2x.png"></p>
	                                <p style="font-size: 1.2rem;margin-top: 1rem;margin-bottom: .3rem;font-weight: bold">支付失败</p>
	                                <p style="margin:2rem;font-size: 1rem;">${message}</p>
                               	</c:otherwise>
                            </c:choose>
                            </div>
                        </div>
                        <div class="btn">
                        <c:choose>
                          <c:when test="${code eq '0'}">
                            <a class="red-Btn" href="javascript:void(0);">查看信用报告(一元查看更多)</a>
                            </c:when>
                        </c:choose>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </section>
</article>
</body>
<script>
    define('payResultV2', ['zepto', 'jumpApp'], function (require, exports, module) {
        var $=require('zepto'),ev = $.support.touch ? 'tap' : 'click', call = require('jumpApp'),yxbId='${yxbId}';

        $('.red-Btn')[ev](function(){
        	goSeecreport();
        });

        //查看信用报告
        function goSeecreport () {
			var option={"userId":yxbId}
            call.callApp('seecreport',option);
        }
    });

    seajs.use(['payResultV2', 'jumpApp']);
</script>
</html>

