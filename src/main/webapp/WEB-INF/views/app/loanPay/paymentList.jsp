
<%@ page contentType="text/html;charset=utf-8" language="java"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%@include file="/WEB-INF/views/app/constant.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <title>付款单</title>
    <%@include file="/WEB-INF/views/app/meta-flex.jsp" %>
    <meta name="keywords" content=""/>
    <meta name="description" content=""/>
    <link rel="stylesheet" href="${mbStatic}/assets/css/public.css?v=<%=version%>" type="text/css">
    <link rel="stylesheet" href="${mbStatic}/assets/css/repay.css?v=<%=version%>" type="text/css">
    <link rel="stylesheet" href="${mbStatic}/assets/css/user.css?v=<%=version%>" type="text/css">
    <script src="${mbStatic}/assets/scripts/base/sea.js?v=<%=version%>21"></script>
    <script src="${mbStatic}/assets/scripts/base/flexible.js?v=<%=version%>"></script>
    <style type="text/css">
        .fkdBox{
            background: white;
            border-left-radius: .3rem;
            margin: 1rem;
            padding: .5rem;
            margin-bottom: 0;
            border-top-left-radius: .3rem;
            border-top-right-radius: .3rem;
            border: 1px solid #c4c4c4;
            border-bottom: none;
            font-size:1rem;
        }
        .fkdBox p {
            border-bottom: 1px #ddd dashed;
            height: 3rem;
            line-height: 3rem;
            position: relative;
        }
        .fkdBox p i,.fkdBox p span,.fkdBox p b,.fkdBox p em{
            display: inline-block;
            text-align: center;
        }
        .fkdBox p i{
            width: 10%;

        }
        .fkdBox p b{
            width: 40%;
            margin-left: 4rem;

        }
        .fkdBox p span{
            width: 30%;

        }
        .fkdBox p em{
            width: 10%;
        }
        .fkdBox p:last-child{
            border-bottom:none;
        }
        .radius{
            border-radius: 1rem;
            text-align: center;
            width: 1.8rem!important;
            height: 1.8rem;
            line-height: 1.8rem;
            margin-right: 1rem;
            margin-left: .5rem;
            font-size: 1.1rem;
            position: absolute;
            top: .6rem;
        }
        .gray{
            border: 1px solid gray;
            color:gray ;
        }
        .red{
            background: rgb(217,30,61);
            color: white;
        }
        .redRight{
            background: url("${mbStatic}/assets/images/goods-checked@1x.png") no-repeat;
            background-size: 100%;
            color: transparent;
        }
        .orange{
            color: #F5B043;
        }
        .subBox{
            position: fixed;
            width: 100%;
            height: 3rem;
            bottom: 0;
            background: white;
            line-height: 3rem;
            font-size: 1rem;
        }
        .subBox i{
            display: inline-block;
            width: 1.7rem;
            height: 1.7rem;
            line-height: 1.7rem;
            border: 1px solid grey;
            border-radius: 1rem;
            margin: 0 .5rem;
            position: absolute;
            top:.7rem;
        }
        .checkRed{
            border: none!important;
            background: url("${mbStatic}/assets/images/debt/checked@2x.png") no-repeat;
            background-size: 100%;
        }
        .subBox span{
            display: inline-block;
            width: 66%;
            margin-left: 3.5rem;
        }
        .subBox span em{
            margin: 0 .5rem;
            display: inline-block;
            text-align: center;
            color: red;
        }
        .subBox a{
            width: 22%;
            display: inline-block;
            text-align: center;
            background: red;
            right: 0;
            position: absolute;
            font-size: 1.1rem;
            color: white;
        }
        .boxFooter{
            background: url("${mbStatic}/assets/images/repay/foot.png") no-repeat;
            background-size: 100%;
            margin: 0 1rem;
            height: 2rem;
        }
        .isPointer{
            pointer-events:none;
        }
        .pop-layout{
			top:-10rem;
		}
    </style>
</head>
<c:choose>
	<c:when test="${empty appPlatform}">
		<body>	
	</c:when>
	<c:otherwise>
		<body class="${isWeiXin? 'InWeixin':'InWebView'}" data-platform="${appPlatform }"  data-app="51jt">
	</c:otherwise>
</c:choose>
<article class="views">
    <section class="view">
        <div class="pages navbar-fixed   ">
            <div class="page">
                <div class="page-content" style="padding-top:0;">
                    <!--content -->
                    <div class="fkdBox">
                    		<c:if test="${not empty doneRecords}">
								<c:forEach items="${doneRecords}" var="record">
									<p><i class="radius  gray">${record.periodsSeq}</i><b>${fns:decimalToStr(record.expectRepayAmt,2)}元</b>
										<span>${fns:getDateStr(record.expectRepayDate,"yyyy-MM-dd")}</span><em>${fns:getDictLabel(record.status, "repayRecordStatus", "")}</em></p>
								</c:forEach>
		                   </c:if>
							<c:if test="${not empty overdueRecords}">
								<c:forEach items="${overdueRecords}" var="record">
					                 <p><i class="radius  redRight"></i><b>${fns:decimalToStr(record.expectRepayAmt,2)}元</b><span>${fns:getDateStr(record.expectRepayDate,"yyyy-MM-dd")}</span><em class="orange">${fns:getDictLabel(record.status, "repayRecordStatus", "")}</em></p>
								</c:forEach>
							</c:if>
							<c:if test="${hasOverdue}">
								<p><i class="radius  red">逾</i><b>${fns:decimalToStr(overdueInterest,2)}元</b><span>逾期利息</span><em class="orange">未还</em></p>
							</c:if>
							<c:if test="${not empty pendingRecords}">
								<c:forEach items="${pendingRecords}" var="record">
									<c:if test="${isArbitration eq true}">
					                    <p><i class="radius  redRight"></i><b>${fns:decimalToStr(record.expectRepayAmt,2)}元</b><span>${fns:getDateStr(record.expectRepayDate,"yyyy-MM-dd")}</span><em>未还</em></p>
									</c:if>
									<c:if test="${isArbitration eq false}">
										<p><i class="radius  gray canCheck">${record.periodsSeq}</i><b>${fns:decimalToStr(record.expectRepayAmt,2)}元</b><span>${fns:getDateStr(record.expectRepayDate,"yyyy-MM-dd")}</span><em>未还</em></p>
									</c:if>
								</c:forEach>
							</c:if>
                    </div>
                    <p class="boxFooter"></p>
                    <c:if test="${isArbitration eq true}">
                    	<p class="subBox"><i class='isPointer'></i><span>已选<em>2700</em>元</span><a href="javascript:void 0;">立即支付</a></p>
                    </c:if>
                    <c:if test="${isArbitration eq false}">
                    	<p class="subBox"><i></i><span>已选<em>2700</em>元</span><a href="javascript:void 0;">立即支付</a></p>
                    </c:if>
                </div>
                <form id="dataForm" action="${pageContext.request.contextPath}/app/wyjt/loan/stagesRepay" method="post">
	                <input type="hidden" id="memberToken" name="_header" value="${memberToken}" />
			        <input type="hidden" name="_type" value="h5">
			        <input type="hidden" id="allNum" name="allNum">
			        <input type="hidden" id="loanId" name="loanId" value="${loan.id}">
					<input type="hidden" id="calTotalInput" >
				 </form>
            </div>
        </div>
    </section>
</article>
<script type="text/javascript">
    define('payList', ['zepto','jumpApp'], function (require, exports, module) {
        var $ = require('zepto'), ev = $.support.touch ? 'tap' : 'click',call = require('jumpApp');
        var $check=$('.subBox i'),$sub=$('.subBox a'),passwordVerify,$canCheck=$('.fkdBox p');
        var isArbitration=${isArbitration};
        var allNum = 0,flag=false,token='${memberToken}';
        var isPayPsw = ${isPayPsw};
        _sum();
        $check[ev](function () {
        	var _this=$(this);
            if(_this.hasClass('checkRed')){ 
            	_this.removeClass('checkRed');
            	$('.canCheck').addClass('gray').removeClass('redRight checked');
            }else{
            	_this.addClass('checkRed');
            	$('.canCheck').addClass('redRight checked').removeClass('gray');
            }
            _sum();
        });
        $canCheck[ev](function() {
        	
        	var $this=$(this),_this=$this.find('.canCheck'),_index=$this.index();
            if(_index==($canCheck.length-1)) $('.canCheck').addClass('redRight checked').removeClass('gray'),$check.addClass('checkRed');
            else {
                $('.canCheck').removeClass('redRight checked').addClass('gray'),$check.removeClass('checkRed');
                for (var i=_index;i>=0;i--){
                    var that=$('.fkdBox p').eq(i);
                    if(that.find('.canCheck').length>0){
                        if(that.find('.canCheck').hasClass('gray')) that.find('.canCheck').removeClass('gray').addClass('redRight checked');
                    }
                }
            }
            _sum();
        });
        $sub[ev](function () {
        	payByPwd();
        });
        function _sum(arr) {
            var $sumBox=$('.red ,.redRight'),sum=0;
            for (var i=0;i<$sumBox.length;i++){
                sum+=parseFloat($sumBox.eq(i).next().html());
            }
            sum = sum.toFixed(2);
            $('.subBox span em').html(sum);;
        };
        
        function payByPwd() {
            var _this = this, tial = parseFloat($('.subBox span em').html()), remainMoney = parseFloat('${avlBal}');
            if (flag) {
                $.toast('请勿重复提交~！');
                return false;
            }
            var isEnough = false;
            if(tial <= 0){
            	 $.toast('请选择要还的期数！');
            	return false;
            }
            if(tial <= remainMoney){
            	isEnough = true;
            };
            if (!isEnough) {
	                 flag = false;
	                $.confirm('<p class="withdraw-pass item-password"><label for="withdrawPass">对不起，您的余额不足，请充值后再支付。</label></p>', {
	                    callback: function () {
	                        goRecharge();
	                    },
	                    btn: ['再想想', '去充值'],
	                    cls: 'withdraw-pass-dialog'
	                });
            }else {
	               if (isPayPsw){
		                if (!passwordVerify) {
		                    require.async(['user/password-verify'], function (pverify) {
		                        passwordVerify = pverify.verify({
		                            token: token,
		                            action: 'checkPwd',
		                            pwdType:1,
		                            title: '<span style="line-height: 2.6">输入支付密码</span>',
		                            callback: function (code, msg) {
		                                if (code) {
		                                	flag = true;
		                                    $.toast('支付申请成功', 1000, function () {
		                                    	allNum = $('.fkdBox .checked').length;
		                                    	$("#allNum").val(allNum);
		                                    	$("#dataForm").submit();
		                                    });
		                                } else {
		    								flag = false;
		                                	$.alert(msg);
		                                };
		                            }
		                        })
		                    })
		                }
		                else passwordVerify.open();
	              }else{
	                $.alert('为了您的账户安全，请先去设置支付密码。', setPwd);
	              }
            } 
        };
        function goRecharge(){
        	 var parameter = {"xxx": ""};
             call.callApp('recharge', parameter);
        };
        
        function setPwd() {
            call.callApp('setpaypass');
        }
    });
    seajs.use(['payList', 'jumpApp']);
</script>

</body>
</html>