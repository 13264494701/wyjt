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
		function pass(id){
			var confirmMsg = confirm("确定要通过审核吗？");
			if(confirmMsg){
				$.ajax({
					url:'${ctx}/wdrl/nfsWdrlRecord/checkWdrl',
					type:'POST',
					data:{
						id:id,
						pass:'y'
					},
					dataType:'json',
					success:function(res){
						if(res.code == '0'){
							$("#status"+id).text("待打款");
							$("#check"+id).text("");
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
			var confirmMsg = confirm("确定要拒绝该提现申请吗？");
			if(confirmMsg){
				$.ajax({
					url:'${ctx}/wdrl/nfsWdrlRecord/checkWdrl',
					type:'POST',
					data:{
						id:id,
						pass:'n'
					},
					success:function(res){
						if(res.code == '0'){
							$("#status"+id).text("已拒绝");
							$("#check"+id).text("");
						}else{
							alert(res.getMessage());
						}
					}
					
				});
			}else{
				return false;
			}
		}
		function showCancelWdrlBalDialog(wdrlId){
			var url = '${ctx}/wdrl/nfsWdrlRecord/cancelWdrlRecord?id='+wdrlId;
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
					url:'${admin}/wdrl/nfsWdrlRecord/successedRemitted',
					type:'POST',
					data:{
						id:id
					},
					dataType:'json',
					success:function(res){
						if(res.code == '0'){
							$("#status"+id).text("已打款");
							$("#check"+id).text("");
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
					url:'${ctx}/wdrl/nfsWdrlRecord/failedRemitted',
					type:'POST',
					data:{
						id:id
					},
					dataType:'json',
					success:function(res){
						if(res.code == '0'){
							$("#status"+id).text("打款失败");
							$("#check"+id).text("");
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
			var url = '${ctx}/wdrl/nfsWdrlRecord/queryThirdOrder?id='+wdrlId;
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
		<li class="active"><a href="${ctx}/wdrl/nfsWdrlRecord/">提现记录已审核列表</a></li>
	</ul>
	<form:form id="searchForm" modelAttribute="nfsWdrlRecord" action="${ctx}/wdrlRecord/findAuditedRecord" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
		<li><label style="text-align:right;width:150px">提现编号：</label>
				<form:input path="id" htmlEscape="false" maxlength="32" class="input-medium"/>
			</li>
			<li><label style="text-align:right;width:150px">手机号：</label>
				<form:input path="member.username" htmlEscape="false" maxlength="20" class="input-medium"/>
			</li>
			<li><label style="text-align:right;width:150px">提现金额：</label>
				<form:input path="minAmount" htmlEscape="false" maxlength="20" class="input-medium"/>
				<label style="text-align:center;width:30px">至：</label>
				<form:input path="maxAmount" htmlEscape="false" maxlength="20" class="input-medium"/>
			</li>
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
				<th style="text-align:center">提现金额</th>
				<th style="text-align:center">银行卡号</th>
				<th style="text-align:center">银行名称</th>
				<th style="text-align:center">提现状态</th>
				<th style="text-align:center">提现方式</th>
				<th style="text-align:center">提现说明</th>
				<th style="text-align:center">申请时间</th>
				<th style="text-align:center">审核时间</th>
				<th style="text-align:center">打款时间</th>
				<th style="text-align:center">第三方单号</th>
				<shiro:hasPermission name="wdrl:nfsWdrlRecord:edit"><th style="text-align:center">操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="nfsWdrlRecord">
			<tr id="${nfsWdrlRecord.id}">
				<td style="text-align:center">
					<a href="${ctx}/wdrl/nfsWdrlRecord/query?id=${nfsWdrlRecord.id}">
						${nfsWdrlRecord.id}
					</a>
				</td>
				<td style="text-align:center">
					<a href="${ctx}/member/query?id=${nfsWdrlRecord.member.id}">
						${nfsWdrlRecord.member.name}
					</a>
				</td>
				<td style="text-align:center">
					${nfsWdrlRecord.member.username}
				</td>
				<td style="text-align:center">
					${fns:decimalToStr(nfsWdrlRecord.amount,2)}
				</td>
				<td style="text-align:center">
					${nfsWdrlRecord.cardNo}
				</td>
				<td style="text-align:center">
					${nfsWdrlRecord.bankName}
				</td>
				<td style="text-align:center">
					<span id="status${nfsWdrlRecord.id}">${fns:getDictLabel(nfsWdrlRecord.status, 'wdrlRecordStatus', '')}</span>
				</td>
				<td style="text-align:center">
					${fns:getDictLabel(nfsWdrlRecord.type, 'wdrlRecordType', '')}
				</td>
				<td style="text-align:center">
					${nfsWdrlRecord.rmk}
				</td>
				<td style="text-align:center">
					<fmt:formatDate value="${nfsWdrlRecord.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td style="text-align:center">
					<fmt:formatDate value="${nfsWdrlRecord.checkTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td style="text-align:center">
					<fmt:formatDate value="${nfsWdrlRecord.payTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td style="text-align:center">
					${nfsWdrlRecord.thirdOrderNo}
				</td>
				<td style="text-align:center">
					<span id="check${nfsWdrlRecord.id}">
						<shiro:hasPermission name="wdrl:nfsWdrlRecord:edit">
								<c:if test="${nfsWdrlRecord.status eq 'auditing' || nfsWdrlRecord.status eq 'mayRepeatOrder'}">
									<a  href="javascript:void(0)" onclick='showCancelWdrlBalDialog("${nfsWdrlRecord.id}");'>撤销提现</a> |
				    				<a  href="javascript:void(0)" onclick='pass("${nfsWdrlRecord.id}");'>通过审核</a> |
				    				<a  href="javascript:void(0)"onclick='refuse("${nfsWdrlRecord.id}");'>拒绝申请</a> |
								</c:if>
				    			<a  href="javascript:void(0)"onclick='successedRemitted("${nfsWdrlRecord.id}");'>汇款成功</a> |
				    			<a  href="javascript:void(0)"onclick='failedRemitted("${nfsWdrlRecord.id}");'>汇款失败 </a> | 
								<a  href="javascript:void(0)"onclick='queryOrder("${nfsWdrlRecord.id}");'>查询订单</a>
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