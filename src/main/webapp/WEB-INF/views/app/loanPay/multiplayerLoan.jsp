<%@ page contentType="text/html;charset=utf-8" language="java"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%@include file="/WEB-INF/views/app/constant.jsp" %>
<html>
<head>
    <title>确认出借</title>
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
            /*height: 3.5rem;*/
            border-bottom: 1px RGB(226, 226, 226) solid;
            margin-left: 1rem;
            margin-right: 1rem;
        }

        .radius p span {
            display: inline-block;
            padding: .765rem 0rem;
            height: 2rem;
            line-height: 2rem;
        }

        .radius p :first-child {
            width: 25%;
            float: left;
            text-align: left;
            color: RGB(153, 153, 153);
        }

        .radius p :last-child {
            width: 50%;
        }

        .radiusTtile {
            text-align: center;
            font-size: 1.34rem;
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
        }

        p a {
            /*display: inline-block;*/
            width: 66% !important;
        }

        .recharge-but {
            margin-top: 1.5rem;
            margin-bottom: 1.5rem;
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
            background: url("${mbStatic}//assets/images/debt/toRight_blue@2x.png") 4.2rem 1.24rem no-repeat;
            background-size: 14%;
        }

        .zcType {
            display: inline-block;
            width: 8rem !important;
            background: url("${mbStatic}/assets/images/debt/toRight_blue@2x.png") 7.2rem 1.24rem no-repeat;
            background-size: 9%;
            height: 3rem;
        }

        .labCheck {
            width: 58% !important;
            display: inline-block !important;
            background: url("${mbStatic}/assets/images/debt/agree_on@2x.png") 0rem 1.1rem no-repeat;
            background-size: 8%;
            padding-right: 0 !important;
            padding-left: 1.5rem !important;
            height: 2rem !important;
            line-height: 2rem !important;
        }

        .chekOn {
            background: url("${mbStatic}/assets/images/debt/agree_off@2x.png") 0rem 1.1rem no-repeat;
            background-size: 8%;
        }

        .isVideo {
            /*display: inline-block !important;*/
            width: 10rem !important;
            background: url("${mbStatic}/assets/images/debt/switch_on@2x.png") 6.8rem 1.3rem no-repeat;
            background-size: 30%;
            height: 2rem !important;
            line-height: 2rem !important;
        }

        .isOn {
            display: inline-block !important;
            width: 10rem !important;
            background: url("${mbStatic}/assets/images/debt/switch_off@2x.png") 6.8rem 1.3rem no-repeat;
            background-size: 30%;
            height: 2rem !important;
            line-height: 2rem !important;
        }

        /*
           以下是public密码区的样式
        */

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
                    <p class="radiusTtile"><img src="${mbStatic}/assets/images/debt/lend@2x.png"><span>我要出借</span></p>
                    <p class="radiusTtileMoney">- ${amount}</p>
                    <div class="radius" style="">
                       <p><span>借出金额</span><span>${amount}元</span></p>
                        <p><span>借给谁</span><span>${loanee.name}</span></p>
                        <c:if test="${repayType == '0'}">
                      	  <p><span>还款方式</span><span>全额</span></p>
                        </c:if>
                        <c:if test="${repayType == '1'}">
                      	  <p><span>还款方式</span><span>分期</span></p>
                        </c:if>
                        <p><span>借款时长</span><span>${repayDate}（${term}天）</span></p>
                        <p><span>年化利率</span><span>${intRate}%，${interest}元</span></p>
	                    <p style="border-bottom: none;"><span>可用余额</span><span>${avilableAmount}元</span>
                        <span style="float: right;width: 20%;color:RGB(66,106,249) " id="rechargeBtn">&nbsp;去充值</span></p>
                    </div>
                    <div class="radius">
                        <p style="height: 3.5rem">
                            <span class="labCheck ">我同意
                            	<a style="float: right;width: 75%!important;color:RGB(66,106,249)" href="javascript:void (0);" 
                            	data-href="${pageContext.request.contextPath}/app/wyjt/common/agreement?appPlatform=${appPlatform}&isWeiXin=${isWeiXin}" data-title="友借款服务协议">《友借款服务协议》</a>
                            </span>
                            <span class="zcType" style="float: right;color: RGB(66,106,249) ;" data-href="${pageContext.request.contextPath}/app/wyjt/common/lawArbiDetailedExplain?appPlatForm=${appPlatForm}&isWeiXin=${isWeiXin}&zcType=0" data-title="详细说明">查看详情说明</span>
                        </p>
                        <p><span style="width: 40%;">选择争议解决方式</span>
                        	<span style="width: 60%;">
                        		<select name="billType" id="litType">
                        			<option value="1" selected="selected">仲裁方式(推荐方式)</option>
                        			<option value="2">诉讼方式</option>
                        		</select>
                        	</span>
                        </p>
                        <p style="border-bottom:none">
                        	<span style="width: 59%;">是否需要对方录制视频</span>
                        	<span class="isVideo isOn" style="margin-top: -.5rem"></span>
                        </p>
                    </div>
                    <a href="javascript:void 0" class="recharge-but removeDis" id="payBtn"> <span>立即支付</span> </a>
                    <form id="dataForm" action="${pageContext.request.contextPath}/app/wyjt/loan/payForMultiplayerLoan" method="post">
                   		<input type="hidden" name="_header" value="${memberToken}">
                   		<input type="hidden" name="_type" value="h5">
                   		<input type="hidden" id="amount" name="amount" value="${amount}" />
                   		<input type="hidden" id="detailId" name="detailId" value="${detailId}" />
                   		<input type="hidden" id="disputeSolveType" name="disputeSolveType" />
	                    <input type="hidden" id="isVideo" name="isVideo" value="0" />
                   </form>
                </div>
            </div>
        </div>
    </section>
</article>
<section class="panel" id="infoPanel" style="width: 100%">
    <div class="page-view theme-red">
        <div class="navbar">
            <div class="navbar-inner">
                <div class="left" style="margin-right:2.7rem;"><a class="link close-popup" href="javascript:void 0" target="_self"><i
                        class="icon icon-back"></i><span></span></a></div>
                <div class="center"><h3 style="font-size: 1.4rem;color: white;" id="sibTtile"></h3></div>
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
<script type="text/javascript">
    define('loanPayYXB', ['zepto', 'jumpApp','panel'], function (require, exports, module) {
        var $ = require('zepto'), ev = $.support.touch ? 'tap' : 'click', call = require('jumpApp'),panel=require('panel');
        var postUrl = 'multiplayerLoanLogic.jsp?action=MultipayerLoanPay&data=', token = '${memberToken}', isEnough ='${isEnough}', 
        		passwordVerify, flag = false,isPayPsw='${isPayPsw}';
        var $sub=$('#payBtn'),$litType = $('#litType');
        var amountPay = '${loan.amount}';//应付金额
        var $labCheck = $('.labCheck'),$isVideo=$('#isVideo');
        $labCheck[ev](function (e) {
            var that = $(this),tagName=e.target.tagName;
            if(tagName=='SPAN')
                that.hasClass('chekOn') ? (that.removeClass('chekOn'), $sub.removeClass('disabled').addClass('removeDis')) : (that.addClass('chekOn'), $sub.removeClass('removeDis').addClass('disabled'));
            else if(tagName=='A') {
                location.href='/mb/app/agreement.html';
            }
        });
        $('.zcType').live(ev, function () {
            location.href='/mb/app/arbitrationDetail.html';
        });
        function _openPannl(src) {
            var panel = $('#infoPanel').Panel({
                direction: 'right',
                src: src,
                callback: function () {
                    if (!this.isOpen) {
                        $(this.el).find('.page-content').empty()
                    }
                }
            });
            panel.open();
        }
        $('.isVideo')[ev](function () {
            var that = $(this);
            that.hasClass('isOn') ? (that.removeClass('isOn'),$isVideo.val(1)) : (that.addClass('isOn'),$isVideo.val(0));
        });
        $sub[ev](function () {
            $.cookie('YXBLoanTipV', showTips(1));
            return false;
        });
        $('#rechargeBtn')[ev](function () {
            goRecharge();
            return false;
        });
        function showTips(val) {
            var f = val == '1';
            if(f){
                return payFn();
            }
            $.alert('<p class="pop-info">请确认对方是您信任的好友，对方如不按时还款，损失由您本人承担！无忧借条会为您提供有法律效力的电子借据！</p><p class="pop-label"><label for="mys"><input type="checkbox" id="mys"> 下次不再提醒我 </label></p>', {
                cls: 'pop-gray',
                btn: ['知道了'],
                onShow: function (pop) {
                    var $btn = pop.find('.pop-btn'), html = $btn.html(), t = 3;
                    (function () {
                        if (t == 0) {
                            $btn.html(html);
                            pop.removeClass('pop-gray');
                            return;
                        }
                        $btn.html(html + ' (' + t-- + '秒)');
                        setTimeout(arguments.callee, 1e3)
                    })();
                },
                callback: function (pop) {
                    if (pop.hasClass('pop-gray'))  return false;
                    var $p = pop.find('#mys');
                    if ($p.length) $p.prop('checked') ? $.cookie('YXBLoanTipV', "1", 365) : $.cookie('YXBLoanTipV', null, -1);
                    payFn();
                }
            })
        }

        function payFn() {
            if (flag) {
                $.toast('请勿重复提交~！');
                return false;
            }
            flag = true;
           if(!isEnough){
                flag = false;
        	   $.confirm('<p class="withdraw-pass item-password"><label for="withdrawPass">对不起，您的余额不足，请充值后再支付。</label></p>', {
                   callback: function () {
                       goRecharge();
                   },
                   btn: ['再想想', '去充值'],
                   cls: 'withdraw-pass-dialog'
               });
           	}else {
                      if(!isPayPsw){
                    	  flag = false;
                        if (!passwordVerify) {
                            require.async(['user/password-verify'], function (pverify) {
                                passwordVerify = pverify.verify({
                                    token: token,
                                    action: 'checkPwd',
                                    pwdType: 1,
                                    title: '<span style="line-height: 2.6">输入支付密码</span>',
                                    callback: function (code, msg) {
                                        var _pwd=this.opts.pwd;
                                        if (code) {
                                            $.toast('支付申请成功', 1000, function () {
                                            	 $("#disputeSolveType").val(zcType);
                                            	 $("#dataForm").submit();
                                            });
                                        } else $.toast(msg);
                                    }
                                })
                            });
                        }
                        else passwordVerify.open();
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
    seajs.use(['loanPayYXB', 'jumpApp']);
</script>
</body>
</html>