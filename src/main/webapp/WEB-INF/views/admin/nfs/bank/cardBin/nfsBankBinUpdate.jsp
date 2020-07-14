<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>卡BIN管理</title>
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
		<li><a href="${ctx}/nfsBankBin/?bank.id=${nfsBankBin.bank.id}">卡BIN列表</a></li>
		<shiro:hasPermission name="bank:nfsBankBin:edit">
			<li class="active"><a href="${ctx}/nfsBankBin/update?id=${nfsBankBin.id}">卡BIN修改</a></li>
		</shiro:hasPermission>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="nfsBankBin" action="${ctx}/nfsBankBin/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<form:hidden path="bank.id"/>
		<sys:message content="${message}"/>		
		<div class="control-group">
			<label style="text-align:right;width:150px">银行：</label>
			<form:input path="bank.name" style="width:230px" readonly="true" htmlEscape="false" maxlength="20" class="input-xlarge"/>
			<span class="help-inline"  style="width:150px"> </span>
		</div>
		<div class="control-group">
			<label style="text-align:right;width:150px">卡BIN：</label>
			<form:input path="cardBin" style="width:230px" htmlEscape="false" maxlength="8" class="input-xlarge required"/>
			<span class="help-inline"  style="width:150px"><font color="red">*</font> </span>
		</div>
		<div class="control-group">
			<label style="text-align:right;width:150px">卡片种类：</label>
			<form:select path="cardType" class="input-xlarge required">
				<form:options items="${fns:getDictList('cardType')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
			</form:select>
			<span class="help-inline"  style="width:150px"></span>	
		</div>
		<div class="form-actions">
			<shiro:hasPermission name="bank:nfsBankBin:edit"><input id="btnSubmit" class="btn btn-primary" style="width:80px;" type="submit" value="保   存"/>&nbsp;</shiro:hasPermission>
			<label style="text-align:right;width:50px"></label>
			<input id="btnCancel" class="btn btn-primary" type="button" style="width:80px;" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>