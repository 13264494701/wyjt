<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%@include file="/WEB-INF/views/app/constant.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <title>信用报告认证示例-无忧借条</title>
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
<article class="views">
    <section class="view">
        <div class="pages navbar-fixed toolbar-fixed">
            <div class="page">
                <div class="page-content user-main withdraw-main " style="background: white;padding-bottom:1rem;"> 
					<style>
                        .baogaoImgBox{
                            position: absolute;
                            top: 0;
                            width:100%;
                        }
                        .baogaoImgBoxIcon{
                            position: fixed;
                            width: 13rem;
                            top: 13rem;
                            right: 7rem;
                        }
					</style>
					<div class="baogaoImgBox">
						<img src="${mbStatic}/assets/images/debt/xinyongbgExamples@2x.png" alt="" style="width:100%;">
						<img src="${mbStatic}/assets/images/debt/xinyongbgExamplesIcon@2x.png" alt="" class="baogaoImgBoxIcon">
					</div>
                </div>
            </div>
        </div>
    </section>
</article>
</body>
<script>
    seajs.use('jumpApp');
</script>
</html>
