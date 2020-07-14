<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>交易规则管理</title>
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
		function changeTrxRole(obj){
			var $subNo = $("#subNo");
			var trxRole = $(obj).val();
			$.ajax({
				type : 'POST',
				url : '${ctx}/nfsActSub/getSubList',
				data : {
					trxRole:trxRole
				},
				success : function(rsp) {
					var subList = rsp.result;
					var i;	
					$subNo.empty(); //清空原有的选项 
					for(i=0;i<subList.length;i++){
			            var option = "<option value='" + subList[i].subNo + "'>" + subList[i].name + "</option>";
			            $subNo.append(option);			            
			        }
					$subNo.prev().find("span.select2-chosen").text(subList[0].name);
				}
			});
		}
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/nfsTrxRuleItem/?trxCode=${nfsTrxRuleItem.trxCode}">规则明细列表</a></li>
		<shiro:hasPermission name="trx:nfsTrxRuleItem:edit">
		<li class="active"><a href="${ctx}/nfsTrxRuleItem/add?trxCode=${nfsTrxRuleItem.trxCode}">规则明细添加</a></li>
		</shiro:hasPermission>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="nfsTrxRuleItem" action="${ctx}/nfsTrxRuleItem/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>		
		<div class="control-group">
			<label style="text-align:right;width:150px">交易代码：</label>
			<form:input path="trxCode" style="width:230px" readonly="true" htmlEscape="false" maxlength="5" class="input-xlarge"/>
			<span class="help-inline"  style="width:150px"></span>
		</div>
		<div class="control-group">
			<label style="text-align:right;width:150px">交易名称：</label>
			<form:input path="name" style="width:630px" readonly="true" htmlEscape="false" maxlength="127" class="input-xlarge"/>
			<span class="help-inline"  style="width:150px"> </span>
		</div>
		<div class="control-group">
			<label style="text-align:right;width:150px">业务角色：</label>
			<form:select path="trxRole" onchange="changeTrxRole(this)" style="width:245px" class="input-xlarge required">
				<form:option value="" label="请选择"/>
				<form:options items="${fns:getDictList('trxRole')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
			</form:select>
			<span class="help-inline"  style="width:150px"><font color="red">*</font> </span>
		</div>
		<div class="control-group">
			<label style="text-align:right;width:150px">交易方向：</label>
			<form:select path="drc" style="width:245px" class="input-xlarge required">
				<form:option value="" label="请选择"/>
				<form:options items="${fns:getDictList('drc')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
			</form:select>
			<span class="help-inline"  style="width:150px"><font color="red">*</font> </span>
		</div>
		<div class="control-group">
			<label style="text-align:right;width:150px">归属科目：</label>
			<form:select path="subNo" style="width:245px" class="input-xlarge required">
					<form:option value="" label="请选择"/>
			</form:select>
			<span class="help-inline"  style="width:150px"><font color="red">*</font> </span>
		</div>


		<div class="control-group">
			<label style="text-align:right;width:150px">金额计算表达式：</label>
			<form:input path="expression" style="width:630px" htmlEscape="false" maxlength="255" class="input-xlarge required"/>
			<span class="help-inline"  style="width:150px"><font color="red">*</font> </span>
		</div>
		<div class="control-group">
			<label style="text-align:right;width:150px">流水标题：</label>
			<form:input path="title" style="width:630px" htmlEscape="false" maxlength="255" class="input-xlarge  "/>
			<span class="help-inline"  style="width:150px"> </span>
		</div>
		<div class="control-group">
			<label style="text-align:right;width:150px">流水组(前台展示)：</label>
			<form:select path="trxGroup" style="width:245px" class="input-xlarge ">
				<form:option value="" label="请选择"/>
				<form:options items="${fns:getDictList('trxGroup')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
			</form:select>
			<span class="help-inline"  style="width:150px"></span>
		</div>
		<div class="control-group">
			<label style="text-align:right;width:150px">流水备注：</label>
			<form:input path="rmk" style="width:630px" htmlEscape="false" maxlength="255" class="input-xlarge required"/>
			<span class="help-inline"  style="width:150px"><font color="red">*</font> </span>
		</div>
		<div class="control-group"  style="text-align:center">
			<shiro:hasPermission name="trx:nfsTrxRuleItem:edit"><input id="btnSubmit" class="btn btn-primary" style="width:80px;" type="submit" value="保   存"/>&nbsp;</shiro:hasPermission>
			<label style="text-align:right;width:50px"></label>
			<input id="btnCancel" class="btn btn-primary" type="button" style="width:80px;" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>