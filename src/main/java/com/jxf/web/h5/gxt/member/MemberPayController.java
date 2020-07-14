package com.jxf.web.h5.gxt.member;



import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jxf.loan.entity.NfsLoanApply;
import com.jxf.loan.entity.NfsLoanApply.LoanRole;
import com.jxf.loan.entity.NfsLoanApplyDetail;
import com.jxf.loan.entity.NfsLoanApplyDetail.Status;
import com.jxf.loan.entity.NfsLoanArbitration;
import com.jxf.loan.entity.NfsLoanArbitrationExecution;
import com.jxf.loan.entity.NfsLoanPartialAndDelay;
import com.jxf.loan.entity.NfsLoanRecord;
import com.jxf.loan.entity.NfsLoanRecord.DelayStatus;
import com.jxf.loan.service.NfsLoanApplyDetailService;
import com.jxf.loan.service.NfsLoanApplyService;
import com.jxf.loan.service.NfsLoanArbitrationExecutionService;
import com.jxf.loan.service.NfsLoanArbitrationService;
import com.jxf.loan.service.NfsLoanPartialAndDelayService;
import com.jxf.loan.service.NfsLoanRecordService;
import com.jxf.mem.entity.Member;
import com.jxf.mem.entity.MemberActTrx;
import com.jxf.mem.entity.MemberMessage;
import com.jxf.mem.service.MemberActTrxService;
import com.jxf.mem.service.MemberMessageService;
import com.jxf.mem.service.MemberService;
import com.jxf.mem.utils.VerifiedUtils;
import com.jxf.nfs.constant.TrxRuleConstant;
import com.jxf.pay.service.WxPayService;
import com.jxf.payment.entity.Payment;
import com.jxf.payment.service.PaymentService;
import com.jxf.svc.config.Constant;
import com.jxf.svc.config.Global;
import com.jxf.svc.plugin.PaymentPlugin;
import com.jxf.svc.plugin.PluginConfig;
import com.jxf.svc.plugin.PluginService;
import com.jxf.svc.plugin.weixinPayment.WxPaymentPlugin;
import com.jxf.svc.utils.Collections3;
import com.jxf.svc.utils.Exceptions;
import com.jxf.svc.utils.StringUtils;
import com.jxf.web.minipro.BaseController;
import com.jxf.web.model.ResponseData;
import com.jxf.wx.account.entity.WxAccount;
import com.jxf.wx.account.service.WxAccountService;
import com.jxf.wx.user.entity.WxUserInfo;
import com.jxf.wx.user.service.WxUserInfoService;

/**
 * Controller - 会员中心
 * 
 * @author xrd
 */
@Controller("gxtH5MemberPayController")
@RequestMapping(value="${gxtH5}/member/pay")
public class MemberPayController extends BaseController {
	
	private static final Logger log = LoggerFactory.getLogger(MemberPayController.class);
	
	@Autowired
	private WxUserInfoService wxUserInfoService;
	@Autowired
	private PluginService pluginService;
	@Autowired
	private WxPaymentPlugin wxPaymentPlugin;
	@Autowired
	private PaymentService paymentService;
	@Autowired
	private WxPayService wxPayService;
	@Autowired
	private NfsLoanApplyService loanApplyService;
	@Autowired
	private NfsLoanApplyDetailService applyDetailService;
	@Autowired
	private MemberService memberService;
	@Autowired
	private WxAccountService wxAccountService;
	@Autowired
	private NfsLoanPartialAndDelayService loanPartialAndDelayService;
	@Autowired
	private NfsLoanRecordService loanRecordService;
	@Autowired
	private NfsLoanArbitrationService arbitrationService;
	@Autowired
	private NfsLoanArbitrationExecutionService loanArbitrationExecutionService;
	@Autowired
	private MemberMessageService memberMessageService;
	@Autowired
	private MemberActTrxService memberActTrxService;
	
	/**
	 * 微信支付
	 * @param type
	 * @param orgId
	 * @param amount
	 * @param pwd
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "wxPay")
	@ResponseBody
	public ResponseData wxPaySubmit(String type, String orgId, String amount ,HttpServletRequest request) {
		Member member = memberService.getCurrent();
		if (member == null) {
			return ResponseData.error("登录状态已过期，请重新登录后再操作");
		}
		if(StringUtils.equals(type, "1") || StringUtils.equals(type, "2")) {
			String pwd = request.getParameter("pwd");
			ResponseData checkPayPwd = memberService.checkPayPwd(pwd, member);
			if(checkPayPwd == null) {
				return ResponseData.error("系统错误，请联系客服处理！");
			}
			if (checkPayPwd.getCode() != 0) {
				return ResponseData.error(checkPayPwd.getMessage());
			}
		}
		log.info("==============member:=====================" + member.getName());
		Integer verifiedList = member.getVerifiedList();
		if (!(VerifiedUtils.isVerified(verifiedList, 1) && VerifiedUtils.isVerified(verifiedList, 2))) {
			return ResponseData.error("请完成实名认证后再操作");
		}
		Payment.Type payType = Payment.Type.values()[Integer.valueOf(type)];
		BigDecimal payAmount = BigDecimal.ZERO;
		switch (payType) {
		case recharge:
			break;
		case loanDone:
			// 获取原业务
			NfsLoanApplyDetail applyDetail = applyDetailService.get(Long.valueOf(orgId));
			if (applyDetail == null) {
				log.error("错误的请求参数，orgId:{},没有找到对应的借条detail", orgId);
				return ResponseData.error("参数错误");
			}
			if(applyDetail.getLoanRole().equals(NfsLoanApply.LoanRole.loanee)) {
				if(!StringUtils.equals(member.getName(), applyDetail.getMember().getName())) {
					return ResponseData.error("不能操作与自己无关的借条");
				}
			}else{
				NfsLoanApply loanApply = loanApplyService.get(applyDetail.getApply());
				Member loanee = memberService.get(loanApply.getMember());
				if (!StringUtils.equals(member.getName(), loanee.getName())) {
					return ResponseData.error("不能操作与自己无关的借条");
				}
			}
			if (!applyDetail.getStatus().equals(NfsLoanApplyDetail.Status.pendingAgree)) {
				String statusName = getStatusName(applyDetail.getStatus());
				logger.error("detail{}的状态为{}", applyDetail.getId(), applyDetail.getStatus());
				return ResponseData.error("借条"+statusName+"，不能进行此操作");
			} 
			if(!applyDetail.getPayStatus().equals(NfsLoanApplyDetail.PayStatus.waitingPay)) {
				String payStatusName = getPayStatusName(applyDetail.getPayStatus());				
				logger.error("detail{}的状态为{}", applyDetail.getId(),payStatusName);
				return ResponseData.error("借条"+payStatusName+"，不能进行此操作");
			}
			payAmount = new BigDecimal(Global.getConfig("gxt.loanDoneFee"));
			break;
		case loanDelay:
			NfsLoanPartialAndDelay partialAndDelay = loanPartialAndDelayService.get(Long.valueOf(orgId));
			if(partialAndDelay == null) {
				log.error("错误的请求参数，orgId:{},没有找到对应的延期申请记录", orgId);
				return ResponseData.error("参数错误");
			}
			NfsLoanRecord nfsLoanRecord = loanRecordService.get(partialAndDelay.getLoan());
			LoanRole memberRole = partialAndDelay.getMemberRole();
			if((memberRole.equals(NfsLoanApply.LoanRole.loaner) && !partialAndDelay.getStatus().equals(NfsLoanPartialAndDelay.Status.confirm))
					||(memberRole.equals(NfsLoanApply.LoanRole.loanee) && !partialAndDelay.getStatus().equals(NfsLoanPartialAndDelay.Status.other))
					|| (memberRole.equals(NfsLoanApply.LoanRole.loaner) && !nfsLoanRecord.getDelayStatus().equals(NfsLoanRecord.DelayStatus.loanerApplyDelay))
					|| (memberRole.equals(NfsLoanApply.LoanRole.loanee) && !nfsLoanRecord.getDelayStatus().equals(NfsLoanRecord.DelayStatus.initial))) {
				return ResponseData.error("延期申请状态已改变，不能进行此操作");
			}
			if( !nfsLoanRecord.getLoanee().equals(member)) {
				return ResponseData.error("不能操作与自己无关的借条");
			}
			if(!partialAndDelay.getPayStatus().equals(NfsLoanPartialAndDelay.PayStatus.waitingPay)) {
				String payStatusName = getPayStatusName(partialAndDelay.getPayStatus());				
				logger.error("延期申请{}的状态为{}", partialAndDelay.getId(), payStatusName);
				return ResponseData.error("借条"+payStatusName+"，不能进行此操作");
			}
			payAmount = new BigDecimal(Global.getConfig("gxt.loanDelayFee"));
			break;
		case arbitration:
			NfsLoanArbitration loanArbitration = arbitrationService.get(Long.valueOf(orgId));
			if(loanArbitration == null) {
				log.error("错误的请求参数，orgId:{},没有找到对应的仲裁申请记录", orgId);
				return ResponseData.error("参数错误");
			}
			NfsLoanRecord loanRecord = loanRecordService.get(loanArbitration.getLoan());
			if(!loanRecord.getArbitrationStatus().equals(NfsLoanRecord.ArbitrationStatus.initial)) {
				return ResponseData.error("该借条已申请仲裁，不能重复申请");
			}
			if(!loanArbitration.getStatus().equals(NfsLoanArbitration.Status.waitingPay)) {
				return ResponseData.error("该仲裁申请已缴费，请勿重复操作");
			}
			payAmount = new BigDecimal(loanArbitration.getFee());
			break;
		case execution:
			NfsLoanArbitrationExecution arbitrationExecution = loanArbitrationExecutionService.get(Long.valueOf(orgId));
			if(arbitrationExecution == null) {
				log.error("错误的请求参数，orgId:{},没有找到对应的仲裁申请记录", orgId);
				return ResponseData.error("参数错误");
			}
			if(!arbitrationExecution.getStatus().equals(NfsLoanArbitrationExecution.ExecutionStatus.executionPayment)) {
				return ResponseData.error("该强执状态为"+ getExecutionStatusName(arbitrationExecution.getStatus())+",不能进行此操作");
			}
			payAmount = arbitrationExecution.getFee();
			break;
		default:
			break;
		}
		WxAccount gxtAccount = wxAccountService.findByCode("gxt");
		WxUserInfo wxUserInfo = wxUserInfoService.findByMember(member.getId(), "gxt");
		if(wxUserInfo == null) {
			return ResponseData.error("该用户微信尚未授权");
		}
		Payment payment = new Payment();
		payment.setPrincipalId(member.getId());
		payment.setChannel(Payment.Channel.valueOf(gxtAccount.getCode()));
		payment.setType(payType);
		payment.setOrgId(Long.valueOf(orgId));
		payment.setPaymentAmount(payAmount);
		payment.setPaymentFee(new BigDecimal("0.00"));
		payment.setStatus(Payment.Status.wait);
		List<PaymentPlugin> paymentPlugins = pluginService.getPaymentPlugins(true);
		for (PaymentPlugin paymentPlugin : paymentPlugins) {
			if (StringUtils.contains(paymentPlugin.getName(), "微信")) {
				payment.setPaymentPluginId(paymentPlugin.getId());
				payment.setPaymentPluginName(paymentPlugin.getName());
				break;
			}
		}
		if (payment.getPaymentPluginId() == null) {
			return ResponseData.error("系统异常，请联系客服！");
		}
		Map<String, String> paySignMap = new HashMap<String, String>();
		log.info("===公信堂请求微信支付接口====");
		// 获取插件参数
		PluginConfig config = wxPaymentPlugin.getPluginConfig();
		Map<String, String> configAttr = config.getAttributeMap();
		String key = configAttr.get(WxPaymentPlugin.PAYMENT_KEY_ATTRIBUTE_NAME);
		String mchId = configAttr.get(WxPaymentPlugin.PAYMENT_ACCTID_ATTRIBUTE_NAME);
		String url = configAttr.get(WxPaymentPlugin.REQUEST_URL_ATTRIBUTE_NAME);
		payment.setRequestUrl(url);
		payment.setMchId(mchId);
		payment.setKey(key);
		paymentService.save(payment);
		String notifyUrl = Global.getConfig("domain") + "/callback/wx/payNotify";
		log.info("公信堂微信下单回调地址："+notifyUrl);
		payment.setRemoteAddr(Global.getRemoteAddr(request));
		payment.setOpenID(wxUserInfo.getOpenid());
		payment.setNotifyUrl(notifyUrl);
		paySignMap = wxPayService.payUnifiedOrder(payment, gxtAccount);
		if(paySignMap.isEmpty()) {
			return ResponseData.error("请求微信支付异常");
		}
		if(StringUtils.equals(paySignMap.get("result_code"), "FAIL")) {
			String err_code_des = paySignMap.get("err_code_des");
			return ResponseData.error(err_code_des);
		}
		log.info("微信支付预付单："+paySignMap.toString());
		paymentService.save(payment);// 更新prepay_id
		return ResponseData.success("支付提交成功", paySignMap);
	}
	
	/**
	 * 账户余额支付
	 * @param type
	 * @param orgId
	 * @param pwd
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "actBalPay")
	@ResponseBody
	public ResponseData actBalPay(String type, String orgId, String pwd,HttpServletRequest request) {
		Member member = memberService.getCurrent();
		ResponseData checkPayPwd = memberService.checkPayPwd(pwd, member);
		if(checkPayPwd == null) {
			return ResponseData.error("系统错误，请联系客服处理！");
		}
		if (checkPayPwd.getCode() != 0) {
			return ResponseData.error(checkPayPwd.getMessage());
		}
		Integer verifiedList = member.getVerifiedList();
		if(!(VerifiedUtils.isVerified(verifiedList, 1) && VerifiedUtils.isVerified(verifiedList, 2))) {
			return ResponseData.error("请完成实名认证后再操作");
		}
		ResponseData responseData = null;
		NfsLoanRecord loanRecord = null;
		Payment.Type payType = Payment.Type.values()[Integer.valueOf(type)];
		switch (payType) {
		case recharge:
			break;
		case loanDone:
			NfsLoanApplyDetail loanApplyDetail = applyDetailService.get(Long.valueOf(orgId));
			if(loanApplyDetail == null) {
				log.error("错误的请求参数，orgId:{},没有找到对应的借条detail",orgId);
				return ResponseData.error("参数错误");
			}
			try {
				ResponseData checkResult = preCheckForLoanApplyPay(loanApplyDetail, member);
				if(checkResult != null) {
					return checkResult;
				}

				responseData = applyDetailService.loaneePayForApplyByActBal(loanApplyDetail);
				if(responseData == null || (responseData != null && responseData.getCode()< 0)) {
					log.error("会员：{}支付补借条detail:{}服务费失败{}",member.getId(),loanApplyDetail.getId());
					if(loanApplyDetail.getLoanRole().equals(NfsLoanApply.LoanRole.loaner)) {
						//借款人主动申请的补借条，支付失败就删除申请
						//更新下支付状态
						loanApplyDetail.setPayStatus(NfsLoanApplyDetail.PayStatus.fail);
						applyDetailService.update(loanApplyDetail);
						loanApplyService.delete(loanApplyDetail.getApply());
						applyDetailService.delete(loanApplyDetail);
					}
					return ResponseData.error("支付失败");
				}
			} catch (Exception e) {
				log.error("会员：{}支付补借条detail:{}服务费异常：{}",member.getId(),loanApplyDetail.getId(),Exceptions.getStackTraceAsString(e));
				if(loanApplyDetail.getLoanRole().equals(NfsLoanApply.LoanRole.loaner)) {
					//借款人主动申请的补借条，支付失败就删除申请
					//更新下支付状态
					loanApplyDetail.setPayStatus(NfsLoanApplyDetail.PayStatus.fail);
					applyDetailService.update(loanApplyDetail);
					loanApplyService.delete(loanApplyDetail.getApply());
					applyDetailService.delete(loanApplyDetail);
				}
				return ResponseData.error("支付失败");
			}
			
			//发消息
			sendMessageForLoanApplyPay(loanApplyDetail,member);
			break;
		case loanDelay:
			NfsLoanPartialAndDelay loanPartialAndDelay = loanPartialAndDelayService.get(Long.valueOf(orgId));
			if(loanPartialAndDelay == null) {
				log.error("错误的请求参数，orgId:{},没有找到对应的延期申请记录",orgId);
				return ResponseData.error("参数错误");
			}
			loanRecord = loanRecordService.get(loanPartialAndDelay.getLoan());
			Member loanee = memberService.get(loanRecord.getLoanee());
			if(loanee.getId().longValue() != member.getId().longValue()) {
				return ResponseData.error("不能操作不属于自己的延期申请");
			}
			NfsLoanPartialAndDelay.Status delayApplyStatus = loanPartialAndDelay.getStatus();
			NfsLoanRecord.DelayStatus recordDelayStatus = loanRecord.getDelayStatus();
			LoanRole memberRole = loanPartialAndDelay.getMemberRole();
			if((memberRole.equals(NfsLoanApply.LoanRole.loaner) && !delayApplyStatus.equals(NfsLoanPartialAndDelay.Status.confirm)) 
					|| (memberRole.equals(NfsLoanApply.LoanRole.loaner) && !recordDelayStatus.equals(DelayStatus.loanerApplyDelay))
					|| (memberRole.equals(NfsLoanApply.LoanRole.loanee) && !delayApplyStatus.equals(NfsLoanPartialAndDelay.Status.other))) {
				return ResponseData.error("延期申请状态已改变，不能进行此操作");
			}
			
			Member loaner = memberService.get(loanRecord.getLoaner());
			loanRecord.setLoaner(loaner);
			loanRecord.setLoanee(loanee);
			loanPartialAndDelay.setLoan(loanRecord);
			try {
				responseData = loanPartialAndDelayService.loaneePayForDelayByActBal(loanPartialAndDelay);
			} catch (Exception e) {
				log.error("延期申请：{}支付服务费时异常：{}",loanPartialAndDelay.getId(),Exceptions.getStackTraceAsString(e));
				if(loanPartialAndDelay.getMemberRole().equals(NfsLoanApply.LoanRole.loanee)) {
					//借款人主动申请的补借条，支付失败就删除申请
					loanPartialAndDelay.setPayStatus(NfsLoanPartialAndDelay.PayStatus.fail);
					loanPartialAndDelayService.update(loanPartialAndDelay);
					loanPartialAndDelayService.delete(loanPartialAndDelay);
				}
				return ResponseData.error("支付失败");
			}
			break;
		case arbitration:
			NfsLoanArbitration loanArbitration = arbitrationService.get(Long.valueOf(orgId));
			if(loanArbitration == null) {
				log.error("错误的请求参数，orgId:{},没有找到对应的仲裁申请记录", orgId);
				return ResponseData.error("参数错误");
			}
			
			loanRecord = loanRecordService.get(loanArbitration.getLoan());
			if(!loanRecord.getArbitrationStatus().equals(NfsLoanRecord.ArbitrationStatus.initial)) {
				return ResponseData.error("该借条已申请仲裁，不能重复申请");
			}
			if(!loanArbitration.getStatus().equals(NfsLoanArbitration.Status.waitingPay)) {
				return ResponseData.error("该仲裁申请已缴费，请勿重复操作");
			}
			loanArbitration.setLoan(loanRecord);
			try {
				int code = arbitrationService.payForApplyArbitration(member, loanArbitration);
				if(code == Constant.UPDATE_FAILED) {
					arbitrationService.delete(loanArbitration);
					return ResponseData.error("支付失败，仲裁申请缴费失败");
				}
			} catch (Exception e) {
				log.error(Exceptions.getStackTraceAsString(e));
				//缴费失败 删除申请
				arbitrationService.delete(loanArbitration);
				return ResponseData.error("支付失败，仲裁申请缴费失败");
			}
			sendMessageForArbitrationPay(loanArbitration,member);
			
			Map<String, String> data = new HashMap<String,String>();
			data.put("arbitrationId", loanArbitration.getId()+"");
			responseData = new ResponseData(0, "请求成功",data);
			break;
		case execution:
			NfsLoanArbitrationExecution arbitrationExecution = loanArbitrationExecutionService.get(Long.valueOf(orgId));
			if(arbitrationExecution == null) {
				log.error("错误的请求参数，orgId:{},没有找到对应的仲裁申请记录", orgId);
				return ResponseData.error("参数错误");
			}
			if(!arbitrationExecution.getStatus().equals(NfsLoanArbitrationExecution.ExecutionStatus.executionPayment)) {
				return ResponseData.error("该强执状态为"+ getExecutionStatusName(arbitrationExecution.getStatus())+",不能进行此操作");
			}
			arbitrationExecution.setStatus(NfsLoanArbitrationExecution.ExecutionStatus.executionProcessing);

			try {
				loanArbitrationExecutionService.payForExecutionApply(member, arbitrationExecution);
			} catch (Exception e) {
				//强执失败的
				log.error("强执申请：{}缴费异常：{}",arbitrationExecution.getId(),Exceptions.getStackTraceAsString(e));
				return ResponseData.error("强执申请缴费失败");
			}
			Map<String, String> data1 = new HashMap<String,String>();
			data1.put("arbitrationId", arbitrationExecution.getArbitrationId()+"");
			responseData = new ResponseData(0, "请求成功",data1);
			break;
		default:
			break;
		}
		return responseData;
	}
	
	@RequestMapping("getPayResult")
	@ResponseBody
	public ResponseData getPayResult(HttpServletRequest request,int type,Long orgId) {
		ResponseData responseData = null;
		Payment.Type payType = Payment.Type.values()[Integer.valueOf(type)];
		Map<String, Object> data = new HashMap<String,Object>();
		boolean isSuccess = true;
		String reason = "";
		Payment payment = null;
		switch (payType) {
		case recharge:
			break;
		case loanDone:
			NfsLoanApplyDetail loanApplyDetail = applyDetailService.get(orgId);
			payment = paymentService.getByTypeAndOrgId(Payment.Type.loanDone, orgId);
			if(loanApplyDetail == null) {
				reason = payment == null? reason:payment.getRespMsg();
				data.put("isSuccess", false);
				data.put("reason", reason);
				return ResponseData.success("请求成功",data);
			}
			if(payment.getStatus().equals(Payment.Status.failure)) {
				isSuccess = false;
				reason = payment.getRespCode();
			}
			data.put("isSuccess", isSuccess);
			data.put("reason", reason);
			responseData = new ResponseData(0, "请求成功", data);
			break;
		case loanDelay:
			NfsLoanPartialAndDelay partialAndDelay = loanPartialAndDelayService.get(orgId);
			payment = paymentService.getByTypeAndOrgId(Payment.Type.loanDelay, orgId);
			if(partialAndDelay == null) {
				reason = payment == null? reason:payment.getRespMsg();
				data.put("isSuccess", false);
				data.put("reason", reason);
				return ResponseData.success("请求成功",data);
			}
			if(payment.getStatus().equals(Payment.Status.failure)) {
				isSuccess = false;
				reason = payment.getRespCode();
			}
			data.put("isSuccess", isSuccess);
			data.put("reason", reason);
			responseData = new ResponseData(0, "请求成功", data);
			break;
		case arbitration:
			NfsLoanArbitration loanArbitration = arbitrationService.get(orgId);
			payment = paymentService.getByTypeAndOrgId(Payment.Type.arbitration, orgId);
			if(loanArbitration == null) {
				reason = payment == null? reason:payment.getRespMsg();
				data.put("isSuccess", false);
				data.put("reason", reason);
				return ResponseData.success("请求成功",data);
			}
			if(payment.getStatus().equals(Payment.Status.failure)) {
				isSuccess = false;
				reason = payment==null?reason:payment.getRespMsg();
			}
			data.put("isSuccess", isSuccess);
			data.put("reason", reason);
			responseData = new ResponseData(0, "请求成功", data);
			break;
		case execution:
			payment = paymentService.getByTypeAndOrgId(Payment.Type.execution, orgId);
			if(payment.getStatus().equals(Payment.Status.failure)) {
				isSuccess = false;
				reason = payment==null?reason:payment.getRespMsg();
			}
			data.put("isSuccess", isSuccess);
			data.put("reason", reason);
			responseData = new ResponseData(0, "请求成功", data);
			break;
		default:
			break;
		}
		return responseData;
	}
	
	private ResponseData preCheckForLoanApplyPay(NfsLoanApplyDetail loanApplyDetail,Member member) {
		if(loanApplyDetail.getLoanRole().equals(NfsLoanApply.LoanRole.loanee)) {
			if(!StringUtils.equals(member.getName(), loanApplyDetail.getMember().getName())) {
				return ResponseData.error("不能操作与自己无关的借条");
			}
		}else{
			NfsLoanApply loanApply = loanApplyService.get(loanApplyDetail.getApply());
			Member loanee = memberService.get(loanApply.getMember());
			if (!StringUtils.equals(member.getName(), loanee.getName())) {
				return ResponseData.error("不能操作与自己无关的借条");
			}
		}
		if(loanApplyDetail.getLoanRole().equals(NfsLoanApply.LoanRole.loanee)) {
			loanApplyDetail.setMember(member);
		}
		Status status = loanApplyDetail.getStatus();
		if (!loanApplyDetail.getStatus().equals(NfsLoanApplyDetail.Status.pendingAgree)
				|| !loanApplyDetail.getPayStatus().equals(NfsLoanApplyDetail.PayStatus.waitingPay)) {
			logger.error("detail{}的状态为{}", loanApplyDetail.getId(), status);
			String statusName = getStatusName(status);
			return ResponseData.error("借条"+statusName+"，不能进行此操作");
		}
		if(!status.equals(NfsLoanApplyDetail.Status.pendingAgree) || !loanApplyDetail.getPayStatus().equals(NfsLoanApplyDetail.PayStatus.waitingPay)) {
			logger.error("detail{}的状态为{}",loanApplyDetail.getId(),loanApplyDetail.getStatus());
			return ResponseData.error("借条状态已改变，请勿重复操作");
		}
		return null;
	}
	
	
	private void sendMessageForLoanApplyPay(NfsLoanApplyDetail loanApplyDetail,Member member) {
		Long orgTrxId = null;
		MemberActTrx memberActTrx = new MemberActTrx();
		memberActTrx.setOrgId(loanApplyDetail.getId());
		memberActTrx.setTrxCode(TrxRuleConstant.GXT_LOAN_DONE_LOANACT);
		List<MemberActTrx> list2 = memberActTrxService.findList(memberActTrx);
		if(!Collections3.isEmpty(list2)) {
			orgTrxId = list2.get(0).getId();
		}else {
			memberActTrx.setTrxCode(TrxRuleConstant.GXT_LOAN_DONE_AVLACT);
			List<MemberActTrx> list3 = memberActTrxService.findList(memberActTrx);
			if(!Collections3.isEmpty(list3)) {
				orgTrxId = list3.get(0).getId();
			}else {
				memberActTrx.setTrxCode(TrxRuleConstant.GXT_LOAN_DONE_WXPAY);
				List<MemberActTrx> list4 = memberActTrxService.findList(memberActTrx);
				if(!Collections3.isEmpty(list4)) {
					orgTrxId = list4.get(0).getId();
				}
			}
		}
		memberMessageService.sendMessage(MemberMessage.Type.payFeeForLoan,orgTrxId);
	}
	
	private void sendMessageForArbitrationPay(NfsLoanArbitration loanArbitration,Member member) {
		NfsLoanRecord loanRecord = loanArbitration.getLoan();
		memberMessageService.sendMessage(MemberMessage.Type.acceptanceArbitrationLoanee,loanRecord.getId());
		memberMessageService.sendMessage(MemberMessage.Type.acceptanceArbitrationLoaner,loanRecord.getId());
		
		Long orgTrxId1 = 0L;
		MemberActTrx memberActTrx1 = new MemberActTrx();
		memberActTrx1.setOrgId(loanArbitration.getId());
		memberActTrx1.setTrxCode(TrxRuleConstant.ARBITRATION_PREPAY);
		List<MemberActTrx> list5 = memberActTrxService.findList(memberActTrx1);
		if(!Collections3.isEmpty(list5)) {
			orgTrxId1 = list5.get(0).getId();
		}else {
			memberActTrx1.setTrxCode(TrxRuleConstant.GXT_ARBITRATION_PREPAY);
			List<MemberActTrx> list6 = memberActTrxService.findList(memberActTrx1);
			if(!Collections3.isEmpty(list6)) {
				orgTrxId1 = list6.get(0).getId();
			}
		}
		memberMessageService.sendMessage(MemberMessage.Type.payArbitrationFee,orgTrxId1);
	}
	
	
	private String getPayStatusName(NfsLoanApplyDetail.PayStatus payStatus) {
		switch (payStatus) {
		case waitingPay:
			return "待支付";
		case paying:
			return "支付中";
		case success:
			return "支付成功";
		case fail:
			return "支付失败";
		default:
			return "状态未知";
		}
	}
	
	private String getPayStatusName(NfsLoanPartialAndDelay.PayStatus payStatus) {
		switch (payStatus) {
		case waitingPay:
			return "待支付";
		case paying:
			return "支付中";
		case success:
			return "支付成功";
		case fail:
			return "支付失败";
		default:
			return "状态未知";
		}
	}
	
	private String getStatusName(NfsLoanApplyDetail.Status status) {
		String name = "";
		switch (status) {
		case pendingAgree:
			name = "待确认";
			break;
		case success:
			name = "已成功";
			break;
		case reject:
			name = "已拒绝";
			break;
		case canceled:
			name = "已取消";
			break;
		case expired:
			name = "已过期 ";
			break;
		default:
			name = "状态异常";
			break;
		}
		return name;
	}
	
	private String getExecutionStatusName(NfsLoanArbitrationExecution.ExecutionStatus status) {
		String name = "";
		switch (status) {
		case executionApplication:
			name = "申请中 ";
			break;
		case executionRefuseToAccept:
			name = "拒绝受理";
			break;
		case executionPayment:
			name = "缴费中";
			break;
		case executionProcessing:
			name = "进行中";
			break;
		case executionOver:
			name = "已结束 ";
			break;
		case executionExpired:
			name = "已失效 ";
			break;
		case executionFailure:
			name = "强执失败 ";
			break;
		case debit:
			name = "借条关闭";
			break;
		default:
			name = "状态异常";
			break;
		}
		return name;
	}
	
}