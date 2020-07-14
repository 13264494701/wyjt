<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>好友通讯录管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			
		});
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active">催收通讯录查询</li>
	</ul>
	<form:form id="searchForm" modelAttribute="memberPhonebook" action="${ctx}/memberPhonebook/booklist" method="post" class="breadcrumb form-search">
		<ul class="ul-form">
			<li><label style="text-align:right;width:150px">手机号：</label>
				<form:input path="member.username" htmlEscape="false" class="input-medium"/>
			</li>
			<li class="btns"><label style="text-align:right;width:50px"></label><input id="btnSubmit" class="btn btn-primary " style="width:80px;" type="submit" value="查   询"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th style="text-align:center;width:1000px">通讯录手机号</th>	
			</tr>
		</thead>
		<tbody>
			<c:if test="${empty resList}">
			   	<tr>
					<td style="text-align:center;">
						${note}
					</td>
				</tr>
			</c:if>
			<c:if test="${not empty resList}">
				<c:forEach items="${resList}" var="phonebook">
					<tr>
						<td style="text-align:center;">
							${phonebook}
						</td>
					</tr>
				</c:forEach>
			</c:if>
		</tbody>
	</table>

</body>
</html>