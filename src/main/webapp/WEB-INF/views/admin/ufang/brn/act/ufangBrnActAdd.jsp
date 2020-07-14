<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>机构账户管理</title>
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
		<li><a href="${admin}/ufangBrnAct/">机构账户列表</a></li>
		<shiro:hasPermission name="brn:ufangBrnAct:edit">
		<li class="active"><a href="${admin}/ufangBrnAct/add?id=${ufangBrnAct.id}">机构账户添加</a></li>
		</shiro:hasPermission>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="ufangBrnAct" action="${admin}/ufangBrnAct/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>		
		<div class="control-group">
			<label style="text-align:right;width:150px">机构号：</label>
				<sys:treeselect id="company" name="company.id" value="${ufangBrnAct.company.id}" labelName="company.brnName" labelValue="${ufangBrnAct.company.brnName}"
					title="部门"  baseUrl="${admin}" url="/ufang/brn/treeData?type=2" cssClass="required" allowClear="true" notAllowSelectParent="false"/>
				<span class="help-inline"><font color="red">*</font> </span>
		</div>
		<div class="control-group">
			<label style="text-align:right;width:150px">账户科目：</label>
				<form:select path="subNo" class="input-xlarge required">
					<form:options items="${fns:getDictList('subNo')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			<span class="help-inline"  style="width:150px"><font color="red">*</font> </span>
		</div>
		<div class="control-group">
			<label style="text-align:right;width:150px">账户币种：</label>
			<form:select path="currCode" class="input-xlarge required">
				<form:options items="${fns:getDictList('currCode')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
			</form:select>
			<span class="help-inline"  style="width:150px"><font color="red">*</font> </span>
		</div>

		<div class="form-actions">
			<shiro:hasPermission name="brn:ufangBrnAct:edit"><input id="btnSubmit" class="btn btn-primary" style="width:80px;" type="submit" value="保   存"/>&nbsp;</shiro:hasPermission>
			<label style="text-align:right;width:50px"></label>
			<input id="btnCancel" class="btn btn-primary" type="button" style="width:80px;" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>