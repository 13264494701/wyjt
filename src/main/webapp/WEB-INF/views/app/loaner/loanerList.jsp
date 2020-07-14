<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<%@include file="/WEB-INF/views/app/constant.jsp" %>
<html>
<head>
    <title>找专业出借人-无忧借条</title>
    <%@include file="/WEB-INF/views/include/head.jsp" %>
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <script src="${mbStatic}/assets/scripts/base/sea.js?v=<%=version%>"></script>
    <script>
        (function (b, e) {
            typeof define === "function" && define.cmd ? (define("flex", function (g, d) {
                return b(e, d)
            }), seajs.use("flex")) : document.readyState === "complete" ? b(e, e) : document.addEventListener("DOMContentLoaded", function () {
                b(e, e)
            }, false)
        })(function (b, e) {
            function g() {
                var a = i.getBoundingClientRect(), c = a.width;
                a = a.height;
                if (c > a) c = a;
                c > 640 && (c = 640);
                c /= 7.5;
                i.style.fontSize = c + "px";
                f.rem = d.rem = c
            }

            var d = window, i = d.document.documentElement;
            b = 0;
            var h, k, f = {};
            if (!b) {
                var j = d.navigator.userAgent, l = d.devicePixelRatio;
                (k = (!!j.match(/android/gi), !!j.match(/iphone/gi))) && j.match(/OS 9_3/);
                b = k ? l >= 3 && (!b || b >= 3) ? 3 : l >= 2 && (!b || b >= 2) ? 2 : 1 : 1
            }
            i.setAttribute("data-dpr", b);
            d.addEventListener("resize", function () {
                clearTimeout(h);
                h = setTimeout(g, 300)
            }, false);
            d.addEventListener("pageshow", function (a) {
                if (a.persisted) {
                    clearTimeout(h);
                    h = setTimeout(g, 300)
                }
            }, false);
            g();
            f.dpr = d.dpr = b;
            f.refreshRem = g;
            f.rem2px = function (a) {
                var c = parseFloat(a) * this.rem;
                if (typeof a === "string" && a.match(/rem$/)) c += "px";
                return c
            };
            f.px2rem = function (a) {
                var c = parseFloat(a) / this.rem;
                if (typeof a === "string" && a.match(/px$/)) c += "rem";
                return c
            };
            e.flex = f
        }, this);
    </script>
    <style type="text/css">
        .labelnav {
            height: .864rem;
            padding: 0 .3rem;
            line-height: .964rem;
            font-size: .26rem;
            color: #313131
        }

        .labelnav:before {
            content: '';
            width: .04rem;
            height: .2rem;
            background: #df3132;
            display: inline-block;
            margin-right: .2rem
        }

        .lendperson {
            background: white;
            margin-bottom: .1rem
        }

        .lendp-info {
            border-bottom: .01rem solid #e1e1e1;
            height: 1.3rem;
            display: -webkit-box;
            display: -ms-flexbox;
            display: -webkit-flex;
            display: flex;
            -webkit-box-align: center;
            -ms-flex-align: center;
            -webkit-align-items: center;
            align-items: center;
            padding: 0 .3rem;
            -webkit-box-pack: justify;
            -ms-flex-pack: justify;
            -webkit-justify-content: space-between;
            justify-content: space-between
        }

        .user-pic {
            width: .8rem;
            height: .8rem;
            -webkit-border-radius: 50%;
            -moz-border-radius: 50%;
            border-radius: 50%;
            margin-right: .3rem;
            display: block
        }

        .user-name {
            -moz-box-flex: 1;
            -webkit-box-flex: 1;
            -ms-flex: 1;
            -webkit-flex: 1;
            flex: 1
        }

        .user-name p {
            font-size: .3rem;
            line-height: .4rem;
            color: #313131
        }

        .user-name span {
            display: block;
            font-size: .26rem;
            line-height: .4rem;
            color: #898989
        }

        .user-name span b {
            color: #df3030;
            font-weight: normal
        }

        .applyBtn {
            width: 1.4rem;
            height: .6rem;
            background: #df2f31;
            -webkit-border-radius: .1rem;
            -moz-border-radius: .1rem;
            border-radius: .1rem;
            text-align: center;
            line-height: .6rem;
            font-size: .3rem;
            color: white;
            border: 0;
            display: block
        }

        .lendp-require {
            display: -webkit-box;
            display: -ms-flexbox;
            display: -webkit-flex;
            display: flex;
            padding: .3rem;
            font-size: .26rem
        }

        .lendp-require .requireL {
            margin-right: .1rem
        }

        .lendp-require .requireR {
            line-height: .39rem
        }

        .navbar h1 {
            line-height: 1.0405rem;
            font-size: .4345rem;
        }
    </style>
</head>
<body>
<article class="pages " style="background: #f1f2f6;">

    <section class="page">
        <div class="page-content" style="background-color:rgb(225,225,225);height: auto;">
            <div class="labelnav">专业出借人列表</div>
            <c:forEach items="${ufangLoanerList}" var="loaner">
                <div class="lendperson">
                    <div class="lendp-info">
                        <img src="${loaner.headImage}" class="user-pic">
                        <div class="user-name">
                            <p>${loaner.name}</p>
                            <span>已成功放款<b>${loaner.loanQuantity}</b>笔</span>
                        </div>
                        <form action="/app/wyjt/member/loanerLoan">
                            <input type="hidden" name="_header" value="${memberToken}">
                            <input type="hidden" name="_type" value="h5">
                            <input type="hidden" name="loanerId" value="${loaner.id}">
                            <button class="applyBtn" style="cursor: pointer;font-size: 11px">立即申请</button>
                        </form>
                    </div>
                    <div class="lendp-require">
                        <div class="requireL">放款要求</div>
                        <div class="requireR">
                            <p>${loaner.loanRequirement}</p>
                        </div>
                    </div>
                </div>
            </c:forEach>
                              暂无
        </div>
    </section>
</article>
<script type="text/javascript">
    define('list', ['zepto', 'jumpApp'], function (require, exports, module) {
        var $ = require('zepto'), call = require('jumpApp');
        var ev = $.support.touch ? 'tap' : 'click', $applyBtns = $('.applyBtn'), data = "";
        $applyBtns.on(ev, function () {
            var _this = $(this);
            $.confirm('<p class="pop-info" style="text-align: left;">本服务为第三方借款，请谨慎操作，本公司不提供放贷业务和承担相关责任，特此告知!</p>', {
                callback: function () {
                    location.href = "loanerLoan.jsp?id=" + _this.attr("data-id") + "&data=" + data;
                },
                btn: ['再想想', '同意并继续'],
                cls: 'withdraw-pass-dialog'
            });
        });
    });
    seajs.use(['list']);
</script>
</body>
</html>