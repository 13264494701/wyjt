<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>会员账户管理</title>
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
		<li><a href="${admin}/memberAct/">会员账户列表</a></li>
		<shiro:hasPermission name="act:memberAct:edit">
			<li class="active"><a href="${admin}/memberAct/update?id=${memberAct.id}">会员账户修改</a></li>
		</shiro:hasPermission>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="memberAct" action="${admin}/memberActTrx/updateMemberActBal" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>		
		<div class="control-group">
			<label style="text-align:right;width:150px">账户名称：</label>
			<form:input path="name" style="width:230px" readonly="true" htmlEscape="false" maxlength="64" class="input-xlarge required"/>
			<span class="help-inline"  style="width:150px"></span>
		</div>
		<div class="control-group">
			<label style="text-align:right;width:150px">账户币种：</label>
			<form:input path="currCode" style="width:230px" readonly="true" htmlEscape="false" maxlength="3" class="input-xlarge required"/>
			<span class="help-inline"  style="width:150px"> </span>
		</div>
		<div class="control-group">
			<label style="text-align:right;width:150px">加减金额：</label>
			<form:input path="trxAmt" style="width:230px" htmlEscape="false" class="input-xlarge required"/>
			<span class="help-inline"  style="width:150px"><font color="red">*</font> </span>
		</div>

		<div class="control-group"  style="text-align:center">
			<shiro:hasPermission name="act:memberAct:edit"><input id="btnSubmit" class="btn btn-primary" style="width:80px;" type="submit" value="保   存"/>&nbsp;</shiro:hasPermission>
			<label style="text-align:right;width:50px"></label>
			<input id="btnCancel" class="btn btn-primary" type="button" style="width:80px;" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>