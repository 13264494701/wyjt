<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>会员借贷分析</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">

	</script>
</head>
<body>
<ul class="nav nav-tabs">
    <li><a href="${admin}/member/loanReport?type=7&id=${id}">一周</a></li>
    <li><a href="${admin}/member/loanReport?type=30&id=${id}">一月</a></li>
    <li><a href="${admin}/member/loanReport?type=180&id=${id}">六月</a></li>
    <li><a href="${admin}/member/loanReport?type=6&id=${id}">六月前</a></li>
    <li><a href="${admin}/member/loanReport?type=99999&id=${id}">总计</a></li>
</ul>
<div class="control-group" style="display: flex;flex-direction: row;justify-content: center">
    <label style="text-align:right;width:300px;height: 40px;align-items: center;display: flex">待还金额：<fmt:formatNumber value="${memberLoanReport.pendingReceiveAmt}" type="currency" currencySymbol="￥"/></label>
    <label style="text-align:right;width:300px;height: 40px;align-items: center;display: flex">待还借条数量：${memberLoanReport.pendingReceiveQuantity}</label>
</div>
<div class="control-group" style="display: flex;flex-direction: row;justify-content: center">
    <label style="text-align:right;width:300px;height: 40px;align-items: center;display: flex">待收金额：<fmt:formatNumber value="${memberLoanReport.pendingRepaymentAmt}" type="currency" currencySymbol="￥"/></label>
    <label style="text-align:right;width:300px;height: 40px;align-items: center;display: flex">待收借条数量：${memberLoanReport.pendingRepaymentQuantity}</label>
</div>
<div class="control-group" style="display: flex;flex-direction: row;justify-content: center">
    <label style="text-align:right;width:300px;height: 40px;align-items: center;display: flex">逾期未还金额：<fmt:formatNumber value="${memberLoanReport.overduePendingRepayAmt}" type="currency" currencySymbol="￥"/></label>
    <label style="text-align:right;width:300px;height: 40px;align-items: center;display: flex">逾期未还借条数量：${memberLoanReport.overduePendingRepayQuantity}</label>
</div>
<div class="control-group" style="display: flex;flex-direction: row;justify-content: center">
    <label style="text-align:right;width:300px;height: 40px;align-items: center;display: flex">逾期已还金额：<fmt:formatNumber value="${memberLoanReport.overdueRepayedAmt}" type="currency" currencySymbol="￥"/></label>
    <label style="text-align:right;width:300px;height: 40px;align-items: center;display: flex">逾期已还借条数量：${memberLoanReport.overdueRepayedQuantity}</label>
</div>
<div class="control-group" style="display: flex;flex-direction: row;justify-content: center">
    <label style="text-align:right;width:300px;height: 40px;align-items: center;display: flex">借入金额：<fmt:formatNumber value="${memberLoanReport.loanInAmt}" type="currency" currencySymbol="￥"/></label>
    <label style="text-align:right;width:300px;height: 40px;align-items: center;display: flex">借入借条数量：${memberLoanReport.loanInQuantity}</label>
</div>
<div class="control-group" style="display: flex;flex-direction: row;justify-content: center">
    <label style="text-align:right;width:300px;height: 40px;align-items: center;display: flex">借出金额：<fmt:formatNumber value="${memberLoanReport.loanOutAmt}" type="currency" currencySymbol="￥"/></label>
    <label style="text-align:right;width:300px;height: 40px;align-items: center;display: flex">借出借条数量：${memberLoanReport.loanOutQuantity}</label>
</div>
<div class="control-group" style="display: flex;flex-direction: row;justify-content: center">
    <label style="text-align:right;width:300px;height: 40px;align-items: center;display: flex">按时还款金额：<fmt:formatNumber value="${memberLoanReport.onTimeRepayedAmt}" type="currency" currencySymbol="￥"/></label>
    <label style="text-align:right;width:300px;height: 40px;align-items: center;display: flex">按时还款借条数量：${memberLoanReport.onTimeRepayedQuantity}</label>
</div>
<div class="control-group" style="display: flex;flex-direction: row;justify-content: center">
    <label style="text-align:right;width:300px;height: 40px;align-items: center;display: flex">延期还款金额：<fmt:formatNumber value="${memberLoanReport.delayRepayedAmt}" type="currency" currencySymbol="￥"/></label>
    <label style="text-align:right;width:300px;height: 40px;align-items: center;display: flex">延期还款借条数量：${memberLoanReport.delayRepayedQuantity}</label>
</div>
</body>
</html>