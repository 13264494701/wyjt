<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>会员账户交易管理</title>
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
		<li class="active"><a href="${ctx}/memberActTrx/allSubTrxList?member.id=${memberActTrx.member.id}">会员账户交易列表</a></li>		
	</ul>
	<form:form id="searchForm" modelAttribute="memberActTrx" action="${ctx}/memberActTrx/allSubTrxList?member.id=${memberActTrx.member.id}" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
		<li><label style="text-align:right;width:150px">操作类型：</label>
			<form:select path="title"  style="width:177px" class="input-medium">
			<form:option value="" label="请选择"/>
			<form:option value="充值" label="充值"/>
			<form:option value="提现" label="提现"/>
			<form:option value="转账" label="转账"/>
			<form:option value="借款" label="借款"/>
			<form:option value="放款" label="放款"/>
			<form:option value="催收" label="催收"/>
			<form:option value="仲裁" label="仲裁"/>
		</form:select>
		</li>
		<li><label style="text-align:right;width:150px">账户名称：</label>
				<form:select path="subNo"  style="width:177px" class="input-medium">
					<form:option value="" label="请选择"/>
					<form:options items="${fns:getDictList('subNo')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
		</li>
		<li><label style="text-align:right;width:150px">源业务ID：</label>
				<form:input path="orgId" htmlEscape="false" maxlength="64" class="input-medium"/>
		</li>
		<li><label style="text-align:right;width:80px">创建时间：</label>
			<input name="beginTime" type="text"  maxlength="20" class="input-medium Wdate"
				value="<fmt:formatDate value="${memberActTrx.beginTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"
				onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',startDate:'%y-%M-%d 00:00:00',isShowClear:true});"/> - 
			<input name="endTime" type="text"  maxlength="20" class="input-medium Wdate"
				value="<fmt:formatDate value="${memberActTrx.endTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"
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
				<th style="text-align:center">会员ID</th>
				<th style="text-align:center">标题</th>
				<th style="text-align:center">记账方向</th>
				<th style="text-align:center">交易金额</th>
				<th style="text-align:center">账户名称</th>
				<th style="text-align:center">账户余额</th>
				<th style="text-align:center">交易状态</th>
				<th style="text-align:center">源业务ID</th>
				<th style="text-align:center">备注</th>
				<th style="text-align:center">生成时间</th>				
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="memberActTrx">
			<tr>
				<td style="text-align:center"><a href="${ctx}/member/query?id=${memberActTrx.member.id}">
					${memberActTrx.member.id}(${memberActTrx.member.name})
				</a></td>
				<td style="text-align:center">
					${memberActTrx.title}
				</td>
				<td style="text-align:center">
					${fns:getDictLabel(memberActTrx.drc,'drc','')}
				</td>
				<td style="text-align:center">
					${fns:decimalToStr(memberActTrx.trxAmt,2)}
				</td>
				<td style="text-align:center">
					${fns:getSubName('member',memberActTrx.subNo)}
				</td>
				<td style="text-align:center">
					${fns:decimalToStr(memberActTrx.curBal,2)}
				</td>
				<td style="text-align:center">
					${fns:getDictLabel(memberActTrx.status,'trxSts','')}
				</td>
				<td style="text-align:center">
					${memberActTrx.orgId}
				</td>
				<td style="text-align:center">
					${memberActTrx.rmk}
					<c:choose>
						<c:when test="${memberActTrx.trxCode == 'LN010'}">
							<a href="${admin}/nfsLoanRecord/query?id=${memberActTrx.orgId}">借款详情</a>
						</c:when>
						<c:when test="${memberActTrx.trxCode == 'MB010'}">
							<a href="${admin}/rchgRecord/query?id=${memberActTrx.orgId}">充值详情</a>
						</c:when>
						<c:when test="${memberActTrx.trxCode == 'MB020'||memberActTrx.trxCode == 'MB021'}">
							<a href="${admin}/wdrlRecord/query?id=${memberActTrx.orgId}">提现详情</a>
						</c:when>
						<c:when test="${memberActTrx.trxCode == 'MB030'}">
							<a href="${admin}/transferRecord/query?id=${memberActTrx.orgId}">转账详情</a>
						</c:when>
						<c:when test="${memberActTrx.trxCode == 'MB040'}">
							<a href="${admin}/fundAddReduce/query?id=${memberActTrx.orgId}">加款详情</a>
						</c:when>
						<c:when test="${memberActTrx.trxCode == 'MB050'}">
							<a href="${admin}/fundAddReduce/query?id=${memberActTrx.orgId}">减款详情</a>
						</c:when>
					</c:choose>															    
				</td>
				<td style="text-align:center">
					<fmt:formatDate value="${memberActTrx.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>

			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>