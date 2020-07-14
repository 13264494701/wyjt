<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>中智诚上报管理</title>
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
		<li class="active"><a href="${ctx}/ufang/zZCUpload/">中智诚上报列表</a></li>
		<shiro:hasPermission name="ufang:zZCUpload:edit"><li><a href="${ctx}/ufang/zZCUpload/add">中智诚上报添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="zZCUpload" action="${ctx}/ufang/zZCUpload/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label style="text-align:right;width:150px">上报类型：</label>
				<form:input path="type" htmlEscape="false" maxlength="4" class="input-medium"/>
			</li>
			<li class="btns"><label style="text-align:right;width:50px"></label><input id="btnSubmit" class="btn btn-primary " style="width:80px;" type="submit" value="查   询"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th style="text-align:center">上报类型</th>
				<th style="text-align:center">起始ID(包含)</th>
				<th style="text-align:center">结束ID(包含)</th>
				<th style="text-align:center">本次上报数量</th>
				<th style="text-align:center">创建时间</th>
				<shiro:hasPermission name="ufang:zZCUpload:edit"><th style="text-align:center">操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="zZCUpload">
			<tr>
				<td style="text-align:center"><a href="${ctx}/ufang/zZCUpload/query?id=${zZCUpload.id}">
					${zZCUpload.type}
				</a></td>
				<td style="text-align:center">
					${zZCUpload.startId}
				</td>
				<td style="text-align:center">
					${zZCUpload.endId}
				</td>
				<td style="text-align:center">
					${zZCUpload.count}
				</td>
				<td style="text-align:center">
					<fmt:formatDate value="${zZCUpload.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td  style="text-align:center">
				<shiro:hasPermission name="ufang:zZCUpload:edit">
    				<a href="${ctx}/ufang/zZCUpload/update?id=${zZCUpload.id}">修改</a>
					<a href="${ctx}/ufang/zZCUpload/delete?id=${zZCUpload.id}" onclick="return confirmx('确认要删除该中智诚上报吗？', this.href)">删除</a>
				</shiro:hasPermission>
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>