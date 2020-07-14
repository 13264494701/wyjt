<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>贷超管理管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">

	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${admin}/ufangLoanMarket/">贷超列表</a></li>
		<li class="active"><a href="${admin}/ufangLoanMarket/query?id=${ufangLoanMarket.id}">贷超查看</a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="ufangLoanMarket"  method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>	
		<div class="control-group">
			<label style="text-align:right;width:150px">归属机构：</label>
            <sys:treeselect id="brn" name="brn.id" value="${ufangLoanMarket.brn.id}" labelName="brn.brnName" labelValue="${ufangLoanMarket.brn.brnName}"
					title="部门"  baseUrl="${admin}" url="/ufang/brn/treeData" disabled = "disabled" cssClass="required" notAllowSelectParent="false"/>	
			<span class="help-inline"  style="width:168px"></span>
		</div> 	
		<div class="control-group">
			<label style="text-align:right;width:150px">贷超名称：</label>
			<form:input path="name" style="width:230px"  readonly="true" htmlEscape="false" maxlength="100" class="input-xlarge "/>
			<span class="help-inline"  style="width:150px"></span>	
		</div>
		<div class="control-group">
			<label style="text-align:right;width:150px">图标选择：</label>
			<div class="controls">
			<form:hidden id="logo" path="logo" htmlEscape="false" maxlength="255" class="input-xlarge"/>
			<sys:ckfinder input="logo" type="images" uploadPath="/smarket/icon" readonly = "true" selectMultiple="false"/>
			<span class="help-inline"  style="width:150px"></span>	
			</div>
		</div>

		<div class="control-group">
			<label style="text-align:right;width:150px">最小借款金额：</label>
			<form:input path="minLoanAmt" style="width:230px"  readonly="true" htmlEscape="false" class="input-xlarge "/>

			<label style="text-align:right;width:150px">最大借款金额：</label>
			<form:input path="maxLoanAmt" style="width:230px"  readonly="true" htmlEscape="false" class="input-xlarge "/>
			<span class="help-inline"  style="width:150px"></span>	
		</div>
		<div class="control-group">
			<label style="text-align:right;width:150px">最短借款周期：</label>
			<form:input path="minLoanTerm" style="width:230px"  readonly="true" htmlEscape="false" maxlength="11" class="input-xlarge "/>

			<label style="text-align:right;width:150px">最长借款周期：</label>
			<form:input path="maxLoanTerm" style="width:230px"  readonly="true" htmlEscape="false" maxlength="11" class="input-xlarge "/>
			<span class="help-inline"  style="width:150px"></span>	
		</div>
		<div class="control-group">
			<label style="text-align:right;width:150px">最小利率：</label>
			<form:input path="minIntRate" style="width:230px"  readonly="true" htmlEscape="false" maxlength="4" class="input-xlarge "/>

			<label style="text-align:right;width:150px">最大利率：</label>
			<form:input path="maxIntRate" style="width:230px"  readonly="true" htmlEscape="false" maxlength="4" class="input-xlarge "/>
			<span class="help-inline"  style="width:150px"></span>	
		</div>

		<div class="control-group">
			<label style="text-align:right;width:150px">展示已放款笔数：</label>
			<form:input path="displayLoanQuantity" style="width:230px"  readonly="true" htmlEscape="false" maxlength="11" class="input-xlarge "/>

			<label style="text-align:right;width:150px">审批时长：</label>
			<form:input path="checkTerm" style="width:230px"  readonly="true" htmlEscape="false" maxlength="11" class="input-xlarge "/>
			<span class="help-inline"  style="width:150px"></span>	
		</div>
		<div class="control-group">
			<label style="text-align:right;width:150px">是否需要认证：</label>
			<form:radiobuttons path="needsIdentify" disabled = "true" items="${fns:getDictList('trueOrFalse')}" itemLabel="label" itemValue="value" htmlEscape="false" class="required"/>
	        <span class="help-inline"  style="width:168px"><font color="red">*</font> </span>
		</div>
		<div class="control-group">
			<label style="text-align:right;width:150px">贷款要求：</label>
			<form:input path="loanRequirement" style="width:230px"  readonly="true" htmlEscape="false" maxlength="1024" class="input-xlarge "/>
			<span class="help-inline"  style="width:150px"></span>	
		</div>
		<div class="control-group">
			<label style="text-align:right;width:150px">申请材料：</label>
			<form:input path="applyMaterials" style="width:230px"  readonly="true" htmlEscape="false" maxlength="255" class="input-xlarge "/>
			<span class="help-inline"  style="width:150px"></span>	
		</div>
		<div class="control-group">
			<label style="text-align:right;width:150px">跳转链接：</label>
			<form:input path="redirectUrl" style="width:630px" htmlEscape="false" maxlength="255" class="input-xlarge "/>
			<span class="help-inline"  style="width:150px"></span>	
		</div>
		<div class="control-group">
			<label style="text-align:right;width:150px">是否上架：</label>
			<form:radiobuttons path="isMarketable" disabled = "true" items="${fns:getDictList('trueOrFalse')}" itemLabel="label" itemValue="value" htmlEscape="false" class="required"/>
	        <span class="help-inline"  style="width:168px"><font color="red">*</font> </span>
		</div>
		<div class="form-actions">
			<input id="btnCancel" class="btn btn-primary" type="button" style="width:80px;" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>