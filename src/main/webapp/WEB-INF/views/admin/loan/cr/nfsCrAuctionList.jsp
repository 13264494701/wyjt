<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>债权买卖管理</title>
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
		<li class="active"><a href="${admin}/crAuction/list?status=successed">债权已转让列表</a></li>
	</ul>
	<form:form id="searchForm" modelAttribute="nfsCrAuction" action="${admin}/crAuction/list?status=successed" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label style="text-align:right;width:150px">借款单ID：</label>
				<form:input path="loanRecord.id" htmlEscape="false" maxlength="20" class="input-medium"/>
			</li>
			<li><label style="text-align:right;width:150px">借款人姓名：</label>
				<form:input path="loanRecord.loanee.name" htmlEscape="false" maxlength="20" class="input-medium"/>
			</li>
			<li><label style="text-align:right;width:150px">借款人手机号：</label>
				<form:input path="loanRecord.loanee.username" htmlEscape="false" maxlength="20" class="input-medium"/>
			</li>
			<li><label style="text-align:right;width:150px">放款人姓名：</label>
				<form:input path="loanRecord.loaner.name" htmlEscape="false" maxlength="20" class="input-medium"/>
			</li>
			<li><label style="text-align:right;width:150px">放款人手机号：</label>
				<form:input path="loanRecord.loaner.username" htmlEscape="false" maxlength="20" class="input-medium"/>
			</li>
			<li><label style="text-align:right;width:150px">购买人姓名：</label>
				<form:input path="crBuyer.name" htmlEscape="false" maxlength="20" class="input-medium"/>
			</li>
			<li><label style="text-align:right;width:150px">购买人手机号：</label>
				<form:input path="crBuyer.username" htmlEscape="false" maxlength="20" class="input-medium"/>
			</li>
			<li><label style="text-align:right;width:150px">申请时间：</label>
				<input name="beginTime" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate required"
					value="<fmt:formatDate value="${nfsCrAuction.beginTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',startDate:'%y-%M-%d 00:00:00',isShowClear:true});"/>
				<label style="text-align:center;width:30px">至：</label>
				<input name="endTimes" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate required"
					value="<fmt:formatDate value="${nfsCrAuction.endTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"
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
				<th style="text-align:center">借款单ID</th>
				<th style="text-align:center">放款人姓名</th>
				<th style="text-align:center">放款人手机号</th>
				<th style="text-align:center">借款人姓名</th>
				<th style="text-align:center">借款人手机号</th>
				<th style="text-align:center">借款金额</th>
				<th style="text-align:center">借款利息</th>
				<th style="text-align:center">借款利率</th>
				<th style="text-align:center">转让金额</th>
				<th style="text-align:center">本期到期日</th>
				<th style="text-align:center">创建时间</th>
				<shiro:hasPermission name="cr:nfsCrAuction:edit"><th style="text-align:center">操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="nfsCrAuction">
			<tr id="${nfsCrAuction.id}">
				<td  style="text-align:center"><a href="${ctx}/nfsLoanRecord/query?id=${nfsCrAuction.loanRecord.id}">
				${nfsCrAuction.loanRecord.id}
				</a></td>
				<td style="text-align:center">
					${nfsCrAuction.loanRecord.loaner.name}
				</td>
				<td style="text-align:center">
					<a href="${admin}/member/query?id=${nfsCrAuction.loanRecord.loaner.id}">
						${nfsCrAuction.loanRecord.loaner.username}
					</a>
				</td>
				<td style="text-align:center">
					${nfsCrAuction.loanRecord.loanee.name}
				</td>
				<td style="text-align:center">
					<a href="${admin}/member/query?id=${nfsCrAuction.loanRecord.loanee.id}">
					${nfsCrAuction.loanRecord.loanee.username}
					</a>
				</td>
				<td style="text-align:center">
					${fns:decimalToStr(nfsCrAuction.loanRecord.amount,2)}
				</td>
				<td style="text-align:center">
					${fns:decimalToStr(nfsCrAuction.loanRecord.interest,2)}
				</td>
				<td style="text-align:center">
					${fns:decimalToStr(nfsCrAuction.loanRecord.intRate,2)}%
				</td>
				<td style="text-align:center">
					${fns:decimalToStr(nfsCrAuction.crSellPrice,2)}
				</td>
				<td style="text-align:center">
					<fmt:formatDate value="${nfsCrAuction.loanRecord.dueRepayDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td style="text-align:center">
					<fmt:formatDate value="${nfsCrAuction.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td  style="text-align:center">
				<shiro:hasPermission name="cr:nfsCrAuction:edit">
					<a href="${admin}/crAuction/download?id=${nfsCrAuction.id}" onclick="return confirmx('确认下载数据？', this.href)">下载数据</a>
				</shiro:hasPermission>
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>