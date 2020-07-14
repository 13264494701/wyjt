<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>订单查询结果</title>
	<meta name="decorator" content="default"/>
	<script src="${ctxStatic}/jquery-form/jquery.form.js"></script>
	<script type="text/javascript">
		$(document).ready(function() {
		});
		function closeDialog(){
			var d = parent.dialog.get('queryRechargeOrder');	
       	  	setTimeout(function(){  
       		  d.close(); 
       		  }
       	  ,200);//单位毫秒   
		}
		
	</script>
	<style type="text/css">
		.mydiv{
 			margin:0 auto;
			width:400px;   
		    height:200px; 
		    left:150%; 
		    top:100%; 
		    margin-top: 10%;
		}
	</style>
</head>
<body>
		<div id="mydiv" class="mydiv" style="text-align: center;" >
			<span style="font-size: x-large;"> 查询结果：</span><span style="font-size: x-large;">${result}</span><br/>
			<span style="font-size: x-large;"> 第三方订单号：</span><span style="font-size: x-large;">${thirdOrderNo}</span>
		</div>
</body>
</html>