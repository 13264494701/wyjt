<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>机构账户管理</title>
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
		function showAddActBalDialog(obj){
			var actId = $(obj).closest("tr").attr("id");
			var url = '${admin}/ufangBrnAct/addBal?id='+actId;
			dialog({
		    	id: "addActBal",
		    	title: '加款',
		    	url:url,
		    	width: 600,
		    	height: 400,
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
		function showReduceActBalDialog(obj){
			var actId = $(obj).closest("tr").attr("id");
			var url = '${admin}/ufangBrnAct/reduceBal?id='+actId;
			dialog({
		    	id: "reduceActBal",
		    	title: '减款',
		    	url:url,
		    	width: 600,
		    	height: 400,
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
		function showDistributeFundDialog(obj){
            var brnid = $(obj).closest("tr").attr("brnid");
			var url = '${admin}/ufangBrnActTrx?company.id='+brnid;
			dialog({
		    	id: "distributeFund",
		    	title: '交易明细',
		    	url:url,
		    	width: 1200,
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
		<li class="active"><a href="${admin}/ufangBrnAct/">机构账户列表</a></li>
		<shiro:hasPermission name="ufang:brnAct:edit"><li><a href="${admin}/ufangBrnAct/add">机构账户添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="ufangBrnAct" action="${admin}/ufangBrnAct/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label style="text-align:right;width:150px">机构名称：</label>
				<form:input path="company.brnName" htmlEscape="false" maxlength="16" class="input-medium"/>
			</li>
			<li><label style="text-align:right;width:150px">账户科目：</label>
				<form:input path="subNo" htmlEscape="false" maxlength="4" class="input-medium"/>
			</li>
			<li class="btns"><label style="text-align:right;width:50px"></label><input id="btnSubmit" class="btn btn-primary " style="width:80px;" type="submit" value="查   询"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th style="text-align:center">归属公司</th>
				<th style="text-align:center">账户名称</th>
				<th style="text-align:center">账户余额</th>
				<th style="text-align:center">账户状态</th>
				<th style="text-align:center">创建时间</th>
				<shiro:hasPermission name="ufang:brnAct:edit"><th style="text-align:center">操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="ufangBrnAct">
			<tr id="${ufangBrnAct.id}" brnid="${ufangBrnAct.company.id}">
				<td style="text-align:center"><a href="${admin}/ufangBrnAct/query?id=${ufangBrnAct.id}">
					${ufangBrnAct.company.brnName}
				</a></td>
				<td style="text-align:center">
					${fns:getSubName('ufangBrn',ufangBrnAct.subNo)}账户
				</td>
				<td style="text-align:center">
					${fns:decimalToStr(ufangBrnAct.curBal,2)}元
				</td>
				<td style="text-align:center">
					${fns:getDictLabel(ufangBrnAct.status, 'actStatus', '')}
				</td>
				<td style="text-align:center">
					<fmt:formatDate value="${ufangBrnAct.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td  style="text-align:center">	
				    <a href ="javascript:void(0);" onclick="showAddActBalDialog(this);">加款</a> 
				    <a href ="javascript:void(0);" onclick="showReduceActBalDialog(this);">减款</a>  
				    <a href="${admin}/ufang/user/list?brn.id=${ufangBrnAct.company.id}">员工列表</a>
					<a href ="javascript:void(0);" onclick="showDistributeFundDialog(this);">交易明细</a>
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>