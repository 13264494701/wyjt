<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>手机通讯录管理</title>
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
		<li class="active"><a href="${ctx}/memberPhonebook/">手机通讯录列表</a></li>
	</ul>
	<form:form id="searchForm" modelAttribute="memberPhonebook" action="${ctx}/memberPhonebook/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label style="text-align:right;width:150px">会员编号：</label>
				<form:input path="member.id" htmlEscape="false" maxlength="20" class="input-medium"/>
			</li>
			<li><label style="text-align:right;width:150px">手机号码：</label>
				<form:input path="member.username" htmlEscape="false" maxlength="11" class="input-medium"/>
			</li>
			<li class="btns"><label style="text-align:right;width:50px"></label><input id="btnSubmit" class="btn btn-primary " style="width:80px;" type="submit" value="查   询"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed" style="word-break:break-all; word-wrap:break-all;margin:50px;">
		<thead>
			<tr>
				<th style="text-align:center">会员编号</th>
				<th style="text-align:center">用户名</th>
				<th style="text-align:center">手机号列表</th>
				<th style="text-align:center">通讯录</th>
				<th style="text-align:center">创建时间</th>				
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="memberPhonebook">
			<tr>
				<td style="text-align:center">
					${memberPhonebook.member.id}
				</td>
				<td style="text-align:center">
					${memberPhonebook.member.username}
				</td>
				<td style="text-align:center">
					${memberPhonebook.phoneList}
				</td>
				<td style="text-align:center">
					${memberPhonebook.phoneBook}
				</td>
				<td style="text-align:center">
					<fmt:formatDate value="${memberPhonebook.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>