<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>员工账户管理</title>
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
		function showActTrxDetailDialog(obj) {
			var id = $(obj).closest("tr").attr("id");
			var url = '${ufang}/userActTrx?id='+id;
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
        function showCollectFundDialog(obj){
            var actId = $(obj).closest("tr").attr("actid");
            var url = '${ufang}/ufangFundDistColl/collectFund?userAct.id='+actId;
            dialog({
                id: "collectFund",
                title: '回收交易金',
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

	</ul>
	<form:form id="searchForm" modelAttribute="ufangUserAct" action="${ufang}/userAct/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label style="text-align:right;width:150px">账户科目：</label>
				<form:input path="subNo" htmlEscape="false" maxlength="4" class="input-medium"/>
			</li>
			<li><label style="text-align:right;width:150px">账户状态：</label>
				<form:select path="status"  style="width:177px" class="input-medium">
					<form:option value="" label="请选择"/>
					<form:options items="${fns:getDictList('actStatus')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
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
			    <th style="text-align:center">员工姓名</th>
				<th style="text-align:center">员工编号</th>
				<th style="text-align:center">账户科目</th>
				<th style="text-align:center">账户余额</th>
				<th style="text-align:center">账户状态</th>
				<th style="text-align:center">创建时间</th>
				<shiro:hasPermission name="act:ufangUserAct:edit"><th style="text-align:center">操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="ufangUserAct">
			<tr id="${ufangUserAct.user.id}" actid="${ufangUserAct.id}">
				<td style="text-align:center">
					${ufangUserAct.user.empNam}
				</td>
				<td style="text-align:center">
					${ufangUserAct.user.empNo}
				</td>
				<td style="text-align:center">
					${fns:getSubName('ufangUser',ufangUserAct.subNo)}
				</td>
				<td style="text-align:center">
					${ufangUserAct.curBal}
				</td>
				<td style="text-align:center">
					${fns:getDictLabel(ufangUserAct.status, 'actStatus', '')}
				</td>
				<td style="text-align:center">
					<fmt:formatDate value="${ufangUserAct.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td style="text-align:center">
	                <a href ="javascript:void(0);" onclick="showActTrxDetailDialog(this);">交易明细</a>
					<a href ="javascript:void(0);" onclick="showCollectFundDialog(this);">回收交易金</a>
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>