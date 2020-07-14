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
        .pop-overlay.pop-overlay-visible {
            visibility: visible;
            opacity: 1;
        }

        .pop-overlay, .modal-overlay {
            position: absolute;
            left: 0;
            top: 0;
            width: 100%;
            height: 100%;
            background-color: rgba(0, 0, 0, 0.4);
            opacity: 0;
            z-index: 10600;
            visibility: hidden;
            -webkit-transition-duration: 400ms;
            transition-duration: 400ms;
            -webkit-transition-property: -webkit-transform, opacity;
            -moz-transition-property: -moz-transform, opacity;
            -o-transition-property: -o-transform, opacity;
            transition-property: transform, opacity;
        }

        .pop-layout {
            -webkit-box-sizing: border-box;
            -moz-box-sizing: border-box;
            box-sizing: border-box;
            display: -webkit-box;
            display: -ms-flexbox;
            display: -webkit-flex;
            display: flex;
            -webkit-box-pack: center;
            -ms-flex-pack: center;
            -webkit-justify-content: center;
            justify-content: center;
            -webkit-box-align: center;
            -ms-flex-align: center;
            -webkit-align-items: center;
            align-items: center;
            bottom: 0;
            left: 0;
            position: absolute;
            right: 0;
            top: 0;
            z-index: 10700;
        }

        .pop-dialogs {
            width: 21.66667rem;
            overflow: hidden;
            border-radius: 7px;
            -webkit-border-radius: 7px;
            visibility: hidden;
        }

        [data-dpr="3"] .pop-dialogs {
            width: 22rem;
        }

        .pop-dialogs.pop-in {
            visibility: visible;
            opacity: 1;
            -webkit-transition-duration: 400ms;
            transition-duration: 400ms;
            -webkit-transform: translate3d(0, 0, 0) scale(1);
            transform: translate3d(0, 0, 0) scale(1);
        }

        .pop-dialogs .pop-inner {
            padding: 1.25rem;
            border-radius: 7px 7px 0 0;
            position: relative;
            background: #F8F8F8;
        }

        .pop-dialogs .pop-bd {
            min-height: 2.5rem;
            overflow: hidden;
        }

        .pop-dialogs .pop-msg {
            font-size: 1.16667rem;
            line-height: 1.5;
            text-align: center;
            word-break: break-all;
        }

        .pop-dialogs .pop-ft {
            height: 3.66667rem;
            overflow: hidden;
            display: -webkit-box;
            display: -ms-flexbox;
            display: -webkit-flex;
            display: flex;
            -webkit-box-pack: center;
            -ms-flex-pack: center;
            -webkit-justify-content: center;
            justify-content: center;
        }

        .pop-dialogs .pop-btn {
            width: 100%;
            padding: 0 5px;
            height: 3.66667rem;
            font-size: 1.33333rem;
            line-height: 3.66667rem;
            text-align: center;
            color: #007aff;
            background: #F8F8F8;
            display: block;
            position: relative;
            white-space: nowrap;
            text-overflow: ellipsis;
            overflow: hidden;
            cursor: pointer;
            -webkit-box-sizing: border-box;
            -moz-box-sizing: border-box;
            box-sizing: border-box;
            -moz-box-flex: 1;
            -webkit-box-flex: 1;
            box-flex: 1;
            -moz-flex: 1;
            -ms-flex: 1;
            -webkit-flex: 1;
            flex: 1;
        }

        .pop-dialogs .pop-btn:first-child {
            border-radius: 0 0 0 7px;
			color:gray;
        }

        .pop-dialogs .pop-btn:last-child {
            border-radius: 0 0 7px 0;
            color: red;
        }

        .tipContent {
            text-align: center;
            margin: 1rem;
            font-size: 1rem;
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
                <div class="page-content user-main withdraw-main" style="padding-top: 0;background:#f9f2e5">
                    <div class="pop-overlay pop-overlay-visible"
                     style="z-index: 11111; width: 100%; height: 2436px;"></div>
					<div id="pop-b5df-2881" style="z-index:11112" class="pop-layout">
						<div class="pop-dialogs  pop-in" style="height: auto;">
							<div class="pop-inner">
								<div class="pop-bd">
									<div class="pop-msg">提示</div>
									<p class="tipContent">当前账户尚未登录</p>
								</div>
							</div>
							<div class="pop-ft"><span class="pop-btn grayBtn" data-i="0">取消</span><span class="pop-btn reBtn" data-i="1">去登录</span>
							</div>
						</div>
					</div>
                </div>
            </div>
        </div>
    </section>
</article>
<script>
    seajs.use(['zepto', 'jumpApp'], function ($, jumpApp) {
        var ev = $.support.touch ? 'tap' : 'click';
		//取消--返回首页
		$('.grayBtn')[ev](function () {
			jumpApp.callApp('backIndexPage');
        });
		//登录--跳往登录页面
        $('.reBtn')[ev](function () {
			jumpApp.callApp('backLoginPage');
        });
    });
</script>
</body>
</html>