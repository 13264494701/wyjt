<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<%@include file="/WEB-INF/views/app/constant.jsp" %>
<%
    String agent = request.getHeader("user-agent");
    if (agent == null) agent = "";
    agent = agent.toUpperCase();
    String appType = "";
    if (agent.contains("IPHONE")) appType = "ios";
    else if (agent.contains("ANDROID")) appType = "android";
%>
<!DOCTYPE html>
<html xmlns=http://www.w3.org/1999/xhtml>
<head><title>无忧借条APP跳转</title>
    <%@include file="../meta-flex.jsp" %>
    <meta name="keywords" content="无忧借条"/>
    <meta name="description" content="无忧借条"/>
    <link rel="stylesheet" href="${mbStatic}/assets/css/public.css?v=<%=version%>" type="text/css">
    <script src="${mbStatic}/assets/scripts/base/sea.js?v=<%=version%>"></script>
    <script src="${mbStatic}/assets/scripts/base/flexible.js?v=<%=version%>"></script>
    <style type="text/css"> .page-content {
        background: #f7f8f4;
    }

    .guide-download-ios {
        position: absolute;
        width: 100%;
        height: 100%;
        background-image: url(/wap/assets/images/app-download@1x.jpg);
        background-image: -webkit-image-set(url(/wap/assets/images/app-download@1x.jpg) 1x, url(/wap/assets/images/app-download@1x.jpg) 2x, url(/wap/assets/images/app-download@2x.jpg) 3x);
        background-repeat: no-repeat;
        background-position: right top;
        background-size: 101%;
    }

    .guide-download-android {
        position: absolute;
        width: 100%;
        height: 100%;
        background-image: url(/wap/assets/images/android-download@1x.jpg);
        background-image: -webkit-image-set(url(/wap/assets/images/android-download@1x.jpg) 1x, url(/wap/assets/images/android-download@1x.jpg) 2x, url(/wap/assets/images/android-download@2x.jpg) 3x);
        background-repeat: no-repeat;
        background-position: right top;
        background-size: 104%;
    }

    .yxb-guidebody {
        background-color: #d91d3d;
        height: 100%;
        overflow: hidden;
    }

    .yxb-guide .guide-top {
        display: -webkit-box; /* Chrome 4+, Safari 3.1, iOS Safari 3.2+ */
        display: -moz-box; /* Firefox 17- */
        display: -webkit-flex; /* Chrome 21+, Safari 6.1+, iOS Safari 7+, Opera 15/16 */
        display: -moz-flex; /* Firefox 18+ */
        display: -ms-flexbox; /* IE 10 */
        display: flex;
        height: 5.41667rem;
        padding: 0.83333rem;
        padding-left: 0;
        background: #ffffff;
        transition: height .5s linear;
        overflow: hidden
    }

    .yxb-guide .guide-top .close {
        background: url("data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABUAAAAUCAMAAABVlYYBAAAAWlBMVEUAAAB3d3d3d3d3d3d3d3d3d3d3d3d3d3d3d3d3d3d3d3d3d3d3d3d3d3d3d3d3d3d3d3d3d3d3d3d3d3d3d3d3d3d3d3d3d3d3d3d3d3d3d3d3d3d3d3d3d3cyECA3AAAAHXRSTlMA+RM/OtZ1DnpGNQaN5eDS0JlPLBkJ7MCxiGldIi/ViiMAAACSSURBVBjTXdDdDsIgDAXgdkUR2eY25799/9eU2iw92bkg5MsJUIiouxKkyo0sTxXQk77ammdVFkDV90q0cOMCePjaLgUbXrIXZOOjNTuiYJVA4HHmQGg7Ys5saANgPla945R+u4XTHh/LABzvTOyMaFc6x5iGwLlvODk6a6l/nbKbfxVLbZt+xInK4AfnlSDVKj8aawyEb2C+PAAAAABJRU5ErkJggg==") center center no-repeat;
        background-size: 50%;
        width: 2.5rem;
        height: 100%;
        display: inline-block;
    }

    .yxb-guide .guide-top.h {
        height: 0;
        padding: 0
    }

    .yxb-guide .guide-top .logo {
        background: url("/wap/assets/images/yxb-guide/logo.png?") left center no-repeat;
        background-size: 100%;
        width: 5.58333rem;
        height: 5.41667rem;
        display: inline-block;
        margin-right: 0.83333rem;
    }

    .yxb-guide .guide-top .txt {
        -moz-box-flex: 1;
        -webkit-box-flex: 1;
        box-flex: 1;
        -moz-flex: 1;
        -ms-flex: 1;
        -webkit-flex: 1;
        flex: 1;
        font-size: 1.3333333333rem;
        color: #363636;
        margin: 0.83333rem 1.25rem 0 0;
    }

    .yxb-guide .guide-top .txt span {
        display: block;
        margin-top: 0.41667rem;
        font-size: 1.1666666666667rem;
        color: #363636;
    }

    .yxb-guide .guide-top .open {
        font-size: 1.5rem;
        line-height: 5.41667rem;
        color: #d91d3d;
        width: 4.16666666667rem;
        text-align: center;
    }

    .yxb-guide .guide-con {
        margin-top: 4.08333rem;
        padding-bottom: 1.66667rem;
    }

    .yxb-guide .guide-con .yxb {
        background: url("/wap/assets/images/yxb-guide/yxb.png?") center center no-repeat;
        background-size: 100%;
        width: 18.75rem;
        height: 6.6667rem;
        display: block;
        margin: 0 auto;
    }

    .yxb-guide .guide-con p {
        font-size: 1.33333rem;
        color: #ffc0cb;
        text-align: center;
        margin: 3.25rem;
        height: 2.5rem;
    }

    .yxb-guide .guide-con a {
        margin: 0 2.25rem 1.66667rem;
        font-size: 1.5rem;
        display: block;
        text-align: center;
        height: 4.16667rem;
        line-height: 4.16667rem;
        -webkit-border-radius: 10px;
        -moz-border-radius: 10px;
        border-radius: 10px;
    }

    .yxb-guide .guide-con a.login {
        color: #ffffff;
        border: 2px solid #c81231; /* Fallback for sad browsers */
        background-color: #ff8400; /* Mozilla Firefox */
        background-image: -moz-linear-gradient(#fda03c, #ff8400); /* Opera */
        background-image: -o-linear-gradient(#fda03c, #ff8400); /* WebKit (Chrome 11+) */
        background-image: -webkit-gradient(linear, left top, left bottom, color-stop(0, #fda03c), color-stop(1, #ff8400)); /* WebKit (Safari 5.1+, Chrome 10+) */
        background-image: -webkit-linear-gradient(#fda03c, #ff8400); /* IE10 */
        background-image: -ms-linear-gradient(#fda03c, #ff8400); /* W3C */
        background-image: linear-gradient(#fda03c, #ff8400);
        -moz-box-shadow: inset 0px 1px 2px white;
        -webkit-box-shadow: inset 0px 1px 2px white;
        box-shadow: inset 0px 1px 2px white;
    }

    .yxb-guide .guide-con a.download {
        color: #484848;
        border: 1px solid #c81231; /* Fallback for sad browsers */
        background-color: #e7e0e0; /* Mozilla Firefox */
        background-image: -moz-linear-gradient(#f6f1f1, #e7e0e0); /* Opera */
        background-image: -o-linear-gradient(#f6f1f1, #e7e0e0); /* WebKit (Chrome 11+) */
        background-image: -webkit-gradient(linear, left top, left bottom, color-stop(0, #f6f1f1), color-stop(1, #e7e0e0)); /* WebKit (Safari 5.1+, Chrome 10+) */
        background-image: -webkit-linear-gradient(#f6f1f1, #e7e0e0); /* IE10 */
        background-image: -ms-linear-gradient(#f6f1f1, #e7e0e0); /* W3C */
        background-image: linear-gradient(#f6f1f1, #e7e0e0);
    } </style>
</head>
<body data-platform="51jt">
<article class="views docBody">
    <section class="view">
        <div class="pages">
            <div class="page">
                <div class="page-content">
                    <div class="yxb-guidebody">
                        <div class="yxb-guide">
                            <div class="guide-top"><a href="javascript:void 0" class="close"></a> <em class="logo"></em>
                                <a href="javascript:void 0" class="txt downbtn"> 无忧借条<span>安装 </span> </a> <a
                                        href="javascript:void 0" class="open openbtn">打开</a></div>
                            <div class="guide-con"><em class="yxb"></em>
                                <p>友情靠诚信呵护，社会靠诚信进步</p> <a href="javascript:void 0" class="login downbtn">下载应用</a> <a
                                        href="javascript:void 0" class="download hide">下载应用</a></div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </section>
</article>
<%
    String download;
    if (appType.equals("ios")) download = "/app/wyjt/common/download";
    else download = "/app/wyjt/common/download"; %>
<script> seajs.use(['$', 'jumpApp'], function ($, jumpApp) {
    (function () {
        var ev = $.support.touch ? 'tap' : 'click';

        function download() {
            var a = new Date;
            location.href = '<%=download%>';
            <%if(appType.equals("ios")){%>
            if (new Date() - a < 2) {
                $.alert('哎呀，连接itunes官网出错，请检查网络设置！')
            }
            <%}%>
        }

        /* got();*/
        $('.openbtn')[ev](function () {
            openclient();
        });
        $('.downbtn')[ev](function () {
            download();
        });
        var $top = $('.guide-top');
        $('.close')[ev](function () {
            $top.addClass('h')
        });

        function openclient() {
            jumpApp.gotoApp();
        }

        openclient();
    })()
}) </script>

<script> var _hmt = _hmt || [];
(function () {
    var hm = document.createElement("script");
    hm.src = "//hm.baidu.com/hm.js?cf846b436f7853dc01e509ff521e6640";
    var s = document.getElementsByTagName("script")[0];
    s.parentNode.insertBefore(hm, s);
})(); </script>
</body>
</html>