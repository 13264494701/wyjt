<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>系统版本管理</title>
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
		<li class="active"><a href="${ctx}/sysVersion/">系统版本列表</a></li>
		<shiro:hasPermission name="version:sysVersion:edit"><li><a href="${ctx}/sysVersion/add">系统版本添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="sysVersion" action="${ctx}/sysVersion/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label style="text-align:right;width:150px">前端类型：</label>
				<form:select path="type"  style="width:177px" class="input-medium">
					<form:option value="" label="请选择"/>
					<form:options items="${fns:getDictList('frontType')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
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
				<th style="text-align:center">前端类型</th>
				<th style="text-align:center">最新版本号</th>
				<th style="text-align:center">是否需要升级</th>
				<th style="text-align:center">是否强制升级</th>
				<th style="text-align:center">更新时间</th>
				<shiro:hasPermission name="version:sysVersion:edit"><th style="text-align:center">操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="sysVersion">
			<tr>
				<td style="text-align:center"><a href="${ctx}/sysVersion/query?id=${sysVersion.id}">
					${fns:getDictLabel(sysVersion.type, 'frontType', '')}
				</a></td>
				<td style="text-align:center">
					${sysVersion.lastVersion}
				</td>
				<td style="text-align:center">
					${fns:getDictLabel(sysVersion.needsUpdate, 'trueOrFalse', '')}
				</td>
				<td style="text-align:center">
					${fns:getDictLabel(sysVersion.isForce, 'trueOrFalse', '')}
				</td>
				<td style="text-align:center">
					<fmt:formatDate value="${sysVersion.updateTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td  style="text-align:center">
				<shiro:hasPermission name="version:sysVersion:edit">
    				<a href="${ctx}/sysVersion/update?id=${sysVersion.id}">修改</a>
					<a href="${ctx}/sysVersion/delete?id=${sysVersion.id}" onclick="return confirmx('确认要删除该系统版本吗？', this.href)">删除</a>
				</shiro:hasPermission>
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>