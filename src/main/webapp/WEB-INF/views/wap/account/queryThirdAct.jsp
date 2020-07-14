<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title></title>
</head>
<body>
	<c:if test="${failed eq true}">
		<div>
			<label style="text-align: right; width: 250px">查询出错啦！请联系管理员处理！</label>
		</div>
	</c:if>
	<c:if test="${failed ne true}">
		<table style="font-size: 75px">
			<tr>
				<th colspan="4">富友充值账户</th>
				<c:if test="${fyfailed eq true}">
					<td>查询出错啦！请联系管理员处理！</td>
				</c:if>
			</tr>
			<c:if test="${fyfailed ne true}">
				<tr>
					<td>账面余额(元)</td>
					<td>${ctamt}</td>
				</tr>
				<tr>
					<td>可用余额(元)</td>
					<td>${caamt}</td>
				</tr>
				<tr>
					<td>待结转余额(元)</td>
					<td>${cuamt}</td>
				</tr>
				<tr>
					<td>冻结余额(元)</td>
					<td>${cfamt}</td>
				</tr>
			</c:if>
			<tr>
				<th colspan="4">富友代付账户</th>
				<c:if test="${fytxfailed eq true}">
					<td>查询出错啦！请联系管理员处理！</td>
				</c:if>
			</tr>
			<c:if test="${fytxfailed ne true}">
				<tr>
					<td>账面余额(元)</td>
					<td>${txctamt}</td>
				</tr>
				<tr>
					<td>可用余额(元)</td>
					<td>${txcaamt}</td>
				</tr>
				<tr>
					<td>待结转余额(元)</td>
					<td>${txcuamt}</td>
				</tr>
				<tr>
					<td>冻结余额(元)</td>
					<td>${txcfamt}</td>
				</tr>
			</c:if>
			<tr>
				<th colspan="4">连连账户</th>
			</tr>
			<tr>
				<c:if test="${llfailed eq true}">
					<td>查询出错啦！请联系管理员处理！</td>
				</c:if>
				<c:if test="${llfailed ne true}">
					<td>可用余额(元)</td>
					<td>${lianlianBal}</td>
				</c:if>
			</tr>
		</table>
	</c:if>
</body>
</html>