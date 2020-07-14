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
                    $("#newImage").val(res.result.image);
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
                    uploader.upload();
                    uploader.on('uploadFinished', function (file, response) {
                    	loading('正在提交，请稍等...');
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
        function imageDeleteFunc(id) {
            $("#imagedelete").hide();
        }
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/nfsBankInfo/">银行列表</a></li>
		<shiro:hasPermission name="bank:nfsBankInfo:edit">
			<li class="active"><a href="${ctx}/nfsBankInfo/update?id=${nfsBankInfo.id}">银行修改</a></li>
		</shiro:hasPermission>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="nfsBankInfo" action="${ctx}/nfsBankInfo/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<input type="hidden" name="newImage" id="newImage">
		<sys:message content="${message}"/>		
		<div class="control-group">
			<label style="text-align:right;width:150px">银行名称：</label>
			<form:input path="name" style="width:230px" htmlEscape="false" maxlength="64" class="input-xlarge required"/>
			<span class="help-inline"  style="width:150px"><font color="red">*</font> </span>
		</div>

		<div class="control-group">
             <label style="text-align:right;width:150px">银行图标：</label>
             <ul class="upload-ul clearfix">
                 <li class="upload-pick" onclick="imageDeleteFunc()">
                     <div class="webuploader-container clearfix" id="imageUpload">
                         <img style="position:absolute" src="${nfsBankInfo.logo}" alt="" id="imagedelete" >
                         <img style="position:absolute;width:36px;height: 36px" src="data:image/png;base64,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" alt="">
                     </div>
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