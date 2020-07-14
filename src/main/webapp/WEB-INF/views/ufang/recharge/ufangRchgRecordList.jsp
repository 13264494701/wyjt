<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<html>
<head>
    <title>充值记录</title>
    <meta name="decorator" content="default"/>
    <script type="text/javascript">
        $(document).ready(function () {

        });

        function page(n, s) {
            $("#pageNo").val(n);
            $("#pageSize").val(s);
            $("#searchForm").submit();
            return false;
        }
    </script>
</head>
<body>
<ul class="nav nav-tabs">
    <li class="active"><a href="${ufang}/recharge/">充值记录</a></li>
</ul>
<form:form id="searchForm" modelAttribute="ufangRchgRecord" action="${ufang}/recharge/" method="post"
           class="breadcrumb form-search">
    <input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
    <input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
    <ul class="ul-form">
            <%--<li><label style="text-align:right;width:150px">充值客户号：</label>--%>
            <%--<sys:treeselect id="user" name="user.id" value="${ufangRchgRecord.user.id}" labelName="user.name" labelValue="${ufangRchgRecord.user.name}"--%>
            <%--title="用户" baseUrl="${ufang}" url="/sys/office/treeData?type=3" cssClass="input-small" allowClear="true" notAllowSelectParent="true"/>--%>
            <%--</li>--%>
            <%--<li><label style="text-align:right;width:150px">支付单id：</label>--%>
            <%--<form:input path="payment.id" htmlEscape="false" maxlength="20" class="input-medium"/>--%>
            <%--</li>--%>
            <%--<li><label style="text-align:right;width:150px">充值方式：</label>--%>
            <%--<form:input path="type" htmlEscape="false" maxlength="4" class="input-medium"/>--%>
            <%--</li>--%>
            <%--<li><label style="text-align:right;width:150px">充值渠道：</label>--%>
            <%--<form:input path="channel" htmlEscape="false" maxlength="4" class="input-medium"/>--%>
            <%--</li>--%>
            <%--<li><label style="text-align:right;width:150px">交易状态：</label>--%>
            <%--<form:input path="status" htmlEscape="false" maxlength="4" class="input-medium"/>--%>
            <%--</li>--%>

        <%--<li><label style="text-align:right;width:80px">充值时间：</label>--%>
            <%--<input name="beginTime" type="text" maxlength="20" class="input-medium Wdate"--%>
                   <%--value="<fmt:formatDate value="${nfsLoanRecord.beginTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"--%>
                   <%--onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:true});"/> ---%>
            <%--<input name="endTime" type="text" maxlength="20" class="input-medium Wdate"--%>
                   <%--value="<fmt:formatDate value="${nfsLoanRecord.endTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"--%>
                   <%--onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:true});"/>--%>
        <%--</li>--%>
        <%--<li class="btns">--%>
            <%--<label style="text-align:right;width:50px"></label>--%>
            <%--<input id="btnSubmit" class="btn btn-primary " style="width:80px;" type="submit" value="查   询"/></li>--%>
        <%--<li class="clearfix"></li>--%>
    </ul>
</form:form>
<sys:message content="${message}"/>
<table id="contentTable" class="table table-striped table-bordered table-condensed">
    <thead>
    <tr>
        <th style="text-align:center">充值员工姓名</th>
        <th style="text-align:center">充值金额</th>
        <%--<th style="text-align:center">充值方式</th>--%>
        <%--<th style="text-align:center">充值渠道</th>--%>
        <th style="text-align:center">充值状态</th>
        <th style="text-align:center">充值时间</th>
        <%--<shiro:hasPermission name="recharge:ufangRchgRecord:edit"><th style="text-align:center">操作</th></shiro:hasPermission>--%>
    </tr>
    </thead>
    <tbody>
    <c:forEach items="${page.list}" var="ufangRchgRecord">
        <tr>
            <td style="text-align:center">
                    ${ufangRchgRecord.user.empNam}
            </td>
            <td style="text-align:center">
                    ${ufang:decimalToStr(ufangRchgRecord.amount,2)}
            </td>
                <%--<td style="text-align:center">--%>
                <%--${ufangRchgRecord.type}--%>
                <%--</td>--%>
                <%--<td style="text-align:center">--%>
                <%--${ufangRchgRecord.channel}--%>
                <%--</td>--%>
            <td style="text-align:center">
                    ${fns:getDictLabel(ufangRchgRecord.status, 'rechargeStatus', '')}
            </td>
            <td style="text-align:center">
                <fmt:formatDate value="${ufangRchgRecord.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
            </td>
                <%--<td  style="text-align:center">
                    <shiro:hasPermission name="recharge:ufangRchgRecord:edit">--%>
                <%--<a href="${ufang}/recharge/update?id=${ufangRchgRecord.id}">修改</a>--%>
                <%--<a href="${ufang}/recharge/delete?id=${ufangRchgRecord.id}" onclick="return confirmx('确认要删除该优放充值记录吗？', this.href)">删除</a>--%>
                <%--</shiro:hasPermission>
                </td>--%>
        </tr>
    </c:forEach>
    </tbody>
</table>
<div class="pagination">${page}</div>
</body>
</html>