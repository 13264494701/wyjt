<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>会员账户交易管理</title>
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
	<ul class="nav nav-tabs">
		<li class="active"><a href="${admin}/memberActTrx/list?member.id=${memberActTrx.member.id}&subNo=${memberActTrx.subNo}">会员账户交易列表</a></li>		
	</ul>
	<form:form id="searchForm" modelAttribute="memberActTrx" action="${admin}/memberActTrx/list?member.id=${memberActTrx.member.id}&subNo=${memberActTrx.subNo}" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
			<th style="text-align:center">会员ID</th>
			<th style="text-align:center">标题</th>
			<th style="text-align:center">交易金额</th>
			<th style="text-align:center">账户余额</th>
			<th style="text-align:center">记账方向</th>
			<th style="text-align:center">交易状态</th>
			<th style="text-align:center">备注</th>
			<th style="text-align:center">生成时间</th>					
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="memberActTrx">
			<tr>
				<td style="text-align:center">
					${memberActTrx.member.id}
				</td>
				<td style="text-align:center">
					${memberActTrx.title}
				</td>
				<td style="text-align:center">
					${fns:decimalToStr(memberActTrx.trxAmt,2)}
				</td>
				<td style="text-align:center">
					${fns:decimalToStr(memberActTrx.curBal,2)}
				</td>
				<td style="text-align:center">
					${fns:getDictLabel(memberActTrx.drc,'drc','')}
				</td>
				<td style="text-align:center">
					${fns:getDictLabel(memberActTrx.status,'trxSts','')}
				</td>
				<td style="text-align:center">
					${memberActTrx.rmk}
				</td>
				<td style="text-align:center">
					<fmt:formatDate value="${memberActTrx.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>