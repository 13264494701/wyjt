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
    <h2>银行卡信息</h2><br>
	<sys:message content="${message}"/>
	<c:forEach items="${debit_card_accounts}" var="li" varStatus="status">
	<table width="100%" style="height: 40px" border="1" bordercolor="white" cellspadding="5" cellspacing="0"> 
	<colgroup style="background:#2FB6EF"></colgroup>
	<tr>
	<td style="color: white;font-size: 16px;padding-left: 18px;font-weight: bold;">账户信息</td>
	</tr>			
	</table>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
	<tr>
				<th style="text-align:center; width:15%;height:25px ">账号</th>
				<th style="text-align:center; width:25% ">${li.get("account")}</th>
				<th style="text-align:center; width:15% ">账户类型</th>
				<th style="text-align:center; width:25% ">${li.get("type")}</th>
	</tr>
	<tr>
				<th style="text-align:center; width:15%;height:25px ">户名</th>
				<th style="text-align:center; width:25% ">${li.get("name")}</th>
				<th style="text-align:center; width:15% ">账户状态</th>
				<th style="text-align:center; width:25% ">${li.get("status")}</th>
	</tr>
	<tr>
				<th style="text-align:center; width:15%;height:25px ">开户行</th>
				<th style="text-align:center; width:25% ">${li.get("deposit_bank")}</th>
				<th style="text-align:center; width:15% ">开户日期</th>
				<th style="text-align:center; width:25% ">${li.get("open_date")}</th>
	</tr>
	<tr>
				<th style="text-align:center; width:15%;height:25px ">账户地区</th>
				<th style="text-align:center; width:25% ">${li.get("area")}</th>
				<th style="text-align:center; width:15% "></th>
				<th style="text-align:center; width:25% "></th>
	</tr>
	</table>
	</c:forEach>
	<c:choose>  
       <c:when test="${debit_card_accounts == null}">
       <table width="100%" style="height: 40px" border="1" bordercolor="white" cellspadding="5" cellspacing="0"> 
	<colgroup style="background:#2FB6EF"></colgroup>
	<tr>
	<td style="color: white;font-size: 16px;padding-left: 18px;font-weight: bold;">账户信息</td>
	</tr>			
	</table>
	 <table width="100%" style="height: 40px" border="1" bordercolor="white" cellspadding="5" cellspacing="0"> 
	<tr>
	<td style="color: white;font-size: 16px;align-items: cnter">暂无数据</td>
	</tr>			
	</table>
       </c:when> 
       <c:otherwise>  
       </c:otherwise>  
    </c:choose>
    <c:forEach items="${account_detail}" var="li" varStatus="status">
	<table width="100%" style="height: 40px" border="1" bordercolor="white" cellspadding="5" cellspacing="0"> 
	<colgroup style="background:#2FB6EF"></colgroup>
	<tr>
	<td style="color: white;font-size: 16px;padding-left: 18px;font-weight: bold;">子账号账户信息</td>
	</tr>			
	</table>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
	<tr>
				<th style="text-align:center; width:15%;height:25px ">币种</th>
				<th style="text-align:center; width:25% ">${li.get("currency")}</th>
				<th style="text-align:center; width:15% ">账户余额</th>
				<th style="text-align:center; width:25% ">${li.get("balance")/100}</th>
	</tr>
	<tr>
				<th style="text-align:center; width:15%;height:25px ">可用余额</th>
				<th style="text-align:center; width:25% ">${li.get("available_balance")/100}</th>
				<th style="text-align:center; width:15% "></th>
				<th style="text-align:center; width:25% "></th>
	</tr>
	</table>
	</c:forEach>
	<c:choose>  
       <c:when test="${account_detail == null}">
       <table width="100%" style="height: 40px" border="1" bordercolor="white" cellspadding="5" cellspacing="0"> 
	<colgroup style="background:#2FB6EF"></colgroup>
	<tr>
	<td style="color: white;font-size: 16px;padding-left: 18px;font-weight: bold;">子账号信息</td>
	</tr>			
	</table>
	 <table width="100%" style="height: 40px" border="1" bordercolor="white" cellspadding="5" cellspacing="0"> 
	<tr>
	<td style="color: white;font-size: 16px;align-items: cnter">暂无数据</td>
	</tr>			
	</table>
       </c:when> 
       <c:otherwise>  
       </c:otherwise>  
    </c:choose>
	<table width="100%" style="height: 40px" border="1" bordercolor="white" cellspadding="5" cellspacing="0"> 
	<colgroup style="background:#2FB6EF"></colgroup>
	<tr>
	<td style="color: white;font-size: 16px;padding-left: 18px;font-weight: bold;">交易明细</td>
	</tr>			
	</table>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
	<tr>
				<th style="text-align:center; width:15%;height:25px ">交易日期</th>
				<th style="text-align:center; width:15% ">金额</th>
				<th style="text-align:center; width:15%;height:25px ">币种</th>
				<th style="text-align:center; width:15% ">账户余额</th>
				<th style="text-align:center; width:15%;height:25px ">对方信息</th>
				<th style="text-align:center; width:15% ">交易类型</th>
				<th style="text-align:center; width:15%;height:25px ">交易备注</th>
	</tr>
	 <c:forEach items="${sub_accounts}" var="li" varStatus="status">
	<tr>
	<th style="text-align:center; width:25% ">${li.get("trade_date")}</th>
				<th style="text-align:center; width:25% ">${li.get("income")/100}</th>
				<th style="text-align:center; width:25% ">${li.get("currency")}</th>
				<th style="text-align:center; width:25% ">${li.get("balance")/100}</th>
				<th style="text-align:center; width:25% ">${li.get("counterpart")}</th>
				<th style="text-align:center; width:25% ">${li.get("trade_type")}</th>
				<th style="text-align:center; width:25% ">${li.get("remark")}</th>						
	</tr>
	</c:forEach>
	</table>
    <div class="pagination">${page}</div>
</body>
</html>