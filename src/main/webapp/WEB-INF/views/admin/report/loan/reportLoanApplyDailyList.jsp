<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>申请统计管理</title>
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
		<li class="active"><a style="cursor:pointer;" href="${admin}/reportLoanApplyDaily/?type=10">日借条统计列表</a></li>
        <li class="active"><a style="cursor:pointer;" href="${admin}/reportLoanApplyDaily/?type=7">月借条统计列表</a></li>
		<li class="active"><a style="cursor:pointer;" href="${admin}/reportLoanApplyDaily/?type=4">年借条统计列表</a></li>
	</ul>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th style="text-align:center">日期</th>
				<th style="text-align:center">借款申请笔数</th>
				<th style="text-align:center">借款申请金额</th>
				<th style="text-align:center">借款达成笔数</th>
				<th style="text-align:center">借款达成金额</th>
				<th style="text-align:center">放款申请笔数</th>
				<th style="text-align:center">放款申请金额</th>
				<th style="text-align:center">放款达成笔数</th>
				<th style="text-align:center">放款达成金额</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="reportLoanApplyDaily">
			<tr>
				<td style="text-align:center">
					${reportLoanApplyDaily.date}
				</td>
				<td style="text-align:center">
					${reportLoanApplyDaily.borrowCount}
				</td>
				<td style="text-align:center">
					${reportLoanApplyDaily.borrowAmount}
				</td>
				<td style="text-align:center">
					${reportLoanApplyDaily.borrowSuccessCount}
				</td>
				<td style="text-align:center">
					${reportLoanApplyDaily.borrowSuccessAmount}
				</td>
				<td style="text-align:center">
					${reportLoanApplyDaily.loanCount}
				</td>
				<td style="text-align:center">
					${reportLoanApplyDaily.loanAmount}
				</td>
				<td style="text-align:center">
					${reportLoanApplyDaily.loanSuccessCount}
				</td>
				<td style="text-align:center">
					${reportLoanApplyDaily.loanSuccessAmount}
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
	<form:form id="searchForm" modelAttribute="reportLoanApplyDaily" action="${ctx}/reportLoanApplyDaily/?type=10" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
	</form:form>
</body>
</html>