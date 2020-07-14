<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<html>
<head>
    <title>流量管理管理</title>
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
	<c:if test="${prodCode eq '001'}"><li class="active"><a href="${ufang}/ufangLoaneeData/?prodCode=001">优享流量</a></li>
    <li><p style="color: red;line-height: 20px;padding-top: 8px; padding-bottom: 8px;padding-left: 8px;">1-7天内，在平台申请借款的用户。经用户授权，平台根据芝麻分高低进行分层，分值越高，质量越好。
购买后不得非法出售或非法向他人提供，如若发生纠纷，我方不承担法律责任</p></li>
    </c:if>
    <c:if test="${prodCode eq '002'}"><li class="active"><a href="${ufang}/ufangLoaneeData/?prodCode=002">优淘流量</a></li>
        <li><p style="color: red;line-height: 20px;padding-top: 8px; padding-bottom: 8px;padding-left: 8px;">经用户授权，平台根据风险画像评估的“风险等级较低”“贷后行为良好”的优质借款用户。
购买后不得非法出售或非法向他人提供，如若发生纠纷，我方不承担法律责任</p></li>
    </c:if>
	<c:if test="${prodCode eq '003'}"><li class="active"><a href="${ufang}/ufangLoaneeData/?prodCode=002">特价流量</a></li>
	    <li><p style="color: red;line-height: 20px;padding-top: 8px; padding-bottom: 8px;padding-left: 8px;">8-30天内，在平台申请借款的用户。经用户授权，平台根据芝麻分高低进行分层，分值越高，质量越好。
购买后不得非法出售或非法向他人提供，如若发生纠纷，我方不承担法律责任。</p></li>
	</c:if>
</ul>
<form:form id="searchForm" modelAttribute="ufangLoaneeData" action="${ufang}/ufangLoaneeData/?prodCode=${prodCode}" method="post"
           class="breadcrumb form-search">
    <input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
    <input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
    <ul class="ul-form">
        <li><label style="text-align:right;width:100px">年龄：</label>
            <form:input path="minAge" htmlEscape="false" maxlength="3" class="input-medium" cssStyle="width: 30px"/>-
            <form:input path="maxAge" htmlEscape="false" maxlength="3" class="input-medium" cssStyle="width: 30px"/>
        </li>
        <c:if test="${prodCode eq '001'||prodCode eq '003'}">
	        <li><label style="text-align:right;width:100px">芝麻分：</label>
	            <form:input path="minZmf" htmlEscape="false" maxlength="4" class="input-medium" cssStyle="width:30px"/>-
	            <form:input path="maxZmf" htmlEscape="false" maxlength="4" class="input-medium" cssStyle="width:30px"/>
	        </li>
	        <li><label style="text-align:right;width:100px">申请时间：</label>
					<input name="beginApplyTime" type="text"  maxlength="20" class="input-medium Wdate required"
						value="<fmt:formatDate value="${ufangLoaneeData.beginApplyTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"
						onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',startDate:'%y-%M-%d 00:00:00',isShowClear:true});"/>
					<label style="text-align:center;width:30px">至：</label>
					<input name="endApplyTime" type="text"  maxlength="20" class="input-medium Wdate required"
						value="<fmt:formatDate value="${ufangLoaneeData.endApplyTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"
						onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',startDate:'%y-%M-%d 00:00:00',isShowClear:true});"/>
			</li>
        </c:if>

        <c:if test="${prodCode eq '002'}">
			<li><label style="text-align:right;width:100px">更新时间：</label>
					<input name="beginUpdateTime" type="text"  maxlength="20" class="input-medium Wdate required"
						value="<fmt:formatDate value="${ufangLoaneeData.beginUpdateTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"
						onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',startDate:'%y-%M-%d 00:00:00',isShowClear:true});"/>
					<label style="text-align:center;width:30px">至：</label>
					<input name="endUpdateTime" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate required"
						value="<fmt:formatDate value="${ufangLoaneeData.endUpdateTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"
						onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',startDate:'%y-%M-%d 00:00:00',isShowClear:true});"/>
			</li>
		</c:if>
		<li><label style="text-align:right;width:100px">所在地区：</label>
			<form:input path="applyArea" htmlEscape="false" maxlength="64" class="input-medium"/>
	    </li>
	    <li><label style="text-align:right;width:100px">价格：</label>
            <form:input path="minPrice" htmlEscape="false" maxlength="3" class="input-medium" cssStyle="width: 30px"/>-
            <form:input path="maxPrice" htmlEscape="false" maxlength="3" class="input-medium" cssStyle="width: 30px"/>
        </li>
        <li class="btns">
            <label style="text-align:right;width:50px"></label>
            <input id="btnSubmit" class="btn btn-primary " style="width:80px;" type="submit" value="查   询"/>
        </li>
        <li class="clearfix"></li>
    </ul>
</form:form>
<sys:message content="${message}"/>
<table id="contentTable" class="table table-striped table-bordered table-condensed">
    <thead>
    <tr>
        <th style="text-align:center"><input type="checkbox" onclick="allchoose()" class="allchoose">全选 借款人姓名</th>
        <th style="text-align:center">手机号码</th>
        <th style="text-align:center">年龄</th>
        <th style="text-align:center">性别</th>
        <c:if test="${prodCode eq '001'||prodCode eq '003'}">
        <th style="text-align:center">芝麻分</th>
        </c:if>
        <c:if test="${channel eq 'weixin'}">
			 <th style="text-align:center">QQ号码</th>
             <th style="text-align:center">微信账号</th>
		</c:if>
        <c:if test="${prodCode eq '001'||prodCode eq '003'}">
			<th style="text-align:center">运营商状态</th> 
		</c:if>
        <th style="text-align:center">申请金额</th>
        <th style="text-align:center">价格</th>
        <c:if test="${prodCode eq '001'||prodCode eq '003'}">
        <th style="text-align:center">申请IP归属</th>
        </c:if>
        <c:if test="${prodCode eq '002'}">
        <th style="text-align:center">登录IP归属</th>
        </c:if>
        <th style="text-align:center">手机号归属</th>
        <c:if test="${prodCode eq '001'||prodCode eq '003'}">
        <th style="text-align:center">申请时间</th>
        </c:if>
        <c:if test="${prodCode eq '002'}">
        <th style="text-align:center">更新时间</th>
        </c:if>
        <th style="text-align:center">操作</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach items="${page.list}" var="ufangLoaneeData" varStatus="status">
        <tr>
            <td style="text-align:center">
                <input type="checkbox" onchange="choose('${ufangLoaneeData.id}',${status.index})" class="choose"/>
                    ${ufangLoaneeData.name}
            </td>
            <td style="text-align:center">
                    ${ufangLoaneeData.phoneNo}
            </td>
            <td style="text-align:center">
                    ${ufangLoaneeData.age}
            </td>
            <td style="text-align:center">
            		<c:choose>
						<c:when test="${ufang:substring(ufangLoaneeData.idNo,16, 17)%2 == 1}">
							男
						</c:when>
						<c:when test="${ufang:substring(ufangLoaneeData.idNo,16, 17)%2 == 0}">
							女
						</c:when>
					</c:choose>	               
            </td>
            <c:if test="${prodCode eq '001'||prodCode eq '003'}">
            <td style="text-align:center">
                    ${ufangLoaneeData.zhimafen}
            </td>
            </c:if>
            <c:if test="${channel eq 'weixin'}">
              <td style="text-align:center">
                    ${ufangLoaneeData.qqNo}
              </td>
              <td style="text-align:center">
                    ${ufangLoaneeData.weixinNo}
              </td>
            </c:if>
            <c:if test="${prodCode eq '001'||prodCode eq '003'}">
               <td style="text-align:center">
                   ${fns:getDictLabel(ufangLoaneeData.yunyingshangStatus, 'YunyingshangStatus', '')}
               </td> 
            </c:if>
            <td style="text-align:center">
                    ${ufang:decimalToStr(ufangLoaneeData.applyAmount,2)}
            </td>
            <td style="text-align:center">
                    ${ufang:decimalToStr(ufangLoaneeData.price,2)}
            </td>
            <td style="text-align:center">
					${ufangLoaneeData.applyArea}
			</td>
			<td style="text-align:center">
					${ufangLoaneeData.phoneArea}
			</td>
			<c:if test="${prodCode eq '001'||prodCode eq '003'}">
            <td style="text-align:center">
                <fmt:formatDate value="${ufangLoaneeData.applyTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
            </td>
            </c:if>
            <c:if test="${prodCode eq '002'}">
            <td style="text-align:center">
                <fmt:formatDate value="${ufangLoaneeData.updateTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
            </td>
            </c:if>
            <td style="text-align:center">             
                    <a href="${ufang}/ufangLoaneeData/buy?id=${ufangLoaneeData.id}&prodCode=${prodCode}"
                       onclick="return confirmx('确认花费${ufang:decimalToStr(ufangLoaneeData.price,2)}购买该流量吗？如有免费流量条数会优先扣除', this.href)">购买</a>  
            </td>
        </tr>
    </c:forEach>
    </tbody>
</table>
<button class="btn btn-primary" style="width: 144px" onclick="buy()">批量购买</button>
<div class="pagination">${page}</div>
<script type="text/javascript">
    var chooseList = [];

    function choose(id, index) {
        if (chooseList[index] == null) {
            chooseList[index] = id
        } else {
            chooseList[index] = null
        }
    }

    function buy() {
        var buyList = [];
        var channel = "${channel}";
        var count = 0;
        for (var i = 0; i < chooseList.length; i++) {
            if (chooseList[i] != null) {
                buyList.push(chooseList[i]);
                count = count + 1
            }
        }
        if (count == 0) {
            return false
        }
        var result = confirm('选择了' + count + '条流量，确定批量购买吗');
        if (result == true) {
            jQuery.ajaxSettings.traditional = true;
            $.post('${ufang}/ufangLoaneeData/batchBuy', {buyList: buyList}, function (res) {
                loading(res);
                setTimeout(function () {
                    location.href = "${ufang}/ufangLoaneeData?message=" + res +"&prodCode=" + prodCode;
                }, 1000);
            })
        }
    }
    
    function allchoose() {
        if (chooseList.length != 0) {
            $(".choose").attr("checked", false).change();
            $(".allchoose").attr("checked", false).change();
            chooseList = []
        } else {
            $(".choose").attr("checked", true).change();
        }
    }
</script>
</body>
</html>