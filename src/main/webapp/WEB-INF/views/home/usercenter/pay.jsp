<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/user/include/public.jsp" %>
<!DOCTYPE html>
<html xmlns=http://www.w3.org/1999/xhtml>
<head>
    <meta charset="UTF-8"/>
    <title>充值-无忧借条</title>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"/>
    <link rel="stylesheet" href="<%=cdnUrl%>/assets/css/public.css?v=<%=version%>" type="text/css">
    <link rel="stylesheet" href="<%=cdnUrl%>/assets/css/v1/usercenter.css?v=<%=version%>" type="text/css">
    <link rel="stylesheet" href="<%=cdnUrl%>/assets/css/user.css?v=<%=version%>" type="text/css">
    <script src="<%=cdnUrlJS%>assets/scripts/base/sea.js?v=<%=version%>" type="text/javascript"></script>
</head>
<body>
<div class="docBody" style="background: white;">
    <!--頭部 開始-->
    <jsp:include page="/common/new-header.jsp">        <jsp:param name="type" value="business"></jsp:param></jsp:include>
    <jsp:include page="/common/new-userinfo.jsp"></jsp:include>
    <!--頭部 結束-->
    <div class="yxb-paybg" style="padding-top:2px;; background: #f6f6f6;border-top:none;">
        <div class="yxb-pay">
            <div class="paycont">
                <div class="way-title">
                    <ul>
                        <label >充值方式</label>
                        <li class="checked" style="display: none;">快捷支付</li>
                        <li class="checked">银联支付</li>
                        <li style="display: none;">微信支付</li>
                        <%--<li>微信支付</li>--%>
                        <%--<li>微信支付</li>--%>
                    </ul>
                    <form id="formAlypay" action="" method="post" style="display: none;">
                        <div class="way-con">
                            <table>
                                <tr>
                                    <td class="name">请输入充值金额:</td>
                                    <td class="moneyinput">
                                        <input type="text" placeholder="最少充值1元" name="money" value=""  style="color: black;">元
                                        <span class="wrong" style="background-size:10%;width:150px;padding-left:20px;margin-left:20px; display: none; " name="rechargeErr">充值金额为整数~！</span>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="name name2">请选择银行卡:</td>
                                    <td class="bank">
                                        <div class="notice">友信宝暂不支持信用卡充值</div>

                                        <div class="clearfix" name="checkBanking">
                                            <div class="checked-bank bankChecked">
                                                <span class="bank-logo"></span><em>尾号（0398）</em>
                                            </div>
                                            <div class="otherbank">选择其他银行</div>
                                        </div>
                                    </td>
                                </tr>
                                <tr class="lasttr">
                                    <td class="name">请输入银行卡号:</td>
                                    <td class="bank-num">
                                        <input type="text" placeholder="请谨慎输入银行卡号" name="inputOtherBankCard" readonly="readonly" maxlength="19">
                                        <span class="wrong" style="background-size:10%;width:150px;padding-left:20px;margin-left:40px;display: none;" name="bankErr">银行卡输入有误~！</span>
                                    </td>
                                </tr>
                                <tr>
                                    <td></td>
                                    <td class="charge-but"><input type="button" value="立即充值" name="paySub"></td>
                                </tr>
                            </table>
                        </div>
                    </form>
                    <form id="formYlpay" action="/user/charge/unionpay_pay.jsp" method="post">
                        <div class="way-con">
                             <table>
                                <tr>
                                    <td class="name">请输入充值金额:</td>
                                    <td class="moneyinput">
                                        <input type="text" placeholder="最少充值1元" name="money" value="" style="color: black;">元
                                        <span class="wrong" style="background-size:10%;width:150px;padding-left:20px;margin-left:20px; display: none;" name="rechargeErr">充值金额为整数~！</span>
                                    </td>
                                </tr>
                                <tr>
                                    <td></td>
                                    <td class="charge-but"><input type="button" value="立即充值" name="paySub" id="paySub"></td>
                                </tr>
                            </table>
                        </div>
                    </form>

                    <form id="formWx2" style="display: none;">
                        <div class="way-con">
                            <p style="margin: 40px;">暂未开通</p>
                        </div>
                    </form>

                    <form id="formWx3" style="display: none;">
                        <div class="way-con">
                             <p style="margin: 40px;">暂未开通</p>
                        </div>
                    </form>

                    <form id="formWx4" style="display: none;">
                        <div class="way-con">
                             <p style="margin: 40px;">暂未开通</p>
                        </div>
                    </form>
                    <div class="charge-notice" style="height: 111px;">
                        <p class="red"><i class="noticets"></i> 温馨提示： </p>
                        <p style="color: red;">1、用户在大额不方便充值的情况下，可选择银行汇款到公司银行账户，再联系客服核实确认后，可直接在账户加款。</p>
                        <p>2、为了您的资金安全，请尽量不要在网吧等公共场合充值！</p>
                        <p>3、网银充值无手续费。一般情况下从支付完成到资金进入友信宝账户只需一秒钟。 </p>
                    </div>
                </div>
                <!--弹框提示-->
                <div class="charge-tcdiv" style="display: none">
                    <div class="title">提示<a href="#"></a></div>
                    <p class="txt">请在新打开的银行页面完成支付！</p>

                    <div class="ques">
                        <p>您的付款成功了吗?</p>

                        <p class="riwr"><i class="right"></i>成功？<a href="#">继续充值</a><a href="#">查询记录</a></p>

                        <p class="riwr"><i class="wrong"></i>失败？<a href="#">更换其他充值方式</a><a href="#">查询帮助</a></p>
                    </div>
                </div>

                <!--弹框提示2222222-->
                <div class="charge-tcdiv" style="display: none">
                    <div class="title">完善身份信息<a href="#"></a></div>

                    <div class="userinfo-wans">
                        <p><label>真实姓名：</label><input type="text"><span><i class="wrong"></i>格式有误</span></p>

                        <p><label>身份证号：</label><input type="text"><span><i class="wrong"></i>身份证号码有误</span></p>
                    </div>
                    <div class="userinfo-ts"><i class="noticets"></i>快捷支付只能使用和您身份信息一致的银行卡，请认真填写</div>
                    <div class="userinfo-but"><a href="#" class="submit">提交</a><a href="#" class="other">更换其他充值方式</a></div>
                </div>

            </div>
        </div>
    </div>
    <!--尾部 開始-->
    <jsp:include page="/common/new-footer.jsp"></jsp:include>

    <!--尾部結束-->
</div>
<script>

    define('formSub', ['jquery'], function (require, exports, module) {
        var $ = require('jquery');
        $.fn.form = function (func, onInit) {
            func = $.isFunction(func) ? func : new Function("return true");
            return this.each(function () {
                var $form = $(this);
                if ($form.data('isReady')) return;
                var $subBtn = $form.find('[name="paySub"]');
                var validateFunc = function () {
                    return func.apply($form, arguments);
                };
                $subBtn.click(function () {
                    if (validateFunc()) {
                        $form.submit();
                    }
                });
                $form.data('isReady', !0);
                $.isFunction(onInit) && onInit.call($form);
            })
        };
    });

    define('payMain', ['jquery', 'formSub'], function (require, exports, module) {
        var needUpdateUserInfo = false, $ = require('jquery'),
                otherBankCardFalg = true;//验证是否满足form提交所需要的全部条件的tag：true则满足，反正不能提交；
        require('formSub');

        //tab选项卡事件
        function tabClick() {
            var tabEvent = $('.way-title li'),$forms=$('form');
            tabEvent.click(function () {
                var $this=$(this),_index=$this.index()-1;
                $this.addClass('checked').siblings().removeClass('checked');
                $forms.eq(_index).show().siblings('form').hide();
            });
        }

        tabClick();

        //首先判断该用户是否完善身份信息
        if (needUpdateUserInfo) {
            require.async(['user/updateInfo'], function (updateInfo) {
                var updateDialog = updateInfo(function () {
                    var $content = this.content.find('p'), p1 = $content.eq(0), p2 = $content.eq(1),
                            val1 = p1.find('input').val(), val2 = p2.find('input').val(),
                            err1 = p1.find('span'), err2 = p2.find('span'), postUrl = '';
                    if (val1.trim().length == 0) {
                        err1.show();
                        return false;
                    }
                    else err1.hide();
                    if (val2.trim().length == 0 || val2.trim().length < 19) {
                        err2.show();
                        return false;
                    }
                    else err2.hide();
                    $.post(postUrl, function (msg) {
                        var rel = $.parseJSON(msg);
                        if (rel && rel['success']) {
                            $.alert('身份信息完成成功~！');
                            updateDialog.close();
                        }
                        else {
                            $.alert(rel.msg);
                        }
                    });
                    return false;
                })
            })
        }
        //验证充值金额输入
        function rechargeMoneyFun(obj,err) {
            var val = $(obj).val().trim();
            if (!/^\d+$/.test(val)) {
                err.show();
                return false;
            }
            else{
                err.hide();
                return true;
            }

        }
        //验证银行卡输入
        function inputOtherBankCardFun(obj,err) {
            var val = $(obj).val().trim();
            if (val.length < 19) {
                err.html('银行卡输入不完整~！').show();
                return false;
            }
            else if (!/^\d{19}$/.test(val)) {
                err.html('银行卡输入有误~！').show();
                return false;
            }
            else{
                err.hide();
                return true;
            }

        }

        //调用插件：在$对象上调用
        $('#formAlypay').form(function () {
            var $form = this;
            var $rechargeMoney = $form.find('[name="money"]'),
                    $rechargeErr = $form.find('[name="rechargeErr"]'),
                    $bankErr = $form.find('[name="bankErr"]'),
                    $inputOtherBankCard = $form.find('[name="inputOtherBankCard"]');
            var flag = rechargeMoneyFun($rechargeMoney,$rechargeErr);
            if (flag) {
                !otherBankCardFalg && (otherBankCardFalg = inputOtherBankCardFun($inputOtherBankCard,$bankErr));
                if (otherBankCardFalg) return true;
            }
        }, function () {
            var $form = this;
            var $rechargeMoney = $form.find('[name="rechargeMoney"]'),
                    $rechargeErr = $form.find('[name="rechargeErr"]'),
                    $bankErr = $form.find('[name="bankErr"]'),
                    $checkBanking = $form.find('[name="checkBanking"]').children('div'),
                    $inputOtherBankCard = $form.find('[name="inputOtherBankCard"]');
            $rechargeMoney.blur(function () {
                rechargeMoneyFun(this,$rechargeErr);
            });
            $checkBanking.click(function () {
                var $this = $(this);
                if (!$this.hasClass('bankChecked')) {
                    $this.addClass('bankChecked').siblings().removeClass('bankChecked');
                    if ($this.hasClass('otherbank')) {
                        $inputOtherBankCard.removeAttr('readonly').val('');
                        otherBankCardFalg = false;
                    }
                    else {
                        $inputOtherBankCard.attr('readonly', 'readonly').val('');
                        $bankErr.hide();
                        otherBankCardFalg = true;
                    }
                }
            });
            $inputOtherBankCard.blur(function () {
                inputOtherBankCardFun(this,$bankErr);
            });
        });

        $('#formYlpay').form(function () {
            var $form = this;
            var $rechargeMoney = $form.find('[name="money"]'),
                    $rechargeErr = $form.find('[name="rechargeErr"]');
            var flag = rechargeMoneyFun($rechargeMoney,$rechargeErr);
            if (flag) {
                if (otherBankCardFalg) return true;
            }
        }, function () {
            var $form = this;
            var $rechargeMoney = $form.find('[name="rechargeMoney"]'),
                    $rechargeErr = $form.find('[name="rechargeErr"]');
            $rechargeMoney.blur(function () {
                rechargeMoneyFun(this,$rechargeErr);
            });
        });

    });

    seajs.use('payMain');
</script>
</body>
</html>
