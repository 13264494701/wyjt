<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<%@include file="/WEB-INF/views/app/constant.jsp" %>
<!DOCTYPE html>
<html xmlns=http://www.w3.org/1999/xhtml>
<head>
    <title>更新提示</title>
    <%@include file="../meta-flex.jsp" %>
    <meta name="keywords" content=""/>
    <meta name="description" content=""/>
    <link rel="stylesheet" href="${mbStatic}/assets/css/public.css?v=<%=version%>" type="text/css">
    <link rel="stylesheet" href="${mbStatic}/assets/css/user.css?v=<%=version%>" type="text/css">
    <script src="${mbStatic}/assets/scripts/base/sea.js?v=<%=version%>21"></script>
    <script src="${mbStatic}/assets/scripts/base/flexible.js?v=<%=version%>"></script>
    <style>
        html, body {
            position: relative;
            height: 100%;
        }
        html{
            font-size: 62.5%;
            -webkit-tap-highlight-color: transparent;
            -ms-text-size-adjust: 100%;
            -webkit-text-size-adjust: 100%;
            -webkit-font-smoothing: antialiased;
        }
        head {
            display: none;
        }
        body {
            background-color: #f7f7f8;
            font: 1.2em/1.5 "Segoe UI",Arial,"Microsoft YaHei","Helvetica Neue";
            color: #333;
            height: 100%;
        }
        body, div, dl, dt, dd, ul, ol, li, h1, h2, h3, h4, h5, h6, pre, form, fieldset, input, textarea, p, blockquote, th, td {
            padding: 0;
            margin: 0;
            display: block;
        }
        h1, h2, h3, h4, h5, h6 {
            font-size: 100%;
            font-weight: normal;
        }
        .views {
            overflow: auto;
            max-width: 26.66667rem;
            -webkit-overflow-scrolling: touch;
            margin: 0 auto;
        }
        .views, .view {
            position: relative;
            width: 100%;
            height: 100%;
            z-index: 5000;
        }
        .view {
            overflow: hidden;
            -webkit-box-sizing: border-box;
            -moz-box-sizing: border-box;
            box-sizing: border-box;
        }
        section, article, aside, header, footer, nav, dialog, figure {
            display: block;
        }
        article, aside, footer, header, hgroup, main, nav, section {
            display: block;
        }
        .pages {
            position: relative;
            width: 100%;
            height: 100%;
            overflow: hidden;
            background: #000;
        }
        .page {
            -webkit-box-sizing: border-box;
            -moz-box-sizing: border-box;
            box-sizing: border-box;
            position: absolute;
            left: 0;
            top: 0;
            width: 100%;
            height: 100%;
            background: #fff;
            -webkit-transform: translate3d(0, 0, 0);
            transform: translate3d(0, 0, 0);
        }
        .toolbar-fixed .page-content {
            padding-bottom: 3.66667rem;
        }
        .navbar-fixed .page-content {
            padding-top: 3.66667rem;
        }
        .user-main {
            background-color: #f4f4f4;
        }
        .page-content {
            overflow: auto;
            -webkit-overflow-scrolling: touch;
            -webkit-box-sizing: border-box;
            -moz-box-sizing: border-box;
            box-sizing: border-box;
            height: 100%;
            position: relative;
            z-index: 1;
        }

        h2,p{
            font-size: 1rem;
        }
        h2{
            margin-top: 1rem;
        }
        p{
            line-height: 2rem;
            text-indent: 2em;
        }
        .ending{
            text-align: right;
            padding-right: 1rem;
            margin: 2rem 0;
        }
    </style>
</head>
<c:choose>
<c:when test="${empty appPlatform}">
<body>
</c:when>
<c:otherwise>
<body class="${isWeiXin? 'InWeixin':'InWebView'}" data-platform="${appPlatform}" data-app="51jt">
</c:otherwise>
</c:choose>
<article class="views">
    <section class="view">
        <div class="pages navbar-fixed toolbar-fixed">
            <div class="page">
                <div class="page-content user-main withdraw-main " style="background:RGB(238,242,243);padding: 1rem;padding-top:0rem;">
                    <h2>尊敬的无忧借条用户：</h2>
                    <p>您好！</p>
                    <p>如您想要参加此活动，请将无忧借条更新至最新版本，为此给您带来的不便，敬请谅解！</p>
                    <p>谢谢！</p>
                </div>
            </div>
        </div>
    </section>
</article>
</body>
</html>