<%-- <%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<%@include file="/WEB-INF/views/app/constant.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <%@include file="../meta-flex.jsp" %>
    <title>仲裁进度-友信宝</title>
    <meta name="keywords" content="友信宝"/>
    <meta name="description" content="友信宝"/>
    <link rel="stylesheet" href="${ctxStatic}/assets/css/public.css?v=<%=version%>" type="text/css">
    <link rel="stylesheet" href="${ctxStatic}/assets/css/user/draw.css?v=<%=version%>" type="text/css">
    <script src="${ctxStatic}/assets/scripts/base/sea.js?v=<%=version%>"></script>
    <script src="${ctxStatic}/assets/scripts/base/flexible.js?v=<%=version%>"></script>
    <style>

        .speed span {
            display: inline-block;
            color: RGB(137, 137, 137);
        }

        .speed span:nth-child(1) {
            width: 7rem;
            text-align: center;

            float: left;
            padding-top: .4rem;
        }

        .speed span:nth-child(2) {
            width: 14rem;

            padding-left: 3rem;
            padding-top: .2rem;

        }

        .speedList p {
            background: url(${ctxStatic}/assets/images/debt/graySpeed@3x.png) 6.8rem 0rem no-repeat;
            background-size: 8%;
            padding-bottom: 1.5rem
        }

        .speedList p:first-child {
            background: url("${ctxStatic}/assets/images/debt/speed@3x.png") 6.5rem 0rem no-repeat;
            background-size: 10%;
        }

        .speedList p:first-child .right em {
            color: RGB(49, 49, 49);
            font-weight: bold;
        }

        .speedList p:last-child {
            background: url("${ctxStatic}/assets/images/debt/graySpeedUp@3x.png") 6.8rem 0rem no-repeat;
            background-size: 8%;
        }

        .speed b {
            display: inline-block;
            width: 80%;
            text-align: center;
            font-weight: normal;
        }

        .speed i {
            display: inline-block;
            width: 100%;
            height: 2rem;
            line-height: 2rem;
            font-size: .9rem;
        }

        .speed i:first-child {
            font-size: 1.1rem;
            font-weight: bold;
        }
    </style>
</head>
<c:choose>
<c:when test="${empty appPlatform}">
<body>
</c:when>
<c:otherwise>
<body class="${isWeiXin? 'InWeixin':'InWebView'}" data-platform="${appPlatform}"  data-app="51jt">
</c:otherwise>
</c:choose>
<article class="views docBody">
    <section class="view">

        <jsp:include page="../header-public.jsp">
            <jsp:param name="title" value="仲裁进度"></jsp:param>
        </jsp:include>

        <div class="pages navbar-fixed ">
            <div class="page">
                <div class="page-content" style="background: #FFFFFF;padding-top: 5.66667rem;padding-bottom: 2rem">
                    <div class="speedList">
                        <c:forEach items="${detail}" var="d">
                            <p class="speed">
                            <span class="left">
                                <b><fmt:formatDate value="${d.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/></b>
                            </span>
                                <span class="right">
                                <c:if test="${d.status == 'refundHasArrived'}">
	                                <i><em>${fns:getDictLabel(d.status,'arbitrationDetailStatus','')}<a href="javascript:void (0);"  data-fundId="${actTrxId}" class="detailsBill">查看账单详情</a></em></i>
                                </c:if>
    							<c:if test="">
	                                <i><em>${fns:getDictLabel(d.status,'arbitrationDetailStatus','')}</em></i>
    							</c:if>
                                <i>类别：<em>${fns:getDictLabel(d.type,'arbitrationDetailType','')}</em></i>
                                <i>任务：<em>${fns:getDictLabel(d.task,'arbitrationDetailTask','')}</em></i>
                            </span>
                            </p>
                        </c:forEach>
                    </div>
                </div>
            </div>
        </div>
    </section>
</article>

<script>
    seajs.use(['zepto', 'jumpApp'], function ($, jumpApp) {
        var ev = $.support.touch ? 'tap' : 'click', $detailsBill = $('.detailsBill');

        $detailsBill[ev](function () {
            var fundID = Number($(this).attr('data-fundId')), parameter = {"fundID": fundID};
            jumpApp.callApp('funddetail', parameter);
        });
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
 --%>