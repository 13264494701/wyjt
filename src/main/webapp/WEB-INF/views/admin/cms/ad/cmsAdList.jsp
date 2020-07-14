<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>广告管理</title>
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
		function turn(obj){
			var id = $(obj).closest("tr").attr("id");
			var sts = $(obj).attr("sts");
			$.ajax({
				type : 'POST',
				url : '${ctx}/ad/turn',
				data : {
					 id:id,
				     sts : sts
				},
				success : function(rsp) {
					if(rsp.status == false)
					{
					   top.$.jBox.tip(rsp.message);
					}else{
					   top.$.jBox.tip(rsp.message);
					   if(sts=='1'){
						   $("#isEnabled"+id).text("是");
						   $(obj).text("停用");
						   $(obj).attr("sts","0")

					   }else{
						   $("#isEnabled"+id).text("否");
						   $(obj).text("启用");
						   $(obj).attr("sts","1")
					   }						   
					}						
				}
			})

		}
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/ad">广告列表</a></li>
		<shiro:hasPermission name="cms:ad:edit"><li><a href="${ctx}/ad/add">广告添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="cmsAd" action="${ctx}/ad/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label style="text-align:right;width:150px">广告位置：</label>
				<form:select path="positionNo"  style="width:177px" class="input-medium">
					<form:option value="" label="请选择"/>
					<form:options items="${fns:getAdPositionList()}" itemLabel="positionName" itemValue="positionNo" htmlEscape="false"/>
				</form:select>
			</li>
			<li><label style="text-align:right;width:150px">广告标题：</label>
				<form:input path="title" htmlEscape="false" maxlength="64" class="input-medium"/>
			</li>
			<li class="btns"><label style="text-align:right;width:50px"></label><input id="btnSubmit" class="btn btn-primary " style="width:80px;" type="submit" value="查   询"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th style="text-align:center">广告位置</th>
				<th style="text-align:center">广告标题</th>
				<th style="text-align:center">广告类型</th>
				<th style="text-align:center">展示顺序</th>
				<th style="text-align:center">是否启用</th>
				<th style="text-align:center">跳转方式</th>
				<shiro:hasPermission name="ads:ad:edit"><th style="text-align:center">操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="ad">
			<tr id="${ad.id}">
				<td style="text-align:center"><a href="${ctx}/ad/query?id=${ad.id}">
					${fns:getPositionNameByPositionNo(ad.positionNo)}
				</a></td>
				<td style="text-align:center">
					${ad.title}
				</td>
				<td style="text-align:center">
					${fns:getDictLabel(ad.type, 'adType', '')}
				</td>
			   <td style="text-align:center">
					${ad.sort}
				</td>
				<td style="text-align:center">
					<span id="isEnabled${ad.id}">${fns:getDictLabel(ad.isEnabled, 'trueOrFalse', '')}</span>
				</td>
				<td style="text-align:center">
					${fns:getDictLabel(ad.redirectType, 'redirectType', '')}
				</td>
				<td  style="text-align:center">
				<shiro:hasPermission name="cms:ad:edit">
					<c:choose>
						<c:when test="${ad.isEnabled == false}">
							<a href ="javascript:void(0);" sts="1" onclick="turn(this)">启用</a>
						</c:when>
						<c:when test="${ad.isEnabled == true}">
							<a href ="javascript:void(0);" sts="0" onclick="turn(this)">停用</a>
						</c:when>
					</c:choose>	
    				<a href="${ctx}/ad/update?id=${ad.id}">修改</a>
					<a href="${ctx}/ad/delete?id=${ad.id}" onclick="return confirmx('确认要删除该广告吗？', this.href)">删除</a>
				</shiro:hasPermission>
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>