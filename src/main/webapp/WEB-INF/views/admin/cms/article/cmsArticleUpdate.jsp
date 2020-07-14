<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>文章管理</title>
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
		<li><a href="${ctx}/cms/cmsArticle/">文章列表</a></li>
		<shiro:hasPermission name="cms:cmsArticle:edit">
			<li class="active"><a href="${ctx}/cms/cmsArticle/update?id=${cmsArticle.id}">文章修改</a></li>
		</shiro:hasPermission>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="cmsArticle" action="${ctx}/cms/cmsArticle/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>		
		<div class="control-group">
			<label style="text-align:right;width:150px">文章标题：</label>
			<form:input path="title" style="width:430px" htmlEscape="false" maxlength="255" class="input-xlarge required"/>
			<span class="help-inline"  style="width:150px"><font color="red">*</font> </span>
		</div>
		<div class="control-group">
			<label style="text-align:right;width:150px">展示频道：</label>
			<form:select path="channel.id"  style="width:245px" class="input-xlarge required">
				   <form:option value="" label="请选择"/>
				   <form:options items="${fns:getChannelList()}" itemLabel="name" itemValue="id" htmlEscape="false"/>
			</form:select>
			<span class="help-inline"  style="width:80px"><font color="red">*</font> </span>

			<label style="text-align:right;width:80px">归属分类：</label>	
            <sys:treeselect id="categoryId" name="category.id" value="${cmsArticle.category.id}" labelName="category.name" labelValue="${cmsArticle.category.name}" cssStyle="width:185px"
					title="分类" url="/cms/category/treeData"  notAllowSelectRoot="false" notAllowSelectParent="false" cssClass="required"/>
            <span class="help-inline"  style="width:150px"><font color="red">*</font> </span>
		</div>
		<div class="control-group">
			<label style="text-align:right;width:150px">文章图片：</label>
				<div class="controls">
					<form:hidden id="images"  path="images" htmlEscape="false" maxlength="2048" class="input-xlarge"/>
					<sys:ckfinder input="images"  type="images" uploadPath="/cms/article" selectMultiple="true"/>
					<span class="help-inline"  style="width:150px"></span>
				</div>	
		</div>
		<div class="control-group">
			<label style="text-align:right;width:150px">SEO关键字：</label>
			<form:input path="keywords" style="width:460px" htmlEscape="false" maxlength="255" class="input-xlarge "/>
			<span class="help-inline"  style="width:150px"></span>	
		</div>
		<div class="control-group">
			<label style="text-align:right;width:150px">SEO描述：</label>
			<form:textarea path="description"  htmlEscape="false" rows="4" maxlength="255" class="input-xxlarge "/>
			<span class="help-inline"  style="width:150px"></span>	
		</div>
		<div class="control-group">
			<label style="text-align:right;width:150px" class="control-label" >文章正文：</label>
			<div style="text-align:left; margin-left: 155px;">
			    <form:textarea id="content" htmlEscape="true" path="articleContent.content" rows="4" maxlength="200" class="input-xxlarge"/>
			    <sys:ckeditor replace="content" uploadPath="/cms/article" />
			</div>
		</div>
		<div class="control-group">
			<label style="text-align:right;width:150px">文章作者：</label>
			<form:input path="author" style="width:230px" htmlEscape="false" maxlength="64" class="input-xlarge "/>

			<label style="text-align:right;width:150px">文章来源：</label>
			<form:input path="copyfrom" style="width:230px" htmlEscape="false" maxlength="64" class="input-xlarge "/>
			<span class="help-inline"  style="width:150px"></span>	
		</div>
		<div class="control-group">
			<label style="text-align:right;width:150px">是否置顶：</label>
			<form:radiobuttons path="isTop" items="${fns:getDictList('trueOrFalse')}" itemLabel="label" itemValue="value" htmlEscape="false" class="required"/>
            <span class="help-inline"  style="width:150px"><font color="red">*</font> </span>
			<label style="text-align:right;width:170px">允许评论：</label>
			<form:radiobuttons path="allowComment" items="${fns:getDictList('trueOrFalse')}" itemLabel="label" itemValue="value" htmlEscape="false" class="required"/>
			<span class="help-inline"  style="width:150px"><font color="red">*</font> </span>
		</div>
		<div class="control-group">
			<label style="text-align:right;width:150px">模板类型：</label>
			<form:select path="moduleType" style="width:245px" class="input-xlarge required">
			<form:option value="" label="请选择"/>
				<form:options items="${fns:getDictList('moduleType')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
			</form:select>
			<span class="help-inline"  style="width:150px"><font color="red">*</font> </span>
		</div>

		<div class="control-group">
			<label style="text-align:right;width:150px">备注信息：</label>
			<form:textarea path="rmk" htmlEscape="false" rows="4" maxlength="255" class="input-xxlarge "/>
			<span class="help-inline"  style="width:150px"></span>	
		</div>
		<div class="control-group"  style="text-align:center">
			<shiro:hasPermission name="cms:cmsArticle:edit"><input id="btnSubmit" class="btn btn-primary" style="width:80px;" type="submit" value="保   存"/>&nbsp;</shiro:hasPermission>
			<label style="text-align:right;width:50px"></label>
			<input id="btnCancel" class="btn btn-primary" type="button" style="width:80px;" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>