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

<form id="searchForm"  action="${ctx}/sql/newdbupdate" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label style="text-align:right;width:150px;">执行SQL：</label>
               <textarea style="margin: 0px; width: 1000px; height: 125px;" name="sqlStr" >${sqlStr}</textarea>
			</li>
			<li class="btns"><label style="text-align:right;width:50px"></label><input id="btnSubmit" class="btn btn-primary " style="width:80px;" type="submit" value="执  行"/></li>
			<li class="clearfix"></li>
		</ul>
</form>
<sys:message content="${message}"/>
	
</body>
</html>
