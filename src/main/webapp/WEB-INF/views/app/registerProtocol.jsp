<%@ page contentType="text/html;charset=utf-8" language="java"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%@include file="/WEB-INF/views/app/constant.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <title>无忧借条用户注册协议</title>
    <meta charset="utf-8">
    <meta name="keywords" content=""/>
    <meta name="description" content=""/>
    <script src="${mbStatic}/assets/scripts/base/sea.js"></script>
    <script src="${mbStatic}/assets/scripts/base/flexible.js"></script>
    <style>
    html, body {
        position: relative;
        height: 100%;
    }

    html {
        font-size: 62.5%;
        -webkit-tap-highlight-color: transparent;
        -ms-text-size-adjust: 100%;
        -webkit-text-size-adjust: 100%;
        -webkit-font-smoothing: antialiased;
    }

    head {
        display: none;
    }

    body {
        background-color: #f7f7f8;
        font: 1.2em/1.5 "Segoe UI", Arial, "Microsoft YaHei", "Helvetica Neue";
        color: #333;
        height: 100%;
    }

    body, div, dl, dt, dd, ul, ol, li, h1, h2, h3, h4, h5, h6, pre, form, fieldset, input, textarea, p, blockquote, th, td {
        padding: 0;
        margin: 0;
        display: block;
    }

    h1, h2, h3, h4, h5, h6 {
        font-size: 100%;
        font-weight: normal;
    }

    .views {
        overflow: auto;
        max-width: 26.66667rem;
        -webkit-overflow-scrolling: touch;
        margin: 0 auto;
    }

    .views, .view {
        position: relative;
        width: 100%;
        height: 100%;
        z-index: 5000;
    }

    .view {
        overflow: hidden;
        -webkit-box-sizing: border-box;
        -moz-box-sizing: border-box;
        box-sizing: border-box;
    }

    section, article, aside, header, footer, nav, dialog, figure {
        display: block;
    }

    article, aside, footer, header, hgroup, main, nav, section {
        display: block;
    }

    .pages {
        position: relative;
        width: 100%;
        height: 100%;
        overflow: hidden;
        background: #000;
    }

    .page {
        -webkit-box-sizing: border-box;
        -moz-box-sizing: border-box;
        box-sizing: border-box;
        position: absolute;
        left: 0;
        top: 0;
        width: 100%;
        height: 100%;
        background: #fff;
        -webkit-transform: translate3d(0, 0, 0);
        transform: translate3d(0, 0, 0);
    }

    .toolbar-fixed .page-content {
        padding-bottom: 3.66667rem;
    }

    .navbar-fixed .page-content {
        padding-top: 3.66667rem;
    }

    .user-main {
        background-color: #f4f4f4;
    }

    .page-content {
        overflow: auto;
        -webkit-overflow-scrolling: touch;
        -webkit-box-sizing: border-box;
        -moz-box-sizing: border-box;
        box-sizing: border-box;
        height: 100%;
        position: relative;
        z-index: 1;
    }

    h2, p {
        font-size: 1rem;
    }

    h2 {
        margin-top: 1rem;
    }

    p {
        line-height: 2rem;
        text-indent: 2em;
    }

    .ending {
        text-align: right;
        padding-right: 1rem;
        margin: 2rem 0;
    }

    .underLine {
        text-decoration: underline;
        color: black;
    }

    .divTop {
        background: #F9F9F9;
        text-align: center;
        position: fixed;
        left: 0;
        top: 0rem;
        width: 100%;
        height: 2rem;
        line-height: 2rem;
        color: red;
        font-size: 1rem;
    }

    .pFlex {
        display: -webkit-flex;
        display: flex;
        -webkit-flex-wrap: nowrap;
        flex-wrap: nowrap;
    }

    a {
        width: 50%;
        height: 2.5rem;
        line-height: 2.5rem;
        text-align: center;
        display: inline-block;
        margin: 1rem;
        border-radius: .25rem;
        text-indent: 0;
        text-decoration: none;
        background: white;
    }

    a:first-child {
        border: 1px solid rgb(157, 157, 157);
        color: rgb(157, 157, 157);
    }

    a:last-child {
        border: 1px solid red;
        color: red;
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
<div class="divTop">请仔细阅读并滑至最底部，完成【同意】操作</div>
<div class="" style="background:white;padding: 1rem;padding-top:2rem;">
    <p>
        无忧借条是北京友信宝网络科技有限公司（以下简称本公司）开发并拥有所有权和经营权的服务平台，本公司通过无忧借条向用户提供相关服务。您在使用相关服务之前，需要仔细阅读本协议，并通过在线形式签署，您选择阅读【同意】，即表示协议生效。本协议由北京友信宝网络科技有限公司与您签订。</p>
    <h2>一、 定义及解释</h2>
    <p>1. 无忧借条：系基于社交类金融服务的第三方服务平台，是北京友信宝网络科技有限公司拥有所有权与经营权的产品，包括yxinbao.com、yxinbao.me等无借条客户端以及后续开发的其他产品。</p>
    <p>2. 用户：系通过无忧借条APP完成全部注册程序后，使用无忧借条平台服务者。</p>
    <p>3. 无忧借条账户（或本文yspjMin中所称谓的“该账户”）：系本公司向您提供的无忧借条平台上的唯一身份。您可以自行设置密码，您使用该账户登录后可以使用无忧借条的各项功能、查询或计量您的交易记录和账户余额。</p>
    <h2>二、 声明和承诺</h2>
    <p class="underLine">1. 您完全理解并确认，在实际使用无忧借条前，您已充分阅读、理解并接受本协议的全部内容，同意遵循本协议之所有约定。</p>
    <p class="underLine">
        2. 您完全理解并同意，本公司有权随时对本协议内容进行单方面的变更，并以在平台上公告的方式予以公布，无需另行单独通知您；若您在本协议内容公告变更后继续使用无忧借条的，表示您已充分阅读、理解并接受修改后的协议内容，也将遵循修改后的协议内容使用无忧借条；若您不同意修改后的协议内容，您应停止使用无忧借条。</p>
    <p>3. 您完全理解并保证，在使用无忧借条时，您应是具备完全民事行为能力的自然人、法人或其他组织；若您在国外，本协议内容不受您所属国家或地区法律的排斥。不具备前述条件的，您应立即停止使用无忧借条。</p>
    <h2>三、 注册</h2>


    <p>1. 您应该根据本公司的要求填写必要的注册信息和账户信息，并保证信息的合法性、真实性、及时性和有效性。注册成功后，如果您所填写的信息发生改变，您应当及时登录无忧借条网站或APP变更相关内容。</p>
    <p>2. 您注册使用的昵称中不得含有诱导性广告内容，如利用奖励机制诱导关注公众号、诱导加好友等。</p>
    <p>3. 如果本公司发现您填写的注册信息和账户信息存在违法、不真实或无效的可能性，有权删除相关信息或注销您的账户。</p>
    <p>
        4. 您不得将在无忧借条注册获得的账户借给他人使用，否则应承担由此产生的全部责任，并与实际使用人承担连带责任。为保证交易安全，您应当妥善保管用户名和密码，如果因为您的疏忽或管理不善、外借账户等自身原因，造成用户名和密码外泄、被盗等后果的，您应当自行承担由此所引发的一切损失。</p>
    <p>
        5. 您在本站进行注册、浏览、发布信息等行为时，涉及您真实姓名/名称、通信地址、联系电话、电子邮箱、订单情况、评价或反馈、投诉内容、cookies等信息的，本公司有权从完成交易、提供配送、客户服务、开展活动、完成良好的客户体验等多种角度予以收集，并将对其中涉及个人隐私信息予以严格保密，除非得到您的授权、为履行强行性法律义务（如国家安全机关指令）或法律另有规定、本协议或本公司提供的其他协议另有约定外，本公司不会向外界披露您的隐私信息。</p>
    <p class="underLine">6. 您完全理解并同意，本公司有权使用您的注册信息、用户名、密码等信息，登录进入您的注册账户，进行证据保全，包括但不限于公证、见证、协助司法机关进行调查取证等。</p>
    <h2>四、 用户守则</h2>

    <p>1. 您可以在无忧借条平台上发布各种信息，但所有信息及评论均提交由无忧借条进行内容审核。</p>
    <p>2. 您在无忧借条发布的内容应符合网络道德，遵守中华人民共和国相关法律法规。不得发布以下信息：</p>
    <p>1) 反对宪法所确定的基本原则的；</p>
    <p>2) 危害国家安全，泄露国家秘密，颠覆国家政权，破坏国家统一的；</p>
    <p>3) 损害国家荣誉和利益的；</p>
    <p>4) 煽动民族仇恨、民族歧视，破坏民族团结的；</p>
    <p>5) 破坏国家宗教政策，宣扬邪教和封建迷信的；</p>
    <p>6) 散布谣言，扰乱社会秩序，破坏社会稳定的；</p>
    <p>7) 散布淫秽、色情、赌博、暴力、凶杀、恐怖或者教唆犯罪的；</p>
    <p>8) 侮辱或者诽谤他人，侵害他人合法权益的；</p>
    <p>9) 含有法律、行政法规禁止的其他内容的。</p>
    <p>本公司有权您在无忧借条发布的上述信息进行屏蔽、删除处理，同时对账户采取警告、封号等措施，同时本公司有权将相关事项提交司法机关依法处理。</p>
    <p class="underLine">3. 您必须保证您发布的信息（包括但不限于文字、图片、视频及活动本身）真实、合法且不侵犯第三方的合法权益，否则应承担所有相关法律责任；因此给无忧借条造成损失的，应当承担赔偿责任。</p>
    <p>4. 您不得利用无忧借条从事侵害他人合法权益之行为，否则应承担所有相关法律责任，因此导致本公司或本公司雇员受损的，您应承担赔偿责任。</p>
    <p>5. 上述侵权行为包括但不限于：</p>
    <p>1) 侵害他人名誉权、隐私权、商业秘密、商标权、著作权、专利权等合法权益；</p>
    <p>2) 违反依法定或约定之保密义务；</p>
    <p>3) 冒用他人名义使用无忧借条；</p>
    <p class="underLine">4) 从事非法交易行为，如非法集资、洗钱、传销、发布反动信息、倒卖毒品、禁药、组织淫秽活动等本公司认为不得使用无忧借条进行发布的活动等；</p>
    <p>5) 提供赌博资讯或以任何方式引诱他人参与赌博；</p>
    <p>6) 非法使用他人银行账户（包括信用卡账户）或无效银行账号（包括信用卡账户）交易；</p>
    <p>7) 违反《银行卡业务管理办法》使用银行卡，或利用信用卡套取现金（以下简称套现）；</p>
    <p>8) 开展与您或其他用户宣称的活动内容不符合的非法活动，或不真实的活动；</p>
    <p>9) 从事任何可能含有电脑病毒或是可能侵害本服务系统、资料之行为；</p>
    <p>10) 其他本公司有正当理由认为不适当之行为；</p>
    <p>11) 其他违反法律法规的行为。</p>
    <p>6. 您不得干扰本站的正常运转，不得侵入本站及国家计算机信息系统。</p>
    <p>7. 您不得发布广告、垃圾邮件。</p>
    <p>8. 您不得恶意利用技术手段或其他方式，为获取优惠、折扣或其他利益而注册账户、下单等，影响其他用户正常使用无忧借条或其相关合法权益，影响无忧借条正常运营秩序。</p>
    <p>9. 您账户内的任何无忧借条优惠信息（包括但不限于无忧借条红包等）由我公司享有解释权和修改权，严禁转卖无忧借条账户、无忧借条红包或其他类型的优惠券，或利用无忧借条账户进行其他经营性行为等。</p>
    <p class="underLine">10.您完全理解并同意本公司将您的注册信息借贷信息经过智能风控手段筛选后将会在本站APP贷超、公众号贷超、优放系统范围内发布，以帮助您获得更多借贷机会。</p>
    <h2>五、 通知和公告</h2>
    <p>1. 我公司将在无忧借条用户界面明显位置发布通知和公告。</p>
    <p>2. 我公司将不会另行通知您公告内容，在本平台发布的通知和公告，视为您已阅读并同意。</p>
    <p>3. 我公司对于已发布的通知和公告有单方面解释权。</p>
    <h2>六、 免责条款</h2>
    <p>1. 本公司不对因下述任一情况导致的任何损害赔偿承担责任，包括但不限于利润、商誉、使用、数据等方面的损失或其他无形损失的损害赔偿。</p>
    <p>1) 本公司有权基于单方判断，包含但不限于本公司认为您已经违反本协议的明文规定及精神，暂停、中断或终止向您提供本服务或其任何部分，并移除您的资料。</p>
    <p>2) 本公司在发现异常交易或有疑义或有违反法律规定或本协议约定之时，有权不经通知先行暂停或终止该账户的使用（包括但不限于对该账户名下的款项和在途交易采取取消交易、调账等限制措施），并拒绝您使用无忧借条之部分或全部功能。</p>
    <p>3) 在必要时，本公司无需事先通知即可终止提供本服务，并暂停、关闭或删除该账户及您账号中所有相关资料及档案，并将您滞留在该账户的全部合法资金退回到您的银行账户。</p>
    <p class="underLine">4) 用户使用友借款中功能，无忧借条仅提供电子借据以及到期催款提醒，请借贷双方自行控制风险，勿借给陌生人钱。若双方产生借贷法律纠纷无忧借条积极配合出具相关证据。</p>
    <p>
        5) 用户使用催款小秘书功能模块应遵守国家的相关法律法规，不得发布非法违规内容，无忧借条有权依法删除、暂停、处理违规违法内容，情节严重者，无忧借条有权交由国家公安机关处理，用户对所发内容应付全部法律责任，无忧借条不承担任何形式的法律责任。</p>
    <p>2. 系统因下列状况无法正常运作，使您无法使用各项服务时，本公司不承担任何损害赔偿责任，该状况包括但不限于：</p>
    <p>1) 本公司在无忧借条客户端公告之系统停机维护期间；</p>
    <p>2) 电信设备出现故障不能进行数据传输的；</p>
    <p>3) 因台风、地震、海啸、洪水、停电、战争、恐怖袭击等不可抗力之因素，造成本公司系统障碍不能执行业务的；</p>
    <p>4) 由于黑客攻击、电信部门技术调整或故障、网站升级、银行、第三方支付工具方面的问题等原因而造成的服务中断或者延迟。</p>
    <p>3. 因您的过错导致的任何损失由您自行承担，本公司不承担任何损害赔偿责任，该过错包括但不限于：遗忘或泄漏密码，密码被他人破解，您使用的手机或其他终端被他人侵入等。</p>
    <h2>七、 法律适用与争端解决</h2>
    <p>1. 本协议之效力、解释、变更、执行与争议解决均适用中华人民共和国法律，没有相关法律规定的，参照通用国际商业惯例和（或）行业惯例。本协议部分内容被有管辖权的法院认定为违法的，不因此影响其他内容的效力。</p>
    <p>2. 因本协议产生之争议，均应依照中华人民共和国法律予以处理，并由我公司住所所在地人民法院管辖。</p>
    <p class="pFlex"><a href="javascript:void 0;" id="refuse">不同意</a><a href="javascript:void 0;" id="agree">同意</a></p>
</div>
</body>
<script type="text/javascript">
seajs.use(['zepto','jumpApp'],function ($,jumpApp) {
    var ev = $.support.touch ? 'tap' : 'click';
    $('#refuse')[ev](function () {
        goUserLogin("0");
    });
    $('#agree')[ev](function () {
        goUserLogin("1");
    });
    /*
    *   从H5页面跳往客户端用户登录/注册页面：
    *   需要调用的方法名：gouserlogin，
    *   需要的参数：isAgreeStr---"0"为不同意，"1"为同意
    */
    function goUserLogin(tag) {
        var parameter = {"isAgreeStr": tag}, mathod = 'gouserlogin';
        jumpApp.callApp(mathod, parameter);
    }

});
</script>
</html>