<%@page import="com.teamshopping.teamshopping.util.SYSUtil" %>
<%@page contentType="text/html; charset=utf-8" %>
<%@include file="/wap/inc/function.jsp" %>
<%
    String docTitle = "安全认证";
%><!DOCTYPE html>
<html>
<head>
    <title><%=docTitle%></title>
    <%@include file="/wap/inc/meta.jsp" %>
    <meta name="keywords" content="无忧借条"/>
    <meta name="description" content="无忧借条"/>
    <link rel="stylesheet" href="<%=httpUrl%>assets/css/public.css?v=<%=version%>" type="text/css">
    <link rel="stylesheet" href="<%=httpUrl%>assets/css/v1/wechat.css?v=<%=version%>1" type="text/css">
    <script src="<%=httpUrl%>assets/scripts/sea-modules/seajs/2.3.0/sea.js?v=<%=version%>"></script>
    <script src="<%=httpUrl%>assets/scripts/base/flexible.js?v=<%=version%>"></script>
</head>

<body<%=appPlatform.equals("") ? "" : " class=\"InWeixin\"" + "\" data-platform=\"" + appPlatform + "\""%>>
<article class="views docBody">
    <section class="view">
        <jsp:include page="../inc/header-public.jsp">
            <jsp:param name="title" value="<%=docTitle%>"></jsp:param>
        </jsp:include>
        <div class="pages navbar-fixed">
            <div class="page">
                <div class="page-content">
                    <div class="about51 quality">
                        <div class="logo"></div>

                        <div class="txt1">
                            <h3>无忧借条背景介绍</h3>
                            <p>
                                无忧借条是友信宝商业保理（天津）有限公司、无忧借条融资租赁（天津）有限公司、友信商业保理（天津）有限公司、北京环球互联网国际资产管理有限公司联合研发运营的全国首款电子借条、商业保理、融资担保类金融工具。</p>

                            <p>无忧借条通过为用户提供 CA认证、第三方数字存证、200多家公证处等多方认证的电子借条，以及专业的欠款催收服务维护广大用户的财产利益。并行提供0风险理财服务。</p>

                        </div>
                        <div class="box2">
                            <p></p>

                            <p></p>
                            <p></p>

                            <p></p></div>
                    </div>
                </div>
            </div>
        </div>
    </section>
</article>
<jsp:include page="../inc/footer.jsp"></jsp:include>
</body>
</html>