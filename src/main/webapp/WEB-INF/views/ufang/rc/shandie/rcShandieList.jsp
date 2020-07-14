<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>闪谍报告管理</title>
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
<h1>
	    	闪谍报告查询记录
</h1>
	<form:form id="searchForm" modelAttribute="rcShandie" action="${ufang}/rcShandie" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="page.pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="page.pageSize" type="hidden" value="30"/>
		<ul class="ul-form">
			<li><label style="text-align:right;width:150px">姓名：</label>
				<form:input path="name" htmlEscape="false" maxlength="255" class="input-medium"/>
			</li>
			<li><label style="text-align:right;width:150px">手机号：</label>
				<form:input path="phoneNo" htmlEscape="false" maxlength="255" class="input-medium"/>
			</li>
			<li class="btns"><label style="text-align:right;width:50px"></label><input id="btnSubmit" class="btn btn-primary " style="width:80px;" type="submit" value="查   询"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th style="text-align:center">姓名</th>
				<th style="text-align:center">手机号</th>
				<th style="text-align:center">身份证</th>
				<th style="text-align:center">查询时间</th>
				<shiro:hasPermission name="ufang:rcShandie:edit"><th style="text-align:center">操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="rcShandie">
			<tr>
				<td style="text-align:center">
					<a href="${ufang}/rcShandie/query?id=${rcShandie.id}">
						${rcShandie.name}
					</a>
				</td>
				<td style="text-align:center">
						${rcShandie.phoneNo}
				</td>
				<td style="text-align:center">
						${rcShandie.idNo}
				</td>
				<td style="text-align:center">
						<fmt:formatDate value="${rcShandie.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td  style="text-align:center">
				<shiro:hasPermission name="ufang:rcShandie:view">
    				<a href="${ufang}/rcShandie/query?id=${rcShandie.id}">查看报告</a>
				</shiro:hasPermission>
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>