<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>借条金额统计管理</title>
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
		<li class="active"><a href="${admin}/reportLoanAmtDaily/?type=10">日借条金额统计列表</a></li>
        <li class="active"><a href="${admin}/reportLoanAmtDaily/?type=7">月借条金额统计列表</a></li>
		<li class="active"><a href="${admin}/reportLoanAmtDaily/?type=4">年借条金额统计列表</a></li>
	</ul>
	<form:form id="searchForm" modelAttribute="reportLoanAmtDaily" action="${admin}/reportLoanAmtDaily/?type=10" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th style="text-align:center">日期</th>
				<th style="text-align:center">借款笔数</th>
				<th style="text-align:center">放款人借出</th>
				<th style="text-align:center">借款人借入</th>
				<th style="text-align:center">还款笔数</th>
				<th style="text-align:center">借款人还款</th>
				<th style="text-align:center">放款人收款</th>
				<th style="text-align:center">创建时间</th>				
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="reportLoanAmtDaily">
			<tr>
				<td style="text-align:center">
					${reportLoanAmtDaily.date}
				</td>
				<td style="text-align:center">
					${reportLoanAmtDaily.loanQuantity}
				</td>
				<td style="text-align:center">
					${fns:decimalToStr(reportLoanAmtDaily.loanerLend,2)}
				</td>
				<td style="text-align:center">
					${fns:decimalToStr(reportLoanAmtDaily.loaneeBorrow,2)}
				</td>
				
				<td style="text-align:center">
					${reportLoanAmtDaily.repayQuantity}
				</td>
				<td style="text-align:center">
					${fns:decimalToStr(reportLoanAmtDaily.loaneeRepay,2)}
				</td>
				<td style="text-align:center">
					${fns:decimalToStr(reportLoanAmtDaily.loanerReceive,2)}
				</td>
				<td style="text-align:center">
					<fmt:formatDate value="${reportLoanAmtDaily.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>