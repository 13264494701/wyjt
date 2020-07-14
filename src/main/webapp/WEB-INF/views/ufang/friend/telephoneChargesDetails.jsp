<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>话费记录</title>
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
		<li class="active">话费记录</li>
	</ul>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<td style="text-align:center;font-weight:bold">月份</td>
				<td style="text-align:center;font-weight:bold">消费金额(元)</td>
			</tr>
		</thead>
			
		<tbody>
			<c:forEach items="${listMapFromJson2 }" var="list">
				<tr>
					<td style="text-align:center">${list.stri}</td>
					<td style="text-align:center">${list.moneyNew}</td>
				</tr>
			</c:forEach>
			
			
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>