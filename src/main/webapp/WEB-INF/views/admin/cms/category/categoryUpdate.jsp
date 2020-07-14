<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>分类管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			$("#name").focus();
			$("#inputForm").validate({
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
		<li><a href="${ctx}/cms/category/">分类列表</a></li>
		<li class="active"><a href="${ctx}/cms/category/update?id=${category.id}&parent.id=${category.parent.id}">栏目<shiro:hasPermission name="cms:category:edit">${not empty category.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="cms:category:edit">查看</shiro:lacksPermission></a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="category" action="${ctx}/cms/category/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>

		<div class="control-group">
			<label class="control-label">上级分类:</label>
			<div class="controls">
                <sys:treeselect id="category" name="parent.id" value="${category.parent.id}" labelName="parent.name" labelValue="${category.parent.name}"
					title="栏目" url="/cms/category/treeData" extId="${category.id}" cssClass="required"/>
			</div>
		</div>

		<div class="control-group">
			<label class="control-label">分类名称:</label>
			<div class="controls">
				<form:input path="name" htmlEscape="false" maxlength="50" class="input-xlarge required"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">分类别名:</label>
			<div class="controls">
				<form:input path="alias" htmlEscape="false" maxlength="50" class="input-xlarge"/>
				<span class="help-inline"  style="width:150px"></span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">排序:</label>
			<div class="controls">
				<form:input path="sort" htmlEscape="false" maxlength="11" class="required digits"/>
				<span class="help-inline">分类的排列次序</span>
			</div>
		</div>

		<div class="form-actions">
			<shiro:hasPermission name="cms:category:edit">
			<input id="btnSubmit" class="btn btn-primary" style="width:80px;" type="submit" value="保 存"/>&nbsp;
			</shiro:hasPermission>
			<input id="btnCancel" class="btn btn-primary" type="button" style="width:80px;" value="返回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>