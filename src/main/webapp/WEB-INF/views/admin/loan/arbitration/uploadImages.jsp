<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>上传凭证</title>
	<meta name="decorator" content="default"/>
	<%-- <script src="${ctxStatic}/artdialog/js/dialog-plus.js"></script>
    <link href="${ctxStatic}/artdialog/css/dialog.css" rel="stylesheet" /> --%>
    
<script src="${ctxStatic}/upload/js/webuploader.min.js"></script>
<script src="${ctxStatic}/upload/js/diyUpload.js"></script>
<link rel="stylesheet" type="text/css" href="${ctxStatic}/upload/css/upload.css" />
<script type="text/javascript">
	$(document).ready(function() {
		//$("#name").focus();
			//上传图片
	        var uploader = $("#imagesUpload").diyUpload({
	            url:'${ctx}/common/uploadArbitrationImage',
	            success:function( res ) { 	 
                    console.log(res); 
                    $("#iamges").val(res.result.image);
	            },
	            error:function( err ) { 
	            	console.log(err);
	            },
	            buttonText : '',
	            fileNumLimit:5,
	    		fileSizeLimit:500000 * 1024,
	    		fileSingleSizeLimit:50000 * 1024,
	            accept: {
	                title: "Images",
	                extensions: 'gif,jpg,jpeg,bmp,png'
	            },
	            thumb:{
	                width:60,
	                height:45,
	                quality:100,
	                allowMagnify:true,
	                crop:true,
	                type:"image/jpeg"
	            }
	        });
		$.validator.setDefaults({
			ignore : ""
		});
		$("#inputForm").validate({
			onfocusout : function(element) {
				$(element).valid();
			},
			submitHandler : function(form) {
				var form = $("#inputForm");
				loading('正在提交，请稍等...');
				uploader.upload();
				uploader.on('uploadFinished', function (file, response) {
					$.ajax({
						url:form.attr("action"),
						type:form.attr("method"),
						data:form.serialize(),
						success:function(res){
				          if(res.type=='success'){
				        	  showTip(res.content);	
				        	  var d = parent.dialog.get('uploadImages');	
				        	  setTimeout(function(){  
				        		  d.close(res.type); 
				        		  }
				        	  ,2000);//单位毫秒   
				          }
						},
						error:function(e){
							alert(e.type);
						}
					})
				});	
			},
			errorContainer : "#messageBox",
			errorPlacement : function(error, element) {
				$("#messageBox").text("输入有误，请先更正。");
				if (element.is(":checkbox")||element.is(":radio")||element.parent().is(".input-append")) {
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
		<br/>
		<form:form id="inputForm" modelAttribute="nfsLoanArbitration" action="${ctx}/loanArbitration/uploadArbitrationImages" method="post" enctype="multipart/form-data" class="form-horizontal">
		<form:hidden path="id" />
		<input type="hidden" name="iamges" id="iamges">
		<sys:message content="${message}"/>
		<div class="control-group">
							<label style="text-align:right;width:150px">凭证图片：</label>
							<ul class="upload-ul clearfix">
							    <li class="upload-pick">
									 <div class="webuploader-container clearfix" id="imagesUpload"></div>
								</li>
							</ul>
						</div>
		
		<div class="form-actions" >
			<shiro:hasPermission name="loan:nfsLoanArbitration:edit"><input id="btnSubmit"  class="btn btn-primary" style="twidth:80px;" type="submit" value="上传"/>&nbsp;</shiro:hasPermission>
		</div>
	</form:form>
</body>
</html>