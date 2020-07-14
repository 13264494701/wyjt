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
    <h2>公积金信息</h2><br>
	<sys:message content="${message}"/>
	<table width="100%" style="height: 40px" border="1" bordercolor="white" cellspadding="5" cellspacing="0"> 
	<colgroup style="background:#2FB6EF"></colgroup>
	<tr>
	<td style="color: white;font-size: 16px;padding-left: 18px;font-weight: bold;">基本信息</td>
	</tr>			
	</table>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
	<tr>
				<th style="text-align:center; width:15%;height:25px  ">公积金账号</th>
				<th style="text-align:center; width:25%  ">${base_info.get("cust_no")}</th>
				<th style="text-align:center; width:15% "></th>
				<th style="text-align:center; width:25% "></th>
			</tr>
			<tr>
				<th style="text-align:center; width:15%;height:25px ">真实姓名</th>
				<th style="text-align:center; width:25%  ">${base_info.get("name")}</th>
				<th style="text-align:center; width:15% ">身份证号</th>
				<th style="text-align:center; width:25% ">${base_info.get("cert_no")}</th>
			</tr>
			<tr>
				<th style="text-align:center; width:15%;height:25px  ">单位名称</th>
				<th style="text-align:center; width:25%  ">${base_info.get("corp_name")}</th>
				<th style="text-align:center; width:15% ">账户余额</th>
				<th style="text-align:center; width:25% ">${base_info.get("balance")/100}</th>
			</tr>
			<tr>
				<th style="text-align:center; width:15%;height:25px  ">月缴存</th>
				<th style="text-align:center; width:25%  ">${base_info.get("monthly_total_income")/100}</th>
				<th style="text-align:center; width:15% ">缴存基数</th>
				<th style="text-align:center; width:25% ">${base_info.get("base_number")/100}</th>
			</tr>
			<tr>
				<th style="text-align:center; width:15%;height:25px  ">末次缴存年月</th>
				<th style="text-align:center; width:25%  ">${base_info.get("last_pay_date")}</th>
				<th style="text-align:center; width:15% ">缴存状态</th>
				<th style="text-align:center; width:25% ">${base_info.get("pay_status_desc")}</th>
			</tr>
			<tr>
				<th style="text-align:center; width:15%;height:25px  ">开户日期</th>
				<th style="text-align:center; width:25%  ">${base_info.get("begin_date")}</th>
				<th style="text-align:center; width:15% ">地区</th>
				<th style="text-align:center; width:25% ">${base_info.get("registed")}</th>
			</tr>
	</table>
	  <table width="100%" style="height: 40px" border="1" bordercolor="white" cellspadding="5" cellspacing="0"> 
	<colgroup style="background:#2FB6EF"></colgroup>
	<tr>
	  <table width="100%" style="height: 40px" border="1" bordercolor="white" cellspadding="5" cellspacing="0"> 
	<colgroup style="background:#2FB6EF"></colgroup>
	<tr>
	<td style="color: white;font-size: 16px;padding-left: 18px;font-weight: bold;">存缴明细</td>
	</tr>			
	</table>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
	<tr>
				<th style="text-align:center; width:15%;height:25px ">日期</th>
				<th style="text-align:center; width:25% ">单位名称</th>
				<th style="text-align:center; width:15% ">缴存金额</th>
				<th style="text-align:center; width:25% ">业务描述</th>
	</tr>
	<c:forEach items="${bill_record}" var="li" varStatus="status">
			<tr>
				<th style="text-align:center; width:15%;height:25px ">${li.get("month")}</th>
				<th style="text-align:center; width:25% ">${li.get("corp_name")}</th>
					<th style="text-align:center; width:25%  ">${li.get("income")/100}</th>
				<th style="text-align:center; width:25% ">${li.get("desc")}</th>
			</tr>
  		</c:forEach>	
	</table>
    <div class="pagination">${page}</div>
</body>
</html>