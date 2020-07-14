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
		<li class="active"><a href="${ufang}/reportUfangLoanDaily/">借条统计列表</a></li>
	</ul>
	<form:form id="searchForm" modelAttribute="reportUfangLoanDaily" action="${ufang}/reportUfangLoanDaily/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label style="text-align:right;width:150px">日期：</label>
				<input name="date" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${reportUfangLoanDaily.beginTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:true});"/>
			-
				<input name="date" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${reportUfangLoanDaily.endTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:true});"/>
			</li>
			<li class="btns"><label style="text-align:right;width:50px"></label><input id="btnSubmit" class="btn btn-primary " style="width:80px;" type="submit" value="查   询"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th style="text-align:center">日期</th>
				<th style="text-align:center">结清个数</th>
				<!-- <th style="text-align:center">还款金额</th> -->
				<th style="text-align:center">借出个数</th>
				<th style="text-align:center">借出金额</th>
				<th style="text-align:center">逾期未还总数</th>
				<th style="text-align:center">逾期未还金额</th>
				<th style="text-align:center">待收总个数</th>
				<th style="text-align:center">待收总金额</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="reportUfangLoanDaily">
			<tr>
				<td style="text-align:center">
					<fmt:formatDate value="${reportUfangLoanDaily.date}" pattern="yyyy-MM-dd"/>
				</a></td>
				<td style="text-align:center">
					${reportUfangLoanDaily.completedLoanQuantity}
				</td>
				<%-- <td style="text-align:center">
					${fns:decimalToStr(reportUfangLoanDaily.repayedLoanAmount,2)}
				</td> --%>
				<td style="text-align:center">
					${reportUfangLoanDaily.createdLoanQuantity}
				</td>
				<td style="text-align:center">
					${fns:decimalToStr(reportUfangLoanDaily.createdLoanAmount,2)}
				</td>
				<td style="text-align:center">
					${reportUfangLoanDaily.overdueLoanQuantity}
				</td>
				<td style="text-align:center">
					${fns:decimalToStr(reportUfangLoanDaily.overdueLoanAmount,2)}
				</td>
				<td style="text-align:center">
					${reportUfangLoanDaily.lendingLoanQuantity}
				</td>
				<td style="text-align:center">
					${fns:decimalToStr(reportUfangLoanDaily.lendingLoanAmount,2)}
				</td>
				<td  style="text-align:center">
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>