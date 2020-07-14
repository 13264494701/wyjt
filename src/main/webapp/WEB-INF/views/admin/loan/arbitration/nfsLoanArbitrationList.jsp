<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>借条仲裁查询管理</title>
	<meta name="decorator" content="default"/>
	<script src="${ctxStatic}/artdialog/js/dialog-plus.js"></script>
    <link href="${ctxStatic}/artdialog/css/dialog.css" rel="stylesheet" />
	<script type="text/javascript">
		$(document).ready(function() {
			
		});
		
		function showUploadCaseDialog(id){
			var url = '${ctx}/loanArbitration/uploadCase?id='+id;
			dialog({
		    	id: "uploadCase",
		    	title: '上传案件',
		    	url:url,
		    	width: 1200,
		    	height: 800,
		    	padding: 0,
		    	drag:true,
		    	quickClose: true,
                onshow: function() {
                     console.log('onshow');
                 },
                 oniframeload: function() {
                     console.log('oniframeload');
                 },
                 onclose: function() {
                     if(this.returnValue == 'success'){
                    	 location.reload();
                     }               
                 },
                 onremove: function() {
                     console.log('onremove');            
                 }
		    }).show();	
		}
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
		<li class="active"><a href="${ctx}/loanArbitration/">借条仲裁查询列表</a></li>
	</ul>
	<form:form id="searchForm" modelAttribute="nfsLoanArbitration" action="${ctx}/loanArbitration/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label style="text-align:right;width:150px">借条编号：</label>
				<form:input path="loan.id" htmlEscape="false" maxlength="20" class="input-medium"/>
			</li>
			<li><label style="text-align:right;width:150px">放款人姓名：</label>
				<form:input path="loan.loaner.name" htmlEscape="false" maxlength="20" class="input-medium"/>
			</li>
			<li><label style="text-align:right;width:150px">放款人手机号：</label>
				<form:input path="loan.loaner.username" htmlEscape="false" maxlength="20" class="input-medium"/>
			</li>
			<li><label style="text-align:right;width:150px">借款人姓名：</label>
				<form:input path="loan.loanee.name" htmlEscape="false" maxlength="20" class="input-medium"/>
			</li>
			<li><label style="text-align:right;width:150px">借款人手机号：</label>
				<form:input path="loan.loanee.username" htmlEscape="false" maxlength="20" class="input-medium"/>
			</li>
			<li><label style="text-align:right;width:150px">仲裁状态：</label>
				<form:select path="status"  style="width:177px" class="input-medium">
					<form:option value="" label="请选择"/>
					<form:options items="${fns:getDictList('arbitrationStatus')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</li>
			<li><label style="text-align:right;width:150px">代理渠道：</label>
				<form:select path="channel"  style="width:177px" class="input-medium">
					<form:option value="" label="请选择"/>
					<form:options items="${fns:getDictList('arbitrationChannel')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</li>
			<li><label style="text-align:right;width:150px">仲裁申请时间：</label>
				<input name="beginTime" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${beginTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:true});"/>
			</li>
			<li><label style="text-align:right;width:150px">至：</label>
				<input name="endTimes" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${endTimes}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:true});"/>
			</li>
			<li class="btns"><label style="text-align:right;width:50px"></label><input id="btnSubmit" class="btn btn-primary " style="width:80px;" type="submit" value="查   询"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th style="text-align:center">借条编号</th>
				<th style="text-align:center">放款人姓名（手机号）</th>
				<th style="text-align:center">放款人身份证号码</th>
				<th style="text-align:center">借款人姓名（手机号）</th>
				<th style="text-align:center">借款人身份证号码</th>
				<th style="text-align:center">申请仲裁金额</th>
				<th style="text-align:center">仲裁服务费</th>
				<th style="text-align:center">退费金额</th>
				<th style="text-align:center">仲裁状态</th>
				<th style="text-align:center">保全进程</th>
				<th style="text-align:center">申请渠道</th>
				<th style="text-align:center">结案时间</th>
				<th style="text-align:center">出裁决时间</th>
				<th style="text-align:center">申请时间</th>
				<shiro:hasPermission name="loan:nfsLoanArbitration:edit"><th style="text-align:center">操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="nfsLoanArbitration">
			<tr>
				<td style="text-align:center"><a href="${ctx}/nfsLoanRecord/query?id=${nfsLoanArbitration.loan.id}">
					${nfsLoanArbitration.loan.id}</a>
				</td>
				<td style="text-align:center"><a href="${ctx}/member/findMemberById?id=${nfsLoanArbitration.loan.loaner.id}">
					${nfsLoanArbitration.loan.loaner.name}(${nfsLoanArbitration.loan.loaner.username})</a>
				</td>
				<td style="text-align:center"><a href="${ctx }/memberVideoVerify/getMemberVideoVerifyByMemberId?memberId=${nfsLoanArbitration.loan.loaner.id}">
					${nfsLoanArbitration.loan.loaner.idNo}</a>
				</td>
				<td style="text-align:center"><a href="${ctx}/member/findMemberById?id=${nfsLoanArbitration.loan.loanee.id}">
					${nfsLoanArbitration.loan.loanee.name}(${nfsLoanArbitration.loan.loanee.username})</a>
				</td>
				<td style="text-align:center"><a href="${ctx }/memberVideoVerify/getMemberVideoVerifyByMemberId?memberId=${nfsLoanArbitration.loan.loanee.id}">
					${nfsLoanArbitration.loan.loanee.idNo}</a>
				</td>
				<td style="text-align:center">
					${fns:decimalToStr(nfsLoanArbitration.applyAmount,2)}元
				</td>
				<td style="text-align:center">
					${fns:decimalToStr(nfsLoanArbitration.fee,2)}元
				</td>
				<td style="text-align:center">
					${fns:decimalToStr(nfsLoanArbitration.refundFee,2)}元
				</td>
				<td style="text-align:center">
					${fns:getDictLabel(nfsLoanArbitration.status, 'arbitrationStatus', '')}
				</td>
				<td style="text-align:center">
					${fns:getDictLabel(nfsLoanArbitration.preservationProcess, 'preservationProcess', '')}
				</td>
				<td style="text-align:center">
					${fns:getDictLabel(nfsLoanArbitration.loan.trxType, 'loanTrxType', '')}
				</td>
				<td style="text-align:center">
					<fmt:formatDate value="${nfsLoanArbitration.endTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td style="text-align:center">
					<fmt:formatDate value="${nfsLoanArbitration.ruleTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td style="text-align:center">
					<fmt:formatDate value="${nfsLoanArbitration.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td  style="text-align:center">
				<shiro:hasPermission name="loan:nfsLoanArbitration:edit">
    				<a href="${ctx}/loanArbitration/update?id=${nfsLoanArbitration.id}">修改</a><br/>
    				<a href="${ctx}/loanArbitration/arbitrationDetailList?id=${nfsLoanArbitration.id}">查看详情</a><br/>
    				<a href="${ctx}/loanArbitration/loanCaseData?loanId=${nfsLoanArbitration.loan.id}">调取证据</a><br/>
    				<a href="${ctx}/loanArbitration/loanCard?id=${nfsLoanArbitration.loan.id}">调取借款单</a><br/>
    				<c:if test="${nfsLoanArbitration.preservationProcess ne 'success'}">
	    				<a  href="javascript:void(0)"onclick='showUploadCaseDialog("${nfsLoanArbitration.id}");'>上传案件</a>
    				</c:if>
				</shiro:hasPermission>
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>