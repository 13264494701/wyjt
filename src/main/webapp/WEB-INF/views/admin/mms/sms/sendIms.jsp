<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>发送短信管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			
		});
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active">发送站内信</li>
	</ul>
	<form id="sendMessage" action="${ctx}/mem/messageUserRecord/sendIms" method="post" class="breadcrumb form-search">
			<li><label>用户手机号：</label>
				<textarea name="username" htmlEscape="false" maxlength="12288" class="input-medium" style="width:200px"></textarea>
			</li><br/><br/>
			<li><label>站内信标题：</label>
				<textarea name="title" htmlEscape="false" maxlength="1024" class="input-medium" style="width:200px"></textarea>
			</li><br/><br/>
			<li><label>站内信内容：</label> 
				<textarea name="content" htmlEscape="false" maxlength="1024" class="input-medium" style="width:1000px"></textarea>
			</li>
			<li class="btns">
				<label style="text-align:right;width:50px">
				</label><input id="btnSubmit" class="btn btn-primary " style="width:80px;" type="submit" value="保存"/></li>
			<li class="clearfix"></li>
	</form>
	<sys:message content="${message}"/>
</body>
</html>