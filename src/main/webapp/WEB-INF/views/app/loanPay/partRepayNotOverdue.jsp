<%@ page contentType="text/html;charset=utf-8" language="java"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%@include file="/WEB-INF/views/app/constant.jsp" %>
<!DOCTYPE html>
<html>
<head>
	<title>还款详情</title>
	  <%@include file="/WEB-INF/views/app/meta-flex.jsp" %>
	<meta name="keywords" content="" />
	<meta name="description" content="" />
	<link rel="stylesheet" href="${mbStatic}/assets/css/public.css?v=<%=version%>" type="text/css">
	<link rel="stylesheet" href="${mbStatic}/assets/css/user.css?v=<%=version%>" type="text/css">
	<link rel="stylesheet" href="${mbStatic}/assets/css/repay.css?v=<%=version%>" type="text/css">
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
		height: 21.5rem;
		font-size: 1.1rem;
		background: white;
	}
	
	.radius p {
		height: 3.5rem;
		line-height: 3.5rem;
		border-bottom: 3px solid RGB(230, 230, 230);
	}
	
	.radius p span {
		display: inline-block;
		padding: .5rem 0rem;
		height: 2.5rem;
		line-height: 2.5rem;
	}
	
	.radius p span:first-child {
		width: 38%;
		float: left;
		text-align: left;
		color: RGB(153, 153, 153);
	}
	
	.radius p span:last-child {
		width: 60%;
	}
	
	.radiusTtile {
		text-align: center;
		font-size: 1.34rem;
		height: 3rem;
		line-height: 4rem;
	}
	
	.radiusTtileMoney {
		font-size: 2.5rem;
		text-align: center;
		font-weight: bold;
	}
	
	.circle {
		font-size: 1.1rem;
		background: url(${mbStatic}/assets/images/debt/toRight_blue@2x.png) 4rem 1.0rem
			no-repeat;
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
					<div class="page-content user-main withdraw-main "
						style="background: white; padding-bottom: 1rem;">
						<p class="radiusTtile">
							<span>应还金额（元）</span>
						</p>
						<p class="radiusTtileMoney" id="payMoney">
							￥<span style="font-weight: bold">${repayAmount}</span>
						</p>
						<div class="radius">
							<p>
								<span>原&nbsp;&nbsp;应&nbsp;&nbsp;还&nbsp;&nbsp;本&nbsp;&nbsp;金</span><span>${fns:decimalToStr(loan.amount,2)}</span>
							</p>
							<p>
								<span>原&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;利&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;息</span><span>${fns:decimalToStr(loan.interest,2)}</span>
							</p>
							<p>
								<span>延&nbsp;&nbsp;&nbsp;&nbsp;期&nbsp;&nbsp;&nbsp;&nbsp;利&nbsp;&nbsp;率</span><span>${delayIntRate}%</span>
							</p>
							<p>
								<span>延&nbsp;&nbsp;&nbsp;&nbsp;期&nbsp;&nbsp;&nbsp;&nbsp;利&nbsp;&nbsp;息</span><span>${delayInterest}</span>
							</p>
							<p>
								<span>还款后应还本金</span><span>${remainderAmt}</span>
							</p>
							<p>
								<span>还&nbsp;&nbsp;款&nbsp;&nbsp;后&nbsp;利&nbsp;&nbsp;息</span><span>${remainderInt}</span>
							</p>
							<p>
								<span style="width: 25%;">可用余额</span><span style="width: 75%;"><i style="color: red;">${avlBal}</i>&nbsp;元<i style="float: right;"><a class="circle">去充值</a></i></span>
							</p>
						</div>
						<br/>
						<a href="javascript:void 0" class="recharge-but removeDis payOnline" id="payBtn">在线支付</a> 
						<form id="dataForm" action="" method="post">
							<input type="hidden" id="memberToken" name="_header" value="${memberToken}">
							<input type="hidden" id="type" name="_type" value="h5">
							<input type="hidden" id=repayAmount name="repayAmount" value="${repayAmount}">
							<input type="hidden" id="nextRepayDate" name="nextRepayDate" value="${nextRepayDate}">
							<input type="hidden" id="loanId" name="loanId" value="${loan.id}">
							<input type="hidden" id="delayTerm" name="delayTerm" value="${delayTerm}">
							<input type="hidden" id="delayIntRate" name="delayIntRate" value="${delayIntRate}">
							<input type="hidden" id="delayInterest" name="delayInterest" value="${delayInterest}">
						</form>
					</div>
				</div>
			</div>
		</section>
	</article>
</body>
<script>
	define('partRepayNotOverdue', ['zepto', 'pop', 'jumpApp'], function (require, exports, module) {
        var $ = require('zepto'), ev = $.support.touch ? 'tap' : 'click', pop = require('pop'), call = require('jumpApp'),
                passwordVerify, token = '${memberToken}', repayMoney ='${repayAmount}', loanId ='${loan.id}';
        var isEnough = ${isEnough},isPayPsw=${isPayPsw};
        //去充值loanPayPartLoan
        $('.circle')[ev](function () {
        	goRecharge();
        });
        //在线支付
        $('.payOnline')[ev](function () {
            $.confirm('确定支付吗？',{
                btn: ['取消', '确定'],
                callback:function () {
                    pay();
                }
            });
        });
        function pay() {
        	if(!isEnough){
                //提醒充值
                $.confirm('<p class="withdraw-pass item-password"><label for="withdrawPass">对不起，您的余额不足，请充值后再支付。</label></p>', {
                    callback: function () {
                        goRecharge();
                    },
                    btn: ['再想想', '去充值'],
                    cls: 'withdraw-pass-dialog'
                });
          }else{
        		if (isPayPsw){
			            if (!passwordVerify) {
			                require.async(['user/password-verify'], function (pverify) {
			                    passwordVerify = pverify.verify({
			                        token: token,
			                        action: 'checkPwd',
			                        pwdType:1,
			                        isShowMoney:1,
			                        payMoney:repayMoney,
			                        title: '<span style="line-height: 2.6">输入支付密码</span>',
			                        callback: function (code, msg) {
			                            if (code) {
			                            	$.toast('操作成功', function () {
												var hasApply = ${hasApply};
												if(hasApply){
													$("#dataForm").attr("action","${pageContext.request.contextPath}/app/wyjt/loan/agreePartialRepay");
												}else{
													$("#dataForm").attr("action","${pageContext.request.contextPath}/app/wyjt/loan/partialRepay");
												}
			                            		$("#dataForm").submit();
						                         });
			                            } else $.toast(msg);
			                        }
			                    })
			                })
			            }
			            else passwordVerify.open();
        		}else{
	                $.alert('为了您的账户安全，请先去设置支付密码。', setPwd);
	            }
          }
        }
        
        function setPwd() {
            call.callApp('setpaypass');
        }
        function goRecharge(){
        	var parameter = {"xxx": ""};
            call.callApp('recharge', parameter);
        }
    });
    seajs.use('partRepayNotOverdue');
</script>
</html>