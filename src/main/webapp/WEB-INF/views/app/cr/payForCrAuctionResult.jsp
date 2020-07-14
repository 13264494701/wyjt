<%@ page contentType="text/html;charset=utf-8" language="java"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%@include file="/WEB-INF/views/app/constant.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <title>购买转让借条</title>
    <%@include file="/WEB-INF/views/app/meta-flex.jsp" %>
    <meta name="keywords" content=""/>
    <meta name="description" content=""/>
    <link rel="stylesheet" href="${mbStatic}/assets/css/public.css?v=<%=version%>" type="text/css">
    <link rel="stylesheet" href="${mbStatic}/assets/css/user.css?v=<%=version%>" type="text/css">
    <script src="${mbStatic}/assets/scripts/base/sea.js?v=<%=version%>21"></script>
    <script src="${mbStatic}/assets/scripts/base/flexible.js?v=<%=version%>"></script>
    <style type="text/css">
        .pop-gray.pop-dialogs .pop-btn {
            background: #eeedf2;
            color: #999;
        }

        .pop-info {
            margin-top: 1rem;
        }

        .pop-label {
            color: #5d5d5d;
            font-size: 1rem;
            margin-top: 1rem;
        }

        .pop-label input {
            width: 1rem;
            height: 1rem;
            vertical-align: -0.1667rem;
        }

        .radius {
            border-radius: .5rem;
            /*border: 1px RGB(219,219,219) solid;*/
            margin: .2rem 0;
            /*height: 25.5rem;*/
            font-size: 1.1rem;
            background: white;
        }

        .radius p {
            height: 3rem;
            line-height: 1.5rem;
            border-bottom: 1px RGB(226, 226, 226) solid;
            margin-left: 1rem;
            margin-right: 1rem;
        }

        .radius p span {
            display: inline-block;
            padding: .765rem 0rem;
            /*height: 2rem;*/
            /*line-height: 2rem;*/
            width: 66%;
            text-align: right;
        }

        .radius p :first-child {
            width: 33%;
            float: left;
            text-align: left;
            color: RGB(153, 153, 153);
        }

        .radius p span :last-child {
            width: 40%;
        }

        .radiusPaySusses {
            background: url(${mbStatic}/assets/images/debt/success@2x.png) 11.5rem 1rem no-repeat;
            background-size: 4rem;
            height: 6rem;
            line-height: 6rem;
        }

        .radiusTtile img {
            margin-right: .5rem;
            width: 2.5rem;
            height: 2.5rem;
            display: inline-block;
            margin-bottom: .3rem;
        }

        .radiusTtileMoney {
            font-size: 1.2rem;
            text-align: center;
            font-weight: bold;
            background: white;
            padding: 1rem 0;
            border-bottom: 5px solid RGB(226, 226, 226);
            margin-bottom: 1rem;
        }
        .radiusUserTitle{
            font-size: 1.2rem;
            text-align: left;
            /*padding-left: 1rem;*/
            border-bottom: 1px solid RGB(226, 226, 226);
            height: 3rem;
            margin: 0 1rem;
        }
        .radiusUserTitle img{
            width: 2.5rem;
            height:2.5rem;
            margin-right: .5rem;
            border-radius:.6rem;
        }

        p a {
            /*display: inline-block;*/
            width: 66% !important;
        }

        .recharge-but {
            margin-top: 1.5rem;
            margin-bottom:.5rem;
            display: block;
            margin: 2.5rem 0.33333rem 0;
            -webkit-border-radius: 10px;
            -moz-border-radius: 10px;
            border-radius: .3rem;
            background-color: #3ea3ff;
            height: 3.16667rem;
            font-size: 1.5rem;
            line-height: 3.16667rem;
            text-align: center;
            width: 47%;
            float: left;
            margin-left: .4rem;
        }

        .disabled {
            pointer-events: none;
            cursor: default;
            opacity: 0.2;
            background-color: #666;
        }

        .removeDis {
            background-color: #E71B1B !important;
        }

        .user-main .radius p select {
                float: right;
                height: 2.0351rem;
                line-height: 2.0351rem;
                width: 7.5rem;
                background: url(${mbStatic}/assets/images/debt/dispute@2x.png) 0rem .1rem no-repeat;
                background-size: 99%;
                padding-right: 2.2rem;
                padding-left: .85rem;
                align-items: center;
                -webkit-appearance: none;
        }

        #rechargeBtn {
            width: 5rem !important;
            background: url("${mbStatic}/assets/images/debt/toRight_blue@2x.png") 4.2rem 1rem no-repeat;
            background-size: 14%;
        }

        .labCheck {
            width: 62% !important;
            display: inline-block !important;
            background: url("${mbStatic}/assets/images/debt/agree_on@2x.png") 0rem .9rem no-repeat;
            background-size: 8%;
            padding-right: 0 !important;
            padding-left: 1.5rem !important;
            height: 2rem !important;
            /*line-height: 2rem !important;*/
        }

        .chekOn {
            background: url("${mbStatic}/assets/images/debt/agree_off@2x.png") 0rem .9rem no-repeat;
            background-size: 8%;
        }

        .subTip{
            color: red;
            height: 2rem;
            line-height: 2rem;
            margin: 0 .8rem;
            padding-left: 2rem;
            background: url(${mbStatic}/assets/images/debt/prompt@2x.png) 0rem .3rem no-repeat;
            background-size: 1.3rem;
        }
        .yellowBtn{
            background:rgb(247,181,0);
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
            <jsp:include page="/WEB-INF/views/app/header-public.jsp">
	            <jsp:param name="title" value="购买转让借条"></jsp:param>
	            <jsp:param name="url" value="payResult"></jsp:param>
       	 	</jsp:include>
        <div class="pages navbar-fixed toolbar-fixed">
            <div class="page">
                <div class="page-content user-main withdraw-main " style="background:RGB(238,242,243);padding-bottom:1rem;    padding-top: 0;margin-top: 1rem;">
                    <p><br/></p>
                    <br/>
                    <br/>
                    <p class="radiusPaySusses"></p>
                    <p class="radiusTtileMoney">支付成功</p>
                    <p class="radiusUserTitle"><img src="${loanRecord.loanee.headImage}">&nbsp;${loanRecord.loanee.name}</p>
                    <div class="radius" style="">
                        <p><span>借条本金</span><span>${fns:decimalToStr(loanRecord.amount,2)}</span></p>
                        <p><span>借条利息</span><span>${fns:decimalToStr(loanRecord.interest,2)}</span></p>
                        <p><span>逾期利息</span><span>${fns:decimalToStr(overdueInterest,2)}</span></p>
                        <p><span>还款时间</span><span>${fns:getDateStr(loanRecord.dueRepayDate,"yyyy-MM-dd")}<i style="color: red;float: right;width: 8rem;text-align: right;">&nbsp;(已逾期${overdueDays}天)</i></span></p>
                        <p><span>转让金额</span><span>${fns:decimalToStr(crAuction.crSellPrice,2)}</span></p>
                    </div>
                    <a href="javascript:void 0" class="recharge-but yellowBtn" id="againBuy"> <span>继续购买</span> </a>
                    <a href="javascript:void 0" class="recharge-but removeDis" id="myDebtLoan"> <span>我的债转借条</span> </a>
                </div>
            </div>
        </div>
    </section>
</article>
<script type="text/javascript">
    define('payDebtResult', ['zepto', 'jumpApp'], function (require, exports, module) {
        var $ = require('zepto'), ev = $.support.touch ? 'tap' : 'click', call = require('jumpApp'),token = $('#utk').val();
        //继续购买
         $('#back2Cr')[ev](function(){
        	againBuy();
        });
        $('#againBuy')[ev](function () {
            againBuy();
        });
        //我的债转借条
        $('#myDebtLoan')[ev](function () {
            myDebtLoan();
        });

		
        function againBuy() {
            call.callApp('againbuy');
        }

        function myDebtLoan() {
            call.callApp('mydebtloan');
        }
    });
    seajs.use(['payDebtResult', 'jumpApp']);
</script>
</body>
</html>
