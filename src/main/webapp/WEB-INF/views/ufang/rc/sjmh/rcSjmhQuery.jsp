<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>风控 数据魔盒管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">

	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${ufang}/rcSjmh/">风控 数据魔盒列表</a></li>
		<li class="active"><a href="${ufang}/rcSjmh/query?id=${rcSjmh.id}">风控 数据魔盒查看</a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="rcSjmh"  method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>		
		<div class="control-group">
			<label style="text-align:right;width:150px">任务id：</label>
			<form:input path="taskId" style="width:230px"  readonly="true" htmlEscape="false" maxlength="50" class="input-xlarge "/>
			<span class="help-inline"  style="width:150px"></span>	
		</div>
		<div class="control-group">
			<label style="text-align:right;width:150px">手机号码：</label>
			<form:input path="phoneNo" style="width:230px"  readonly="true" htmlEscape="false" maxlength="11" class="input-xlarge "/>
			<span class="help-inline"  style="width:150px"></span>	
		</div>
		<div class="control-group">
			<label style="text-align:right;width:150px">身份证号：</label>
			<form:input path="idNo" style="width:230px"  readonly="true" htmlEscape="false" maxlength="18" class="input-xlarge "/>
			<span class="help-inline"  style="width:150px"></span>	
		</div>
		<div class="control-group">
			<label style="text-align:right;width:150px">用户名：</label>
			<form:input path="userName" style="width:230px"  readonly="true" htmlEscape="false" maxlength="50" class="input-xlarge "/>
			<span class="help-inline"  style="width:150px"></span>	
		</div>
		<div class="control-group">
			<label style="text-align:right;width:150px">真实姓名：</label>
			<form:input path="realName" style="width:230px"  readonly="true" htmlEscape="false" maxlength="50" class="input-xlarge "/>
			<span class="help-inline"  style="width:150px"></span>	
		</div>
		<div class="control-group">
			<label style="text-align:right;width:150px">渠道类型：</label>
			<form:input path="channelType" style="width:230px"  readonly="true" htmlEscape="false" maxlength="20" class="input-xlarge "/>
			<span class="help-inline"  style="width:150px"></span>	
		</div>
		<div class="control-group">
			<label style="text-align:right;width:150px">渠道编码：</label>
			<form:input path="channelCode" style="width:230px"  readonly="true" htmlEscape="false" maxlength="10" class="input-xlarge "/>
			<span class="help-inline"  style="width:150px"></span>	
		</div>
		<div class="control-group">
			<label style="text-align:right;width:150px">渠道数据源：</label>
			<form:input path="channelSrc" style="width:230px"  readonly="true" htmlEscape="false" maxlength="50" class="input-xlarge "/>
			<span class="help-inline"  style="width:150px"></span>	
		</div>
		<div class="control-group">
			<label style="text-align:right;width:150px">渠道属性：</label>
			<form:input path="channelAttr" style="width:230px"  readonly="true" htmlEscape="false" maxlength="50" class="input-xlarge "/>
			<span class="help-inline"  style="width:150px"></span>	
		</div>
		<div class="control-group">
			<label style="text-align:right;width:150px">缺失数据：</label>
			<form:input path="lostData" style="width:230px"  readonly="true" htmlEscape="false" class="input-xlarge "/>
			<span class="help-inline"  style="width:150px"></span>	
		</div>
		<div class="control-group">
			<label style="text-align:right;width:150px">任务消息：</label>
			<form:input path="taskMsg" style="width:230px"  readonly="true" htmlEscape="false" maxlength="255" class="input-xlarge "/>
			<span class="help-inline"  style="width:150px"></span>	
		</div>
		<div class="control-group">
			<label style="text-align:right;width:150px">任务数据：</label>
			<form:input path="taskData" style="width:230px"  readonly="true" htmlEscape="false" class="input-xlarge "/>
			<span class="help-inline"  style="width:150px"></span>	
		</div>
		<div class="form-actions">
			<input id="btnCancel" class="btn btn-primary" type="button" style="width:80px;" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>