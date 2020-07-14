<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>账户交易管理</title>
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
		<li class="active"><a href="${admin}/ufangBrnActTrx?company.id=${ufangBrnActTrx.company.id}">账户交易列表</a></li>
	</ul>
	<form:form id="searchForm" modelAttribute="ufangBrnActTrx" action="${admin}/ufangBrnActTrx?company.id=${ufangBrnActTrx.company.id}" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label style="text-align:right;width:150px">交易编号：</label>
				<form:input path="trxCode" htmlEscape="false" maxlength="16" class="input-medium"/>
			</li>
			<li><label style="text-align:right;width:150px">交易状态：</label>
				<form:select path="status"  style="width:177px" class="input-medium">
					<form:option value="" label="请选择"/>
					<form:options items="${fns:getDictList('trxSts')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</li>
			<li class="btns"><label style="text-align:right;width:50px"></label><input id="btnSubmit" class="btn btn-primary " style="width:80px;" type="submit" value="查   询"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th style="text-align:center">交易编号</th>
				<th style="text-align:center">归属机构</th>
				<th style="text-align:center">科目编号</th>
				<th style="text-align:center">币种</th>
				<th style="text-align:center">记账方向</th>
				<th style="text-align:center">交易金额</th>
				<th style="text-align:center">账户余额</th>
				<th style="text-align:center">交易状态</th>
				<th style="text-align:center">创建时间</th>
				<th style="text-align:center">操作</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="ufangBrnActTrx">
			<tr>
				<td style="text-align:center">
					<c:choose>
					<c:when test="${ufangBrnActTrx.trxCode == 'UF010'}">
						机构充值
					</c:when>
					<c:when test="${ufangBrnActTrx.trxCode == 'UF020'}">
						会员消费
					</c:when>
					<c:when test="${ufangBrnActTrx.trxCode == 'UF030'}">
						派发交易金
					</c:when>
					<c:when test="${ufangBrnActTrx.trxCode == 'UF040'}">
						回收交易金
					</c:when>
					<c:when test="${ufangBrnActTrx.trxCode == 'UF050'}">
						公司加款
					</c:when>
					<c:when test="${ufangBrnActTrx.trxCode == 'UF060'}">
						公司减款
					</c:when>
					<c:otherwise>
					</c:otherwise>
					</c:choose>
				</td>
				<td style="text-align:center">
					${ufangBrnActTrx.company.brnName}
				</td>
				<td style="text-align:center">
					${ufangBrnActTrx.subNo}
				</td>
				<td style="text-align:center">
						${ufangBrnActTrx.currCode}
				</td>
				<td style="text-align:center">
					${fns:getDictLabel(ufangBrnActTrx.drc, 'drc', '')}
				</td>
				<td style="text-align:center">
					${fns:decimalToStr(ufangBrnActTrx.trxAmt,2)}元
				</td>
				<td style="text-align:center">
				    ${fns:decimalToStr(ufangBrnActTrx.curBal,2)}元
				</td>
				<td style="text-align:center">
					${fns:getDictLabel(ufangBrnActTrx.status, 'trxSts', '')}
				</td>
				<td style="text-align:center">
					<fmt:formatDate value="${ufangBrnActTrx.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td style="text-align: center">
					<a href="${admin}/ufangFundDistColl/query?id=${ufangBrnActTrx.orgId}">详情</a>
			    </td>		
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>