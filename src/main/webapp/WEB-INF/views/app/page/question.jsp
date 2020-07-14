<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<%@ include file="/WEB-INF/views/app/constant.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <title>常见问题</title>
    <%@include file="../meta-flex.jsp" %>
    <meta name="keywords" content="无忧借条"/>
    <meta name="description" content="无忧借条"/>
    <link rel="stylesheet" href="${mbStatic}/assets/css/public.css" type="text/css">
    <link rel="stylesheet" href="${mbStatic}/assets/css/helpcenter.css" type="text/css">
    <script src="${mbStatic}/assets/scripts/base/sea.js"></script>
    <script src="${mbStatic}/assets/scripts/base/flexible.js"></script>
    <style>
        .list-block ul:before, .list-block ul:after {
            height: 0;
        }

        .list-block .item-inner:after {
            height: 0;
        }

        .list-block .item-link .item-inner {
            border-bottom: 1px solid #E0E0E0;
        }

        .list-block {
            font-size: 1.133rem;
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
<article class="views">
    <section class="view">
        <jsp:include page="../header-public.jsp">
            <jsp:param name="title" value="常见问题"></jsp:param>
            <jsp:param name="url" value="back2app"></jsp:param>
        </jsp:include>
        <div class="pages navbar-fixed ">
            <div class="page">
                <div class="page-content">
                    <!--content -->
                    <div class="list-block" style="margin: 0;" id="serviceList">
                        <ul style="border-bottom: 1px solid #E0E0E0;">
                            <li><a data-href="/app/wyjt/common/answer?parm=cztx&appPlatform=${appPlatform}" class="item-link" data-no="aa">
                                <div class="item-content">
                                    <div class="item-inner">
                                        <div class="item-title">关于充值提现
                                        </div>
                                    </div>
                                </div>
                            </a></li>

                            <li><a data-href="/app/wyjt/common/answer?parm=hyjk" class="item-link" data-no="bzq">
                                <div class="item-content">
                                    <div class="item-inner">
                                        <div class="item-title">关于好友借款
                                        </div>
                                    </div>
                                </div>
                            </a></li>

                            <li><a data-href="/app/wyjt/common/answer?parm=csfw" class="item-link" data-no="xydt">
                                <div class="item-content">
                                    <div class="item-inner">
                                        <div class="item-title">关于催收服务
                                        </div>
                                    </div>
                                </div>
                            </a></li>

                            <li><a data-href="/mb/app/aboutArbitration.html" class="item-link"
                                   data-no="xfq">
                                <div class="item-content">
                                    <div class="item-inner">
                                        <div class="item-title">关于仲裁服务
                                        </div>
                                    </div>
                                </div>
                            </a></li>

                            <li><a data-href="/app/wyjt/common/answer?parm=zhzc" class="item-link" data-no="xfq">
                                <div class="item-content">
                                    <div class="item-inner">
                                        <div class="item-title">关于账号注册
                                        </div>
                                    </div>
                                </div>
                            </a></li>

                            <li><a data-href="/app/wyjt/common/answer?parm=sfrz" class="item-link" data-no="yjyh">
                                <div class="item-content">
                                    <div class="item-inner">
                                        <div class="item-title">关于身份认证
                                        </div>
                                    </div>
                                </div>
                            </a></li>

                            <li><a data-href="/app/wyjt/common/answer?parm=aqbz" class="item-link" data-no="tx">
                                <div class="item-content">
                                    <div class="item-inner">
                                        <div class="item-title">关于安全保障
                                        </div>
                                    </div>
                                </div>
                            </a></li>

                            <li><a data-href="/app/wyjt/common/answer?parm=cjwt" class="item-link" data-no="xms">
                                <div class="item-content">
                                    <div class="item-inner" style="border-bottom: 0px solid #E0E0E0;">
                                        <div class="item-title">关于账号密码
                                        </div>
                                    </div>
                                </div>
                            </a></li>

                        </ul>
                    </div>
                </div>
            </div>
        </div>
    </section>
</article>
<style>
    .page-content iframe {
        height: 100% !important;
    }
</style>

<section class="panel" id="infoPanel" style="width: 100%;">
    <div class="page-view theme-red">
        <div class="navbar">
            <div class="navbar-inner">
                <div class="left"><a class="link close-popup" href="javascript:void 0" target="_self"><i
                        class="icon icon-back"></i><span></span></a></div>
                <div class="center"><h3 id="sibTtile"></h3></div>
                <div class="right"><a href="javascript:" class="link icon-only"></a></div>
            </div>
        </div>
        <div class="page navbar-fixed">
            <div class="page-content">
                <div class="page-center-middle" style="height: auto;">
                    <div class="preloader"></div>
                </div>
            </div>
        </div>
    </div>
</section>
<style type="text/css">
    iframe {
        pointer-events: none;
    }
</style>
<script>
    define('openUrl', ['panel', 'zepto', 'jumpApp'], function (require, exports, module) {
        var $ = require('zepto'), panel = require('panel'), call = require('jumpApp');
        var ev = $.support.touch ? 'tap' : 'click', $sibTtile = $('#sibTtile');

        $('.item-link').live(ev, function () {
            var _this = $(this), src = _this.attr('data-href'), title = _this.find('.item-title').html();
            $sibTtile.html(title);
            var panel = $('#infoPanel').Panel({
                direction: 'right',
                src: src,
                callback: function () {
                    if (!this.isOpen) {
                        $(this.el).find('.page-content').empty()
                    }
                }
            });
            panel.open();
        });
    });
    seajs.use(['openUrl', 'jumpApp']);
</script>
</body>
</html>