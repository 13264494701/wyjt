<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>银行编码管理</title>
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
		function showBinDetails(obj) {
			var bankId = $(obj).closest("tr").attr("id");
			var url = '${ctx}/nfsBankBin?bank.id='+bankId;
			var d = dialog({
		    	id: "binDetails",
		    	title: '支持BIN',
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
		<li class="active"><a href="${ctx}/nfsBankInfo/">银行列表</a></li>
		<shiro:hasPermission name="bank:nfsBankInfo:edit"><li><a href="${ctx}/nfsBankInfo/add">银行添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="nfsBankInfo" action="${ctx}/nfsBankInfo/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label style="text-align:right;width:150px">银行名称：</label>
				<form:input path="name" htmlEscape="false" maxlength="64" class="input-medium"/>
			</li>
			<li class="btns"><label style="text-align:right;width:50px"></label><input id="btnSubmit" class="btn btn-primary " style="width:80px;" type="submit" value="查   询"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th style="text-align:center">银行名称</th>
				<th style="text-align:center">图标</th>
				<th style="text-align:center">每笔限额</th>
				<th style="text-align:center">每天限额</th>
				<th style="text-align:center">每月限额</th>
				<th style="text-align:center">客服热线</th>
				<th style="text-align:center">是否支持</th>
				<th style="text-align:center">创建时间</th>
				<shiro:hasPermission name="bank:nfsBankInfo:edit"><th style="text-align:center">操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="nfsBankInfo">
			<tr id="${nfsBankInfo.id}">
				<td style="text-align:center"><a href="${ctx}/nfsBankInfo/query?id=${nfsBankInfo.id}">
					${nfsBankInfo.name}
				</a></td>
				<td style="text-align:center">
					<img src="${nfsBankInfo.logo}" />
				</td>
				<td style="text-align:center">
					${nfsBankInfo.limitPerTrx}
				</td>
				<td style="text-align:center">
					${nfsBankInfo.limitPerDay}
				</td>
				<td style="text-align:center">
					${nfsBankInfo.limitPerMonth}
				</td>
				<td style="text-align:center">
					${nfsBankInfo.hotline}
				</td>
				<td style="text-align:center">
					${fns:getDictLabel(nfsBankInfo.isSupport, 'trueOrFalse', '')}
				</td>
				<td style="text-align:center">
					<fmt:formatDate value="${nfsBankInfo.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td  style="text-align:center">
				<shiro:hasPermission name="bank:nfsBankInfo:edit">
				    <a href ="javascript:void(0);" onclick="showBinDetails(this)">支持BIN</a>	
    				<a href="${ctx}/nfsBankInfo/update?id=${nfsBankInfo.id}">修改</a>
					<a href="${ctx}/nfsBankInfo/delete?id=${nfsBankInfo.id}" onclick="return confirmx('确认要删除该银行编码吗？', this.href)">删除</a>
				</shiro:hasPermission>
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>