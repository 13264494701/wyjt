<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html lang="en">
<head>
    <title>优放后台管理系统--首页</title>
    <meta name="decorator" content="default"/>
    <link rel="stylesheet" href="${ctxStatic}/dashboard/css/index.css" type="text/css">
    <script>
    (function() {
    var a = document.querySelectorAll("li");
    a.forEach(function(c, b) {
      var d = c.children[0].innerHTML;
      c.setAttribute("style", "background:url(./dashboard/img/" + d + ".png) 0px 10px no-repeat;background-size:5%;")
    })
    })();
    </script>
</head>
<body>
<div class="notice">通知栏：有通知内容时显示内容，否则该栏隐藏；文字左对齐。</div>
<div class="rightContent">
    <div class="rightTopLeft">
        <div class="left">
            <p>
                <span class="title">流量统计</span>
            </p>
            <p class="leftToday">
                <i class="blue">今日</i><span><t>购买流量(条)</t><b>6</b></span><span><t>消费金额(元)</t><b>200.00</b></span><span><t>账户余额(元)</t><b>5554</b></span>
                <a href ="${ufang}/brnAct/recharge">充值</a>
            </p>
            <p class="leftToday">
                <i class="yellow">昨日</i><span><b>6</b></span><span><b>200.00</b></span><span><b>5554</b></span>
            </p>
        </div>
    </div>
    <div class="rightTopRight">
        <div class="right">
            <p>
                <span class="title">借条统计</span>
            </p>
            <p class="leftToday">
                <i class="blue">今日</i><span><t>新的借条(个)</t><b>6</b></span><span><t>当日待收(个)</t><b>8</b></span><span><t>当日待放金额(元)</t><b>5554.00</b></span><span><t>当日已收(个)</t><b>8</b></span><span><t>当日已收金额(元)</t><b>5554.00</b></span>
            </p>
            <p class="leftToday">
                <i class="yellow">昨日</i><span><b>6</b></span><span><b>8</b></span><span><b>5554.00</b></span><span><b>8</b></span><span><b>5554.00</b></span>
            </p>
        </div>
    </div>
</div>
<div class="rightBottom">
    <div class="oneBox">
        <p><span class="title">流量</span></p>
        <ul>
            <li class="oneBg"><a href="${ufang}/ufangLoaneeData?prodCode=001">优享流量</a><i class="fire"></i></li>
            <li><a href="${ufang}/ufangLoaneeData?prodCode=002">优淘流量</a></li>
            <li><a href="${ufang}/ufangLoaneeData?prodCode=003">特价流量</a></li>
            <li><a href="${ufang}/ufangLoaneeDataOrder?dataGroup=pendingFollow">已购流量</a></li>
        </ul>
    </div>
    <div class="oneBox">
        <p><span class="title">借条</span></p>
        <ul>
            <li><a href="${ufang}/loanApply/singleLoanApplyList">借条申请</a></li>
            <li><a href="${ufang}/loanRecord">借条列表</a></li>
            <li><a href="${ufang}/nfsLoanCollection/list">催收列表</a></li>
            <li><a href="${ufang}/loanArbitration">仲裁列表</a></li>
        </ul>
    </div>
    <div class="oneBox">
        <p><span class="title">好友</span></p>
        <ul>
            <li><a href="${ufang}/friend">好友列表</a></li>
            <li><a href="${ufang}/friend/add">添加好友</a></li>
        </ul>
    </div>
    <div class="oneBox">
        <p><span class="title">风控查询</span></p>
        <ul>
            <li><a href="${ufang}/rcWyjt/blacklist/query">黑名单查询</a><i class="new"></i></li>
            <li><a href="${ufang}/rcGxb/add?authItem=sesame_multiple">芝麻分查询</a></li>
            <li><a href="${ufang}/rcSjmh/add?channel=0">运营商查询</a></li>
            <li><a href="${ufang}/rcSjmh/add?channel=1">淘宝查询</a></li>
            <li><a href="${ufang}/">更多</a></li>
        </ul>
    </div>
</div>
</body>
</html>