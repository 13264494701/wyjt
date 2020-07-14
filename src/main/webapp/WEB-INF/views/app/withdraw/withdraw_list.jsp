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
    <link rel="stylesheet" href="${mbStatic}/assets/css/user/draw.css?v=<%=version%>" type="text/css">
    <script src="${mbStatic}/assets/scripts/sea-modules/seajs/2.3.0/sea.js?v=<%=version%>"></script>
    <script src="${mbStatic}/assets/scripts/base/flexible.js?v=<%=version%>"></script>
    <style>
        .tabBar{
            background: white;
            width: 96%;
            height: 4rem;
            line-height: 4rem;
            border: 0px solid red;
            margin-bottom: .5rem;
            padding: 0 .5rem;
        }
        ul{
        }
        .tabBar li{
            list-style-type: none;
            float: left;
            width: 32%;
            height: 3.5rem;
            line-height: 3.5rem;
            font-size: 1.2rem;
            color: RGB(43,43,43);
            text-align: center;
            margin-top:.3rem;
        }
        ul li:first-child{
            border-right: 1px solid RGB(213,213,213);

        }
        ul li:last-child{
            border-left: 1px solid RGB(213,213,213);
        }
        .tabBar .checked{
            border-bottom: .245rem solid red;
            color: red;
            font-weight: bold;
        }
        .tkjilu{
            border-bottom: 1px solid RGB(237,237,237);
            padding: .5rem 0.083333rem 0rem 0;
        }
        .tkjilu p:first-child{
            width: 12rem;
            height: 4rem;
        }
        .tkjilu p:last-child{
            line-height: 3rem;
            font-size: 1rem;
        }
        .tkjilu .leftSpan{
            display: inline-block;
            font-weight: bold;
            color: black;
        }
        .tkjilu .leftSpan span{
            font-weight: normal;
        }
        .tkjilu img{
            display: inline-block;
            width: 2.5rem;
            height: 2.5rem;
            margin-bottom: 2rem;
            margin-right: .2rem;
        }
        .tabContent{
            width: 100%;
        }
        .content{
            float: left;
            width: 97%;
            display: none;
        }
        .tabContent .checkedBox{
            display: block;
        }
        .red{
            color: RGB(252,8,8)!important;
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
<article class="views docBody">
    <section class="view">
        <jsp:include page="/WEB-INF/views/app/header-public.jsp"><jsp:param name="title" value="提现记录"></jsp:param></jsp:include>
        <div class="pages navbar-fixed ">
            <div class="page">
                <div class="page-content" style="background: RGB(237,237,237);">
                    <div class="tabBar">
                        <ul>
                            <li class="checked">审核中</li>
                            <li >提现成功</li>
                            <li >提现失败</li>
                        </ul>
                    </div>
                    <div class="tabContent">
                    	<form id="tokenForm" action=""	method="post">
                			<input type="hidden" id="recordId" name="id" />
                			<input type="hidden" name="_header" value="${memberToken}"/>
                			<input type="hidden" name="_type" value="h5"/>
                		</form>
                    <div class="tkjilubg data-list content  checkedBox"><%--审核中--%>
	                    <c:if test="${!empty listAuditing}">
	                    	<c:forEach items="${listAuditing}" var="record">
	                    		<div class="tkjilu goDetails" data-href="${pageContext.request.contextPath}/app/wyjt/common/withdrawDetail.do?id=${record.id}">
		                    		<img src="${mbStatic}/assets/images/debt/tx@3x.png">
		                            <p style="width: 12rem">${fns:decimalToStr(record.amount,2)}元 <span>${fns:getDateStr(record.createTime,"yyyy-MM-dd HH:mm:ss")}</span></p>
		                            <p class="red">审核中</p>
		                        </div>
	                    	</c:forEach>
	                    </c:if>
	                    <c:if test="${empty listAuditing}">
	                    	<div class="tkjilu">
	                            <p class="red" style="text-align: center;">暂无数据</p>
	                        </div>
	                    </c:if>                   
                    </div>
                    <div class="tkjilubg data-list content "><%--成功--%>
	                      <c:if test="${!empty listMadeMoney}">
	                    	<c:forEach items="${listMadeMoney}" var="record">
	                    		<%-- <div class="tkjilu goDetails" data-href="withdrawDetail.do?id=${record.id}"> --%>
	                    		<div class="tkjilu goDetails" data-href="${pageContext.request.contextPath}/app/wyjt/common/withdrawDetail.do?id=${record.id}">
	                    		 <img src="${mbStatic}/assets/images/debt/tx@3x.png">
	                            <p style="width: 12rem">${fns:decimalToStr(record.amount,2)}元 <span>${fns:getDateStr(record.updateTime,"yyyy-MM-dd HH:mm:ss")}</span></p>
	                            <p class="red">提现成功</p>
	                        </div>
	                    	</c:forEach>
	                    </c:if>
	                    <c:if test="${empty listMadeMoney}">
	                    	<div class="tkjilu">
	                            <p class="red" style="text-align: center;">暂无数据</p>
	                        </div>
	                    </c:if>
                    </div>
                    <div class="tkjilubg data-list content"><%--失败--%>
                  		
                       <c:if test="${!empty listFail}">
	                    	<c:forEach items="${listFail}" var="record">
	                    		<div class="tkjilu goDetails" data-href="${pageContext.request.contextPath}/app/wyjt/common/withdrawDetail.do?id=${record.id}">
	                    		<img src="${mbStatic}/assets/images/debt/tx@3x.png">
	                            <p style="width: 12rem">${fns:decimalToStr(record.amount,2)}元 <span>${fns:getDateStr(record.updateTime,"yyyy-MM-dd HH:mm:ss")}</span></p>
	                            <p class="red">提现失败</p>
	                        </div>
	                    	</c:forEach>
	                    </c:if>
	                    <c:if test="${empty listFail}">
	                    	<div class="tkjilu">
	                            <p class="red" style="text-align: center;">暂无数据</p>
	                        </div>
	                    </c:if>
                    </div>
                        </div>
                </div>
            </div>
        </div>
    </section>
</article>
<section class="panel" id="infoPanel" style="width: 100%">
    <div class="page-view theme-red">
        <div class="navbar">
            <div class="navbar-inner">
                <div class="left"><a class="link close-popup" href="javascript:void 0" target="_self"><i class="icon icon-back"></i><span>返回</span></a></div>
                <div class="center">提现记录详情</div>
                <div class="right"><a href="javascript:" class="link icon-only"></a></div>
            </div>
        </div>
        <div class="page navbar-fixed">
            <div class="page-content">
                <div class="page-center-middle" style="height: 100%">
                    <div class="preloader"></div>
                </div>
            </div>
        </div>
    </div>
</section>
<jsp:include page="/WEB-INF/views/app/footer.jsp" flush="true"></jsp:include>
<script type="text/javascript">
    define('withdraw', ['zepto', 'pop','panel','jumpApp'], function (require, exports, module) {
        var $ = require('zepto'), pop = require('pop'),call = require('jumpApp'),panel=require('panel');
        var ev = $.support.touch ? 'tap' : 'click',$lis=$('ul li'),$content=$('.tabContent .content');
        $lis[ev](function () {
            var _this=$(this),_index=_this.index();
            _this.siblings().removeClass('checked'),_this.addClass('checked');
            $content.removeClass('checkedBox'),$content.eq(_index).addClass('checkedBox');
        });
      
         $('.goDetails').live(ev, function () {
            var src = $(this).attr('data-href');
            var panel = $('#infoPanel').Panel({
                direction: 'right',
                src: src,
                callback: function () {
                    if (!this.isOpen) {
                        $(this.el).find('.page-content').empty()
                    }
                }
            });
            panel.open();
            return false;
        }); 
    });
    seajs.use(['withdraw', 'jumpApp']);
</script>
</body>
</html>
