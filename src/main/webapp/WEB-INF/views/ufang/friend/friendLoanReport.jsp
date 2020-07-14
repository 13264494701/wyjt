<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>用户信用档案</title>
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
		
		function pass(obj){
			var type = obj.value;
			window.location.href = "${ufang}/friend/loanReport?id=${memberLoanReport.id}&type="+type;
			
		}
	</script>
	<style type="text/css">
			P{
				background: #2FB6EF;
				height: 40px; 
				color: #FFF;
				font-size:16px;
				font-weight: bold;
				line-height: 40px;
				padding-left: 20px;
				border-radius:5px;
				margin-top: 15px; 
			}
	</style>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${ufang}/friend/loanAnalysis?friend.id=${memberLoanReport.id}">风险画像</a></li>
		<li class="active"><a href="${ufang}/friend/loanReport?id=${memberLoanReport.id}">信用档案</a></li>
	</ul>
	<sys:message content="${message}"/>
	
		<thead>
		<table id="contentTable2" class="table table-striped table-bordered table-condensed">
		<tr>
		    <P>认证信息</P>
		</tr>
		<tr>
			<td style="font-weight:bold">真实姓名</td>
			<td style="font-weight:bold">手机号</td>
			<td style="font-weight:bold">信用记录</td>
			<td style="font-weight:bold">淘宝认证</td>
			<td style="font-weight:bold">运营商认证</td>
			<td style="font-weight:bold">芝麻分</td>
			<td style="font-weight:bold">学信网</td>
			<td style="font-weight:bold">社保</td>
			<td style="font-weight:bold">公积金</td>
		</tr>
		<tr>
			<td>${memberLoanReport.name}</td>
			<td>${memberLoanReport.username}</td>
			<td>${memberLoanReport.crStatus}</td>
			<td>${memberLoanReport.taobaoStatus}</td>
			<td>${memberLoanReport.yunyingshangStatus}</td>
			<td>${memberLoanReport.zhimafenStatus}</td>
			<td>${memberLoanReport.xuexingwangStatus}</td>
			<td>${memberLoanReport.shebaoStatus}</td>
			<td>${memberLoanReport.gongjijingStatus}</td>
		</tr>
		</table>
		</thead>
		
		<tbody>
			<table id="contentTable" class="table table-striped table-bordered table-condensed">
			<tr>
			    <td style="font-weight:bold;;background-color: #2FB6EF;color: white" colspan ="4">信用记录(数据源于无忧借条)</td>
			</tr>
			<tr>
			    <td style="text-align:center;font-weight:bold">时间</td>
			    <td style="text-align:center;font-weight:bold" colspan ="3">
					<select onchange="pass(this);">
						<option value="7" <c:if test="${memberLoanReport.type == 7}">selected="selected"</c:if>>一周</option>
						<option value="30" <c:if test="${memberLoanReport.type == 30}">selected="selected"</c:if>>一月</option>
						<option value="180" <c:if test="${memberLoanReport.type == 180}">selected="selected"</c:if>>半年</option>
						<option value="9999" <c:if test="${memberLoanReport.type == 9999}">selected="selected"</c:if>>总计</option>
					</select>
				</td>
			   
			</tr>
			<tr>
			 	<td style="text-align:center;font-weight:bold">类别</td>
			 	<td style="text-align:center;font-weight:bold">借条类型</td>
			 	<td style="text-align:center;font-weight:bold">条数</td>
			 	<td style="text-align:center;font-weight:bold">金额</td>
			</tr>
			<tr>
			 	<td style="text-align:center;font-weight:bold" rowspan = "4">借入借出</td>
			 	<td style="text-align:center;font-weight:bold">待还借条</td>
			 	<td style="text-align:center">${memberLoanReport.pendingReceiveQuantity}</td>
			 	<td style="text-align:center">${memberLoanReport.pendingReceiveAmt}</td>
			</tr>
			<tr>
			 	<td style="text-align:center;font-weight:bold">待收借条</td>
			 	<td style="text-align:center">${memberLoanReport.pendingRepaymentQuantity}</td>
			 	<td style="text-align:center">${memberLoanReport.pendingRepaymentAmt}</td>
			</tr>
			<tr>
			 	<td style="text-align:center;font-weight:bold">借入借条</td>
			 	<td style="text-align:center">${memberLoanReport.loanInQuantity}</td>
			 	<td style="text-align:center">${memberLoanReport.loanInAmt}</td>
			</tr>
			<tr>
			 	<td style="text-align:center;font-weight:bold">借出借条</td>
			 	<td style="text-align:center">${memberLoanReport.loanOutQuantity}</td>
			 	<td style="text-align:center">${memberLoanReport.loanOutAmt}</td>
			</tr>
			<tr>
		 	<td style="text-align:center;font-weight:bold" rowspan = "4">还款情况</td>
			 	<td style="text-align:center;font-weight:bold">按时还款</td>
			 	<td style="text-align:center">${memberLoanReport.onTimeRepayedQuantity}</td>
			 	<td style="text-align:center">${memberLoanReport.onTimeRepayedAmt}</td>
			</tr>
			<tr>
			 	<td style="text-align:center;font-weight:bold">延期还款</td>
			 	<td style="text-align:center">${memberLoanReport.delayRepayedQuantity}</td>
			 	<td style="text-align:center">${memberLoanReport.delayRepayedAmt}</td>
			</tr>
			<tr>
			 	<td style="text-align:center;font-weight:bold">逾期已还</td>
			 	<td style="text-align:center">${memberLoanReport.overdueRepayedQuantity}</td>
			 	<td style="text-align:center">${memberLoanReport.overdueRepayedAmt}</td>
			</tr>
			<tr>
			 	<td style="text-align:center;font-weight:bold">逾期未还</td>
			 	<td style="text-align:center;color:red">${memberLoanReport.overduePendingRepayQuantity}</td>
			 	<td style="text-align:center;color:red">${memberLoanReport.overduePendingRepayAmt}</td>
			</tr>
			<c:if test="${memberLoanReport.taobaoStatus =='已过期'||memberLoanReport.taobaoStatus =='已认证'}">
			<tr>
			    <td style="font-weight:bold;;background-color: #2FB6EF;color: white" colspan ="4">淘宝信息(数据源于淘宝)</td>
			</tr>
			<tr>
				<td style="text-align:center;font-weight:bold" rowspan = "3">用户信息</td>
				<td style="text-align:center;font-weight:bold">花呗信用额度</td>
				<td style="text-align:center" colspan ="2">${memberLoanReport.huabeiAmount}</td>
			</tr>
			<tr>
				<td style="text-align:center;font-weight:bold">余额宝余额</td>
				<td style="text-align:center" colspan ="2">${memberLoanReport.yueAmount}</td>
			</tr>
			<tr>
				<td style="text-align:center;font-weight:bold">默认收货地址</td>
				<td style="text-align:center" colspan ="2">${memberLoanReport.taoboAddr}</td>
			</tr>
			<tr>
				<td style="text-align:center;font-weight:bold" rowspan = "5">消费情况</td>
				<td style="text-align:center;font-weight:bold">半年内订单数量</td>
				<td style="text-align:center" colspan ="2">${memberLoanReport.orderCount}</td>
			</tr>
			<tr>
				<td style="text-align:center;font-weight:bold">半年内订单总额</td>
				<td style="text-align:center" colspan ="2">${memberLoanReport.orderAmount}</td>
			</tr>
			<tr>
				<td style="text-align:center;font-weight:bold">半年内月均消费</td>
				<td style="text-align:center" colspan ="2">${memberLoanReport.oneMonthMoney}</td>
			</tr>
			<tr>
				<td style="text-align:center;font-weight:bold">半年内价格最高商品</td>
				<td style="text-align:center" colspan ="2">${memberLoanReport.maxMoney}</td>
			</tr>
			<tr>
				<td style="text-align:center;font-weight:bold">半年内价格最低商品</td>
				<td style="text-align:center" colspan ="2">${memberLoanReport.minMoney}</td>
			</tr>
			</c:if>
			<c:if test="${memberLoanReport.yunyingshangStatus =='已过期'||memberLoanReport.yunyingshangStatus =='已认证'}">
			<tr>
			    <td style="font-weight:bold;;background-color: #2FB6EF;color: white" colspan ="4">运营商信息(数据源于运营商,统计最近六个月的数据)</td>
			</tr>
			<tr>
				<td style="text-align:center;font-weight:bold" rowspan = "3">基本信息</td>
				<td style="text-align:center;font-weight:bold">运营商手机号</td>
				<td style="text-align:center" colspan ="2">${memberLoanReport.yunyingshangUsername}</td>
			</tr>
			<tr>
				<td style="text-align:center;font-weight:bold">运营商</td>
				<td style="text-align:center" colspan ="2">${memberLoanReport.yunyingshang}</td>
			</tr>
			<tr>
				<td style="text-align:center;font-weight:bold">使用时长</td>
				<td style="text-align:center" colspan ="2">${memberLoanReport.userTime}</td>
			</tr>
			<tr>
				<td style="text-align:center;font-weight:bold" rowspan = "5">通话情况</td>
				<td style="text-align:center;font-weight:bold">日均通话时长</td>
				<td style="text-align:center" colspan ="2">${memberLoanReport.oneDayCallTime}</td>
			</tr>
			<tr>
				<td style="text-align:center;font-weight:bold">夜间通话次数</td>
				<td style="text-align:center" colspan ="2">${memberLoanReport.nightCallCount}</td>
			</tr>
			<tr>
				<td style="text-align:center;font-weight:bold">半年内通话次数</td>
				<td style="text-align:center" colspan ="2">${memberLoanReport.callCount}</td>
			</tr>
			<tr>
				<td style="text-align:center;font-weight:bold">连续无通话静默大于3天（次）</td>
				<td style="text-align:center" colspan ="2">${memberLoanReport.silenceCount}</td>
			</tr>
			<tr>
				<td style="text-align:center;font-weight:bold">通话记录</td>
				<td style="text-align:center;" colspan ="2"><a href = "${ufang}/friend/callRecordDetails?param=${memberLoanReport.id}" style="text-align:center;">点击查看详情</a></td>
			</tr>
			<tr>
				<td style="text-align:center;font-weight:bold" rowspan = "3">消费情况</td>
				<td style="text-align:center;font-weight:bold">基础套餐金额</td>
				<td style="text-align:center" colspan ="2">${memberLoanReport.taocanMoney}</td>
			</tr>
			<tr>
				<td style="text-align:center;font-weight:bold">月均消费</td>
				<td style="text-align:center" colspan ="2">${memberLoanReport.oneMonthUseMoney}</td>
			</tr>
			<tr>
				<td style="text-align:center;font-weight:bold">话费记录</td>
				<td style="text-align:center;" colspan ="2"><a href = "${ufang}/friend/telephoneChargesDetails?param=${memberLoanReport.id}" style="text-align:center;">点击查看详情</a></td>
			</tr>
			</c:if>
			<c:if test="${memberLoanReport.xuexingwangStatus =='已过期'||memberLoanReport.xuexingwangStatus =='已认证'}">
			<tr>
			    <td style="font-weight:bold;;background-color: #2FB6EF;color: white" colspan ="4">学信网信息(数据源于学信网)</td>
			</tr>
			<tr>
				<td style="text-align:center;font-weight:bold">毕业院校</td>
				<td style="text-align:center">${memberLoanReport.colleges}</td>
				<td style="text-align:center;font-weight:bold">专业</td>
				<td style="text-align:center">${memberLoanReport.major}</td>
			</tr>
			<tr>
				<td style="text-align:center;font-weight:bold">入学时间</td>
				<td style="text-align:center">${memberLoanReport.entranceTime}</td>
				<td style="text-align:center;font-weight:bold">学历</td>
				<td style="text-align:center">${memberLoanReport.education}</td>
			</tr>
			<tr>
				<td style="text-align:center;font-weight:bold">毕业时间</td>
				<td style="text-align:center">${memberLoanReport.graduationTime}</td>
				<td style="text-align:center"></td>
				<td style="text-align:center"></td>
			</tr>
			</c:if>
			<c:if test="${memberLoanReport.shebaoStatus =='已过期'||memberLoanReport.shebaoStatus =='已认证'}">
			<tr>
			    <td style="font-weight:bold;;background-color: #2FB6EF;color: white" colspan ="4">社保信息(数据源于社保网)</td>
			</tr>
			<tr>
				<td style="text-align:center;font-weight:bold">单位名称</td>
				<td style="text-align:center">${memberLoanReport.shebaoCompanyName}</td>
				<td style="text-align:center;font-weight:bold">参加工作时间</td>
				<td style="text-align:center">${memberLoanReport.shebaoWorkTime}</td>
			</tr>
			<tr>
				<td style="text-align:center;font-weight:bold">家庭地址</td>
				<td style="text-align:center">${memberLoanReport.shebaoFamilyAddr}</td>
				<td style="text-align:center;font-weight:bold">起缴日</td>
				<td style="text-align:center">${memberLoanReport.shebaoPaymentTime}</td>
			</tr>
			<tr>
				<td style="text-align:center;font-weight:bold">社保详情</td>
				<td style="text-align:center;"><a href = "${ufang}/friend/socialSecurityDetails?param=${memberLoanReport.id}" style="text-align:center;">点击查看详情</a></td>
				<td style="text-align:center"></td>
				<td style="text-align:center"></td>
			</tr>
			</c:if>
			<c:if test="${memberLoanReport.gongjijingStatus =='已过期'||memberLoanReport.gongjijingStatus =='已认证'}">
			<tr>
			    <td style="font-weight:bold;;background-color: #2FB6EF;color: white" colspan ="4">公积金信息(数据源于公积金)</td>
			</tr>
			<tr>
				<td style="text-align:center;font-weight:bold">单位名称</td>
				<td style="text-align:center">${memberLoanReport.gongjijinCompanyName}</td>
				<td style="text-align:center;font-weight:bold">开户日期</td>
				<td style="text-align:center">${memberLoanReport.gongjijinOpenAccountTime}</td>
			</tr>
			<tr>
				<td style="text-align:center;font-weight:bold">缴费状态</td>
				<td style="text-align:center">${memberLoanReport.gongjijinPaymentStatus}</td>
				<td style="text-align:center;font-weight:bold">最后缴费日期</td>
				<td style="text-align:center">${memberLoanReport.gongjijinLastPaymentTime}</td>
			</tr>
			<tr>
				<td style="text-align:center;font-weight:bold">家庭住址</td>
				<td style="text-align:center">${memberLoanReport.gongjijinAddr}</td>
				<td style="text-align:center;font-weight:bold">公积金详情</td>
				<td style="text-align:center;"><a href = "${ufang}/friend/accumulationDetails?param=${memberLoanReport.id}" style="text-align:center;">点击查看详情</a></td>
			</tr>
			</c:if>
			<tr>
			    <td style="font-weight:bold;;background-color: #2FB6EF;color: white" colspan ="4">紧急联系人信息(数据源于交叉验证)</td>
			</tr>
			<tr>
				<td style="text-align:center;font-weight:bold">姓名</td>
				<td style="text-align:center;font-weight:bold">手机号</td>
				<td style="text-align:center;font-weight:bold">半年内通话次数</td>
				<td style="text-align:center;font-weight:bold">通话时长</td>
			</tr>
			<c:forEach items="${memberLoanReport.extendMemberList }" var="extendList">
				<tr>
					<td style="text-align:center">${extendList.name}</td>
					<td style="text-align:center">${extendList.username}</td>
					<td style="text-align:center">${extendList.callCount}</td>
					<td style="text-align:center">${extendList.callTime}</td>
				</tr>
			</c:forEach>
			
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>