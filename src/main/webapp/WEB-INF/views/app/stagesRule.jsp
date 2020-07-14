<%@ page contentType="text/html;charset=utf-8" language="java"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%@include file="/WEB-INF/views/app/constant.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <title>分期规则</title>
     <%@include file="/WEB-INF/views/app/meta-flex.jsp" %>
    <meta name="keywords" content="无忧借条"/>
    <meta name="description" content="无忧借条"/>
    <link rel="stylesheet" href="${mbStatic}/assets/css/public.css" type="text/css">
    <link rel="stylesheet" href="${mbStatic}/assets/css/helpcenter.css" type="text/css">
    <script src="${mbStatic}/assets/scripts/sea-modules/seajs/2.3.0/sea.js"></script>
    <script src="${mbStatic}/assets/scripts/base/flexible.js"></script>
    <style>
        p{
            font-size:1.1rem;
            line-height: 2rem;
        }
        .gray{
            font-size: 1rem;
            color: gray;
            text-indent: 1.7em;
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
        <div class="pages navbar-fixed ">
            <div class="page">
                <div class="page-content" style="padding-top: 0;padding: 1rem;">
                        <p>1、分期1期为30天；</p>
                        <p>2、还款日期设置示例如下：</p>
                        <p class="gray">假定今日为2016-01-01，分期次数为6期（180天），则每期的还款日为：2016-01-30（借款日为1日，从借款日当前开始计算，向后累加30日，即为还款日）</p>
                        <p class="gray">2016-03-01</p>
                        <p class="gray">2016-03-31</p>
                        <p class="gray">2016-04-30</p>
                        <p class="gray">2016-05-30</p>
                        <p class="gray">2016-06-29</p>
                        <p>3、实际分期情况根据双方达成借款协议（即电子借条生成）时间开始计算。</p>
                        <p>4、分期还款金额遵循等额本息的规则计算，等额本息指将收益和本金加起来后平均到每个月，每月偿还同等数额的资金，还款额中的本金比重逐月递增、利息比重逐月递减。</p>
                        <p class="gray">等额本息还款金额规则：</p>
                        <p class="gray">1)、每期还款金额 = 〔借款本金×月利率×(1＋月利率)＾还款月数〕÷〔(1＋月利率)＾还款月数-1〕；</p>
                        <p class="gray">2)、每月还款本金 = 借款本金×月利率×〔(1+月利率)^还款月数-(1+月利率)^(分期期号-1)〕÷〔(1+月利率)^还款月数-1〕；</p>
                        <p class="gray">3)、每月还款利息 = 贷款本金×月利率×(1+月利率)^(分期期号-1) ÷〔(1+月利率)^还款月数-1〕；月利率 = 年利率 ÷ 12；</p>
                        <p class="gray">例如：300元本金，年利率10%，分4期还款</p>
                        <p class="gray">1期-76.57元(本金-74.07元，利息-2.50元)</p>
                        <p class="gray">2期-76.57元(本金-74.69元，利息-1.88元)</p>
                        <p class="gray">3期-76.57元(本金-75.31元，利息-1.26元)</p>
                        <p class="gray">4期-76.57元(本金-75.93元，利息-0.63元)</p>
                        <br><br>
                </div>
            </div>
        </div>
    </section>
</article>
<jsp:include page="/WEB-INF/views/app/footer.jsp"></jsp:include>
<script>
    seajs.use(['zepto', 'jumpApp']);
</script>
</body>
</html>
<!--created by jozhua 2016/01/27-->