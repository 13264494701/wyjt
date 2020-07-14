<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>收费规则管理</title>
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
		<li class="active"><a href="${ctx}/nfsFeeRule/">收费规则列表</a></li>
		<shiro:hasPermission name="fee:nfsFeeRule:edit"><li><a href="${ctx}/nfsFeeRule/add">收费规则添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="nfsFeeRule" action="${ctx}/nfsFeeRule/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label style="text-align:right;width:150px">业务代码：</label>
				<form:input path="trxCode" htmlEscape="false" maxlength="5" class="input-medium"/>
			</li>
			<li class="btns"><label style="text-align:right;width:50px"></label><input id="btnSubmit" class="btn btn-primary " style="width:80px;" type="submit" value="查   询"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th style="text-align:center">业务代码</th>
				<th style="text-align:center">收费规则表达式</th>
				<th style="text-align:center">创建时间</th>
				<shiro:hasPermission name="fee:nfsFeeRule:edit"><th style="text-align:center">操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="nfsFeeRule">
			<tr>
				<td style="text-align:center">
					${nfsFeeRule.trxCode}
				</td>
				<td style="text-align:center">
					${nfsFeeRule.expression}
				</td>
				<td style="text-align:center">
					<fmt:formatDate value="${nfsFeeRule.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td  style="text-align:center">
				<shiro:hasPermission name="fee:nfsFeeRule:edit">
    				<a href="${ctx}/nfsFeeRule/update?id=${nfsFeeRule.id}">修改</a>
					<a href="${ctx}/nfsFeeRule/delete?id=${nfsFeeRule.id}" onclick="return confirmx('确认要删除该收费规则吗？', this.href)">删除</a>
				</shiro:hasPermission>
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>