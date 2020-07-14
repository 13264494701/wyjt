<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<%@include file="/WEB-INF/views/app/constant.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <title>升级攻略-无忧借条</title>
    <%@include file="../meta-flex.jsp" %>
    <link rel="stylesheet" href="${mbStatic}/assets/css/public.css" type="text/css">
    <link rel="stylesheet" href="${mbStatic}/assets/css/h5/credit-grade.css" type="text/css">
    <script src="${mbStatic}/assets/scripts/base/sea.js"></script>
    <script src="${mbStatic}/assets/scripts/base/flexible.js"></script>
</head>
<c:choose>
<c:when test="${empty appPlatform}">
<body>
</c:when>
<c:otherwise>
<body class="${isWeiXin? 'InWeixin':'InWebView'}" data-platform="${appPlatform}"  data-app="51jt">
</c:otherwise>
</c:choose>
<article class="views docbody">
    <section class="view">
        <jsp:include page="../header-public.jsp">
            <jsp:param name="title" value="升级攻略"></jsp:param>
        </jsp:include>
        <div class="pages navbar-fixed  ">
            <div class="page">
                <div class="page-content" style="padding-top: 0;background-size: contain;">
                    <div class="xyimg"></div>
                </div>
            </div>
        </div>
    </section>
</article>
<script>
    seajs.use(['zepto', "jumpApp"], function ($, jumpApp) {
        var ev = $.support.touch ? 'tap' : 'click';
        $('.goBackIndex')[ev](function () {
            backIndex();
        });

        function backIndex() {
            jumpApp.callApp('backIndexPage');
        }
    });
</script>
</body>
</html>
