<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>审核记录管理</title>
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
		<li class="active"><a href="${ctx}/check/nfsCheckRecord/">审核记录列表</a></li>
		<%-- <shiro:hasPermission name="check:nfsCheckRecord:edit"><li><a href="${ctx}/check/nfsCheckRecord/add">审核记录添加</a></li></shiro:hasPermission> --%>
	</ul>
	<form:form id="searchForm" modelAttribute="nfsCheckRecord" action="${ctx}/check/nfsCheckRecord/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label style="text-align:right;width:150px">原业务类型：</label>
				<form:select path="orgType"  style="width:177px" class="input-medium">
					<form:option value="" label="请选择"/>
					<form:options items="${fns:getDictList('checkRecordOrgType')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</li>
			<li><label style="text-align:right;width:150px">原业务编号：</label>
				<form:input path="orgId" htmlEscape="false" maxlength="20" class="input-medium"/>
			</li>
			<li><label style="text-align:right;width:150px">审核状态：</label>
				<form:select path="checkStatus"  style="width:177px" class="input-medium">
					<form:option value="" label="请选择"/>
					<form:options items="${fns:getDictList('checkRecordCheckStatus')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</li>
			<li><label style="text-align:right;width:150px">审核时间：</label>
				<input name="beginTime" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${beginTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:true});"/>
				<label style="text-align:center;width:30px">至：</label>
				<input name="endTime" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${endTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:true});"/>
			</li>
			<%-- <li><label style="text-align:right;width:150px">复审状态：</label>
				<form:select path="recheckStatus"  style="width:177px" class="input-medium">
					<form:option value="" label="请选择"/>
					<form:options items="${fns:getDictList('checkRecordCheckStatus')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</li> --%>
			<li class="btns"><label style="text-align:right;width:50px"></label><input id="btnSubmit" class="btn btn-primary " style="width:80px;" type="submit" value="查   询"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th style="text-align:center">原业务编号</th>
				<th style="text-align:center">原业务类型</th>
				<th style="text-align:center">审核人姓名</th>
				<th style="text-align:center">审核时间</th>
				<th style="text-align:center">审核状态</th>
				<th style="text-align:center">审核意见</th>
				<!-- <th style="text-align:center">复审人姓名</th>
				<th style="text-align:center">复审时间</th>
				<th style="text-align:center">复审状态</th>
				<th style="text-align:center">复审意见</th> -->
				<%-- <shiro:hasPermission name="check:nfsCheckRecord:edit"><th style="text-align:center">操作</th></shiro:hasPermission> --%>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="nfsCheckRecord">
			<tr>
				<td style="text-align:center"><a href="${ctx}/check/nfsCheckRecord/queryOrgOrder?orgId=${nfsCheckRecord.orgId}">
					${nfsCheckRecord.orgId}
					</a>
				</td>
				<td style="text-align:center">
					${fns:getDictLabel(nfsCheckRecord.orgType, 'checkRecordOrgType', '')}
				</td>
				<td style="text-align:center">
					${nfsCheckRecord.checkerName}
				</td>
				<td style="text-align:center">
					<fmt:formatDate value="${nfsCheckRecord.checkTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td style="text-align:center">
					${fns:getDictLabel(nfsCheckRecord.checkStatus, 'checkRecordCheckStatus', '')}
				</td>
				<td style="text-align:center">
					${nfsCheckRecord.checkDesc}
				</td>
				<%-- <td style="text-align:center">
					${nfsCheckRecord.recheckerName}
				</td>
				<td style="text-align:center">
					<fmt:formatDate value="${nfsCheckRecord.recheckTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td style="text-align:center">
					${fns:getDictLabel(nfsCheckRecord.recheckStatus, 'checkRecordCheckStatus', '')}
				</td>
				<td style="text-align:center">
					${nfsCheckRecord.recheckDesc}
				</td> --%>
				<%--<td  style="text-align:center">
				 <shiro:hasPermission name="check:nfsCheckRecord:edit">
    				<a href="${ctx}/check/nfsCheckRecord/update?id=${nfsCheckRecord.id}">修改</a>
					<a href="${ctx}/check/nfsCheckRecord/delete?id=${nfsCheckRecord.id}" onclick="return confirmx('确认要删除该审核记录吗？', this.href)">删除</a>
				</shiro:hasPermission>
				</td> --%>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>