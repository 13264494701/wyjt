<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>流量订单管理</title>
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
		<li class="active"><a href="${admin}/ufangLoaneeDataOrder/">流量订单列表</a></li>
	</ul>
	<form:form id="searchForm" modelAttribute="ufangLoaneeDataOrder" action="${admin}/ufangLoaneeDataOrder/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label style="text-align:right;width:150px">优放用户：</label>
				<form:input path="user.empNam" htmlEscape="false" maxlength="8" class="input-medium"/>
			</li>
			<li><label style="text-align:right;width:150px">产品类型：</label>
				<form:select path="prodCode"  style="width:177px" class="input-medium">
					<form:option value="" label="请选择"/>
					<form:options items="${fns:getDictList('ufangProdCode')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
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
				<th style="text-align:center">优放用户</th>
				<th style="text-align:center">产品编码</th>
				<th style="text-align:center">借款人姓名</th>
				<th style="text-align:center">手机号码</th>
				<th style="text-align:center">年龄</th>
				<th style="text-align:center">芝麻分</th>
				<th style="text-align:center">运营商</th>
				<th style="text-align:center">申请金额</th>
				<th style="text-align:center">价格</th>
				<th style="text-align:center">购买时间</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="ufangLoaneeDataOrder">
			<tr>
				<td style="text-align:center"><a href="${admin}/ufangLoaneeDataOrder/query?id=${ufangLoaneeDataOrder.id}">
					${ufangLoaneeDataOrder.user.empNam}
				</a></td>
				<td style="text-align:center">
						${ufangLoaneeDataOrder.prodCode}
				</td>
				<td style="text-align:center">
						${ufangLoaneeDataOrder.data.name}
				</td>
				<td style="text-align:center">
						${ufangLoaneeDataOrder.data.phoneNo}
				</td>
				<td style="text-align:center">
						${ufangLoaneeDataOrder.data.age}
				</td>
				<td style="text-align:center">
						${ufangLoaneeDataOrder.data.zhimafen}
				</td>
				<td style="text-align:center">
					<c:if test="${ufangLoaneeDataOrder.data.yunyingshangStatus eq 'verified'}">
						<a href="${admin}/rcSjmh/report?taskId=${ufangLoaneeDataOrder.data.reportTaskId}" target="_blank">查看报告</a>
					</c:if>
					<c:if test="${ufangLoaneeDataOrder.data.yunyingshangStatus eq 'unverified'}">
						未认证
					</c:if>
					<c:if test="${ufangLoaneeDataOrder.data.yunyingshangStatus eq 'expired'}">
						已过期
					</c:if>
				</td>
				<td style="text-align:center">
						${fns:decimalToStr(ufangLoaneeDataOrder.data.applyAmount,2)}
				</td>
				<td style="text-align:center">
						${fns:decimalToStr(ufangLoaneeDataOrder.amount,2)}
				</td>
				<td style="text-align:center">
					<fmt:formatDate value="${ufangLoaneeDataOrder.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>