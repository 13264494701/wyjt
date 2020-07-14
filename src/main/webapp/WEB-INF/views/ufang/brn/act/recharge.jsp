<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<html>
<head>
    <title>充值</title>
    <meta name="decorator" content="default"/>
</head>
<body>
<form method="post" action="${ufang}/brnAct/rechargePage" onSubmit="return myCheck()">
	<div style="display: flex;align-items: flex-start;padding-top:20px">
		<div style="width: 44%;;margin: 0px 30px">
			<h5 style="margin-left:30px;font-weight: bold">快速充值</h5>
	        <div style="margin-left:30px;margin-top: 10px">充值金额：<input id="amount" name="amount" style="width:250px;margin-top: 10px;margin-left:30px;height:30px" type="text" placeholder="最低充值100元"></div><br>
	        <input type="submit" class="btn btn-primary" value="充值" style="width:360px;margin-left: 30px;height:50px">
        </div>
        <div>
        	<h5 style="margin-left:30px;font-weight: bold">线下充值</h5><br>
        	<div style="margin-left:30px;color:#777">银行账号</div><br>
        	<div style="margin-left:30px;font-weight: bold">账     号： 1028 5000 0014 6002 0</div><br>
        	<div style="margin-left:30px;font-weight: bold">户     名：北京友信宝网络科技有限公司&nbsp;&nbsp;&nbsp;<span><img style= "width:20px;height:20px" src="${local}/icon/recharge/yesMark.png"></span></div><br>
        	<div style="margin-left:30px;font-weight: bold">开 户 行：华夏银行北京分行上地支行</div><br>
	        <div style="margin-left:30px;color:#777">线下充值说明：</div>
	        <div style="margin-left:30px;color:#777">1、线下充值，请您向我司打款时备注上您的公司名称手机号及汇款说明；</div>
	        <div style="margin-left:30px;color:red">（具体格式如下：公司名称+姓名+手机号+优放线下充值）</div><br>
	        <div style="margin-left:30px;color:#777">2、汇款成功后请第一时间<span style="color:blue">致电我司客服</span>或<span style="color:blue">联系微信客服</span>给您账户加款；</div><br>
	        <div><img style= "width:120px;height:120px;float:left;margin-left:20px" src="${local}/icon/recharge/qrCode.png">
	        <div style="margin-left:30px"><br><br><br>客服微信：YOUFANGXT<br>客服电话：400-6688-658<br><span style="color:red">客服加款时间：09:00 - 18:00</span></div>
	        </div>      
        </div>
        <div style="margin-top: 38px">
        	<div style="margin-left:30px;color:#777">支付宝账号</div><br>
        	<div style="margin-left:30px;font-weight: bold">账     号： mbank@yxinbao.com</div><br>
        	<div style="margin-left:30px;font-weight: bold">户     名：北京友信宝网络科技有限公司&nbsp;&nbsp;&nbsp;<span><img style= "width:20px;height:20px" src="${local}/icon/recharge/yesMark.png"></span></div><br>
        </div>
    </div>
    
</form>
<div style="display: flex;">
    <table style="width: 44%;margin: 0px 30px" class="table">
        <tr>
            <td colspan="4" style="font-weight: bold">收费标准</td>
        </tr>
        <tr>
            <td>优享流量</td>
            <td>5-10元/条</td>
            <td>优淘流量</td>
            <td>10元/条</td>
        </tr>
        <tr>
            <td>特价流量</td>
            <td>2.5-3元/条</td>
            <td>公积金查询</td>
            <td>1元/次</td>
        </tr>
        <tr>
            <td>芝麻分查询</td>
            <td>1元/次</td>
            <td>社保查询</td>
            <td>1元/次</td>
        </tr>
        <tr>
            <td>网银查询</td>
            <td>1元/次</td>
            <td>学信网查询</td>
            <td>1元/次</td>
        </tr>
        <tr>
            <td>淘宝查询</td>
            <td>1元/次</td>
            <td>闪蝶报告</td>
            <td>1元/次</td>
        </tr>
        <tr>
            <td>京东查询</td>
            <td>1元/次</td>
            <td>运营商查询</td>
            <td>1元/次</td>
        </tr>
    </table>
    <table style="width: 44%;margin: 0px 30px" class="table">
        <tr>
            <td colspan="2" style="font-weight: bold">充值优惠</td>
        </tr>
        <tr>
            <th>充值金额</th>
            <th>赠送流量条数</th>
        </tr>
        <tr>
            <td>充值10000</td>
            <td>送100条</td>
        </tr>
        <tr>
            <td>充值20000</td>
            <td>送300条</td>
        </tr>
        <tr>
            <td>充值30000</td>
            <td>送500条</td>
        </tr>
        <tr>
            <td>充值40000</td>
            <td>送800条</td>
        </tr>
        <tr>
            <td>充值50000以上</td>
            <td>送2000条</td>
        </tr>
    </table>
</div>

<div class="alert alert-danger" style="margin: 30px;background-color: #f3dad4;">
    温馨提示：<br>
    1、充值金额不能进行退款，请谨慎操作。<br>
    2、不承诺转化效果。<br>
    3、不保证回款质量。<br>
</div>

<script type="text/javascript">
    function myCheck() {
        var reg = /^[1-9]\d*$/;
        var amount = $('#amount').val();
        if (!reg.test(amount)) {
            alert('请输入正确的金额.');
            return false;
        }
        if (amount < 100) {
            alert('金额最低为100元.');
            return false;
        }
        if (!confirm("是否确认充值?")) {
            return false;
        }
        return true;
    }
</script>
</body>
</html>