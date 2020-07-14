<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>评论回复管理</title>
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
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/cms/cmsArticleCommentReply/">评论回复列表</a></li>
		<shiro:hasPermission name="cms:cmsArticleCommentReply:edit"><li><a href="${ctx}/cms/cmsArticleCommentReply/add">评论回复添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="cmsArticleCommentReply" action="${ctx}/cms/cmsArticleCommentReply/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label style="text-align:right;width:150px">评论编号：</label>
				<form:input path="commentId" htmlEscape="false" maxlength="64" class="input-medium"/>
			</li>
			<li><label style="text-align:right;width:150px">发送会员：</label>
				<form:input path="fromMember" htmlEscape="false" maxlength="16" class="input-medium"/>
			</li>
			<li><label style="text-align:right;width:150px">接收会员：</label>
				<form:input path="toMember" htmlEscape="false" maxlength="16" class="input-medium"/>
			</li>
			<li><label style="text-align:right;width:150px">评论类型：</label>
				<form:select path="type"  style="width:177px" class="input-medium">
					<form:option value="" label="请选择"/>
					<form:options items="${fns:getDictList('')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</li>
			<li class="btns"><label style="text-align:right;width:50px"></label><input id="btnSubmit" class="btn btn-primary " style="width:80px;" type="submit" value="查   询"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th style="text-align:center">评论编号</th>
				<th style="text-align:center">发送会员</th>
				<th style="text-align:center">接收会员</th>
				<th style="text-align:center">评论类型</th>
				<shiro:hasPermission name="cms:cmsArticleCommentReply:edit"><th style="text-align:center">操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="cmsArticleCommentReply">
			<tr>
				<td style="text-align:center"><a href="${ctx}/cms/cmsArticleCommentReply/query?id=${cmsArticleCommentReply.id}">
					${cmsArticleCommentReply.commentId}
				</a></td>
				<td style="text-align:center">
					${cmsArticleCommentReply.fromMember}
				</td>
				<td style="text-align:center">
					${cmsArticleCommentReply.toMember}
				</td>
				<td style="text-align:center">
					${fns:getDictLabel(cmsArticleCommentReply.type, '', '')}
				</td>
				<td  style="text-align:center">
				<shiro:hasPermission name="cms:cmsArticleCommentReply:edit">
    				<a href="${ctx}/cms/cmsArticleCommentReply/update?id=${cmsArticleCommentReply.id}">修改</a>
					<a href="${ctx}/cms/cmsArticleCommentReply/delete?id=${cmsArticleCommentReply.id}" onclick="return confirmx('确认要删除该评论回复吗？', this.href)">删除</a>
				</shiro:hasPermission>
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>