<%@page import="com.teamshopping.teamshopping.util.SYSUtil" %>
<%@ page import="com.teamshopping.teamshopping.util.AppContext" %>
<%@ page import="com.teamshopping.teamshopping.dao.Dao" %>
<%@ page import="java.util.*" %>
<%@ page import="net.sf.json.JSONObject" %>
<%@page contentType="text/html; charset=utf-8" %>
<%@include file="/wap/inc/function.jsp" %>
<%
    TUserManager userManager = (TUserManager) (AppContext.getInstance().getAppContext().getBean("TUserManager"));
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
    int pageNum = SYSUtil.getIntParameter(request, "pageNum", 1);
//    String userId = request.getParameter("userId");
//    TUser user = userManager.checkUserInfo(Integer.valueOf(userId));
    Dao daoManager = (Dao) AppContext.getInstance().getAppContext().getBean("daoManager");
    if (pageNum < 1)
        pageNum = 1;
    Integer size = 5;//固定每次查询5
    Integer from = (pageNum - 1) * size;

    ArrayList<TLoanCenter> tLoanCenters = new ArrayList<TLoanCenter>();//借款单数据
    List list = daoManager.find("from TLoanCenter t where t.lenderId =" + user.getId() + " or t.borrowerId=" + user.getId() + " order by t.createDate desc", from, size);
    if (list != null && list.size() > 0) {
        tLoanCenters = (ArrayList<TLoanCenter>) list;
    }
    String result = "";
    if (tLoanCenters.size() > 0) {
        for (TLoanCenter data : tLoanCenters) {
            result += " <div class=\"jtbox\">";
            result += "<div class=\"boxcon\">";
            result += "<div class=\"top\"><p>单据编号：<span>" + data.getSerialNo() + "</span></p></div>";
            result += "<div class=\"borloan\">";
            result += "<p><span class=\"jkicon\"></span>放款人：" + data.getLenderRealname() + "</p>";
            result += "<p><span class=\"fkicon\"></span>借款人：" + data.getBorrowerRealname() + "</p>";
            result += "<b>" + DateUtils.sdf_ymd.format(data.getCreateDate()) + "</b>";
            result += "</div>";
            result += "</div>";
            result += "</div>";
        }
    }
    //System.out.println(result);
    HashMap<String, Object> map = new HashMap<String, Object>();
    map.put("success", true);
    map.put("msg", result);
    out.print(result);
%>

