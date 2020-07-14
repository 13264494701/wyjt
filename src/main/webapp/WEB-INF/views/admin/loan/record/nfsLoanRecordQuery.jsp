<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>借条记录管理</title>
	<meta name="decorator" content="default"/>
	<script src="${ctxStatic}/jquery/jquery.lazyload.js" type="text/javascript"></script>
	<script src="${ctxStatic}/artdialog/js/dialog-plus.js"></script>
    <link href="${ctxStatic}/artdialog/css/dialog.css" rel="stylesheet" />
	<script type="text/javascript">
	function showOperatingRecord(obj) {
		var loanId = $("#loanId").val();
		var url = '${admin}/loan/nfsLoanOperatingRecord?oldRecord.id='+loanId;
		var d = dialog({
	    	id: "loanOperatingRecordList",
	    	title: '借条操作记录',
	    	url:url,
	    	width: 1200,
	    	height: 500,
	    	padding: 0,
	    	drag:true,
	    	quickClose: true,
            onshow: function() {
                 console.log('onshow');
             },
             oniframeload: function() {
                 console.log('oniframeload');
             },
             onclose: function() {
                 console.log('onclose');
             },
             onremove: function() {
                 console.log('onremove');
             }
	    }).show();	
	}
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/nfsLoanRecord/">借条记录列表</a></li>
		<li class="active"><a href="${ctx}/nfsLoanRecord/query?id=${nfsLoanRecord.id}">借条记录查看</a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="nfsLoanRecord"  method="post" class="form-horizontal">
		<form:hidden id="loanId" path="id"/>
		<sys:message content="${message}"/>		
		<div class="control-group">
			<label style="text-align:right;width:150px">借条编号：</label>${nfsLoanRecord.loanNo}
			<span class="help-inline"  style="width:150px"></span>
			<input id="btnCancel" class="btn btn-primary" type="button" style="width:80px;" value="返 回" onclick="history.go(-1)"/>
		</div>
		<div class="control-group">
			<label style="text-align:right;width:150px">借款人姓名：</label>
			<form:input path="loanee.name" style="width:230px"  readonly="true" htmlEscape="false" maxlength="64" class="input-xlarge "/>
			<span class="help-inline"  style="width:150px"></span>	
			<label style="text-align:right;width:150px">放款人姓名：</label>
			<form:input path="loaner.name" style="width:230px"  readonly="true" htmlEscape="false" maxlength="64" class="input-xlarge "/>
			<span class="help-inline"  style="width:150px"></span>	
		</div>
		<div class="control-group">
			<label style="text-align:right;width:150px">借款人手机号：</label>
			<form:input path="loanee.username" style="width:230px"  readonly="true" htmlEscape="false" maxlength="64" class="input-xlarge "/>
			<span class="help-inline"  style="width:150px"></span>	
			<label style="text-align:right;width:150px">放款人手机号：</label>
			<form:input path="loaner.username" style="width:230px"  readonly="true" htmlEscape="false" maxlength="64" class="input-xlarge "/>
			<span class="help-inline"  style="width:150px"></span>	
		</div>
		<div class="control-group">
			<label style="text-align:right;width:150px">借款金额：</label>
			<form:input path="amount" value="${fns:decimalToStr(nfsLoanRecord.amount,2)}" style="width:230px"  readonly="true" htmlEscape="false" class="input-xlarge "/>
			<span class="help-inline"  style="width:150px"></span>	
			<label style="text-align:right;width:150px">待还金额：</label>
			<form:input path="dueRepayAmount" value="${fns:decimalToStr(nfsLoanRecord.dueRepayAmount,2)}" style="width:230px"  readonly="true" htmlEscape="false" maxlength="64" class="input-xlarge "/>
			<span class="help-inline"  style="width:150px"></span>	
		</div>
		<div class="control-group">
			<label style="text-align:right;width:150px">借款利息：</label>
			<form:input path="interest" value="${fns:decimalToStr(nfsLoanRecord.interest,2)}" style="width:230px"  readonly="true" htmlEscape="false" class="input-xlarge "/>
			<span class="help-inline"  style="width:150px"></span>	
		</div>
		<div class="control-group">
			<label style="text-align:right;width:150px">借款利率：</label>
			<form:input path="intRate" style="width:230px"  readonly="true" htmlEscape="false" class="input-xlarge "/>
			<span class="help-inline"  style="width:150px"></span>	
			<label style="text-align:right;width:150px">还款方式：</label>
			<form:input path="repayType" value="${fns:getDictLabel(nfsLoanRecord.repayType, 'repayType', '')}" style="width:230px"  readonly="true" htmlEscape="false" maxlength="11" class="input-xlarge "/>
			<span class="help-inline"  style="width:150px"></span>	
		</div>
			<div class="control-group">
			<label style="text-align:right;width:150px">创建时间：</label>
			<input name="createTime" type="text" style="width:230px"  disabled="disabled" maxlength="20" class="input-xlarge"
				value="<fmt:formatDate value="${nfsLoanRecord.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
			
			<span class="help-inline"  style="width:150px"></span>	
			<label style="text-align:right;width:150px">借款用途：</label>
			<form:input path="repayType" value="${fns:getDictLabel(nfsLoanRecord.loanPurp, 'loanPurp', '')}" style="width:230px"  readonly="true" htmlEscape="false" maxlength="11" class="input-xlarge "/>
			<span class="help-inline"  style="width:150px"></span>	
		</div>
		<div class="control-group">
			<label style="text-align:right;width:150px">还款时间：</label>
			<input name="dueRepayDate" type="text" style="width:230px"  disabled="disabled" maxlength="20" class="input-xlarge"
				value="<fmt:formatDate value="${nfsLoanRecord.dueRepayDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
			<span class="help-inline"  style="width:150px"></span>
			<label style="text-align:right;width:150px">借款时长：</label>${nfsLoanRecord.term}天
			<span class="help-inline"  style="width:150px"></span>	
		</div>
		<div class="control-group">
			<label style="text-align:right;width:150px">借条状态：</label>
			<form:input path="status" value="${fns:getDictLabel(nfsLoanRecord.status, 'loanStatus', '')}" style="width:230px"  readonly="true" htmlEscape="false" maxlength="11" class="input-xlarge "/>
			<span class="help-inline"  style="width:150px"></span>
			
			<label style="text-align:right;width:150px">争议解决方式：</label>
			<form:input path="repayType" value="${fns:getDictLabel(nfsLoanRecord.loanApplyDetail.disputeResolution, 'disputeResolution', '')}" style="width:230px"  readonly="true" htmlEscape="false" maxlength="11" class="input-xlarge "/>
			<span class="help-inline"  style="width:150px"></span>
		</div>
		
		<div class="control-group">
			<label style="text-align:right;width:150px">催收状态</label>
			<form:input path="collectionStatus" value="${fns:getDictLabel(nfsLoanRecord.collectionStatus, 'loanCollectionStatus', '')}" style="width:230px"  readonly="true" htmlEscape="false" maxlength="11" class="input-xlarge "/>
			<span class="help-inline"  style="width:150px"></span>
		</div>
		<div class="control-group">
			<label style="text-align:right;width:150px">仲裁状态</label>
			<form:input path="arbitrationStatus" value="${fns:getDictLabel(nfsLoanRecord.arbitrationStatus, 'loanArbitrationStatus', '')}" style="width:230px"  readonly="true" htmlEscape="false" maxlength="11" class="input-xlarge "/>
			<span class="help-inline"  style="width:150px"></span>
		</div>
		<c:if test="${nfsLoanRecord.loanApplyDetail.videoUrl != null && nfsLoanRecord.loanApplyDetail.videoUrl != ''}">
			<div class="control-group">
				<td style="text-align:center" width="50%">
					<label style="text-align:right;width:150px">录制视频地址：</label><a target="_Blank" href="${nfsLoanRecord.loanApplyDetail.videoUrl}">${nfsLoanRecord.loanApplyDetail.videoUrl}</a>
				</td>
			</div>
		</c:if>
		<div class="control-group">
			<label style="text-align:right;width:150px">电子借条：</label>
			<c:choose>
				<c:when test="${nfsLoanRecord.contractUrl=='正在生成'}">
					${nfsLoanRecord.contractUrl}
				</c:when>
				<c:otherwise>
					<a target="_Blank" href="${nfsLoanRecord.contractUrl}">${nfsLoanRecord.contractUrl}</a>
				</c:otherwise>
			</c:choose>
			<span class="help-inline"  style="width:150px"></span>		
		</div>
		<div class="control-group">
			<label style="text-align:right;width:150px">备注：</label>
			<form:textarea path="rmk" readonly="true" htmlEscape="false" rows="4" maxlength="1024" class="input-xxlarge "/>
			<span class="help-inline"  style="width:150px"></span>	
		</div>
	</form:form>
	
	<h4>还款计划</h4>
		<br/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
			<tr>
				<th style="text-align:center">还款期数</th>
				<th style="text-align:center">还款状态</th>
				<th style="text-align:center">当前应还金额</th>
				<th style="text-align:center">当前还款利息</th>
				<th style="text-align:center">实际还款金额</th>
				<th style="text-align:center">应还时间</th>
				<th style="text-align:center">实际还款时间</th>
			</tr>
		<tbody>
			<c:forEach items="${loanRepayRecordList }" var="repay">
				<tr>
					<td style="text-align:center">
						${repay.periodsSeq }
					</td>
					<td style="text-align:center">
						${fns:getDictLabel(repay.status, 'repayRecordStatus', '')}
					</td>
					<td style="text-align:center">
						${fns:decimalToStr(repay.expectRepayAmt,2)}
					</td>
					<td style="text-align:center">
						${fns:decimalToStr(repay.expectRepayInt,2)}
					</td>
					<td style="text-align:center">
						${fns:decimalToStr(repay.actualRepayAmt,2)}
					</td>
					<td style="text-align:center">
						<fmt:formatDate value="${repay.expectRepayDate}" pattern="yyyy.MM.dd"/>	
					</td>
					<td style="text-align:center">
						<fmt:formatDate value="${repay.actualRepayDate }" pattern="yyyy.MM.dd"/>	
					</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	
	<h4>资金流水</h4>
		<br/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
			<tr>
				<th style="text-align:center">标题</th>
				<th style="text-align:center">记账方向</th>
				<th style="text-align:center">交易金额</th>
				<th style="text-align:center">账户名称</th>
				<th style="text-align:center">账户余额</th>
				<th style="text-align:center">交易状态</th>
				<th style="text-align:center">备注</th>
				<th style="text-align:center">操作时间</th>
			</tr>
		<tbody>
			<c:forEach items="${trxList }" var="trx">
				<tr>
					<td style="text-align:center">
						${trx.title }
					</td>
					<td style="text-align:center">
						${trx.drc =='D'?'入账':'出账' }
					</td>
					<td style="text-align:center">
						${fns:decimalToStr(trx.trxAmt,2)}
					</td>
					<td style="text-align:center">
						${trx.subNo=='0001'?'可用余额':trx.subNo=='0002'?'借款账户':trx.subNo=='0003'?'冻结账户':trx.subNo=='0004'?'红包账户':trx.subNo=='0005'?'待还账户':'待收账户' }
					</td>
					<td style="text-align:center">
						${fns:decimalToStr(trx.curBal,2)}
					</td>
					<td style="text-align:center">
						${fns:getDictLabel(trx.status, 'trxSts', '')}
					</td>
					<td style="text-align:center">
						${trx.rmk }
					</td>
					<td style="text-align:center">
						<fmt:formatDate value="${trx.createTime }" pattern="yyyy.MM.dd"/>	
					</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	
	<h4>操作记录</h4>
		<br/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
			<tr>
				<th style="text-align:center">标题</th>
				<th style="text-align:center">借条本金</th>
				<th style="text-align:center">借条利息</th>
				<th style="text-align:center">还款时间</th>
				<th style="text-align:center">借条状态</th>
				<th style="text-align:center">操作时间</th>
			</tr>
		<tbody>
			<c:forEach items="${operatingList }" var="operating">
				<tr>
					<td style="text-align:center">
						${fns:getDictLabel(operating.type, 'operatingRecordType', '')}
					</td>
					<td style="text-align:center">
						${fns:decimalToStr(operating.nowRecord.amount,2)}
					</td>
					<td style="text-align:center">
						${fns:decimalToStr(operating.nowRecord.interest,2)}
					</td>
					<td style="text-align:center">
						<fmt:formatDate value="${operating.nowRecord.dueRepayDate }" pattern="yyyy-MM-dd HH:mm:ss"/>	
					</td>
					<td style="text-align:center">
						${fns:getDictLabel(operating.nowRecord.status, 'loanStatus', '')}
					</td>
					<td style="text-align:center">
						<fmt:formatDate value="${operating.createTime }" pattern="yyyy.MM.dd"/>	
					</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
</body>
</html>