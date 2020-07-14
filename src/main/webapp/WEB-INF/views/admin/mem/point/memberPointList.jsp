<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>会员积分管理</title>
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
		<li class="active"><a href="${ctx}/member/memberPoint/">会员积分列表</a></li>
	</ul>
	<form:form id="searchForm" modelAttribute="memberPoint" action="${ctx}/member/memberPoint/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label style="text-align:right;width:150px">会员编号：</label>
				<form:input path="memberNo" htmlEscape="false" maxlength="16" class="input-medium"/>
			</li>
			<li class="btns"><label style="text-align:right;width:50px"></label><input id="btnSubmit" class="btn btn-primary " style="width:80px;" type="submit" value="查   询"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th style="text-align:center">会员编号</th>
				<th style="text-align:center">积分余额</th>
				<th style="text-align:center">累计积分</th>
				<th style="text-align:center">扣除积分</th>
				<shiro:hasPermission name="mem:memberPoint:edit"><th style="text-align:center">操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="memberPoint">
			<tr>
				<td style="text-align:center">
					${memberPoint.memberNo}
				</td>
				<td style="text-align:center">
					${memberPoint.balancePoints}
				</td>
				<td style="text-align:center">
					${memberPoint.totalPoints}
				</td>
				<td style="text-align:center">
					${memberPoint.reducePoints}
				</td>
				<td  style="text-align:center">
				<a href="${ctx}/member/memberPointDetail/list?memberNo=${memberPoint.memberNo}">积分明细</a>
				<shiro:hasPermission name="mem:memberPoint:edit">
					<a href="${ctx}/member/memberPoint/update?memNo=${memberPoint.memberNo}&redirectUrl=memberPoint">积分调整</a>
				</shiro:hasPermission>
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>