<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>文章管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			$('.pub').click(function() {
				var id = $(this).data('id');
				var sts = $(this).data('sts');
				var trId = "tr"+id;
				var pubId = "pub"+id;
				$.ajax({
					type : 'POST',
					url : '${ctx}/cms/cmsArticle/pub',
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
							   document.getElementById(trId).cells[7].innerText="是";  
							   document.getElementById(trId).cells[8].innerText= fomateDate(new Date(), 'YYYY-MM-DD hh:mm:ss');
							   document.getElementById(pubId).innerText="撤回";
							   $("#pub"+id).data("sts","0");
	
						   }else{
							   document.getElementById(trId).cells[7].innerText="否";
							   document.getElementById(trId).cells[8].innerText="";
							   document.getElementById(pubId).innerText="发布";
							   $("#pub"+id).data("sts","1");
						   }
						   
						}
						
					}
				})
			});
			$('.top').click(function() {
				var id = $(this).data('id');
				var sts = $(this).data('sts');
				var trId = "tr"+id;
				var topId = "top"+id;
				$.ajax({
					type : 'POST',
					url : '${ctx}/cms/cmsArticle/top',
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
							   document.getElementById(trId).cells[6].innerText="是";  							   
							   document.getElementById(topId).innerText="取消置顶";
							   $("#top"+id).data("sts","0");
	
						   }else{
							   document.getElementById(trId).cells[6].innerText="否";						  
							   document.getElementById(topId).innerText="置顶";
							   $("#top"+id).data("sts","1");
						   }
						   
						}
						
					}
				})
			});
			$('.genstatic').click(function() {
				var id = $(this).data('id');
				var trId = "tr"+id;
				$.ajax({
					type : 'POST',
					url : '${ctx}/cms/cmsArticle/genstatic',
					data : {
						 id:id
					},
					success : function(rsp) {
						 document.getElementById(trId).cells[5].innerText="是"; 
						 top.$.jBox.tip(rsp.message);					
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
		function fomateDate(oDate, sFomate, bZone)
		{
			sFomate = sFomate.replace("YYYY", oDate.getFullYear());
			sFomate = sFomate.replace("YY", String(oDate.getFullYear()).substr(2))
			sFomate = sFomate.replace("MM", oDate.getMonth()+1)
			sFomate = sFomate.replace("DD", oDate.getDate()<10?'0'+oDate.getDate():oDate.getDate());
			sFomate = sFomate.replace("hh", oDate.getHours());
			sFomate = sFomate.replace("mm", oDate.getMinutes()<10?'0'+oDate.getMinutes():oDate.getMinutes());
			sFomate = sFomate.replace("ss", oDate.getSeconds()<10?'0'+oDate.getSeconds():oDate.getSeconds());
			if (bZone) sFomate = sFomate.replace(/\b(\d)\b/g, '0$1');
			return sFomate;
		}
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/cms/cmsArticle/">文章列表</a></li>
		<shiro:hasPermission name="cms:cmsArticle:edit"><li><a href="${ctx}/cms/cmsArticle/add">文章添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="cmsArticle" action="${ctx}/cms/cmsArticle/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label style="text-align:right;width:100px">频道：</label>
			   <form:select path="channel.id"  style="width:177px" class="input-medium">
				   <form:option value="" label="请选择"/>
				   <form:options items="${fns:getChannelList()}" itemLabel="name" itemValue="id" htmlEscape="false"/>
			   </form:select>
		    </li>
			<li><label style="text-align:right;width:100px">标题：</label>
				<form:input path="title" htmlEscape="false" maxlength="255" style="width:430px" class="input-medium"/>
			</li>

		    <li><label style="text-align:right;width:100px">分类：</label>
		        <sys:treeselect id="category" name="category.id" value="${cmsArticle.category.id}" labelName="category.name" labelValue="${cmsArticle.category.name}"
					title="分类" url="/cms/category/treeData" notAllowSelectRoot="false" cssClass="input-small"/>
			</li>
			<li><label style="text-align:right;width:100px">是否静态：</label>
				<form:radiobuttons path="isStatic" items="${fns:getDictList('trueOrFalse')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
			</li>
			<li><label style="text-align:right;width:100px">是否发布：</label>
				<form:radiobuttons path="isPub" items="${fns:getDictList('trueOrFalse')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
			</li>
			<li><label style="text-align:right;width:100px">是否置顶：</label>
				<form:radiobuttons path="isTop" items="${fns:getDictList('trueOrFalse')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
			</li>
			<li class="btns">
			<label style="text-align:right;width:60px"></label>
			<input id="btnSubmit" class="btn btn-primary " style="width:80px;" type="submit" value="查   询"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th style="text-align:center">标题</th>
				<th style="text-align:center">频道</th>
				<th style="text-align:center">分类</th>
				<th style="text-align:center">模板类型</th>
				<th style="text-align:center">文章来源</th>
<!-- 				<th style="text-align:center">权重</th>
				<th style="text-align:center">展示点击数</th>
				<th style="text-align:center">真实点击数</th>
				<th style="text-align:center">允许评论</th> -->
				<th style="text-align:center">是否静态</th>
				<th style="text-align:center">是否置顶</th>			
				<th style="text-align:center">是否发布</th>
				<th style="text-align:center">发布时间</th>
				<shiro:hasPermission name="cms:cmsArticle:edit"><th style="text-align:center">操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="cmsArticle">
			<tr id="tr${cmsArticle.id}">
				<td><a href="${ctx}/cms/cmsArticle/query?id=${cmsArticle.id}">
					<c:choose>
					  <c:when test="${fn:length(cmsArticle.title)>40}">
		                 <span>${fn:substring(cmsArticle.title,0,40)}...</span>
		        	  </c:when>
		        	  <c:otherwise>
		        	    <span>${cmsArticle.title}</span>
		        	  </c:otherwise>
					</c:choose>
				</a></td>
				<td style="text-align:center">
					${cmsArticle.channel.name}
				</td>
				<td style="text-align:center">
					${cmsArticle.category.name}
				</td>
			    <td style="text-align:center">
					${fns:getDictLabel(cmsArticle.moduleType, 'moduleType', '')}
				</td>
				<td style="text-align:center">
					${cmsArticle.copyfrom}
				</td>
<!-- 				<td style="text-align:center"> -->
<%-- 					${cmsArticle.weight} --%>
<!-- 				</td> -->
<!-- 				<td style="text-align:center"> -->
<%-- 					${cmsArticle.displayHits} --%>
<!-- 				</td> -->
<!-- 				<td style="text-align:center"> -->
<%-- 					${cmsArticle.realHits} --%>
<!-- 				</td> -->
<!-- 				<td style="text-align:center"> -->
<%-- 					${fns:getDictLabel(cmsArticle.allowComment, 'trueOrFalse', '')} --%>
<!-- 				</td> -->
				<td style="text-align:center">
					${fns:getDictLabel(cmsArticle.isStatic, 'trueOrFalse', '')}
				</td>
				<td style="text-align:center">
					${fns:getDictLabel(cmsArticle.isTop, 'trueOrFalse', '')}
				</td>
				<td style="text-align:center">
					${fns:getDictLabel(cmsArticle.isPub, 'trueOrFalse', '')}
				</td>
				<td style="text-align:center">
					<fmt:formatDate value="${cmsArticle.pubTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td  style="text-align:center">
				<shiro:hasPermission name="cms:cmsArticle:edit">
					<c:choose>
						<c:when test="${cmsArticle.isTop == 'false'}">
						   <a href="javascript:;" id = "top${cmsArticle.id}" class="top" data-id="${cmsArticle.id}" data-sts="1">置顶</a>
						</c:when>
						<c:when test="${cmsArticle.isTop == 'true'}">
						   <a href="javascript:;" id = "top${cmsArticle.id}" class="top" data-id="${cmsArticle.id}" data-sts="0">取消置顶</a>
						</c:when>
					</c:choose>		
					<c:choose>
						<c:when test="${cmsArticle.isPub == 'false'}">
						   <a href="javascript:;" id = "pub${cmsArticle.id}" class="pub" data-id="${cmsArticle.id}" data-sts="1">发布</a>
						</c:when>
						<c:when test="${cmsArticle.isPub == 'true'}">
						   <a href="javascript:;" id = "pub${cmsArticle.id}" class="pub" data-id="${cmsArticle.id}" data-sts="0">撤回</a>
						</c:when>
					</c:choose>		
    				<a href="${ctx}/cms/cmsArticle/update?id=${cmsArticle.id}">修改</a>
    				<a href="javascript:;" id = "genstatic${cmsArticle.id}" class="genstatic" data-id="${cmsArticle.id}">生成静态</a>
    			</shiro:hasPermission>
    			<shiro:hasPermission name="cms:cmsArticle:delete">
					<a href="${ctx}/cms/cmsArticle/delete?id=${cmsArticle.id}" onclick="return confirmx('确认要删除该文章吗？', this.href)">删除</a>
				</shiro:hasPermission>
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>