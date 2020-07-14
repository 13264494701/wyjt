<%@ page contentType="text/html;charset=utf-8" language="java"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8"/>
    <title>下载-无忧借条</title>
    <meta name="keywords" content="无忧借条"/>
    <meta name="description" content="无忧借条"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"/>
    <link rel="stylesheet" href="${homeStatic}/assets/css/p1.css?v=25" type="text/css">
    <link rel="stylesheet" href="${homeStatic}/assets/css/home/download.css?v=24" type="text/css">
</head>
<body>

<div class="v1-topbg">
    <div class="download">
 <%@include file="/WEB-INF/views/home/header.jsp" %>
        <div class="slider-bar">
            <div class="w1 pr clearfix">
                <div class="slider-bar-1">
                    <img src="${homeStatic}/assets/images/down-s1.png?216">
                </div>
                <div class="slider-bar-2">
                    <img src="${homeStatic}/assets/images/down-s2.png?61">
                    <p>可以帮您催款的电子借条！
                        <br>
                        已有<i>2437925</i>人在用

                    </p>
                </div>
                <!-- /app/go4ios.jsp
                /app/go4android.jsp -->
                <div class="slider-bar-btns clearfix">
                    <a href="${iosVersion}" target="_blank"  class="ios" id="click">App Store</a>
                    <a href="${androidVersion}" target="_blank"  class="android"  id="click1">安卓下载</a>
                </div>
            </div>
        </div>
    </div>

    <div class="screen"><p><img src="${homeStatic}/assets/images/down-d1.jpg?6"></p></div>
    <div class="screen odds"><p><img src="${homeStatic}/assets/images/down-d2.jpg?6"></p></div>
    <div class="screen"><p><img src="${homeStatic}/assets/images/down-d3.jpg?6"></p>
    </div>
    <div class="screen odds"><p><img src="${homeStatic}/assets/images/down-d4.jpg?2"></p>
    </div>
    <div class="screen"><p><img src="${homeStatic}/assets/images/down-d5.jpg"></p></div>
    <div class="line"></div>
 <%@include file="/WEB-INF/views/home/footer.jsp" %>
</div>
</body>
</html>
<!-- created in 2016-04-12 16:49-->