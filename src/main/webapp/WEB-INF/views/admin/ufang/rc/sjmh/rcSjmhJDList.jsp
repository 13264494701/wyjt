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
 <h2>京东信息</h2><br>
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
				<th style="text-align:center; width:15% ">会员成长值</th>
				<th style="text-align:center; width:25% ">${base_info.get("vip_count")}</th>
			</tr>
			<tr>
				<th style="text-align:center; width:15%;height:25px  ">性别</th>
				<th style="text-align:center; width:25%  ">${base_info.get("gender")}</th>
				<th style="text-align:center; width:15% ">绑定银行卡数</th>
				<th style="text-align:center; width:25% ">${base_info.get("binded_bankcard_amount")}</th>
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
				<th style="text-align:center; width:15% ">信用分数</th>
				<th style="text-align:center; width:25% ">${(account_info.get("credit_point")==null || account_info.get("credit_point").toString().equals(""))?暂无:account_info.get("credit_point")}</th>
			</tr>
			<tr>
				<th style="text-align:center; width:15%;height:25px  ">金融帐户余额</th>
				<th style="text-align:center; width:25%  ">${account_info.get("financial_account_balance")/100}</th>
				<th style="text-align:center; width:15% ">白条额度</th>
				<th style="text-align:center; width:25% ">${account_info.get("credit_quota")/100}</th>
			</tr>
			<tr>
				<th style="text-align:center; width:15%;height:25px  ">白条欠款</th>
				<th style="text-align:center; width:25%  ">${account_info.get("consume_quota")/100}</th>
				<th style="text-align:center; width:15% ">白条可用额度</th>
				<th style="text-align:center; width:25% ">${account_info.get("baitiao_balance")/100}</th>
			</tr>
			<tr>
				<th style="text-align:center; width:15%;height:25px  ">总负债</th>
				<th style="text-align:center; width:25%  ">${account_info.get("debt_total")/100}</th>
				<th style="text-align:center; width:15% "></th>
				<th style="text-align:center; width:25% "></th>
			</tr>
	</table>
	<table width="100%" style="height: 40px" border="1" bordercolor="white" cellspadding="5" cellspacing="0"> 
	<colgroup style="background:#2FB6EF"></colgroup>
	<tr>
	<td style="color: white;font-size: 16px;padding-left: 18px;font-weight: bold;">白条账单</td>
	</tr>			
	</table>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
	<tr>
				<th style="text-align:center; width:15%;height:25px ">日期</th>
				<th style="text-align:center; width:25% ">账单金额</th>
				<th style="text-align:center; width:15% ">账单状态</th>
				<th style="text-align:center; width:25% ">剩余待还</th>
				<th style="text-align:center; width:25% ">最后还款日</th>
	</tr>
	<c:forEach items="${baitiao_bill_list}" var="li" varStatus="status">
			<tr>
				<th style="text-align:center; width:15%;height:25px ">${li.get("month")}</th>
				<th style="text-align:center; width:25%  ">${li.get("total_amount")/100}</th>
				<th style="text-align:center; width:15% ">${li.get("bill_status")}</th>
				<th style="text-align:center; width:25% ">${li.get("remain_amount")/100}</th>
			    <th style="text-align:center; width:25% ">${li.get("payment_deadline")}</th>
			</tr>
  		</c:forEach>	
	</table>
	<table width="100%" style="height: 40px" border="1" bordercolor="white" cellspadding="5" cellspacing="0"> 
	<colgroup style="background:#2FB6EF"></colgroup>
	<tr>
	<td style="color: white;font-size: 16px;padding-left: 18px;font-weight: bold;">绑定银行卡信息</td>
	</tr>			
	</table>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
	<tr>
				<th style="text-align:center; width:15%;height:25px ">姓名</th>
				<th style="text-align:center; width:25% ">银行卡尾号</th>
				<th style="text-align:center; width:15% ">银行卡类型</th>
				<th style="text-align:center; width:25% ">发卡银行名称</th>
	</tr>
	<c:forEach items="${base_info.binded_bank_cards}" var="li" varStatus="status">
			<tr>
				<th style="text-align:center; width:15%;height:25px ">${li.get("card_owner")}</th>
				<th style="text-align:center; width:25%  ">${li.get("tail_number")}</th>
				<th style="text-align:center; width:15% ">${li.get("credit_type")}</th>
			    <th style="text-align:center; width:25% ">${li.get("bank_name")}</th>
			</tr>
  		</c:forEach>	
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
				<th style="text-align:center; width:10%;height:25px ">订单金额</th>
				<th style="text-align:center; width:10% ">收货人</th>
				<th style="text-align:center; width:65% ">商品名称</th>
				<th style="text-align:center; width:15% ">订单时间</th>
	</tr>
	<c:forEach items="${order_list}" var="li" varStatus="status">
			<tr>
				<th style="text-align:center; width:10%;height:25px ">${li.get("order_amount")/100}</th>
				<th style="text-align:center; width:10%  ">${li.get("receiver_name")}</th>
				<th style="text-align:center; width:65% ">${li.get("product_name")}</th>
				<th style="text-align:center; width:15% ">${li.get("order_time")}</th>
			</tr>
  		</c:forEach>
			
	</table>
    <div class="pagination">${page}</div>
</body>
</html>