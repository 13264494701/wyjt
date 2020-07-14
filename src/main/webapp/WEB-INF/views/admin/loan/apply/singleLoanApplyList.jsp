<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>借款申请管理</title>
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
		<li class="active"><a href="${ctx}/loanApply/singleLoanApplyList">单人申请</a></li>		
	</ul>
	<form:form id="searchForm" modelAttribute="nfsLoanApply" action="${ctx}/loanApply/singleLoanApplyList" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label style="text-align:right;width:150px">发起会员：</label>
				<form:input path="member.name" htmlEscape="false" maxlength="20" class="input-medium"/>
			</li>
			<li><label style="text-align:right;width:150px">借贷角色：</label>
				<form:select path="loanRole"  style="width:177px" class="input-medium">
					<form:option value="" label="请选择"/>
					<form:options items="${fns:getDictList('loanRole')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</li>
			<li><label style="text-align:right;width:150px">还款方式：</label>
				<form:select path="repayType"  style="width:177px" class="input-medium">
					<form:option value="" label="请选择"/>
					<form:options items="${fns:getDictList('repayType')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</li>
			<li><label style="text-align:right;width:150px">申请状态：</label>
				<form:select path="detail.status"  style="width:177px" class="input-medium">
					<form:option value="" label="请选择"/>
					<form:options items="${fns:getDictList('loanApplyStatus')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</li>
			<li><label style="text-align:right;width:150px">申请渠道：</label>
				<form:select path="channel"  style="width:177px" class="input-medium">
					<form:option value="" label="请选择"/>
					<form:options items="${fns:getDictList('trxChannel')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
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
				<th style="text-align:center">发起会员</th>
				<th style="text-align:center">借贷角色</th>
				<th style="text-align:center">借款用途</th>
				<th style="text-align:center">借款金额</th>
				<th style="text-align:center">借款利息</th>
				<th style="text-align:center">还款方式</th>
				<th style="text-align:center">借款期限</th>
				<th style="text-align:center">申请状态</th>
				<th style="text-align:center">申请时间</th>
				<shiro:hasPermission name="loan:loanApply:edit"><th style="text-align:center">操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="nfsLoanApply">
			<tr>
				<td style="text-align:center"><a href="${admin}/member/query?id=${nfsLoanApply.member.id}">
					${nfsLoanApply.member.name}
				</a></td>
				<td style="text-align:center">
					${fns:getDictLabel(nfsLoanApply.loanRole, 'loanRole', '')}
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
				<td style="text-align:center">
					${fns:getDictLabel(nfsLoanApply.detail.status, 'loanApplyStatus', '')}
				</td>	
				<td style="text-align:center">
					<fmt:formatDate value="${nfsLoanApply.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td  style="text-align:center">
				<shiro:hasPermission name="loan:loanApply:edit">
    			    <a href="${admin}/loanApply/query?id=${nfsLoanApply.id}">查看详情</a>									    
				    <a href="${admin}/nfsLoanRecord/?loanee.id=${nfsLoanApply.member.id}">借款借条</a>
				    <a href="${admin}/nfsLoanRecord/?loaner.id=${nfsLoanApply.member.id}">放款借条</a>				    
				</shiro:hasPermission>
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>