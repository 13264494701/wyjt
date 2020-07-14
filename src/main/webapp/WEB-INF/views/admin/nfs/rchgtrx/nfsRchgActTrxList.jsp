<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>充值流水管理</title>
	<meta name="decorator" content="default"/>
	<script src="${ctxStatic}/artdialog/js/dialog-plus.js"></script>
    <link href="${ctxStatic}/artdialog/css/dialog.css" rel="stylesheet" />
	<script type="text/javascript">
	$(document).ready(function() {
		$("#btnExport").click(function() {
			top.$.jBox.confirm("确认要导出用户充值账户流水数据吗？", "系统提示", function(v, h, f) {
				if (v == "ok") {
					$("#searchForm").attr("action", "${admin}/rchgActTrx/exportRchgActTrxData");
					$("#searchForm").submit();
				}
			}, {
				buttonsFocus : 1
			});
			top.$('.jbox-body .jbox-icon').css('top', '55px');
		});
	});
		function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#searchForm").submit();
        	return false;
        }
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${admin}/rchgActTrx/">充值流水列表</a></li>		
	</ul>
	<form:form id="searchForm" modelAttribute="nfsRchgActTrx" action="${admin}/rchgActTrx/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label style="text-align:right;width:150px">会员编号号：</label>
				<form:input path="member.id" htmlEscape="false" maxlength="20" class="input-medium"/>
			</li>
			<li><label style="text-align:right;width:150px">手机号：</label>
				<form:input path="member.username" htmlEscape="false" maxlength="64" class="input-medium"/>
			</li>
			<li><label style="text-align:right;width:150px">充值方式：</label>
				<form:select path="type"  style="width:177px" class="input-medium">
					<form:option value="" label="请选择"/>
					<form:options items="${fns:getDictList('rchgType')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</li>
			<li><label style="text-align:right;width:150px">充值编号：</label>
				<form:input path="rchgId" htmlEscape="false" maxlength="20" class="input-medium"/>
			</li>
			<li><label style="text-align:right;width:150px">第三方支付订单号：</label>
				<form:input path="thirdPaymentNo" htmlEscape="false" maxlength="64" class="input-medium"/>
			</li>
			<li><label style="text-align:right;width:150px">充值时间：</label>
				<input name="beginTime" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${nfsRchgActTrx.beginTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',startDate:'%y-%M-%d 00:00:00',isShowClear:true});"/> - 
				<input name="endTime" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${nfsRchgActTrx.endTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',startDate:'%y-%M-%d 00:00:00',isShowClear:true});"/>
			</li>
			<li class="btns">
			    <label style="text-align:right;width:50px"></label>
			    <input id="btnSubmit" class="btn btn-primary " style="width:80px;" type="submit" value="查   询"/>
				<label style="text-align:right;width:50px"></label>
				<input id="btnExport" class="btn btn-primary" style="width:80px;" type="button" value="导  出" />
			</li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th style="text-align:center">会员编号</th>
				<th style="text-align:center">会员姓名</th>
				<th style="text-align:center">用户手机号</th>
				<th style="text-align:center">充值金额</th>
				<th style="text-align:center">账户余额</th>
				<th style="text-align:center">充值方式</th>
				<th style="text-align:center">充值编号</th>
				<th style="text-align:center">第三方支付订单号</th>
				<th style="text-align:center">银行卡号</th>
				<th style="text-align:center">银行名称</th>	
                <th style="text-align:center">充值时间</th>	
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="nfsRchgActTrx">
			<tr>
				<td style="text-align:center"><a href="${ctx}/member/query?id=${nfsRchgActTrx.member.id}">
					${nfsRchgActTrx.member.id}
				</a></td>
				<td style="text-align:center">
					${nfsRchgActTrx.member.name}
				</td>
				<td style="text-align:center">
					${nfsRchgActTrx.member.username}
				</td>
				<td style="text-align:center">
					${nfsRchgActTrx.trxAmt}
				</td>
				<td style="text-align:center">
					${nfsRchgActTrx.curBal}
				</td>
				<td style="text-align:center">
					${fns:getDictLabel(nfsRchgActTrx.type, 'rchgType', '')}
				</td>
				<td style="text-align:center">
					${nfsRchgActTrx.rchgId}
				</td>
				<td style="text-align:center">
					${nfsRchgActTrx.thirdPaymentNo}
				</td>
				<td style="text-align:center">
					${nfsRchgActTrx.cardNo}
				</td>
				<td style="text-align:center">
					${nfsRchgActTrx.bankName}
				</td>
				<td style="text-align:center">
					<fmt:formatDate value="${nfsRchgActTrx.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>