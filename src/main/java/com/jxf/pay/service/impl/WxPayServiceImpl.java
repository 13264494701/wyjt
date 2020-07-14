/**    
 * @文件名称: WXPayServiceImpl.java 
 * @类路径: com.jxf.pay.service.impl 
 * @描述: TODO 
 * @作者：李新 
 * @时间：2016年8月10日 上午10:18:59 
 * @版本：V1.0    
 */ 
package com.jxf.pay.service.impl;


import java.net.ConnectException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jxf.pay.entity.UnifiedOrder;
import com.jxf.pay.service.WxPayService;
import com.jxf.payment.entity.Payment;
import com.jxf.payment.entity.Payment.Status;
import com.jxf.payment.service.PaymentService;
import com.jxf.svc.config.Global;
import com.jxf.svc.plugin.PluginConfig;
import com.jxf.svc.plugin.weixinPayment.WxPaymentPlugin;
import com.jxf.svc.utils.DateUtils;
import com.jxf.svc.utils.HttpsRequest;
import com.jxf.svc.utils.MD5Code;
import com.jxf.svc.utils.StringUtils;
import com.jxf.svc.utils.XMLParser;
import com.jxf.wx.account.entity.WxAccount;
/**
 * 支付相关API
 *
 * @author lixin
 * @since 1.2
 */
@Service("wxPayService")
public class WxPayServiceImpl implements WxPayService {

	
	@Autowired
	private PaymentService paymentService;
	@Autowired
	private WxPaymentPlugin wxPaymentPlugin;
	
	protected Logger log = LoggerFactory.getLogger(getClass());
	
	/**
     * 统一下单
     *
     * @param payment 支付对象
     * @return 调用结果
     */
	@Override
	public Map<String, String> payUnifiedOrder(Payment payment,WxAccount wxAccount) {
		log.debug("微信统一支付接口.....");
        Map<String, String>	paySignMap=new HashMap<>();
        try {
        	String unifiedOrderRsp = new HttpsRequest().sendPost(payment.getRequestUrl(),UnifiedOrderReqXmlObj(payment,wxAccount),payment.getMchId(),Global.getConfig("cert.key"));
        	Map<String, String> unifiedOrderRspMap = XMLParser.readStringXmlOut(unifiedOrderRsp);
        	log.debug("微信统一支付接口返回：{}",unifiedOrderRspMap.toString());
        	String return_code = unifiedOrderRspMap.get("return_code");
        	if(StringUtils.isNotBlank(return_code) && return_code.equals("SUCCESS")){
        		String result_code = unifiedOrderRspMap.get("result_code");
        		if(StringUtils.isNotBlank(result_code) && StringUtils.equals(result_code, "SUCCESS")) {
        			log.debug("微信统一支付提交成功、生成JS签名数据");
        			paySignMap = unifiedOrderToJsPaySignInfo(unifiedOrderRspMap,wxAccount,payment.getKey());
        			payment.setPayId(unifiedOrderRspMap.get("prepay_id"));
        		}else {
        			String err_code = unifiedOrderRspMap.get("err_code");
        			String err_code_des = unifiedOrderRspMap.get("err_code_des");
        			log.error("paymentId:{},微信支付单预支付请求：result_code:{},error_code:{},err_code_des:{}",payment.getId(),result_code,err_code,err_code_des);
        			paySignMap.put("result_code", result_code);
        			paySignMap.put("err_code", err_code);
        			paySignMap.put("err_code_des", err_code_des);
        		}
        	}else{
        		log.error("微信统一支付提交出错{}！",return_code);
        	}
        } catch (ConnectException ce) {
        	log.error("连接超时："+ce.getMessage());
        } catch (Exception e) {
        	log.error("https请求异常："+e.getMessage());
        }
        return paySignMap;
	}
    
    //统一下单参数整理
    private  UnifiedOrder UnifiedOrderReqXmlObj(Payment payment,WxAccount wxAccount) {
    	
    	UnifiedOrder unifiedOrder =new UnifiedOrder();
		String appId = wxAccount.getAppid();
		String appName = wxAccount.getName();
		
		log.info("/////////////////////-APPID-////////////////:" + appId);
		log.info("/////////////////////-openid-////////////////:" + payment.getOpenID());
		
		unifiedOrder.setAppid(appId);
    	unifiedOrder.setMch_id(payment.getMchId());
    	unifiedOrder.setNonce_str(StringUtils.getRandomStringByLength(32));
    	unifiedOrder.setBody(appName+"-微信支付");
    	unifiedOrder.setOut_trade_no(payment.getPaymentNo());
    	unifiedOrder.setTotal_fee(StringUtils.StrTOInt(payment.getPaymentAmount().toString()));
    	unifiedOrder.setSpbill_create_ip(payment.getRemoteAddr());
    	unifiedOrder.setNotify_url(payment.getNotifyUrl());
    	unifiedOrder.setTrade_type("JSAPI");
    	unifiedOrder.setOpenid(payment.getOpenID());
    	//签名
    	String signTemp = "appid="+unifiedOrder.getAppid()
    			+"&body="+unifiedOrder.getBody()
    			+"&mch_id="+unifiedOrder.getMch_id()
    			+"&nonce_str="+unifiedOrder.getNonce_str()
    			+"&notify_url="+unifiedOrder.getNotify_url()
    			+"&openid="+unifiedOrder.getOpenid()
    			+"&out_trade_no="+unifiedOrder.getOut_trade_no()
    			+"&spbill_create_ip="+unifiedOrder.getSpbill_create_ip()
    			+"&total_fee="+unifiedOrder.getTotal_fee()
    			+"&trade_type="+unifiedOrder.getTrade_type()
    			+"&key="+payment.getKey(); //商户号对应的秘钥
    	log.debug("签名字符串={}",signTemp);
    	String characterEncoding = "UTF-8"; 
    	String sign=MD5Code.MD5Encode(signTemp, characterEncoding).toUpperCase();
		log.debug("签名后的值"+sign);
    	unifiedOrder.setSign(sign);
        return unifiedOrder;
       }
    //统一支付结果转JS支付签名信息
     private Map<String, String> unifiedOrderToJsPaySignInfo(Map<String, String> unifiedOrderRspMap,WxAccount wxAccount,String key) throws ParseException {
    	 Map<String, String> paySignMap = new HashMap<>();
 		 
 		 String appId = wxAccount.getAppid();

     	 paySignMap.put("appId", appId);
    	 paySignMap.put("timeStamp", DateUtils.calLastedTime()+"");
    	 paySignMap.put("nonceStr", StringUtils.getRandomStringByLength(32));		
    	 paySignMap.put("package", "prepay_id="+unifiedOrderRspMap.get("prepay_id"));
    	 paySignMap.put("signType", "MD5");
    	//签名
    	String signTemp = "appId="+paySignMap.get("appId")
    			+"&nonceStr="+paySignMap.get("nonceStr")
    			+"&package="+paySignMap.get("package")
    			+"&signType=MD5"
    			+"&timeStamp="+paySignMap.get("timeStamp")
    			+"&key="+key; //商户号对应的秘钥
    	log.debug("签名字符串={}",signTemp);
    	String paySign=MD5Code.MD5Encode(signTemp, "UTF-8").toUpperCase();
    	log.debug("签名后的值"+paySign);
    	paySignMap.put("paySign", paySign);
        return paySignMap;
    }
    
   //微信支付结果回调方法
  	@Override
  	public boolean wxNotifyProcess(HttpServletRequest request) {

  		log.info("============微信支付异步返回===========");
  		// 获取微信POST过来反馈信息  
          String inputLine;  
          String notityXml = "";  
          try {  
    
              while ((inputLine = request.getReader().readLine()) != null) {  
                  notityXml += inputLine;  
              }  
              
              request.getReader().close();  
              
          } catch (Exception e) {  
          	log.error("xml获取失败：" + e);  
          }  
          log.debug("收到微信异步回调：{}",notityXml);  
          if(StringUtils.isEmpty(notityXml)){  
          	log.error("xml为空：");  
          }  
          Map<String, String> result = new HashMap<>();
          result = XMLParser.readStringXmlOut(notityXml);
  		String paymentNo = result.get("out_trade_no");
  		String thirdPaymentNo = result.get("transaction_id");
  		String respCode = result.get("return_code");
  		String respMsg = result.get("return_msg");
  		String resultCode = result.get("result_code");
  		log.info("响应支付单:[{}]",paymentNo);
  		log.info("respCode:[{}]",respCode);
  		log.info("respMsg:[{}]",respMsg);
  		Payment  payment= paymentService.getByPaymentNo(paymentNo);
  		if(Payment.Status.success.equals(payment.getStatus())){//去重处理
  			log.info("=========该支付单已经成功========");
  			return true;
  		}
  		
  		if(StringUtils.equals("SUCCESS", respCode)){
  				log.info("=========微信支付通讯成功=========");
  				if(checkIsSignValidFromResponse(wxPaymentPlugin,result)) {
  					log.info("=========签名验证成功=========");
  					if(StringUtils.equals("SUCCESS", resultCode)){
  						log.info("=========交易结果验证成功=========");
  						payment.setStatus(Payment.Status.success);
  						payment.setRespCode(respCode);
  						payment.setRespMsg(respMsg);
  						payment.setThirdPaymentNo(thirdPaymentNo);
  						paymentService.payFinishProcess(payment);
  						return true;
  					}else {
  						tradeProcessByFailureStatus(Payment.Status.failure,payment,respCode,respMsg);
  						log.error("=========微信支付交易失败=========");
  						return false;
  					}
  				}else {
  					tradeProcessByFailureStatus(Payment.Status.failure,payment,respCode,respMsg);
  					log.error("=========微信支付签名不合法=========");
  					return false;
  				}
  		}else{
  			tradeProcessByFailureStatus(Payment.Status.failure,payment,respCode,respMsg);
  			log.error("=========微信支付通讯失败=========");
  	        return false;
  		}
  	}
  	/**
  	 * 微信支付失败 统一处理
  	 * @param failure
  	 * @param payment
  	 * @param respCode
  	 * @param respMsg
  	 */
  	private void tradeProcessByFailureStatus(Status failure, Payment payment, String respCode,
  			String respMsg) {
  		payment.setStatus(Payment.Status.failure);
  		payment.setRespCode(respCode);
  		payment.setRespMsg(respMsg);
  		paymentService.payFinishProcess(payment);
  	}
  	/**
  	 * 验证签名 
  	 * @param wxPaymentPlugin 
  	 * @param payment 
  	 * @param result
  	 * @return	验证通过返回true
  	 */
  	private boolean checkIsSignValidFromResponse(WxPaymentPlugin wxPaymentPlugin, Map<String, String> result) {
  		
  		//获取商户秘钥
  		PluginConfig config = wxPaymentPlugin.getPluginConfig();
  		Map<String,String> configAttr = config.getAttributeMap();
  		String key = configAttr.get(WxPaymentPlugin.PAYMENT_KEY_ATTRIBUTE_NAME);
  		if (!(result instanceof SortedMap<?,?>)) {
  			result = new TreeMap<String, String>(result);
  	      }
  		//服务端按照签名方式生成签名与返回签名进行比对
  		StringBuffer signTemp = new StringBuffer();
  		Iterator<String> it = result.keySet().iterator();
  		while(it.hasNext()) {
  			String paramName = it.next();
  			String paramValue = result.get(paramName);
  			if(paramValue!=null&&!"".equals(paramValue)&&!"sign".equals(paramName)) {
  				signTemp.append(paramName+"="+paramValue+"&");
  			}
  		}
  		signTemp.append("key="+key);
  		log.debug("签名字符串={}",signTemp);
      	String characterEncoding = "UTF-8"; 
      	String sign=MD5Code.MD5Encode(signTemp.toString(), characterEncoding).toUpperCase();
  		log.debug("服务端生成的签名========"+sign);
  		String returnSign = result.get("sign");
  		log.debug("返回的签名========"+returnSign);
  		
  		return sign.equals(returnSign);
  	}
}
