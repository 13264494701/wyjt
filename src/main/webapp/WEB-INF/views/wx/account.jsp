<%@page import="com.teamshopping.teamshopping.util.SYSUtil" %>
<%@ page import="com.teamshopping.teamshopping.util.AppContext" %>
<%@ page import="com.teamshopping.teamshopping.dao.Dao" %>
<%@ page import="java.util.*" %>
<%@page contentType="text/html; charset=utf-8" %>
<%@include file="/wap/inc/function.jsp" %>
<%
    String docTitle = "我的借条";
    int pageNum = SYSUtil.getIntParameter(request, "pageNum", 1);
    Dao daoManager = (Dao) AppContext.getInstance().getAppContext().getBean("daoManager");
    TUser user = SsoWebLoginHolder.getUserLogIn(request, response);
    if (user == null) {
        String urlPath = request.getRequestURL().toString();
        String urlQuery = request.getQueryString();
        if (urlQuery != null) urlPath += "?" + request.getQueryString();
        if (!urlPath.equals("")) {
            urlPath = "?retUrl=" + URLEncoder.encode(urlPath);
        }
        response.sendRedirect("/wap/wechat/wx-login.jsp" + urlPath);
    }
    if (pageNum < 1)
        pageNum = 1;
    Integer size = 5;//固定每次查询5
    Integer from = (pageNum - 1) * size;

    List<TNewLoanCenter> tLoanCenter = daoManager.find("from TNewLoanCenter t where t.lenderId =" + user.getId() + " or t.borrowerId=" + user.getId() + " order by t.createDate desc", from, size);
    String userName = user.getUsername();//用户名
    String money = user.getRemainMoney().toString();//用户剩余金额
    String userIcon = user.getIconAddr();


%><!DOCTYPE html>
<html>
<head>
    <title><%=docTitle%>-无忧借条</title>
    <%@include file="/wap/inc/meta.jsp" %>
    <meta name="keywords" content="无忧借条"/>
    <meta name="description" content="无忧借条"/>
    <link rel="stylesheet" href="<%=httpUrl%>assets/css/public.css?v=<%=version%>" type="text/css">
    <link rel="stylesheet" href="<%=httpUrl%>assets/css/v1/wechat.css?v=<%=version%>" type="text/css">
    <script src="<%=httpUrl%>assets/scripts/sea-modules/seajs/2.3.0/sea.js?v=<%=version%>"></script>
    <script src="<%=httpUrl%>assets/scripts/base/flexible.js?v=<%=version%>"></script>
</head>

<body<%=appPlatform.equals("") ? "" : " class=\"" + (isWeiXin ? "InWeixin" : "InWebView") + "\" data-platform=\"" + appPlatform + "\""%>>
<article class="views docBody">
    <section class="view">
        <jsp:include page="../inc/header-public.jsp">
            <jsp:param name="title" value="<%=docTitle%>"></jsp:param>
        </jsp:include>
        <div class="pages navbar-fixed">
            <div class="page">
                <div class="page-content" style=" background-color: #d9d9d9;">
                    <div id="appendMsg" class="account51">
                        <div class="logo"></div>
                        <div class="redtitle">我的账户</div>
                        <div class="zhboxbg">
                            <div class="zhbox clearfix">
                                <div class="img"><img src="<%=userIcon%>"></div>
                                <div class="xq">
                                    <p>欢迎您，<span><%=userName%></span>&nbsp;&nbsp;&nbsp;<a href="/user/logout.jsp?retUrl=/wap/wechat/wx-login.jsp">退出</a></p>

                                    <p>现金余额：<b><%=money%>
                                    </b>元 </p>
                                </div>
                            </div>
                        </div>
                        <div class="redtitle">我的借条</div>
                        <%
                            if (tLoanCenter != null && tLoanCenter.size() > 0) {
                                for (TNewLoanCenter center : tLoanCenter) {
                                    String createDate = center.getCreateDate() == null ?BaseUtils.getCurrentDateByString(): DateUtils.sdf_ymd.format(center.getCreateDate());
                        %>
                        <div class="jtbox">
                            <div class="boxcon">
                                <div class="top"><p>单据编号：<span><%=center.getSerialNo()%></span></p></div>
                                <div class="borloan">
                                    <p><span class="jkicon"></span>放款人：<%=center.getLenderRealname()%>
                                    </p>

                                    <p><span class="fkicon"></span>借款人：<%=center.getBorrowerRealname()%>
                                    </p>
                                    <b><%=createDate%>
                                    </b>
                                </div>
                            </div>
                        </div>
                        <%
                            }
                        } else {%>
                        <div class="jt-nojt"></div>
                        <%
                            }
                            if (tLoanCenter.size() == 5) {
                        %>
                        <div id="clickMore" class="jt-foot">点击加载更多</div>
                        <%
                        } else if (tLoanCenter.size() >= 0 && tLoanCenter.size() < 5) {
                        %>
                        <div class="jt-foot">已加载所有数据</div>
                        <%
                            }%>

                    </div>
                </div>
            </div>
        </div>
    </section>
</article>
<jsp:include page="../inc/footer.jsp"></jsp:include>
<script>
    define('account', ['zepto', "core", "pop"], function (require, exports, module) {
        var $ = require('zepto'), core = require("core"), ev = $.support.touch ? 'tap' : 'click', dialog = require('pop');
        var $clickMore = $("#clickMore"), isEnd = false, pageNum = 1;
        $clickMore[ev](function (e) {
            if (isEnd) {
                return false; //表示已经加载完毕，不在发送请求
            }
            core.post("accountMoreData.jsp", {"pageNum": pageNum}, function (error, rel) {
                if (!error) {
                    if ($.trim(rel) === '') {
                        isEnd = true;
                        $clickMore.html("已加载所有数据").unbind();
                    }
                    else {
                        pageNum++;
                        $(rel).insertBefore($clickMore)
                    }
                } else dialog.alert('网络原因提交失败。');
            });
        })
    });
    seajs.use("account");
</script>
</body>
</html>