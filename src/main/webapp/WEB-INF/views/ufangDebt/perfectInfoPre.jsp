<%@ page contentType="text/html;charset=utf-8" language="java"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%@ include file="/WEB-INF/views/app/constant.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <title>完善信息</title>
    <meta charset="utf-8">
    <meta name="keywords" content=""/>
    <meta name="description" content=""/>
	<link rel="stylesheet" href="${mbStatic}/assets/css/public.css?v=<%=version%>" type="text/css">
    <link rel="stylesheet" href="${mbStatic}/ufangDebt/css/loanSMarket.css" type="text/css">
    <script src="${mbStatic}/assets/scripts/base/sea.js?v=<%=version%>212"></script>
    <script src="${mbStatic}/assets/scripts/base/flexible.js?v=<%=version%>"></script>
    <script src="${mbStatic}/ufangDebt/js/zepto.min.js?v=8143"></script>
</head>
<body>
<article class="views">
    <section class="view">
        <div class="pages navbar-fixed toolbar-fixed">
            <div class="page">
                <form id="form" method="post" action="">
                    <div class="wsxx"><i></i></div>
                    <div class="wsxxContent">
                        <i class="wsxxTip"></i>
                         <input type="hidden" id="applyerId" name="applyerId" value="${applyer.id}">
                          <input type="hidden" id="phoneNo" name="phoneNo" value="${applyer.phoneNo}">
                        <p style="margin-top: 3.5rem">
                            <span>姓名</span><input type="text" placeholder="请输入您的姓名（必填）" name="userName">
                        </p>
                        <p>
                            <span>身份证号</span><input type="text" placeholder="请输入您的身份证号（必填）" name="userId">
                        </p>
                        <p>
                            <span>银行卡号</span><input type="text" placeholder="请输入您的银行卡号（必填）" name="userBankCode">
                        </p>
                        <p>
                           <span>微信号</span> <input type="text" placeholder="请输入您的微信号（选填）" name="weixinNo">
                        </p>
                        <div class="remind">
                            <b>注意</b>
                            <div>您的信息仅用于放款审核，贷款过程中不会向您收取任何费用！</div>
                        </div>
                        <a class="wsxxSub orange">下一步</a>
                    </div>
                </form>
            </div>
        </div>
    </section>
</article>

<script type="text/javascript">
	window.panel=null;
	define('otherLoan', ['zepto', 'pop','panel','jumpApp'], function (require, exports, module) {
	    var $ = require('zepto'), pop = require('pop'),call = require('jumpApp'),panel=require('panel'),md5;
	    var ev = $.support.touch ? 'tap' : 'click',$wsxxSub=$('.wsxxSub'),flag=false;
		var subMitUrl = "${pageContext.request.contextPath}/wap/wyjt/ufangDebt/perfectInfo";		
	    $wsxxSub[ev](function () {
	        var userName=$('input[name="userName"]').val(),userId=$('input[name="userId"]').val(),userBankCode=$('input[name="userBankCode"]').val();
	    	var phoneNo = '${applyer.phoneNo}';
	        if(!flag){
	            pop.confirm('确认提交吗？',function () {
	            	//四要素认证
	            	var renzhengUrl = "${pageContext.request.contextPath}/wap/wyjt/ufangDebt/siYaoSuRenzheng";
	            	var data={'phoneNo':phoneNo,'realName':userName,'cardNo':userBankCode,'idNo':userId};
	    			$.ajax({
	    				url:renzhengUrl,
	    				type:"POST",
	    				data:data,
	    				dataType:"json",
	    				success: function(data){
	    					if(data.code >= 0){
								//四要素验证成功 提交表单
								$("#form").attr("action",subMitUrl);
								$("#form").submit();
	    					}else{
	    						$.toast(data.message);
	    					}
	    				},
	    				error:function(data){
	    					return !$.toast('网络原因，操作失败。');
	    				}
	    			});
	            return;
	        })
	       }
	   });
	    $('input').on('input',function () {
	        console.log(222);
	        var str='';
	        str = checking();
	        if(!str) $wsxxSub.addClass('verfied');
	        else $wsxxSub.removeClass('verfied')
	    });
	    function checking() {
	        var str='',$userName=$('input[name="userName"]'),$userId=$('input[name="userId"]'),$userBankCode=$('input[name="userBankCode"]');
	        if(!$.trim($userBankCode.val())) str='请输入银行卡号';
	        else if($userBankCode.val()!='' && ($userBankCode.val().length <16 || $userBankCode.val().length >19)) str='请输入正确的银行卡号';
	        if(!$.trim($userId.val())) str='请输入您的身份证号';
	        else if(!/^\d{6}(18|19|20)?\d{2}(0[1-9]|1[012])(0[1-9]|[12]\d|3[01])\d{3}(\d|[xX])$/.test($.trim($userId.val()))){
	                str='请输入正确的身份证号';
	        }
	        if(!$.trim($userName.val())) str='请输入您的姓名';
	        return str;
	    }
	});
	seajs.use(['otherLoan']);
</script>
</body>
</html>