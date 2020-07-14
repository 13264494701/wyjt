<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/inc/function.jsp" %>
<%

    String userId = (String) request.getSession().getAttribute("forgetpwd");
    if (userId == null || userId.equals("")) {
        response.sendRedirect("forgetpwd1.jsp");
        return;
    }
%>
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
                    <li style=" margin-right:0;" class="checked"><em>2</em><a>第二步：重置密码</a></li>
                </ul>
            </div>
            <div class="regist-input regist-input2">
                <form id="form1" action="#" method="post">
                    <ul>
                        <!--第三步开始-->
                        <li class="clearfix"><p>新 密 码</p>

                            <p><input type="password" name="pwd" placeholder="请设置新密码"></p>

                            <p class="tip"></p>
                        </li>
                        <li class="clearfix"><p>确认密码</p>

                            <p><input type="password" name="pwd2" placeholder="再次确认新密码" id="pwd2"></p>

                            <p class="tip"></p>
                        </li>
                        <!--第三步结束-->
                    </ul>
                </form>
            </div>
            <div class="login-but"><a class="button" id="nextBtn">下 一 步</a></div>
        </div>
    </div>

    <jsp:include page="/common/new-footer.jsp"></jsp:include>
</div>
<script type="text/javascript">
    define('forgetPwd', ['jquery'], function (require, exports, module) {
        var md5, $form = $('#form1'), $pwd = $('[name="pwd"]', $form), $pwd2 = $('[name="pwd2"]', $form);

        function validatePwd($el) {
            var val = $.trim($el.val()), error;
            if (val.length <= 0) {
                error = "请输入密码";
            }
            else if (val.length < 6 || val.length > 18) {
                error = "密码长度必须为6-18个字符";
            }
            else if (!/^[a-zA-Z0-9_]{6,18}$/.test(val)) {
                error = "密码只能是数字、字母和下划线";
            }
            var $error = $el.closest('li').find('.tip');
            if (error) {
                $error.html(error).addClass('wrong').removeClass('ok');
                return false;
            }
            $error.html('').addClass('ok').removeClass('wrong');
            return true;
        }

        function validatePwd2($el1, $el2) {
            var val = $.trim($el1.val()), val2 = $.trim($el2.val()), error;
            if (val2.length <= 0) {
                error = "请再次输入确认密码";
            }
            else if (val2 != val) {
                error = "两次输入的密码不同";
            }
            var $error = $el2.closest('li').find('.tip');
            if (error) {
                $error.html(error).addClass('wrong').removeClass('ok');
                return false;
            }
            $error.html('').addClass('ok').removeClass('wrong');
            return true;
        };
        $pwd.bind('blur ', function () {
            validatePwd($(this))
        });
        $pwd2.bind('blur', function () {
            validatePwd2($pwd, $(this))
        });
        function setMd5() {
            if (md5) {
                var pwd = ($.trim($pwd.val()));
                $pwd.val(pwd);
                $pwd2.val(pwd);
                setTimeout(function () {
//                    $form.submit();
                    $.cookie('JSESSIONID', function (cookie) {
                        if (cookie) {
                            var url = '/wap/jsp/api.jsp',para={"action":"updatePAS","JSESSIONID":cookie,"password":$.trim($pwd.val()),"mobile":""};
                            $.ipostJSON(url,para, function (err, rel) {
                                if (!err) {
//									rel=$.parseJSON(rel);
                                    if (rel&&rel['success']) {
                                        $.ialert('恭喜，您密码重置成功~！',function(){
                                            location.href='/home/login.jsp';
                                        });
                                    }
                                    else {
                                        $.ialert('提交失败，请重试~！');
                                    }
                                }
                                else $.ialert('网络连接超时，请刷新再试！');
                            });
                        }
                    });
                }, 50)
            }
        }

        $('#nextBtn').click(function () {
            var f = validatePwd($pwd) && validatePwd2($pwd, $pwd2);
            if (f) {
                if (!md5) {
                    require.async(['tools/security'], function (security) {
                        md5 = security;
                        setMd5();
                    })
                } else setMd5();
            }
        });
    });
    seajs.use('forgetPwd');
</script>
</body>
</html>
<!-- created in 2016-04-12 16:49-->