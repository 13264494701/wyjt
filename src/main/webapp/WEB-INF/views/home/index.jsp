<%@ page contentType="text/html;charset=utf-8" language="java"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8"/>
    <title>无忧借条—玩转电子借据，好友借钱有保障</title>
    <meta name="keywords" content="无忧借条,电子借条,电子借据,欠条,借条,借据,小额催收"/>
    <meta name="description" content="无忧借条-安全便捷的电子借据工具。具有CA认证、第三方数据存证、200多家公证处认可，可生成规范电子借据的手机应用。该应用为用户提供好友间借款的电子借条生成，专业催收，第三方平台信用借贷，汽车抵押贷款等服务!无忧借条客服热线：400-6688-658。"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"/>
    <meta name="baidu-site-verification" content="9cGlPzhjwm"/> 
    <meta name="decorator" content="default"/>
    <link rel="shortcut icon" href="${homeStatic}/assets/images/favicon.ico"/>
    <link rel="bookmark" href="${homeStatic}/assets/images/favicon.ico"/>
    <meta name="author" content="51jt.com"/>
    <meta name="baidu-site-verification" content="vXjdHXbcqW" />
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"/>
    <link rel="stylesheet" href="${homeStatic}/assets/css/p1.css?v=25" type="text/css">
    <link rel="stylesheet" href="${homeStatic}/assets/css/home/home.css?v=24" type="text/css">
</head>
<body>

<div class="v1-topbg">
    <div class="home">
<%@include file="/WEB-INF/views/home/header.jsp" %>
        <div class="slider-bar">
            <div class="w1 pr clearfix">
                <div class="slider-bar-1">
                    <img src="${homeStatic}/assets/images/home-d1.png">
                </div>
                <div class="slider-bar-2">
                    <img src="${homeStatic}/assets/images/home-d2.png">
                </div>
                <div class="slider-bar-btns clearfix">
                    <p class="bar"><img src="${homeStatic}/assets/images/home-d3.png"></p>
                    <p class="btns">
                        <a href="${iosVersion}" target="_blank" class="ios" id="click1">App Store</a>
                        <a href="${androidVersion}" target="_blank"  class="android" id="click1" >安卓下载</a>
                    </p>
                </div>
            </div>
        </div>
    </div>

    <div class="screen"><p><img src="${homeStatic}/assets/images/home-s1.jpg"></p></div>
    <div class="screen"><p><img src="${homeStatic}/assets/images/home-s2.jpg"></p></div>
    <div class="line"></div>
<%@include file="/WEB-INF/views/home/footer.jsp" %>
</div>
</body>
</html>
