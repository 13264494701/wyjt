<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>实名认证管理</title>
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
		<li class="active"><a href="${ctx}/verified/memberVerified/">实名认证列表</a></li>
		<shiro:hasPermission name="verified:memberVerified:edit"><li><a href="${ctx}/verified/memberVerified/add">实名认证添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="memberVerified" action="${ctx}/verified/memberVerified/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label style="text-align:right;width:150px">会员编号：</label>
				<form:input path="memberId" htmlEscape="false" maxlength="20" class="input-medium"/>
			</li>
			<li><label style="text-align:right;width:150px">真实姓名：</label>
				<form:input path="realName" htmlEscape="false" maxlength="64" class="input-medium"/>
			</li>
			<li><label style="text-align:right;width:150px">身份证号：</label>
				<form:input path="idNo" htmlEscape="false" maxlength="32" class="input-medium"/>
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
				<th style="text-align:center">会员编号</th>
				<th style="text-align:center">真实姓名</th>
				<th style="text-align:center">身份证号</th>
				<th style="text-align:center">银行卡号</th>
				<th style="text-align:center">邮箱地址</th>
				<th style="text-align:center">状态</th>
				<th style="text-align:center">创建时间</th>
				<shiro:hasPermission name="verified:memberVerified:edit"><th style="text-align:center">操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="memberVerified">
			<tr>
				<td style="text-align:center"><a href="${ctx}/verified/memberVerified/query?id=${memberVerified.id}">
					${memberVerified.memberId}
				</a></td>
				<td style="text-align:center">
					${memberVerified.realName}
				</td>
				<td style="text-align:center">
					${memberVerified.idNo}
				</td>
				<td style="text-align:center">
					${memberVerified.cardNo}
				</td>
				<td style="text-align:center">
					${memberVerified.email}
				</td>
				<td style="text-align:center">
					${fns:getDictLabel(memberVerified.status, '', '')}
				</td>
				<td style="text-align:center">
					<fmt:formatDate value="${memberVerified.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td  style="text-align:center">
				<shiro:hasPermission name="verified:memberVerified:edit">
    				<a href="${ctx}/verified/memberVerified/update?id=${memberVerified.id}">修改</a>
					<a href="${ctx}/verified/memberVerified/delete?id=${memberVerified.id}" onclick="return confirmx('确认要删除该实名认证吗？', this.href)">删除</a>
				</shiro:hasPermission>
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>