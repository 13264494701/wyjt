<%@page contentType="text/html;charset=UTF-8"%>
<%@include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>仲裁凭证</title>
	<meta name="decorator" content="default"/>
    <script type="text/javascript">
	    $(document).ready(function() {
			
		});
	</script>
	<style type="text/css">
		body{ text-align:center}
		
		div{
			position: absolute;
			top: 50%;
			left: 50%;
			height: 30%;
			width: 50%;
			margin: -15% 0 0 -25%;
		}
	</style>
</head>
<body class="body">
		<c:if test="${empty imageUrlList}">
			<div class="div">
				<label style="font-size: xx-large;">该仲裁记录还没有上传仲裁凭证哦！</label>
			</div>
		</c:if>
		<c:if test="${not empty imageUrlList}">
			<c:forEach items="${imageUrlList}" var="imageUrl">
				<img src="${imageUrl}"/><br/>
			</c:forEach>
		</c:if>
		
</body>
</html>