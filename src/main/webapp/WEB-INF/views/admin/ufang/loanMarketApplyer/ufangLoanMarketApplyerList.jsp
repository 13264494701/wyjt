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
		<li class="active"><a href="${ctx}/loanMarketApplyer/">优放贷申请人列表</a></li>
	</ul>
	<form:form id="searchForm" modelAttribute="ufangLoanMarketApplyer" action="${ctx}/loanMarketApplyer/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label style="text-align:right;width:100px">手机号码</label>
				<form:input path="phoneNo" htmlEscape="false" maxlength="11" class="input-medium"/>
			</li>
			<li><label style="text-align:right;width:100px">姓名</label>
				<form:input path="name" htmlEscape="false" maxlength="64" class="input-medium"/>
			</li>
			<li><label style="text-align:right;width:100px">归属贷超：</label>
				<form:select path="ufangLoanMarket.id"  style="width:177px" class="input-medium">
				   <form:option value="" label="请选择"/>
				   <form:options items="${fns:findMarketList()}" itemLabel="name" itemValue="id" htmlEscape="false"/>
				</form:select>
			</li>
			<li><label style="text-align:right;width:150px">申请时间：</label>
				<input name="beginTime" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate required"
					value="<fmt:formatDate value="${ufangLoanMarketApplyer.beginTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',startDate:'%y-%M-%d 00:00:00',isShowClear:true});"/>
				<label style="text-align:center;width:30px">至：</label>
				<input name="endTime" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate required"
					value="<fmt:formatDate value="${ufangLoanMarketApplyer.endTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',startDate:'%y-%M-%d 00:00:00',isShowClear:true});"/>
			</li>
			<li class="btns"><label style="text-align:right;width:50px"></label><input id="btnSubmit" class="btn btn-primary " style="width:80px;" type="submit" value="查   询"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
			    <th style="text-align:center">归属贷超</th>
				<th style="text-align:center">手机号码</th>
				<th style="text-align:center">姓名</th>
				<th style="text-align:center">实名认证 </th>
				<th style="text-align:center">芝麻分认证</th>
				<th style="text-align:center">芝麻分</th>
				<th style="text-align:center">运营商认证</th>
				<th style="text-align:center">运营商报告</th>
				<th style="text-align:center">APP注册用户</th>
				<th style="text-align:center">注册Id</th>
				<th style="text-align:center">申请地区</th>
				<th style="text-align:center">申请时间</th>	
				<th style="text-align:center">风险画像</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="ufangLoanMarketApplyer">
			<tr>
			    <td style="text-align:center">
					${ufangLoanMarketApplyer.ufangLoanMarket.name}
				</td>
				<td style="text-align:center">
					${ufangLoanMarketApplyer.phoneNo}
				</a></td>
				<td style="text-align:center">
					${ufangLoanMarketApplyer.name}
				</td>
				<td style="text-align:center">
					${fns:getDictLabel(ufangLoanMarketApplyer.realNameStatus, 'realNameStatus', '')}
				</td>
				<td style="text-align:center">
					${fns:getDictLabel(ufangLoanMarketApplyer.sesameStatus, 'sesameStatus', '')}
				</td>
				<td style="text-align:center">
					${ufangLoanMarketApplyer.sesameScore}
				</td>
				<td style="text-align:center">
					${fns:getDictLabel(ufangLoanMarketApplyer.operatorStatus, 'operatorStatus', '')}
				</td>
				<td style="text-align:center">
					<c:if test="${ufangLoanMarketApplyer.operatorStatus eq 'authed'}">
						<a href="${ufang}/rcSjmh/report?taskId=${ufangLoanMarketApplyer.reportTaskId}" target="_blank">查看报告</a>
					</c:if>
					<c:if test="${ufangLoanMarketApplyer.operatorStatus eq 'unauth'}">
						未认证
					</c:if>
				</td>
				<td style="text-align:center">
					${fns:getDictLabel(ufangLoanMarketApplyer.appRegister, 'appRegister', '')}
				</td>
				<td style="text-align:center">
					${ufangLoanMarketApplyer.member.id}
				</td>
				<td style="text-align:center">
					${ufangLoanMarketApplyer.applyArea}
				</td>
				<td style="text-align:center">
					<fmt:formatDate value="${ufangLoanMarketApplyer.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td style="text-align:center">
				    <a href ="${admin}/loanMarketApplyer/applyerAnalysis?phoneNo=${ufangLoanMarketApplyer.phoneNo}">借贷分析</a> 
				</td> 
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>