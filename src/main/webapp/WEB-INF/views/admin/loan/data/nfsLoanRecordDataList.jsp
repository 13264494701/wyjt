<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>借条数据管理</title>
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
		<li class="active"><a href="${ctx}/loan/nfsLoanRecordData/">借条数据列表</a></li>
		<shiro:hasPermission name="loan:nfsLoanRecordData:edit"><li><a href="${ctx}/loan/nfsLoanRecordData/add">借条数据添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="nfsLoanRecordData" action="${ctx}/loan/nfsLoanRecordData/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label style="text-align:right;width:150px">债务人ID：</label>
				<form:input path="loaneeId" htmlEscape="false" maxlength="20" class="input-medium"/>
			</li>
			<li><label style="text-align:right;width:150px">借款人手机号码：</label>
				<form:input path="loaneePhoneNo" htmlEscape="false" maxlength="11" class="input-medium"/>
			</li>
			<li><label style="text-align:right;width:150px">还款状态：</label>
				<form:input path="repayStatus" htmlEscape="false" maxlength="4" class="input-medium"/>
			</li>
			<li><label style="text-align:right;width:150px">逾期天数：</label>
				<form:input path="overdueDays" htmlEscape="false" maxlength="11" class="input-medium"/>
			</li>
			<li><label style="text-align:right;width:150px">是否OK：</label>
				<form:input path="isOk" htmlEscape="false" maxlength="1" class="input-medium"/>
			</li>
			<li class="btns"><label style="text-align:right;width:50px"></label><input id="btnSubmit" class="btn btn-primary " style="width:80px;" type="submit" value="查   询"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th style="text-align:center">债务人ID</th>
				<th style="text-align:center">债务人姓名</th>
				<th style="text-align:center">借款人身份证号</th>
				<th style="text-align:center">借款人手机号码</th>
				<th style="text-align:center">借款金额</th>
				<th style="text-align:center">借款利率</th>
				<th style="text-align:center">利息</th>
				<th style="text-align:center">借款期限</th>
				<th style="text-align:center">应还日期</th>
				<th style="text-align:center">结清日期</th>
				<th style="text-align:center">还款状态</th>
				<th style="text-align:center">逾期天数</th>
				<th style="text-align:center">是否OK</th>
				<th style="text-align:center">创建时间</th>
				<shiro:hasPermission name="loan:nfsLoanRecordData:edit"><th style="text-align:center">操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="nfsLoanRecordData">
			<tr>
				<td style="text-align:center"><a href="${ctx}/loan/nfsLoanRecordData/query?id=${nfsLoanRecordData.id}">
					${nfsLoanRecordData.loaneeId}
				</a></td>
				<td style="text-align:center">
					${nfsLoanRecordData.loaneeName}
				</td>
				<td style="text-align:center">
					${nfsLoanRecordData.loaneeIdNo}
				</td>
				<td style="text-align:center">
					${nfsLoanRecordData.loaneePhoneNo}
				</td>
				<td style="text-align:center">
					${nfsLoanRecordData.amount}
				</td>
				<td style="text-align:center">
					${nfsLoanRecordData.intRate}
				</td>
				<td style="text-align:center">
					${nfsLoanRecordData.interest}
				</td>
				<td style="text-align:center">
					${nfsLoanRecordData.term}
				</td>
				<td style="text-align:center">
					<fmt:formatDate value="${nfsLoanRecordData.dueRepayDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td style="text-align:center">
					<fmt:formatDate value="${nfsLoanRecordData.completeDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td style="text-align:center">
					${nfsLoanRecordData.repayStatus}
				</td>
				<td style="text-align:center">
					${nfsLoanRecordData.overdueDays}
				</td>
				<td style="text-align:center">
					${nfsLoanRecordData.isOk}
				</td>
				<td style="text-align:center">
					<fmt:formatDate value="${nfsLoanRecordData.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td  style="text-align:center">
				<shiro:hasPermission name="loan:nfsLoanRecordData:edit">
    				<a href="${ctx}/loan/nfsLoanRecordData/update?id=${nfsLoanRecordData.id}">修改</a>
					<a href="${ctx}/loan/nfsLoanRecordData/delete?id=${nfsLoanRecordData.id}" onclick="return confirmx('确认要删除该借条数据吗？', this.href)">删除</a>
				</shiro:hasPermission>
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>