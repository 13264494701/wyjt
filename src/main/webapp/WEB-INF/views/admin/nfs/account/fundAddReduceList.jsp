<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>会员加减款管理</title>
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
		
		function pass(id){
			var confirmMsg = confirm("确定要通过审核吗？");
			if(confirmMsg){
				$.ajax({
					url:'${ctx}/fundAddReduce/applyCheck',
					type:'POST',
					data:{
						fundAddReduceId:id,
						status:'pass'
					},
					dataType:'json',
					success:function(res){
						if(res.code == '0'){
							location.reload();
						}else{
							alert(res.getMessage());
						}
					}
					
				});
			}else{
				return false;
			}
		}
		function refuse(id){
			var confirmMsg = confirm("确定要拒绝该申请吗？");
			if(confirmMsg){
				$.ajax({
					url:'${ctx}/fundAddReduce/applyCheck',
					type:'POST',
					data:{
						id:id,
						status:'reject'
					},
					success:function(res){
						if(res.code == '0'){
							location.reload();
						}else{
							alert(res.getMessage());
						}
					}
					
				});
			}else{
				return false;
			}
		}
		function showActTrxDetailDialog(obj){
		    var rchgId = $(obj).closest("tr").attr("id");
			var url = '${admin}/memberActTrx/list?orgId='+rchgId;
			dialog({
		    	id: "actTrxDetail",
		    	title: '流水明细',
		    	url:url,
		    	width: 1000,
		    	height: 600,
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
		<li class="active"><a href="${admin}/account/fundAddReduceList/">会员加减款列表</a></li>
	</ul>
	<form:form id="searchForm" modelAttribute="nfsFundAddReduce" action="${admin}/account/fundAddReduceList/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label style="text-align:right;width:150px">会员手机号：</label>
				<form:input path="member.username" htmlEscape="false" maxlength="20" class="input-medium"/>
			</li>
			<li><label style="text-align:right;width:150px">交易类型：</label>
				<form:select path="type"  style="width:177px" class="input-medium">
					<form:option value="" label="请选择"/>
					<form:options items="${fns:getDictList('fundAddReduceType')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</li>
			<li><label style="text-align:right;width:150px">交易状态：</label>
				<form:select path="status"  style="width:177px" class="input-medium">
					<form:option value="" label="请选择"/>
					<form:options items="${fns:getDictList('fundAddReduceStatus')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</li>
			<li><label style="text-align:right;width:150px">申请时间：</label>
				<input name="beginTime" type="text"  maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${nfsFundAddReduce.beginTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',startDate:'%y-%M-%d 00:00:00',isShowClear:true});"/>
				<label style="text-align:center;width:30px">至：</label>
				<input name="endTime" type="text"  maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${nfsFundAddReduce.endTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"
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
				<th style="text-align:center">会员姓名</th>
				<th style="text-align:center">会员手机号</th>
				<th style="text-align:center">交易类型</th>
				<th style="text-align:center">交易金额</th>
				<th style="text-align:center">账户余额</th>
				<th style="text-align:center">币种</th>
				<th style="text-align:center">交易状态</th>
				<th style="text-align:center">交易时间</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="nfsFundAddReduce">
			<tr id="${nfsFundAddReduce.id}">
				<td style="text-align:center">					
					${nfsFundAddReduce.id}			
				</td>
				<td style="text-align:center"><a href="${ctx}/member/query?id=${nfsFundAddReduce.member.id}">					
					${nfsFundAddReduce.member.name}					
				</a></td>
				<td style="text-align:center">
					${nfsFundAddReduce.member.username}
				</td>
				<td style="text-align:center">
					${fns:getDictLabel(nfsFundAddReduce.type, 'fundAddReduceType', '')}
				</td>
				<td style="text-align:center">
					${fns:decimalToStr(nfsFundAddReduce.amount,2)}
				</td>
				<td style="text-align:center">
					${nfsFundAddReduce.curBal}
				</td>
				<td style="text-align:center">
					${nfsFundAddReduce.currCode}
				</td>
				<td id="status${nfsFundAddReduce.id}" style="text-align:center">
					${fns:getDictLabel(nfsFundAddReduce.status, 'fundAddReduceStatus', '')}
				</td>
				<td style="text-align:center">
					<fmt:formatDate value="${nfsFundAddReduce.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td style="text-align:center">
				    <a href ="javascript:void(0);" onclick="showActTrxDetailDialog(this);">流水明细</a> 
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>