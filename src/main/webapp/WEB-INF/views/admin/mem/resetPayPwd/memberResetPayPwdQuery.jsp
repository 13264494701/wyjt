<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>修改支付密码管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">

	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/mem/memberResetPayPwd/">修改支付密码列表</a></li>
		<li class="active"><a href="${ctx}/mem/memberResetPayPwd/query?id=${memberResetPayPwd.id}">修改支付密码查看</a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="memberResetPayPwd"  method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>		
		<div class="control-group">
			<label style="text-align:right;width:150px">用户ID：</label>
			<form:input path="memberId" style="width:230px"  readonly="true" htmlEscape="false" maxlength="20" class="input-xlarge  digits"/>
			<span class="help-inline"  style="width:150px"></span>	
		</div>
		<div class="control-group">
			<label style="text-align:right;width:150px">支付密码：</label>
			<form:input path="payPwd" style="width:230px"  readonly="true" htmlEscape="false" maxlength="255" class="input-xlarge "/>
			<span class="help-inline"  style="width:150px"></span>	
		</div>
		<div class="control-group">
			<label style="text-align:right;width:150px">认证状态 0待审核 1 认证成功 2认证失败  3微信小程序修改密码：</label>
			<form:input path="status" style="width:230px"  readonly="true" htmlEscape="false" maxlength="11" class="input-xlarge "/>
			<span class="help-inline"  style="width:150px"></span>	
		</div>
		<div class="control-group">
			<label style="text-align:right;width:150px">失败原因：</label>
			<form:input path="failReason" style="width:230px"  readonly="true" htmlEscape="false" maxlength="255" class="input-xlarge "/>
			<span class="help-inline"  style="width:150px"></span>	
		</div>
		<div class="form-actions">
			<input id="btnCancel" class="btn btn-primary" type="button" style="width:80px;" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>