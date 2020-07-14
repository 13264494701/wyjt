<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>债权买卖管理</title>
	<meta name="decorator" content="default"/>
	<script src="${ctxStatic}/artdialog/js/dialog-plus.js"></script>
    <link href="${ctxStatic}/artdialog/css/dialog.css" rel="stylesheet" />
	<script type="text/javascript">
		$(document).ready(function() {
			
		});
		function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#searchForm").submit();
        	return false;
        }
		function pass(obj){
			var id = $(obj).closest("tr").attr("id");
			var url = "${ctx}/crAuction/toCheck?id="+id+"&status=forsale";
			dialog({
		    	id: "toCheck",
		    	title: '审核',
		    	url:url,
		    	width: 600,
		    	height: 400,
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
                     console.log(this.returnValue);
                     if(this.returnValue=='success'){
                    	 location.reload();
                     }               
                 },
                 onremove: function() {
                     console.log('onremove');            
                 }
		    }).show();	
		}
		
		function refuse(obj){
			var id = $(obj).closest("tr").attr("id");
			var url = "${admin}/crAuction/toCheck?id="+id+"&status=auditFailed";
			dialog({
		    	id: "toCheck",
		    	title: '审核',
		    	url:url,
		    	width: 600,
		    	height: 400,
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
                     console.log(this.returnValue);
                     if(this.returnValue=='success'){
                    	 location.reload();
                     }               
                 },
                 onremove: function() {
                     console.log('onremove');            
                 }
		    }).show();	
		}
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/crAuction/">债权待审核列表</a></li>
	</ul>
	<form:form id="searchForm" modelAttribute="nfsCrAuction" action="${ctx}/crAuction/pendingCheck" method="post" class="breadcrumb form-search">
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
			<li><label style="text-align:right;width:150px">利率：</label>
                <form:input path="minInRate" htmlEscape="false" maxlength="3" class="input-medium" cssStyle="width: 30px"/>-
                <form:input path="maxInRate" htmlEscape="false" maxlength="5" class="input-medium" cssStyle="width: 30px"/>
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
				<c:if test="${nfsCrAuction.status eq 'audit'}">
				<a  href ="javascript:void(0);" onclick="pass(this);">审核通过</a> |
				<a  href ="javascript:void(0);" onclick="refuse(this);">审核拒绝</a> 
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