<%@ page contentType="text/html;charset=utf-8" language="java"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%@include file="/WEB-INF/views/app/constant.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <title>缴纳强执费</title>
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
	                                    .gray-Btn,.red-Btn{
	                                        border-radius:.3rem;
	                                    }
	                                </style>
	                        <div class="listbox">
	                            <div class="paysucessed">
	                              <c:choose>
	                            	<c:when test="${code eq '0'}">
		                                <p style="margin-top: 3rem;"><img src="${mbStatic}/assets/images/debt/success@2x.png"></p>
		                                <p style="font-size: 1.5rem;margin-top: 1rem;margin-bottom: .3rem;font-weight: bold">支付成功</p>
		                                <p style="margin:2rem;font-size: 1rem;color: RGB(109,109,109);">您可以在催收仲裁模块的“查看记录”中查看仲裁进度哦~</p>
	                                </c:when>
	                            	<c:otherwise>
		                                <p style="margin-top: 3rem;"><img src="${mbStatic}/assets/images/debt/err@2x.png"></p>
		                                <p style="font-size: 1.5rem;margin-top: 1rem;margin-bottom: .3rem;font-weight: bold">支付失败</p>
		                                <p style="color:red;margin-bottom:1rem;font-size: 1rem">${message}</p>
	                              </c:otherwise>
	                            </c:choose>
	                            </div>
	                        </div>
	                        <div class="btn">
	                            <style>
	                                .btn a{display: inline-block;width: 49%;}
	                            </style>
	                            <c:choose>
	                            	<c:when test="${code eq '0'}">
	                            	 	 <a class="gray-Btn" href="javascript:void(0);" style="background: RGB(248,181,42);">返回首页</a>
	                            	 	 <a class="red-Btn" href="javascript:void(0);">查看仲裁进度</a>
	                            	</c:when>
	                            	<c:otherwise>
	                            		 <a class="gray-Btn" href="javascript:void(0);" style="background: RGB(248,181,42); width: 100%;">返回首页</a>
	                            	</c:otherwise>
	                            </c:choose>
	                          <form id="dataForm" action="${pageContext.request.contextPath}/app/wyjt/arbitration/goArbSchedule" method="post">
									<input type="hidden" name="_header" value="${memberToken}">
			                   		<input type="hidden" name="_type" value="h5">
			                   		<input type="hidden" id="arbRecordId" name="arbRecordId" value="${arbRecordId}" />
	                       	  </form>
	                        </div>
	                    </div>
	                </div>
	            </div>
	        </div>
	    </section>
	</article>
</body>
<script>
    define('payEnforcementResult', ['zepto', 'jumpApp'], function (require, exports, module) {
    	var $ = require('zepto'),ev = $.support.touch ? 'tap' : 'click', call = require('jumpApp'),loanId='${loanId}';

        $('.red-Btn')[ev](function(){
            $("#dataForm").submit();
        });
        $('.gray-Btn')[ev](function(){
            backIndex();
        });

        //返回首页
        function backIndex () {
//            call.gotoApp();
            call.callApp('backIndexPage');
        }
    });

    seajs.use(['payEnforcementResult', 'jumpApp']);
</script>
</html>