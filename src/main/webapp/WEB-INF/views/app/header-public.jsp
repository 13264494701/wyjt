<%@ page import="java.util.HashMap" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%@ include file="/WEB-INF/views/app/constant.jsp" %> 
<%!
    private static String getHeaderLink(String type, String webPath) {
        HashMap<String, String> linkHrefs = new HashMap<String, String>();
        linkHrefs.put("usercenter", "<a href=\"" + webPath + "\" class=\"link icon-only\"><i class=\"icon usercenter\"></i></a>");
        linkHrefs.put("reminder", "<a href=\"javascript:void 0\" class=\"link\"  id=\"sendReminder\" >发送</a>");
        linkHrefs.put("taskrule", "<a href=\"javascript:void 0\" class=\"link\" id=\"ruleBtn\" >活动规则</a>");
        linkHrefs.put("withdraw", "<a href=\"withdraw.jsp\" class=\"link\" id=\"ruleBtn\" >立即提现</a>");
        linkHrefs.put("tx_withdrawList", "<a href=\"javascript:void 0\" class=\"link\" id=\"recordBtn\" >提现记录</a>");
//        <i style="background:url(../../wap/assets/images/debt/note.png) no-repeat;background-size: 100%;display: inline-block;width: 1.41667rem;height: 1.54rem;"></i>
        linkHrefs.put("deptList", "<a href=\"my-list.jsp\" class=\"link\" id=\"ruleBtn\" ><span style=\"font-size: 1.2rem;padding-top: 0.3rem;\">催收记录</span></a>");
//        linkHrefs.put("buyoneNext", "<a href=\"buy-one.jsp\" class=\"link\" id=\"ruleBtn\" ></a>");
        return linkHrefs.containsKey(type) ? linkHrefs.get(type) : "<a href=\"javascript:\" class=\"link icon-only\"></a>";
    }
%>
<% 
    String title = request.getParameter("title");
    String url = request.getParameter("url");
    String type = request.getParameter("type");
//    String pid = request.getParameter("pid");
    if (title == null) title = "";
    if (url == null) url = "";
    if (type == null) type = "";
    if (headerReferer != null && headerReferer.equals("") && url.equals("")) {
        url = "back2app";
    }
%>
<noscript>
    <div id="noScript">请开启浏览器的Javascript功能，或使用支持javascript的浏览器访问</div>
</noscript>
<script>
    ~function (seajs) {
    seajs.use('core', function (core) {
        core && core.configInit("", "${member.id}", "${member.username}", "2018-11-05", "3.44");
    });}(window['seajs']);
</script>
<style>
    .navbar-inner, .toolbar-inner{
        font-size:1.25rem;
        padding: 0 1.2rem;
    }
    .navbar .right{
        margin-left:0;
    }
    .navbar h1{
        font-weight:normal;
        font-size: 1.55rem;
    }
</style>
<header class="navbar">
    <div class="navbar-inner">
		<div class="left">
		<%if (url.equals("")) {%><a class="goBack link" href="javascript:void(0);" data-method="go" data-href="back" target="_self"><i class="icon icon-back"></i><span></span></a><%} 
		else if (url.equals("back2app")) {%><a class="goBack link" href="javascript:void 0" data-method="go" data-href="back2app"><i class="icon icon-back"></i><span></span></a><%}
	    else{%><a class="goBack link" href="javascript:void 0" cp-url="<%=url%>"><i class="icon icon-back"></i><span></span></a><%}%></div>
        <div class="center"><h1><%=title%></h1></div>
        <div class="right"><%=getHeaderLink(type,"/wap/")%></div>
    </div>
</header>
