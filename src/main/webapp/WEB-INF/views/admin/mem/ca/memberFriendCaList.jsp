<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>信用报告申请管理</title>
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
		<li class="active"><a href="${ctx}/ca/memberFriendCa/">信用报告申请列表</a></li>	
	</ul>
	<form:form id="searchForm" modelAttribute="memberFriendCa" action="${ctx}/ca/memberFriendCa/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label style="text-align:right;width:150px">会员id：</label>
				<form:input path="memberId" htmlEscape="false" maxlength="20" class="input-medium"/>
			</li>
			<li><label style="text-align:right;width:150px">好友id：</label>
				<form:input path="friendId" htmlEscape="false" maxlength="20" class="input-medium"/>
			</li>
			<li><label style="text-align:right;width:150px">方向：</label>
				<form:select path="drc"  style="width:177px" class="input-medium">
					<form:option value="" label="请选择"/>
					<form:options items="${fns:getDictList('caApplyDrc')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</li>
			<li><label style="text-align:right;width:150px">类型：</label>
				<form:select path="type"  style="width:177px" class="input-medium">
					<form:option value="" label="请选择"/>
					<form:options items="${fns:getDictList('')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</li>
			<li><label style="text-align:right;width:150px">状态：</label>
				<form:select path="status"  style="width:177px" class="input-medium">
					<form:option value="" label="请选择"/>
					<form:options items="${fns:getDictList('')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
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
				<th style="text-align:center">会员id</th>
				<th style="text-align:center">好友id</th>
				<th style="text-align:center">方向</th>
				<th style="text-align:center">类型</th>
				<th style="text-align:center">状态</th>
				<th style="text-align:center">备注</th>
				<th style="text-align:center">生成时间</th>
				<shiro:hasPermission name="ca:memberFriendCa:edit"><th style="text-align:center">操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="memberFriendCa">
			<tr>
				<td style="text-align:center"><a href="${ctx}/ca/memberFriendCa/query?id=${memberFriendCa.id}">
					${memberFriendCa.memberId}
				</a></td>
				<td style="text-align:center">
					${memberFriendCa.friendId}
				</td>
				<td style="text-align:center">
					${fns:getDictLabel(memberFriendCa.drc, '', '')}
				</td>
				<td style="text-align:center">
					${fns:getDictLabel(memberFriendCa.type, '', '')}
				</td>
				<td style="text-align:center">
					${fns:getDictLabel(memberFriendCa.status, '', '')}
				</td>
				<td style="text-align:center">
					${memberFriendCa.rmk}
				</td>
				<td style="text-align:center">
					<fmt:formatDate value="${memberFriendCa.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td  style="text-align:center">
				<shiro:hasPermission name="ca:memberFriendCa:edit">
    				<a href="${ctx}/ca/memberFriendCa/update?id=${memberFriendCa.id}">修改</a>
					<a href="${ctx}/ca/memberFriendCa/delete?id=${memberFriendCa.id}" onclick="return confirmx('确认要删除该信用报告申请吗？', this.href)">删除</a>
				</shiro:hasPermission>
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>