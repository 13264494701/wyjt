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
		<li class="active"><a href="${ctx}/nfsBrnActTrx/">账户交易列表</a></li>		
	</ul>
	<form:form id="searchForm" modelAttribute="nfsBrnActTrx" action="${ctx}/nfsBrnActTrx/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label style="text-align:right;width:150px">交易编号：</label>
				<form:input path="trxNo" htmlEscape="false" maxlength="16" class="input-medium"/>
			</li>
			<li><label style="text-align:right;width:150px">交易类型：</label>
				<form:select path="trxType"  style="width:177px" class="input-medium">
					<form:option value="" label="请选择"/>
					<form:options items="${fns:getDictList('trxType')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</li>
			<li><label style="text-align:right;width:150px">交易状态：</label>
				<form:select path="trxSts"  style="width:177px" class="input-medium">
					<form:option value="" label="请选择"/>
					<form:options items="${fns:getDictList('trxSts')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</li>
			<li><label>归属公司：</label>
			<sys:treeselect id="company" name="company.id" value="${nfsBrnActTrx.company.id}" labelName="company.brnName" labelValue="${nfsBrnActTrx.company.brnName}"
				title="部门" baseUrl="${admin}" url="/sys/brn/treeData?type=2" cssClass="required" allowClear="true" notAllowSelectParent="false"/>
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
				<th style="text-align:center">账户名称</th>
				<th style="text-align:center">交易类型</th>								
				<th style="text-align:center">记账方向</th>
				<th style="text-align:center">交易金额</th>
				<th style="text-align:center">账户余额</th>
				<th style="text-align:center">交易状态</th>
				<th style="text-align:center">创建时间</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="nfsBrnActTrx">
			<tr>
				<td style="text-align:center">
					${nfsBrnActTrx.trxNo}
				</td>
				<td style="text-align:center">
					${nfsBrnActTrx.company.brnName}
				</td>
				<td style="text-align:center">
					${nfsBrnActTrx.subNo}
				</td>
				<td style="text-align:center">
					${nfsBrnActTrx.actNam}
				</td>
				<td style="text-align:center">
					${fns:getDictLabel(nfsBrnActTrx.trxType, 'trxType', '')}
				</td>
				<td style="text-align:center">
					${fns:getDictLabel(nfsBrnActTrx.actDrc, 'actDrc', '')}
				</td>
				<td style="text-align:center">
					${fns:decimalToStr(nfsBrnActTrx.trxAmt,2)}元
				</td>
				<td style="text-align:center">
				    ${fns:decimalToStr(nfsBrnActTrx.curBal,2)}元
				</td>
				<td style="text-align:center">
					${fns:getDictLabel(nfsBrnActTrx.trxSts, 'trxSts', '')}
				</td>
				<td style="text-align:center">
					<fmt:formatDate value="${nfsBrnActTrx.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>