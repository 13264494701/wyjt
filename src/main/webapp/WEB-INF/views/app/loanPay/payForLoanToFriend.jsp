<%@ page contentType="text/html;charset=utf-8" language="java"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%@include file="/WEB-INF/views/app/constant.jsp" %>
<!DOCTYPE html>
<html>
<head>
	<!-- 主动放款给好友付款页面 -->
    <title>确认出借</title>
    <%@include file="/WEB-INF/views/app/meta-flex.jsp" %>
    <meta name="keywords" content=""/>
    <meta name="description" content=""/>
    <link rel="stylesheet" href="${mbStatic}/assets/css/public.css?v=<%=version%>" type="text/css">
    <link rel="stylesheet" href="${mbStatic}/assets/css/user.css?v=<%=version%>" type="text/css">
    <script src="${mbStatic}/assets/scripts/base/sea.js?v=<%=version%>21"></script>
    <script src="${mbStatic}/assets/scripts/base/flexible.js?v=<%=version%>"></script>
    <style type="text/css">
        .pop-gray.pop-dialogs .pop-btn {
            background: #eeedf2;
            color: #999;
        }

        .pop-info {
            margin-top: 1rem;
        }

        .pop-label {
            color: #5d5d5d;
            font-size: 1rem;
            margin-top: 1rem;
        }

        .pop-label input {
            width: 1rem;
            height: 1rem;
            vertical-align: -0.1667rem;
        }

        .radius {
            border-radius: .5rem;
            /*border: 1px RGB(219,219,219) solid;*/
            margin: .2rem 0;
            /*height: 25.5rem;*/
            font-size: 1.1rem;
            background: white;
        }

        .radius p {
            /*height: 3.5rem;*/
            border-bottom: 1px RGB(226, 226, 226) solid;
            margin-left: 1rem;
            margin-right: 1rem;
        }

        .radius p span {
            display: inline-block;
            padding: .565rem 0rem;
            height: 1.5rem;
            line-height: 1.5rem;
        }

        .radius p :first-child {
            width: 25%;
            float: left;
            text-align: left;
            color: RGB(153, 153, 153);
        }

        .radius p :last-child {
            width: 75%;
			text-align:right;
        }

        .radiusTtile {
            text-align: center;
            font-size: 1.34rem;
            height: 3rem;
            line-height: 4rem;
            background: white;
        }

        .radiusTtile img {
            margin-right: .5rem;
            width: 2.5rem;
            display: inline-block;
            margin-bottom: .3rem;
        }

        .radiusTtileMoney {
            font-size: 2.5rem;
            text-align: center;
            font-weight: bold;
            background: white;
        }

        p a {
            /*display: inline-block;*/
            width: 66% !important;
        }

        .recharge-but {
            margin-top: 1.5rem;
            margin-bottom: 1.5rem;
        }

        .disabled {
            pointer-events: none;
            cursor: default;
            opacity: 0.2;
            background-color: #666;
        }

        .removeDis {
            background-color: #E71B1B !important;
        }

        .user-main .radius p select {
                float: right;
                height: 2.0351rem;
                line-height: 2.0351rem;
                width: 7.5rem;
                background: url(${mbStatic}/assets/images/debt/dispute@2x.png) 0rem .1rem no-repeat;
                background-size: 98%;
                padding-right: 2.2rem;
                padding-left: .85rem;
                align-items: center;
                -webkit-appearance: none;
        }

        #rechargeBtn {
            width: 5rem !important;
            background: url("${mbStatic}/assets/images/debt/toRight_blue@2x.png") 4.2rem .85rem no-repeat;
            background-size: 14%;
        }

        .zcType {
            display: inline-block;
            width: 8rem !important;
            background: url("${mbStatic}/assets/images/debt/toRight_blue@2x.png") 7.2rem 1.1rem no-repeat;
            background-size: 9%;
            height: 3rem;
        }

        .labCheck {
            width: 58% !important;
            display: inline-block !important;
            background: url("${mbStatic}/assets/images/debt/agree_on@2x.png") 0rem 1.1rem no-repeat;
            background-size: 8%;
            padding-right: 0 !important;
            padding-left: 1.5rem !important;
            height: 2rem !important;
            line-height: 2rem !important;
        }

        .chekOn {
            background: url("${mbStatic}/assets/images/debt/agree_off@2x.png") 0rem 1.1rem no-repeat;
            background-size: 8%;
        }

        .isVideo {
            /*display: inline-block !important;*/
            width: 10rem !important;
            background: url("${mbStatic}/assets/images/debt/switch_on@2x.png") 6.8rem 1.3rem no-repeat;
            background-size: 30%;
            height: 2rem !important;
            line-height: 2rem !important;
        }

        .isOn {
            display: inline-block !important;
            width: 10rem !important;
            background: url("${mbStatic}/assets/images/debt/switch_off@2x.png") 6.8rem 1.3rem no-repeat;
            background-size: 30%;
            height: 2rem !important;
            line-height: 2rem !important;
        }

        /*
           以下是弹出框区的样式
        */
		.infoPut{
			position: relative;
		}
		.infoPut input{
			border-radius:.2rem;
			width: 75%;
			height: 2rem;
			margin-top:.5rem;
			padding-left:.5rem;
			text-align: center;
		}
		.infoPut i{
			position: absolute;
			right: 2rem;
			top:.35rem;
			background: url("${mbStatic}/assets/images/v1/cancel.png") 0rem .4rem no-repeat;
			background-size:1.6rem;
			display: inline-block;
			width: 2rem;
			height: 2rem;
		}
		.pop-toast .pop-inner{
			background:white;

		}
		.pop-toast .pop-msg{
			color:#a0a0a0;
		}
		.sendSuccess{
			background: url("${mbStatic}/assets/images/debt/right.png") 5rem 0rem no-repeat;
			background-size:1.8rem;
			height: 3rem;
			color: #00abff;
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
        <div class="pages navbar-fixed toolbar-fixed">
            <div class="page">
                <div class="page-content user-main withdraw-main " style="background:RGB(238,242,243);padding-bottom:1rem;    padding-top: 0;">
                    <p class="radiusTtile"><img src="${mbStatic}/assets/images/debt/lend@2x.png"><span>我要出借</span></p>
                    <p class="radiusTtileMoney">- ${amount}</p>
                    <div class="radius" style="">
                        <p><span>借出金额</span><span>${amount}元</span></p>
                        <p><span>借给谁</span><span>${loanee.name}</span></p>
                       	<p><span>还款方式</span>
				            <c:if test="${repayType == '0'}">
				                   <span>全额</span>
				            </c:if>
				            <c:if test="${repayType == '1'}">
				                   <span>分期</span>
				            </c:if>
		                </p>
		                <p>
		                    <c:if test="${repayType == '0'}">
		                        	<span>借款时长</span><span>${repayDate}（${term}天）</span>
				            </c:if>
				            <c:if test="${repayType == '1'}">
				                    <span>分期期数</span><span style="float: right;width: 60%;color:RGB(66,106,249) " id="goDetails" class="goDetails"  data-title="计算结果">分期计划&nbsp;&nbsp; ${periods}期</span>
				           
				            </c:if>
		                </p>
                        <p><span>年化利率</span><span>${intRate}%，${interest}元</span></p>
	                    <p><span>可用余额</span><span>${avlBal}元</span>
                        <span style="float: right;width: 20%;color:RGB(66,106,249);text-align:left;" id="rechargeBtn">&nbsp;去充值</span></p>
                    </div>
                    <div class="radius">
                        <p style="height: 3.5rem">
                            <span class="labCheck ">我同意
                            	<a style="float: right;width: 75%!important;color:RGB(66,106,249)" href="javascript:void (0);" data-href="" data-title="友借款服务协议">《友借款服务协议》</a>
                            </span>
                            <span class="zcType" style="float: right;color: RGB(66,106,249);text-align:left;padding-top:.85rem" data-href="" data-title="详细说明">查看详情说明</span>
                        </p>
                        <p><span style="width: 40%;">选择争议解决方式</span>
                        	<span style="width: 60%;padding-top:.2rem;">
                        		<select name="billType" id="litType">
                        			<option value="0" selected="selected">仲裁方式(推荐方式)</option>
                        			<option value="1">诉讼</option>
                        		</select>
                        	</span>
                        </p>
                        <p style="border-bottom:none">
                        	<span style="width: 59%;">是否需要对方录制视频</span>
                        	<span class="isVideo isOn" style="margin-top: -.5rem"></span>
                        </p>
                    </div>
                    <a href="javascript:void 0" class="recharge-but removeDis" id="payBtn"> <span>立即支付</span> </a>
                   <form id="dataForm" action="" method="post">
                   		<input type="hidden" name="_header" value="${memberToken}">
                   		<input type="hidden" name="_type" value="h5">
                   		<input type="hidden" id="amount" name="amount" value="${amount}" />
                   		<input type="hidden" id="term" name="term" value="${term}" />
                   		<input type="hidden" id="intRate" name="intRate" value="${intRate}" />
                   		<input type="hidden" id="interest" name="interest" value="${interest}" />
                   		<input type="hidden" id="loanee" name="loanee" value="${loanee.id}" />
                   		<input type="hidden" id="repayType" name="repayType" value="${repayType}" />
                   		<input type="hidden" id="disputeSolveType" name="disputeSolveType" />
	                    <input type="hidden" id="isVideo" name="isVideo" value="0" />
                   </form>
                </div>
            </div>
        </div>
    </section>
</article>
<section class="panel" id="infoPanel" style="width: 100%">
    <div class="page-view theme-red">
        <div class="navbar">
            <div class="navbar-inner">
                <div class="left" style="margin-right:2.7rem;"><a class="link close-popup" href="javascript:void 0" target="_self"><i
                        class="icon icon-back"></i><span></span></a></div>
                <div class="center"><h3 style="font-size: 1.4rem;color: white;" id="sibTtile"></h3></div>
                <div class="right"><a href="javascript:" class="link icon-only"></a></div>
            </div>
        </div>
        <div class="page navbar-fixed">
            <div class="page-content">
                <div class="page-center-middle" style="height: 100%">
                    <div class="preloader"></div>
                </div>
            </div>
        </div>
    </div>
</section>
<script type="text/javascript">
    define('loanPayYXB', ['zepto', 'jumpApp','panel'], function (require, exports, module) {
        var $ = require('zepto'), ev = $.support.touch ? 'tap' : 'click', call = require('jumpApp'),panel=require('panel');
        //调用方法
        var token='${memberToken}', passwordVerify, flag = false;
        var isEnough = ${isEnough};
        var isPayPsw=${isPayPsw};
        var $sub=$('#payBtn'),$litType = $('#litType'),$goDetails=$('#goDetails');
        var errCode = '${errCode}';
        var errString = '${errString}';
        var $labCheck = $('.labCheck'),$isVideo=$('#isVideo');
      	var zcType = $("#litType").val();
        $labCheck[ev](function (e) {
            var that = $(this),tagName=e.target.tagName;
            if(tagName=='SPAN')
                that.hasClass('chekOn') ? (that.removeClass('chekOn'), $sub.removeClass('disabled').addClass('removeDis')) : (that.addClass('chekOn'), $sub.removeClass('removeDis').addClass('disabled'));
            else if(tagName=='A') {
                location.href='/mb/app/agreement.html';
            }
        });
        $('.zcType').live(ev, function () {
        	location.href='/mb/app/arbitrationDetail.html';
        });
        function _openPannl(src) {
            var panel = $('#infoPanel').Panel({
                direction: 'right',
                src: src,
                callback: function () {
                    if (!this.isOpen) {
                        $(this.el).find('.page-content').empty()
                    }
                }
            });
            panel.open();
        }
        $('.isVideo')[ev](function () {
            var that = $(this);
            that.hasClass('isOn') ? (that.removeClass('isOn'),$isVideo.val(1)) : (that.addClass('isOn'),$isVideo.val(0));
        });
        $sub[ev](function () {
        	//检查放款人和借款人好友的好友关系。
        	if(errCode==-50) {
				$.confirm('<p class="withdraw-pass item-password"><label for="withdrawPass">该用户和您不是好友关系，请添加好友后，再进行下一步操作!</label></p>', {
                	callback: function () {
						flag = false;
						$.confirm('<p class="withdraw-pass item-password"><label for="withdrawPass">输入对方知道的验证信息，<br />能更快成为好友哦！</label><p class="infoPut"><input type="text"><i>&nbsp;</i></p></p>',{
							callback:function () {
								//重新添加好友的操作，请求好友添加接口
								var url='${pageContext.request.contextPath}/app/wyjt/friend/againAddFriend';
								var data={'note':$('.infoPut input').val(),'friendId':'${loanee.id}'};
								$.ajax({
									url:url,
									type:"POST",
									headers:{
										"x-memberToken":token,
										"_type":"h5"
									},
									data:data,
									dataType:"json",
									success: function(data){
										if(data.errCode >= 0){
											$.toast('<p class="sendSuccess">发送成功</p><p>不要着急，对方通过后，<br />你们才能成为无忧借条好友哦！</p>');
										}else{
											$.alert(data.errString);
										}
									},
									error:function(data){
										flag = false;
										return !$.toast('网络原因，操作失败。');
									}
								});
							},
							btn:['取消','发送'],
							cls: 'withdraw-pass-dialog',
							onShow:function () {
								$('.infoPut i')[ev](function () {
									$('.infoPut input').val('')
								});
							}
						});
                     },
                     btn: ['取消', '添加'],
                     cls: 'withdraw-pass-dialog'
                });
				return false;
			}else if(errCode == -100){
				$.alert(errString);
				return false;
			}
            $.cookie('YXBLoanTipV', showTips(1));
            return false;
        });
        $('#goDetails')[ev](function () {
        	goDetails();
            return false;
        });
        $('#rechargeBtn')[ev](function () {
            goRecharge();
            return false;
        });
        function showTips(val) {
            var f = val == '1';
            if(f){
                return payFn();
            }
            $.alert('<p class="pop-info">请确认对方是您信任的好友，对方如不按时还款，损失由您本人承担！无忧借条会为您提供有法律效力的电子借据！</p><p class="pop-label"><label for="mys"><input type="checkbox" id="mys"> 下次不再提醒我 </label></p>', {
                cls: 'pop-gray',
                btn: ['知道了'],
                onShow: function (pop) {
                    var $btn = pop.find('.pop-btn'), html = $btn.html(), t = 3;
                    (function () {
                        if (t == 0) {
                            $btn.html(html);
                            pop.removeClass('pop-gray');
                            return;
                        }
                        $btn.html(html + ' (' + t-- + '秒)');
                        setTimeout(arguments.callee, 1e3)
                    })();
                },
                callback: function (pop) {
                    if (pop.hasClass('pop-gray'))  return false;
                    var $p = pop.find('#mys');
                    if ($p.length) $p.prop('checked') ? $.cookie('YXBLoanTipV', "1", 365) : $.cookie('YXBLoanTipV', null, -1);
                    payFn();
                }
            })
        }

        function payFn() {
            if (flag) {
                $.toast('请勿重复提交~！');
                return false;
            }
            flag= true;
	        if (!isEnough) {
	        		flag=false;
	               $.confirm('<p class="withdraw-pass item-password"><label for="withdrawPass">对不起，您的余额不足，<br />请充值后再支付。</label></p>', {
	                            callback: function () {
	                                goRecharge();
	                            },
	                            btn: ['再想想', '去充值'],
	                            cls: 'withdraw-pass-dialog'
	               	});
	        }else {
	        	flag=false;
		       if(isPayPsw){
			        if (!passwordVerify) {
			                require.async(['user/password-verify'], function (pverify) {
					                passwordVerify = pverify.verify({
							                token: token,
							                action: 'checkPwd',
							                pwdType: 1,
							                title: '<span style="line-height: 2.6">输入支付密码</span>',
							                callback: function (code, msg) {
								                var _pwd=this.opts.pwd;
								                flag=false;
								                var loanType = '${loanType}';
								                if (code) {
								                     $.toast('支付申请成功', 1000, function () {
										                   $("#disputeSolveType").val($(litType).val());
												           $("#dataForm").attr("action",'${pageContext.request.contextPath}/app/wyjt/loan/payForLendLoan');
										                   $("#dataForm").submit();
								                      });
								                } else {
								                	$.toast(msg)
								                	};
							                }
					                 })
			                 });
			         }else {
			            passwordVerify.open();
			         }
		       }else{
		            $.alert('为了您的账户安全，请先去设置支付密码。', setPwd);
		            flag=false;
		       }
	      }           
          }
        function goRecharge() {
            var parameter = {"xxx": ""};
            call.callApp('recharge', parameter);
        }

        function goDetails() {
        	var url = '${pageContext.request.contextPath}/app/wyjt/common/loanPeriodDetails?appPlatform=${appPlatform}&isWeiXin=${isWeiXin}';
        	var intRate = '${intRate}';
        	var amount = '${amount}';
        	var loanTerm = '${term}';
        	location.href = url + "&intRate=" + intRate + "&amount=" + amount + "&loanTerm=" + loanTerm;
        }
        
        function setPwd() {
            call.callApp('setpaypass');
        }
    });
    seajs.use(['loanPayYXB', 'jumpApp']);
</script>
</body>
</html>