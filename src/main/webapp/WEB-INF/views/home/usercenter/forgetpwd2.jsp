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
                    <li class="checked"><em>2</em><a>第二步：安全验证</a></li>
                    <li class="line "></li>
                    <li style=" margin-right:0;"><em>3</em><a>第三步：重置密码</a></li>
                </ul>
            </div>
            <form action="forgetpwd3.jsp" id="form1" method="post">
                <div class="regist-input regist-input2">
                    <ul>
                        <!--第二步开始-->
                        <li class="clearfix">
                            <p>手机号码</p>

                            <p class="nump2">152******50 </p>

                        </li>
                        <li class="clearfix"><p>验 证 码 </p>

                            <p><input type="text" class="yam" placeholder="输入验证码"></p>

                            <p><a href="javascript:void(0);" class="hqyam">短信验证码</a></p>

                            <p class="tip" id="yzmErr"></p>
                        </li>
                        <!--第二步结束-->
                    </ul>
                </div>
                <div class="login-but"><a class="button" href="javascript:void(0);" id="nextBtn">下 一 步</a></div>
            </form>
        </div>
    </div>

    <jsp:include page="/common/new-footer.jsp"></jsp:include>
</div>
</body>
<script>
    define('regist', ['jquery'], function (require, exports, module) {
        var $yam = $('.yam'), $hqyam = $('.hqyam'), $yzmErr = $('#yzmErr'),
                $nextBtn = $('#nextBtn'), $form = $('#form1'), mobileVerCode;

        preventReSend();
        function getVCodeEvent() {
            $hqyam.click(function () {
                getMobileVerCodeHandle();
            });
        }

        $nextBtn.click(function () {
            var flag = $yam.val().trim().length >= 6;
            if (!flag) {
                $yzmErr.addClass('wrong').html('验证码有误');
                return false;
            }
            $yzmErr.addClass('ok').removeClass('wrong').html('');
            $form.submit();
        });

        //获取手机验证码方法
        function getMobileVerCodeHandle(callback) {
            var mobile =$('.nump2').html(),
                    url = '/wap/jsp/action.jsp?action=getCaptcha&username=' + mobile + '&ccType=1';
            if (!mobileVerCode) {
                require.async(['tools/getVerCode'], function (vcode) {
                    mobileVerCode = vcode.getVerCode({
                        'ev': 'click',
                        'url': url,
                        'clickBtn': $hqyam,
                        'mobile': mobile,
                        'callback': function () {
                            $hqyam.addClass('hqyam-again');
                        },
                        'endCallBack': function () {
                            $hqyam.removeClass('hqyam-again');
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
                        $yam.attr('readonly', 'readonly').css('color', '#b1b3b8');
                        $hqyam.val('已发送(' + _countdown + ')秒').addClass('hqyam-again');
                        require.async(['tools/getVerCode'], function (vcode) {
                            mobileVerCode = vcode.getVerCode({
                                'ev': 'click',
                                'refFlag': true,
                                'clickBtn': $hqyam,
                                'mobile': checkMobile,
                                'endCallBack': function () {
                                    $yam.removeAttr('readonly').css('color', '');
                                    $hqyam.removeClass('hqyam-again');
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
    });
    seajs.use('regist');
</script>
</html>
<!-- created in 2016-04-12 16:49-->