<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>会员积分管理</title>
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
		<shiro:hasPermission name="mem:memberPoint:edit">
			<li class="active"><a href="${ctx}/member/memberPoint/update?memNo=${memberPoint.memberNo}&redirectUrl=memberPoint">会员积分调整</a></li>
		</shiro:hasPermission>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="memberPoint" action="${ctx}/member/memberPoint/update?redirectUrl=${redirectUrl}" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>		
		<div class="control-group">
			<label style="text-align:right;width:150px">会员编号：</label>
			<form:input path="memberNo" style="width:230px" htmlEscape="false" readonly="true"  maxlength="16" class="input-xlarge required"/>
			<span class="help-inline"  style="width:150px"><font color="red">*</font> </span>
		</div>
		<div class="control-group">
			<label style="text-align:right;width:150px">积分余额：</label>
			<form:input path="balancePoints" style="width:230px" htmlEscape="false" readonly="true"  maxlength="20" class="input-xlarge required digits"/>
			<span class="help-inline"  style="width:150px"><font color="red">*</font> </span>
		</div>
		
		<div class="control-group">
			<label style="text-align:right;width:150px">调整值：</label>
			<input type="text" name="points" style="width:230px"  placeholder="正数代表增加,负数代表扣除"  maxlength="20" class="input-xlarge required integer"/>
			<span class="help-inline"  style="width:150px"><font color="red">*</font> </span>
		</div>
		<div class="control-group">
			<label style="text-align:right;width:150px">备注：</label>
			<form:textarea path="rmk" htmlEscape="false" rows="4" maxlength="1024" class="input-xxlarge "/>
			<span class="help-inline"  style="width:150px"></span>	
		</div>
		<div class="form-actions">
			<input id="btnSubmit" class="btn btn-primary" style="width:80px;" type="submit" value="保   存"/>
			<label style="text-align:right;width:50px"></label>
			<input id="btnCancel" class="btn btn-primary" type="button" style="width:80px;" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>