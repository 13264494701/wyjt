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
            <div class="howcharge">
                <c:if test="${str=='hyjk' }">
                <p class="firstp"><a href="javascript:;" class="hyjk">好友借款</a></p>
                <p><span id="w1"> 如何向好友借款？ </span>
                    首页点击找好友借款，然后设置借款信息和还款设置（必填项：借款金额、还款时间），即可发送借款申请给好友，好友同意并支付后，即借款成功。
                </p>
                <p><span id="w2"> 如何还款？ </span>
                    首页点击借条中心，然后点击我欠谁钱，选择要还款的借条，在我的借条页点击我要还款，支付成功即可。若还款方式为全额还款，可选择线下还款也可选择申请延期，但需注意：线下还款需好友确认后才算还款成功；一笔借款只能延期一次，一次默认延期1个月。
                </p>

                <p><span id="w3"> 跟好友借款有利息吗？ </span>
                    跟好友借款可以先和好友协商好利息，如果不好意思开口，可适当加些利息，更容易完成借款。
                </p>
                <p><span id="w4"> 如果好友不还款怎么办？ </span>
                    在无忧借条内完成的借款，无忧借条有专业的催款模式，同时无忧借条还可以为用户提供具有法律效益的电子借条（登录无忧借条官网：www.51jt.com，在“业务服务-下载借条”即可下载）。所以为了保障自己的利益，借款请尽量在无忧借条线上完成。
                </p>
                <p><span id="w5"> 催款都有哪些方式？ </span>
                    目前网站有短信提醒、站内消息提醒、人工电话提醒等催款方式。
                </p>
                <p><span id="w6"> 逾期未还款有什么影响？ </span>
                    逾期还款会影响个人信用，严重者会被拉入黑名单，还会影响您日后的借款。
                </p>
                </c:if>
                <c:if test="${str=='csfw' }">
                <p class="firstp"><a href="javascript:;" class="csfw">催收服务</a></p>
                <p>
                    <span id="w1">如何申请催收服务？</span>
                </p>
                <p>1.非本站借条催收。</p>
                <p>先注册无忧借条APP，点击催收服务，点击申请催收，填写相关信息并提交审核。无忧借条催收工作人员会在2个工作日内反馈审核结果。</p>
                <p>2.本站借条催收</p>
                <p>通过无忧借条APP借款的放款人（债权人）点击我的借条“催收服务”按钮，仔细阅读催收服务协议并核对确认。无忧借条催收工作人员会在2个工作日内反馈审核结果。</p>
                <p>3.查看催收进度</p>
                <p>无忧借条客户端首页点击催收服务，点击右上角图标，进入我的催收，可查看催收进度。</p>

                <p>
                    <span id="w2">债务人如何还款？</span>
                    债务人从首页进入催收服务页，点击“我要还款”按钮，汇款到指定账号并上传汇款凭证，汇款成功后联系客服核对确认。
                </p>
                </c:if>
                <c:if test="${str=='sfrz' }">
                <p class="firstp"><a href="javascript:;" class="sfrz">身份认证</a></p>
                <p>
                    <span id="w1">如何进行身份认证？</span>
                </p>
                <p>注册登录后，点击“个人中心”，进入“安全中心”里点击身份认证，输入真实姓名及身份证号码等相关信息即可完成身份认证。</p>
                <p class="red">（注：认证信息必须是真实有效的，以免耽误您在无忧借条平台的使用）</p>
                <p>
                    <span id="w2">如何进行学生认证？</span>
                </p>
                <p>注册登录后，点击“个人中心”，进入“安全中心”里点击学生认证，输入真实姓名、身份证号码以及学校等相关信息即可完成学生认证。</p>
                <p class="red">（注：认证信息必须是真实有效的，以免耽误您在无忧借条平台的使用）</p>
                </c:if>
                <c:if test="${str=='zhzc' }">
                <p class="firstp"><a href="javascript:;" class="zhzc">账号注册</a></p>
                <p>
                    <span id="w1">如何注册？</span>
                </p>
                <p>首先需要您下载无忧借条客户端，下载后打开客户端，点击“免费注册”填写手机号码、密码等相关信息即可完成注册。</p>
				</c:if>
				<c:if test="${str=='cjwt' }">
                <p class="firstp"><a href="javascript:;" class="cjwt">常见问题</a></p>
                <p>
                    <span id="w1">忘记密码怎么找回？</span>
                    首先打开登录页面，在登录页面上点击忘记密码，输入手机号码进行验证，然后重新修改新的密码即可。
                </p>
                <p>
                    <span id="w2">如何修改密码？</span>
                    登录无忧借条客户端后，首页点击“个人中心”，进入“安全中心”里面进行设置，可进行修改手势密码与登录密码。
                </p>
                </c:if>
                <c:if test="${str=='aqbz' }">
                <p class="firstp"><a href="javascript:;" class="aqbz">安全保障</a></p>
                <p>
                    <span id="w1">用无忧借条借款安全吗？</span>
                    无忧借条平台提供经过CA认证、第三方数字存证、200多家公证处认可的规范电子借条,保障电子借条的安全性、真实性、 合法合规性。可作为有效的电子证据提交给法院。 </p>
                </c:if>
            </div>
        </div>
    </div>
<%@include file="/WEB-INF/views/home/footer.jsp" %>
</div>
</body>
</html>
<!-- created in 2016-04-12 16:49-->