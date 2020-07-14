<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>流量管理管理</title>
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
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li <c:if test="${channel eq 'ufang'}">class="active" </c:if>><a href="${admin}/ufangLoaneeData/?channel=ufang">优放贷流量</a></li>
	</ul>
	<form:form id="searchForm" modelAttribute="ufangLoaneeData" action="${admin}/ufangLoaneeData/?channel=${channel}" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
	        <li><label style="text-align:right;width:100px">年龄：</label>
	            <form:input path="minAge" htmlEscape="false" maxlength="3" class="input-medium" cssStyle="width: 30px"/>-
	            <form:input path="maxAge" htmlEscape="false" maxlength="3" class="input-medium" cssStyle="width: 30px"/>
	        </li>
	        <li><label style="text-align:right;width:100px">芝麻分：</label>
	            <form:input path="minZmf" htmlEscape="false" maxlength="4" class="input-medium"
	                        cssStyle="width: 30px"/>-
	            <form:input path="maxZmf" htmlEscape="false" maxlength="4" class="input-medium"
	                        cssStyle="width: 30px"/>
	        </li>
	        <li><label style="text-align:right;width:100px">所在地区：</label>
				<form:input path="applyArea" htmlEscape="false" maxlength="64" class="input-medium"/>
		    </li>
	        <li><label style="text-align:right;width:100px">申请时间：</label>
					<input name="beginApplyTime" type="text" maxlength="20" class="input-medium Wdate required"
						value="<fmt:formatDate value="${ufangLoaneeData.beginApplyTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"
						onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',startDate:'%y-%M-%d 00:00:00',isShowClear:true});"/>
					<label style="text-align:center;width:30px">至：</label>
					<input name="endApplyTime" type="text" maxlength="20" class="input-medium Wdate required"
						value="<fmt:formatDate value="${ufangLoaneeData.endApplyTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"
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
				<th style="text-align:center">借款人姓名</th>
				<th style="text-align:center">手机号码</th>
				<th style="text-align:center">年龄</th>
				<th style="text-align:center">芝麻分</th>
		        <c:if test="${channel eq 'weixin'}">
					 <th style="text-align:center">QQ号码</th>
		             <th style="text-align:center">微信账号</th>
				</c:if>
		        <c:if test="${channel eq 'ufang'}">
					<th style="text-align:center">运营商状态</th> 
				</c:if>
				<th style="text-align:center">申请金额</th>
				<th style="text-align:center">价格</th>
				<th style="text-align:center">销售量</th>
			    <th style="text-align:center">申请IP归属</th>
                <th style="text-align:center">手机号归属</th>
				<th style="text-align:center">申请时间</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="ufangLoaneeData">
			<tr>
				<td style="text-align:center"><a href="${admin}/ufangLoaneeData/query?id=${ufangLoaneeData.id}">
					${ufangLoaneeData.name}
				</a></td>
				<td style="text-align:center">
					${ufangLoaneeData.phoneNo}
				</td>
				<td style="text-align:center">
					${ufangLoaneeData.age}
				</td>
				<td style="text-align:center">
					${ufangLoaneeData.zhimafen}
				</td>
	            <c:if test="${channel eq 'weixin'}">
	              <td style="text-align:center">
	                    ${ufangLoaneeData.qqNo}
	              </td>
	              <td style="text-align:center">
	                    ${ufangLoaneeData.weixinNo}
	              </td>
	            </c:if>
	            <c:if test="${channel eq 'ufang'}">
	               <td style="text-align:center">
	                   ${fns:getDictLabel(ufangLoaneeData.yunyingshangStatus, 'YunyingshangStatus', '')}
	               </td> 
	            </c:if>
				<td style="text-align:center">
					${ufangLoaneeData.applyAmount}
				</td>
				<td style="text-align:center">
					${fns:decimalToStr(ufangLoaneeData.price,2)}
				</td>
				<td style="text-align:center">
					${ufangLoaneeData.sales}
				</td>
                <td style="text-align:center">
					${ufangLoaneeData.applyArea}
			    </td>
			    <td style="text-align:center">
					${ufangLoaneeData.phoneArea}
			    </td>
				<td style="text-align:center">
					<fmt:formatDate value="${ufangLoaneeData.updateTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>