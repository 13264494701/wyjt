<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>第三方查询流量管理</title>
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
		<li class="active"><a href="${ctx}/api/apiLoaneeData/">第三方查询流量列表</a></li>
		<shiro:hasPermission name="api:apiLoaneeData:edit"><li><a href="${ctx}/api/apiLoaneeData/add">第三方查询流量添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="apiLoaneeData" action="${ctx}/api/apiLoaneeData/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label style="text-align:right;width:150px">商户号：</label>
				<form:input path="merchantNumber" htmlEscape="false" maxlength="255" class="input-medium"/>
			</li>
			<li><label style="text-align:right;width:150px">商户订单号：由商户自定义传入的唯一且不大于32位的字符串， 生成的订单号不能带有:、_#_等特殊字符，建议使用时间戳或数字、字母、下划线等组合：</label>
				<form:input path="orderId" htmlEscape="false" maxlength="255" class="input-medium"/>
			</li>
			<li><label style="text-align:right;width:150px">第三方ip：</label>
				<form:input path="ip" htmlEscape="false" maxlength="255" class="input-medium"/>
			</li>
			<li class="btns"><label style="text-align:right;width:50px"></label><input id="btnSubmit" class="btn btn-primary " style="width:80px;" type="submit" value="查   询"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th style="text-align:center">商户号</th>
				<th style="text-align:center">商户订单号：由商户自定义传入的唯一且不大于32位的字符串， 生成的订单号不能带有:、_#_等特殊字符，建议使用时间戳或数字、字母、下划线等组合</th>
				<th style="text-align:center">本次查询数据数量</th>
				<th style="text-align:center">第三方ip</th>
				<th style="text-align:center">数据区间-起始时间</th>
				<th style="text-align:center">数据区间-结束时间</th>
				<shiro:hasPermission name="api:apiLoaneeData:edit"><th style="text-align:center">操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="apiLoaneeData">
			<tr>
				<td style="text-align:center"><a href="${ctx}/api/apiLoaneeData/query?id=${apiLoaneeData.id}">
					${apiLoaneeData.merchantNumber}
				</a></td>
				<td style="text-align:center">
					${apiLoaneeData.orderId}
				</td>
				<td style="text-align:center">
					${apiLoaneeData.count}
				</td>
				<td style="text-align:center">
					${apiLoaneeData.ip}
				</td>
				<td style="text-align:center">
					<fmt:formatDate value="${apiLoaneeData.startTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td style="text-align:center">
					<fmt:formatDate value="${apiLoaneeData.endTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td  style="text-align:center">
				<shiro:hasPermission name="api:apiLoaneeData:edit">
    				<a href="${ctx}/api/apiLoaneeData/update?id=${apiLoaneeData.id}">修改</a>
					<a href="${ctx}/api/apiLoaneeData/delete?id=${apiLoaneeData.id}" onclick="return confirmx('确认要删除该第三方查询流量吗？', this.href)">删除</a>
				</shiro:hasPermission>
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>