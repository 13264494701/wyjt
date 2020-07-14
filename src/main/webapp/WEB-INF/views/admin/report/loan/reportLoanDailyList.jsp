<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>借条统计管理</title>
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
		<li class="active"><a href="${admin}/reportLoanDaily/?type=10">日借条统计列表</a></li>
        <li class="active"><a href="${admin}/reportLoanDaily/?type=7">月借条统计列表</a></li>
		<li class="active"><a href="${admin}/reportLoanDaily/?type=4">年借条统计列表</a></li>
	</ul>
	<form:form id="searchForm" modelAttribute="reportLoanDaily" action="${admin}/reportLoanDaily/?type=10" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th style="text-align:center">日期</th>
				<th style="text-align:center">交易渠道</th>
				<th style="text-align:center">借条数量</th>
				<th style="text-align:center">借条金额</th>
				<th style="text-align:center">借条手续费</th>
				<th style="text-align:center">创建时间</th>				
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="reportLoanDaily">
			<tr>
				<td style="text-align:center">
					${reportLoanDaily.date}
				</td>
				<td style="text-align:center">
					${fns:getDictLabel(reportLoanDaily.trxType, 'loanTrxType', '')}
				</td>
				<td style="text-align:center">
					${reportLoanDaily.loanQuantity}
				</td>
				<td style="text-align:center">
					${fns:decimalToStr(reportLoanDaily.loanAmount,2)}
				</td>
				<td style="text-align:center">
					${fns:decimalToStr(reportLoanDaily.loanFee,2)}
				</td>
				<td style="text-align:center">
					<fmt:formatDate value="${reportLoanDaily.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>