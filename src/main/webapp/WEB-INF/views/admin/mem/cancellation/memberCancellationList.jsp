<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>会员注销申请管理</title>
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
		function showCheckCancellationDialog(cancellationId){
			var url = '${ctx}/memberCancellation/update?id='+cancellationId;
			dialog({
		    	id: "memberCancellationUpdate",
		    	title: '审核注销',
		    	url:url,
		    	width: 800,
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
                     if(this.returnValue == '0'){
                    	 location.reload();
                     }               
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
		<li class="active"><a href="${ctx}/memberCancellation/">会员注销申请列表</a></li>
	</ul>
	<form:form id="searchForm" modelAttribute="memberCancellation" action="${ctx}/memberCancellation/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label style="text-align:right;width:150px">会员编号：</label>
				<form:input path="member.id" htmlEscape="false" maxlength="20" class="input-medium"/>
			</li>
			<li><label style="text-align:right;width:150px">会员手机号：</label>
				<form:input path="member.username" htmlEscape="false" maxlength="20" class="input-medium"/>
			</li>
			<li><label style="text-align:right;width:150px">注销申请状态：</label>
				<form:select path="status"  style="width:177px" class="input-medium">
					<form:option value="" label="请选择"/>
					<form:options items="${fns:getDictList('cancellationStatus')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
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
				<th style="text-align:center">编号</th>
				<th style="text-align:center">会员编号</th>
				<th style="text-align:center">会员手机号</th>
				<th style="text-align:center">注销申请状态</th>
				<th style="text-align:center">注销原因</th>
				<th style="text-align:center">申请时间</th>
				<th style="text-align:center">备注</th>
				<shiro:hasPermission name="mem:memberCancellation:edit"><th style="text-align:center">操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="memberCancellation">
			<tr>
				<td style="text-align:center"><a href="${ctx}/memberCancellation/query?id=${memberCancellation.id}">
					${memberCancellation.id}
				</a></td>
				<td style="text-align:center"><a href="${admin}/member/query?id=${memberCancellation.member.id}">
					${memberCancellation.member.id}（${memberCancellation.member.name}）
				</a></td>
				<td style="text-align:center">
					${memberCancellation.member.username}
				</td>
				<td style="text-align:center">
					${fns:getDictLabel(memberCancellation.status, 'cancellationStatus', '')}
				</td>
				<td style="text-align:center">
					${memberCancellation.reason}
				</td>
				<td style="text-align:center">
					${memberCancellation.rmk}
				</td>
				<td style="text-align:center">
					${fns:getDateStr(memberCancellation.createTime,"yyyyMMdd HH:mm:ss")}
				</td>
				<td  style="text-align:center">
				<shiro:hasPermission name="mem:memberCancellation:edit">
					<c:if test="${memberCancellation.status eq 'review'}">
	    				<a  href="javascript:void(0)" onclick='showCheckCancellationDialog("${memberCancellation.id}");'>通过</a> ||
	    				<a  href="javascript:void(0)" onclick='showCheckCancellationDialog("${memberCancellation.id}");'>拒绝</a>
					</c:if>
				</shiro:hasPermission>
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>