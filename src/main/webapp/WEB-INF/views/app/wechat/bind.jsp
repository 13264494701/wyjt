<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
    <%@include file="/WEB-INF/views/include/head.jsp" %>
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <title>微信登录绑定</title>
    <style>
        body {
            font-size: 15px;
            padding: 0px 16px;
        }
    </style>
</head>
<body>
<div class="form-group">
    <form action="/app/wyjt/register/wechat/bindMember" method="post" onsubmit="return check()">
        <div style="display: flex;flex-direction: row;margin-top: 100px;margin-bottom: 10px">
            <div>
                <img src="${headimgurl}" style="width: 60px;height: 60px">
            </div>
            <div style="display:flex; align-items: center;padding-left: 30px">
                <p>欢迎，${nickname}</p>
            </div>
        </div>
        <p>请输入手机号码绑定一个无忧借条账号，绑定成功后微信账号和无忧借条账号都可以登录哦！</p>
        <input type="text" id="username" name="username" placeholder="请输入常用的手机号码：" maxlength="11" style="height:40px;width: 100%">
        <input type="submit" style="width: 100%;height: 40px" class="btn btn-primary" value="绑定">
        <input type="hidden" name="wxUserInfoId" value="${wxUserInfoId}">
    </form>
    <p style="text-align: center;color: #ff0000;font-size: 14px" id="msg"></p>
</div>
<script>
    function check() {
        var username = $("#username").val();
        if (username.length !== 11) {
            $("#msg").html("请输入正确的手机号");
            return false
        }
    }
</script>
</body>
</html>