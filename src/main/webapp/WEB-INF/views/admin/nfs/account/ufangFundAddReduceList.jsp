<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>优放机构加减款记录管理</title>
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
		<li class="active"><a href="${admin}/account/ufangFundAddReduce/">优放机构加减款记录列表</a></li>
	</ul>
	<form:form id="searchForm" modelAttribute="ufangFundAddReduce" action="${admin}/account/ufangFundAddReduce/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label style="text-align:right;width:150px">公司名称：</label>
				<form:input path="ufangBrn.brnName" htmlEscape="false"  class="input-medium"/>
			</li>
			<li><label style="text-align:right;width:150px">交易类型：</label>
				<form:select path="type"  style="width:177px" class="input-medium">
					<form:option value="" label="请选择"/>
					<form:options items="${fns:getDictList('ufangFundAddReduceType')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</li>
			<li><label style="text-align:right;width:150px">交易金额：</label>
				<form:input path="amount" htmlEscape="false" class="input-medium"/>
			</li>
			<li><label style="text-align:right;width:150px">交易状态：</label>
				<form:select path="status"  style="width:177px" class="input-medium">
					<form:option value="" label="请选择"/>
					<form:options items="${fns:getDictList('ufangFundAddReduceStatus')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</li>
			<li><label style="text-align:right;width:150px">申请时间：</label>
				<input name="beginTime" type="text" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${ufangFundAddReduce.beginTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',startDate:'%y-%M-%d 00:00:00',isShowClear:true});"/>
				<label style="text-align:center;width:30px">至：</label>
				<input name="endTime" type="text"  maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${ufangFundAddReduce.endTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"
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
				<th style="text-align:center">业务编号</th>
				<th style="text-align:center">公司名称</th>
				<th style="text-align:center">交易类型</th>
				<th style="text-align:center">交易金额</th>
				<th style="text-align:center">账户余额</th>
				<th style="text-align:center">币种</th>
				<th style="text-align:center">交易状态</th>
				<th style="text-align:center">备注</th>
				<th style="text-align:center">创建时间</th>		
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="ufangFundAddReduce">
			<tr>
				<td style="text-align:center">
					${ufangFundAddReduce.id}
				</td>
				<td style="text-align:center">
					${ufangFundAddReduce.ufangBrn.brnName}
				</td>
				<td style="text-align:center">
					${fns:getDictLabel(ufangFundAddReduce.type, 'ufangFundAddReduceType', '')}
				</td>
				<td style="text-align:center">
					${fns:decimalToStr(ufangFundAddReduce.amount,2)}
				</td>
				<td style="text-align:center">
					${ufangFundAddReduce.curBal}
				</td>
				<td style="text-align:center">
					${ufangFundAddReduce.currCode}
				</td>
				<td  style="text-align:center">
					${fns:getDictLabel(ufangFundAddReduce.status, 'ufangFundAddReduceStatus', '')}
				</td>
				<td style="text-align:center">
					${ufangFundAddReduce.rmk}
				</td>
				<td style="text-align:center">
					<fmt:formatDate value="${ufangFundAddReduce.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>