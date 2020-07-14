<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			
		});
		function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#searchForm").submit();
        	return false;
        }
	</script>
</head>
<body>
<h1> 运营商查询记录   </h1>
	<form:form id="searchForm" modelAttribute="rcSjmh" action="${ufang}/rcSjmh/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label style="text-align:right;width:150px">手机号码：</label>
				<form:input path="phoneNo" htmlEscape="false" maxlength="11" class="input-medium"/>
			</li>
			<li><label style="text-align:right;width:150px">身份证号：</label>
				<form:input path="idNo" htmlEscape="false" maxlength="18" class="input-medium"/>
			</li>
<%--			<li><label style="text-align:right;width:150px">渠道类型：</label>
				<form:input path="channelType" htmlEscape="false" maxlength="20" class="input-medium"/>
			</li>--%>
			<li class="btns"><label style="text-align:right;width:50px"></label><input id="btnSubmit" class="btn btn-primary " style="width:80px;" type="submit" value="查   询"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th style="text-align:center">手机号码</th>
				<th style="text-align:center">身份证号</th>
				<th style="text-align:center">用户名</th>
				<th style="text-align:center">真实姓名</th>
<%--				<th style="text-align:center">渠道类型</th>--%>
				<th style="text-align:center">运营商</th>
				<th style="text-align:center">归属地</th>
<%--				<th style="text-align:center">缺失数据</th>--%>
				<th style="text-align:center">消息</th>
				<th style="text-align:center">查询时间</th>
				<%--<th style="text-align:center">任务数据</th>--%>
				<shiro:hasPermission name="rcSjmh:edit"><th style="text-align:center">操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="rcSjmh">
			<tr>
				<td style="text-align:center">
					${rcSjmh.phoneNo}
				</td>
				<td style="text-align:center">
					${rcSjmh.idNo}
				</td>
				<td style="text-align:center">
					${rcSjmh.userName}
				</td>
				<td style="text-align:center">
					${rcSjmh.realName}
				</td>
<%--				<td style="text-align:center">
					${rcSjmh.channelType}
				</td>--%>
				<td style="text-align:center">
					${rcSjmh.channelSrc}
				</td>
				<td style="text-align:center">
					${rcSjmh.channelAttr}
				</td>
<%--				<td style="text-align:center">
					${rcSjmh.lostData}
				</td>--%>
				<td style="text-align:center">
					${rcSjmh.rmk}
				</td>
				<%--<td style="text-align:center">--%>
					<%--${rcSjmh.taskData}--%>
				<%--</td>--%>
				<td style="text-align:center">
					<fmt:formatDate value="${rcSjmh.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td  style="text-align:center">
				<%--<shiro:hasPermission name="rcSjmh:edit">--%>
					<%--<a href="${ufang}/rcSjmh/delete?id=${rcSjmh.id}" onclick="return confirmx('确认要删除该风控 数据魔盒吗？', this.href)">删除</a>--%>
				<%--</shiro:hasPermission>--%>
                    <a href="${ufang}/rcSjmh/report?taskId=${rcSjmh.taskId}" target="_blank">查看报告</a>
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>