<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>撤销提现申请</title>
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
				        	  var d = parent.dialog.get('cancelWdrlRecord');	
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
						error.appendTo(element.next());
					}
				}
			});
		});
	</script>
</head>
<body>
		<br/>
		<form:form id="inputForm" modelAttribute="nfsWdrlRecord" action="${ctx}/wdrlRecord/cancelWdrl" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>		
		<div class="control-group">
			<label style="text-align:right;width:150px">提现编号：</label>
			<form:input path="wdrlNo" style="width:230px" htmlEscape="false" maxlength="16" class="input-xlarge required"/>
			<span class="help-inline"  style="width:50px"><font color="red">*</font> </span>
		</div>
		<div class="control-group">
			<label style="text-align:right;width:150px">会员姓名：</label>
			<form:input path="member.name" style="width:230px" htmlEscape="false" maxlength="64" class="input-xlarge required"/>
			<span class="help-inline"  style="width:50px"><font color="red">*</font> </span>
		</div>
		<div class="control-group">
			<label style="text-align:right;width:150px">提现金额：</label>
			<form:input path="amount" value="${fns:decimalToStr(nfsWdrlRecord.amount,2)}" style="width:230px" htmlEscape="false" class="input-xlarge "/>
			<span class="help-inline"  style="width:50px"></span>	
		</div>
		<div class="control-group">
			<label style="text-align:right;width:150px">手续费：</label>
			<form:input path="fee" value="${fns:decimalToStr(nfsWdrlRecord.fee,2)}" style="width:230px" htmlEscape="false" class="input-xlarge "/>
			<span class="help-inline"  style="width:50px"></span>	
		</div>
		<div class="control-group">
			<label style="text-align:right;width:150px">银行名称：</label>
			<form:input path="bankName" style="width:230px" htmlEscape="false" maxlength="64" class="input-xlarge required"/>
			<span class="help-inline"  style="width:50px"><font color="red">*</font> </span>
		</div>
		<div class="control-group">
			<label style="text-align:right;width:150px">银行卡号：</label>
			<form:input path="cardNo" style="width:230px" htmlEscape="false" maxlength="32" class="input-xlarge required"/>
			<span class="help-inline"  style="width:50px"><font color="red">*</font> </span>
		</div>
		<div class="control-group">
			<label style="text-align:right;width:150px">撤销原因：</label>
			<form:textarea path="rmk" style="width:230px" htmlEscape="false" maxlength="200" class="input-xlarge required"/>
			<span class="help-inline"  style="width:150px"><font color="red">*</font> </span>
		</div>
		<div class="form-actions">
			<shiro:hasPermission name="wdrl:nfsWdrlRecord:edit"><input id="btnSubmit" class="btn btn-primary" style="width:80px;" type="submit" value="保   存"/>&nbsp;</shiro:hasPermission>
		</div>
	</form:form>
</body>
</html>