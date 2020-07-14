<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>资金分发归集管理</title>
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
		<li class="active"><a href="${ufang}/ufangFundDistColl/">资金派发归集列表</a></li>		
	</ul>
	<form:form id="searchForm" modelAttribute="ufangFundDistColl" action="${ufang}/ufangFundDistColl/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">

			<li><label style="text-align:right;width:150px">员工姓名：</label>
				<form:input path="user.empNam" htmlEscape="false" maxlength="20" class="input-medium"/>
			</li>
			<li><label style="text-align:right;width:150px">交易类型：</label>
				<form:select path="type"  style="width:177px" class="input-medium">
					<form:option value="" label="请选择"/>
					<form:options items="${fns:getDictList('distCollType')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</li>
			<li><label style="text-align:right;width:150px">交易状态：</label>
				<form:select path="status"  style="width:177px" class="input-medium">
					<form:option value="" label="请选择"/>
					<form:options items="${fns:getDictList('trxSts')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
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
			    <th style="text-align:center">交易编号</th>
				<th style="text-align:center">用户</th>
				<th style="text-align:center">交易类型</th>
				<th style="text-align:center">交易金额</th>
				<th style="text-align:center">交易状态</th>
				<th style="text-align:center">生成时间</th>				
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="ufangFundDistColl">
			<tr>
			    <td style="text-align:center">
					${ufangFundDistColl.id}
				</td>
				<td style="text-align:center">
					${ufangFundDistColl.user.empNam}
				</td>
				<td style="text-align:center">
					${fns:getDictLabel(ufangFundDistColl.type, 'distCollType', '')}
				</td>
				<td style="text-align:center">
					${ufangFundDistColl.amount}元
				</td>
				<td style="text-align:center">
					${fns:getDictLabel(ufangFundDistColl.status, 'trxSts', '')}
				</td>
				<td style="text-align:center">
					<fmt:formatDate value="${ufangFundDistColl.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>