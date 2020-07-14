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

        .radiusTtile {
            text-align: center;
            font-size: 1.1rem;
            height: 3rem;
            line-height: 4rem;
            background: white;
        }

        .radiusTtile img {
            margin-right: .5rem;
            width: 2.5rem;
            display: inline-block;
            margin-bottom: .3rem;
        }

        .radiusTtileMoney {
            font-size: 2.5rem;
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
            margin-top: .5rem;
            margin-bottom:.5rem;
            border-radius: .3rem;
            height: 3.16667rem;
            font-size: 1.3rem;
            line-height: 3.16667rem;
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
        	<jsp:include page="/WEB-INF/views/app/header-public.jsp">
	            <jsp:param name="title" value="购买转让借条"></jsp:param>
	            <jsp:param name="url" value="back2app"></jsp:param>
       	 	</jsp:include>
            <div class="page">
                <div class="page-content user-main withdraw-main " style="background:RGB(238,242,243);padding-bottom:1rem;    padding-top: 0;margin-top: 1rem;">
                    <p class="radiusTtile"><span>转让金额</span></p>
                    <p class="radiusTtileMoney">${fns:decimalToStr(crAuction.crSellPrice,2)}</p>
                    <p class="radiusUserTitle"><img src="${loanRecord.loanee.headImage}">&nbsp;${loanRecord.loanee.name}</p>
                    <div class="radius" style="">
                        <p><span>借条本金</span><span>${fns:decimalToStr(loanRecord.amount,2)}</span></p>
                        <p><span>借条利息</span><span>${fns:decimalToStr(loanRecord.interest,2)}</span></p>
                        <p><span>逾期利息</span><span>${fns:decimalToStr(overdueInterest,2)}</span></p>
                        <p><span>还款时间</span><span>${fns:getDateStr(loanRecord.dueRepayDate,"yyyy-MM-dd")}<i style="color: red;float: right;width: 8rem;text-align: right;">&nbsp;(已逾期${overdueDays}天)</i></span></p>
                        <p ><span>转让金额</span><span>${fns:decimalToStr(crAuction.crSellPrice,2)}</span></p>
                        <p ><span>您当前账户余额</span><span style="width: 45%">${fns:decimalToStr(curBal,2)}&nbsp;元</span>
                        <span style="float: right;width: 20%;color:RGB(66,106,249);text-align: left;" id="rechargeBtn">&nbsp;去充值</span></p>
                    </div>
                    <div class="radius">
                        <p style="height: 3.5rem;border-bottom:0;">
                            <span class="labCheck chekOn">阅读并同意
                            	<a style="float: right;width: 62%!important;color:RGB(66,106,249)" href="javascript:void 0;" data-href="" data-title="债权转让协议" id="goXieyi">《债权转让协议》</a>
                            </span>
                        </p>
                    </div>
                    <a href="javascript:void 0" class="recharge-but removeDis" id="payBtn"> <span>立即购买</span> </a>
                    <p class="subTip">您将支付${fns:decimalToStr(crAuction.crSellPrice,2)}元，购买后该债转不支持继续转让，请仔细确认后再进行操作。</p>
                    <form id="tokenForm" action=""	method="post">
                		<input type="hidden" name="_header" value="${memberToken}"/>
                		<input type="hidden" name="crAuctionId" value="${crAuction.id}"/>
                		<input type="hidden" name="auctionId" value="${crAuction.id}"/>
                		<input type="hidden" name="_type" value="h5"/>
                	</form>
                    <input type="hidden" id="utk" value="">
                </div>
            </div>
        </div>
    </section>
</article>
<script type="text/javascript">
    define('payDebt', ['zepto', 'jumpApp','panel'], function (require, exports, module) {
        var $ = require('zepto'), ev = $.support.touch ? 'tap' : 'click', call = require('jumpApp'),panel=require('panel'),userName='',isPayPsw=${isPayPsw};
        //调用方法

        var body=document.body.querySelector('body');
        var token = '${memberToken}', isEnough =${isEnough},passwordVerify, flag = false;
        var $sub=$('#payBtn');
        var amountPay = '';//应付金额
        var $labCheck = $('.labCheck');
        $("#goXieyi")[ev](function () {
            //location.href='${pageContext.request.contextPath}/app/wyjt/auction/seeCrAuctionAgreementBeforePay?auctionId=${crAuction.id}&mtk=${memberToken}';
            $("#tokenForm").attr("action","${pageContext.request.contextPath}/app/wyjt/auction/seeCrAuctionAgreementBeforePay");
            $("#tokenForm").submit();
        });
        $labCheck[ev](function (e) {
            var that = $(this),tagName=e.target.tagName;
            if(tagName=='SPAN')
                that.hasClass('chekOn') ? (that.removeClass('chekOn'), $sub.removeClass('disabled').addClass('removeDis')) : (that.addClass('chekOn'), $sub.removeClass('removeDis').addClass('disabled'));
        });
        $sub[ev](function () {
        	 var hasRead = $labCheck.hasClass('chekOn');
        	 if(hasRead){
        		 $.toast('请先阅读并同意协议！');
        		 return false;
        	 }
			payFn()
        });
        $('#rechargeBtn')[ev](function () {
            goRecharge();
            return false;
        });

        function payFn() {
            if (flag) {
                $.toast('请勿重复提交~！');
                return false;
            }
            flag= true;
	        if (!isEnough) {
	        	flag=false;
                $.confirm('<p class="withdraw-pass item-password"><label for="withdrawPass">对不起，您的余额不足，<br />请充值后再支付。</label></p>', {
                    callback: function () {
                        goRecharge();
                    },
                    btn: ['再想想', '去充值'],
                    cls: 'withdraw-pass-dialog'
                });
	        }else {
		       if(isPayPsw){
                   flag = false;
                   if (!passwordVerify) {
                       require.async(['user/password-verify'], function (pverify) {
                           passwordVerify = pverify.verify({
                               token: token,
                               action: 'checkPwd',
                               pwdType: 1,
                               title: '<span style="line-height: 2.6">输入支付密码</span>',
                               callback: function (code, msg) {
                                   flag = false;
                                   if (code) {
                                       $.toast('支付申请成功', 1000, function () {
                                      	   $("#tokenForm").attr("action","${pageContext.request.contextPath}/app/wyjt/auction/payAuction");
                                           $("#tokenForm").submit();
                                       });
                                   } else $.toast(msg);
                               }
                           })
                       });
			         }else {
			            passwordVerify.open();
			         }
		       }else{
		            $.alert('为了您的账户安全，请先去设置支付密码。', setPwd);
		       }
	      }
          }
        function goRecharge() {
            var parameter = {"xxx": ""};
            call.callApp('recharge', parameter);
        }

        function setPwd() {
            call.callApp('setpaypass');
        }
    });
    seajs.use(['payDebt', 'jumpApp']);
</script>
</body>
</html>
