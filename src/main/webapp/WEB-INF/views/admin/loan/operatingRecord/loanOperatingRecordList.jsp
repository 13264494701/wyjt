<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>借条操作记录管理</title>
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
		<li class="active"><a href="${ctx}/loanOperatingRecord/?oldRecord.id=${loanRecord.id}">借条操作记录</a></li>
	</ul>
	<form:form id="searchForm" modelAttribute="nfsLoanOperatingRecord" action="${ctx}/loanOperatingRecord/?oldRecord.id=${loanRecord.id}" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label style="text-align:right;width:150px">借条ID：</label>
				<form:input path="oldRecord.id" readonly="true" htmlEscape="false" maxlength="20" class="input-medium"/>
			</li>
			<li><label style="text-align:right;width:150px">发起者：</label>
				<form:select path="initiator"  style="width:177px" class="input-medium">
					<form:option value="" label="请选择"/>
					<form:options items="${fns:getDictList('loanRole')}" itemLabel="label" itemValue="label" htmlEscape="false"/>
				</form:select>
			</li>
			<li><label style="text-align:right;width:150px">操作类型：</label>
				<form:select path="type"  style="width:177px" class="input-medium">
					<form:option value="" label="请选择"/>
					<form:options items="${fns:getDictList('operatingRecordType')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</li>
			<li><label style="text-align:right;width:80px">创建时间：</label>
			<input name="beginTime" type="text"  maxlength="20" class="input-medium Wdate"
				value="<fmt:formatDate value="${beginTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"
				onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:true});"/> - 
			<input name="endTime" type="text"  maxlength="20" class="input-medium Wdate"
				value="<fmt:formatDate value="${endTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"
				onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:true});"/>
			</li>
			<li class="btns"><label style="text-align:right;width:50px"></label><input id="btnSubmit" class="btn btn-primary " style="width:80px;" type="submit" value="查   询"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th style="text-align:center">发起者</th>
				<th style="text-align:center">操作类型</th>
				<th style="text-align:center">原本金</th>
				<th style="text-align:center">原利息</th>
				<th style="text-align:center">原逾期利息</th>
				<th style="text-align:center">延期天数</th>
				<th style="text-align:center">延期利息</th>
				<th style="text-align:center">部分还款金额</th>
				<th style="text-align:center">现本金</th>
				<th style="text-align:center">现利息</th>
				<th style="text-align:center">现还款日</th>
				<th style="text-align:center">现状态</th>
				<th style="text-align:center">时间</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="nfsLoanOperatingRecord">
			<tr>
				<td style="text-align:center">
					${nfsLoanOperatingRecord.initiator}
				</td>
				<td style="text-align:center">
					${fns:getDictLabel(nfsLoanOperatingRecord.type, 'operatingRecordType', '')}
				</td>
				<td style="text-align:center">
					${nfsLoanOperatingRecord.oldRecord.amount}
				</td>
				<td style="text-align:center">
					${nfsLoanOperatingRecord.oldRecord.interest}
				</td>
				<td style="text-align:center">
					${nfsLoanOperatingRecord.oldRecord.overdueInterest}
				</td>
				<td style="text-align:center">
					${nfsLoanOperatingRecord.delayDays}
				</td>
				
				<td style="text-align:center">
					${nfsLoanOperatingRecord.delayInterest}
				</td>
				<td style="text-align:center">
					${nfsLoanOperatingRecord.repaymentAmount}
				</td>
				<td style="text-align:center">
					${nfsLoanOperatingRecord.nowRecord.amount}
				</td>
				<td style="text-align:center">
					${nfsLoanOperatingRecord.nowRecord.interest}
				</td>
				<td style="text-align:center">
					<fmt:formatDate value="${nfsLoanOperatingRecord.nowRecord.dueRepayDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td style="text-align:center">
					${fns:getDictLabel(nfsLoanOperatingRecord.nowRecord.status, 'loanStatus', '')}
				</td>
				<td style="text-align:center">
					<fmt:formatDate value="${nfsLoanOperatingRecord.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>