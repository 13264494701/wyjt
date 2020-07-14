<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>机构管理</title>
<meta name="decorator" content="default" />
<%@include file="/WEB-INF/views/include/treetable.jsp"%>
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
		<li class="active"><a href="${admin}/ufang/brn/list">机构列表</a></li>
		<li><a href="${admin}/ufang/brn/add">机构添加</a></li>
	</ul>
	<form:form id="searchForm" modelAttribute="ufangBrn" action="${admin}/ufang/brn/list?role=${role}" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label style="text-align:right;width:150px">机构名称：</label>
				<form:input path="brnName" htmlEscape="false" maxlength="16" class="input-medium"/>
			</li>
			<li class="btns"><label style="text-align:right;width:50px"></label><input id="btnSubmit" class="btn btn-primary " style="width:80px;" type="submit" value="查   询"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th style="text-align:center">机构编号</th>
				<th style="text-align:center">机构名称</th>
				<th style="text-align:center">剩余免费条数</th>
				<th style="text-align:center">创建时间</th>
				<shiro:hasPermission name="ufang:brn:edit"><th style="text-align:center">操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="ufangBrn">
			<tr id="${ufangBrn.id}">
				<td style="text-align:center"><a href="${admin}/ufang/brn/query?id=${ufangBrn.id}">
					${ufangBrn.brnNo}
				</a></td>
				<td style="text-align:center">
					${ufangBrn.brnName}
				</td>
				<td style="text-align:center">
					${ufangBrn.freeData}
				</td>
				<td style="text-align:center">
					<fmt:formatDate value="${ufangBrn.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td  style="text-align:center">	
				   <shiro:hasPermission name="ufang:brn:edit">	
				    <a href="${admin}/ufang/user/list?brn.id=${ufangBrn.id}">员工列表</a>							  
    				<a href="${admin}/ufang/brn/update?id=${ufangBrn.id}">修改</a>
					<a href="${admin}/ufang/brn/delete?id=${ufangBrn.id}" onclick="return confirmx('确认要删除该数据批次吗？', this.href)">删除</a>
				  </shiro:hasPermission>
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>