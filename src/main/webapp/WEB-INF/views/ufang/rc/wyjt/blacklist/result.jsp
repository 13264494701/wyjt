<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>查询结果</title>
<meta name="decorator" content="default" />
<style>
.btn {
	font-size: 18px;
	height: 35px;
	display: flex;
	align-items: center;
	justify-content: center;
	border-radius: 4px;
	color: #ffffff;
	background-color: #3daae9;
	background-image: linear-gradient(to bottom, #46aeea, #2fa4e7);
	border-color: #2fa4e7 #2fa4e7 #157ab5;
	border-color: rgba(0, 0, 0, 0.1) rgba(0, 0, 0, 0.1) rgba(0, 0, 0, 0.25);
}
</style>
</head>
<body>
	<h1>查询结果</h1>
	<div style="padding: 10px 10px 10px 0px">
		<div class="control-group">
			<c:if test="${isMatch}">
				<label style="text-align:right; color:red;font-size:16px;font-weight:bold;">该用户命中无忧借条黑名单</label>
				<br>
			</c:if>
			<c:if test="${!isMatch}">
				<label style="text-align:right;font-size:16px;font-weight:bold;">该用户在无忧借条平台信誉良好，没有逾期15天未还的借条</label>
				<br>
			</c:if>
		</div>
		<div style="width:400px;height:200px;">
			<div style="float: left;">
				<div class="control-group">
					<label style="text-align: left; width: 150px">姓名：${order.qName}</label><br>
				</div>
				<div class="control-group">
					<label style="text-align: left; width: 150px">年龄：${age}</label><br>
				</div>
				<div class="control-group">
					<label style="text-align: left; width: 220px">手机号码：${order.qPhoneNo}</label><br>
				</div>
				<div class="control-group">
					<label style="text-align: left; width: 220px">身份证号：${order.qIdNo}</label><br>
				</div>

				<div class="control-group">
					<label style="text-align: left; width: 150px">逾期15天的借条：<c:if test="${isMatch}"><font style="color:red;font-weight:bold;"></c:if>${overDueCnt}笔<c:if test="${isMatch}"></font></c:if></label><br>
				</div>
				<div class="control-group">
					<label style="text-align: left; width: 220px">逾期15天的金额：<c:if test="${isMatch}"><font style="color:red;font-weight:bold;"></c:if>${overDueAmt}元<c:if test="${isMatch}"></font></c:if></label><br>
				</div>
			</div>
			<div style="float: left; width: 100px; text-align: left; margin-left: 10px;">
				<c:if test="${isMatch}">
					<img src="/icon/ufang/is_match.png">
				</c:if>
				<c:if test="${!isMatch}">
					<img src="/icon/ufang/no_match.png">
				</c:if>
			</div>
		</div>
	</div>
	<div style="padding: 0px 10px 0px 10px; display:block">
		<a class="btn btn-block" style="margin-top: 10px; width: 400px" href="${ufang}/rcWyjt/blacklist/query">继续查询</a>
	</div>
</body>
</html>