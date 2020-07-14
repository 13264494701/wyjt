<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>好友投诉管理</title>
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
		<li class="active"><a href="${ctx}/memberFriendReport/">好友投诉列表</a></li>	
	</ul>
	<form:form id="searchForm" modelAttribute="memberFriendReport" action="${ctx}/memberFriendReport/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label style="text-align:right;width:150px">投诉人手机号：</label>
				<form:input path="member.username" htmlEscape="false" maxlength="20" class="input-medium"/>
			</li>
			<li><label style="text-align:right;width:150px">被投诉人手机号：</label>
				<form:input path="friend.username" htmlEscape="false" maxlength="20" class="input-medium"/>
			</li>
			<li><label style="text-align:right;width:150px">投诉类型：</label>
			    <form:select path="type"  style="width:177px" class="input-medium">
					<form:option value="" label="请选择"/>
					<form:options items="${fns:getDictList('reportType')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</li>
			<li><label style="text-align:right;width:150px">投诉类型：</label>
			    <form:select path="status"  style="width:177px" class="input-medium">
					<form:option value="" label="请选择"/>
					<form:options items="${fns:getDictList('reportStatus')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</li>
			<li><label style="text-align:right;width:150px">投诉标题：</label>
				<form:input path="title" htmlEscape="false" maxlength="64" class="input-medium"/>
			</li>
			<li class="btns"><label style="text-align:right;width:50px"></label><input id="btnSubmit" class="btn btn-primary " style="width:80px;" type="submit" value="查   询"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
			    <th style="text-align:center">投诉编号</th>
				<th style="text-align:center">投诉人</th>
				<th style="text-align:center">投诉人手机号</th>
				<th style="text-align:center">被投诉人</th>
				<th style="text-align:center">被投诉人手机号</th>
				<th style="text-align:center">投诉类型</th>
				<th style="text-align:center">投诉标题</th>
				<th style="text-align:center">投诉时间</th>
				<th style="text-align:center">投诉状态</th>
				<shiro:hasPermission name="report:memberFriendReport:edit"><th style="text-align:center">操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="memberFriendReport">
			<tr>
			    <td style="text-align:center">
					${memberFriendReport.id}
				</td>
			    <td style="text-align:center">
					${memberFriendReport.member.name}
				</td>
				<td style="text-align:center"><a href="${admin}/member/query?id=${memberFriendReport.member.id}">
					${memberFriendReport.member.username}
				</a></td>
				<td style="text-align:center">
					${memberFriendReport.friend.name}
				</td>
				<td style="text-align:center"><a href="${admin}/member/query?id=${memberFriendReport.friend.id}">
					${memberFriendReport.friend.username}
				</a></td>
				<td style="text-align:center">
					${fns:getDictLabel(memberFriendReport.type, 'reportType', '')}
				</td>
				<td style="text-align:center">
					${memberFriendReport.title}
				</td>
				<td style="text-align:center">
					<fmt:formatDate value="${memberFriendReport.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td style="text-align:center">
					${fns:getDictLabel(memberFriendReport.status, 'reportStatus', '')}
				</td>
				<td  style="text-align:center">
				<shiro:hasPermission name="report:memberFriendReport:edit">
    				<a href="${ctx}/memberFriendReport/query?id=${memberFriendReport.id}">查看</a>	
    				<a href="${ctx}/memberFriendReport/update?id=${memberFriendReport.id}">处理</a>				
				</shiro:hasPermission>
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>