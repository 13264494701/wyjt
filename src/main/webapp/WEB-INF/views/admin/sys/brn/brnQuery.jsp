<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>机构管理</title>
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
		<li><a href="${ctx}/sys/brn/list">机构列表</a></li>
		<li class="active"><a href="${ctx}/sys/brn/form?id=${brn.id}&parent.id=${brn.parent.id}">机构<shiro:hasPermission name="sys:brn:edit">${not empty brn.id?'查看':'添加'}</shiro:hasPermission><shiro:lacksPermission name="sys:brn:edit">查看</shiro:lacksPermission></a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="brn"  method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>
		<div class="control-group">
			<label class="control-label">归属区域:</label>
			<div class="controls">
                <sys:treeselect id="area" name="area.id" value="${brn.area.id}" labelName="area.name" labelValue="${brn.area.name}"
					title="区域" baseUrl="${admin}"  url="/sys/area/treeData" cssClass="required" disabled="disabled" />
			</div>
		</div>
		<c:if test="${brn.brnGrade != '1'}">
	 		<div class="control-group">
				<label class="control-label">上级机构:</label>
				<div class="controls">
	                <sys:treeselect  id="brn" name="parent.id" value="${brn.parent.id}" labelName="parent.brnName" labelValue="${brn.parent.brnName}"
						title="机构" baseUrl="${admin}" url="/sys/brn/treeData" extId="${brn.id}" cssClass="" allowClear="false" disabled="disabled"/> 
				</div>
			</div>
		</c:if>
 		<div class="control-group">
			<label class="control-label">机构级别:</label>
			<div class="controls">
				<form:select path="brnGrade"  disabled="true"  style="width:265px;" class="input-medium required">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('brnGrade')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">机构名称:</label>
			<div class="controls">
				<form:input path="brnName" disabled="true" style="width:250px;" htmlEscape="false" maxlength="50" class="required"/>
				
			</div>
		</div>
 		<div class="control-group">
			<label class="control-label">机构编码:</label>
			<div class="controls">
				<form:input path="brnNo"  disabled="true" style="width:250px;" htmlEscape="false" maxlength="50"/>
			</div>
		</div> 
		<div class="control-group">
			<label class="control-label">主负责人:</label>
			<div class="controls">
				<form:input path="primaryPerson"  disabled="true" htmlEscape="false" style="width:250px;" maxlength="64"/>
			</div>
		</div>

		<div class="control-group">
			<label class="control-label">电话号码:</label>
			<div class="controls">
				<form:input path="phone"  disabled="true"  htmlEscape="false" style="width:250px;" maxlength="50"/>
			</div>
		</div>

		<div class="control-group">
			<label class="control-label">邮箱地址:</label>
			<div class="controls">
				<form:input path="email"  disabled="true" htmlEscape="false" style="width:250px;" maxlength="50"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">联系地址:</label>
			<div class="controls">
				<form:input path="address"  disabled="true" htmlEscape="false" style="width:250px;" maxlength="50"/>
			</div>
		</div>

		<div class="control-group">
			<label class="control-label">备注:</label>
			<div class="controls">
				<form:textarea path="rmk"  disabled="true" htmlEscape="false" style="width:250px;" rows="3" maxlength="200" class="input-xlarge"/>
			</div>
		</div>
		<div class="form-actions">
			<input id="btnCancel" class="btn btn-primary" type="button" style="width:80px;" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>