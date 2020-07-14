<%@ page contentType="text/html;charset=utf-8" language="java"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8"/>
    <title>大额充值-无忧借条</title>
    <meta name="keywords" content="无忧借条"/>
    <meta name="description" content="无忧借条"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"/>
    <link rel="stylesheet" href="${homeStatic}/assets/css/p1.css?v=25" type="text/css">
    <link rel="stylesheet" href="${homeStatic}/assets/css/home/usercenter.css?v=24" type="text/css">
    <script type="text/javascript" src="${homeStatic}/assets/js/My97DatePicker/WdatePicker.js?v=23"></script>
    <script src="http://image.51jt.com/assets/scripts/base/sea.js?v=23" type="text/javascript"></script>
</head>
<body>
<div class="v1-topbg">
<%@include file="/WEB-INF/views/home/header.jsp" %>       
    <div class="wrapper">
        <div class="w1">
            <a href="#" class="charge-btn hide">去充值</a>
            <div class="menus clearfix">
                <a href="javascript:void 0" onclick="recharge()" class="hover">大额充值</a> 
                <a href="javascript:;" onclick="loan_list()">下载借条</a>
                <a href="javascript:;" onclick="loan_caseList()">下载诉讼材料</a>
                <a href="javascript:;" onclick="crList()">债转借条</a>
            </div>
                <form:form id="searchForm" modelAttribute="nfsRchgRecord" action="${home}/member/charge_list?memberToken=${memberToken}" method="post" class="breadcrumb form-search">
					<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
					<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
					<ul class="ul-form" style="width: 100%">
                    <li><label>充值方式：</label>
						<form:select path="type"  style="width:100px" class="input-medium">
							<form:option value="" label="请选择"/>
							<form:options items="${fns:getDictList('rechargeType')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
                    </li>
                    <li>
                        <label>充值状态：</label>
                        <form:select path="status"  style="width:100px" class="input-medium">
							<form:option value="" label="请选择"/>
							<form:options items="${fns:getDictList('rchgStatus')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
                    </li>
                   <li><label style="text-align:right;width:50px">时间：</label>
						<input name="beginTime" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
						value="<fmt:formatDate value="${beginTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"
						onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:true});" style="text-align:right;width:100px"/>
					</li>
					<li><label style="text-align:right;width:50px">至：</label>
						<input name="endTimes" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
							value="<fmt:formatDate value="${endTimes}" pattern="yyyy-MM-dd HH:mm:ss"/>"
							onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:true});" style="text-align:right;width:100px"/>
					</li>
                    <li class="btns"><label style="text-align:right;width:20px"></label><input id="btnSubmit" class="btn btn-primary " style="width:80px;" type="submit" value="查   询"/></li>
					<li class="clearfix"></li>
                    </ul>
                    </form:form>
                <table cellspacing="0" cellpadding="0" class="data-table" id="dataTb">
                    <tr>
                        <th width="18%"><span>订单号</span></th>
                        <th width="18%"><span>交易时间</span></th>
                        <th width="16%"><span>充值金额</span></th>
                        <th width="16%"><span>充值方式</span></th>
                        <th width="16%"><span>充值状态</span></th>
                        <th width="16%"><span class="nobr">交易详情</span></th>
                    </tr>
                    <c:if test="${page.list.size()<0}">
                    	<tr>
	                        <td colspan="6">
	                            <p style="height: 240px; line-height: 190px; font-size: 20px;">暂无充值记录</p>
	                        </td>
                    	</tr>
                    </c:if>
                    <c:forEach items="${page.list}" var="nfsRchgRecord">
                    <tr>
                        <td>${nfsRchgRecord.id }</td>
                        <td style="text-align:center">
							<fmt:formatDate value="${nfsRchgRecord.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
						</td>
                        <td><i class="red">${fns:decimalToStr(nfsRchgRecord.amount,2)}</i>元
                        </td>
                        <td>${fns:getDictLabel(nfsRchgRecord.type, 'rechargeType', '')}</td>
                        <td>${fns:getDictLabel(nfsRchgRecord.status, 'rchgStatus', '')}</td>
                        <td>--</td>
                    </tr>
                    </c:forEach>
                </table>
             	<div class="pagination">${page}</div>
        </div>
    </div>
<%@include file="/WEB-INF/views/home/footer.jsp" %>
</div>
<script type="text/javascript">
	$(document).ready(function() {
		
	});
	function page(n,s){
		$("#pageNo").val(n);
		$("#pageSize").val(s);
		$("#searchForm").submit();
       	return false;
    }
	function recharge(){
		window.location.href="${home}/member/recharge_list?memberToken=${memberToken}";
	}
	function loan_list(){
		window.location.href="${home}/member/loan_list?memberToken=${memberToken}";
	}
	function loan_caseList(){
		window.location.href="${home}/member/loan_caseList?memberToken=${memberToken}";
	}
	function crList(){
		window.location.href="${home}/member/crList?memberToken=${memberToken}";
	}
</script>
</body>
</html>