<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>好友投诉管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			//$("#name").focus();
			$("#inputForm").validate({
				onfocusout: function(element){
			        $(element).valid();
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
		<li><a href="${ctx}/memberFriendReport/">好友投诉列表</a></li>
		<shiro:hasPermission name="member:memberFriendReport:edit">
		<li class="active"><a href="${ctx}/memberFriendReport/add?id=${memberFriendReport.id}">好友投诉添加</a></li>
		</shiro:hasPermission>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="memberFriendReport" action="${ctx}/memberFriendReport/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>		
		<div class="control-group">
			<label style="text-align:right;width:150px">投诉人id：</label>
			<form:input path="memberId" style="width:230px" htmlEscape="false" maxlength="20" class="input-xlarge required digits"/>
			<span class="help-inline"  style="width:150px"><font color="red">*</font> </span>
		</div>
		<div class="control-group">
			<label style="text-align:right;width:150px">好友id：</label>
			<form:input path="friendId" style="width:230px" htmlEscape="false" maxlength="20" class="input-xlarge required digits"/>
			<span class="help-inline"  style="width:150px"><font color="red">*</font> </span>
		</div>
		<div class="control-group">
			<label style="text-align:right;width:150px">投诉类型：</label>
			<form:input path="type" style="width:230px" htmlEscape="false" maxlength="4" class="input-xlarge required"/>
			<span class="help-inline"  style="width:150px"><font color="red">*</font> </span>
		</div>
		<div class="control-group">
			<label style="text-align:right;width:150px">投诉标题：</label>
			<form:input path="title" style="width:230px" htmlEscape="false" maxlength="64" class="input-xlarge "/>
			<span class="help-inline"  style="width:150px"></span>	
		</div>
		<div class="control-group">
			<label style="text-align:right;width:150px">投诉内容：</label>
			<form:textarea path="content" htmlEscape="false" rows="4" maxlength="255" class="input-xxlarge "/>
			<span class="help-inline"  style="width:150px"></span>	
		</div>
		<div class="control-group">
			<label style="text-align:right;width:150px">图片证据：</label>
			<form:input path="images" style="width:230px" htmlEscape="false" maxlength="255" class="input-xlarge "/>
			<span class="help-inline"  style="width:150px"></span>	
		</div>
		<div class="form-actions">
			<shiro:hasPermission name="member:memberFriendReport:edit"><input id="btnSubmit" class="btn btn-primary" style="width:80px;" type="submit" value="保   存"/>&nbsp;</shiro:hasPermission>
			<label style="text-align:right;width:50px"></label>
			<input id="btnCancel" class="btn btn-primary" type="button" style="width:80px;" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>