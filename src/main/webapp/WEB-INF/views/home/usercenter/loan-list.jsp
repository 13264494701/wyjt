<%@ page contentType="text/html;charset=utf-8" language="java"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8"/>
    <title>下载借条-无忧借条</title>
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
                <a href="javascript:;" class="hover" onclick="loan_list()">下载借条</a>
                <a href="javascript:;" onclick="loan_caseList()">下载诉讼材料</a>
                <a href="javascript:;" onclick="crList()">债转借条</a>
            </div>
            <form:form id="searchForm" modelAttribute="nfsLoanRecord" action="${home}/member/loan_list?memberToken=${memberToken}" method="post" class="breadcrumb form-search">
				<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
				<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
			</form:form>
            <div class="content">
                <div class="downjt">
                    <table border="0" cellpadding="0" cellspacing="0">
                        <tr class="trth">
                            <th width="20%"><b>序号</b></th>
                            <th width="20%"><b>单据编号</b></th>
                            <th width="10%"><b>借款人</b></th>
                            <th width="10%"><b>放款人</b></th>
                            <th width="15%"><b>借款达成时间</b></th>
                            <th width="15%"><b>协议还款时间</b></th>
                            <th width="10%"><b class="nobr">管理操作</b></th>
                        </tr>
                    <c:if test="${page.list.size()<0}">
                    	<tr>
	                        <td colspan="6">
	                            <p style="height: 240px; line-height: 190px; font-size: 20px;">暂无记录</p>
	                        </td>
                    	</tr>
                    </c:if>
                    <c:forEach items="${page.list}" var="nfsLoanRecord">
                        <tr>
                            <td>${nfsLoanRecord.id }
                            </td>
                            <td>${nfsLoanRecord.loanNo }
                            </td>
                            <td>${nfsLoanRecord.loanee.name }
                            </td>
                            <td>${nfsLoanRecord.loaner.name }
                            </td>
                            <td><fmt:formatDate value="${nfsLoanRecord.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
                            </td>
                            <td><fmt:formatDate value="${nfsLoanRecord.dueRepayDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
                            </td>
                            <td><a class="down" href="https://www.51jt.com${nfsLoanRecord.contractUrl}" target="_blank">【下载】</a></td>
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
</script>
</html>