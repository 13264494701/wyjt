<%@ page contentType="text/html;charset=UTF-8" %>
<%@include file="/WEB-INF/views/app/constant.jsp" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <title>我要借款</title>
    <%@include file="/WEB-INF/views/include/head.jsp" %>
    <link rel="stylesheet" href="${mbStatic}/assets/css/public.css?v=<%=version%>" type="text/css">
    <link rel="stylesheet" href="${mbStatic}/assets/css/repay.css?v=<%=version%>" type="text/css">
    <script src="${mbStatic}/assets/scripts/base/sea.js?v=<%=version%>"></script>
    <script src="${mbStatic}/assets/scripts/base/flexible.js?v=<%=version%>"></script>
    <style>
        .listbox {
            border: 0;
        }

        .listbox:after {
            background: none;
        }

        .paysucessed p.p4 {
            margin: 0 0 0 2.68rem;
        }

        .paysucessed img {
            width: 5rem;
        }
    </style>
</head>
<body>
<article class="views" style="max-width:100%">
    <section class="view">

        <div class="pages navbar-fixed">
            <div class="page">
                <div class="page-content" style="background: white;padding-top:0;">
                    <!--content -->
                    <div class="bg" style="background: white;padding-top:0;">
                        <div class="listbox">
                            <div class="paysucessed">
                                <c:if test="${status==1}">
                                    <p style="margin-top: 3rem;"><img
                                            src="${mbStatic}/assets/images/debt/success@2x.png"></p>
                                    <p style="font-size: 1.2rem;margin-top: 1rem;margin-bottom: .3rem;font-weight: bold">
                                        提交成功</p>
                                    <p style="margin:2rem;font-size: 1rem;color:red;">您已申请成功，通过后会有第三方短信<br/>或电话联系您，请保持手机畅通~
                                    </p>
                                </c:if>
                                <c:if test="${status==0}">
                                    <p style="margin-top: 3rem;"><img
                                            src="${mbStatic}/assets/images/debt/err@2x.png"></p>
                                    <p style="font-size: 1.2rem;margin-top: 1rem;margin-bottom: .3rem;font-weight: bold">
                                        提交失败</p>
                                    <p style="margin:2rem;font-size: 1rem;color:red;">7天内请勿重复申请，通过后会有第三方短信<br/>或电话联系您，请保持手机畅通~
                                    </p>
                                </c:if>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </section>
</article>
</body>
<script>
    seajs.use('jumpApp');
</script>
</html>

