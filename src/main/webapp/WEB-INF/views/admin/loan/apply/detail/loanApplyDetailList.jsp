<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>借贷申请明细管理</title>
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
		
	</ul>
	<form:form id="searchForm" modelAttribute="nfsLoanApplyDetail" action="${ctx}/apply/detail/nfsLoanApplyDetail/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th style="text-align:center">会员姓名</th>
				<th style="text-align:center">会员头像</th>
				<th style="text-align:center">借贷角色</th>
				<th style="text-align:center">借款金额</th>
				<th style="text-align:center">状态</th>
				<shiro:hasPermission name="loan:loanApplyDetail:edit"><th style="text-align:center">操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="nfsLoanApplyDetail">
			<tr>
				<td style="text-align:center">
					${nfsLoanApplyDetail.member.name}
				</td>
				<td style="text-align:center">
					<img src="${nfsLoanApplyDetail.member.headImage}" />
				</td>
				<td style="text-align:center">
					${fns:getDictLabel(nfsLoanApplyDetail.loanRole, 'loanRole', '')}
				</td>
				<td style="text-align:center">
					${ufang:decimalToStr(nfsLoanApplyDetail.amount,2)}
				</td>
				<td style="text-align:center">
					${fns:getDictLabel(nfsLoanApplyDetail.status, 'loanApplyStatus', '')}
				</td>
				<td  style="text-align:center">
				<shiro:hasPermission name="loan:loanApplyDetail:edit">
    				
				</shiro:hasPermission>
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>