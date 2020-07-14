<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>好友投诉管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">

	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/memberFriendReport/">好友投诉列表</a></li>
		<li class="active"><a href="${ctx}/memberFriendReport/query?id=${memberFriendReport.id}">好友投诉查看</a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="memberFriendReport"  method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>		
		<div class="control-group">
			<label style="text-align:right;width:150px">投诉人：</label>
			<form:input path="member.name" style="width:230px"  readonly="true" htmlEscape="false" maxlength="64" class="input-xlarge"/>
			<span class="help-inline"  style="width:150px"></span>	
		</div>
		<div class="control-group">
			<label style="text-align:right;width:150px">投诉人手机号：</label>
			<form:input path="member.username" style="width:230px"  readonly="true" htmlEscape="false" maxlength="20" class="input-xlarge  digits"/>
			<span class="help-inline"  style="width:150px"></span>	
		</div>
		<div class="control-group">
			<label style="text-align:right;width:150px">被投诉人：</label>
			<form:input path="friend.name" style="width:230px"  readonly="true" htmlEscape="false" maxlength="64" class="input-xlarge"/>
			<span class="help-inline"  style="width:150px"></span>	
		</div>
		<div class="control-group">
			<label style="text-align:right;width:150px">被投诉人手机号：</label>
			<form:input path="friend.username" style="width:230px"  readonly="true" htmlEscape="false" maxlength="20" class="input-xlarge  digits"/>
			<span class="help-inline"  style="width:150px"></span>	
		</div>
		<div class="control-group">
			<label style="text-align:right;width:150px">投诉类型：</label>
			<form:select path="type" style="width:245px"  disabled="true"  class="input-xlarge ">
			<form:option value="" label="请选择"/>
				<form:options items="${fns:getDictList('reportType')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
			</form:select>
			<span class="help-inline"  style="width:150px"></span>	
		</div>
		<div class="control-group">
			<label style="text-align:right;width:150px">投诉时间：</label>
			<input name="createTime" type="text" style="width:230px"  disabled="disabled" maxlength="20" class="input-medium Wdate "
				value="<fmt:formatDate value="${memberFriendReport.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"
				onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:true});"/>
			<span class="help-inline"  style="width:150px"></span>	
		</div>
		<div class="control-group">
			<label style="text-align:right;width:150px">投诉标题：</label>
			<form:input path="title" style="width:230px"  readonly="true" htmlEscape="false" maxlength="64" class="input-xlarge "/>
			<span class="help-inline"  style="width:150px"></span>	
		</div>
		<div class="control-group">
			<label style="text-align:right;width:150px">投诉内容：</label>
			<form:textarea path="content"  readonly="true" htmlEscape="false" rows="4" maxlength="255" class="input-xxlarge "/>
			<span class="help-inline"  style="width:150px"></span>	
		</div>
		<div class="control-group">
			<label style="text-align:right;width:150px">图片证据：</label>
			<ul>
			   <c:forEach items="${memberFriendReport.imagesList}" var="image" varStatus="status">
			      <li>
                  <div  style="margin:0 auto;width:300px;height:550px">
                         <img src="${image}" alt="">
                  </div>
                  </li>
               </c:forEach>
            </ul>	
		</div>
		<div class="control-group">
			<label style="text-align:right;width:150px">投诉状态：</label>
			<form:select path="status" style="width:245px"  disabled="true"  class="input-xlarge ">
			<form:option value="" label="请选择"/>
				<form:options items="${fns:getDictList('reportStatus')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
			</form:select>
			<span class="help-inline"  style="width:150px"></span>	
		</div>
		<div class="control-group">
			<label style="text-align:right;width:150px">备注：</label>
			<form:textarea path="rmk" readonly="true" htmlEscape="false" rows="4" maxlength="1024" class="input-xxlarge "/>
			<span class="help-inline"  style="width:150px"></span>	
		</div>
		<div class="form-actions">
			<input id="btnCancel" class="btn btn-primary" type="button" style="width:80px;" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>