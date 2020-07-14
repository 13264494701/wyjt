<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<%@include file="/WEB-INF/views/app/constant.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <title>找专业出借人-无忧借条</title>
    <%@include file="/WEB-INF/views/include/head.jsp" %>
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <link rel="stylesheet" href="${mbStatic}/assets/css/public.css?v=<%=version%>" type="text/css">
    <link rel="stylesheet" href="${mbStatic}/assets/css/user.css?v=<%=version%>" type="text/css">
    <script src="${mbStatic}/assets/scripts/base/sea.js?v=<%=version%>"></script>
    <script src="${mbStatic}/assets/scripts/base/flexible.js?v=<%=version%>"></script>
    <style type="text/css">
        .pop-dialogs {
            width: 17rem !important;
        }

        .pop-gray.pop-dialogs .pop-btn {
            background: #eeedf2;
            color: #999;
        }

        .pop-info {
            margin-top: 1rem;
        }

        .pop-label {
            color: #5d5d5d;
            font-size: 1rem;
            margin-top: 1rem;
        }

        .pop-label input {
            width: 1rem;
            height: 1rem;
            vertical-align: -0.1667rem;
        }

        .radius {
            border-radius: .5rem;
            margin: .2rem 0;
            font-size: 1.1rem;
            background: white;
        }

        .radius p {
            border-bottom: 1px RGB(226, 226, 226) solid;
            margin-left: 1rem;
        }

        .radius p span {
            display: inline-block;
            padding: .765rem 0rem;
            height: 2rem;
            line-height: 2rem;
            color: RGB(37, 37, 37);
        }

        .radius p :first-child {
            width: 30%;
            text-align: left;
        }

        .radius p :last-child {
            width: 70%;
        }

        p a {
            width: 66% !important;
        }

        .recharge-but {
            margin-top: 1.5rem;
            margin-bottom: 1.5rem;
        }

        .disabled {
            pointer-events: none;
            cursor: default;
            opacity: 0.2;
            background-color: #666;
        }

        .removeDis {
            background-color: #E71B1B !important;
        }

        .radius input, select {
            border: none;
            height: 2rem;
        }

        .infoTitle {
            height: 2rem;
            text-align: left;
            padding: 1rem 1rem;
            font-size: 1.1rem;
            background-size: .3rem;
            border-bottom: 1px solid RGB(223, 223, 223);
            color: rgb(128, 128, 128);
        }

        .labCheck {
            width: 63% !important;
            display: inline-block !important;
            background: url("${mbStatic}/assets/images/debt/agree_on@2x.png") 0rem 1.1rem no-repeat;
            background-size: 7%;
            padding-right: 0 !important;
            padding-left: 1.5rem !important;
            height: 2rem !important;
            line-height: 2rem !important;
        }

        .chekOn {
            background: url("${mbStatic}/assets/images/debt/agree_off@2x.png") 0rem 1.1rem no-repeat;
            background-size: 8%;
        }
    </style>
</head>
<body>
<article class="views">
    <section class="view">
        <div class="pages navbar-fixed toolbar-fixed">
            <div class="page">
                <form method="post" id="fid" action="/app/wyjt/member/addLoanerLoan">
                    <input type="hidden" name="_header" value="${memberToken}">
                    <input type="hidden" name="_type" value="h5">
                    <div class="page-content user-main withdraw-main" style="background:white;padding-top: 0;">
                        <h2 class="infoTitle">申请信息</h2>
                        <div class="radius" style="">
                            <p>
                                <span>芝麻信用分：</span><span>
                                <input type="number" value="" name="zhimafen" placeholder="请输入您的芝麻信用分" maxlength="19">
                                </span>
                            </p>
                            <p>
                                <span>联&nbsp;&nbsp;系&nbsp;&nbsp;Q&nbsp;Q：</span><span>
                                    <input type="text" value="" name="qqNo" placeholder="请输入您的QQ号码"
                                           maxlength="19">
                                </span>
                            </p>
                            <p>
                                <span>微&nbsp;信&nbsp;账&nbsp;号：</span><span>
                                    <input type="text" value="" name="weixinNo" placeholder="请输入您的微信号"
                                           maxlength="19">
                                </span>
                            </p>
                            <p style="height: 3.5rem;font-size: 13px;">
                                <span class="labCheck">
                                    <i>&nbsp;我已阅读并同意</i>
                                </span>
                                <a style="color:RGB(66,106,249);margin-left: -36%" href="javascript:void (0);"
                                   data-href="/mb/cont/lendersPermitAgreement.html"
                                   data-title="用户使用许可协议"
                                   class="item-link">《用户使用许可协议》</a>
                                与
                                <a class="item-link" style="color: RGB(66,106,249)"
                                   href="javascript:void (0);"
                                   data-href="/mb/cont/lendersPrivacyAgreement.html"
                                   data-title="隐私协议">《隐私协议》</a>
                            </p>
                            <input type="hidden" value="${loanerId}" name="loanerId">
                        </div>
                        <a href="javascript:void 0" class="recharge-but  removeDis " id="conformWithdraw">
                            <span>下一步</span>
                        </a>
                    </div>
                </form>
            </div>
        </div>
    </section>
</article>
<section class="panel" id="infoPanel" style="width: 100%">
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
                <div class="page-center-middle" style="height: 100%">
                    <div class="preloader"></div>
                </div>
            </div>
        </div>
    </div>
</section>

<script type="text/javascript">
    window.panel = null;
    define('bindBand', ['zepto', 'pop', 'panel', 'jumpApp'], function (require, exports, module) {
        var $ = require('zepto'), pop = require('pop'), call = require('jumpApp'), panel = require('panel');
        var ev = $.support.touch ? 'tap' : 'click', $btn = $('#conformWithdraw'), $labCheck = $('.labCheck'),
            $sibTtile = $('#sibTtile');

        $btn[ev](function () {
            var str = check();
            if (str) return !!$.toast(str);
            $.confirm('是否确认提交该申请？', function () {
                $("#fid").submit();
            });
        });
        $labCheck[ev](function (e) {
            var that = $(this), tagName = e.target.tagName;
            if (tagName == 'SPAN')
                that.hasClass('chekOn') ? (that.removeClass('chekOn'), $btn.removeClass('disabled').addClass('removeDis')) : (that.addClass('chekOn'), $btn.removeClass('removeDis').addClass('disabled'));
        });
        $('.item-link').live(ev, function () {
            var _this = $(this), src = _this.attr('data-href'), title = _this.attr('data-title');
            location.href = src;
            return false;
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
            return false;
        });

        function check() {
            var str = '', $userZM = $('input[name="zhimafen"]'), $userQQCode = $('input[name="qqNo"]'),
                $userWxCode = $('input[name="weixinNo"]');
            if (!$.trim($userWxCode.val())) str = '请输入微信账号';
            if (!$.trim($userQQCode.val())) str = '请输入QQ账号';
            if (!$.trim($userZM.val())) str = '请输入芝麻信用分';

            return str;
        }
    });
    seajs.use(['bindBand']);
</script>
</body>
</html>