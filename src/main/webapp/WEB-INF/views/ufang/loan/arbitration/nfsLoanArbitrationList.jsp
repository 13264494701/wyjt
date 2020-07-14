<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>借条仲裁查询管理</title>
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
		<li class="active"><a href="${ctx}/loanArbitration/">借条仲裁查询列表</a></li>
	</ul>
	<form:form id="searchForm" modelAttribute="nfsLoanArbitration" action="${ufang}/loanArbitration/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label style="text-align:right;width:150px">借条编号：</label>
				<form:input path="loan.id" htmlEscape="false" maxlength="20" class="input-medium"/>
			</li>
			<li><label style="text-align:right;width:150px">债权人姓名：</label>
				<form:input path="loan.loaner.name" htmlEscape="false" maxlength="20" class="input-medium"/>
			</li>
			<li><label style="text-align:right;width:150px">债权人手机号：</label>
				<form:input path="loan.loaner.username" htmlEscape="false" maxlength="20" class="input-medium"/>
			</li>
			<li><label style="text-align:right;width:150px">债务人姓名：</label>
				<form:input path="loan.loanee.name" htmlEscape="false" maxlength="20" class="input-medium"/>
			</li>
			<li><label style="text-align:right;width:150px">债务人手机号：</label>
				<form:input path="loan.loanee.username" htmlEscape="false" maxlength="20" class="input-medium"/>
			</li>
			<li><label style="text-align:right;width:150px">仲裁状态：</label>
				<form:select path="status"  style="width:177px" class="input-medium">
					<form:option value="" label="请选择"/>
					<form:options items="${fns:getDictList('arbitrationStatus')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</li>
			<li><label style="text-align:right;width:150px">代理渠道：</label>
				<form:select path="channel"  style="width:177px" class="input-medium">
					<form:option value="" label="请选择"/>
					<form:options items="${fns:getDictList('arbitrationChannel')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</li>
			<li><label style="text-align:right;width:150px">仲裁申请时间：</label>
				<input name="beginTime" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${beginTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:true});"/>
			</li>
			<li><label style="text-align:right;width:150px">至：</label>
				<input name="endTimes" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${endTimes}" pattern="yyyy-MM-dd HH:mm:ss"/>"
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
				<th style="text-align:center">债权人编号</th>
				<th style="text-align:center">债权人姓名</th>
				<th style="text-align:center">债权人联系方式</th>
				<th style="text-align:center">债权人身份证号码</th>
				<th style="text-align:center">债务人编号</th>
				<th style="text-align:center">债务人姓名</th>
				<th style="text-align:center">债务人联系方式</th>
				<th style="text-align:center">债务人身份证号码</th>
				<th style="text-align:center">申请仲裁金额</th>
				<th style="text-align:center">仲裁服务费</th>
				<th style="text-align:center">退费金额</th>
				<th style="text-align:center">仲裁状态</th>
				<th style="text-align:center">代理渠道</th>
				<th style="text-align:center">结案时间</th>
				<th style="text-align:center">出裁决时间</th>
				<th style="text-align:center">申请时间</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="nfsLoanArbitration">
			<tr>
				<td style="text-align:center">
				${nfsLoanArbitration.loan.id}
				</td>
				<td style="text-align:center">
					${nfsLoanArbitration.loan.loaner.id}
				</td>
				<td style="text-align:center">
					${nfsLoanArbitration.loan.loaner.name}
				</td>
				<td style="text-align:center">
					${nfsLoanArbitration.loan.loaner.username}
				</td>
				<td style="text-align:center">
					${nfsLoanArbitration.loan.loaner.idNo}
				</td>
				<td style="text-align:center">
					${nfsLoanArbitration.loan.loanee.id}
				</td>
				<td style="text-align:center">
					${nfsLoanArbitration.loan.loanee.name}
				</td>
				<td style="text-align:center">
					${nfsLoanArbitration.loan.loanee.username}
				</td>
				<td style="text-align:center">
					${nfsLoanArbitration.loan.loanee.idNo}
				</td>
				<td style="text-align:center">
					${fns:decimalToStr(nfsLoanArbitration.applyAmount,2)}元
				</td>
				<td style="text-align:center">
					${fns:decimalToStr(nfsLoanArbitration.fee,2)}元
				</td>
				<td style="text-align:center">
					${fns:decimalToStr(nfsLoanArbitration.refundFee,2)}元
				</td>
				<td style="text-align:center">
					${fns:getDictLabel(nfsLoanArbitration.status, 'arbitrationStatus', '')}
				</td>
				<td style="text-align:center">
					${fns:getDictLabel(nfsLoanArbitration.channel, 'arbitrationChannel', '')}
				</td>
				<td style="text-align:center">
					<fmt:formatDate value="${nfsLoanArbitration.endTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td style="text-align:center">
					<fmt:formatDate value="${nfsLoanArbitration.ruleTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td style="text-align:center">
					<fmt:formatDate value="${nfsLoanArbitration.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>