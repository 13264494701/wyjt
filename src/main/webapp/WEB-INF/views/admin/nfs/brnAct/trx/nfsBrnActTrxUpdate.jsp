<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>账户交易管理</title>
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
		<li><a href="${ctx}/trx/nfsBrnActTrx/">账户交易列表</a></li>
		<shiro:hasPermission name="trx:nfsBrnActTrx:edit">
			<li class="active"><a href="${ctx}/trx/nfsBrnActTrx/update?id=${nfsBrnActTrx.id}">账户交易修改</a></li>
		</shiro:hasPermission>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="nfsBrnActTrx" action="${ctx}/trx/nfsBrnActTrx/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>		
		<div class="control-group">
			<label style="text-align:right;width:150px">交易编号：</label>
			<form:input path="trxNo" style="width:230px" htmlEscape="false" maxlength="16" class="input-xlarge required"/>
			<span class="help-inline"  style="width:150px"><font color="red">*</font> </span>
		</div>
		<div class="control-group">
			<label style="text-align:right;width:150px">交易类型：</label>
			<form:select path="trxType" style="width:245px" class="input-xlarge required">
			<form:option value="" label="请选择"/>
				<form:options items="${fns:getDictList('')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
			</form:select>
			<span class="help-inline"  style="width:150px"><font color="red">*</font> </span>
		</div>
		<div class="control-group">
			<label style="text-align:right;width:150px">交易状态：</label>
			<form:select path="trxSts" style="width:245px" class="input-xlarge required">
			<form:option value="" label="请选择"/>
				<form:options items="${fns:getDictList('')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
			</form:select>
			<span class="help-inline"  style="width:150px"><font color="red">*</font> </span>
		</div>
		<div class="control-group">
			<label style="text-align:right;width:150px">币种：</label>
			<form:input path="currCode" style="width:230px" htmlEscape="false" maxlength="3" class="input-xlarge required"/>
			<span class="help-inline"  style="width:150px"><font color="red">*</font> </span>
		</div>
		<div class="control-group">
			<label style="text-align:right;width:150px">记账方向：</label>
			<form:input path="actDrc" style="width:230px" htmlEscape="false" maxlength="1" class="input-xlarge required"/>
			<span class="help-inline"  style="width:150px"><font color="red">*</font> </span>
		</div>
		<div class="control-group">
			<label style="text-align:right;width:150px">交易金额：</label>
			<form:input path="trxAmt" style="width:230px" htmlEscape="false" class="input-xlarge required"/>
			<span class="help-inline"  style="width:150px"><font color="red">*</font> </span>
		</div>
		<div class="control-group">
			<label style="text-align:right;width:150px">账户余额：</label>
			<form:input path="curBal" style="width:230px" htmlEscape="false" class="input-xlarge "/>
			<span class="help-inline"  style="width:150px"></span>	
		</div>
		<div class="control-group">
			<label style="text-align:right;width:150px">机构编号：</label>
			<sys:treeselect id="brnNo" style="width:230px" name="brnNo" value="${nfsBrnActTrx.brnNo}" labelName="" labelValue="${nfsBrnActTrx.}"
				title="部门" baseUrl="${admin}" url="/sys/office/treeData?type=2" cssClass="required" allowClear="true" notAllowSelectParent="true"/>
			<span class="help-inline"  style="width:150px"><font color="red">*</font> </span>
		</div>
		<div class="control-group">
			<label style="text-align:right;width:150px">科目编号：</label>
			<form:input path="subNo" style="width:230px" htmlEscape="false" maxlength="4" class="input-xlarge required"/>
			<span class="help-inline"  style="width:150px"><font color="red">*</font> </span>
		</div>
		<div class="control-group">
			<label style="text-align:right;width:150px">账户名称：</label>
			<form:input path="actNam" style="width:230px" htmlEscape="false" maxlength="64" class="input-xlarge required"/>
			<span class="help-inline"  style="width:150px"><font color="red">*</font> </span>
		</div>
		<div class="control-group">
			<label style="text-align:right;width:150px">生成时间：</label>
			<input name="createTime" type="text" style="width:230px" readonly="readonly" maxlength="20" class="input-medium Wdate required"
				value="<fmt:formatDate value="${nfsBrnActTrx.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"
				onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:true});"/>
			<span class="help-inline"  style="width:150px"><font color="red">*</font> </span>
		</div>
		<div class="control-group">
			<label style="text-align:right;width:150px">修改时间：</label>
			<input name="updateTime" type="text" style="width:230px" readonly="readonly" maxlength="20" class="input-medium Wdate required"
				value="<fmt:formatDate value="${nfsBrnActTrx.updateTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"
				onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:true});"/>
			<span class="help-inline"  style="width:150px"><font color="red">*</font> </span>
		</div>
		<div class="control-group"  style="text-align:center">
			<shiro:hasPermission name="trx:nfsBrnActTrx:edit"><input id="btnSubmit" class="btn btn-primary" style="width:80px;" type="submit" value="保   存"/>&nbsp;</shiro:hasPermission>
			<label style="text-align:right;width:50px"></label>
			<input id="btnCancel" class="btn btn-primary" type="button" style="width:80px;" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>