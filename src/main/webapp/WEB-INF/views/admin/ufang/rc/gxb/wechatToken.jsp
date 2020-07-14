<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
    <title>风控</title>
    <meta name="decorator" content="default"/>
</head>
<body>
<h1>微信查询</h1>
<form action="${ufang}/rcGxb/getToken" method="get" style="margin-top: 50px" onsubmit="return check()">
    <input type="hidden" value="${authItem}" name="authItem">
    <%--<div class="col-lg-12">--%>
    <%--<input type="text" id="name" name="name" class="form-control" placeholder="姓 名：" style="margin-top: 10px">--%>
    <%--</div>--%>
        <input type="text" id="mobile" name="phone" class="form-control" placeholder="手 机：" style="margin-top: 10px;width: 386px" maxlength="11">
    <%--<div class="col-lg-12">--%>
    <%--<input type="text" id="idCard" name="idCard" class="form-control" placeholder="身份证：" style="margin-top: 10px">--%>
    <%--</div>--%>
    <div class="col-lg-12">
        <input type="submit" class="btn btn-block btn-primary" style="margin-top: 10px;width: 400px">
    </div>
    <div style="padding-left: 50px">${msg}
    </div>
    <div class="alert alert-danger" role="alert" style="margin: 50px 0px 0px 0px;padding: 6px 20px">


        温馨提示：
        <br>
        1、查询费用 1元/次；
        <br>
        2、查询手机号关联微信数据返回一般10-15s即可返回，请手动刷新页面；
        <br>
        3、查询量大时数据返回较慢，请耐心等待，切勿重复提交，以免重复扣费。
    </div>
</form>
<script>
    function check() {
        if ($("#mobile").val() == null || $("#mobile").val() == "" || $("#mobile").val().length != 11) {
            alert("手机不正确");
            return false
        }
        var result = confirm('确定付费1元查询数据吗？');
        if (result === true) {
            return true
        }
        else {
            return false
        }
    }
</script>
</body>
</html>