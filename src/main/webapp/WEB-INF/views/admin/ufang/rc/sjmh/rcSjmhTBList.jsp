<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			
		});
		function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#searchForm").submit();
        	return false;
        }
	</script>
</head>
<body>
    <h2>淘宝信息</h2><br>
	<sys:message content="${message}"/>
	<table width="100%" style="height: 40px" border="1" bordercolor="white" cellspadding="5" cellspacing="0"> 
	<colgroup style="background:#2FB6EF"></colgroup>
	<tr>
	<td style="color: white;font-size: 16px;padding-left: 18px;font-weight: bold;">基本信息</td>
	</tr>			
	</table>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
			<tr>
				<th style="text-align:center; width:15%;height:25px ">真实姓名</th>
				<th style="text-align:center; width:25%  ">${base_info.get("name")}</th>
				<th style="text-align:center; width:15% ">登录邮箱</th>
				<th style="text-align:center; width:25% ">${base_info.get("email")}</th>
			</tr>
			<tr>
				<th style="text-align:center; width:15%;height:25px  ">身份证号</th>
				<th style="text-align:center; width:25%  ">${base_info.get("identity_code")}</th>
				<th style="text-align:center; width:15% ">绑定手机号</th>
				<th style="text-align:center; width:25% ">${base_info.get("mobile")}</th>
			</tr>
			<tr>
				<th style="text-align:center; width:15%;height:25px  ">用户名</th>
				<th style="text-align:center; width:25%  ">${base_info.get("user_name")}</th>
				<th style="text-align:center; width:15% ">用户等级</th>
				<th style="text-align:center; width:25% ">${base_info.get("user_level")}</th>
			</tr>
			<tr>
				<th style="text-align:center; width:15%;height:25px  ">昵称</th>
				<th style="text-align:center; width:25%  ">${base_info.get("nick_name")}</th>
				<th style="text-align:center; width:15% ">淘气值</th>
				<th style="text-align:center; width:25% ">${base_info.get("vip_count")}</th>
			</tr>
			<tr>
				<th style="text-align:center; width:15%;height:25px  ">性别</th>
				<th style="text-align:center; width:25%  ">${base_info.get("gender")}</th>
				<th style="text-align:center; width:15% "></th>
				<th style="text-align:center; width:25% "></th>
			</tr>
	</table>
	<table width="100%" style="height: 40px" border="1" bordercolor="white" cellspadding="5" cellspacing="0"> 
	<colgroup style="background:#2FB6EF"></colgroup>
	<tr>
	<td style="color: white;font-size: 16px;padding-left: 18px;font-weight: bold;">账户信息</td>
	</tr>			
	</table>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
			<tr>
				<th style="text-align:center; width:15%;height:25px ">账户余额</th>
				<th style="text-align:center; width:25%  ">${account_info.get("account_balance")/100}</th>
				<th style="text-align:center; width:15% ">芝麻信用分</th>
				<th style="text-align:center; width:25% ">${(account_info.get("zhima_point")==null || account_info.get("zhima_point").toString().equals(""))?暂无:account_info.get("zhima_point")}</th>
			</tr>
			<tr>
				<th style="text-align:center; width:15%;height:25px  ">余额宝余额</th>
				<th style="text-align:center; width:25%  ">${account_info.get("financial_account_balance")/100}</th>
				<th style="text-align:center; width:15% ">花呗信用额度</th>
				<th style="text-align:center; width:25% ">${account_info.get("credit_quota")/100}</th>
			</tr>
			<tr>
				<th style="text-align:center; width:15%;height:25px  ">花呗已消费额度</th>
				<th style="text-align:center; width:25%  ">${account_info.get("consume_quota")/100}</th>
				<th style="text-align:center; width:15% ">花呗可用额度</th>
				<th style="text-align:center; width:25% ">${account_info.get("available_quota")/100}</th>
			</tr>
			<tr>
				<th style="text-align:center; width:15%;height:25px  ">借呗额度</th>
				<th style="text-align:center; width:25%  ">${account_info.get("jiebei_quota")/100}</th>
				<th style="text-align:center; width:15% "></th>
				<th style="text-align:center; width:25% "></th>
			</tr>
	</table>
	<table width="100%" style="height: 40px" border="1" bordercolor="white" cellspadding="5" cellspacing="0"> 
	<colgroup style="background:#2FB6EF"></colgroup>
	<tr>
	<td style="color: white;font-size: 16px;padding-left: 18px;font-weight: bold;">收货地址</td>
	</tr>			
	</table>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
	<tr>
				<th style="text-align:center; width:15%;height:25px ">姓名</th>
				<th style="text-align:center; width:25% ">地址</th>
				<th style="text-align:center; width:15% ">邮编</th>
				<th style="text-align:center; width:25% ">手机号</th>
	</tr>
	<c:forEach items="${receiver_list}" var="li" varStatus="status">
			<tr>
				<th style="text-align:center; width:15%;height:25px ">${li.get("name")}</th>
				<th style="text-align:center; width:25%  ">${li.get("address")}</th>
				<th style="text-align:center; width:15% ">${li.get("zip_code")}</th>
				<th style="text-align:center; width:25% ">${li.get("mobile")}</th>
			</tr>
  		</c:forEach>
			
	</table>
	<table width="100%" style="height: 40px" border="1" bordercolor="white" cellspadding="5" cellspacing="0"> 
	<colgroup style="background:#2FB6EF"></colgroup>
	<tr>
	<td style="color: white;font-size: 16px;padding-left: 18px;font-weight: bold;">购物车信息</td>
	</tr>			
	</table>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
	<tr>
				<th style="text-align:center; width:15%;height:25px ">商品名称</th>
				<th style="text-align:center; width:25% ">商品单价</th>
				<th style="text-align:center; width:15% ">商品数量</th>
				<th style="text-align:center; width:25% ">商品型号</th>
	</tr>
	<c:forEach items="${shopping_cart}" var="li" varStatus="status">
			<tr>
				<th style="text-align:center; width:15%;height:25px ">${li.get("good_name")}</th>
				<th style="text-align:center; width:25%  ">${li.get("good_price")/100}</th>
				<th style="text-align:center; width:25% ">${li.get("good_number")}</th>
				<th style="text-align:center; width:25% ">${li.get("good_model")}</th>
			</tr>
  		</c:forEach>
			
	</table>
	<table width="100%" style="height: 40px" border="1" bordercolor="white" cellspadding="5" cellspacing="0"> 
	<colgroup style="background:#2FB6EF"></colgroup>
	<tr>
	<td style="color: white;font-size: 16px;padding-left: 18px;font-weight: bold;">订单信息</td>
	</tr>			
	</table>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
	<tr>
				<th style="text-align:center; width:15%;height:25px ">订单金额</th>
				<th style="text-align:center; width:25% ">收货人</th>
				<th style="text-align:center; width:15% ">商品名称</th>
				<th style="text-align:center; width:25% ">订单时间</th>
	</tr>
	<c:forEach items="${order_list}" var="li" varStatus="status">
			<tr>
				<th style="text-align:center; width:15%;height:25px ">${li.get("order_amount")/100}</th>
				<th style="text-align:center; width:25%  ">${li.get("receiver_name")}</th>
				<th style="text-align:center; width:25% ">${li.get("product_name")}</th>
				<th style="text-align:center; width:25% ">${li.get("order_time")}</th>
			</tr>
  		</c:forEach>
			
	</table>
    <div class="pagination">${page}</div>
</body>
</html>