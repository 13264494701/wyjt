<%@page contentType="text/html; charset=utf-8" %>
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
<html>
<head>
    <title>index</title>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"/>
    <meta name="apple-mobile-web-app-capable" content="yes"/>
    <meta name="apple-mobile-web-app-status-bar-style" content="black"/>
    <meta name="format-detection" content="telephone=no"/>
    <meta name="viewport"
          content="width=device-width, target-densitydpi=high-dpi, initial-scale=1.0, maximum-scale=1.0, user-scalable=no"/>
    <meta name="keywords" content=""/>
    <meta name="description" content=""/>
    <link rel="stylesheet" href="${mbStatic}/assets/css/v1/m1.css?v=<%=version%>" type="text/css">
    <link rel="stylesheet" href="${mbStatic}/assets/css/v1/wechat.css?v=<%=version%>" type="text/css">
    <script src="${mbStatic}/assets/scripts/base/sea.js?v=<%=version%>21"></script>
    <script src="${mbStatic}/assets/scripts/base/flexible.js?v=<%=version%>"></script>
</head>
<body>
<article class="views docBody">
    <section class="view">
        <div class="pages">
            <div class="page">
                <div class="page-content ">
                    <div class="xz51">
                        <ul>
                            <li class=""></li>
                            <li class="img"></li>
                            <li class="txt"> 无忧借条<span>安装</span></li>
                            <li class="rig" id="openbtn">打开</li>
                        </ul>
                    </div>
                    <div class="xz51-but"><a href="javascript:void 0" id="downbtn" style="display: block">下载应用</a></div>
                    <div class="clearfix xz51-contxt">
                        <h1>详情介绍</h1>

                        <div class="introduce xz51-conimg clearfix">
                            <ul>
                                <li></li>
                                <li></li>
                                <li></li>
                                <li></li>
                            </ul>
                        </div>
                    </div>
                    <div class="clearfix xz51-contxt">
                        <h1>内容摘要</h1>

                        <div class="con">
                            <p>
                                友情靠诚信呵护，社会靠诚信进步！你讲你的仗义，我可让你安心无忧。好友借贷神器，可以帮您催款的电子借条--无忧借条app横空出世，多好的朋友借钱也要用无忧借条打借条！</p>


                            【应用介绍】<br/>

                            <p> 无忧借条是一款随手可用的电子借条手机应用，作为电子借条类工具，为用户提供经过CA认证、第三方数字存证、200多家公证处认可的规范电子借条。</p>

                            【特色服务】<br/>

                            1.致力于做合法有效电子借条，在线办理，方便快捷；<br/>

                            2.智能、有趣借钱场景，避免求人尴尬；<br/>

                            3.7*24小时实时到账、最快速度解决资金周转的困境！<br/>
                            
                            4.友情提醒还款服务，不再头疼如何要账?<br/>
                            
                            5.逾期催收，法律仲裁，全方位保障权益。
                             【联系我们】<br/>
                                                                      微信关注订阅号：无忧借条官网<br/>   
                                                                      服务号：无忧借条服务<br/>
                                                                      客服电话：400-6688-658<br/>   
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </section>
</article>
</body>
<%
    String jump, download;
    if (appType.equals("ios")) {
        jump = "utrust://";
        download = "/app/wyjt/common/download";
    } else {
        jump = "m://www.51jt.com";
        download = "/app/wyjt/common/download";
    }
%>
<script>
    seajs.use('zepto', function ($) {
        (function () {
            var config = {appSrc: '<%=jump%>', downloadSrc: '<%=download%>', timeout: 600};
            var ev = $.support.touch ? 'tap' : 'click';

            function download() {
                location.href = '<%=download%>';
            }

            $('#openbtn')[ev](function () {
                openclient();
            });
            $('#downbtn')[ev](function () {
                download();
            });
            var $top = $('.guide-top');
            $('.close')[ev](function () {
                $top.addClass('h')
            });

            function openclient() {
                var startTime = Date.now();
                var ifr = document.createElement('iframe');
                ifr.src = config.appSrc;
                ifr.style.display = 'none';
                document.body.appendChild(ifr);
                var t = setTimeout(function () {
                    var endTime = Date.now();
                    if (!startTime || endTime - startTime < config.timeout + 200) {
                        window.location = config.downloadSrc;
                    } else {
                    }
                }, config.timeout);
                $(window).on('blur', function () {
                    clearTimeout(t);
                });
            }

           // openclient();
        })()
    })
</script>
<script> var _hmt = _hmt || [];
(function () {
    var hm = document.createElement("script");
    hm.src = "//hm.baidu.com/hm.js?cf846b436f7853dc01e509ff521e6640";
    var s = document.getElementsByTagName("script")[0];
    s.parentNode.insertBefore(hm, s);
})(); </script>
</html>