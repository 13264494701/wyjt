<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>协议管理</title>
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
		<li><a href="${ctx}/cms/contTmpl/">协议列表</a></li>
		<shiro:hasPermission name="cont:cmsContTmpl:edit">
		<li class="active"><a href="${ctx}/cms/contTmpl/add?id=${cmsContTmpl.id}">协议添加</a></li>
		</shiro:hasPermission>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="cmsContTmpl" action="${ctx}/cms/contTmpl/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>		
		<div class="control-group">
			<label style="text-align:right;width:150px">协议类型：</label>
			<form:select path="type" style="width:245px" class="input-xlarge ">
			<form:option value="" label="请选择"/>
				<form:options items="${fns:getDictList('contType')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
			</form:select>
			<span class="help-inline"  style="width:150px"></span>	
		</div>
		<div class="control-group">
			<label style="text-align:right;width:150px">协议状态：</label>
			<form:select path="status" style="width:245px" class="input-xlarge ">
			<form:option value="" label="请选择"/>
				<form:options items="${fns:getDictList('contSts')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
			</form:select>
			<span class="help-inline"  style="width:150px"></span>	
		</div>
		<div class="control-group">
			<label style="text-align:right;width:150px" class="control-label" >模板内容：</label>
			<div style="text-align:left; margin-left: 150px;">
				<form:textarea path="content" htmlEscape="true" rows="4" class="input-xxlarge "/>
				<sys:ckeditor replace="content"  uploadPath="/cont/cmsContTmpl" />
				<span class="help-inline"  style="width:150px"></span>	
			</div>
		</div>
		<div class="control-group">
			<label style="text-align:right;width:150px">生效时间：</label>
			<input name="validTime" type="text" style="width:230px" readonly="readonly" maxlength="20" class="input-medium Wdate "
				value="<fmt:formatDate value="${cmsContTmpl.validTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"
				onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:true});"/>
			<span class="help-inline"  style="width:150px"></span>	
		</div>
		<div class="control-group"  style="text-align:center">
			<shiro:hasPermission name="cont:cmsContTmpl:edit"><input id="btnSubmit" class="btn btn-primary" style="width:80px;" type="submit" value="保   存"/>&nbsp;</shiro:hasPermission>
			<label style="text-align:right;width:50px"></label>
			<input id="btnCancel" class="btn btn-primary" type="button" style="width:80px;" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>