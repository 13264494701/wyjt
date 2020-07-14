<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<html>
<head>
    <meta name="decorator" content="default"/>
    <title>充值</title>
</head>
<body>
<form id="payForm" action="https://cashier.lianlianpay.com/payment/bankgateway.htm" method="post" target="_top">
    <input type="hidden" name="version" value="${lianLianPay.version}">
    <input type="hidden" name="oid_partner" value="${lianLianPay.oid_partner}">
    <input type="hidden" name="user_id" value="${lianLianPay.user_id}">
    <input type="hidden" name="sign_type" value="${lianLianPay.sign_type}">
    <input type="hidden" name="sign" value="${lianLianPay.sign}">
    <input type="hidden" name="busi_partner" value="${lianLianPay.busi_partner}">
    <input type="hidden" name="no_order" value="${lianLianPay.no_order}">
    <input type="hidden" name="dt_order" value="${lianLianPay.dt_order}">
    <input type="hidden" name="name_goods" value="${lianLianPay.name_goods}">
    <input type="hidden" name="info_order" value="${lianLianPay.info_order}">
    <input type="hidden" name="money_order" value="${lianLianPay.money_order}">
    <input type="hidden" name="notify_url" value="${lianLianPay.notify_url}">
    <input type="hidden" name="url_return" value="${lianLianPay.url_return}">
    <input type="hidden" name="userreq_ip" value="${lianLianPay.userreq_ip}">
    <input type="hidden" name="url_order" value="${lianLianPay.url_order}">
    <input type="hidden" name="valid_order" value="${lianLianPay.valid_order}">
    <input type="hidden" name="risk_item" value='${lianLianPay.risk_item}'>
    <input type="hidden" name="timestamp" value="${lianLianPay.timestamp}">
</form>
<script language="JavaScript" type="text/javascript">
    $(document).ready(function() {
        document.getElementById("payForm").submit();
    });
</script>
</body>
</html>