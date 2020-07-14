<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>贷超管理管理</title>
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
		<li class="active"><a href="${admin}/ufangLoanMarket/">贷超列表</a></li>
		<shiro:hasPermission name="smarket:ufangLoanMarket:edit"><li><a href="${admin}/ufangLoanMarket/add">贷超添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="ufangLoanMarket" action="${admin}/ufangLoanMarket/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label style="text-align:right;width:150px">贷超名称：</label>
				<form:input path="name" htmlEscape="false" maxlength="100" class="input-medium"/>
			</li>
			<li class="btns"><label style="text-align:right;width:50px"></label><input id="btnSubmit" class="btn btn-primary " style="width:80px;" type="submit" value="查   询"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th style="text-align:center">贷超名称</th>
				<th style="text-align:center">归属机构</th>
				<th style="text-align:center">最小借款金额</th>
				<th style="text-align:center">最大借款金额</th>
				<th style="text-align:center">最短借款周期</th>
				<th style="text-align:center">最长借款周期</th>
				<th style="text-align:center">最小利率</th>
				<th style="text-align:center">最大利率</th>
				<th style="text-align:center">审批时长</th>
				<th style="text-align:center">展示已放款笔数</th>
				<th style="text-align:center">是否需要认证</th>
				<th style="text-align:center">是否上架</th>
				<th style="text-align:center">跳转方式</th>
				<th style="text-align:center">展示排序</th>
				<shiro:hasPermission name="smarket:ufangLoanMarket:edit"><th style="text-align:center">操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="ufangLoanMarket">
			<tr>
				<td style="text-align:center"><a href="${admin}/ufangLoanMarket/query?id=${ufangLoanMarket.id}">
					${ufangLoanMarket.name}
				</a></td>
				<td style="text-align:center">
					${ufangLoanMarket.brn.brnName}
				</td>
				<td style="text-align:center">
					${ufangLoanMarket.minLoanAmt}
				</td>
				<td style="text-align:center">
					${ufangLoanMarket.maxLoanAmt}
				</td>
				<td style="text-align:center">
					${ufangLoanMarket.minLoanTerm}
				</td>
				<td style="text-align:center">
					${ufangLoanMarket.maxLoanTerm}
				</td>
				<td style="text-align:center">
					${ufangLoanMarket.minIntRate}
				</td>
				<td style="text-align:center">
					${ufangLoanMarket.maxIntRate}
				</td>
				<td style="text-align:center">
					${ufangLoanMarket.checkTerm}
				</td>
				<td style="text-align:center">
					${ufangLoanMarket.displayLoanQuantity}
				</td>
				<td style="text-align:center">
					${fns:getDictLabel(ufangLoanMarket.needsIdentify, 'trueOrFalse', '')}
				</td>
				<td style="text-align:center">
					${fns:getDictLabel(ufangLoanMarket.isMarketable, 'trueOrFalse', '')}
				</td>
				<td style="text-align:center">
					${fns:getDictLabel(ufangLoanMarket.redirectType, 'redirectType', '')}
				</td>
				<td style="text-align:center">
					${ufangLoanMarket.sort}
				</td>
				<td  style="text-align:center">
				<shiro:hasPermission name="smarket:ufangLoanMarket:edit">
    				<a href="${admin}/ufangLoanMarket/update?id=${ufangLoanMarket.id}">修改</a>
					<a href="${admin}/ufangLoanMarket/delete?id=${ufangLoanMarket.id}" onclick="return confirmx('确认要删除该贷超管理吗？', this.href)">删除</a>
				</shiro:hasPermission>
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>