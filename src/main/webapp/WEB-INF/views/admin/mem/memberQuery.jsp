<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>会员管理管理</title>
	<meta name="decorator" content="default"/>
	<script src="${ctxStatic}/artDialog-5.0.3/jquery.artDialog.min.js"></script>
	<script src="${ctxStatic}/artDialog-5.0.3/artDialog.plugins.min.js"></script>
    <link href="${ctxStatic}/artDialog-5.0.3/skins/blue.css" rel="stylesheet" />
    <script src="${ctxStatic}/jquery/jquery.lazyload.js" type="text/javascript"></script>
	<script src="${ctxStatic}/artdialog/js/dialog-plus.js"></script>
    <link href="${ctxStatic}/artdialog/css/dialog.css" rel="stylesheet" />
	<script type="text/javascript">
	function showMemberAct() {
		var url = "${admin}/memberAct?member.id=${member.id}";
		var d = dialog({
	    	id: "memberAct",
	    	title: '账户明细',
	    	url:url,
	    	width: 1000,
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
	function showMemberLoginInfo() {
		var url = "${admin}/member/queryLoginInfo?memberId=${member.id}";
		var d = dialog({
	    	id: "loginInfo",
	    	title: '登录信息',
	    	url:url,
	    	width: 1000,
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
	function setMemberPassword(){		
		if($("#password").val()==''){$.jBox.alert("会员密码不能为空!","提醒");return false;}
		$.ajax({
			type : 'POST',
			url : '${ctx}/member/setMemberPassword',
			data : {
				memberId:"${member.id}",
				password:$("#password").val()
			},
			success : function(rsp) {
				top.$.jBox.tip(rsp.message);
			}
		});
	}
	function showSetMemberPasswordForm(obj) {	
		
		$("#password").val("");
		$("#setMemberPasswordSubmit").click(function() {
			setMemberPassword();
			$("#setMemberPasswordSubmit").unbind("click"); 
			$("#setMemberPasswordCancel").unbind("click");
			dialog.close(); 
		});
		$("#setMemberPasswordCancel").click(function() {	
			$("#setMemberPasswordSubmit").unbind("click"); 
			$("#setMemberPasswordCancel").unbind("click");
			dialog.close(); 
			return false;
		});
		var dialog = $.dialog({
	    	id: "setMemberPassword",
	    	title: '密码设置',
	    	width: 460,
	    	height: 300,
	    	padding: 0,
	    	drag:true,
	    	cancel:false,
	        content: document.getElementById("setMemberPassword")
	    });
	}
	function getSpareMobile(username) {	
		$.ajax({
			type : 'POST',
			async:false,
			url : '${admin}/member/getSpareMobile',
			data : {
				username:username
			},
			success : function(rsp) {
				$("#msg").html(rsp);
			}
		}); 
	}
	function sendSmsVerifyCode(username) {
		
		$.ajax({
			url:"${admin}/member/sendSmsVerifyCode",
			type:"post",
			data:{username:username},
			success:function(res) {
				if(res.code == 0) {
					$("#msg").html(res.message);
				}
			}
		})
	}
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${admin}/member/query?id=${member.id}">会员查看</a></li>
	</ul><br/>
	<div id="setMemberPassword" hidden>
        <form:form id="setMemberPasswordForm" modelAttribute="member" action="${ctx}/member/setMemberPassword" method="post" class="breadcrumb form-search">
	         <ul class="ul-form">
				<li><label style="text-align:right;width:80px">会员密码：</label>
					<form:input path="password" style="width:230px"  htmlEscape="false" class="input-medium"/>
				</li>
			 </ul>				
			<label style="text-align:right;height:30px;line-height:30px;width:70px"></label>	
			<input id="setMemberPasswordSubmit" class="btn btn-primary " style="width:80px;" type="button" value="确认"/>
			<label style="text-align:right;height:30px;line-height:30px;width:5px"></label>
			<input id="setMemberPasswordCancel" class="btn btn-primary " style="width:80px;" type="button" value="取消"/>											
        </form:form>
	</div>
	<form:form id="inputForm" modelAttribute="member"  method="post" class="form-horizontal">
		<sys:message content="${message}"/>
		<div class="control-group">
			<label style="text-align:right;width:150px">会员编号：</label>
			<form:input path="id" style="width:230px"  readonly="true" htmlEscape="false" maxlength="64" class="input-xlarge "/>
			 <a href ="javascript:void(0);" onclick="showMemberLoginInfo()">登录信息</a>			
			<span class="help-inline"  style="width:150px"></span>	
		</div>
		<div class="control-group">	
			<label style="text-align:right;width:150px">用户名：</label>
			<form:input path="username" style="width:230px"  readonly="true" htmlEscape="false" maxlength="64" class="input-xlarge "/>

			<label style="text-align:right;width:150px">会员昵称：</label>
			<form:input path="nickname" style="width:230px"  readonly="true" htmlEscape="false" maxlength="64" class="input-xlarge "/>
			<span class="help-inline"  style="width:150px"></span>	
		</div>
		<div class="control-group">
			<label style="text-align:right;width:150px">会员姓名：</label>
			<form:input path="name" style="width:230px"  readonly="true" htmlEscape="false" maxlength="64" class="input-xlarge "/>

			<label style="text-align:right;width:150px">身份证号：</label>
			<form:input path="idNo" style="width:230px"  readonly="true" htmlEscape="false" maxlength="64" class="input-xlarge "/>
			<span class="help-inline"  style="width:150px"></span>	
		</div>
		<div class="control-group">
			<label style="text-align:right;width:150px">会员等级：</label>
			<form:select path="memberRank.rankNo" style="width:245px" disabled="true" class="input-xlarge ">
				<form:option value="" label="请选择"/>
				<form:options items="${fns:getMemRanks()}" itemLabel="rankName" itemValue="rankNo" htmlEscape="false"/>
			</form:select>
			
			<label style="text-align:right;width:150px">会员性别：</label>
			<form:radiobuttons path="gender" items="${fns:getDictList('gender')}" itemLabel="label" itemValue="value"  htmlEscape="false" disabled="true" class="required"/>
			<span class="help-inline"  style="width:150px"></span>	
		</div>
		<div class="control-group">
			<label style="text-align:right;width:150px">邮箱地址：</label>
			<form:input path="email" style="width:230px"  readonly="true" htmlEscape="false" maxlength="128" class="input-xlarge "/>
			<span class="help-inline"  style="width:150px"></span>	
			 <a id="setPasswordBtn" class="btn btn-primary" onclick="showSetMemberPasswordForm()">密码重置</a>	
			 <a id="setPasswordBtn" class="btn btn-primary" onclick="sendSmsVerifyCode(${member.username})">发送验证码</a>
			 <a id="setPasswordBtn" class="btn btn-primary" onclick="getSpareMobile(${member.username})">查看备用手机号</a>	
			 &nbsp;&nbsp;<span id="msg" style="color:red;"></span>
		</div>

		<div class="control-group">
			<label style="text-align:right;width:150px">是否可用：</label>
			<form:radiobuttons path="isEnabled" items="${fns:getDictList('trueOrFalse')}" itemLabel="label" itemValue="value" disabled="true" htmlEscape="false" class="required"/>
	        <span class="help-inline"  style="width:168px"> </span>
			<label style="text-align:right;width:150px">是否锁定：</label>
			<form:radiobuttons path="isLocked" items="${fns:getDictList('trueOrFalse')}" itemLabel="label" itemValue="value" disabled="true" htmlEscape="false" class="required"/>
			<span class="help-inline"  style="width:150px"> </span>
		</div>
		<div class="control-group">		
			<label style="text-align:right;width:150px">锁定日期：</label>
			<input name="lockedDate" type="text" style="width:230px"  disabled="disabled" maxlength="20" class="input-medium Wdate "
				value="<fmt:formatDate value="${member.lockedDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"
				onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:true});"/>
			<span class="help-inline"  style="width:150px"></span>	
		</div>
		<div class="control-group">
			<label style="text-align:right;width:150px">注册IP：</label>
			<form:input path="registerIp" style="width:230px"  readonly="true" htmlEscape="false" maxlength="15" class="input-xlarge "/>
		
			<label style="text-align:right;width:150px">登录IP：</label>
			<form:input path="loginIp" style="width:230px"  readonly="true" htmlEscape="false" maxlength="15" class="input-xlarge "/>
			<span class="help-inline"  style="width:150px"></span>	
		</div>
		<div class="control-group">
			<label style="text-align:right;width:150px">最后登录日期：</label>
			<input name="loginDate" type="text" style="width:230px"  disabled="disabled" maxlength="20" class="input-medium Wdate "
				value="<fmt:formatDate value="${member.loginDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"
				onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:true});"/>

			<label style="text-align:right;width:150px">连续登录失败次数：</label>
			<form:input path="loginFailureCount" style="width:230px"  readonly="true" htmlEscape="false" maxlength="11" class="input-xlarge "/>
			<span class="help-inline"  style="width:150px"></span>	
		</div>

		<div class="control-group">
			<label style="text-align:right;width:150px">生成时间：</label>
			<input name="createTime" type="text" style="width:230px"  disabled="disabled" maxlength="20" class="input-medium Wdate "
				value="<fmt:formatDate value="${member.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"
				onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:true});"/>
		
			<label style="text-align:right;width:150px">修改时间：</label>
			<input name="updateTime" type="text" style="width:230px"  disabled="disabled" maxlength="20" class="input-medium Wdate "
				value="<fmt:formatDate value="${member.updateTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"
				onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:true});"/>
			<span class="help-inline"  style="width:150px"></span>	
		</div>
       <div class="control-group">
        <label style="text-align:right;width:150px">认证列表：</label>
		${realIdentityStatus==1?' ✔ ':' ✘ '}实名认证&nbsp;&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;&nbsp;
		${bankCardStatus==1?' ✔ ':' ✘ '}银行卡认证&nbsp;&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;&nbsp;
		${zhimafenStatus==1?' ✔ ':' ✘ '}芝麻信用&nbsp;&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;&nbsp;
		${taobaoStatus==1?' ✔ ':' ✘ '}淘宝授权&nbsp;&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;&nbsp;
		${yunyingshangStatus==1?' ✔ ':' ✘ '}运营商授权&nbsp;&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;&nbsp;
		${bankTrxStatus==1?' ✔ ':' ✘ '}银行卡账单&nbsp;&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;&nbsp;
		${xuexingwangStatus==1?' ✔ ':' ✘ '}学信网&nbsp;&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;&nbsp;
		${shebaoStatus==1?' ✔ ':' ✘ '}社保&nbsp;&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;&nbsp;
		${gongjijingStatus==1?' ✔ ':' ✘ '}公积金&nbsp;&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;&nbsp;
		${payPwStatus==1?' ✔ ':' ✘ '}支付密码&nbsp;&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;&nbsp;
		${emailStatus==1?' ✔ ':' ✘ '}邮箱&nbsp;&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;&nbsp;
		${emergencyStatus==1?' ✔ ':' ✘ '}紧急联系人&nbsp;&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;&nbsp;
		${tooManyFriends==1?' ✔ ':' ✘ '}好友数过多&nbsp;&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;&nbsp;
		<a href="${admin}/member/deleteVerify?id=${member.id}" onclick = "return confirm('确定清除该用户的认证吗？',this.href)">消除认证</a>	
		</div>
		<div class="control-group">
			<label style="text-align:right;width:150px">备注：</label>
			<form:textarea path="rmk" readonly="true" htmlEscape="false" rows="4" maxlength="1024" class="input-xxlarge "/>
			<span class="help-inline"  style="width:150px"></span>	
		</div>
		<div class="form-actions">
			<input id="btnCancel" class="btn btn-primary" type="button" style="width:80px;" value="返 回" onclick="history.go(-1)"/>
			<label style="text-align:right;width:50px"></label>
			<a href ="javascript:void(0);" class="btn btn-primary" style="width:80px;"  onclick="showMemberAct()">账户列表</a>
			<label style="text-align:right;width:50px"></label>
			<a class="btn btn-primary"  style="width:80px;" href ="${admin}/memberActTrx/allSubTrxList?member.id=${member.id}">资金明细</a>
			<label style="text-align:right;width:50px"></label>
			<a class="btn btn-primary"  style="width:80px;" href="${admin}/memberVideoVerify/?member.id=${member.id}">视频认证</a>
			<label style="text-align:right;width:50px"></label>
			<a class="btn btn-primary"  style="width:80px;" href="${admin}/transferRecord/?member.id=${member.id}">转账列表</a>
		    <label style="text-align:right;width:50px"></label>
			<a class="btn btn-primary"  style="width:80px;" href="${admin}/rchgRecord/?member.id=${member.id}">充值列表</a>
			<label style="text-align:right;width:50px"></label>
			<a class="btn btn-primary"  style="width:80px;" href="${admin}/wdrlRecord/?member.id=${member.id}">提现列表</a>
		</div>
	</form:form>
</body>
</html>