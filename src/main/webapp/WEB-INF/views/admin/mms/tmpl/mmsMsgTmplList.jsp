<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>消息模板管理</title>
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
		<li class="active"><a href="${ctx}/msgTmpl/">消息模板列表</a></li>
		<shiro:hasPermission name="tmpl:mmsMsgTmpl:edit"><li><a href="${ctx}/msgTmpl/add">消息模板添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="mmsMsgTmpl" action="${ctx}/msgTmpl/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label style="text-align:right;width:150px">模板识别码：</label>
				<form:input path="code" htmlEscape="false" maxlength="32" class="input-medium"/>
			</li>
			<li><label style="text-align:right;width:150px">消息模板名称：</label>
				<form:input path="name" htmlEscape="false" maxlength="64" class="input-medium"/>
			</li>
			<li><label style="text-align:right;width:150px">模板类型：</label>
				<form:select path="type"  style="width:177px" class="input-medium">
					<form:option value="" label="请选择"/>
					<form:options items="${fns:getDictList('msgTmplType')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
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
				<th style="text-align:center">模板识别码</th>
				<th style="text-align:center">消息模板名称</th>
				<th style="text-align:center">模板类型</th>
				<th style="text-align:center">模板内容</th>
				<th style="text-align:center">创建时间</th>
				<shiro:hasPermission name="tmpl:mmsMsgTmpl:edit"><th style="text-align:center">操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="mmsMsgTmpl">
			<tr>
				<td style="text-align:center"><a href="${ctx}/msgTmpl/query?id=${mmsMsgTmpl.id}">
					${mmsMsgTmpl.code}
				</a></td>
				<td style="text-align:center">
					${mmsMsgTmpl.name}
				</td>
				<td style="text-align:center">
					${fns:getDictLabel(mmsMsgTmpl.type, 'msgTmplType', '')}
				</td>
				<td style="text-align:center">
					${mmsMsgTmpl.content}
				</td>
				<td style="text-align:center">
					<fmt:formatDate value="${mmsMsgTmpl.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td  style="text-align:center">
				<shiro:hasPermission name="tmpl:mmsMsgTmpl:edit">
    				<a href="${ctx}/msgTmpl/update?id=${mmsMsgTmpl.id}">修改</a>
					<a href="${ctx}/msgTmpl/delete?id=${mmsMsgTmpl.id}" onclick="return confirmx('确认要删除该消息模板吗？', this.href)">删除</a>
				</shiro:hasPermission>
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>