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
		<form:form id="inputForm" modelAttribute="queryPaymentResponseBean"  method="post" class="form-horizontal">
			<sys:message content="${message}"/>		
			<c:if test="${noRecord == '0'}">
				<div class="control-group">
				<label style="text-align:right;width:150px">商户付款流水号：</label>
				<form:input path="no_order" style="width:230px"  readonly="true" htmlEscape="false" maxlength="30" class="input-xlarge "/>
				<span class="help-inline"  style="width:150px"></span>	
				</div>
				<div class="control-group">
					<label style="text-align:right;width:150px">商户下单时间：</label>
					<form:input path="dt_order" style="width:230px"  readonly="true" htmlEscape="false" maxlength="30" class="input-xlarge  digits"/>
					<span class="help-inline"  style="width:150px"></span>	
				</div>
				<div class="control-group">
					<label style="text-align:right;width:150px">付款金额：</label>
					<form:input path="money_order" style="width:230px"  readonly="true" htmlEscape="false" maxlength="64" class="input-xlarge "/>
					<span class="help-inline"  style="width:150px"></span>	
				</div>
				<div class="control-group">
					<label style="text-align:right;width:150px">付款状态：</label>
					<form:input path="result_pay" value="${fns:getDictLabel(queryPaymentResponseBean.result_pay, 'lianlianPaymentStatus', '')}" style="width:230px"  readonly="true" htmlEscape="false" maxlength="20" class="input-xlarge "/>
					<span class="help-inline"  style="width:150px"></span>	
				</div>
				<div class="control-group">
					<label style="text-align:right;width:150px">连连银通付款流水号：</label>
					<form:input path="oid_paybill" style="width:230px"  readonly="true" htmlEscape="false" class="input-xlarge "/>
					<span class="help-inline"  style="width:150px"></span>	
				</div>
				<div class="control-group">
					<label style="text-align:right;width:150px">订单描述：</label>
					<form:input path="info_order"  style="width:230px"  readonly="true" htmlEscape="false" class="input-xlarge "/>
					<span class="help-inline"  style="width:150px"></span>	
				</div>
				<div class="control-group">
					<label style="text-align:right;width:150px">清算日期：</label>
					<input name="settle_date" type="text" style="width:230px"  disabled="disabled" maxlength="20" class="input-medium Wdate "
						value="<fmt:formatDate value="${settle_date}" pattern="yyyy-MM-dd"/>"
						onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:true});"/>
					<span class="help-inline"  style="width:150px"></span>	
					<span class="help-inline"  style="width:150px"></span>	
				</div>
				<div class="control-group">
					<label style="text-align:right;width:150px">支付备注：</label>
					<form:input path="memo" style="width:230px"  readonly="true" htmlEscape="false" maxlength="64" class="input-xlarge "/>
					<span class="help-inline"  style="width:150px"></span>	
				</div>
			</c:if>
			<c:if test="${noRecord == '1'}">
				<div class="control-group">
					<span style="font: 300;"> 没有记录 </span>
				</div>
			</c:if>
		</form:form>
</body>
</html>