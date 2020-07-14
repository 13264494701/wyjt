<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>流量订单管理</title>
	<meta name="decorator" content="default"/>
	<script src="${ctxStatic}/artdialog/js/dialog-plus.js"></script>
    <link href="${ctxStatic}/artdialog/css/dialog.css" rel="stylesheet" />
	<script src="${ctxStatic}/artDialog-5.0.3/jquery.artDialog.min.js"></script>
	<script src="${ctxStatic}/artDialog-5.0.3/artDialog.plugins.min.js"></script>
    <link href="${ctxStatic}/artDialog-5.0.3/skins/blue.css" rel="stylesheet" />
	<script type="text/javascript">
		$(document).ready(function() {
			$("#btnExport").click(function() {
				top.$.jBox.confirm("确认要导出流量数据吗？", "系统提示", function(v, h, f) {
					if (v == "ok") {
						$("#searchForm").attr("action", "${ufang}/ufangLoaneeDataOrder/exportData");
						$("#searchForm").submit();
					}
				}, {
					buttonsFocus : 1
				});
				top.$('.jbox-body .jbox-icon').css('top', '55px');
			});
		});
		function showAddRmkForm(obj) {	
			var id = $(obj).closest("tr").attr("id");
			var rmk = $(obj).closest("tr").attr("dataOrderRmk");
			var dataGroup = $(obj).closest("tr").attr("dataGroup");

			var $addRmkSubmit = $("#addRmkSubmit");
			var $addRmkCancel = $("#addRmkCancel");		
			// 提交
			$addRmkSubmit.click(function() {		        
               	var rmk = $("#rmk").val();	
            	var dataGroup = $("#dataGroup").val();
            	var dataGroupText = $("#dataGroup").find("option:selected").text();
				$.ajax({
					type : 'POST',
					url : '${ufang}/ufangLoaneeDataOrder/addDataGroupRmk',
					data : {
						id:id,
						dataGroup:dataGroup,
						rmk:rmk
					},
					success : function(rsp) {
						if(rsp.status == true)
						{		
						   $addRmkSubmit.unbind("click"); 
						   $addRmkCancel.unbind("click");
						   dialog.close();  //关闭窗口后默认会提交表单   
						   showTip(rsp.message);
						   $("#rmk"+id).text(rmk);
						   $("#dataGroup"+id).text(dataGroupText);
						}else{
						   showTip(rsp.message);
						   return false;
						}
					}
				});				    				
			});
			$addRmkCancel.click(function() {	
				$addRmkSubmit.unbind("click"); 
				$addRmkCancel.unbind("click");
				dialog.close(); 
				return false;
			});
			var dialog = $.dialog({
		    	id: "addRmk",
		    	title: '分类备注',
		    	width: 660,
		    	height: 300,
		    	padding: 0,
		    	drag:true,
		    	cancel:false,
		        content: document.getElementById("addRmk")
		    });
			
			$("#rmk").val(rmk);
			$("#dataGroup").val(dataGroup).select2();//需要放在dialog初始化后面，否则会被重新初始化

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
		<li class="active"><a href="${ufang}/ufangLoaneeDataOrder/">已购流量列表</a></li>
	</ul>	
	<form:form id="searchForm" modelAttribute="ufangLoaneeDataOrder" action="${ufang}/ufangLoaneeDataOrder/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">

	        <li><label style="text-align:right;width:100px">购买时间：</label>
					<input name="beginTime" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate required"
						value="<fmt:formatDate value="${ufangLoaneeDataOrder.beginTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"
						onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',startDate:'%y-%M-%d 00:00:00',isShowClear:true});"/>
					<label style="text-align:center;width:30px">至：</label>
					<input name="endTime" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate required"
						value="<fmt:formatDate value="${ufangLoaneeDataOrder.endTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"
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
	<div id="addRmk" hidden>
        <form:form id="addRmkForm" modelAttribute="ufangLoaneeDataOrder" action="${ufang}/ufangLoaneeDataOrder/addRmk" method="post" class="breadcrumb form-search">      
			<div class="control-group">
			<label style="text-align:right;width:80px">流量分类：</label>
			<form:select path="dataGroup"  style="width:177px" class="input-medium">
				<form:options items="${fns:getDictList('dataGroup')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
			</form:select>
			</div>
            <div class="control-group">
			<label style="text-align:right;width:80px">分类备注：</label>	
			<form:textarea path="rmk" htmlEscape="false" rows="8" maxlength="1024" class="input-xxlarge"/>					 				
			</div>
			<div class="control-group">
			<label style="text-align:right;width:150px"></label>
			<input id="addRmkSubmit" class="btn btn-primary " style="width:80px;" type="button" value="确认"/>
			<label style="text-align:right;height:30px;line-height:30px;width:5px"></label>
			<input id="addRmkCancel" class="btn btn-primary " style="width:80px;" type="button" value="取消"/>
			</div>											
        </form:form>
	</div>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th style="text-align:center">购买人</th>
				<th style="text-align:center">借款人姓名</th>
				<th style="text-align:center">手机号码</th>
				<th style="text-align:center">年龄</th>
				<th style="text-align:center">芝麻分</th>
				<th style="text-align:center">运营商</th>
				<th style="text-align:center">QQ号码</th>
				<th style="text-align:center">微信账号</th>
				<th style="text-align:center">申请金额</th>
				<th style="text-align:center">价格</th>
				<th style="text-align:center">购买时间</th>
				<th style="text-align:center">分类</th>
				<th style="text-align:center">备注</th>
				<th style="text-align:center">操作</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="ufangLoaneeDataOrder">
			<tr id="${ufangLoaneeDataOrder.id}" dataOrderRmk="${ufangLoaneeDataOrder.rmk}" dataGroup="${ufangLoaneeDataOrder.dataGroup}">
				<td style="text-align:center">
					${ufangLoaneeDataOrder.user.empNam}
				</td>
				<td style="text-align:center">
						${ufangLoaneeDataOrder.data.name}
				</td>
				<td style="text-align:center">
						${ufangLoaneeDataOrder.data.phoneNo}
				</td>
				<td style="text-align:center">
						${ufangLoaneeDataOrder.data.age}
				</td>
				<td style="text-align:center">
						${ufangLoaneeDataOrder.data.zhimafen}
				</td>
				<td style="text-align:center">
					<c:if test="${ufangLoaneeDataOrder.data.yunyingshangStatus eq 'verified'}">
						<a href="${ufang}/rcSjmh/report?taskId=${ufangLoaneeDataOrder.data.reportTaskId}" target="_blank">查看报告</a>
					</c:if>
					<c:if test="${ufangLoaneeDataOrder.data.yunyingshangStatus eq 'unverified'}">
						未认证
					</c:if>
					<c:if test="${ufangLoaneeDataOrder.data.yunyingshangStatus eq 'expired'}">
						已过期
					</c:if>
				</td>
				<td style="text-align:center">
						${ufangLoaneeDataOrder.data.qqNo}
				</td>
				<td style="text-align:center">
						${ufangLoaneeDataOrder.data.weixinNo}
				</td>
				<td style="text-align:center">
						${ufang:decimalToStr(ufangLoaneeDataOrder.data.applyAmount,2)}
				</td>
				<td style="text-align:center">
						${ufang:decimalToStr(ufangLoaneeDataOrder.amount,2)}
				</td>
				<td style="text-align:center">
					<fmt:formatDate value="${ufangLoaneeDataOrder.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td style="text-align:center">
					<div id="dataGroup${ufangLoaneeDataOrder.id}">${fns:getDictLabel(ufangLoaneeDataOrder.dataGroup, 'dataGroup', '')}</div>
				</td>
				<td style="text-align:center">					
					<div id="rmk${ufangLoaneeDataOrder.id}">${ufangLoaneeDataOrder.rmk}</div>
				</td>
				<td style="text-align:center">
					<a href ="javascript:void(0);" onclick="showAddRmkForm(this)">添加备注</a>	
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>