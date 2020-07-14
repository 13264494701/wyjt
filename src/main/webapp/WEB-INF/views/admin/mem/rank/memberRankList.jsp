<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>会员等级管理</title>
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
		<li class="active"><a href="${ctx}/member/memberRank/">会员等级列表</a></li>
		<shiro:hasPermission name="mem:memberRank:edit"><li><a href="${ctx}/member/memberRank/add">会员等级添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="memberRank" action="${ctx}/member/memberRank/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label style="text-align:right;width:150px">等级编号：</label>
				<form:input path="rankNo" htmlEscape="false" maxlength="3" class="input-medium"/>
			</li>
			<li><label style="text-align:right;width:150px">等级名称：</label>
				<form:input path="rankName" htmlEscape="false" maxlength="64" class="input-medium"/>
			</li>
			<li class="btns"><label style="text-align:right;width:50px"></label><input id="btnSubmit" class="btn btn-primary " style="width:80px;" type="submit" value="查   询"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th style="text-align:center">等级编号</th>
				<th style="text-align:center">等级名称</th>
				<th style="text-align:center">是否默认</th>
				<th style="text-align:center">是否特殊</th>
				<shiro:hasPermission name="mem:rank:memberRank:edit"><th style="text-align:center">操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="memberRank">
			<tr>
				<td style="text-align:center"><a href="${ctx}/member/memberRank/query?id=${memberRank.id}">
					${memberRank.rankNo}
				</a></td>
				<td style="text-align:center">
					${memberRank.rankName}
				</td>
				<td style="text-align:center">
					${fns:getDictLabel(memberRank.isDefault, 'trueOrFalse', '')}
				</td>
				<td style="text-align:center">
					${fns:getDictLabel(memberRank.isSpecial, 'trueOrFalse', '')}
				</td>
				<td  style="text-align:center">
				<shiro:hasPermission name="mem:memberRank:edit">
				    <a href="${ctx}/member/memberRank/setdefault?id=${memberRank.id}">设置默认</a>
    				<a href="${ctx}/member/memberRank/update?id=${memberRank.id}">修改</a>
					<a href="${ctx}/member/memberRank/delete?id=${memberRank.id}" onclick="return confirmx('确认要删除该会员等级吗？', this.href)">删除</a>
				</shiro:hasPermission>
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>