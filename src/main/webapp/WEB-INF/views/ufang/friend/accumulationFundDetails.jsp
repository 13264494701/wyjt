<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>公积金详情</title>
	<meta name="decorator" content="default"/>
    <script src="${ctxStatic}/artdialog/js/dialog-plus.js"></script>
    <link href="${ctxStatic}/artdialog/css/dialog.css" rel="stylesheet" />
	<script type="text/javascript">
		$(document).ready(function() {
			
		});
		function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#searchForm").submit();
        	return false;
        }
		
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active">公积金记录</li>
	</ul>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<td style="text-align:center;font-weight:bold">到账日期</td>
				<td style="text-align:center;font-weight:bold">业务类型</td>
				<td style="text-align:center;font-weight:bold">缴存额(元)</td>
				<td style="text-align:center;font-weight:bold">取出额(元)</td>
				<td style="text-align:center;font-weight:bold">余额(元)</td>
			</tr>
		</thead>
			
		<tbody>
			<c:forEach items="${listMapFromJson2 }" var="list">
				<tr>
					<td style="text-align:center">${list.time}</td>
					<td style="text-align:center">${list.desc}</td>
					<td style="text-align:center">${list.incom}</td>
					<td style="text-align:center">${list.outcom}</td>
					<td style="text-align:center">${list.yue}</td>
				</tr>
			</c:forEach>
			
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>