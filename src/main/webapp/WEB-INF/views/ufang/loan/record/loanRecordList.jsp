<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>借条记录管理</title>
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
		<li class="active"><a href="${ufang}/loanRecord/">借条记录列表</a></li>		
	</ul>
	<form:form id="searchForm" modelAttribute="nfsLoanRecord" action="${ufang}/loanRecord/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label style="text-align:right;width:150px">借条编号：</label>
				<form:input path="loanNo" htmlEscape="false" maxlength="16" class="input-medium"/>
			</li>

			<li><label style="text-align:right;width:150px">放款人姓名：</label>
				<form:input path="loaner.name" htmlEscape="false" maxlength="64" class="input-medium"/>
			</li>

			<li><label style="text-align:right;width:150px">借款人姓名：</label>
				<form:input path="loanee.name" htmlEscape="false" maxlength="64" class="input-medium"/>
			</li>
			<li><label style="text-align:right;width:150px">借条状态：</label>
				<form:select path="status"  style="width:177px" class="input-medium">
					<form:option value="" label="请选择"/>
					<form:options items="${fns:getDictList('loanStatus')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</li>
			
			<li><label style="text-align:right;width:80px">本期到期时间：</label>
				<input name="beginTime" type="text"  maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${nfsLoanRecord.beginTime}" pattern="yyyy-MM-dd"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:true});"/> - 
				<input name="endTime" type="text"  maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${nfsLoanRecord.endTime}" pattern="yyyy-MM-dd"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:true});"/>
			</li>
			
			<li class="btns"><label style="text-align:right;width:50px"></label><input id="btnSubmit" class="btn btn-primary " style="width:80px;" type="submit" value="查   询"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<c:if test="${message!=null}">
		<div id="messageBox" class="alert ${messageType=='success'?'alert-success':'alert-danger'} hide" style="display: block;">
			<button data-dismiss="alert" class="close">×</button>
				${message}
		</div>
	</c:if>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th style="text-align:center">借条编号</th>
				<th style="text-align:center">借款人姓名</th>
				<th style="text-align:center">出借人姓名</th>
				<th style="text-align:center">借款金额</th>
				<th style="text-align:center">借款利率</th>
				<th style="text-align:center">借款期限</th>
				<th style="text-align:center">本期到期日</th>
				<th style="text-align:center">本期应还金额</th>
				<th style="text-align:center">结清日期</th>
				<th style="text-align:center">借条状态</th>
				<th style="text-align:center">创建时间</th>
				<shiro:hasPermission name="ufang:loanRecord:edit"><th style="text-align:center">操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="loanRecord">
			<tr>
				<td style="text-align:center">
					${loanRecord.loanNo}
				</td>
				<td style="text-align:center">
					${loanRecord.loanee.name}
				</td>
				<td style="text-align:center">
					${loanRecord.loaner.name}
				</td>

				<td style="text-align:center">
					${ufang:decimalToStr(loanRecord.amount,2)}
				</td>
				<td style="text-align:center">
					${loanRecord.intRate}
				</td>
				<td style="text-align:center">
					${loanRecord.term}天
				</td>
				<td style="text-align:center">
					<fmt:formatDate value="${loanRecord.dueRepayDate}" pattern="yyyy-MM-dd"/>
				</td>
				<td style="text-align:center">
					${ufang:decimalToStr(loanRecord.dueRepayAmount,2)}
				</td>
				<td style="text-align:center">
					<fmt:formatDate value="${loanRecord.completeDate}" pattern="yyyy-MM-dd"/>
				</td>
				<td style="text-align:center">
					${fns:getDictLabel(loanRecord.status, 'loanStatus', '')}
				</td>
				<td style="text-align:center">
					<fmt:formatDate value="${loanRecord.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td  style="text-align:center">
				<shiro:hasPermission name="ufang:loanRecord:edit">
    				<a href="${ufang}/loanRecord/query?id=${loanRecord.id}">
					查看详情
				    </a>
					<c:if test="${loanRecord.status=='overdue'}">
						<a href="${ufang}/loanRecord/collectionApply?loanId=${loanRecord.id}">申请催收</a>
						<a href="${ufang}/loanRecord/arbitrationApply?loanId=${loanRecord.id}">申请仲裁</a>
					</c:if>
				</shiro:hasPermission>
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>