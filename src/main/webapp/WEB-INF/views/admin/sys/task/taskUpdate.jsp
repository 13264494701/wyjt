<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>定时任务管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			//$("#name").focus();
		//	alert('${nfsCiMain.nickName}');
			$("#inputForm").validate({
				onfocusout: function(element){
			        $(element).valid();
			    },
/* 				rules: {
					nickName: {remote: "${ctx}/cis/nfsCiMain/validationNickName" },
					phoneNo: {remote: "${ctx}/cis/nfsCiMain/validationPhoneNo" },
					email: {remote: "${ctx}/cis/nfsCiMain/validationEmail" }
				},
				messages: {
					nickName: {remote: "用户已注册"},
					phoneNo: {remote: "手机号码已存在"},
					email: {remote: "邮箱已存在"}
				}, */
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
		<li ><a href="${ctx}/task/list">任务列表</a></li>
		<%-- <li><a href="${ctx}/task/runningTasks">运行中的任务</a></li> --%>
		<li class="active"><a href="${ctx}/task/update?name=${scheduleJob.name}&group=${scheduleJob.group}">任务修改</a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="scheduleJob" action="${ctx}/task/update" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>		
		<div class="control-group">
			<label class="control-label">任务名称（唯一）：</label>
			<div class="controls">
				<form:input path="name" htmlEscape="false" readonly="true"  maxlength="64"  onBlur="true" class="input-xlarge required"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">任务组名：</label>
			<div class="controls">
				<form:input path="group"   htmlEscape="false" readonly="true"  maxlength="64" required="true"  class="input-xlarge required"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">cron表达式"：</label>
			<div class="controls">
				<form:input path="cronExpression" htmlEscape="false"   class="input-xlarge required"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">执行任务类全路径名：</label>
			<div class="controls">
				<form:input path="className" htmlEscape="false" readonly="true"  class="input-xlarge "/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">描述：</label>
			<div class="controls">
				<form:input path="description" htmlEscape="false"   class="input-xlarge "/>
			</div>
		</div>

		<div class="form-actions">
            <input id="btnSubmit" class="btn btn-primary" style="width:80px;" type="submit" value="保 存"/>&nbsp;
			<input id="btnCancel" class="btn btn-primary" type="button" style="width:80px;" value="返回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>