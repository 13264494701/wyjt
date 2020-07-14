package com.jxf.web.app.wyjt.member;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.jxf.fee.service.NfsFeeRuleService;
import com.jxf.loan.entity.NfsLoanRecord;
import com.jxf.loan.service.NfsLoanRecordService;
import com.jxf.mem.entity.Member;
import com.jxf.mem.entity.MemberCard;
import com.jxf.mem.service.MemberCardService;
import com.jxf.mem.service.MemberService;
import com.jxf.mem.utils.H5Utils;
import com.jxf.mem.utils.VerifiedUtils;
import com.jxf.nfs.constant.TrxRuleConstant;
import com.jxf.nfs.entity.NfsRchgRecord;
import com.jxf.nfs.entity.NfsWdrlRecord;
import com.jxf.nfs.entity.NfsWdrlRecord.Status;
import com.jxf.nfs.service.NfsRchgRecordService;
import com.jxf.nfs.service.NfsWdrlRecordService;
import com.jxf.pay.entity.LianlianMerchantDataBean;
import com.jxf.payment.entity.Payment;
import com.jxf.payment.service.PaymentService;
import com.jxf.pwithdraw.utils.TraderRSAUtil;
import com.jxf.svc.config.Global;
import com.jxf.svc.plugin.PluginConfig;
import com.jxf.svc.plugin.fuyouPayment.FyPaymentPlugin;
import com.jxf.svc.plugin.lianlianPayment.LianlianPaymentPlugin;
import com.jxf.svc.utils.DateUtils;
import com.jxf.svc.utils.EncryptUtils;
import com.jxf.svc.utils.Exceptions;
import com.jxf.svc.utils.HttpUtils;
import com.jxf.svc.utils.MD5Code;
import com.jxf.svc.utils.StringUtils;
import com.jxf.web.app.BaseController;
import com.jxf.web.model.ResponseData;
import com.jxf.web.model.wyjt.app.act.ActRechargeRequestParam;
import com.jxf.web.model.wyjt.app.act.ActRechargeResponseResult;
import com.jxf.web.model.wyjt.app.act.LianlianRechargeResponseResult;
import com.jxf.web.model.wyjt.app.act.PayWayResponseResult;
import com.jxf.web.model.wyjt.app.act.PayWayResponseResult.PayWay;

/**
 *  充值提现
 * @author suHuimin
 * @version 2018-10-31
 */
@Controller("wyjtAppMemberRchgAndWithdrawController")
@RequestMapping(value = "${wyjtApp}/member")
public class MemberRchgAndWithdrawController extends BaseController {
	
	private static final Logger log = LoggerFactory.getLogger(MemberRchgAndWithdrawController.class);
	@Autowired
	private MemberService memberService;
	@Autowired
	private NfsRchgRecordService nfsRchgRecordService;
	@Autowired
	private FyPaymentPlugin fyPaymentPlugin;
	@Autowired
	private LianlianPaymentPlugin lianlianPaymentPlugin;
	@Autowired
	private PaymentService paymentService;
	@Autowired 
	private MemberCardService memberCardService;
	@Autowired
	private NfsWdrlRecordService nfsWdrlRecordService;
	@Autowired 
	private NfsFeeRuleService feeRuleService;
	@Autowired
	private NfsLoanRecordService loanRecordService;
	
	/**
	 * 富友充值-获取富友充值参数
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="gainFuiouPayData")
	@ResponseBody
	public ResponseData gainFuiouPayData(HttpServletRequest request,HttpServletResponse response) {
		String param = request.getParameter("param");
		ActRechargeRequestParam reqData = JSONObject.parseObject(param,ActRechargeRequestParam.class);
			
		String amount = reqData.getMoney();	
		if(StringUtils.isBlank(amount)) {
			return ResponseData.error("操作失败：充值金额为零");
		}
		if (new BigDecimal(amount).doubleValue() < 2) {
			return ResponseData.error("操作失败：充值金额需要大于2元");
		}
		
		Member member = memberService.getCurrent();
		Integer verifiedList = member.getVerifiedList();
		if(!(VerifiedUtils.isVerified(verifiedList, 1) && VerifiedUtils.isVerified(verifiedList, 2))) {
			return ResponseData.error("操作失败：您尚未进行实名认证，不能进行此操作");
		}
		MemberCard card = memberCardService.getCardByMember(member);
		if(card == null) {
			//正常情况下不会走到这一步
			return ResponseData.error("操作失败：您尚未绑定银行卡，不能进行此操作");
		}
		
		NfsRchgRecord rchgRecord = new NfsRchgRecord();
		rchgRecord.setAmount(new BigDecimal(amount));
		rchgRecord.setMember(member);
		rchgRecord.setChannel(NfsRchgRecord.Channel.app);
		rchgRecord.setType(NfsRchgRecord.Type.fuiou);
		rchgRecord.setStatus(NfsRchgRecord.Status.wait);
		rchgRecord.setCard(card);
		rchgRecord.setBankName(card.getBank().getName());
		nfsRchgRecordService.save(rchgRecord);
		
		Payment payment = new Payment();
		payment.setType(Payment.Type.recharge);
		payment.setPrincipalId(member.getId());
		payment.setChannel(Payment.Channel.wyjt);
		payment.setOrgId(rchgRecord.getId());
		payment.setType(Payment.Type.recharge);
		payment.setStatus(Payment.Status.wait);
		payment.setPaymentFee(new BigDecimal("0.00"));
		payment.setPaymentPluginId(fyPaymentPlugin.getId());
		payment.setPaymentPluginName(fyPaymentPlugin.getName());
		PluginConfig config = fyPaymentPlugin.getPluginConfig();
		Map<String,String> configAttr = config.getAttributeMap();
		String MCHNTCD = configAttr.get(FyPaymentPlugin.PAYMENT_MCHNTCD_ATTRIBUTE_NAME);
		String key = configAttr.get(FyPaymentPlugin.PAYMENT_KEY_ATTRIBUTE_NAME);
		payment.setMchId(MCHNTCD);
		payment.setPaymentAmount(new BigDecimal(amount));
		payment.setRemoteAddr(Global.getRemoteAddr(request));
		paymentService.save(payment);
		rchgRecord.setPayment(payment);
		nfsRchgRecordService.save(rchgRecord);//更新充值单号
		String out_trade_no = payment.getPaymentNo();
		String name  = member.getName();
		String idNo = member.getIdNo();
		String cardNo = card.getCardNo();
		String bankName = card.getBank().getName();
		String idType = "0";
		String version = "2.0";
		String signType = "MD5";
		String type = "02";
		String backUrl = Global.getConfig("domain") + "/callback/fuiou/fuiouPayNotifyForApp";
		String amt = StringUtils.StrTOInt(StringUtils.decimalToStr(new BigDecimal(amount), 2)) + "";
		String macSource = type + "|" + version + "|" + MCHNTCD + "|" + out_trade_no + "|" + member.getId() + "|" + amt + "|" + cardNo
				+ "|" + backUrl + "|" +name + "|" + idNo + "|" + idType + "|" + key;
		log.info("富友充值单号[{}]待发送的数据[{}]",payment.getPaymentNo(),macSource);
		String mac = MD5Code.MD5Encode(macSource, "UTF-8").toUpperCase();
		ActRechargeResponseResult result = new ActRechargeResponseResult();
		result.setMCHNTCD(MCHNTCD);
		result.setMCHNTORDERID(out_trade_no);
		result.setAMT(amt);
		result.setBACKURL(backUrl);
		result.setBANKCARD(cardNo);
		result.setBANKNAME(bankName);
		result.setIDTYPE(idType);
		result.setIDNO(idNo);
		result.setSIGNTP(signType);
		result.setTYPE(type);
		result.setVERSION(version);
		result.setUSERID(member.getId()+ "");
		result.setNAME(member.getName());
		result.setTIME(DateUtils.getDate("yyyy-MM-dd HH:mm:ss"));
		result.setSIGN(mac);
		return ResponseData.success("获取成功", result);
	}
	
	/**
	 * lianlian充值-获取lianlian充值参数
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="gainLianlianRechargeData")
	@ResponseBody
	public ResponseData gainLianlianRechargeData(HttpServletRequest request) {
		String param = request.getParameter("param");
		ActRechargeRequestParam reqData = JSONObject.parseObject(param,ActRechargeRequestParam.class);
			
		String amount = reqData.getMoney();	
		if(StringUtils.isBlank(amount)) {
			return ResponseData.error("操作失败：充值金额为零");
		}
		if (new BigDecimal(amount).doubleValue() < 2) {
			return ResponseData.error("操作失败：充值金额需要大于2元");
		}
		
		Member member = memberService.getCurrent();
		Integer verifiedList = member.getVerifiedList();
		if(!(VerifiedUtils.isVerified(verifiedList, 1) && VerifiedUtils.isVerified(verifiedList, 2))) {
			return ResponseData.error("操作失败：您尚未进行实名认证，不能进行此操作");
		}
		MemberCard card = memberCardService.getCardByMember(member);
		if(card == null) {
			//正常情况下不会走到这一步
			return ResponseData.error("操作失败：您尚未绑定银行卡，不能进行此操作");
		}
		
		NfsRchgRecord rchgRecord = new NfsRchgRecord();
		rchgRecord.setAmount(new BigDecimal(amount));
		rchgRecord.setMember(member);
		rchgRecord.setChannel(NfsRchgRecord.Channel.app);
		rchgRecord.setType(NfsRchgRecord.Type.lianlian);
		rchgRecord.setStatus(NfsRchgRecord.Status.wait);
		rchgRecord.setCard(card);
		rchgRecord.setBankName(card.getBank().getName());
		nfsRchgRecordService.save(rchgRecord);
		
		Payment payment = new Payment();
		payment.setType(Payment.Type.recharge);
		payment.setPrincipalId(member.getId());
		payment.setChannel(Payment.Channel.wyjt);
		payment.setOrgId(rchgRecord.getId());
		payment.setType(Payment.Type.recharge);
		payment.setStatus(Payment.Status.wait);
		payment.setPaymentFee(new BigDecimal("0.00"));
		payment.setPaymentPluginId(lianlianPaymentPlugin.getId());
		payment.setPaymentPluginName(lianlianPaymentPlugin.getName());
		PluginConfig config = lianlianPaymentPlugin.getPluginConfig();
		Map<String,String> configAttr = config.getAttributeMap();
		String oid_partner = configAttr.get(LianlianPaymentPlugin.PAYMENT_OID_PARTNER_ATTRIBUTE_NAME);
		String key = configAttr.get(LianlianPaymentPlugin.PAYMENT_BUSINESS_PRIVATE_KEY_ATTRIBUTE_NAME);
		payment.setMchId(oid_partner);
		payment.setPaymentAmount(new BigDecimal(amount));
		payment.setRemoteAddr(Global.getRemoteAddr(request));
		paymentService.save(payment);
		rchgRecord.setPayment(payment);
		nfsRchgRecordService.save(rchgRecord);//更新充值单号
		
		TreeMap<String, String> paramMap = new TreeMap<String,String>();
		paramMap.put("oid_partner", oid_partner);
		paramMap.put("no_order", payment.getPaymentNo());
		paramMap.put("dt_order", DateUtils.getAllNumTime(rchgRecord.getCreateTime()));
		paramMap.put("user_id", member.getId().toString());
		paramMap.put("api_version", "1.0");
		paramMap.put("sign_type", "RSA");
		paramMap.put("time_stamp", DateUtils.getAllNumTime());
		paramMap.put("busi_partner", "101001");
		paramMap.put("notify_url", Global.getConfig("domain") + "/callback/lianlian/rechargeNotify");
		paramMap.put("money_order", amount);
		//支付产品标识： 5，新认证收款
		paramMap.put("flag_pay_product", "5");
		String flag_chnl = "";
		String osType = request.getHeader("x-osType");
		if(StringUtils.equals(osType, "ios")) {
			flag_chnl = "1";
		}else {
			flag_chnl = "0";
		}
		paramMap.put("flag_chnl", flag_chnl);
		paramMap.put("id_type", "0");
		paramMap.put("id_no", member.getIdNo());
		paramMap.put("acct_name", member.getName());
		paramMap.put("card_no", card.getCardNo());
		
		String risk_item = "";
		JSONObject risk_itemJson = new JSONObject();
		//商品类目
		risk_itemJson.put("frms_ware_category", "2012");
		risk_itemJson.put("user_info_mercht_userno", member.getId().toString());
		risk_itemJson.put("user_info_bind_phone", member.getUsername());
		risk_itemJson.put("user_info_dt_register", DateUtils.getAllNumTime(member.getCreateTime()));
		risk_itemJson.put("goods_name", "用户充值");
		risk_itemJson.put("user_info_full_name", member.getName());
		risk_itemJson.put("user_info_id_no", member.getIdNo());
		risk_itemJson.put("user_info_identify_state", "1");
		//实名认证方式 1：银行卡认证 2：现场认证  3：身份证远程认证 4：其它认证 
		risk_itemJson.put("user_info_identify_type", "3");
		risk_itemJson.put("user_info_id_type", "0");
		risk_item = risk_itemJson.toString();
		paramMap.put("risk_item", risk_item);
		StringBuilder sb = new StringBuilder();
		int count = 0;
		for (Map.Entry<String, String> entry : paramMap.entrySet()) {
			count++;
			sb.append(entry.getKey() + "=" + entry.getValue());
			if(count != paramMap.size()) {
				sb.append("&");
			}
		}
		String signStr = sb.toString();
		String sign = TraderRSAUtil.sign(key, signStr);
		paramMap.put("sign", sign);
		try {
			String response = HttpUtils.doPostForWithdraw(LianlianMerchantDataBean.PAY_REQUEST_URL,JSONObject.toJSONString(paramMap).toString());
			JSONObject resp = JSONObject.parseObject(response);
			String ret_code = resp.getString("ret_code");
			log.info("连连充值下单返回结果：{}",response);
			if(StringUtils.equals(ret_code, "0000")) {
				String gateway_url = resp.getString("gateway_url");
				LianlianRechargeResponseResult result = new LianlianRechargeResponseResult();
				result.setGatewayUrl(gateway_url);
				result.setBankName(card.getBank().getName());
				String cardNo = card.getCardNo();
				result.setCardNo(cardNo.substring(cardNo.length()-4,cardNo.length()));
				return ResponseData.success("获取成功",result);
			}else {
				log.error("连连充值请求返回失败：{}",response);
				return ResponseData.error(resp.getString("ret_msg"));
			}
		} catch (Exception e) {
			log.error("会员：{}连连充值请求异常：{}",member.getId(),Exceptions.getStackTraceAsString(e));
			return ResponseData.error("网络错误");
		}
	}
	 
	/**
	 * 跳转提现页面
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="toCash")
	public ModelAndView  toCash(HttpServletRequest request,HttpServletResponse response) {
		Member member = memberService.getCurrent();
		ModelAndView mv = new ModelAndView("app/withdraw/withdraw");
		mv = H5Utils.addPlatform(member, mv);
		boolean canWithdraw = true;
		boolean bankFlag = false;
		boolean locked = false;
		if(member.getIsEnabled() == false || member.getIsLocked() == true) {
			locked = true;
		}
		MemberCard memberCard = memberCardService.getCardByMember(member);
		if(memberCard != null) {
			bankFlag = true;
		}
		//查看是否有逾期借条
		NfsLoanRecord loanRecord = new NfsLoanRecord();
		loanRecord.setLoanee(member);
		loanRecord.setStatus(NfsLoanRecord.Status.overdue);
		int overdueCount = loanRecordService.countLoaneeLoan(loanRecord);
		if(overdueCount > 0) {
			canWithdraw = false;
		}
		BigDecimal fee = feeRuleService.getFee(TrxRuleConstant.MEMBER_WITHDRAWALS_FEE,null);
		String token = request.getHeader("x-memberToken");
		mv.addObject("memberToken",token);
		mv.addObject("locked", locked);
		mv.addObject("bankFlag", bankFlag);
		mv.addObject("canWithdraw", canWithdraw);
		mv.addObject("withdrawSxf", StringUtils.decimalToStr(fee, 2));
		mv.addObject("member", member);
		BigDecimal curBal = memberService.getCulBal(member);
		mv.addObject("curBal", StringUtils.decimalToStr(curBal, 2));
		return mv;
	}
	/**
	 * 校验提现金额
	 * @param money
	 * @return
	 */
	@RequestMapping(value="checkMoney")
	@ResponseBody
	public Map<String, Object> checkMoney(String money) {
		Map<String, Object> data = new HashMap<String, Object>();
		if(StringUtils.isBlank(money)) {
			data.put("success", false);
			data.put("errStr", "提现金额不能为空");
			return data;
		}
		Member member = memberService.getCurrent2();	
		//获取提现申请或通过的提现申请判断是否还能提钱
		Map<String, Object> result = nfsWdrlRecordService.checkMoney(member,new BigDecimal(money));
		boolean flag = (boolean) result.get("success"); //false为不能提现
		if(!flag) {
			data.put("success", false);
			data.put("errStr", "提现金额超出限制");
		}else {
			data.put("success", true);
		}
		return data;
	}
	                                                                                                                                                                                                                                                                                                                                                                                                                                                   
	/**
	 * 提现确认页面
	 * @param money
	 * @return
	 */
	@RequestMapping(value="confirmCash")
	public ModelAndView  confirmCash(HttpServletRequest request,String money) {
		Member member = memberService.getCurrent();
		ModelAndView mv = new ModelAndView("app/withdraw/withdraw_sub");
		mv = H5Utils.addPlatform(member, mv);
		MemberCard card = memberCardService.getCardByMember(member);
		BigDecimal fee = feeRuleService.getFee(TrxRuleConstant.MEMBER_WITHDRAWALS_FEE,null);
		BigDecimal amount = new BigDecimal(money);
		BigDecimal actualAmount = amount.subtract(fee);
	    String memberToken = request.getHeader("x-memberToken");
	    mv.addObject("memberToken", memberToken);
	    mv.addObject("member", member);
		String curBal = memberService.getMemberInfo(member).getCurBal();
		if(curBal == null || curBal.length() == 0) {
			curBal = "0.00";
		}
		mv.addObject("curBal", curBal);
		mv.addObject("withdrawSxf", StringUtils.decimalToStr(fee, 2));
		mv.addObject("actualMoney", StringUtils.decimalToStr(actualAmount, 2));
		mv.addObject("money", StringUtils.decimalToStr(amount, 2));
		mv.addObject("cardNo", EncryptUtils.encryptString(card.getCardNo(), EncryptUtils.Type.CARD));
		mv.addObject("isPayPsw", true);
		return mv;
	}
	/*
	 *提现确认提交 
	 */
	@RequestMapping(value="submit")
	public ModelAndView  submit(HttpServletRequest request,String money) {
		Member member = memberService.getCurrent();
		ModelAndView mv = new ModelAndView("app/withdraw/failResult");
		mv = H5Utils.addPlatform(member, mv);
		ModelAndView mv2 = new ModelAndView("app/withdraw/withdraw_result");
		if(money == null || money.length() == 0) {
			mv.addObject("message","金额输入有误，请重新输入");
			return mv;
		}	
		MemberCard memberCard = memberCardService.getCardByMember(member);
		if(memberCard == null) {
			mv.addObject("message","您还没有绑定银行卡，暂时不能提现，请先去绑定银行卡！");
			return mv;
		}	
		BigDecimal curBal = memberService.getCulBal(member);
		BigDecimal amount = new BigDecimal(money);
		if (curBal.compareTo(amount) < 0) {
			mv.addObject("message","已超过提现额度");
			return mv;
		}
		int flag = nfsWdrlRecordService.isNeedCheck(amount, member);
		try {
			NfsWdrlRecord wdrlRecord = new NfsWdrlRecord();
			BigDecimal fee = feeRuleService.getFee(TrxRuleConstant.MEMBER_WITHDRAWALS_FEE,null);
			wdrlRecord.setMember(member);
			wdrlRecord.setAmount(amount);
			wdrlRecord.setFee(fee.toString());
			wdrlRecord.setType(NfsWdrlRecord.Type._default);
			wdrlRecord.setBankId(memberCard.getBank().getId());
			wdrlRecord.setBankName(memberCard.getBank().getName());
			wdrlRecord.setCardNo(memberCard.getCardNo());
			if(memberService.lockExists(member)) {
				wdrlRecord.setStatus(Status.retrial);
			}else {
				wdrlRecord.setStatus(NfsWdrlRecord.Status.auditing);
			}
			wdrlRecord.setCheckTime(new Date());
			wdrlRecord.setSource(NfsWdrlRecord.Source.APP);
			wdrlRecord = nfsWdrlRecordService.withdrawSubmit(wdrlRecord,flag);
			if(wdrlRecord != null && wdrlRecord.getId() != null){
				String cardNo = memberCard.getCardNo().substring(memberCard.getCardNo().length()-5);
				String createTime = DateUtils.getDateStr(new Date(), "yyyy-MM-dd HH:mm:ss");
				mv2.addObject("money", StringUtils.decimalToStr(amount, 2));
				mv2.addObject("bankName", memberCard.getBank().getName());
				mv2.addObject("cardNo",cardNo);
				mv2.addObject("createTime", createTime);
				String memberToken = request.getHeader("x-memberToken");
				mv2.addObject("memberToken", memberToken);
				mv2 = H5Utils.addPlatform(member, mv2);
				return mv2;
			}else{
				mv.addObject("message", "提现申请失败");
				return mv;
			}
		} catch (Exception e) {
			//扣款失败
			log.error(Exceptions.getStackTraceAsString(e));
			mv.addObject("message", "提现申请失败");
			return mv;
		}
	}
	
	
	/**
	 * 提现记录列表
	 * @param request
	 * @return
	 */
	@RequestMapping(value="withdrawList")
	public ModelAndView withdrawList(HttpServletRequest request) {
		Member member = memberService.getCurrent();
		ModelAndView mv2 = new ModelAndView("app/withdraw/withdraw_list");
		//TODO 分页优化
		NfsWdrlRecord record = new NfsWdrlRecord();
		record.setMember(member);
		List<NfsWdrlRecord> listAuditing = nfsWdrlRecordService.findAuditingRecord(record);
		mv2.addObject("listAuditing",listAuditing);
		NfsWdrlRecord record2 = new NfsWdrlRecord();
		record2.setMember(member);
		record2.setStatus(NfsWdrlRecord.Status.madeMoney);
		List<NfsWdrlRecord> listMadeMoney= nfsWdrlRecordService.findList(record2);
		mv2.addObject("listMadeMoney",listMadeMoney);
		NfsWdrlRecord record3 = new NfsWdrlRecord();
		record3.setMember(member);
		List<NfsWdrlRecord> listFail= nfsWdrlRecordService.findFailedRecord(record3);
		mv2.addObject("listFail",listFail);
		mv2 = H5Utils.addPlatform(member, mv2);
		//TODO 分页优化
		return mv2;
	}
	
    /**
     * 进入充值方式页面
     * @param request
     * @return
     */
    @RequestMapping(value="payWayList")
    @ResponseBody
    public ResponseData payWayList(HttpServletRequest request) {
    	PayWayResponseResult result = new PayWayResponseResult();
    	String tips = "近期发现有诈骗分子，骗取用户登录/支付/密码以及验证码，让用户充值后，进入账户非法操作。请您谨慎操作，保护好个人隐私！举报电话：400-6688-658";
    	result.setTips(tips);
    	List<PayWay> payWays = new ArrayList<PayWay>(2);
    	PayWay payWay = new PayWayResponseResult().new PayWay();
    	payWay.setExplain("无需开通网银，免手续费");
    	payWay.setLogo(Global.getConfig("domain") + "/icon/recharge/kuaijiezhifu.png");
    	String appVersion = request.getHeader("x-appVersion");
    	String appType = request.getHeader("x-osType");
    	if((StringUtils.equals(appVersion,"4.09")&&StringUtils.equals(appType,"ios"))
    			|| (StringUtils.equals(appVersion,"3.61")&&StringUtils.equals(appType,"android"))) {
    		payWay.setMethod(Global.getConfig("rechargeMethod"));
    	}else {
    		payWay.setMethod("fuiou");
    	}
    	payWay.setName("快捷支付");
    	payWays.add(payWay);
    	PayWay payWay2 = new PayWayResponseResult().new PayWay();
    	payWay2.setExplain("打款后需联系客服加款");
    	payWay2.setLogo(Global.getConfig("domain") + "/icon/recharge/daezhifu.png");
    	payWay2.setMethod("daeCz");
    	payWay2.setName("大额支付");
    	payWays.add(payWay2);
    	result.setPayWays(payWays);
		return  ResponseData.success("获取成功", result);
	} 
     
   /**
    * 大额支付
    * @return
    */
	 @RequestMapping("largeRecharge")
	 public String largeRecharge(HttpServletRequest request) {
		  return "app/largeRecharge";
	} 
}