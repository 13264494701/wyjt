<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>信用档案运营商通话与花费详情管理</title>
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
		<li class="active"><a href="${ctx}/ca/rcCaYysDetails/">信用档案运营商通话与花费详情列表</a></li>
		<shiro:hasPermission name="ca:rcCaYysDetails:edit"><li><a href="${ctx}/ca/rcCaYysDetails/add">信用档案运营商通话与花费详情添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="rcCaYysDetails" action="${ctx}/ca/rcCaYysDetails/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label style="text-align:right;width:150px">member_id：</label>
				<form:input path="memberId" htmlEscape="false" maxlength="20" class="input-medium"/>
			</li>
			<li><label style="text-align:right;width:150px">创建时间：</label>
				<input name="createTime" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${rcCaYysDetails.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"
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
				<th style="text-align:center">member_id</th>
				<th style="text-align:center">创建时间</th>
				<shiro:hasPermission name="ca:rcCaYysDetails:edit"><th style="text-align:center">操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="rcCaYysDetails">
			<tr>
				<td style="text-align:center"><a href="${ctx}/ca/rcCaYysDetails/query?id=${rcCaYysDetails.id}">
					${rcCaYysDetails.memberId}
				</a></td>
				<td style="text-align:center">
					<fmt:formatDate value="${rcCaYysDetails.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td  style="text-align:center">
				<shiro:hasPermission name="ca:rcCaYysDetails:edit">
    				<a href="${ctx}/ca/rcCaYysDetails/update?id=${rcCaYysDetails.id}">修改</a>
					<a href="${ctx}/ca/rcCaYysDetails/delete?id=${rcCaYysDetails.id}" onclick="return confirmx('确认要删除该信用档案运营商通话与花费详情吗？', this.href)">删除</a>
				</shiro:hasPermission>
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>