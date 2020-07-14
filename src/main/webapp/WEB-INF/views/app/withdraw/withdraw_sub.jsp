<%@ page contentType="text/html;charset=utf-8" language="java"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%@include file="/WEB-INF/views/app/constant.jsp" %>
<!DOCTYPE html>
<html xmlns=http://www.w3.org/1999/xhtml>
<head>
    <title>提现</title>
    <%@include file="/WEB-INF/views/app/meta-flex.jsp" %>
    <meta name="keywords" content=""/>
    <meta name="description" content=""/>
    <link rel="stylesheet" href="${mbStatic}/assets/css/public.css?v=<%=version%>" type="text/css">
    <link rel="stylesheet" href="${mbStatic}/assets/css/user.css?v=<%=version%>" type="text/css">
    <script src="${mbStatic}/assets/scripts/base/sea.js?v=<%=version%>21"></script>
    <script src="${mbStatic}/assets/scripts/base/flexible.js?v=<%=version%>"></script>
    <style>
        .radius{
            border-radius: .5rem;
            /*border: 1px RGB(219,219,219) solid;*/
            margin: 1rem;
            height: 19.5rem;
            font-size: 1.1rem;
            background: white;
        }

        .radius p{
            height: 3.5rem;
            /*border-bottom:1px RGB(226,226,226) solid ;*/
        }
        .radius p span{
            display: inline-block;
            padding:.5rem 1rem;
            height: 2rem;
            line-height: 2rem;
        }

        .radius p :first-child{
            width: 20%;
            float: left;
            text-align: left;
            color: RGB(153,153,153);
        }
        .radius p :last-child{
            width: 50%;
            /*float: right;*/
            /*text-align: right;*/
        }
        .radiusTtile {
            text-align: center;
            font-size: 1.34rem;
            height: 3rem;
            line-height: 4rem;
        }
        .radiusTtile img{
            margin-right: .5rem;
            width: 2.5rem;
            display: inline-block;
            margin-bottom: .3rem;
        }
        .radiusTtileMoney{
            font-size: 2.5rem;
            text-align: center;
            font-weight: bold;
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
            <jsp:param name="title" value="账户提现"></jsp:param>
            <jsp:param name="type" value="tx_withdrawList"></jsp:param>
        </jsp:include>
         <div class="pages navbar-fixed toolbar-fixed">
            <div class="page">
            		<form id="tokenForm" action=""	method="post">
                		<input type="hidden" id="money" name="money" />
                		<input type="hidden" name="_header" value="${memberToken}"/>
                		<input type="hidden" name="_type" value="h5"/>
                	</form>
                <div class="page-content user-main withdraw-main" style="background: white;padding-bottom:1rem;">
                    <p class="radiusTtile"><img src="${mbStatic}/assets/images/debt/tx@2x.png"><span>账户提现（${member.name}）</span></p>
                    <p class="radiusTtileMoney">- ${money}</p>
                    <div class="radius">
                    	<p><span>用&nbsp;户&nbsp;名</span><span>${member.name}</span></p>
                        <p><span>提现金额</span><span>${money}元</span></p>
                        <p><span>可用余额</span><span>${fns:decimalToStr(curBal,2)}元</span></p>
                        <p><span>提现费用</span><span>${withdrawSxf}元</span></p>
                        <p><span>实际到账</span><span>${actualMoney}元</span></p>
                        <p style="border-bottom: none;"><span>银行卡号</span><span>${cardNo}</span></p>
                    <a href="javascript:void 0" class="recharge-but" id="conformWithdraw" style="display: block;background: #E71B1B;margin-top: 3rem;"> <span>确认提现</span> </a>
                </div>
                <input type="hidden" id="utk" value="${memberToken}">
            </div>
        </div>
    </section>
</article>
</body>
<script type="text/javascript">
    define('withdraw', ['zepto', 'pop','jumpApp'], function (require, exports, module) {
        var $ = require('zepto'), pop = require('pop'),call = require('jumpApp');
        var ev = $.support.touch ? 'tap' : 'click',money='${money}',token=$('#utk').val(),
            passwordVerify,$recordBtn=$('#recordBtn');
        $('#conformWithdraw')[ev](function () {
            pop.confirm('确认提交该笔提现吗？',function () {
               var isPayPsw = '${isPayPsw}';
               if(isPayPsw){
                if (!passwordVerify) {
                    require.async(['user/password-verify'], function (pverify) {
                        passwordVerify = pverify.verify({
                        	token: token,
                            action: 'checkPwd',
                            pwdType:1,
                            title: '<span style="line-height: 2.6">输入支付密码</span>',
                            callback: function (code, msg) {
                                if (code) {
                                	var payPassword=this.opts.pwd;
                                     $.toast('操作成功', function () {
                                    	 $("#money").val(money);
                                    	 $("#tokenForm").attr("action","${pageContext.request.contextPath}/app/wyjt/member/submit");
                                         $("#tokenForm").submit();
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
            })
        });
        $recordBtn[ev](function () {
     	   $("#tokenForm").attr("action","${pageContext.request.contextPath}/app/wyjt/member/withdrawList");
            $("#tokenForm").submit();
        });
        function setPwd() {
            call.callApp('setpaypass');
        }

    });
    seajs.use(['withdraw', 'jumpApp']);
</script>
</html>
