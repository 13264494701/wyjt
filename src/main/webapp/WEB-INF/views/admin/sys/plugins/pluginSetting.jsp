<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>插件设置</title>
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
		<li><a href="${ctx}/plugin/list/${pluginType}">插件列表</a></li>
		<shiro:hasPermission name="plugins:edit">
		<li class="active"><a href="${ctx}/plugin/setting/${pluginId}">插件设置</a></li>
		</shiro:hasPermission>
	</ul><br/>
	<form id="inputForm"  action="${ctx}/plugin/setting" method="post"  class="form-horizontal">
		<sys:message content="${message}"/>
		<input type="hidden" name="id" value="${pluginId}"/>		
		<c:forEach items="${attrs}" var="attr" varStatus="vs">
			<div class="control-group">
				<input type="hidden" name="pluginConfigAttrs[${vs.index}].field" value="${attr.field}"/>
				<input type="hidden" name="pluginConfigAttrs[${vs.index}].name" value="${attr.name}"/>
				<input type="hidden" name="pluginConfigAttrs[${vs.index}].sort" value="${attr.sort}"/>
				<label class="control-label">${attr.name}：</label>
				<div class="controls">
				<c:choose>
					<c:when test="${attr.showType == 'INPUT'}">
						<input name="pluginConfigAttrs[${vs.index}].value" type="text" style="width:1024px"  value="${attr.value}" class="input-xlarge ${attr.required}"/>
					</c:when>
					<c:when test="${attr.showType == 'PASSWORD'}">
						<input name="pluginConfigAttrs[${vs.index}].value" type="password" style="width:270px"  value="${attr.value}" class="input-xlarge ${attr.required}"/>
					</c:when>
					<c:when test="${attr.showType == 'SELECT'}">
				      <select name="pluginConfigAttrs[${vs.index}].value" value = "${attr.value}" style="width:270px" class="input-xlarge ${attr.required}">
				            <option value="">请选择</option> 
			                <c:forEach items="${fns:getDictList(attr.field)}" var="feeType"> 
                               <option value="${feeType.value}" <c:if test="${attr.value ==feeType.value}">selected</c:if>>${feeType.label}</option> 
                           </c:forEach> 
			          <select>
					</c:when>
					<c:when test="${attr.showType == 'TEXTAREA'}">
						<textarea name="pluginConfigAttrs[${vs.index}].value"  rows="4"  class="input-xlarge ${attr.required}">${attr.value}</textarea>
					</c:when>
					<c:when test="${attr.showType == 'IMAGESELECT'}">
						<input type="hidden" id="${attr.field}"  name="pluginConfigAttrs[${vs.index}].value" value="${attr.value}" maxlength="255" class="input-xlarge"/>
						<sys:ckfinder input="${attr.field}" type="images" uploadPath="/plugin/images" selectMultiple="false"/>
					</c:when>
					<c:when test="${attr.showType == 'FILESELECT'}">
						<input type="hidden" id="${attr.field}"  name="pluginConfigAttrs[${vs.index}].value" value="${attr.value}"  maxlength="255" class="input-xlarge"/>
						<sys:ckfinder input="${attr.field}" type="files" uploadPath="/plugin/files" selectMultiple="false"/>
					</c:when>
					<c:otherwise>
						<input name="pluginConfigAttrs[${vs.index}].value" type="text" style="width:270px"  value="${attr.value}" class="input-xlarge ${attr.required}"/>
					</c:otherwise>
				</c:choose>
				<span class="help-inline"  style="width:150px">${attr.required?'<font color="red">*</font>':'' } </span>
				</div>
			</div>
		</c:forEach>
		<div class="control-group">
			<label class="control-label">排序</label>
			<div class="controls">
				<input type="text" name="sort" value="${order}" style="width:270px" class="input-xlarge">
				<span class="help-inline" style="width:150px"></span>
			</div>
		</div>
		<div class="control-group"  style="text-align:center">
			<input id="btnSubmit" class="btn btn-primary" style="width:80px;" type="submit" value="保   存"/>
			<label style="text-align:right;width:50px"></label>
			<input id="btnCancel" class="btn btn-primary" type="button" style="width:80px;" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form>
</body>
</html>