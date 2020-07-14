<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<%@include file="/WEB-INF/views/app/constant.jsp" %>
<html>
<head>
    <title>找专业出借人-无忧借条</title>
    <%@include file="/WEB-INF/views/include/head.jsp" %>
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <link rel="stylesheet" href="${mbStatic}/assets/css/public.css?v=<%=version%>" type="text/css">
    <link rel="stylesheet" href="${mbStatic}/assets/css/repay.css?v=<%=version%>" type="text/css">
    <script src="${mbStatic}/assets/scripts/base/sea.js?v=<%=version%>"></script>
    <script src="${mbStatic}/assets/scripts/base/flexible.js?v=<%=version%>"></script>
    <style>
        .listbox {
            border: 0;
        }

        .listbox:after {
            background: none;
        }

        .paysucessed p.p4 {
            margin: 0 0 0 2.68rem;
        }

        .paysucessed img {
            width: 5rem;
        }
    </style>
</head>
<c:choose>
<c:when test="${empty appPlatform}">
<body>
</c:when>
<c:otherwise>
<body class="${isWeiXin? 'InWeixin':'InWebView'}" data-platform="${appPlatform}" data-app="51jt">
</c:otherwise>
</c:choose>
<article class="views" style="max-width:100%">
    <section class="view">

        <div class="pages navbar-fixed">
            <div class="page">
                <div class="page-content" style="background: white;padding-top: 0;">

                    <div class="bg" style="background: white">
                        <c:if test="${status==1}">
                            <div class="listbox">
                                <div class="paysucessed">
                                    <p style="margin-top: 3rem;"><img
                                            src="${mbStatic}/assets/images/debt/success@2x.png"></p>
                                    <p style="font-size: 1.2rem;margin-top: 1rem;margin-bottom: .3rem;font-weight: bold">
                                        申请成功</p>
                                    <p style="margin:2rem;font-size: 1rem;color: RGB(109,109,109);">${msg}</p>
                                </div>
                            </div>
                        </c:if>
                        <c:if test="${status==0}">
                            <div class="listbox">
                                <div class="paysucessed">
                                    <p style="margin-top: 3rem;"><img
                                            src="${mbStatic}/assets/images/debt/err@2x.png"></p>
                                    <p style="font-size: 1.2rem;margin-top: 1rem;margin-bottom: .3rem;font-weight: bold">
                                        申请失败</p>
                                    <p style="margin:2rem;font-size: 1rem;color: RGB(109,109,109);">${msg}</p>
                                </div>
                            </div>
                        </c:if>
                        <a class="red-Btn" href="javascript:void(0);" style="width: 100%;">返回首页</a>
                    </div>
                </div>
            </div>
        </div>
    </section>
</article>
</body>
<script>
    define('payResultV2', ['zepto', 'jumpApp'], function (require, exports, module) {
        var $ = require('zepto'), ev = $.support.touch ? 'tap' : 'click', call = require('jumpApp');
        $('.red-Btn')[ev](function () {
            backIndex();
        });

        function backIndex() {
            call.callApp('backIndexPage');
        }
    });
    seajs.use(['payResultV2', 'jumpApp']);
</script>
</html>