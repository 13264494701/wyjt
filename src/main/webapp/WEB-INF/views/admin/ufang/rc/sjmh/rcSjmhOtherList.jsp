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
<h1>  
	<c:choose>  
       <c:when test="${channel==0}">
           	运营商查询记录   
       </c:when> 
       <c:when test="${channel==1}">
           	淘宝查询记录  
       </c:when> 
       <c:when test="${channel==2}">
           	网银查询记录  
       </c:when> 
       <c:when test="${channel==3}">
           	社保查询记录 
       </c:when> 
       <c:when test="${channel==4}">
           	公积金查询记录  
       </c:when> 
       <c:when test="${channel==5}">
           	学信网查询记录  
       </c:when> 
       <c:when test="${channel==6}">
           	京东查询记录  
       </c:when> 
       <c:otherwise>  
       </c:otherwise>  
    </c:choose>
</h1>
	<form:form id="searchForm" modelAttribute="rcSjmh" action="${ufang}/rcSjmh/otherlist" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
       <input type="hidden" name="channel" value="${channel}">
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
				<th style="text-align:center">查询时间</th>
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
					${rcSjmh.realName}
				</td>
				<td style="text-align:center">
					<fmt:formatDate value="${rcSjmh.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td  style="text-align:center">
				<shiro:hasPermission name="ufang:rcSjmh:view">
				<c:choose>  
       <c:when test="${channel==1}">
        <a href="${ufang}/rcSjmh/tblist?rcSjmhId=${rcSjmh.id}">查看报告</a> 
       </c:when> 
       <c:when test="${channel==2}">
           <a href="${ufang}/rcSjmh/banklist?rcSjmhId=${rcSjmh.id}">查看报告</a> 
       </c:when> 
       <c:when test="${channel==3}">
           	<a href="${ufang}/rcSjmh/sblist?rcSjmhId=${rcSjmh.id}">查看报告</a> 
       </c:when> 
       <c:when test="${channel==4}">
           	<a href="${ufang}/rcSjmh/gjjlist?rcSjmhId=${rcSjmh.id}">查看报告</a> 
       </c:when> 
       <c:when test="${channel==5}">
           <a href="${ufang}/rcSjmh/xxlist?rcSjmhId=${rcSjmh.id}">查看报告</a> 
       </c:when> 
       <c:when test="${channel==6}">
           	<a href="${ufang}/rcSjmh/jdlist?rcSjmhId=${rcSjmh.id}">查看报告</a>
       </c:when> 
       <c:otherwise>  
       </c:otherwise>  
    </c:choose>
    	</shiro:hasPermission>
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>