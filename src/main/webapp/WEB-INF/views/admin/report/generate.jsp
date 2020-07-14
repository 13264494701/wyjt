<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<html>
<head>
    <title>报表生成</title>
    <meta name="decorator" content="default"/>
</head>
<body>
<form method="get" action="${ctx}/report/generate" style="margin: 50px">
    <input name="date" type="text" maxlength="20" class="input-medium Wdate" value=""
           onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:true});">
    <br>
    <input type="submit" value="生成" class="btn btn-success" style="width: 178px">
    <br>
    ${msg}
</form>
</body>
</html>