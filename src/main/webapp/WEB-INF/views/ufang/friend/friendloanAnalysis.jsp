<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>用户借贷分析</title>
	<meta name="decorator" content="default"/>
    <script src="${ctxStatic}/artdialog/js/dialog-plus.js"></script>
    <link href="${ctxStatic}/artdialog/css/dialog.css" rel="stylesheet" />
    <style type="text/css">
    	.dialog{    width: 700px;
    height: 200px;
    border: solid 1px #b5b0b0;
    border-radius: 5px;
    position: absolute;
    left: 30%;
    bottom: 30%;
    background: #eee;
    line-height: 30px;
    text-align: center;
    display: none;
    font-size:16px;
}
    
    </style>
	<script type="text/javascript">
		$(document).ready(function() {
			
		});
		function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#searchForm").submit();
        	return false;
        }
		
		function score(){
			$("#score").show(200)
		}
		function risk(){
			$("#risk").show(200)
		}
		function features(){
			$("#features").show(200)
		}
		function detail(){
			$("#detail").show(200)
		}
		function pic(){
			$("#pic").show(200)
		}
		function scoreClose(){
			$("#score").hide(200)
		}
		function riskClose(){
			$("#risk").hide(200)
		}
		function featuresClose(){
			$("#features").hide(200)
		}
		function detailClose(){
			$("#detail").hide(200)
		}
		function picClose(){
			$("#pic").hide(200)
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
		<li class="active"><a href="${ufang}/friend/loanAnalysis?friend.id=${memberFriendRelation.friend.id}">风险画像</a></li>
		<li><a href="${ufang}/friend/loanReport?id=${memberFriendRelation.friend.id}">信用档案</a></li>
	</ul>
	<sys:message content="${message}"/>
	
		<thead>
			<div class="dialog" id="score" hidden="true">
		        <h3>风险评估模型得分：<img onclick="scoreClose();" src="${ctxStatic}/images/close.png"></h3><br>
		        <h5>风险评估分值从1-99分<span>，分值越高</span>，逾期概率越大<span>，0分表示为评分</span>，不在此风险评估统计值中</h5>        
	        </div>
	        <div class="dialog" id="risk" hidden="true">
		        <h3>风险等级：<img onclick="riskClose();" src="${ctxStatic}/images/close.png"></h3><br>
		        <h5>极高风险【80，100)，中高风险【55，80)，中低风险【30，55)，较低风险(0，30)未评分用户为缺少足够信息无法评分</h5>        
	        </div>
	        <div class="dialog" id="features" hidden="true">
		        <h3>用户特征：<img onclick="featuresClose();" src="${ctxStatic}/images/close.png"></h3><br>
		        <h5>数据系统根据用户的互联网交易行为、黑名单匹配情况、操作行为等多维度来识别用户的特征，特征下的日期为用户最近一次满足该特征的日期。同时特征颜色反应了用户在互联网借贷行业失信风险的高低程度，红色最高、黄色次之、蓝色最低
		        </h5>        
	        </div>
	        <div class="dialog" id="detail" hidden="true">
		        <h3>行业借款清单：<img onclick="detailClose();" src="${ctxStatic}/images/close.png"></h3><br>
		        <h5>数据系统分析的行业涉及非银行信贷、大学生分期、电商分期、旅游分期、医美分期、教育、汽车、租房、农业消金等十余个细分类目，表格仅展示该用户有匹配到数据的行业清单
		        </h5>        
	        </div>
	        <div class="dialog" id="pic" hidden="true">
		        <h3>关联图谱：<img onclick="picClose();" src="${ctxStatic}/images/close.png"></h3><br>
		        <h5>数据系统根据用户、设备、银行卡、手机号四个因素在互联网消费金融行业中进行可视化的关联关系展示，通过命中黑名单的因子找到其关联的其他因子，从而直观有效的发现各个因子之前存在的关系以及潜在风险。
		        </h5>        
	        </div>
		</thead>
		
		<tbody>
		<table id="contentTable" class="table table-striped table-bordered table-condensed">
			<tr>
			    <P>基本信息</P>
			</tr>
			<tr>
			    <td style="text-align:center;font-weight:bold">可信姓名</td>
			    <td style="text-align:center">
					${content.id_detail.names}
				</td>
				<td style="text-align:center;font-weight:bold" rowspan = "3">
					风险评估模型得分
				</td>
				<td style="text-align:center;color:red;font-weight:bold" rowspan = "3" colspan ="2">
					${content.score_detail.score}&nbsp;&nbsp;<img onclick="score();" alt="" src="${ctxStatic}/images/question.png">
				</td>
			</tr>
			<tr>
			    <td style="text-align:center;font-weight:bold">性别</td>
			    <td style="text-align:center">
					${content.id_detail.gender}
				</td>
			</tr>
			<tr>
			    <td style="text-align:center;font-weight:bold">出生日期</td>
			    <td style="text-align:center">
					${content.id_detail.birthday}
				</td>
			</tr>
			<tr>
			    <td style="text-align:center;font-weight:bold">身份证号码</td>
			    <td style="text-align:center">
					${content.id_detail.id_number_mask}
				</td>
				<td style="text-align:center;font-weight:bold" rowspan = "2">
					风险等级
				</td>
				<td style="text-align:center;color:red;font-weight:bold" rowspan = "2" colspan ="2">
					${content.score_detail.risk_evaluation}&nbsp;&nbsp;<img onclick="risk();" alt="" src="${ctxStatic}/images/question.png">
				</td>
			</tr>
			<tr>
			    <td style="text-align:center;font-weight:bold">身份证解析地</td>
			    <td style="text-align:center">
					${content.id_detail.city}
				</td>
			</tr>
			<tr>
			    <td style="font-weight:bold;;background-color: #2FB6EF;color: white" colspan ="5">用户特征&nbsp;&nbsp;<img onclick="features();" alt="" src="${ctxStatic}/images/question.png"></td>
			</tr>
			<tr>
				<c:forEach items="${content.user_features}" var="userFeatures">
						<td style="text-align:center">
							<c:choose>
								<c:when test="${userFeatures.user_feature_type == 0}">
									<div style="color:#e8b008">多头借贷</div>
									<br>${userFeatures.last_modified_date}
								</c:when>
								<c:when test="${userFeatures.user_feature_type == 2}">
									<div style="color:#e8b008">羊毛党</div>
									<br>${userFeatures.last_modified_date}
								</c:when>
								<c:when test="${userFeatures.user_feature_type == 5}">
									<div style="color:#e8b008">作弊软件</div>
									<br>${userFeatures.last_modified_date}
								</c:when>
								<c:when test="${userFeatures.user_feature_type == 6}">
									<div style="color:red">法院失信</div>
									<br>${userFeatures.last_modified_date}
								</c:when>
								<c:when test="${userFeatures.user_feature_type == 7}">
									<div style="color:red">网贷失信</div>
									<br>${userFeatures.last_modified_date}
								</c:when>
								<c:when test="${userFeatures.user_feature_type == 8}">
									<div style="color:#e8b008">关联过多</div>
									<br>${userFeatures.last_modified_date}
								</c:when>
								<c:when test="${userFeatures.user_feature_type == 10}">
									<div style="color:#e8b008">曾使用可疑设备</div>
									<br>${userFeatures.last_modified_date}
								</c:when>
								<c:when test="${userFeatures.user_feature_type == 11}">
									<div style="color:#e8b008">安装极多借贷app</div>
									<br>${userFeatures.last_modified_date}
								</c:when>
								<c:when test="${userFeatures.user_feature_type == 13}">
									<div style="color:red">身份信息疑似泄漏</div>
									<br>${userFeatures.last_modified_date}
								</c:when>
								<c:when test="${userFeatures.user_feature_type == 17}">
									<div style="color:red">活体攻击设备</div>
									<br>${userFeatures.last_modified_date}
								</c:when>
								<c:when test="${userFeatures.user_feature_type == 18}">
									<div style="color:red">活体攻击行为</div>
									<br>${userFeatures.last_modified_date}
								</c:when>
								<c:when test="${userFeatures.user_feature_type == 21}">
									<div style="color:red">疑似欺诈团伙</div>
									<br>${userFeatures.last_modified_date}
								</c:when>
								<c:when test="${userFeatures.user_feature_type == 23}">
									<div style="color:#e8b008">网贷不良</div>
									<br>${userFeatures.last_modified_date}
								</c:when>
								<c:when test="${userFeatures.user_feature_type == 24}">
									<div style="color:blue">短期逾期</div>
									<br>${userFeatures.last_modified_date}
								</c:when>
								<c:when test="${userFeatures.user_feature_type == null}">
									无
								</c:when>
								<c:otherwise>
									无
								</c:otherwise>
							</c:choose>
						</td>
				</c:forEach>
			</tr>
			<tr>
			    <td style="font-weight:bold;;background-color: #2FB6EF;color: white" colspan ="5">信贷行为</td>
			</tr>
			<tr>
				<td style="text-align:center;font-weight:bold">信贷行为</td>
				<td style="text-align:center;font-weight:bold">近一个月</td>
				<td style="text-align:center;font-weight:bold">近三个月</td>
				<td style="text-align:center;font-weight:bold">近六个月</td>
				<td style="text-align:center;font-weight:bold">总计</td>
			</tr>
			<tr>
				<td style="text-align:center;font-weight:bold">申请借款平台数</td>
				<td style="text-align:center">
					<c:if test="${content.loan_detail.loan_platform_count_1m == null}">
							0
					</c:if>
					<c:if test="${content.loan_detail.loan_platform_count_1m != null}">
						${content.loan_detail.loan_platform_count_1m}
					</c:if>
				</td>
				<td style="text-align:center">
					<c:if test="${content.loan_detail.loan_platform_count_3m == null}">
							0
					</c:if>
					<c:if test="${content.loan_detail.loan_platform_count_3m != null}">
						${content.loan_detail.loan_platform_count_3m}
					</c:if>
				</td>
				<td style="text-align:center">
					<c:if test="${content.loan_detail.loan_platform_count_6m == null}">
							0
					</c:if>
					<c:if test="${content.loan_detail.loan_platform_count_6m != null}">
						${content.loan_detail.loan_platform_count_6m}
					</c:if>
				</td>
				<td style="text-align:center">
					<c:if test="${content.loan_detail.loan_platform_count == null}">
							0
					</c:if>
					<c:if test="${content.loan_detail.loan_platform_count != null}">
						${content.loan_detail.loan_platform_count}
					</c:if>
				</td>
			</tr>
			<tr>
				<td style="text-align:center;font-weight:bold">申请借款天数</td>
				<td style="text-align:center">
					<c:if test="${content.request_info.request_days_1m == null}">
							0
					</c:if>
					<c:if test="${content.request_info.request_days_1m != null}">
						${content.request_info.request_days_1m}
					</c:if>
				</td>
				<td style="text-align:center">
					<c:if test="${content.request_info.request_days_3m == null}">
							0
					</c:if>
					<c:if test="${content.request_info.request_days_3m != null}">
						${content.request_info.request_days_3m}
					</c:if>
				</td>
				<td style="text-align:center">
					<c:if test="${content.request_info.request_days_6m == null}">
							0
					</c:if>
					<c:if test="${content.request_info.request_days_6m != null}">
						${content.request_info.request_days_6m}
					</c:if>
				</td>
				<td style="text-align:center">
					<c:if test="${content.request_info.request_days == null}">
							0
					</c:if>
					<c:if test="${content.request_info.request_days != null}">
						${content.request_info.request_days}
					</c:if>
				</td>
			</tr>
			<tr>
				<td style="text-align:center;font-weight:bold">实际借款平台数</td>
				<td style="text-align:center">
					<c:if test="${content.loan_detail.actual_loan_platform_count_1m == null}">
							0
					</c:if>
					<c:if test="${content.loan_detail.actual_loan_platform_count_1m != null}">
						${content.loan_detail.actual_loan_platform_count_1m}
					</c:if>
				</td>
				<td style="text-align:center">
					<c:if test="${content.loan_detail.actual_loan_platform_count_3m == null}">
							0
					</c:if>
					<c:if test="${content.loan_detail.actual_loan_platform_count_3m != null}">
						${content.loan_detail.actual_loan_platform_count_3m}
					</c:if>
				</td>
				<td style="text-align:center">
					<c:if test="${content.loan_detail.actual_loan_platform_count_6m == null}">
							0
					</c:if>
					<c:if test="${content.loan_detail.actual_loan_platform_count_6m != null}">
						${content.loan_detail.actual_loan_platform_count_6m}
					</c:if>
				</td>
				<td style="text-align:center">
					<c:if test="${content.loan_detail.actual_loan_platform_count == null}">
							0
					</c:if>
					<c:if test="${content.loan_detail.actual_loan_platform_count != null}">
						${content.loan_detail.actual_loan_platform_count}
					</c:if>
				</td>
			</tr>
			<tr>
				<td style="text-align:center;font-weight:bold">还款平台数</td>
				<td style="text-align:center">
					<c:if test="${content.loan_detail.repayment_platform_count_1m == null}">
							0
					</c:if>
					<c:if test="${content.loan_detail.repayment_platform_count_1m != null}">
						${content.loan_detail.repayment_platform_count_1m}
					</c:if>
				</td>
				<td style="text-align:center">
					<c:if test="${content.loan_detail.repayment_platform_count_3m == null}">
							0
					</c:if>
					<c:if test="${content.loan_detail.repayment_platform_count_3m != null}">
						${content.loan_detail.repayment_platform_count_3m}
					</c:if>
				</td>
				<td style="text-align:center">
					<c:if test="${content.loan_detail.repayment_platform_count_6m == null}">
							0
					</c:if>
					<c:if test="${content.loan_detail.repayment_platform_count_6m != null}">
						${content.loan_detail.repayment_platform_count_6m}
					</c:if>
				</td>
				<td style="text-align:center">
					<c:if test="${content.loan_detail.repayment_platform_count == null}">
							0
					</c:if>
					<c:if test="${content.loan_detail.repayment_platform_count != null}">
						${content.loan_detail.repayment_platform_count}
					</c:if>
				</td>
			</tr>
			<tr>
			    <td style="font-weight:bold;;background-color: #2FB6EF;color: white" colspan ="5">行业借款详情&nbsp;&nbsp;<img onclick="detail();" alt="" src="${ctxStatic}/images/question.png"></td>
			</tr>
			<tr>
				<td style="text-align:center;font-weight:bold">行业分类</td>
				<td style="text-align:center;font-weight:bold">申请借款平台数</td>
				<td style="text-align:center;font-weight:bold">实际借款平台数</td>
				<td style="text-align:center;font-weight:bold">还款平台数</td>
				<td style="text-align:center;font-weight:bold">还款次数</td>
			</tr>
			<c:forEach items="${content.loan_detail.loan_industry}" var="loan_industry">
				<tr>
					<td style="text-align:center;font-weight:bold">
						${loan_industry.name}
					</td>
					<td style="text-align:center">
						<c:if test="${loan_industry.loan_platform_count == null}">
							0
						</c:if>
						<c:if test="${loan_industry.loan_platform_count != null}">
							${loan_industry.loan_platform_count}
						</c:if>
					</td>
					<td style="text-align:center">
						<c:if test="${loan_industry.actual_loan_platform_count == null}">
							0
						</c:if>
						<c:if test="${loan_industry.actual_loan_platform_count != null}">
							${loan_industry.actual_loan_platform_count}
						</c:if>
					</td>
					<td style="text-align:center">
						<c:if test="${loan_industry.repayment_platform_count == null}">
							0
						</c:if>
						<c:if test="${loan_industry.repayment_platform_count != null}">
							${loan_industry.repayment_platform_count}
						</c:if>
					</td>
					<td style="text-align:center">
						<c:if test="${loan_industry.repayment_times_count == null}">
							0
						</c:if>
						<c:if test="${loan_industry.repayment_times_count != null}">
							${loan_industry.repayment_times_count}
						</c:if>
					</td>
				</tr>
			</c:forEach>
			<tr>
			    <td style="font-weight:bold;;background-color: #2FB6EF;color: white" colspan ="5">申请借款行为</td>
			</tr>
			<tr>
				<td style="text-align:center;font-weight:bold"></td>
				<td style="text-align:center;font-weight:bold">近一个月最大值</td>
				<td style="text-align:center;font-weight:bold">近三个月最大值</td>
				<td style="text-align:center;font-weight:bold">近六个月最大值</td>
				<td style="text-align:center;font-weight:bold">历史最大值</td>
			</tr>
			<tr>
				<td style="text-align:center;font-weight:bold">总申请次数</td>
				<td style="text-align:center">
					<c:if test="${content.request_info.request_count_1m == null}">
							0
					</c:if>
					<c:if test="${content.request_info.request_count_1m != null}">
						${content.request_info.request_count_1m}
					</c:if>
				</td>
				<td style="text-align:center">
					<c:if test="${content.request_info.request_count_3m == null}">
							0
					</c:if>
					<c:if test="${content.request_info.request_count_3m != null}">
						${content.request_info.request_count_3m}
					</c:if>
				</td>
				<td style="text-align:center">
					<c:if test="${content.request_info.request_count_6m == null}">
							0
					</c:if>
					<c:if test="${content.request_info.request_count_6m != null}">
						${content.request_info.request_count_6m}
					</c:if>
				</td>
				<td style="text-align:center">
					<c:if test="${content.request_info.request_count == null}">
							0
					</c:if>
					<c:if test="${content.request_info.request_count != null}">
						${content.request_info.request_count}
					</c:if>
				</td>
			</tr>
			<tr>
				<td style="text-align:center;font-weight:bold">单日申请次数</td>
				<td style="text-align:center">
					<c:if test="${content.request_info.request_count_on_same_day_1m == null}">
							0
					</c:if>
					<c:if test="${content.request_info.request_count_on_same_day_1m != null}">
						${content.request_info.request_count_on_same_day_1m}
					</c:if>
				</td>
				<td style="text-align:center">
					<c:if test="${content.request_info.request_count_on_same_day_3m == null}">
							0
					</c:if>
					<c:if test="${content.request_info.request_count_on_same_day_3m != null}">
						${content.request_info.request_count_on_same_day_3m}
					</c:if>
				</td>
				<td style="text-align:center">
					<c:if test="${content.request_info.request_count_on_same_day_6m == null}">
							0
					</c:if>
					<c:if test="${content.request_info.request_count_on_same_day_6m != null}">
						${content.request_info.request_count_on_same_day_6m}
					</c:if>
				</td>
				<td style="text-align:center">
					<c:if test="${content.request_info.request_count_on_same_day == null}">
							0
					</c:if>
					<c:if test="${content.request_info.request_count_on_same_day != null}">
						${content.request_info.request_count_on_same_day}
					</c:if>
				</td>
			</tr>
			<tr>
				<td style="text-align:center;font-weight:bold">单日申请平台数</td>
				<td style="text-align:center">
					<c:if test="${content.request_info.max_request_platform_count_on_same_day_1m == null}">
							0
					</c:if>
					<c:if test="${content.request_info.max_request_platform_count_on_same_day_1m != null}">
						${content.request_info.max_request_platform_count_on_same_day_1m}
					</c:if>
				</td>
				<td style="text-align:center">
					<c:if test="${content.request_info.max_request_platform_count_on_same_day_3m == null}">
							0
					</c:if>
					<c:if test="${content.request_info.max_request_platform_count_on_same_day_3m != null}">
						${content.request_info.max_request_platform_count_on_same_day_3m}
					</c:if>
				</td>
				<td style="text-align:center">
					<c:if test="${content.request_info.max_request_platform_count_on_same_day_6m == null}">
							0
					</c:if>
					<c:if test="${content.request_info.max_request_platform_count_on_same_day_6m != null}">
						${content.request_info.max_request_platform_count_on_same_day_6m}
					</c:if>
				</td>
				<td style="text-align:center">
					<c:if test="${content.request_info.max_request_platform_count_on_same_day == null}">
							0
					</c:if>
					<c:if test="${content.request_info.max_request_platform_count_on_same_day != null}">
						${content.request_info.max_request_platform_count_on_same_day}
					</c:if>
				</td>
			</tr>
			<tr>
				<td style="text-align:center;font-weight:bold">单平台申请次数</td>
				<td style="text-align:center">
					<c:if test="${content.request_info.max_request_count_on_same_platform_1m == null}">
							0
					</c:if>
					<c:if test="${content.request_info.max_request_count_on_same_platform_1m != null}">
						${content.request_info.max_request_count_on_same_platform_1m}
					</c:if>
				</td>
				<td style="text-align:center">
					<c:if test="${content.request_info.max_request_count_on_same_platform_3m == null}">
							0
					</c:if>
					<c:if test="${content.request_info.max_request_count_on_same_platform_3m != null}">
						${content.request_info.max_request_count_on_same_platform_3m}
					</c:if>
				</td>
				<td style="text-align:center">
					<c:if test="${content.request_info.max_request_count_on_same_platform_6m == null}">
							0
					</c:if>
					<c:if test="${content.request_info.max_request_count_on_same_platform_6m != null}">
						${content.request_info.max_request_count_on_same_platform_6m}
					</c:if>
				</td>
				<td style="text-align:center">
					<c:if test="${content.request_info.max_request_count_on_same_platform == null}">
							0
					</c:if>
					<c:if test="${content.request_info.max_request_count_on_same_platform != null}">
						${content.request_info.max_request_count_on_same_platform}
					</c:if>
				</td>
			</tr>
			<tr>
			    <td style="font-weight:bold;;background-color: #2FB6EF;color: white" colspan ="5">关联图谱&nbsp;&nbsp;<img onclick="pic();" alt="" src="${ctxStatic}/images/question.png"></td>
			</tr>
			<tr>
				<td style="text-align:center;font-weight:bold">关联用户数</td>
				<td style="text-align:center">
					${content.graph_detail.link_user_count}
				</td>
				<td style="text-align:center" colspan ="3">
				</td>
			</tr>
			<tr>
				<td style="text-align:center;font-weight:bold">法院失信</td>
				<td style="text-align:center">
					${content.graph_detail.link_user_detail.court_dishonest_count}
				</td>
				<td style="text-align:center;font-weight:bold">网贷失信</td>
				<td style="text-align:center">
					${content.graph_detail.link_user_detail.online_dishonest_count}
				</td>
				<td style="text-align:center" >
				</td>
			</tr>
			<tr>
				<td style="text-align:center;font-weight:bold">活体攻击行为</td>
				<td style="text-align:center">
					${content.graph_detail.link_user_detail.living_attack_count}
				</td>
				<td style="text-align:center;font-weight:bold">商户标记个数</td>
				<td style="text-align:center">
					${content.graph_detail.link_user_detail.partner_mark_count}
				</td>
				<td style="text-align:center" >
				</td>
			</tr>
			<tr>
				<td style="text-align:center;font-weight:bold">使用设备数</td>
				<td style="text-align:center">
					${content.graph_detail.link_device_count}
				</td>
				<td style="text-align:center" colspan ="2">
				</td>
				<td style="text-align:center">
				</td>
			</tr>
			<tr>
				<td style="text-align:center;font-weight:bold">可疑设备</td>
				<td style="text-align:center">
					${content.graph_detail.link_device_detail.frand_device_count}
				</td>
				<td style="text-align:center;font-weight:bold">活体攻击设备</td>
				<td style="text-align:center">
					${content.graph_detail.link_device_detail.living_attack_device_count}
				</td>
				<td style="text-align:center" >
				</td>
			</tr>
			<tr>
				<td style="text-align:center;font-weight:bold">其他关联设备数</td>
				<td style="text-align:center">
					${content.graph_detail.other_link_device_count}
				</td>
				<td style="text-align:center" >
				</td>
				<td style="text-align:center" >
				</td>
				<td style="text-align:center" >
				</td>
			</tr>
			<tr>
				<td style="text-align:center;font-weight:bold">可疑设备</td>
				<td style="text-align:center">
					${content.graph_detail.other_link_device_detail.other_frand_device_count}
				</td>
				<td style="text-align:center;font-weight:bold">活体攻击设备</td>
				<td style="text-align:center">
					${content.graph_detail.other_link_device_detail.other_living_attack_device_count}
				</td>
				<td style="text-align:center" >
				</td>
			</tr>
			<tr>
				<td style="text-align:center;font-weight:bold">名下银行卡数</td>
				<td style="text-align:center">
					${content.graph_detail.user_have_bankcard_count}
				</td>
				<td style="text-align:center;font-weight:bold">关联银行卡数</td>
				<td style="text-align:center">
					${content.graph_detail.bankcard_count}
				</td>
				<td style="text-align:center" >
				</td>
			</tr>
			<tr>
				<td style="text-align:center;font-weight:bold">使用手机号</td>
				<td style="text-align:center">
					${content.graph_detail.mobile_count}
				</td>
				<td style="text-align:center" >
				</td>
				<td style="text-align:center" >
				</td>
				<td style="text-align:center" >
				</td>
			</tr>
			</table>
			<table id="contentTable2" class="table table-striped table-bordered table-condensed">
			<tr>
			    <td style="font-weight:bold;;background-color: #2FB6EF;color: white" colspan ="7">用户使用设备信息</td>
			</tr>
			<tr>
				<td style="text-align:center;font-weight:bold">移动设备型号</td>
				<td style="text-align:center;font-weight:bold">设备使用人数</td>
				<td style="text-align:center;font-weight:bold">是否为可疑设备</td>
				<td style="text-align:center;font-weight:bold">是否安装作弊软件</td>
				<td style="text-align:center;font-weight:bold">借贷app安装数量</td>
				<td style="text-align:center;font-weight:bold">设备越狱</td>
				<td style="text-align:center;font-weight:bold">最近使用时间</td>
			</tr>
			<c:forEach items="${content.devices_list}" var="devicesList">
				<tr>
					<td style="text-align:center">
						${devicesList.device_name}
					</td>
					<td style="text-align:center">
						${devicesList.device_link_id_count}
					</td>
					<td style="text-align:center">
						<c:choose>
							<c:when test="${devicesList.device_detail.fraud_device == 1}">
								<div style="color:red">是</div>
							</c:when>
							<c:when test="${devicesList.device_detail.fraud_device == 0}">
								否
							</c:when>
							<c:otherwise>
								未知
							</c:otherwise>
						</c:choose>
					</td>
					<td style="text-align:center">
						<c:choose>
							<c:when test="${devicesList.device_detail.cheats_device == 1}">
								<div style="color:red">是</div>
							</c:when>
							<c:when test="${devicesList.device_detail.cheats_device == 0}">
								否
							</c:when>
							<c:otherwise>
								未知
							</c:otherwise>
						</c:choose>
					</td>
					<td style="text-align:center">
						<c:choose>
							<c:when test="${devicesList.device_detail.app_instalment_count == null}">
								0
							</c:when>
							<c:otherwise>
								${devicesList.device_detail.app_instalment_count }
							</c:otherwise>
						</c:choose>
					</td>
					<td style="text-align:center">
						<c:choose>
							<c:when test="${devicesList.device_detail.is_rooted == 1}">
								<div style="color:red">是</div>
							</c:when>
							<c:when test="${devicesList.device_detail.is_rooted == 0}">
								否
							</c:when>
							<c:otherwise>
								未知
							</c:otherwise>
						</c:choose>
					</td>
					<td style="text-align:center">
						${devicesList.device_last_use_date}
					</td>
				</tr>
			</c:forEach>
			</table>
			
			
		</tbody>
	
	<div class="pagination">${page}</div>
</body>
</html>