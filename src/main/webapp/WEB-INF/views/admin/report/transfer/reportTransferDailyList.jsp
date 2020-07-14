<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>转账统计管理</title>
	<meta name="decorator" content="default"/>
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
		<li class="active"><a href="${admin}/reportTransferDaily/?type=10">日转账统计列表</a></li>
        <li class="active"><a href="${admin}/reportTransferDaily/?type=7">月转账统计列表</a></li>
		<li class="active"><a href="${admin}/reportTransferDaily/?type=4">年转账统计列表</a></li>
	</ul>
	<form:form id="searchForm" modelAttribute="reportTransferDaily" action="${admin}/reportTransferDaily/?type=10" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th style="text-align:center">日期</th>
				<th style="text-align:center">转账笔数</th>
				<th style="text-align:center">转账金额</th>
				<th style="text-align:center">创建时间</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="reportTransferDaily">
			<tr>
				<td style="text-align:center">
					${reportTransferDaily.date}
				</td>
				<td style="text-align:center">
					${reportTransferDaily.quantity}
				</td>
				<td style="text-align:center">
					${reportTransferDaily.amount}
				</td>
				<td style="text-align:center">
					<fmt:formatDate value="${reportTransferDaily.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>