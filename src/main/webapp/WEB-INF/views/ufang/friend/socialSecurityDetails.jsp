<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>社保详情</title>
	<meta name="decorator" content="default"/>
    <script src="${ctxStatic}/artdialog/js/dialog-plus.js"></script>
    <link href="${ctxStatic}/artdialog/css/dialog.css" rel="stylesheet" />
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
	<ul class="nav nav-tabs">
		<li class="active">话费记录</li>
	</ul>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			
		</thead>
			
		<tbody>
			<tr>
				<td style="text-align:center;font-weight:bold;background-color:#eae0e0;" colspan ="5">养老</td>
			</tr>
			<tr>
				<td style="text-align:center;font-weight:bold">缴纳时间</td>
				<td style="text-align:center;font-weight:bold">缴纳基数</td>
				<td style="text-align:center;font-weight:bold">单位缴存</td>
				<td style="text-align:center;font-weight:bold">个人缴存</td>
				<td style="text-align:center;font-weight:bold">缴费状态</td>
			</tr>
			<c:forEach items="${listMapFromJsonYaL }" var="list1">
				<tr>
					<td style="text-align:center">${list1.month}</td>
					<td style="text-align:center">${list1.jishu}</td>
					<td style="text-align:center">${list1.com}</td>
					<td style="text-align:center">${list1.ge1}</td>
					<td style="text-align:center">${list1.type}</td>
				</tr>
			</c:forEach>
			<tr>
				<td style="text-align:center;font-weight:bold;background-color:#eae0e0;" colspan ="5">医疗</td>
			</tr>
			<tr>
				<td style="text-align:center;font-weight:bold">缴纳时间</td>
				<td style="text-align:center;font-weight:bold">缴纳基数</td>
				<td style="text-align:center;font-weight:bold">单位缴存</td>
				<td style="text-align:center;font-weight:bold">个人缴存</td>
				<td style="text-align:center;font-weight:bold">缴费状态</td>
			</tr>
			<c:forEach items="${listMapFromJsonYiL }" var="list2">
				<tr>
					<td style="text-align:center">${list2.month}</td>
					<td style="text-align:center">${list2.jishu}</td>
					<td style="text-align:center">${list2.com}</td>
					<td style="text-align:center">${list2.ge1}</td>
					<td style="text-align:center">${list2.type}</td>
				</tr>
			</c:forEach>
			<tr>
				<td style="text-align:center;font-weight:bold;background-color:#eae0e0;" colspan ="5">事业</td>
			</tr>
			<tr>
				<td style="text-align:center;font-weight:bold">缴纳时间</td>
				<td style="text-align:center;font-weight:bold">缴纳基数</td>
				<td style="text-align:center;font-weight:bold">单位缴存</td>
				<td style="text-align:center;font-weight:bold">个人缴存</td>
				<td style="text-align:center;font-weight:bold">缴费状态</td>
			</tr>
			<c:forEach items="${listMapFromJsonSiY }" var="list3">
				<tr>
					<td style="text-align:center">${list3.month}</td>
					<td style="text-align:center">${list3.jishu}</td>
					<td style="text-align:center">${list3.com}</td>
					<td style="text-align:center">${list3.ge1}</td>
					<td style="text-align:center">${list3.type}</td>
				</tr>
			</c:forEach>
			<tr>
				<td style="text-align:center;font-weight:bold;background-color:#eae0e0;" colspan ="5">工伤</td>
			</tr>
			<tr>
				<td style="text-align:center;font-weight:bold">缴纳时间</td>
				<td style="text-align:center;font-weight:bold">缴纳基数</td>
				<td style="text-align:center;font-weight:bold">单位缴存</td>
				<td style="text-align:center;font-weight:bold">个人缴存</td>
				<td style="text-align:center;font-weight:bold">缴费状态</td>
			</tr>
			<c:forEach items="${listMapFromJsonGoS }" var="list4">
				<tr>
					<td style="text-align:center">${list4.month}</td>
					<td style="text-align:center">${list4.jishu}</td>
					<td style="text-align:center">${list4.com}</td>
					<td style="text-align:center">${list4.ge1}</td>
					<td style="text-align:center">${list4.type}</td>
				</tr>
			</c:forEach>
			<tr>
				<td style="text-align:center;font-weight:bold;background-color:#eae0e0;" colspan ="5">生育</td>
			</tr>
			<tr>
				<td style="text-align:center;font-weight:bold">缴纳时间</td>
				<td style="text-align:center;font-weight:bold">缴纳基数</td>
				<td style="text-align:center;font-weight:bold">单位缴存</td>
				<td style="text-align:center;font-weight:bold">个人缴存</td>
				<td style="text-align:center;font-weight:bold">缴费状态</td>
			</tr>
			<c:forEach items="${listMapFromJsonSeY }" var="list5">
				<tr>
					<td style="text-align:center">${list5.month}</td>
					<td style="text-align:center">${list5.jishu}</td>
					<td style="text-align:center">${list5.com}</td>
					<td style="text-align:center">${list5.ge1}</td>
					<td style="text-align:center">${list5.type}</td>
				</tr>
			</c:forEach>
			
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>