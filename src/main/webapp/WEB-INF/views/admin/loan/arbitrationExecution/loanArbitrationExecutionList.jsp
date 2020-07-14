<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>操作成功管理</title>
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
		<li class="active"><a href="${admin}/loanArbitrationExecution/">强执列表</a></li>
	</ul>
	<form:form id="searchForm" modelAttribute="nfsLoanArbitrationExecution" action="${admin}/loanArbitrationExecution/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label style="text-align:right;width:150px">借条编号：</label>
				<form:input path="loanNo" htmlEscape="false" maxlength="16" class="input-medium"/>
			</li>
			<li><label style="text-align:right;width:150px">放款人ID：</label>
				<form:input path="loanerId" htmlEscape="false" maxlength="20" class="input-medium"/>
			</li>
			<li><label style="text-align:right;width:150px">放款人姓名：</label>
				<form:input path="loanerName" htmlEscape="false" maxlength="64" class="input-medium"/>
			</li>
			<li><label style="text-align:right;width:150px">借款人ID：</label>
				<form:input path="loaneeId" htmlEscape="false" maxlength="20" class="input-medium"/>
			</li>
			<li><label style="text-align:right;width:150px">借款人姓名：</label>
				<form:input path="loaneeName" htmlEscape="false" maxlength="64" class="input-medium"/>
			</li>
			<li><label style="text-align:right;width:150px">强执状态：</label>
				<form:select path="status"  style="width:177px" class="input-medium">
					<form:option value="" label="请选择"/>
					<form:options items="${fns:getDictList('arbitrationExecutionStatus')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</li>
			<li><label style="text-align:right;width:150px">借条状态：</label>
				<form:select path="loan.status"  style="width:177px" class="input-medium">
					<form:option value="" label="请选择"/>
					<form:options items="${fns:getDictList('loanStatus')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</li>
			<li><label style="text-align:right;width:150px">强执申请时间：</label>
				<input name="beginTime" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${beginTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:true});"/>
			</li>
			<li><label style="text-align:right;width:150px">至：</label>
				<input name="endTime" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${endTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"
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
				<th style="text-align:center">借条编号</th>
				<th style="text-align:center">借条状态</th>
				<th style="text-align:center">放款人ID</th>
				<th style="text-align:center">放款人姓名</th>
				<th style="text-align:center">借款人ID</th>
				<th style="text-align:center">借款人姓名</th>
				<th style="text-align:center">借款金额</th>
				<th style="text-align:center">借款利息</th>
				<th style="text-align:center">借款利率</th>
				<th style="text-align:center">借款期限</th>
				<th style="text-align:center">已还期数</th>
				<th style="text-align:center">未还期数</th>
				<th style="text-align:center">本期到期日</th>
				<th style="text-align:center">可缴费时间</th>
				<th style="text-align:center">出裁决时间</th>
				<th style="text-align:center">本期应还金额</th>
				<th style="text-align:center">强执状态</th>
				<th style="text-align:center">成交渠道</th>
				<th style="text-align:center">申请渠道</th>
				<th style="text-align:center">费用</th>
				<shiro:hasPermission name="loan:arbitrationExecution:edit"><th style="text-align:center">操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="arbitrationExecution">
			<tr>
				<td style="text-align:center"><a href="${admin}/nfsLoanRecord/query?id=${arbitrationExecution.loan.id}">
					${arbitrationExecution.loan.id}
				</a></td>
				<td style="text-align:center">
					${fns:getDictLabel(arbitrationExecution.loan.status, 'loanStatus', '')}
				</td>
				<td style="text-align:center">
					${arbitrationExecution.loanerId}
				</td>
				<td style="text-align:center"><a href="${ctx}/member/findMemberById?id=${arbitrationExecution.loanerId}">
					${arbitrationExecution.loanerName}</a>
				</td>
				<td style="text-align:center">
					${arbitrationExecution.loaneeId}
				</td>
				<td style="text-align:center"><a href="${ctx}/member/findMemberById?id=${arbitrationExecution.loaneeId}">
					${arbitrationExecution.loaneeName}</a>
				</td>
				<td style="text-align:center">
					${fns:decimalToStr(arbitrationExecution.amount,2)}元
				</td>
				<td style="text-align:center">
					${fns:decimalToStr(arbitrationExecution.interest,2)}元
				</td>
				<td style="text-align:center">
					${arbitrationExecution.intRate}
				</td>
				<td style="text-align:center">
					${arbitrationExecution.term}
				</td>
				<td style="text-align:center">
					${arbitrationExecution.repayedTerm}
				</td>
				<td style="text-align:center">
					${arbitrationExecution.dueRepayTerm}
				</td>
				<td style="text-align:center">
					<fmt:formatDate value="${arbitrationExecution.dueRepayDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td style="text-align:center">
					<fmt:formatDate value="${arbitrationExecution.paytime}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td style="text-align:center">
					<fmt:formatDate value="${arbitrationExecution.rulingtime}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td style="text-align:center">
					${fns:decimalToStr(arbitrationExecution.dueRepayAmount,2)}元
				</td>
				<td style="text-align:center">
					${fns:getDictLabel(arbitrationExecution.status, 'arbitrationExecutionStatus', '')}
				</td>
				<td style="text-align:center">
					${fns:getDictLabel(arbitrationExecution.channel, 'arbitrationExecutionChannel', '')}
				</td>
				<td style="text-align:center">
					${fns:getDictLabel(arbitrationExecution.loan.trxType, 'loanTrxType', '')}
				</td>
				<td style="text-align:center">
					${fns:decimalToStr(arbitrationExecution.fee,2)}元
				</td>
				<td  style="text-align:center">
				<shiro:hasPermission name="loan:arbitrationExecution:edit">
    				<a href="${admin}/loanArbitrationExecution/update?id=${arbitrationExecution.id}">修改</a>
    				<a href="${admin}/loanArbitrationExecutionDetail?executionId=${arbitrationExecution.id}">查看详情</a>
				</shiro:hasPermission>
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>