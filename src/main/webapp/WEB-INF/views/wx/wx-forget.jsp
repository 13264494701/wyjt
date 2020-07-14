<%@ page contentType="text/html;charset=UTF-8" %>
<%@include file="/WEB-INF/views/app/constant.jsp" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <title>忘记密码-无忧借条</title>
    <%@include file="/WEB-INF/views/include/head.jsp" %>
    <link rel="stylesheet" href="${mbStatic}/assets/css/public.css?v=<%=version%>" type="text/css">
    <link rel="stylesheet" href="${mbStatic}/assets/css/v1/wechat.css?v=<%=version%>" type="text/css">
    <script src="${mbStatic}/assets/scripts/base/sea.js?v=<%=version%>"></script>
    <script src="${mbStatic}/assets/scripts/base/flexible.js?v=<%=version%>"></script>
</head>

<body>
<article class="views docBody">
    <section class="view">
        <div class="pages">
            <div class="page">
                <div class="page-content">
                    <div class="login regist">
                        <form action="/wx/wyjt/password/check" method="post" autocomplete="off"
                              onsubmit="return check()">
                            <div class="logo"></div>
                            <div class="login-input iconinput clearfix regist-input">
                                <p>
                                    <i class="user"></i><input type="tel" placeholder="手机号码" id="phoneNo" name="phoneNo"
                                                               maxlength="11">
                                </p>

                                <p class="yzm">
                                    <i class="pic"></i>
                                    <input type="text" placeholder="图片验证码" id="txyzm" name="txyzm">
                                    <span class="picimg">
                                        <img src="/app/wyjt/common/getImageCaptcha?captchaId=${uuid}"
                                             id="getPic" onclick="shuaxin()">
                                    </span>
                                </p>

                                <p class="yzm">
                                    <i class="mess"></i>
                                    <input type="tel" placeholder="短信验证码" id="dxyzm" name="dxyzm">
                                    <a href="javascript:void(0);" id="hqyzm" onclick="yanzheng()">短信验证码</a>
                                </p>

                                <input class="redBtn btn-block" type="submit" style="background-color: #ed2e24;color: white;padding: 0" value="提交">

                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </section>
</article>

<script type="text/javascript">
    $(document).ready(function () {
        if ("${msg}" != "" && "${msg}" != null) {
            alert("${msg}")
        }
    })

    function shuaxin() {
        $("#getPic").attr("src", "/app/wyjt/common/getImageCaptcha?captchaId=${uuid}&v=" + new Date())
    }

    function yanzheng() {
        if ($("#hqyzm").attr("disabled") == "disabled") {
            return;
        }
        if ($("#phoneNo").val() == null || $("#phoneNo").val() == "" || $("#phoneNo").val().length != 11) {
            alert("手机号码格式不正确");
            return false
        }
        if ($("#txyzm").val() == null || $("#txyzm").val() == "" || $("#txyzm").val().length != 4) {
            alert("图片验证码格式不正确");
            return false
        }
        var time = 60;
        $("#hqyzm").attr("disabled", true);
        var url = "/app/wyjt/common/checkImageCaptchaAndSendSmsCode?param=";
        var json = "{\"tmplCode\": \"wyjtAppRegister\",\"phoneNo\":" + $("#phoneNo").val() + ",\"imageCaptcha\": \"" + $("#txyzm").val() + "\",\"captchaId\": \"${uuid}\"}";
        $.post(encodeURI(url + json), {},
            function (res) {
                if (res.code == 0) {
                    var timer = setInterval(function () {
                        if (time <= 0) {
                            clearInterval(timer);
                            $("#hqyzm").html("短信验证码");
                            $("#hqyzm").attr("disabled", false);
                        } else {
                            $("#hqyzm").html(time + "秒");
                            time--;
                        }
                    }, 1000);
                } else {
                    $("#hqyzm").attr("disabled", false);
                    shuaxin();
                    alert(res.message);
                }
            });
    }

    function check() {
        if ($("#phoneNo").val() == null || $("#phoneNo").val() == "" || $("#phoneNo").val().length != 11) {
            alert("手机号码格式不正确");
            return false
        }
        if ($("#txyzm").val() == null || $("#txyzm").val() == "" || $("#txyzm").val().length != 4) {
            alert("图片验证码格式不正确");
            return false
        }
        if ($("#dxyzm").val() == null || $("#dxyzm").val() == "" || $("#dxyzm").val().length != 6) {
            alert("短信验证码格式不正确");
            return false
        }
    }
</script>
</body>
</html>