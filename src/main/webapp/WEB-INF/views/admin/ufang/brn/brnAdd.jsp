<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>机构管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			$('#parent_brn').attr('readonly','readonly');
			$("#name").focus();
			$("#inputForm").validate({
				onfocusout: function(element){
			        $(element).valid();
			    },
				rules: {
					phoneNo: {remote: "${admin}/ufang/brn/checkPhoneNo" }
				},
				messages: {
					phoneNo: {remote: "该手机号码已被其它机构占用!"}
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
						error.insertAfter(element);
					}
				}
			});
		});
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${admin}/ufang/brn/list">机构列表</a></li>
		<li class="active"><a href="${admin}/ufang/brn/add?id=${brn.id}&parent.id=${brn.parent.id}">机构添加</a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="brn" action="${admin}/ufang/brn/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>
		 <div class="control-group">
			<label class="control-label">上级机构:</label>
			<div class="controls">
                <sys:treeselect  id="brn" name="parent.id" value="${brn.parent.id}" labelName="parent.brnName" labelValue="${brn.parent.brnName}"
					title="机构" baseUrl="${admin}" url="/ufang/brn/treeData" extId="${brn.id}" cssClass="" allowClear="false"/> 
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">归属区域:</label>
			<div class="controls">
                <sys:treeselect id="area" name="area.id" value="${brn.area.id}" labelName="area.name" labelValue="${brn.area.name}"
					title="区域" baseUrl="${admin}" url="/sys/area/treeData" cssClass="required"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">机构名称:</label>
			<div class="controls">
				<form:input path="brnName" htmlEscape="false" style="width:250px;" maxlength="50" class="required"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">主负责人:</label>
			<div class="controls">
				<form:input path="primaryPerson" htmlEscape="false" style="width:250px;"  maxlength="64" class="required"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>

		<div class="control-group">
			<label class="control-label">手机号码:</label>
			<div class="controls">
				<form:input path="phoneNo" htmlEscape="false" style="width:250px;" maxlength="11" class="digits required"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>

		<div class="control-group">
			<label class="control-label">邮箱地址:</label>
			<div class="controls">
				<form:input path="email" htmlEscape="false" style="width:250px;" class="email" maxlength="50"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">联系地址:</label>
			<div class="controls">
				<form:input path="address" htmlEscape="false" style="width:250px;" maxlength="50"/>
			</div>
		</div>

		<div class="control-group">
			<label class="control-label">备注:</label>
			<div class="controls">
				<form:textarea path="rmk" htmlEscape="false" rows="3" style="width:250px;"  maxlength="200" class="input-xlarge"/>
			</div>
		</div>
		<div class="form-actions" >
			<shiro:hasPermission name="ufang:brn:edit"><input id="btnSubmit" style="width:80px;" class="btn btn-primary" type="submit" value="保  存"/>&nbsp;</shiro:hasPermission>
			<label style="text-align:right;width:50px"></label>
			<input id="btnCancel" class="btn btn-primary" type="button" style="width:80px;" value="返 回" onclick="history.go(-1)"/>
		</div>
		
	</form:form>
</body>
</html>