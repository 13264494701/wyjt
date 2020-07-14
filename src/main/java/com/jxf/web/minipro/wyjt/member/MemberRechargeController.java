package com.jxf.web.minipro.wyjt.member;


import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jxf.mem.entity.Member;
import com.jxf.mem.entity.MemberCard;
import com.jxf.mem.entity.MemberVerified;
import com.jxf.mem.service.MemberCardService;
import com.jxf.mem.service.MemberService;
import com.jxf.mem.service.MemberVerifiedService;
import com.jxf.nfs.entity.NfsBankProtocol;
import com.jxf.nfs.entity.NfsRchgRecord;
import com.jxf.nfs.service.NfsBankProtocolService;
import com.jxf.nfs.service.NfsRchgRecordService;
import com.jxf.pay.service.FuyouPayService;
import com.jxf.payment.entity.Payment;
import com.jxf.payment.service.PaymentService;
import com.jxf.svc.config.Global;
import com.jxf.svc.plugin.PaymentPlugin;
import com.jxf.svc.plugin.PluginConfig;
import com.jxf.svc.plugin.PluginService;
import com.jxf.svc.plugin.fuyouPayment.FyPaymentPlugin;
import com.jxf.svc.utils.EncryptUtils;
import com.jxf.web.minipro.BaseController;
import com.jxf.web.model.ResponseData;
import com.jxf.wx.user.entity.WxUserInfo;
import com.jxf.wx.user.service.WxUserInfoService;





/**
 * Controller - 会员信息
 * 
 * @author JINXINFU
 * @version 2.0
 */
@Controller("wyjtMiniproMemberRechargeController")
@RequestMapping(value="${wyjtMinipro}/member/recharge")
public class MemberRechargeController extends BaseController {


	@Autowired
	private MemberService memberService;
	@Autowired
	private WxUserInfoService wxUserInfoService;
	@Autowired
	private PluginService pluginService;
	@Autowired
	private MemberCardService memberCardService;
	@Autowired
	private MemberVerifiedService memberVerifiedService;
	@Autowired
	private NfsBankProtocolService nfsBankProtocolService;
	@Autowired
	private FyPaymentPlugin fyPaymentPlugin;
	@Autowired
	private PaymentService paymentService;
	@Autowired
	private FuyouPayService fyPayService;
	@Autowired
	private NfsRchgRecordService nfsRchgRecordService;

	
	/**
	 * 充值支付
	 */
	@RequestMapping(value = "/pay", method = RequestMethod.GET)
	@ResponseBody
	public ResponseData rechargePay() {	

		Map<String, Object> data = new HashMap<String, Object>();
		//获取member
		Member member = wxUserInfoService.getCurrent().getMember();
		member = memberService.get(member.getId());
		//获取member的银行卡信息
		MemberCard card = memberCardService.getCardByMember(member);
		//获取当前的支付插件信息
		PaymentPlugin paymentPlugin = pluginService.getPaymentPlugin(fyPaymentPlugin.getId());
		//获取member的签约协议信息
		NfsBankProtocol bankProtocol = new NfsBankProtocol();
		bankProtocol.setCardNo(card.getCardNo());
		bankProtocol.setMemberId(member.getId());
		bankProtocol.setPaymentPluginId(paymentPlugin.getId());
		List<NfsBankProtocol> protocols = nfsBankProtocolService.findList(bankProtocol);
		if(protocols == null || protocols.size() == 0) {
			data.put("hasProtocol", "0");
		}else {
			data.put("hasProtocol", "1");
			data.put("protocol", protocols.get(0));
		}
		//获取认证信息
		MemberVerified memberVerified = new MemberVerified();
		memberVerified.setMember(member);
		List<MemberVerified> memberVerifiedList = memberVerifiedService.findList(memberVerified);
		if(memberVerifiedList != null && memberVerifiedList.size() > 0) {
			data.put("name", memberVerifiedList.get(0).getRealName());
			//身份证号码加密中间几位
			data.put("idNo", EncryptUtils.encryptString(memberVerifiedList.get(0).getIdNo(), EncryptUtils.Type.ID));
			//手机号加密中间四位
			data.put("phoneNo", EncryptUtils.encryptString(memberVerifiedList.get(0).getPhoneNo(), EncryptUtils.Type.PHONE));
		}
		//银行卡号加密中间位
		data.put("cardNo", EncryptUtils.encryptString(card.getCardNo(), EncryptUtils.Type.CARD));
		return ResponseData.success("data",data);
	}
	
	/**
	 * 发送签约短信
	 */
	@RequestMapping(value = "/sendContractSms", method = RequestMethod.GET)
	@ResponseBody
	public ResponseData sendContractSms(BigDecimal money,HttpServletRequest request) {	
		Map<String, Object> data = new HashMap<String, Object>();

		Member member= wxUserInfoService.getCurrent().getMember();
		member = memberService.get(member.getId());

		MemberVerified paramMV = new MemberVerified();
		paramMV.setMember(member);
		List<MemberVerified> mvList = memberVerifiedService.findList(paramMV);
		MemberVerified memberInfo =mvList.get(0);
		
		MemberCard card = memberCardService.getCardByMember(member);
		
		NfsRchgRecord rchgRecord = new NfsRchgRecord();
		rchgRecord.setAmount(money);
		rchgRecord.setMember(member);
		rchgRecord.setChannel(NfsRchgRecord.Channel.minipro);
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
		payment.setMchId(MCHNTCD);
		payment.setPaymentAmount(money);
//		payment.setOpenID(member.getOpenId());
		payment.setRemoteAddr(Global.getRemoteAddr(request));
		paymentService.save(payment);
		Map<String, Object> mapFromJson = fyPayService.sendContractSms(memberInfo,payment);
		if(mapFromJson != null) {
			data.put("errCode", "0");
			data.put("mapFromJson", mapFromJson);
		}else {
			data.put("errCode", "100");
			data.put("errMsg", "哎呀！系统出了点小差～");
		}
		return ResponseData.success("data", data);
	}
	/**
	 * 首充协议支付
	 * @param orgId
	 * @param vifyCode
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/rechargeWithoutPro", method = RequestMethod.POST)
	@ResponseBody
	public ResponseData rechargeWithoutProtocol(String orderId,String verCode,HttpServletRequest request) {

		Map<String, Object> data = new HashMap<String, Object>();

		WxUserInfo wxUserInfo = wxUserInfoService.getCurrent();
		Member member = memberService.get(wxUserInfo.getMember().getId());

		MemberVerified paramMV = new MemberVerified();
		paramMV.setMember(member);
		List<MemberVerified> mvList = memberVerifiedService.findList(paramMV);
		MemberVerified memberInfo =mvList.get(0);
		MemberCard tempCard = new MemberCard();
		tempCard.setMember(member);
		List<MemberCard> cards = memberCardService.findList(tempCard);
		MemberCard card = cards.get(0);
		Payment payment = paymentService.getByPaymentNo(orderId);
		
		PluginConfig config = fyPaymentPlugin.getPluginConfig();
		Map<String,String> configAttr = config.getAttributeMap();
		String url = configAttr.get(FyPaymentPlugin.PAYMENT_BINDCOMMIT_REQUEST_URL_ATTRIBUTE_NAME);
		
		//协议绑卡
		Map<String, Object> resultMapdata = new HashMap<String,Object>();
		resultMapdata = fyPayService.bindCommit(memberInfo, payment, verCode);
		NfsBankProtocol bankProtocol = null;
		if("0".equals(resultMapdata.get("success"))) {
			//保存协议号
			bankProtocol = new NfsBankProtocol();
			bankProtocol.setProtocolNo(String.valueOf(resultMapdata.get("proNo")));
			bankProtocol.setMemberId(memberInfo.getId());
			bankProtocol.setCardNo(memberInfo.getCardNo());
			bankProtocol.setBankName(card.getBank().getName());
			bankProtocol.setPaymentPluginId(fyPaymentPlugin.getId());
			bankProtocol.setPaymentPluginName(fyPaymentPlugin.getName());
			nfsBankProtocolService.save(bankProtocol);
		}else {
			data.put("errCode", "100");
			data.put("errMsg", resultMapdata.get("reson"));
			payment.setRequestUrl(url);
			payment.setStatus(Payment.Status.failure);
			paymentService.save(payment);
			return ResponseData.success("data",data);
		}
		//协议支付
		payment.setProtocolNo(String.valueOf(resultMapdata.get("proNo")));
		Map<String, Object> mapFromJson = fyPayService.newProPay(payment);
		if(mapFromJson != null) {
			data.put("errCode", "0");
			data.put("mapFromJson", mapFromJson);
			Map<String, String> rechargeCache = new HashMap<>();
			rechargeCache.put("bankName", card.getBank().getName());
			rechargeCache.put("money", payment.getPaymentAmount().setScale(2) + "");
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			rechargeCache.put("rechargeTime", sdf.format(new Date()));
			data.put("rechargeCache", rechargeCache);
		}else {
			data.put("errCode", "100");
			data.put("errMsg", "哎呀！系统出了点小差～");
		}
		return ResponseData.success("data",data);
	}
	
	/**
	 * 非首充协议支付
	 * @return
	 */
	@RequestMapping(value = "newProPay" , method = RequestMethod.POST)
	@ResponseBody
	public ResponseData newProPay(BigDecimal money,HttpServletRequest request) {
		
		Map<String , Object> data = new HashMap<String,Object>();
		//获取用户信息
		Member member = wxUserInfoService.getCurrent().getMember();
		member = memberService.get(member.getId());
		MemberCard card = memberCardService.getCardByMember(member);
		//充值记录
		NfsRchgRecord rchgRecord = new NfsRchgRecord();
		rchgRecord.setAmount(money);
		rchgRecord.setMember(member);
		rchgRecord.setCard(card);
		rchgRecord.setBankName(card.getBank().getName());
		rchgRecord.setChannel(NfsRchgRecord.Channel.minipro);
		rchgRecord.setType(NfsRchgRecord.Type.fuiou);
		rchgRecord.setStatus(NfsRchgRecord.Status.wait);
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
		
		NfsBankProtocol bankProtocol = new NfsBankProtocol();
		bankProtocol.setMemberId(member.getId());
		List<NfsBankProtocol> protocols = nfsBankProtocolService.findList(bankProtocol);
		NfsBankProtocol protocol = protocols.get(0);
		payment.setProtocolNo(protocol.getProtocolNo());
		
		PluginConfig config = fyPaymentPlugin.getPluginConfig();
		Map<String,String> configAttr = config.getAttributeMap();
		String MCHNTCD = configAttr.get(FyPaymentPlugin.PAYMENT_MCHNTCD_ATTRIBUTE_NAME);
		payment.setMchId(MCHNTCD);
		payment.setPaymentAmount(money);
//		payment.setOpenID(member.getOpenId());
		payment.setRemoteAddr(Global.getRemoteAddr(request));
		paymentService.save(payment);
		Map<String, Object> mapFromJson = fyPayService.newProPay(payment);
		if(mapFromJson != null) {
			data.put("errCode", "0");
			data.put("mapFromJson", mapFromJson);
			Map<String, String> rechargeCache = new HashMap<>();
			rechargeCache.put("bankName", protocol.getBankName());
			rechargeCache.put("money", money.setScale(2) + "");
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			rechargeCache.put("rechargeTime", sdf.format(new Date()));
			data.put("rechargeCache", rechargeCache);
		}else {
			data.put("errCode", "100");
			data.put("errMsg", "哎呀！系统出了点小差～");
		}
		return ResponseData.success("data",data);
	}
	
	@RequestMapping(value="fynotify")
	public void fuiouNotify(HttpServletRequest request) {
		try {
			fyPayService.fuiouNotifyProcess(request);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}