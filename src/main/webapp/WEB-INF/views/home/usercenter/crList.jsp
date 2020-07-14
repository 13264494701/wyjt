<%@ page contentType="text/html;charset=utf-8" language="java"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8"/>
    <title>债转借条-无忧借条</title>
    <meta name="keywords" content="无忧借条"/>
    <meta name="description" content="无忧借条"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"/>
    <link rel="stylesheet" href="${homeStatic}/assets/css/p1.css?v=25" type="text/css">
    <link rel="stylesheet" href="${homeStatic}/assets/css/home/usercenter.css?v=24" type="text/css">
    <script src="http://image.51jt.com/assets/scripts/base/sea.js?v=24" type="text/javascript"></script>
</head>
<body>
<div class="v1-topbg">
    <%@include file="/WEB-INF/views/home/header.jsp" %> 
    <div class="wrapper">
        <div class="w1">
            <div class="menus clearfix">
                <a href="javascript:void 0" onclick="recharge()">大额充值</a> 
                <a href="javascript:;" onclick="loan_list()">下载借条</a>
                <a href="javascript:;" onclick="loan_caseList()">下载诉讼材料</a>
                <a href="javascript:;" onclick="crList()" class="hover">债转借条</a>
            </div>
            <form:form id="searchForm" modelAttribute="nfsCrAuction" action="${home}/member/crList?memberToken=${memberToken}" method="post" class="breadcrumb form-search">
				<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
				<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
			</form:form>
            <div class="content">
                <div class="downjt">
                    <table border="0" cellpadding="0" cellspacing="0">
                        <tr class="trth">
                            <th width="20%"><b>债权编号</b></th>
                            <th width="10%"><b>债务人</b></th>
                            <th width="10%"><b>债务人手机号</b></th>
                            <th width="10%"><b>债权金额</b></th>
                            <th width="10%"><b>购买金额</b></th>
                            <th width="15%"><b>债权到期日</b></th>
                            <th width="10%"><b class="nobr">操作</b></th>
                        </tr>
                        <c:if test="${page.list.size()<0}">
                    	<tr>
	                        <td colspan="6">
	                            <p style="height: 240px; line-height: 190px; font-size: 20px;">暂无记录</p>
	                        </td>
                    	</tr>
                    </c:if>
                    <c:forEach items="${page.list}" var="nfsCrAuction">
                        <tr id="${nfsCrAuction.id}" >
                            <td>${nfsCrAuction.loanRecord.id}</td>
                            <td>${nfsCrAuction.loanRecord.loanee.name }</td>
                            <td>${nfsCrAuction.loanRecord.loanee.username} </td>
                            <td>${nfsCrAuction.loanRecord.amount}</td>
                            <td>${nfsCrAuction.crBuyPrice}</td>
                            <td>
                              <fmt:formatDate value="${nfsCrAuction.loanRecord.dueRepayDate}" pattern="yyyy-MM-dd"/>
                            </td>
                            <td>
                              <c:if test="${nfsCrAuction.proofStatus == 'pendingApply'}">
                                <a href ="javascript:void(0);"  onclick="applyDownLoad(this)">【申请下载】</a>                           
                              </c:if> 
                              <c:if test="${nfsCrAuction.proofStatus == 'pendingCreate'}">
                                                                                   【待生成】  
                              </c:if> 
                              <c:if test="${nfsCrAuction.proofStatus == 'created'}">
                                  <a class="down" href="${fns:getConfig('domain')}${nfsCrAuction.zipPath}">【下载资料】</a>    
                              </c:if> 
                            </td>
                        </tr>
                        </c:forEach>
                    </table>
                 <div class="pagination">${page}</div>
            </div>
        </div>
    </div>
 </div>
<%@include file="/WEB-INF/views/home/footer.jsp" %>  
</div>
</body>
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
function applyDownLoad(obj){
	var crId = $(obj).closest("tr").attr("id");
	$.ajax({
		type : 'POST',
		url : '${home}/member/applyDownLoad',
		data : {
			crId:crId
		},
		success : function(rsp) {
			if(rsp.status == true)
			{
			   top.$.jBox.tip(rsp.message);
			   $(obj).text("【待生成】");	
			}else{
			   top.$.jBox.tip(rsp.message);					   
			}					
		}
	})
}
</script>
</html>
<!-- created in 2016-04-12 16:49-->