<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>借条催收管理</title>
	<meta name="decorator" content="default"/>
	<script src="${ctxStatic}/artdialog/js/dialog-plus.js"></script>
    <link href="${ctxStatic}/artdialog/css/dialog.css" rel="stylesheet" />
	<script type="text/javascript">
		$(document).ready(function() {
			$("#btnExport").click(function() {
				top.$.jBox.confirm("确认要导出催收数据吗？", "系统提示", function(v, h, f) {
					if (v == "ok") {
						$("#searchForm").attr("action", "${admin}/nfsLoanCollection/exportData");
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
		//特别注意函数名称不要和变量重名，
		//函数名不能和页面的某个标签的id名相同。一些浏览器可以通过在js代码中指定ID访问节点元素，然后定义的函数就会被DOM中的元素覆盖了。您需要重命名函数名称或元素ID
		function edit(obj){
			var collectionId = $(obj).closest("tr").attr("id");
			var url = '${admin}/nfsLoanCollection/update?id='+collectionId;
			dialog({
		    	id: "loanCollectionUpdate",
		    	title: '催收修改',
		    	url:url,
		    	width: 700,
		    	height: 500,
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
		<li class="active"><a href="${ctx}/nfsLoanCollection/list">借条催收列表</a></li>
	</ul>
	<form:form id="searchForm" modelAttribute="nfsLoanCollection" action="${ctx}/nfsLoanCollection/list" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label style="text-align:right;width:100px">借条编号：</label>
				<form:input path="loan.id" htmlEscape="false" maxlength="20" class="input-medium"/>
			</li>
			<li><label style="text-align:right;width:100px">放款人手机号：</label>
				<form:input path="loan.loaner.username" htmlEscape="false" maxlength="11" class="input-medium"/>
			</li>
			<li><label style="text-align:right;width:100px">借款人手机号：</label>
				<form:input path="loan.loanee.username" htmlEscape="false" maxlength="11" class="input-medium"/>
			</li>
			<li><label style="text-align:right;width:100px">借款单状态：</label>
				<form:select path="loan.status"  style="width:177px" class="input-medium">
					<form:option value="" label="请选择"/>
					<form:options items="${fns:getDictList('loanStatus')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</li>
			<li><label style="text-align:right;width:100px">催收状态：</label>
				<form:select path="status"  style="width:177px" class="input-medium">
					<form:option value="" label="请选择"/>
					<form:options items="${fns:getDictList('collectionStatus')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</li>
			<li><label style="text-align:right;width:100px">结清时间：</label>
					<input name="loan.beginCompleteDate" type="text"  maxlength="20" class="input-medium Wdate required"
						value="<fmt:formatDate value="${nfsLoanCollection.loan.beginCompleteDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"
						onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',startDate:'%y-%M-%d 00:00:00',isShowClear:true});"/>
					<label style="text-align:center;width:30px">至：</label>
					<input name="loan.endCompleteDate" type="text"  maxlength="20" class="input-medium Wdate required"
						value="<fmt:formatDate value="${nfsLoanCollection.loan.endCompleteDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"
						onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',startDate:'%y-%M-%d 00:00:00',isShowClear:true});"/>
			</li>
		    <li><label style="text-align:right;width:100px">申请时间：</label>
					<input name="beginTime" type="text"  maxlength="20" class="input-medium Wdate required"
						value="<fmt:formatDate value="${nfsLoanCollection.beginTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"
						onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',startDate:'%y-%M-%d 00:00:00',isShowClear:true});"/>
					<label style="text-align:center;width:30px">至：</label>
					<input name="endTime" type="text"  maxlength="20" class="input-medium Wdate required"
						value="<fmt:formatDate value="${nfsLoanCollection.endTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"
						onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',startDate:'%y-%M-%d 00:00:00',isShowClear:true});"/>
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
				<th style="text-align:center">借条编号</th>
				<th style="text-align:center">借款单状态</th>
				<th style="text-align:center">催收状态</th>
				<th style="text-align:center">借款总金额</th>
				<th style="text-align:center">借款利息</th>
				<th style="text-align:center">催收费用</th>
				<th style="text-align:center">放款人姓名</th>
				<th style="text-align:center">放款人联系方式</th>
				<th style="text-align:center">借款人姓名</th>
				<th style="text-align:center">借款人联系方式</th>
				<th style="text-align:center">借款人身份证号</th>
				<th style="text-align:center">借条达成时间</th>
				<th style="text-align:center">到期还款时间</th>
				<th style="text-align:center">催收申请时间</th>
				<th style="text-align:center">借条结清时间</th>
				<th style="text-align:center">催收天数</th>
				<shiro:hasPermission name="loan:nfsLoanCollection:edit">
				<th style="text-align:center">操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="nfsLoanCollection">
			<tr id="${nfsLoanCollection.id }">
				<td style="text-align:center"><a href="${ctx}/nfsLoanRecord/query?id=${nfsLoanCollection.loan.id}">
					${nfsLoanCollection.loan.id}
				</a></td>
				<td style="text-align:center">
					${fns:getDictLabel(nfsLoanCollection.loan.status, 'loanStatus', '')}
				</td>
				<td style="text-align:center">
					${fns:getDictLabel(nfsLoanCollection.status, 'collectionStatus', '')}
				</td>
				<td style="text-align:center">
					${fns:decimalToStr(nfsLoanCollection.loan.amount,2)}
				</td>
				<td style="text-align:center">
					${fns:decimalToStr(nfsLoanCollection.loan.interest,2)}
				</td>
				<td style="text-align:center">
					${fns:decimalToStr(nfsLoanCollection.fee,2)}
				</td>
				<td style="text-align:center"><a href="${ctx}/member/findMemberById?id=${nfsLoanCollection.loan.loaner.id}">
					${nfsLoanCollection.loan.loaner.name}</a>
				</td>
				<td style="text-align:center">
					${nfsLoanCollection.loan.loaner.username}
				</td>
				<td style="text-align:center"><a href="${ctx}/member/findMemberById?id=${nfsLoanCollection.loan.loanee.id}">
					${nfsLoanCollection.loan.loanee.name}</a>
				</td>
				<td style="text-align:center">
					${nfsLoanCollection.loan.loanee.username}
				</td>
				<td style="text-align:center">
					${nfsLoanCollection.loan.loanee.idNo}
				</td>
				<td style="text-align:center">
					<fmt:formatDate value="${nfsLoanCollection.loan.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td style="text-align:center">
					<fmt:formatDate value="${nfsLoanCollection.loan.dueRepayDate}" pattern="yyyy-MM-dd"/>
				</td>
				<td style="text-align:center">
					<fmt:formatDate value="${nfsLoanCollection.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td style="text-align:center">
					<fmt:formatDate value="${nfsLoanCollection.loan.completeDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td style="text-align:center">
					${fns:pastDays(nfsLoanCollection.createTime)}
				</td>
				<td  style="text-align:center">
				<shiro:hasPermission name="loan:nfsLoanCollection:edit">
    				<a href="javascript:;" onclick="edit(this)">修改</a>
    				<a href="${ctx}/nfsLoanCollection/collectionDetailList?id=${nfsLoanCollection.id}">查看详情</a>
				</shiro:hasPermission>
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>