<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>会员额度管理</title>
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
		<li class="active"><a href="${ctx}/quota/quota/rcQuota/">会员额度列表</a></li>
		<shiro:hasPermission name="quota:quota:rcQuota:edit"><li><a href="${ctx}/quota/quota/rcQuota/add">会员额度添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="rcQuota" action="${ctx}/quota/quota/rcQuota/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label style="text-align:right;width:150px">会员编号：</label>
				<form:input path="memberId" htmlEscape="false" maxlength="20" class="input-medium"/>
			</li>
			<li><label style="text-align:right;width:150px">手机号：</label>
				<form:input path="phoneNo" htmlEscape="false" maxlength="11" class="input-medium"/>
			</li>
			<li><label style="text-align:right;width:150px">会员姓名：</label>
				<form:input path="name" htmlEscape="false" maxlength="255" class="input-medium"/>
			</li>
			<li class="btns"><label style="text-align:right;width:50px"></label><input id="btnSubmit" class="btn btn-primary " style="width:80px;" type="submit" value="查   询"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th style="text-align:center">会员编号</th>
				<th style="text-align:center">手机号</th>
				<th style="text-align:center">身份证号</th>
				<th style="text-align:center">会员姓名</th>
				<th style="text-align:center">额度</th>
				<th style="text-align:center">综合评分</th>
				<shiro:hasPermission name="quota:quota:rcQuota:edit"><th style="text-align:center">操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="rcQuota">
			<tr>
				<td style="text-align:center"><a href="${ctx}/quota/quota/rcQuota/query?id=${rcQuota.id}">
					${rcQuota.memberId}
				</a></td>
				<td style="text-align:center">
					${rcQuota.phoneNo}
				</td>
				<td style="text-align:center">
					${rcQuota.idNo}
				</td>
				<td style="text-align:center">
					${rcQuota.name}
				</td>
				<td style="text-align:center">
					${rcQuota.quota}
				</td>
				<td style="text-align:center">
					${rcQuota.score}
				</td>
				<td  style="text-align:center">
				<shiro:hasPermission name="quota:quota:rcQuota:edit">
    				<a href="${ctx}/quota/quota/rcQuota/update?id=${rcQuota.id}">修改</a>
					<a href="${ctx}/quota/quota/rcQuota/delete?id=${rcQuota.id}" onclick="return confirmx('确认要删除该会员额度吗？', this.href)">删除</a>
				</shiro:hasPermission>
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>