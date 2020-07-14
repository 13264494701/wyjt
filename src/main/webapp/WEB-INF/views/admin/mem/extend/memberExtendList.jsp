<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>会员扩展信息管理</title>
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
		<li class="active"><a href="${ctx}/mem/memberExtend/">会员扩展信息列表</a></li>
		<shiro:hasPermission name="mem:memberExtend:edit"><li><a href="${ctx}/mem/memberExtend/add">会员扩展信息添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="memberExtend" action="${ctx}/mem/memberExtend/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label style="text-align:right;width:150px">邮箱：</label>
				<form:input path="email" htmlEscape="false" maxlength="64" class="input-medium"/>
			</li>
			<li class="btns"><label style="text-align:right;width:50px"></label><input id="btnSubmit" class="btn btn-primary " style="width:80px;" type="submit" value="查   询"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th style="text-align:center">邮箱</th>
				<th style="text-align:center">性别</th>
				<th style="text-align:center">民族</th>
				<shiro:hasPermission name="mem:memberExtend:edit"><th style="text-align:center">操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="memberExtend">
			<tr>
				<td style="text-align:center"><a href="${ctx}/mem/memberExtend/query?id=${memberExtend.id}">
					${memberExtend.email}
				</a></td>
				<td style="text-align:center">
					${fns:getDictLabel(memberExtend.sex, '', '')}
				</td>
				<td style="text-align:center">
					${memberExtend.nation}
				</td>
				<td  style="text-align:center">
				<shiro:hasPermission name="mem:memberExtend:edit">
    				<a href="${ctx}/mem/memberExtend/update?id=${memberExtend.id}">修改</a>
					<a href="${ctx}/mem/memberExtend/delete?id=${memberExtend.id}" onclick="return confirmx('确认要删除该会员扩展信息吗？', this.href)">删除</a>
				</shiro:hasPermission>
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>