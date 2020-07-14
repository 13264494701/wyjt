<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>流量订单管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">

	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/order/ufangLoaneeDataOrder/">流量订单列表</a></li>
		<li class="active"><a href="${ctx}/order/ufangLoaneeDataOrder/query?id=${ufangLoaneeDataOrder.id}">流量订单查看</a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="ufangLoaneeDataOrder"  method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>		
		<div class="control-group">
			<label style="text-align:right;width:150px">优放用户：</label>
			<form:input path="user.empNo" style="width:230px"  readonly="true" htmlEscape="false" maxlength="8" class="input-xlarge "/>
			<span class="help-inline"  style="width:150px"></span>	
		</div>
		<div class="control-group">
			<label style="text-align:right;width:150px">数据ID：</label>
			<form:input path="data.id" style="width:230px"  readonly="true" htmlEscape="false" maxlength="20" class="input-xlarge  digits"/>
			<span class="help-inline"  style="width:150px"></span>	
		</div>
		<div class="control-group">
			<label style="text-align:right;width:150px">付款金额：</label>
			<form:input path="amount" style="width:230px"  readonly="true" htmlEscape="false" class="input-xlarge "/>
			<span class="help-inline"  style="width:150px"></span>	
		</div>
		<div class="control-group">
			<label style="text-align:right;width:150px">订单状态：</label>
			<form:input path="status" style="width:230px"  readonly="true" htmlEscape="false" maxlength="4" class="input-xlarge "/>
			<span class="help-inline"  style="width:150px"></span>	
		</div>
		<div class="control-group">
			<label style="text-align:right;width:150px">支付流水：</label>
			<form:input path="payId" style="width:230px"  readonly="true" htmlEscape="false" maxlength="20" class="input-xlarge  digits"/>
			<span class="help-inline"  style="width:150px"></span>	
		</div>
		<div class="form-actions">
			<input id="btnCancel" class="btn btn-primary" type="button" style="width:80px;" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>