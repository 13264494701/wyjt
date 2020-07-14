<%@ page contentType="text/html;charset=utf-8" language="java"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%@ include file="/WEB-INF/views/app/constant.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <title>优放超市，为您急速贷款</title>
    <meta charset="utf-8">
    <meta name="keywords" content=""/>
    <meta name="description" content=""/>
    <link rel="stylesheet" href="${mbStatic}/ufangDebt/css/loanSMarket.css?v=<%=version%>" type="text/css">
    <link rel="stylesheet" href="${mbStatic}/assets/css/public.css?v=<%=version%>" type="text/css">
      <script src="${mbStatic}/assets/scripts/base/sea.js?v=<%=version%>212"></script>
    <script src="${mbStatic}/assets/scripts/base/flexible.js?v=<%=version%>"></script>
    <script src="${mbStatic}/ufangDebt/js/zepto.min.js?v=8143"></script>
</head>
<body>
<div class="bg" style="">
    <p class="tpHead"></p>
	    <form id="form" method="post" action="" >
		    <div class="tpCenter">
		        <p class="sqjdTelAndCode">
		            <input type="tel" placeholder="请输入您的手机号" name="userTel" style="border-radius: 2rem;">
		        </p>
		        <p class="sqjdTelAndCode" style="position: relative">
		            <input type="tel" placeholder="请输入验证码" name="telCode" class="lastInput"  style="border-radius: 2rem;">
		            <a href="javascript:void 0;" class="sqjdGetCode">获取验证码</a>
		        </p>
		        <p class="sqjdRead"><span class="sqjdReadTip">阅读并同意</span><a href="javascript:void 0;" class="sqjdReadName">《借款知情告知书》</a>
		        </p>
		        <a class="sqjdSub">免费申请</a>
		    </div>
	    </form>
    <p class="tpFoot"></p>
</div>
</body>
<script type="text/javascript">
	window.panel=null;
	define('preLending', ['zepto','panel'], function (require, exports, module) {
	    var $ = require('zepto'),panel=require('panel'),md5;
	    var ev = $.support.touch ? 'tap' : 'click',$sqjdGetCode=$('.sqjdGetCode'),$sqjdRead=$('.sqjdReadTip'),$subBtn=$('.sqjdSub'),time=60,flag=false,subFlag=false;
	    var $userTel=$('input[name="userTel"]'),$readNotice = $(".sqjdReadName"),$telCode=$('input[name="telCode"]');
	    var verified = false;
	    getVerifyCodeUrl='${pageContext.request.contextPath}/app/wyjt/common/sendSmsVerifyCodeForWeb';
	    subActionUrl='${pageContext.request.contextPath}/wap/wyjt/ufangDebt/applyPre'
	    /***********以下是留给后台的数据接口*****************结束************/
		$readNotice[ev](function (){
			location.href = "https://www.51jt.com/mb/ufangDebt/notification.html";			
		});	
	    $sqjdGetCode[ev](function () {
	        var _this=$(this),phoneNo=$userTel.val(),str='';
	        str = checking();
	        if(str) return !!$.toast(str);
	        else if(!flag) getCodeCount(_this);
	        //return
	        if(!flag){
	        		flag = false;
	            	var tmplCode = "wyjtUfangDebtApply";
	            	var url='${pageContext.request.contextPath}/app/wyjt/common/sendSmsVerifyCodeForWeb';
	    			var data={'phoneNo':$userTel.val(),'tmplCode':tmplCode};
	    			$.ajax({
	    				url:url,
	    				type:"POST",
	    				data:data,
	    				dataType:"json",
	    				success: function(data){
	    					if(data.code >= 0){
	    						$.toast("短信验证码已发送成功，请注意查收！");
	    					}else{
	    						$.toast(data.message);
	    					}
	    				},
	    				error:function(data){
	    					return !$.toast('网络原因，操作失败。');
	    				}
	    			});
	                return;
	        	}else{
	        		$.toast('验证码正在发送中，请勿重复提交');
	        	}
	    });
	    
	    $sqjdRead[ev](function () {
	        var _this=$(this);
	        if(!_this.hasClass('readCheckOn')) _this.addClass('readCheckOn'),$subBtn.addClass('sqjdSubCheckOn');
	        else _this.removeClass('readCheckOn'),$subBtn.removeClass('sqjdSubCheckOn');
	    });
	    $subBtn[ev](function () {
	         var _this=$(this),phoneNum=$userTel.val(),str='';
	        str = checking(1);
	        if(str) return !!$.toast(str);
	        if(subFlag) return !!$.toast('请不要重复提交申请');
	       	var smsCode = $telCode.val();
	    	//验证短信验证码
			var url='${pageContext.request.contextPath}/app/wyjt/common/checkSmsCodeForWeb';
			var data={'phoneNo':$userTel.val(),'smsCode':$telCode.val()};
			$.ajax({
				url:url,
				type:"POST",
				data:data,
				dataType:"json",
				success: function(data){
					if(data.code >= 0){
						if(!subFlag){
			                subFlag=true;
							/* $.post(subActionUrl, {'phoneNo': phoneNum}, function (response) {
								//跳转至贷款超市
								subFlag=false;
							}) */
							$("#form").attr("action",subActionUrl);
							$("#form").submit();
			            }
					}else{
						
						 $.toast(data.message);
					}
				},
				error:function(data){
					return !$.toast('网络原因，操作失败。');
				}
			});
	        
	    });
	    function getCodeCount($obj) {
	        var countTime = time || 60;
	        var timeInter = null, _this = $obj;
	        timeInter = setInterval(function () {
	            countTime--,flag=true;
	            if (countTime < 0) {
	                _this.removeClass('sendding').text('获取验证码');
	                clearInterval(timeInter),flag=false;
	            } else {
	                _this.addClass('sendding').text('剩余' + countTime + '秒');
	            }
	        }, 1000);
	    }
	    function checking(bol) {
	        var str='';
	        if(!$.trim($telCode.val()) && bol == true) str='请输入验证码';
	        if(!$.trim($userTel.val())) str='请输入您的手机';
	        else if(!/^1\d{10}$/.test($.trim($userTel.val()))){
	                str='请输入正确的手机号码';
	        }
	        return str;
	    }
	});
	seajs.use(['preLending']);
</script>
</html>