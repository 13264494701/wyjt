<%@ page contentType="text/html;charset=utf-8" language="java"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8"/>
    <title>帮助中心-无忧借条</title>
    <meta name="keywords" content="无忧借条"/>
    <meta name="description" content="无忧借条"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"/>
    <link rel="stylesheet" href="${homeStatic}/assets/css/p1.css?v=25" type="text/css">
    <link rel="stylesheet" href="${homeStatic}/assets/css/home/helpcenter.css?v=24" type="text/css">
</head>
<body>
<div class="v1-topbg">
<%@include file="/WEB-INF/views/home/header.jsp" %>
    <div class="charge-bg">
        <div class="w1 charge clearfix ">
            <div class="charge-lnav">
                <ul>
                    <li><a href="javascript:;" class="h-charge">充值提现</a><b></b></li>
                    <li><a href="javascript:;" class="hyjk">好友借款</a><b></b></li>
                    <li><a href="javascript:;" class="csfw">催收服务</a><b></b></li>
                    <li><a href="javascript:;" class="sfrz">身份认证</a><b></b></li>
                    <li><a href="javascript:;" class="zhzc">账号注册</a><b></b></li>
                    <li><a href="javascript:;" class="cjwt">常见问题</a><b></b></li>
                    <li><a href="javascript:;" class="aqbz">安全保障</a><b></b></li>
                </ul>
            </div>
            <div class="charge-r">
                <c:if test="${str=='hyjk' }">
                <p><span>好友借款</span>
                    <a href="${home}/help_con1_cont?v=hyjk" data-to="w1">1. 如何向好友借款？</a>
                    <a href="${home}/help_con1_cont?v=hyjk" data-to="w2"> 2. 如何还款？</a>
                    <a href="${home}/help_con1_cont?v=hyjk" data-to="w3"> 3. 跟好友借款有利息吗？</a>
                    <a href="${home}/help_con1_cont?v=hyjk" data-to="w4">4. 如果好友不还款怎么办？</a>
                    <a href="${home}/help_con1_cont?v=hyjk" data-to="w5"> 5. 催款都有哪些方式？</a>
                    <a href="${home}/help_con1_cont?v=hyjk" data-to="w6"> 6. 逾期未还款有什么影响？</a></p>
               </c:if>
               <c:if test="${str=='csfw' }">
                <p><span>催收服务</span>
                    <a href="${home}/help_con1_cont?v=csfw" data-to="w1">1. 如何申请催收服务？</a>
                    <a href="${home}/help_con1_cont?v=csfw" data-to="w2"> 2. 债务人如何还款？</a></p>
               </c:if>
               <c:if test="${str=='sfrz' }">
                <p><span>身份认证</span>
                    <a href="${home}/help_con1_cont?v=sfrz" data-to="w1">1. 如何进行身份认证？</a>
                    <a href="${home}/help_con1_cont?v=sfrz" data-to="w2"> 2. 如何进行学生认证？</a></p>
               </c:if>
               <c:if test="${str=='zhzc' }">
                <p><span>账号注册</span>
                    <a href="${home}/help_con1_cont?v=zhzc" data-to="w1">1. 如何注册？</a></p>
                </c:if>
                <c:if test="${str=='cjwt' }">
                <p><span>常见问题</span>
                    <a href="${home}/help_con1_cont?v=cjwt" data-to="w1">1. 忘记密码怎么找回？</a>
                    <a href="${home}/help_con1_cont?v=cjwt" data-to="w2">2. 如何修改密码？</a>
                </p>
                </c:if>
                <c:if test="${str=='aqbz' }">
                <p><span>安全保障</span>
                    <a href="${home}/help_con1_cont?v=aqbz" data-to="w1">1. 用无忧借条借款安全吗？</a></p>
                </c:if>
            </div>
        </div>
    </div>
<%@include file="/WEB-INF/views/home/footer.jsp" %>
</div>
</body>
</html>
<!-- created in 2016-04-12 16:49-->