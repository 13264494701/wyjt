<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>充值记录管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
	function downLoadReceipt(){  
		var rchgId = "${nfsRchgRecord.id}";	
		$.ajax({
			type : 'POST',
			url : '${admin}/rchgRecord/downLoadReceipt',
			data : {
				rchgId:rchgId,
			},
			success : function(rsp) {
				console.log(rsp);
				if(rsp.status){
					var FM = rsp.result;
					location.href = 'https://mpay.fuiou.com:16128/checkInfo/createReceipt.pay?FM='+FM;
				}else{
					$.jBox.alert(rsp.message,"提醒");
				}

			}
		});
	}
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${admin}/rchgRecord/">充值记录列表</a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="nfsRchgRecord"  method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>		
		<div class="control-group">
			<label style="text-align:right;width:150px">会员编号：</label>
			<form:input path="member.id" style="width:230px"  readonly="true" htmlEscape="false" maxlength="20" class="input-xlarge  digits"/>
			<span class="help-inline"  style="width:150px"></span>	
		</div>
		<div class="control-group">
			<label style="text-align:right;width:150px">会员姓名：</label>
			<form:input path="member.name" style="width:230px"  readonly="true" htmlEscape="false" maxlength="64" class="input-xlarge "/>
			<span class="help-inline"  style="width:150px"></span>	
		</div>
		<div class="control-group">
			<label style="text-align:right;width:150px">充值金额：</label>
			<form:input path="amount" value="${fns:decimalToStr(nfsRchgRecord.amount,2)}" style="width:230px"  readonly="true" htmlEscape="false" class="input-xlarge "/>
			<span class="help-inline"  style="width:150px"></span>	
		</div>
		<div class="control-group">
			<label style="text-align:right;width:150px">银行卡号：</label>
			<form:input path="card.cardNo" style="width:230px"  readonly="true" htmlEscape="false" class="input-xlarge "/>
			<span class="help-inline"  style="width:150px"></span>	
		</div>
		<div class="control-group">
			<label style="text-align:right;width:150px">银行名称：</label>
			<form:input path="bankName" style="width:230px"  readonly="true" htmlEscape="false" class="input-xlarge "/>
			<span class="help-inline"  style="width:150px"></span>	
		</div>
		<div class="control-group">
			<label style="text-align:right;width:150px">充值方式：</label>
			<form:select path="type" style="width:245px"  disabled="true"  class="input-xlarge ">
			<form:option value="" label="请选择"/>
				<form:options items="${fns:getDictList('rechargeType')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
			</form:select>
			<span class="help-inline"  style="width:150px"></span>	
		</div>
		<div class="control-group">
			<label style="text-align:right;width:150px">充值渠道：</label>
			<form:select path="channel" style="width:245px"  disabled="true"  class="input-xlarge ">
			<form:option value="" label="请选择"/>
				<form:options items="${fns:getDictList('rechargeChannel')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
			</form:select>
			<span class="help-inline"  style="width:150px"></span>	
		</div>
		<div class="control-group">
			<label style="text-align:right;width:150px">交易状态：</label>
			<form:select path="status" style="width:245px"  disabled="true"  class="input-xlarge ">
			<form:option value="" label="请选择"/>
				<form:options items="${fns:getDictList('rechargeStatus')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
			</form:select>
			<span class="help-inline"  style="width:150px"></span>	
		</div>
		<div class="control-group">
			<label style="text-align:right;width:150px">支付单号：</label>
			<form:input path="payment.paymentNo" style="width:230px"  readonly="true" htmlEscape="false" maxlength="64" class="input-xlarge "/>
			<span class="help-inline"  style="width:150px"></span>	
		</div>
		<div class="control-group">
			<label style="text-align:right;width:150px">第三方交易平台：</label>
			<form:input path="payment.paymentPluginName" style="width:230px"  readonly="true" htmlEscape="false" maxlength="64" class="input-xlarge "/>
			<span class="help-inline"  style="width:150px"></span>	
		</div>
		<div class="control-group">
			<label style="text-align:right;width:150px">第三方交易单号：</label>
			<form:input path="payment.thirdPaymentNo" style="width:230px"  readonly="true" htmlEscape="false" maxlength="64" class="input-xlarge "/>
			<span class="help-inline"  style="width:150px"></span>	
		</div>
		<div class="form-actions">
		    <label style="text-align:right;width:50px"></label>
			<a class="btn btn-primary"  style="width:80px;" href ="javascript:void(0);" onclick="downLoadReceipt()">充值凭证</a>
			<label style="text-align:right;width:50px"></label>
			<input id="btnCancel" class="btn btn-primary" type="button" style="width:80px;" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>