<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>员工管理</title>
<meta name="decorator" content="default" />
<script src="${ctxStatic}/artdialog/js/dialog-plus.js"></script>
<link href="${ctxStatic}/artdialog/css/dialog.css" rel="stylesheet" />
<script type="text/javascript">
	$(document).ready(function() {

	});
	function page(n, s) {
		if (n)
			$("#pageNo").val(n);
		if (s)
			$("#pageSize").val(s);
		$("#searchForm").attr("action", "${admin}/ufang/user/list");
		$("#searchForm").submit();
		return false;
	}
	function showActTrxDetailDialog(obj) {
		var id = $(obj).closest("tr").attr("id");
		var url = '${ctx}/ufangUserActTrx?id='+id;
		var d = dialog({
	    	id: "actTrxDetail",
	    	title: '交易明细',
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
	function addUfangLoaner(obj) {
		var id = $(obj).closest("tr").attr("id");
		$.ajax({
			type : 'GET',
			url : '${admin}/ufangLoaner/checkUser',
			data : {
				 id:id
			},
			success : function(rsp) {
				if(rsp.status == true)
				{
					location.href= "${admin}/ufangLoaner/add?user.id="+id;
				}else{
				   top.$.jBox.tip(rsp.message);					   
				}					
			}
		})
	}
</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${admin}/ufang/user/list">员工列表</a></li>
		<shiro:hasPermission name="ufang:user:edit">
			<li><a href="${admin}/ufang/user/add">员工添加</a></li>
		</shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="user" action="${admin}/ufang/user/list" method="post" class="breadcrumb form-search ">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}" />
		<input id="pageSize" name="pageSize" type="hidden"value="${page.pageSize}" />
		<sys:tableSort id="orderBy" name="orderBy" value="${page.orderBy}" callback="page();" />
		<ul class="ul-form">
			<li><label>登录名称：</label>
				<form:input path="username" htmlEscape="false" maxlength="50" class="input-medium" />
			</li>
			<li><label>员工姓名：</label>
				<form:input path="empNam" htmlEscape="false" maxlength="50" class="input-medium" />
			</li>
			<li><label>归属机构：</label>
			<sys:treeselect id="brn" name="brn.id" value="${user.brn.id}" labelName="brn.brnName" labelValue="${user.brn.brnName}" title="机构"
					 baseUrl="${admin}" url="/ufang/brn/treeData?type=2" cssClass="input-small" allowClear="true" notAllowSelectParent="false" />
			</li>
			<li class="btns">
				<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询" onclick="return page();" /> 
			</li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}" />
	<table id="contentTable"
		class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
			    <th style="text-align:center">编号</th>
			    <th style="text-align:center" class="sort-column name">姓名</th>
			    <th style="text-align:center" class="sort-column login_name">用户名</th>
				<th style="text-align:center">归属公司</th>
				<th style="text-align:center">归属部门</th>	
				<th style="text-align:center">账户余额</th>	
				<th style="text-align:center">是否绑定</th>
				<th style="text-align:center">是否可用</th>	
				<th style="text-align:center">是否锁定</th>	
				<th style="text-align:center">登录时间</th>			
				<shiro:hasPermission name="ufang:user:edit"><th style="text-align:center">操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${page.list}" var="user">
				<tr id="${user.id}">
				    <td style="text-align:center"><a href="${admin}/ufang/user/query?id=${user.id}">${user.empNo}</a></td>
				    <td style="text-align:center">${user.empNam}</td>
				    <td style="text-align:center">${user.username}</td>
				    <td style="text-align:center">${user.brn.parent.brnName}</td>
					<td style="text-align:center">${user.brn.brnName}</td>	
					<td style="text-align:center">${ufang:getByUserAndSubNo(user.id, '4001')}</td>	
					<td style="text-align:center">${fns:getDictLabel(user.bindStatus, 'bindStatus', '')}</td>	
					<td style="text-align:center">${fns:getDictLabel(user.isEnabled, 'trueOrFalse', '')}</td>
					<td style="text-align:center">${fns:getDictLabel(user.isLocked, 'trueOrFalse', '')}</td>	
					<td style="text-align:center">
					   <fmt:formatDate value="${user.loginDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				    </td>			
					<td style="text-align: center">
					   <shiro:hasPermission name="ufang:user:edit">		
					    <a href ="javascript:void(0);" onclick="showActTrxDetailDialog(this);">交易明细</a>		    
						<a href="${admin}/ufang/user/update?id=${user.id}">修改</a>
						<a href="${admin}/ufang/user/resetPwd?id=${user.id}"onclick="return confirmx('确认要重置该员工密码吗？', this.href)">密码重置</a>
						<a href="${admin}/ufang/user/delete?id=${user.id}" onclick="return confirmx('确认要删除该员工吗？', this.href)">删除</a>
					   </shiro:hasPermission>
				    </td>								
				</tr>
			</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>