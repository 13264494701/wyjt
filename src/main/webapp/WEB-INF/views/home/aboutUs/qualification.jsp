<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/inc/function.jsp" %><%
    if (serverName.contains("yxinbao.com")) {
        String urlPath = request.getRequestURI().toString();
        String urlQuery = request.getQueryString();
        if (urlQuery != null) urlPath += "?" + urlQuery;
        response.sendRedirect("http://www.51jt.com" + urlPath);
        return;
    }%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8"/>
    <title>安全资质-无忧借条</title>
    <meta name="keywords" content="无忧借条"/>
    <meta name="description" content="无忧借条"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"/>
    <link rel="stylesheet" href="<%=cdnUrl%>p1.css?v=<%=version%>" type="text/css">
    <link rel="stylesheet" href="<%=cdnUrl%>p/aboutUs.css?v=<%=version%>" type="text/css">
    <script src="<%=cdnUrlJS%>assets/scripts/base/sea.js?v=<%=version%>" type="text/javascript"></script>
</head>
<body>

<div class="v1-topbg">
    <jsp:include page="/common/new-header.jsp"></jsp:include>
    <div class="anqzz ">
        <div class="bannbg">
            <div class="w1 bann clearfix">
                <div class="bann-txt">
                    <p><span>安全认证</span></p>

                    <p>无忧借条是北京友信宝网络科技有限公司运营的安全便捷的电子借条类金融工具</p>

                    <p>无忧借条通过为用户提供CA认证、第三方数字存证、200多家公证处等多方认证的电子借条，以及专业的欠款催收服务维护广大用户的财产利益，并提供0风险理财服务。</p></div>
                <div class="bann-img"></div>
            </div>
        </div>
        <div class="w1 conbox">
            <img src="<%=cdnUrl%>images/v1/animg2.png??">
            <%--<img src="<%=cdnUrl%>images/v1/animg3.png">--%>
            <img src="<%=cdnUrl%>images/v1/animg4.png">
        </div>
    </div>
    <jsp:include page="/common/new-footer.jsp"></jsp:include>
</div>
</body>
</html>
<!-- created in 2016-04-12 16:49-->