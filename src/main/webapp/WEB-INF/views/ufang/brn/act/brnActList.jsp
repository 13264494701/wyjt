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
        function showRechargeDialog(obj){
            var url = '${ufang}/brnAct/recharge';
            dialog({
                id: "updateActBal",
                title: '充值',
                url:url,
                width: 900,
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
		function showDistributeFundDialog(obj){
			var actId = $(obj).closest("tr").attr("id");
			var url = '${ufang}/ufangFundDistColl/distributeFund?brnAct.id='+actId;
			dialog({
		    	id: "distributeFund",
		    	title: '派发交易金',
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
		<li class="active"><a href="${ufang}/brnAct/">机构账户</a></li>		
	</ul>
	<form:form id="searchForm" modelAttribute="ufangBrnAct" action="${ufang}/brnAct/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>归属机构：</label>
			<sys:treeselect id="company" name="company.brnNo" value="${ufangBrnAct.company.brnNo}" labelName="brn.name" labelValue="${ufangBrnAct.company.brnName}"
				title="部门" baseUrl="${ufang}" url="/brn/treeData?type=2" cssClass="required" allowClear="true" notAllowSelectParent="true"/>
			</li>
			<li><label style="text-align:right;width:150px">账户名称：</label>
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
				<th style="text-align:center">操作</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="ufangBrnAct">
			<tr id="${ufangBrnAct.id}">
				<td style="text-align:center">
					${ufangBrnAct.company.brnName}
				</td>
				<td style="text-align:center">
					${ufang:getSubName('ufangBrn',ufangBrnAct.subNo)}
				</td>
				<td style="text-align:center">
					${ufang:decimalToStr(ufangBrnAct.curBal,2)}元
				</td>
				<td style="text-align:center">
					${ufang:getDictLabel(ufangBrnAct.status, 'actStatus', '')}
				</td>
				<td style="text-align:center">
					<fmt:formatDate value="${ufangBrnAct.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td  style="text-align:center">	
				    <a href ="${ufang}/brnAct/recharge">充值</a>
				    <a href ="javascript:void(0);" onclick="showDistributeFundDialog(this);">派发交易金</a> 
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>