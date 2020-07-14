<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%@ page import="com.alibaba.fastjson.JSON" %>
<%@ page import="java.util.Map" %>
<html>
<head>
	<title>闪谍报告</title>
	<meta name="decorator" content="default"/>
	<script src="${ctxStatic}/jquery/jquery.lazyload.js" type="text/javascript"></script>
	<script src="${ctxStatic}/artdialog/js/dialog-plus.js"></script>
    <link href="${ctxStatic}/artdialog/css/dialog.css" rel="stylesheet" />
</head>
<body>
		<h2>闪谍报告</h2><br>
		<h5>注：-1代表 无</h5><br/>
		<h4>基本信息</h4><br>
	<table class="table table-striped table-bordered table-condensed">
		<tbody>
				<tr>
					<td style="text-align:center;width: 50%;">
						姓名
					</td>
					<td style="text-align:center">
						${name}
					</td>
				</tr>
				<tr>
					<td style="text-align:center">
						身份证号
					</td>
					<td style="text-align:center">
						${idNo}
					</td>
				</tr>
				<tr>
					<td style="text-align:center">
						手机号
					</td>
					<td style="text-align:center">
						${phoneNo}
					</td>
				</tr>
		</tbody>
	</table>

	<h4>借贷信息</h4> <br/>
	<table class="table table-striped table-bordered table-condensed">
			<tr>
				<th style="text-align:center;width: 25%;" colspan="4">贷后数据(根据身份证查询)</th>
			</tr>
		<tbody>
				<tr>
					<td style="text-align:center;width: 50%;" colspan="2">
						90天
					</td>
					<td style="text-align:center;width: 50%;" colspan="2">
						360天
					</td>
				</tr>
				<tr>
					<td style="text-align:center;width: 25%;">
						报告查询次数
					</td>
					<td style="text-align:center;width: 25%;">
						${content.loanInfo.pid.all.timeScopes.D90.queryCount}
					</td>
					<td style="text-align:center;width: 25%;">
						报告查询次数
					</td>
					<td style="text-align:center;width: 25%;">
						${content.loanInfo.pid.all.timeScopes.D360.queryCount}
					</td>
				</tr>
				<tr>
					<td style="text-align:center;width: 25%;">
						贷款笔数
					</td>
					<td style="text-align:center;width: 25%;">
						${content.loanInfo.pid.all.timeScopes.D90.loanCount}
					</td>
					
					<td style="text-align:center;width: 25%;">
						贷款笔数
					</td>
					<td style="text-align:center;width: 25%;">
						${content.loanInfo.pid.all.timeScopes.D360.loanCount}
					</td>
				</tr>
				<tr>
					<td style="text-align:center;width: 25%;">
						贷款机构数
					</td>
					<td style="text-align:center;width: 25%;">
						${content.loanInfo.pid.all.timeScopes.D90.loanTenantCount}
					</td>
					
					<td style="text-align:center;width: 25%;">
						贷款机构数
					</td>
					<td style="text-align:center;width: 25%;">
						${content.loanInfo.pid.all.timeScopes.D360.loanTenantCount}
					</td>
				</tr>
				
				<tr>
					<td style="text-align:center;width: 25%;">
						平均申请间隔时长
					</td>
					<td style="text-align:center;width: 25%;">
						${content.loanInfo.pid.all.timeScopes.D90.averageLoanGapDays}
					</td>
					
					<td style="text-align:center;width: 25%;">
						平均申请间隔时长
					</td>
					<td style="text-align:center;width: 25%;">
						${content.loanInfo.pid.all.timeScopes.D360.averageLoanGapDays}
					</td>
				</tr>
				
				<tr>
					<td style="text-align:center;width: 25%;">
						平均换新机构间隔时长
					</td>
					<td style="text-align:center;width: 25%;">
						${content.loanInfo.pid.all.timeScopes.D90.averageTenantGapDays}
					</td>
					
					<td style="text-align:center;width: 25%;">
						平均换新机构间隔时长
					</td>
					<td style="text-align:center;width: 25%;">
						${content.loanInfo.pid.all.timeScopes.D360.averageTenantGapDays}
					</td>
				</tr>
				
				<tr>
					<td style="text-align:center;width: 25%;">
						最大贷款金额
					</td>
					<td style="text-align:center;width: 25%;">
						${content.loanInfo.pid.all.timeScopes.D90.maxLoanAmount}
					</td>
					
					<td style="text-align:center;width: 25%;">
						最大贷款金额
					</td>
					<td style="text-align:center;width: 25%;">
						${content.loanInfo.pid.all.timeScopes.D360.maxLoanAmount}
					</td>
				</tr>
				
				<tr>
					<td style="text-align:center;width: 25%;">
						最大贷款天数
					</td>
					<td style="text-align:center;width: 25%;">
						${content.loanInfo.pid.all.timeScopes.D90.maxLoanPeriodDays}
					</td>
					
					<td style="text-align:center;width: 25%;">
						最大贷款天数
					</td>
					<td style="text-align:center;width: 25%;">
						${content.loanInfo.pid.all.timeScopes.D360.maxLoanPeriodDays}
					</td>
				</tr>
				
				<tr>
					<td style="text-align:center;width: 25%;">
						平均贷款额度
					</td>
					<td style="text-align:center;width: 25%;">
						${content.loanInfo.pid.all.timeScopes.D90.averageLoanAmount}
					</td>
					
					<td style="text-align:center;width: 25%;">
						平均贷款额度
					</td>
					<td style="text-align:center;width: 25%;">
						${content.loanInfo.pid.all.timeScopes.D360.averageLoanAmount}
					</td>
				</tr>
				
				<tr>
					<td style="text-align:center;width: 25%;">
						平均贷款额度
					</td>
					<td style="text-align:center;width: 25%;">
						${content.loanInfo.pid.all.timeScopes.D90.averageLoanAmount}
					</td>
					
					<td style="text-align:center;width: 25%;">
						平均贷款额度
					</td>
					<td style="text-align:center;width: 25%;">
						${content.loanInfo.pid.all.timeScopes.D360.averageLoanAmount}
					</td>
				</tr>
				
				<tr>
					<td style="text-align:center;width: 25%;">
						最大逾期天数
					</td>
					<td style="text-align:center;width: 25%;">
						${content.loanInfo.pid.all.timeScopes.D90.maxOverdueDays}
					</td>
					
					<td style="text-align:center;width: 25%;">
						最大逾期天数
					</td>
					<td style="text-align:center;width: 25%;">
						${content.loanInfo.pid.all.timeScopes.D360.maxOverdueDays}
					</td>
				</tr>
				
				<tr>
					<td style="text-align:center;width: 25%;">
						逾期的贷款笔数
					</td>
					<td style="text-align:center;width: 25%;">
						${content.loanInfo.pid.all.timeScopes.D90.overdueLoanCount}
					</td>
					
					<td style="text-align:center;width: 25%;">
						逾期的贷款笔数
					</td>
					<td style="text-align:center;width: 25%;">
						${content.loanInfo.pid.all.timeScopes.D360.overdueLoanCount}
					</td>
				</tr>
				
				<tr>
					<td style="text-align:center;width: 25%;">
						逾期的贷款机构数
					</td>
					<td style="text-align:center;width: 25%;">
						${content.loanInfo.pid.all.timeScopes.D90.overdueTenantCount}
					</td>
					
					<td style="text-align:center;width: 25%;">
						逾期的贷款机构数
					</td>
					<td style="text-align:center;width: 25%;">
						${content.loanInfo.pid.all.timeScopes.D360.overdueTenantCount}
					</td>
				</tr>
				
				<tr>
					<td style="text-align:center;width: 25%;">
						逾期两周期以上的机构数
					</td>
					<td style="text-align:center;width: 25%;">
						${content.loanInfo.pid.all.timeScopes.D90.overdueFor2TermTenantCount}
					</td>
					
					<td style="text-align:center;width: 25%;">
						逾期两周期以上的机构数
					</td>
					<td style="text-align:center;width: 25%;">
						${content.loanInfo.pid.all.timeScopes.D360.overdueFor2TermTenantCount}
					</td>
				</tr>
				
				<tr>
					<td style="text-align:center;width: 25%;">
					</td>
					<td style="text-align:center;width: 25%;">
					</td>
					
					<td style="text-align:center;width: 25%;">
						最后一次贷款距今天数
					</td>
					<td style="text-align:center;width: 25%;">
						${content.loanInfo.pid.all.timeScopes.D360.daysFromLastLoan}
					</td>
				</tr>
				
				<tr>
					<td style="text-align:center;width: 25%;">
					</td>
					<td style="text-align:center;width: 25%;">
					</td>
					
					<td style="text-align:center;width: 25%;">
						最早贷款距今月数
					</td>
					<td style="text-align:center;width: 25%;">
						${content.loanInfo.pid.all.timeScopes.D360.monthsFromFirstLoan}
					</td>
				</tr>
				
				<tr>
					<td style="text-align:center;width: 25%;">
					</td>
					<td style="text-align:center;width: 25%;">
					</td>
					
					<td style="text-align:center;width: 25%;">
						最后一次逾期距今月数
					</td>
					<td style="text-align:center;width: 25%;">
						${content.loanInfo.pid.all.timeScopes.D360.monthsFromLastOverdue}
					</td>
				</tr>
				
				<tr>
					<td style="text-align:center;width: 25%;">
					</td>
					<td style="text-align:center;width: 25%;">
					</td>
					
					<td style="text-align:center;width: 25%;">
						连续正常还款月数
					</td>
					<td style="text-align:center;width: 25%;">
						${content.loanInfo.pid.all.timeScopes.D360.monthsForNormalRepay}
					</td>
				</tr>
				
				<tr>
					<td style="text-align:center;width: 25%;">
					</td>
					<td style="text-align:center;width: 25%;">
					</td>
					
					<td style="text-align:center;width: 25%;">
						未还款总金额
					</td>
					<td style="text-align:center;width: 25%;">
						${content.loanInfo.pid.all.timeScopes.D360.remainingAmount}
					</td>
				</tr>
		</tbody>
	</table>
	
	<table class="table table-striped table-bordered table-condensed">
			<tr>
				<th style="text-align:center;width: 25%;" colspan="4">贷后数据(根据手机号查询)</th>
			</tr>
		<tbody>
				<tr>
					<td style="text-align:center;width: 25%;" colspan="2">
						90天
					</td>
					<td style="text-align:center;width: 25%;" colspan="2">
						360天
					</td>
				</tr>
				<tr>
					<td style="text-align:center;width: 25%;">
						报告查询次数
					</td>
					<td style="text-align:center;width: 25%;">
						${content.loanInfo.mobile.all.timeScopes.D90.queryCount}
					</td>
					<td style="text-align:center;width: 25%;">
						报告查询次数
					</td>
					<td style="text-align:center;width: 25%;">
						${content.loanInfo.mobile.all.timeScopes.D360.queryCount}
					</td>
				</tr>
				<tr>
					<td style="text-align:center;width: 25%;">
						贷款笔数
					</td>
					<td style="text-align:center;width: 25%;">
						${content.loanInfo.mobile.all.timeScopes.D90.loanCount}
					</td>
					
					<td style="text-align:center;width: 25%;">
						贷款笔数
					</td>
					<td style="text-align:center;width: 25%;">
						${content.loanInfo.mobile.all.timeScopes.D360.loanCount}
					</td>
				</tr>
				<tr>
					<td style="text-align:center;width: 25%;">
						贷款机构数
					</td>
					<td style="text-align:center;width: 25%;">
						${content.loanInfo.mobile.all.timeScopes.D90.loanTenantCount}
					</td>
					
					<td style="text-align:center;width: 25%;">
						贷款机构数
					</td>
					<td style="text-align:center;width: 25%;">
						${content.loanInfo.mobile.all.timeScopes.D360.loanTenantCount}
					</td>
				</tr>
				
				<tr>
					<td style="text-align:center;width: 25%;">
						平均申请间隔时长
					</td>
					<td style="text-align:center;width: 25%;">
						${content.loanInfo.mobile.all.timeScopes.D90.averageLoanGapDays}
					</td>
					
					<td style="text-align:center;width: 25%;">
						平均申请间隔时长
					</td>
					<td style="text-align:center;width: 25%;">
						${content.loanInfo.mobile.all.timeScopes.D360.averageLoanGapDays}
					</td>
				</tr>
				
				<tr>
					<td style="text-align:center;width: 25%;">
						平均换新机构间隔时长
					</td>
					<td style="text-align:center;width: 25%;">
						${content.loanInfo.mobile.all.timeScopes.D90.averageTenantGapDays}
					</td>
					
					<td style="text-align:center;width: 25%;">
						平均换新机构间隔时长
					</td>
					<td style="text-align:center;width: 25%;">
						${content.loanInfo.mobile.all.timeScopes.D360.averageTenantGapDays}
					</td>
				</tr>
				
				<tr>
					<td style="text-align:center;width: 25%;">
						最大贷款金额
					</td>
					<td style="text-align:center;width: 25%;">
						${content.loanInfo.mobile.all.timeScopes.D90.maxLoanAmount}
					</td>
					
					<td style="text-align:center;width: 25%;">
						最大贷款金额
					</td>
					<td style="text-align:center;width: 25%;">
						${content.loanInfo.mobile.all.timeScopes.D360.maxLoanAmount}
					</td>
				</tr>
				
				<tr>
					<td style="text-align:center;width: 25%;">
						最大贷款天数
					</td>
					<td style="text-align:center;width: 25%;">
						${content.loanInfo.mobile.all.timeScopes.D90.maxLoanPeriodDays}
					</td>
					
					<td style="text-align:center;width: 25%;">
						最大贷款天数
					</td>
					<td style="text-align:center;width: 25%;">
						${content.loanInfo.mobile.all.timeScopes.D360.maxLoanPeriodDays}
					</td>
				</tr>
				
				<tr>
					<td style="text-align:center;width: 25%;">
						平均贷款额度
					</td>
					<td style="text-align:center;width: 25%;">
						${content.loanInfo.mobile.all.timeScopes.D90.averageLoanAmount}
					</td>
					
					<td style="text-align:center;width: 25%;">
						平均贷款额度
					</td>
					<td style="text-align:center;width: 25%;">
						${content.loanInfo.mobile.all.timeScopes.D360.averageLoanAmount}
					</td>
				</tr>
				
				<tr>
					<td style="text-align:center;width: 25%;">
						平均贷款额度
					</td>
					<td style="text-align:center;width: 25%;">
						${content.loanInfo.mobile.all.timeScopes.D90.averageLoanAmount}
					</td>
					
					<td style="text-align:center;width: 25%;">
						平均贷款额度
					</td>
					<td style="text-align:center;width: 25%;">
						${content.loanInfo.mobile.all.timeScopes.D360.averageLoanAmount}
					</td>
				</tr>
				
				<tr>
					<td style="text-align:center;width: 25%;">
						最大逾期天数
					</td>
					<td style="text-align:center;width: 25%;">
						${content.loanInfo.mobile.all.timeScopes.D90.maxOverdueDays}
					</td>
					
					<td style="text-align:center;width: 25%;">
						最大逾期天数
					</td>
					<td style="text-align:center;width: 25%;">
						${content.loanInfo.mobile.all.timeScopes.D360.maxOverdueDays}
					</td>
				</tr>
				
				<tr>
					<td style="text-align:center;width: 25%;">
						逾期的贷款笔数
					</td>
					<td style="text-align:center;width: 25%;">
						${content.loanInfo.mobile.all.timeScopes.D90.overdueLoanCount}
					</td>
					
					<td style="text-align:center;width: 25%;">
						逾期的贷款笔数
					</td>
					<td style="text-align:center;width: 25%;">
						${content.loanInfo.mobile.all.timeScopes.D360.overdueLoanCount}
					</td>
				</tr>
				
				<tr>
					<td style="text-align:center;width: 25%;">
						逾期的贷款机构数
					</td>
					<td style="text-align:center;width: 25%;">
						${content.loanInfo.mobile.all.timeScopes.D90.overdueTenantCount}
					</td>
					
					<td style="text-align:center;width: 25%;">
						逾期的贷款机构数
					</td>
					<td style="text-align:center;width: 25%;">
						${content.loanInfo.mobile.all.timeScopes.D360.overdueTenantCount}
					</td>
				</tr>
				
				<tr>
					<td style="text-align:center;width: 25%;">
						逾期两周期以上的机构数
					</td>
					<td style="text-align:center;width: 25%;">
						${content.loanInfo.mobile.all.timeScopes.D90.overdueFor2TermTenantCount}
					</td>
					
					<td style="text-align:center;width: 25%;">
						逾期两周期以上的机构数
					</td>
					<td style="text-align:center;width: 25%;">
						${content.loanInfo.mobile.all.timeScopes.D360.overdueFor2TermTenantCount}
					</td>
				</tr>
				
				<tr>
					<td style="text-align:center;width: 25%;">
					</td>
					<td style="text-align:center;width: 25%;">
					</td>
					
					<td style="text-align:center;width: 25%;">
						最后一次贷款距今天数
					</td>
					<td style="text-align:center;width: 25%;">
						${content.loanInfo.mobile.all.timeScopes.D360.daysFromLastLoan}
					</td>
				</tr>
				
				<tr>
					<td style="text-align:center;width: 25%;">
					</td>
					<td style="text-align:center;width: 25%;">
					</td>
					
					<td style="text-align:center;width: 25%;">
						最早贷款距今月数
					</td>
					<td style="text-align:center;width: 25%;">
						${content.loanInfo.mobile.all.timeScopes.D360.monthsFromFirstLoan}
					</td>
				</tr>
				
				<tr>
					<td style="text-align:center;width: 25%;">
					</td>
					<td style="text-align:center;width: 25%;">
					</td>
					
					<td style="text-align:center;width: 25%;">
						最后一次逾期距今月数
					</td>
					<td style="text-align:center;width: 25%;">
						${content.loanInfo.mobile.all.timeScopes.D360.monthsFromLastOverdue}
					</td>
				</tr>
				
				<tr>
					<td style="text-align:center;width: 25%;">
					</td>
					<td style="text-align:center;width: 25%;">
					</td>
					
					<td style="text-align:center;width: 25%;">
						连续正常还款月数
					</td>
					<td style="text-align:center;width: 25%;">
						${content.loanInfo.mobile.all.timeScopes.D360.monthsForNormalRepay}
					</td>
				</tr>
				
				<tr>
					<td style="text-align:center;width: 25%;">
					</td>
					<td style="text-align:center;width: 25%;">
					</td>
					
					<td style="text-align:center;width: 25%;">
						未还款总金额
					</td>
					<td style="text-align:center;width: 25%;">
						${content.loanInfo.mobile.all.timeScopes.D360.remainingAmount}
					</td>
				</tr>
		</tbody>
	</table>
	
	<table class="table table-striped table-bordered table-condensed">
			<tr>
				<th style="text-align:center;width: 33%;"  colspan="3">黑名单命中情况</th>
			</tr>
		<tbody>
				<tr>
					<td style="text-align:center;width: 33%;">
						黑名单类型
					</td>
					<td style="text-align:center;width: 33%;">
						身份证维度
					</td>
					<td style="text-align:center;">
						手机号维度
					</td>
				</tr>
				<tr>
					<td style="text-align:center;width: 33%;">
						最近6个月确认为黑名单的机构数
					</td>
					<td style="text-align:center;width: 33%;">
						${content.blacklist.pid.all.last6MTenantCount}
					</td>
					<td style="text-align:center;">
						${content.blacklist.mobile.all.last6MTenantCount}
					</td>
				</tr>
				<tr>
					<td style="text-align:center;width: 33%;">
						最近6个月身份证申请查询数
					</td>
					<td style="text-align:center;width: 33%;">
						${content.blacklist.pid.all.last6MQueryCount}
					</td>
					<td style="text-align:center;">
						${content.blacklist.mobile.all.last6MQueryCount}
					</td>
				</tr>
				<tr>
					<td style="text-align:center;width: 33%;">
						最新入库距离天数
					</td>
					<td style="text-align:center;width: 33%;">
						${content.blacklist.pid.all.lastConfirmAtDays}
					</td>
					<td style="text-align:center;">
						${content.blacklist.mobile.all.lastConfirmAtDays}
					</td>
				</tr>
				<tr>
					<td style="text-align:center;width: 33%;">
						最新欺诈/逾期状态
					</td>
					<td style="text-align:center;width: 33%;">
						${content.blacklist.pid.all.lastConfirmStatus}
					</td>
					<td style="text-align:center;">
						${content.blacklist.mobile.all.lastConfirmStatus}
					</td>
				</tr>
				
				<tr>
					<td style="text-align:center;width: 33%;">
							最近12个月身份证最严重的欺诈/逾期状态
					</td>
					<td style="text-align:center;width: 33%;">
						${content.blacklist.pid.all.last12MMaxConfirmStatus}
					</td>
					<td style="text-align:center;">
						${content.blacklist.mobile.all.last12MMaxConfirmStatus}
					</td>
				</tr>
		</tbody>
	</table>
</body>
</html>