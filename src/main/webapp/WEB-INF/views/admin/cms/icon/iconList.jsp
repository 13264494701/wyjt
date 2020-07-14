<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>图标管理</title>
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
		<li class="active"><a href="${ctx}/cms/icon/">图标列表</a></li>
		<shiro:hasPermission name="cms:icon:edit"><li><a href="${ctx}/cms/icon/add">图标添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="cmsIcon" action="${ctx}/cms/icon/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
		     <li><label style="text-align:right;width:150px">图标位置：</label>
			   <form:select path="positionNo"  style="width:177px" class="input-medium">
				   <form:option value="" label="请选择"/>
				   <form:options items="${fns:getIconPositionList()}" itemLabel="positionName" itemValue="positionNo" htmlEscape="false"/>
			   </form:select>
		    </li>
		    <li><label style="text-align:right;width:150px">图标名称：</label>
				<form:input path="iconName" htmlEscape="false" maxlength="64" class="input-medium"/>
		    </li>

			<li class="btns"><label style="text-align:right;width:50px"></label><input id="btnSubmit" class="btn btn-primary " style="width:80px;" type="submit" value="查   询"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th style="text-align:center">图标位置</th>
				<th style="text-align:center">图标名称</th>
				<th style="text-align:center">跳转方式</th>
				<shiro:hasPermission name="cms:icon:edit"><th style="text-align:center">操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="icon">
			<tr>
				<td style="text-align:center"><a href="${ctx}/cms/icon/query?id=${icon.id}">
					${fns:getNameByIconPositionNo(icon.positionNo)}
				</a></td>
				<td style="text-align:center">
					${icon.iconName}
				</td>
				<td style="text-align:center">
					${fns:getDictLabel(icon.redirectType, 'redirectType', '')}
				</td>
				<td  style="text-align:center">
				<shiro:hasPermission name="cms:icon:edit">
    				<a href="${ctx}/cms/icon/update?id=${icon.id}">修改</a>
					<a href="${ctx}/cms/icon/delete?id=${icon.id}" onclick="return confirmx('确认要删除该图标吗？', this.href)">删除</a>
				</shiro:hasPermission>
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>