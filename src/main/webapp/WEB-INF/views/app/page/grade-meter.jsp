<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.util.Calendar" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<%@include file="/WEB-INF/views/app/constant.jsp" %>
<%
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
%>
<!DOCTYPE html>
<html>
<head>
    <title>信用评级-无忧借条</title>
    <%@include file="../meta-flex.jsp" %>
    <link rel="stylesheet" href="${mbStatic}/assets/css/public.css?v=<%=version%>" type="text/css">
    <link rel="stylesheet" href="${mbStatic}/assets/css/h5/credit-grade.css?v=<%=version%>" type="text/css">
    <script src="${mbStatic}/assets/scripts/base/sea.js?v=<%=version%>"></script>
    <script src="${mbStatic}/assets/scripts/base/flexible.js?v=<%=version%>"></script>
</head>
<c:choose>
<c:when test="${empty appPlatform}">
<body>
</c:when>
<c:otherwise>
<body class="${isWeiXin? 'InWeixin':'InWebView'}" data-platform="${appPlatform}" data-app="51jt">
</c:otherwise>
</c:choose>
<article class="views docBody">
    <section class="view">
        <jsp:include page="../header-public.jsp">
            <jsp:param name="title" value="信用评级"></jsp:param>
            <jsp:param name="url" value="back2app"></jsp:param>
            <jsp:param name="type" value="grademeter"></jsp:param>
        </jsp:include>

        <div class="pages navbar-fixed ">
            <div class="page">
                <div class="page-content ${className}">

                    <div class="dj-top2"><p class="big-font">
                        ${title}
                    </p></div>
                    <div class="dj-con">
                        <div class="top-t">
                            <div class="tit"></div>
                            <div class="txt" id="levelTitle">${tips}</div>
                        </div>

                        <c:if test="${member.memberRank.rankNo=='B'}">
                            <div class="circles-zero"><img src="${mbStatic}/assets/images/hall/circle-b.png?"></div>
                        </c:if>

                        <c:if test="${member.memberRank.rankNo!='B'}">
                            <div class="circles" id="circles">
                                <div class="circle"></div>
                            </div>
                        </c:if>

                        <div class="tablediscp">
                            <p class="p1" id="xydj">信用等级</p>

                            <p class="p2" style="color: #fff;font-weight: 400">${member.memberRank.rankNo}</p>

                            <p class="p1">${tips1}</p>

                            <p class="p4">评估时间：<%=sdf.format(Calendar.getInstance().getTime())%>

                            <p class="p3" style="display: none"><span>${tips2}</span></p>

                        </div>
                        <c:if test="${member.memberRank.rankNo!='B'}">
                            <c:if test="${member.memberRank.rankNo!='C'}">
                                <div class="djfoot2">
                                    <a class="but4" href="javascript:void 0" data-method="share"
                                       data-parameter='{"title":"人生新高度！我的信用已达到${member.memberRank.rankNo}级啦！","type":"weChatFriend",  "link":"https://${pageContext.request.serverName}/app/wyjt/common/shareCreditRating?id=${member.id}&appPlatform=${appPlatform}","imgUrl":"https://${pageContext.request.serverName}${mbStatic}/assets/images/yxb-icon1_1x.png", "desc":"诚信时代，无忧借条为你记录信用，传承美德！"}'></a>
                                    <a class="but2" href="javascript:void 0" data-method="share"
                                       data-parameter='{"title":"人生新高度！我的信用已达到${member.memberRank.rankNo}级啦！","type":"weChat",  "link":"https://${pageContext.request.serverName}/app/wyjt/common/shareCreditRating?id=${member.id}&appPlatform=${appPlatform}","imgUrl":"https://${pageContext.request.serverName}${mbStatic}/assets/images/yxb-icon1_1x.png", "desc":"诚信时代，无忧借条为你记录信用，传承美德！"}'></a>

                                </div>
                            </c:if>
                        </c:if>
                    </div>

                </div>
            </div>
        </div>
    </section>
</article>

<script type="text/javascript">
    seajs.use(['zepto', 'tools/circle-progress', 'jumpApp'], function ($) {
        var a = new Image(), b = new Image;
        var level = '0.0';
        var creditDegreeLevel = "${member.memberRank.rankNo}";
        if(!(creditDegreeLevel=="B")){
            getLevel();
            drawCanvas();
        }
        function getLevel() {
            switch(creditDegreeLevel){
                case "A":
                    level = 0.2;
                    break;
                case "AA":
                    level = 0.4;
                    break;
                case "AAA":
                    level = 0.6;
                    break;
                case "AAAA":
                    level = 0.8;
                    break;
                case "AAAAA":
                    level = 1;
                    break;
                default:
                    level = 0;
                    break;
            }
        }

        function drawCanvas() {
            a.src = '${mbStatic}/assets/images/hall/circle.png';
            a.onload = function () {
                b.src = '${mbStatic}/assets/images/hall/circle-bg.png?';
            };
            b.onload = function () {
                var $circle = $('.circle'), width = $circle.width();
                $circle.circleProgress({
                    value: level,
                    size: width,
                    thickness: 30 * rem / 12,
                    startAngle: -Math.PI - (Math.PI / 8),
                    fill: {
                        color: 'rgba(0, 0, 0, 0)',
                        image: a,
                        fillImg: b
                    },
                    textFile: $('#textFile')
                })
            };
        }
    });
</script>
</body>
</html>
