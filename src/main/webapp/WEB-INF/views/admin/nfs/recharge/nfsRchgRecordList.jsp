<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>充值记录管理</title>
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
		
		function showQueryThirdOrderDialog(id){
			var url = '${admin}/rchgRecord/queryOrder?id='+id;
			dialog({
		    	id: "queryRechargeOrder",
		    	title: '订单查询',
		    	url:url,
		    	width: 600,
		    	height: 300,
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
                     if(this.returnValue == 'success'){
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
		<li class="active"><a href="${admin}/rchgRecord/">充值记录列表</a></li>
	</ul>
	<form:form id="searchForm" modelAttribute="nfsRchgRecord" action="${admin}/rchgRecord/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label style="text-align:right;width:150px">会员姓名：</label>
				<form:input path="member.name" htmlEscape="false" maxlength="64" class="input-medium"/>
			</li>
			<li><label style="text-align:right;width:150px">会员手机号：</label>
				<form:input path="member.username" htmlEscape="false" maxlength="64" class="input-medium"/>
			</li>
			<li><label style="text-align:right;width:150px">充值单号：</label>
				<form:input path="payment.paymentNo" htmlEscape="false" maxlength="64" class="input-medium"/>
			</li>
			<li><label style="text-align:right;width:150px">第三方单号：</label>
				<form:input path="payment.thirdPaymentNo" htmlEscape="false" maxlength="64" class="input-medium"/>
			</li>
			<li><label style="text-align:right;width:150px">充值金额：</label>
				<form:input path="minAmount" htmlEscape="false" maxlength="20" class="input-medium"/>
				<label style="text-align:center;width:30px">至：</label>
				<form:input path="maxAmount" htmlEscape="false" maxlength="20" class="input-medium"/>
			</li>
			<li><label style="text-align:right;width:150px">交易状态：</label>
				<form:select path="status"  style="width:177px" class="input-medium">
					<form:option value="" label="请选择"/>
					<form:options items="${fns:getDictList('rechargeStatus')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</li>
			<li><label style="text-align:right;width:150px">充值时间：</label>
				<input name="beginTime" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${nfsRchgRecord.beginTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:true});"/>
				<label style="text-align:center;width:30px">至：</label>
				<input name="endTimes" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${nfsRchgRecord.endTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:true});"/>
			</li>
			<li class="btns"><label style="text-align:right;width:50px"></label><input id="btnSubmit" class="btn btn-primary " style="width:80px;" type="submit" value="查   询"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th style="text-align:center">业务编号</th>
				<th style="text-align:center">充值单号</th>
				<th style="text-align:center">第三方单号</th>
				<th style="text-align:center">会员姓名</th>
				<th style="text-align:center">会员编号</th>
				<th style="text-align:center">会员手机号</th>
				<th style="text-align:center">充值金额</th>
				<th style="text-align:center">充值方式</th>
				<th style="text-align:center">银行卡号</th>
				<th style="text-align:center">银行名称</th>
				<th style="text-align:center">交易状态</th>
				<th style="text-align:center">充值时间</th>
				<shiro:hasPermission name="recharge:nfsRchgRecord:edit"><th style="text-align:center">操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="nfsRchgRecord" varStatus="index">
			 <c:if test="${index.count%2==0}">
			   <tr style="background-color:#e2dede;">
			</c:if> 
			<c:if test="${index.count%2==1}">
			   <tr>
			</c:if> 
				<td style="text-align:center"><a href="${ctx}/rchgRecord/query?id=${nfsRchgRecord.id}">
					${nfsRchgRecord.id}</a>
				</td>
				<td style="text-align:center">
					${nfsRchgRecord.payment.paymentNo}
				</td>
				<td style="text-align:center">
					${nfsRchgRecord.payment.thirdPaymentNo}
				</td>
				<td style="text-align:center"><a href="${ctx}/member/query?id=${nfsRchgRecord.member.id}">
					${nfsRchgRecord.member.name}</a>
				</td>
				<td style="text-align:center"><a href="${ctx}/member/query?id=${nfsRchgRecord.member.id}">
					${nfsRchgRecord.member.id}</a>
				</td>
				<td style="text-align:center">
					${nfsRchgRecord.member.username}
				</td>
				<td style="text-align:center">
					${fns:decimalToStr(nfsRchgRecord.amount,2)}
				</td>
				<td style="text-align:center">
					${fns:getDictLabel(nfsRchgRecord.type, "rechargeType", "")}
				</td>
				<td style="text-align:center">
					${nfsRchgRecord.card.cardNo}
				</td>
				<td style="text-align:center">
					${nfsRchgRecord.bankName}
				</td>
				<td style="text-align:center">
					${fns:getDictLabel(nfsRchgRecord.status, "rechargeStatus", "")}
				</td>
				<td style="text-align:center">
					<fmt:formatDate value="${nfsRchgRecord.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td style="text-align:center">
					<span>
						<shiro:hasPermission name="recharge:nfsRchgRecord:edit">
								<a href="${admin}/rchgRecord/successConfirm?id=${nfsRchgRecord.id}">充值成功</a>||
								<a  href="javascript:void(0)"onclick='showQueryThirdOrderDialog("${nfsRchgRecord.id}");'>查询订单</a>
						</shiro:hasPermission>
					</span>
				</td>
				
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>