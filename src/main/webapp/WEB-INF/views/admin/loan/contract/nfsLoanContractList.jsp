<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>合同表管理</title>
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
		<li class="active"><a href="${ctx}/contract/nfsLoanContract/">合同表列表</a></li>
	</ul>
	<form:form id="searchForm" modelAttribute="nfsLoanContract" action="${ctx}/contract/nfsLoanContract/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label style="text-align:right;width:100px">编号：</label>
				<form:input path="id" htmlEscape="false" maxlength="20" class="input-medium"/>
			</li>
			<li><label style="text-align:right;width:100px">借条编号：</label>
				<form:input path="loanId" htmlEscape="false" maxlength="20" class="input-medium"/>
			</li>
			<li><label style="text-align:right;width:100px">签章类型：</label>
				<form:select path="SignatureType"  style="width:177px" class="input-medium">
					<form:option value="" label="请选择"/>
					<form:options items="${fns:getDictList('signatureType')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</li>
				<li><label style="text-align:right;width:100px">合同状态：</label>
				<form:select path="status"  style="width:177px" class="input-medium">
					<form:option value="" label="请选择"/>
					<form:options items="${fns:getDictList('signatureStatus')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</li>
			<li class="btns"><label style="text-align:right;width:50px"></label><input id="btnSubmit" class="btn btn-primary " style="width:80px;" type="submit" value="查   询"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th style="text-align:center">编号</th>
				<th style="text-align:center">借条编号</th>
				<th style="text-align:center">签章类型</th>
				<th style="text-align:center">签章地址</th>
				<th style="text-align:center">合同状态</th>
				<th style="text-align:center">创建时间</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="nfsLoanContract">
			<tr>
				<td style="text-align:center"><a href="${ctx}/contract/nfsLoanContract/query?id=${nfsLoanContract.id}">
					${nfsLoanContract.id}
				</a></td>
				<td style="text-align:center">
					${nfsLoanContract.loanId}
				</td>
				<td style="text-align:center">
				${fns:getDictLabel(nfsLoanContract.signatureType,'signatureType', '')}
				</td>
				<td style="text-align:center"><a href="${pageContext.request.contextPath}${nfsLoanContract.contractUrl}">
					${nfsLoanContract.contractUrl}
				</td>
				<td style="text-align:center">
				${fns:getDictLabel(nfsLoanContract.status,'signatureStatus', '')}
				</td>
				<td style="text-align:center">
					<fmt:formatDate value="${nfsLoanContract.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
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