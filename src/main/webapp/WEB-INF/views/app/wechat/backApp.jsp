<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<html>
<head>
    <%@include file="/WEB-INF/views/include/head.jsp" %>
    <title>微信登录成功</title>
</head>
<body>
<script type="text/javascript">
    YXBaoAndroid.thirdLogin('${result}')
</script>
</body>
</html>