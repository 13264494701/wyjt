package com.jxf.pay.service.impl;


import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jxf.mem.entity.Member;
import com.jxf.mem.entity.MemberActTrx;
import com.jxf.mem.entity.MemberMessage;
import com.jxf.mem.entity.MemberVerified;
import com.jxf.mem.service.MemberActTrxService;
import com.jxf.mem.service.MemberMessageService;
import com.jxf.mem.service.MemberService;
import com.jxf.nfs.constant.TrxRuleConstant;
import com.jxf.nfs.entity.NfsBankProtocol;
import com.jxf.nfs.entity.NfsRchgRecord;
import com.jxf.nfs.service.NfsActService;
import com.jxf.nfs.service.NfsRchgRecordService;
import com.jxf.pay.service.FuyouPayService;
import com.jxf.pay.utils.DESCoderFUIOU;
import com.jxf.pay.utils.HttpPoster;
import com.jxf.payment.dao.PaymentDao;
import com.jxf.payment.entity.Payment;
import com.jxf.payment.service.PaymentService;
import com.jxf.svc.config.Constant;
import com.jxf.svc.config.Global;
import com.jxf.svc.plugin.PluginConfig;
import com.jxf.svc.plugin.fuyouPayment.FyPaymentPlugin;
import com.jxf.svc.security.MD5Utils;
import com.jxf.svc.utils.Collections3;
import com.jxf.svc.utils.Exceptions;
import com.jxf.svc.utils.MD5Code;
import com.jxf.svc.utils.StringUtils;

/**
 * 支付相关API
 *
 * @author lixin
 * @since 1.2
 */
@Service("fyPayService")
public class FuyouPayServiceImpl implements FuyouPayService {

	private static final Logger log = LoggerFactory.getLogger(FuyouPayServiceImpl.class);
	@Autowired
	private PaymentService paymentService;
	@Autowired
	private PaymentDao paymentDao;
	@Autowired
	private NfsRchgRecordService nfsRchgRecordService;
	@Autowired
	private NfsActService actService;
	@Autowired
	private FyPaymentPlugin fyPaymentPlugin;
	@Autowired 
	private MemberService memberService;
	@Autowired
	private MemberMessageService memberMessageService;
	@Autowired
	private MemberActTrxService memberActTrxService;
	
	
	
	@Override
	public Map<String, Object> sendContractSms(MemberVerified memberInfo,Payment payment) {
		PluginConfig config = fyPaymentPlugin.getPluginConfig();
		Map<String,String> configAttr = config.getAttributeMap();
		String merKey = configAttr.get(FyPaymentPlugin.PAYMENT_KEY_ATTRIBUTE_NAME);
		String MCHNTCD=	configAttr.get(FyPaymentPlugin.PAYMENT_MCHNTCD_ATTRIBUTE_NAME);
		String sendurl= configAttr.get(FyPaymentPlugin.PAYMENT_BINDMSG_REQUEST_URL_ATTRIBUTE_NAME);
		String orderNo = payment.getPaymentNo();
		String userId = String.valueOf(memberInfo.getMember().getId());
		String idCard = memberInfo.getIdNo();
		String realName = memberInfo.getRealName();
		String mobileNum = memberInfo.getPhoneNo();
		String bankNO = memberInfo.getCardNo();
		String version = "1.0";
		
		Map<String, Object> mapFromJson = new HashMap<String, Object>();
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		StringBuffer bf = new StringBuffer();
		try {
			bf.append("<?xml ");
			bf.append("version=\"1.0\" encoding=\"");
			bf.append("UTF-8");
			bf.append("\"?>");
			bf.append("<REQUEST>");

			bf.append("<VERSION>");
			bf.append(version);
			bf.append("</VERSION>");

			// 订单号
			bf.append("<MCHNTSSN>");
			bf.append(orderNo);
			bf.append("</MCHNTSSN>");

			bf.append("<MCHNTCD>");
			bf.append(MCHNTCD);
			bf.append("</MCHNTCD>");

			bf.append("<USERID>");
			bf.append(userId);
			bf.append("</USERID>");
			// 日期
			bf.append("<TRADEDATE>");
			bf.append(sdf.format(date));
			bf.append("</TRADEDATE>");

			bf.append("<IDCARD>");
			bf.append(idCard);
			bf.append("</IDCARD>");

			bf.append("<IDTYPE>");
			bf.append("0");
			bf.append("</IDTYPE>");

			// 真实名称
			bf.append("<ACCOUNT>");
			bf.append(realName);
			bf.append("</ACCOUNT>");
			// 卡号
			bf.append("<CARDNO>");
			bf.append(bankNO);
			bf.append("</CARDNO>");

			bf.append("<MOBILENO>");
			bf.append(mobileNum);
			bf.append("</MOBILENO>");

			String signM = version + "|" + orderNo + "|" + MCHNTCD + "|" + userId + "|" + realName + "|" + bankNO + "|"
					+ "0" + "|" + idCard + "|" + mobileNum + "|" + merKey;
			signM = MD5Utils.EncoderByMd5(signM);

			bf.append("<SIGN>");
			bf.append(signM);
			bf.append("</SIGN>");
			bf.append("</REQUEST>");
			Map<String, String> map = new HashMap<String, String>();
			String xml = bf.toString();
			xml = DESCoderFUIOU.desEncrypt(xml, DESCoderFUIOU.getKeyLength8(merKey));
			map.put("MCHNTCD", MCHNTCD);
			map.put("APIFMS", xml);
			String result = new HttpPoster(sendurl).postStr(map);
			String html = DESCoderFUIOU.desDecrypt(result, DESCoderFUIOU.getKeyLength8(merKey));
			log.info("签约短信请求返回信息：" + html);
			String substring = html.substring(html.indexOf("<RESPONSECODE>") + 14, html.indexOf("</RESPONSECODE>"));
			log.info(substring);
			String reson = html.substring(html.indexOf("<RESPONSEMSG>") + 13, html.indexOf("</RESPONSEMSG>"));
			String respOrderNo = html.substring(html.indexOf("<MCHNTSSN>") + 10, html.indexOf("</MCHNTSSN>"));
			if ("0000".equals(substring)) {
				mapFromJson.put("sucess","0");
				mapFromJson.put("reson", reson);
				mapFromJson.put("orderNo",respOrderNo);
			}else{
				mapFromJson.put("sucess", "-1");
				mapFromJson.put("reson", reson);
				mapFromJson.put("orderNo",respOrderNo);
			}
			
		} catch (Exception e) {
		}
		return mapFromJson;
	}

	@Override
	public Map<String, Object> bindCommit(MemberVerified memberInfo,Payment payment, String vifyCode) {
		Map<String, Object> mapFromJson = new HashMap<String, Object>();
		PluginConfig config = fyPaymentPlugin.getPluginConfig();
		Map<String,String> configAttr = config.getAttributeMap();
		String orderId = payment.getPaymentNo();
		String MCHNTCD = configAttr.get(FyPaymentPlugin.PAYMENT_MCHNTCD_ATTRIBUTE_NAME);
		String userId = String.valueOf(memberInfo.getMember().getId());
		String idCard = memberInfo.getIdNo();
		String realName = memberInfo.getRealName();
		String bankNO = memberInfo.getCardNo();
		String mobileNum = memberInfo.getPhoneNo();
		String merKey = configAttr.get(FyPaymentPlugin.PAYMENT_KEY_ATTRIBUTE_NAME);
		String version = "1.0";
		String checkMsgurl = configAttr.get(FyPaymentPlugin.PAYMENT_BINDCOMMIT_REQUEST_URL_ATTRIBUTE_NAME);
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		StringBuffer bf = new StringBuffer();
		try {
			bf.append("<?xml ");
			bf.append("version=\"1.0\" encoding=\"");
			bf.append("UTF-8");
			bf.append("\"?>");
			bf.append("<REQUEST>");

			bf.append("<VERSION>");
			bf.append(version);
			bf.append("</VERSION>");

			// 订单号
			bf.append("<MCHNTSSN>");
			bf.append(orderId + "");
			bf.append("</MCHNTSSN>");

			bf.append("<MCHNTCD>");
			bf.append(MCHNTCD);
			bf.append("</MCHNTCD>");

			bf.append("<USERID>");
			bf.append(userId + "");
			bf.append("</USERID>");
			// 日期
			bf.append("<TRADEDATE>");
			bf.append(sdf.format(date));
			bf.append("</TRADEDATE>");

			bf.append("<IDCARD>");
			bf.append(idCard);
			bf.append("</IDCARD>");

			bf.append("<IDTYPE>");
			bf.append("0");
			bf.append("</IDTYPE>");

			// 真实名称
			bf.append("<ACCOUNT>");
			bf.append(realName);
			bf.append("</ACCOUNT>");
			// 卡号
			bf.append("<CARDNO>");
			bf.append(bankNO);
			bf.append("</CARDNO>");

			bf.append("<MOBILENO>");
			bf.append(mobileNum);
			bf.append("</MOBILENO>");
			
			bf.append("<MSGCODE>");
			bf.append(vifyCode);
			bf.append("</MSGCODE>");
			

			String signM = version + "|" + orderId + "|" + MCHNTCD + "|" + userId + "|" + realName + "|" + bankNO + "|"
					+ "0" + "|" + idCard + "|" + mobileNum + "|" + vifyCode+ "|" +merKey ;
			signM = MD5Utils.EncoderByMd5(signM);

			bf.append("<SIGN>");
			bf.append(signM);
			bf.append("</SIGN>");
			bf.append("</REQUEST>");
			Map<String, String> map = new HashMap<String, String>();
			String xml = bf.toString();
			xml = DESCoderFUIOU.desEncrypt(xml, DESCoderFUIOU.getKeyLength8(merKey));
			map.put("MCHNTCD", MCHNTCD);
			map.put("APIFMS", xml);
			String result = new HttpPoster(checkMsgurl).postStr(map);
			String html = DESCoderFUIOU.desDecrypt(result, DESCoderFUIOU.getKeyLength8(merKey));
			log.info("协议绑卡请求返回信息：" + html);
			String substring = html.substring(html.indexOf("<RESPONSECODE>") + 14, html.indexOf("</RESPONSECODE>"));
			String reson = html.substring(html.indexOf("<RESPONSEMSG>") + 13, html.indexOf("</RESPONSEMSG>"));
			String orderNo = html.substring(html.indexOf("<MCHNTSSN>") + 10, html.indexOf("</MCHNTSSN>"));
			log.info(orderNo);
			if ("0000".equals(substring)) {
				mapFromJson.put("sucess","0");
				mapFromJson.put("reson", reson);
				mapFromJson.put("orderNo",orderNo);
				String proNO  = html.substring(html.indexOf("<PROTOCOLNO>") + 12, html.indexOf("</PROTOCOLNO>"));
				log.info("签约协议号：" + proNO);
				mapFromJson.put("proNO",proNO);
			}else{
				mapFromJson.put("sucess", "-1");
				mapFromJson.put("reson", reson);
				mapFromJson.put("orderNo",orderNo);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mapFromJson;
	}

	@Override
	public Map<String, Object> newProPay(Payment payment) {
		log.info("============富友支付开始============");
		Map<String, Object> mapFromJson = new HashMap<String, Object>();
		PluginConfig config = fyPaymentPlugin.getPluginConfig();
		Map<String,String> configAttr = config.getAttributeMap();
		String VERSION = "1.0";
		String orderNo = payment.getPaymentNo();
		String MCHNTCD = configAttr.get(FyPaymentPlugin.PAYMENT_MCHNTCD_ATTRIBUTE_NAME);
		String userId = String.valueOf(payment.getPrincipalId());
		String TYPE = "03";
		String money = StringUtils.StrTOInt(payment.getPaymentAmount().toString()) + "";
		String proNO = payment.getProtocolNo();
		String BACKURL = Global.getConfig("domain") + Global.getConfig("wyjtMinipro") + "/member/recharge/fynotify";
		log.info("异步回调通知地址： " + BACKURL );
		String userIp = payment.getRemoteAddr();
		String merKey = configAttr.get(FyPaymentPlugin.PAYMENT_KEY_ATTRIBUTE_NAME);
		String payurl = configAttr.get(FyPaymentPlugin.PAYMENT_ORDER_REQUEST_URL_ATTRIBUTE_NAME);
		StringBuffer bf = new StringBuffer();
		try {
			bf.append("<?xml ");
			bf.append("version=\"1.0\" encoding=\"");
			bf.append("UTF-8");
			bf.append("\"?>");
			bf.append("<REQUEST>");

			bf.append("<VERSION>");
			bf.append(VERSION);
			bf.append("</VERSION>");

			bf.append("<USERIP>");
			bf.append(userIp);
			bf.append("</USERIP>");

			bf.append("<MCHNTCD>");
			bf.append(MCHNTCD);
			bf.append("</MCHNTCD>");
			
			bf.append("<TYPE>");
			bf.append(TYPE);
			bf.append("</TYPE>");
			// 订单号
			bf.append("<MCHNTORDERID>");
			bf.append(orderNo + "");
			bf.append("</MCHNTORDERID>");

			bf.append("<USERID>");
			bf.append(userId + "");
			bf.append("</USERID>");

			bf.append("<AMT>");
			bf.append(money);
			bf.append("</AMT>");

			bf.append("<PROTOCOLNO>");
			bf.append(proNO);
			bf.append("</PROTOCOLNO>");

			bf.append("<NEEDSENDMSG>");
			bf.append("0");
			bf.append("</NEEDSENDMSG>");
			
			bf.append("<BACKURL>");
			bf.append(BACKURL);
			bf.append("</BACKURL>");
			
			bf.append("<SIGNTP>");
			bf.append("MD5");
			bf.append("</SIGNTP>");
			
			String signM =TYPE + "|" +VERSION + "|" + MCHNTCD + "|" + orderNo + "|" + userId + "|" + proNO + "|"
					+ money + "|" + BACKURL+ "|" +userIp+ "|" +merKey ;
			signM = MD5Utils.EncoderByMd5(signM);

			bf.append("<SIGN>");
			bf.append(signM);
			bf.append("</SIGN>");
			bf.append("</REQUEST>");
			Map<String, String> map = new HashMap<String, String>();
			String xml = bf.toString();
			xml = DESCoderFUIOU.desEncrypt(xml, DESCoderFUIOU.getKeyLength8(merKey));
			map.put("MCHNTCD", MCHNTCD);
			map.put("APIFMS", xml);
			String result = new HttpPoster(payurl).postStr(map);
			String html = DESCoderFUIOU.desDecrypt(result, DESCoderFUIOU.getKeyLength8(merKey));
			log.info("协议支付请求返回信息：" + html);
			String substring = html.substring(html.indexOf("<RESPONSECODE>") + 14, html.indexOf("</RESPONSECODE>"));
			String reson = html.substring(html.indexOf("<RESPONSEMSG>") + 13, html.indexOf("</RESPONSEMSG>"));
			String RespOrderNo = html.substring(html.indexOf("<MCHNTORDERID>") + 14, html.indexOf("</MCHNTORDERID>"));
			log.info(orderNo);
			if ("0000".equals(substring)) {
				mapFromJson.put("sucess","0");
				mapFromJson.put("reson", reson);
				mapFromJson.put("orderNo",orderNo);
				String amt = html.substring(html.indexOf("<AMT>") + 5, html.indexOf("</AMT>"));
				mapFromJson.put("money",amt);
			}else{
				mapFromJson.put("sucess", "-1");
				mapFromJson.put("reson", reson);
				mapFromJson.put("orderNo",RespOrderNo);
			}
		} catch (Exception e) {
			log.error("FuyouPayServiceImpl 377 :");
		}
		log.info("==================富友支付结束===============");
		return mapFromJson;
	}
	@Transactional(readOnly = false)
	@Override
	public boolean fuiouNotifyProcess(HttpServletRequest request) {
		log.info("===========富友支付异步回调处理开始===============");
		
		PluginConfig config = fyPaymentPlugin.getPluginConfig();
		Map<String,String> configAttr = config.getAttributeMap();
		String merKey = configAttr.get(FyPaymentPlugin.PAYMENT_KEY_ATTRIBUTE_NAME);
		String type = request.getParameter("TYPE");
		String version = request.getParameter("VERSION");
		String mchId = request.getParameter("MCHNTCD");
		String respCode = request.getParameter("RESPONSECODE");
		String mchOrderId = request.getParameter("MCHNTORDERID");
		String fyOrderId = request.getParameter("ORDERID");
		String amt = request.getParameter("AMT");
		String bankCard = request.getParameter("BANKCARD");
		String sign = request.getParameter("SIGN");
		log.info("富友支付异步回调返回参数RESPONSECODE= " + respCode + " ; MCHNTORDERID= " + mchOrderId + " ; ORDERID= " + fyOrderId + " ; AMT= " + amt +" ; BANKCARD = " 
				+ bankCard + " ; SIGN =" + sign);
		
		Boolean result = false;
		
		Payment payment = paymentService.getByPaymentNo(mchOrderId);
		if(Payment.Status.success.equals(payment.getStatus())) {
			log.info("订单号：" + mchOrderId + " 已经处理完毕!");
			return true ;
		}
		if("0000".equals(respCode)) {
			String signOrg = type + "|" + version + "|" + respCode + "|" + mchId + "|" + mchOrderId + "|" + fyOrderId + "|" + StringUtils.StrTOInt(payment.getPaymentAmount().toString()) + "|" + bankCard + "|" + merKey;
			String mySign = MD5Code.MD5Encode(signOrg, "UTF-8");
				if (sign.equals(mySign)) {
					log.info("富友支付异步回调验签成功");
					//payment状态改为success
					payment.setThirdPaymentNo(fyOrderId);
					payment.setStatus(Payment.Status.success);
					paymentService.save(payment);	
					//充值记录状态改为成功，加入paymentid
					NfsRchgRecord record = nfsRchgRecordService.get(payment.getOrgId());
					record.setPayment(payment);
					record.setStatus(NfsRchgRecord.Status.success);
					nfsRchgRecordService.save(record);
					//充值人账户增加余额
					Member member = memberService.get(payment.getPrincipalId());

					int code = actService.updateAct(TrxRuleConstant.MEMBER_RECHARGE, payment.getPaymentAmount(), member, record.getId());
					if(code == 0) {
						result= true;
					}
				}else {
					log.info("富友异步回调验签失败！ 返回金额 AMT=" + amt + " 本地记录：amount=" + payment.getPaymentAmount().toString());
					update4Failure(payment);
				}
		}else {
			log.info("富友支付异步通知支付单号： paymentNo: " + mchOrderId + "支付失败");
			update4Failure(payment);
		}
		log.info("支付单号：" + mchOrderId + "  =================富友异步处理完毕==================");
		return result;
	}
	
	private void update4Failure(Payment payment) {
		payment.setStatus(Payment.Status.failure);
		paymentService.save(payment);
		NfsRchgRecord record = nfsRchgRecordService.get(payment.getOrgId());
		record.setStatus(NfsRchgRecord.Status.failure);
		record.setPayment(payment);
		nfsRchgRecordService.save(record);
	}

	@Override
	public Map<String, Object> unBind(NfsBankProtocol protocol) {
		PluginConfig config = fyPaymentPlugin.getPluginConfig();
		Map<String,String> configAttr = config.getAttributeMap();
		String version = "1.0";
		String mchId = configAttr.get(FyPaymentPlugin.PAYMENT_MCHNTCD_ATTRIBUTE_NAME);
		String merKey = configAttr.get(FyPaymentPlugin.PAYMENT_KEY_ATTRIBUTE_NAME);
		String unBindUrl = configAttr.get(FyPaymentPlugin.PAYMENT_UNBIND_REQUEST_URL_ATTRIBUTE_NAME);
		String userId = String.valueOf(protocol.getMemberId());
		String proNO = protocol.getProtocolNo();
		
		Map<String, Object> mapFromJson = new HashMap<String, Object>();
		StringBuffer bf = new StringBuffer();
		try {
			bf.append("<?xml ");
			bf.append("version=\"1.0\" encoding=\"");
			bf.append("UTF-8");
			bf.append("\"?>");
			bf.append("<REQUEST>");

			bf.append("<VERSION>");
			bf.append(version);
			bf.append("</VERSION>");

			bf.append("<MCHNTCD>");
			bf.append(mchId);
			bf.append("</MCHNTCD>");

			bf.append("<USERID>");
			bf.append(userId + "");
			bf.append("</USERID>");
	
			bf.append("<PROTOCOLNO>");
			bf.append(proNO);
			bf.append("</PROTOCOLNO>");

			String signM = version + "|" + mchId + "|" + userId + "|" + proNO + "|" +merKey ;
			signM = MD5Utils.EncoderByMd5(signM);

			bf.append("<SIGN>");
			bf.append(signM);
			bf.append("</SIGN>");
			bf.append("</REQUEST>");
			Map<String, String> map = new HashMap<String, String>();
			String xml = bf.toString();
			xml = DESCoderFUIOU.desEncrypt(xml, DESCoderFUIOU.getKeyLength8(merKey));
			map.put("MCHNTCD", mchId);
			map.put("APIFMS", xml);
			String result = new HttpPoster(unBindUrl).postStr(map);
			String html = DESCoderFUIOU.desDecrypt(result, DESCoderFUIOU.getKeyLength8(merKey));
			log.info(html);
			String substring = html.substring(html.indexOf("<RESPONSECODE>") + 14, html.indexOf("</RESPONSECODE>"));
			String reson = html.substring(html.indexOf("<RESPONSEMSG>") + 13, html.indexOf("</RESPONSEMSG>"));
			if ("0000".equals(substring)) {
				mapFromJson.put("sucess","0");
				mapFromJson.put("reson", reson);
			}else{
				mapFromJson.put("sucess", "-1");
				mapFromJson.put("reson", reson);
			}
		} catch (Exception e) {
			log.error("FuyouPayService 512 : " + e.toString());
		}
		return mapFromJson;
	}
	
	@Transactional(readOnly = false)
	@Override
	public boolean fuiouNotifyProcessforApp(HttpServletRequest request) {
		log.info("===========富友支付异步回调处理开始===============");	
		PluginConfig config = fyPaymentPlugin.getPluginConfig();
		Map<String,String> configAttr = config.getAttributeMap();
		String merKey = configAttr.get(FyPaymentPlugin.PAYMENT_KEY_ATTRIBUTE_NAME);
		String mchId = request.getParameter("MCHNTCD");
		String respCode = request.getParameter("RESPONSECODE");
		String mchOrderId = request.getParameter("MCHNTORDERID");
		String fyOrderId = request.getParameter("ORDERID");
		String amt = request.getParameter("AMT");
		String bankCard = request.getParameter("BANKCARD");
		String sign = request.getParameter("SIGN");
		String type = request.getParameter("TYPE");
		log.info("富友支付异步回调返回参数RESPONSECODE= {}; MCHNTORDERID= {}; ORDERID= {}; AMT= {}; BANKCARD = {}; SIGN ={}",respCode,mchOrderId,fyOrderId,amt,bankCard,sign);
		
		Payment payment = paymentService.getByPaymentNo(mchOrderId);
		NfsRchgRecord rechargeRecord = nfsRchgRecordService.get(payment.getOrgId());
		if(payment.getStatus().equals(Payment.Status.success)) {
			log.info("订单号：" + mchOrderId + " 已经处理完毕!");
			return true ;
		}
		if(rechargeRecord.getStatus().equals(NfsRchgRecord.Status.success)) {
			log.info("订单号：" + mchOrderId + " 已经处理完毕!");
			return true ;
		}
		if(StringUtils.equals(respCode, "0000")) {
			String s = MD5Code.MD5Encode(type + "|2.0|" + respCode + "|" + mchId + "|" + mchOrderId + "|" + fyOrderId + "|" + StringUtils.StrTOInt(payment.getPaymentAmount().toString()) + "|" + bankCard + "|" + merKey,"UTF-8").toLowerCase();
			if (StringUtils.equals(sign, s)) {
				log.info("富友支付异步回调验签成功");
					//payment状态改为success
					payment.setThirdPaymentNo(fyOrderId);
					payment.setStatus(Payment.Status.success);
					int updateLines = paymentDao.update(payment);
					if(updateLines == 0) {
						log.error("富友异步回调充值单号：{}状态更新失败！",mchOrderId);
						return true;
					}
					rechargeRecord.setPayment(payment);
					rechargeRecord.setStatus(NfsRchgRecord.Status.success);
					nfsRchgRecordService.save(rechargeRecord);
					//充值人账户增加余额
					BigDecimal czAmount = payment.getPaymentAmount();
					Member member = memberService.get(payment.getPrincipalId());
					int code = actService.updateAct(TrxRuleConstant.MEMBER_RECHARGE, czAmount, member, rechargeRecord.getId());
					if(code == Constant.UPDATE_FAILED) {
						log.error("富友异步回调账户更新失败！充值商户订单号{},富友订单号{}充值金额{}",mchOrderId,fyOrderId,czAmount);
					}
					Long orgTrxId = null;
					MemberActTrx memberActTrx = new MemberActTrx();
					memberActTrx.setOrgId(rechargeRecord.getId());
					memberActTrx.setTrxCode(TrxRuleConstant.MEMBER_RECHARGE);
					List<MemberActTrx> list = memberActTrxService.findList(memberActTrx);
					if(!Collections3.isEmpty(list)) {
						orgTrxId = list.get(0).getId();
					}
					memberMessageService.sendMessage(MemberMessage.Type.successfulRecharge,orgTrxId);
					log.info("支付单号：{}=================富友异步处理完毕==================",mchOrderId);
				}else {
					log.error("富友异步回调验签失败！充值商户订单号{},富友订单号{}签名[{}]",mchOrderId,fyOrderId,sign);
					update4Failure(payment);
				}
		}else {
			log.info("富友支付异步通知支付单号： paymentNo:{}支付失败",mchOrderId);
			update4Failure(payment);
		}
		return true;
	}
	
	
	@Override
	public  Map<String, String> queryPayResultByMchOrderNo(String orderNo) {
		log.info("============富友充值查询============");
		Map<String, String> mapFromJson = new HashMap<String, String>();
		PluginConfig config = fyPaymentPlugin.getPluginConfig();
		Map<String,String> configAttr = config.getAttributeMap();
		String version = "1.0";
		String MCHNTCD = configAttr.get(FyPaymentPlugin.PAYMENT_MCHNTCD_ATTRIBUTE_NAME);
		String merKey = configAttr.get(FyPaymentPlugin.PAYMENT_KEY_ATTRIBUTE_NAME);
		String queryUrl = configAttr.get(FyPaymentPlugin.PAYMENT_BIBIYANCHECKRESULT_REQUEST_URL_ATTRIBUTE_NAME);//富友笔笔验订单查询接口地址
//		String payurl = "https://mpay.fuioupay.com:16128/checkInfo/checkResult.pay";//富友笔笔验订单查询接口地址
		StringBuffer bf = new StringBuffer();
		try {
			bf.append("<ORDER>");

			bf.append("<VERSION>");
			bf.append(version);
			bf.append("</VERSION>");
			//商户号
			bf.append("<MCHNTCD>");
			bf.append(MCHNTCD);
			bf.append("</MCHNTCD>");
			// 订单号
			bf.append("<MCHNTORDERID>");
			bf.append(orderNo);
			bf.append("</MCHNTORDERID>");
			bf.append("<REM1>");
			bf.append("</REM1>");
			bf.append("<REM2>");
			bf.append("</REM2>");
			bf.append("<REM3>");
			bf.append("</REM3>");
			String signM =version + "|" + MCHNTCD + "|" + orderNo + "|" + merKey ;
			signM = MD5Utils.EncoderByMd5(signM);
			bf.append("<SIGN>");
			bf.append(signM);
			bf.append("</SIGN>");
			bf.append("</ORDER>");
			Map<String, String> map = new HashMap<String, String>();
			String xml = bf.toString();
			map.put("FM", xml);
			String respStr = new HttpPoster(queryUrl).postStr(map);
			log.info("富友订单查询接口返回信息：" + respStr);
			String responseCode = respStr.substring(respStr.indexOf("<RESPONSECODE>") + 14, respStr.indexOf("</RESPONSECODE>"));
			String responseMsg = respStr.substring(respStr.indexOf("<RESPONSEMSG>") + 13, respStr.indexOf("</RESPONSEMSG>"));
			String sign = respStr.substring(respStr.indexOf("<SIGN>")+6,respStr.indexOf("</SIGN>"));
			if ("0000".equals(responseCode)) {
				String mySign = "1.0|" + responseCode +"|"+ responseMsg + "|" + orderNo +"|" + merKey;
				mySign = MD5Utils.EncoderByMd5(mySign);
				if(StringUtils.equals(mySign, sign)) {
					log.info("订单号[{}]富友查询接口返回数据签名通过！",orderNo);
					mapFromJson.put("success","0");
				}else {
					log.error("订单号[{}]富友查询接口返回数据签名验证错误！返回签名：{}，我们的签名：{}",orderNo,sign,mySign);
				}
			}else{
				log.error("订单号[{}]富友查询接口返回码为：{}",orderNo,responseCode);
				mapFromJson.put("success", "-1");
			}
		} catch (Exception e) {
			log.error(Exceptions.getStackTraceAsString(e));
		}
		return mapFromJson;
	}
}
