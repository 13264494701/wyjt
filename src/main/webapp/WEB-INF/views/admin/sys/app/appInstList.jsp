<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>应用激活管理</title>
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
		<li class="active"><a href="${ctx}/app/appInst/">应用激活列表</a></li>
		<shiro:hasPermission name="app:appInst:edit"><li><a href="${ctx}/app/appInst/add">应用激活添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="appInst" action="${ctx}/app/appInst/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label style="text-align:right;width:150px">设备号：</label>
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
				<th style="text-align:center">系统类型</th>
				<th style="text-align:center">系统版本</th>
				<th style="text-align:center">应用版本</th>
				<th style="text-align:center">ak</th>
				<th style="text-align:center">设备型号</th>
				<th style="text-align:center">设备号</th>
				<th style="text-align:center">推送码</th>
				<th style="text-align:center">渠道编号</th>
				<th style="text-align:center">最后登录IP</th>
				<th style="text-align:center">生成时间</th>
				<shiro:hasPermission name="app:appInst:edit"><th style="text-align:center">操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="appInst">
			<tr>
				<td style="text-align:center"><a href="${ctx}/app/appInst/query?id=${appInst.id}">
					${appInst.osType}
				</a></td>
				<td style="text-align:center">
					${appInst.osVersion}
				</td>
				<td style="text-align:center">
					${appInst.appVersion}
				</td>
				<td style="text-align:center">
					${appInst.ak}
				</td>
				<td style="text-align:center">
					${appInst.deviceModel}
				</td>
				<td style="text-align:center">
					${appInst.deviceToken}
				</td>
				<td style="text-align:center">
					${appInst.pushToken}
				</td>
				<td style="text-align:center">
					${appInst.channeId}
				</td>
				<td style="text-align:center">
					${appInst.loginIp}
				</td>
				<td style="text-align:center">
					<fmt:formatDate value="${appInst.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td  style="text-align:center">
				<shiro:hasPermission name="app:appInst:edit">
    				<a href="${ctx}/app/appInst/update?id=${appInst.id}">修改</a>
					<a href="${ctx}/app/appInst/delete?id=${appInst.id}" onclick="return confirmx('确认要删除该应用激活吗？', this.href)">删除</a>
				</shiro:hasPermission>
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>