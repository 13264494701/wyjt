<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>文章评论管理</title>
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
		<li class="active"><a href="${ctx}/cms/cmsArticleComment/">文章评论列表</a></li>
		<shiro:hasPermission name="cms:cmsArticleComment:edit"><li><a href="${ctx}/cms/cmsArticleComment/add">文章评论添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="cmsArticleComment" action="${ctx}/cms/cmsArticleComment/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label style="text-align:right;width:150px">会员编号：</label>
				<form:input path="memberNo" htmlEscape="false" maxlength="16" class="input-medium"/>
			</li>
			<li><label style="text-align:right;width:150px">评论类型：</label>
				<form:input path="type" htmlEscape="false" maxlength="11" class="input-medium"/>
			</li>
			<li class="btns"><label style="text-align:right;width:50px"></label><input id="btnSubmit" class="btn btn-primary " style="width:80px;" type="submit" value="查   询"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th style="text-align:center">文章编号</th>
				<th style="text-align:center">会员编号</th>
				<th style="text-align:center">评论类型</th>
				<th style="text-align:center">评论内容</th>
				<th style="text-align:center">评论IP</th>
				<th style="text-align:center">评论时间</th>
				<shiro:hasPermission name="cms:cmsArticleComment:edit"><th style="text-align:center">操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="cmsArticleComment">
			<tr>
				<td style="text-align:center"><a href="${ctx}/cms/cmsArticleComment/query?id=${cmsArticleComment.id}">
					${cmsArticleComment.articleId}
				</a></td>
				<td style="text-align:center">
					${cmsArticleComment.memberNo}
				</td>
				<td style="text-align:center">
					${cmsArticleComment.type}
				</td>
				<td style="text-align:center">
					${cmsArticleComment.content}
				</td>
				<td style="text-align:center">
					${cmsArticleComment.ip}
				</td>
				<td style="text-align:center">
					<fmt:formatDate value="${cmsArticleComment.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td  style="text-align:center">
				<shiro:hasPermission name="cms:cmsArticleComment:edit">
    				<a href="${ctx}/cms/cmsArticleComment/update?id=${cmsArticleComment.id}">修改</a>
					<a href="${ctx}/cms/cmsArticleComment/delete?id=${cmsArticleComment.id}" onclick="return confirmx('确认要删除该文章评论吗？', this.href)">删除</a>
				</shiro:hasPermission>
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>