<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>员工管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">

	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${admin}/sys/user/list">员工列表</a></li>
		<li class="active"><a href="${admin}/sys/user/form?id=${user.id}">员工基本信息<shiro:hasPermission name="sys:user:edit">${not empty user.id?'':'添加'}</shiro:hasPermission><shiro:lacksPermission name="sys:user:edit">查看</shiro:lacksPermission></a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="user" action="${admin}/sys/user/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>
		<div class="control-group">
			<label class="control-label">头像:</label>
			<div class="controls">
				<form:hidden id="nameImage" path="photo" htmlEscape="false" maxlength="255" class="input-xlarge"/>
				<sys:ckfinder input="nameImage" readonly="true" type="images" uploadPath="/photo" selectMultiple="false" maxWidth="100" maxHeight="100"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">归属部门:</label>
			<div class="controls">
                <sys:treeselect id="brn" name="brn.id" value="${user.brn.id}" labelName="brn.brnName" labelValue="${user.brn.brnName}"
					title="部门" baseUrl="${admin}" url="/sys/brn/treeData?type=2" cssClass="required"  disabled="disabled" notAllowSelectParent="true"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">登录名称:</label>
			<div class="controls">
				<form:input path="loginName" style="width:253px" htmlEscape="false" disabled="true" maxlength="50" class="required userName"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">员工编号:</label>
			<div class="controls">
				<form:input path="empNo" style="width:253px" htmlEscape="false" disabled="true" maxlength="50" class="required"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">员工姓名:</label>
			<div class="controls">
				<form:input path="empNam" style="width:253px" htmlEscape="false" readonly="true" maxlength="50" class="required"/>
			</div>
		</div>

		<div class="control-group">
			<label class="control-label">邮箱地址:</label>
			<div class="controls">
				<form:input path="email" style="width:253px" htmlEscape="false" readonly="true" maxlength="100" class="email"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">电话号码:</label>
			<div class="controls">
				<form:input path="phone"  style="width:253px" readonly="true" htmlEscape="false" maxlength="100"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">手机号码:</label>
			<div class="controls">
				<form:input path="mobile"  style="width:253px" readonly="true" htmlEscape="false" maxlength="100"/>
			</div>
		</div>

		<div class="control-group">
			<label class="control-label">员工角色:</label>
			<div class="controls">
				<form:checkboxes  disabled="true" path="roleIdList" items="${allRoles}" itemLabel="roleName" itemValue="id" htmlEscape="false" class="required"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">备注:</label>
			<div class="controls">
				<form:textarea path="rmk"  disabled="true" htmlEscape="false" rows="3" maxlength="200" class="input-xlarge"/>
			</div>
		</div>
		<c:if test="${not empty user.id}">
			<div class="control-group">
				<label class="control-label">创建时间:</label>
				<div class="controls">
					<label class="lbl"><fmt:formatDate value="${user.createTime}" type="both" dateStyle="full"/></label>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">最后登陆:</label>
				<div class="controls">
					<label class="lbl">IP: ${user.loginIp}&nbsp;&nbsp;&nbsp;&nbsp;时间：<fmt:formatDate value="${user.loginDate}" type="both" dateStyle="full"/></label>
				</div>
			</div>
		</c:if>
		<div class="form-actions">
			<input id="btnCancel" class="btn btn-primary" type="button" style="width:80px;" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>