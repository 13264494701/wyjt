<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>会员管理</title>
	<meta name="decorator" content="default"/>
	<script src="${ctxStatic}/jquery/jquery.lazyload.js" type="text/javascript"></script>
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
		function showMemberAct(obj) {
			var memberId = $(obj).closest("tr").attr("id");
			var url = '${admin}/memberAct?member.id='+memberId;
			var d = dialog({
		    	id: "memberAct",
		    	title: '账户明细',
		    	url:url,
		    	width: 1000,
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
		function showMemberFriend(obj) {
			var memberId = $(obj).closest("tr").attr("id");
			var url = '${admin}/memberFriendRelation?member.id='+memberId;
			var d = dialog({
		    	id: "memberFriend",
		    	title: '好友列表',
		    	url:url,
		    	width: 1000,
		    	height: 600,
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
		<li class="active"><a href="${admin}/member/list">会员列表</a></li>
	</ul>
	<form:form id="searchForm" modelAttribute="member" action="${admin}/member/list" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label style="text-align:right;width:150px">会员编号：</label>
				<form:input path="id" htmlEscape="false" maxlength="64" class="input-medium"/>
			</li>
			<li><label style="text-align:right;width:150px">手机号码：</label>
				<form:input path="username" htmlEscape="false" maxlength="64" class="input-medium"/>
			</li>
			<li><label style="text-align:right;width:150px">会员姓名：</label>
				<form:input path="name" htmlEscape="false" maxlength="64" class="input-medium"/>
			</li>
			<li><label style="text-align:right;width:150px">会员昵称：</label>
				<form:input path="nickname" htmlEscape="false" maxlength="64" class="input-medium"/>
			</li>
			<li><label style="text-align:right;width:150px">注册渠道：</label>
				<form:select path="regChannel"  style="width:177px" class="input-medium">
					<form:option value="" label="请选择"/>
					<form:options items="${fns:getDictList('regChannel')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
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
			    <th style="text-align:center">会员编号</th>	
				<th style="text-align:center">手机号码</th>		
				<th style="text-align:center">会员等级</th>
				<th style="text-align:center">会员昵称</th>
				<th style="text-align:center">会员姓名</th>
				<th style="text-align:center">是否可用</th>
				<th style="text-align:center">是否锁定</th>
				<th style="text-align:center">注册渠道</th>
				<shiro:hasPermission name="mem:member:edit"><th style="text-align:center">操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="member">
			<tr id="${member.id}">
				<td style="text-align:center">
					${member.id}
				</td>
				<td style="text-align:center"><a href="${admin}/member/query?id=${member.id}">
					${member.username}
				</a></td>
				<td style="text-align:center">
					${fns:getMemRankName(member.memberRank.rankNo)}
				</td>
				<td style="text-align:center">
					${member.nickname}
				</td>
				<td style="text-align:center">
					${member.name}
				</td>
				<td style="text-align:center">
					${fns:getDictLabel(member.isEnabled, 'trueOrFalse', '')}
				</td>
				<td style="text-align:center">
					${fns:getDictLabel(member.isLocked, 'trueOrFalse', '')}
				</td>
				<td style="text-align:center">
					${fns:getDictLabel(member.regChannel, 'regChannel', '')}
				</td>
				<td  style="text-align:center">
				<shiro:hasPermission name="mem:member:edit">
				    <a href ="javascript:void(0);" onclick="showMemberAct(this)">账户列表</a>
				    <br>
				    <a href ="javascript:void(0);" onclick="showMemberFriend(this)">好友列表</a>
				    <br>
				    <a href ="${admin}/memberActTrx/allSubTrxList?member.id=${member.id}">资金明细</a>
				    <br>
				    <a href ="${admin}/memberCard/list?member.id=${member.id}">银行卡列表</a>
				    <br>
    				<a href="${admin}/member/update?id=${member.id}">修改</a>
				</shiro:hasPermission>
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>