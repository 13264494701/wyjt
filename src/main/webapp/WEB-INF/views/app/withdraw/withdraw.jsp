<%@ page contentType="text/html;charset=utf-8" language="java"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%@include file="/WEB-INF/views/app/constant.jsp" %>
<!DOCTYPE html>
<html xmlns=http://www.w3.org/1999/xhtml>
<head>
  	<title>提现</title>
    <%@include file="/WEB-INF/views/app/meta-flex.jsp" %>
    <meta name="keywords" content=""/>
    <meta name="description" content=""/>
    <link rel="stylesheet" href="${mbStatic}/assets/css/public.css?v=<%=version%>12" type="text/css">
    <link rel="stylesheet" href="${mbStatic}/assets/css/user.css?v=<%=version%>12" type="text/css">
    <script src="${mbStatic}/assets/scripts/base/sea.js?v=<%=version%>12"></script>
    <script src="${mbStatic}/assets/scripts/base/flexible.js?v=<%=version%>"></script>
    <style>
        .txgz p {
            color: #303030;
        }

        .txgz p:last-child {
            margin-left: 0
        }
        .recharge-ts table{
            font-size: 1.2rem;
            line-height: 2.5rem;
        }
        .recharge-ts{
            padding: 1.66667rem 0.83333rem 0;
        }
        .recharge-ts p:first-child{
            color: red;
            float: none;
            font-size: 1.2rem;
            font-weight: bold;
            color: black;
        }
        table{
            width: 95%;
            margin: 2rem 0 0rem 1.5rem;
            text-align: center;
            border-right: 1px solid #ccc;
            border-bottom: 1px solid #ccc;
            background: RGB(237,237,237)
        }
        table td{
            border-left: 1px solid #ccc;
            border-top: 1px solid #ccc;
            border-radius: 14px;
            font-size: 1rem;
            text-align: left;
            padding: .32rem;
            line-height: 1.5rem;
            color: RGB(153,153,153);
            width: ;
        }
        table tr:first-child td{
            text-align: center;
            font-weight: bold;
            color: #474747;
        }
        .red{
            color: RGB(252,8,8);
        }
        .remTitle{
            font-size: 1.1rem;
            font-weight: bold;
            color: RGB(252,8,8);
        }
    </style>
    <style>
        .recharge-jine{
            border-top:none;
            border-bottom:none;
        }
        .recharge-jine p{
            margin: 0;
            padding: 0;
            font-size: 1.1333rem;
        }
        .disabled {
            pointer-events: none;
            cursor: default;
            opacity: 0.2;
        }
        .removeDis{
            background-color: #E71B1B !important;
        }
        .recharge-but{
            background-color: #666;
            margin-top:.5rem;
        }
        .inputForce{
            font-weight: bold;
        }
        .circle{
            color: red;
            padding: 1.25rem 0.83333rem 0;
            font-size: 1.1rem;
        }
        .circle img{
            width: 1.2rem;
            line-height: 1.2rem;
            display: inline-block;
            margin-right:.5rem;
            margin-bottom: .5rem;
        }
        .textIndex{
            margin-left: 1.8rem;
            padding-top: 0;
            color: RGB(153,153,153);
        }
        .inputBox{
            padding-bottom:.5rem;
            position: relative;
        }
        .inputTitle{
            display: inline-block;
            width: 1rem;
            position: absolute;
            left: .2rem;
            font-size: 1.85rem;
        }
        .recharge-jine{
            padding:.25rem 0.83333rem 0;
        }
        .recharge-jine p .con-input{
            text-align: left;
            color:black;
            padding-left: 1.758rem;
            width: 100%;
            border: none;
            border-bottom: 1px solid #cecece;
            border-radius:inherit;
        }
        .toolbar-fixed .page-content{
            padding-bottom: 3.66667rem;
        }
        .user-main{
            background:white;
        }
        .recharge-name p:last-child{
            padding-bottom:0;
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
        <jsp:include page="/WEB-INF/views/app/header-public.jsp">
            <jsp:param name="title" value="提现"></jsp:param>
            <jsp:param name="url" value="back2app"></jsp:param>
            <jsp:param name="type" value="tx_withdrawList"></jsp:param>
        </jsp:include>
        <div class="pages navbar-fixed toolbar-fixed">
            <div class="page">
                <div class="page-content user-main withdraw-main">
                	<form id="tokenForm" action=""	method="post">
                		<input type="hidden" id="money" name="money" />
                		<input type="hidden" name="_header" value="${memberToken}"/>
                		<input type="hidden" name="_type" value="h5"/>
                	</form>
                    <div class="recharge-jine">
                        <p>提现金额</p>
                        <p  class="inputBox"><span  class="inputTitle">¥</span> <input type="tel" placeholder="最少4元" class="con-input" id="amount" maxlength="10"> </p>
                        <div class="recharge-name addcard-name clearfix">
                            <p class="userBalance">账户余额：<span class="money" id="balance">${curBal}</span>&nbsp;元</p>
                        </div>
                    </div>
                    <input type="hidden" id="utk" value="${memberToken}"> <a href="javascript:void 0" class="recharge-but removeDis" id="conformWithdraw"> <span>下一步</span> </a>
                    <p class="circle" style="background: url(${mbStatic}/assets/images/debt/prompt@2x.png) 0.8rem 1.5rem no-repeat;background-size:1.3rem;padding-left: 2.5rem;padding-bottom: .5rem;">温馨提示</p>
                    <p class="circle textIndex">您在本站绑定提交的信息如果与您实际身份不符，将导致无法提现，并扣除${withdrawSxf}元手续费。</p>
                    <div class="recharge-ts clearfix txgz">
                        <p >注：</p>
                        <p class="remTitle" style="margin: .7rem 0;">①&nbsp;&nbsp;提现时间说明:</p>
                        <table style="">
                            <tr>
                                <td style="width: 8.5rem;">时间</td>
                                <td>金额限制</td>
                            </tr>
                            <tr>
                                <td style="text-align: center;">09:30--17:30</td>
                                <td class="content">(提现金额不受限制。)</td>
                            </tr>
                            <tr>
                                <td style="text-align: center;">17:30--09:30(<span class="red">次日</span>)</td>
                                <td class="content">(单笔提现限额<span class="red">&le;3万</span>,累计提现限额<span class="red">&le;3万</span>)</td>
                            </tr>
                        </table>
                        <p class="remTitle" style="margin: .4rem 0;">②&nbsp;&nbsp;到账时间:</p>
                        <p  style="margin: 0;padding-left: 1.5rem;color: RGB(153,153,153)">预计2小时内到账，实际到账时间依据银行而有所差别。特殊情况未到账，请联系本站在线客服人员。</p>
                        <p  class="remTitle" style="margin: .7rem 0;">③&nbsp;&nbsp;手续费每笔<span class="red">${withdrawSxf}</span>元。</p>
                    </div>
                </div>
            </div>
        </div>
    </section>
</article>
</body>
<script>
    define('withdraw', ['zepto', 'pop','jumpApp'], function (require, exports, module) {
        var $ = require('zepto'), pop = require('pop'),call = require('jumpApp');
        var ev = $.support.touch ? 'tap' : 'click';
        var bankFlag=${bankFlag};
        var canWithdraw = ${canWithdraw};
        var locked=${locked};
        var balance=$('#balance').html(),$subBtn=$('#conformWithdraw'),$recordBtn=$('#recordBtn');
        var $error = $('.tips em'), errorArr = ['提款额不能大于账户余额。', '提款额≤100元，需自行支付手续费，每笔2元。', '提款额>100元，每日第1笔可免支付手续费。','提现金额不能大于50万元。','请输入提现金额','提现金额最少4元'];

        $("#amount").bind('input propertychange', function () {
            _check();
        });
        $subBtn[ev](function () {
          var amount = $("#amount").val(),str='';
          if(!canWithdraw){
        	  $.alert('<p class="pop-info">您有借条逾期尚未还款，</p><p class="pop-label">需还款后再申请提现！</p>',
                      {
                          btn: ['知道了']
                      }
              );
              return false;
          };
          if(locked){
              $.alert('<p class="pop-info">您的账户被锁定，</p><p class="pop-label">不能提现！</p>',
                      {
                          btn: ['知道了']
                      }
              );
              return false;
          };
          if(!bankFlag){
              return !!pop.pop({
                  isClose:!1,
                  msg:'<p style=" font-weight: bold;font-size: 1.23rem;height: 4rem;line-height: 4rem;">请先在安全中心绑定银行卡信息</p>',
                  width:'20rem',
                  btns: [
                      {
                          txt: '再看看',
                          fn: function () {
                              this.close()
                          }
                      }
              ]});
          };
          str = _check();
          if(str!='') return !!pop.alert(str);
          var para={'money':amount};
          $.ajax({
              url: '${pageContext.request.contextPath}/app/wyjt/member/checkMoney.do',
              type: 'POST',
              headers: {
                  'x-memberToken': '${memberToken}',
                  "_type":"h5"
              },
              data: para,
              dataType: 'json',
              success: function (ret) { 
                  if (ret['success']) {  
                      $("#money").val(amount);
                      $("#tokenForm").attr("action","${pageContext.request.contextPath}/app/wyjt/member/confirmCash");
                      $("#tokenForm").submit();
                  }
                  else {
                      if(ret['errStr'] && ret['errStr'].indexOf('提现金额超出限制')>-1){
                          return !!pop.pop({
                              isClose:!1,
                              msg:'<p style=" font-weight: bold">您当前的提现金额超出限制,<br />请重新输入金额！</p><p class="red" style="font-size: .8rem;margin-top:.2rem;">注：每日17:30--次日9:30</p><p class="red" style="font-size: .8rem;">单笔提现&le;3万，累计提现限额&le;3万</p>',
                              btns: [
                                  {
                                      txt: '知道了',
                                      fn: function () {
                                          this.close()
                                      }
                                  }
                          ]});
                      }
                      else  $.toast(ret['errStr']);
                  }
              },
              error: function () {
                  $.toast('服务器开小差，请稍后重试');
              }
          });
      });
       $recordBtn[ev](function () {
    	   $("#tokenForm").attr("action","${pageContext.request.contextPath}/app/wyjt/member/withdrawList");
           $("#tokenForm").submit();
       });
      function _check() {
    	  var withdrawSxf = '${withdrawSxf}';
      	  var minAmount = '4';
          var _this=$("#amount"),amount = _this.val(),str='';
//          ((amount != '') && (amount*1 >= 1))?(_this.addClass('inputForce'),$subBtn.removeClass('disabled').addClass('removeDis')):(_this.val(''),_this.removeClass('inputForce'),$subBtn.addClass('disabled').removeClass('removeDis'));
          if(amount==''){
              str =errorArr[4]
          }
          else if (!(/^[0-9]+.?[0-9]*$/.test(amount))) {
              $error.html(errorArr[0]);
          } else if(parseFloat(amount)>parseFloat(balance)) _this.val(parseFloat(balance));
          else if(amount*1 < minAmount)  str = '提现金额最小' + minAmount + '元'//_this.val(Math.round( amount * 1*100)/100),;
          else if(amount*1 > 500000) str = errorArr[3];
          else{
              if(amount.indexOf('.')>-1){
                  var valArr=amount.split('.');
                  if(valArr[1].length>2){
                      _this.val(valArr[0]+'.'+valArr[1].substring(0,2));
                  }
              }
          }
          return str;
      }
  });
  seajs.use(['withdraw']);
</script>
</html>
