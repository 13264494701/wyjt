<%@ page contentType="text/html;charset=utf-8" language="java"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%@include file="/WEB-INF/views/app/constant.jsp" %>
<!DOCTYPE html>
<html xmlns=http://www.w3.org/1999/xhtml>
<head>
    <title>提现</title>
    <%@include file="/WEB-INF/views/app/meta-flex.jsp" %>
    <meta name="keywords" content=""/>
    <meta name="description" content=""/>
    <link rel="stylesheet" href="${mbStatic}/assets/css/public.css?v=<%=version%>" type="text/css">
    <link rel="stylesheet" href="${mbStatic}/assets/css/user.css?v=<%=version%>" type="text/css">
    <script src="${mbStatic}/assets/scripts/sea-modules/seajs/2.3.0/sea.js?v=<%=version%>"></script>
    <script src="${mbStatic}/assets/scripts/base/flexible.js?v=<%=version%>"></script>
    <style>
        .radius{
            border-radius: .5rem;
            margin: 1rem;
            height: auto;
            font-size: 1.1rem;
            background: white;
        }
        .radius p{
            height: 4rem;
        }
        .radius p span{
            display: inline-block;
            padding:.5rem 0;
            height: 3rem;
            line-height: 3rem;
        }
        .radius p :first-child{
            width: 24%;
            float: left;
            text-align: left;
            color: RGB(153,153,153);
        }
        .radius p :last-child{
            width: 72%;
            float: left;
            text-align: left;
        }
        .radiusTtile {
            text-align: center;
            font-size: 1.34rem;
            height: 3rem;
            line-height: 4rem;
        }
        .radiusTtile img{
            margin-right: .5rem;
            width: 1.75rem;
            display: inline-block;
            margin-bottom: .3rem;
        }
        .radiusTtileMoney{
            font-size: 2.5rem;
            text-align: center;
            font-weight: bold;
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
                <div class="page-content user-main withdraw-main" style="padding-top: 0;background: white">
                    <p class="radiusTtile"><img src="${mbStatic}/assets/images/debt/tx@2x.png"><span>账户提现（${member.name}）</span></p>
                    <p class="radiusTtileMoney">-${fns:decimalToStr(detail.amount,2)}</p>
                    <div class="radius">
                        <p><span>提现时间</span><span>${fns:getDateStr(detail.createTime,"yyyy-MM-dd HH:mm:ss")}</span></p>
                        <p><span>提现金额</span><span>${fns:decimalToStr(detail.amount,2)}元</span></p>
                        <p><span>手&nbsp;续&nbsp;费</span><span>${fns:decimalToStr(detail.fee,2)}元</span></p>
                        <p><span>到账金额</span><span>${actualMoney}元</span></p>
                        <p><span>交易编号</span><span>${detail.id }</span></p>
                        <p><span>操作状态</span>
                        <c:choose>
                        	<c:when test="${detail.status == 'madeMoney'}">
                        		<span style="color:red">${fns:getDictLabel(detail.status, 'wdrlRecordStatus', '')}</span></p>
                        	</c:when>
                        	<c:otherwise>
                       		<span>${fns:getDictLabel(detail.status, 'wdrlRecordStatus', '')}</span></p>
                        	</c:otherwise>
                        </c:choose>
                       <c:if test="${detail.status == 'failure'}">
                         	<p style="padding: 0 1rem;height: 4rem;padding-top: .5rem">原&nbsp;&nbsp;&nbsp;&nbsp;因：账户提现异常，请及时联系在线客服人员处理！</p>
                       </c:if>
                       </div>
                </div>
            </div>
        </div>
    </section>
</article>
</body>
</html>
