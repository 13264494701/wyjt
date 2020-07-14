<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%@include file="/WEB-INF/views/app/constant.jsp" %>
<!DOCTYPE html>
<html xmlns=http://www.w3.org/1999/xhtml>
<head>
<title>公积金缴费详情</title>
 <%@include file="/WEB-INF/views/app/meta-flex.jsp" %>
    <meta name="keywords" content=""/>
    <meta name="description" content=""/>
    <link rel="stylesheet" href="${mbStatic}/assets/css/public.css?v=<%=version%>" type="text/css">
    <link rel="stylesheet" href="${mbStatic}/assets/css/user.css?v=<%=version%>" type="text/css">
    <script src="${mbStatic}/assets/scripts/base/sea.js?v=<%=version%>21"></script>
    <script src="${mbStatic}/assets/scripts/base/flexible.js?v=<%=version%>"></script>
<style>
.box3 p span {
	height: 2.5rem;
	line-height: 2.5rem;
	text-align: center;
	display: inline-block;
	width: 10rem;
	border-bottom: 1px solid RGB(223, 223, 223);
	color: RGB(109, 109, 109);
	font-size: 1rem;
}

.box3 p span:nth-child(1) {
	width: 5.2rem;
}

.box3 p span:nth-child(2) {
	width: 5.5rem;
	border-left: 1px solid RGB(223, 223, 223);
	border-right: 1px solid RGB(223, 223, 223);
}

.box3 p span:nth-child(3) {
	width: 5.5rem;
	border-right: 1px solid RGB(223, 223, 223);
}

.box3 p span:nth-child(4) {
	width: 5.5rem;
	border-right: 1px solid RGB(223, 223, 223);
}

.box3 p span:nth-child(5) {
	width: 4.67rem;
}

.box3 .title span {
	color: black;
	font-size: .9rem;
}
</style>
</head>
<body>
	<article class="views">
		<section class="view">
			<div class="pages navbar-fixed toolbar-fixed">
				<div class="page">
					<div class="page-content user-main withdraw-main"
						style="background: white;padding-top: 0;">
						<div class="box3">
							<p>
								<span>到账日期</span><span>业务类型</span><span>缴存额(元)</span><span>取出额(元)</span><span>余额(元)</span>
							</p>
                            <c:forEach items = "${listMapFromJson2 }" var = "lmfj2">
                            <p class="title">
								<span>${lmfj2.time }</span><span>${lmfj2.desc }</span><span>${lmfj2.incom }</span><span>${lmfj2.outcom }</span><span>${lmfj2.yue }</span>
							</p>
                            </c:forEach>
						</div>
					</div>
				</div>
			</div>
		</section>
	</article>
</body>
<script>
	seajs.use(['zepto'],function ($,jumpApp) {
		var ev = $.support.touch ? 'tap' : 'click',$i=$('.modelTitle i');
		$i[ev](function () {
			var _this=$(this),$nextBox=_this.closest('.modelTitle').next();
			_this.hasClass('close')?(_this.removeClass('close'),$nextBox.show()):(_this.addClass('close'),$nextBox.hide());
		});
	});
</script>
</html>
