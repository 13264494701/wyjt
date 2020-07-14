<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>批次任务管理</title>
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
		<li class="active"><a href="${ctx}/sysDataTask/?data.id=${dataId}">批次任务列表</a></li>		
	</ul>
	<form:form id="searchForm" modelAttribute="sysDataTask" action="${ctx}/sysDataTask/?data.id=${dataId}" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th style="text-align:center">任务名称</th>
				<th style="text-align:center">起始ID</th>
				<th style="text-align:center">结束ID</th>
				<th style="text-align:center">quantity</th>
				<th style="text-align:center">任务状态</th>
				<th style="text-align:center">是否启动</th>
				<th style="text-align:center">创建时间</th>
				<th style="text-align:center">更新时间</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="sysDataTask">
			<tr>
				<td style="text-align:center"><a href="${ctx}/sysDataTask/query?id=${sysDataTask.id}">
					${sysDataTask.data.name}
				</a></td>
				<td style="text-align:center">
					${sysDataTask.startId}
				</td>
				<td style="text-align:center">
					${sysDataTask.endId}
				</td>
				<td style="text-align:center">
					${sysDataTask.quantity}
				</td>
				<td style="text-align:center">
					${sysDataTask.status}
				</td>
				<td style="text-align:center">
					${sysDataTask.isOn}
				</td>
				<td style="text-align:center">
					<fmt:formatDate value="${sysDataTask.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td style="text-align:center">
					<fmt:formatDate value="${sysDataTask.updateTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>