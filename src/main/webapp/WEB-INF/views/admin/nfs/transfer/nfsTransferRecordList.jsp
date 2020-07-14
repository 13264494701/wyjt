<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>转账管理</title>
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
		<li class="active"><a href="${admin}/transferRecord/">转账列表</a></li>		
	</ul>
	<form:form id="searchForm" modelAttribute="nfsTransferRecord" action="${ctx}/transferRecord/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label style="text-align:right;width:150px">付款人：</label>
				<form:input path="member.name" htmlEscape="false" maxlength="20" class="input-medium"/>
			</li>
			<li><label style="text-align:right;width:150px">付款人手机：</label>
				<form:input path="member.username" htmlEscape="false" maxlength="20" class="input-medium"/>
			</li>
			<li><label style="text-align:right;width:150px">收款人：</label>
				<form:input path="friend.name" htmlEscape="false" maxlength="20" class="input-medium"/>
			</li>
			<li><label style="text-align:right;width:150px">收款人手机：</label>
				<form:input path="friend.username" htmlEscape="false" maxlength="20" class="input-medium"/>
			</li>
			<li><label style="text-align:right;width:150px">状态：</label>
				<form:select path="status"  style="width:177px" class="input-medium">
					<form:option value="" label="请选择"/>
					<form:options items="${fns:getDictList('transferStatus')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</li>
			<li><label style="text-align:right;width:80px">转账时间：</label>
				<input name="beginTime" type="text"  maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${nfsTransferRecord.beginTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:true});"/> - 
				<input name="endTime" type="text"  maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${nfsTransferRecord.endTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:true});"/>
			</li>	
			<li class="btns"><label style="text-align:right;width:50px"></label><input id="btnSubmit" class="btn btn-primary " style="width:80px;" type="submit" value="查   询"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>			
				<th style="text-align:center">付款人</th>
				<th style="text-align:center">付款人手机</th>
				<th style="text-align:center">收款人</th>
				<th style="text-align:center">收款人手机</th>
				<th style="text-align:center">转账金额</th>
				<th style="text-align:center">转账状态</th>
				<th style="text-align:center">转账时间</th>
				<th style="text-align:center">失败原因</th>		
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="transferRecord">
			<tr>
				<td style="text-align:center"><a href="${ctx}/member/query?id=${transferRecord.member.id}">
					${transferRecord.member.name}
				</a></td>
				<td style="text-align:center">
					${transferRecord.friend.username}
				</td>
				<td style="text-align:center"><a href="${ctx}/member/query?id=${transferRecord.friend.id}">
					${transferRecord.friend.name}
				</a></td>
				<td style="text-align:center">
					${transferRecord.friend.username}
				</td>
				<td style="text-align:center">
					${fns:decimalToStr(transferRecord.amount,2)}
				</td>
				<td style="text-align:center">
					${fns:getDictLabel(transferRecord.status, 'transferStatus', '')}
				</td>
				<td style="text-align:center">
					<fmt:formatDate value="${transferRecord.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td style="text-align:center">
					${transferRecord.failReason}
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>