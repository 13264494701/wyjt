<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>上传案件</title>
	<meta name="decorator" content="default"/>
	<script src="${ctxStatic}/upload/js/webuploader.min.js"></script>
	<script src="${ctxStatic}/upload/js/diyUpload.js"></script>
	<script src="${ctxStatic}/artdialog/js/dialog-plus.js"></script>
    <link href="${ctxStatic}/artdialog/css/dialog.css" rel="stylesheet" />
	<link rel="stylesheet" type="text/css" href="${ctxStatic}/upload/css/upload.css" />
    
	<script type="text/javascript">
		$(document).ready(function() {
			//$("#name").focus();
			var uploader = $("#imagesUpload").diyUpload({
	            url:'${ctx}/common/uploadArbitrationImage',
	            success:function( res ) { 	 
                    console.log(res);
                    var oldImage = $("#iamges").val();
                    if(oldImage != null){
	                    $("#iamges").val(oldImage + "," + res.result.image);
                    }else{
	                    $("#iamges").val(res.result.image);
                    }
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
			
			var form = $("#inputForm");
			$("#inputForm").validate({
				onfocusout: function(element){
			        $(element).valid();
			    },
				submitHandler: function(){
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
					        	  var d = parent.dialog.get('uploadCase');	
					        	  setTimeout(function(){  
					        		  d.close(res.type); 
					        		  }
					        	  ,2000);//单位毫秒   
					        	 
					          }else{
					        	  showTip(res.content);
					          }
							},
							error:function(e){
								showTip(res.content);
							}
						})
					});
				},
				errorContainer: "#messageBox",
				errorPlacement: function(error, element) {
					$("#messageBox").text("输入有误，请先更正。");
					if (element.is(":checkbox")||element.is(":radio")||element.parent().is(".input-append")){
						error.appendTo(element.parent().parent());
					} else {
						error.appendTo(element.next());
					}
				}
			});
		});
		
		function showUploadImagesDialog(id){
			var url = '${ctx}/loanArbitration/uploadImages?loanId='+id;
			dialog({
		    	id: "uploadImages",
		    	title: '上传凭证',
		    	url:url,
		    	width: 800,
		    	height: 400,
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
                     if(this.returnValue == 0){
                    	// location.reload();
                     }               
                 },
                 onremove: function() {
                     console.log('onremove');            
                 }
		    }).show();	
		}
	</script>
</head>
<body>
		<br/>
		<form:form id="inputForm" modelAttribute="nfsLoanArbitration" action="${ctx}/loanArbitration/uploadLoanToUdPreservation" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<form:hidden path="images" id="iamges"/>
		<sys:message content="${message}"/>
		<div class="control-group">
			<label style="text-align:right;width:150px">申请人：</label>
			<form:input path="loan.loaner.name" style="width:150px" readonly="true" htmlEscape="false" maxlength="16" class="input-xlarge required"/>
		</div>
		<div class="control-group">
			<label style="text-align:right;width:150px">借款人：</label>
			<form:input path="loan.loanee.name" style="width:150px" readonly="true" htmlEscape="false" maxlength="16" class="input-xlarge required"/>
		</div>		
		<div class="control-group">
			<label style="text-align:right;width:150px">资金流水：</label>
			<a href="${admin}/loanArbitration/loanCaseData?loanId=${nfsLoanArbitration.loan.id}" target="_blank">查看</a>
		</div>
		<div class="control-group">
			<label style="text-align:right;width:150px">借款单详情：</label>
			<a href="${admin}/loanArbitration/loanCard?id=${nfsLoanArbitration.loan.id}" target="_blank">查看</a>
		</div>
		<div class="control-group">
			<label style="text-align:right;width:150px">借款协议：</label>
			<a href="${admin}/loanArbitration/checkContract?loanId=${nfsLoanArbitration.loan.id}" target="_blank">查看</a>
		</div>
		<div class="control-group">
			<label style="text-align:right;width:150px">借款人身份证：</label>
			<a href="${admin}/loanArbitration/IdCardPhoto?memberId=${nfsLoanArbitration.loan.loanee.id}" target="_blank">查看</a>
		</div>
		<div class="control-group">
			<label style="text-align:right;width:150px">放款人身份证：</label>
			<a href="${admin}/loanArbitration/IdCardPhoto?memberId=${nfsLoanArbitration.loan.loaner.id}" target="_blank">查看</a>
		</div>
		<div class="control-group">
			<!-- <label style="text-align:right;width:150px">仲裁凭证：</label> -->
			<label style="text-align:right;width:150px">凭证图片：</label>
			<ul class="upload-ul clearfix">
				<li class="upload-pick">
					<div class="webuploader-container clearfix" id="imagesUpload"></div>
				</li>
			</ul>
			<%-- <a  href="javascript:void(0)"onclick='showUploadImagesDialog("${nfsLoanArbitration.loan.id}");'>上传</a> --%>
			&nbsp;&nbsp;&nbsp;&nbsp;
			<a href="${admin}/loanArbitration/seeImages?id=${nfsLoanArbitration.id}" target="_blank">查看</a>
		</div>
		<div class="control-group">
			<label style="text-align:right;width:150px">线下交易补全信息：</label>
			<span class="help-inline"  style="width:150px"></span>	
		</div>
		<div class="control-group">
			<label style="text-align:right;width:150px">借款人账号：</label>
			<form:input path="loaneeAccount" style="width:230px" htmlEscape="false" class="input-xlarge "/>
			<span class="help-inline"  style="width:150px"></span>	
		</div>
		<div class="control-group">
			<label style="text-align:right;width:150px">借款人账户开户机构：</label>
			<form:input path="loaneeAccountName" style="width:230px" htmlEscape="false" class="input-xlarge "/>
			<span class="help-inline"  style="width:150px"></span>	
		</div>
		<div class="control-group">
			<label style="text-align:right;width:150px">放款人账号：</label>
			<form:input path="loanerAccount" style="width:230px" htmlEscape="false" class="input-xlarge "/>
			<span class="help-inline"  style="width:150px"></span>	
		</div>
		<div class="control-group">
			<label style="text-align:right;width:150px">放款人账户开户机构：</label>
			<form:input path="loanerAccountName" style="width:230px" htmlEscape="false" class="input-xlarge "/>
			<span class="help-inline"  style="width:150px"></span>	
		</div>
		<div class="control-group">
			<label style="text-align:right;width:150px">支付单号：</label>
			<form:input path="payOrderNo" style="width:230px" htmlEscape="false" class="input-xlarge "/>
			<span class="help-inline"  style="width:150px"></span>	
		</div>
		<div class="form-actions" >
			<shiro:hasPermission name="loan:nfsLoanArbitration:edit"><input id="btnSubmit"  class="btn btn-primary" style="twidth:80px;" type="submit" value="上传案件"/>&nbsp;</shiro:hasPermission>
		</div>
	</form:form>
</body>
</html>