<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>流量订单管理</title>
	<meta name="decorator" content="default"/>
	<script src="${ctxStatic}/artdialog/js/dialog-plus.js"></script>
    <link href="${ctxStatic}/artdialog/css/dialog.css" rel="stylesheet" />
	<script src="${ctxStatic}/artDialog-5.0.3/jquery.artDialog.min.js"></script>
	<script src="${ctxStatic}/artDialog-5.0.3/artDialog.plugins.min.js"></script>
    <link href="${ctxStatic}/artDialog-5.0.3/skins/blue.css" rel="stylesheet" />
	<script type="text/javascript">
		$(document).ready(function() {

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
		<li class="active"><a href="${admin}/ufangLoaneeRcOrder/">黑名单列表</a></li>
	</ul>	
	<form:form id="searchForm" modelAttribute="ufangLoaneeRcOrder" action="${admin}/ufangLoaneeRcOrder/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">

	        <li><label style="text-align:right;width:100px">查询时间：</label>
					<input name="beginTime" type="text"  maxlength="20" class="input-medium Wdate required"
						value="<fmt:formatDate value="${ufangLoaneeRcOrder.beginTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"
						onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',startDate:'%y-%M-%d 00:00:00',isShowClear:true});"/>
					<label style="text-align:center;width:30px">至：</label>
					<input name="endTime" type="text"  maxlength="20" class="input-medium Wdate required"
						value="<fmt:formatDate value="${ufangLoaneeRcOrder.endTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"
						onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',startDate:'%y-%M-%d 00:00:00',isShowClear:true});"/>
			</li>
	        <li class="btns">
				<label style="text-align:right;width:50px"></label>
				<input id="btnSubmit" class="btn btn-primary " style="width:80px;" type="submit" value="查   询"/>

			</li>
	        <li class="clearfix"></li>
       </ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th style="text-align:center">查询人</th>
				<th style="text-align:center">被查询人姓名</th>
				<th style="text-align:center">被查询人手机号码</th>
				<th style="text-align:center">被查询人身份证号码</th>
				<th style="text-align:center">逾期信息</th>
				<th style="text-align:center">查询价格</th>
				<th style="text-align:center">查询时间</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="ufangLoaneeRcOrder">
			<tr id="${ufangLoaneeRcOrder.id}">
				<td style="text-align:center">
					${ufangLoaneeRcOrder.user.empNam}
				</td>
				<td style="text-align:center">
						${ufangLoaneeRcOrder.qName}
				</td>
				<td style="text-align:center">
						${ufangLoaneeRcOrder.qPhoneNo}
				</td>
				<td style="text-align:center">
						${ufangLoaneeRcOrder.qIdNo}
				</td>
				<td style="text-align:center">
						逾期笔数:${fns:parseObject(ufangLoaneeRcOrder.data).overDueCnt}<br>
						逾期金额:${fns:parseObject(ufangLoaneeRcOrder.data).overDueAmt}
				</td>
				<td style="text-align:center">
						${fns:decimalToStr(ufangLoaneeRcOrder.price,2)}
				</td>
				<td style="text-align:center">
					<fmt:formatDate value="${ufangLoaneeRcOrder.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>

			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>