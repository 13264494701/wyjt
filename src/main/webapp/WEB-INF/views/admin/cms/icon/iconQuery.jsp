<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>图标管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">

	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/cms/icon/">图标列表</a></li>
		<li class="active"><a href="${ctx}/cms/icon/query?id=${icon.id}">图标查看</a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="cmsIcon"  method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>		
		<div class="control-group">
			<label style="text-align:right;width:150px">图标位置：</label>
			<form:select path="positionNo" style="width:245px"  disabled="true"  class="input-xlarge ">
			<form:option value="" label="请选择"/>
				<form:options items="${fns:getIconPositionList()}" itemLabel="positionName" itemValue="positionNo" htmlEscape="false"/>
			</form:select>
			<span class="help-inline"  style="width:150px"></span>	
		</div>
		<div class="control-group">
			<label style="text-align:right;width:150px">图标名称：</label>
			<form:input path="iconName" style="width:230px"  readonly="true" htmlEscape="false" maxlength="64" class="input-xlarge "/>
			<span class="help-inline"  style="width:150px"></span>	
		</div>
		<div class="control-group" id="imgSelectDiv">
			<label style="text-align:right;width:150px">图片路径：</label>
			<div class="controls">
			<form:hidden id="imagePath" path="imagePath" htmlEscape="false" maxlength="255" class="input-xlarge"/>
			<sys:ckfinder input="imagePath" readonly="true" type="images" uploadPath="/cms/icon" selectMultiple="false"/>
			<span class="help-inline"  style="width:150px"></span>	
			</div>
		</div>
		<div class="control-group">
			<label style="text-align:right;width:150px">链接地址：</label>
			<form:input path="url" style="width:630px"  readonly="true" htmlEscape="false" maxlength="255" class="input-xlarge "/>
			<span class="help-inline"  style="width:150px"></span>	
		</div>
		<div class="control-group">
			<label style="text-align:right;width:150px">备注：</label>
			<form:textarea path="rmk"  readonly="true" htmlEscape="false" rows="4" maxlength="255" class="input-xxlarge "/>
			<span class="help-inline"  style="width:150px"></span>	
		</div>
		<div class="control-group">
			<label style="text-align:right;width:150px">生成时间：</label>
			<input name="createTime" type="text" style="width:230px"  disabled="true" maxlength="20" class="input-medium Wdate "
				value="<fmt:formatDate value="${icon.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"
				onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:true});"/>
			<span class="help-inline"  style="width:150px"></span>	
		</div>
		<div class="control-group">
			<label style="text-align:right;width:150px">修改时间：</label>
			<input name="updateTime" type="text" style="width:230px"  disabled="true" maxlength="20" class="input-medium Wdate "
				value="<fmt:formatDate value="${icon.updateTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"
				onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:true});"/>
			<span class="help-inline"  style="width:150px"></span>	
		</div>
		<div class="form-actions">
			<input id="btnCancel" class="btn btn-primary" type="button" style="width:80px;" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>