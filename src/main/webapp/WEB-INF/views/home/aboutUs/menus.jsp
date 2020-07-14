<%@ page contentType="text/html;charset=utf-8" language="java"%>
<div class="bannerbg">
    <div class="pos1000">
        <div class="discribe">
            <p class="tit">关于我们-----</p>

            <p>无忧借条是一款随手可用的电子借条手机应用。是安全便捷的电子借条类工具，为用户提供经过CA认证、第三方数字存证、200多家公证处认可的规范电子借条。</p>
        </div>
        <!--导航条-->
        <div class="aboutus-navbar clearfix">
            <ul>
                <li>
                    <a href="javascript:;" class="intro">公司简介</a>
                </li>
                <li>
                    <a href="javascript:;" class="join">加入我们</a>
                </li>
                <li>
                    <a href="javascript:;" class="cooperation">战略合作</a>
                </li>
                <li>
                    <a href="javascript:;" class="third_pf">第三方平台</a>
                </li>
                <li style="margin-right: 0;"><a href="javascript:;" class="agreement">用户协议</a>
                </li>
            </ul>
        </div>
    </div>
</div>
<script src="${homeStatic}/assets/jquery/jquery-1.8.3.js"></script>
<script>
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