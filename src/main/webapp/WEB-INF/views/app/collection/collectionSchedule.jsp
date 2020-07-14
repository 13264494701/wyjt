<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<%@include file="/WEB-INF/views/app/constant.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <%@include file="../meta-flex.jsp" %>
    <title>催收进度-友信宝</title>
    <meta name="keywords" content="友信宝"/>
    <meta name="description" content="友信宝"/>
    <link rel="stylesheet" href="${mbStatic}/assets/css/public.css?v=<%=version%>" type="text/css">
    <link rel="stylesheet" href="${mbStatic}/assets/css/user/debt.css?v=<%=version%>" type="text/css">
    <script src="${mbStatic}/assets/scripts/base/sea.js?v=<%=version%>"></script>
    <script src="${mbStatic}/assets/scripts/base/flexible.js?v=<%=version%>"></script>
</head>
<c:choose>
<c:when test="${empty appPlatform}">
<body>
</c:when>
<c:otherwise>
<body class="${isWeiXin? 'InWeixin':'InWebView'}" data-platform="${appPlatform}"  data-app="51jt">
</c:otherwise>
</c:choose>
<article class="views">
    <section class="view">
        <jsp:include page="../header-public.jsp">
            <jsp:param name="title" value="催收进度"></jsp:param>
            <jsp:param name="url" value="back2app"></jsp:param>
        </jsp:include>
        <div class="pages navbar-fixed  ">
            <div class="page">
                <div class="page-content">
                    <div class="cuishoujd">
                        <div class="cuishoujd-box">
                            <c:forEach items="${detail}" var="d">
                                <div class="jd-r"><em></em>

                                    <p>
                                        <span>状态：${fns:getDictLabel(d.status,'collectionDetailStatus','')}</span>
                                    </p>
                                    <p>
                                        <span>类型：${fns:getDictLabel(d.type,'collectionDetailType','')}</span>
                                    </p>
                                    <c:if test="${d.status == 'refuseToAccept'}">
                                  		 <p><span>原因：</span>${d.task == null? "暂无":d.task}</p>
                                    </c:if>
                                    <p>
                                        <span>时间：<fmt:formatDate value="${d.createTime}"
                                                                 pattern="yyyy-MM-dd HH:mm:ss"/></span>
                                    </p>

                                </div>
                            </c:forEach>
                        </div>
                    </div>
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