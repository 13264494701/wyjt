<%@page import="com.teamshopping.teamshopping.util.SYSUtil" %>
<%@page contentType="text/html; charset=utf-8" %>
<%@include file="/wap/inc/function.jsp" %>
<%
    String docTitle = "首页";
%><!DOCTYPE html>
<html>
<head>
    <title><%=docTitle%>-无忧借条</title>
    <%@include file="/wap/inc/meta.jsp" %>
    <meta name="keywords" content="无忧借条"/>
    <meta name="description" content="无忧借条"/>
    <link rel="stylesheet" href="<%=httpUrl%>assets/css/public.css?v=<%=version%>" type="text/css">
    <link rel="stylesheet" href="<%=httpUrl%>assets/css/v1/wechat.css?v=<%=version%>" type="text/css">
    <script src="<%=httpUrl%>assets/scripts/sea-modules/seajs/2.3.0/sea.js?v=<%=version%>"></script>
    <script src="<%=httpUrl%>assets/scripts/base/flexible.js?v=<%=version%>"></script>
</head>

<body<%=appPlatform.equals("") ? "" : " class=\"" + (isWeiXin ? "InWeixin" : "InWebView") + "\" data-platform=\"" + appPlatform + "\""%>>
<article class="views docBody">
    <section class="view">
        <jsp:include page="../inc/header-public.jsp">
            <jsp:param name="title" value="<%=docTitle%>"></jsp:param>
        </jsp:include>
        <div class="pages navbar-fixed">
            <div class="page">
                <div class="page-content">
                        {body}

                </div>
            </div>
        </div>
    </section>
</article>
<jsp:include page="../inc/footer.jsp"></jsp:include>
<script>
</script>
</body>
</html>