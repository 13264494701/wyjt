<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>交易规则管理</title>
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
		//特别注意函数名称不要和变量重名，
		//函数名不能和页面的某个标签的id名相同。一些浏览器可以通过在js代码中指定ID访问节点元素，然后定义的函数就会被DOM中的元素覆盖了。您需要重命名函数名称或元素ID
		function showTrxRuleItemDialog(obj){
			var trxCode = $(obj).closest("tr").attr("trxCode");
			var url = '${ctx}/nfsTrxRuleItem?trxCode='+trxCode;
			dialog({
		    	id: "ruleItem",
		    	title: '规则明细',
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
                     console.log(this.returnValue);
                     if(this.returnValue=='success'){
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
		<li class="active"><a href="${ctx}/nfsTrxRule/">交易规则列表</a></li>
		<shiro:hasPermission name="trx:nfsTrxRule:edit"><li><a href="${ctx}/nfsTrxRule/add">交易规则添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="nfsTrxRule" action="${ctx}/nfsTrxRule/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label style="text-align:right;width:150px">交易代码：</label>
				<form:input path="trxCode" htmlEscape="false" maxlength="5" class="input-medium"/>
			</li>
			<li><label style="text-align:right;width:150px">交易名称：</label>
				<form:input path="name" htmlEscape="false" maxlength="32" class="input-medium"/>
			</li>
			<li class="btns"><label style="text-align:right;width:50px"></label><input id="btnSubmit" class="btn btn-primary " style="width:80px;" type="submit" value="查   询"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th style="text-align:center">交易代码</th>
				<th style="text-align:center">交易名称</th>
				<th style="text-align:center">创建时间</th>
				<shiro:hasPermission name="trx:nfsTrxRule:edit"><th style="text-align:center">操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="nfsTrxRule">
			<tr trxCode="${nfsTrxRule.trxCode}">
				<td style="text-align:center">
					${nfsTrxRule.trxCode}
				</td>
				<td style="text-align:center">
					${nfsTrxRule.name}
				</td>
				<td style="text-align:center">
					<fmt:formatDate value="${nfsTrxRule.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td  style="text-align:center">
				<shiro:hasPermission name="trx:nfsTrxRule:edit">
				    <a href ="javascript:void(0);" onclick="showTrxRuleItemDialog(this);">规则明细</a>
    				<a href="${ctx}/nfsTrxRule/update?id=${nfsTrxRule.id}">修改</a>
					<a href="${ctx}/nfsTrxRule/delete?id=${nfsTrxRule.id}" onclick="return confirmx('确认要删除该交易规则吗？', this.href)">删除</a>
				</shiro:hasPermission>
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>