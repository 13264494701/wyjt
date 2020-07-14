<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>协议管理1</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			
			$('.static').click(function() {
				var id = $(this).data('id');
				var staticId = "static"+id;
				$.ajax({
					type : 'POST',
					url : '${ctx}/cms/contTmpl/genstatic',
					data : {
						 id:id,
					},
					success : function(rsp) {
						   top.$.jBox.tip(rsp.message);
							   $("#isStatic"+id).text("是")
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
		<li class="active"><a href="${ctx}/cms/contTmpl/">协议列表</a></li>
		<shiro:hasPermission name="cont:cmsContTmpl:edit"><li><a href="${ctx}/cms/contTmpl/add">协议添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="cmsContTmpl" action="${ctx}/cms/contTmpl/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label style="text-align:right;width:150px">协议类型：</label>
				<form:select path="type"  style="width:177px" class="input-medium">
					<form:option value="" label="请选择"/>
					<form:options items="${fns:getDictList('contType')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</li>
			<li><label style="text-align:right;width:150px">协议状态：</label>
				<form:select path="status"  style="width:177px" class="input-medium">
					<form:option value="" label="请选择"/>
					<form:options items="${fns:getDictList('contSts')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
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
				<th style="text-align:center">协议类型</th>
				<th style="text-align:center">协议状态</th>
				<th style="text-align:center">生效时间</th>
				<th style="text-align:center">失效时间</th>
				<th style="text-align:center">是否静态</th>
				<shiro:hasPermission name="cont:cmsContTmpl:edit"><th style="text-align:center">操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="cmsContTmpl">
			<tr>
				<td style="text-align:center"><a href="${ctx}/cms/contTmpl/query?id=${cmsContTmpl.id}">
					${fns:getDictLabel(cmsContTmpl.type, 'contType', '')}
				</a></td>
				<td style="text-align:center">
					${fns:getDictLabel(cmsContTmpl.status, 'contSts', '')}
				</td>
				<td style="text-align:center">
					<fmt:formatDate value="${cmsContTmpl.validTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td style="text-align:center">
					<fmt:formatDate value="${cmsContTmpl.invalidTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td style="text-align:center">
					<span id="isStatic${cmsContTmpl.id}">${fns:getDictLabel(cmsContTmpl.isStatic, 'trueOrFalse', '')}</span>
				</td>
				<td  style="text-align:center">
				<shiro:hasPermission name="cont:cmsContTmpl:edit">
						   <a href="javascript:;" id = "static${cmsContTmpl.id}" class="static" data-id="${cmsContTmpl.id}">静态化</a>
    				<%-- <a href="${ctx}/cms/contTmpl/genstatic?id=${cmsContTmpl.id}">设置成静态</a> --%>
    				<a href="${ctx}/cms/contTmpl/update?id=${cmsContTmpl.id}">修改</a>
				</shiro:hasPermission>
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>