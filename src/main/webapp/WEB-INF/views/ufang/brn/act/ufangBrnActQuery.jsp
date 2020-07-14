<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>机构账户管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">

	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${ufang}/brn/ufangBrnAct/">机构账户列表</a></li>
		<li class="active"><a href="${ufang}/brn/ufangBrnAct/query?id=${ufangBrnAct.id}">机构账户查看</a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="ufangBrnAct"  method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>		
		<div class="control-group">
			<label style="text-align:right;width:150px">归属公司：</label>
				<sys:treeselect id="company" name="company.id" value="${ufangBrnAct.company.id}" labelName="company.brnName" labelValue="${ufangBrnAct.company.brnName}"
					title="部门" url="/sys/brn/treeData?type=2" cssClass="required" cssStyle="width:184px;" allowClear="true" notAllowSelectParent="true" disabled="disabled"/>
		</div>
		<div class="control-group">
			<label style="text-align:right;width:150px">账户科目：</label>
			<form:select path="subNo" style="width:244px" disabled="true" class="input-xlarge required">
				<form:options items="${fns:getDictList('subNo')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
			</form:select>
		</div>
		<div class="control-group">
			<label style="text-align:right;width:150px">账户币种：</label>
			<form:select path="currCode" style="width:244px" disabled="true" class="input-xlarge">
				<form:options items="${fns:getDictList('currCode')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
			</form:select>
			<span class="help-inline"  style="width:150px"></span>	
		</div>
		<div class="control-group">
			<label style="text-align:right;width:150px">账户名称：</label>
			<form:input path="actNam" style="width:230px"  readonly="true" htmlEscape="false" maxlength="64" class="input-xlarge "/>
			<span class="help-inline"  style="width:150px"></span>	
		</div>
		<div class="control-group">
			<label style="text-align:right;width:150px">账户余额：</label>
			<form:input path="curBal" style="width:230px"  readonly="true" htmlEscape="false" class="input-xlarge "/>
			<span class="help-inline"  style="width:150px"></span>	
		</div>
		<div class="control-group">
			<label style="text-align:right;width:150px">账户状态：</label>
			<form:select path="status" style="width:244px" disabled="true" class="input-xlarge">
				<form:options items="${fns:getDictList('actStatus')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
			</form:select>
			<span class="help-inline"  style="width:150px"></span>	
		</div>
		<div class="control-group">
			<label style="text-align:right;width:150px">创建时间：</label>
			<input name="createTime" type="text" style="width:230px"  disabled="disabled" maxlength="20" class="input-medium Wdate "
				value="<fmt:formatDate value="${ufangBrnAct.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"
				onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:true});"/>
			<span class="help-inline"  style="width:150px"></span>	
		</div>
		<div class="control-group">
			<label style="text-align:right;width:150px">更新时间：</label>
			<input name="updateTime" type="text" style="width:230px"  disabled="disabled" maxlength="20" class="input-medium Wdate "
				value="<fmt:formatDate value="${ufangBrnAct.updateTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"
				onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:true});"/>
			<span class="help-inline"  style="width:150px"></span>	
		</div>
		<div class="form-actions">
		    <a href="${ufang}/ufangBrnAct/delete?id=${ufangBrnAct.id}" class="btn btn-primary " style="width:60px;" type="button" onclick="return confirmx('确认要删除该机构账户吗？', this.href)">删除</a>
			<label style="text-align:right;width:50px"></label>
			<input id="btnCancel" class="btn btn-primary" type="button" style="width:80px;" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>