<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>交易规则管理</title>
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
		<li class="active"><a href="${ctx}/nfsTrxRuleItem/?trxCode=${trxCode}">规则明细列表</a></li>
		<shiro:hasPermission name="trx:nfsTrxRuleItem:edit"><li><a href="${ctx}/nfsTrxRuleItem/add?trxCode=${trxCode}">规则明细添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="nfsTrxRuleItem" action="${ctx}/nfsTrxRuleItem/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th style="text-align:center">交易代码</th>
				<th style="text-align:center">交易名称</th>
				<th style="text-align:center">业务角色</th>
				<th style="text-align:center">交易方向</th>
				<th style="text-align:center">科目编号</th>
				<th style="text-align:center">科目名称</th>				
				<th style="text-align:center">流水组(前台展示)</th>
				<shiro:hasPermission name="trx:nfsTrxRuleItem:edit"><th style="text-align:center">操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="nfsTrxRuleItem">
			<tr>
				<td style="text-align:center">
					${nfsTrxRuleItem.trxCode}
				</td>
				<td style="text-align:center">
					${nfsTrxRuleItem.name}
				</td>
				<td style="text-align:center">
					${fns:getDictLabel(nfsTrxRuleItem.trxRole, 'trxRole', '')}
				</td>
				<td style="text-align:center">
				    ${fns:getDictLabel(nfsTrxRuleItem.drc, 'drc', '')}
				</td>
				<td style="text-align:center">
					${nfsTrxRuleItem.subNo}
				</td>
				<td style="text-align:center">
					${nfsTrxRuleItem.subName}
				</td>

				<td style="text-align:center">
				    ${fns:getDictLabel(nfsTrxRuleItem.trxGroup, 'trxGroup', '')}
				</td>
				<td  style="text-align:center">
				<shiro:hasPermission name="trx:nfsTrxRuleItem:edit">
    				<a href="${ctx}/nfsTrxRuleItem/update?id=${nfsTrxRuleItem.id}">修改</a>
					<a href="${ctx}/nfsTrxRuleItem/delete?id=${nfsTrxRuleItem.id}" onclick="return confirmx('确认要删除该交易规则吗？', this.href)">删除</a>
				</shiro:hasPermission>
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>