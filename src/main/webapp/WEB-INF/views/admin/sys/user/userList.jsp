<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>员工管理</title>
<meta name="decorator" content="default" />
<script type="text/javascript">
	$(document).ready(function() {
		$("#btnExport").click(function() {
			top.$.jBox.confirm("确认要导出员工数据吗？", "系统提示", function(v, h, f) {
				if (v == "ok") {
					$("#searchForm").attr("action", "${admin}/sys/user/export");
					$("#searchForm").submit();
				}
			}, {
				buttonsFocus : 1
			});
			top.$('.jbox-body .jbox-icon').css('top', '55px');
		});
		$("#btnImport").click(function() {
			$.jBox($("#importBox").html(), {
				title : "导入数据",
				buttons : {
					"关闭" : true
				},
				bottomText : "导入文件不能超过5M，仅允许导入“xls”或“xlsx”格式文件！"
			});
		});
	});
	function page(n, s) {
		if (n)
			$("#pageNo").val(n);
		if (s)
			$("#pageSize").val(s);
		$("#searchForm").attr("action", "${admin}/sys/user/list");
		$("#searchForm").submit();
		return false;
	}
</script>
</head>
<body>
	<div id="importBox" class="hide">
		<form id="importForm" action="${admin}/sys/user/import" method="post"
			enctype="multipart/form-data" class="form-search"
			style="padding-left: 20px; text-align: center;" onsubmit="loading('正在导入，请稍等...');">
			<br/> 
			<input id="uploadFile" name="file" type="file" style="width: 330px" />
			<br/>
			<br/> 
			<input id="btnImportSubmit" class="btn btn-primary" type="submit" value="   导    入   " /> 
			<a href="${admin}/sys/user/import/template">下载模板</a>
		</form>
	</div>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${admin}/sys/user/list">员工列表</a></li>
		<shiro:hasPermission name="sys:user:edit">
			<li><a href="${admin}/sys/user/add">员工添加</a></li>
		</shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="user"
		action="${admin}/sys/user/list" method="post"
		class="breadcrumb form-search ">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}" />
		<input id="pageSize" name="pageSize" type="hidden"value="${page.pageSize}" />
		<sys:tableSort id="orderBy" name="orderBy" value="${page.orderBy}" callback="page();" />
		<ul class="ul-form">
			<li><label>登录名称：</label>
				<form:input path="loginName" htmlEscape="false" maxlength="50" class="input-medium" />
			</li>
			<li><label>员工姓名：</label>
				<form:input path="empNam" htmlEscape="false" maxlength="50" class="input-medium" />
			</li>
			<li class="clearfix"></li>
			<li><label>归属机构：</label>
			<sys:treeselect id="brn" name="brn.id" value="${user.brn.id}" labelName="brn.brnName" labelValue="${user.brn.brnName}" title="机构"
					baseUrl="${admin}" url="/sys/brn/treeData?type=2" cssClass="input-small" allowClear="true" notAllowSelectParent="false" />
			</li>
			<li class="btns">
				<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询" onclick="return page();" /> 
				<input id="btnExport" class="btn btn-primary" type="button" value="导出" /> 
				<input id="btnImport" class="btn btn-primary" type="button" value="导入" />
			</li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}" />
	<table id="contentTable"
		class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th style="text-align:center">归属公司</th>
				<th style="text-align:center">归属部门</th>
				<th style="text-align:center" class="sort-column login_name">登录名</th>
				<th style="text-align:center" class="sort-column name">姓名</th>
				<th style="text-align:center">电话号码</th>
				<th style="text-align:center">手机号码</th>
				<th style="text-align:center">邮箱地址</th>
				<shiro:hasPermission name="sys:user:edit"><th style="text-align:center">操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${page.list}" var="user">
				<tr>
				    <td style="text-align:center">${user.loginName=='999999'?'总部平台':user.brn.parent.brnName}</td>
					<td style="text-align:center">${user.brn.brnName}</td>
					<td style="text-align:center"><a href="${admin}/sys/user/query?id=${user.id}">${user.loginName}</a></td>
					<td style="text-align:center">${user.empNam}</td>
					<td style="text-align:center">${user.phone}</td>
					<td style="text-align:center">${user.mobile}</td>
					<td style="text-align:center">${user.email}</td>
					<c:choose>
					<c:when test="${user.loginName=='999999'}">
					<td style="text-align: center"><shiro:hasPermission name="sys:user:edit">
							<a href="${admin}/sys/user/update?id=${user.id}">修改</a>
							<a href="${admin}/sys/user/resetPwd?id=${user.id}"onclick="return confirmx('确认要重置该员工密码吗？', this.href)">密码重置</a>
						</shiro:hasPermission></td>
					</c:when>
					<c:otherwise>
					<td style="text-align: center"><shiro:hasPermission name="sys:user:edit">
						<a href="${admin}/sys/user/update?id=${user.id}">修改</a>
						<a href="${admin}/sys/user/resetPwd?id=${user.id}"onclick="return confirmx('确认要重置该员工密码吗？', this.href)">密码重置</a>
						<a href="${admin}/sys/user/delete?id=${user.id}" onclick="return confirmx('确认要删除该员工吗？', this.href)">删除</a>
					 </shiro:hasPermission></td>
					 </c:otherwise>
					</c:choose>										
				</tr>
			</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>