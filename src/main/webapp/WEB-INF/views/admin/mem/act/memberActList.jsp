<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>会员账户管理</title>
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
			var url = '${admin}/memberAct/addBal?id='+actId;
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
			var url = '${admin}/memberAct/reduceBal?id='+actId;
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
		function showActTrxDetailDialog(obj){
		    var subNo = $(obj).closest("tr").attr("subNo");
			var memberId = $(obj).closest("tr").attr("memberId");
			var url = '${admin}/memberActTrx/list?member.id='+memberId+'&subNo='+subNo;
			dialog({
		    	id: "actTrxDetail",
		    	title: '流水明细',
		    	url:url,
		    	width: 1000,
		    	height: 800,
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
		<li class="active"><a href="${ctx}/memberAct/">会员账户列表</a></li>
		<%-- <shiro:hasPermission name="act:memberAct:edit">
			<li class="active"><a href="${ctx}/memberAct/add?member.id=${memberAct.member.id}">会员账户添加</a></li>
		</shiro:hasPermission> --%>
	</ul>
	<form:form id="searchForm" modelAttribute="memberAct" action="${ctx}/memberAct/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th style="text-align:center">账户科目</th>
				<th style="text-align:center">币种</th>
				<th style="text-align:center">账户名称</th>
				<th style="text-align:center">账户余额</th>
				<th style="text-align:center">是否默认</th>
				<th style="text-align:center">账户状态</th>
				<shiro:hasPermission name="act:memberAct:edit"><th style="text-align:center">操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="memberAct">
			<tr id="${memberAct.id}" memberId="${memberAct.member.id}" subNo="${memberAct.subNo}">
				<td style="text-align:center">
					${memberAct.subNo}
				</td>
				<td style="text-align:center">
					${memberAct.currCode}
				</td>
				<td style="text-align:center">
					${fns:getSubName('member',memberAct.subNo)}
				</td>
				<td style="text-align:center">
					${memberAct.curBal}
				</td>
				<td style="text-align:center">
					${fns:getDictLabel(memberAct.isDefault, 'trueOrFalse', '')}
				</td>
				<td style="text-align:center">
					${fns:getDictLabel(memberAct.status, 'actStatus', '')}
				</td>
				<td  style="text-align:center">
				<shiro:hasPermission name="act:memberAct:edit">
				   <c:if test="${memberAct.subNo=='0001'}">
    				<a href ="javascript:void(0);" onclick="showAddActBalDialog(this);">加款</a>  	
    				<a href ="javascript:void(0);" onclick="showReduceActBalDialog(this);">减款</a>
    			   </c:if>
    			   <a href ="javascript:void(0);" onclick="showActTrxDetailDialog(this);">流水明细</a> 				
				</shiro:hasPermission>
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>