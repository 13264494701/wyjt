<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>会员等级管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">

	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/member/memberRank/">会员等级列表</a></li>
		<li class="active"><a href="${ctx}/member/memberRank/query?id=${memberRank.id}">会员等级查看</a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="memberRank"  method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>		
		<div class="control-group">
			<label style="text-align:right;width:150px">等级编号：</label>
			<form:input path="rankNo" style="width:230px"  readonly="true" htmlEscape="false" maxlength="3" class="input-xlarge "/>
			<span class="help-inline"  style="width:150px"></span>	
		</div>
		<div class="control-group">
			<label style="text-align:right;width:150px">等级名称：</label>
			<form:input path="rankName" style="width:230px"  readonly="true" htmlEscape="false" maxlength="64" class="input-xlarge "/>
			<span class="help-inline"  style="width:150px"></span>	
		</div>

		<div class="control-group">
			<label style="text-align:right;width:150px">是否默认：</label>
			<form:radiobuttons path="isDefault" items="${fns:getDictList('trueOrFalse')}" itemLabel="label" itemValue="value" disabled="true" htmlEscape="false" class="required"/>
			<span class="help-inline"  style="width:150px"><font color="red">*</font> </span>
		</div>
		<div class="control-group">
			<label style="text-align:right;width:150px">是否特殊：</label>
			<form:radiobuttons path="isSpecial" items="${fns:getDictList('trueOrFalse')}" itemLabel="label" itemValue="value" disabled="true" htmlEscape="false" class="required"/>
			<span class="help-inline"  style="width:150px"><font color="red">*</font> </span>
		</div>
		<div class="form-actions">
			<input id="btnCancel" class="btn btn-primary" type="button" style="width:80px;" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>