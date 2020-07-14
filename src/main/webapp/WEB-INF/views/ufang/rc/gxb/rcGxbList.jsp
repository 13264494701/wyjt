<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<html>
<head>
    <title>风控管理</title>
    <meta name="decorator" content="default"/>
    <script type="text/javascript">
        $(document).ready(function () {

        });

        function page(n, s) {
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
	  <c:when test="${type=='sesame_multiple'}">   
	    	芝麻分查询记录
	  </c:when> 
	  <c:when test="${subType=='jdb'}">   
	    	借贷宝查询记录
	  </c:when> 
	  <c:when test="${subType=='hhd'}">   
	    	米房查询记录
	  </c:when> 
	  <c:when test="${subType=='jjd'}">   
	    	今借到查询记录
	  </c:when> 
	</c:choose> 
</h1>
<form:form id="searchForm" modelAttribute="rcGxb" action="${ufang}/rcGxb/" method="post" class="breadcrumb form-search">
    <input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
    <input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
    <input id="type" name="type" type="hidden" value="${type}"/>
    <input id="subType" name="subType" type="hidden" value="${subType}"/>
    <ul class="ul-form">
            <%--	<li><label style="text-align:right;width:150px">授权类型：</label>
                    <form:input path="authType" htmlEscape="false" maxlength="4" class="input-medium"/>
                </li>--%>
        <c:if test="${type!='wechat_phone'}">
            <li><label style="text-align:right;width:150px">身份证号码：</label>
                <form:input path="idNo" htmlEscape="false" maxlength="18" class="input-medium"/>
            </li>
        </c:if>
        <li><label style="text-align:right;width:150px">手机号码：</label>
            <form:input path="phoneNo" htmlEscape="false" maxlength="11" class="input-medium"/>
        </li>
        <li class="btns"><label style="text-align:right;width:50px"></label>
            <input id="btnSubmit" class="btn btn-primary " style="width:80px;" type="submit" value="查   询"/></li>
        <li class="clearfix"></li>
    </ul>
</form:form>
<sys:message content="${message}"/>
<table id="contentTable" class="table table-striped table-bordered table-condensed">
    <thead>
    <tr>
        <%--				<th style="text-align:center">授权类型</th>--%>
        <c:if test="${type=='debit'}">
        <th style="text-align:center">手机号码</th>
            <th style="text-align:center">姓名</th>
            <th style="text-align:center">身份证号码</th>
            <th style="text-align:center">平台</th>
       		<th style="text-align:center">查询时间</th>
       		<th style="text-align:center"></th>
        </c:if>
        <c:if test="${type=='sesame_multiple'}">
        <th style="text-align:center">手机号码</th>
            <th style="text-align:center">姓名</th>
            <th style="text-align:center">身份证号码</th>
            <th style="text-align:center">芝麻分</th>
            <th style="text-align:center">是否为本人的芝麻分</th>
       		<th style="text-align:center">查询时间</th>
        </c:if>
        <c:if test="${type=='wechat_phone'}">
        <th style="text-align:center">手机号码</th>
            <th style="text-align:center">地区</th>
            <th style="text-align:center">结果</th>
            <th style="text-align:center">头像</th>
            <th style="text-align:center">昵称</th>
            <th style="text-align:center">性别</th>
            <th style="text-align:center">签名</th>
       		<th style="text-align:center">查询时间</th>
        </c:if>
        
        <%--			<shiro:hasPermission name="rcGxb:edit"><th style="text-align:center">操作</th></shiro:hasPermission>--%>
    </tr>
    </thead>
    <tbody>
    <c:forEach items="${page.list}" var="rcGxb">
        <tr>
            <c:if test="${type=='debit'}">
	            <td style="text-align:center">
	                    ${rcGxb.phoneNo}
	            </td>
                <td style="text-align:center">
                        ${rcGxb.name}
                </td>
                <td style="text-align:center">
                        ${rcGxb.idNo}
                </td>
                <td style="text-align:center">
                        ${fns:getDictLabel(rcGxb.subItem, 'gxbSubItem', '')}
                </td>
                <td style="text-align:center">
                    <fmt:formatDate value="${rcGxb.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
            	</td>
                <td style="text-align:center">
                    <a href="${ufang}/rcGxb/query?id=${rcGxb.id}&type=debit">查看详情</a>
            	</td>
            </c:if>
            <c:if test="${type=='sesame_multiple'}">
	            <td style="text-align:center">
	                    ${rcGxb.phoneNo}
	            </td>
                <td style="text-align:center">
                        ${rcGxb.name}
                </td>
                <td style="text-align:center">
                        ${rcGxb.idNo}
                </td>
                <td style="text-align:center">
                        ${rcGxb.sesameScore}
                </td>
                <td style="text-align:center">
                    <c:if test="${rcGxb.status==1}">
                        是
                    </c:if>
                    <c:if test="${rcGxb.status==0}">
                        否
                    </c:if>
                </td>
	            <td style="text-align:center">
	                    <fmt:formatDate value="${rcGxb.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
	            </td>
            </c:if>
            <c:if test="${type=='wechat_phone'}">
	            <td style="text-align:center">
	                    ${rcGxb.phoneNo}
	            </td>
                <td style="text-align:center">
                        ${rcGxb.area}
                </td>
                <td style="text-align:center">
                        ${rcGxb.remark}
                </td>
                <td style="text-align:center">
                    <img src="${rcGxb.avatar}" style="width: 40px;height: 40px">
                </td>
                <td style="text-align:center">
                        ${rcGxb.nickname}
                </td>
                <td style="text-align:center">
                    <c:if test="${rcGxb.sex==0}">
                        女
                    </c:if>
                    <c:if test="${rcGxb.sex==1}">
                        男
                    </c:if>
                    <c:if test="${rcGxb.sex==-1}">
                        未知
                    </c:if>
                </td>
                <td style="text-align:center">
                        ${rcGxb.signature}
                </td>
	            <td style="text-align:center">
	                    <fmt:formatDate value="${rcGxb.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
	            </td>
            </c:if>
                <%--<td  style="text-align:center">
                <shiro:hasPermission name="rcGxb:edit">
                    <a href="${ufang}/rcGxb/delete?id=${rcGxb.id}" onclick="return confirmx('确认要删除该风控 公信宝吗？', this.href)">删除</a>
                </shiro:hasPermission>
                </td>--%>
        </tr>
    </c:forEach>
    </tbody>
</table>
<div class="pagination">${page}</div>
</body>
</html>