<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>签约协议管理</title>
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
		<li class="active"><a href="${ctx}/bank/nfsBankProtocol/">签约协议列表</a></li>
		<shiro:hasPermission name="bank:nfsBankProtocol:edit"><li><a href="${ctx}/bank/nfsBankProtocol/add">签约协议添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="nfsBankProtocol" action="${ctx}/bank/nfsBankProtocol/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label style="text-align:right;width:150px">协议号：</label>
				<form:input path="protocolNo" htmlEscape="false" maxlength="127" class="input-medium"/>
			</li>
			<li><label style="text-align:right;width:150px">会员信息：</label>
				<form:input path="memberId" htmlEscape="false" maxlength="20" class="input-medium"/>
			</li>
			<li><label style="text-align:right;width:150px">银行卡信息：</label>
				<form:input path="cardId" htmlEscape="false" maxlength="20" class="input-medium"/>
			</li>
			<li><label style="text-align:right;width:150px">支付插件：</label>
				<form:input path="paymentPluginId" htmlEscape="false" maxlength="127" class="input-medium"/>
			</li>
			<li class="btns"><label style="text-align:right;width:50px"></label><input id="btnSubmit" class="btn btn-primary " style="width:80px;" type="submit" value="查   询"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th style="text-align:center">协议号</th>
				<th style="text-align:center">会员信息</th>
				<th style="text-align:center">银行卡信息</th>
				<th style="text-align:center">支付插件</th>
				<th style="text-align:center">支付插件名称</th>
				<th style="text-align:center">创建时间</th>
				<shiro:hasPermission name="bank:nfsBankProtocol:edit"><th style="text-align:center">操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="nfsBankProtocol">
			<tr>
				<td style="text-align:center"><a href="${ctx}/bank/nfsBankProtocol/query?id=${nfsBankProtocol.id}">
					${nfsBankProtocol.protocolNo}
				</a></td>
				<td style="text-align:center">
					${nfsBankProtocol.memberId}
				</td>
				<td style="text-align:center">
					${nfsBankProtocol.cardId}
				</td>
				<td style="text-align:center">
					${nfsBankProtocol.paymentPluginId}
				</td>
				<td style="text-align:center">
					${nfsBankProtocol.paymentPluginName}
				</td>
				<td style="text-align:center">
					<fmt:formatDate value="${nfsBankProtocol.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td  style="text-align:center">
				<shiro:hasPermission name="bank:nfsBankProtocol:edit">
    				<a href="${ctx}/bank/nfsBankProtocol/update?id=${nfsBankProtocol.id}">修改</a>
					<a href="${ctx}/bank/nfsBankProtocol/delete?id=${nfsBankProtocol.id}" onclick="return confirmx('确认要删除该签约协议吗？', this.href)">删除</a>
				</shiro:hasPermission>
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>