<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>广告位置管理</title>
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
		<li><a href="${ctx}/adPosition/">广告位置列表</a></li>
		<shiro:hasPermission name="cms:adPosition:edit">
		<li class="active"><a href="${ctx}/adPosition/add?id=${cmsAdPosition.id}">广告位置添加</a></li>
		</shiro:hasPermission>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="cmsAdPosition" action="${ctx}/adPosition/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>		
		<div class="control-group">
			<label style="text-align:right;width:150px">名称：</label>
			<form:input path="positionName" style="width:230px" htmlEscape="false" maxlength="64" class="input-xlarge required"/>
			<span class="help-inline"  style="width:150px"><font color="red">*</font> </span>
		</div>
		<div class="control-group">
			<label style="text-align:right;width:150px">高度：</label>
			<form:input path="height" style="width:230px" htmlEscape="false" maxlength="11" class="input-xlarge required"/>
			<span class="help-inline"  style="width:150px"><font color="red">*</font> </span>
		</div>
		<div class="control-group">
			<label style="text-align:right;width:150px">宽度：</label>
			<form:input path="width" style="width:230px" htmlEscape="false" maxlength="11" class="input-xlarge required"/>
			<span class="help-inline"  style="width:150px"><font color="red">*</font> </span>
		</div>
		<div class="control-group">
			<label style="text-align:right;width:150px">模板：</label>
			<form:textarea id="template" htmlEscape="true" path="template" rows="20" maxlength="800" style="width:800px" class="input-xlarge"/>
		</div>
		<div class="control-group">
			<label style="text-align:right;width:150px">描述：</label>
			<form:textarea id="description" htmlEscape="true" path="description" rows="4" maxlength="800" style="width:800px" class="input-xlarge"/>
			<span class="help-inline"  style="width:150px"></span>	
		</div>
		<div class="control-group"  style="text-align:center">
			<shiro:hasPermission name="cms:adPosition:edit"><input id="btnSubmit" class="btn btn-primary" style="width:80px;" type="submit" value="保   存"/>&nbsp;</shiro:hasPermission>
			<label style="text-align:right;width:50px"></label>
			<input id="btnCancel" class="btn btn-primary" type="button" style="width:80px;" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>