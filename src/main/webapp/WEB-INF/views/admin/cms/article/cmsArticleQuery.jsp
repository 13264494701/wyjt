<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>文章管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">

	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/cms/cmsArticle/">文章列表</a></li>
		<li class="active"><a href="${ctx}/cms/cmsArticle/query?id=${cmsArticle.id}">文章查看</a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="cmsArticle"  method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>		
		<div class="control-group">
			<label style="text-align:right;width:150px">文章标题：</label>
			<form:input path="title" style="width:430px" htmlEscape="false" maxlength="255" readonly="true" class="input-xlarge required"/>
			<span class="help-inline"  style="width:150px"><font color="red">*</font> </span>
		</div>
		<div class="control-group">
			<label style="text-align:right;width:150px">展示频道：</label>
			<form:select path="channel.id"  style="width:245px" disabled="true" class="input-xlarge required">
				   <form:option value="" label="请选择"/>
				   <form:options items="${fns:getChannelList()}" itemLabel="name" itemValue="id" htmlEscape="false"/>
			</form:select>
			<span class="help-inline"  style="width:80px"><font color="red">*</font> </span>

			<label style="text-align:right;width:80px">归属分类：</label>	
            <sys:treeselect id="categoryId" name="category.id" value="${cmsArticle.category.id}" labelName="category.name" labelValue="${cmsArticle.category.name}" cssStyle="width:185px"
					title="栏目" url="/cms/category/treeData"  notAllowSelectRoot="false" notAllowSelectParent="false" disabled = "disabled" cssClass="required"/>
            <span class="help-inline"  style="width:150px"><font color="red">*</font> </span>
		</div>
		<div class="control-group">
			<label style="text-align:right;width:150px">文章封面：</label>
			<div class="controls">
			<ol>
			<li>
            <img src="${cmsArticle.defaultCover}" style="max-width:200px;max-height:200px;_height:200px;border:0;padding:3px;">
		    </li>
		    </ol>
		    </div>
		</div>
		<div class="control-group">
			<label style="text-align:right;width:150px">SEO关键字：</label>
			<form:input path="keywords" style="width:460px" htmlEscape="false" maxlength="255" readonly="true" class="input-xlarge "/>
			<span class="help-inline"  style="width:150px"></span>	
		</div>
		<div class="control-group">
			<label style="text-align:right;width:150px">SEO描述：</label>
			<form:textarea path="description"  htmlEscape="false" rows="4" maxlength="255" readonly="true" class="input-xxlarge "/>
			<span class="help-inline"  style="width:150px"></span>	
		</div>
		<div class="control-group">
			<label style="text-align:right;width:150px" class="control-label" >文章正文：</label>
			<div style="text-align:left; margin-left: 155px;">
			    <form:textarea id="content"  htmlEscape="true" path="articleContent.content" rows="4" class="input-xxlarge"/>
			    <sys:ckeditor replace="content" readOnly="true" uploadPath="/cms/article" />
			</div>
		</div>
		<div class="control-group">
			<label style="text-align:right;width:150px">文章作者：</label>
			<form:input path="author" style="width:230px" htmlEscape="false" maxlength="64" readonly="true" class="input-xlarge "/>

			<label style="text-align:right;width:150px">文章来源：</label>
			<form:input path="copyfrom" style="width:230px" htmlEscape="false" maxlength="64" readonly="true" class="input-xlarge "/>
			<span class="help-inline"  style="width:150px"></span>	
		</div>
		<div class="control-group">
			<label style="text-align:right;width:150px">是否置顶：</label>
			<form:radiobuttons path="isTop" items="${fns:getDictList('trueOrFalse')}" itemLabel="label" itemValue="value" htmlEscape="false" disabled="true" class="required"/>
            <span class="help-inline"  style="width:150px"><font color="red">*</font> </span>
			<label style="text-align:right;width:170px">允许评论：</label>
			<form:radiobuttons path="allowComment" items="${fns:getDictList('trueOrFalse')}" itemLabel="label" itemValue="value" htmlEscape="false" disabled="true" class="required"/>
			<span class="help-inline"  style="width:150px"><font color="red">*</font> </span>
		</div>
		<div class="control-group">
			<label style="text-align:right;width:150px">模板类型：</label>
			<form:select path="moduleType" style="width:245px" disabled="true" class="input-xlarge required">
			<form:option value="" label="请选择"/>
				<form:options items="${fns:getDictList('moduleType')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
			</form:select>
			<span class="help-inline"  style="width:150px"><font color="red">*</font> </span>
		</div>
		<div class="control-group">
			<label style="text-align:right;width:150px">创建时间：</label>
			<input name="createTime" type="text" style="width:230px"  disabled="disabled" maxlength="20" class="input-medium Wdate "
				value="<fmt:formatDate value="${cmsArticle.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"
				onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:true});"/>

			<label style="text-align:right;width:150px">更新时间：</label>
			<input name="updateTime" type="text" style="width:230px"  disabled="disabled" maxlength="20" class="input-medium Wdate "
				value="<fmt:formatDate value="${cmsArticle.updateTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"
				onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:true});"/>
			<span class="help-inline"  style="width:150px"></span>	
		</div>
		<div class="control-group">
			<label style="text-align:right;width:150px">备注信息：</label>
			<form:textarea path="rmk"  readonly="true" htmlEscape="false" rows="4" maxlength="255" class="input-xxlarge "/>
			<span class="help-inline"  style="width:150px"></span>	
		</div>
		<div class="control-group"  style="text-align:center">
			<input id="btnCancel" class="btn btn-primary" type="button" style="width:80px;" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>