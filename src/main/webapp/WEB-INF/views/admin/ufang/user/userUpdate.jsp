<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>员工管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			$("#name").focus();
			$("#inputForm").validate({
				rules: {
					
				},
				messages: {
					confirmNewPassword: {equalTo: "输入与上面相同的密码"}
				},
				submitHandler: function(form){
					loading('正在提交，请稍等...');
					form.submit();
				},
				errorContainer: "#messageBox",
				errorPlacement: function(error, element) {
					$("#messageBox").text("输入有误，请先更正。");
					if (element.is(":checkbox")||element.is(":radio")||element.parent().is(".input-append")){
						error.appendTo(element.parent().parent());
					} else {
						error.insertAfter(element);
					}
				}
			});
		});
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${admin}/ufang/user/list">员工列表</a></li>
		<li class="active"><a href="${admin}/ufang/user/update?id=${user.id}">员工修改</a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="user" action="${admin}/ufang/user/update" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>
		<div class="control-group">
			<label class="control-label">归属部门:</label>
			<div class="controls">
                <sys:treeselect id="brn" name="brn.id" value="${user.brn.id}" labelName="brn.brnName" labelValue="${user.brn.brnName}"
					title="部门"  baseUrl="${admin}" url="/ufang/brn/treeData" cssClass="required"  disabled="disabled" notAllowSelectParent="true"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">用户名:</label>
			<div class="controls">
				<form:input path="username" style="width:253px" htmlEscape="false" disabled="true" maxlength="50" class="required userName"/>
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
				<form:input path="empNam" style="width:253px" htmlEscape="false" maxlength="50" class="required"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">邮箱地址:</label>
			<div class="controls">
				<form:input path="email" style="width:253px" htmlEscape="false" maxlength="50" class="email"/>
			</div>
		</div>

		<div class="control-group">
			<label class="control-label">员工角色:</label>
			<div class="controls">
				<form:checkboxes path="roleIdList" items="${allRoles}" itemLabel="roleName" itemValue="id" htmlEscape="false" class="required"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">是否可用:</label>
			<div class="controls">
			<form:radiobuttons path="isEnabled" items="${fns:getDictList('trueOrFalse')}" itemLabel="label" itemValue="value" htmlEscape="false" class="required"/>
	        <span class="help-inline"><font color="red">*</font> </span>
	        </div>
	    </div>    
	    <div class="control-group">
			<label class="control-label">是否锁定:</label>
			<div class="controls">
			<form:radiobuttons path="isLocked" items="${fns:getDictList('trueOrFalse')}" itemLabel="label" itemValue="value"  htmlEscape="false" class="required"/>
			<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">备注:</label>
			<div class="controls">
				<form:textarea path="rmk" htmlEscape="false" rows="3" maxlength="200" class="input-xlarge"/>
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
			<shiro:hasPermission name="ufang:user:edit"><input id="btnSubmit" style="width:80px;"  class="btn btn-primary" type="submit" value="保   存"/>&nbsp;</shiro:hasPermission>
			<label style="text-align:right;width:50px"></label>
			<input id="btnCancel" class="btn btn-primary" type="button" style="width:80px;" value="返 回" onclick="history.go(-1)"/>	
		</div>

	</form:form>
</body>
</html>