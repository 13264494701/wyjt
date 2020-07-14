<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>会员管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">

	</script>
</head>
<body>
	<ul class="nav nav-tabs">
	</ul><br/>
	<form:form id="inputForm" modelAttribute="member"  method="post" class="form-horizontal">
		<sys:message content="${message}"/>
		<div class="control-group">
			<label style="text-align:right;width:150px">系统类型：</label>${osType}		
			<span class="help-inline"  style="width:150px"></span>	
		</div>
		<div class="control-group">	
			<label style="text-align:right;width:150px">系统版本：</label>${osVersion}
			<span class="help-inline"  style="width:150px"></span>	
		</div>
		<div class="control-group">
			<label style="text-align:right;width:150px">应用版本：</label>${appVersion}
			<span class="help-inline"  style="width:150px"></span>	
		</div>
		<div class="control-group">
			<label style="text-align:right;width:150px">ak：</label>${ak}		
			<span class="help-inline"  style="width:150px"></span>	
		</div>
		<div class="control-group">	
			<label style="text-align:right;width:150px">设备型号：</label>${deviceModel}
			<span class="help-inline"  style="width:150px"></span>	
		</div>
		<div class="control-group">
			<label style="text-align:right;width:150px">设备号：</label>${deviceToken}
			<span class="help-inline"  style="width:150px"></span>	
		</div>

		<div class="control-group">
			<label style="text-align:right;width:150px">渠道号：</label>${channeId}		
			<span class="help-inline"  style="width:150px"></span>	
		</div>
		<div class="control-group">	
			<label style="text-align:right;width:150px">推送码：</label>${pushToken}
			<span class="help-inline"  style="width:150px"></span>	
		</div>
		<div class="control-group">
			<label style="text-align:right;width:150px">登录IP：</label>${loginIp}
			<span class="help-inline"  style="width:150px"></span>	
		</div>
		<div class="control-group">
			<label style="text-align:right;width:150px">登录时间：</label>${loginTime}
			<span class="help-inline"  style="width:150px"></span>	
		</div>
	</form:form>
</body>
</html>