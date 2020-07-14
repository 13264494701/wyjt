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
    <h2>社保信息</h2><br>
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
				<th style="text-align:center; width:25%  ">${user_info.get("name")}</th>
				<th style="text-align:center; width:15% ">身份证号</th>
				<th style="text-align:center; width:25% ">${user_info.get("certificate_number")}</th>
			</tr>
			<tr>
				<th style="text-align:center; width:15%;height:25px  ">单位名称</th>
				<th style="text-align:center; width:25%  ">${user_info.get("company_name")}</th>
				<th style="text-align:center; width:15% ">起缴日</th>
				<th style="text-align:center; width:25% ">${user_info.get("begin_date")}</th>
			</tr>
	</table>
	<table width="100%" style="height: 40px" border="1" bordercolor="white" cellspadding="5" cellspacing="0"> 
	<colgroup style="background:#2FB6EF"></colgroup>
	<tr>
	<td style="color: white;font-size: 16px;padding-left: 18px;font-weight: bold;">累计缴费</td>
	</tr>			
	</table>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
			<tr>
				<th style="text-align:center; width:15%;height:25px ">险种</th>
				<th style="text-align:center; width:25%  ">账户余额</th>
				<th style="text-align:center; width:15% ">余额截止时间</th>
				<th style="text-align:center; width:25% ">账号状态</th>
			</tr>
			<tr>
				<th style="text-align:center; width:15%;height:25px  ">养老保险</th>
				<th style="text-align:center; width:25%  ">${endowment_overview.get("balance")/100}</th>
				<th style="text-align:center; width:15% ">${endowment_overview.get("end_date")}</th>
				<th style="text-align:center; width:25% ">${user_info.get("type")}</th>
			</tr>
			<tr>
				<th style="text-align:center; width:15%;height:25px  ">医疗保险</th>
				<th style="text-align:center; width:25%  ">${medical_overview.get("balance")/100}</th>
				<th style="text-align:center; width:15% ">${medical_overview.get("end_date")}</th>
				<th style="text-align:center; width:25% ">${user_info.get("type")}</th>
			</tr>
			<tr>
				<th style="text-align:center; width:15%;height:25px  ">失业保险</th>
				<th style="text-align:center; width:25%  ">${unemployment_overview.get("balance")/100}</th>
				<th style="text-align:center; width:15% ">${unemployment_overview.get("end_date")}</th>
				<th style="text-align:center; width:25% ">${user_info.get("type")}</th>
			</tr>
			<tr>
				<th style="text-align:center; width:15%;height:25px  ">工伤保险</th>
				<th style="text-align:center; width:25%  ">${accident_overview.get("balance")/100}</th>
				<th style="text-align:center; width:15% ">${accident_overview.get("end_date")}</th>
				<th style="text-align:center; width:25% ">${user_info.get("type")}</th>
			</tr>
			<tr>
				<th style="text-align:center; width:15%;height:25px  ">生育保险</th>
				<th style="text-align:center; width:25%  ">${maternity_overview.get("balance")/100}</th>
				<th style="text-align:center; width:15% ">${maternity_overview.get("end_date")}</th>
				<th style="text-align:center; width:25% ">${user_info.get("type")}</th>
			</tr>
	</table>
	<table width="100%" style="height: 40px" border="1" bordercolor="white" cellspadding="5" cellspacing="0"> 
	<colgroup style="background:#2FB6EF"></colgroup>
	<tr>
	<td style="color: white;font-size: 16px;padding-left: 18px;font-weight: bold;">养老保险存缴明细</td>
	</tr>			
	</table>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
	<tr>
				<th style="text-align:center; width:15%;height:25px ">日期</th>
				<th style="text-align:center; width:25% ">个人存缴金额</th>
				<th style="text-align:center; width:15% ">存缴基数</th>
				<th style="text-align:center; width:25% ">单位名称</th>
	</tr>
	<c:forEach items="${endowment_insurance}" var="li" varStatus="status">
			<tr>
				<th style="text-align:center; width:15%;height:25px ">${li.get("month")}</th>
				<th style="text-align:center; width:25% ">${li.get("monthly_personal_income")/100}</th>
					<th style="text-align:center; width:25%  ">${li.get("base_number")/100}</th>
				<th style="text-align:center; width:25% ">${li.get("company_name")}</th>
			</tr>
  		</c:forEach>
			
	</table>
	  <table width="100%" style="height: 40px" border="1" bordercolor="white" cellspadding="5" cellspacing="0"> 
	<colgroup style="background:#2FB6EF"></colgroup>
	<tr>
	<td style="color: white;font-size: 16px;padding-left: 18px;font-weight: bold;">医疗保险存缴明细</td>
	</tr>			
	</table>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
	<tr>
				<th style="text-align:center; width:15%;height:25px ">日期</th>
				<th style="text-align:center; width:25% ">个人存缴金额</th>
				<th style="text-align:center; width:15% ">存缴基数</th>
				<th style="text-align:center; width:25% ">单位名称</th>
	</tr>
	<c:forEach items="${medical_insurance}" var="li" varStatus="status">
			<tr>
				<th style="text-align:center; width:15%;height:25px ">${li.get("month")}</th>
				<th style="text-align:center; width:25% ">${li.get("monthly_personal_income")/100}</th>
					<th style="text-align:center; width:25%  ">${li.get("base_number")/100}</th>
				<th style="text-align:center; width:25% ">${li.get("company_name")}</th>
			</tr>
  		</c:forEach>
			
	</table>
	  <table width="100%" style="height: 40px" border="1" bordercolor="white" cellspadding="5" cellspacing="0"> 
	<colgroup style="background:#2FB6EF"></colgroup>
	<tr>
	<td style="color: white;font-size: 16px;padding-left: 18px;font-weight: bold;">失业保险存缴明细</td>
	</tr>			
	</table>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
	<tr>
				<th style="text-align:center; width:15%;height:25px ">日期</th>
				<th style="text-align:center; width:25% ">个人存缴金额</th>
				<th style="text-align:center; width:15% ">存缴基数</th>
				<th style="text-align:center; width:25% ">单位名称</th>
	</tr>
	<c:forEach items="${unemployment_insurance}" var="li" varStatus="status">
			<tr>
				<th style="text-align:center; width:15%;height:25px ">${li.get("month")}</th>
				<th style="text-align:center; width:25% ">${li.get("monthly_personal_income")/100}</th>
					<th style="text-align:center; width:25%  ">${li.get("base_number")/100}</th>
				<th style="text-align:center; width:25% ">${li.get("company_name")}</th>
			</tr>
  		</c:forEach>
			
	</table>
	  <table width="100%" style="height: 40px" border="1" bordercolor="white" cellspadding="5" cellspacing="0"> 
	<colgroup style="background:#2FB6EF"></colgroup>
	<tr>
	<td style="color: white;font-size: 16px;padding-left: 18px;font-weight: bold;">工伤保险存缴明细</td>
	</tr>			
	</table>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
	<tr>
				<th style="text-align:center; width:15%;height:25px ">日期</th>
				<th style="text-align:center; width:25% ">个人存缴金额</th>
				<th style="text-align:center; width:15% ">存缴基数</th>
				<th style="text-align:center; width:25% ">单位名称</th>
	</tr>
	<c:forEach items="${accident_insurance}" var="li" varStatus="status">
			<tr>
				<th style="text-align:center; width:15%;height:25px ">${li.get("month")}</th>
				<th style="text-align:center; width:25% ">${li.get("monthly_personal_income")/100}</th>
					<th style="text-align:center; width:25%  ">${li.get("base_number")/100}</th>
				<th style="text-align:center; width:25% ">${li.get("company_name")}</th>
			</tr>
  		</c:forEach>
			
	</table>
	  <table width="100%" style="height: 40px" border="1" bordercolor="white" cellspadding="5" cellspacing="0"> 
	<colgroup style="background:#2FB6EF"></colgroup>
	<tr>
	<td style="color: white;font-size: 16px;padding-left: 18px;font-weight: bold;">生育保险存缴明细</td>
	</tr>			
	</table>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
	<tr>
				<th style="text-align:center; width:15%;height:25px ">日期</th>
				<th style="text-align:center; width:25% ">个人存缴金额</th>
				<th style="text-align:center; width:15% ">存缴基数</th>
				<th style="text-align:center; width:25% ">单位名称</th>
	</tr>
	<c:forEach items="${maternity_insurance}" var="li" varStatus="status">
			<tr>
				<th style="text-align:center; width:15%;height:25px ">${li.get("month")}</th>
				<th style="text-align:center; width:25% ">${li.get("monthly_personal_income")/100}</th>
					<th style="text-align:center; width:25%  ">${li.get("base_number")/100}</th>
				<th style="text-align:center; width:25% ">${li.get("company_name")}</th>
			</tr>
  		</c:forEach>
			
	</table>
    <div class="pagination">${page}</div>
</body>
</html>