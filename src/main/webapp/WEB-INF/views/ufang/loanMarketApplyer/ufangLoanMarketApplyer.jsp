<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>优放贷申请人管理</title>
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
		<li class="active"><a href="${ctx}/ufangdebt/applyer/ufangLoanMarketApplyer/">优放贷申请人列表</a></li>
		<shiro:hasPermission name="ufangdebt:applyer:ufangDebtPublicApplyer:edit"><li><a href="${ctx}/ufangdebt/applyer/ufangDebtPublicApplyer/add">优放贷申请人添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="ufangDebtPublicApplyer" action="${ctx}/ufangdebt/applyer/ufangDebtPublicApplyer/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label style="text-align:right;width:150px">手机号码：</label>
				<form:input path="phoneNo" htmlEscape="false" maxlength="11" class="input-medium"/>
			</li>
			<li><label style="text-align:right;width:150px">申请人姓名：</label>
				<form:input path="name" htmlEscape="false" maxlength="64" class="input-medium"/>
			</li>
			<li class="btns"><label style="text-align:right;width:50px"></label><input id="btnSubmit" class="btn btn-primary " style="width:80px;" type="submit" value="查   询"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th style="text-align:center">手机号码</th>
				<th style="text-align:center">申请人姓名</th>
				<th style="text-align:center">身份证号</th>
				<th style="text-align:center">银行卡号</th>
				<th style="text-align:center">微信号</th>
				<th style="text-align:center">实名认证状态 </th>
				<th style="text-align:center">芝麻分认证状态</th>
				<th style="text-align:center">芝麻分</th>
				<th style="text-align:center">运营商认证状态</th>
				<th style="text-align:center">运营商报告查看地址</th>
				<th style="text-align:center">是否app注册用户</th>
				<th style="text-align:center">app注册用户的id</th>
				<th style="text-align:center">描述</th>
				<th style="text-align:center">创建时间</th>
				<shiro:hasPermission name="ufangdebt:applyer:ufangDebtPublicApplyer:edit"><th style="text-align:center">操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="ufangDebtPublicApplyer">
			<tr>
				<td style="text-align:center"><a href="${ctx}/ufangdebt/applyer/ufangDebtPublicApplyer/query?id=${ufangDebtPublicApplyer.id}">
					${ufangDebtPublicApplyer.phoneNo}
				</a></td>
				<td style="text-align:center">
					${ufangDebtPublicApplyer.name}
				</td>
				<td style="text-align:center">
					${ufangDebtPublicApplyer.idNo}
				</td>
				<td style="text-align:center">
					${ufangDebtPublicApplyer.cardNo}
				</td>
				<td style="text-align:center">
					${ufangDebtPublicApplyer.weixinNo}
				</td>
				<td style="text-align:center">
					${fns:getDictLabel(ufangDebtPublicApplyer.realNameStatus, '', '')}
				</td>
				<td style="text-align:center">
					${fns:getDictLabel(ufangDebtPublicApplyer.sesameStatus, '', '')}
				</td>
				<td style="text-align:center">
					${ufangDebtPublicApplyer.sesameScore}
				</td>
				<td style="text-align:center">
					${fns:getDictLabel(ufangDebtPublicApplyer.operatorStatus, '', '')}
				</td>
				<td style="text-align:center">
					${ufangDebtPublicApplyer.operatorReportUrl}
				</td>
				<td style="text-align:center">
					${fns:getDictLabel(ufangDebtPublicApplyer.appRegister, '', '')}
				</td>
				<td style="text-align:center">
					${ufangDebtPublicApplyer.memberId}
				</td>
				<td style="text-align:center">
					${ufangDebtPublicApplyer.rmk}
				</td>
				<td style="text-align:center">
					<fmt:formatDate value="${ufangDebtPublicApplyer.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td  style="text-align:center">
				<shiro:hasPermission name="ufangdebt:applyer:ufangDebtPublicApplyer:edit">
    				<a href="${ctx}/ufangdebt/applyer/ufangDebtPublicApplyer/update?id=${ufangDebtPublicApplyer.id}">修改</a>
					<a href="${ctx}/ufangdebt/applyer/ufangDebtPublicApplyer/delete?id=${ufangDebtPublicApplyer.id}" onclick="return confirmx('确认要删除该优放贷申请人吗？', this.href)">删除</a>
				</shiro:hasPermission>
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>