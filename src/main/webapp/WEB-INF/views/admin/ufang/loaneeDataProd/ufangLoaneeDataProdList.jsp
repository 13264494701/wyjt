<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>借款人数据产品表管理</title>
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
		<li class="active"><a href="${ctx}/ufang/ufangLoaneeDataProd/">借款人数据产品表列表</a></li>
		<shiro:hasPermission name="ufang:ufangLoaneeDataProd:edit"><li><a href="${ctx}/ufang/ufangLoaneeDataProd/add">借款人数据产品表添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="ufangLoaneeDataProd" action="${ctx}/ufang/ufangLoaneeDataProd/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label style="text-align:right;width:150px">产品名称：</label>
				<form:input path="name" htmlEscape="false" maxlength="64" class="input-medium"/>
			</li>
			<li><label style="text-align:right;width:150px">申请渠道：</label>
				<form:input path="channel" htmlEscape="false" maxlength="4" class="input-medium"/>
			</li>
			<li class="btns"><label style="text-align:right;width:50px"></label><input id="btnSubmit" class="btn btn-primary " style="width:80px;" type="submit" value="查   询"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th style="text-align:center">产品名称</th>
				<th style="text-align:center">申请渠道</th>
				<th style="text-align:center">最小年龄</th>
				<th style="text-align:center">最大年龄</th>
				<th style="text-align:center">芝麻分</th>
				<th style="text-align:center">最大芝麻分</th>
				<th style="text-align:center">运营商认证</th>
				<th style="text-align:center">售价</th>
				<th style="text-align:center">最大销量</th>
				<th style="text-align:center">折扣</th>
				<th style="text-align:center">折扣开始天数</th>
				<th style="text-align:center">是否启用</th>
				<th style="text-align:center">描述</th>
				<shiro:hasPermission name="ufang:ufangLoaneeDataProd:edit"><th style="text-align:center">操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="ufangLoaneeDataProd">
			<tr>
				<td style="text-align:center"><a href="${ctx}/ufang/ufangLoaneeDataProd/query?id=${ufangLoaneeDataProd.id}">
					${ufangLoaneeDataProd.name}
				</a></td>
				<td style="text-align:center">
					${ufangLoaneeDataProd.channel}
				</td>
				<td style="text-align:center">
					${ufangLoaneeDataProd.minAge}
				</td>
				<td style="text-align:center">
					${ufangLoaneeDataProd.maxAge}
				</td>
				<td style="text-align:center">
					${ufangLoaneeDataProd.minZmf}
				</td>
				<td style="text-align:center">
					${ufangLoaneeDataProd.maxZmf}
				</td>
				<td style="text-align:center">
					${ufangLoaneeDataProd.isYys}
				</td>
				<td style="text-align:center">
					${ufangLoaneeDataProd.price}
				</td>
				<td style="text-align:center">
					${ufangLoaneeDataProd.maxSales}
				</td>
				<td style="text-align:center">
					${ufangLoaneeDataProd.discount}
				</td>
				<td style="text-align:center">
					${ufangLoaneeDataProd.discountBeginDays}
				</td>
				<td style="text-align:center">
					${ufangLoaneeDataProd.isOn}
				</td>
				<td style="text-align:center">
					${ufangLoaneeDataProd.rmk}
				</td>
				<td  style="text-align:center">
				<shiro:hasPermission name="ufang:ufangLoaneeDataProd:edit">
    				<a href="${ctx}/ufang/ufangLoaneeDataProd/update?id=${ufangLoaneeDataProd.id}">修改</a>
					<a href="${ctx}/ufang/ufangLoaneeDataProd/delete?id=${ufangLoaneeDataProd.id}" onclick="return confirmx('确认要删除该借款人数据产品表吗？', this.href)">删除</a>
				</shiro:hasPermission>
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>