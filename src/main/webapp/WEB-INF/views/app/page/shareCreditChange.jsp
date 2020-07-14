<%@ page import="java.util.*" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>

<!DOCTYPE html>
<html>
<head>
    <title>人生新高度！我的信用升级啦！</title>
    <%@include file="../meta-flex.jsp" %>
    <link rel="stylesheet" href="${mbStatic}/assets/css/public.css" type="text/css">
    <link rel="stylesheet" href="${mbStatic}/assets/css/credit-hall.css" type="text/css">
    <script src="${mbStatic}/scripts/base/flexible.js"></script>
</head>
<body>
<article class="views">
    <section class="view">
        <div class="pages">
            <div class="page">
                <div class="page-content share-content">
                    <div class="share-box">
                        <div class="top"></div>
                        <div class="people"></div>
                        <div class="inner">
                            <div class="cup"></div>
                            <div class="userinfo">
                                <strong>${nickname}</strong>
                                <c:if test="${type==1}">
                                    <p class="txt">
                                        恭喜您！您的信用等级已升级至
                                    </p>
                                </c:if>
                                <c:if test="${type==2}">
                                    <p class="txt">
                                        您的信用等级已降至
                                    </p>
                                </c:if>
                                <div class="line">
                                    <span>${rankNo}</span>
                                </div>
                                <div class="info">
                                    <div class="cell">
                                        <p>
                                            ${percent}
                                        </p>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="mark">
                        <p>来无忧借条吧！与我一起，挑战全国信友！</p>
                        <div class="dot"></div>
                        <div class="btn"><a href="/app/wyjt/common/callApp">点击下载无忧借条APP</a></div>
                    </div>
                </div>
            </div>
        </div>
    </section>
</article>
</body>
</html>