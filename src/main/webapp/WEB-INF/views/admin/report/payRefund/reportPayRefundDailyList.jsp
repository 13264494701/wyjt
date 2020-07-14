<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>缴费退费统计管理</title>
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
		<li class="active"><a href="${admin}/reportPayRefundDaily/?type=10">日缴费退费统计列表</a></li>
        <li class="active"><a href="${admin}/reportPayRefundDaily/?type=7">月缴费退费统计列表</a></li>
		<li class="active"><a href="${admin}/reportPayRefundDaily/?type=4">年缴费退费统计列表</a></li>
	</ul>
	<form:form id="searchForm" modelAttribute="reportPayRefundDaily" action="${admin}/reportPayRefundDaily/?type=10" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th style="text-align:center">日期</th>
				<th style="text-align:center">交易类型</th>
				<th style="text-align:center">总金额</th>
				<th style="text-align:center">催收金额</th>
				<th style="text-align:center">仲裁金额</th>
				<th style="text-align:center">强执金额</th>
				<th style="text-align:center">创建时间</th>				
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="reportPayRefundDaily">
			<tr>
				<td style="text-align:center">
					${reportPayRefundDaily.date}
				</td>
				<td style="text-align:center">
					${fns:getDictLabel(reportPayRefundDaily.trxType, 'payRefundType', '')}
				</td>
				<td style="text-align:center">
					${fns:decimalToStr(reportPayRefundDaily.totalAmount,2)}
				</td>
				<td style="text-align:center">
					${fns:decimalToStr(reportPayRefundDaily.clAmount,2)}
				</td>
				<td style="text-align:center">
					${fns:decimalToStr(reportPayRefundDaily.arAmount,2)}
				</td>
				<td style="text-align:center">
					${fns:decimalToStr(reportPayRefundDaily.exAmount,2)}
				</td>
				<td style="text-align:center">
					<fmt:formatDate value="${reportPayRefundDaily.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>