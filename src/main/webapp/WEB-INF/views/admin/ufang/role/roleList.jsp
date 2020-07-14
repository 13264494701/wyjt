]<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>角色管理</title>
<meta name="decorator" content="default" />
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${admin}/ufang/role/">角色列表</a></li>
		<shiro:hasPermission name="ufang:role:edit">
			<li><a href="${admin}/ufang/role/add">角色添加</a></li>
		</shiro:hasPermission>
	</ul>
	<sys:message content="${message}" />
	<table id="contentTable"
		class="table table-striped table-bordered table-condensed">
		<tr>

			<th style="text-align:center">角色名称</th>
			<th style="text-align:center">数据范围</th>
			<shiro:hasPermission name="ufang:role:edit"><th style="text-align:center">操作</th></shiro:hasPermission>
		</tr>
		<c:forEach items="${list}" var="role">
			<tr>

				<td style="text-align:center">${role.roleName}</td>
				<td style="text-align:center">${fns:getDictLabel(role.dataScope, 'ufangDataScope', '无')}</td>
				<c:choose>
					<c:when test="${role.isGod}">
					<td style="text-align: center">
					   <shiro:hasPermission name="ufang:role:edit">
					    <a href="${admin}/ufang/role/query?id=${role.id}">查看</a>
						<a href="${admin}/ufang/role/update?id=${role.id}">修改</a>
					   </shiro:hasPermission></td>
					</c:when>
					<c:otherwise>
					<td style="text-align: center"><shiro:hasPermission name="ufang:role:edit">
					    <a href="${admin}/ufang/role/query?id=${role.id}">查看</a>
						<a href="${admin}/ufang/role/update?id=${role.id}">修改</a>
						<a href="${admin}/ufang/role/delete?id=${role.id}" onclick="return confirmx('确认要删除该角色吗？', this.href)">删除</a>
					 </shiro:hasPermission></td>
					 </c:otherwise>
				</c:choose>
			</tr>
		</c:forEach>
	</table>
</body>
</html>