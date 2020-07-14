<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>数据导入管理</title>
	<meta name="decorator" content="default"/>
	<script src="${ctxStatic}/jquery/jquery.lazyload.js" type="text/javascript"></script>
	<script src="${ctxStatic}/artdialog/js/dialog-plus.js"></script>
    <link href="${ctxStatic}/artdialog/css/dialog.css" rel="stylesheet" />
	<script type="text/javascript">

		$(document).ready(function() {

		});
		function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#searchForm").submit();
        	return false;
        }

		function showDataTask(obj) {
			var dataId = $(obj).closest("tr").attr("id");
			var url = '${admin}/sysDataTask?data.id='+dataId;
			var d = dialog({
		    	id: "dataTask",
		    	title: '任务明细',
		    	url:url,
		    	width: 1000,
		    	height: 500,
		    	padding: 0,
		    	drag:true,
		    	quickClose: true,
                onshow: function() {
                     console.log('onshow');
                 },
                 oniframeload: function() {
                     console.log('oniframeload');
                 },
                 onclose: function() {
                     console.log('onclose');
                 },
                 onremove: function() {
                     console.log('onremove');
                 }
		    }).show();	
		}
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/sysDataImport/">数据导入列表</a></li>
		<shiro:hasPermission name="data:sysDataImport:edit"><li><a href="${ctx}/sysDataImport/add">数据导入添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="sysDataImport" action="${ctx}/sysDataImport/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label style="text-align:right;width:150px">名称：</label>
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
				<th style="text-align:center">最小ID</th>
				<th style="text-align:center">最大ID</th>
				<th style="text-align:center">总数量</th>
				<th style="text-align:center">已完成数量</th>
				<th style="text-align:center">每次处理数量</th>
				<th style="text-align:center">创建时间</th>
				<th style="text-align:center">更新时间</th>
				<th style="text-align:center">名称</th>
				<th style="text-align:center">处理器</th>
				<shiro:hasPermission name="data:sysDataImport:edit"><th style="text-align:center">操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="sysDataImport" varStatus="index">
		    <c:if test="${index.count%2==0}">
			   <tr id="${sysDataImport.id}" style="background-color:#e2dede;">
			</c:if> 
			<c:if test="${index.count%2==1}">
			   <tr id="${sysDataImport.id}" >
			</c:if> 
				<td style="text-align:center">
					${sysDataImport.minId}
				</td>
				<td style="text-align:center">
					${sysDataImport.maxId}
				</td>
				<td style="text-align:center">
					${sysDataImport.totalQuantity}
				</td>
				<td style="text-align:center">
					${sysDataImport.importQuantity}
				</td>
				<td style="text-align:center">
					${sysDataImport.pQuantity}
				</td>

				<td style="text-align:center">
					<fmt:formatDate value="${sysDataImport.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td style="text-align:center">
					<fmt:formatDate value="${sysDataImport.updateTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td style="text-align:center">
					${sysDataImport.name}
				</td>
				<td style="text-align:center">
					${sysDataImport.handler}
				</td>
				<td  style="text-align:center">
				<shiro:hasPermission name="data:sysDataImport:edit">
				    <a href="${ctx}/sysDataImport/createTask?id=${sysDataImport.id}">创建任务</a>
				    <br>
				    <a href="${ctx}/sysDataImport/clearTask?id=${sysDataImport.id}">清空任务</a>
				    <br>
				    <a href ="javascript:void(0);" onclick="showDataTask(this)">任务明细</a>
				    <br>
    				<a href="${ctx}/sysDataImport/update?id=${sysDataImport.id}">修改</a>
					<a href="${ctx}/sysDataImport/delete?id=${sysDataImport.id}" onclick="return confirmx('确认要删除该数据导入吗？', this.href)">删除</a>
				</shiro:hasPermission>
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>