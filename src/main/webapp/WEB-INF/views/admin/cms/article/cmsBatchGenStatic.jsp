<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>文章管理</title>
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
		function batchGenStatic(){
			
			var data = $("#searchForm").serialize();
			$.ajax({
				type : 'POST',
				url : '${ctx}/cms/cmsArticle/batchGenStatic',
				data : data,
				success : function(rsp) {
					top.$.jBox.tip(rsp.message);  						
				}
			})
			$("#btnBatchGenStatic").attr("disabled",true);
	    }
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/cms/cmsArticle/waitGenStatic">文章列表</a></li>
	</ul>
	<form:form id="searchForm" modelAttribute="cmsArticle" action="${ctx}/cms/cmsArticle/waitGenStatic" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<input type="hidden" name="batchFlag" id="batchFlag">
		<ul class="ul-form">
			<li><label style="text-align:right;width:100px">频道：</label>
			   <form:select path="channel.id"  style="width:177px" class="input-medium">
				   <form:option value="" label="请选择"/>
				   <form:options items="${fns:getChannelList()}" itemLabel="name" itemValue="id" htmlEscape="false"/>
			   </form:select>
		    </li>
		    <li><label style="text-align:right;width:120px">生成时间：</label>
				<input name="beginDate" type="text"  maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${cmsArticle.beginDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:true});"/> - 
				<input name="endDate" type="text" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${cmsArticle.endDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:true});"/>
			</li>
			<li><label style="text-align:right;width:100px">标题：</label>
				<form:input path="title" htmlEscape="false" maxlength="255" style="width:355px" class="input-medium"/>
			</li>
			<li><label style="text-align:right;width:70px">数量：</label>
				<form:input path="count" htmlEscape="false" maxlength="255" style="width:80px" class="input-medium"/>
			</li>
			<li class="btns">
			   <label style="text-align:right;width:30px"></label>
			   <input id="btnWaitGenStatic" class="btn btn-primary" style="width:80px;" type="submit" value="查&nbsp;&nbsp;&nbsp;询"/>
			   <span class="help-inline"  style="width:30px"></span>
			   <input id="btnBatchGenStatic" class="btn btn-primary" style="width:80px;" type="button" onclick="batchGenStatic()" value="生&nbsp;&nbsp;&nbsp;成"/>
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
				<th style="text-align:center">是否静态</th>
				<th style="text-align:center">是否发布</th>
				<th style="text-align:center">发布时间</th>				
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="cmsArticle">
			<tr>
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
				<td style="text-align:center">
					${fns:getDictLabel(cmsArticle.isStatic, 'trueOrFalse', '')}
				</td>
				<td style="text-align:center">
					${fns:getDictLabel(cmsArticle.isPub, 'trueOrFalse', '')}
				</td>
				<td style="text-align:center">
					<fmt:formatDate value="${cmsArticle.pubTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>