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
			$("#btnExport").click(function() {
				top.$.jBox.confirm("确认要导出提现数据吗？", "系统提示", function(v, h, f) {
					if (v == "ok") {
						$("#searchForm").attr("action", "${admin}/account/exportWdrlData");
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

	
		function showQueryThirdOrder(obj){
			var wdrlId = $(obj).closest("tr").attr("id");
			var url = '${admin}/wdrlRecord/queryThirdOrder?id='+wdrlId;
			dialog({
		    	id: "queryThirdOrder",
		    	title: '第三方查询',
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
		function showActTrxDetailDialog(obj){
		    var wdrlId = $(obj).closest("tr").attr("id");
			var url = '${admin}/memberActTrx/list?orgId='+wdrlId;
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
		<li class="active"><a href="${admin}/account/wdrlList">提现记录列表</a></li>
	</ul>
	<form:form id="searchForm" modelAttribute="wdrlRecord" action="${admin}/account/wdrlList" method="post" class="breadcrumb form-search">
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
			<li><label style="text-align:right;width:150px">提现状态：</label>
				<form:select path="status"  style="width:177px" class="input-medium">
					<form:option value="" label="请选择"/>
					<form:options items="${fns:getDictList('wdrlRecordStatus')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</li>
			<li><label style="text-align:right;width:150px">时间：</label>
				<input name="beginTime" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate required"
					value="<fmt:formatDate value="${wdrlRecord.beginTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',startDate:'%y-%M-%d 00:00:00',isShowClear:true});"/>
				<label style="text-align:center;width:30px">至：</label>
				<input name="endTime" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate required"
					value="<fmt:formatDate value="${wdrlRecord.endTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"
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
				<td style="text-align:center">				
					${wdrlRecord.member.name}					
				</td>
				<td style="text-align:center">
					${wdrlRecord.member.username}
				</td>
				<td style="text-align:center">
					${fns:decimalToStr(wdrlRecord.amount,2)}
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
				   <a href ="javascript:void(0);" onclick="showActTrxDetailDialog(this);">流水明细</a> 
				   <br>
				   <a href="javascript:void(0)"onclick="showQueryThirdOrder(this)">查询第三方</a>
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>