<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>广告位置管理</title>
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
		<li class="active"><a href="${ctx}/adPosition/">广告位置列表</a></li>
		<shiro:hasPermission name="cms:adPosition:edit"><li><a href="${ctx}/adPosition/add">广告位置添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="cmsAdPosition" action="${ctx}/adPosition/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label style="text-align:right;width:150px">名称：</label>
				<form:input path="positionName" htmlEscape="false" maxlength="64" class="input-medium"/>
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
				<th style="text-align:center">名称</th>
				<th style="text-align:center">高度</th>
				<th style="text-align:center">宽度</th>
				<shiro:hasPermission name="cms:adPosition:edit"><th style="text-align:center">操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="adPosition">
			<tr>
				<td style="text-align:center"><a href="${ctx}/adPosition/query?id=${adPosition.id}">
					${adPosition.positionNo}
				</a></td>
				<td style="text-align:center">
					${adPosition.positionName}
				</td>
				<td style="text-align:center">
					${adPosition.height}
				</td>
				<td style="text-align:center">
					${adPosition.width}
				</td>
				<td  style="text-align:center">
				<shiro:hasPermission name="cms:adPosition:edit">
    				<a href="${ctx}/adPosition/update?id=${adPosition.id}">修改</a>
					<a href="${ctx}/adPosition/delete?id=${adPosition.id}" onclick="return confirmx('确认要删除该广告位置吗？', this.href)">删除</a>
				</shiro:hasPermission>
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>