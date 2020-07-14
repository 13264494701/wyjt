<%@ page import="java.text.SimpleDateFormat"%>
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
            <jsp:param name="title" value="无忧借条-信用评级"></jsp:param>
            <jsp:param name="url" value="back"></jsp:param>
        </jsp:include>
        <div class="pages navbar-fixed">
            <div class="page">
                <div class="page-content ${className}">

                        <div class="dj-top">

                               <div class="user-icon"><img src="${member.headImage}" style="width: 100%;display: block;"></div>
                            <div class="user-txt">
                                <p>${member.name}</p>
                                <p>我在无忧借条的信用等级已达到<span>${member.memberRank.rankNo}</span>级啦！</p>
                            </div>

                        </div>
                        <div class="dj-con">
                            <div class="top-t">
                                <div class="tit"></div>
                                <div class="txt" id="levelTitle">${tips}</div>
                            </div>

                            <c:if test="${member.memberRank.rankNo=='B'}">
                                <div class="circles-zero"><img src="${mbStatic}/assets/images/hall/circle-b.png?"></div>
                            </c:if>
                            <c:if test="${member.memberRank.rankNo=='C'}">
                                <div class="circles-zero"><img src="${mbStatic}/assets/images/hall/circle-c.png?"></div>
                            </c:if>
                            <c:if test="${member.memberRank.rankNo!='B'}">
                                <c:if test="${member.memberRank.rankNo!='C'}">
                                <div class="circles" id="circles">
                                    <div class="circle"></div>
                                </div>
                                </c:if>
                            </c:if>

                            <div class="tablediscp">
                                <p class="p1" id="xydj">信用等级</p>

                                <p class="p2" style="color: #fff;font-weight: 400">${member.memberRank.rankNo}</p>

                                <p class="p1">${tips1}</p>

                                <p class="p4">评估时间：<%=sdf.format(Calendar.getInstance().getTime())%>

                                <p class="p3" style="display: none"><span>${tips2}</span></p>

                            </div>
                        </div>
                        <div class="djfoot">
                            <div class="foot-img"></div>
                            <div class=" foot-con">
                                <p>我在无忧借条等你来战！</p>
                                <a href="/app/wyjt/common/callApp">速去挑战</a>
                            </div>
                        </div>
                </div>
            </div>
        </div>
    </section>
</article>
<script type="text/javascript">
    seajs.use(['zepto', 'tools/circle-progress'], function ($) {
        var level = '0.0', creditDegreeLevel = '${member.memberRank.rankNo}';

        getLevel();
        drawCanvas();

        function getLevel() {
            var creditDegreeLevelArr = ['B', 'A', 'AA', 'AAA', 'AAAA', 'AAAAA'];
            if (creditDegreeLevel == 'AAAAA') {
                level = '1';
            }
            else {
                var levelPersentDouble = parseFloat('50');
                isNaN(levelPersentDouble) && (levelPersentDouble = 0);
                var inx = $.inArray(creditDegreeLevel, creditDegreeLevelArr);
                if (inx) {
                    level = (inx + levelPersentDouble)/ (creditDegreeLevelArr.length - 1);
                }
            }
            console.log(level);
        }

        function drawCanvas() {
            var a = new Image(), b = new Image;
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

<%
	String wxShareDes = "诚信时代，无忧借条为你记录信用，传承美德！";
%>
<%@include file="../weixinApi.jsp" %>
<script>
    function getWeixinShareData() {
        return {
            title: '人生新高度！我的信用已达到${member.memberRank.rankNo}级啦！',
            link: '/app/wyjt/common/shareCreditRating?type=weChat&id=${member.id}&appPlatform=${appPlatform}',
            desc: '<%=wxShareDes%>',
            imgUrl: '${mbStatic}/assets/images/yxb-icon1_1x.png',
            bgUrl: '${mbStatic}/assets/images/share-webchat.png'
        }
    }
    seajs.use('base/shareWeixin', function (wx) {
        var weixin = wx.wx;
        weixin.ready(function () {
            wx.register(getWeixinShareData());
        });
    });
</script>
</body>
</html>
