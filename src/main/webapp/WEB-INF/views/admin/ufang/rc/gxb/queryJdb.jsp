<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%@ page import="com.alibaba.fastjson.JSON" %>
<%@ page import="java.util.Map" %>
<html>
<head>
	<title>借贷宝报告</title>
	<meta name="decorator" content="default"/>
	<script src="${ctxStatic}/jquery/jquery.lazyload.js" type="text/javascript"></script>
	<script src="${ctxStatic}/artdialog/js/dialog-plus.js"></script>
    <link href="${ctxStatic}/artdialog/css/dialog.css" rel="stylesheet" />
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
		<h2>借贷宝用户分析</h2><br>
		<P>基本信息</P><br>
		<table class="table table-striped table-bordered table-condensed">
		<tbody>
				<tr>
					<td style="text-align:center;width: 25%;">
						姓名：
					</td>
					<td style="text-align:center;width: 25%;">
						${result.data.baseInfo.name}
					</td>
					<td style="text-align:center;width: 25%;">
						性别：
					</td>
					<td style="text-align:center;width: 25%;">
						${result.data.baseInfo.gender==0?'男':'女'}
					</td>
				</tr>
				<tr>
					<td style="text-align:center;width: 25%;">
						手机号码：
					</td>
					<td style="text-align:center;width: 25%;">
						${result.data.baseInfo.phone}
					</td>
					<td style="text-align:center;width: 25%;">
						是否本人账号：
					</td>
					<td style="text-align:center;width: 25%;">
						<c:choose>
							<c:when test="${result.data.baseInfo.status==1}">
								<span style="color:red">是</span>
							</c:when>
							<c:otherwise>
								否
							</c:otherwise>
						</c:choose>
					</td>
				</tr>
				<tr>
					 <td style="text-align:center;width: 25%;">
						逾期提示：
					</td>
					<td style="text-align:center;width: 25%;">
						${result.data.baseInfo.overdueTips}
					</td>
					<td style="text-align:center;width: 25%;">
						账号等级：
					</td>
					<td style="text-align:center;width: 25%;">
						${result.data.baseInfo.level}
					</td>
				</tr>
				<tr>
					 <td style="text-align:center;width: 25%;">
						是否通过肖像认证：
					</td>
					<td style="text-align:center;width: 25%;">
						${result.data.baseInfo.faceStatus}
					</td>
					<td style="text-align:center;width: 25%;">
						是否实名认证：
					</td>
					<td style="text-align:center;width: 25%;">
						${result.data.baseInfo.realNameStatus}
					</td>
				</tr>
				<tr>
					 <td style="text-align:center;width: 25%;">
						注册时间：
					</td>
					<td style="text-align:center;width: 25%;">
						${result.data.baseInfo.registerTime}
					</td>
					<td style="text-align:center;width: 50%;" colspan="2">
					</td>
				</tr>
		</tbody>
	</table>
<P>用户认证信息</P><br>
	<table class="table table-striped table-bordered table-condensed">
		<tbody>
			<tr>
				<td colspan="2" style="text-align:center;width: 50%;">
					认证项数：
				</td>
				<td colspan="2" style="text-align:center;">
					${result.data.baseInfo.identNum}
				</td>
			</tr>
				<c:set var="i" value="0"/>
				<c:forEach items="${result.data.authList}" var="item" >
					<c:set var="i" value="${(i+1)%2}"/> 
						<c:if test="${i eq 1}">
							<tr>
						</c:if>
							<td style="text-align:center;width: 25%;">
								${item.authName}
							</td>
							<td style="text-align:center;width: 25%;">
								<c:choose>
									<c:when test="${item.authStatus==0}">
										<span style="color:red">是</span>
									</c:when>
									<c:otherwise>
										否
									</c:otherwise>
								</c:choose>
							</td>
						<c:if test="${i eq 0}">
							</tr>
						</c:if>
				</c:forEach>
		</tbody>
	</table>
<P>用户借贷信息</P><br>
	<table class="table table-striped table-bordered table-condensed">
		<tbody>
			<tr>
				<td style="text-align:center;width: 25%;">
					当前借出金额：
				</td>
				<td style="text-align:center;width: 25%;">
					${result.data.jdbCreditInfo.currentLendAmount}
				</td>
				<td colspan="2" rowspan="2" style="text-align:center;">
				</td>
			</tr>
			<tr>
				<td style="text-align:center;width: 25%;">
					当前借入金额：
				</td>
				<td style="text-align:center;">
					${result.data.jdbCreditInfo.currentBorrowAmount}
				</td>
			</tr>
			<tr>
				<td style="text-align:center;width: 25%;">
					历史借出次数：
				</td>
				<td style="text-align:center;width: 25%;">
					${result.data.jdbCreditInfo.historicalLendCount}
				</td>
				<td style="text-align:center;width: 25%;">
					历史借出金额：
				</td>
				<td style="text-align:center;width: 25%;">
					${result.data.jdbCreditInfo.historicalLendAmount}
				</td>
			</tr>
			<tr>
				<td style="text-align:center;width: 25%;">
					历史借入次数：
				</td>
				<td style="text-align:center;width: 25%;">
					${result.data.jdbCreditInfo.historicalBorrowCount}
				</td>
				<td style="text-align:center;width: 25%;">
					历史借入金额：
				</td>
				<td style="text-align:center;width: 25%;">
					${result.data.jdbCreditInfo.historicalBorrowAmount}
				</td>
			</tr>
		</tbody>
	</table>	
<c:if test="${result.data.iouList !=NULL && result.data.iouList!=''}">
	<P>用户借条信息</P><br>
		<table class="table table-striped table-bordered table-condensed">
			<tbody>
				<tr>
					<td style="text-align:center;">
						借条类型
					</td>
					<td style="text-align:center;">
						还款方式
					</td>
					<td style="text-align:center;">
						借条状态
					</td>
					<td style="text-align:center;">
						借款金额
					</td>
					<td style="text-align:center;">
						待还金额
					</td>
					<td style="text-align:center;">
						逾期天数
					</td>
					<td style="text-align:center;">
						借款目的
					</td>
					<td style="text-align:center;">
						还款时间
					</td>
					<td style="text-align:center;">
						借款时间
					</td>
				</tr>
					<c:forEach items="${result.data.iouList}" var="item" >
						<tr>
							<td style="text-align:center;">
								${item.iouType==0?"借入":"出借"}
							</td>
							<td style="text-align:center;">
								${item.repayType==0?"全额":"分期"}
							</td>
							<td style="text-align:center;">
								${item.iouStatus==0?"未知":item.iouStatus==1?"待出借":item.iouStatus==2?"未到期":item.iouStatus==3?"已归还":item.iouStatus==4?"到期未还":item.iouStatus==5?"已拒绝":item.iouStatus==6?"已冻结":item.iouStatus==7?"还款中":item.iouStatus==8?"有争议":"其他(失效，违规或失败等其他原因)"}
							</td>
							<td style="text-align:center;">
								${item.baseAmt}
							</td>
							<td style="text-align:center;">
								${item.toRepaidAmt}
							</td>
							<td style="text-align:center;">
								${item.overDueDays<0?"未逾期":item.overDueDays}
							</td>
							<td style="text-align:center;">
								${item.purpose}
							</td>
							<td style="text-align:center;">
								${item.repayTime}
							</td>
							<td style="text-align:center;">
								${item.borrowerTime}
							</td>
						</tr>
					</c:forEach>
			</tbody>
		</table>
</c:if>		
	
<c:if test="${result.data.contactList !=NULL && result.data.contactList!=''}">
	<P>用户联系人信息</P><br>
		<c:set var="count" value="0"/><!--人数 -->
		<c:set var="fans" value="0"/><!--粉丝 -->
		<c:set var="attention" value="0"/><!--关注 -->
		<c:set var="attentionEachOther" value="0"/><!--互相关注 -->
		<c:forEach items="${result.data.contactList}" var="item" >
			<c:set var="count" value="${count+1}"/> 
			<c:choose>
			    <c:when test="${item.contactType==1}">
			        <c:set var="fans" value="${attention+1}"/> 
			    </c:when>
			    <c:when test="${item.contactType==2}">
			        <c:set var="fans" value="${fans+1}"/> 
			    </c:when>
			    <c:when test="${item.contactType==3}">
			        <c:set var="attentionEachOther" value="${attentionEachOther+1}"/> 
			    </c:when>
			    <c:otherwise>
			        
			    </c:otherwise>
			</c:choose>
		</c:forEach>
		<table class="table table-striped table-bordered table-condensed">
			<tbody>
				<tr>
					<td style="text-align:center;width: 25%;">
						联系人个数
					</td>
					<td style="text-align:center;width: 25%;">
						 ${count}
					</td>
					<td style="text-align:center;width: 25%;">
						粉丝人数
					</td>
					<td style="text-align:center;width: 25%;">
						 ${fans}
					</td>
				</tr>
				<tr>
					<td style="text-align:center;width: 25%;">
						关注人数
					</td>
					<td style="text-align:center;width: 25%;">
						  ${attention}
					</td>
					<td style="text-align:center;width: 25%;">
						互相关注人数
					</td>
					<td style="text-align:center;width: 25%;">
						 ${attentionEachOther}
					</td>
				</tr>
					 
			</tbody>
		</table>
</c:if>			
</body>
</html>