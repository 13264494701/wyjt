<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>通话记录</title>
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
		<li class="active">通话记录</li>
	</ul>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<td style="text-align:center;font-weight:bold">手机号</td>
				<td style="text-align:center;font-weight:bold">通话时间</td>
				<td style="text-align:center;font-weight:bold">时长</td>
				<td style="text-align:center;font-weight:bold">呼叫类型</td>
			</tr>
		</thead>
			
		<tbody>
			<c:forEach items="${list }" var="list">
				<tr>
					<td style="text-align:center">${list.moblie}</td>
					<td style="text-align:center">${list.time}</td>
					<td style="text-align:center">${list.count}</td>
					<td style="text-align:center">${list.type}</td>			
				</tr>
			</c:forEach>
			
			
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>