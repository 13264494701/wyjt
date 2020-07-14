<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>借条记录管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			$("#btnExport").click(function() {
				top.$.jBox.confirm("确认要导出借条数据吗？", "系统提示", function(v, h, f) {
					if (v == "ok") {
						$("#searchForm").attr("action", "${admin}/manage/exportLoanData?status=${loanStatus}");
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
		<li class="active"><a href="${admin}/manage/loanList?status=${loanStatus}">借条列表</a></li>		
	</ul>
	<form:form id="searchForm" modelAttribute="nfsLoanRecord" action="${admin}/manage/loanList?status=${loanStatus}" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label style="text-align:right;width:150px">放款人姓名：</label>
				<form:input path="loaner.name" htmlEscape="false" maxlength="64" class="input-medium"/>
			</li>
			<li><label style="text-align:right;width:150px">放款人手机号：</label>
				<form:input path="loaner.username" htmlEscape="false" maxlength="64" class="input-medium"/>
			</li>
			<li><label style="text-align:right;width:150px">借款人姓名：</label>
				<form:input path="loanee.name" htmlEscape="false" maxlength="64" class="input-medium"/>
			</li>
			<li><label style="text-align:right;width:150px">借款人手机号：</label>
				<form:input path="loanee.username" htmlEscape="false" maxlength="64" class="input-medium"/>
			</li>
	        <c:if test="${loanStatus=='penddingRepay'}">
			<li><label style="text-align:right;width:80px">待还日期：</label>
				<input name="beginDueRepayDate" type="text"  maxlength="10" class="input-medium Wdate"
					value="<fmt:formatDate value="${nfsLoanRecord.beginDueRepayDate}" pattern="yyyy-MM-dd"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:true});"/> - 
				<input name="endDueRepayDate" type="text"  maxlength="10" class="input-medium Wdate"
					value="<fmt:formatDate value="${nfsLoanRecord.endDueRepayDate}" pattern="yyyy-MM-dd"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:true});"/>
			</li>	
			</c:if>
		    <c:if test="${loanStatus=='repayed'}">
			<li><label style="text-align:right;width:80px">结清日期：</label>
				<input name="beginCompleteDate" type="text"  maxlength="10" class="input-medium Wdate"
					value="<fmt:formatDate value="${nfsLoanRecord.beginCompleteDate}" pattern="yyyy-MM-dd"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:true});"/> - 
				<input name="endCompleteDate" type="text"  maxlength="10" class="input-medium Wdate"
					value="<fmt:formatDate value="${nfsLoanRecord.endCompleteDate}" pattern="yyyy-MM-dd"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:true});"/>
			</li>	
			</c:if>
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
				<th style="text-align:center">借条编号</th>
				<th style="text-align:center">借款人姓名</th>
				<th style="text-align:center">借款人手机号</th>
				<th style="text-align:center">出借人姓名</th>
				<th style="text-align:center">出借人手机号</th>
				<th style="text-align:center">借款金额</th>
				<th style="text-align:center">借款利率</th>
				<th style="text-align:center">借款期限</th>
				<th style="text-align:center">还款方式</th>
				<th style="text-align:center">本期到期日</th>
				<th style="text-align:center">本期应还金额</th>
				<th style="text-align:center">结清日期</th>
				<th style="text-align:center">借条状态</th>
				<th style="text-align:center">创建时间</th>
				<shiro:hasPermission name="record:nfsLoanRecord:edit"><th style="text-align:center">操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="nfsLoanRecord">
			<tr>
				<td style="text-align:center">
					${nfsLoanRecord.id}
				</td>
				<td style="text-align:center">
					${nfsLoanRecord.loanee.name}
				</td>
				<td style="text-align:center">
					<a href="${admin}/member/query?id=${nfsLoanRecord.loanee.id}">
						${nfsLoanRecord.loanee.username}
					</a>
				</td>
				<td style="text-align:center">
					${nfsLoanRecord.loaner.name}
				</td>
				<td style="text-align:center">
					<a href="${admin}/member/query?id=${nfsLoanRecord.loaner.id}">
						${nfsLoanRecord.loaner.username}.
					</a>
				</td>
				<td style="text-align:center">
					${fns:decimalToStr(nfsLoanRecord.amount,2)}
				</td>
				<td style="text-align:center">
					${nfsLoanRecord.intRate}
				</td>
				<td style="text-align:center">
					${nfsLoanRecord.term}天
				</td>
				<td style="text-align:center">
					${fns:getDictLabel(nfsLoanRecord.repayType, 'repayType', '')}
				</td>
				<td style="text-align:center">
					<fmt:formatDate value="${nfsLoanRecord.dueRepayDate}" pattern="yyyy-MM-dd"/>
				</td>
				<td style="text-align:center">
					${ufang:decimalToStr(nfsLoanRecord.dueRepayAmount,2)}
				</td>
				<td style="text-align:center">
					<fmt:formatDate value="${nfsLoanRecord.completeDate}" pattern="yyyy-MM-dd"/>
				</td>
				<td style="text-align:center">
					${fns:getDictLabel(nfsLoanRecord.status, 'loanStatus', '')}
				</td>
				<td style="text-align:center">
					<fmt:formatDate value="${nfsLoanRecord.createTime}" pattern="yyyy-MM-dd"/>
				</td>
				<td  style="text-align:center">
				<shiro:hasPermission name="record:nfsLoanRecord:edit">
    				<a href="${admin}/nfsLoanRecord/query?id=${nfsLoanRecord.id}">查看详情</a>
					<br>		    
				    <a href="${admin}/loanOperatingRecord/?oldRecord.id=${nfsLoanRecord.id}">操作记录</a>
				</shiro:hasPermission>
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>