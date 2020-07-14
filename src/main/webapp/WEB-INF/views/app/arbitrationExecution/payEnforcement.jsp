<%@ page contentType="text/html;charset=utf-8" language="java"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%@include file="/WEB-INF/views/app/constant.jsp" %>
<!DOCTYPE html>
<html>
<head>
	<title>缴纳强执费</title>
	<%@include file="/WEB-INF/views/app/meta-flex.jsp" %>
	<meta name="keywords" content="" />
	<meta name="description" content="" />
	<link rel="stylesheet"	href="${mbStatic}/assets/css/public.css?v=<%=version%>" type="text/css">
	<link rel="stylesheet"	href="${mbStatic}/assets/css/user.css?v=<%=version%>" type="text/css">
	<link rel="stylesheet"	href="${mbStatic}/assets/css/repay.css?v=<%=version%>" type="text/css">
	<script src="${mbStatic}/assets/scripts/base/sea.js?v=<%=version%>21"></script>
	<script	src="${mbStatic}/assets/scripts/base/flexible.js?v=<%=version%>"></script>
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
		border-radius: .2rem;
		margin: 1rem;
		height: 21.5rem;
		font-size: 1.1rem;
		background: white;
	}
	
	.radius p {
		height: 3.5rem;
		line-height: 3.5rem;
		border-bottom: 1px solid RGB(230, 230, 230);
	}
	
	.radius p span {
		display: inline-block;
		padding: .5rem 0rem;
		height: 2.5rem;
		line-height: 2.5rem;
	}
	
	.radius p span:first-child {
		width: 24%;
		float: left;
		text-align: left;
		color: RGB(153, 153, 153);
	}
	
	.radius p span:last-child {
		width: 76%;
	}
	.radius p span i{
		color: #FF0000;
		margin-left:.2rem;
		margin-right: .2rem;
	}
	.radius p span a{
		color: #33CCCC;
	}
	.radiusTtile {
		text-align: center;
		font-size: 1.34rem;
		height: 4rem;
		line-height: 4rem;
		background: url("${mbStatic}/assets/images/debt/lend2@3x.png") no-repeat 5.5rem 1rem;
	    background-size: 2rem;;
	}
	
	.radiusTtileMoney {
		font-size: 2.5rem;
		text-align: center;
		font-weight: bold;
	}
	
	.circle {
		font-size: 1.1rem;
		background: url(${mbStatic}/assets/images/debt/toRight_blue@2x.png) 4rem 1.0rem no-repeat;
		background-size: .5rem;
		display: inline-block;
		width: 5rem;
		color: rgb(59, 99, 241);
	}
	
	.recharge-but {
		background-color: rgb(223, 47, 49);
		height: 3.16667rem;
		font-size: 1.2rem;
		line-height: 3.16667rem;
		border-radius:.3rem;
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
					<div class="page-content user-main withdraw-main "	style="background: white; padding-bottom: 1rem;padding-top: 0;">
						<p class="radiusTtile">
							<span>缴纳强制执行费用</span>
						</p>
						<p class="radiusTtileMoney" id="payMoney">
							<span style="font-weight: bold">${forceMoney}元</span>
						</p>
						<div class="radius">
							<p>
								<span>借&nbsp;&nbsp;&nbsp;款&nbsp;&nbsp;&nbsp;人</span><span>${loaneeName}</span>
							</p>
							<p>
								<span>逾&nbsp;期&nbsp;金&nbsp;额</span><span>${loanMoney}元</span>
							</p>
							<p>
								<span>逾&nbsp;期&nbsp;时&nbsp;长</span><span>${delayDays}天</span>
							</p>
							<p>
								<span style="width: 25%;">账&nbsp;户&nbsp;余&nbsp;额</span><span style="width: 75%;"><i style="color: red;">${avlBal}</i>&nbsp;元<i style="float: right;"><a class="circle">去充值</a></i></span>
							</p>
							<p>
								<span style="color:#000;">点&nbsp;击&nbsp;查&nbsp;看</span><span><a href="javascript:void (0);" data-href="/mb/app/arbitrationDetail.html"  data-title="强执流程说明" class="item-link">《强执流程说明》</a></span>
							</p>
						</div>
						<a href="javascript:void 0" class="recharge-but removeDis payOnline" id="payBtn"> <span>立即支付</span></a>
						<form id="dataForm" action="${pageContext.request.contextPath}/app/wyjt/arbitrationExecution/payForExecution" method="post">
	                		<input type="hidden" id="memberToken" name="_header" value="${memberToken}" />
					        <input type="hidden" name="_type" value="h5">
					        <input type="hidden" id="forceId" name="forceId" value="${forceId}">
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
                <div class="center"><h3 id="sibTtile"></h3></div>
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
</body>
<script>
	define('partRepayNotOverdue', ['zepto', 'pop', 'jumpApp','panel'], function (require, exports, module) {
        var $ = require('zepto'), ev = $.support.touch ? 'tap' : 'click', pop = require('pop'),panel = require('panel'), call = require('jumpApp'),passwordVerify;
		//调用接口所需参数，在这里汇总定义
		var token = '${memberToken}',isEnough=${isEnough};

        //去充值loanPayPartLoan
        $('.circle')[ev](function () {
            var parameter = {"xxx": ""};
            call.callApp('recharge', parameter);
        });
        //立即支付
        $('.payOnline')[ev](function () {
            $.confirm('确定支付吗？',{
                btn: ['取消', '确定'],
                callback:function () {
                    pay();
                }
            });
        });
		$('.item-link')[ev](function () {
            var _this=$(this),src = _this.attr('data-href');
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
            return false; 
        });
        function pay() {
        	if(!isEnough){
                //提醒充值
                $.confirm('<p class="withdraw-pass item-password"><label for="withdrawPass">对不起，您的余额不足，请充值后再支付。</label></p>', {
                    callback: function () {
                        flag = false;
                        _this.goRecharge();
                    },
                    btn: ['再想想', '去充值'],
                    cls: 'withdraw-pass-dialog'
                });
          	}else{
	            if (!passwordVerify) {
	                require.async(['user/password-verify'], function (pverify) {
	                    passwordVerify = pverify.verify({
	                        token: token,
	                        action: 'checkPwd',
	                        pwdType:1,
	                        isShowMoney:1,
	                        payMoney:'${forceMoney}',
	                        title: '<span style="line-height: 2.6">输入支付密码</span>',
	                        callback: function (code, msg) {
	                            if (code) {
	                                var _pwd=this.opts.pwd;
	                                $.toast('操作成功', function () {
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
    });
    seajs.use('partRepayNotOverdue');
</script>
</html>