<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>提现统计管理</title>
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
		<li class="active"><a href="${admin}/reportWdrlDaily/?type=10">日提现统计列表</a></li>
        <li class="active"><a href="${admin}/reportWdrlDaily/?type=7">月提现统计列表</a></li>
		<li class="active"><a href="${admin}/reportWdrlDaily/?type=4">年提现统计列表</a></li>
	</ul>
	<form:form id="searchForm" modelAttribute="reportWdrlDaily" action="${admin}/reportWdrlDaily/?type=10" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th style="text-align:center">日期</th>
				<th style="text-align:center">线上提现笔数(连连)</th>
				<th style="text-align:center">线上提现金额(连连)</th>
				<th style="text-align:center">线下减款笔数</th>
				<th style="text-align:center">线下减款金额</th>
				<th style="text-align:center">创建时间</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="reportWdrlDaily">
			<tr>
				<td style="text-align:center">
					${reportWdrlDaily.date}
				</td>
				<td style="text-align:center">
					${reportWdrlDaily.onlineQuantity}
				</td>
				<td style="text-align:center">
					${reportWdrlDaily.onlineAmount}
				</td>
				<td style="text-align:center">
					${reportWdrlDaily.offlineQuantity}
				</td>
				<td style="text-align:center">
					${reportWdrlDaily.offlineAmount}
				</td>
				<td style="text-align:center">
					<fmt:formatDate value="${reportWdrlDaily.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>