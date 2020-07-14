<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<html>
<head>
    <title>好友关系管理</title>
    <meta name="decorator" content="default"/>
    <script type="text/javascript">
        function hqyzm() {
            if ($("#phoneNo").val() == null || $("#phoneNo").val() == "" || $("#phoneNo").val().length != 11) {
                alert("手机号码格式不正确");
                return;
            }
            if($("#hqyzm").attr("disabled") == "disabled"){
                return;
            }
            var phoneNo = $("#phoneNo").val();
            var time = 30;
            $("#hqyzm").attr("disabled", true);
            $.get("${ufang}/friend/sendSmsVerifyCode?phoneNo=" + phoneNo, function (res) {
                if (res != "success") {
                    $("#msg").html(res);
                    time = 10;
                }
            });
            var timer = setInterval(function () {
                if (time <= 0) {
                    clearInterval(timer);
                    $("#hqyzm").html("获取验证码");
                    $("#hqyzm").attr("disabled", false);
                } else {
                    $("#hqyzm").html(time + "秒");
                    time--;
                }
            }, 1000);
        }
    </script>
</head>
<body>
<c:if test="${bindStatus!=0}">
    <form action="${ufang}/friend/addFriend" method="post" class="form-horizontal" onsubmit="return check()">
        <div class="control-group" style="margin-top: 10px">
            <label style="text-align:right;width:177px">好友用户名(手机号)：</label>
            <input style="width:230px" maxlength="11" type="text" id="phoneNo" name="phoneNo"/>
            <span class="help-inline" style="width:150px"></span>
        </div>
        <div class="control-group">
            <label style="text-align:right;width:177px">验证码：</label>
            <input id="smsCode" name="smsCode" type="text" style="width:100px" value="" maxlength="10" minlength="3"/>
            <span class="help-inline" style="width:20px"></span>
            <a id="hqyzm" class="btn btn-primary" style="width:73px;" onclick="hqyzm()">获取验证码</a>
        </div>
        <div class="form-actions">
            <shiro:hasPermission name="ufang:friend:edit">
                <input id="btnSubmit" class="btn btn-primary" style="width:250px;" type="submit" value="添加好友"/>&nbsp;
            </shiro:hasPermission>
        </div>
    </form>
</c:if>
<c:if test="${bindStatus==0}">
    <div style="padding-left: 100px">当前账号还没有绑定无忧借条账号，请先绑定</div>
</c:if>
<div style="padding-left: 100px" id="msg">${msg}</div>
<script>
    function check() {
        if ($("#phoneNo").val() == null || $("#phoneNo").val() == "" || $("#phoneNo").val().length != 11) {
            alert("手机号码格式不正确");
            return false
        }
        if ($("#smsCode").val() == null || $("#smsCode").val() == "" || $("#smsCode").val().length < 6) {
            alert("验证码格式不正确");
            return false
        }
    }
</script>
</body>
</html>