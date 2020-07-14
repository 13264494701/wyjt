<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>专业借款管理</title>
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
		<li class="active"><a href="${ctx}/ufangLoaner/">专业借款列表</a></li>
		<shiro:hasPermission name="list:ufangLoaner:edit"><li><a href="${ctx}/ufangLoaner/add">专业借款添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="ufangLoaner" action="${ctx}/ufangLoaner/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label style="text-align:right;width:150px">会员姓名：</label>
				<form:input path="memberName" htmlEscape="false" maxlength="64" class="input-medium"/>
			</li>
			<li><label style="text-align:right;width:150px">是否开业：</label>
				<form:input path="isOpen" htmlEscape="false" maxlength="1" class="input-medium"/>
			</li>
			<li><label style="text-align:right;width:150px">是否有存量借条：</label>
				<form:input path="isStock" htmlEscape="false" maxlength="1" class="input-medium"/>
			</li>
			<li class="btns"><label style="text-align:right;width:50px"></label><input id="btnSubmit" class="btn btn-primary " style="width:80px;" type="submit" value="查   询"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th style="text-align:center">会员姓名</th>
				<th style="text-align:center">头像</th>
				<th style="text-align:center">放款笔数</th>
				<th style="text-align:center">放款金额</th>
				<th style="text-align:center">放款要求</th>
				<th style="text-align:center">是否开业</th>
				<th style="text-align:center">是否有存量借条</th>
				<th style="text-align:center">描述</th>
				<shiro:hasPermission name="list:ufangLoaner:edit"><th style="text-align:center">操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="ufangLoaner">
			<tr>
				<td style="text-align:center"><a href="${ctx}/ufangLoaner/query?id=${ufangLoaner.id}">
					${ufangLoaner.memberName}
				</a></td>
				<td style="text-align:center">
					${ufangLoaner.headimage}
				</td>
				<td style="text-align:center">
					${ufangLoaner.loanQuantity}
				</td>
				<td style="text-align:center">
					${ufangLoaner.loanAmount}
				</td>
				<td style="text-align:center">
					${ufangLoaner.loanRequirement}
				</td>
				<td style="text-align:center">
					${ufangLoaner.isOpen}
				</td>
				<td style="text-align:center">
					${ufangLoaner.isStock}
				</td>
				<td style="text-align:center">
					${ufangLoaner.rmk}
				</td>
				<td  style="text-align:center">
				<shiro:hasPermission name="list:ufangLoaner:edit">
    				<a href="${ctx}/ufangLoaner/update?id=${ufangLoaner.id}">修改</a>
					<a href="${ctx}/ufangLoaner/delete?id=${ufangLoaner.id}" onclick="return confirmx('确认要删除该专业借款吗？', this.href)">删除</a>
				</shiro:hasPermission>
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>