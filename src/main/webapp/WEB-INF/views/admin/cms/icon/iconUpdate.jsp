<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>图标管理</title>
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
		<li><a href="${ctx}/cms/icon/">图标列表</a></li>
		<shiro:hasPermission name="cms:icon:edit">
			<li class="active"><a href="${ctx}/cms/icon/update?id=${icon.id}">图标修改</a></li>
		</shiro:hasPermission>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="cmsIcon" action="${ctx}/cms/icon/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>		
		<div class="control-group">
			<label style="text-align:right;width:150px">图标位置：</label>
			<form:select path="positionNo" style="width:245px" class="input-xlarge required">
			<form:option value="" label="请选择"/>
				<form:options items="${fns:getIconPositionList()}" itemLabel="positionName" itemValue="positionNo" htmlEscape="false"/>
			</form:select>
			<span class="help-inline"  style="width:150px"><font color="red">*</font> </span>
		</div>
		<div class="control-group">
			<label style="text-align:right;width:150px">图标名称：</label>
			<form:input path="iconName" style="width:230px" htmlEscape="false" maxlength="64" class="input-xlarge required"/>
			<span class="help-inline"  style="width:150px"><font color="red">*</font> </span>
		</div>
		<div class="control-group" id="imgSelectDiv">
			<label style="text-align:right;width:150px">图片选择：</label>
			<div class="controls">
			<form:hidden id="imagePath" path="imagePath" htmlEscape="false" maxlength="255" class="input-xlarge"/>
			<sys:ckfinder input="imagePath" type="images" uploadPath="/cms/icon" selectMultiple="false"/>
			<span class="help-inline"  style="width:150px"></span>	
			</div>
		</div>
		<div class="control-group">
			<label style="text-align:right;width:150px">跳转类型：</label>
			<form:select path="redirectType" style="width:245px" class="input-xlarge required">
				<form:options  items="${fns:getDictList('redirectType')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
			</form:select>
			<span class="help-inline"  style="width:150px"><font color="red">*</font> </span>
		</div>
		<div class="control-group">
			<label style="text-align:right;width:150px">链接地址：</label>
			<form:input path="redirectUrl" style="width:630px" htmlEscape="false" maxlength="255" class="input-xlarge"/>
			<span class="help-inline"  style="width:150px"></span>	
		</div>
		<div class="control-group">
			<label style="text-align:right;width:150px">备注：</label>
			<form:textarea path="rmk" htmlEscape="false" rows="4" maxlength="255" class="input-xxlarge "/>
			<span class="help-inline"  style="width:150px"></span>	
		</div>
		<div class="control-group">
			<label style="text-align:right;width:150px">生成时间：</label>
			<input name="createTime" type="text" style="width:230px" disabled="true" maxlength="20" class="input-medium Wdate "
				value="<fmt:formatDate value="${icon.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"
				onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:true});"/>
			<span class="help-inline"  style="width:150px"></span>	
		</div>

		<div class="form-actions">
			<shiro:hasPermission name="cms:icon:edit"><input id="btnSubmit" class="btn btn-primary" style="width:80px;" type="submit" value="保   存"/>&nbsp;</shiro:hasPermission>
			<label style="text-align:right;width:50px"></label>
			<input id="btnCancel" class="btn btn-primary" type="button" style="width:80px;" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>