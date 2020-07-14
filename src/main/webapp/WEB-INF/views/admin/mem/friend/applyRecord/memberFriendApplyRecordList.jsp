<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>好友申请管理</title>
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
		<li class="active"><a href="${ctx}/mem/memberFriendApplyRecord/">好友申请列表</a></li>
		<shiro:hasPermission name="mem:memberFriendApplyRecord:edit"><li><a href="${ctx}/mem/memberFriendApplyRecord/add">好友申请添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="memberFriendApplyRecord" action="${ctx}/mem/memberFriendApplyRecord/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label style="text-align:right;width:150px">会员用户名(登录电话号)：</label>
				<form:input path="username" htmlEscape="false" maxlength="64" class="input-medium"/>
			</li>
			<li><label style="text-align:right;width:150px">会员昵称：</label>
				<form:input path="nickname" htmlEscape="false" maxlength="64" class="input-medium"/>
			</li>
			<li><label style="text-align:right;width:150px">会员用户名(登录电话号)：</label>
				<form:input path="friendUsername" htmlEscape="false" maxlength="64" class="input-medium"/>
			</li>
			<li><label style="text-align:right;width:150px">会员昵称：</label>
				<form:input path="friendNickname" htmlEscape="false" maxlength="64" class="input-medium"/>
			</li>
			<li><label style="text-align:right;width:150px">申请状态   0：申请中，1：已同意，2：已拒绝：</label>
				<form:select path="status"  style="width:177px" class="input-medium">
					<form:option value="" label="请选择"/>
					<form:options items="${fns:getDictList('')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</li>
			<li><label style="text-align:right;width:150px">0 免费申请 1一元信用档案：</label>
				<form:radiobuttons path="freeType" items="${fns:getDictList('')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
			</li>
			<li class="btns"><label style="text-align:right;width:50px"></label><input id="btnSubmit" class="btn btn-primary " style="width:80px;" type="submit" value="查   询"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th style="text-align:center">会员用户名(登录电话号)</th>
				<th style="text-align:center">会员昵称</th>
				<th style="text-align:center">会员用户名(登录电话号)</th>
				<th style="text-align:center">会员昵称</th>
				<th style="text-align:center">申请状态   0：申请中，1：已同意，2：已拒绝</th>
				<th style="text-align:center">0 免费申请 1一元信用档案</th>
				<shiro:hasPermission name="mem:memberFriendApplyRecord:edit"><th style="text-align:center">操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="memberFriendApplyRecord">
			<tr>
				<td style="text-align:center"><a href="${ctx}/mem/memberFriendApplyRecord/query?id=${memberFriendApplyRecord.id}">
					${memberFriendApplyRecord.username}
				</a></td>
				<td style="text-align:center">
					${memberFriendApplyRecord.nickname}
				</td>
				<td style="text-align:center">
					${memberFriendApplyRecord.friendUsername}
				</td>
				<td style="text-align:center">
					${memberFriendApplyRecord.friendNickname}
				</td>
				<td style="text-align:center">
					${fns:getDictLabel(memberFriendApplyRecord.status, '', '')}
				</td>
				<td style="text-align:center">
					${fns:getDictLabel(memberFriendApplyRecord.freeType, '', '')}
				</td>
				<td  style="text-align:center">
				<shiro:hasPermission name="mem:memberFriendApplyRecord:edit">
    				<a href="${ctx}/mem/memberFriendApplyRecord/update?id=${memberFriendApplyRecord.id}">修改</a>
					<a href="${ctx}/mem/memberFriendApplyRecord/delete?id=${memberFriendApplyRecord.id}" onclick="return confirmx('确认要删除该好友申请吗？', this.href)">删除</a>
				</shiro:hasPermission>
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>