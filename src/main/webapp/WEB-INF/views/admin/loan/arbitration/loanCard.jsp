<%@page contentType="text/html;charset=UTF-8"%>
<%@include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>借条仲裁案件证据</title>
	<meta name="decorator" content="default"/>
	<script src="${ctxStatic}/jquery/jquery.lazyload.js" type="text/javascript"></script>
	<script src="${ctxStatic}/artdialog/js/dialog-plus.js"></script>
    <link href="${ctxStatic}/artdialog/css/dialog.css" rel="stylesheet" />
    <script type="text/javascript">
	    $(document).ready(function() {
			
		});
	    function exportSearch(){
	    	var id = $("#id").val();
	    	window.location.href="${ctx}/loanArbitration/loanCard?id="+id;
	    }
		function exportPdf(){
			var id = $("#id").val();
			window.location.href="${admin}/loanArbitration/exportCardPdf?id="+id;
		}
</script>
</head>
<body>
	<table>
		<tr>
			<td>
				<input type="text" id = "id" name = "id" value="${nfsLoanRecord.id }"/>
				<input type="button" onclick = "exportSearch()" value="查询"/>
				<input type="button" onclick = "exportPdf()" value="转出pdf"/>
			</td>
		</tr>
	</table>
	<h2>借款单详情</h2>
		<br/>
		<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<tbody>
			<tr>
				<td style="text-align:center" width="50%">
					借款人：${nfsLoanRecord.loanee.name}
				</td>
				<td style="text-align:center" width="50%">
					借款单id：${nfsLoanRecord.id}
				</td>
			</tr>
			<tr>
				<td style="text-align:center" width="50%">
					放款人：${nfsLoanRecord.loaner.name}
				</td>
				<td style="text-align:center" width="50%">
					借款金额：${nfsLoanRecord.amount }
				</td>
			</tr>
			<tr>
				<td style="text-align:center" width="50%">
					借款利息：${nfsLoanRecord.interest }
				</td>
				<td style="text-align:center" width="50%">
					待还金额：${nfsLoanRecord.dueRepayAmount }
				</td>
			</tr>
			<tr>
				<td style="text-align:center" width="50%">
					待还利息：${nfsLoanRecord.overdueInterest }
				</td>
				<td style="text-align:center" width="50%">
					借款时间：<fmt:formatDate value="${nfsLoanRecord.createTime }" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
			</tr>
			<tr>
				<td style="text-align:center" width="50%">
					还款时间：<fmt:formatDate value="${nfsLoanRecord.dueRepayDate }" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td style="text-align:center" width="50%">
					借款时长：${nfsLoanRecord.term}天
				</td>
			</tr>
			<tr>
				<td style="text-align:center" width="50%">
					还款方式：${fns:getDictLabel(nfsLoanRecord.repayType, 'repayType', '')}
				</td>
				<td style="text-align:center" width="50%">
					合同编号：${nfsLoanRecord.loanNo}
				</td>
			</tr>
			<tr>
				<td style="text-align:center" width="50%">
					借款单号：${nfsLoanRecord.loanApplyDetail.id }
				</td>
			</tr>
		</tbody>
	</table>
	<br/>
</body>
</html>