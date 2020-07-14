<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>视频认证管理</title>
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
		<li class="active"><a href="${ctx}/memberVideoVerify/">视频认证列表</a></li>		
	</ul>
	<form:form id="searchForm" modelAttribute="memberVideoVerify" action="${ctx}/memberVideoVerify/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label style="text-align:right;width:150px">会员编号：</label>
				<form:input path="member.id" htmlEscape="false" maxlength="64" class="input-medium"/>
			</li>
			<li><label style="text-align:right;width:150px">真实姓名：</label>
				<form:input path="realName" htmlEscape="false" maxlength="255" class="input-medium"/>
			</li>
			<li><label style="text-align:right;width:150px">身份证号：</label>
				<form:input path="idNo" htmlEscape="false" maxlength="255" class="input-medium"/>
			</li>
			<li><label style="text-align:right;width:150px">认证类型：</label>
				<form:select path="type"  style="width:177px" class="input-medium">
					<form:option value="" label="请选择"/>
					<form:options items="${fns:getDictList('videoVerifyType')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</li>
			<li><label style="text-align:right;width:150px">认证状态：</label>
				<form:select path="status"  style="width:177px" class="input-medium">
					<form:option value="" label="请选择"/>
					<form:options items="${fns:getDictList('videoVerifyStatus')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</li>
			<li><label style="text-align:right;width:150px">认证渠道：</label>
				<form:select path="channel"  style="width:177px" class="input-medium">
					<form:option value="" label="请选择"/>
					<form:options items="${fns:getDictList('videoVerifyChannel')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
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
			    <th style="text-align:center">真实姓名</th>
				<th style="text-align:center">身份证号</th>
				<th style="text-align:center">民族</th>
				<th style="text-align:center">认证类型</th>
				<th style="text-align:center">认证状态</th>
				<th style="text-align:center">认证渠道</th>
				<th style="text-align:center">相关照片</th>	
				<th style="text-align:center">申请时间</th>	
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="memberVideoVerify">
			<tr>
			    <td style="text-align:center"><a href="${ctx}/member/query?id=${memberVideoVerify.member.id}">
					${memberVideoVerify.member.id}
				</a></td>
			    <td style="text-align:center">
					${memberVideoVerify.realName}
				</td>
				<td style="text-align:center">
					${memberVideoVerify.idNo}
				</td>
				<td style="text-align:center">
					${memberVideoVerify.nation}
				</td>
				<td style="text-align:center">
					${fns:getDictLabel(memberVideoVerify.type, 'videoVerifyType', '')}
				</td>
				<td style="text-align:center">
					${fns:getDictLabel(memberVideoVerify.status, 'videoVerifyStatus', '')}
				</td>
			    <td style="text-align:center">
					${fns:getDictLabel(memberVideoVerify.channel, 'videoVerifyChannel', '')}
				</td>
				<td style="text-align:center">
					<a href="${memberVideoVerify.idcardFrontPhoto}" target="_blank">正面</a>
					<a href="${memberVideoVerify.idcardBackPhoto}" target="_blank">背面</a>
					<a href="${memberVideoVerify.idcardPortraitPhoto}" target="_blank">头像</a>
					<a href="${memberVideoVerify.livingPhoto}" target="_blank">活体</a>
				</td>
				<td style="text-align:center">
					<fmt:formatDate value="${memberVideoVerify.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>