<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>静态生成</title>
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
			$("#genType").change(function(){
				var type = $("#genType").val();
				switch(type)
				{
					case "index":
						$("#articleCategorySelectDiv").hide();					
						$("#beginDateSelectDiv").hide();					
						$("#endDateSelectDiv").hide();
						
						break;
					case "article":
						$("#articleCategorySelectDiv").show();					
						$("#beginDateSelectDiv").show();					
						$("#endDateSelectDiv").show();
						
						break;
					case "other":
						$("#articleCategorySelectDiv").hide();					
						$("#beginDateSelectDiv").hide();					
						$("#endDateSelectDiv").hide();
						break;
				}
			});
			$("#genType").val("index").trigger("change");
		});
	</script>
</head>
<body>
	<form:form id="inputForm" modelAttribute="generateStatic" action="${ctx}/cms/generateStatic/generate" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>		
		<div class="control-group">

		</div>
		<div class="control-group">
			<label style="text-align:right;width:150px">生成类型：</label>
			<form:select path="genType" id="genType" style="width:245px" class="input-xlarge required">
			<form:option value="" label="请选择"/>
				<form:options items="${fns:getDictList('genType')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
			</form:select>
			<span class="help-inline"  style="width:150px"><font color="red">*</font> </span>
		</div>
		<div class="control-group" id="articleCategorySelectDiv">
			<label style="text-align:right;width:150px">文章分类：</label>
			<sys:treeselect id="category" name="category.id" value="${generateStatic.category.id}" labelName="category.name" labelValue="${generateStatic.category.name}"
					title="分类" url="/cms/category/treeData"  cssClass="" allowClear="true"/>
		</div>
		<div class="control-group" id="beginDateSelectDiv">
			<label style="text-align:right;width:150px">起始日期：</label>
			<input name="beginDate" type="text" style="width:230px" maxlength="10" class="input-medium Wdate required"
				value="<fmt:formatDate value="${generateStatic.beginDate}" pattern="yyyy-MM-dd"/>"
				onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:true});"/>
			<span class="help-inline"  style="width:150px"><font color="red">*</font> </span>
		</div>
		<div class="control-group" id="endDateSelectDiv">
			<label style="text-align:right;width:150px">结束日期：</label>
			<input name="endDate" type="text" style="width:230px"  maxlength="10" class="input-medium Wdate required"
				value="<fmt:formatDate value="${generateStatic.endDate}" pattern="yyyy-MM-dd"/>"
				onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:true});"/>
			<span class="help-inline"  style="width:150px"><font color="red">*</font> </span>
		</div>
		<div class="control-group" id="genCountSelectDiv">
			<label style="text-align:right;width:150px">生成数量：</label>
			<form:input path="genCount" style="width:230px" htmlEscape="false" maxlength="20" class="input-xlarge required digits"/>
			<span class="help-inline"  style="width:150px"><font color="red">*</font> </span>
		</div>
		<div class="form-actions">
			<shiro:hasPermission name="static:generateStatic:edit"><input id="btnSubmit" class="btn btn-primary" style="width:80px;" type="submit" value="确定"/>&nbsp;</shiro:hasPermission>
			<label style="text-align:right;width:50px"></label>
			<input id="btnCancel" class="btn btn-primary" type="button" style="width:80px;" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>