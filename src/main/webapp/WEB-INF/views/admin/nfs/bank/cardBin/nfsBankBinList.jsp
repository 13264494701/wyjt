<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>卡BIN管理</title>
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
		<li class="active"><a href="${ctx}/nfsBankBin/?bank.id=${bank.id}">卡BIN列表</a></li>
		<shiro:hasPermission name="bank:nfsBankBin:edit"><li><a href="${ctx}/nfsBankBin/add?bank.id=${bank.id}">卡BIN添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="nfsBankBin" action="${ctx}/nfsBankBin/?bank.id=${bank.id}" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>

	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th style="text-align:center">银行编码</th>
				<th style="text-align:center">卡BIN</th>
				<th style="text-align:center">卡种类型</th>
				<th style="text-align:center">创建时间</th>
				<shiro:hasPermission name="bank:nfsBankBin:edit"><th style="text-align:center">操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="nfsBankBin">
			<tr>
				<td style="text-align:center">
					${nfsBankBin.bank.name}
				</td>
				<td style="text-align:center">
					${nfsBankBin.cardBin}
				</td>
				<td style="text-align:center">
					${fns:getDictLabel(nfsBankBin.cardType, 'cardType', '')}
				</td>
				<td style="text-align:center">
					<fmt:formatDate value="${nfsBankBin.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td  style="text-align:center">
				<shiro:hasPermission name="bank:nfsBankBin:edit">
    				<a href="${ctx}/nfsBankBin/update?id=${nfsBankBin.id}">修改</a>
					<a href="${ctx}/nfsBankBin/delete?id=${nfsBankBin.id}" onclick="return confirmx('确认要删除该卡BIN吗？', this.href)">删除</a>
				</shiro:hasPermission>
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>