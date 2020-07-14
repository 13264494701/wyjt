<%@ page contentType="text/html;charset=utf-8" language="java"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%@include file="/WEB-INF/views/app/constant.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <title>大额支付</title>
    <%@include file="/WEB-INF/views/app/meta-flex.jsp" %>
    <meta name="keywords" content="无忧借条"/>
    <meta name="description" content="无忧借条"/>
    <link rel="stylesheet" href="${mbStatic}/assets/css/public.css" type="text/css">
    <link rel="stylesheet" href="${mbStatic}/assets/css/helpcenter.css" type="text/css">
    <script src="${mbStatic}/assets/scripts/sea-modules/seajs/2.3.0/sea.js"></script>
    <script src="${mbStatic}/assets/scripts/base/flexible.js"></script>
    <style>
        .ques-con .red {
            color: Red;
        }

        .ques-con p, h3, h4 {
            font-size: 1rem;
            line-height: 2.3rem;
            color: RGB(134, 135, 139)
        }

        .ques-con p span {
            color: RGB(9, 9, 9)
        }

        p {
            font-size: 1rem;
        }

        p img {
            width: 2rem;
            margin-right: .4rem;
        }

        div p i {
            margin: 0 1rem;
        }

        p a {
            color: RGB(104, 130, 226);
        }
    </style>
</head>
<%-- <body<%=appPlatform.equals("") ? "" : " class=\"" + (isWeiXin ? "InWeixin" : "InWebView") + "\" data-platform=\"" + appPlatform + "\""%>> --%>
<body>
<article class="views">
    <section class="view">
        <div class="pages navbar-fixed ">
            <div class="page">
                <div class="page-content" style="padding-top:0; background: RGB(237,237,237)">
                    <div class="ques-con" style="position: static">
                        <h4 style="color: RGB(168,168,168);margin-bottom: 10px;">银行账户信息</h4>
                        <div style="background: white; padding: 10px;border-radius:10px;-moz-border-radius:10px; /* Old Firefox */">
                            <p>账&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;号：<span>1028 5000 0014 6002 0</span></p>
                            <p style="font-size: 1.022456rem;">户&nbsp;&nbsp;
                                &nbsp;&nbsp;&nbsp;名：<span>北京友信宝网络科技有限公司</span>
                                <img src="${mbStatic}/assets/images/debt/right.png"
                                     style="display: inline-block;width: 5%;margin-left:.5rem;margin-bottom: 3px;"><span
                                        style="color: RGB(168,168,168);margin-left:2px;">已认证</span></p>
                            <p style="font-size: 1.022456rem;">开&nbsp;户&nbsp;行：<span>华夏银行北京分行上地支行</span></p>
                        </div>
                        <br>
                        <h4>充值说明：</h4>
                        <p>1.大额充值，请您向我司账户打款时备注上您的姓名手机以及汇款说明；</p>
                        <p class="red" style="font-size: 1rem;">（具体格式如下：姓名+手机号+无忧借条大额充值）</p>
                        <p>2.汇款成功后请第一时间<span style="color: RGB(104, 130, 226)">致电我司客服</span>或<span
                                style="color: RGB(104, 130, 226)">联系在线客服</span>给您账户加款；</p>
                        <p class="red" style="font-size: 1rem;">（客服加款时间：10：00 - 19：00）</p>
                    </div>
                    <div style="position: absolute;bottom: 2%;left: 19%; color: RGB(140,141,143);">
                        <p><img src="${mbStatic}/assets/images/debt/serviceTel-icon@2x.png"><a
                                href="tel:4006688658">客服电话</a><i>|</i><img
                                src="${mbStatic}/assets/images/debt/questions-icon@2x.png" style="width: 1.6rem;"><a href="javascript:void 0;" class="item-link" data-href="${pageContext.request.contextPath}/app/wyjt/common/rechargeHelp?title=cztx">充值失败解答</a>
                        </p>
                    </div>
                </div>
            </div>
        </div>
    </section>
</article>
<section class="panel" id="infoPanel" style="width: 100%">
    <div class="page-view theme-red">
        <div class="navbar">
            <div class="navbar-inner">
                <div class="left" style="margin-right:2.7rem;"><a class="link close-popup" href="javascript:void 0" target="_self"><i
                        class="icon icon-back"></i><span></span></a></div>
                <div class="center"><h3 style="font-size: 1.4rem;color: white;">充值失败解答</h3></div>
                <div class="right"><a href="javascript:" class="link icon-only"></a></div>
            </div>
        </div>
        <div class="page navbar-fixed">
            <div class="page-content">
                <div class="page-center-middle" style="height: 100%">
                    <div class="preloader"></div>
                </div>
            </div>
        </div>
    </div>
</section>
<script>
    seajs.use(['zepto', 'jumpApp','panel'], function ($,panel) {
        var ev =  $.support.touch ? 'tap' : 'click';
        $('.item-link').live(ev, function () {
            location.href = "${pageContext.request.contextPath}/app/wyjt/common/rechargeHelp?title=cztx";
        });
    });
</script>
</body>
</html>
<!--created by jozhua 2016/01/27-->