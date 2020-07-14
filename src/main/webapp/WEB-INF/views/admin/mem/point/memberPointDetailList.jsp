<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>积分明细管理</title>
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
		<li class="active"><a href="${ctx}/member/memberPointDetail/">积分明细列表</a></li>
	</ul>
	<form:form id="searchForm" modelAttribute="memberPointDetail" action="${ctx}/member/memberPointDetail/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label style="text-align:right;width:100px">会员编号：</label>
				<form:input path="memberNo" htmlEscape="false" maxlength="16" class="input-medium"/>
			</li>
			<li><label style="text-align:right;width:100px">交易编号：</label>
				<form:input path="trxNo" htmlEscape="false" maxlength="16" class="input-medium"/>
			</li>

			<li class="btns"><label style="text-align:right;width:50px"></label><input id="btnSubmit" class="btn btn-primary " style="width:80px;" type="submit" value="查   询"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th style="text-align:center">会员编号</th>
				<th style="text-align:center">交易类型</th>			
				<th style="text-align:center">获取积分</th>
				<th style="text-align:center">扣除积分</th>
				<th style="text-align:center">当前积分</th>
				<th style="text-align:center">交易编号</th>
				<th style="text-align:center">生成日期</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="memberPointDetail">
			<tr>
				<td style="text-align:center">
					${memberPointDetail.memberNo}
				</td>
				<td style="text-align:center">
					${fns:getDictLabel(memberPointDetail.type, 'pointGetType', '')}
				</td>
				<td style="text-align:center">
					${memberPointDetail.creditPoints}
				</td>
				<td style="text-align:center">
					${memberPointDetail.debitPoints}
				</td>
				<td style="text-align:center">
					${memberPointDetail.currBalPoints}
				</td>
				<td style="text-align:center">
					${memberPointDetail.trxNo}
				</td>
				<td style="text-align:center">
					<fmt:formatDate value="${memberPointDetail.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>