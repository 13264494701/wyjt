<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>站点信息管理</title>
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
		<li class="active"><a href="${ctx}/cms/cmsSiteInfo/">站点信息列表</a></li>
		<shiro:hasPermission name="cms:cmsSiteInfo:edit"><li><a href="${ctx}/cms/cmsSiteInfo/add">站点信息添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="cmsSiteInfo" action="${ctx}/cms/cmsSiteInfo/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label style="text-align:right;width:150px">引用名称：</label>
				<form:input path="key" htmlEscape="false" maxlength="100" class="input-medium"/>
			</li>
			<li class="btns"><label style="text-align:right;width:50px"></label><input id="btnSubmit" class="btn btn-primary " style="width:80px;" type="submit" value="查   询"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th style="text-align:center">引用名称</th>
				<th style="text-align:center">显示内容</th>
				<th style="text-align:center">创建时间</th>
				<shiro:hasPermission name="cms:cmsSiteInfo:edit"><th style="text-align:center">操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="cmsSiteInfo">
			<tr>
				<td style="text-align:center"><a href="${ctx}/cms/cmsSiteInfo/query?id=${cmsSiteInfo.id}">
					${cmsSiteInfo.key}
				</a></td>
				<td style="text-align:center">
					<c:choose>
					   <c:when test="${fn:length(cmsSiteInfo.value)>40}">
		                 <span>${fn:substring(cmsSiteInfo.value,0,40)}...</span>
		        	   </c:when>
		        	   <c:otherwise>
		        	    <span>${cmsSiteInfo.value}</span>
		        	   </c:otherwise>
					</c:choose>
				</td>
				<td style="text-align:center">
					<fmt:formatDate value="${cmsSiteInfo.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td  style="text-align:center">
				<shiro:hasPermission name="cms:cmsSiteInfo:edit">
    				<a href="${ctx}/cms/cmsSiteInfo/update?id=${cmsSiteInfo.id}">修改</a>
					<a href="${ctx}/cms/cmsSiteInfo/delete?id=${cmsSiteInfo.id}" onclick="return confirmx('确认要删除该站点信息吗？', this.href)">删除</a>
				</shiro:hasPermission>
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>