<%@page import="com.teamshopping.teamshopping.util.SYSUtil" %>
<%@page contentType="text/html; charset=utf-8" %>
<%@include file="/wap/inc/function.jsp" %>
<%
    String docTitle = "首页";
    isWeiXin = true;
%><!DOCTYPE html>
<html>
<head>
    <title><%=docTitle%>-无忧借条</title>
    <%@include file="/wap/inc/meta.jsp" %>
    <meta name="keywords" content="无忧借条"/>
    <meta name="description" content="无忧借条"/>
    <link rel="stylesheet" href="<%=httpUrl%>assets/css/public.css?v=<%=version%>" type="text/css">
    <link rel="stylesheet" href="<%=httpUrl%>assets/css/v1/wechat.css?v=<%=version%>" type="text/css">
    <script src="<%=httpUrl%>assets/scripts/sea-modules/seajs/2.3.0/sea.js?v=<%=version%>"></script>
    <script src="<%=httpUrl%>assets/scripts/base/flexible.js?v=<%=version%>"></script>
</head>

<body<%=appPlatform.equals("") ? "" : " class=\"" + (isWeiXin ? "InWeixin" : "InWebView") + "\" data-platform=\"" + appPlatform + "\""%>>
<article class="views docBody">
    <section class="view">
        <jsp:include page="../inc/header-public.jsp">
            <jsp:param name="title" value="<%=docTitle%>"></jsp:param>
        </jsp:include>
        <div class="pages navbar-fixed">
            <div class="page">
                <div class="page-content">
                    <div class="index51 clearfix" id="brower">
                        <ul class="u1">
                            <li><p></p></li>
                            <li><p></p></li>
                            <li><p></p></li>
                        </ul>
                        <a href="javascript:void 0" class="card-btn"></a>
                    </div>

                </div>
            </div>
        </div>
    </section>
</article>
<jsp:include page="../inc/footer.jsp"></jsp:include>
<script>
    seajs.use('zepto', function ($) {
        var $container = $('#brower'), $scoller = $container.find('ul'), index = 1, max = $container.find('li').length, $btn = $('.card-btn'), isAnimating = false;
        $scoller.bind($.fx.transitionEnd + " transitionend", function () {
            isAnimating = false;
            if (index == max)     $btn.hide();
            else $btn.show();
        });
        function goTo(inx) {
            var d = index + inx;
            if (d < 1) return;
            if (d > max) return;
            isAnimating = true;
            index = d;
            $scoller.removeClass().addClass('u' + index)
        }

        $(document).swipeUp(function () {
            if (isAnimating) return;
            goTo(1);
        }).swipeDown(function () {
            if (self.isAnimating) return;
            goTo(-1);
        }).bind('touchmove', function (event) {  //禁用手机默认的触屏滚动行为
            event.preventDefault();
        });
        $($btn).bind('tap', function () {
            $(document).trigger('swipeUp');
        })
    });
</script>
</body>
</html>