<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>订单查询结果</title>
	<meta name="decorator" content="default"/>
	<%-- <script src="${ctxStatic}/jquery-form/jquery.form.js"></script> --%>
	<script type="text/javascript">
		$(document).ready(function() {
		});
		function closeDialog(){
			var d = parent.dialog.get('queryThirdOrder');	
       	  	setTimeout(function(){  
       		  d.close(); 
       		  }
       	  ,200);//单位毫秒   
		}
	</script>
	<style type="text/css">
		body{ text-align:center} 	
	</style>
</head>
<body>
		<br/>
		<sys:message content="${message}"/>		
		<form:form id="inputForm" modelAttribute="fuiouQueryPaymentResponseBean"  method="post" class="form-horizontal">
			<sys:message content="${message}"/>		
				<div class="control-group">
					<label style="text-align:right;width:150px">响应码：</label>
					<form:input path="ret" style="width:230px"  readonly="true" htmlEscape="false" maxlength="30" class="input-xlarge "/>
					<span class="help-inline"  style="width:150px"></span>	
				</div>
				<div class="control-group">
					<label style="text-align:right;width:150px">响应描述：</label>
					<form:input path="memo" style="width:230px"  readonly="true" htmlEscape="false" maxlength="30" class="input-xlarge  digits"/>
					<span class="help-inline"  style="width:150px"></span>	
				</div>
				<div class="control-group">
					<label style="text-align:right;width:150px">交易金额：</label>
					<form:input path="amt"  style="width:230px"  readonly="true" htmlEscape="false" class="input-xlarge "/>
					<span class="help-inline"  style="width:150px"></span>	
				</div>
				<div class="control-group">
					<label style="text-align:right;width:150px">交易状态：</label>
					<form:input path="state" value="${fns:getDictLabel(fuiouQueryPaymentResponseBean.state, 'fuiouWithdrawState', '')}" style="width:230px"  readonly="true" htmlEscape="false" class="input-xlarge "/>
					<span class="help-inline"  style="width:150px"></span>	
				</div>
				<div class="control-group">
					<label style="text-align:right;width:150px">交易状态类别：</label>
					<form:input path="transStatusDesc" style="width:230px"  readonly="true" htmlEscape="false" maxlength="64" class="input-xlarge "/>
					<span class="help-inline"  style="width:150px"></span>	
				</div>
				<div class="control-group">
					<label style="text-align:right;width:150px">是否退款：</label>
					<form:input path="tpst" value="${fns:getDictLabel(fuiouQueryPaymentResponseBean.tpst, 'fuiouTpst', '')}" style="width:230px"  readonly="true" htmlEscape="false" class="input-xlarge "/>
					<span class="help-inline"  style="width:150px"></span>	
				</div>
				<div class="control-group">
					<label style="text-align:right;width:150px">结果原因：</label>
					<form:input path="reason"  style="width:230px"  readonly="true" htmlEscape="false" class="input-xlarge "/>
					<span class="help-inline"  style="width:150px"></span>	
				</div>
				<div class="control-group">
					<label style="text-align:right;width:150px">银行响应码：</label>
					<form:input path="rspcd" style="width:230px"  readonly="true" htmlEscape="false" maxlength="64" class="input-xlarge "/>
					<span class="help-inline"  style="width:150px"></span>	
				</div>
		</form:form>
</body>
</html>