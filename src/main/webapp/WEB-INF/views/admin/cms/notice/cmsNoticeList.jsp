<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>通知管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			$('.pub').click(function() {
				var id = $(this).data('id');
				var sts = $(this).data('sts');

				var pubId = "pub"+id;
				$.ajax({
					type : 'POST',
					url : '${ctx}/cmsNotice/pub',
					data : {
						 id:id,
					     sts : sts
					},
					success : function(rsp) {
						if(rsp.status == false)
						{
						   top.$.jBox.tip(rsp.content);
						}else{
						   top.$.jBox.tip(rsp.content);
						   if(sts=='1'){
							   $("#isPub"+id).text("是")
							  
							   document.getElementById(pubId).innerText="撤回";
							   $("#pub"+id).data("sts","0");
	
						   }else{
							   $("#isPub"+id).text("否")
				
							   document.getElementById(pubId).innerText="发布";
							   $("#pub"+id).data("sts","1");
						   }
						   
						}
						
					}
				})
			});
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
		<li class="active"><a href="${ctx}/cmsNotice/">通知列表</a></li>
		<shiro:hasPermission name="notice:cmsNotice:edit"><li><a href="${ctx}/cmsNotice/add">通知添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="cmsNotice" action="${ctx}/cmsNotice/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
		    <li><label style="text-align:right;width:150px">展示位置：</label>
				<form:select path="position"  style="width:177px" class="input-medium">
					<form:option value="" label="请选择"/>
					<form:options items="${fns:getDictList('noticePosition')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</li>
			<li><label style="text-align:right;width:150px">通知标题：</label>
				<form:input path="title" htmlEscape="false" maxlength="127" class="input-medium"/>
			</li>
			<li class="btns"><label style="text-align:right;width:50px"></label><input id="btnSubmit" class="btn btn-primary " style="width:80px;" type="submit" value="查   询"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
			    <th style="text-align:center">展示位置</th>
				<th style="text-align:center">通知标题</th>
				<th style="text-align:center">展示顺序</th>
				<th style="text-align:center">是否发布</th>
				<th style="text-align:center">生成时间</th>
				<shiro:hasPermission name="notice:cmsNotice:edit"><th style="text-align:center">操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="cmsNotice">
			<tr>
			    <td style="text-align:center">
					${fns:getDictLabel(cmsNotice.position, 'noticePosition', '')}
				</td>
				<td style="text-align:center"><a href="${ctx}/cmsNotice/query?id=${cmsNotice.id}">
					${cmsNotice.title}
				</a></td>
				<td style="text-align:center">
					${cmsNotice.sort}
				</td>
				<td style="text-align:center">
					<span id="isPub${cmsNotice.id}">${fns:getDictLabel(cmsNotice.isPub, 'trueOrFalse', '')}</span>
				</td>
				<td style="text-align:center">
					<fmt:formatDate value="${cmsNotice.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td  style="text-align:center">
				<shiro:hasPermission name="notice:cmsNotice:edit">
				    <c:choose>
						<c:when test="${cmsNotice.isPub == 'false'}">
						
						   <a href="javascript:;" id = "pub${cmsNotice.id}" class="pub" data-id="${cmsNotice.id}" data-sts="1">发布</a>
						   
						</c:when>
						<c:when test="${cmsNotice.isPub == 'true'}">
						   <a href="javascript:;" id = "pub${cmsNotice.id}" class="pub" data-id="${cmsNotice.id}" data-sts="0">撤回</a>
						</c:when>
					</c:choose>		
    				<a href="${ctx}/cmsNotice/update?id=${cmsNotice.id}">修改</a>
					<a href="${ctx}/cmsNotice/delete?id=${cmsNotice.id}" onclick="return confirmx('确认要删除该通知吗？', this.href)">删除</a>
				</shiro:hasPermission>
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>