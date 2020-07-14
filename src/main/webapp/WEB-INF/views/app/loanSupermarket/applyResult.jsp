<%@ page contentType="text/html;charset=utf-8" language="java"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%@include file="/WEB-INF/views/app/constant.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <title>等待放款</title>
    <%@include file="/WEB-INF/views/app/meta-flex.jsp" %>
    <meta name="keywords" content=""/>
    <meta name="description" content=""/>
    <link rel="stylesheet" href="<%=httpUrl%>assets/css/public.css?v=<%=version%>" type="text/css">
    <link rel="stylesheet" href="<%=httpUrl%>assets/css/user.css?v=<%=version%>" type="text/css">
    <script src="<%=cdnUrlJS%>assets/scripts/base/sea.js?v=<%=version%>21"></script>
    <script src="<%=cdnUrlJS%>assets/scripts/base/flexible.js?v=<%=version%>"></script>
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
        .wsxxContent{
            width: 90%;
            height: 31rem;
            /*border: 1px red solid;*/
            position: absolute;
            left: 1.3rem;
            top: 2rem;
            background: white;
            border-radius: .5rem;
            text-align: center;
        }
        .wsxxTip{
            position: absolute;
            background:url("${mbStatic}/assets/images/debt/czhl/applyResult.png") no-repeat;
            background-size: 100%;
            display: inline-block;
            width: 11rem;
            height: 3rem;
            left: 6.5rem;
        }
        .wsxxContent div{
            margin-top:4rem;
        }
        .wsxxContent div b,.wsxxContent div span{
            display: inline-block;
            width: 20rem;
            font-size: 1rem;
            color: #898989;
        }
        .wsxxContent b{
            background:url("${mbStatic}/assets/images/debt/success@2x.png") 7rem 1rem  no-repeat;
            background-size:5rem;
            height: 6rem;
            margin-bottom: 2rem;
        }
        #sussesTip{
            font-weight: bold;
            font-size: 1.3rem;
            margin: 1rem 0;
            color: #313131;
        }
        .wsxxSub{
            width: 92%;
            height: 3rem;
            background: #cbcbcb;
            text-align: center;
            display: inline-block;
            border-radius: .5rem;
            letter-spacing:.1rem;
            font-size: 1.3rem;
            font-family: HiraginoSansGB-W6;
            line-height: 3rem;
            pointer-events:none;
            margin-top:4rem;
            background-image: linear-gradient(-14deg,rgba(255, 44, 44, 0.74) 0%, rgba(255, 153, 20, 0.74) 99%),
	            linear-gradient(#faaf00,#faaf00)!important;
            pointer-events:auto;
            color: white;
            letter-spacing: .3rem;
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
                    <div class="wsxx"></div>
                    <div class="wsxxContent">
                        <i class="wsxxTip"></i>
                        <div>
                            <b></b>
                            <span id="sussesTip">您的申请已提交！</span>
                            <span>请耐心等待匹配放款<br />我们将会在1-3个工作日联系您！</span>
                            <a class="wsxxSub verfied">返回</a>
                        </div>

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
        var ev = $.support.touch ? 'tap' : 'click';
    });
    seajs.use(['otherLoan']);
</script>
</body>
</html>
