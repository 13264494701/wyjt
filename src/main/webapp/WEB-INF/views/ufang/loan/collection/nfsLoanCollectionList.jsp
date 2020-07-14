<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>借条催收管理</title>
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
		<li class="active"><a href="${ctx}/nfsLoanCollection/list">借条催收列表</a></li>
	</ul>
	<form:form id="searchForm" modelAttribute="nfsLoanCollection" action="${ufang}/nfsLoanCollection/list" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label style="text-align:right;width:150px">放款人手机号：</label>
				<form:input path="loan.loaner.username" htmlEscape="false" maxlength="11" class="input-medium"/>
			</li>
			<li><label style="text-align:right;width:150px">借款人手机号：</label>
				<form:input path="loan.loanee.username" htmlEscape="false" maxlength="11" class="input-medium"/>
			</li>
			<li><label style="text-align:right;width:150px">借款单状态：</label>
				<form:select path="loan.status"  style="width:177px" class="input-medium">
					<form:option value="" label="请选择"/>
					<form:options items="${fns:getDictList('loanStatus')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</li>
			<li><label style="text-align:right;width:150px">借条编号：</label>
				<form:input path="loan.id" htmlEscape="false" maxlength="20" class="input-medium"/>
			</li>
			<li><label style="text-align:right;width:150px">催收状态：</label>
				<form:select path="status"  style="width:177px" class="input-medium">
					<form:option value="" label="请选择"/>
					<form:options items="${fns:getDictList('collectionStatus')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</li>
			<li><label style="text-align:right;width:150px">催收申请时间：</label>
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
				<th style="text-align:center">借款单状态</th>
				<th style="text-align:center">催收状态</th>
				<th style="text-align:center">借款总金额</th>
				<th style="text-align:center">催收费用</th>
				<th style="text-align:center">放款人姓名</th>
				<th style="text-align:center">放款人联系方式</th>
				<th style="text-align:center">借款人姓名</th>
				<th style="text-align:center">借款人联系方式</th>
				<th style="text-align:center">借款人身份证号</th>
				<th style="text-align:center">到期还款时间</th>
				<th style="text-align:center">催收申请时间</th>
				<th style="text-align:center">催收天数</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="nfsLoanCollection">
			<tr>
				<td style="text-align:center">
				${nfsLoanCollection.loan.id}
				</td>
				<td style="text-align:center">
					${fns:getDictLabel(nfsLoanCollection.loan.status, 'loanStatus', '')}
				</td>
				<td style="text-align:center">
					${fns:getDictLabel(nfsLoanCollection.status, 'collectionStatus', '')}
				</td>
				<td style="text-align:center">
					${fns:decimalToStr(nfsLoanCollection.loan.amount,2)}
				</td>
				<td style="text-align:center">
					${fns:decimalToStr(nfsLoanCollection.fee,2)}
				</td>
				<td style="text-align:center">
					${nfsLoanCollection.loan.loaner.name}
				</td>
				<td style="text-align:center">
					${nfsLoanCollection.loan.loaner.username}
				</td>
				<td style="text-align:center">
					${nfsLoanCollection.loan.loanee.name}
				</td>
				<td style="text-align:center">
					${nfsLoanCollection.loan.loanee.username}
				</td>
				<td style="text-align:center">
					${nfsLoanCollection.loan.loanee.idNo}
				</td>
				<td style="text-align:center">
					<fmt:formatDate value="${nfsLoanCollection.loan.completeDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td style="text-align:center">
					<fmt:formatDate value="${nfsLoanCollection.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td style="text-align:center">
					${fns:pastDays(nfsLoanCollection.createTime)}
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>