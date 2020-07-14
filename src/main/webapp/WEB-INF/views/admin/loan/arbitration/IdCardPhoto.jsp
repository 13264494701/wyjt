<%@page contentType="text/html;charset=UTF-8"%>
<%@include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>借条仲裁案件证据</title>
	<meta name="decorator" content="default"/>
	<script src="${ctxStatic}/jquery/jquery.lazyload.js" type="text/javascript"></script>
	<script src="${ctxStatic}/artdialog/js/dialog-plus.js"></script>
    <link href="${ctxStatic}/artdialog/css/dialog.css" rel="stylesheet" />
    <script type="text/javascript">
	    $(document).ready(function() {
			
		});
		function exportPdf(){
			var id = $("#id").val();
			window.location.href="${admin}/loanArbitration/exportIdCardPdf?id="+id;
		}
</script>
</head>
<body>
	<table>
		<tr>
			<td>
				<input type="hidden" id = "id" value="${memberVideoVerify.member.id }"/>
				<input type="button" onclick = "exportPdf()" value="转出pdf"/>
			</td>
		</tr>
	</table>
		<img src="${urlfont}"/><br/>
		身份证前照：${urlfont}<br/>
		<img src="${urlback}"/><br/>
		身份证后照：${urlback}
		<%-- <img src="${local}/upload/image/shortStory.jpg"/>
		<img src="${local}/upload/image/shortStory.jpg"/> --%>
</body>
</html>