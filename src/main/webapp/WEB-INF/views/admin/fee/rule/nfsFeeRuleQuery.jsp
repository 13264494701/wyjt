<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>收费规则管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">

	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/nfsFeeRule/">收费规则列表</a></li>
		<li class="active"><a href="${ctx}/nfsFeeRule/query?id=${nfsFeeRule.id}">收费规则查看</a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="nfsFeeRule"  method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>		
		<div class="control-group">
			<label style="text-align:right;width:150px">业务代码：</label>
			<form:input path="trxCode" style="width:230px"  readonly="true" htmlEscape="false" maxlength="5" class="input-xlarge "/>
			<span class="help-inline"  style="width:150px"></span>	
		</div>
		<div class="control-group">
			<label style="text-align:right;width:150px">收费规则表达式：</label>
			<form:input path="expression" style="width:630px"  readonly="true" htmlEscape="false" maxlength="255" class="input-xlarge "/>
			<span class="help-inline"  style="width:150px"></span>	
		</div>
		<div class="form-actions">
			<input id="btnCancel" class="btn btn-primary" type="button" style="width:80px;" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>