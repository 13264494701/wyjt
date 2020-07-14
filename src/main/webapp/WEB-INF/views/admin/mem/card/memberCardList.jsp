<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>银行卡管理</title>
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
		<li class="active"><a href="${ctx}/memberCard/">银行卡列表</a></li>		
	</ul>
	<form:form id="searchForm" modelAttribute="memberCard" action="${ctx}/memberCard/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label style="text-align:right;width:150px">会员编号：</label>
				<form:input path="member.id" htmlEscape="false" maxlength="20" class="input-medium"/>
			</li>
			<li><label style="text-align:right;width:150px">会员手机号：</label>
				<form:input path="member.username" htmlEscape="false" maxlength="32" class="input-medium"/>
			</li>
			<li><label style="text-align:right;width:150px">银行卡号：</label>
				<form:input path="cardNo" htmlEscape="false" maxlength="32" class="input-medium"/>
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
				<th style="text-align:center">会员姓名</th>
				<th style="text-align:center">会员手机号</th>
				<th style="text-align:center">银行卡号</th>
				<th style="text-align:center">卡片种类</th>
				<th style="text-align:center">银行名称</th>
				<th style="text-align:center">状态</th>
				<th style="text-align:center">创建时间</th>
				<shiro:hasPermission name="card:memberCard:edit"><th style="text-align:center">操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="memberCard">
			<tr>
			    <td style="text-align:center"><a href="${ctx}/member/query?id=${memberCard.member.id}">
					${memberCard.member.id}
				</a></td>
				<td style="text-align:center">
					${memberCard.member.name}
				</td>
				<td style="text-align:center">
					${memberCard.member.username}
				</td>
				<td style="text-align:center">
					${memberCard.cardNo}
				</td>
				<td style="text-align:center">
					${fns:getDictLabel(memberCard.cardType, 'cardType', '')}
				</td>
				<td style="text-align:center">
					${memberCard.bank.name}
				</td>

				<td style="text-align:center">
					${fns:getDictLabel(memberCard.status, 'bindStatus', '')}
				</td>
				<td style="text-align:center">
					<fmt:formatDate value="${memberCard.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td style="text-align:center">
				<shiro:hasPermission name="card:memberCard:edit">
				<c:if test="${memberCard.status == 'binded'}"> 
					<a href="${admin}/memberCard/update?id=${memberCard.id}">修改</a>
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