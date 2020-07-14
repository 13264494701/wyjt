<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>会员登陆信息管理</title>
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
		<li class="active"><a href="${ctx}/mem/memMemberLoginInfo/">会员登陆信息列表</a></li>
		<shiro:hasPermission name="mem:memMemberLoginInfo:edit"><li><a href="${ctx}/mem/memMemberLoginInfo/add">会员登陆信息添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="memMemberLoginInfo" action="${ctx}/mem/memMemberLoginInfo/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label style="text-align:right;width:150px">会员编号：</label>
				<form:input path="memberId" htmlEscape="false" maxlength="20" class="input-medium"/>
			</li>
			<li><label style="text-align:right;width:150px">系统类型：</label>
				<form:input path="osType" htmlEscape="false" maxlength="4" class="input-medium"/>
			</li>
			<li><label style="text-align:right;width:150px">系统版本：</label>
				<form:input path="osVersion" htmlEscape="false" maxlength="16" class="input-medium"/>
			</li>
			<li><label style="text-align:right;width:150px">应用版本：</label>
				<form:input path="appVersion" htmlEscape="false" maxlength="16" class="input-medium"/>
			</li>
			<li><label style="text-align:right;width:150px">会员昵称：</label>
				<form:input path="deviceToken" htmlEscape="false" maxlength="64" class="input-medium"/>
			</li>
			<li class="btns"><label style="text-align:right;width:50px"></label><input id="btnSubmit" class="btn btn-primary " style="width:80px;" type="submit" value="查   询"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<shiro:hasPermission name="mem:memMemberLoginInfo:edit"><th style="text-align:center">操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="memMemberLoginInfo">
			<tr>
				<td  style="text-align:center">
				<shiro:hasPermission name="mem:memMemberLoginInfo:edit">
    				<a href="${ctx}/mem/memMemberLoginInfo/update?id=${memMemberLoginInfo.id}">修改</a>
					<a href="${ctx}/mem/memMemberLoginInfo/delete?id=${memMemberLoginInfo.id}" onclick="return confirmx('确认要删除该会员登陆信息吗？', this.href)">删除</a>
				</shiro:hasPermission>
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>