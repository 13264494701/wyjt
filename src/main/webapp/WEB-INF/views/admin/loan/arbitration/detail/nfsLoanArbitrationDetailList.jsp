<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>仲裁明细管理</title>
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
		<li class="active"><a href="javascript:;">仲裁明细列表</a></li>
	</ul>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th style="text-align:center">借条编号</th>
				<th style="text-align:center">仲裁类型</th>
				<th style="text-align:center">仲裁状态</th>
				<th style="text-align:center">任务</th>
				<th style="text-align:center">仲裁备注</th>
				<th style="text-align:center">创建时间</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="nfsLoanArbitrationDetail">
			<tr>
				<td style="text-align:center">
					${nfsLoanArbitrationDetail.arbitrationId}
				</td>
				<td style="text-align:center">
					${fns:getDictLabel(nfsLoanArbitrationDetail.type, 'arbitrationDetailType', '')}
				</td>
				<td style="text-align:center">
					${fns:getDictLabel(nfsLoanArbitrationDetail.status, 'arbitrationDetailStatus', '')}
				</td>
				<td style="text-align:center">
					${fns:getDictLabel(nfsLoanArbitrationDetail.task, 'arbitrationDetailTask', '')}
				</td>
				<td style="text-align:center">
					${nfsLoanArbitrationDetail.rmk}
				</td>
				<td style="text-align:center">
					<fmt:formatDate value="${nfsLoanArbitrationDetail.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>