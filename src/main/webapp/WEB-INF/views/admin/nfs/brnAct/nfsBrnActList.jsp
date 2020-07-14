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
		function showUpdateActBalDialog(obj){
			var actId = $(obj).closest("tr").attr("id");
			var url = '${ctx}/nfsBrnAct/update?id='+actId;
			dialog({
		    	id: "updateActBal",
		    	title: '加减款',
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
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/nfsBrnAct/">机构账户列表</a></li>
		<shiro:hasPermission name="brn:nfsBrnAct:edit"><li><a href="${ctx}/nfsBrnAct/add">机构账户添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="nfsBrnAct" action="${ctx}/nfsBrnAct/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>归属机构：</label>
			<sys:treeselect id="company" name="company.brnNo" value="${nfsBrnAct.company.brnNo}" labelName="brn.name" labelValue="${nfsBrnAct.company.brnName}"
				title="部门"  baseUrl="${admin}" url="/sys/brn/treeData?type=2" cssClass="required" allowClear="true" notAllowSelectParent="true"/>
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
				<th style="text-align:center">归属机构</th>
				<th style="text-align:center">账户名称</th>
				<th style="text-align:center">账户余额</th>
				<th style="text-align:center">账户状态</th>
				<th style="text-align:center">创建时间</th>
				<shiro:hasPermission name="brn:nfsBrnAct:edit"><th style="text-align:center">操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="nfsBrnAct">
			<tr id="${nfsBrnAct.id}">
				<td style="text-align:center"><a href="${ctx}/nfsBrnAct/query?id=${nfsBrnAct.id}">
					${nfsBrnAct.company.brnName}
				</a></td>
				<td style="text-align:center">
					${nfsBrnAct.actNam}
				</td>
				<td style="text-align:center">
					${fns:decimalToStr(nfsBrnAct.curBal,2)}元
				</td>
				<td style="text-align:center">
					${fns:getDictLabel(nfsBrnAct.status, 'actStatus', '')}
				</td>
				<td style="text-align:center">
					<fmt:formatDate value="${nfsBrnAct.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td  style="text-align:center">	
				    <a href ="javascript:void(0);" onclick="showUpdateActBalDialog(this);">加减款</a>   								
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>