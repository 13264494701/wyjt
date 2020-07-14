<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
    <title>查询结果</title>
    <meta name="decorator" content="default"/>
    <style>
        .btn {
            font-size: 14px;
            height: 28px;
            display: flex;
            align-items: center;
            justify-content: center;
            border-radius: 4px;
            color: #ffffff;
            background-color: #3daae9;
            background-image: linear-gradient(to bottom, #46aeea, #2fa4e7);
            border-color: #2fa4e7 #2fa4e7 #157ab5;
            border-color: rgba(0, 0, 0, 0.1) rgba(0, 0, 0, 0.1) rgba(0, 0, 0, 0.25);
        }
    </style>
</head>
<body>
<h3 style="text-align: center;margin-top: 200px;">查询成功请在历史记录中查看</h3>
<div style="padding: 0px 10px 0px 10px">
    <a class="btn btn-block" style="margin-top: 10px;width: 100%;" href="${ufang}/rcSjmh/add?channel=0">继续查询</a>
</div>
</body>
</html>