<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>催收明细管理</title>
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
		<li class="active"><a href="javascript:;">催收明细列表</a>
		</li>
	</ul>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th style="text-align:center">借条编号</th>
				<th style="text-align:center">催收类型</th>
				<th style="text-align:center">催收状态</th>
				<th style="text-align:center">备注</th>
				<th style="text-align:center">创建时间</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="nfsLoanCollectionDetail">
			<tr>
				<td style="text-align:center">
					${nfsLoanCollectionDetail.collectionId}
				</td>
				<td style="text-align:center">
					${fns:getDictLabel(nfsLoanCollectionDetail.type, 'collectionDetailType', '')}
				</td>
				<td style="text-align:center">
					${fns:getDictLabel(nfsLoanCollectionDetail.status, 'collectionDetailStatus', '')}
				</td>
				<td style="text-align:center">
					${nfsLoanCollectionDetail.rmk}
				</td>
				<td style="text-align:center">
					<fmt:formatDate value="${nfsLoanCollectionDetail.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>