<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>通知管理</title>
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
		<li><a href="${ctx}/cmsNotice/">通知列表</a></li>
		<shiro:hasPermission name="notice:cmsNotice:edit">
		<li class="active"><a href="${ctx}/cmsNotice/add?id=${cmsNotice.id}">通知添加</a></li>
		</shiro:hasPermission>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="cmsNotice" enctype="multipart/form-data" action="${ctx}/cmsNotice/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>		
		<div class="control-group">
			<label style="text-align:right;width:150px">通知标题：</label>
			<form:input path="title" style="width:630px" htmlEscape="false" maxlength="255" class="input-xlarge required"/>
			<span class="help-inline"  style="width:150px"><font color="red">*</font> </span>
		</div>
		<div class="control-group">
			<label style="text-align:right;width:150px">选择图片：</label>
				<div class="controls">
					<form:hidden id="image"  path="image" htmlEscape="false" maxlength="2048" class="input-xlarge"/>
					<sys:ckfinder input="image"  type="images" uploadPath="/cms/notice" selectMultiple="false"/>
					<span class="help-inline"  style="width:150px"></span>
				</div>	
		</div>
		<div class="control-group">
			<label style="text-align:right;width:150px" class="control-label" >通知正文：</label>
			<div style="text-align:left; margin-left: 155px;">
			    <form:textarea id="content" htmlEscape="true" path="content" rows="4" maxlength="200" class="input-xxlarge"/>
			    <sys:ckeditor replace="content" uploadPath="/cms/notice" />
			</div>
		</div>
		<div class="control-group">
			<label style="text-align:right;width:150px">链接地址：</label>
			<form:input path="url" style="width:630px" htmlEscape="false" maxlength="255" class="input-xlarge "/>
			<span class="help-inline"  style="width:150px"></span>	
		</div>
		<div class="control-group">
			<label style="text-align:right;width:150px">展示顺序：</label>
			<form:input path="sort" style="width:230px" htmlEscape="false" maxlength="11" class="input-xlarge "/>
			<span class="help-inline"  style="width:150px"></span>	
		</div>
		<div class="control-group">
			<label style="text-align:right;width:150px">开始日期：</label>
			<input name="beginDate" type="text" style="width:230px" readonly="readonly" maxlength="20" class="input-medium Wdate "
				value="<fmt:formatDate value="${cmsNotice.beginDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"
				onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:true});"/>
			<span class="help-inline"  style="width:150px"></span>	
		</div>
		<div class="control-group">
			<label style="text-align:right;width:150px">结束日期：</label>
			<input name="endDate" type="text" style="width:230px" readonly="readonly" maxlength="20" class="input-medium Wdate "
				value="<fmt:formatDate value="${cmsNotice.endDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"
				onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:true});"/>
			<span class="help-inline"  style="width:150px"></span>	
		</div>
		<div class="control-group">
			<label style="text-align:right;width:150px">展示位置：</label>
			<form:select path="position" style="width:245px" class="input-xlarge required">
			<form:option value="" label="请选择"/>
				<form:options items="${fns:getDictList('noticePosition')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
			</form:select>
			<span class="help-inline"  style="width:150px"><font color="red">*</font> </span>
		</div>

		<div class="form-actions">
			<shiro:hasPermission name="notice:cmsNotice:edit"><input id="btnSubmit" class="btn btn-primary" style="width:80px;" type="submit" value="保   存"/>&nbsp;</shiro:hasPermission>
			<label style="text-align:right;width:50px"></label>
			<input id="btnCancel" class="btn btn-primary" type="button" style="width:80px;" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>