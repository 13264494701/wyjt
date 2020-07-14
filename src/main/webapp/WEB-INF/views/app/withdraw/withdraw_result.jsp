<%@ page contentType="text/html;charset=utf-8" language="java"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%@include file="/WEB-INF/views/app/constant.jsp" %>
<!DOCTYPE html>
<html xmlns=http://www.w3.org/1999/xhtml>
<head>
    <title>账户提现</title>
    <%@include file="/WEB-INF/views/app/meta-flex.jsp" %>
    <meta name="keywords" content=""/>
    <meta name="description" content=""/>
    <link rel="stylesheet" href="${mbStatic}/assets/css/public.css?v=<%=version%>" type="text/css">
    <link rel="stylesheet" href="${mbStatic}/assets/css/repay.css?v=<%=version%>" type="text/css">
    <script src="${mbStatic}/assets/scripts/base/sea.js?v=<%=version%>21"></script>
    <script src="${mbStatic}/assets/scripts/base/flexible.js?v=<%=version%>"></script>
    <style>

        .radius {
            border-top: 1px RGB(219, 219, 219) solid;
            margin: 1rem;
            height: 12rem;
            font-size: 1.1rem;
            background: white;
        }

        .radius p {
            height: 4rem;
            border-bottom: 1px RGB(226, 226, 226) solid;
        }

        .radius p span {
            display: inline-block;
            padding: .5rem 1rem;

            height: 3rem;
            line-height: 3rem;
        }

        .radius p :first-child {
            width: 22%;
            float: left;
            text-align: left;
        }

        .radius p :last-child {
            width: 58%;
            float: right;
            text-align: right;
            color: RGB(153, 153, 153);
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
       
       <style>
		    .navbar-inner, .toolbar-inner{
		        font-size:1.25rem;
		        padding: 0 1.2rem;
		    }
		    .navbar .right{
		        margin-left:0;
   			 }
	    	.navbar h1{
	        font-weight:normal;
	        font-size: 1.55rem;
   			 }
	  </style>
		<header class="navbar">
    		<div class="navbar-inner">
    			<div class="left"><a class="goBack link" href="javascript:void 0" data-method="go" data-href="back2app"><i class="icon icon-back"></i><span></span></a></div>
      		  <div class="center"><h1>账户提现</h1></div>
        		<div class="right"><a href="javascript:void 0" class="link" id="recordBtn" >提现记录</a></div>
    	   </div>
       </header>
        <div class="pages navbar-fixed toolbar-fixed">
            <div class="page">
          		    <form id="tokenForm" action=""	method="post">
                		<input type="hidden" name="_header" value="${memberToken}"/>
                		<input type="hidden" name="_type" value="h5"/>
                	</form>
                <div class="page-content user-main withdraw-main">
                    <div class="bg">
                        <div class="listbox">
                            <div class="paysucessed">
                                <style>
                                    .paysucessed p.p2:before {
                                        background: url(${mbStatic}/assets/images/repay/right_blue.png) left top no-repeat;
                                        background-size: 100%;
                                        width: 5.33333rem;
                                        height: 5.33333rem;
                                    }
                                </style>
                                <p class="p2" style="margin: 2rem 0 1rem 0;"></p>
                                <p style="font-size: 1.2rem;font-weight: bold;margin-bottom: 2rem;">提现<span
                                        style="color: red; ">申请</span>成功</p>
                                <div class="radius">
                                    <p><span>储值卡</span><span>${bankName} 尾号 ${ cardNo}</span></p>
                                    <p><span>提现时间</span><span>${createTime}</span></p>
                                    <p><span>提现金额</span><span>人民币 ${money}元</span></p>
                                </div>
                            </div>
                        </div>
                        <div>
                            <a class="red-Btn" href="javascript:void(0);">返回无忧借条客户端</a>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </section>
</article>
</body>

<script type="text/javascript">
    seajs.use(['zepto','jumpApp'], function ($,jumpApp) {
        var ev = $.support.touch ? 'tap' : 'click';
    	var  $recordBtn=$('#recordBtn');
        $('.red-Btn')[ev](function () {
            var appPlatform = '${appPlatform}', jump;
            if (appPlatform.indexOf("ios") > -1) {
                jump = "backindexpage://";
                location.href = jump;
            } else {
//                jump = "m://yxinbao.com";
                jumpApp.callApp('backMyCenter');
            }
        });
        $recordBtn[ev](function () {
      	   $("#tokenForm").attr("action","${pageContext.request.contextPath}/app/wyjt/member/withdrawList");
           $("#tokenForm").submit();
         });
    });
</script>
</html>
