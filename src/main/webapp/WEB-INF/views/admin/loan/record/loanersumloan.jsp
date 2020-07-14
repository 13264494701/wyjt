<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>借条记录管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			
		});
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${admin}/nfsLoanRecord/loanersumloan">放款统计</a></li>		
	</ul>
	<form:form id="searchForm" modelAttribute="nfsLoanRecord" action="${admin}/nfsLoanRecord/loanersumloan" method="post" class="breadcrumb form-search">
		<ul class="ul-form">
			<li><label style="text-align:right;width:150px">放款人手机号：</label>
				<form:input path="loaner.username" htmlEscape="false" maxlength="64" class="input-medium"/>
			</li>
			<li><label style="text-align:right;width:80px">放款时间：</label>
				<input name="beginTime" type="text"  maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${nfsLoanRecord.beginTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',startDate:'%y-%M-%d 00:00:00',isShowClear:true});"/> - 
				<input name="endTime" type="text"  maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${nfsLoanRecord.endTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',startDate:'%y-%M-%d 00:00:00',isShowClear:true});"/>
			</li>	
			<li class="btns"><label style="text-align:right;width:50px"></label><input id="btnSubmit" class="btn btn-primary " style="width:80px;" type="submit" value="查   询"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th style="text-align:center">开始时间</th>
				<th style="text-align:center">结束时间</th>
				<th style="text-align:center">放款人手机号</th>
				<th style="text-align:center">放款笔数</th>
				<th style="text-align:center">放款金额</th>				
			</tr>
		</thead>
		<tbody>
			<tr>
				<td style="text-align:center">
					<fmt:formatDate value="${beginDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td style="text-align:center">
					<fmt:formatDate value="${endDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td style="text-align:center">
					${username}
				</td>
				<td style="text-align:center">
					${totalQuantity}笔
				</td>
				<td style="text-align:center">
					${fns:decimalToStr(totalAmount,2)}元
				</td>
			</tr>
		</tbody>
	</table>
</body>
</html>