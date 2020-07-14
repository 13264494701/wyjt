<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>借条详情对话记录管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">

	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/loan/nfsLoanDetailMessage/">借条详情对话记录列表</a></li>
		<li class="active"><a href="${ctx}/loan/nfsLoanDetailMessage/query?id=${nfsLoanDetailMessage.id}">借条详情对话记录查看</a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="nfsLoanDetailMessage"  method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>		
		<div class="control-group">
			<label style="text-align:right;width:150px">借条详情ID：</label>
			<form:input path="detailId" style="width:230px"  readonly="true" htmlEscape="false" maxlength="20" class="input-xlarge  digits"/>
			<span class="help-inline"  style="width:150px"></span>	
		</div>
		<div class="control-group">
			<label style="text-align:right;width:150px">用户id：</label>
			<form:input path="memberId" style="width:230px"  readonly="true" htmlEscape="false" maxlength="20" class="input-xlarge "/>
			<span class="help-inline"  style="width:150px"></span>	
		</div>
		<div class="control-group">
			<label style="text-align:right;width:150px">对话ID NewLoanMessage：</label>
			<form:input path="type" style="width:230px"  readonly="true" htmlEscape="false" maxlength="10" class="input-xlarge "/>
			<span class="help-inline"  style="width:150px"></span>	
		</div>
		<div class="control-group">
			<label style="text-align:right;width:150px">内容：</label>
			<form:input path="note" style="width:230px"  readonly="true" htmlEscape="false" class="input-xlarge "/>
			<span class="help-inline"  style="width:150px"></span>	
		</div>
		<div class="form-actions">
			<input id="btnCancel" class="btn btn-primary" type="button" style="width:80px;" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>