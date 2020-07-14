<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<html>
<head>
    <title>充值记录</title>
    <meta name="decorator" content="default"/>
    <script src="${ctxStatic}/artdialog/js/dialog-plus.js"></script>
    <link href="${ctxStatic}/artdialog/css/dialog.css" rel="stylesheet" />
    <script type="text/javascript">
        $(document).ready(function () {
			$("#btnExport").click(function() {
				top.$.jBox.confirm("确认要导出优放充值数据吗？", "系统提示", function(v, h, f) {
					if (v == "ok") {
						$("#searchForm").attr("action", "${admin}/account/exportUfangRchgData");
						$("#searchForm").submit();
					}
				}, {
					buttonsFocus : 1
				});
				top.$('.jbox-body .jbox-icon').css('top', '55px');
			});
        });

        function page(n, s) {
            $("#pageNo").val(n);
            $("#pageSize").val(s);
            $("#searchForm").submit();
            return false;
        }
		function showActTrxDetailDialog(obj){
		    var rchgId = $(obj).closest("tr").attr("id");
			var url = '${admin}/ufangBrnActTrx/list?orgId='+rchgId;
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
    <li class="active"><a href="${admin}/account/ufangRchgList">充值记录</a></li>
</ul>
<form:form id="searchForm" modelAttribute="rchgRecord" action="${admin}/account/ufangRchgList" method="post"
           class="breadcrumb form-search">
    <input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
    <input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
    <ul class="ul-form">
		<li><label style="text-align:right;width:150px">交易状态：</label>
				<form:select path="status"  style="width:177px" class="input-medium">
					<form:option value="" label="请选择"/>
					<form:options items="${fns:getDictList('rechargeStatus')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
		</li>

        <li><label style="text-align:right;width:80px">充值时间：</label>
            <input name="beginTime" type="text" maxlength="20" class="input-medium Wdate"
                   value="<fmt:formatDate value="${ufangRchgRecord.beginTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"
                   onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',startDate:'%y-%M-%d 00:00:00',isShowClear:true});"/> -
            <input name="endTime" type="text" maxlength="20" class="input-medium Wdate"
                   value="<fmt:formatDate value="${ufangRchgRecord.endTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"
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
        <th style="text-align:center">姓名</th>
        <th style="text-align:center">充值金额</th>
        <th style="text-align:center">商户订单号</th>
        <th style="text-align:center">第三方单号</th>
        <th style="text-align:center">充值状态</th>
        <th style="text-align:center">充值时间</th>
        <th style="text-align:center">操作</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach items="${page.list}" var="ufangRchgRecord">
        <tr  id="${ufangRchgRecord.id}">
            <td style="text-align:center">
               ${ufangRchgRecord.user.empNam}
            </td>
            <td style="text-align:center">
                ${ufang:decimalToStr(ufangRchgRecord.amount,2)}
            </td>
			<td style="text-align:center">
				${ufangRchgRecord.payment.paymentNo}
			</td>
            <td style="text-align:center">
                ${ufangRchgRecord.payment.thirdPaymentNo}
            </td>
            <td style="text-align:center">
                ${fns:getDictLabel(ufangRchgRecord.status, 'rechargeStatus', '')}
            </td>
            <td style="text-align:center">
                <fmt:formatDate value="${ufangRchgRecord.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
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