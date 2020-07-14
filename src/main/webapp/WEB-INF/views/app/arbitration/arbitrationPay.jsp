<%@ page contentType="text/html;charset=utf-8" language="java"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%@include file="/WEB-INF/views/app/constant.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <title>预支付仲裁费用</title>
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
        .pop-label input {
            width: 1rem;
            height: 1rem;
            vertical-align: -0.1667rem;
        }
        .radius {
            border-radius: .5rem;
            margin: 1rem;
            height: 17.5rem;
            font-size: 1.1rem;
            background: white;
        }
        .radius p {
            height: 3.5rem;
            line-height: 3.5rem;
            border-bottom: 1px solid RGB(230,230,230);
        }
        .radius p span {
            display: inline-block;
            padding: .5rem 0rem;
            height: 2.5rem;
            line-height: 2.5rem;
        }
        .radius p :first-child {
            width: 25%;
            float: left;
            text-align: left;
            color: RGB(153, 153, 153);
        }
        .radius p :last-child {
            width: 40%;
        }
        .radiusTtile {
            text-align: center;
            font-size: 1.34rem;
            height: 3rem;
            line-height: 4rem;
        }
        .radiusTtile img {
            margin-right: .5rem;
            width: 1.75rem;
            display: inline-block;
            margin-bottom: .3rem;
        }
        .radiusTtileMoney {
            font-size: 2.5rem;
            text-align: center;
            font-weight: bold;
        }

        p a {
            /*display: inline-block;*/
            /*width: 66% !important;*/
        }
        .removeDis {
            background-color: #E71B1B !important;
        }
        .user-main .radius p select {
            float: right;
            height: 2.1rem;
            width: 7rem;
            appearance: menulist;
            -moz-appearance: menulist;
            -webkit-appearance: menulist;
            border: 1px solid;
            border-radius: .3rem;
        }
        .radius div {
            /*margin-left: 1rem;*/
            font-size: 1rem;
            margin-top: 1.5rem;
            /*border-top:1px RGB(226,226,226) solid ;*/
        }
        div a {
            color: RGB(0, 0, 255);
        }
        #rechargeBtn {
            width: 4rem !important;
            background: url(${mbStatic}/assets/images/debt/toRight_blue@2x.png) 5.2rem .95rem no-repeat;
            background-size: 11%;
        }
        .recharge-but{
            display: block;
            margin: 1.5rem 0.83333rem 0;
            height: 4.16667rem;
            font-size: 1.5rem;
            line-height: 4.16667rem;
            text-align: center;
            border-radius:.25rem;
            background: url("${mbStatic}/assets/images/debt/sub-Red@2x.png");
            background-size: 2%;
        }
        .recharge-but:link, .recharge-but:visited, .recharge-but:hover, .recharge-but:active{
            color: #ffffff;
        }
        .removeDis{

        }
        .disabled {
            pointer-events: none;
            cursor: default;
            opacity: 0.2;
            background: url("${mbStatic}/assets/images/debt/sub-gray@2x.png");
            background-size: 2%;
        }
        .applyTipBox{
            width: 24rem;
            height: 32rem;
            position: absolute;
            top: 50%;
            left: 50%;
            margin-top:-15rem;
            margin-left:-12rem;
            background: white;
            z-index:11112;
            border-radius:.3rem;
            display: none;
        }
        .boxTitle{
            text-align: center;
            font-size: 1.2rem;
            font-weight: bold;
            height: 3rem;
            line-height: 3rem;
            border-bottom: 1px rgb(210,210,210) solid;
            color: #333333;
            margin-bottom:.5rem;
        }
        .boxContent{
            text-align: left;
            line-height: 1.5rem;
            padding:0 1rem;
            color: #797979;
        }
        .bcTip{
            font-size: 1.1rem;
            margin-bottom: .3rem;
        }
        .red{
            color: red;
        }
        .grad{
            color: #333333;
        }
        .boxContent .goExplain a{
            color: rgb(85,98,212);
            height: 1.5rem;
            line-height: 1.5rem;
        }
        .boxContent a{
            display: inline-block;
            height: 3rem;
            line-height: 3rem;
        }
        .redBtnSub{
            height: 3rem;
            line-height: 3rem;
            font-size: 1.2rem;
            text-align:center ;
            background: rgb(223, 47, 49);
            color: white;
            margin: 1rem 0 1rem .5rem;
            width: 48%;
            border-radius:.3rem ;
            height: 3.1rem!important;
        }
        .gradBtn{
            text-align: center;
            font-size: 1.2rem;
            width: 48%;
            background: white;
            color: rgb(223, 47, 49);
            border: .1rem rgb(223, 47, 49) solid;
            border-radius:.3rem ;
        }
        .tipTitle{
            padding-left: 2.5rem;
            margin-top: 1rem;
            margin-bottom: .5rem;
            font-size: 1rem;
            background: url(${mbStatic}/assets/images/debt/prompt@2x.png) no-repeat 1rem .1rem;
            background-size: 1.2rem;
        }
        .tipContent{
            padding-left: 2.5rem;
            line-height: 1.5rem;
            font-size: .8rem;
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
                <div class="page-content user-main withdraw-main " style="background: white;padding-bottom:1rem;">
                    <p class="radiusTtile"><img src="${mbStatic}/assets/images/debt/lend@2x.png"><span>支付裁决仲裁服务费</span></p>
                    <p class="radiusTtileMoney" id="payMoney">${slmoney }</p>
                    <div class="radius">
                        <p><span>借款人</span><span>${loaneeName }</span></p>
                        <p><span>逾期金额</span><span>${fns:decimalToStr(amount,2)}元</span></p>
                        <p><span>逾期时长</span><span>${overDueDuration }天</span></p>
                        <p><span>账户余额</span><span>${accountBalance }元</span><span style="float: right;width: 20%;color:RGB(66,106,249) " id="rechargeBtn">&nbsp;去充值</span></p>
                        <div><span style="width: 70%;text-align: left">点击查看<a href="javascript:void (0);" data-href="${pageContext.request.contextPath}/app/wyjt/common/lawArbiDetailedExplain?appPlatForm=' +'${appPlatForm}' + '&isWeiXin='+ '${isWeiXin}' +'&zcType=0" class="item-link">《仲裁服务说明》</a></span><span></span>
                        </div>
                    </div>
                    <c:if test = "${ageBoolean eq true}"  var="flag">
                    	<a href="javascript:void 0" class="recharge-but removeDis immePay" id="payBtn"> <span>立即支付</span> </a>
                    </c:if>
					<c:if test = "${ageBoolean eq false}">
                    <div style="width:100%;text-align: center; color: red; font-size: 20px" >对方未满23岁，暂不支持仲裁</div>
                    </c:if>
                    <%-- <input type="hidden" id="utk" value="<%=user.getYxbToken()%>"> --%>
                     <p class="tipTitle red">特别说明：</p>
                     <p class="tipContent">平台仲裁服务分为<span  class="red">仲裁裁决</span>和<span class="red">裁决强执</span>两个阶段，需先支付仲裁裁决服务费，待裁决书下发后，在“我的强执”中申请仲裁强执（费用因不同地区而变动）。</p>
                	<form id="dataForm" action="${pageContext.request.contextPath}/app/wyjt/arbitration/payForArbPay" method="post">
                		<input type="hidden" id="memberToken" name="_header" value="${memberToken}" />
				        <input type="hidden" name="_type" value="h5">
				        <input type="hidden" id="loanId" name="loanId" value="${loan.id}">
				        <input type="hidden" id="slmoney" name="slmoney" value="${slmoney}">
				        <input type="hidden" id="overdueAmount" name="overdueAmount" value="${amount}">
                	</form>
                </div>
            </div>
        </div>
    </section>
</article>
<section class="panel" id="infoPanel" style="width: 100%;z-index: 11115;">
    <div class="page-view theme-red">
        <div class="navbar">
            <div class="navbar-inner">
                <div class="left"><a class="link close-popup" href="javascript:void 0" target="_self"><i class="icon icon-back"></i><span></span></a></div>
                <div class="center"><h3 id="sibTtile">仲裁详细说明</h3></div>
                <div class="right"><a href="javascript:" class="link icon-only"></a></div>
            </div>
        </div>
        <div class="page navbar-fixed">
            <div class="page-content">
                <div class="page-center-middle" style="height: 100%">
                    <div class="preloader"></div>
                </div>
            </div>
        </div>
    </div>
</section>
<div class="pop-overlay pop-overlay-visible " style="z-index: 11111; width: 100%;display: none; "></div>
	<div class="applyTipBox">
		<p class="boxTitle">申请须知</p>
		<div class="boxContent">
			<p class="bcTip red">重要说明：</p>
			<p>出借人申请仲裁出裁决服务，会将该借条（含本金利息逾期利息）全部债权转让给北京友信宝网络科技有限公司，由我司代为进行仲裁相关业务，仲裁执行追回的款项将按照实际金额结算给出借人。</p>
			<p>申请仲裁后，您将不能对该借条进行债权转让服务。</p><br />
			<p class="bcTip grad">关于服务内容：</p>
			<p>您申请的是仲裁出裁决服务，如需申请强执，请在出裁决结果后在“我的强执”中申请！</p><br />
			<p class="bcTip grad">关于费用和处理进度：</p>
			<p class="goExplain">详见<a href="javascript:void 0;" data-href="${pageContext.request.contextPath}/app/wyjt/common/lawArbiDetailedExplain?appPlatForm=${appPlatForm}&isWeiXin=${isWeiXin}&zcType=0" class="item-link">《仲裁服务说明》</a></p><br />
			<p><a class="gradBtn">放弃申请</a><a class="redBtnSub">同意并继续</a></p>
		</div>
	</div>
</body>
<script>
    define('PayArbi', ['zepto', 'jumpApp','panel'], function (require, exports, module) {
        var $ = require('zepto'), ev = $.support.touch ? 'tap' : 'click', call = require('jumpApp'),
                passwordVerify, token = '${memberToken}', $payMoney = $('#payMoney'), $goPay = $('#payBtn'),panel = require('panel'),$overLay=$('.pop-overlay,.applyTipBox');
        var flag = false;

        $goPay[ev](function () {
            $overLay.show();
        });
        //支付
        $('.redBtnSub')[ev](function () {
			$overLay.hide('slow');
            $.confirm('确定支付吗？',{
                btn: ['取消', '确定'],
                callback:function () {
                    pay();
                }
            });
        });
		$('.gradBtn')[ev](function () {
			$overLay.hide('slow');
		});
        $('#rechargeBtn')[ev](function () {
            goRecharge();
        });
        function pay() {
            var errorMsg = checkMsg();
            if (errorMsg == '余额不足') {
                $.confirm('<p class="withdraw-pass item-password"><label for="withdrawPass">对不起，您的余额不足，请充值后再支付。</label></p>', {
                    callback: function () {
                        flag = false;
                        goRecharge();
                    },
                    btn: ['再想想', '去充值'],
                    cls: 'withdraw-pass-dialog'
                });
                return false;
            } else if (errorMsg) {
                $.toast(errorMsg);
            } else {
                if (!passwordVerify) {
                    require.async(['user/password-verify'], function (pverify) {
                        passwordVerify = pverify.verify({
                            token: token,
                            pwdType:1,
                            isShowMoney:1,
                            payMoney:$payMoney.html(),
                            title: '<span style="line-height: 2.6">输入支付密码</span>',
                            callback: function (code, msg) {
                                flag = false;
                                var _pwd=this.opts.pwd;
                          		if (code) {
                                    $.toast('支付申请成功', 1000, function () {
                                    	$("#dataForm").submit();
                                    });
                                } else $.toast(msg);
                            }
                        })
                    })
                }
                else passwordVerify.open();
            }
        }
        function checkMsg() {
            var errorMsg = '';
            var moneyVal = $payMoney.html();
            if (moneyVal <= 0) {
                errorMsg = '请检查支付金额';
            }
            var custMoney = '${accountBalance }';
            if (parseFloat(moneyVal) > parseFloat(custMoney)) {
                errorMsg = '余额不足';
            }
            return errorMsg;
        }
        function goRecharge () {
            var parameter = {"xxx": ""};
            call.callApp('recharge', parameter);
        }
        $('.item-link')[ev](function () {
				location.href = "${pageContext.request.contextPath}/app/wyjt/common/lawArbiDetailedExplain?appPlatForm=${appPlatForm}&isWeiXin=${isWeiXin}&zcType=0";
        });
    });
    seajs.use('PayArbi');
</script>
</html>