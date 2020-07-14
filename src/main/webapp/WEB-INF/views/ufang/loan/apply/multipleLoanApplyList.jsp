<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>借款申请管理</title>
	<meta name="decorator" content="default"/>
	<script src="${ctxStatic}/artdialog/js/dialog-plus.js"></script>
    <link href="${ctxStatic}/artdialog/css/dialog.css" rel="stylesheet" />
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
		<li class="active"><a href="${ufang}/loanApply/multipleLoanApplyList">多人申请</a></li>		
	</ul>
	<form:form id="searchForm" modelAttribute="nfsLoanApply" action="${ufang}/loanApply/multipleLoanApplyList" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label style="text-align:right;width:150px">借款人：</label>
				<form:input path="member.name" htmlEscape="false" maxlength="20" class="input-medium"/>
			</li>
			<li><label style="text-align:right;width:150px">手机号：</label>
				<form:input path="member.username" htmlEscape="false" maxlength="20" class="input-medium"/>
			</li>
			<li class="btns"><label style="text-align:right;width:50px"></label><input id="btnSubmit" class="btn btn-primary " style="width:80px;" type="submit" value="查   询"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th style="text-align:center">借款人</th>
				<th style="text-align:center">手机号</th>
				<th style="text-align:center">借款用途</th>
				<th style="text-align:center">借款金额</th>
				<th style="text-align:center">借款利息</th>
				<th style="text-align:center">还款方式</th>
				<th style="text-align:center">借款期限</th>
				<th style="text-align:center">申请状态</th>
				<th style="text-align:center">申请时间</th>
				<shiro:hasPermission name="ufang:loanApply:edit"><th style="text-align:center">操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="nfsLoanApply">
			<tr id="${nfsLoanApply.id}">
				<td style="text-align:center">
					${nfsLoanApply.member.name}
				</td>
				<td style="text-align:center">
					${nfsLoanApply.member.username}
				</td>
				<td style="text-align:center">
				    ${fns:getDictLabel(nfsLoanApply.loanPurp, 'loanPurp', '')}
				</td>
				<td style="text-align:center">
					${nfsLoanApply.amount}
				</td>
				<td style="text-align:center">
					${ufang:decimalToStr(nfsLoanApply.interest,2)}
				</td>
				<td style="text-align:center">
					${fns:getDictLabel(nfsLoanApply.repayType, 'repayType', '')}
				</td>
				<td style="text-align:center">
					${nfsLoanApply.term}天
				</td>
				<td  style="text-align:center">
                    ${fns:getDictLabel(nfsLoanApply.detail.status, 'loanApplyStatus', '')}
				</td>
				<td style="text-align:center">
					<fmt:formatDate value="${nfsLoanApply.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td  style="text-align:center">
                    <a href ="${ufang}/loanApply/query?id=${nfsLoanApply.id}">申请明细</a>
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>