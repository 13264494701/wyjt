<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%@include file="/WEB-INF/views/app/constant.jsp" %><%@ page contentType="text/html;charset=UTF-8" language="java"%>

<!DOCTYPE html>
<html xmlns=http://www.w3.org/1999/xhtml>
<head>
<title>社保缴费详情</title>
<%@include file="/WEB-INF/views/app/meta-flex.jsp" %>
    <meta name="keywords" content=""/>
    <meta name="description" content=""/>
    <link rel="stylesheet" href="${mbStatic}/assets/css/public.css?v=<%=version%>" type="text/css">
    <link rel="stylesheet" href="${mbStatic}/assets/css/user.css?v=<%=version%>" type="text/css">
    <script src="${mbStatic}/assets/scripts/base/sea.js?v=<%=version%>21"></script>
    <script src="${mbStatic}/assets/scripts/base/flexible.js?v=<%=version%>"></script>
<style>
.tabTitle span {
	width: 5.32rem;
	height: 2rem;
	line-height: 2rem;
	text-align: center;
	font-size: 1.1rem;
	display: inline-block;
}

.tabChecked {
	color: red;
	font-size: 1.3rem;
	font-weight: bold;
	border-bottom: .2rem red solid;
}

.box3 {
	display: none;
}

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
	width: 5.3rem;
}

.box3 p span:nth-child(2) {
	width: 5.3rem;
	border-left: 1px solid RGB(223, 223, 223);
	border-right: 1px solid RGB(223, 223, 223);
}

.box3 p span:nth-child(3) {
	width: 5.3rem;
	border-right: 1px solid RGB(223, 223, 223);
}

.box3 p span:nth-child(4) {
	width: 5.2rem;
	border-right: 1px solid RGB(223, 223, 223);
}

.box3 p span:nth-child(5) {
	width: 5.25rem;
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
						<p class="tabTitle">
							<span class="tabChecked">养老</span><span>医疗</span><span>事业</span><span>工伤</span><span>生育</span>
						</p>
						<div>
							<div class="box3" style="display: block;">
								<p>
									<span>缴纳时间</span><span>缴纳基数</span><span>单位缴存</span><span>个人缴存</span><span>缴费状态</span>
								</p>
								 <c:forEach items = "${listMapFromJsonYaL}" var = "lmfj">
								<p class="title">
								<span>${lmfj.month }</span><span>${lmfj.jishu }</span><span>${lmfj.com }</span><span>${lmfj.ge1}</span><span>${lmfj.type }</span>
								</p>
                            </c:forEach>
							</div>
							<div class="box3">
								<p>
									<span>缴纳时间</span><span>缴纳基数</span><span>单位缴存</span><span>个人缴存</span><span>缴费状态</span>
								 <c:forEach items = "${listMapFromJsonYiL}" var = "lmfj2">
								<p class="title">
								<span>${lmfj2.month }</span><span>${lmfj2.jishu }</span><span>${lmfj2.com }</span><span>${lmfj2.ge1}</span><span>${lmfj2.type }</span>
								</p>
                            </c:forEach>
							</div>
							<div class="box3">
								<p>
									<span>缴纳时间</span><span>缴纳基数</span><span>单位缴存</span><span>个人缴存</span><span>缴费状态</span>
								</p>
								 <c:forEach items = "${listMapFromJsonGoS}" var = "lmfj5">
								<p class="title">
								<span>${lmfj5.month }</span><span>${lmfj5.jishu }</span><span>${lmfj5.com }</span><span>${lmfj5.ge1}</span><span>${lmfj5.type }</span>
								</p>
                            </c:forEach>
							</div>
							<div class="box3">
								<p>
									<span>缴纳时间</span><span>缴纳基数</span><span>单位缴存</span><span>个人缴存</span><span>缴费状态</span>
								</p>
								 <c:forEach items = "${listMapFromJsonSiY}" var = "lmfj4">
								<p class="title">
								<span>${lmfj4.month }</span><span>${lmfj4.jishu }</span><span>${lmfj4.com }</span><span>${lmfj4.ge1}</span><span>${lmfj4.type }</span>
								</p>
                            </c:forEach>
							</div>
							<div class="box3">
								<p>
									<span>缴纳时间</span><span>缴纳基数</span><span>单位缴存</span><span>个人缴存</span><span>缴费状态</span>
								</p>
								 <c:forEach items = "${listMapFromJsonSeY}" var = "lmfj3">
								<p class="title">
								<span>${lmfj3.month }</span><span>${lmfj3.jishu }</span><span>${lmfj3.com }</span><span>${lmfj3.ge1}</span><span>${lmfj3.type }</span>
								</p>
                            </c:forEach>
							</div>
						</div>
					</div>
				</div>
			</div>
		</section>
	</article>
</body>
<script>
	seajs.use(['zepto'],function ($,jumpApp) {
		var ev = $.support.touch ? 'tap' : 'click',$tabBox=$('.tabTitle span'),$Boxs=$('.box3');
		$tabBox[ev](function () {
			var _this=$(this),_index=_this.index(),$sibTab=_this.siblings();
			_this.hasClass('tabChecked')?'':(_this.addClass('tabChecked'),$sibTab.removeClass('tabChecked'),$Boxs.siblings().hide(),$Boxs.eq(_index).show());
		});
	});
</script>
</html>
