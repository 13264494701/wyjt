<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>业务保全管理</title>
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
		<li class="active"><a href="${ctx}/loan/preservation/nfsLoanPreservation/">业务保全列表</a></li>
		<shiro:hasPermission name="loan:preservation:nfsLoanPreservation:edit"><li><a href="${ctx}/loan/preservation/nfsLoanPreservation/add">业务保全添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="nfsLoanPreservation" action="${ctx}/loan/preservation/nfsLoanPreservation/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label style="text-align:right;width:150px">借条id：</label>
				<form:input path="loan.id" htmlEscape="false" maxlength="20" class="input-medium"/>
			</li>
			<li><label style="text-align:right;width:150px">节点类型：</label>
				<form:select path="nodeType"  style="width:177px" class="input-medium">
					<form:option value="" label="请选择"/>
					<form:options items="${fns:getDictList('')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</li>
			<li><label style="text-align:right;width:150px">保全号：</label>
				<form:input path="precode" htmlEscape="false" maxlength="64" class="input-medium"/>
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
				<th style="text-align:center">借条id</th>
				<th style="text-align:center">父节点订单id</th>
				<th style="text-align:center">节点类型</th>
				<th style="text-align:center">保全号</th>
				<th style="text-align:center">备注</th>
				<shiro:hasPermission name="loan:preservation:nfsLoanPreservation:edit"><th style="text-align:center">操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="nfsLoanPreservation">
			<tr>
				<td style="text-align:center"><a href="${ctx}/loan/preservation/nfsLoanPreservation/query?id=${nfsLoanPreservation.id}">
					${nfsLoanPreservation.id}
				</a></td>
				<td style="text-align:center">
					${nfsLoanPreservation.loan.id}
				</td>
				<td style="text-align:center">
					${nfsLoanPreservation.parentOrderId}
				</td>
				<td style="text-align:center">
					${fns:getDictLabel(nfsLoanPreservation.nodeType, '', '')}
				</td>
				<td style="text-align:center">
					${nfsLoanPreservation.precode}
				</td>
				<td style="text-align:center">
					${nfsLoanPreservation.rmk}
				</td>
				<td  style="text-align:center">
				<shiro:hasPermission name="loan:preservation:nfsLoanPreservation:edit">
    				<a href="${ctx}/loan/preservation/nfsLoanPreservation/update?id=${nfsLoanPreservation.id}">修改</a>
					<a href="${ctx}/loan/preservation/nfsLoanPreservation/delete?id=${nfsLoanPreservation.id}" onclick="return confirmx('确认要删除该业务保全吗？', this.href)">删除</a>
				</shiro:hasPermission>
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>