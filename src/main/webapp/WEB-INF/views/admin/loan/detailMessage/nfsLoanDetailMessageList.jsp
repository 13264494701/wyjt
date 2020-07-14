<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>借条详情对话记录管理</title>
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
		<li class="active"><a href="${ctx}/loan/nfsLoanDetailMessage/">借条详情对话记录列表</a></li>
		<shiro:hasPermission name="loan:nfsLoanDetailMessage:edit"><li><a href="${ctx}/loan/nfsLoanDetailMessage/add">借条详情对话记录添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="nfsLoanDetailMessage" action="${ctx}/loan/nfsLoanDetailMessage/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label style="text-align:right;width:150px">借条详情ID：</label>
				<form:input path="detailId" htmlEscape="false" maxlength="20" class="input-medium"/>
			</li>
			<li class="btns"><label style="text-align:right;width:50px"></label><input id="btnSubmit" class="btn btn-primary " style="width:80px;" type="submit" value="查   询"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th style="text-align:center">借条详情ID</th>
				<th style="text-align:center">用户id</th>
				<th style="text-align:center">内容</th>
				<shiro:hasPermission name="loan:nfsLoanDetailMessage:edit"><th style="text-align:center">操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="nfsLoanDetailMessage">
			<tr>
				<td style="text-align:center"><a href="${ctx}/loan/nfsLoanDetailMessage/query?id=${nfsLoanDetailMessage.id}">
					${nfsLoanDetailMessage.detailId}
				</a></td>
				<td style="text-align:center">
					${nfsLoanDetailMessage.memberId}
				</td>
				<td style="text-align:center">
					${nfsLoanDetailMessage.note}
				</td>
				<td  style="text-align:center">
				<shiro:hasPermission name="loan:nfsLoanDetailMessage:edit">
    				<a href="${ctx}/loan/nfsLoanDetailMessage/update?id=${nfsLoanDetailMessage.id}">修改</a>
					<a href="${ctx}/loan/nfsLoanDetailMessage/delete?id=${nfsLoanDetailMessage.id}" onclick="return confirmx('确认要删除该借条详情对话记录吗？', this.href)">删除</a>
				</shiro:hasPermission>
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>