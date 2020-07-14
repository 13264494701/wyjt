<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
    <title>风控</title>
    <meta name="decorator" content="default"/>
 <link rel="stylesheet" href="//code.jquery.com/ui/1.10.4/themes/smoothness/jquery-ui.css">
<script src="//code.jquery.com/jquery-1.10.2.js"></script>
<script src="//code.jquery.com/ui/1.10.4/jquery-ui.js"></script>
</head>
<body>
<h1>  
	<c:choose>  
       <c:when test="${channel==0}">
           	运营商查询   
       </c:when> 
       <c:when test="${channel==1}">
           	淘宝查询 
       </c:when> 
       <c:when test="${channel==2}">
           	网银查询 
       </c:when> 
       <c:when test="${channel==3}">
           	社保查询 
       </c:when> 
       <c:when test="${channel==4}">
           	公积金查询 
       </c:when> 
       <c:when test="${channel==5}">
           	学信网查询 
       </c:when> 
       <c:when test="${channel==6}">
           	京东查询 
       </c:when> 
       <c:otherwise>  
       </c:otherwise>  
    </c:choose>
</h1>
<form action="${ufang}/rcSjmh/getToken" method="get" style="margin-top: 30px" onsubmit="return check()">
    <div>
       <input type="hidden" name="channel" value="${channel}">
    </div>
    <div>
        <input type="text" id="name" name="name" class="form-control" placeholder="姓 名：" style="margin-top: 10px;width: 386px" maxlength="20">
    </div>
    <div>
        <input type="text" id="phone" name="phone" class="form-control" placeholder="手 机：" style="margin-top: 10px;width: 386px" maxlength="11">
    </div>
    <div>
        <input type="text" id="idcard" name="idcard" class="form-control" placeholder="身份证：" style="margin-top: 10px;width: 386px" maxlength="20">
    </div>
    <div>
        <input type="checkbox" id="allow" name="allow" >本人已获取该查询用户授权 <a href ="javascript:void(0);" onclick="showAgreement()" style="text-decoration:none;cursor:pointer;">《查看授权协议》</a>
    </div>
   	<div>
        <input type="submit" class="btn btn-primary" style="margin-top: 10px;width: 400px">
    </div>
    ${msg}
    <div class="alert alert-danger" role="alert" style="margin: 50px 0px 0px 0px;padding: 6px 20px">查询费用：1元/次</div>
</form>
<div class="formPanel" id="agreement" style="display:none;">
	<p>
		根据《征信业管理条例》第十八条规定，向征信机构查询个人信息的，应当取得信息主体本人的书面同意并约定用途，但是，法律规定可以不经同意查询的除外，征信机构不得违反前款规定提供个人信息。
		 <br><br>
		勾选以获取该查询用户授权即默认您已获取该查询用户授权，如若发生法律纠纷，我方不承担任何责任。
	</p>
</div>

<script>
function showAgreement(){
	$('#agreement').dialog({
		title : '授权说明',
		height : 220,
		width : 380,
		modal : true,
		show : 'clip',
		draggable: false,
		resizable:false
	});
}
    function check() {
       	if(!$("#allow").is(":checked")){
       		alert("请勾选《授权协议》");
            return false
       	}
        if ($("#name").val() == null || $("#name").val() == "" || $("#name").val().length < 2) {
            alert("姓名不正确");
            return false
        }
        if ($("#phone").val() == null || $("#phone").val() == "" || $("#phone").val().length != 11) {
            alert("手机不正确");
            return false
        }
        if ($("#idcard").val() == null || $("#idcard").val() == "" || $("#idcard").val().length < 15) {
            alert("身份证不正确");
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