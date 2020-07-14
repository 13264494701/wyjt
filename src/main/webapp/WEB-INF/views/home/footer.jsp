<%@ page contentType="text/html;charset=utf-8" language="java"%>
<!--尾部开始-->
<div class="footerbg">
    <div class="footer">
        <div class="foot-top">
            <a><img src="${homeStatic}/assets/images/footimg_01.png"> </a> <a><img src="${homeStatic}/assets/images/footimg_02.png"> </a>
            <a><img src="${homeStatic}/assets/images/footimg_03.png"> </a> <a><img src="${homeStatic}/assets/images/footimg_04.png"> </a>
            <a><img src="${homeStatic}/assets/images/footimg_05.png"> </a><a><img src="${homeStatic}/assets/images/footimg_06.png"> </a>
        </div>
        <div class="foot-con">
            <ul>
                <li>
                    <a class="num1">业务服务</a>
                    <a href="javascript:;" onclick="hover()">大额充值</a>
                    <a href="javascript:;" onclick="loan_list()">我的电子借条</a>
                    <a href="javascript:;" onclick="loan_caseList()">下载诉讼材料</a>
                </li>
                <li>
                    <a class="num1">新手帮助</a>
                    <a href="javascript:;" class="h-charge">充值提现</a>
                    <%--<a href="/home/helpcenter/help-con1.jsp?v=hyjk">好友借款</a>--%>
                    <a href="javascript:;" class="csfw">催收服务</a>
                    <a href="javascript:;" class="zhzc">账号注册</a>
                    <a href="javascript:;" class="sfrz">身份认证</a>
                    <a href="javascript:;" class="cjwt">常见问题</a>
                    <a href="javascript:;" class="aqbz">安全保障</a>
                </li>
                <li>
                    <a class="num1">关于我们</a>
                    <a href="javascript:;" class="intro">公司简介</a>
                    <%--<a href="/home/aboutUs/contact.jsp">联系我们</a>--%>
                    <a href="javascript:;" class="join">加入我们</a>
                    <a href="javascript:;" class="cooperation">战略合作</a>
                    <a href="javascript:;" class="third_pf">第三方平台</a>
                    <a href="javascript:;" class="agreement">用户协议</a>
                </li>
                <li>
               <!--  http://www.51jt.com/app/go4ios.jsp
                http://www.51jt.com/app/go4android.jsp -->
                    <a class="num1">APP下载</a>
                    <a href="javascript:;">苹果下载</a>
                    <a href="javascript:;">安卓下载</a>
                </li>
                <li class="tel"><p class="telphone">400-6688-658</p>

                    <p>（周一到周日：9:00-19:00）</p></li>
            </ul>
        </div>
        <div class="foot-address">
            <p class="bh">“Copyright@2014-2015 无忧借条 All right reserved 京ICP备15027773号    京ICP证 京B2-20170257号   <a target="_blank" href="http://www.beian.gov.cn/portal/registerSystemInfo?recordcode=11010502030732" style="display:inline-block;text-decoration:none;height:20px;line-height:20px;*display: inline;zoom:1"><img src="${homeStatic}/assets/images/beian.png"/> 京公网安备11010502030732号</a>”</p>

            <%--<p>公司名称：北京友信宝网络科技有限公司</p>--%>

            <%--<p>办公地址：北京市，朝阳区，望京街，望京SOHO，塔2，C区18层1807室。</p>--%>

            <p>客服热线：400-6688-658</p>

        </div>
    </div>
</div>
<!--尾部结束--========================================-->
<script>
var _hmt = _hmt || [];
(function() {
  var hm = document.createElement("script");
  hm.src = "//hm.baidu.com/hm.js?dedc7acab9e723effc2782a2a1d1d448";
  var s = document.getElementsByTagName("script")[0];
  s.parentNode.insertBefore(hm, s);
})();
  (function(i,s,o,g,r,a,m){i['GoogleAnalyticsObject']=r;i[r]=i[r]||function(){
  (i[r].q=i[r].q||[]).push(arguments)},i[r].l=1*new Date();a=s.createElement(o),
  m=s.getElementsByTagName(o)[0];a.async=1;a.src=g;m.parentNode.insertBefore(a,m)
  })(window,document,'script','https://www.google-analytics.com/analytics.js','ga');

  ga('create', 'UA-77652937-1', 'auto');
  ga('send', 'pageview');

$(".h-charge").on("click",function(){
	window.location.href="${home}/charge";
});
$(".csfw").on("click",function(){
	var str = "csfw";
	window.location.href="${home}/help_con1?str="+str;
});
$(".zhzc").on("click",function(){
	var str = "zhzc";
	window.location.href="${home}/help_con1?str="+str;
});
$(".sfrz").on("click",function(){
	var str = "sfrz";
	window.location.href="${home}/help_con1?str="+str;
});
$(".cjwt").on("click",function(){
	var str = "cjwt";
	window.location.href="${home}/help_con1?str="+str;
});
$(".aqbz").on("click",function(){
	var str = "aqbz";
	window.location.href="${home}/help_con1?str="+str;
});
$(".hyjk").on("click",function(){
	var str = "hyjk";
	window.location.href="${home}/help_con1?str="+str;
});
$("#intro").on("click",function(){
	$("#intro").attr("class","check");
	window.location.href="${home}/intro";
});
$(".intro").on("click",function(){
	window.location.href="${home}/intro";
});
$(".join").on("click",function(){
	window.location.href="${home}/join";
});
$(".cooperation").on("click",function(){
	window.location.href="${home}/cooperation";
});
$(".third_pf").on("click",function(){
	window.location.href="${home}/third_pf";
});
$(".agreement").on("click",function(){
	window.location.href="${home}/agreement";
});

</script>