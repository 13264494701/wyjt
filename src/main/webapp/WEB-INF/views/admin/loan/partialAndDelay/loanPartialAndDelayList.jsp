<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>部分还款和延期管理</title>
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
		<li class="active"><a href="${ctx}/loanPartialAndDelay/">部分还款和延期列表</a></li>		
	</ul>
	<form:form id="searchForm" modelAttribute="nfsLoanPartialAndDelay" action="${ctx}/loanPartialAndDelay/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label style="text-align:right;width:150px">申请人ID：</label>
				<form:input path="member.id" htmlEscape="false" maxlength="20" class="input-medium"/>
			</li>
			<li><label style="text-align:right;width:150px">申请人姓名(精确查)：</label>
				<form:input path="member.name" htmlEscape="false" maxlength="10" class="input-medium"/>
			</li>
			<li><label style="text-align:right;width:150px">申请状态：</label>
				<form:select path="status"  style="width:177px" class="input-medium">
					<form:option value="" label="请选择"/>
					<form:options items="${fns:getDictList('partialAndDelayStatus')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</li>
			<li><label style="text-align:right;width:80px">创建时间：</label>
			<input name="beginTime" type="text"  maxlength="20" class="input-medium Wdate"
				value="<fmt:formatDate value="${beginTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"
				onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:true});"/> - 
			<input name="endTime" type="text"  maxlength="20" class="input-medium Wdate"
				value="<fmt:formatDate value="${endTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"
				onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:true});"/>
			</li>
			<li class="btns"><label style="text-align:right;width:50px"></label><input id="btnSubmit" class="btn btn-primary " style="width:80px;" type="submit" value="查   询"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th style="text-align:center">申请人ID</th>
				<th style="text-align:center">申请人姓名</th>
				<th style="text-align:center">申请人角色</th>
				<th style="text-align:center">借条ID</th>
				<th style="text-align:center">状态</th>
				<th style="text-align:center">申请时间</th>
				<shiro:hasPermission name="loan:loanPartialAndDelay:edit"><th style="text-align:center">操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="loanPartialAndDelay">
			<tr>
				<td style="text-align:center"><a href="${admin}/member/query?id=${loanPartialAndDelay.member.id}">
					${loanPartialAndDelay.member.id}
				</a></td>
				<td style="text-align:center">
					${loanPartialAndDelay.member.name}
				</td>
				<td style="text-align:center">
					${loanPartialAndDelay.memberRole}
				</td>
				<td style="text-align:center"><a href="${ctx}/nfsLoanRecord/query?id=${loanPartialAndDelay.loan.id}">
					${loanPartialAndDelay.loan.id}
				</a></td>
				<td style="text-align:center">
					${fns:getDictLabel(loanPartialAndDelay.status, 'partialAndDelayStatus', '')}
				</td>
				<td style="text-align:center">
					<fmt:formatDate value="${loanPartialAndDelay.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td  style="text-align:center">
				<shiro:hasPermission name="loan:loanPartialAndDelay:edit">
    				<a href="${ctx}/loanPartialAndDelay/update?id=${loanPartialAndDelay.id}">修改</a>
					<a href="${ctx}/loanPartialAndDelay/delete?id=${loanPartialAndDelay.id}" onclick="return confirmx('确认要删除该部分还款和延期吗？', this.href)">删除</a>
				</shiro:hasPermission>
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>