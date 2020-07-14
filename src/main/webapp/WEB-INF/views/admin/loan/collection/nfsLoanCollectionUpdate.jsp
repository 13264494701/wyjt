<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>借条催收管理</title>
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
				        	  var d = parent.dialog.get('loanCollectionUpdate');	
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
		<shiro:hasPermission name="loan:nfsLoanCollection:edit">
			<li class="active"><a href="${ctx}/nfsLoanCollection/update?id=${nfsLoanCollection.id}">借条催收修改</a></li>
		</shiro:hasPermission>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="nfsLoanCollection" action="${ctx}/nfsLoanCollection/editCollection" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>	
		<div class="control-group">
			<label style="text-align:right;width:150px">借条编号：</label>
			<form:input path="loan.id" style="width:230px" readOnly="true" htmlEscape="false" maxlength="20" class="input-xlarge required digits"/>
			<span class="help-inline"  style="width:150px"></span>
		</div>
		<div class="control-group">
			<label style="text-align:right;width:150px">催收费用：</label>
			<form:input path="fee" value="${fns:decimalToStr(nfsLoanCollection.fee,2)}" style="width:230px" readOnly="true" htmlEscape="false" class="input-xlarge "/>
			<span class="help-inline"  style="width:150px"></span>	
		</div>	
		<div class="control-group">
			<label style="text-align:right;width:150px">催收状态：</label>	
			<form:select path="status" style="width:245px" class="input-xlarge required">
			<form:option value="" label="请选择"/>
				<form:options items="${fns:getDictList('collectionStatus')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
			</form:select>
			<span class="help-inline"  style="width:150px"><font color="red">*</font> </span>					
		</div>
		<div class="control-group">
			<label style="text-align:right;width:150px">备注：</label>
			<form:textarea path="rmk" htmlEscape="false" rows="8" maxlength="1024" class="input-xxlarge"/>	
			<span class="help-inline"  style="width:150px"></span>	
		</div>
		<div class="form-actions">
			<shiro:hasPermission name="loan:nfsLoanCollection:edit"><input id="btnSubmit" class="btn btn-primary" style="width:80px;" type="submit" value="保   存"/>&nbsp;</shiro:hasPermission>
			<label style="text-align:right;width:50px"></label>
			<!-- <input id="btnCancel" class="btn btn-primary" type="button" style="width:80px;" value="返 回" onclick="history.go(-1)"/> -->
		</div>
	</form:form>
</body>
</html>