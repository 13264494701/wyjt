<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>数据查询</title>
<!-- 	<meta name="decorator" content="default"/> -->
    <script src="${ctxStatic}/jquery/jquery-1.8.3.min.js" type="text/javascript"></script>
	<script type="text/javascript">
		$(document).ready(function() {

		});
	</script>
</head>
<body>

<form id="searchForm"  action="${ctx}/sql/olddb" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label style="text-align:right;width:150px;">查询SQL：</label>
               <textarea style="margin: 0px; width: 1000px; height: 125px;" name="sqlStr" >${sqlStr}</textarea>
			</li>
			<li class="btns"><label style="text-align:right;width:50px"></label><input id="btnSubmit" class="btn btn-primary " style="width:80px;" type="submit" value="查   询"/></li>
			<li class="clearfix"></li>
		</ul>
</form>
<sys:message content="${message}"/>
	
<table width="95%" border="1" cellpadding="0" cellspacing="0">
	<thead>
		<tr>
			<c:forEach items="${keyList}" var="object">
				<th style="text-align:center">${object}</th>
			</c:forEach>
		</tr>
	</thead>
	<tbody>
		<c:forEach items="${userList}" var="object">
			<tr>
				<c:forEach items="${object}" var="var">
					<td style="text-align:center">
								${var}
					</td>
				</c:forEach>
			</tr>
		</c:forEach>
	</tbody>
</table>

</body>
</html>
