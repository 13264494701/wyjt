<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>广告管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
	$(document).ready(function() {
		//$("#name").focus();
		$("#inputForm").validate({
			onfocusout: function(element){
		        $(element).valid();
		    },
			submitHandler: function(form){
				loading('正在提交，请稍等...');
				form.submit();
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
		$("#adType").change(function(){
			var type = $("#adType").val();
			$("#textContent").val("");
			imagePathDelAll();
			if(type=="image"){
				$("#imgSelectDiv").show();					
				$("#textContDiv").hide();
			}else{					
				$("#textContDiv").show();
				$("#imgSelectDiv").hide();	
			}
		});
		$("#adType").val("image").trigger("change");
	});
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/ad/">广告列表</a></li>
		<li class="active"><a href="${ctx}/ad/query?id=${cmsAd.id}">广告查看</a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="cmsAd"  method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>		
		<div class="control-group">
			<label style="text-align:right;width:150px">广告位置：</label>
			<form:select path="positionNo" style="width:245px"  disabled="true"  class="input-xlarge ">
			<form:option value="" label="请选择"/>
				<form:options items="${fns:getAdPositionList()}" itemLabel="positionName" itemValue="positionNo" htmlEscape="false"/>
			</form:select>
			<span class="help-inline"  style="width:150px"></span>	
		</div>
		<div class="control-group">
			<label style="text-align:right;width:150px">广告标题：</label>
			<form:input path="title" style="width:230px"  readonly="true" htmlEscape="false" maxlength="64" class="input-xlarge "/>
			<span class="help-inline"  style="width:150px"></span>	
		</div>
		<div class="control-group">
			<label style="text-align:right;width:150px">广告类型：</label>
			<form:select path="type" id="adType" disabled="true" style="width:245px" class="input-xlarge required">
			    <form:option value="" label="请选择"/>
				<form:options items="${fns:getDictList('adType')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
			</form:select>
			<span class="help-inline"  style="width:150px"><font color="red">*</font> </span>
		</div>
		<div class="control-group" id="imgSelectDiv">
			<label style="text-align:right;width:150px">图片路径：</label>
			<div class="controls">
			<form:hidden id="imagePath" path="imagePath" htmlEscape="false" maxlength="255" class="input-xlarge"/>
			<sys:ckfinder input="imagePath" readonly="true" type="images" uploadPath="/cms/ad" selectMultiple="false"/>
			<span class="help-inline"  style="width:150px"></span>	
			</div>
		</div>
		<div class="control-group" id="textContDiv">
			<label style="text-align:right;width:150px" class="control-label" >文本内容：</label>
			<div style="text-align:left; margin-left: 150px;">
				<form:textarea path="textContent" htmlEscape="true" rows="4" class="input-xxlarge "/>
				<sys:ckeditor replace="textContent"  uploadPath="/cms/ad/shopAd" />
				<span class="help-inline"  style="width:150px"></span>	
			</div>
		</div>
		<div class="control-group">
			<label style="text-align:right;width:150px">链接地址：</label>
			<form:input path="redirectUrl" style="width:630px" htmlEscape="false" maxlength="255" class="input-xlarge  digits"/>
			<span class="help-inline"  style="width:150px"></span>	
		</div>
		<div class="control-group">
			<label style="text-align:right;width:150px">展示顺序：</label>
			<form:input path="sort" style="width:230px"  readonly="true" htmlEscape="false" maxlength="11" class="input-xlarge  digits"/>
			<span class="help-inline"  style="width:150px"></span>	
		</div>
		<div class="control-group">
			<label style="text-align:right;width:150px">生成时间：</label>
			<input name="createTime" type="text" style="width:230px"  disabled="disabled" maxlength="20" class="input-medium Wdate "
				value="<fmt:formatDate value="${cmsAd.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"
				onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:true});"/>
			<span class="help-inline"  style="width:150px"></span>	
		</div>
		<div class="control-group">
			<label style="text-align:right;width:150px">修改时间：</label>
			<input name="updateTime" type="text" style="width:230px"  disabled="disabled" maxlength="20" class="input-medium Wdate "
				value="<fmt:formatDate value="${cmsAd.updateTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"
				onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:true});"/>
			<span class="help-inline"  style="width:150px"></span>	
		</div>
		<div class="form-actions">
			<input id="btnCancel" class="btn btn-primary" type="button" style="width:80px;" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>