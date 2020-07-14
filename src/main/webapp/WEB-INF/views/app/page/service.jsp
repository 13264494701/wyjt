<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<%@include file="/WEB-INF/views/app/constant.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <title>客服服务</title>
    <%@include file="../meta-flex.jsp" %>
    <meta name="keywords" content="无忧借条"/>
    <meta name="description" content="无忧借条"/>
    <link rel="stylesheet" href="${mbStatic}/assets/css/public.css?v=<%=version%>" type="text/css">
    <link rel="stylesheet" href="${mbStatic}/assets/css/helpcenter.css?v=<%=version%>" type="text/css">
    <script src="${mbStatic}/assets/scripts/base/sea.js?v=<%=version%>"></script>
    <script src="${mbStatic}/assets/scripts/base/flexible.js?v=<%=version%>"></script>
    <style>
        .red {
            color: red;
        }

        h2 {
            font-weight: bold;
        }

        table {
            width: 100%;
            margin: 10px 0;
            text-align: center;
            border-right: 0px solid #ccc;
            border-bottom: 1px solid #ccc;
            font-size: 1rem;
            line-height: 2rem;
        }

        table td {
            border-left: 1px solid #ccc;
            border-top: 1px solid #ccc;
            border-right: 1px solid #ccc;
        }

        .quetit {
            width: 100%;
            height: 1px;
            background: #e1e1e1;
            position: relative;
            margin-top: 2rem;
            margin-bottom: 1.5rem;
        }

        .quetit span {
            background: white;
            position: absolute;
            left: 50%;
            top: -0.9rem;
            display: block;
            width: 8rem;
            text-align: center;
            margin-left: -4rem;
            color: #313131;
            font-size: 1.16667rem;
            font-weight: 700;
        }

        .topnav {
            display: flex;
            text-align: center;
            height: 8.2rem;
        }

        .topnav img {
            width: 3.6rem;
            height: 3.6rem;
            margin: 1rem 0;
        }

        .topnav p {
            font-size: 1.5rem;
            color: white;
        }

        .top-tel {
            flex: 1;
            background: #ff5908;
            margin-right: 0.8rem;
            border-radius: 10px;
        }

        .top-kf {
            flex: 1;
            background: #278dff;
            border-radius: 10px;
        }

        .animate {
            -webkit-animation: roll 4s linear 0s infinite forwards;
            animation: roll 4s linear 0s infinite forwards;
            transform-style: preserve-3d
        }

        .animate1 {
            -moz-animation-delay: 1s;
            -webkit-animation-delay: 1s;
            animation-delay: 1s
        }

        @keyframes roll {
            from {
                transform: rotateZ(0deg);
                animation-timing-function: ease-out
            }
            50% {
                transform: rotateZ(360deg);
                animation-timing-function: ease-in
            }
            100% {
                transform: rotateZ(360deg)
            }
        }

        @-webkit-keyframes roll {
            from {
                -webkit-transform: rotateZ(0deg);
                -webkit-animation-timing-function: ease-out
            }
            50% {
                -webkit-transform: rotateZ(360deg);
                -webkit-animation-timing-function: ease-in
            }
            100% {
                -webkit-transform: rotateZ(360deg)
            }
        }

        .navbar:after {
            background-color: transparent;
        }
    </style>
</head>
<body>
<article class="views">
    <section class="view">
        <%--<jsp:include page="../header-public.jsp">--%>
            <%--<jsp:param name="title" value="客服服务"></jsp:param>--%>
            <%--<jsp:param name="url" value="back2app"></jsp:param>--%>
        <%--</jsp:include>--%>
        <div class="pages navbar-fixed ">
            <div class="page">
                <div class="page-content" style="padding-top: 0">
                    <div class="ques-con">
                        <div class="topnav">
                            <div class="top-tel">
                                <a href="tel:4006688658">
                                    <div class="animate"><img src="${mbStatic}/assets/images/contact-phone.png">
                                    </div>
                                    <p>400-6688-658</p></a>
                            </div>
                            <div class="top-kf" id="submitBtn">
                                <form action="http://vipwebchat6303.tq.cn/pageinfo.jsp" method="post" id="navForm">
                                    <input type="hidden" value="vip" name="version">
                                    <input type="hidden" value="9728030" name="admiuin">
                                    <input type="hidden" value="1" name="ltype">
                                    <input type="hidden" value="0" name="iscallback">
                                    <input type="hidden" value="41564" name="page_templete_id">
                                    <input type="hidden" value="0" name="is_message_sms">
                                    <input type="hidden" value="0" name="is_send_mail">
                                    <input type="hidden" value="acd" name="action">
                                    <input type="hidden" value="1" name="acd">
                                    <input type="hidden" value="123" name="type_code">
                                    <input type="hidden" value="${member.id}" name="clientid">
                                    <input type="hidden" value="${member.username}" name="clientname">
                                    <div class="animate animate1"><img src="${mbStatic}/assets/images/contact-chat.png"></div>
                                    <p>在线客服</p></form>
                            </div>

                        </div>

                        <div class="quetit"><span>常见问题</span></div>
                        <h2>1、如何注册/登录？ </h2>

                        <p>应用市场下载无忧借条APP<span class="bold">--></span>点击首页右下角“我的”<span class="bold">--></span>注册登录<span
                                class="bold">--></span>身份验证（可以点击右上角“跳过”）。</p>
                        <p>下载网址：<a href="/app/wyjt/common/download" target="_blank">http://www.51jt.com</a>关注微信公众号“无忧借条服务”-“阅读全文”下载。
                        </p>

                        <h2>2、如何进行身份认证？</h2>

                        <p>点击首页右下角“我的”<span class="bold">--></span>安全中心<span class="bold">--></span>身份认证。</p>
                        <p>注：</p>
                        <p>a. 认证身份信息必须是本人使用自己真实有效的实名证件进行认证；</p>
                        <p>b. 绑定银行卡：需提供身份证号、本人储蓄银行卡号、开户行、开户城市（没有所需开户地地可以直接选择北京市）；</p>
                        <p>c. 其他信息：需提供常用邮箱、常用地址。</p>
                        <p>d.认证扫描失败的原因：</p>
                        <p style="text-indent:2em;">①外部环境原因：如光线反光遮挡住了识别的关键区域（头像）。建议在不遮挡、不背光、不曝光的状态下扫描；</p>
                        <p style="text-indent:2em;">②证件本身问题：信息校验关键区域磨损严重；</p>
                        <p style="text-indent:2em;">③设备问题：手机摄像头像素太低，无法聚焦，建议摄像头像素 500
                            万以上；单个程序内存占用过大，未及时释放，可用内存过少（和手机本身内存无关）</p>
                        <p style="text-indent:2em;">④操作原因：证件不在对准框；用户操作不太规范。</p>
                        <p style="text-indent:2em;">⑤网络环境：建议在网络好的情况下进行扫描。等</p>
                        <p style="text-indent:2em;">（注：目前本人面部轮廓和注册身份证的时候有差异建议去公安系统更新自己的网格照片）</p>
                        <p class="bgGray">e. 如何修改绑定银行卡：</p>
                        <p class="bgGray" style="text-indent:2em;">第一层验证：输入之前完整的绑定银行卡号；</p>
                        <p class="bgGray" style="text-indent:2em;">输入需要绑定新银行卡的开户城市、银行名称、新的银行卡号；</p>
                        <p class="bgGray" style="text-indent:2em;">第二层验证：输入我们平台新增加的支付密码（只能通过人脸识别并且与实名信息比对政工后才可以修改）；</p>
                        <p class="bgGray" style="text-indent:2em;">第一层验证：输入之前完整的绑定银行卡号；</p>
                        <p class="bgGray" style="text-indent:2em;">
                            第三层验证：进行人脸识别比对，对比成功后直接修改成功；对比失败结果告知用户。（如用户特殊情况无法完成可在线联系客服）</p>
                        <p class="bgGray" style="text-indent:2em;color:red;">
                            注：同一账户享有5次/年的修改权限，超出次数不能再修改，请谨慎操作！（修改次数为一个自然年5次，即2017年如超出5次，需要等到2018年1月1日0点才能继续修改）</p>

                        <h2>3、忘记密码怎么找回？</h2>

                        <p>a. 注册手机号码在使用中：登录页面 <span class="bold">--></span>点击忘记密码<span
                                class="bold">--></span>输入手机号码验证<span class="bold">--></span>设置新密码 。</p>
                        <p>b. 注册手机号码不在使用中：点击右上角带耳麦的小人图标，<span class="bgGray">选择上端右侧在线咨询，</span>在线提供手持身份证的正反面照片帮您处理。</p>


                        <h2>4、如何修改密码？</h2>

                        <p>点击首页右下角“我的”<span class="bold">--></span>安全中心<span class="bold">--></span>修改登录密码/手势密码/支付密码。
                        </p>

                        <h2>5、如何借款？</h2>

                        <p>
                            登录账户，首页“找好友借款”填写借款信息（借款金额，还款时间必填）点击下一步，进入还款设置页面（调整还款方式，利息）点击下一步，进入确认借条信息页面，核实信息没有问题选择找好友借款，发起借款申请。</p>
                        <p>注：找好友借款，好友需要下载无忧借条APP，注册账户，认证身份信息后可处理收到的借条信息，同意放款立即支付即可。</p>

                        <h2>6、如何还款？</h2>

                        <p>点击客户端首页<span class="bold">--></span>借条中心<span class="bold">--></span>我欠谁钱<span class="bold">--></span>点击订单<span
                                class="bold">--></span>我要还款。</p>
                        <p>
                            a、若为全额还款，可选择线下还款也可选择申请延期，线下还款需好友确认后才算还款成功；（线下还款需是双方信任的人否则存在一定风险，建议全部线上还款操作，有疑问请直接联系客服确认之后再操作。）</p>
                        <p>b、借条申请延期的次数最多为五次，申请延期默认是1个月，延期时间可自行修改，最长一年；</p>
                        <p>c、申请延期，<span
                                class="bgGray">需在还款当天至逾期第14天之内操作，超出15天，则不能进行延期申请。（延期操作需要放款人确认才生效，请及时和放款人确定。）</span></p>

                        <h2>7、和好友借款有利息吗？</h2>

                        <p>借贷利率是由借款人和出借人双方约定的，<span class="bgGray">不得超出法律规定年化利率24%。</span></p>

                        <h2>8、如果好友不还款怎么办？</h2>

                        <p>逾期未还款的将上传至<span class="bgGray">上海资信</span>和芝麻信用行业关注名单。</p>
                        <p>无忧借条有专业的催收、在线仲裁服务，您可以在APP端首页申请。</p>

                        <h2>9、还款之后对方拒绝处理借条？</h2>
                        <p>
                            登录账户直接充值到无忧账户还款，借条是自动失效的，如果您已经选择了线下还款操作，请您尽量联系放款人沟通解决，如果对方恶意不确认借条，建议您保留好还款凭证和您跟放款人之间协商线下还款的相关记录，后期如有需要可以通过法律途径解决。</p>
                        <p>（借条处理只能本人登录账户进行操作，其他人或者平台均无权登录客户个人账户私自进行操作。）</p>

                        <h2>10、如何申请催收服务？</h2>

                        <p>登录页面，点击“催收仲裁” <span class="bold">--></span>选择法律仲裁/专业催收</p>

                        <h2>11、如何充值/提现？</h2>

                        <p>登陆账户，点击首页右下角“我的”<span class="bold">--></span>点击“充值”/“提现”</p>

                        <h2>12、充值都有哪些充值方式？充值限额多少？</h2>

                        <p>大额充值模式和<span class="bgGray">快捷支付</span>（需先绑定本人银行卡）;快捷支付单次充值最低2元起；</p>
                        <p>a、快捷充值会有其他错误原因次数过多而限制，具体说明文档请看手机支付常见问题处理方法</p>
                        <p>b、支持银行及充值限额说明如下表</p>

                        <table border="0" cellpadding="0" cellspacing="0">
                            <tr>
                                <td colspan="6">认证支付业务支持银行及限额</td>
                            </tr>
                            <tr>
                                <td>序号</td>
                                <td>银行</td>
                                <td style="width: 15%;">卡类型</td>
                                <td>单笔限额</td>
                                <td>单日限额</td>
                                <td>单月限额</td>
                            </tr>
                            <tr>
                                <td>1</td>
                                <td>工商银行</td>
                                <td>借记卡</td>
                                <td>10万</td>
                                <td>40万（单日仅5笔成功交易）</td>
                                <td>400万</td>
                            </tr>
                            <tr>
                                <td>2</td>
                                <td>农业银行</td>
                                <td>借记卡</td>
                                <td>5万</td>
                                <td>30万（单日仅6笔成功交易）</td>
                                <td>400万</td>
                            </tr>
                            <tr>
                                <td>3</td>
                                <td>中国银行</td>
                                <td>借记卡</td>
                                <td>5万</td>
                                <td>200万</td>
                                <td>400万</td>
                            </tr>
                            <tr>
                                <td>4</td>
                                <td>建设银行</td>
                                <td>借记卡</td>
                                <td>5万</td>
                                <td>10万</td>
                                <td>400万</td>
                            </tr>
                            <tr>
                                <td>5</td>
                                <td>邮政储蓄银行</td>
                                <td>借记卡</td>
                                <td>30万</td>
                                <td>200万</td>
                                <td>400万</td>
                            </tr>
                            <tr>
                                <td>6</td>
                                <td>平安银行</td>
                                <td>借记卡</td>
                                <td>30万</td>
                                <td>200万</td>
                                <td>400万</td>
                            </tr>
                            <tr>
                                <td>7</td>
                                <td>民生银行</td>
                                <td>借记卡</td>
                                <td>30万</td>
                                <td>200万</td>
                                <td>400万</td>
                            </tr>
                            <tr>
                                <td>8</td>
                                <td>光大银行</td>
                                <td>借记卡</td>
                                <td>5万</td>
                                <td>5万</td>
                                <td>400万</td>
                            </tr>
                            <tr>
                                <td>9</td>
                                <td>广发银行</td>
                                <td>借记卡</td>
                                <td>30万</td>
                                <td>200万</td>
                                <td>400万</td>
                            </tr>
                            <tr>
                                <td>10</td>
                                <td>中信银行</td>
                                <td>借记卡</td>
                                <td>1万</td>
                                <td>2万</td>
                                <td>2万</td>
                            </tr>
                            <tr>
                                <td>11</td>
                                <td>兴业银行</td>
                                <td>借记卡</td>
                                <td>5万</td>
                                <td>5万</td>
                                <td>400万</td>
                            </tr>
                            <tr>
                                <td>12</td>
                                <td>华夏银行</td>
                                <td>借记卡</td>
                                <td>30万</td>
                                <td>200万</td>
                                <td>400万</td>
                            </tr>
                            <tr>
                                <td>13</td>
                                <td>招商银行</td>
                                <td>借记卡</td>
                                <td>5万</td>
                                <td>5万</td>
                                <td>400万</td>
                            </tr>
                            <tr>
                                <td>14</td>
                                <td>浦发银行</td>
                                <td>借记卡</td>
                                <td>30万</td>
                                <td>200万</td>
                                <td>400万</td>
                            </tr>
                            <tr>
                                <td>15</td>
                                <td>交通银行</td>
                                <td>借记卡</td>
                                <td>5万</td>
                                <td>5万</td>
                                <td>400万</td>
                            </tr>
                            <tr>
                                <td>16</td>
                                <td>北京银行</td>
                                <td>借记卡</td>
                                <td>5万</td>
                                <td>5万</td>
                                <td>400万</td>
                            </tr>
                            <tr>
                                <td>17</td>
                                <td>上海银行</td>
                                <td>借记卡</td>
                                <td>5万</td>
                                <td>5万</td>
                                <td>400万</td>
                            </tr>
                            <tr>
                                <td colspan="6">
                                    备注：认证支付交易金额最低2元起。银行列表及额度如有变动，会更新在富友商户操作平台
                                    （http://fht.fuiou.com），请及时关注。 农业，邮储，北京银行和上海银行交易需要开通银联在线支付功能。
                                </td>
                            </tr>
                        </table>

                        <h2>13、充值之后多久到帐？未到账怎么办？</h2>

                        <p>即时到账，如果充值15分钟后仍未到账，核实银行是否成功扣款；特殊情况致电客服电话400-6688-658核实。</p>

                        <h2>14、提现提示登录无效：打开您的手机设置-safari-阻止cookie-勾选始终允许。</h2>

                        <h2>15、提款到账时间？提现手续费多少？</h2>

                        <p>7*24小时账户提现实时到账(一般10分钟之内)，实际到账时间因银行有所差别；手续费每笔1元。</p>

                        <table>
                            <tr>
                                <td>时间</td>
                                <td>金额限制</td>
                            </tr>
                            <tr>
                                <td>09:30-17:30</td>
                                <td>提现金额不受限制</td>
                            </tr>
                            <tr>
                                <td>17:30-21:00（次日）</td>
                                <td>单笔提现限额小于5万元</td>
                            </tr>
                            <tr>
                                <td>21:00-09:30（次日）</td>
                                <td>单笔提现限额小于3万元</td>
                            </tr>
                        </table>

                        <p>a、9:30-20:30期间提现单笔大于等于3W元时，需人工审核后才可以提现。</p>
                        <p>b、充值提现的银行卡类型仅支持储蓄卡，不支持信用卡；不支持提款到别人银行卡。</p>

                    </div>

                </div>
            </div>
        </div>
    </section>
</article>
<script>
    window.panel = null;
    define('contact', ['zepto', 'pop', 'panel', 'jumpApp'], function (require, exports, module) {
        var $ = require('zepto'), pop = require('pop'), call = require('jumpApp'), panel = require('panel');
        var ev = $.support.touch ? 'tap' : 'click', $btn = $('#submitBtn');
        $btn[ev](function () {
            $("#navForm").submit();
        });
    });
    seajs.use(['contact']);
</script>
</body>
</html>