[#escape x as x?html]
<!DOCTYPE html>
<html lang="zh-CN">
<head>
	<title>出借人资金流水</title>
</head>
<body>
	<h2>出借人资金流水</h2>
	<br/>
	<table width="100%" class="tables" border="1" cellpadding="0" cellspacing="0">
			<tr>
				<th style="text-align:center">流水编号</th>
				<th style="text-align:center">流水标题</th>
				<th style="text-align:center">出借人姓名</th>
				<th style="text-align:center">交易金额</th>
				<th style="text-align:center">账户余额</th>	
				<th style="text-align:center">流水备注</th>
				<th style="text-align:center">创建时间</th>
			</tr>
		<tbody>
		    [#list loanerActTrxList as actTrx]
				<tr>
					<td style="text-align:center">
						${actTrx.id }
					</td>
					<td style="text-align:center">
						${actTrx.title }
					</td>
					<td style="text-align:center">
						${loaner.name }
					</td>
					<td style="text-align:center">
						${actTrx.trxAmt }
					</td>
					<td style="text-align:center">
						${actTrx.curBal }
					</td>
					<td style="text-align:center">
						${actTrx.rmk }
					</td>
					<td style="text-align:center">
						${actTrx.createTime?string("yyyy-MM-dd HH:mm:ss") }
					</td>
				</tr>
			[/#list]
		</tbody>
	</table>	
</body>
</html>
[/#escape]