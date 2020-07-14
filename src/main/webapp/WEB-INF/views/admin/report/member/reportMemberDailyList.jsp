<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>用户统计管理</title>
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
		<li class="active"><a href="${ctx}/reportMemberDaily?type=10">日用户统计列表</a></li>
		<li class="active"><a href="${ctx}/reportMemberDaily?type=7">月用户统计列表</a></li>
		<li class="active"><a href="${ctx}/reportMemberDaily?type=4">年用户统计列表</a></li>
	</ul>
    <form:form id="searchForm" modelAttribute="reportMemberDaily" action="${ctx}/reportMemberDaily?type=10" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>

	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th style="text-align:center">日期</th>
				<th style="text-align:center">新增会员</th>
				<th style="text-align:center">总会员数</th>
<%--				<th style="text-align:center">活跃会员</th>--%>

			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="reportMemberDaily">
			<tr>
				<td style="text-align:center">
					${reportMemberDaily.date}
				</td>
				<td style="text-align:center">
					${reportMemberDaily.newMembers}
				</td>
				<td style="text-align:center">
					${reportMemberDaily.totalMembers}
				</td>
<%--				<td style="text-align:center">
					${reportMemberDaily.activeMembers}
				</td>--%>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>