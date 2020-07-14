<%@ page contentType="text/html;charset=UTF-8" %>
<%@include file="/WEB-INF/views/app/constant.jsp" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <title>我要借款</title>
    <%@include file="/WEB-INF/views/include/head.jsp" %>
    <link rel="stylesheet" href="${mbStatic}/assets/css/public.css?v=<%=version%>" type="text/css">
    <link rel="stylesheet" href="${mbStatic}/assets/css/user.css?v=<%=version%>" type="text/css">
    <script src="${mbStatic}/assets/scripts/base/sea.js?v=<%=version%>"></script>
    <script src="${mbStatic}/assets/scripts/base/flexible.js?v=<%=version%>"></script>
    <style type="text/css">
        .pop-gray.pop-dialogs .pop-btn {

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
            width: 25%;
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

        .radius .rightTo {
            background: url("${mbStatic}/assets/images/debt/toRight@2x.png") 24rem 1.24rem no-repeat;
            background-size: .6rem;
        }

        .remark {
            padding: 0 1rem 0 2.5rem;
            margin: 0;
            font-size: 1rem;
            color: RGB(239, 52, 44);
        }

        .remarkCon {
            color: rgb(131, 131, 131);
        }

        .notice {
            background: url("${mbStatic}/assets/images/debt/prompt@2x.png") 1rem 0.24rem no-repeat;
            background-size: 1.1rem;
        }

        .infoTitle {
            height: 2rem;
            text-align: left;
            padding: 1rem 2rem;
            font-size: 1.2rem;
            font-weight: bold;
            background: url("${mbStatic}/assets/images/debt/columu@2x.png") 1rem no-repeat;
            background-size: .3rem;
            border-bottom: 1px solid RGB(223, 223, 223);
        }

        .labCheck {
            width: 62% !important;
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
                <form id="form" method="post" action="/wx/wyjt/loanee/add">
                    <input type="hidden" name="_header" value="${memberToken}">
                    <input type="hidden" name="_type" value="h5">
                    <div class="page-content user-main withdraw-main" style="background:white;padding-top: 0;">
                        <h2 class="infoTitle" style="color: black">申请信息</h2>
                        <div class="radius" style="">
                            <p>
                                <span>真实姓名：</span>
                                <span>
                                    <input type="text" value="" name="userName" placeholder="请输入您的姓名" maxlength="19"
                                           style="font-size: 1.1rem">
                                </span>
                            </p>
                            <p>
                                <span>真实年龄：</span>
                                <span>
                                    <input type="number" value="" name="userAge" placeholder="请输入您的年龄" maxlength="19"
                                           style="font-size: 1.1rem">
                                </span>
                            </p>
                            <p>
                                <span>手机号码：</span>
                                <span>
                                    <input type="number" value="" name="userTel"
                                           placeholder="请输入您的手机号码" maxlength="19"
                                           style="font-size: 1.1rem">
                                </span>
                            </p>
                            <p>
                                <span>芝麻信用：</span>
                                <span>
                                    <input type="number" value="" name="userZM"
                                           placeholder="请输入您的芝麻信用分" maxlength="19"
                                           style="font-size: 1.1rem">
                                </span>
                            </p>
                            <p>
                                <span>联系QQ：</span>
                                <span>
                                <input type="text" value="" name="userQQCode"
                                       placeholder="请输入您的QQ号码" maxlength="19"
                                       style="font-size: 1.1rem">
                                </span>
                            </p>
                            <p>
                                <span>微信账号</span>
                                <span>
                                    <input type="text" value="" name="userWxCode"
                                           placeholder="请输入您的微信账号" maxlength="19"
                                           style="font-size: 1.1rem">
                                </span>
                            </p>
                            <p style="height: 3.5rem;font-size: 0.9rem;">
                                <span class="labCheck"><i>&nbsp;我已阅读并同意</i>
                                    <a style="width: 57%!important;color:RGB(66,106,249)" href="javascript:void (0);"
                                       data-href="/mb/cont/junyunRegisterAgreement.html" data-title="君云借注册协议"
                                       class="item-link">《君云借注册协议》</a>
                                </span>
                                <i style="display: inline-block;height: 2rem;line-height: 2rem;margin-top: .7rem;">与</i>
                                <span class="zcType item-link" style="float: right;color: RGB(66,106,249);width: 25%;"
                                      data-href="/mb/cont/privacyAgreement.html" data-title="隐私协议">
                                    《隐私协议》
                                </span>
                            </p>
                        </div>
                        <a href="javascript:void 0" class="recharge-but  removeDis " id="conformWithdraw">
                            <span>下一步</span> </a>
                        <input type="hidden" name="action" value="addLoan">
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
                <div class="center"><h3 id="sibTtile" style="color: white"></h3></div>
                <div class="right"><a href="javascript:" class=""></a></div>
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
    define('otherLoan', ['zepto', 'pop', 'panel', 'jumpApp'], function (require, exports, module) {
        var $ = require('zepto'), pop = require('pop'), call = require('jumpApp'), panel = require('panel'), md5;
        var ev = $.support.touch ? 'tap' : 'click', $btn = $('#conformWithdraw'), $sibTtile = $('#sibTtile');

        $btn[ev](function () {
            var str = check(), $form = $('#form');
            if (str) {
                $.toast(str);
                return false;
            }
            $.confirm('<h2 style="font-size: 1.2rem;font-weight:bold;">声明</h2><p class="pop-info" style="text-align: left;">本服务由第三方杭州君云借提供，请谨慎操作，本公司不提供放贷业务和承担相关任何责任！特此告知!</p>', {
                callback: function () {
                    $form.submit();
                },
                btn: ['再想想', '同意并继续'],
                cls: 'withdraw-pass-dialog'
            });
        });
        var $labCheck = $('.labCheck'), $sub = $('.immePay');
        $labCheck[ev](function (e) {
            var that = $(this), tagName = e.target.tagName;
            if (tagName == 'SPAN')
                that.hasClass('chekOn') ? (that.removeClass('chekOn'), $btn.removeClass('disabled').addClass('removeDis')) : (that.addClass('chekOn'), $btn.removeClass('removeDis').addClass('disabled'));
        });

        function check() {
            var str = '', $userName = $('input[name="userName"]'), $userAge = $('input[name="userAge"]'),
                $userTel = $('input[name="userTel"]'), $userZM = $('input[name="userZM"]'),
                $userQQCode = $('input[name="userQQCode"]'), $userWxCode = $('input[name="userWxCode"]');
            if (!$.trim($userWxCode.val())) str = '请输入微信账号';
            if (!$.trim($userQQCode.val())) str = '请输入QQ账号';
            if (!$.trim($userZM.val())) str = '请输入芝麻信用分';
            if (!$.trim($userTel.val())) str = '请输入您的手机';
            else if (!/^((\+?[0-9]{1,4})|(\(\+86\)))?(13[0-9]|14[57]|15[012356789]|17[012356789]|18[0-9])\d{8}$/.test($.trim($userTel.val()))) {
                str = '请输入正确的手机号码';
            }
            if (!$.trim($userAge.val())) str = '请输入您的年龄';
            if (!$.trim($userName.val())) str = '请输入您的姓名';

            return str;
        }

        function openPannl($obj) {
            var _this = $obj, src = _this.attr('data-href'), title = _this.attr('data-title');
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
        }

        $('.item-link').live(ev, function () {
            openPannl($(this));
        });

    });
    seajs.use(['otherLoan']);
</script>
</body>
</html>
