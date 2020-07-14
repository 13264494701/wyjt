<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>还款计划管理</title>
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
		<li class="active"><a href="${ctx}/repay/nfsLoanRepayRecord/">还款计划列表</a></li>
		<shiro:hasPermission name="repay:nfsLoanRepayRecord:edit"><li><a href="${ctx}/repay/nfsLoanRepayRecord/add">还款计划添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="nfsLoanRepayRecord" action="${ctx}/repay/nfsLoanRepayRecord/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label style="text-align:right;width:150px">借条编号：</label>
				<form:input path="loanId" htmlEscape="false" maxlength="20" class="input-medium"/>
			</li>
			<li><label style="text-align:right;width:150px">期数序号：</label>
				<form:input path="periodsSeq" htmlEscape="false" maxlength="4" class="input-medium"/>
			</li>
			<li><label style="text-align:right;width:150px">还款状态：</label>
				<form:select path="status"  style="width:177px" class="input-medium">
					<form:option value="" label="请选择"/>
					<form:options items="${fns:getDictList('')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</li>
			<li class="btns"><label style="text-align:right;width:50px"></label><input id="btnSubmit" class="btn btn-primary " style="width:80px;" type="submit" value="查   询"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th style="text-align:center">借条编号</th>
				<th style="text-align:center">期数序号</th>
				<th style="text-align:center">应还金额</th>
				<th style="text-align:center">应还本金</th>
				<th style="text-align:center">应还利息</th>
				<th style="text-align:center">应还日期</th>
				<th style="text-align:center">实还金额</th>
				<th style="text-align:center">实还日期</th>
				<th style="text-align:center">还款状态</th>
				<th style="text-align:center">创建时间</th>
				<shiro:hasPermission name="repay:nfsLoanRepayRecord:edit"><th style="text-align:center">操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="nfsLoanRepayRecord">
			<tr>
				<td style="text-align:center"><a href="${ctx}/repay/nfsLoanRepayRecord/query?id=${nfsLoanRepayRecord.id}">
					${nfsLoanRepayRecord.loanId}
				</a></td>
				<td style="text-align:center">
					${nfsLoanRepayRecord.periodsSeq}
				</td>
				<td style="text-align:center">
					${nfsLoanRepayRecord.expectRepayAmt}
				</td>
				<td style="text-align:center">
					${nfsLoanRepayRecord.expectRepayPrn}
				</td>
				<td style="text-align:center">
					${nfsLoanRepayRecord.expectRepayInt}
				</td>
				<td style="text-align:center">
					<fmt:formatDate value="${nfsLoanRepayRecord.expectRepayDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td style="text-align:center">
					${nfsLoanRepayRecord.actualRepayAmt}
				</td>
				<td style="text-align:center">
					<fmt:formatDate value="${nfsLoanRepayRecord.actualRepayDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td style="text-align:center">
					${fns:getDictLabel(nfsLoanRepayRecord.status, '', '')}
				</td>
				<td style="text-align:center">
					<fmt:formatDate value="${nfsLoanRepayRecord.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td  style="text-align:center">
				<shiro:hasPermission name="repay:nfsLoanRepayRecord:edit">
    				<a href="${ctx}/repay/nfsLoanRepayRecord/update?id=${nfsLoanRepayRecord.id}">修改</a>
					<a href="${ctx}/repay/nfsLoanRepayRecord/delete?id=${nfsLoanRepayRecord.id}" onclick="return confirmx('确认要删除该还款计划吗？', this.href)">删除</a>
				</shiro:hasPermission>
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>