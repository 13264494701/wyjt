<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>信用报告原始数据表管理</title>
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
		<li class="active"><a href="${ctx}/ca/rcCaSourceData/">信用报告原始数据表列表</a></li>
		<shiro:hasPermission name="ca:rcCaSourceData:edit"><li><a href="${ctx}/ca/rcCaSourceData/add">信用报告原始数据表添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="rcCaSourceData" action="${ctx}/ca/rcCaSourceData/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label style="text-align:right;width:150px">member_id：</label>
				<form:input path="memberId" htmlEscape="false" maxlength="20" class="input-medium"/>
			</li>
			<li><label style="text-align:right;width:150px">数据类型：</label>
				<form:select path="type"  style="width:177px" class="input-medium">
					<form:option value="" label="请选择"/>
					<form:options items="${fns:getDictList('')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</li>
			<li><label style="text-align:right;width:150px">数据状态：</label>
				<form:select path="status"  style="width:177px" class="input-medium">
					<form:option value="" label="请选择"/>
					<form:options items="${fns:getDictList('')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</li>
			<li><label style="text-align:right;width:150px">create_time：</label>
				<input name="createTime" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${rcCaSourceData.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"
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
				<th style="text-align:center">member_id</th>
				<th style="text-align:center">数据类型</th>
				<th style="text-align:center">数据状态</th>
				<th style="text-align:center">create_time</th>
				<shiro:hasPermission name="ca:rcCaSourceData:edit"><th style="text-align:center">操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="rcCaSourceData">
			<tr>
				<td style="text-align:center"><a href="${ctx}/ca/rcCaSourceData/query?id=${rcCaSourceData.id}">
					${rcCaSourceData.memberId}
				</a></td>
				<td style="text-align:center">
					${fns:getDictLabel(rcCaSourceData.type, '', '')}
				</td>
				<td style="text-align:center">
					${fns:getDictLabel(rcCaSourceData.status, '', '')}
				</td>
				<td style="text-align:center">
					<fmt:formatDate value="${rcCaSourceData.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td  style="text-align:center">
				<shiro:hasPermission name="ca:rcCaSourceData:edit">
    				<a href="${ctx}/ca/rcCaSourceData/update?id=${rcCaSourceData.id}">修改</a>
					<a href="${ctx}/ca/rcCaSourceData/delete?id=${rcCaSourceData.id}" onclick="return confirmx('确认要删除该信用报告原始数据表吗？', this.href)">删除</a>
				</shiro:hasPermission>
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>