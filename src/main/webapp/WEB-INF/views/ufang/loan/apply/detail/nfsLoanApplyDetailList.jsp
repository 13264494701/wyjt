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
		<li class="active"><a href="${ctx}/apply/detail/nfsLoanApplyDetail/">借贷申请明细列表</a></li>
		<shiro:hasPermission name="apply:detail:nfsLoanApplyDetail:edit"><li><a href="${ctx}/apply/detail/nfsLoanApplyDetail/add">借贷申请明细添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="nfsLoanApplyDetail" action="${ctx}/apply/detail/nfsLoanApplyDetail/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label style="text-align:right;width:150px">会员ID：</label>
				<form:input path="memberId" htmlEscape="false" maxlength="20" class="input-medium"/>
			</li>
			<li><label style="text-align:right;width:150px">借贷角色：</label>
				<form:select path="loanRole"  style="width:177px" class="input-medium">
					<form:option value="" label="请选择"/>
					<form:options items="${fns:getDictList('')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</li>
			<li><label style="text-align:right;width:150px">状态：</label>
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
				<th style="text-align:center">申请ID</th>
				<th style="text-align:center">会员ID</th>
				<th style="text-align:center">会员姓名</th>
				<th style="text-align:center">会员头像</th>
				<th style="text-align:center">借贷角色</th>
				<th style="text-align:center">借款金额</th>
				<th style="text-align:center">视频地址</th>
				<th style="text-align:center">状态</th>
				<th style="text-align:center">利息状态</th>
				<th style="text-align:center">活体视频状态</th>
				<th style="text-align:center">争议解决方式：0：仲裁，1：起诉</th>
				<th style="text-align:center">申请进度</th>
				<shiro:hasPermission name="apply:detail:nfsLoanApplyDetail:edit"><th style="text-align:center">操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="nfsLoanApplyDetail">
			<tr>
				<td style="text-align:center"><a href="${ctx}/apply/detail/nfsLoanApplyDetail/query?id=${nfsLoanApplyDetail.id}">
					${nfsLoanApplyDetail.applyId}
				</a></td>
				<td style="text-align:center">
					${nfsLoanApplyDetail.memberId}
				</td>
				<td style="text-align:center">
					${nfsLoanApplyDetail.memberName}
				</td>
				<td style="text-align:center">
					${nfsLoanApplyDetail.memberImage}
				</td>
				<td style="text-align:center">
					${fns:getDictLabel(nfsLoanApplyDetail.loanRole, '', '')}
				</td>
				<td style="text-align:center">
					${nfsLoanApplyDetail.amount}
				</td>
				<td style="text-align:center">
					${nfsLoanApplyDetail.videoUrl}
				</td>
				<td style="text-align:center">
					${fns:getDictLabel(nfsLoanApplyDetail.status, '', '')}
				</td>
				<td style="text-align:center">
					${nfsLoanApplyDetail.intStatus}
				</td>
				<td style="text-align:center">
					${nfsLoanApplyDetail.aliveVideoStatus}
				</td>
				<td style="text-align:center">
					${nfsLoanApplyDetail.disputeResolution}
				</td>
				<td style="text-align:center">
					${nfsLoanApplyDetail.progress}
				</td>
				<td  style="text-align:center">
				<shiro:hasPermission name="apply:detail:nfsLoanApplyDetail:edit">
    				<a href="${ctx}/apply/detail/nfsLoanApplyDetail/update?id=${nfsLoanApplyDetail.id}">修改</a>
					<a href="${ctx}/apply/detail/nfsLoanApplyDetail/delete?id=${nfsLoanApplyDetail.id}" onclick="return confirmx('确认要删除该借贷申请明细吗？', this.href)">删除</a>
				</shiro:hasPermission>
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>