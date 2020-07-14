<%@ page contentType="text/html;charset=utf-8" language="java"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%@include file="/WEB-INF/views/app/constant.jsp" %>
<!DOCTYPE html>
<html>
<head>
	<title>付款单</title>
    <%@include file="/WEB-INF/views/app/meta-flex.jsp" %>
    <meta name="keywords" content=""/>
    <meta name="description" content=""/>
    <link rel="stylesheet" href="${mbStatic}/assets/css/public.css?v=<%=version%>" type="text/css">
    <link rel="stylesheet" href="${mbStatic}/assets/css/repay.css?v=<%=version%>" type="text/css">
  	<script src="${mbStatic}/assets/scripts/base/sea.js?v=<%=version%>21"></script>
    <script src="${mbStatic}/assets/scripts/base/flexible.js?v=<%=version%>"></script>
</head>
<c:choose>
	<c:when test="${empty appPlatform}">
		<body>	
	</c:when>
	<c:otherwise>
		<body class="${isWeiXin? 'InWeixin':'InWebView'}" data-platform="${appPlatform }"  data-app="51jt">
	</c:otherwise>
</c:choose>
<article class="views" style="max-width:100%">
    <section class="view">
        <div class="pages navbar-fixed">
	     		<jsp:include page="/WEB-INF/views/app/header-public.jsp">
		            <jsp:param name="title" value="购买转让借条"></jsp:param>
		            <jsp:param name="url" value="payResult"></jsp:param>
	       	 	</jsp:include>
            <div class="page">
                <div class="page-content" style="background: white">
                    <!--content -->
                    <div class="bg" style="background: white">
                                <style>
                                    .listbox{
                                        border:0;
                                    }
                                    .listbox:after{
                                        background:none;
                                    }
                                    .paysucessed p.p4{
                                        margin:0 0 0 2.68rem
                                    }
                                    .paysucessed img{
                                        width: 5rem;
                                    }
                                </style>
                        <div class="listbox">
                            <div class="paysucessed">
	                                <p style="margin-top: 3rem;"><img src="${mbStatic}/assets/images/debt/err@2x.png"></p>
	                                <p style="font-size: 1.5rem;margin-top: 1rem;margin-bottom: .3rem;font-weight: bold">操作失败</p>
	                                <p style="color:red;margin-bottom:1rem;font-size: 1rem">${message}</p>
                            </div>
                        </div>
                        <div class="btn">
                            <style>
                            	body{ text-align:center} 
                                .btn a{display: inline-block;width: 49%;}
                            </style>
	                            <a class="gray-Btn" href="javascript:void(0);" style="background: RGB(248,181,42);">返回首页</a>
	                            <a class="red-Btn" href="javascript:void(0);">继续购买</a>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </section>
</article>
</body>
<script>
    define('payResultV2', ['zepto', 'jumpApp'], function (require, exports, module) {
        var $ = require('zepto'), ev = $.support.touch ? 'tap' : 'click', call = require('jumpApp'),plat='${appPlatform}';

        $('.red-Btn')[ev](function(){
        	againBuy();
        });
        $('#back2Cr')[ev](function(){
        	againBuy();
        });
        $('.gray-Btn')[ev](function(){
            backIndex();
        });
        //查看我的借条
        function againBuy () {
        	  call.callApp('againbuy');
        }

        //返回首页
        function backIndex () {
//            call.gotoApp();
            call.callApp('backIndexPage');
        }
    });

    seajs.use(['payResultV2', 'jumpApp']);
</script>
</html>
