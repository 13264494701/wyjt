<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/inc/function.jsp" %>
<%
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
    <title>联系我们-无忧借条</title>
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
    <div class="aboutus">
        <%@include file="menus.jsp" %>
        <div class="linkus">
            <img src="<%=cdnUrl%>images/v1/map.png?de">
        </div>
    </div>
    <jsp:include page="/common/new-footer.jsp"></jsp:include>
</div>
</body>
</html>
<!-- created in 2016-04-12 16:49-->