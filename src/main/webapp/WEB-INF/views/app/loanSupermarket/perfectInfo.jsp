<%@ page contentType="text/html;charset=utf-8" language="java"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%@include file="/WEB-INF/views/app/constant.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <title>完善信息</title>
    <%@include file="/WEB-INF/views/app/meta-flex.jsp" %>
    <meta name="keywords" content=""/>
    <meta name="description" content=""/>
    <link rel="stylesheet" href="${mbStatic}/assets/css/public.css?v=<%=version%>" type="text/css">
    <link rel="stylesheet" href="${mbStatic}/assets/css/user.css?v=<%=version%>" type="text/css">
    <script src="${mbStatic}/assets/scripts/base/sea.js?v=<%=version%>21"></script>
    <script src="${mbStatic}/assets/scripts/base/flexible.js?v=<%=version%>"></script>
    <style type="text/css">
        .pop-gray.pop-dialogs .pop-btn {
            
        }

        .pop-info {
            margin-top: 1rem;
        }
        .wsxx{
            width: 100%;
            height: 12rem;
            background: #e33336;
        }
        .wsxx i{
            width: 25rem;
            height: 7rem;
            display: inline-block;
            background: url(${mbStatic}/assets/images/debt/czhl/wsxxSpeed.png) 3rem 3rem no-repeat;
            background-size: 83%;
        }
        .wsxxContent{
            width: 90%;
            height: 31rem;
            /*border: 1px red solid;*/
            position: absolute;
            left: 1.3rem;
            top: 8rem;
            background: white;
            border-radius: .5rem;
        }
        .wsxxTip{
            position: absolute;
            background:url("${mbStatic}/assets/images/debt/czhl/wsxxTip.png") no-repeat;
            background-size: 100%;
            display: inline-block;
            width: 10rem;
            height: 3rem;
            left: 6.5rem;
        }
        input::-webkit-input-placeholder {
            color: #dcdcdc;
            font-size:1rem;
        }
        .wsxxContent p{
            width: 82%;
            height: 4rem;
            line-height: 4rem;
            padding-left: .5rem;
            margin-left: 2rem;
            font-size: 1.1rem;
            border-bottom: 1px solid rgb(225,225,225);
        }
        .wsxxContent span{
            width: 5rem;
            height: 3rem;
            line-height: 3rem;
            display: inline-block;
        }
        .wsxxContent input{
            text-align: right;
            width: 70%;
            height: 3rem;
            border:none;
        }
        .wsxxContentVer{
            width: 89%;
            height: 4rem;
            line-height: 4rem;
            font-size: 1.1rem;
            margin-left: 1.5rem;
            margin-top:.5rem;
        }
        .wsxxContentVer a{
            width: 47%;
            height: 3rem;
            line-height: 3rem;
            display: inline-block;
            background: #cbcbcb;
            text-align: center;
            border-radius: .3rem;
            color: white;
        }
        .wsxxContentVer a:first-child{
            margin-right: 1rem;
        }
        .verfied{
            background-image: linear-gradient(-14deg,rgba(255, 44, 44, 0.74) 0%, rgba(255, 153, 20, 0.74) 99%),
	            linear-gradient(#faaf00,#faaf00)!important;
            pointer-events:auto;
        }
        .remind{
            width: 86%;
            margin-left: 1rem;
        }
        .remind b{
            height: 2rem;
            display: inline-block;
            background: url(${mbStatic}/assets/images/debt/prompt@2x.png) 0rem .2rem no-repeat;
            background-size: 1.3rem;
            padding-left: 1.8rem;
            font-size: 1rem;
            color: red;
            margin-top: .5rem;
        }
        .remind div{
            width: 96%;
            margin-left: 1.8rem;
            color: #7b6d3d;
        }
        .wsxxSub{
            width: 92%;
            height: 3rem;
            background: #cbcbcb;
            text-align: center;
            color: #ffffff;
            display: inline-block;
            border-radius: .5rem;
            letter-spacing:.1rem;
            font-size: 1.3rem;
            font-family: HiraginoSansGB-W6;
            line-height: 3rem;
            margin-top:.5rem;
            margin-left: 1rem;
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
                <form id="form" method="post" action="otherLoan.jsp" >
                    <div class="wsxx"><i></i></div>
                    <div class="wsxxContent">
                        <i class="wsxxTip"></i>
                        <p style="margin-top: 3.5rem">
                            <span>姓名</span><input type="text" placeholder="请输入您的姓名" name="userName" value="${userName}" readonly="readonly">
                        </p>
                        <p>
                            <span>身份证号</span><input type="text" placeholder="请输入您的身份证号" name="userId" value="${userId}" readonly="readonly">
                        </p>
                        <p>
                            <span>银行卡号</span><input type="text" placeholder="请输入您的银行卡号" name="bankCardNo" value="${bankCardNo}" readonly="readonly">
                        </p>
                        <div class="wsxxContentVer"><a href="javascript:void 0;" class="verfied">芝麻分认证</a><a href="javascript:void 0;">运营商认证</a></div>
                        <div class="remind">
                            <b>注意</b>
                            <div>为了尽快审核通过，请填写您的真实信息，您的信息仅用放款审核，贷款过程中不会向您收取任何费用！</div>
                        </div>
                        <a class="wsxxSub verfied">立即申请</a>
                    </div>
                </form>
            </div>
        </div>
    </section>
</article>

<script type="text/javascript">
    window.panel=null;
    define('otherLoan', ['zepto', 'pop','panel','jumpApp'], function (require, exports, module) {
        var $ = require('zepto'), pop = require('pop'),call = require('jumpApp'),panel=require('panel'),md5;
        var ev = $.support.touch ? 'tap' : 'click',$wsxxSub=$('.wsxxSub');

        $wsxxSub[ev](function () {
            var str='';
            str = checking();
            console.log(str);
        });

        function checking() {
            var str='';
            return str;
        }
    });
    seajs.use(['otherLoan']);
</script>
</body>
</html>
