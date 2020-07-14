<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>好友关系管理</title>
	<meta name="decorator" content="default"/>
    <script src="${ctxStatic}/artdialog/js/dialog-plus.js"></script>
    <link href="${ctxStatic}/artdialog/css/dialog.css" rel="stylesheet" />
	<script type="text/javascript">
		$(document).ready(function() {
			
		});
		function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#searchForm").submit();
        	return false;
        }
		function showLoanToFriend(obj) {
			var friendId = $(obj).closest("tr").attr("id");
			var url = '${ufang}/loanApply/loanToFriend?friendId='+friendId;
			var d = dialog({
		    	id: "loanToFriend",
		    	title: '借款给好友',
		    	url:url,
		    	width: 800,
		    	height: 500,
		    	padding: 0,
		    	drag:true,
		    	quickClose: true,
                onshow: function() {
                     console.log('onshow');
                 },
                 oniframeload: function() {
                     console.log('oniframeload');
                 },
                 onclose: function() {
                     console.log('onclose');
                 },
                 onremove: function() {
                     console.log('onremove');
                 }
		    }).show();	
		}
		
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ufang}/friend/">好友列表</a></li>		
	</ul>
	<p style = "text-align:center;color:red;font-size:20px;width:1500px">${msg}</p>
	<form:form id="searchForm" modelAttribute="memberFriendRelation" action="${ufang}/friend/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label style="text-align:right;width:150px">好友姓名：</label>
				<form:input path="friend.name" htmlEscape="false" maxlength="64" class="input-medium"/>
			</li>
			<li><label style="text-align:right;width:150px">手机号码：</label>
				<form:input path="friend.username" htmlEscape="false" maxlength="64" class="input-medium"/>
			</li>
			<li class="btns"><label style="text-align:right;width:50px"></label><input id="btnSubmit" class="btn btn-primary " style="width:80px;" type="submit" value="查   询"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
			    <th style="text-align:center">姓名</th>
				<th style="text-align:center">手机号</th>
			
				<shiro:hasPermission name="ufang:friend:edit">
				<th style="text-align:center">操作</th>
				</shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="memberFriendRelation">
			<tr id="${memberFriendRelation.friend.id}">
				<td style="text-align:center">
					${memberFriendRelation.friend.name}
				</td>
				<td style="text-align:center">
					${memberFriendRelation.friend.username}
				</td>

				<td  style="text-align:center">
				<shiro:hasPermission name="ufang:friend:edit">  								    
				    <a href ="${ufang}/friend/loanAnalysis?friend.id=${memberFriendRelation.friend.id}">借贷分析</a>  			
                    <a href ="javascript:void(0);" onclick="showLoanToFriend(this)">借款给好友</a>
				</shiro:hasPermission>
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>