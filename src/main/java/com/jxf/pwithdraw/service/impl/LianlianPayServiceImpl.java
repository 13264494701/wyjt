/**    
 * @文件名称: LianlianPayServiceImpl.java 
 * @类路径: com.jxf.pay.service.impl 
 * @描述: TODO 
 * @作者：李新 
 * @时间：2016年8月10日 上午10:18:59 
 * @版本：V1.0    
 */ 
package com.jxf.pwithdraw.service.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jxf.mem.entity.Member;
import com.jxf.mem.entity.MemberActTrx;
import com.jxf.mem.entity.MemberMessage;
import com.jxf.mem.service.MemberActTrxService;
import com.jxf.mem.service.MemberMessageService;
import com.jxf.mem.service.MemberService;
import com.jxf.nfs.constant.TrxRuleConstant;
import com.jxf.nfs.entity.NfsRchgRecord;
import com.jxf.nfs.entity.NfsWdrlRecord;
import com.jxf.nfs.entity.NfsWdrlRecord.Status;
import com.jxf.nfs.entity.NfsWdrlRecord.Type;
import com.jxf.nfs.service.NfsActService;
import com.jxf.nfs.service.NfsRchgRecordService;
import com.jxf.nfs.service.NfsWdrlRecordService;
import com.jxf.pay.entity.LianlianMerchantDataBean;
import com.jxf.payment.dao.PaymentDao;
import com.jxf.payment.entity.Payment;
import com.jxf.payment.service.PaymentService;
import com.jxf.pwithdraw.entity.BusinessNoticeBean;
import com.jxf.pwithdraw.entity.ConfirmPaymentRequestBean;
import com.jxf.pwithdraw.entity.ConfirmPaymentResponseBean;
import com.jxf.pwithdraw.entity.PaymentRequestBean;
import com.jxf.pwithdraw.entity.PaymentResponseBean;
import com.jxf.pwithdraw.entity.PaymentStatusEnum;
import com.jxf.pwithdraw.entity.QueryPaymentRequestBean;
import com.jxf.pwithdraw.entity.QueryPaymentResponseBean;
import com.jxf.pwithdraw.entity.RetCodeEnum;
import com.jxf.pwithdraw.service.LianlianPayService;
import com.jxf.pwithdraw.utils.SignUtil;
import com.jxf.pwithdraw.utils.TraderRSAUtil;
import com.jxf.svc.config.Constant;
import com.jxf.svc.config.Global;
import com.jxf.svc.plugin.PluginConfig;
import com.jxf.svc.plugin.lianlianPayment.LianlianPaymentPlugin;
import com.jxf.svc.utils.Collections3;
import com.jxf.svc.utils.DateUtils;
import com.jxf.svc.utils.Exceptions;
import com.jxf.svc.utils.HttpUtils;
import com.jxf.svc.utils.StringUtils;
import com.lianlianpay.security.utils.LianLianPaySecurity;

/**
 * 支付相关API
 *
 * @author lixin
 * @since 1.2
 */
@Service("lianlianPayService")
@Transactional(readOnly=true)
public class LianlianPayServiceImpl implements LianlianPayService {

	private static final Logger log = LoggerFactory.getLogger(LianlianPayServiceImpl.class);
	
	@Autowired
	private LianlianPaymentPlugin lianlianPaymentPlugin;
	@Autowired
	private NfsWdrlRecordService wdrlRecordService;
	@Autowired
	private MemberService memberService;
	@Autowired
	private MemberMessageService memberMessageService;
	@Autowired
	private MemberActTrxService memberActTrxService;
	@Autowired
	private PaymentService paymentService;
	@Autowired
	private NfsRchgRecordService nfsRchgRecordService;
	@Autowired
	private NfsActService actService;
	@Autowired
	private PaymentDao paymentDao;

	@Override
	public String sendData(PaymentRequestBean paymentRequestBean) {
		PluginConfig config = lianlianPaymentPlugin.getPluginConfig();
		Map<String,String> configAttr = config.getAttributeMap();
		String oid_partner = configAttr.get(LianlianPaymentPlugin.PAYMENT_OID_PARTNER_ATTRIBUTE_NAME);
		String version = configAttr.get(LianlianPaymentPlugin.PAYMENT_API_VERSION_ATTRIBUTE_NAME);
		String signType = configAttr.get(LianlianPaymentPlugin.PAYMENT_SIGN_TYPE_ATTRIBUTE_NAME);
		String key = configAttr.get(LianlianPaymentPlugin.PAYMENT_BUSINESS_PRIVATE_KEY_ATTRIBUTE_NAME);
		String llPubKey = configAttr.get(LianlianPaymentPlugin.PAYMENT_LLPUBLIC_KEY_ATTRIBUTE_NAME);
		String requestUrl = configAttr.get(LianlianPaymentPlugin.PAYMENT_PAYMENT_ATTRIBUTE_NAME);
		String notifyUrl = Global.getConfig("domain") + "/callback/lianlian/llnotifyForApp";
		try {
			paymentRequestBean.setInfo_order("用户提现");
			paymentRequestBean.setFlag_card("0");
			// 填写商户自己的接收付款结果回调异步通知 长度64位
			paymentRequestBean.setNotify_url(notifyUrl);
			paymentRequestBean.setOid_partner(oid_partner);
			// paymentRequestBean.setPlatform("test.com");
			paymentRequestBean.setApi_version(version);
			paymentRequestBean.setSign_type(signType);
			// 用商户自己的私钥加签
			paymentRequestBean.setSign(SignUtil.genRSASign(JSON.parseObject(JSON.toJSONString(paymentRequestBean)),key));
			String jsonStr = JSON.toJSONString(paymentRequestBean);
//			logger.info(paymentRequestBean.getNo_order()+" 实时付款请求报文：" + jsonStr);
			log.info("lianlian-"+paymentRequestBean.getNo_order()+"-实时付款请求报文：" + jsonStr);
			// 用银通公钥对请求参数json字符串加密
			// 报Illegal key
			// size异常时，可参考这个网页解决问题http://www.wxdl.cn/java/security-invalidkey-exception.html
			String encryptStr = LianLianPaySecurity.encrypt(jsonStr, llPubKey);
			if (StringUtils.isEmpty(encryptStr)) {
				log.error("lianlian----加密异常");
				// 加密异常
                return null;
            }

			JSONObject json = new JSONObject();
			json.put("oid_partner", oid_partner);
			json.put("pay_load", encryptStr);
			String response = HttpUtils.doPostForWithdraw(requestUrl,json.toJSONString());
			log.info("lianlian-"+paymentRequestBean.getNo_order()+"-付款接口返回响应报文：" + response);
			return response;
		} catch (Exception e) {
			e.printStackTrace();
			log.error(Exceptions.getStackTraceAsString(e));
		}
		return null;
	}

	@Override
	public String confirm(ConfirmPaymentRequestBean paymentRequestBean) {
		PluginConfig config = lianlianPaymentPlugin.getPluginConfig();
		Map<String,String> configAttr = config.getAttributeMap();
		String oid_partner = configAttr.get(LianlianPaymentPlugin.PAYMENT_OID_PARTNER_ATTRIBUTE_NAME);
		String version = configAttr.get(LianlianPaymentPlugin.PAYMENT_API_VERSION_ATTRIBUTE_NAME);
		String signType = configAttr.get(LianlianPaymentPlugin.PAYMENT_SIGN_TYPE_ATTRIBUTE_NAME);
		String key = configAttr.get(LianlianPaymentPlugin.PAYMENT_BUSINESS_PRIVATE_KEY_ATTRIBUTE_NAME);
		String llPubKey = configAttr.get(LianlianPaymentPlugin.PAYMENT_LLPUBLIC_KEY_ATTRIBUTE_NAME);
		String confirmUrl = configAttr.get(LianlianPaymentPlugin.PAYMENT_CONFIRMPAYMENT_ATTRIBUTE_NAME);
		String notifyUrl = Global.getConfig("domain") + "/callback/lianlian/llnotifyForApp";
		try {
			// 填写商户自己的接收付款结果回调异步通知 长度64位
			paymentRequestBean.setNotify_url(notifyUrl);
			paymentRequestBean.setOid_partner(oid_partner);
			// paymentRequestBean.setPlatform("test.com");
			paymentRequestBean.setApi_version(version);
			paymentRequestBean.setSign_type(signType);
			// 用商户自己的私钥加签
			paymentRequestBean.setSign(SignUtil.genRSASign(JSON.parseObject(JSON.toJSONString(paymentRequestBean)),key));
			String jsonStr = JSON.toJSONString(paymentRequestBean);
			log.info("lianlian-"+paymentRequestBean.getNo_order()+"-确认接口请求报文 = " + jsonStr);
			// 用银通公钥对请求参数json字符串加密
			// 报Illegal key
			// size异常时，可参考这个网页解决问题http://www.wxdl.cn/java/security-invalidkey-exception.html
			String encryptStr = LianLianPaySecurity.encrypt(jsonStr, llPubKey);

			if (StringUtils.isEmpty(encryptStr)) {
                return null;
            }
			JSONObject json = new JSONObject();
			json.put("oid_partner", oid_partner);
			json.put("pay_load", encryptStr);
			String response = HttpUtils.doPostForWithdraw(confirmUrl,json.toJSONString());
			
			log.info("lianlian-"+paymentRequestBean.getNo_order()+"-确认付款接口返回响应报文：" + response);
			return response;
		} catch (Exception e) {
			log.error(Exceptions.getStackTraceAsString(e));
		}
		return null;
	}

	@Override
	public String queryOrder(QueryPaymentRequestBean queryRequestBean) {
		PluginConfig config = lianlianPaymentPlugin.getPluginConfig();
		Map<String,String> configAttr = config.getAttributeMap();
		String oid_partner = configAttr.get(LianlianPaymentPlugin.PAYMENT_OID_PARTNER_ATTRIBUTE_NAME);
		String version = configAttr.get(LianlianPaymentPlugin.PAYMENT_API_VERSION_ATTRIBUTE_NAME);
		String signType = configAttr.get(LianlianPaymentPlugin.PAYMENT_SIGN_TYPE_ATTRIBUTE_NAME);
		String key = configAttr.get(LianlianPaymentPlugin.PAYMENT_BUSINESS_PRIVATE_KEY_ATTRIBUTE_NAME);
		String queryPaymentUrl = configAttr.get(LianlianPaymentPlugin.PAYMENT_QUERYPAYMENT_ATTRIBUTE_NAME);
		try {
			queryRequestBean.setOid_partner(oid_partner);
			queryRequestBean.setApi_version(version);
			queryRequestBean.setSign_type(signType);
			queryRequestBean.setSign(SignUtil.genRSASign(JSON.parseObject(JSON.toJSONString(queryRequestBean)),key));

			//log.info(SignUtil.genRSASign(JSON.parseObject(JSON.toJSONString(queryRequestBean))));

			log.info("lianlian-"+queryRequestBean.getNo_order()+"-实时付款查询接口请求报文：" + JSON.toJSONString(queryRequestBean));
			
			String json = JSON.toJSONString(queryRequestBean);
			String queryResult = HttpUtils.doPostForWithdraw(queryPaymentUrl,json);
			log.info("lianlian-"+queryRequestBean.getNo_order()+"-实时付款查询接口响应报文：" + queryResult);
			return queryResult;
		} catch (Exception e) {
			log.error(Exceptions.getStackTraceAsString(e));
		}
		return null;
	}

	@Override
	@Transactional(readOnly=false)
	public boolean notifyProcess(HttpServletRequest request) {
		log.info("============连连支付异步返回===========");
  		// 获取连连POST过来反馈信息  
          String inputLine;  
          String notityJson = "";  
          try {  
              while ((inputLine = request.getReader().readLine()) != null) {  
            	  notityJson += inputLine;  
              }             
              request.getReader().close();       
          } catch (Exception e) {  
          	log.error("========json获取失败=======" + e);  
          	return false;
          }  
          log.debug("收到连连异步回调：{}",notityJson);  
          if(StringUtils.isBlank(notityJson)){  
          	log.error("=======收到连连异步回调json为空======");  
          	return false;
          }  
  		  PluginConfig config = lianlianPaymentPlugin.getPluginConfig();
  		  Map<String,String> configAttr = config.getAttributeMap();
  		  String key = configAttr.get(LianlianPaymentPlugin.PAYMENT_LLPUBLIC_KEY_ATTRIBUTE_NAME);
          BusinessNoticeBean businessNoticeBean = JSONObject.parseObject(notityJson, BusinessNoticeBean.class);
          String wdrlId = businessNoticeBean.getNo_order();//商户付款流水号
//        String payTime = businessNoticeBean.getDt_order();//商户下单时间
          String payAmount = businessNoticeBean.getMoney_order();//付款金额
          String thirdOrderNo = businessNoticeBean.getOid_paybill();//第三方订单号
          String sign = businessNoticeBean.getSign();//签名
          String resultPay = businessNoticeBean.getResult_pay();//付款状态
          String info_order = businessNoticeBean.getInfo_order();//失败原因、订单信息
          boolean checkResult = TraderRSAUtil.checksign(key,SignUtil.genSignData(JSONObject.parseObject(notityJson)),businessNoticeBean.getSign());
          if(!checkResult) {
        	  log.error("连连提现订单： " + wdrlId + " 签名验证错误！连连发送签名： " + sign);
        	  return false;
          }
          NfsWdrlRecord wdrlRecord = wdrlRecordService.get(Long.valueOf(wdrlId));
          if(wdrlRecord.getStatus().equals(NfsWdrlRecord.Status.submited)) {
        	  try {
				if (StringUtils.equals(resultPay, "SUCCESS")) {
					wdrlRecord.setThirdOrderNo(thirdOrderNo);
					wdrlRecord.setPayTime(new Date());
					wdrlRecord.setPayAmount(StringUtils.toDecimal(payAmount));
					wdrlRecord.setStatus(NfsWdrlRecord.Status.madeMoney);
					wdrlRecord.setRmk(resultPay);
					wdrlRecordService.save(wdrlRecord);

					Long orgTrxId = null;
					MemberActTrx memberActTrx = new MemberActTrx();
					memberActTrx.setTrxCode(TrxRuleConstant.MEMBER_WITHDRAWALS);
					memberActTrx.setOrgId(wdrlRecord.getId());
					List<MemberActTrx> list = memberActTrxService.findList(memberActTrx);
					if (!Collections3.isEmpty(list)) {
						orgTrxId = list.get(0).getId();
					} else {
						memberActTrx.setTrxCode(TrxRuleConstant.MEMBER_WITHDRAWALS_AVL);
						List<MemberActTrx> list1 = memberActTrxService.findList(memberActTrx);
						if (!Collections3.isEmpty(list1)) {
							orgTrxId = list1.get(0).getId();
						}
					}
					// 发消息
					memberMessageService.sendMessage(MemberMessage.Type.cashWithdrawalAccount,orgTrxId);
					log.debug("=======提现成功========");
				}else if (StringUtils.equals(resultPay, "FAILURE")||StringUtils.equals(resultPay, "CLOSED")||StringUtils.equals(resultPay, "CANCEL")) {
	        		  wdrlRecord.setPayTime(new Date());
	        		  wdrlRecord.setThirdOrderNo(thirdOrderNo);
	    			  int code = wdrlRecordService.failure(wdrlRecord,info_order);
	    			  if(code == Constant.UPDATE_SUCCESS) {
	    				  log.info("连连提现订单ID{}回调状态码为{}，提现失败退款成功",wdrlRecord.getId(),resultPay);
		              }else {
		            	  log.error("连连提现订单ID{}回调状态码为{}，提现失败,退款失败",wdrlRecord.getId(),resultPay);
		              }
	        	  }
        	  } catch (Exception e) {
        		  log.error("提现订单 {}  回调处理异常{}", wdrlId , Exceptions.getStackTraceAsString(e));
        	  }
          }else {
    		  log.warn("连连提现订单{}状态为{}", wdrlId,wdrlRecord.getStatus());
    	  }
          return true;
	}

	@Override
	@Transactional(readOnly=false)
	public void sendLianLianPaymentRequestForAPP(NfsWdrlRecord wdrlRecord) {
		wdrlRecord.setType(Type.lianlian);
		//用户合法性检查
		Member member = memberService.get(wdrlRecord.getMember());
		if(!member.getIsEnabled()||member.getIsLocked()) {
			wdrlRecord.setStatus(NfsWdrlRecord.Status.retrial);
			wdrlRecordService.save(wdrlRecord);
			log.error("用户被锁定或者冻结，不支持提现{}",wdrlRecord.getId());
			return;
		}
		if(memberService.lockExists(member))
		{	wdrlRecord.setStatus(NfsWdrlRecord.Status.retrial);
		    wdrlRecordService.save(wdrlRecord);
			log.error("用户账户资金来源可疑，请重新审核{}",wdrlRecord.getId());
			return;
		}
		//插件中获取连连提供的公钥
		PluginConfig config = lianlianPaymentPlugin.getPluginConfig();
		Map<String,String> configAttr = config.getAttributeMap();
		String key = configAttr.get(LianlianPaymentPlugin.PAYMENT_LLPUBLIC_KEY_ATTRIBUTE_NAME);	

        try {
        	log.info("lianlian-{}-发送请求",wdrlRecord.getId());

            PaymentRequestBean paymentRequestBean = new PaymentRequestBean();
            paymentRequestBean.setNo_order(wdrlRecord.getId()+"");
            paymentRequestBean.setDt_order(DateUtils.getAllNumTime());
            paymentRequestBean.setMoney_order(StringUtils.decimalToStr(wdrlRecord.getAmount().subtract(new BigDecimal(wdrlRecord.getFee())), 2));
            paymentRequestBean.setCard_no(wdrlRecord.getCardNo());

            paymentRequestBean.setAcct_name(member.getName());
            paymentRequestBean.setBank_name("");//对私可为空
            String response = sendData(paymentRequestBean);
            wdrlRecord.setSubmitTime(new Date());
            wdrlRecord.setStatus(Status.submited);
            wdrlRecordService.save(wdrlRecord);
            
            if (StringUtils.isEmpty(response)) {
                // 出现异常时调用订单查询，明确订单状态，不能私自设置订单为失败状态，以免造成这笔订单在连连付款成功了，而商户设置为失败
            	log.error("lianlian-"+wdrlRecord.getId()+"-实时付款交易接口返回结果为空,将调用实时查询接口查询");
                queryLianLianPayMent(wdrlRecord.getId()+"");
            } else {
                PaymentResponseBean paymentResponseBean = JSONObject.parseObject(response, PaymentResponseBean.class);
                log.info("lianlian-" + wdrlRecord.getId() + "-Ret_code：" + paymentResponseBean.getRet_code() +
                        " Ret_msg:" + paymentResponseBean.getRet_msg() + " time = " + DateUtils.getDateTime());
                // 对返回0000时验证签名
                if (paymentResponseBean.getRet_code().equals("0000")) {
                    // 先对结果验签
                    boolean signCheck = TraderRSAUtil.checksign(key, SignUtil.genSignData(JSONObject.parseObject(response)), paymentResponseBean.getSign());
                    if (!signCheck) {
                        // 传送数据被篡改，可抛出异常，再人为介入检查原因
                    	log.error("lianlian-" + wdrlRecord.getId() + "-付款交易接口返回结果验签异常,可能数据被篡改");
                        return;
                    }
                    // 已生成连连支付单，付款处理中（交易成功，不是指付款成功，是指跟连连流程正常），商户可以在这里处理自已的业务逻辑（或者不处理，在异步回调里处理逻辑）,最终的付款状态由异步通知回调告知
                
                } else if (paymentResponseBean.getRet_code().equals("4002")
                        || paymentResponseBean.getRet_code().equals("4003")
                        || paymentResponseBean.getRet_code().equals("4004")) {
                    // 当调用付款接口返回4002，4003，4004,会同时返回验证码，用于确认付款接口
                    // 对于疑似重复订单，需先人工审核这笔订单是否正常的付款请求，而不是系统产生的重复订单，确认后再调用确认付款接口
                    // 对于疑似重复订单，也可不做处理，
                	log.error("连连提现-疑似重复订单 ID :{} 确认码： {} " , wdrlRecord.getId() ,paymentResponseBean.getConfirm_code());
                	NfsWdrlRecord wdrlRecord2 = wdrlRecordService.get(wdrlRecord.getId());
                	wdrlRecord2.setStatus(NfsWdrlRecord.Status.mayRepeatOrder);
                	wdrlRecord2.setRmk(paymentResponseBean.getConfirm_code());
                    wdrlRecordService.update(wdrlRecord2);
                } else if (RetCodeEnum.isNeedQuery(paymentResponseBean.getRet_code())) {
                    // 出现1002，2005，4006，4007，4009，9999这6个返回码时（或者对除了0000之后的code都查询一遍查询接口）调用订单查询，明确订单状态，不能私自设置订单为失败状态，以免造成这笔订单在连连付款成功了，而商户设置为失败
                    // 第一次测试对接时，返回{"ret_code":"4007","ret_msg":"敏感信息解密异常"},可能原因报文加密用的公钥改动了,demo中的公钥是连连公钥，商户生成的公钥用于上传连连商户站用于连连验签，生成的私钥用于加签
                	log.error("连连提现订单{}连连同步返回code为{}",wdrlRecord.getId(),paymentResponseBean.getRet_code());
                    queryLianLianPayMent(wdrlRecord.getId()+"");
                } else {
                    // 返回其他code时，可将订单置为失败
                	log.error("lianlian-" + wdrlRecord.getId() + "-失败操作:Ret_code：" + paymentResponseBean.getRet_code() +
                            " Ret_msg:" + paymentResponseBean.getRet_msg() );
                	NfsWdrlRecord wdrlRecord2 = wdrlRecordService.get(wdrlRecord.getId());
                	int respCode = wdrlRecordService.failure(wdrlRecord2,paymentResponseBean.getRet_msg() );
                	if(respCode == Constant.UPDATE_FAILED) {
                		log.error("连连提现订单{}连连同步返回code为{}，用户提现失败，退款成功",wdrlRecord.getId(),paymentResponseBean.getRet_code());
                	}else {
                		log.error("连连提现订单{}连连同步返回code为{}，用户提现失败，退款失败！",wdrlRecord.getId(),paymentResponseBean.getRet_code());
                	}
                }
            }
        } catch (Exception e) {
            log.error(Exceptions.getStackTraceAsString(e));
        }
    }
	@Override
	@Transactional(readOnly=false)
	public void queryLianLianPayMent(String orderId) {
		//插件中获取连连提供的公钥
		PluginConfig config = lianlianPaymentPlugin.getPluginConfig();
		Map<String,String> configAttr = config.getAttributeMap();
		String key = configAttr.get(LianlianPaymentPlugin.PAYMENT_LLPUBLIC_KEY_ATTRIBUTE_NAME);

		log.info("lianlian-{} 实时付款查询接口",orderId);

        NfsWdrlRecord wdrlRecord = wdrlRecordService.get(Long.valueOf(orderId));
        if (NfsWdrlRecord.Status.madeMoney.equals(wdrlRecord.getStatus()) || NfsWdrlRecord.Status.failure.equals(wdrlRecord.getStatus())) {
        	log.warn("lianlian-{}实时付款查询接口 订单已处理 ，状态为：{} ",orderId, wdrlRecord.getStatus());
            return;
        }
        try {
            QueryPaymentRequestBean requestBean = new QueryPaymentRequestBean();
            requestBean.setNo_order(orderId);
            String queryResult = queryOrder(requestBean);

            if (StringUtils.isEmpty(queryResult)) {
                // 可抛异常，查看原因
            	log.error("lianlian-{} 实时付款查询接口 return_null",orderId);
                return;
            }

            QueryPaymentResponseBean queryPaymentResponseBean = JSONObject.parseObject(queryResult, QueryPaymentResponseBean.class);

            // 先对结果验签
            boolean signCheck = TraderRSAUtil.checksign(key,SignUtil.genSignData(JSONObject.parseObject(queryResult)), queryPaymentResponseBean.getSign());
            if (!signCheck) {
                // 传送数据被篡改，可抛出异常，再人为介入检查原因
            	log.error("连连提现订单ID: {} 实时付款查询接口返回结果验签异常,可能数据被篡改",orderId);
                return;
            }

            log.info("连连提现订单ID:" + orderId +"实时付款查询接口- Info_order：" + queryPaymentResponseBean.getInfo_order() + " Ret_code：" + queryPaymentResponseBean.getRet_code() +
                    " Ret_msg:" + queryPaymentResponseBean.getRet_msg() + " Result_pay:" +queryPaymentResponseBean.getResult_pay());

            String info_order = queryPaymentResponseBean.getInfo_order();
            if (StringUtils.contains(info_order, "_")) {
                info_order = info_order.substring(info_order.indexOf("_") + 1);
            }

            if (queryPaymentResponseBean.getRet_code().equals("0000")) {
                PaymentStatusEnum paymentStatusEnum = PaymentStatusEnum.getPaymentStatusEnumByValue(queryPaymentResponseBean.getResult_pay());
                // TODO商户根据订单状态处理自已的业务逻辑
                int respCode = Constant.UPDATE_FAILED;
                switch (paymentStatusEnum) {
                    case PAYMENT_APPLY:
                        // 付款申请，这种情况一般不会发生，如出现，请直接找连连技术处理
                        break;
                    case PAYMENT_CHECK:
                        // 复核状态 TODO
                        // 返回4002，4003，4004时，订单会处于复核状态，这时还未创建连连支付单，没提交到银行处理，如需对该订单继续处理，需商户先人工审核这笔订单是否是正常的付款请求，没问题后再调用确认付款接口
                        // 如果对于复核状态的订单不做处理，可当做失败订单
                        break;
                    case PAYMENT_SUCCESS:
                        // 成功 TODO
                        wdrlRecord.setStatus(NfsWdrlRecord.Status.madeMoney);
                        if(wdrlRecord.getSubmitTime() == null) {
                        	wdrlRecord.setSubmitTime(DateUtils.parse(queryPaymentResponseBean.getDt_order()));
                        }
                        wdrlRecord.setThirdOrderNo(queryPaymentResponseBean.getOid_paybill());
                        wdrlRecord.setPayTime(DateUtils.parse(queryPaymentResponseBean.getDt_order()));
                        wdrlRecord.setRmk((wdrlRecord.getRmk()==null? "": (wdrlRecord.getRmk()+"#")) + "调取查询接口确认打款成功");
                        wdrlRecord.setPayAmount(StringUtils.toDecimal(queryPaymentResponseBean.getMoney_order()));
                        wdrlRecordService.update(wdrlRecord);
                        
                        Long orgTrxId = null;
    					MemberActTrx memberActTrx = new MemberActTrx();
    					memberActTrx.setTrxCode(TrxRuleConstant.MEMBER_WITHDRAWALS);
    					memberActTrx.setOrgId(wdrlRecord.getId());
    					List<MemberActTrx> list = memberActTrxService.findList(memberActTrx);
    					if (!Collections3.isEmpty(list)) {
    						orgTrxId = list.get(0).getId();
    					} else {
    						memberActTrx.setTrxCode(TrxRuleConstant.MEMBER_WITHDRAWALS_AVL);
    						List<MemberActTrx> list1 = memberActTrxService.findList(memberActTrx);
    						if (!Collections3.isEmpty(list1)) {
    							orgTrxId = list1.get(0).getId();
    						}
    					}
    					memberMessageService.sendMessage(MemberMessage.Type.cashWithdrawalAccount,orgTrxId);
                        break;
                    case PAYMENT_FAILURE:
                        // 失败 TODO 用户退款。。
                    	respCode = wdrlRecordService.failure(wdrlRecord,info_order);
                    	if(respCode == Constant.UPDATE_SUCCESS) {
                    		log.info("连连提现订单{}查询接口显示汇款失败，用户退款成功！",wdrlRecord.getId());
                    	}else {
                    		log.info("连连提现订单{}查询接口显示汇款失败，用户退款失败！",wdrlRecord.getId());
                    	}
                        break;
                    case PAYMENT_DEALING:
                        // 处理中 TODO
                        //drawingOrder.setState(DrawingOrderState.PASS);
                        //drawingOrderDao.save(drawingOrder);
                        break;
                    case PAYMENT_RETURN:
                        // 退款 TODO
                        // 可当做失败（退款情况
                        // 极小概率下会发生，个别银行处理机制是先扣款后打款给用户时再检验卡号信息是否正常，异常时会退款到连连商户账上）
                    	respCode = wdrlRecordService.failure(wdrlRecord,info_order);
                    	if(respCode == Constant.UPDATE_SUCCESS) {
                    		log.info("连连提现订单{}查询接口显示汇款失败，用户退款成功！",wdrlRecord.getId());
                    	}else {
                    		log.info("连连提现订单{}查询接口显示汇款失败，用户退款失败！",wdrlRecord.getId());
                    	}
                        break;
                    case PAYMENT_CLOSED:
                        // 关闭 TODO 可当做失败 ，对于复核状态的订单不做处理会将订单关闭
                    	respCode = wdrlRecordService.failure(wdrlRecord,info_order);
                    	if(respCode == Constant.UPDATE_SUCCESS) {
                    		log.info("连连提现订单{}查询接口显示汇款失败，用户退款成功！",wdrlRecord.getId());
                    	}else {
                    		log.info("连连提现订单{}查询接口显示汇款失败，用户退款失败！",wdrlRecord.getId());
                    	}
                        break;
                    default:
                        break;
                }
            } else if (queryPaymentResponseBean.getRet_code().equals("8901")) {
                // 订单不存在，这种情况可以用原单号付款，最好不要换单号，如换单号，在连连商户站确认下改订单是否存在，避免系统并发时返回8901，实际有一笔订单
                //------sendPaymentRequest(drawingOrder);
            } else {
                // 查询异常（极端情况下才发生,对于这种情况，可人工介入查询，在连连商户站查询或者联系连连客服，查询订单状态）
            	log.error("连连提现订单ID: {} 实时付款查询接口:查询异常 Ret_code：{},Ret_msg:{}" ,orderId, queryPaymentResponseBean.getRet_code(), queryPaymentResponseBean.getRet_msg());
            }
        } catch (Exception e) {
        	log.error(Exceptions.getStackTraceAsString(e));
        }
    }
	
	@Override
	@Transactional(readOnly=false)
	public void confirmLianLianPayment(String orderId, String confirm_code) {
		PluginConfig config = lianlianPaymentPlugin.getPluginConfig();
		Map<String,String> configAttr = config.getAttributeMap();
		String key = configAttr.get(LianlianPaymentPlugin.PAYMENT_LLPUBLIC_KEY_ATTRIBUTE_NAME);
		
        log.info("连连提现订单ID:{}复核疑似订单确认码: {}" , orderId, confirm_code);
        ConfirmPaymentRequestBean requestBean = new ConfirmPaymentRequestBean();
        requestBean.setNo_order(orderId);
        // 当调用付款接口返回4002，4003，4004时，会返回验证码信息
        requestBean.setConfirm_code(confirm_code);
        NfsWdrlRecord wdrlRecord = wdrlRecordService.get(Long.valueOf(orderId));
        wdrlRecord.setStatus(Status.submited);
        wdrlRecordService.save(wdrlRecord);
        String response = confirm(requestBean);
        if (StringUtils.isEmpty(response)) {
            // 出现异常时调用订单查询，明确订单状态，不能私自设置订单为失败状态，以免造成这笔订单在连连付款成功了，而商户设置为失败
            //            queryPaymentAndDealBusiness(paymentRequestBean.getNo_order());
            log.info("连连提现订单ID:{} 确认付款接口返回结果为空,将调用实时查询接口查询",orderId);
            queryLianLianPayMent(orderId);
        } else {
            ConfirmPaymentResponseBean confirmaymentResponseBean = JSONObject.parseObject(response, ConfirmPaymentResponseBean.class);
            log.warn("连连提现订单ID:{}确认疑似订单操作: 状态编码：{}状态信息:{}",orderId,confirmaymentResponseBean.getRet_code(),confirmaymentResponseBean.getRet_msg());
            // 对返回0000时验证签名
            if (confirmaymentResponseBean.getRet_code().equals(RetCodeEnum.SUCC.code)) {
                // 先对结果验签
                boolean signCheck = TraderRSAUtil.checksign(key,SignUtil.genSignData(JSONObject.parseObject(response)), confirmaymentResponseBean.getSign());
                if (!signCheck) {
                    log.error("连连提现订单ID: " + orderId + " 确认付款接口返回结果验签异常,可能数据被篡改 ");
                    return;
                }
                log.info("连连提现订单ID: " + orderId + "确认疑似订单结果:订单处于付款处理中");
            }else {
            	// 返回其他code时，调取查询接口通过查询接口确认订单状态以及后续操作
            	log.error("连连提现订单ID: {}确认疑似操作错误，错误编码：{}，错误信息:{}，现在调起查询接口确认订单状态",orderId , confirmaymentResponseBean.getRet_code(), confirmaymentResponseBean.getRet_msg());
            	queryLianLianPayMent(orderId);
            }
//            else if (RetCodeEnum.isNeedQuery(confirmaymentResponseBean.getRet_code())) {
//                // 出现1002，2005，4006，4007，4009，9999这6个返回码时（或者对除了0000之后的code都查询一遍查询接口）调用订单查询，明确订单状态，不能私自设置订单为失败状态，以免造成这笔订单在连连付款成功了，而商户设置为失败
//                // 第一次测试对接时，返回{"ret_code":"4007","ret_msg":"敏感信息解密异常"},可能原因报文加密用的公钥改动了,demo中的公钥是连连公钥，商户生成的公钥用于上传连连商户站用于连连验签，生成的私钥用于加签
//                queryLianLianPayMent(orderId);
//            } 
        }
    }

	@Override
	@Transactional(readOnly=false)
	public boolean rechargeNotify(HttpServletRequest request) {
		log.info("===========连连充值支付异步回调处理开始===============");
		PluginConfig config = lianlianPaymentPlugin.getPluginConfig();
		Map<String,String> configAttr = config.getAttributeMap();
		String merKey = configAttr.get(LianlianPaymentPlugin.PAYMENT_LLPUBLIC_KEY_ATTRIBUTE_NAME);
		JSONObject notifyJson = new JSONObject();
		try {
			InputStream inputStream = request.getInputStream();
			String json = convertStreamToString(inputStream);
			log.info("连连回调数据：{}",json);
			notifyJson = JSONObject.parseObject(json);
			
		} catch (IOException e) {
			log.error("连连支付回调解析异常：{}",Exceptions.getStackTraceAsString(e));
		}
		String oid_partner = notifyJson.getString("oid_partner");
		String sign = notifyJson.getString("sign");
		String dt_order = notifyJson.getString("dt_order");
		//商户订单号
		String no_order = notifyJson.getString("no_order");
		//连连收款单号
		String oid_paybill = notifyJson.getString("oid_paybill");
		//
		String money_order = notifyJson.getString("money_order");
		String result_pay = notifyJson.getString("result_pay");
		String sign_type = notifyJson.getString("sign_type");
		String pay_type = notifyJson.getString("pay_type");
		String bank_code = notifyJson.getString("bank_code");
		String settle_date = notifyJson.getString("settle_date");
		
		TreeMap<String, String> paramMap = new TreeMap<String,String>();
		paramMap.put("oid_partner", oid_partner);
		paramMap.put("dt_order", dt_order);
		paramMap.put("no_order", no_order);
		paramMap.put("oid_paybill", oid_paybill);
		paramMap.put("money_order", money_order);
		paramMap.put("result_pay", result_pay);
		paramMap.put("sign_type", sign_type);
		paramMap.put("pay_type", pay_type);
		paramMap.put("bank_code", bank_code);
		paramMap.put("settle_date", settle_date);
		
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
		Payment payment = paymentService.getByPaymentNo(no_order);
		NfsRchgRecord rechargeRecord = nfsRchgRecordService.get(payment.getOrgId());
		if(payment.getStatus().equals(Payment.Status.success)) {
			log.info("订单号：" + no_order + " 已经处理完毕!");
			return true ;
		}
		if(rechargeRecord.getStatus().equals(NfsRchgRecord.Status.success)) {
			log.info("订单号：" + no_order + " 已经处理完毕!");
			return true ;
		}
		if(StringUtils.equals(result_pay, "SUCCESS")) {
			if (TraderRSAUtil.checksign(merKey, signStr, sign)) {
				log.info("连连支付异步回调验签成功");
					//payment状态改为success
					payment.setThirdPaymentNo(oid_paybill);
					payment.setStatus(Payment.Status.success);
					int updateLines = paymentDao.update(payment);
					if(updateLines == 0) {
						log.error("连连异步回调充值单号：{}状态更新失败！",no_order);
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
						log.error("连连异步回调账户更新失败！充值商户订单号{},连连订单号{}充值金额{}",no_order,oid_paybill,czAmount);
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
					log.info("充值支付单号：{}=================连连异步处理完毕==================",no_order);
				}else {
					log.error("连连充值异步回调验签失败！充值商户订单号{},连连订单号{}签名[{}]",no_order,oid_paybill,sign);
					update4Failure(payment);
				}
		}else {
			log.info("连连充值异步通知支付单号： paymentNo:{}支付失败",no_order);
			update4Failure(payment);
		}
		return true;
	}
	
	private void update4Failure(Payment payment) {
		payment.setStatus(Payment.Status.failure);
		paymentService.save(payment);
		NfsRchgRecord record = nfsRchgRecordService.get(payment.getOrgId());
		record.setStatus(NfsRchgRecord.Status.failure);
		record.setPayment(payment);
		nfsRchgRecordService.save(record);
	}
	
	private String convertStreamToString(InputStream is) {   
		   BufferedReader reader = new BufferedReader(new InputStreamReader(is));   
		        StringBuilder sb = new StringBuilder();   
		        String line = null;   
		        try {   
		            while ((line = reader.readLine()) != null) {   
		                sb.append(line);   
		            }   
		        } catch (IOException e) {   
		            e.printStackTrace();   
		        } finally {   
		            try {   
		                is.close();   
		            } catch (IOException e) {   
		                e.printStackTrace();   
		            }   
		        }
		        return sb.toString();   
		    }

	@Override
	public Map<String, String> queryRchgRecordByOrderNo(String orderNo) {
		Map<String, String> mapFromJson = new HashMap<String, String>();
		
		Payment payment = paymentService.getByPaymentNo(orderNo);
		NfsRchgRecord rechargeRecord = nfsRchgRecordService.get(payment.getOrgId());
		PluginConfig config = lianlianPaymentPlugin.getPluginConfig();
		Map<String,String> configAttr = config.getAttributeMap();
		String merKey = configAttr.get(LianlianPaymentPlugin.PAYMENT_LLPUBLIC_KEY_ATTRIBUTE_NAME);
		String oid_partner = configAttr.get(LianlianPaymentPlugin.PAYMENT_OID_PARTNER_ATTRIBUTE_NAME);
		String key = configAttr.get(LianlianPaymentPlugin.PAYMENT_BUSINESS_PRIVATE_KEY_ATTRIBUTE_NAME);
		String no_order = orderNo.toString();
		String dt_order = DateUtils.getAllNumTime(rechargeRecord.getCreateTime());
		StringBuilder sb = new StringBuilder();
		TreeMap<String, String> paramMap = new TreeMap<String,String>();
		paramMap.put("oid_partner", oid_partner);
		paramMap.put("sign_type", "RSA");
		paramMap.put("no_order", no_order);
		paramMap.put("dt_order", dt_order);
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
			String response = HttpUtils.doPostForWithdraw(LianlianMerchantDataBean.QUERY_PAY_RESULT_REQUEST_URL,JSONObject.toJSONString(paramMap).toString());
			JSONObject respJson = JSONObject.parseObject(response);
			String ret_code = respJson.getString("ret_code");
			if(StringUtils.equals(ret_code, "0000")) {
				TreeMap<String, String> respParmaMap = new TreeMap<String,String>();
				Map<String, Object> map = respJson;
				for (Map.Entry<String, Object> entry : map.entrySet()) {
					if(!StringUtils.equals(entry.getKey(), "sign") && !StringUtils.equals(entry.getKey(), "card_no")) {
						respParmaMap.put(entry.getKey(), String.valueOf(entry.getValue()));
					}
				}
				StringBuilder respSb = new StringBuilder();
				int count1 = 0;
				for (Map.Entry<String, String> entry : respParmaMap.entrySet()) {
					count1++;
					respSb.append(entry.getKey() + "=" + entry.getValue());
					if(count1 != respParmaMap.size()) {
						respSb.append("&");
					}
				}
				String respSignStr = respSb.toString();
				String respSign = respJson.getString("sign");
				if(TraderRSAUtil.checksign(merKey, respSignStr, respSign)) {
					String oid_paybill = respJson.getString("oid_paybill");
					String result_pay = respJson.getString("result_pay");
					mapFromJson.put("oid_paybill", oid_paybill);
					mapFromJson.put("resultPay", getResultPay(result_pay));
				}else {
					log.error("订单号[{}]富友查询接口返回码为：{},签名验证不通过",orderNo,ret_code);
				}
			}else {
				log.error("订单号[{}]连连查询接口返回码为：{}",orderNo,ret_code);
				mapFromJson.put("resultPay", "没有记录");
			}
		} catch (Exception e) {
			log.error("连连充值查询订单异常：{}",Exceptions.getStackTraceAsString(e));
			mapFromJson.put("resultPay", "查询异常，请联系技术处理");
		}
		return mapFromJson;
	} 
	
	private String getResultPay(String resultPay) {
		switch (resultPay) {
		case "SUCCESS":
			return "成功";
		case "WAITING":
			return "等待支付";
		case "PROCESSING":
			return "银行支付处理中";
		case "REFUND":
			return "退款";
		case "FAILURE":
			return "失败";
		default:
			return "失败";
		}
	}
}
