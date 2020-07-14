<%@ page contentType="text/html;charset=utf-8" language="java"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%@include file="/WEB-INF/views/app/constant.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <title>预支付催收费用</title>
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
        .disabled {
            pointer-events: none;
            cursor: default;
            background: url("${mbStatic}/assets/images/debt/sub-gray@2x.png");
            background-size: 2%;
        }
        .labCheck {
            width: 66% !important;
            display: inline-block !important;
            background: url("${mbStatic}/assets/images/debt/agree_on@2x.png") 0rem 1.1rem no-repeat;
            background-size: 8%;
            padding-right: 0 !important;
            padding-left: 1.5rem !important;
        }

        .chekOn {
            background: url("${mbStatic}/assets/images/debt/agree_off@2x.png") 0rem 1.1rem no-repeat;
            background-size: 8%;
        }
        .circle{
            color: red;
            padding: 2rem 0rem .5rem 3rem;
            font-size: 1.1rem;
            background: url(${mbStatic}/assets/images/debt/prompt@2x.png) 1rem 2rem no-repeat;
            background-size: 1.5rem
        }
        .circleTip{
            height: 2rem;
            line-height: 2rem;
            padding-left: 2.86rem;
            font-size: 1rem;
            color: RGB(143,143,143);
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
	        <jsp:param name="title" value="预支付"></jsp:param>
	        <jsp:param name="url" value="back2app"></jsp:param>
        </jsp:include>
        <div class="pages navbar-fixed toolbar-fixed">
            <div class="page">
                <div class="page-content user-main withdraw-main " style="background: white;padding-bottom:1rem;">
                    <p class="radiusTtile"><img src="${mbStatic}/assets/images/debt/expenditure@2x.png"><span>支付专业催收服务费</span></p>
                    <p class="radiusTtileMoney" id="payMoney">- ${payMoney}</p>
                    <div class="radius">
                        <p><span>预支付</span><span>${payMoney}元</span></p>
                        <p><span>未还金额</span><span>${fns:decimalToStr(totalMoney,2)}元</span></p>
                        <p><span>未还期数</span><span>共${repayNum}期未还</span></p>
                        <p><span>账户余额</span><span>${remainMoney}元</span><span style="float: right;width: 20%;color:RGB(66,106,249) " id="rechargeBtn">&nbsp;去充值</span></p>
                        <p style="height: 3.5rem">
                            <span class="labCheck ">我同意<a style="float: right;width: 78%!important;color:RGB(66,106,249)"  class="xieyi" href="javascript:void 0">《专业催收服务协议》</a></span>
                    </div>
                    <a href="javascript:void 0" class="recharge-but removeDis immePay" id="payBtn"> <span>立即支付</span> </a>
                    <input type="hidden" id="utk" value="${memberToken}">
                    <p class="circle">友情提示</p>
                    <p class="circleTip">1.请仔细阅读<a id="xieyi" class="xieyi" href="javascript:void 0">《专业催收服务协议》</a>，完全理解和表</p>
                    <p class="circleTip">&nbsp;&nbsp;&nbsp;&nbsp;达意思后再勾选我同意协议，再确认支付；</p>
                    <p class="circleTip">2.请确保您的账户有足够的支付资金；</p>
                    <p class="circleTip">3.催收失败后您支付的催收服务费将全额退还到您的</p>
                    <p class="circleTip">&nbsp;&nbsp;&nbsp;&nbsp;账户；</p>
                    <p class="circleTip">4.申请人在债权催收期间内无论以何种方式收回该 </p>
                    <p class="circleTip">&nbsp;&nbsp;&nbsp;笔借款，都视为无忧借条风控履行完其催收服务。</p>
                	<form id="dataForm" action="${pageContext.request.contextPath}/app/wyjt/collection/payForApply" method="post">
                   		<input type="hidden" name="_header" value="${memberToken}">
                   		<input type="hidden" name="_type" value="h5">
                   		<input type="hidden" id="payMoney" name="payMoney" value="${payMoney}" />
                   		<input type="hidden" id="loanId" name="loanId" value="${loan.id}" />
                  	 </form>
                </div>
            </div>
        </div>
    </section>
</article>
</body>
<script>
    define('payUrge', ['zepto', 'jumpApp'], function (require, exports, module) {
        var $ = require('zepto'), ev = $.support.touch ? 'tap' : 'click', call = require('jumpApp'),
                passwordVerify, token='${memberToken}';
        var flag = false,isPayPsw=${isPayPsw},isEnough=${isEnough};

        function PayUrge() {
            this.$payMoney = $('#payMoney');
            this.$agreeRule = $('.labCheck');
            this.$goPay = $('#payBtn');
            this.$reCharge = $('#rechargeBtn');
            this.$xieyi = $('#xieyi');
            this.init();
        }

        PayUrge.prototype.init = function () {
            this.bindEvent();
        }
        PayUrge.prototype.bindEvent = function () {
            var _this = this;
            //点击同意条款
            _this.$agreeRule[ev](function (e) {
          /*       var that = $(this);
            that.hasClass('chekOn') ? (that.removeClass('chekOn'), _this.$goPay.removeClass('disabled').addClass('removeDis')) : (that.addClass('chekOn'), _this.$goPay.removeClass('removeDis').addClass('disabled'));
            */
            var that = $(this),tagName=e.target.tagName;
            if(tagName=='SPAN')
                that.hasClass('chekOn') ? (that.removeClass('chekOn'), _this.$goPay.removeClass('disabled').addClass('removeDis')) : (that.addClass('chekOn'), _this.$goPay.removeClass('removeDis').addClass('disabled'));
            else if(tagName=='A') {
                location.href='${pageContext.request.contextPath}/app/wyjt/common/collectionAgreement?appPlatform=' + '${appPlatform}'+ '&isWeiXin='+ '${isWeiXin}'
            }
            
            });
           //点击查看协议
            _this.$xieyi[ev](function () {
            	location.href='${pageContext.request.contextPath}/app/wyjt/common/collectionAgreement?appPlatform=' + '${appPlatform}'+ '&isWeiXin='+ '${isWeiXin}';
            }); 
            //点击充值
            _this.$reCharge[ev](function () {
                _this.goRecharge();
            });
            //点击支付
            _this.$goPay[ev](function () {
                var payUrl='',token='${memberToken}';
               if(!isPayPsw){
                //设置支付密码
               	 $.alert('为了您的账户安全，请先去设置支付密码。', setPwd);
               }else {
            	   if(!isEnough){
		                flag = false;
		                //提醒充值
		                $.confirm('<p class="withdraw-pass item-password"><label for="withdrawPass">对不起，您的余额不足，请充值后再支付。</label></p>', {
		                    callback: function () {
		                        _this.goRecharge();
		                    },
		                    btn: ['再想想', '去充值'],
		                    cls: 'withdraw-pass-dialog'
		                });
	              }else{
	                //传参数：支付密码、借款单id、
	                if (!passwordVerify) {
	                        require.async(['user/password-verify'], function (pverify) {
	                            passwordVerify = pverify.verify({
	                                token: token,
	                                action: 'checkPwd',
	                                pwdType:1,
	                                title: '<span style="line-height: 2.6">输入支付密码</span>', 
	                                callback: function (code, msg) {
	                                    flag = false;
	                                    var pwd=this.opts.pwd;
	                                    if (code) {
	                                        $.toast('支付申请成功', 1000, function () {
	                                            $('#dataForm').submit();d
	                                        });
	                                    } else $.toast(msg);
	                                }
	                            })
	                        })
	                    }
	                else passwordVerify.open();
	             }
               }
            });

        };
        PayUrge.prototype.goRecharge = function () {
            var parameter = {"xxx": ""};
            call.callApp('recharge', parameter);
        }

        function setPwd() {
            call.callApp('setpaypass');
        }
        new PayUrge();
        
    });
    seajs.use('payUrge');
</script>
</html>