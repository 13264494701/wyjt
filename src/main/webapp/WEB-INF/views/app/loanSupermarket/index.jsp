<%@ page contentType="text/html;charset=utf-8" language="java"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%@include file="/WEB-INF/views/app/constant.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <title>优放贷</title>
   	<%@include file="/WEB-INF/views/app/meta-flex.jsp" %>
    <meta name="keywords" content=""/>
    <meta name="description" content=""/>
    <link rel="stylesheet" href="<%=httpUrl%>assets/css/public.css?v=<%=version%>" type="text/css">
    <link rel="stylesheet" href="<%=httpUrl%>assets/css/user.css?v=<%=version%>" type="text/css">
    <script src="<%=cdnUrlJS%>assets/scripts/base/sea.js?v=<%=version%>212"></script>
    <script src="<%=cdnUrlJS%>assets/scripts/base/flexible.js?v=<%=version%>"></script>
    <style type="text/css">
        .pop-gray.pop-dialogs .pop-btn {
            
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
            background: #e33336;
            width: 100%;
            height: 42rem;
            background: url("${mbStatic}/assets/images/debt/czhl/preLendingBg.png") -1.15rem 1rem no-repeat;
            background-size:100%;
        }
        .sqjd{
            width: 82%;
            height: 20rem;
            background: white;
            position: relative;
            left: 1.3rem;
            top: 20.5rem;
            margin: 0;
            padding: 0;
            border-radius: .5rem;
            overflow: hidden;
            padding: 0 1rem;
        }
        .sqjdTip{
            position: absolute;
            background:url("${mbStatic}/assets/images/debt/czhl/sqjdTip.png") no-repeat;
            background-size: 80%;
            display: inline-block;
            width: 14rem;
            height: 3rem;
            left: 6.5rem;
        }
        .sqjdTitle{
            text-align: center;
            margin:4rem 0 1rem 0;
            color: rgb(159,159,159);
            font-size: 1.1rem;
        }
        .sqjdTitle span{
            color: rgb(252,87,90);
            font-weight: bold;
            margin-left:.5rem;
        }
        .sqjdProcessBottom{
            background:rgb(255,241,221);
            width: 100%;
            height: .35rem;
            border-radius: 1rem;
            position: relative;
        }
        .sqjdProcessTop{
            background: rgb(252,87,90);
            width: 25%;
            height:.35rem;
            border-radius:1rem;
            display: inline-block;
            position: absolute;
        }
        input::-webkit-input-placeholder {
            color: #dcdcdc;
            font-size:1rem;
        }
        .sqjdTelAndCode input{
            width: 90%;
            height: 2.5rem;
            margin-left: 1rem;
            padding-left:1rem;
            border: 0;
            border-bottom: 1px solid #e1e1e1;
            position: relative;
            font-size: 1rem;
        }
        .sqjdGetCode{
            width: 6rem;
            height: 2rem;
            line-height: 2rem;
            display: inline-block;
            border: 1px solid #fc575a;
            position: absolute;
            right: 0;
            top: .2rem;
            border-radius: .2rem;
            text-decoration: none;
            color: #fc575a;
            text-align: center;
            margin-right: .2rem;
        }
        .sendding{
            border: 1px solid #b3b3b3;
            color: #b4b4b4;
        }
        .sqjdRead{
            height: 2rem;
            margin-top: 1rem;
        }
        .sqjdReadTip{
            font-size:1rem;
            margin-left: 1rem;
            padding-left: 1.5rem;
            background: url("${mbStatic}/assets/images/debt/czhl/agree_off_blue.png") 0rem 0.1rem no-repeat;
            background-size: 1.1rem;
        }
        .readCheckOn{
            background: url("${mbStatic}/assets/images/debt/czhl/agree_on_blue.png") 0rem 0.1rem no-repeat;
            background-size: 1.1rem;
        }
        .sqjdReadName{
            margin-left: .1rem;
            text-decoration: none;
            color: #3f98ff;
            font-size: 1rem;
            letter-spacing: .1rem;
        }
        .sqjdSub{
            width: 100%;
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
            pointer-events:none;
            margin-top:.5rem;
        }
        .sqjdSubCheckOn{
            background-image: linear-gradient(-14deg,rgba(255, 44, 44, 0.74) 0%, rgba(255, 153, 20, 0.74) 99%),
	            linear-gradient(#faaf00,#faaf00);
            pointer-events:auto;
        }
        .notice{
            background: url("${mbStatic}/assets/images/debt/czhl/agree_off_blue.png") 1rem 0.24rem no-repeat;
            background-size: 1.1rem;
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
            <div class="page" style="background: #e33336;overflow: scroll;">
                <form id="form" method="post" action="otherLoan.jsp" >
                    <div class="radius" style="">
                    <div class="sqjd">
                        <i class="sqjdTip"></i>
                        <p class="sqjdTitle">今日申请名额还剩<span></span></p>
                        <div style="width: 94%;padding-left: 1rem;">
                            <p class="sqjdProcessBottom">
                                <i class="sqjdProcessTop"></i>
                            </p>
                        </div>
                        <p class="sqjdTelAndCode">
                            <input type="tel"  placeholder="请输入您的手机号" name="userTel">
                        </p>
                        <p class="sqjdTelAndCode" style="position: relative">
                            <input type="tel"  placeholder="请输入验证码" name="telCode">
                            <a href="javascript:void 0;" class="sqjdGetCode">获取验证码</a>
                        </p>
                        <p class="sqjdRead"><span class="sqjdReadTip">阅读并同意</span><a href="notification.jsp" class="sqjdReadName" >《借款知情告知书》</a></p>
                        <a class="sqjdSub">立即申请</a>
                    </div>
                </div>
                </form>
            </div>
        </div>
    </section>
</article>

<script type="text/javascript">
    window.panel=null;
    define('preLending', ['zepto','panel'], function (require, exports, module) {
        var $ = require('zepto'),panel=require('panel'),md5;
        var ev = $.support.touch ? 'tap' : 'click',$sqjdGetCode=$('.sqjdGetCode'),$sqjdRead=$('.sqjdReadTip'),$subBtn=$('.sqjdSub'),time=5,flag=false,subFlag=false;

         /***********以下是留给后台的数据接口*****************开始************/
        /*
        *       以下变量在静态页面下由前端赋值，后需要由后台赋值；
        *       theHours---------------------由后台赋予当前的终点，按照24小时制计；如16点
        *       getVerifyCodeUrl-------------由后台提供的获取验证码的接口
        *       subActionUrl-----------------由后台提供的提交申请的接口
        * */
        theHours=new Date().getHours();
        getVerifyCodeUrl='index.jsp';
        subActionUrl='index.jsp'
        /***********以下是留给后台的数据接口*****************结束************/



        function getProcess() {
            var randomNumber=0,pressArr=[[100,90],[90,70],[70,40],[40,15],[15,5],[5,0]],temArr=[];
            if(0<=theHours && theHours<6) temArr=pressArr[0];
            else if(6<=theHours && theHours<10) temArr=pressArr[1];
            else if(10<=theHours && theHours<14) temArr=pressArr[2];
            else if(14<=theHours && theHours<18) temArr=pressArr[3];
            else if(18<=theHours && theHours<22) temArr=pressArr[4];
            else if(22<=theHours && theHours<24) temArr=pressArr[5];
            randomNumber=parseInt(Math.random()*(temArr[0]-temArr[1]+1)+temArr[1],10);
            $('.sqjdProcessTop').css('width',randomNumber+'%');
            $('.sqjdTitle').html(Number(100-randomNumber)==0?'今日申请名额已满':('今日申请名额还剩<span>'+Number(100-randomNumber)+'%</span>'));
        }
        getProcess();

        $sqjdGetCode[ev](function () {
            var _this=$(this),phoneNum=13810772717,str='';
            str = checking();
            if(str) return !!$.toast(str);
            else if(!flag) getCodeCount(_this);
            return
            if(!flag){
                $.ipostJSON(getVerifyCodeUrl, {'mobile': phoneNum}, function (error, rel) {
                    if (!error) {
                        if (rel.success) {
                            //
                        }
                        else {
                            pop.alert('' + rel['errStr']);
                        }
                    } else pop.alert('网络原因提交失败。');
                });
                return;
            }
        });
        $sqjdRead[ev](function () {
            var _this=$(this);
            if(!_this.hasClass('readCheckOn')) _this.addClass('readCheckOn'),$subBtn.addClass('sqjdSubCheckOn');
            else _this.removeClass('readCheckOn'),$subBtn.removeClass('sqjdSubCheckOn');
        });
        $subBtn[ev](function () {
             var _this=$(this),phoneNum=13810772717,str='';
            str = checking(1);
            if(str) return !!$.toast(str);
            if(subFlag) return !!_toast('请不要重复提交申请');;
            if(!subFlag){
                subFlag=true;
				$.post(subActionUrl, {'mobile': phoneNum}, function (response) {
					//跳转至贷款超市
					subFlag=false;

				})
            }
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
            var str='',$userTel=$('input[name="userTel"]'),$telCode=$('input[name="telCode"]');
            if(!$.trim($telCode.val()) && bol == true) str='请输入验证码';
            if(!$.trim($userTel.val())) str='请输入您的手机';
            else if(!/^((\+?[0-9]{1,4})|(\(\+86\)))?(13[0-9]|14[57]|15[012356789]|17[012356789]|18[0-9])\d{8}$/.test($.trim($userTel.val()))){
                    str='请输入正确的手机号码';
            }
            return str;
        }
    });
    seajs.use(['preLending']);
</script>
</body>
</html>
