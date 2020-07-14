<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>机构账户管理</title>
	<meta name="decorator" content="default"/>
	<script src="${ctxStatic}/jquery-form/jquery.form.js"></script>
	<script type="text/javascript">
		$(document).ready(function() {
			//$("#name").focus();
			var form = $("#inputForm");
			$("#inputForm").validate({
				onfocusout: function(element){
			        $(element).valid();
			    },
				submitHandler: function(){
					loading('正在提交，请稍等...');				   
					$.ajax({
						url:form.attr("action"),
						type:form.attr("method"),
						data:form.serialize(), 
						success:function(res){							
				          if(res.type=='success'){
				        	  showTip(res.content);				        	
				        	  var d = parent.dialog.get('updateActBal');	
				        	  setTimeout(function(){  
				        		  d.close(res.type); 
				        		  }
				        	  ,2000);//单位毫秒   
				        	 
				          }
						},
						error:function(e){
							alert(e.type);
						}
					})
					
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

	</ul><br/>
	<form:form id="inputForm" modelAttribute="nfsBrnAct" action="${ctx}/nfsBrnAct/updateActBal" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>		
		<div class="control-group">
			<label style="text-align:right;width:150px">归属公司：</label>
				<sys:treeselect id="company" name="company.id" value="${nfsBrnAct.company.id}" labelName="company.brnName" labelValue="${nfsBrnAct.company.brnName}"
					title="部门"  baseUrl="${admin}" url="/sys/brn/treeData?type=2" cssClass="required" cssStyle="width:184px;" allowClear="true" notAllowSelectParent="true" disabled="disabled"/>
		</div>
		<div class="control-group">
			<label style="text-align:right;width:150px">账户科目：</label>
			<form:select path="subNo" style="width:244px" disabled="true" class="input-xlarge">
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
			<form:input path="actNam" style="width:230px" readonly="true" htmlEscape="false" maxlength="64" class="input-xlarge"/>
		</div>
		<div class="control-group">
			<label style="text-align:right;width:150px">账户余额：</label>
			<div class="input-append">
			   <form:input path="curBal" value="${fns:decimalToStr(nfsBrnAct.curBal,2) }" style="width:204px" readonly="true" htmlEscape="false" class="input-xlarge required"/>
			   <span class="add-on">元</span>
			</div>
			<span class="help-inline"  style="width:150px"></span>
		</div>
		<div class="control-group">
			<label style="text-align:right;width:150px">加减款额：</label>
			<div class="input-append">
			   <input name="trxAmt" style="width:214px;height:24px;" htmlEscape="false" class="input-xlarge required"/>
			   <span class="add-on">元</span>
			</div>
			<font color="red">*</font>
		</div>
		<div class="form-actions">
			<shiro:hasPermission name="brn:nfsBrnAct:edit"><input id="btnSubmit" class="btn btn-primary" style="width:80px;" type="submit" value="提    交"/>&nbsp;</shiro:hasPermission>		
		</div>
	</form:form>
</body>
</html>