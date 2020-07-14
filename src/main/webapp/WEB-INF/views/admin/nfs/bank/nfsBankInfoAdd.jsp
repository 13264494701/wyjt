<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>银行编码管理</title>
	<meta name="decorator" content="default"/>
	<script src="${ctxStatic}/upload/js/webuploader.min.js"></script>
    <script src="${ctxStatic}/upload/js/diyUpload.js"></script>
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/upload/css/upload.css" />
	<script type="text/javascript">
		$(document).ready(function() {
			//上传图片
	        var uploader = $('#imageUpload').diyUpload({
	            url:'${ctx}/common/uploadIcon',
	            success:function( res ) { 	 
                    console.log(res); 
                    $("#logo").val(res.result.image);
	            },
	            error:function( err ) { 
	            	console.log(err);
	            },
	            buttonText : '',
	            fileNumLimit:1,
	    		fileSizeLimit:500000 * 1024,
	    		fileSingleSizeLimit:50000 * 1024,
	            accept: {
	                title: "Images",
	                extensions: 'gif,jpg,jpeg,bmp,png'
	            },
	            thumb:{
	                width:120,
	                height:90,
	                quality:100,
	                allowMagnify:true,
	                crop:true,
	                type:"image/jpeg"
	            }
	        });
			//$("#name").focus();
			$("#inputForm").validate({
				onfocusout: function(element){
			        $(element).valid();
			    },
				submitHandler: function(form){
					loading('正在提交，请稍等...');
					uploader.upload();
					uploader.on('uploadFinished', function (file, response) {
						form.submit();	
					});	
				},
				errorContainer: "#messageBox",
				errorPlacement: function(error, element) {
					$("#messageBox").text("输入有误，请先更正。");
					if (element.is(":checkbox")||element.is(":radio")||element.parent().is(".input-append")){
						error.appendTo(element.parent().parent());
					} else {
		//				error.insertAfter(element);
						error.appendTo(element.next());
					}
				}
			});
		});
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/nfsBankInfo/">银行列表</a></li>
		<shiro:hasPermission name="bank:nfsBankInfo:edit">
		<li class="active"><a href="${ctx}/nfsBankInfo/add?id=${nfsBankInfo.id}">银行添加</a></li>
		</shiro:hasPermission>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="nfsBankInfo" action="${ctx}/nfsBankInfo/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<form:hidden path="logo"/>
		<sys:message content="${message}"/>		
		<div class="control-group">
			<label style="text-align:right;width:150px">银行名称：</label>
			<form:input path="name" style="width:230px" htmlEscape="false" maxlength="64" class="input-xlarge required"/>
			<span class="help-inline"  style="width:150px"><font color="red">*</font> </span>
		</div>
		<label style="text-align:right;width:150px">显示图标：</label>
			<ul class="upload-ul clearfix">
				<li class="upload-pick">
					<div class="webuploader-container clearfix" id="imageUpload"></div>
				</li>
			</ul>
		</div>
		<div class="control-group">
			<label style="text-align:right;width:150px">每笔限额：</label>
			<form:input path="limitPerTrx" style="width:230px" htmlEscape="false" maxlength="11" class="input-xlarge  digits"/>
			<span class="help-inline"  style="width:150px"></span>	
		</div>
		<div class="control-group">
			<label style="text-align:right;width:150px">每天限额：</label>
			<form:input path="limitPerDay" style="width:230px" htmlEscape="false" maxlength="11" class="input-xlarge  digits"/>
			<span class="help-inline"  style="width:150px"></span>	
		</div>
		<div class="control-group">
			<label style="text-align:right;width:150px">每月限额：</label>
			<form:input path="limitPerMonth" style="width:230px" htmlEscape="false" maxlength="11" class="input-xlarge  digits"/>
			<span class="help-inline"  style="width:150px"></span>	
		</div>
		<div class="control-group">
			<label style="text-align:right;width:150px">客服热线：</label>
			<form:input path="hotline" style="width:230px" htmlEscape="false" maxlength="32" class="input-xlarge "/>
			<span class="help-inline"  style="width:150px"></span>	
		</div>
		<div class="control-group">
			<label style="text-align:right;width:150px">是否支持：</label>
			<form:radiobuttons path="isSupport" items="${fns:getDictList('trueOrFalse')}" itemLabel="label" itemValue="value" htmlEscape="false" class="required"/>
			<span class="help-inline"  style="width:150px"><font color="red">*</font> </span>
		</div>
		<div class="form-actions">
			<shiro:hasPermission name="bank:nfsBankInfo:edit"><input id="btnSubmit" class="btn btn-primary" style="width:80px;" type="submit" value="保   存"/>&nbsp;</shiro:hasPermission>
			<label style="text-align:right;width:50px"></label>
			<input id="btnCancel" class="btn btn-primary" type="button" style="width:80px;" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>