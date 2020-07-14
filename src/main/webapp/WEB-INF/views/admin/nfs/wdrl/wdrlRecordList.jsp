<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>提现记录管理</title>
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


		function showCancelWdrlBalDialog(wdrlId){
			var url = '${ctx}/wdrlRecord/cancel?id='+wdrlId;
			dialog({
		    	id: "cancelWdrlRecord",
		    	title: '撤销提现',
		    	url:url,
		    	width: 600,
		    	height: 450,
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
		
		function successedRemitted(id){
			var confirmMsg = confirm("确定要修改该提现申请状态为汇款成功吗？");
			if(confirmMsg){
				$.ajax({
					url:'${admin}/wdrlRecord/successedRemitted',
					type:'POST',
					data:{
						id:id
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
		function failedRemitted(id){
			var confirmMsg = confirm("确定要修改该提现申请状态为汇款失败吗？");
			if(confirmMsg){
				$.ajax({
					url:'${ctx}/wdrlRecord/failedRemitted',
					type:'POST',
					data:{
						id:id
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
		function showQueryThirdOrderDialog(wdrlId){
			var url = '${ctx}/wdrlRecord/queryThirdOrder?id='+wdrlId;
			dialog({
		    	id: "queryThirdOrder",
		    	title: '订单查询',
		    	url:url,
		    	width: 600,
		    	height: 450,
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
		function queryOrder(id){
			showQueryThirdOrderDialog(id);
		}
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/wdrlRecord/">提现记录列表</a></li>
	</ul>
	<form:form id="searchForm" modelAttribute="nfsWdrlRecord" action="${ctx}/wdrlRecord/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
		<li><label style="text-align:right;width:150px">提现编号：</label>
				<form:input path="id" htmlEscape="false" maxlength="32" class="input-medium"/>
			</li>
			<li><label style="text-align:right;width:150px">会员姓名：</label>
				<form:input path="member.name" htmlEscape="false" maxlength="20" class="input-medium"/>
			</li>
			<li><label style="text-align:right;width:150px">手机号：</label>
				<form:input path="member.username" htmlEscape="false" maxlength="20" class="input-medium"/>
			</li>
			<li><label style="text-align:right;width:150px">提现金额：</label>
				<form:input path="minAmount" htmlEscape="false" maxlength="20" class="input-medium"/>
				<label style="text-align:center;width:30px">至：</label>
				<form:input path="maxAmount" htmlEscape="false" maxlength="20" class="input-medium"/>
			</li>
			<%-- <li><label style="text-align:right;width:150px">银行卡号：</label>
				<form:input path="cardNo" htmlEscape="false" maxlength="32" class="input-medium"/>
			</li> --%>
			<li><label style="text-align:right;width:150px">提现状态：</label>
				<form:select path="status"  style="width:177px" class="input-medium">
					<form:option value="" label="请选择"/>
					<form:options items="${fns:getDictList('wdrlRecordStatus')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</li>
			<li><label style="text-align:right;width:150px">申请时间：</label>
				<input name="beginTime" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${nfsWdrlRecord.beginTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:true});"/>
				<label style="text-align:center;width:30px">至：</label>
				<input name="endTime" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${nfsWdrlRecord.endTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"
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
				<th style="text-align:center">提现编号</th>
				<th style="text-align:center">会员姓名</th>
				<th style="text-align:center">手机号</th>
				<th style="text-align:center">提现申请金额</th>
				<th style="text-align:center">连连付款金额</th>
				<th style="text-align:center">手续费</th>
				<th style="text-align:center">银行卡号</th>
				<th style="text-align:center">银行名称</th>
				<th style="text-align:center">提现状态</th>
				<th style="text-align:center">提现方式</th>
				<th style="text-align:center">提现说明</th>
				<th style="text-align:center">申请时间</th>
				<th style="text-align:center">审核时间</th>
				<th style="text-align:center">打款时间</th>
				<th style="text-align:center">第三方单号</th>
				<shiro:hasPermission name="wdrl:wdrlRecord:edit"><th style="text-align:center">操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="wdrlRecord">
			<tr id="${wdrlRecord.id}">
				<td style="text-align:center"><a href="${ctx}/wdrlRecord/query?id=${wdrlRecord.id}">				
					${wdrlRecord.id}				
				</a></td>
				<td style="text-align:center"><a href="${ctx}/member/query?id=${wdrlRecord.member.id}">					
					${wdrlRecord.member.name}					
				</a></td>
				<td style="text-align:center">
					${wdrlRecord.member.username}
				</td>
				<td style="text-align:center">
					<a href ="${admin}/memberActTrx/trx?member.id=${wdrlRecord.member.id}">${fns:decimalToStr(wdrlRecord.amount,2)}</a>
			    </td>
				<td style="text-align:center">
					${fns:decimalToStr(wdrlRecord.payAmount,2)}
				</td>
				<td style="text-align:center">
					${fns:decimalToStr(wdrlRecord.fee,2)}
				</td>
				<td style="text-align:center">
					${wdrlRecord.cardNo}
				</td>
				<td style="text-align:center">
					${wdrlRecord.bankName}
				</td>
				<td style="text-align:center">
					<span id="status${wdrlRecord.id}">${fns:getDictLabel(wdrlRecord.status, 'wdrlRecordStatus', '')}</span>
				</td>
				<td style="text-align:center">
					${fns:getDictLabel(wdrlRecord.type, 'wdrlRecordType', '')}
				</td>
				<td style="text-align:center">
					${wdrlRecord.rmk}
				</td>
				<td style="text-align:center">
					<fmt:formatDate value="${wdrlRecord.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td style="text-align:center">
					<fmt:formatDate value="${wdrlRecord.checkTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td style="text-align:center">
					<fmt:formatDate value="${wdrlRecord.payTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td style="text-align:center">
					${wdrlRecord.thirdOrderNo}
				</td>
				<td style="text-align:center">
					<span id="check${wdrlRecord.id}">
						<shiro:hasPermission name="wdrl:wdrlRecord:edit">
								<c:if test="${wdrlRecord.status eq 'auditing' || wdrlRecord.status eq 'mayRepeatOrder' || wdrlRecord.status eq 'questionOrder'}">
									<%-- <a  href="javascript:void(0)" onclick='showCancelWdrlBalDialog("${wdrlRecord.id}");'>撤销提现</a> | --%>
				    				<a href="${admin}/wdrlRecord/checkPass?id=${wdrlRecord.id}">通过审核</a>||
				    				<a href="${admin}/wdrlRecord/refuse?id=${wdrlRecord.id}">拒绝提现</a>||
								</c:if>
								<a href="${admin}/wdrlRecord/successConfirm?id=${wdrlRecord.id}">汇款成功</a>||
								<c:if test="${wdrlRecord.status != 'failure'}">
									<a href="${admin}/wdrlRecord/failedRemitted?id=${wdrlRecord.id}">汇款失败</a>||
								</c:if>
				    			<%-- <a  href="javascript:void(0)"onclick='failedRemitted("${wdrlRecord.id}");'>汇款失败 </a> | --%> 
								<a  href="javascript:void(0)"onclick='queryOrder("${wdrlRecord.id}");'>查询订单</a>
						</shiro:hasPermission>
					</span>
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>