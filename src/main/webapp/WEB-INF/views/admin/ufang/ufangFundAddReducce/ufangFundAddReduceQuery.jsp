<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>优放机构加减款记录管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">

	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/ufangFundAddReduce/">优放机构加减款记录列表</a></li>
		<li class="active"><a href="${ctx}/ufangFundAddReduce/query?id=${ufangFundAddReduce.id}">优放机构加减款记录查看</a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="ufangFundAddReduce"  method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>		
		<div class="control-group">
			<label style="text-align:right;width:150px">公司ID：</label>
			<form:input path="ufangBrn.id" style="width:230px"  readonly="true" htmlEscape="false" maxlength="20" class="input-xlarge  digits"/>
			<span class="help-inline"  style="width:150px"></span>	
		</div>
		<div class="control-group">
			<label style="text-align:right;width:150px">交易类型：</label>
			<form:select path="type" style="width:245px"  disabled="true"  class="input-xlarge ">
			<form:option value="" label="请选择"/>
				<form:options items="${fns:getDictList('ufangFundAddReduceType')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
			</form:select>
			<span class="help-inline"  style="width:150px"></span>	
		</div>
		<div class="control-group">
			<label style="text-align:right;width:150px">交易金额：</label>
			<form:input path="amount" style="width:230px"  readonly="true" htmlEscape="false" class="input-xlarge "/>
			<span class="help-inline"  style="width:150px"></span>	
		</div>
		<div class="control-group">
			<label style="text-align:right;width:150px">账户余额：</label>
			<form:input path="curBal" style="width:230px"  readonly="true" htmlEscape="false" class="input-xlarge "/>
			<span class="help-inline"  style="width:150px"></span>	
		</div>
		<div class="control-group">
			<label style="text-align:right;width:150px">币种：</label>
			<form:input path="currCode" style="width:230px"  readonly="true" htmlEscape="false" maxlength="3" class="input-xlarge "/>
			<span class="help-inline"  style="width:150px"></span>	
		</div>
		<div class="control-group">
			<label style="text-align:right;width:150px">交易状态：</label>
			<form:select path="status" style="width:245px"  disabled="true"  class="input-xlarge ">
			<form:option value="" label="请选择"/>
				<form:options items="${fns:getDictList('ufangFundAddReduceStatus')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
			</form:select>
			<span class="help-inline"  style="width:150px"></span>	
		</div>
		<div class="form-actions">
			<input id="btnCancel" class="btn btn-primary" type="button" style="width:80px;" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>