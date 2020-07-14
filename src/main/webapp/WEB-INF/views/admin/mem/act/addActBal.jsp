<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>机构账户管理</title>
	<meta name="decorator" content="default"/>
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
				        	  var d = parent.dialog.get('addActBal');	
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
	<form:form id="inputForm" modelAttribute="memberAct" action="${admin}/memberAct/addActBal" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>		
		<div class="control-group">
			<label style="text-align:right;width:150px">会员姓名：</label>
			<form:input path="member.name" style="width:230px" readonly="true" htmlEscape="false" maxlength="64" class="input-xlarge"/>
			<span class="help-inline"  style="width:150px"></span>
		</div>
		<div class="control-group">
			<label style="text-align:right;width:150px">账户名称：</label>
			<form:input path="subNo" value="${fns:getSubName('member',memberAct.subNo)}" style="width:230px" readonly="true" htmlEscape="false" maxlength="64" class="input-xlarge"/>
		</div>
		<div class="control-group">
			<label style="text-align:right;width:150px">账户币种：</label>
			<form:select path="currCode" style="width:244px" disabled="true" class="input-xlarge">
				<form:options items="${fns:getDictList('currCode')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
			</form:select>
			<span class="help-inline"  style="width:150px"></span>	
		</div>
		<div class="control-group">
			<label style="text-align:right;width:150px">账户余额：</label>
			<div class="input-append">
			   <form:input path="curBal" value="${fns:decimalToStr(memberAct.curBal,2) }" style="width:204px" readonly="true" htmlEscape="false" class="input-xlarge required"/>
			   <span class="add-on">元</span>
			</div>
			<span class="help-inline"  style="width:150px"></span>
		</div>
		<div class="control-group">
			<label style="text-align:right;width:150px">加款金额：</label>
			<div class="input-append">
			   <input name="trxAmt" style="width:214px;height:24px;" htmlEscape="false" class="input-xlarge required"/>
			   <span class="add-on">元</span>
			</div>
			<font color="red">*</font>
		</div>
		<div class="control-group">
			<label style="text-align:right;width:150px">加款备注：</label>
			<form:input path="rmk" style="width:230px" htmlEscape="false" maxlength="255" class="input-xlarge required"/>
			<span class="help-inline"  style="width:150px"><font color="red">*</font> </span>
		</div>
		<div class="form-actions">
			<shiro:hasPermission name="act:memberAct:edit"><input id="btnSubmit" class="btn btn-primary" style="width:80px;" type="submit" value="提    交"/>&nbsp;</shiro:hasPermission>		
		</div>
	</form:form>
</body>
</html>