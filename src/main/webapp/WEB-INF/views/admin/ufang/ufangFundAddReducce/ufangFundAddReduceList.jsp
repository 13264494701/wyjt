<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>优放机构加减款记录管理</title>
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
		function pass(id){
			var confirmMsg = confirm("确定要通过审核吗？");
			if(confirmMsg){
				$.ajax({
					url:'${ctx}/ufangFundAddReduce/applyCheck',
					type:'POST',
					data:{
						ufangFundAddReduceId:id,
						status:'pass'
					},
					dataType:'json',
					success:function(res){
						if(res.code == '0'){
							$("#status"+id).text("已通过");
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
			var confirmMsg = confirm("确定要拒绝该申请吗？");
			if(confirmMsg){
				$.ajax({
					url:'${ctx}/ufangFundAddReduce/applyCheck',
					type:'POST',
					data:{
						ufangFundAddReduceId:id,
						status:'reject'
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
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/ufangFundAddReduce/">优放机构加减款记录列表</a></li>
	<%-- 	<shiro:hasPermission name="ufangbrnfundaddreducce:ufangFundAddReduce:edit"><li><a href="${ctx}/ufangBrnAct/reduceActBal">优放机构加减款记录添加</a></li></shiro:hasPermission> --%>
	</ul>
	<form:form id="searchForm" modelAttribute="ufangFundAddReduce" action="${ctx}/ufangFundAddReduce/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label style="text-align:right;width:150px">公司名称：</label>
				<form:input path="ufangBrn.brnName" htmlEscape="false"  class="input-medium"/>
			</li>
			<li><label style="text-align:right;width:150px">交易类型：</label>
				<form:select path="type"  style="width:177px" class="input-medium">
					<form:option value="" label="请选择"/>
					<form:options items="${fns:getDictList('ufangFundAddReduceType')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</li>
			<li><label style="text-align:right;width:150px">交易金额：</label>
				<form:input path="amount" htmlEscape="false" class="input-medium"/>
			</li>
			<li><label style="text-align:right;width:150px">交易状态：</label>
				<form:select path="status"  style="width:177px" class="input-medium">
					<form:option value="" label="请选择"/>
					<form:options items="${fns:getDictList('ufangFundAddReduceStatus')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</li>
			<li><label style="text-align:right;width:150px">申请时间：</label>
				<input name="beginTime" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${beginTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:true});"/>
				<label style="text-align:center;width:30px">至：</label>
				<input name="endTime" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${endTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"
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
				<th style="text-align:center">业务编号</th>
				<th style="text-align:center">公司名称</th>
				<th style="text-align:center">交易类型</th>
				<th style="text-align:center">交易金额</th>
				<th style="text-align:center">账户余额</th>
				<th style="text-align:center">币种</th>
				<th style="text-align:center">交易状态</th>
				<th style="text-align:center">备注</th>
				<th style="text-align:center">创建时间</th>
				<shiro:hasPermission name="ufangbrnfundaddreducce:ufangFundAddReduce:edit"><th style="text-align:center">操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="ufangFundAddReduce">
			<tr>
				<td style="text-align:center">
					<a href="${ctx}/ufangFundAddReduce/query?id=${ufangFundAddReduce.id}">
						${ufangFundAddReduce.id}
					</a>
				</td>
				<td style="text-align:center">
					<a href="${ctx}/ufang/brn/query?id=${ufangFundAddReduce.ufangBrn.id}">
						${ufangFundAddReduce.ufangBrn.brnName}
					</a>
				</td>
				<td style="text-align:center">
					${fns:getDictLabel(ufangFundAddReduce.type, 'ufangFundAddReduceType', '')}
				</td>
				<td style="text-align:center">
					${fns:decimalToStr(ufangFundAddReduce.amount,2)}
				</td>
				<td style="text-align:center">
					${ufangFundAddReduce.curBal}
				</td>
				<td style="text-align:center">
					${ufangFundAddReduce.currCode}
				</td>
				<td id="status${ufangFundAddReduce.id}" style="text-align:center">
					${fns:getDictLabel(ufangFundAddReduce.status, 'ufangFundAddReduceStatus', '')}
				</td>
				<td style="text-align:center">
					${ufangFundAddReduce.rmk}
				</td>
				<td style="text-align:center">
					<fmt:formatDate value="${ufangFundAddReduce.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td  id="check${ufangFundAddReduce.id}" style="text-align:center">
				<shiro:hasPermission name="ufangbrnfundaddreducce:ufangFundAddReduce:edit">
    				<c:if test="${ufangFundAddReduce.status eq 'auditing'}">
						<a  href="javascript:void(0)" onclick='pass("${ufangFundAddReduce.id}");'>通过申请</a> |
						<a  href="javascript:void(0)"onclick='refuse("${ufangFundAddReduce.id}");'>拒绝申请</a> 
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