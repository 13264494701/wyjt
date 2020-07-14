<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%@include file="/WEB-INF/views/app/constant.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <title>信用报告认证支付</title>
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
            background: url(${mbStatic}//assets/images/debt/prompt@2x.png) 1rem 2rem no-repeat;
            background-size: 1.5rem
        }
        .circleTip{
            height: 2rem;
            line-height: 2rem;
            padding-left: 2.86rem;
            font-size: 1rem;
            color: RGB(143,143,143);
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
                    <p class="radiusTtile"><img src="${mbStatic}/assets/images/debt/expenditure@2x.png"><span>支付信用报告认证费</span></p>
                    <p class="radiusTtileMoney" id="payMoney">-1.00</p>
                    <div class="radius">
                        <p><span>金&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;额</span><span>1.00元</span></p>
                        <p><span>有&nbsp;效&nbsp;期</span><span>30天</span></p>
                        <p style="height: 3.5rem">
                            <a class="" href="javascript:void 0" style="color: rgb(81,118,184);width: 30%;" id="showBaogao">查看示例报告</a>
                        </p>
                    </div>
                    <a href="javascript:void 0" class="recharge-but removeDis immePay" id="payBtn"> <span>立即支付</span> </a>
                    <form id="dataForm" action="${pageContext.request.contextPath}/app/wyjt/member/payXinyanBaogaologic" method="post">
                		<input type="hidden" id="memberToken" name="_header" value="${memberToken}" />
				        <input type="hidden" name="_type" value="h5">
				        <input type="hidden" id="friendId" name="friendId" value="${friendId}">
				        <input type="hidden" id="amount" name="amount" value="1.00">
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
                passwordVerify, token ='${memberToken}',yxbId='${friendId}';
        $('#showBaogao')[ev](function () {
        	self.location="${pageContext.request.contextPath}/callback/sjmh/xinyanBgTemplate";
        	return false;
        });
        $('.immePay')[ev](function () {
            pop.confirm('确认支付吗？',function () {
                if (!passwordVerify) {
                    require.async(['user/password-verify'], function (pverify) {
                        passwordVerify = pverify.verify({
                            token: token,
                            action: 'checkPwd',
                            pwdType:1,
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
            })
        });
    });
    seajs.use('payXinyongBaogao');
</script>
</html>
