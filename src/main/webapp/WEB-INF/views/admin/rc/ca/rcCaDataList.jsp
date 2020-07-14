<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>信用报告数据表管理</title>
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
		<li class="active"><a href="${ctx}/ca/rcCaData/">信用报告数据表列表</a></li>
	</ul>
	<form:form id="searchForm" modelAttribute="rcCaData" action="${ctx}/ca/rcCaData/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label style="text-align:right;width:150px">会员Id：</label>
				<form:input path="memberId" htmlEscape="false" maxlength="20" class="input-medium"/>
			</li>
			<li><label style="text-align:right;width:150px">数据来源类型 ：</label>
				<form:select path="type"  style="width:177px" class="input-medium">
					<form:option value="" label="请选择"/>
					<form:options items="${fns:getDictList('caType')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
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
				<th style="text-align:center">会员Id</th>
				<th style="text-align:center">数据来源类型 </th>
				<th style="text-align:center">数据内容</th>
				<th style="text-align:center">创建时间</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="rcCaData">
			<tr>
				<td style="text-align:center"><a href="${ctx}/ca/rcCaData/query?id=${rcCaData.id}">
					${rcCaData.memberId}
				</a></td>
				<td style="text-align:center">
					${fns:getDictLabel(rcCaData.type,'caType', '')}
				</td>
				<td style="text-align:left">
					${rcCaData.content}
				</td>
				<td style="text-align:center">
					<fmt:formatDate value="${rcCaData.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>