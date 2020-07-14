<%@ page contentType="text/html;charset=utf-8" language="java"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%@include file="/WEB-INF/views/app/constant.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <title>预支付催收结果-无忧借条</title>
    <%@include file="/WEB-INF/views/app/meta-flex.jsp" %>
    <meta name="keywords" content=""/>
    <meta name="description" content=""/>
    <link rel="stylesheet" href="${mbStatic}/assets/css/public.css?v=<%=version%>" type="text/css">
    <link rel="stylesheet" href="${mbStatic}/assets/css/repay.css?v=<%=version%>" type="text/css">
    <script src="${mbStatic}/assets/scripts/base/sea.js?v=<%=version%>21"></script>
    <script src="${mbStatic}/assets/scripts/base/flexible.js?v=<%=version%>"></script>
    <style>
        .listbox{border:0;}
        .listbox:after{background:none;}
        .paysucessed p.p4{margin:0 0 0 2.68rem;}
        .paysucessed img{ width: 5rem;}
    </style>
</head>
<c:choose>
	<c:when test="${empty appPlatform}">
		<body>	
	</c:when>
	<c:otherwise>
		<body class="${isWeiXin? 'InWeixin':'InWebView'}" data-platform="${appPlatform }"  data-app="51jt">
	</c:otherwise>
</c:choose>
<article class="views" style="max-width:100%">
    <section class="view">
         <jsp:include page="/WEB-INF/views/app/header-public.jsp">
            <jsp:param name="title" value="预支付"></jsp:param>
            <jsp:param name="url" value="back2app"></jsp:param>
        </jsp:include>
        <div class="pages navbar-fixed">
            <div class="page">
                <div class="page-content" style="background: white">
                    <!--content -->
                    <div class="bg" style="background: white">
                        <div class="listbox">
                            <div class="paysucessed">
                                <c:if test="${code == 0}">
	                                <p style="margin-top: 3rem;"><img src="${mbStatic}/assets/images/debt/success@2x.png"></p>
	                                <p style="font-size: 1.2rem;margin-top: 1rem;margin-bottom: .3rem;font-weight: bold">支付成功</p>
	                                <p style="margin:2rem;font-size: 1rem;color: RGB(109,109,109);">您可以在催收仲裁模块的“查看记录”中查看催收进度哦~</p>
                                </c:if>
                                <c:if test="${code < 0}">
	                                <p style="margin-top: 3rem;"><img src="${mbStatic}/assets/images/debt/err@2x.png"></p>
	                                <p style="font-size: 1.2rem;margin-top: 1rem;margin-bottom: .3rem;font-weight: bold">支付失败</p>
	                                <p style="margin:2rem;font-size: 1rem">失败原因：${message}</p>
                                </c:if>
                            </div>
                        </div>
                        <div class="btn">
                            <style>
                                .btn a{display: inline-block;width: 49%;}
                            </style>
                            <c:if test="${code == 0}">
	                            <a class="gray-Btn" href="javascript:void(0);" style="background: RGB(248,181,42);">返回首页</a>
	                            <a class="red-Btn" href="javascript:void(0);">查看催收进度</a>
                            </c:if>
                            <c:if test="${code < 0}">
                            	<a class="gray-Btn" href="javascript:void(0);" style="background: RGB(248,181,42);width: 100%;">返回首页</a>
                            </c:if>
                        <form id="dataForm" action="${pageContext.request.contextPath}/app/wyjt/collection/goColSchedule" method="post">
							<input type="hidden" name="_header" value="${memberToken}">
	                   		<input type="hidden" name="_type" value="h5">
	                   		<input type="hidden" id="collectionId" name="collectionId" value="${collectionId}" />
                        </form>
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
        var $=require('zepto'),ev = $.support.touch ? 'tap' : 'click', call = require('jumpApp'),loanId=0;
        var detailId='${detailId}';
        $('.red-Btn')[ev](function(){
        	$("#dataForm").submit();
        });
        $('.gray-Btn')[ev](function(){
            backIndex();
        });
        //返回首页
        function backIndex () {
            call.callApp('backIndexPage');
        }
    });
    seajs.use(['payResultV2', 'jumpApp']);
</script>
</html>