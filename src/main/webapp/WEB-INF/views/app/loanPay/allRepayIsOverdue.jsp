<%@ page contentType="text/html;charset=utf-8" language="java"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%@include file="/WEB-INF/views/app/constant.jsp" %>
  
<!DOCTYPE html>
<html>
<head>
    <title>还款详情</title>
     <%@include file="/WEB-INF/views/app/meta-flex.jsp" %>
    <meta name="keywords" content=""/>
    <meta name="description" content=""/>
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
            height: 15.5rem;
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
        .radius p span:first-child {
            width: 23%;
            float: left;
            text-align: left;
            color: RGB(153, 153, 153);
        }
        .radius p span:last-child {
            width: 77%;
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
        .circle{
            font-size: 1.1rem;
            background: url(${mbStatic}/assets/images/debt/toRight_blue@2x.png) 4rem 1.0rem no-repeat;
            background-size: .5rem;
            display: inline-block;
            width: 5rem;
            color: rgb(59,99,241);
        }
        .btn{
            margin-left: 3%;
        }
        .gray-Btn,.red-Btn{
            display: inline-block;
            width: 47%;
            -webkit-border-radius: 5px;
            -moz-border-radius: 5px;
            border-radius: 5px;
        }
        .gray-Btn{
            background: rgb(248,182,0);
            margin-right:.5rem;
        }
        .pop-layout{
			top:-10rem;
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
                    <p class="radiusTtile"><span>应还金额（元）</span></p>
                    <p class="radiusTtileMoney" id="payMoney">￥<span style="font-weight: bold">${loan.dueRepayAmount}</span></p>
                    <div class="radius">
                        <p><span>借条本金</span><span>${loan.amount}</span></p>
                        <p><span>借条利息</span><span>${loan.interest}</span></p>
                       	<c:if test="${isOverdue eq true}">
                       		<p><span>逾期利息</span><span>${overdueInterest}（<i style="color: red;font-size: 1rem;">${reminder}</i>）</span></p>
                       	</c:if>
                       <p><span>可用余额</span><span><i style="color: red;">${avlBal}</i>&nbsp;元<i style="float: right;">余额不足？&nbsp;<a class="circle">去充值</a></i></span></p>
                    </div>
                    <div class="btn">
                        <a class="gray-Btn payOnline" href="javascript:void(0);">在线支付</a>
                        <a class="red-Btn payUnderline"  href="javascript:void(0);">线下已还款</a>
                    </div>
                    <form id="dataForm" action="" method="post">
                   		<input type="hidden" name="_header" value="${memberToken}">
                   		<input type="hidden" name="_type" value="h5">
                   		<input type="hidden" id="loanId" name="loanId" value="${loan.id}">
                   		<input type="hidden" id="overdueInterest" name="overdueInterest" value="${overdueInterest}">
                   </form>
                </div>
            </div>
        </div>
    </section>
</article>
</body>
<script>
    define('payXinyongBaogao', ['zepto', 'pop', 'jumpApp'], function (require, exports, module) {
        var $ = require('zepto'), ev = $.support.touch ? 'tap' : 'click', pop = require('pop'), call = require('jumpApp'),
        		 passwordVerify, token = '${memberToken}', overdueInterest ='${overdueInterest}',loanId ='${loan.id}',loanMoney ='${loan.dueRepayAmount}';
        		 
        var flag = false,passwordVerify,isPayPsw=${isPayPsw},isEnough=${isEnough};
        //去充值
        $('.circle')[ev](function () {
            goRecharge();
        });
        //在线支付
        $('.payOnline')[ev](function () {
        	
	            $.confirm('确定支付吗？',{
	                btn: ['取消', '确定'],
	                callback:function () {
	                    pay('YXBRepayLoanAll',loanMoney);
	                }
	            });
        });
        //线下已还款
        $('.payUnderline')[ev](function () {
        	if (isPayPsw == '1'){
	            $.confirm('<p class="withdraw-pass item-password">您已选择通过其他方式还款，请您</p><p>及时您的好友确认本次还款，对方</p><p>确认后本次还款才可完成。</p>',{
	                btn: ['取消', '确定'],
	                callback:function () {
	                    pay('YXBRepayDownLoan',overdueInterest);
	                }
	            });
        	}else{
        		$.toast('请先设置支付密码！');
        	}
        });
        function pay(action,money) {
        	if (flag) {
                $.toast('请勿重复提交~！');
                return false;
            };
        	flag = true;
        	if(action == "YXBRepayDownLoan"){
        		isEnough = true;
        	}
        	if(!isEnough){
                flag = false;
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
	        		flag = false;
			        	if (!passwordVerify) {
				           	require.async(['user/password-verify'], function (pverify) {
					              passwordVerify = pverify.verify({
				                    token: token,
				                    action: 'checkPwd',
				                    pwdType:1,
				                    isShowMoney:1,
				                    payMoney:money,
				                    title: '<span style="line-height: 2.6">输入支付密码</span>',
				                    callback: function (code, msg) {
				                        if (code) {
				                            var _pwd=this.opts.pwd;
				                            var url = "";
				                            if(action == "YXBRepayDownLoan"){
				                          		  url = "${pageContext.request.contextPath}/app/wyjt/loan/repayDownLine";
				                            }else{
				                           		  url = "${pageContext.request.contextPath}/app/wyjt/loan/repayAll";
				                            }
				                            $.toast('操作成功', function () {
						                    	 $("#dataForm").attr("action",url);
						                    	 $("#dataForm").submit();
				                            });
				                        } else $.toast(msg);
				                    }
				                })
				            })
			           }else passwordVerify.open();
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
    seajs.use('payXinyongBaogao');
</script>
</html>