<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/inc/function.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8"/>
    <title>忘记密码-无忧借条</title>
    <meta name="keywords" content="无忧借条"/>
    <meta name="description" content="无忧借条"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"/>
    <link rel="stylesheet" href="<%=cdnUrl%>p1.css?v=<%=version%>" type="text/css">
    <link rel="stylesheet" href="<%=cdnUrl%>p/login.css?v=<%=version%>" type="text/css">
    <link rel="stylesheet" href="<%=httpUrl%>/assets/css/verCode.css?v=<%=version%>" type="text/css">
    <script src="http://image.51jt.com/assets/scripts/base/sea.js?v=<%=version%>"
            type="text/javascript"></script>
</head>
<body>

<div class="v1-topbg">
    <jsp:include page="/common/new-header.jsp"></jsp:include>

    <div class="login-bg">
        <div class="login-box">
            <div class="title clear">
                <p class="txt"> 找回密码</p>
            </div>
            <div class="findpassword-lc clearfix">
                <ul>
                    <li class="checked"><em>1</em><a>第一步：确认账号</a></li>
                    <li class="line checked"></li>
                    <li style=" margin-right:0;"><em>2</em><a>第二步：重置密码</a></li>
                </ul>
            </div>
            <div class="regist-input regist-input2">
                <form  id="form1" method="post">
                    <ul>
                        <!--第一步开始-->
                        <li class="clearfix">
                            <p style="width: 90px">手机号码</p>

                            <p><input type="text" name="mobile" placeholder="请输入手机号码" maxlength="11" id="mobile"></p>

                            <p class="tip"></p>
                        <li class="clearfix"><p>短信验证码 </p>

                            <p><input type="text" class="yam" placeholder="输入验证码" name="captcha"></p>

                            <p><a href="javascript:void(0);" class="hqyam">短信验证码</a></p>

                            <p class="tip" id="yzmErr"></p>
                        </li>
                        <!--第一步结束-->
                    </ul>
                </form>
            </div>
            <div class="login-but"><a class="button" href="javascript:void 0" id="nextBtn">下 一 步</a></div>
        </div>
    </div>
    <jsp:include page="/common/new-footer.jsp"></jsp:include>
</div>
<script type="text/javascript">
    define('forgetPwd', ['jquery'], function (require, exports, module) {
        var $form = $('#form1'), $mobile = $('[name="mobile"]', $form), $cap = $('[name="captcha"]', $form),
                $hqyzm = $cap.closest('li').find('.hqyam'), mobileVerCode, imgVerCode;

        function validateMobile(el) {
            var v = $.trim(el.val()), err;
            if (v == '') {
                err = '手机号不能为空';
            }
            else if (!/[\d]{11}/.test(v)) {
                err = '手机号格式不对';
            }
            var $error = el.closest('li').find('.tip');
            if (err) {
                $error.html(err).addClass('wrong').removeClass('ok');
                return false;
            }
            $error.html('').addClass('ok').removeClass('wrong');
            return true;
        }

        function validateCaptcha(el) {
            var v = $.trim(el.val()), err;
            if (v == '') {
                err = '短信验证码不能为空';
            }
            var $error = el.closest('li').find('.tip');
            if (err) {
                $error.html(err).addClass('wrong').removeClass('ok');
                return false;
            }
            $error.html('').addClass('ok').removeClass('wrong');
            return true;
        }

        $mobile.bind('blur ', function () {
            validateMobile($(this))
        });
        $cap.bind('blur', function () {
            validateCaptcha($(this))
        });

        preventReSend();
        function getVCodeEvent() {
            $hqyzm.click(function () {
                var f = validateMobile($mobile);
                if (f) {
                    getImgVerCodeHandle();
                }
            });
        }

        //获取图片验证码方法
        function getImgVerCodeHandle() {
            var _this = this, $hqyzm = $('.hqyam');
            if (!imgVerCode) {
                require.async(['tools/verCode'], function (vcode) {
                    imgVerCode = vcode.verify({
                        parameter: function (d) {
                            if (d) return d['postUrl'] = '/wap/jsp/api.jsp',d['mobile'] = $mobile.val(),d['action'] = 'checkImgYzmByForgetpwd', d;
                        },
                        callback: function (flag) {
                            if (!flag) return;
                            $hqyzm.html('正在发送验证码').addClass('hqyam-again');
                            getMobileVerCodeHandle(function () {
                                imgVerCode.verCodeConfirmBox.hide();
                                $hqyzm.removeClass('hqyam-again');
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
        function getMobileVerCodeHandle(callback) {
            var mobile = $('#mobile').val(),
                    url = '/wap/jsp/action.jsp?action=getCaptcha&username=' + mobile + '&ccType=1';
            if (!mobileVerCode) {
                require.async(['tools/getVerCode'], function (vcode) {
                    mobileVerCode = vcode.getVerCode({
                        'ev': 'click',
                        'url': url,
                        'clickBtn': $hqyzm,
                        'mobile': mobile,
                        'callback': function () {
                            $hqyzm.addClass('hqyam-again');
                        },
                        'endCallBack': function () {
                            $hqyzm.removeClass('hqyam-again');
                            $.cookie('getVCodeCountdown_YXB', null);//清除
                        }
                    });
                });
            } else {
                mobileVerCode.url = url;
                mobileVerCode.handle();
            }
        }

        //防止验证码倒计时完毕之前的页面刷新造成重新发送
        function preventReSend() {
            var _this = this;
            $.cookie('getVCodeCountdown_YXB', function (cookie) {
                if (cookie) {
                    var cookies = cookie.split('_');
                    var checkMobile = cookies[1], countdown = cookies[0];
                    if (countdown > new Date().getTime()) {
                        _this.refFlag = false;
                        var _countdown = parseInt((countdown - new Date().getTime()) / 1000);
                        $cap.attr('readonly', 'readonly').css('color', '#b1b3b8');
                        $hqyzm.val('已发送(' + _countdown + ')秒').addClass('hqyam-again');
                        require.async(['tools/getVerCode'], function (vcode) {
                            mobileVerCode = vcode.getVerCode({
                                'ev': 'click',
                                'refFlag': true,
                                'clickBtn': $hqyzm,
                                'mobile': checkMobile,
                                'endCallBack': function () {
                                    $cap.removeAttr('readonly').css('color', '');
                                    $hqyzm.removeClass('hqyam-again');
                                    getVCodeEvent();
                                }
                            });
                            mobileVerCode.countdown = _countdown;
                            mobileVerCode = mobileVerCode.interval();
                        });
                        return false;
                    }
                }
                getVCodeEvent();
            });

        }


        $('#nextBtn').click(function () {
            var f = validateMobile($mobile) && validateCaptcha($cap);
            if (f) {
                $.cookie('JSESSIONID', function (cookie) {
                    if (cookie) {
                        var url = '/wap/jsp/api.jsp?action=checkYzmByForgetpwd&JSESSIONID=' + cookie + '&yzm=' + $cap.val() + '&mobile=' + $mobile.val();
                        $.ipostByJSON(url, function (err, rel) {
                            if (!err) {
								rel=$.parseJSON(rel);
                                if (rel['success']) {
                                    location.href = 'forgetpwd3.jsp';
                                }
                                else {
                                    $.ialert('提交失败，请重试~！');
                                }
                            }
                            else $.ialert('网络连接超时，请刷新再试！');
                        });
                    }
                });
            }
        });
        var $capImg = $('.verCodeChange'), src = $capImg.attr('src');
        $capImg.add('.hyz').click(function () {
            $capImg.attr('src', src + '?v=' + (+new Date()));
        })
    });

    seajs.use('forgetPwd');

</script>
</body>
</html>
<!-- created in 2016-04-12 16:49-->