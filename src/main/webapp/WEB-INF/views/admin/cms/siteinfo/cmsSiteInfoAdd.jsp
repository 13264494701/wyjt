<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>站点信息管理</title>
	<meta name="decorator" content="default"/>
	<script id="ckeditor_lib" type="text/javascript" src="${ctxStatic}/ckeditor/ckeditor.js"></script>
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
			$("#editType").change(function(){
				var editType = $("#editType").val();
 				if (CKEDITOR.instances['content']) {
					CKEDITOR.instances['content'].destroy();
				} 
				if(editType != "textarea"){
					var content = CKEDITOR.replace("content");
					content.config.height = "";
					content.config.ckfinderPath="${ctxStatic}/ckfinder";
					var date = new Date(), year = date.getFullYear(), month = (date.getMonth()+1)>9?date.getMonth()+1:"0"+(date.getMonth()+1);
					content.config.ckfinderUploadPath="/cms/siteinfo/"+year+"/"+month+"/";
				}else{
					$("#content").val(getSimpleText($("#content").val()).trim())
				}
			})
			function getSimpleText(html){
				var re1 = new RegExp("<.+?>","g");//匹配html标签的正则表达式，"g"是搜索匹配多个符合的内容
				var msg = html.replace(re1,'');//执行替换成空字符
				return msg;
			}
		});
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/cms/cmsSiteInfo/">站点信息列表</a></li>
		<shiro:hasPermission name="cms:cmsSiteInfo:edit">
		<li class="active"><a href="${ctx}/cms/cmsSiteInfo/add?id=${cmsSiteInfo.id}">站点信息添加</a></li>
		</shiro:hasPermission>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="cmsSiteInfo" action="${ctx}/cms/cmsSiteInfo/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>		
		<div class="control-group">
			<label style="text-align:right;width:150px">引用名称：</label>
			<form:input path="key" style="width:245px" htmlEscape="false" maxlength="100" class="input-xlarge required"/>
			<span class="help-inline"  style="width:150px"><font color="red">*</font> </span>
		</div>
		<div class="control-group">
			<label style="text-align:right;width:150px">编辑类型：</label>
			<form:select path="type" style="width:255px" id="editType" class="input-xlarge required">
			<form:option value="" label="请选择"/>
				<form:options items="${fns:getDictList('editType')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
			</form:select>
			<span class="help-inline"  style="width:150px"><font color="red"> * </font> </span>
		</div>
		<div class="control-group">
			<label style="text-align:right;width:150px" class="control-label" >显示内容：</label>
			<div style="text-align:left; margin-left: 155px;">
				<form:textarea id="content" htmlEscape="true" path="value" rows="4" maxlength="200" class="input-xlarge required"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group"  style="text-align:center">
			<shiro:hasPermission name="cms:cmsSiteInfo:edit"><input id="btnSubmit" class="btn btn-primary" style="width:80px;" type="submit" value="保   存"/>&nbsp;</shiro:hasPermission>
			<label style="text-align:right;width:50px"></label>
			<input id="btnCancel" class="btn btn-primary" type="button" style="width:80px;" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>