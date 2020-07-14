<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<%@include file="/WEB-INF/views/app/constant.jsp" %>
<!DOCTYPE html>
<html xmlns=http://www.w3.org/1999/xhtml>
<head>
    <title>安全登录提醒</title>
    <%@include file="../meta-flex.jsp" %>
    <meta name="keywords" content=""/>
    <meta name="description" content=""/>
    <link rel="stylesheet" href="${mbStatic}/assets/css/public.css?v=<%=version%>" type="text/css">
    <link rel="stylesheet" href="${mbStatic}/assets/css/user.css?v=<%=version%>" type="text/css">
    <script src="${mbStatic}/assets/scripts/base/sea.js?v=<%=version%>21"></script>
    <script src="${mbStatic}/assets/scripts/base/flexible.js?v=<%=version%>"></script>
    <style>
        .radius {
            position: absolute;
            width: 20rem;
            height: 20rem;
            background: white;
            border: 1px solid #dadada;
            border-radius: .5rem;
            top: 50%;
            left: 50%;
            margin-left: -10rem;
            margin-top: -10rem;
        }

        h1 {
            height: 2rem;
            line-height: 2rem;
            text-align: center;
            font-size: 1.2rem;
            margin: .5rem;
        }

        p {
            font-size: 1rem;
            height: 2rem;
            line-height: 2rem;
            padding-left: 1rem;
        }

        .radius p:nth-child(2) {
            margin-bottom: 1rem;
            font-size: 1.1rem;
        }

        .radius p:nth-child(6) {
            color: red;
            margin-top: 1rem;
        }

        .borders {
            border-bottom: 1px solid #dadada;
            height: 1rem;
        }

        .radius p:nth-child(8) {
            font-size: 1.2rem;
            font-weight: bold;
            text-align: center;
            height: 4rem;
            line-height: 4rem;
            color: rgb(32, 129, 246);
            letter-spacing: .2rem;
            padding-left: 0;
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
                <div class="page-content user-main withdraw-main" style="padding-top: 0;background:#c1bebe">
                    <div class="radius">
                        <h1>安全提示</h1>
                        <p>您的账户已在其他设备登录</p>
                        <p>登陆地点：${loginIp}</p>
                        <p>登陆设备：${loginDevice}</p>
                        <p>登陆时间：${loginTime}</p>
                        <p>如非本人操作，请及时修改登陆密码</p>
                        <p class="borders"></p>
                        <p class="reBtn">重新登录</p>
                    </div>
                </div>
            </div>
        </div>
    </section>
</article>
<script>
    seajs.use(['zepto', 'jumpApp'], function ($, jumpApp) {
        var ev = $.support.touch ? 'tap' : 'click';
        $('.reBtn')[ev](function () {
            backIndex();
        });

        function backIndex() {
            jumpApp.callApp('backIndexPage');
        }
    });
</script>
</body>
</html>