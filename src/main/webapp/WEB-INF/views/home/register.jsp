<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.feili.friendcredit.entity.InitProperties" %>
<%@ page import="com.feili.friendcredit.entity.TResultSet" %>
<%@ page import="com.feili.friendcredit.service.UserManager" %>
<%@ page import="com.feili.friendcredit.util.CookieUtil" %>
<%@ page import="com.teamshopping.teamshopping.util.AppContext" %>
<%@ page import="com.teamshopping.teamshopping.util.SYSUtil" %>
<%@include file="/common/function.jsp" %>
<%

    // 请求操作类型 signup->注册 signin->登陆 resetPwd->忘记密码 getCaptcha->获取验证码 checkCaptcha->校验验证码
    String action = SYSUtil.getParameter(request, "action", "");
    String thirdId = SYSUtil.getParameter(request, "thirdId", "");
    // 用户名
    String username = SYSUtil.getParameter(request, "username", "");
    // 密码（密文）
    String pwd = SYSUtil.getParameter(request, "pwd", "");
    // 验证码
    String captcha = SYSUtil.getParameter(request, "captcha", "");
    // 验证码类型 1->注册; 2->重置密码
    int ccType = SYSUtil.getIntParameter(request, "ccType", 0);

    // 跳转地址
    String url = SYSUtil.getParameter(request, "retUrl", "http://yxinbao.com");

    // wap注册 phoneType和channelId为固定值
    String phoneType = "wap";
    int channelId = 9001;

    String tempId = CookieUtil.getCookieByName(request, "yxinbaoChannelId");
    System.out.println("~~~~~~^[regist.jsp:32]^~~~~~~~~~~~~~~tempId = (" + tempId + ")~~~~~~~~~~~~~~~~~~~~~");
    if (tempId == null) tempId = "";
    if (!"".equals(thirdId) && !"0".equals(thirdId)) {
        channelId = Integer.parseInt(thirdId);
    }
    UserManager userManager = (UserManager) AppContext.getInstance().getAppContext().getBean("UserManager");

    TResultSet resultSet = new TResultSet(-99, "服务器忙,请稍后请求.");
    String strAlert = "";
    // 注册
    if ("signup".equals(action)) {
        try {
            resultSet = userManager.userReg(username, captcha, pwd, phoneType, channelId);
            if (InitProperties.ErrCode.ID_OPERATE_SUCCESS.equals(resultSet.getErrCode())) {
                resultSet.setResString("恭喜您，注册成功~！");
                strAlert = "恭喜您，注册成功~！";
            } else {
                strAlert = resultSet.getErrString();
            }
        } catch (Exception e) {
            System.out.println("Error occurred when trying to sign up.");
//            e.printStackTrace();
        }
//        out.print(resultSet.getErrString());
//        return;
    }
    if (strAlert.contains("成功")) {
        response.sendRedirect("" + url);
        return;
    }
%><!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8"/>
    <title>注册-无忧借条</title>
    <meta name="keywords" content="无忧借条"/>
    <meta name="description" content="无忧借条"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"/>
    <link rel="stylesheet" href="<%=cdnUrl%>p1.css?v=<%=version%>" type="text/css">
    <link rel="stylesheet" href="<%=cdnUrl%>p/login.css?v=<%=version%>" type="text/css">
    <link rel="stylesheet" href="<%=httpUrl%>assets/css/verCode.css?v=<%=version%>" type="text/css">
    <script src="<%=cdnUrlJS%>assets/scripts/base/sea.js?v=<%=version%>2"
            type="text/javascript"></script>
</head>
<body>

<div class="v1-topbg">
    <jsp:include page="/common/new-header.jsp"></jsp:include>

    <div class="login-bg">
        <div class="login-box">
            <div class="title clear">
                <p class="txt"> 欢迎回来无忧借条！</p>

                <p class="zc">已有账号，请 <a href="login.jsp">登录</a></p>
            </div>
            <form id="form1" action="register.jsp" method="post" autocomplete="off">
                <div class="regist-input">
                    <ul>
                        <li class="clearfix">
                            <p>手机号码</p>

                            <p><input type="text" placeholder="请输入手机号码" id="mobile" maxlength="11" name="username"></p>

                            <p class="tip" id="mobileErr"></p>
                        </li>
                        <li class="clearfix">
                            <p>验 证 码 </p>

                            <p><input type="text" class="yam" placeholder="输入验证码" id="verCode" name="captcha"></p>

                            <p><a href="javascript:void(0);" class="hqyam" id="getVerCode">获取验证码</a></p>

                            <%--<p><a href="#" class="hqyam-again">重新获取 60s</a></p>--%>

                            <p class="tip" id="verErr"></p>
                        </li>
                        <li class="clearfix">
                            <p>设置密码</p>

                            <p><input type="password" placeholder="请设置新密码" id="pwd1" name="pwd"></p>

                            <p class="tip" id="pwd1Err"></p>
                        </li>
                        <li class="clearfix">
                            <p>再次确认</p>

                            <p><input type="password" placeholder="请再次输入密码" id="pwd2" name="pwd2"></p>
                            <p class="tip" id="pwd2Err"></p>
                            <input type="hidden" name="action" value="signup">
                            <input type="hidden" name="retUrl"    value="<%=url%>">
                            <input type="hidden" name="thirdId"  value="<%=tempId%>">
                        </li>
                        <li class="clearfix checkyx"><input type="checkbox" checked="checked" class="simp"
                                                            id="protoCheck">注册即视为默认<a
                                href="aboutUs/agreement.jsp">《无忧借条用户协议》</a></li>
                    </ul>
                </div>
                <div class="login-but"><a class="button" href="javascript:void 0" id="registBtn">注 册</a></div>
            </form>
        </div>
    </div>

    <jsp:include page="/common/new-footer.jsp"></jsp:include>
</div>
</body>
<script>
    define('regist', ['jquery', 'tools/security', 'core', 'tools/cookie'], function (require, exports, module) {
        var $ = require('jquery'), core = require('core');
        var strAlert = '<%=strAlert%>', mobileVerCode, imgVerCode;
        if (strAlert != '') {
            $.ialert(strAlert);
        }
        function Regist() {
            this.$mobile = $('#mobile');//输入手机号标签
            this.$mobileErr = $('#mobileErr');//输入手机号错误提示对象
            this.$verCode = $('#verCode');//输入验证码标签
            this.$getVerCode = $('#getVerCode');//获取验证码按钮
            this.$pwd1 = $('#pwd1');//第一次输入密码标签
            this.$pwd1Err = $('#pwd1Err');//第一次输入密码错误提示标签
            this.$pwd2 = $('#pwd2');//重复输入密码标签
            this.$pwd2Err = $('#pwd2Err');//重复输入密码错误提示标签
            this.$protoCheck = $('#protoCheck');//协议选择标签
            this.$registBtn = $('#registBtn');//注册提交按钮
            this.flag = true;//一个tag，作判断用户注册条件是否输入完备
            this.$form = $('#form1');
            this.refFlag = true;
            this.ev = 'click';
            this.init();
        }

        //初始化
        Regist.prototype.init = function () {
            this.eventBind();
        };
        //绑定事件
        Regist.prototype.eventBind = function () {
            this.mobileEvent();
            this.pwd1Event();
            this.pwd2Event();
            this.protoCheck();
            this.registEvent();
        };
        //手机号码输入时事件
        Regist.prototype.mobileEvent = function () {
            var _this = this;
            _this.$mobile.blur(function () {
                _this.checkMobile()
            });
            _this.preventReSend();

        };
        Regist.prototype.getVCodeEvent = function () {
            var _this = this;
            _this.$getVerCode.click(function () {
                if (_this.checkMobile()) {
                    _this.getImgVerCodeHandle();
                }
            });
        };
        //防止验证码倒计时完毕之前的页面刷新造成重新发送
        Regist.prototype.preventReSend = function () {
            var _this = this, cookie = require('tools/cookie');
            var getCookie = cookie.getCookie('getVCodeCountdown_YXB');
            if (getCookie) {
                var cookies = getCookie.split('_');
                var checkMobile = cookies[1], countdown = cookies[0];
                if (countdown > new Date().getTime()) {
                    _this.refFlag = false;
                    var _countdown = parseInt((countdown - new Date().getTime()) / 1000);
                    _this.$mobile.val(checkMobile).attr('readonly', 'readonly').css('color', '#b1b3b8');
                    _this.$getVerCode.val('已发送(' + _countdown + ')秒').addClass('hqyam-again');
                    require.async(['tools/getVerCode'], function (vcode) {
                        mobileVerCode = vcode.getVerCode({
                            'ev': 'click',
                            'refFlag': true,
                            'clickBtn': _this.$getVerCode,
                            'mobile': checkMobile,
                            'endCallBack': function () {
                                _this.$mobile.removeAttr('readonly').css('color', '');
                                _this.$getVerCode.removeClass('hqyam-again');
                                _this.getVCodeEvent();
                            }
                        });
                        mobileVerCode.countdown = _countdown;
                        mobileVerCode = mobileVerCode.interval();
                    });
                    return false;
                }
            }
            _this.getVCodeEvent();
        };
        //第一次输入密码事件
        Regist.prototype.pwd1Event = function () {
            var _this = this;
            _this.$pwd1.blur(function () {
                _this.validatePwd();
            });
        };
        //第二次输入密码事件
        Regist.prototype.pwd2Event = function () {
            var _this = this;
            _this.$pwd2.blur(function () {
                _this.validatePwdConfrim();
            });
        };
        //注册协议是否遵循
        Regist.prototype.protoCheck = function () {
            var _this = this;
            _this.$protoCheck.click(function () {
                if (!$(this).is(':checked')) _this.$registBtn.hide();
                else _this.$registBtn.css('display', 'block');
            });
        };
        //注册事件
        Regist.prototype.registEvent = function () {
            var _this = this;
            _this.$registBtn.click(function () {
                var flag = _this.checkMobile();
                var flag2 = _this.validateVerCode();
                var $tips = _this.$verCode.closest('li').children('.tip');
                if (flag)if (!flag2) {
                    $tips.addClass('wrong').html('验证码有误');
                    return false;
                }
                else {
                    $tips.addClass('ok').removeClass('wrong').html('');
                }
                flag = flag && _this.validatePwd() && _this.validatePwdConfrim();
                if (flag && flag2) {
                    var md5 = require('tools/security');
                    var pwd = md5.encryptedString(_this.$pwd1.val().trim());
                    _this.$pwd1.val(pwd);
                    _this.$pwd2.val(pwd);
                    setTimeout(function () {
                        //   _this.$form.submit();
                    }, 50)
                    //
                }
            });
        };
        //手机号验证方法
        Regist.prototype.checkMobile = function () {
            var val = this.$mobile.val().trim();
            if (!val.length) {
                this.$mobileErr.html('请输入手机号码').addClass('wrong').removeClass('ok');
                return false;
            }
            else {
                if (!/^\d{11}$/.test(val)) {
                    this.$mobileErr.html('输入手机号码有误').addClass('wrong').removeClass('ok')
                    return false;
                }
                else {
                    this.$mobileErr.empty().addClass('ok').removeClass('wrong');
                    ;
                }
            }
            return true;
        };
        //验证密码输入
        Regist.prototype.validatePwd = function () {
            var len = this.$pwd1.val().trim().length;
            if (len <= 0) {
                this.$pwd1Err.html('请输入密码').addClass('wrong').removeClass('ok')
                return false;
            }
            else if (len < 6) {
                this.$pwd1Err.html('密码输入小于6位，过于简单').addClass('wrong').removeClass('ok')
                return false;
            }
            else {
                this.$pwd1Err.empty().addClass('ok').removeClass('wrong');
                return true;
            }
        };
        //验证密码确认
        Regist.prototype.validatePwdConfrim = function () {
            var val = this.$pwd2.val();
            if (val != this.$pwd1.val()) {
                this.$pwd2Err.html('两次输入的密码不一致，请重新输入').addClass('wrong').removeClass('ok')
                return false;
            } else {
                this.$pwd2Err.empty().addClass('ok').removeClass('wrong');
            }
            return true;
        };
        //验证验证码是否输入/合规
        Regist.prototype.validateVerCode = function () {
            return this.$verCode.val().trim().length >= 6;
        };
        //获取图片验证码方法
        Regist.prototype.getImgVerCodeHandle = function () {
            var _this = this;
            if (!imgVerCode) {
                require.async(['tools/verCode'], function (vcode) {
                    imgVerCode = vcode.verify({
                        postUrl:'/wap/jsp/action.jsp',
                        parameter: function (d) {
                            if (d) return d['postUrl'] = '/wap/jsp/action.jsp',d['username'] = _this.$mobile.val(),d['action'] = 'checkImgYzm', d;
                        },
                        callback: function (flag) {
                            if (!flag) return;
                            _this.$getVerCode.html('正在发送验证码').addClass('hqyam-again');
                            _this.getMobileVerCodeHandle(function () {
                                imgVerCode.verCodeConfirmBox.hide();
                                _this.$getVerCode.removeClass('hqyam-again');
                            });
                        }
                    });
                });
            } else {
                if (!(mobileVerCode && mobileVerCode.flag)) {
                    imgVerCode.open();
                }
            }
        };
        //获取手机验证码方法
        Regist.prototype.getMobileVerCodeHandle = function (callback) {
            var _this = this, mobile = $('#mobile').val().trim(), $getVerCode = this.$getVerCode,
                    url = '/wap/jsp/action.jsp?action=getCaptcha&username=' + mobile + '&ccType=1';
            if (!mobileVerCode) {
                require.async(['tools/getVerCode'], function (vcode) {
                    mobileVerCode = vcode.getVerCode({
                        'ev': 'click',
                        'url': url,
                        'clickBtn': $getVerCode,
                        'callback': callback,
                        'mobile': _this.$mobile.val(),
                        'endCallBack': function () {
                            _this.$getVerCode.removeClass('hqyam-again');
                        }
                    });
                });
            } else {
                mobileVerCode.url = url;
                mobileVerCode.handle();
            }
        };
        new Regist();
    });
    seajs.use('regist');
</script>
</html>
<!-- created in 2016-04-12 16:49-->