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
    <h2>学信网信息</h2><br>
	<sys:message content="${message}"/>
	<c:forEach items="${school_info}" var="li" varStatus="status">
	<table width="100%" style="height: 40px" border="1" bordercolor="white" cellspadding="5" cellspacing="0"> 
	<colgroup style="background:#2FB6EF"></colgroup>
	<tr>
	<td style="color: white;font-size: 16px;padding-left: 18px;font-weight: bold;">学籍信息</td>
	</tr>			
	</table>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
	<tr>
				<th style="text-align:center; width:15%;height:25px ">姓名</th>
				<th style="text-align:center; width:25% ">${li.get("realname")}</th>
				<th style="text-align:center; width:15% ">院校信息</th>
				<th style="text-align:center; width:25% ">${li.get("school")}</th>
	</tr>
	<tr>
				<th style="text-align:center; width:15%;height:25px ">性别</th>
				<th style="text-align:center; width:25% ">${li.get("gender")}</th>
				<th style="text-align:center; width:15% ">分院</th>
				<th style="text-align:center; width:25% ">${li.get("college")}</th>
	</tr>
	<tr>
				<th style="text-align:center; width:15%;height:25px ">证件号</th>
				<th style="text-align:center; width:25% ">${li.get("card_id")}</th>
				<th style="text-align:center; width:15% ">系</th>
				<th style="text-align:center; width:25% ">${li.get("department")}</th>
	</tr>
	<tr>
				<th style="text-align:center; width:15%;height:25px ">民族</th>
				<th style="text-align:center; width:25% ">${li.get("nation")}</th>
				<th style="text-align:center; width:15% ">专业名称</th>
				<th style="text-align:center; width:25% ">${li.get("major")}</th>
	</tr>
	<tr>
				<th style="text-align:center; width:15%;height:25px ">出生年月</th>
				<th style="text-align:center; width:25% ">${li.get("birthday")}</th>
				<th style="text-align:center; width:15% ">考生号</th>
				<th style="text-align:center; width:25% ">${li.get("examination_id")}</th>
	</tr>
	<tr>
				<th style="text-align:center; width:15%;height:25px ">层次</th>
				<th style="text-align:center; width:25% ">${li.get("edu_level")}</th>
				<th style="text-align:center; width:15% ">学号</th>
				<th style="text-align:center; width:25% ">${li.get("student_id")}</th>
	</tr>
	<tr>
				<th style="text-align:center; width:15%;height:25px ">学制</th>
				<th style="text-align:center; width:25% ">${li.get("edu_system")}</th>
				<th style="text-align:center; width:15% ">班级</th>
				<th style="text-align:center; width:25% ">${li.get("classname")}</th>
	</tr>
	<tr>
				<th style="text-align:center; width:15%;height:25px ">学历类别</th>
				<th style="text-align:center; width:25% ">${li.get("edu_type")}</th>
				<th style="text-align:center; width:15% ">入学日期</th>
				<th style="text-align:center; width:25% ">${li.get("entrance_date")}</th>
	</tr>
	<tr>
				<th style="text-align:center; width:15%;height:25px ">学习形式</th>
				<th style="text-align:center; width:25% ">${li.get("edu_form")}</th>
				<th style="text-align:center; width:15% ">离校日期</th>
				<th style="text-align:center; width:25% ">${li.get("graduate_date")}</th>
	</tr>
	<tr>
				<th style="text-align:center; width:15%;height:25px ">学籍状态</th>
				<th style="text-align:center; width:25% ">${li.get("status")}</th>
				<th style="text-align:center; width:15% "></th>
				<th style="text-align:center; width:25% "></th>
	</tr>
	</table>
	</c:forEach>
	<c:choose>  
       <c:when test="${school_info == null}">
       <table width="100%" style="height: 40px" border="1" bordercolor="white" cellspadding="5" cellspacing="0"> 
	<colgroup style="background:#2FB6EF"></colgroup>
	<tr>
	<td style="color: white;font-size: 16px;padding-left: 18px;font-weight: bold;">学籍信息</td>
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
    <c:forEach items="${education_info}" var="li" varStatus="status">
	<table width="100%" style="height: 40px" border="1" bordercolor="white" cellspadding="5" cellspacing="0"> 
	<colgroup style="background:#2FB6EF"></colgroup>
	<tr>
	<td style="color: white;font-size: 16px;padding-left: 18px;font-weight: bold;">学历信息</td>
	</tr>			
	</table>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
	<tr>
				<th style="text-align:center; width:15%;height:25px ">姓名</th>
				<th style="text-align:center; width:25% ">${li.get("realname")}</th>
				<th style="text-align:center; width:15% ">院校信息</th>
				<th style="text-align:center; width:25% ">${li.get("school")}</th>
	</tr>
	<tr>
				<th style="text-align:center; width:15%;height:25px ">性别</th>
				<th style="text-align:center; width:25% ">${li.get("gender")}</th>
				<th style="text-align:center; width:15% ">专业名称</th>
				<th style="text-align:center; width:25% ">${li.get("major")}</th>
	</tr>
	<tr>
				<th style="text-align:center; width:15%;height:25px ">出生年月</th>
				<th style="text-align:center; width:25% ">${li.get("birthday")}</th>
				<th style="text-align:center; width:15%;height:25px ">学历类别</th>
				<th style="text-align:center; width:25% ">${li.get("edu_type")}</th>
	</tr>
	<tr>
				<th style="text-align:center; width:15%;height:25px ">证书编号</th>
				<th style="text-align:center; width:25% ">${li.get("certificate_id")}</th>
				<th style="text-align:center; width:15% ">院校所在地</th>
				<th style="text-align:center; width:25% ">${li.get("location")}</th>
	</tr>
		<tr>
				<th style="text-align:center; width:15% ">离校日期</th>
				<th style="text-align:center; width:25% ">${li.get("graduate_date")}</th>
				<th style="text-align:center; width:15% ">入学日期</th>
				<th style="text-align:center; width:25% ">${li.get("entrance_date")}</th>
	</tr>
	<tr>
				<th style="text-align:center; width:15%;height:25px ">毕业结论</th>
				<th style="text-align:center; width:25% ">${li.get("edu_conclusion")}</th>
				<th style="text-align:center; width:15% "></th>
				<th style="text-align:center; width:25% "></th>
	</tr>
	
	    			
	</table>
	</c:forEach>
	<c:choose>  
       <c:when test="${education_info == null}">
       <table width="100%" style="height: 40px" border="1" bordercolor="white" cellspadding="5" cellspacing="0"> 
	<colgroup style="background:#2FB6EF"></colgroup>
	<tr>
	<td style="color: white;font-size: 16px;padding-left: 18px;font-weight: bold;">学历信息</td>
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
    <div class="pagination">${page}</div>
</body>
</html>