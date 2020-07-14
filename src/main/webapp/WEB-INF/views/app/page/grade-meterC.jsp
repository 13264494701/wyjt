<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import="java.util.Calendar" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<%@include file="/WEB-INF/views/app/constant.jsp" %>
<%
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
%>
<!DOCTYPE html>
<html class="hairlines">
<head>
    <title>信用评级-无忧借条</title>
    <%@include file="../meta-flex.jsp" %>
    <link rel="stylesheet" href="${mbStatic}/assets/css/m3-min.css" type="text/css">
    <link rel="stylesheet" href="${mbStatic}/assets/css/creditRating.css" type="text/css">
    <script src="${mbStatic}/assets/scripts/base/sea.js?v=<%=version%>"></script>
    <script src="${mbStatic}/assets/scripts/base/flexible.js?v=<%=version%>"></script>
    <script>(function (b, e) {
        typeof define === "function" && define.cmd ? (define("flex", function (g, d) {
            return b(e, d)
        }), seajs.use("flex")) : document.readyState === "complete" ? b(e, e) : document.addEventListener("DOMContentLoaded", function () {
            b(e, e)
        }, false)
    })(function (b, e) {
        function g() {
            var a = i.getBoundingClientRect(), c = a.width;
            a = a.height;
            if (c > a) c = a;
            c > 640 && (c = 640);
            c /= 7.5;
            i.style.fontSize = c + "px";
            f.rem = d.rem = c
        }

        var d = window, i = d.document.documentElement;
        b = 0;
        var h, k, f = {};
        if (!b) {
            var j = d.navigator.userAgent, l = d.devicePixelRatio;
            (k = (!!j.match(/android/gi), !!j.match(/iphone/gi))) && j.match(/OS 9_3/);
            b = k ? l >= 3 && (!b || b >= 3) ? 3 : l >= 2 && (!b || b >= 2) ? 2 : 1 : 1
        }
        i.setAttribute("data-dpr", b);
        d.addEventListener("resize", function () {
            clearTimeout(h);
            h = setTimeout(g, 300)
        }, false);
        d.addEventListener("pageshow", function (a) {
            if (a.persisted) {
                clearTimeout(h);
                h = setTimeout(g, 300)
            }
        }, false);
        g();
        f.dpr = d.dpr = b;
        f.refreshRem = g;
        f.rem2px = function (a) {
            var c = parseFloat(a) * this.rem;
            if (typeof a === "string" && a.match(/rem$/)) c += "px";
            return c
        };
        f.px2rem = function (a) {
            var c = parseFloat(a) / this.rem;
            if (typeof a === "string" && a.match(/px$/)) c += "rem";
            return c
        };
        e.flex = f
    }, this);
    </script>
    <style>
        .rate-circle {
            position: relative;
            background: white;
            -webkit-border-radius: 100%;
            -moz-border-radius: 100%;
            border-radius: 100%;
            display: block;
            -moz-box-flex: none;
            -webkit-box-flex: none;
            box-flex: none;
            -moz-flex: none;
            -ms-flex: none;
            -webkit-flex: none;
            flex: none;
        }
        .zjcirbox .rate-circle {
            -moz-box-flex: none;
            -webkit-box-flex: none;
            box-flex: none;
            -moz-flex: none;
            -ms-flex: none;
            -webkit-flex: none;
            flex: none;
        }
        .cr-cen img{
            margin:0 auto;
        }
        .rate-circle svg {
            display: block;
            width: 100%;
            height: 100%;
            -webkit-transform: rotate(-90deg);
            transform: rotate(-90deg);
        }
        .circlerotate circle:first-child {
            stroke: #dedede;
        }
        .cr-cen{
            width: 90%;
            position: absolute;
            left: 50%;
            margin-left: -45%;
        }
        .circlerotate circle {
            fill: rgba(255,255,255,0);
            stroke-width: 6;
        }
        .circlerotate circle:last-child {
            stroke-dasharray: 440;
            transform-origin: 50% 50% 0;
            -webkit-transform-origin: 50% 50% 0;
            stroke-dashoffset: 440;
            -moz-transition: all 0.9s ease-out;
            -webkit-transition: all 0.9s ease-out;
            transition: all 0.9s ease-out;
        }
        .zjr {
            position: absolute;
            left: 30%;
            top: 30%;
            overflow: hidden;
            text-align: center;
        }
        .zjr em {
            font-size: 27px;
        }
        .zjr em {
            font-size: 1.5rem;
            color: #e83e3f;
            margin-top: 0.2rem;
            display: block;
            overflow: hidden;
        }
        .zjr i {
            font-size: 14px;
        }
        .zjr i {
            font-size: 13px;
            color: #e83e3f;
            font-style: normal;
        }
        .zjr b {
            font-size: 13px;
        }
        .zjr b {
            color: #e83e3f;
            font-weight: bold;
            display: block;
            font-size: 0.4rem;
            overflow: hidden;
        }
        .cr-cen p{
            bottom: -.5rem;
        }
        .rateB{
            position: absolute;
            left: 30%;
            bottom: 17%;
            color: #F8E4D2;
            font-size: 0.4rem;
        }
        .rateB:before{
            content: '';
            background-color: #F8E4D2;
            width: 4px;
            height: 20px;
            display: block;
            transform: rotate(35deg);
            position: absolute;
            bottom: -56%;
            left: -90%;
        }
        .rateA{
            position: absolute;
            left: 11%;
            top: 42%;
            color:#DC796A;
            font-size:0.4rem;
        }
        .rateA:before{
            content:'';
            background-color:#DC796A;
            width:4px;
            height:20px;
            display: block;
            transform:rotate(-90deg);
            position: absolute;
            top: 18%;
            left: -90%;

        }
        .rateAA{
            position: absolute;
            left: 26%;
            top: 17%;
            color:#DC796A;
            font-size:0.4rem;
        }
        .rateAA:before{
            content: '';
            background-color: #DC796A;
            width: 4px;
            height: 20px;
            display: block;
            transform: rotate(-43deg);
            position: absolute;
            left: 0;
            bottom: 93%;

        }
        .rateAAA{
            position: absolute;
            left: 62%;
            top: 16%;
            color: #DC796A;
            font-size: 0.4rem;
        }
        .rateAAA:before{
            content: '';
            background-color: #DC796A;
            width: 4px;
            height: 20px;
            display: block;
            transform: rotate(32deg);
            position: absolute;
            left: 78%;
            top: -68%;
        }
        .rateAAAA{
            position: absolute;
            right: 11%;
            top: 42%;
            color: #DC796A;
            font-size: 0.4rem;
        }
        .rateAAAA:before{
            content: '';
            background-color: #DC796A;
            width: 4px;
            height: 20px;
            display: block;
            transform: rotate(90deg);
            position: absolute;
            right: -25%;
            bottom: 23%;
        }
        .rateAAAAA{
            position: absolute;
            right: 23%;
            bottom: 18%;
            color: #DC796A;
            font-size: 0.4rem;
        }
        .rateAAAAA:before{
            content: '';
            background-color: #DC796A;
            width: 4px;
            height: 20px;
            display: block;
            transform: rotate(135deg);
            position: absolute;
            right: 8%;
            bottom: -56%;
        }
        .cr-foot{
            z-index:10;
        }
    </style>

</head>
<c:choose>
<c:when test="${empty appPlatform}">
<body>
</c:when>
<c:otherwise>
<body class="${isWeiXin? 'InWeixin':'InWebView'}" data-platform="${appPlatform}"  data-app="51jt">
</c:otherwise>
</c:choose>
<article class="pages docBody" style="max-width: unset">
    <header class="navbar" style="height: 1.03rem;line-height: 1.03rem; background: #E71B1B;">
        <a href="javascript:void 0" class="goBack link" data-method="yxbaoback"><i class="icon icon-back" style="margin-top:0;background-size:.295603rem;height: .54rem;width: .524rem;margin-left: .15rem;"></i></a>
        <h1 style="line-height:1.1rem;font-size: .43rem;width: 90%;right: .35rem;">信用评级</h1>
        <div class="navright" style="line-height: 1.1rem;font-size: .353rem;right: .35rem;"><a href="/app/wyjt/common/upgradeInstructions?appPlatform=${appPlatform}" class="link" id="ruleBtn" style="color: white; ">升级说明</a></div>
    </header>
    <section class="page">
        <div class="page-content">
            <div class="creditRating-top">
                <img src="${mbStatic}/assets/images/yhimg.png" style="margin:0 auto;">
                <div class="toplab">
                    <span>您被列入无忧借条黑名单</span>
                </div>
            </div>
            <div class="creditRating-cen">
                降级容易升级难<br/>请珍惜您的信用
            </div>
            <div class="creditRating-pf" style="margin: .7rem 0 1.18rem" >
                <b>${member.memberRank.rankNo}</b>
                <p>信用很差</p>
                <span>评估时间：<%=sdf.format(Calendar.getInstance().getTime())%></span>
            </div>
            <div class="creditRating-foot">
                请及时呵护您的信用！
            </div>
        </div>
        </div>
        </div>
    </section>
</article>
<script>
    seajs.use(['zepto','jumpApp']);
</script>
</body>
</html>
