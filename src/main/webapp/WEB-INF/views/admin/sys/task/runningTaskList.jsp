<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>定时任务管理</title>
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
		<li><a href="${ctx}/task/list">任务列表</a></li>
		<li class="active"><a href="${ctx}/task/runningTasks">运行中的任务</a></li>
		<li><a href="${ctx}/task/add">任务添加</a></li>
	</ul>
<%-- 	<form:form id="searchForm" modelAttribute="nfsBrnAct" action="${ctx}/brn/nfsBrnAct/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li class="clearfix"></li>
			<li><label>机构号：</label>
			<sys:treeselect id="brnNo" name="brnNo" value="${nfsBrnAct.id}" labelName="brn.name" labelValue="${user.brn.code}"
				title="部门" url="/sys/brn/treeData?type=2" cssClass="required" allowClear="true" notAllowSelectParent="true"/>
			</li>
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>
		</ul>
	</form:form> --%>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th style="text-align:center">任务名</th>
				<th style="text-align:center">任务组</th>
				<th style="text-align:center">cron表达式</th>
				<th style="text-align:center">状态</th>
				<th style="text-align:center">描述</th>
				<th style="text-align:center">要执行的任务类路径名</th>
				<th style="text-align:center">操作</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${tasks}" var="task">
			<tr>
<%-- 				<td  style="text-align:center"><a href="${ctx}/brn/nfsBrnAct/form?id=${nfsBrnAct.id}">
					${nfsBrnAct.brnNo}
				</a></td> --%>
				<td  style="text-align:center">
					${task.name}
				</td>
				<td  style="text-align:center">
					${task.group}
				</td>
				<td  style="text-align:center">
					${task.cronExpression}
				</td>
				<td  style="text-align:center">
					${task.status}	
				</td>
				<td  style="text-align:center">
					${task.description}
				</td>
				<td  style="text-align:center">
					${task.className}
				</td>
				<td  style="text-align:center">
					<c:choose>
					<c:when test="${task.status == 'NORMAL'}">
						<a href="${ctx}/task/stop?name=${task.name}&group=${task.group}"  onclick="return confirmx('确认要暂停该任务吗？', this.href)">暂停</a>
					</c:when>
					<c:otherwise>
						<a href="${ctx}/task/resume?name=${task.name}&group=${task.group}"  onclick="return confirmx('确认要恢复该任务吗？', this.href)">恢复</a>
					</c:otherwise>
					</c:choose>
					<a href="${ctx}/task/startNow?name=${task.name}&group=${task.group}"  onclick="return confirmx('确认要执行吗？', this.href)">立即执行一次</a>
					<a href="${ctx}/task/update?name=${task.name}&group=${task.group}" >修改</a>
					<a href="${ctx}/task/delete?name=${task.name}&group=${task.group}"  onclick="return confirmx('确认要执行吗？', this.href)">删除</a>
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
<%-- 	<div class="pagination">${page}</div> --%>
</body>
</html>