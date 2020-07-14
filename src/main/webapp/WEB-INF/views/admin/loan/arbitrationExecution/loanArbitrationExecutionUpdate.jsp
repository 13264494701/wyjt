<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>操作成功管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			//$("#name").focus();
			$("#inputForm").validate({
				onfocusout: function(element){
			        $(element).valid();
			    },
				submitHandler: function(form){
					loading('正在提交，请稍等...');
					form.submit();
				},
				errorContainer: "#messageBox",
				errorPlacement: function(error, element) {
					$("#messageBox").text("输入有误，请先更正。");
					if (element.is(":checkbox")||element.is(":radio")||element.parent().is(".input-append")){
						error.appendTo(element.parent().parent());
					} else {
		//				error.insertAfter(element);
						error.appendTo(element.next());
					}
				}
			});
		});
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${admin}/loanArbitrationExecution/">操作成功列表</a></li>
		<shiro:hasPermission name="loan:arbitrationExecution:edit">
			<li class="active"><a href="${admin}/loanArbitrationExecution/update?id=${loanArbitrationExecution.id}">操作成功修改</a></li>
		</shiro:hasPermission>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="nfsLoanArbitrationExecution" action="${admin}/loanArbitrationExecution/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>		
		<div class="control-group">
			<label style="text-align:right;width:150px">放款人姓名：</label>
			<form:input path="loanerName" style="width:230px" readonly="true" htmlEscape="false" maxlength="64" class="input-xlarge required"/>
		</div>
		<div class="control-group">
			<label style="text-align:right;width:150px">借款人姓名：</label>
			<form:input path="loaneeName" style="width:230px" readonly="true" htmlEscape="false" maxlength="64" class="input-xlarge required"/>
		</div>
		<div class="control-group">
			<label style="text-align:right;width:150px">借款金额：</label>
			<form:input path="amount" style="width:230px" readonly="true" value="${fns:decimalToStr(loanArbitrationExecution.amount,2)}" htmlEscape="false" class="input-xlarge required"/>
		</div>
		<div class="control-group">
			<label style="text-align:right;width:150px">借款利息：</label>
			<form:input path="interest" style="width:230px" readonly="true" value="${fns:decimalToStr(loanArbitrationExecution.interest,2)}" htmlEscape="false" class="input-xlarge required"/>
		</div>
		<div class="control-group">
			<label style="text-align:right;width:150px">可缴费时间：</label>
			<input name="paytime" type="text" style="width:230px" readonly="readonly" maxlength="20" class="input-medium Wdate "
				value="<fmt:formatDate value="${nfsLoanArbitrationExecution.paytime}" pattern="yyyy-MM-dd HH:mm:ss"/>"
				onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:true});"/>
			<span class="help-inline"  style="width:150px"></span>	
		</div>
		<div class="control-group">
			<label style="text-align:right;width:150px">本期应还金额：</label>
			<form:input path="dueRepayAmount" style="width:230px" value="${fns:decimalToStr(loanArbitrationExecution.dueRepayAmount,2)}" htmlEscape="false" class="input-xlarge" readonly="true"/>
			<span class="help-inline"  style="width:150px"></span>	
		</div>
		<div class="control-group">
			<label style="text-align:right;width:150px">结清日期：</label>
			<input name="completeDate" type="text" style="width:230px" readonly="readonly" maxlength="20" class="input-medium Wdate "
				value="<fmt:formatDate value="${nfsLoanArbitrationExecution.completeDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"
				onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:true});"/>
			<span class="help-inline"  style="width:150px"></span>	
		</div>
		<div class="control-group">
			<label style="text-align:right;width:150px">强执状态：</label>
			<form:select path="status" style="width:245px" class="input-xlarge required">
			<form:option value="" label="请选择"/>
				<form:options items="${fns:getDictList('arbitrationExecutionStatus')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
			</form:select>
			<span class="help-inline"  style="width:150px"><font color="red">*</font> </span>
		</div>
		<div class="control-group">
			<label style="text-align:right;width:150px">费用：</label>
			<form:input path="fee" value="${fns:decimalToStr(nfsLoanArbitrationExecution.fee,2)}" style="width:230px" htmlEscape="false" class="input-xlarge "/>
			<span class="help-inline"  style="width:150px"></span>	
		</div>
		<div class="control-group">
			<label style="text-align:right;width:150px">备注：</label>
			<form:input path="rmk" value="" style="width:230px" htmlEscape="false" maxlength="255" class="input-xlarge "/>
			<span class="help-inline"  style="width:150px"></span>	
		</div>
		<div class="control-group">
			<label style="text-align:right;width:150px">借条ID：</label>
			<form:input path="loan.id" style="width:230px" htmlEscape="false" maxlength="20" readonly="true" class="input-xlarge required digits"/>
		</div>
		<div class="form-actions">
			<shiro:hasPermission name="loan:arbitrationExecution:edit"><input id="btnSubmit" class="btn btn-primary" style="width:80px;" type="submit" value="保   存"/>&nbsp;</shiro:hasPermission>
			<label style="text-align:right;width:50px"></label>
			<input id="btnCancel" class="btn btn-primary" type="button" style="width:80px;" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>