<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>新颜雷达报告管理</title>
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
		<li class="active"><a href="${ctx}/ca/rcXinyan/">新颜雷达报告列表</a></li>
		<shiro:hasPermission name="ca:rcXinyan:edit"><li><a href="${ctx}/ca/rcXinyan/add">新颜雷达报告添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="rcXinyan" action="${ctx}/ca/rcXinyan/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label style="text-align:right;width:150px">授权唯一标识：</label>
				<form:input path="token" htmlEscape="false" maxlength="50" class="input-medium"/>
			</li>
			<li><label style="text-align:right;width:150px">姓名：</label>
				<form:input path="name" htmlEscape="false" maxlength="64" class="input-medium"/>
			</li>
			<li><label style="text-align:right;width:150px">身份证号码：</label>
				<form:input path="idNo" htmlEscape="false" maxlength="18" class="input-medium"/>
			</li>
			<li><label style="text-align:right;width:150px">手机号码：</label>
				<form:input path="phoneNo" htmlEscape="false" maxlength="11" class="input-medium"/>
			</li>
			<li><label style="text-align:right;width:150px">是否是自己支付的：</label>
				<form:input path="isSelfbuy" htmlEscape="false" maxlength="1" class="input-medium"/>
			</li>
			<li class="btns"><label style="text-align:right;width:50px"></label><input id="btnSubmit" class="btn btn-primary " style="width:80px;" type="submit" value="查   询"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th style="text-align:center">授权唯一标识</th>
				<th style="text-align:center">姓名</th>
				<th style="text-align:center">身份证号码</th>
				<th style="text-align:center">手机号码</th>
				<th style="text-align:center">是否是自己支付的</th>
				<shiro:hasPermission name="ca:rcXinyan:edit"><th style="text-align:center">操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="rcXinyan">
			<tr>
				<td style="text-align:center"><a href="${ctx}/ca/rcXinyan/query?id=${rcXinyan.id}">
					${rcXinyan.token}
				</a></td>
				<td style="text-align:center">
					${rcXinyan.name}
				</td>
				<td style="text-align:center">
					${rcXinyan.idNo}
				</td>
				<td style="text-align:center">
					${rcXinyan.phoneNo}
				</td>
				<td style="text-align:center">
					${rcXinyan.isSelfbuy}
				</td>
				<td  style="text-align:center">
				<shiro:hasPermission name="ca:rcXinyan:edit">
    				<a href="${ctx}/ca/rcXinyan/update?id=${rcXinyan.id}">修改</a>
					<a href="${ctx}/ca/rcXinyan/delete?id=${rcXinyan.id}" onclick="return confirmx('确认要删除该新颜雷达报告吗？', this.href)">删除</a>
				</shiro:hasPermission>
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>