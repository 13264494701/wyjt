<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>员工账户管理</title>
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
		<li class="active"><a href="${ctx}/act/ufangUserAct/">员工账户列表</a></li>
		<shiro:hasPermission name="act:ufangUserAct:edit"><li><a href="${ctx}/act/ufangUserAct/add">员工账户添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="ufangUserAct" action="${ctx}/act/ufangUserAct/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label style="text-align:right;width:150px">用户ID：</label>
				<sys:treeselect id="user" name="user.id" value="${ufangUserAct.user.id}" labelName="user.name" labelValue="${ufangUserAct.user.name}"
					title="用户" url="/sys/office/treeData?type=3" cssClass="input-small" allowClear="true" notAllowSelectParent="true"/>
			</li>
			<li><label style="text-align:right;width:150px">员工编号：</label>
				<form:input path="empNo" htmlEscape="false" maxlength="8" class="input-medium"/>
			</li>
			<li><label style="text-align:right;width:150px">账户科目：</label>
				<form:input path="subNo" htmlEscape="false" maxlength="4" class="input-medium"/>
			</li>
			<li><label style="text-align:right;width:150px">账户状态：</label>
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
				<th style="text-align:center">用户ID</th>
				<th style="text-align:center">员工编号</th>
				<th style="text-align:center">账户科目</th>
				<th style="text-align:center">币种</th>
				<th style="text-align:center">账户余额</th>
				<th style="text-align:center">账户状态</th>
				<th style="text-align:center">创建时间</th>
				<shiro:hasPermission name="act:ufangUserAct:edit"><th style="text-align:center">操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="ufangUserAct">
			<tr>
				<td style="text-align:center"><a href="${ctx}/act/ufangUserAct/query?id=${ufangUserAct.id}">
					${ufangUserAct.user.name}
				</a></td>
				<td style="text-align:center">
					${ufangUserAct.empNo}
				</td>
				<td style="text-align:center">
					${ufangUserAct.subNo}
				</td>
				<td style="text-align:center">
					${ufangUserAct.currCode}
				</td>
				<td style="text-align:center">
					${ufangUserAct.curBal}
				</td>
				<td style="text-align:center">
					${fns:getDictLabel(ufangUserAct.status, '', '')}
				</td>
				<td style="text-align:center">
					<fmt:formatDate value="${ufangUserAct.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td  style="text-align:center">
				<shiro:hasPermission name="act:ufangUserAct:edit">
    				<a href="${ctx}/act/ufangUserAct/update?id=${ufangUserAct.id}">修改</a>
					<a href="${ctx}/act/ufangUserAct/delete?id=${ufangUserAct.id}" onclick="return confirmx('确认要删除该员工账户吗？', this.href)">删除</a>
				</shiro:hasPermission>
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>