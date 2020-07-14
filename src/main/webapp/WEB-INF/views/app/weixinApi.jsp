<%@ page import="com.jxf.svc.utils.RandomUtils" %>
<%@ page import="com.jxf.wx.ticket.AppJsApiTicket" %>
<%@ page import="com.jxf.svc.utils.MySecurity" %>
<%
	String url = request.getScheme() + "://";
    url += request.getHeader("host");
    url += "/app/wyjt/common/shareCreditRating";
    if (request.getQueryString() != null)
        url += "?" + request.getQueryString();
    if (url.indexOf("#") > -1) url = url.substring(0, url.indexOf("#"));

    String jsapi_ticket = AppJsApiTicket.getJsapiticket();
    String noncestr = RandomUtils.generateNumString(8);
    Long timestamp = System.currentTimeMillis() / 1000;

    StringBuffer sb = new StringBuffer();
    sb.append("jsapi_ticket=" + jsapi_ticket);
    sb.append("&noncestr=" + noncestr);
    sb.append("&timestamp=" + timestamp);
    sb.append("&url=" + url);

    String signature = new MySecurity().encode(sb.toString(), MySecurity.SHA_1);
%>
<script>
    function getWxConfig(config) {
        config = config || ['checkJsApi', 'onMenuShareTimeline', 'onMenuShareAppMessage', 'onMenuShareQQ', 'hideOptionMenu', 'showOptionMenu'];
        return {
            debug: false,
            appId: 'wxf017e9bab572627f',
            timestamp: '<%=timestamp%>',
            nonceStr: '<%=noncestr%>',
            signature: '<%=signature%>',
            jsApiList: config
        }
    }
</script>
