<%@ page contentType="text/html;charset=utf-8" language="java"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%@include file="/WEB-INF/views/app/constant.jsp" %>
<!DOCTYPE html>
<html xmlns=http://www.w3.org/1999/xhtml>
<head>
    <title>分期还款计划</title>
    <%@include file="/WEB-INF/views/app/meta-flex.jsp" %>
    <meta name="keywords" content=""/>
    <meta name="description" content=""/>
    <link rel="stylesheet" href="${mbStatic}/assets/css/public.css?v=<%=version%>" type="text/css">
    <link rel="stylesheet" href="${mbStatic}/assets/css/user.css?v=<%=version%>" type="text/css">
    <script src="${mbStatic}/assets/scripts/base/sea.js?v=<%=version%>21"></script>
    <script src="${mbStatic}/assets/scripts/base/flexible.js?v=<%=version%>"></script>
    <style>
        .infoTitle{
            height: 2rem;
            text-align: left;
            padding: 1rem 2rem;
            font-size: 1.2rem;
            border-bottom: 1px solid RGB(223,223,223);
        }

        .box1 p span{
            height: 2.5rem;
            line-height: 2.5rem;
            text-align: center;
            display: inline-block;
            width: 11rem;
            border-bottom: 1px solid RGB(223,223,223);
            color: RGB(109,109,109);
            font-size: .9rem;
        }
        .box1 p span:nth-child(1){
            width: 13rem;
        }
        .box1 p span:nth-child(2){
            width: 13.5rem;
            border-left: 1px solid RGB(223,223,223);
        }

        .box2 p span{
            height: 2.5rem;
            line-height: 2.5rem;
            text-align: center;
            display: inline-block;
            width: 11rem;
            border-bottom: 1px solid RGB(223,223,223);
            color: RGB(109,109,109);
            font-size: .9rem;
        }
        .box2 p span:nth-child(1){
            width: 6rem;
        }
        .box2 p span:nth-child(2){
            width: 9.5rem;
            border-left: 1px solid RGB(223,223,223);
            border-right: 1px solid RGB(223,223,223);
        }
        .box2 .dark span,.dark{
            color: #333;
        }
        .black{
            color: black;
            font-weight: bold!important;
        }
        .red{
            color:RGB(225,41,34)!important;
            font-weight: bold;!important;
        }
        .box2 .black span,.black{
            color: black;
        }
    </style>
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
                <div class="page-content user-main withdraw-main" style="background: white;padding-top: 0;">
                    <h2 class="infoTitle">还款本息总额</h2>
                    <div>
                        <p class="dark"><span>期数</span><span>还款额</span><span>本金</span><span>利息</span></p>
                    </div>
                    <h2 class="infoTitle">借款分期：还款详情</h2>
                    <div class="box2">
                        <p class="dark"><span>期数</span><span>还款时间</span><span>每期还款额</span></p>
                        <c:forEach items="${repayRecordList}" var="record" varStatus="status">
                        	 <p class="dark"><span>${status.count}期</span><span>${fns:getDateStr(record.expectRepayDate,"yyyy-MM-dd")}</span><span>${fns:decimalToStr(record.expectRepayAmt,2)}元</span></p>
                        </c:forEach>
                    </div>

				</div>
            </div>
        </div>
    </section>
</article>

</body>
</html>
