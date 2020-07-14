<%@ page contentType="text/html;charset=utf-8" language="java"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%@include file="/WEB-INF/views/app/constant.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <title>完善信息</title>
    <%@include file="/WEB-INF/views/app/meta-flex.jsp" %>
    <meta name="keywords" content=""/>
    <meta name="description" content=""/>
    <link rel="stylesheet" href="${mbStatic}/assets/css/public.css?v=<%=version%>" type="text/css">
    <link rel="stylesheet" href="${mbStatic}/assets/css/user.css?v=<%=version%>" type="text/css">
    <script src="${mbStatic}/assets/scripts/base/sea.js?v=<%=version%>21"></script>
    <script src="${mbStatic}/assets/scripts/base/flexible.js?v=<%=version%>"></script>
    <style type="text/css">
        .pop-gray.pop-dialogs .pop-btn {
            
        }

        .pop-info {
            margin-top: 1rem;
        }
        .wsxx{
            width: 100%;
            height: 12rem;
            background: #e33336;
        }
        .wsxx i{
            width: 25rem;
            height: 7rem;
            display: inline-block;
            background: url(${mbStatic}/assets/images/debt/czhl/wsxxSpeed.png) 3rem 3rem no-repeat;
            background-size: 83%;
        }
        .wsxxContent{
            width: 90%;
            height: 31rem;
            /*border: 1px red solid;*/
            position: absolute;
            left: 1.3rem;
            top: 8rem;
            background: white;
            border-radius: .5rem;
        }
        .wsxxTip{
            position: absolute;
            background:url("${mbStatic}/assets/images/debt/czhl/wsxxTip.png") no-repeat;
            background-size: 100%;
            display: inline-block;
            width: 10rem;
            height: 3rem;
            left: 6.5rem;
        }
        input::-webkit-input-placeholder {
            color: #dcdcdc;
            font-size:1rem;
        }
        .wsxxContent p{
            width: 82%;
            height: 4rem;
            line-height: 4rem;
            padding-left: .5rem;
            margin-left: 2rem;
            font-size: 1.1rem;
            border-bottom: 1px solid rgb(225,225,225);
        }
        .wsxxContent span{
            width: 5rem;
            height: 3rem;
            line-height: 3rem;
            display: inline-block;
        }
        .wsxxContent input{
            text-align: left;
            width: 100%;
            height: 3rem;
            border: none;
        }
        .remind{
            width: 86%;
            margin: 1rem;
        }
        .remind b{
            height: 2rem;
            display: inline-block;
            background: url(${mbStatic}/assets/images/debt/prompt@2x.png) 0rem .2rem no-repeat;
            background-size: 1.3rem;
            padding-left: 1.8rem;
            font-size: 1rem;
            color: red;
            margin-top: .5rem;
        }
        .remind div{
            width: 96%;
            margin-left: 1.8rem;
            color: #7b6d3d;
        }
        .wsxxSub{
            width: 92%;
            height: 3rem;
            background: #cbcbcb;
            text-align: center;
            color: #ffffff;
            display: inline-block;
            border-radius: .5rem;
            letter-spacing:.1rem;
            font-size: 1.3rem;
            font-family: HiraginoSansGB-W6;
            line-height: 3rem;
            margin-top:.5rem;
            margin-left: 1rem;
            pointer-events:none;
        }
        .verfied{
            background-image: linear-gradient(-14deg,rgba(255, 44, 44, 0.74) 0%, rgba(255, 153, 20, 0.74) 99%),
	            linear-gradient(#faaf00,#faaf00)!important;
            pointer-events:auto;
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
                <form id="form" method="post" action="otherLoan.jsp" >
                    <div class="wsxx"><i></i></div>
                    <div class="wsxxContent">
                        <i class="wsxxTip"></i>
                        <p style="margin-top: 3.5rem">
                            <input type="text" placeholder="请输入您的姓名" name="userName">
                        </p>
                        <p>
                            <input type="text" placeholder="请输入您的身份证号" name="userId">
                        </p>
                        <p>
                            <input type="text" placeholder="请输入您的银行卡号" name="userBankCode">
                        </p>
                        <div class="remind">
                            <b>注意</b>
                            <div>为了尽快审核通过，请填写您的真实信息，您的信息仅用放款审核，贷款过程中不会向您收取任何费用！</div>
                        </div>
                        <a class="wsxxSub ">下一步</a>
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

        $wsxxSub[ev](function () {
            var userName=$('input[name="userName"]').val(),userId=$('input[name="userId"]').val(),userBankCode=$('input[name="userBankCode"]').val();
            if(!flag){
                pop.confirm('确认提交吗？',function () {
                    location.href='perfectInfo.jsp?userName='+userName+'&userId='+userId+'&userBankCode='+userBankCode;
                    return;
                    $.ipostJSON('/wap/h5/cxdy/api.jsp?action=', {'userName': userName,"userId":userId,"userBankCode":userBankCode}, function (error, rel) {
                        if (!error) {
                            if (rel.success) {
                                //
                                pop.alert('操作成功~！',function () {
                                    location.href='perfectInfo.jsp?userName='+userName+'&userId='+userId+'&userBankCode='+userBankCode;
                                })
                            }
                            else {
                                pop.alert('' + rel['errStr']);
                            }
                        } else pop.alert('网络原因提交失败。');
                    });
                });
                return;
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
