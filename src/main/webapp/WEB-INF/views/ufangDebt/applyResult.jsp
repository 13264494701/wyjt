<%@ page contentType="text/html;charset=utf-8" language="java"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%@ include file="/WEB-INF/views/app/constant.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <title>等待放款</title>
    <meta charset="utf-8">
    <meta name="keywords" content=""/>
    <meta name="description" content=""/>
	<link rel="stylesheet" href="${mbStatic}/assets/css/public.css?v=<%=version%>" type="text/css">
    <link rel="stylesheet" href="${mbStatic}/ufangDebt/css/loanSMarket.css" type="text/css">   
    <script src="${mbStatic}/assets/scripts/base/sea.js?v=<%=version%>212"></script>
    <script src="${mbStatic}/assets/scripts/base/flexible.js?v=<%=version%>"></script>
    <script src="${mbStatic}/ufangDebt/js/zepto.min.js?v=8143"></script>
</head>
<body>
<article class="views">
    <section class="view">
        <div class="pages navbar-fixed toolbar-fixed">
            <div class="page">
                <div class="wsxx"></div>
                <div class="wsxxContentResult">
                    <i class="wsxxTipResult"></i>
                    <div>
                        <b></b>
                        <span id="sussesTip">您的资料已提交审核！</span>
                        <span>如资料审核通过<br/>我们将会在1--7个工作日联系您！<br/>祝您申请成功！</span>
                        <a class="wsxxSub orange" id="return">返回</a>
                    </div>
                </div>
            </div>
        </div>
    </section>
</article>

<script type="text/javascript">
	window.panel=null;
	define('otherLoan', ['zepto', 'pop','panel','jumpApp'], function (require, exports, module) {
	    var $ = require('zepto'), pop = require('pop'),call = require('jumpApp'),panel=require('panel'),md5;
	    var ev = $.support.touch ? 'tap' : 'click';
	    var $backIndex = $(".wsxxSub");
	    $backIndex[ev](function(){
			location.href = "${pageContext.request.contextPath}/wap/wyjt/ufangDebt/index";        	
	    });
	});
	seajs.use(['otherLoan']);
</script>
</body>
</html>