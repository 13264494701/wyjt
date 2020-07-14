<%@ page contentType="text/html;charset=utf-8" language="java"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%@include file="/WEB-INF/views/app/constant.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <title>分期还款计划</title>
    <%@include file="/WEB-INF/views/app/meta-flex.jsp" %>
    <meta name="keywords" content=""/>
    <meta name="description" content=""/>
    <link rel="stylesheet" href="${mbStatic}/assets/css/public.css?v=<%=version%>" type="text/css">
    <link rel="stylesheet" href="${mbStatic}/assets/css/user.css?v=<%=version%>" type="text/css">
    <script src="${mbStatic}/assets/scripts/base/sea.js?v=<%=version%>21"></script>
    <script src="${mbStatic}/assets/scripts/base/flexible.js?v=<%=version%>"></script>
    <style type="text/css">
        .radiusTtile {
            text-align: center;
            font-size: 1.1rem;
            height: 3rem;
            line-height: 4rem;
            background: white;
        }
        .radiusTtileMoney {
            font-size: 2.5rem;
            text-align: center;
            background: white;
            margin-bottom: 1rem;
        }

        .plan{
            /*border: 1px solid RGB(226, 226, 226);*/
            border-top: none;
            margin: 0 .5rem;
            border-bottom: 1px solid RGB(226, 226, 226);
            color: #313131;
            font-size: 1.1rem;
            height: 4rem;
            line-height: 4rem;
        }
        .title{
            /*font-size: 1.2rem;*/
            /*border: 1px solid RGB(226, 226, 226);*/
            border-bottom: 1px solid RGB(226, 226, 226);/*RGB(226, 226, 226);*/
            line-height: 2rem;
            color: #898989;
        }
        .plan span{
            display: inline-block;
            /*border-right:1px solid RGB(226, 226, 226) ;*/
            text-align:center;
            height: 4rem;
            line-height: 4rem;
            overflow: hidden;
        }
        .plan span:nth-child(1){
            width:9rem;
        }
        .plan span:nth-child(2){
            width: 6rem;
            text-align:center;
        }
        .plan span:nth-child(3){
            width: 6.2rem;
            text-align:center;
        }
        .plan span:nth-child(4){
            border-right: none ;
            width: 4rem;
            text-align:center;
        }
        .plan span em{
            width: 8rem;
            height: 1.5rem;
            line-height: 1.5rem;
            float: left;
        }
        .plan span em:first-child{
            margin-top: .5rem;
        }
        .plan span em:last-child{
            color: #898989;
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
                <div class="page-content user-main withdraw-main " style="background:RGB(238,242,243);padding-bottom:1rem;    padding-top: 0;margin-top: 1rem;">
                    <p class="radiusTtile"><span>还款本息总额</span></p>
                    <p class="radiusTtileMoney">${amountAndInt}</p>
                    <p class="plan title"><span>期数</span><span>还款额</span><span>本金</span><span>利息</span></p>
                    <c:forEach items="${repayRecordList}" var="record" varStatus="status">
                    	<c:if test="${status.count<=9}">
	                	    <p class="plan"><span><em>第0${record.periodsSeq}期</em> <em>${fns:getDateStr(record.expectRepayDate,"yyyy-MM-dd")}</em></span><span>${fns:decimalToStr(record.expectRepayAmt,2)}</span><span>${fns:decimalToStr(record.expectRepayPrn,2)}</span><span>${fns:decimalToStr(record.expectRepayInt,2)}</span></p>
                    	</c:if>
                    	<c:if test="${status.count>9}">
	                	    <p class="plan"><span><em>第${record.periodsSeq}期</em> <em>${fns:getDateStr(record.expectRepayDate,"yyyy-MM-dd")}</em></span><span>${fns:decimalToStr(record.expectRepayAmt,2)}</span><span>${fns:decimalToStr(record.expectRepayPrn,2)}</span><span>${fns:decimalToStr(record.expectRepayInt,2)}</span></p>
                    	</c:if>
                    </c:forEach>
                    <input type="hidden" id="utk" value="">
                    <input type="hidden" id="isVideo" name="isVideo" value="0"  >
                </div>
            </div>
        </div>
    </section>
</article>
<script type="text/javascript">
    seajs.use( 'jumpApp');
</script>
</body>
</html>
