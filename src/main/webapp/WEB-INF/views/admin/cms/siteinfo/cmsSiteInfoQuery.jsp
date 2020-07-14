<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>站点信息管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">

	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/cms/cmsSiteInfo/">站点信息列表</a></li>
		<li class="active"><a href="${ctx}/cms/cmsSiteInfo/query?id=${cmsSiteInfo.id}">站点信息查看</a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="cmsSiteInfo"  method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>		
		<div class="control-group">
			<label style="text-align:right;width:150px">引用名称：</label>
			<form:input path="key" style="width:245px"  readonly="true" htmlEscape="false" maxlength="100" class="input-xlarge "/>
			<span class="help-inline"  style="width:150px"></span>	
		</div>
		<div class="control-group">
			<label style="text-align:right;width:150px">编辑类型：</label>
			<form:select path="type" style="width:255px" disabled="true" class="input-xlarge required">
			<form:option value="" label="请选择"/>
				<form:options items="${fns:getDictList('editType')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
			</form:select>
			<span class="help-inline"  style="width:150px"><font color="red">*</font> </span>
		</div>
		<div class="control-group">
			<label style="text-align:right;width:150px" class="control-label" >显示内容：</label>
			<div style="text-align:left; margin-left: 155px;">
				<form:textarea id="content" htmlEscape="true" path="value" rows="4" maxlength="200" readonly="true" class="input-xlarge required"/>
				<c:choose>
					  <c:when test="${cmsSiteInfo.type=='ckeditor'}">
		                 <sys:ckeditor replace="content" readOnly="true" uploadPath="/cms/siteinfo" />
		        	  </c:when>
		        	  <c:otherwise>
		        	  </c:otherwise>
				</c:choose>				
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label style="text-align:right;width:150px">创建时间：</label>
			<input name="createTime" type="text" style="width:230px"  disabled="disabled" maxlength="20" class="input-medium Wdate "
				value="<fmt:formatDate value="${cmsSiteInfo.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"
				onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:true});"/>
			<span class="help-inline"  style="width:150px"></span>	
		</div>
		<div class="control-group">
			<label style="text-align:right;width:150px">更新时间：</label>
			<input name="updateTime" type="text" style="width:230px"  disabled="disabled" maxlength="20" class="input-medium Wdate "
				value="<fmt:formatDate value="${cmsSiteInfo.updateTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"
				onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:true});"/>
			<span class="help-inline"  style="width:150px"></span>	
		</div>
		<div class="control-group"  style="text-align:center">
			<input id="btnCancel" class="btn btn-primary" type="button" style="width:80px;" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>