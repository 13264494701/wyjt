<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>消息模板管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">

	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/tmpl/mmsMsgTmpl/">消息模板列表</a></li>
		<li class="active"><a href="${ctx}/tmpl/mmsMsgTmpl/query?id=${mmsMsgTmpl.id}">消息模板查看</a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="mmsMsgTmpl"  method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>		
		<div class="control-group">
			<label style="text-align:right;width:150px">模板识别码：</label>
			<form:input path="code" style="width:230px"  readonly="true" htmlEscape="false" maxlength="32" class="input-xlarge "/>
			<span class="help-inline"  style="width:150px"></span>	
		</div>
		<div class="control-group">
			<label style="text-align:right;width:150px">消息模板名称：</label>
			<form:input path="name" style="width:230px"  readonly="true" htmlEscape="false" maxlength="64" class="input-xlarge "/>
			<span class="help-inline"  style="width:150px"></span>	
		</div>
		<div class="control-group">
			<label style="text-align:right;width:150px">模板类型：</label>
			<form:select path="type" style="width:245px"  disabled="true"  class="input-xlarge ">
			<form:option value="" label="请选择"/>
				<form:options items="${fns:getDictList('msgTmplType')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
			</form:select>
			<span class="help-inline"  style="width:150px"></span>	
		</div>
		<div class="control-group">
			<label style="text-align:right;width:150px">模板内容：</label>
			<form:textarea path="content"  readonly="true" htmlEscape="false" rows="4" class="input-xxlarge "/>
			<span class="help-inline"  style="width:150px"></span>	
		</div>
		<div class="form-actions">
			<input id="btnCancel" class="btn btn-primary" type="button" style="width:80px;" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>