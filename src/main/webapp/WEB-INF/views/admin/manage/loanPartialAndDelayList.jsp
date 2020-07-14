<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>部分还款和延期管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			$("#btnExport").click(function() {
				top.$.jBox.confirm("确认要导出延期借条数据吗？", "系统提示", function(v, h, f) {
					if (v == "ok") {
						$("#searchForm").attr("action", "${admin}/manage/exportPartialAndDelayLoanData");
						$("#searchForm").submit();
					}
				}, {
					buttonsFocus : 1
				});
				top.$('.jbox-body .jbox-icon').css('top', '55px');
			});
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
		<li class="active"><a href="${admin}/manage/partialAndDelayLoanList/">延期列表</a></li>		
	</ul>
	<form:form id="searchForm" modelAttribute="nfsLoanPartialAndDelay" action="${admin}/manage/partialAndDelayLoanList/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">

			<li><label style="text-align:right;width:80px">通过时间：</label>
			<input name="beginTime" type="text"  maxlength="20" class="input-medium Wdate"
				value="<fmt:formatDate value="${beginTime}" pattern="yyyy-MM-dd"/>"
				onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:true});"/> - 
			<input name="endTime" type="text"  maxlength="20" class="input-medium Wdate"
				value="<fmt:formatDate value="${endTime}" pattern="yyyy-MM-dd"/>"
				onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:true});"/>
			</li>
			<li class="btns">
			<label style="text-align:right;width:50px"></label>
				<input id="btnSubmit" class="btn btn-primary " style="width:80px;" type="submit" value="查   询"/>
				<label style="text-align:right;width:50px"></label>
				<input id="btnExport" class="btn btn-primary" style="width:80px;" type="button" value="导  出" />
			</li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th style="text-align:center">借款人姓名</th>
				<th style="text-align:center">借款人手机号</th>
				<th style="text-align:center">申请金额</th>
				<th style="text-align:center">状态</th>
				<th style="text-align:center">申请时间</th>
				<th style="text-align:center">通过时间</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="loanPartialAndDelay">
			<tr>
				<td style="text-align:center"><a href="${admin}/member/query?id=${loanPartialAndDelay.loan.loanee.id}">
					${loanPartialAndDelay.loan.loanee.name}
				</a></td>
				<td style="text-align:center">
					${loanPartialAndDelay.loan.loanee.username}
				</td>
				<td style="text-align:center">
					${fns:decimalToStr(loanPartialAndDelay.remainAmount,2)}
				</td>
				<td style="text-align:center">
					${fns:getDictLabel(loanPartialAndDelay.status, 'partialAndDelayStatus', '')}
				</td>
				<td style="text-align:center">
					<fmt:formatDate value="${loanPartialAndDelay.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td style="text-align:center">
					<fmt:formatDate value="${loanPartialAndDelay.updateTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>