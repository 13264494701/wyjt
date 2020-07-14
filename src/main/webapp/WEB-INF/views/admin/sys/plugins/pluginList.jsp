<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>插件管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			
		});
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/acct/splyMemberAct/">插件列表</a></li>
	</ul>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th style="text-align:center">名称</th>
				<th style="text-align:center">版本号</th>
				<shiro:hasPermission name="plugins:edit"><th style="text-align:center">操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${plugins}" var="plugin">
			<tr>
				<td style="text-align:center">
					${plugin.name}
				</td>
				<td style="text-align:center">
					${plugin.version}
				</td>
			
				<td  style="text-align:center">
				<shiro:hasPermission name="plugins:edit">
					<c:choose>
						<c:when test="${plugin.isInstalled }">
							<a href="${ctx}/plugin/setting/${plugin.id}">配置</a>
							<a href="${ctx}/plugin/uninstall/${plugin.id}">卸载</a>
						</c:when>
						<c:otherwise>
							<a href="${ctx}/plugin/install/${plugin.id}">安装</a>
						</c:otherwise>
					</c:choose>
				</shiro:hasPermission>
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
<%-- 	<div class="pagination">${page}</div> --%>
</body>
</html>