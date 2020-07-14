
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/wap/inc/function.jsp" %>
<%
    cdnUrlJS = "//image.51jt.com/wap/";
%>
<!DOCTYPE html>
<html>
<head>
    <title>我要借款</title>
    <%@include file="/wap/inc/meta-flex.jsp" %>
    <meta name="keywords" content=""/>
    <meta name="description" content=""/>
    <link rel="stylesheet" href="<%=httpUrl%>assets/css/public.css?v=<%=version%>" type="text/css">
    <link rel="stylesheet" href="<%=httpUrl%>assets/css/user.css?v=<%=version%>" type="text/css">
    <script src="<%=cdnUrlJS%>assets/scripts/base/sea.js?v=<%=version%>21"></script>
    <script src="<%=cdnUrlJS%>assets/scripts/base/flexible.js?v=<%=version%>"></script>
    <style type="text/css">
        .pop-gray.pop-dialogs .pop-btn {
            /*background: #eeedf2;*/
            /*color: #999;*/
        }

        .pop-info {
            margin-top: 1rem;
        }

    </style>
</head>
<body<%=appPlatform.equals("") ? "" : " class=\"" + (isWeiXin ? "InWeixin" : "InWebView") + "\" data-platform=\"" + appPlatform + "\" data-app=\"" + appName + "\""%>>

<div class="pop-overlay pop-overlay-visible" style="z-index: 11111; width: 100%; height:100%;"></div>
<div id="pop-8162-87ac" style="z-index:11112" class="pop-layout">
    <div class="pop-dialogs pop-gray pop-in" style="height: auto;">
        <div class="pop-inner">
            <div class="pop-bd">
                <div class="pop-msg">
                    <h2 style="font-size: 1.2rem;font-weight:bold;">声明</h2>
                    <p class="pop-info" style="text-align: left;">本服务由第三方深圳前海微贷提供，请谨慎操作，本公司不提供放贷业务和承担相关任何责任！特此告知!</p>
                </div>
            </div>
        </div>
        <div class="pop-ft">
            <span class="pop-btn" data-i="0">同意并继续</span>
            <span class="pop-btn" data-i="1">再想想</span>
        </div>
    </div>
</div>

<script type="text/javascript">
    seajs.use('zepto',function ($) {
        var ev = $.support.touch ? 'tap' : 'click';
        var btns=$('.pop-btn');
        btns.eq(0)[ev](function () {
            location.href='otherLoan.jsp';
        });
        btns.eq(1)[ev](function () {
            window.opener=null;
            window.open('','_self');
            window.close();
            history.back();
        });

    });
</script>
</body>
</html>
