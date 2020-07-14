<%@ page contentType="text/html;charset=utf-8" language="java"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%@ include file="/WEB-INF/views/app/constant.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <title>完善信息</title>
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
                <form id="form" method="post" action="">
                    <div class="wsxx"><i></i></div>
                    <div class="wsxxContent">
                        <i class="wsxxTip"></i>
                        <p style="margin-top: 3.5rem">
                           <input type="hidden"  name="applyerId" value="${applyer.id}">
                        	<input type="hidden"  name="phoneNo" value="${applyer.phoneNo}">
                            <span>姓名</span><input type="text"  name="userName" value="${applyer.name}" readonly="readonly">
                        </p>
                         <p>
                            <span>身份证号</span><input type="text" name="userId" value="${applyer.idNo}" readonly="readonly">
                        </p>
                        <p>
                            <span>银行卡号</span><input type="text" name="bankCardNo" value="${applyer.cardNo}" readonly="readonly">
                        </p>
                        <p>
                            <span>微信号</span><input type="text" name="weixinNo" value="${applyer.weixinNo}" readonly="readonly">
                        </p>	                    
						<p style="border-bottom:none">
                            <span>芝麻分</span> <c:if test="${applyer.sesameStatus eq 'authed'}"><span class="">芝麻分已认证</span></c:if><c:if test="${applyer.sesameStatus eq 'unauth'}"><span id="goZmfRz" class="orange">点击认证</span></c:if>
                        </p>
                        <p>
                            <span>运营商</span> <c:if test="${applyer.operatorStatus eq 'authed'}"><span class="">运营商已认证</span></c:if><c:if test="${applyer.operatorStatus eq 'unauth'}"><span id="goYysRz" class="orange">点击认证</span></c:if>	
                        </p>						
                        <div class="remind">
                            <b>注意</b>
                            <div>您的信息仅用于放款审核，贷款过程中不会向您收取任何费用！</div>
                        </div>
                        <a class="wsxxSub orange">提交申请</a>
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
	    var zmfAuth = '${applyer.sesameStatus}'=='authed'?true:false, yysAuth = '${applyer.operatorStatus}'=='authed'?true:false,allAuth = false;
	    var $goZmfRz = $("#goZmfRz"),$goYysRz = $("#goYysRz");
	     $goZmfRz[ev](function (){
	    	 //先获取芝麻分认证的token 然后带着token去认证，拿到认证结果后跳回页面
	    	var gxbUrl = "${pageContext.request.contextPath}/wap/wyjt/ufangDebt/sesameAuth";
	       	$("#form").attr('action',gxbUrl); 
	       	$("#form").submit();
	     });
	     $goYysRz[ev](function (){
	    	 //跳转运营商认证
	    	 var sjmhUrl = "${pageContext.request.contextPath}/wap/wyjt/ufangDebt/operatorAuth";
	        	$("#form").attr('action',sjmhUrl); 
	        	$("#form").submit();
	     });
	    
	    $wsxxSub[ev](function () {
	        var str='';
	        if(zmfAuth && yysAuth){
	        	allAuth = true;    	
	        }
	        if(allAuth){
	        	//都认证之后可以申请
	        	var subUrl = "${pageContext.request.contextPath}/wap/wyjt/ufangDebt/apply";
	        	$("#form").attr('action',subUrl);
	        	$("#form").submit();
	        }else if(!zmfAuth){
	        	$.toast("请完成芝麻分认证");
	        }else if(!yysAuth){
	        	$.toast("请完成运营商认证");
	        }
	    });
	});
	seajs.use(['otherLoan']);
</script>
</body>
</html>