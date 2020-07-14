<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>修改密码</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			var $password = $("#password");
			var $submit = $("input:submit");
			$("#oldPassword").focus();
			$("#inputForm").validate({
				rules: {
				},
				messages: {
					confirmNewPassword: {equalTo: "输入与上面相同的密码"}
				},
				submitHandler: function(form){
					loading('正在提交，请稍等...');
					$.ajax({
						url: "${ufang}/common/public_key",
						type: "GET",
						dataType: "json",
						cache: false,
						beforeSend: function() {
							$submit.prop("disabled", true);
						},
						success: function(data) {
							var rsaKey = new RSAKey();
							rsaKey.setPublic(b64tohex(data.modulus), b64tohex(data.exponent));
							var enPassword = hex2b64(rsaKey.encrypt($password.val()));
							$password.val(enPassword);
							form.submit();
							$submit.prop("disabled", false);

						},
						error: function(e) { 
							alert(e); 
						} 
					});
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
		<li class="active"><a href="${ufang}/mine/bindMember">绑定无忧借条账号</a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="user" action="${ufang}/mine/bindMember" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>
		<c:choose>
			<c:when test="${user.bindStatus == 'unbind'}">
				<div class="control-group">
					<label style="text-align:right;width:250px">无忧借条账号：</label>
					<input id="username" name="username" type="text" value="" maxlength="50" minlength="3" class="required"/>
					<span class="help-inline"><font color="red">*</font> </span>	
				</div>
				<div class="control-group">
					<label style="text-align:right;width:250px">无忧借条密码：</label>
					<input id="password" name="password" type="password" value="" maxlength="127" minlength="3" class="required"/>
					<span class="help-inline"><font color="red">*</font> </span>
				</div>
				<div class="form-actions">
					<input id="btnSubmit"  style="width:260px;"  class="btn btn-primary" type="submit" value="提交绑定"/>
				</div>		
			</c:when>
			<c:when test="${user.bindStatus == 'binded'}">
				<div class="control-group">
					<label style="text-align:right;width:250px">已经绑定的账号：</label>
					${member.username} 
				</div>
				<div class="control-group">
					<label style="text-align:right;width:250px">姓名：</label>
						${member.name} 
				</div>
				<div class="form-actions">
					<a href ="${ufang}/mine/relieve?user.id=${user.id}"
				 style="width:260px;" class="btn btn-primary" onclick="if(confirm('确定解除绑定?')==false)return false;">解绑</a>
				</div>	
			</c:when>
			<c:otherwise>
			
			</c:otherwise>
		</c:choose>
	</form:form>
</body>
</html>