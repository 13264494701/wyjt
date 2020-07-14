<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>第三方查询流量管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">

	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/api/apiLoaneeData/">第三方查询流量列表</a></li>
		<li class="active"><a href="${ctx}/api/apiLoaneeData/query?id=${apiLoaneeData.id}">第三方查询流量查看</a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="apiLoaneeData"  method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>		
		<div class="control-group">
			<label style="text-align:right;width:150px">商户号：</label>
			<form:input path="merchantNumber" style="width:230px"  readonly="true" htmlEscape="false" maxlength="255" class="input-xlarge "/>
			<span class="help-inline"  style="width:150px"></span>	
		</div>
		<div class="control-group">
			<label style="text-align:right;width:150px">商户订单号：由商户自定义传入的唯一且不大于32位的字符串， 生成的订单号不能带有:、_#_等特殊字符，建议使用时间戳或数字、字母、下划线等组合：</label>
			<form:input path="orderId" style="width:230px"  readonly="true" htmlEscape="false" maxlength="255" class="input-xlarge "/>
			<span class="help-inline"  style="width:150px"></span>	
		</div>
		<div class="control-group">
			<label style="text-align:right;width:150px">本次查询数据数量：</label>
			<form:input path="count" style="width:230px"  readonly="true" htmlEscape="false" maxlength="11" class="input-xlarge "/>
			<span class="help-inline"  style="width:150px"></span>	
		</div>
		<div class="control-group">
			<label style="text-align:right;width:150px">签名：</label>
			<form:input path="sign" style="width:230px"  readonly="true" htmlEscape="false" maxlength="255" class="input-xlarge "/>
			<span class="help-inline"  style="width:150px"></span>	
		</div>
		<div class="control-group">
			<label style="text-align:right;width:150px">第三方ip：</label>
			<form:input path="ip" style="width:230px"  readonly="true" htmlEscape="false" maxlength="255" class="input-xlarge "/>
			<span class="help-inline"  style="width:150px"></span>	
		</div>
		<div class="control-group">
			<label style="text-align:right;width:150px">数据区间-起始时间：</label>
			<input name="startTime" type="text" style="width:230px"  disabled="disabled" maxlength="20" class="input-medium Wdate "
				value="<fmt:formatDate value="${apiLoaneeData.startTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"
				onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:true});"/>
			<span class="help-inline"  style="width:150px"></span>	
		</div>
		<div class="control-group">
			<label style="text-align:right;width:150px">数据区间-结束时间：</label>
			<input name="endTime" type="text" style="width:230px"  disabled="disabled" maxlength="20" class="input-medium Wdate "
				value="<fmt:formatDate value="${apiLoaneeData.endTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"
				onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:true});"/>
			<span class="help-inline"  style="width:150px"></span>	
		</div>
		<div class="form-actions">
			<input id="btnCancel" class="btn btn-primary" type="button" style="width:80px;" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>