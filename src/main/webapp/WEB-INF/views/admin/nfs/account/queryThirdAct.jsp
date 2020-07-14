<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>商户账户详情</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">

	</script>
</head>
<body style="text-align: center;">
	<ul class="nav nav-tabs">
		<li>商户账户详情</li>
	</ul><br/>
	<c:if test="${failed eq true}">
		<div class="control-group">
			<label style="text-align:right;width:250px">查询出错啦！请联系管理员处理！</label>
			<span class="help-inline"  style="width:150px"></span>	
		</div>
	</c:if>
	<c:if test="${failed ne true}">
		<form:form id="inputForm" modelAttribute="merchantAccountDetail"  method="post" class="form-horizontal">
		<sys:message content="${message}"/>		
		<div class="control-group">
			<label style="text-align:right;font-size:x-large; width:150px">富友账户</label>
			<span class="help-inline"  style="width:150px"></span>	
		</div>
		<c:if test="${fyfailed eq true}">
			<div class="control-group">
			<label style="text-align:right;width:250px">富友账户查询出错啦！请联系管理员处理！</label>
			<span class="help-inline"  style="width:150px"></span>	
		</div>
		</c:if>
		<c:if test="${fyfailed ne true}">
			<div class="control-group ">
			<label style="text-align:right;width:150px">账面余额(元)：</label>
			<form:input path="ctamt" value="${fns:decimalToStr(merchantAccountDetail.ctamt,2)}" style="width:250px"  readonly="true" htmlEscape="false" maxlength="64" class="input-xlarge "/>
			<span class="help-inline"  style="width:150px"></span>	
		</div>
		<div class="control-group">
			<label style="text-align:right;width:150px">可用余额(元)：</label>
			<form:input path="caamt" value="${fns:decimalToStr(merchantAccountDetail.caamt,2)}" style="width:250px"  readonly="true" htmlEscape="false" maxlength="64" class="input-xlarge  digits"/>
			<span class="help-inline"  style="width:150px"></span>	
		</div>
		<div class="control-group">
			<label style="text-align:right;width:150px">待结转余额(元)：</label>
			<form:input path="cuamt" value="${fns:decimalToStr(merchantAccountDetail.cuamt,2)}" style="width:250px"  readonly="true" htmlEscape="false" maxlength="64" class="input-xlarge "/>
			<span class="help-inline"  style="width:150px"></span>	
		</div>
		<div class="control-group">
			<label style="text-align:right;width:150px">冻结余额(元)：</label>
			<form:input path="cfamt" value="${fns:decimalToStr(merchantAccountDetail.cfamt,2)}" style="width:250px"  readonly="true" htmlEscape="false" maxlength="64" class="input-xlarge "/>
			<span class="help-inline"  style="width:150px"></span>	
		</div>
		</c:if>
		<div class="control-group">
			<label style="text-align:right;font-size:x-large; width:150px">富友代付账户</label>
			<span class="help-inline"  style="width:150px"></span>	
		</div>
		<c:if test="${fytxfailed eq true}">
			<div class="control-group">
			<label style="text-align:right;width:250px">富友代付账户查询出错啦！请联系管理员处理！</label>
			<span class="help-inline"  style="width:150px"></span>	
		</div>
		</c:if>
		<c:if test="${fytxfailed ne true}">
			<div class="control-group ">
			<label style="text-align:right;width:150px">账面余额(元)：</label>
			<form:input path="txctamt" value="${fns:decimalToStr(merchantAccountDetail.txctamt,2)}" style="width:250px"  readonly="true" htmlEscape="false" maxlength="64" class="input-xlarge "/>
			<span class="help-inline"  style="width:150px"></span>	
		</div>
		<div class="control-group">
			<label style="text-align:right;width:150px">可用余额(元)：</label>
			<form:input path="txcaamt" value="${fns:decimalToStr(merchantAccountDetail.txcaamt,2)}" style="width:250px"  readonly="true" htmlEscape="false" maxlength="64" class="input-xlarge  digits"/>
			<span class="help-inline"  style="width:150px"></span>	
		</div>
		<div class="control-group">
			<label style="text-align:right;width:150px">待结转余额(元)：</label>
			<form:input path="txcuamt" value="${fns:decimalToStr(merchantAccountDetail.txcuamt,2)}" style="width:250px"  readonly="true" htmlEscape="false" maxlength="64" class="input-xlarge "/>
			<span class="help-inline"  style="width:150px"></span>	
		</div>
		<div class="control-group">
			<label style="text-align:right;width:150px">冻结余额(元)：</label>
			<form:input path="txcfamt" value="${fns:decimalToStr(merchantAccountDetail.txcfamt,2)}" style="width:250px"  readonly="true" htmlEscape="false" maxlength="64" class="input-xlarge "/>
			<span class="help-inline"  style="width:150px"></span>	
		</div>
		</c:if>
		
		<label style="text-align:right;font-size:x-large; height:30px; width:150px"></label>
		<div class="control-group">
			<label style="text-align:right;font-size:x-large; height:30px; width:150px">连连账户</label>
			<span class="help-inline"  style="width:150px"></span>	
		</div>
		<c:if test="${llfailed eq true}">
			<div class="control-group">
			<label style="text-align:right;width:250px">连连账户查询出错啦！请联系管理员处理！</label>
			<span class="help-inline"  style="width:150px"></span>	
		</div>
		</c:if>
		<c:if test="${llfailed ne true}">
			<div class="control-group">
				<label style="text-align:right;width:150px">可用余额(元)：</label>
				<form:input path="lianlianBalance"  value="${fns:decimalToStr(merchantAccountDetail.lianlianBalance,2)}"  style="width:250px"  readonly="true" htmlEscape="false" maxlength="64" class="input-xlarge "/>
				<span class="help-inline"  style="width:150px"></span>	
			</div>
		</c:if>
	</form:form>
	</c:if>
</body>
</html>