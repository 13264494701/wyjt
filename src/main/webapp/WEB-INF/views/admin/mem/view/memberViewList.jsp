<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>会员视图管理</title>
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
		<li class="active"><a href="${ctx}/view/memberView/">会员视图列表</a></li>		
	</ul>
	<form:form id="searchForm" modelAttribute="memberView" action="${ctx}/view/memberView/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label style="text-align:right;width:150px">用户名：</label>
				<form:input path="member.username" htmlEscape="false" maxlength="11" class="input-medium"/>
			</li>
			<li class="btns"><label style="text-align:right;width:50px"></label><input id="btnSubmit" class="btn btn-primary " style="width:80px;" type="submit" value="查   询"/></li>
			<li><a href="${ctx}/view/memberView/refresh" class="btn btn-primary" style="width:80px;" >刷新</a></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th style="text-align:center">会员编号</th>
				<th style="text-align:center">用户名</th>
				<th style="text-align:center">姓名</th>
				<th style="text-align:center">好友数量</th>
				<th style="text-align:center">状态</th>
				<th style="text-align:center">生成时间</th>
				<th style="text-align:center">修改时间</th>
				<shiro:hasPermission name="view:memberView:edit"><th style="text-align:center">操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="memberView">
			<tr>
				<td style="text-align:center"><a href="${ctx}/member/query?id=${memberView.member.id}">
					${memberView.member.id}
				</a></td>
				<td style="text-align:center">
					${memberView.member.username}
				</td>
				<td style="text-align:center">
					${memberView.member.name}
				</td>
				<td style="text-align:center">
					${memberView.friendQuantity}
				</td>
				<td style="text-align:center">
					${memberView.status}
				</td>
				<td style="text-align:center">
					<fmt:formatDate value="${memberView.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td style="text-align:center">
					<fmt:formatDate value="${memberView.updateTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td  style="text-align:center">
				<shiro:hasPermission name="view:memberView:edit">
    				<a href="${ctx}/view/memberView/take?id=${memberView.id}">接管</a>
    				<a href="${ctx}/view/memberView/reset?id=${memberView.id}">复位</a>
				</shiro:hasPermission>
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>