<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>个人信息</title>
<meta name="decorator" content="default" />
<script type="text/javascript">
	$(document).ready(
			function() {
				$("#inputForm")
						.validate(
								{
									submitHandler : function(form) {
										loading('正在提交，请稍等...');
										form.submit();
									},
									errorContainer : "#messageBox",
									errorPlacement : function(error, element) {
										$("#messageBox").text("输入有误，请先更正。");
										if (element.is(":checkbox")
												|| element.is(":radio")
												|| element.parent().is(
														".input-append")) {
											error.appendTo(element.parent()
													.parent());
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
		<li class="active"><a href="${ctx}/sys/mine/info">个人信息</a></li>
		<li><a href="${ctx}/sys/mine/updatePwd">修改密码</a></li>
	</ul>
	<br />
	<form:form id="inputForm" modelAttribute="user"
		action="${ctx}/sys/mine/info" method="post" class="form-horizontal">
		<%--
		<form:hidden path="email" htmlEscape="false" maxlength="255" class="input-xlarge"/>
		<sys:ckfinder input="email" type="files" uploadPath="/mytask" selectMultiple="false"/> --%>
		<sys:message content="${message}" />
		<div class="control-group">
			<label class="control-label">头像:</label>
			<div class="controls">
				<form:hidden id="nameImage" path="photo" htmlEscape="false"
					maxlength="255" class="input-xlarge" />
				<sys:ckfinder input="nameImage" type="images" uploadPath="/photo"
					selectMultiple="false" maxWidth="100" maxHeight="100" />
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">归属机构:</label>
			<div class="controls">
				<label class="lbl">${user.brn.brnName}</label>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">员工编号:</label>
			<div class="controls">
				<form:input path="empNo" htmlEscape="false" disabled="true" maxlength="50" class="required"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">员工姓名:</label>
			<div class="controls">
				<form:input path="empNam" htmlEscape="false" maxlength="50"
					class="required" readonly="true" />
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">邮箱地址:</label>
			<div class="controls">
				<form:input path="email" htmlEscape="false" maxlength="50"
					class="email" />
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">电话号码:</label>
			<div class="controls">
				<form:input path="phone" htmlEscape="false" maxlength="50" />
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">手机号码:</label>
			<div class="controls">
				<form:input path="mobile" htmlEscape="false" maxlength="50" />
			</div>
		</div>
<!-- 		<div class="control-group"> -->
<!-- 			<label class="control-label">用户角色:</label> -->
<!-- 			<div class="controls"> -->
<%-- 				<label class="lbl">${user.roleNames}</label> --%>
<!-- 			</div> -->
<!-- 		</div> -->
		<div class="control-group">
			<label class="control-label">上次登录:</label>
			<div class="controls">
				<label class="lbl">IP:
					${user.oldLoginIp}&nbsp;&nbsp;&nbsp;&nbsp;时间：<fmt:formatDate
						value="${user.oldLoginDate}" type="both" dateStyle="full" />
				</label>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">备注:</label>
			<div class="controls">
				<form:textarea path="rmk" htmlEscape="false" rows="3"
					maxlength="200" class="input-xlarge" />
			</div>
		</div>
		<div class="form-actions">
			<input id="btnSubmit" style="width:80px;"  class="btn btn-primary" type="submit" value="保  存" />
		</div>
	</form:form>
</body>
</html>