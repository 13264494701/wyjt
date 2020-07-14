<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>流量统计管理</title>
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
		<li class="active"><a href="${admin}/reportLoaneeDataDaily/?type=10">日流量统计列表</a></li>
        <li class="active"><a href="${admin}/reportLoaneeDataDaily/?type=7">月流量统计列表</a></li>
		<li class="active"><a href="${admin}/reportLoaneeDataDaily/?type=4">年流量统计列表</a></li>
	</ul>
	<form:form id="searchForm" modelAttribute="reportLoaneeDataDaily" action="${admin}/reportLoaneeDataDaily/?type=10" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th style="text-align:center">日期</th>
				<th style="text-align:center">优放贷</th>
				<th style="text-align:center">无忧借条微信</th>
				<th style="text-align:center">创建时间</th>
				
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="reportLoaneeDataDaily">
			<tr>
				<td style="text-align:center">
					${reportLoaneeDataDaily.date}
				</td>
				<td style="text-align:center">
					${reportLoaneeDataDaily.wyjtApp}
				</td>
				<td style="text-align:center">
					${reportLoaneeDataDaily.wyjtWeixin}
				</td>
				<td style="text-align:center">
					<fmt:formatDate value="${reportLoaneeDataDaily.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>