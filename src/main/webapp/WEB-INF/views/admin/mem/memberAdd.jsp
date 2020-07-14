<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>会员管理管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			//$("#name").focus();
			$("#inputForm").validate({
				onfocusout: function(element){
			        $(element).valid();
			    },
				rules: {
					username: {remote: "${ctx}/member/check_username" }
				},
				messages: {
					username: {remote: "用户名已存在!"}
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
		//				error.insertAfter(element);
						error.appendTo(element.next());
					}
				}
			});
		});
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/member/list">会员列表</a></li>
		<shiro:hasPermission name="mem:member:edit">
		<li class="active"><a href="${ctx}/member/add?id=${member.id}">会员添加</a></li>
		</shiro:hasPermission>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="member" action="${ctx}/member/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>		
		<div class="control-group">
			<label style="text-align:right;width:150px">用户名：</label>
			<form:input path="username" style="width:230px" htmlEscape="false" maxlength="64" class="input-xlarge required"/>
			<span class="help-inline"  style="width:150px"><font color="red">*</font> </span>
		</div>
		<div class="control-group">
			<label style="text-align:right;width:150px">会员姓名：</label>
			<form:input path="memberName" style="width:230px" htmlEscape="false" maxlength="64" class="input-xlarge "/>
		
			<label style="text-align:right;width:150px">会员昵称：</label>
			<form:input path="nickname" style="width:230px" htmlEscape="false" maxlength="64" class="input-xlarge "/>
			<span class="help-inline"  style="width:150px"></span>	
		</div>
		<div class="control-group">
			<label style="text-align:right;width:150px">会员等级：</label>
			<form:select path="memberRank.rankNo" style="width:245px" class="input-xlarge ">
				<form:option value="" label="请选择"/>
				<form:options items="${fns:getMemRanks()}" itemLabel="rankName" itemValue="rankNo" htmlEscape="false"/>
			</form:select>

			<label style="text-align:right;width:150px">会员性别：</label>
			<form:radiobuttons path="gender" items="${fns:getDictList('gender')}" itemLabel="label" itemValue="value"  htmlEscape="false" class="required"/>
			<span class="help-inline"  style="width:150px"><font color="red">*</font></span>	
		</div>
		<div class="control-group">
			<label style="text-align:right;width:150px">手机号码：</label>
			<form:input path="mobilephone" style="width:230px" htmlEscape="false" maxlength="11" class="input-xlarge "/>

			<label style="text-align:right;width:150px">电话号码：</label>
			<form:input path="telephone" style="width:230px" htmlEscape="false" maxlength="13" class="input-xlarge "/>
			<span class="help-inline"  style="width:150px"></span>	
		</div>
		<div class="control-group">
			<label style="text-align:right;width:150px">邮箱地址：</label>
			<form:input path="email" style="width:230px" htmlEscape="false" maxlength="128" class="input-xlarge "/>
			<span class="help-inline"  style="width:150px"></span>	
		</div>
		<div class="control-group">
			<label style="text-align:right;width:150px">是否可用：</label>
			<form:radiobuttons path="isEnabled" items="${fns:getDictList('trueOrFalse')}" itemLabel="label" itemValue="value" htmlEscape="false" class="required"/>
	        <span class="help-inline"  style="width:168px"><font color="red">*</font> </span>
			<label style="text-align:right;width:150px">是否锁定：</label>
			<form:radiobuttons path="isLocked" items="${fns:getDictList('trueOrFalse')}" itemLabel="label" itemValue="value"  htmlEscape="false" class="required"/>
			<span class="help-inline"  style="width:150px"><font color="red">*</font> </span>
		</div>
		<div class="form-actions">
			<shiro:hasPermission name="mem:member:edit">
			<input id="btnSubmit" class="btn btn-primary" style="width:80px;" type="submit" value="保   存"/>&nbsp;</shiro:hasPermission>
			<label style="text-align:right;width:50px"></label>
			<input id="btnCancel" class="btn btn-primary" type="button" style="width:80px;" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>