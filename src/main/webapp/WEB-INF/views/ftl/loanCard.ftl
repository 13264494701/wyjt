[#escape x as x?html]
<!DOCTYPE html>
<html lang="zh-CN">
<head>
	<title>借条仲裁案件证据</title>
	<meta name="decorator" content="default"/>
	<script src="${ctxStatic}/jquery/jquery.lazyload.js" type="text/javascript"></script>
	<script src="${ctxStatic}/artdialog/js/dialog-plus.js"></script>
    <link href="${ctxStatic}/artdialog/css/dialog.css" rel="stylesheet" />
    <script type="text/javascript">
	    $(document).ready(function() {
			
		});
</script>
</head>
<body>
	<h2>借款单详情</h2>
		<br/>
		<table width="100%" class="tables" border="1" cellpadding="0" cellspacing="0">
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
					借款时间：${nfsLoanRecord.createTime?string("yyyy-MM-dd HH:mm:ss") }
				</td>
			</tr>
			<tr>
				<td style="text-align:center" width="50%">
					还款时间：${nfsLoanRecord.dueRepayDate?string("yyyy-MM-dd") }
				</td>
				<td style="text-align:center" width="50%">
					借款时长：${nfsLoanRecord.term}天
				</td>
			</tr>
			<tr>
				<td style="text-align:center" width="50%">
					还款方式：全额
				</td>
				<td style="text-align:center" width="50%">
					合同编号：${nfsLoanRecord.loanNo }
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
[/#escape]