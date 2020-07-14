<%@ page contentType="text/html;charset=utf-8" language="java"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%@include file="/WEB-INF/views/app/constant.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <title>客服服务</title>
   <%@include file="/WEB-INF/views/app/meta-flex.jsp" %>
    <meta name="keywords" content="无忧借条"/>
    <meta name="description" content="无忧借条"/>
    <link rel="stylesheet" href="${mbStatic}/assets/css/public.css?v=<%=version%>12" type="text/css">
    <link rel="stylesheet" href="${mbStatic}/assets/css/user.css?v=<%=version%>12" type="text/css">
    <script src="${mbStatic}/assets/scripts/base/sea.js?v=<%=version%>12"></script>
    <script src="${mbStatic}/assets/scripts/base/flexible.js?v=<%=version%>"></script>
    <style>
        h2{font-weight: bold;}
        table{
            width: 100%;
            margin: 10px 0;
            text-align: center;
            border-right: 0px solid #ccc;
            border-bottom: 1px solid #ccc;
            font-size: 1rem;
            line-height: 2rem;
        }
        table td{
            border-left: 1px solid #ccc;
            border-top: 1px solid #ccc;
            border-right: 1px solid #ccc;
        }
        .bold{font-weight: bold;font-size: 1.5rem;}
    </style>
</head>
<body>
<article class="views">
    <section class="view">
   		<c:if test="${!empty data}">    
            <jsp:include page="/WEB-INF/views/app/header-public.jsp">
            <jsp:param name="title" value="${titleName}"></jsp:param>
        	</jsp:include>
        </c:if>
        <div class="pages">
            <div class="page">
            	<c:if test="${empty data}">
            			<div class="page-content" style="padding-top: 0;">
            	</c:if>
            	<c:if test="${not empty data}">
            			<div class="page-content" style="padding-top: 3rem;">
            	</c:if>
                    <div class="ques-con" style="padding-bottom: 2rem;overflow: hidden;">
                   <c:choose>
                       		<c:when test="${title eq'cztx'}">
		                       	<h2>1、如何充值？</h2>
		                        <p>登录账户，点击首页右下角“我的” <span class="bold">--></span> 点击“充值”/“提现”。</p>
		                        <h2>2、充值都有哪些充值方式？ </h2>
		                        <p>大额充值模式和富友充值（需重新绑定银行卡）;富友单次充值最低2元起；</p>
		                        <p>大额充值请汇款至：北京友信宝网络科技有限公司，账号：1028 5000 0014 6002 0，开户行信息： 华夏银行北京分行上地支行。备注：姓名+手机号+无忧借条大额充值。</p>
		                        <p>充值产生的手续由用户承担。</p>
		                        <h2>3、充值之后多久到帐？未到账怎么办？</h2>
		                        <p>即时到账，出现充值15分钟后未到账，核实银行是否扣款；特殊情况致电客服电话400-6688-658核实。</p>
		                        <h2>4、提款到账时间？提现手续费多少？</h2>
		                        <p>上线富友认证支付，实现提现实时到账(一般10分钟之内)，实际到账时间因银行有所差别；手续费每笔2元。</p>
		                        <table>
		                            <tr>
		                                <td>时间</td>
		                                <td>金额限制</td>
		                            </tr>
		                            <tr>
		                                <td>09:30-17:30</td>
		                                <td>提现金额不受限制</td>
		                            </tr>
		                            <tr>
		                                <td>17:30-09:30（次日）</td>
		                                <td>单笔提现限额≤1.5万，累计提现限额≤1.5万</td>
		                            </tr>
		                        </table>
		
		                        <p>a、9:30-17:30期间提现单笔大于等于3W元时，需人工审核后才可以提现。</p>
		                        <p>b、充值提现的银行卡类型仅支持储蓄卡，不支持信用卡；不支持提款到别人银行卡。</p>
		                        <p>c、提现申请通过后，若因申请人信息有误导致银行打款失败，将扣除2元并退还用户。</p>
                       		</c:when>
                       		
                       		<c:when test="${title eq 'hyjk'}">
		                        <h2>1、如何借款？</h2>
		                        <p>账户登录  <span class="bold">--></span> 点击首页“我要借款”顺序操作<span class="bold">--></span>找好友借款<span class="bold">--></span>交流页。</p>
		                        <p>（注：需要好友下载无忧借条APP 相互加为好友）</p>
		                        <h2>2、如何还款？</h2>
		                        <p>点击首页右下角“我的”<span class="bold">--></span> 借条中心<span class="bold">--></span>我欠谁钱<span class="bold">--></span>点击订单<span class="bold">--></span>我要还款。</p>
		                        <p>a、若为全额还款，可选择线下还款也可选择申请延期，线下还款需好友确认后才算还款成功；</p>
		                        <p>b、一笔借款可多次申请延期，申请延期默认是1个月，延期时间可自行修改，最长一年；</p>
		                        <p>c、申请延期，需在逾期15天内（包括15天）申请，如果超出15天，则不能进行申请延期。</p>
		                        <h2>3、好友之间借款有利息吗？</h2>
		                        <p>利息是朋友双方平等协商决定，和无忧借条平台无关。</p>
		                        <h2>4、如果好友不还款怎么办？</h2>
		                        <p>无忧借条有专业的催收、仲裁服务，在APP端首页申请。</p>
		                        <h2>5、逾期未还款有什么影响？</h2>
		                        <p>影响个人信用，严重者会被拉入黑名单，还会影响您日后的借款。借款人可能申请催收、仲裁、诉讼，可能影响到贷款人的正常生活。</p>
							</c:when>
                       		
                       		<c:when test="${title eq 'zhzc'}">
	                       		<h2>如何注册？</h2>
		                        <p>下载客户端<span class="bold">--></span>点击首页右下角“我的”<span class="bold">--></span>注册登录<span class="bold">--></span>身份验证（可以点击右上角“跳过”）。</p>
		                        <br />
		                        <p>下载网址：<a href="/app/wyjt/common/download" target="_blank">http://www.51jt.com</a>或者关注微信公众号“无忧借条官网”-“阅读全文”下载。</p>
                       		</c:when>
                       		<c:when test="${title eq 'sfrz'}">
                       			 <h2>如何进行身份认证？</h2>
			                     <p>点击首页右下角“我的”<span class="bold">--></span>安全中心<span class="bold">--></span>身份认证。</p>
			                     <p>注：</p>
			                     <p>a、认证信息必须是真实有效；</p>
			                     <p>b、需提供身份证号、本人储蓄银行卡号、开户行、开户城市；</p>
			                     <p>c、需提供常用邮箱、常用地址。</p>
                       		</c:when>
                       		<c:when test="${title eq 'aqbz'}">
                       			 <p>a、无忧借条是经过CA认证、第三方数字存证、200多家公证处认可的电子借条,具有安全性、真实性、 合法合规性的特点。</p>
                        		 <p>b、无忧借条具有高级别数据安保系统，上岗员工经过专业培训，签署《客户隐私信息保护协议书》，切实保障客户相关信息安全。</p>
                        		 <p>c、自动和人审相结合对用户信息的采集、甄别、分析严格把控，保障借贷双方信息安全，真实有效。</p>
                       		</c:when>
                       		<c:when test="${title eq 'cjwt'}">
                       		 		<h2>1、忘记密码怎么找回？</h2>

                       				 <p>登录页面，点击忘记密码<span class="bold">--></span>输入手机号码验证<span class="bold">--></span>设置新密码 。</p>

                       				 <h2>2、如何修改密码？</h2>

                       				 <p>点击首页右下角“我的”<span class="bold">--></span>安全中心<span class="bold">--></span>修改登录密码/手势密码。</p>
                       		</c:when>
                       		<c:when test="${title eq 'czcm'}">
	                       		 <h2 >如何充值？</h2>
	
		                        <p>1.支付宝和微信充值无限额，如出现限额提示，是您所绑定银行卡的支付额限制，可联系银行客服或在银行官方网站上修改网上支付限额。充值姓名必须要与在本站绑定真实信息一致，以免耽误您提现。</p>
		
		                        <p>2.银联支付每天限额5000元~20000元，限额因你绑定的银行卡而不同。充值银行卡与提现银行卡必须保持一致，以免耽误您提现。</p>
		
		                        <p>3.大额充值可选择汇款至：北京友信宝网络科技有限公司。</p>
		
		                        <p>账号：1028 5000 0014 6002 0</p>
		
		                        <p>开户行：华夏银行北京分行上地支行</p>
		
		                        <p >须备注：姓名+手机号+无忧借条大额充值</p>
		
		                        <p>汇款后联系客服核实确认，可直接在账户加款。</p>
		
		                        <p>客服电话：400-6688-658。</p>
                       		</c:when>
                       		<c:when test="${title eq 'csfw' }">
                       			<h2 >1、催款都有哪些方式？</h2>

		                        <p>无忧借条有短信提醒、站内消息提醒、人工电话提醒提醒服务方式；同时提供专业的催收、仲裁服务，在APP端首页申请。平台不承诺、不保证100%成功催款。</p>
		
		                        <h2>2、如何申请催收服务？</h2>
		
		                        <p>登录页面，点击“催收仲裁” <span class="bold">--></span>选择法律仲裁/专业催收。</p>
		
		                        <h2>3、哪里查看催收进度？</h2>
		
		                        <p>登录页面，点击“催收仲裁”<span class="bold">--></span>选择“专业催收”<span class="bold">--></span>查看催收进度</p>
		                        <p>注：微信公众号 “无忧借条官网”会定期公布催收成功名单。</p>
                       		</c:when>
                       		<c:otherwise>
                       			<h2 >1、催款都有哪些方式？</h2>

		                        <p>无忧借条有短信提醒、站内消息提醒、人工电话提醒提醒服务方式；同时提供专业的催收、仲裁服务，在APP端首页申请。平台不承诺、不保证100%成功催款。</p>
		
		                        <h2>2、如何申请催收服务？</h2>
		
		                        <p>登录页面，点击“催收仲裁” <span class="bold">--></span>选择法律仲裁/专业催收。</p>
		
		                        <h2>3、哪里查看催收进度？</h2>
		
		                        <p>登录页面，点击“催收仲裁”<span class="bold">--></span>选择“专业催收”<span class="bold">--></span>查看催收进度</p>
		                        <p>注：微信公众号 “无忧借条官网”会定期公布催收成功名单。</p>
                       		</c:otherwise>
                       </c:choose>
                    </div>
                </div>
            </div>
        </div>
    </section>
</article>
<script>
    seajs.use('jumpApp');
</script>
</body>
</html>
<!--created by sson 2015/7/20-->