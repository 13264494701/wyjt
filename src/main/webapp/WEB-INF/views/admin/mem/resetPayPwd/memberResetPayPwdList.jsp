<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>修改支付密码管理</title>
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
		function seachLivingPhoto(id) {
			$.ajax({
				url:"${ctx}/memberResetPayPwd/getLivingPhoto",
				type:"post",
				data:{id:id},
				async:false,
				success:function(result) {
				window.open(result);
			}
			});
		}
		
		
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/memberResetPayPwd/">修改支付密码列表</a></li>
	</ul>
	<form:form id="searchForm" modelAttribute="memberResetPayPwd" action="${ctx}/memberResetPayPwd/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label style="text-align:right;width:150px">手机号码：</label>
				<form:input path="member.username" htmlEscape="false" maxlength="20" class="input-medium"/>
			</li>
			<li><label style="text-align:right;width:150px">用户姓名：</label>
				<form:input path="member.name" htmlEscape="false" maxlength="20" class="input-medium"/>
			</li>
			<li><label style="text-align:right;width:150px">认证状态 ：</label>
				<form:select path="status"  style="width:177px" class="input-medium">
					<form:option value="" label="请选择"/>
					<form:options items="${fns:getDictList('pwdStatus')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</li>
			<li><label style="text-align:right;width:80px">申请时间：</label>
				<input name="beginTime" type="text"  maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${memberResetPayPwd.beginTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:true});"/> - 
				<input name="endTime" type="text"  maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${memberResetPayPwd.endTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"
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
			    <th style="text-align:center">会员编号</th>
			    <th style="text-align:center">会员姓名</th>
				<th style="text-align:center">用户名</th>
				<th style="text-align:center">相关照片</th>	
				<th style="text-align:center">认证状态</th>				
				<th style="text-align:center">申请时间</th>				
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="memberResetPayPwd">
			<tr>
			    <td style="text-align:center">
					${memberResetPayPwd.member.id}<a href="${ctx}/member/query?id=${memberResetPayPwd.member.id}">
				</a></td>
				<td style="text-align:center">
					${memberResetPayPwd.member.name}
				</td>
				<td style="text-align:center">
					${memberResetPayPwd.member.username}
				</td>
				<td style="text-align:center">
					<a href="javascript:void(0);" onclick="seachLivingPhoto(${memberResetPayPwd.id})">活体</a>
				</td>
				<td style="text-align:center">
				    ${fns:getDictLabel(memberResetPayPwd.status, 'videoVerifyStatus', '')}
				</td>
				<td style="text-align:center">
					<fmt:formatDate value="${memberResetPayPwd.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>