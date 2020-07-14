<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>用户反馈意见管理</title>
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
		<li class="active"><a href="${ctx}/feedback/memberFeedback">用户反馈意见列表</a></li>
	</ul>
	<form:form id="searchForm" modelAttribute="memberFeedback" action="${ctx}/feedback/memberFeedback/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label style="text-align:right;width:150px">用户id：</label>
				<form:input path="member.id" htmlEscape="false" maxlength="20" class="input-medium"/>
			</li>
			<li class="btns"><label style="text-align:right;width:50px"></label><input id="btnSubmit" class="btn btn-primary " style="width:80px;" type="submit" value="查   询"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th style="text-align:center">用户id</th>
				<th style="text-align:center">用户意见</th>
				<th style="text-align:center">反馈时间</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="memberFeedback">
			<tr>
				<td style="text-align:center"><a href="${ctx}/member/query?id=${memberFeedback.member.id}">
					${memberFeedback.member.id}
				</a></td>
				<td style="text-align:center">
					${memberFeedback.note}
				</td>
				<td  style="text-align:center">
					${fns:getDateStr(memberFeedback.createTime,"yyyyMMdd HH:mm:ss")}
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>