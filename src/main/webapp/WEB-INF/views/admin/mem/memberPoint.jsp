<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>会员积分</title>
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
		<li><a href="${ctx}/member/querybymemno?memberNo=${memberNo}">会员查看</a></li>
		<li class="active"><a href="${ctx}/member/point?memberNo=${memberNo}">会员积分</a></li>
	</ul>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th style="text-align:center">会员编号</th>
				<th style="text-align:center">积分余额</th>
				<th style="text-align:center">累计积分</th>
				<th style="text-align:center">扣除积分</th>
				<th style="text-align:center">操作</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="memberPoint">
			<tr>
				<td style="text-align:center">
					${memberPoint.memberNo}
				</td>
				<td style="text-align:center">
					${memberPoint.balancePoints}
				</td>
				<td style="text-align:center">
					${memberPoint.totalPoints}
				</td>
				<td style="text-align:center">
					${memberPoint.reducePoints}
				</td>
				<td  style="text-align:center">
				    <a href="${ctx}/member/memberPointDetail/list?memberNo=${memberPoint.memberNo}">积分明细</a>
	
					<a href="${ctx}/member/memberPoint/update?memNo=${memberPoint.memberNo}&redirectUrl=member">积分调整</a>
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>