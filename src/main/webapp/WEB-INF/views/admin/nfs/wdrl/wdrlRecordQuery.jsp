<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>提现记录管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">

	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${admin}/wdrlRecord/">提现记录列表</a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="nfsWdrlRecord"  method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>		
		<div class="control-group">
			<label style="text-align:right;width:150px">提现编号：</label>
			<form:input path="id" style="width:230px"  readonly="true" htmlEscape="false" maxlength="16" class="input-xlarge "/>
			<span class="help-inline"  style="width:150px"></span>	
		</div>
		<div class="control-group">
			<label style="text-align:right;width:150px">会员编号：</label>
			<form:input path="member.id" style="width:230px"  readonly="true" htmlEscape="false" maxlength="20" class="input-xlarge  digits"/>
			<span class="help-inline"  style="width:150px"></span>	
		</div>
		<div class="control-group">
			<label style="text-align:right;width:150px">会员姓名：</label>
			<form:input path="member.name" style="width:230px"  readonly="true" htmlEscape="false" maxlength="64" class="input-xlarge "/>
			<span class="help-inline"  style="width:150px"></span>	
		</div>
		<div class="control-group">
			<label style="text-align:right;width:150px">第三方订单号：</label>
			<form:input path="thirdOrderNo" style="width:230px"  readonly="true" htmlEscape="false" maxlength="20" class="input-xlarge "/>
			<span class="help-inline"  style="width:150px"></span>	
		</div>
		<div class="control-group">
			<label style="text-align:right;width:150px">提现金额：</label>
			<form:input path="amount" value="${fns:decimalToStr(nfsWdrlRecord.amount,2)}" style="width:230px"  readonly="true" htmlEscape="false" class="input-xlarge "/>
			<span class="help-inline"  style="width:150px"></span>	
		</div>
		<div class="control-group">
			<label style="text-align:right;width:150px">手续费：</label>
			<form:input path="fee" value="${fns:decimalToStr(nfsWdrlRecord.fee,2)}" style="width:230px"  readonly="true" htmlEscape="false" class="input-xlarge "/>
			<span class="help-inline"  style="width:150px"></span>	
		</div>
		<div class="control-group">
			<label style="text-align:right;width:150px">提现状态：</label>
			<form:select path="status" style="width:245px"  disabled="true"  class="input-xlarge ">
			<form:option value="" label="请选择"/>
				<form:options items="${fns:getDictList('wdrlRecordStatus')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
			</form:select>
			<span class="help-inline"  style="width:150px"></span>	
		</div>
		<div class="control-group">
			<label style="text-align:right;width:150px">提现方式：</label>
			<form:select path="type" style="width:245px"  disabled="true"  class="input-xlarge ">
			<form:option value="" label="请选择"/>
				<form:options items="${fns:getDictList('wdrlRecordType')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
			</form:select>
			<span class="help-inline"  style="width:150px"></span>	
		</div>
		<div class="control-group">
			<label style="text-align:right;width:150px">银行名称：</label>
			<form:input path="bankName" style="width:230px"  readonly="true" htmlEscape="false" maxlength="64" class="input-xlarge "/>
			<span class="help-inline"  style="width:150px"></span>	
		</div>
		<div class="control-group">
			<label style="text-align:right;width:150px">银行卡号：</label>
			<form:input path="cardNo" style="width:230px"  readonly="true" htmlEscape="false" maxlength="32" class="input-xlarge "/>
			<span class="help-inline"  style="width:150px"></span>	
		</div>
		<%-- <div class="control-group">
			<label style="text-align:right;width:150px">审核记录：</label>
			<a href="${ctx}/check/nfsCheckRecord/query?id=${nfsWdrlRecord.checkRecordId}">
				<form:input path="checkRecordId" style="width:230px"  readonly="true" htmlEscape="false" maxlength="6" class="input-xlarge "/>
			</a>
			<span class="help-inline"  style="width:150px"></span>	
		</div>  --%>
		<%-- <div class="control-group">
			<label style="text-align:right;width:150px">审核人：</label>
			<form:input path="checker.empNam" style="width:230px"  readonly="true" htmlEscape="false" maxlength="6" class="input-xlarge "/>
			<span class="help-inline"  style="width:150px"></span>	
		</div>  --%>
		<%-- <div class="control-group">
			<label style="text-align:right;width:150px">审核人姓名：</label>
			<form:input path="checkerName" style="width:230px"  readonly="true" htmlEscape="false" maxlength="64" class="input-xlarge "/>
			<span class="help-inline"  style="width:150px"></span>	
		</div> --%>
		<div class="control-group">
			<label style="text-align:right;width:150px">审核时间：</label>
			<input name="checkTime" type="text" style="width:230px"  disabled="disabled" maxlength="20" class="input-medium Wdate "
				value="<fmt:formatDate value="${nfsWdrlRecord.checkTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"
				onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:true});"/>
			<span class="help-inline"  style="width:150px"></span>	
		</div>
		<div class="control-group">
			<label style="text-align:right;width:150px">打款时间：</label>
			<input name="payTime" type="text" style="width:230px"  disabled="disabled" maxlength="20" class="input-medium Wdate "
				value="<fmt:formatDate value="${nfsWdrlRecord.payTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"
				onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:true});"/>
			<span class="help-inline"  style="width:150px"></span>	
		</div>
		<div class="form-actions">
		     <label style="text-align:right;width:50px"></label>
			<a class="btn btn-primary"  style="width:80px;" href="${admin}/wdrlRecord/downLoadReceipt?id=${nfsWdrlRecord.id}">提现凭证</a>
			<label style="text-align:right;width:50px"></label>
			<input id="btnCancel" class="btn btn-primary" type="button" style="width:80px;" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>