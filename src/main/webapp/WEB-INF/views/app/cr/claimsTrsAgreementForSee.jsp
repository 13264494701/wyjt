<%@ page contentType="text/html;charset=utf-8" language="java"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%@include file="/WEB-INF/views/app/constant.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <title>债权转让协议</title>
    <%@include file="/WEB-INF/views/app/meta-flex.jsp" %>
    <meta name="keywords" content="无忧借条"/>
    <meta name="description" content="无忧借条"/>
    <link rel="stylesheet" href="${mbStatic}/assets/css/public.css" type="text/css">
    <link rel="stylesheet" href="${mbStatic}/assets/css/helpcenter.css" type="text/css">
    <script src="${mbStatic}/assets/scripts/base/sea.js"></script>
    <script src="${mbStatic}/assets/scripts/base/flexible.js"></script>
</head>
<c:choose>
	<c:when test="${empty appPlatform}">
		<body>	
	</c:when>
	<c:otherwise>
		<body class="${isWeiXin? 'InWeixin':'InWebView'}" data-platform="${appPlatform }"  data-app="51jt">
	</c:otherwise>
</c:choose>
<article class="views">
    <section class="view">
            <style>
                .ques-con p,h2{
                    text-indent: 2em
                }
            </style>
        <div class="pages navbar-fixed ">
            <div class="page">
                <div class="page-content" style="padding-top: 0;">
                    <div class="ques-con" style="position: static">
                        <p style="">
                            本债权转让协议（以下简称“本协议”）由以下各方在北京市朝阳区签订。</p>
                        <p>
                            甲方（以下简称“转让人”）：<strong>${seller.name}</strong>
                        </p>
                        <p>
                            身份证号：<strong>${seller.idNo}</strong>
                        </p>
                        <p>
                            乙方（以下简称“受让人”）：<strong>${buyer.name}</strong>
                        </p>
                        <p>
                            身份证号：<strong>${buyer.idNo}</strong>
                        </p>
                        <p>
                            丙方（以下简称“无忧借条”）：<strong>北京友信宝网络科技有限公司</strong>
                        </p>
                        <p>
                            统一社会信用代码：<strong>${creditCode}</strong>
                        </p>
                        <p>
                            （转让人、受让人和友信宝在本协议中合称为“各方”）
                        </p>
                        <p>
                            就转让人通过友信宝运营管理的无忧借条手机应用程序（以下简称“无忧借条平台”）向受让人转让债权事宜，各方经协商一致，达成如下协议：
                        </p>
                        <h2>
                            第一条 借条债权及转让价款
                        </h2>
                        <p>
                            转让人同意将其在编号：<strong>${loanRecord.loanNo}</strong>《借款协议》（见本协议附件）项下对借款人享有的全部或部分债权（以下简称“借条债权”）转让给受让人，受让人同意受让该借条债权。
                        </p>
                        <p>
                            转让人和受让人均同意放弃知晓对方真实身份信息的权利，且无论是否知晓对方真实身份信息，不影响本协议效力。
                        </p>
                        <p>
                            借条债权具体信息如下：
                        </p>
                        <p>
                            借款人姓名&nbsp;&nbsp;&nbsp;<strong>${loanRecord.loanee.name}</strong>
                        </p>
                        <p>
                            借款利率&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;年化 <strong>${loanRecord.intRate}%</strong>
                        </p>
                        <p>
                            还款方式&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;借款期限届满后一次性偿还本金和利息
                        </p>
                        <p>
                            借款期限&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<strong>${loanRecord.term}</strong>天，自<strong>${createTime}</strong>至<strong>${dueRepayDate}</strong>
                        </p>
                        <p>
                            本协议项下转让的借款本金金额&nbsp;&nbsp;&nbsp;&nbsp;人民币<strong>${fns:decimalToStr(loanRecord.amount,2)}</strong>元，大写:<strong>${amountCHNum}</strong>
                        </p>
                        <p>
                            受让人应收本息之和&nbsp;&nbsp;&nbsp;&nbsp;人民币<strong>${fns:decimalToStr(loanRecord.dueRepayAmount,2)}</strong>元，大写:<strong>${dueRepayCHNum}</strong>
                        </p>
                        <p>
                            转让价格&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;人民币<strong>${fns:decimalToStr(sellPrice,2)}</strong>元，大写:<strong>${sellPriceCHNum}</strong>
                        </p>
                        <h2>第二条 债权转让流程</h2>
                        <p>
                            转让人通过无忧借条平台发出债权转让申请，受让人通过无忧借条平台同意受让该借条债权后，受让人将转让价款存入其无忧借条账户，并授权友信宝将转让价款划转至转让人无忧借条账户，转让价款划转至转让人无忧借条账户即视为借条债权转让成功。
                        </p>
                        <p>
                            转让人与受让人一致同意委托友信宝在转让价款支付完成后将借条债权的转让事项及有关信息通过向借款人无忧借条平台注册手机号码发送短信或平台信息推送的方式通知借款人。短信或平台信息推送一经发出即视为已通知借款人。
                        </p>
                        <p>
                            自借条债权转让成功之日起，受让人取代转让人享有转让人在《借款协议》项下的各项权利。
                        </p>
                        <h2>第三条 违约责任</h2>
                        <p>
                            各方同意，如果一方违反其在本协议中所作的保证、承诺或任何其他义务，致使其他方遭受或发生损害、损失等，违约方须向守约方赔偿守约方因此遭受的一切经济损失。
                        </p>
                        <h2>第四条 适用法律及争议解决</h2>
                        <p>
                            本协议的订立、效力、解释、履行、修改和终止以及争议的解决适用中华人民共和国法律。
                        </p>
                        <p>
                            本协议在履行过程中，如发生任何争执或纠纷，任何一方均应按照《借款协议》的争议解决条款的约定向有管辖权的人民法院提起诉讼。
                        </p>
                        <h2>第五条 其他</h2>
                        <p>
                            本协议附件作为本协议的一部分，与本协议具有同等法律效力。
                        </p>
                        <p>
                            本协议采用电子文本形式制成，经转让人通过无忧借条平台在线发布及受让人在线确认后，在转让价款划付至转让人无忧借条账户之日起生效。各方均认可电子文本形式的协议效力。
                        </p>
                        <p>
                            转让人和受让人均委托友信宝通过其设立的专用服务器保管所有与本协议有关的书面文件和电子信息。本协议任何一方下载打印本协议文本，均不得添加、修改或者涂改任何条款。
                        </p>
                        <p>
                            本协议的任何修改、补充均以无忧借条平台电子文本形式作出。
                        </p>
                        <p>甲方：<strong>${seller.name}</strong></p>
                        <p>乙方：<strong>${buyer.name}</strong></p>
                        <p>丙方：<strong>北京友信宝科技股份有限公司</strong></p>
                        <p>日期：<strong>${endTime}</strong></p>
                        <p>附件：<strong>${loanRecord.loanNo}</strong>借款协议</p>

                    </div>
                </div>
            </div>
        </div>
    </section>
</article>
<script>
    seajs.use(['zepto', 'jumpApp'], function ($) {
    });
</script>
</body>
</html>
<!--created by jozhua 2016/01/27-->