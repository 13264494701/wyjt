<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>频道信息管理</title>
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
		<li class="active"><a href="${ctx}/cms/channel/">频道列表</a></li>
		<shiro:hasPermission name="cms:channel:edit"><li><a href="${ctx}/cms/channel/add">频道添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="cmsChannel" action="${ctx}/cms/channel/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label style="text-align:right;width:150px">频道名称：</label>
				<form:input path="name" htmlEscape="false" maxlength="64" class="input-medium"/>
			</li>
			<li class="btns"><label style="text-align:right;width:50px"></label><input id="btnSubmit" class="btn btn-primary " style="width:80px;" type="submit" value="查   询"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th style="text-align:center">频道名称</th>
				<th style="text-align:center">频道别名</th>
				<th style="text-align:center">排序</th>
				<th style="text-align:center">是否导航显示</th>
				<th style="text-align:center">是否允许评论</th>
				<th style="text-align:center">是否需要审核</th>
				<th style="text-align:center">创建时间</th>
				<shiro:hasPermission name="cms:channel:edit"><th style="text-align:center">操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="cmsChannel">
			<tr>
				<td style="text-align:center"><a href="${ctx}/cms/channel/query?id=${cmsChannel.id}">
					${cmsChannel.name}
				</a></td>
				<td style="text-align:center">
					${cmsChannel.alias}
				</td>
				<td style="text-align:center">
					${cmsChannel.sort}
				</td>
				<td style="text-align:center">
					${fns:getDictLabel(cmsChannel.inNav, 'trueOrFalse', '')}
				</td>
				<td style="text-align:center">
					${fns:getDictLabel(cmsChannel.allowComment, 'trueOrFalse', '')}
				</td>
				<td style="text-align:center">
					${fns:getDictLabel(cmsChannel.isAudit, 'trueOrFalse', '')}
				</td>
				<td style="text-align:center">
					<fmt:formatDate value="${cmsChannel.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td  style="text-align:center">
				<shiro:hasPermission name="cms:channel:edit">
    				<a href="${ctx}/cms/channel/update?id=${cmsChannel.id}">修改</a>
					<a href="${ctx}/cms/channel/delete?id=${cmsChannel.id}" onclick="return confirmx('确认要删除该频道信息吗？', this.href)">删除</a>
				</shiro:hasPermission>
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>