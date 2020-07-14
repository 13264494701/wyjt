<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>会员注销申请管理</title>
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
				          if(res.code == '0'){
				        	  showTip(res.message);				        	
				        	  var d = parent.dialog.get('memberCancellationUpdate');	
				        	  setTimeout(function(){  
				        		  d.close(res.code); 
				        		  }
				        	  ,2000);//单位毫秒   
				        	 
				          }else{
				        	  showTip(res.message);	
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
	<form:form id="inputForm" modelAttribute="memberCancellation" action="${ctx}/memberCancellation/checkCancellation" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>		
		<div class="control-group">
			<label style="text-align:right;width:150px">会员编号：</label>
			<form:input path="member.id" style="width:230px"  readonly="true" htmlEscape="false" maxlength="20" class="input-xlarge  digits"/>
			<span class="help-inline"  style="width:150px"></span>	
		</div>
		<div class="control-group">
			<label style="text-align:right;width:150px">会员姓名：</label>
			<form:input path="member.name" style="width:230px"  readonly="true" htmlEscape="false" maxlength="20" class="input-xlarge"/>
			<span class="help-inline"  style="width:150px"></span>	
		</div>
		<div class="control-group">
			<label style="text-align:right;width:150px">会员手机号：</label>
			<form:input path="member.username" style="width:230px"  readonly="true" htmlEscape="false" maxlength="20" class="input-xlarge  digits"/>
			<span class="help-inline"  style="width:150px"></span>	
		</div>
		<div class="control-group">
			<label style="text-align:right;width:150px">注销原因：</label>
			<form:textarea path="reason"  readonly="true" htmlEscape="false" rows="4" maxlength="255" class="input-xxlarge "/>
			<span class="help-inline"  style="width:150px"></span>	
		</div>
		<div class="control-group">
			<label style="text-align:right;width:150px">操作后状态：</label>
			<form:select path="status" style="width:245px" class="input-xlarge required">
			<form:option value="" label="请选择"/>
				<form:options items="${fns:getDictList('cancellationCheckStatus')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
			</form:select>
			<span class="help-inline"  style="width:150px"><font color="red">*</font> </span>
		</div>
		<div class="control-group">
			<label style="text-align:right;width:150px">审核备注：</label>
			<form:textarea path="rmk" htmlEscape="false" rows="4" maxlength="255" class="input-xxlarge required" placeholder="请填写审核通过或拒绝的原因" />
			<span class="help-inline"  style="width:50px"><font color="red">*</font> </span>	
		</div>
		<div class="form-actions">
			<shiro:hasPermission name="mem:memberCancellation:edit"><input id="btnSubmit" class="btn btn-primary" style="width:80px;" type="submit" value="保   存"/>&nbsp;</shiro:hasPermission>
			<label style="text-align:right;width:50px"></label>
			<input id="btnCancel" class="btn btn-primary" type="button" style="width:80px;" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>