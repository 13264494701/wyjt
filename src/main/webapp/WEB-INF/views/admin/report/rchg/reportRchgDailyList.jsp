<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>充值统计管理</title>
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
		<li class="active"><a href="${admin}/reportRchgDaily/?type=10">日充值统计列表</a></li>
        <li class="active"><a href="${admin}/reportRchgDaily/?type=7">月充值统计列表</a></li>
		<li class="active"><a href="${admin}/reportRchgDaily/?type=4">年充值统计列表</a></li>
	</ul>
	<form:form id="searchForm" modelAttribute="reportRchgDaily" action="${admin}/reportRchgDaily/?type=10" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th style="text-align:center">日期</th>
				<th style="text-align:center">线上充值笔数(富友)</th>
				<th style="text-align:center">线上充值金额(富友)</th>
				<th style="text-align:center">线下充值笔数</th>
				<th style="text-align:center">线下充值金额</th>
				<th style="text-align:center">创建时间</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="reportRchgDaily">
			<tr>
				<td style="text-align:center">
					${reportRchgDaily.date}
				</td>
				<td style="text-align:center">
					${reportRchgDaily.onlineQuantity}
				</td>
				<td style="text-align:center">
					${reportRchgDaily.onlineAmount}
				</td>
				<td style="text-align:center">
					${reportRchgDaily.offlineQuantity}
				</td>
				<td style="text-align:center">
					${reportRchgDaily.offlineAmount}
				</td>
				<td style="text-align:center">
					<fmt:formatDate value="${reportRchgDaily.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>