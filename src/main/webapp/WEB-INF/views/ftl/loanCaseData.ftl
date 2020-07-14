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
	<h2>一、债权人</h2>
	<br/>
	<h4>1、基本信息</h4>
	<br/>
	<table width="100%" class="tables" border="1" cellpadding="0" cellspacing="0">
		<tbody>
			<tr>
				<td style="text-align:center" width="50%">
					姓名：${nfsLoanRecord.loaner.name}
				</td>
				<td style="text-align:center" width="50%">
					手机号：${nfsLoanRecord.loaner.username}
				</td>
			</tr>
			<tr>
				<td style="text-align:center" width="50%">
					昵称：${nfsLoanRecord.loaner.nickname }
				</td>
				<td style="text-align:center" width="50%">
					身份证号码：${nfsLoanRecord.loaner.idNo }
				</td>
			</tr>
			<tr>
				<td style="text-align:center" width="50%">
					注册设备Id：${nfsLoanRecord.loaner.id }
				</td>
				<td style="text-align:center" width="50%">
					注册时间：${nfsLoanRecord.loaner.createTime?string("yyyy-MM-dd") }
				</td>
			</tr>
			<tr>
				<td style="text-align:center" width="50%">
					可用余额：${loanerCurBal}
				</td>
				[#if memberLoanerCard==""]
					<td style="text-align:center" width="50%">
						银行名称：${memberLoanerCard.bank.name }
					</td>
				[/#if]
			</tr>
		</tbody>
	</table>
	<br/>
	<h4>2、资金流水</h4>
		<br/>
	<table width="100%" class="tables" border="1" cellpadding="0" cellspacing="0">
			<tr>
				<th style="text-align:center">流水id</th>
				<th style="text-align:center">姓名</th>
				<th style="text-align:center">金额</th>
				<th style="text-align:center">账户余额</th>
				<th style="text-align:center">资金流转</th>
				<th style="text-align:center">备注</th>
				<th style="text-align:center">借条创建时间流水</th>
			</tr>
		<tbody>
		    [#list memberLoanerActTrxList as memberLoaner]
				<tr>
					<td style="text-align:center">
						${memberLoaner.id }
					</td>
					<td style="text-align:center">
						${nfsLoanRecord.loaner.name }
					</td>
					<td style="text-align:center">
						${memberLoaner.trxAmt }
					</td>
					<td style="text-align:center">
						${memberLoaner.curBal }
					</td>
					<td style="text-align:center">
						${memberLoaner.title }
					</td>
					<td style="text-align:center">
						${memberLoaner.rmk }
					</td>
					<td style="text-align:center">
						${memberLoaner.createTime?string("yyyy-MM-dd HH:mm:ss") }
					</td>
				</tr>
			[/#list]
		</tbody>
	</table>
	<br/>
	<h4>3、充值记录</h4>
		<br/>
	<table width="100%" class="tables" border="1" cellpadding="0" cellspacing="0">
		<tr>
			<th style="text-align: center;">编号</th>
			<th style="text-align: center;">姓名</th>
			<th style="text-align: center;">金额</th>
			<th style="text-align: center;">充值类型</th>
			<th style="text-align: center;">充值状态</th>
			<th style="text-align: center;">充值时间</th>
		</tr>
		<tbody>
			[#list nfsRchgRecordList as rchgRecord]
				<tr>
					<td style="text-align: center;">
						${rchgRecord.id }
					</td>
					<td style="text-align: center;">
						${nfsLoanRecord.loaner.name }
					</td>
					<td style="text-align: center;">
						${rchgRecord.amount}
					</td>
					<td style="text-align: center;">
						[#if rchgRecord.type=="other"]
						其他
						[/#if]
						[#if rchgRecord.type=="fuiou"]
						富友
						[/#if]
						[#if rchgRecord.type=="lianlian"]
						连连
						[/#if]
						[#if rchgRecord.type=="aliPay"]
						支付宝
						[/#if]
						[#if rchgRecord.type=="weiXin"]
						微信
						[/#if]
					</td>
					<td style="text-align: center;">
						[#if rchgRecord.status=="wait"]
						待支付
						[/#if]
						[#if rchgRecord.status=="success"]
						支付成功
						[/#if]
						[#if rchgRecord.status=="failure"]
						支付失败
						[/#if]
					</td>
					<td style="text-align: center;">
						${rchgRecord.createTime?string("yyyy-MM-dd HH:mm:ss") }
					</td>
				</tr>
			[/#list]
		</tbody>
	</table>
	<br/>
	<h2>二、债务人</h2>
		<br/>
	<h4>1、基本信息</h4>
		<br/>
	<table width="100%" class="tables" border="1" cellpadding="0" cellspacing="0">
		<tbody>
			<tr>
				<td style="text-align:center" width="50%">
					姓名：${nfsLoanRecord.loanee.name}
				</td>
				<td style="text-align:center" width="50%">
					手机号：${nfsLoanRecord.loanee.username}
				</td>
			</tr>
			<tr>
				<td style="text-align:center" width="50%">
					昵称：${nfsLoanRecord.loanee.nickname }
				</td>
				<td style="text-align:center" width="50%">
					身份证号码：${nfsLoanRecord.loanee.idNo }
				</td>
			</tr>
			<tr>
				<td style="text-align:center" width="50%">
					注册设备Id：${nfsLoanRecord.loanee.id }
				</td>
				<td style="text-align:center" width="50%">
					注册时间：${nfsLoanRecord.loanee.createTime?string("yyyy-MM-dd") }
				</td>
			</tr>
			<tr>
				<td style="text-align:center" width="50%">
					可用余额：${loaneeCurBal }
				</td>
				[#if memberLoanerCard==""]
					<td style="text-align:center" width="50%">
						银行名称：${memberLoaneeCard.bank.name }
					</td>
				[/#if]
			</tr>
		</tbody>
	</table>
	<br/>
	<h4>2、资金流水</h4>
		<br/>
	<table width="100%" class="tables" border="1" cellpadding="0" cellspacing="0">
			<tr>
				<th style="text-align:center">流水id</th>
				<th style="text-align:center">姓名</th>
				<th style="text-align:center">金额</th>
				<th style="text-align:center">账户余额</th>
				<th style="text-align:center">资金流转</th>
				<th style="text-align:center">备注</th>
				<th style="text-align:center">借条创建时间流水</th>
			</tr>
		<tbody>
			    [#list memberLoaneeActTrxList as loaneeActTrx]
				<tr>
					<td style="text-align:center">
						${loaneeActTrx.id }
					</td>
					<td style="text-align:center">
						${nfsLoanRecord.loanee.name }
					</td>
					<td style="text-align:center">
						${loaneeActTrx.trxAmt }
					</td>
					<td style="text-align:center">
						${loaneeActTrx.curBal }
					</td>
					<td style="text-align:center">
						${loaneeActTrx.title}
					</td>
					<td style="text-align:center">
						${loaneeActTrx.rmk }
					</td>
					<td style="text-align:center">
						${loaneeActTrx.createTime?string("yyyy-MM-dd HH:mm:ss") }
					</td>
				</tr>
				[/#list]
		</tbody>
	</table>
	<br/>
	<h4>3、提现记录</h4>
		<br/>
	<table width="100%" class="tables" border="1" cellpadding="0" cellspacing="0">
		<tr>
			<th style="text-align: center;">编号</th>
			<th style="text-align: center;">姓名</th>
			<th style="text-align: center;">金额</th>
			<th style="text-align: center;">提现类型</th>
			<th style="text-align: center;">提现状态</th>
			<th style="text-align: center;">提现时间</th>
			<th style="text-align: center;">提现说明</th>
		</tr>
		<tbody>
			    [#list nfsWlrdRecordList as wlrdRecord]
				<tr>
					<td style="text-align: center;">
						${wlrdRecord.id }
					</td>
					<td style="text-align: center;">
						${nfsLoanRecord.loanee.name }
					</td>
					<td style="text-align: center;">
						${wlrdRecord.amount }
					</td>
					<td style="text-align: center;">
						[#if wlrdRecord.type=="_default"]
						系统默认值
						[/#if]
						[#if wlrdRecord.type=="fuiou"]
						富友
						[/#if]
						[#if wlrdRecord.type=="lianlian"]
						连连
						[/#if]
					</td>
					<td style="text-align: center;">
						[#if wlrdRecord.status=="auditing"]
						待审核
						[/#if]
						[#if wlrdRecord.status=="retrial"]
						待复审
						[/#if]
						[#if wlrdRecord.status=="pending"]
						待打款
						[/#if]
						[#if wlrdRecord.status=="submited"]
						已提交
						[/#if]
						[#if wlrdRecord.status=="mayRepeatOrder"]
						疑似重复订单
						[/#if]
						[#if wlrdRecord.status=="sendRepeatOrder"]
						重复订单发送(人工查询)
						[/#if]
						[#if wlrdRecord.status=="madeMoney"]
						已打款
						[/#if]
						[#if wlrdRecord.status=="failure"]
						打款失败
						[/#if]
						[#if wlrdRecord.status=="refuse"]
						已拒绝
						[/#if]
						[#if wlrdRecord.status=="cancel"]
						已取消
						[/#if]
					</td>
					<td style="text-align: center;">
						${wlrdRecord.createTime?string("yyyy-MM-dd HH:mm:ss") }
					</td>
					<td style="text-align: center;">
						${wlrdRecord.rmk }
					</td>
				</tr>
			  [/#list]
		</tbody>
	</table>
</body>
</html>
[/#escape]