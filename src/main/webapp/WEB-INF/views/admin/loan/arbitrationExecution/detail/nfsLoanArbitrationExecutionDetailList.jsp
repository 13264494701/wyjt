<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>强执明细管理</title>
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
		<li class="active"><a href="javascript:;">强执明细列表</a></li>
	</ul>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th style="text-align:center">强执编号</th>
				<th style="text-align:center">强执类型</th>
				<th style="text-align:center">强执状态</th>
				<th style="text-align:center">强执备注</th>
				<th style="text-align:center">创建时间</th>
				<th style="text-align:center">更新时间</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="arbitrationExecutionDetail">
			<tr>
				<td style="text-align:center">
					${arbitrationExecutionDetail.executionId}
				</td>
				<td style="text-align:center">
					${fns:getDictLabel(arbitrationExecutionDetail.type, 'arbitrationExecutionDetailType', '')}
				</td>
				<td style="text-align:center">
					${fns:getDictLabel(arbitrationExecutionDetail.status, 'arbitrationExecutionDetailStatus', '')}
				</td>
				<td style="text-align:center">
					${arbitrationExecutionDetail.rmk}
				</td>
				<td style="text-align:center">
					<fmt:formatDate value="${arbitrationExecutionDetail.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td style="text-align:center">
					<fmt:formatDate value="${arbitrationExecutionDetail.updateTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>