<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<%@include file="/WEB-INF/views/app/constant.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <%@include file="../meta-flex.jsp" %>
    <title>我的催收-无忧借条</title>
    <meta name="keywords" content="无忧借条"/>
    <meta name="description" content="无忧借条"/>
    <link rel="stylesheet" href="${mbStatic}/assets/css/public.css?v=<%=version%>" type="text/css">
    <link rel="stylesheet" href="${mbStatic}/assets/css/user/debt.css?v=<%=version%>" type="text/css">
    <script src="${mbStatic}/assets/scripts/base/sea.js?v=<%=version%>"></script>
    <script src="${mbStatic}/assets/scripts/base/flexible.js?v=<%=version%>"></script>
    <style>
        .center ul {
            overflow: hidden;
            height: 40px;
            border: 1px solid #fff;
            border-radius: 5px;
            margin-left: 15px;
        }

        .center li {
            float: left;
            text-align: center;
            height: 40px;
            line-height: 40px;
            height: 40px;
            overflow: hidden;
            padding: 0 5px;
        }

        .center li:first-child {
            border-right: 1px solid #fff
        }

        .center li.active {
            background: white;
        }

        .center a {
            color: #fff;
        }

        .center .active a {
            color: #ff0606;
        }

        .hkType {
            float: right;
        }

        .mycs p:first-child {
            margin-bottom: 1rem;
            border-bottom: 1px dashed RGB(226, 226, 226);
            margin-right: 1rem;
            padding-bottom: 1rem;
        }

        .mycs p:last-child {
            margin-top: 1rem;
        }

        .zqr {
            background: url(${mbStatic}/assets/images/debt/lend2@2x.png) left top no-repeat;
            background-size: 100%;
        }

        .zwr {
            background: url(${mbStatic}/assets/images/debt/borrow@2x.png) left top no-repeat;
            background-size: 100%;
        }

        .shenqcs-tit {
            background: url(${mbStatic}/assets/images/debt/columu@2x.png) 1rem 1rem no-repeat;
            background-size: .33rem;
        }

        .quane {
            color: RGB(246, 116, 92);
            background: RGB(254, 237, 230);
            width: 3rem;
            text-align: center;
        }

        .fenqi {
            color: RGB(93, 117, 248);
            background: RGB(225, 241, 254);
            width: 3rem;
            text-align: center;
        }
    </style>
</head>
<c:choose>
<c:when test="${empty appPlatform}">
<body>
</c:when>
<c:otherwise>
<body class="${isWeiXin? 'InWeixin':'InWebView'}" data-platform="${appPlatform}" data-app="51jt">
</c:otherwise>
</c:choose>
<article class="views">
    <section class="view">


        <header class="navbar">
            <div class="navbar-inner">
                <div class="left"><a class="goBack link" href="javascript:void 0" data-method="yxbaoback"><i
                        class="icon icon-back"></i><span>返回</span></a></div>
                <div class="center">
                    <ul style="height: 2.5rem; line-height: 2.5rem;">
                        <style>
                            .center li {
                                height: 2.5rem;
                                line-height: 2.5rem;
                            }
                        </style>
                        <c:if test="${type=='0'}">
                            <li class="active">
                                <form action="/app/wyjt/member/collectionAndArbitrationRecord" id="navForm1" method="post">
                                    <input type="hidden" name="type" value="0">
                                    <input type="hidden" name="version" value="${version}">
                                    <input type="hidden" name="pt" value="${pt}">
                                    <input type="hidden" name="_header" value="${memberToken}">
                                    <input type="hidden" name="_type" value="h5">
                                    <a style="background-color:rgba(255,255,255,0);border-width: 0px;padding-bottom: 8px;color: red" href="javascript:"
                                       id="submitBtn1">
                                    <c:choose>      
                                     	<c:when test="${(version=='4.09' && pt == 'ios')}">
                                     		电话提醒 
                                     	</c:when> 
                                   	    <c:otherwise> 
                                   	    	专业催收  
						 				</c:otherwise>  
                                     </c:choose>
                                       </a>
                                </form>
                            </li>
                            <li>
                                <form action="/app/wyjt/member/collectionAndArbitrationRecord" id="navForm2" method="post">
                                    <input type="hidden" name="type" value="1">
                                    <input type="hidden" name="version" value="${version}">
                                    <input type="hidden" name="pt" value="${pt}">
                                    <input type="hidden" name="_header" value="${memberToken}">
                                    <input type="hidden" name="_type" value="h5">
                                    <a style="background-color:rgba(255,255,255,0);border-width: 0px;padding-bottom: 8px;color: white" href="javascript:"
                                       id="submitBtn2">
                                       <c:choose>      
                                     	<c:when test="${(version=='4.09' && pt == 'ios')}">
                                     		一键裁决
                                     	</c:when> 
                                   	    <c:otherwise>
                                   	    	法律仲裁
						 				</c:otherwise>  
                                     </c:choose>
                                       </a>
                                </form>
                            </li>
                        </c:if>
                        <c:if test="${type=='1'}">
                            <li>
                                <form action="/app/wyjt/member/collectionAndArbitrationRecord" id="navForm3" method="post">
                                    <input type="hidden" name="type" value="0">
                                    <input type="hidden" name="version" value="${version}">
                                    <input type="hidden" name="pt" value="${pt}">
                                    <input type="hidden" name="_header" value="${memberToken}">
                                    <input type="hidden" name="_type" value="h5">
                                    <a style="background-color:rgba(255,255,255,0);border-width: 0px;padding-bottom: 8px;color: white" href="javascript:"
                                       id="submitBtn3">
                                     <c:choose>      
                                     	<c:when test="${(version=='4.09' && pt == 'ios')}">
                                     		电话提醒
                                     	</c:when> 
                                   	    <c:otherwise>
                                   	    	专业催收
						 				</c:otherwise> 
                                     </c:choose>
                                       </a>
                                </form>
                            </li>
                            <li class="active">
                                <form action="/app/wyjt/member/collectionAndArbitrationRecord" id="navForm4" method="post">
                                    <input type="hidden" name="type" value="1">
                                    <input type="hidden" name="version" value="${version}">
                                    <input type="hidden" name="pt" value="${pt}">
                                    <input type="hidden" name="_header" value="${memberToken}">
                                    <input type="hidden" name="_type" value="h5">
                                    <a style="background-color:rgba(255,255,255,0);border-width: 0px;padding-bottom: 8px;color: red" id="submitBtn4" href="javascript:">
                                      <c:choose>      
                                     	<c:when test="${(version=='4.09' && pt == 'ios')}">
                                     		一键裁决
                                     	</c:when> 
                                   	    <c:otherwise>
                                   	    	法律仲裁
						 				</c:otherwise>
                                     </c:choose>
                                    </a>
                                </form>
                            </li>
                        </c:if>
                    </ul>
                </div>
                <div class="right"><a href="javascript:" class="link icon-only"></a></div>
            </div>
        </header>

        <div class="pages navbar-fixed  ">
            <div class="page">
                <div class="page-content">
                    <c:if test="${type=='0'}">
                        <div class="shenqcs-tit">提醒进行中</div>
                    </c:if>
                    <c:if test="${type=='1'}">
                        <div class="shenqcs-tit">裁决进行中</div>
                    </c:if>
                    <c:if test="${type=='1'}">
                         <c:forEach items="${arbitrationList}" var="arbitration">
                            <c:if test="${arbitration.status=='review'||arbitration.status=='auditFailure'||arbitration.status=='application'||arbitration.status=='inArbitration'}">
                                <a data-href="/app/wyjt/member/arbitrationRecordDetail?id=${arbitration.id}&appPlatform=${appPlatform}" class="goDetails">
                                    <div class="mycs" data-msg="1" data-id="">
                                        <p><fmt:formatDate value="${arbitration.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
                                            <font color="red" class="hkType">
                                                <c:if test="${arbitration.loan.repayType=='oneTimePrincipalAndInterest'}">
                                                   <span class='quane'>全额</span>
                                                </c:if>
                                                <c:if test="${arbitration.loan.repayType=='principalAndInterestByMonth'}">
                                                    <span class='fenqi'>分期</span>
                                                </c:if>
                                            </font>
                                        </p>
                                        <p><em class="zqr"></em>债权人：${arbitration.loan.loaner.name}</p>
                                        <p><em class="zwr"></em>债务人：${arbitration.loan.loanee.name}</p>
                                    </div>
                                </a>
                            </c:if>
                          </c:forEach>
                      </c:if>
                      <c:if test="${type=='0'}">
                            <c:forEach items="${collectionList}" var="collection">
	                            <c:if test="${collection.status=='auditing'||collection.status=='accepted'}">
	                                <a data-href="/app/wyjt/member/collectionRecordDetail?id=${collection.id}&appPlatform=${appPlatform}" class="goDetails">
	                                    <div class="mycs" data-msg="1" data-id="">
	                                        <p><fmt:formatDate value="${collection.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
	                                            <font color="red" class="hkType">
	                                                <c:if test="${collection.loan.repayType=='oneTimePrincipalAndInterest'}">
	                                                   <span class='quane'>全额</span>
	                                                </c:if>
	                                                <c:if test="${collection.loan.repayType=='principalAndInterestByMonth'}">
	                                                   <span class='fenqi'>分期</span>
	                                                </c:if>
	                                            </font>
	                                        </p>
	                                        <p><em class="zqr"></em>债权人：${collection.loan.loaner.name}</p>
	                                        <p><em class="zwr"></em>债务人：${collection.loan.loanee.name}</p>	
	                                    </div>
	                                </a>
	                            </c:if>
                        </c:forEach>
                    </c:if>
                    <c:if test="${type=='0'}">
                        <div class="shenqcs-tit">提醒已完成</div>
                    </c:if>
                    <c:if test="${type=='1'}">
                        <div class="shenqcs-tit">裁决已完成</div>
                    </c:if>
                    <c:if test="${type=='1'}">
                         <c:forEach items="${arbitrationList}" var="arbitration">             
	                            <c:if test="${arbitration.status=='failureToFile'||arbitration.status=='arbitrationFailure'||arbitration.status=='arbitration'}">
	                                <a data-href="/app/wyjt/member/arbitrationRecordDetail?id=${arbitration.id}&appPlatform=${appPlatform}" class="goDetails">
	                                    <div class="mycs" data-msg="1" data-id="">
	                                        <p><fmt:formatDate value="${arbitration.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
	                                            <font color="red" class="hkType">
	                                                <c:if test="${arbitration.loan.repayType=='oneTimePrincipalAndInterest'}">
	                                                    <span class='quane'>全额</span>
	                                                </c:if>
	                                                <c:if test="${arbitration.loan.repayType=='principalAndInterestByMonth'}">
	                                                    <span class='fenqi'>分期</span>
	                                                </c:if>
	                                            </font>
	                                        </p>
	                                        <p><em class="zqr"></em>债权人：${arbitration.loan.loaner.name}
	                                        </p>
	
	                                        <p>
	                                            <em class="zwr"></em>债务人：${arbitration.loan.loanee.name}
	                                        </p>
	
	                                    </div>
	                                </a>
	                            </c:if>
                            </c:forEach>
                      </c:if>
                      <c:if test="${type=='0'}">
                           <c:forEach items="${collectionList}" var="collection">
                            <c:if test="${collection.status=='refuse'||collection.status=='success'||collection.status=='fail'}">
                                <a data-href="/app/wyjt/member/collectionRecordDetail?id=${collection.id}&appPlatform=${appPlatform}" class="goDetails">
                                    <div class="mycs" data-msg="1" data-id="">
                                        <p><fmt:formatDate value="${collection.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
                                            <font color="red" class="hkType">
                                                <c:if test="${collection.loan.repayType=='oneTimePrincipalAndInterest'}">
                                                    <span class='quane'>全额</span>
                                                </c:if>
                                                <c:if test="${collection.loan.repayType=='principalAndInterestByMonth'}">
                                                    <span class='fenqi'>分期</span>
                                                </c:if>
                                            </font>
                                        </p>
                                        <p><em class="zqr"></em>债权人：${collection.loan.loaner.name}
                                        </p>
                                        <p><em class="zwr"></em>债务人：${collection.loan.loanee.name}
                                        </p>
                                    </div>
                                </a>
                            </c:if>                      
                        </c:forEach>
                    </c:if>
                    <div class="mycs-end" style="margin-top: 2rem;height: 5rem;">所有数据都在这儿了</div>
                </div>
            </div>
        </div>
    </section>
    <section class="panel" id="infoPanel" style="width: 100%">
        <div class="page-view theme-red">

                <div class="page-content">
                    <div class="page-center-middle" style="height: 100%">
                        <div class="preloader"></div>
                    </div>
                </div>

        </div>
    </section>
</article>
</body>
<script>
    window.panel = null;
    define('record', ['zepto', 'pop', 'panel', 'jumpApp'], function (require, exports, module) {
        var $ = require('zepto'), pop = require('pop'), call = require('jumpApp'), panel = require('panel');
        var ev = $.support.touch ? 'tap' : 'click', $btn1 = $('#submitBtn1'), $btn2 = $('#submitBtn2'),
            $btn3 = $('#submitBtn3'), $btn4 = $('#submitBtn4');

        $btn1[ev](function () {
            $("#navForm1").submit();
        });
        $btn2[ev](function () {
            $("#navForm2").submit();
        });
        $btn3[ev](function () {
            $("#navForm3").submit();
        });
        $btn4[ev](function () {
            $("#navForm4").submit();
        });

        $('.goDetails').live(ev, function () {
            var src = $(this).attr('data-href');
            var panel = $('#infoPanel').Panel({
                direction: 'right',
                src: src,
                callback: function () {
                    if (!this.isOpen) {
                        $(this.el).find('.page-content').empty()
                    }
                }
            });
            panel.open();
            return false;
        });
    });
    seajs.use(['record']);
</script>
</html>