<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>账户科目管理</title>
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
		<li class="active"><a href="${ctx}/nfsActSub/">账户科目列表</a></li>
		<shiro:hasPermission name="sub:nfsActSub:edit"><li><a href="${ctx}/nfsActSub/add">账户科目添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="nfsActSub" action="${ctx}/nfsActSub/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label style="text-align:right;width:150px">业务角色：</label>
				<form:select path="trxRole"  style="width:177px" class="input-medium">
					<form:option value="" label="请选择"/>
					<form:options items="${fns:getDictList('trxRole')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</li>
			<li><label style="text-align:right;width:150px">科目编号：</label>
				<form:input path="subNo" htmlEscape="false" maxlength="4" class="input-medium"/>
			</li>
			<li><label style="text-align:right;width:150px">科目名称：</label>
				<form:input path="name" htmlEscape="false" maxlength="32" class="input-medium"/>
			</li>
			<li class="btns"><label style="text-align:right;width:50px"></label><input id="btnSubmit" class="btn btn-primary " style="width:80px;" type="submit" value="查   询"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th style="text-align:center">业务角色</th>
				<th style="text-align:center">科目编号</th>
				<th style="text-align:center">科目名称</th>
				<th style="text-align:center">创建时间</th>
				<shiro:hasPermission name="sub:nfsActSub:edit"><th style="text-align:center">操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="nfsActSub">
			<tr>
				<td style="text-align:center">
					${fns:getDictLabel(nfsActSub.trxRole, 'trxRole', '')}
				</td>
				<td style="text-align:center">
					${nfsActSub.subNo}
				</td>
				<td style="text-align:center">
					${nfsActSub.name}
				</td>
				<td style="text-align:center">
					<fmt:formatDate value="${nfsActSub.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td  style="text-align:center">
				<shiro:hasPermission name="sub:nfsActSub:edit">
    				<a href="${ctx}/nfsActSub/update?id=${nfsActSub.id}">修改</a>
					<a href="${ctx}/nfsActSub/delete?id=${nfsActSub.id}" onclick="return confirmx('确认要删除该账户科目吗？', this.href)">删除</a>
				</shiro:hasPermission>
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>