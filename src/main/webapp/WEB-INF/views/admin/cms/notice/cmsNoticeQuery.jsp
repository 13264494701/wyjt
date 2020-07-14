<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>通知管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">

	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/cmsNotice/">通知列表</a></li>
		<li class="active"><a href="${ctx}/cmsNotice/query?id=${cmsNotice.id}">通知查看</a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="cmsNotice"  method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>		
		<div class="control-group">
			<label style="text-align:right;width:150px">通知标题：</label>
			<form:input path="title" style="width:650px"  readonly="true" htmlEscape="false" maxlength="255" class="input-xlarge "/>
			<span class="help-inline"  style="width:150px"></span>	
		</div>
		<div class="control-group">
			<label style="text-align:right;width:150px">文章封面：</label>
			<div class="controls">
			<ol>
			<li>
            <img src="${cmsNotice.image}" style="max-width:200px;max-height:200px;_height:200px;border:0;padding:3px;">
		    </li>
		    </ol>
		    </div>
		</div>
		<div class="control-group">
			<label style="text-align:right;width:150px" class="control-label" >通知正文：</label>
			<div style="text-align:left; margin-left: 155px;">
			    <form:textarea id="content" htmlEscape="true" readonly="true" path="content" rows="4" maxlength="200" class="input-xxlarge"/>
			    <sys:ckeditor replace="content" uploadPath="/cms/notice" />
			</div>
		</div>
		<div class="control-group">
			<label style="text-align:right;width:150px">链接地址：</label>
			<form:input path="url" style="width:630px"  readonly="true" htmlEscape="false" maxlength="255" class="input-xlarge "/>
			<span class="help-inline"  style="width:150px"></span>	
		</div>
		<div class="control-group">
			<label style="text-align:right;width:150px">展示顺序：</label>
			<form:input path="sort" style="width:230px"  readonly="true" htmlEscape="false" maxlength="11" class="input-xlarge "/>
			<span class="help-inline"  style="width:150px"></span>	
		</div>
		<div class="control-group">
			<label style="text-align:right;width:150px">开始日期：</label>
			<input name="beginDate" type="text" style="width:230px"  disabled="disabled" maxlength="20" class="input-medium Wdate "
				value="<fmt:formatDate value="${cmsNotice.beginDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"
				onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:true});"/>
			<span class="help-inline"  style="width:150px"></span>	
		</div>
		<div class="control-group">
			<label style="text-align:right;width:150px">结束日期：</label>
			<input name="endDate" type="text" style="width:230px"  disabled="disabled" maxlength="20" class="input-medium Wdate "
				value="<fmt:formatDate value="${cmsNotice.endDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"
				onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:true});"/>
			<span class="help-inline"  style="width:150px"></span>	
		</div>
		<div class="control-group">
			<label style="text-align:right;width:150px">生成时间：</label>
			<input name="createTime" type="text" style="width:230px"  disabled="disabled" maxlength="20" class="input-medium Wdate "
				value="<fmt:formatDate value="${cmsNotice.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"
				onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:true});"/>
			<span class="help-inline"  style="width:150px"></span>	
		</div>
		<div class="control-group">
			<label style="text-align:right;width:150px">修改时间：</label>
			<input name="updateTime" type="text" style="width:230px"  disabled="disabled" maxlength="20" class="input-medium Wdate "
				value="<fmt:formatDate value="${cmsNotice.updateTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"
				onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:true});"/>
			<span class="help-inline"  style="width:150px"></span>	
		</div>
		<div class="control-group">
			<label style="text-align:right;width:150px">展示位置：</label>
			<form:select path="position" disabled="true" style="width:245px" class="input-xlarge required">
				<form:options items="${fns:getDictList('noticePosition')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
			</form:select>
			<span class="help-inline"  style="width:150px"><font color="red">*</font> </span>
		</div>
		<div class="form-actions">
			<input id="btnCancel" class="btn btn-primary" type="button" style="width:80px;" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>