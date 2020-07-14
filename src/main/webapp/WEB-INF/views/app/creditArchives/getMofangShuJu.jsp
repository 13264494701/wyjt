<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%@include file="/WEB-INF/views/app/constant.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <title>认证界面</title>
 <%@include file="/WEB-INF/views/app/meta-flex.jsp" %>
    <meta name="keywords" content=""/>
    <meta name="description" content=""/>
    <link rel="stylesheet" href="${mbStatic}/assets/css/public.css?v=<%=version%>" type="text/css">
    <link rel="stylesheet" href="${mbStatic}/assets/css/user.css?v=<%=version%>" type="text/css">
    <script src="${mbStatic}/assets/scripts/base/sea.js?v=<%=version%>21"></script>
    <script src="${mbStatic}/assets/scripts/base/flexible.js?v=<%=version%>"></script>
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
        <div class="pages navbar-fixed">
            <div class="page">
                <div class="page-content" style="background: white">
                    <!--content -->
                    <div class="bg" style="background: white">
                                <style>
                                    .listbox{
                                        border:0;
                                    }
                                    .listbox:after{
                                        background:none;
                                    }
                                    .paysucessed p.p4{
                                        margin:0 0 0 2.68rem
                                    }
                                    .paysucessed img{
                                        width: 5rem;
                                        text-align:center;
                                    }
                                </style>
                        <div class="listbox">
                            <div class="paysucessed">
                            <c:choose>
                                <c:when test="${code eq '0'}">
                                <p style="margin-top: 3rem; text-align:center;"><img src="${mbStatic}/assets/images/debt/success@2x.png"></p>
                                <p style="font-size: 1.5rem;margin-top: 1rem;margin-bottom: .3rem;font-weight: bold; text-align:center;">${message}</p>
                                </c:when>
                            	<c:otherwise>
                                <p style="margin-top: 3rem; text-align:center;"><img src="${mbStatic}/assets/images/debt/err@2x.png"></p>
                                <p style="font-size: 1.5rem;margin-top: 1rem;margin-bottom: .3rem;font-weight: bold; text-align:center;">${message}</p>
                               </c:otherwise>
                            </c:choose>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </section>
</article>
</body>
<script type="text/javascript">
    seajs.use(['zepto','jumpApp'], function ($,jumpApp) {
        var ev = $.support.touch ? 'tap' : 'click';
        $('.gray-Btn')[ev](function(){
            jumpApp.callApp('goarchives');
        });
    });
</script>
</html>
