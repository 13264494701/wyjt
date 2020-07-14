package com.jxf.pwithdraw.service.impl;

import java.math.BigDecimal;
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
import com.jxf.mem.service.MemberActTrxService;
import com.jxf.mem.service.MemberMessageService;
import com.jxf.mem.service.MemberService;
import com.jxf.nfs.constant.TrxRuleConstant;
import com.jxf.nfs.entity.NfsWdrlRecord;
import com.jxf.nfs.entity.NfsWdrlRecord.Status;
import com.jxf.nfs.entity.NfsWdrlRecord.Type;
import com.jxf.nfs.service.NfsWdrlRecordService;
import com.jxf.pay.utils.HttpPoster;
import com.jxf.pwithdraw.entity.fuiou.FuiouMerchantDataBean;
import com.jxf.pwithdraw.entity.fuiou.FuiouPaymentRequestBean;
import com.jxf.pwithdraw.entity.fuiou.FuiouQueryPaymentRequestBean;
import com.jxf.pwithdraw.entity.fuiou.FuiouRequestBean;
import com.jxf.pwithdraw.entity.fuiou.FuiouRequestBeanFactory;
import com.jxf.pwithdraw.entity.fuiou.FuiouWithdrawStateEnum;
import com.jxf.pwithdraw.service.FuiouWithdrawService;
import com.jxf.pwithdraw.utils.SignUtil;
import com.jxf.svc.utils.Collections3;
import com.jxf.svc.utils.DateUtils;
import com.jxf.svc.utils.Exceptions;
import com.jxf.svc.utils.StringUtils;
import com.jxf.svc.utils.XMLParser;

/**
 * @description 富友代付
 * @author SuHuimin
 */
@Service("fuiouWithdrawService")
@Transactional(readOnly=true)
public class FuiouWithdrawServiceImpl implements FuiouWithdrawService {

	private static final Logger log = LoggerFactory.getLogger(FuiouWithdrawServiceImpl.class);
	
	@Autowired
	private NfsWdrlRecordService wdrlRecordService;
	@Autowired
	private MemberService memberService;
	@Autowired
	private MemberMessageService memberMessageService;
	@Autowired
	private MemberActTrxService memberActTrxService;
	
	

	@Override
	public String sendData(FuiouPaymentRequestBean fuiouPaymentRequestBean) {
		FuiouRequestBean fuiouRequestBean = FuiouRequestBeanFactory.getInstance().getRequestBean(fuiouPaymentRequestBean);
		Map<String, String> map = new HashMap<String, String>();
		String xml = fuiouRequestBean.getXml();
		String merid = fuiouRequestBean.getMerid();
		String mac = fuiouRequestBean.getMac();
		log.info("富友提现订单【{}】提交请求报文：{}",fuiouPaymentRequestBean.getOrderno(),xml);
		String reqtype = fuiouRequestBean.getReqtype();
		map.put("merid", merid);
		map.put("reqtype", reqtype);
		map.put("xml", xml);
		map.put("mac", mac);
		try {
			String result = new HttpPoster(FuiouMerchantDataBean.PAY_REQUEST_URL).postStr(map);
			log.info("富友提现订单【{}】请求同步返回报文：{}",fuiouPaymentRequestBean.getOrderno(),result);
			return result;
		} catch (Exception e) {
			log.error("富友提现订单【{}】提交请求时出现异常：{}",fuiouPaymentRequestBean.getOrderno(),Exceptions.getStackTraceAsString(e));
		}
		return null;
	}

	@Override
	@Transactional(readOnly=false)
	public void sendFuiouPaymentRequestForAPP(NfsWdrlRecord wdrlRecord) {
		wdrlRecord.setType(Type.fuiou);
		//用户合法性检查
		Member member = memberService.get(wdrlRecord.getMember());
		if(!member.getIsEnabled()||member.getIsLocked()) {
			wdrlRecord.setStatus(NfsWdrlRecord.Status.retrial);
			wdrlRecordService.save(wdrlRecord);
			log.error("用户被锁定或者冻结，不支持提现{}",wdrlRecord.getId());
			return;
		}
		if(memberService.lockExists(member)){	
			wdrlRecord.setStatus(NfsWdrlRecord.Status.retrial);
		    wdrlRecordService.save(wdrlRecord);
			log.error("用户账户资金来源可疑，请重新审核{}",wdrlRecord.getId());
			return;
		}
        try {
        	FuiouPaymentRequestBean fuiouPaymentRequestBean = new FuiouPaymentRequestBean();
        	fuiouPaymentRequestBean.setVer(FuiouMerchantDataBean.PAY_REQUEST_VER);
        	fuiouPaymentRequestBean.setMerdt(DateUtils.getDate("yyyyMMdd"));
        	fuiouPaymentRequestBean.setOrderno(String.valueOf(wdrlRecord.getId()));
        	fuiouPaymentRequestBean.setAccntno(wdrlRecord.getCardNo());
        	fuiouPaymentRequestBean.setAccntnm(wdrlRecord.getMemberName());
        	fuiouPaymentRequestBean.setAddDesc("1");
        	BigDecimal amount = wdrlRecord.getAmount();
        	BigDecimal fee = new BigDecimal(wdrlRecord.getFee());
        	String payAmount = String.valueOf(StringUtils.StrTOInt(StringUtils.decimalToStr(amount.subtract(fee), 2)));
        	fuiouPaymentRequestBean.setAmt(payAmount);
            String response = sendData(fuiouPaymentRequestBean);
            wdrlRecord.setSubmitTime(new Date());
            wdrlRecord.setStatus(Status.submited);
            wdrlRecordService.save(wdrlRecord);
            if (StringUtils.isBlank(response)) {
            	log.error("富友代付交易接口返回结果为空,将调用查询接口查询");
            	queryFuiouPayment(wdrlRecord);
            }else {
                Map<String, String> resultMap = XMLParser.readStringXmlOut(response);
                String ret = resultMap.get("ret");
                String memo = resultMap.get("memo");
                String transStatusDesc = resultMap.get("transStatusDesc");
                if (StringUtils.equals(ret, FuiouWithdrawStateEnum.FUIOUACCEPT.getCode()) 
                		|| StringUtils.equals(ret, FuiouWithdrawStateEnum.FUIOUTIMEOUT.getCode())) {
                    //富友已受理 或者 富友系统处理超时，先把订单置为已提交状态，如果状态一直不改变，可人工查询确认状态
             
                }else {
                	//富友返回失败响应码
                	log.error("富友提现订单{}富友受理失败,返回码为{},响应描述{},响应状态类别{}" , wdrlRecord.getId() ,ret,memo,transStatusDesc);
                	if(StringUtils.equals(ret, "200029")) {
                		//返回信息是 当天交易重复的 先不处理，等人工查询确认后再做处理
                		return;
                	}
                	NfsWdrlRecord wdrlRecord2 = wdrlRecordService.get(wdrlRecord.getId());
                	wdrlRecord2.setStatus(NfsWdrlRecord.Status.failure);
                	wdrlRecord2.setRmk(memo);
                	wdrlRecord2.setPayTime(new Date());
                	wdrlRecord2.setPayAmount(amount.subtract(fee));
                	wdrlRecordService.failureForFuiou(wdrlRecord2);
                }
            }
        } catch (Exception e) {
            log.error("富友提现订单{}请求异常{}",wdrlRecord.getId(),Exceptions.getStackTraceAsString(e));
        }
	}

	@Override
	@Transactional(readOnly=false)
	public boolean successNotifyProcess(HttpServletRequest request) {
		log.info("===========富友提现异步成功回调处理开始==========");
		String orderno = request.getParameter("orderno");// 商户请求流水
		String merdt = request.getParameter("merdt");// 原请求日期 yyyyMMdd
		String fuorderno = request.getParameter("fuorderno");// 富友交易流水
		String accntno = request.getParameter("accntno");// 银行卡号
		String amt = request.getParameter("amt");// 金额 单位：分
		String state = request.getParameter("state");// 交易状态码
		String result = request.getParameter("result");// 交易结果
		String mac = request.getParameter("mac");// 校验值
		log.info("富友提现订单回调数据：order:{},merdt:{},fuorderno:{},accntno:{},amt:{},state:{},result:{},mac:{}", orderno,
				merdt, fuorderno, accntno, amt, state, result, mac);
		NfsWdrlRecord wdrlRecord = wdrlRecordService.get(Long.valueOf(orderno));
		if (wdrlRecord == null) {
			log.error("富友订单{}回调异常，没有找到对应的提现订单！数据可能被篡改，接口被攻击！", orderno);
			return false;
		}
		String orgStr = FuiouMerchantDataBean.MERID + "|" + FuiouMerchantDataBean.KEY + "|" + orderno + "|" + merdt
				+ "|" + accntno + "|" + amt;
		boolean checkResult = SignUtil.checkSignForMD5(orgStr, mac);
		if (!checkResult) {
			log.error("富友提现订单{}回调签名验证错误", orderno);
			return false;
		}
		if (wdrlRecord.getStatus().equals(NfsWdrlRecord.Status.madeMoney)
				|| wdrlRecord.getStatus().equals(NfsWdrlRecord.Status.failure)) {
			log.info("提现订单：{}已处理，现状态为：{}，回调通知状态为订单成功！", wdrlRecord.getId(), wdrlRecord.getStatus());
			return true;
		}
		try {
			if (StringUtils.equals(state, FuiouWithdrawStateEnum.SENDED_CHANACCEPT.getCode())) {
				wdrlRecord.setThirdOrderNo(fuorderno);
				wdrlRecord.setPayTime(new Date());
				BigDecimal payAmount = new BigDecimal(
						StringUtils.decimalToStr(new BigDecimal(amt).divide(new BigDecimal(100)), 2));
				wdrlRecord.setPayAmount(payAmount);
				wdrlRecord.setStatus(NfsWdrlRecord.Status.madeMoney);
				wdrlRecord.setRmk(result);
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
			}
		} catch (Exception e) {
			log.error("提现订单 {}成功回调处理异常{}", wdrlRecord.getId(), Exceptions.getStackTraceAsString(e));
		}
		return true;
	}
	
	@Override
	@Transactional(readOnly=false)
	public boolean refundNotifyProcess(HttpServletRequest request) {
		log.info("===========富友提现退款回调处理开始==========");
		String orderno = request.getParameter("orderno");// 商户请求流水
		String merdt = request.getParameter("merdt");// 原请求日期 yyyyMMdd
		String fuorderno = request.getParameter("fuorderno");// 富友交易流水
		String tpmerdt = request.getParameter("tpmerdt");// 退票日期
		String accntno = request.getParameter("accntno");// 银行卡号
		String amt = request.getParameter("amt");// 金额 单位：分
		String state = request.getParameter("state");// 交易状态码
		String result = request.getParameter("result");// 交易结果
		String reason = request.getParameter("reason");// 结果原因
		String mac = request.getParameter("mac");// 校验值
		log.info(
				"富友提现订单退款回调数据：orderno:{},merdt:{},fuorderno:{},tpmerdt:{},accntno:{},amt:{},state:{},result:{},reason:{},mac:{}",
				orderno, merdt, fuorderno, tpmerdt, accntno, amt, state, result, reason, mac);
		NfsWdrlRecord wdrlRecord = wdrlRecordService.get(Long.valueOf(orderno));
		if (wdrlRecord == null) {
			log.error("富友订单{}回调异常，没有找到对应的提现订单！数据可能被篡改，接口被攻击！", orderno);
			return false;
		}
		String orgStr = FuiouMerchantDataBean.MERID + "|" + FuiouMerchantDataBean.KEY + "|" + orderno + "|" + merdt
				+ "|" + accntno + "|" + amt;
		boolean checkResult = SignUtil.checkSignForMD5(orgStr, mac);
		if (!checkResult) {
			log.error("富友提现订单{}退款回调签名验证错误", orderno);
			return false;
		}
		if (wdrlRecord.getStatus().equals(NfsWdrlRecord.Status.madeMoney)
				|| wdrlRecord.getStatus().equals(NfsWdrlRecord.Status.failure)) {
			log.info("提现订单：{}已处理，现状态为：{}，回调通知状态为订单成功！", wdrlRecord.getId(), wdrlRecord.getStatus());
			return true;
		}
		try {
			if (StringUtils.equals(state, FuiouWithdrawStateEnum.SENDED_CHANACCEPT.getCode())) {
				wdrlRecord.setThirdOrderNo(fuorderno);
				wdrlRecord.setPayTime(new Date());
				BigDecimal payAmount = new BigDecimal(
						StringUtils.decimalToStr(new BigDecimal(amt).divide(new BigDecimal(100)), 2));
				wdrlRecord.setPayAmount(payAmount);
				wdrlRecord.setStatus(NfsWdrlRecord.Status.failure);
				wdrlRecord.setRmk(reason);
				wdrlRecordService.failureForFuiou(wdrlRecord);
				log.debug("=======提现订单{}退款成功========", wdrlRecord.getId());
			}
		} catch (Exception e) {
			log.error("提现订单 {}  退款回调处理异常{}", wdrlRecord.getId(), Exceptions.getStackTraceAsString(e));
		}

		return true;
	}
	
	@Override
	@Transactional(readOnly=false)
	public String queryOrder(FuiouQueryPaymentRequestBean fuiouQueryPaymentRequestBean) {
		String orderno = fuiouQueryPaymentRequestBean.getOrderno();
	    try {
	    	FuiouRequestBean fuiouRequestBean = FuiouRequestBeanFactory.getInstance().getRequestBean(fuiouQueryPaymentRequestBean);
	    	String xml = fuiouRequestBean.getXml();
	    	String mac = fuiouRequestBean.getMac();
	        Map<String, String> queryMap = new HashMap<String, String>();
	        queryMap.put("merid", FuiouMerchantDataBean.MERID);
	        queryMap.put("xml", xml);
	        queryMap.put("mac", mac);
	        String result = new HttpPoster(FuiouMerchantDataBean.QUERY_REQUEST_URL).postStr(queryMap);
	        log.info("富友提现订单{}查询返回结果：{}",orderno,result);
	        return result;
	    } catch (Exception e) {
	    	log.error("富友提现订单{}查询请求异常：{}",orderno,Exceptions.getStackTraceAsString(e));
	    }
		return null;
	}

	@Override
	@Transactional(readOnly=false)
	public Map<String, String> queryFuiouPayment(NfsWdrlRecord nfsWdrlRecord) {
		// 后台系统查询订单显示查询结果返回数据
		Map<String, String> dataMap = new HashMap<String,String>();
		String startdt = DateUtils.getDateStr(nfsWdrlRecord.getSubmitTime(), "yyyyMMdd");
		String enddt = DateUtils.getDateStr(nfsWdrlRecord.getSubmitTime(), "yyyyMMdd");
		FuiouQueryPaymentRequestBean queryPaymentRequestBean = new FuiouQueryPaymentRequestBean();
		queryPaymentRequestBean.setBusicd(FuiouMerchantDataBean.BUSINESS_CODE);
		queryPaymentRequestBean.setOrderno(String.valueOf(nfsWdrlRecord.getId()));
		queryPaymentRequestBean.setVer(FuiouMerchantDataBean.QUERY_REQUEST_VER);
		queryPaymentRequestBean.setStartdt(startdt);
		queryPaymentRequestBean.setEnddt(enddt);
		try {
			String response = queryOrder(queryPaymentRequestBean);
			Map<String, Object> resultMap = XMLParser.xmlStrConvertToMap(response);
			String respCode = (String) resultMap.get("ret");
			@SuppressWarnings("unchecked")
			Map<String, Object> childMap = (Map<String, Object>) resultMap.get("trans");
			if(Collections3.isEmptyMap(childMap)) {
				log.error("富友查询接口返回码为：{}",respCode);
				dataMap.put("message", "富友查询订单不存在，返回码为"+respCode);
				return dataMap;
			}
			String orderno = (String) childMap.get("orderno"); // 非空
			String state = (String) childMap.get("state");//交易状态
			String amt = (String) childMap.get("amt"); // 单位：分
			String tpst = (String) childMap.get("tpst");// 是否退票 1是，0否
			String transStatusDesc = (String) childMap.get("transStatusDesc");// 交易状态类别
			String reason = (String) childMap.get("reason");// 结果原因
			String bankRespCode = (String) childMap.get("rspcd");//银行响应码
			NfsWdrlRecord wdrlRecord = wdrlRecordService.get(Long.valueOf(orderno));
			if (wdrlRecord == null) {
				log.error("富友查询接口返回订单号{}错误,查无此订单", orderno);
				dataMap.put("message", "富友查询接口返回订单号错误，请联系富友人员确认订单！");
				return dataMap;
			}
			Status status = wdrlRecord.getStatus();
			Long orderId = wdrlRecord.getId();
			if (StringUtils.equals(state, FuiouWithdrawStateEnum.UNSEND.code) || StringUtils.equals(state, FuiouWithdrawStateEnum.SENDING.code)) {
				log.error("富友提现订单{}交易处理中，请稍后查询", orderId);
				dataMap.put("message", "交易处理中，请稍后重新查询！");
				return dataMap;
			}else {
				if(StringUtils.equals(tpst, "1")) {
					//交易失败 退款
					log.error("富友提现订单{}交易交易失败，富友查询接口返回退款状态", orderId);
					if(status.equals(NfsWdrlRecord.Status.failure)) {
						dataMap.put("message", "交易已处理");
						return dataMap;
					}
					wdrlRecord.setStatus(NfsWdrlRecord.Status.failure);
					wdrlRecord.setPayTime(wdrlRecord.getSubmitTime());
					wdrlRecord.setRmk(reason);
					wdrlRecordService.failureForFuiou(wdrlRecord);
					dataMap.put("message", "交易失败，退款成功");
					return dataMap;
				}
				if(StringUtils.equals(bankRespCode, "000000") && StringUtils.equals(transStatusDesc, FuiouWithdrawStateEnum.SUCCESS.code)) {
					//富友受理成功
					if(status.equals(NfsWdrlRecord.Status.madeMoney) || status.equals(Status.failure)) {
						dataMap.put("message", "交易已处理");
						return dataMap;
					}
					log.info("富友提现订单{}交易成功", orderId);
					wdrlRecord.setStatus(NfsWdrlRecord.Status.madeMoney);
					BigDecimal payAmount = new BigDecimal(amt).divide(new BigDecimal(100));
					wdrlRecord.setPayAmount(payAmount);
					// 取提交时间作为打款时间，在零点的时候可能会有日期上的误差
					wdrlRecord.setPayTime(wdrlRecord.getSubmitTime());
					// 通过查询获取不到富友订单号，需登录代收付平台查询
					wdrlRecordService.save(wdrlRecord);
					dataMap.put("message", "交易成功！");
					return dataMap;				
				}else if((StringUtils.equals(bankRespCode, "000000") && StringUtils.equals(transStatusDesc, FuiouWithdrawStateEnum.CHANACCEPTSUCCESS.code))
					    || StringUtils.equals(bankRespCode, "200001") || StringUtils.equals(bankRespCode, "200002")
						|| StringUtils.equals(bankRespCode, FuiouWithdrawStateEnum.FUIOUTIMEOUT.code)
						|| StringUtils.equals(bankRespCode, "AAAAAA") || StringUtils.isBlank(bankRespCode)) {
					log.error("富友订单:{}查询接口显示交易处理中，请稍候查询交易结果", orderId);
					dataMap.put("message", "交易处理中，请稍后查询");
					return dataMap;
				}else {
					//交易失败
					log.error("提现订单：{}查询结果显示为rspcd：{}，交易失败！",orderId,bankRespCode);
					if(status.equals(Status.failure)) {
						dataMap.put("message", "交易已处理");
						return dataMap;
					}
					wdrlRecord.setStatus(NfsWdrlRecord.Status.failure);
					wdrlRecord.setPayTime(wdrlRecord.getSubmitTime());
					wdrlRecord.setRmk(reason);
					wdrlRecordService.failureForFuiou(wdrlRecord);
					dataMap.put("message", "交易失败，退款成功");
					return dataMap;
				}
			}
		} catch (Exception e) {
			log.error("富友提现订单{}查询订单结果处理异常{}", nfsWdrlRecord.getId(), Exceptions.getStackTraceAsString(e));
			dataMap.put("success", "false");
			dataMap.put("message", "查询请求异常，请联系技术人员处理");
		}
		return dataMap;
	}

	@Override
	public Map<String, String> queryAccount() {
		FuiouRequestBean fuiouRequestBean = FuiouRequestBeanFactory.getInstance().getRequestBean(null);
		String xml = fuiouRequestBean.getXml();
		Map<String, String> queryMap = new HashMap<String, String>();
		queryMap.put("xml", xml);
		String response = new HttpPoster(FuiouMerchantDataBean.ACCOUNT_QUERY_URL).postStr(queryMap);
		log.info("富友代付账户查询返回结果：{}",response);
		Map<String, String> resultMap = XMLParser.readStringXmlOut(response);
		String ret = resultMap.get("ret");//响应码
		if(StringUtils.equals(ret, "000000")) {
			String memo = resultMap.get("memo");//响应描述
			String ctamt = resultMap.get("ctamt");//账面余额
			String caamt = resultMap.get("caamt");//可用余额
			String cuamt = resultMap.get("cuamt");//未转结余额
			String cfamt = resultMap.get("cfamt");//冻结余额
			String mac = resultMap.get("mac");//校验值
			String orgStr = FuiouMerchantDataBean.MERID + "|" + ret + "|" +memo + "|" + ctamt + "|" + caamt + "|" + cuamt + "|" + cfamt + "|" +FuiouMerchantDataBean.KEY;
			if(SignUtil.checkSignForMD5(orgStr, mac)) {
				return resultMap;
			}
			log.error("富友代付账户查询结果返回签名验证错误！");
			return null;
		}
		log.error("富友代付账户查询失败！");
		return null;
	}
}
