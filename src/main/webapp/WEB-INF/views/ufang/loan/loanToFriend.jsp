<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>高级借出</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			var $password = $("#payPassWord");
			var $submit = $("input:submit");
			//$("#name").focus();
			$("#inputForm").validate({
				onfocusout: function(element){
			        $(element).valid();
			    },
				submitHandler:function(form){
					loading('正在提交，请稍等...');
					$.ajax({
						url: "${ctx}/common/public_key",
						type: "GET",
						dataType: "json",
						cache: false,
						beforeSend: function() {
							$submit.prop("disabled", true);
						},
						success: function(data) {
							var rsaKey = new RSAKey();
							rsaKey.setPublic(b64tohex(data.modulus), b64tohex(data.exponent));
							var enPassword = hex2b64(rsaKey.encrypt($password.val()));
							$password.val(enPassword);
							submit(form);
							$submit.prop("disabled", false);
						},
						error: function(e) { 
							alert(e); 
						} 
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

		function submit(form){
				loading('正在提交，请稍等...');
				$.ajax({
					url:$(form).attr("action"),
					type:$(form).attr("method"),
					data:$(form).serialize(), 
					success:function(res){							
			          if(res.type=='success'){
			        	  showTip(res.content);				        	
			        	  var d = parent.dialog.get('loanToFriend');	
			        	  setTimeout(function(){  
			        		  d.close(res.type); 
			        		  }
			        	  ,2000);//单位毫秒   
			        	 
			          }else if(res.type=='error'){
			        	  showTip(res.content);	
			        	  var d = parent.dialog.get('loanToFriend');	
			        	  setTimeout(function(){  
			        		  form.reset();
			        		  d.close(res.type); 
			        		  }
			        	  ,3000);//单位毫秒 
			          }else if(res.type=='warn'){
			        	  showTip(res.content);	
			        	  $("#payPassWord").val("");
			          }
					},
					error:function(e){
						alert(e.type);
					}
				})
		}

		function calInterest(){
			var amount = $("#amount").val();
			var intRate = $("#intRate").val();		
			var term = $("#term").val();
			var repayType = $("#repayType").val();
            if(amount == ''){
            	showTip("请输入借款金额!");
            	return;
            }
            if(term == ''){
            	showTip("请输入借款时长");
            	return;
            }
			var url = "${ufang}/loanApply/calInterest";
			$.ajax({
				url:url,
				type: "GET",
				data:{
					amount:amount,
					intRate:intRate,				
					term:term,
					repayType:repayType
				},
				success: function(data){
					$("#interest").val(data);	
				}
			});
		}
		function changeRepayType(obj){
			var repayType = $(obj).val();	
			$("#termDays").html(0+" 天");
			$("#term").val(0);
			if(repayType=='oneTimePrincipalAndInterest'){
				var repayDate = ['<input id="repayDate" name="repayDate" type="text" style="width:170px"  maxlength="20" class="input-medium Wdate required"',
					'onclick="WdatePicker({dateFmt:\'yyyy-MM-dd\',isShowClear:true,onpicked:function(){pickDays();}});"/>'].join("");
				$("#periods").after(repayDate);
				$("#periods").remove();
			}else{	
				var periods = ['<select id="periods" name = "periods" onchange="changePeriods(this)" style="text-align:left;width:185px">',
					           '<option value = "30">一期</option>',
					           '<option value = "60">二期</option>',
					           '<option value = "90">三期</option>',
					           '<option value = "180">六期</option>',
					           '<option value = "360">十二期</option>',
					           ' </select>'].join("");
				$("#repayDate").after(periods);
				$("#repayDate").remove();
			}
		}
		function pickDays (){
			var repayDate = $("#repayDate").val();
			var currentDate = '${fns:getDate("yyyy-MM-dd")}';
			var days = datedifference(currentDate,repayDate);
			$("#termDays").html(days+" 天");
			$("#term").val(days);
		}	
		function datedifference(sDate1, sDate2) { //sDate1和sDate2是2006-12-18格式  
	        var dateSpan,tempDate,iDays;
	        sDate1 = Date.parse(sDate1);
	        sDate2 = Date.parse(sDate2);
	        dateSpan = sDate2 - sDate1;
	        dateSpan = Math.abs(dateSpan);
	        iDays = Math.floor(dateSpan / (24 * 3600 * 1000)) + 1;
	        return iDays
	    }
		function changePeriods(obj){
			var days = $(obj).val();	
			$("#termDays").html(days+" 天");
			$("#term").val(days);
		}
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
	</ul><br/>
	<form:form id="inputForm" modelAttribute="nfsLoanApply" action="${ufang}/loanApply/loanToFriend" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<form:hidden path="detail.member.id" value="${friend.id}"/>
		<form:hidden path="term"/>
		<div class="control-group">
			<label style="text-align:right;width:150px">好友姓名：</label>
			<input type="text" name="detail.member.name" value="${friend.name}" readonly="true" style="width:230px" htmlEscape="false" maxlength="64" class="input-xlarge required"/>
			<span class="help-inline"  style="width:150px"> </span>
		</div>
		<div class="control-group">
			<label style="text-align:right;width:150px">好友手机号：</label>
			<input type="text" name="detail.member.username" value="${friend.username}" readonly="true" style="width:230px" htmlEscape="false" maxlength="64" class="input-xlarge required"/>
			<span class="help-inline"  style="width:150px"> </span>
		</div>
		<div class="control-group">
			<label style="text-align:right;width:150px">借款金额：</label>
			<form:input path="amount" style="width:230px" htmlEscape="false" class="input-xlarge required"/>
			<span class="help-inline"  style="width:150px"><font color="red">*</font></span>	
		</div>
		<div class="control-group">
			<label style="text-align:right;width:150px">还款方式：</label>
			<form:select path="repayType" onchange="changeRepayType(this)" style="width:245px" class="input-xlarge required">
				<form:options items="${fns:getDictList('repayType')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
			</form:select>
		</div>
		<div class="control-group">
			<label style="text-align:right;width:150px">借款时长：</label>
			<div class="input-append">			
				<input id="repayDate" name="repayDate" type="text" style="width:170px"  maxlength="20" class="input-medium Wdate required"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:true,onpicked:function(){pickDays();}});"/>					
				<span id="termDays" class="add-on" style="text-align:right;width:50px">0  天</span>
			</div>
			<span class="help-inline"  style="width:150px"><font color="red">*</font></span>	
		</div>
		
		<div class="control-group">
			<label style="text-align:right;width:150px">年化利率：</label>
			<form:select path="intRate" style="width:245px" class="input-xlarge required">
				<form:options items="${fns:getDictList('intRate')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
			</form:select>		
			<a href ="javascript:void(0);" class="btn btn-primary" style="width:80px;" onclick="calInterest()">利息试算</a> 
			<input type="text" id="interest" name="interest" style="width:111px" htmlEscape="false" readonly="readonly"/>
		</div>

		<div class="control-group">
			<label style="text-align:right;width:150px">支付密码：</label>
			<input type="password" id="payPassWord" name="payPassWord" style="width:230px" htmlEscape="false" maxlength="255" class="input-xlarge required"/>
			<span class="help-inline"  style="width:150px"><font color="red">*</font> </span>
		</div>
		<div class="form-actions">
			<input id="btnSubmit" class="btn btn-primary" style="width:160px;" type="submit" value="放款给好友"/>			
		</div>
	</form:form>
</body>
</html>