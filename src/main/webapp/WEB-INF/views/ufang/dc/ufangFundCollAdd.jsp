<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>资金分发归集管理</title>
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
					$.ajax({
						url:$(form).attr("action"),
						type:$(form).attr("method"),
						data:$(form).serialize(), 
						success:function(res){
				        	  showTip(res.content);				        	
				        	  var d = parent.dialog.get('collectFund');
				        	  setTimeout(function(){  
				        		  d.close(res.type); 
				        		  }
				        	  ,2000);//单位毫秒
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
	<form:form id="inputForm" modelAttribute="ufangFundDistColl" action="${ufang}/ufangFundDistColl/collectFund" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<form:hidden path="userAct.user.id"/>
		<sys:message content="${message}"/>		
		<div class="control-group">
			<label style="text-align:right;width:150px">员工账户：</label>
				<form:input path="userAct.user.empNam" value="${ufangFundDistColl.userAct.user.empNam}" style="width:204px" readonly="true" htmlEscape="false" class="input-xlarge required"/>
		</div>
		<div class="control-group">
			<label style="text-align:right;width:150px">账户余额：</label>
			<div class="input-append">
			   <form:input path="userAct.curBal" value="${fns:decimalToStr(ufangFundDistColl.userAct.curBal,2) }" style="width:204px" readonly="true" htmlEscape="false" class="input-xlarge required"/>
			   <span class="add-on">元</span>
			</div>
			<span class="help-inline"  style="width:150px"></span>
		</div>

		<div class="control-group">
			<label style="text-align:right;width:150px">交易类型：</label>
			<form:select path="type" style="width:245px" disabled="true" class="input-xlarge required">
				<form:options items="${fns:getDictList('distCollType')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
			</form:select>
			<span class="help-inline"  style="width:150px"> </span>
		</div>
		<div class="control-group">
			<label style="text-align:right;width:150px">回收金额：</label>
			<div class="input-append">
			   <input name="amount" style="width:214px;height:24px;" htmlEscape="false" class="input-xlarge required"/>
			   <span class="add-on">元</span>
			</div>
			<font color="red">*</font>
		</div>

		<div class="form-actions">
			<shiro:hasPermission name="ufang:fundDistColl:edit"><input id="btnSubmit" class="btn btn-primary" style="width:80px;" type="submit" value="保   存"/>&nbsp;</shiro:hasPermission>
			<!-- <label style="text-align:right;width:50px"></label>
			<input id="btnCancel" class="btn btn-primary" type="button" style="width:80px;" value="返 回" onclick="history.go(-1)"/> -->
		</div>
	</form:form>
</body>
</html>