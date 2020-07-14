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
	    	var loanId = $("#id").val();
	    	window.location.href="${ctx}/loanArbitration/loanCaseData?loanId="+loanId;
	    }
		function exportPdf(){
			var loanId = $("#id").val();
			window.location.href="${admin}/loanArbitration/exportPdf?loanId="+loanId;
		}
</script>
</head>
<body>
	<table>
		<tr>
			<td>
				<input type="text" id = "id" name = "id" value="${loanRecord.id }"/>
				<input type="button" onclick = "exportSearch()" value="查询"/>
				<input type="button" onclick = "exportPdf()" value="转出pdf"/>
			</td>
		</tr>
	</table>
	<h2>一、债权人</h2>
	<br/>
	<h4>1、基本信息</h4>
	<br/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<tbody>
			<tr>
				<td style="text-align:center" width="50%"><a href="${ctx}/loanArbitration/IdCardPhoto?memberId=${loanRecord.loaner.id}">
					姓名：${loanRecord.loaner.name}</a>
				</td>
				<td style="text-align:center" width="50%">
					手机号：${loanRecord.loaner.username}
				</td>
			</tr>
			<tr>
				<td style="text-align:center" width="50%">
					昵称：${loanRecord.loaner.nickname }
				</td>
				<td style="text-align:center" width="50%">
					身份证号码：${loanRecord.loaner.idNo }
				</td>
			</tr>
			<tr>
				<td style="text-align:center" width="50%">
					注册设备Id：${loanRecord.loaner.id }
				</td>
				<td style="text-align:center" width="50%">
					注册时间：<fmt:formatDate value="${loanRecord.loaner.createTime }" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
			</tr>
			<tr>
				<td style="text-align:center" width="50%">
					可用余额：${loanerCurBal }
				</td>
				<c:if test="${loanerCard!=null }">
					<td style="text-align:center" width="50%">
						银行名称：${loanerCard.bank.name }
					</td>
				</c:if>
			</tr>
		</tbody>
	</table>
	<br/>
	<h4>2、资金流水</h4>
		<br/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
			<tr>
				<th style="text-align:center">流水ID</th>
				<th style="text-align:center">姓名</th>
				<th style="text-align:center">金额</th>
				<th style="text-align:center">账户余额</th>
				<th style="text-align:center">资金流转</th>
				<th style="text-align:center">备注</th>
				<th style="text-align:center">创建时间</th>
			</tr>
		<tbody>
			<c:forEach items="${loanerActTrxList }" var="loanerActTrx">
				<tr>
					<td style="text-align:center">
						${loanerActTrx.id }
					</td>
					<td style="text-align:center">
						${loanRecord.loaner.name }
					</td>
					<td style="text-align:center">
						${loanerActTrx.trxAmt }
					</td>
					<td style="text-align:center">
						${loanerActTrx.curBal }
					</td>
					<td style="text-align:center">
						${loanerActTrx.title}
					</td>
					<td style="text-align:center">
						${loanerActTrx.rmk }
					</td>
					<td style="text-align:center">
						<fmt:formatDate value="${loanerActTrx.createTime }" pattern="yyyy-MM-dd HH:mm:ss"/>	
					</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	<br/>
	<h4>3、充值记录</h4>
		<br/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<tr>
			<th style="text-align: center;">编号</th>
			<th style="text-align: center;">姓名</th>
			<th style="text-align: center;">金额</th>
			<th style="text-align: center;">充值类型</th>
			<th style="text-align: center;">充值状态</th>
			<th style="text-align: center;">充值时间</th>
		</tr>
		<tbody>
			<c:forEach items="${nfsRchgRecordList }" var="rchgRecord">
				<tr>
					<td style="text-align: center;">
						${rchgRecord.id }
					</td>
					<td style="text-align: center;">
						${loanRecord.loaner.name }
					</td>
					<td style="text-align: center;">
						${rchgRecord.amount }
					</td>
					<td style="text-align: center;">
						${fns:getDictLabel(rchgRecord.type, 'rechargeType', '')}
					</td>
					<td style="text-align: center;">
						${fns:getDictLabel(rchgRecord.status, 'rchgStatus', '')}
					</td>
					<td style="text-align: center;">
						<fmt:formatDate value="${rchgRecord.createTime }" pattern="yyyy-MM-dd HH:mm:ss"/>
					</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	<br/>
	<h2>二、债务人</h2>
		<br/>
	<h4>1、基本信息</h4>
		<br/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<tbody>
			<tr>
				<td style="text-align:center" width="50%"><a href="${ctx}/loanArbitration/IdCardPhoto?memberId=${loanRecord.loanee.id}">
					姓名：${loanRecord.loanee.name}</a>
				</td>
				<td style="text-align:center" width="50%">
					手机号：${loanRecord.loanee.username}
				</td>
			</tr>
			<tr>
				<td style="text-align:center" width="50%">
					昵称：${loanRecord.loanee.nickname }
				</td>
				<td style="text-align:center" width="50%">
					身份证号码：${loanRecord.loanee.idNo }
				</td>
			</tr>
			<tr>
				<td style="text-align:center" width="50%">
					注册设备Id：${loanRecord.loanee.id }
				</td>
				<td style="text-align:center" width="50%">
					注册时间：<fmt:formatDate value="${loanRecord.loanee.createTime }" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
			</tr>
			<tr>
				<td style="text-align:center" width="50%">
					可用余额：${loaneeCurBal}
				</td>
				<c:if test="${loaneeCard!=null }">
					<td style="text-align:center" width="50%">
						银行名称：${loaneeCard.bank.name }
					</td>
				</c:if>
			</tr>
		</tbody>
	</table>
	<br/>
	<h4>2、资金流水</h4>
		<br/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
			<tr>
				<th style="text-align:center">流水ID</th>
				<th style="text-align:center">姓名</th>
				<th style="text-align:center">金额</th>
				<th style="text-align:center">账户余额</th>
				<th style="text-align:center">资金流转</th>
				<th style="text-align:center">备注</th>
				<th style="text-align:center">创建时间</th>
			</tr>
		<tbody>
			<c:forEach items="${loaneeActTrxList}" var="loaneeActTrx">
				<tr>
					<td style="text-align:center">
						${loaneeActTrx.id }
					</td>
					<td style="text-align:center">
						${loanRecord.loanee.name }
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
						<fmt:formatDate value="${loaneeActTrx.createTime }" pattern="yyyy-MM-dd HH:mm:ss"/>
					</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	<br/>
	<h4>3、提现记录</h4>
		<br/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
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
			<c:forEach items="${nfsWlrdRecordList }" var="nfsWlrdRecord">
				<tr>
					<td style="text-align: center;">
						${nfsWlrdRecord.id }
					</td>
					<td style="text-align: center;">
						${loanRecord.loanee.name }
					</td>
					<td style="text-align: center;">
						${nfsWlrdRecord.amount }
					</td>
					<td style="text-align: center;">
						${fns:getDictLabel(nfsWlrdRecord.type, 'wdrlRecordType', '')}
					</td>
					<td style="text-align: center;">
						${fns:getDictLabel(nfsWlrdRecord.status, 'wdrlRecordStatus', '')}
					</td>
					<td style="text-align: center;">
						<fmt:formatDate value="${nfsWlrdRecord.createTime }" pattern="yyyy-MM-dd HH:mm:ss"/>
					</td>
					<td style="text-align: center;">
						${nfsWlrdRecord.rmk }
					</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	<br/>
</body>
</html>